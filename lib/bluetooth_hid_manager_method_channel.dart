import 'package:flutter/services.dart';
import 'bluetooth_hid_manager_platform_interface.dart';
import 'src/hid_connection_state.dart';

class MethodChannelBluetoothHidManager extends BluetoothHidManagerPlatform {
  static const _methodChannel = MethodChannel('bluetooth_hid_manager');
  static const _eventChannel = EventChannel(
    'bluetooth_hid_manager/connection_state',
  );

  @override
  Stream<HidConnectionEvent> get onConnectionStateChanged =>
      _eventChannel.receiveBroadcastStream().map(
        (event) => HidConnectionEvent.fromMap(
          Map<dynamic, dynamic>.from(event as Map),
        ),
      );

  @override
  Future<void> init() => _methodChannel.invokeMethod('init');

  @override
  Future<void> connect(String address) =>
      _methodChannel.invokeMethod('connect', {'address': address});

  @override
  Future<void> disconnect() => _methodChannel.invokeMethod('disconnect');

  @override
  Future<bool> isConnected() async =>
      await _methodChannel.invokeMethod<bool>('isConnected') ?? false;

  @override
  Future<void> sendKeyboardReport(int modifier, List<int> keys) =>
      _methodChannel.invokeMethod('sendKeyboardReport', {
        'modifier': modifier,
        'keys': keys,
      });

  @override
  Future<void> sendMouseReport(int buttons, int x, int y, int wheel) =>
      _methodChannel.invokeMethod('sendMouseReport', {
        'buttons': buttons,
        'x': x,
        'y': y,
        'wheel': wheel,
      });

  @override
  Future<void> sendGamepadReport(int buttons, int lx, int ly, int rx, int ry) =>
      _methodChannel.invokeMethod('sendGamepadReport', {
        'buttons': buttons,
        'lx': lx,
        'ly': ly,
        'rx': rx,
        'ry': ry,
      });
}
