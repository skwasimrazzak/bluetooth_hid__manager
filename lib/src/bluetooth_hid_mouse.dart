import 'package:flutter/services.dart';
import 'package:bluetooth_hid_manager/bluetooth_hid_manager_method_channel.dart';
import 'hid_exception.dart';

class MouseButton {
  static const int none = 0x00;
  static const int left = 0x01;
  static const int right = 0x02;
  static const int middle = 0x04;
}

class BluetoothHidMouse {
  Future<void> sendReport({
    int buttons = MouseButton.none,
    int x = 0,
    int y = 0,
    int wheel = 0,
  }) async {
    try {
      await MethodChannelBluetoothHidManager.hidMethodChannel
          .invokeMethod('sendMouseReport', {
            'buttons': buttons,
            'x': x.clamp(-127, 127),
            'y': y.clamp(-127, 127),
            'wheel': wheel.clamp(-127, 127),
          });
    } on PlatformException catch (e) {
      throw HidException(e.code, e.message ?? 'sendMouseReport failed');
    }
  }

  Future<void> move(int x, int y) => sendReport(x: x, y: y);
  Future<void> click(int button) => sendReport(buttons: button);
  Future<void> release() => sendReport();
  Future<void> scroll(int delta) => sendReport(wheel: delta);
}
