import Flutter
import UIKit

public class BluetoothHidManagerPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "bluetooth_hid_manager", binaryMessenger: registrar.messenger())
    let instance = BluetoothHidManagerPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result(FlutterError(
                code: "UNSUPPORTED_PLATFORM",
                message: "bluetooth_hid_manager is Android only. iOS does not support HID device emulation for third-party apps.",
                details: nil
    ))
  }
}
