package com.skwasimrazzak.bluetooth_hid_manager

object HidConstants {
    const val CHANNEL = "bluetooth_hid_manager"
    const val EVENT_CHANNEL = "bluetooth_hid_manager/connection_state"

    const val REPORT_ID_KEYBOARD: Byte = 0x01
    const val REPORT_ID_MOUSE: Byte = 0x02
    const val REPORT_ID_GAMEPAD: Byte = 0x03

    // Method names
    const val METHOD_INIT = "init"
    const val METHOD_CONNECT = "connect"
    const val METHOD_DISCONNECT = "disconnect"
    const val METHOD_SEND_KEYBOARD = "sendKeyboardReport"
    const val METHOD_SEND_MOUSE = "sendMouseReport"
    const val METHOD_SEND_GAMEPAD = "sendGamepadReport"
    const val METHOD_IS_CONNECTED = "isConnected"
}