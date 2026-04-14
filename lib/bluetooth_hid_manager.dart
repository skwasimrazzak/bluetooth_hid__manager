export 'src/bluetooth_hid_keyboard.dart';
export 'src/bluetooth_hid_mouse.dart';
export 'src/bluetooth_hid_gamepad.dart';
export 'src/hid_connection_state.dart';
export 'src/hid_exception.dart';

import 'bluetooth_hid_manager_platform_interface.dart';
import 'src/hid_connection_state.dart';
import 'src/hid_exception.dart';
import 'package:flutter/services.dart';

class BluetoothHidManager {
  BluetoothHidManager._();
  static final BluetoothHidManager instance = BluetoothHidManager._();

  Stream<HidConnectionEvent> get onConnectionStateChanged =>
      BluetoothHidManagerPlatform.instance.onConnectionStateChanged;

  Future<void> init() async {
    try {
      await BluetoothHidManagerPlatform.instance.init();
    } on PlatformException catch (e) {
      throw HidException(e.code, e.message ?? 'init failed');
    }
  }

  Future<void> connect(String deviceAddress) async {
    try {
      await BluetoothHidManagerPlatform.instance.connect(deviceAddress);
    } on PlatformException catch (e) {
      throw HidException(e.code, e.message ?? 'connect failed');
    }
  }

  Future<void> disconnect() async {
    try {
      await BluetoothHidManagerPlatform.instance.disconnect();
    } on PlatformException catch (e) {
      throw HidException(e.code, e.message ?? 'disconnect failed');
    }
  }

  Future<bool> get isConnected =>
      BluetoothHidManagerPlatform.instance.isConnected();
}