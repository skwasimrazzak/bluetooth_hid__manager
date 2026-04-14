import 'package:flutter/services.dart';
import 'package:bluetooth_hid_manager/bluetooth_hid_manager_platform_interface.dart';
import 'hid_exception.dart';

class GamepadButton {
  static const int none = 0x0000;
  static const int b1 = 0x0001; // Cross / A
  static const int b2 = 0x0002; // Circle / B
  static const int b3 = 0x0004; // Square / X
  static const int b4 = 0x0008; // Triangle / Y
  static const int l1 = 0x0010;
  static const int r1 = 0x0020;
  static const int l2 = 0x0040;
  static const int r2 = 0x0080;
  static const int select = 0x0100;
  static const int start = 0x0200;
  static const int l3 = 0x0400;
  static const int r3 = 0x0800;
  static const int dpadUp = 0x1000;
  static const int dpadDown = 0x2000;
  static const int dpadLeft = 0x4000;
  static const int dpadRight = 0x8000;
}

class BluetoothHidGamepad {
  Future<void> sendReport({
    int buttons = GamepadButton.none,
    int leftX = 0,
    int leftY = 0,
    int rightX = 0,
    int rightY = 0,
  }) async {
    try {
      await BluetoothHidManagerPlatform.instance.sendGamepadReport(
        buttons,
        leftX.clamp(-127, 127),
        leftY.clamp(-127, 127),
        rightX.clamp(-127, 127),
        rightY.clamp(-127, 127),
      );
    } on PlatformException catch (e) {
      throw HidException(e.code, e.message ?? 'sendGamepadReport failed');
    }
  }

  Future<void> pressButtons(int buttonMask) => sendReport(buttons: buttonMask);
  Future<void> release() => sendReport();
  Future<void> setAxes({int lx = 0, int ly = 0, int rx = 0, int ry = 0}) =>
      sendReport(leftX: lx, leftY: ly, rightX: rx, rightY: ry);
}
