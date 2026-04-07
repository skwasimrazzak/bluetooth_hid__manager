# 📱 Bluetooth HID Manager (Flutter)

A Flutter plugin that allows an Android device to act as a **Bluetooth Human Interface Device (HID)** such as a **keyboard, mouse, or gamepad**, and send input to a paired device like a PC or laptop.

---

## 🚀 Overview

Bluetooth HID Manager enables your phone to behave like a wireless input controller using Android’s native Bluetooth HID capabilities. This can be used to build apps for remote control, gaming, accessibility tools, and more.

---

## 🎯 Features

- ⌨️ Emulate a Bluetooth **Keyboard**
- 🖱 Emulate a Bluetooth **Mouse**
- 🎮 Emulate a Bluetooth **Gamepad**
- 🔗 Connect to already paired Bluetooth devices
- 📡 Send HID input reports from phone → host device

---

## ⚙️ Platform Support

| Platform | Support |
|---------|--------|
| Android | ✅ Supported (API 28+) |
| iOS     | ❌ Not Supported |

> iOS does not allow third-party apps to emulate Bluetooth HID devices.

---

## 🧪 Example Use Case

- Turn your phone into a wireless keyboard for your PC
- Control mouse cursor from your phone
- Use your phone as a game controller

---

## 🚧 Project Status

> ⚠️ This project is currently under development

- Core plugin structure is implemented
- Basic HID functionality is being tested
- Not production-ready yet
- APIs may change

---

## 🛠 Requirements

- Android API level **28 or higher**
- Bluetooth enabled on device
- Target device must be **paired manually** before connecting

---

## ⚠️ Permissions

The app using this plugin must request:

- `BLUETOOTH_CONNECT`
- `BLUETOOTH_ADVERTISE`

(Runtime permissions required on Android 12+)

---

## 📦 Installation

Add the plugin to your `pubspec.yaml`:

```yaml
dependencies:
  bluetooth_hid_manager:
    path: ../bluetooth_hid_manager