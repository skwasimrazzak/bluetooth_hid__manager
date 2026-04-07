import 'package:flutter/services.dart';

import 'bluetooth_hid_manager_platform_interface.dart';

/// An implementation of [BluetoothHidManagerPlatform] that uses method channels.
class MethodChannelBluetoothHidManager extends BluetoothHidManagerPlatform {
  static const _methodChannel = MethodChannel('bluetooth_hid_manager');
  static const _eventChannel = EventChannel(
    'bluetooth_hid_manager/connection_state',
  );

  static MethodChannel get hidMethodChannel => _methodChannel;

  static Stream<Map<dynamic, dynamic>> get connectionStateStream =>
      _eventChannel.receiveBroadcastStream().map(
        (event) => Map<dynamic, dynamic>.from(event as Map),
      );
}
