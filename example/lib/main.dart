import 'package:flutter/material.dart';
import 'package:bluetooth_hid_manager/bluetooth_hid_manager_platform_interface.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(home: HidTestScreen());
  }
}

class HidTestScreen extends StatefulWidget {
  const HidTestScreen({super.key});

  @override
  State<HidTestScreen> createState() => _HidTestScreenState();
}

class _HidTestScreenState extends State<HidTestScreen> {
  final manager = BluetoothHidManagerPlatform.instance;
  String status = "Disconnected";

  @override
  void initState() {
    super.initState();

    manager.onConnectionStateChanged.listen((event) {
      setState(() {
        status = event.state.name;
      });
    });
  }

  Future<void> initHid() async {
    try {
      await manager.init();
    } catch (e) {
      print(e);
    }
  }

  Future<void> connect() async {
    try {
      await manager.connect("XX:XX:XX:XX:XX:XX"); // 🔥 replace with real MAC
    } catch (e) {
      print(e);
    }
  }

  Future<void> sendKey() async {
    try {
      await manager.sendKeyboardReport(0, [0x04]); // 'a'
      await Future.delayed(const Duration(milliseconds: 100));
      await manager.sendKeyboardReport(0, []); // release
    } catch (e) {
      print(e);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("HID Test")),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text("Status: $status"),
          const SizedBox(height: 20),

          ElevatedButton(onPressed: initHid, child: const Text("Init")),

          ElevatedButton(onPressed: connect, child: const Text("Connect")),

          ElevatedButton(onPressed: sendKey, child: const Text("Send 'A'")),
        ],
      ),
    );
  }
}
