class HidException implements Exception {
  final String code;
  final String message;

  const HidException(this.code, this.message);

  @override
  String toString() => 'HidException($code): $message';
}
