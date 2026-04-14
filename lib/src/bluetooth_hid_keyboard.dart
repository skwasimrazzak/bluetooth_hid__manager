import 'package:flutter/services.dart';
import 'hid_exception.dart';
import 'package:bluetooth_hid_manager/bluetooth_hid_manager_platform_interface.dart';

class KeyboardModifier {
  static const int none = 0x00;
  static const int leftCtrl = 0x01;
  static const int leftShift = 0x02;
  static const int leftAlt = 0x04;
  static const int leftMeta = 0x08;
  static const int rightCtrl = 0x10;
  static const int rightShift = 0x20;
  static const int rightAlt = 0x40;
  static const int rightMeta = 0x80;
}

class KeyCode {
  static const int none = 0x00;
  static const int a = 0x04;
  static const int b = 0x05;
  static const int c = 0x06;
  static const int d = 0x07;
  static const int e = 0x08;
  static const int f = 0x09;
  static const int g = 0x0A;
  static const int h = 0x0B;
  static const int i = 0x0C;
  static const int j = 0x0D;
  static const int k = 0x0E;
  static const int l = 0x0F;
  static const int m = 0x10;
  static const int n = 0x11;
  static const int o = 0x12;
  static const int p = 0x13;
  static const int q = 0x14;
  static const int r = 0x15;
  static const int s = 0x16;
  static const int t = 0x17;
  static const int u = 0x18;
  static const int v = 0x19;
  static const int w = 0x1A;
  static const int x = 0x1B;
  static const int y = 0x1C;
  static const int z = 0x1D;
  static const int returnKey = 0x28;
  static const int escape = 0x29;
  static const int backspace = 0x2A;
  static const int tab = 0x2B;
  static const int space = 0x2C;
  static const int arrowRight = 0x4F;
  static const int arrowLeft = 0x50;
  static const int arrowDown = 0x51;
  static const int arrowUp = 0x52;
}

class BluetoothHidKeyboard {
  Future<void> sendKeys({
    int modifier = KeyboardModifier.none,
    List<int> keys = const [],
  }) async {
    if (keys.length > 6) throw ArgumentError('Maximum 6 simultaneous keys');
    try {
      await BluetoothHidManagerPlatform.instance.sendKeyboardReport(
        modifier,
        keys,
      );
    } on PlatformException catch (e) {
      throw HidException(e.code, e.message ?? 'sendKeys failed');
    }
  }

  Future<void> pressKey(int keyCode, {int modifier = KeyboardModifier.none}) =>
      sendKeys(modifier: modifier, keys: [keyCode]);

  Future<void> releaseAll() => sendKeys();
}
