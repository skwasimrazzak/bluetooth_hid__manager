package com.skwasimrazzak.bluetooth_hid_manager

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import android.bluetooth.*
import android.content.Context
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.util.concurrent.Executors

class BluetoothHidManagerPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {

    private lateinit var methodChannel: MethodChannel
    private lateinit var eventChannel: EventChannel
    private lateinit var context: Context

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var hidDevice: BluetoothHidDevice? = null
    private var connectedDevice: BluetoothDevice? = null
    private var isAppRegistered = false

    private var eventSink: EventChannel.EventSink? = null

    override fun onAttachedToEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        context = binding.applicationContext
        methodChannel = MethodChannel(binding.binaryMessenger, HidConstants.CHANNEL)
        methodChannel.setMethodCallHandler(this)
        eventChannel = EventChannel(binding.binaryMessenger, HidConstants.EVENT_CHANNEL)
        eventChannel.setStreamHandler(connectionStreamHandler)
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        methodChannel.setMethodCallHandler(null)
        eventChannel.setStreamHandler(null)
        bluetoothAdapter?.closeProfileProxy(BluetoothProfile.HID_DEVICE, hidDevice)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {}
    override fun onDetachedFromActivityForConfigChanges() {}
    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {}
    override fun onDetachedFromActivity() {}

    private val connectionStreamHandler = object : EventChannel.StreamHandler {
        override fun onListen(arguments: Any?, sink: EventChannel.EventSink?) {
            eventSink = sink
        }
        override fun onCancel(arguments: Any?) {
            eventSink = null
        }
    }

    private fun emitConnectionState(state: String, deviceAddress: String? = null) {
        val event = mapOf("state" to state, "deviceAddress" to deviceAddress)
        // Must emit on main thread
        android.os.Handler(android.os.Looper.getMainLooper()).post {
            eventSink?.success(event)
        }
    }

