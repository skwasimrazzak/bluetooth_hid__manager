enum HidConnectionState {
  registered,
  connected,
  disconnected,
  connecting,
  disconnecting,
  unregistered,
}

class HidConnectionEvent {
  final HidConnectionState state;
  final String? deviceAddress;

  const HidConnectionEvent(this.state, {this.deviceAddress});

  factory HidConnectionEvent.fromMap(Map<dynamic, dynamic> map) {
    final stateStr = map['state'] as String;
    final address = map['deviceAddress'] as String?;
    final state = HidConnectionState.values.firstWhere(
      (e) => e.name == stateStr,
      orElse: () => HidConnectionState.disconnected,
    );
    return HidConnectionEvent(state, deviceAddress: address);
  }
}
