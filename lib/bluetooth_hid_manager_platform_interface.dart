import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:bluetooth_hid_manager/bluetooth_hid_manager_method_channel.dart';
import 'src/hid_connection_state.dart';

abstract class BluetoothHidManagerPlatform extends PlatformInterface {
  BluetoothHidManagerPlatform() : super(token: _token);
  static final Object _token = Object();

  static BluetoothHidManagerPlatform _instance =
      MethodChannelBluetoothHidManager();
  static BluetoothHidManagerPlatform get instance => _instance;
  static set instance(BluetoothHidManagerPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Stream<HidConnectionEvent> get onConnectionStateChanged =>
      throw UnimplementedError('onConnectionStateChanged not implemented');

  Future<void> init() => throw UnimplementedError('init() not implemented');
  Future<void> connect(String address) => throw UnimplementedError();
  Future<void> disconnect() => throw UnimplementedError();
  Future<bool> isConnected() => throw UnimplementedError();

  Future<void> sendKeyboardReport(int modifier, List<int> keys) =>
      throw UnimplementedError();
  Future<void> sendMouseReport(int buttons, int x, int y, int wheel) =>
      throw UnimplementedError();
  Future<void> sendGamepadReport(int buttons, int lx, int ly, int rx, int ry) =>
      throw UnimplementedError();
}