    private val profileListener = object : BluetoothProfile.ServiceListener {
        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
            if (profile == BluetoothProfile.HID_DEVICE) {
                hidDevice = proxy as BluetoothHidDevice
                registerApp()
            }
        }
        override fun onServiceDisconnected(profile: Int) {
            hidDevice = null
            isAppRegistered = false
        }
    }

    private val hidCallback = object : BluetoothHidDevice.Callback() {
        override fun onAppStatusChanged(pluggedDevice: BluetoothDevice?, registered: Boolean) {
            isAppRegistered = registered
            if (!registered) emitConnectionState("unregistered")
        }

        override fun onConnectionStateChanged(device: BluetoothDevice, state: Int) {
            when (state) {
                BluetoothProfile.STATE_CONNECTED -> {
                    connectedDevice = device
                    emitConnectionState("connected", device.address)
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    connectedDevice = null
                    emitConnectionState("disconnected", device.address)
                }
                BluetoothProfile.STATE_CONNECTING ->
                    emitConnectionState("connecting", device.address)
                BluetoothProfile.STATE_DISCONNECTING ->
                    emitConnectionState("disconnecting", device.address)
            }
        }

        // Required overrides — leave empty for now
        override fun onGetReport(device: BluetoothDevice, type: Byte, id: Byte, bufferSize: Int) {}
        override fun onSetReport(device: BluetoothDevice, type: Byte, id: Byte, data: ByteArray) {}
        override fun onInterruptData(device: BluetoothDevice, reportId: Byte, data: ByteArray) {}
    }

    private fun registerApp() {
        val sdpRecord = BluetoothHidDevice.AppSdpSettings(
            "Flutter HID Device",
            "Flutter HID Package",
            "flutter_hid_manager",
            BluetoothHidDevice.SUBCLASS1_COMBO,
            HidDescriptors.COMBINED
        )
        hidDevice?.registerApp(
            sdpRecord,
            null,   // QoS incoming — null = default
            null,   // QoS outgoing — null = default
            Executors.newCachedThreadPool(),
            hidCallback
        )
    }

    private fun init(result: Result) {
        val btManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        bluetoothAdapter = btManager?.adapter

        if (bluetoothAdapter == null) {
            result.error("BLUETOOTH_UNAVAILABLE", "Device does not support Bluetooth", null)
            return
        }
        if (!bluetoothAdapter!!.isEnabled) {
            result.error("BLUETOOTH_DISABLED", "Bluetooth is not enabled", null)
            return
        }

        bluetoothAdapter!!.getProfileProxy(context, profileListener, BluetoothProfile.HID_DEVICE)
        result.success(null)
    }

    private fun connect(call: MethodCall, result: Result) {
        val address = call.argument<String>("address")
            ?: return result.error("INVALID_ARGS", "Device address required", null)

        if (hidDevice == null || !isAppRegistered) {
            return result.error("NOT_READY", "HID app not registered yet", null)
        }

        val device = try {
            bluetoothAdapter!!.getRemoteDevice(address)
        } catch (e: IllegalArgumentException) {
            return result.error("INVALID_ADDRESS", "Invalid Bluetooth address", null)
        }

        val success = hidDevice!!.connect(device)
        if (success) result.success(null)
        else result.error("CONNECT_FAILED", "Failed to initiate connection", null)
    }

    private fun disconnect(result: Result) {
        val device = connectedDevice
            ?: return result.error("NOT_CONNECTED", "No device connected", null)
        hidDevice?.disconnect(device)
        result.success(null)
    }

    private fun sendKeyboardReport(call: MethodCall, result: Result) {
        val device = connectedDevice
            ?: return result.error("NOT_CONNECTED", "No device connected", null)

        val modifier = (call.argument<Int>("modifier") ?: 0).toByte()
        val keys = call.argument<List<Int>>("keys") ?: emptyList()

        val report = ByteArray(8)
        report[0] = modifier
        report[1] = 0x00 // reserved
        for (i in 0 until minOf(keys.size, 6)) {
            report[2 + i] = keys[i].toByte()
        }

        val success = hidDevice!!.sendReport(device, HidConstants.REPORT_ID_KEYBOARD.toInt(), report)
        if (success) result.success(null)
        else result.error("SEND_FAILED", "Failed to send keyboard report", null)
    }

    private fun sendMouseReport(call: MethodCall, result: Result) {
        val device = connectedDevice
            ?: return result.error("NOT_CONNECTED", "No device connected", null)

        val buttons = (call.argument<Int>("buttons") ?: 0).toByte()
        val x = (call.argument<Int>("x") ?: 0).toByte()
        val y = (call.argument<Int>("y") ?: 0).toByte()
        val wheel = (call.argument<Int>("wheel") ?: 0).toByte()

        val report = byteArrayOf(buttons, x, y, wheel)
        val success = hidDevice!!.sendReport(device, HidConstants.REPORT_ID_MOUSE.toInt(), report)
        if (success) result.success(null)
        else result.error("SEND_FAILED", "Failed to send mouse report", null)
    }

    private fun sendGamepadReport(call: MethodCall, result: Result) {
        val device = connectedDevice
            ?: return result.error("NOT_CONNECTED", "No device connected", null)

        val buttons = call.argument<Int>("buttons") ?: 0
        val lx = (call.argument<Int>("lx") ?: 0).toByte()
        val ly = (call.argument<Int>("ly") ?: 0).toByte()
        val rx = (call.argument<Int>("rx") ?: 0).toByte()
        val ry = (call.argument<Int>("ry") ?: 0).toByte()

        val report = byteArrayOf(
            (buttons and 0xFF).toByte(),         // Low byte of button bitmask
            ((buttons shr 8) and 0xFF).toByte(), // High byte of button bitmask
            lx, ly, rx, ry
        )

        val success = hidDevice!!.sendReport(device, HidConstants.REPORT_ID_GAMEPAD.toInt(), report)
        if (success) result.success(null)
        else result.error("SEND_FAILED", "Failed to send gamepad report", null)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            HidConstants.METHOD_INIT -> init(result)
            HidConstants.METHOD_CONNECT -> connect(call, result)
            HidConstants.METHOD_DISCONNECT -> disconnect(result)
            HidConstants.METHOD_SEND_KEYBOARD -> sendKeyboardReport(call, result)
            HidConstants.METHOD_SEND_MOUSE -> sendMouseReport(call, result)
            HidConstants.METHOD_SEND_GAMEPAD -> sendGamepadReport(call, result)
            HidConstants.METHOD_IS_CONNECTED -> result.success(connectedDevice != null)
            else -> result.notImplemented()
        }
    }


}
