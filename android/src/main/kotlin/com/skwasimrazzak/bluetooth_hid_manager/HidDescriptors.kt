package com.skwasimrazzak.bluetooth_hid_manager

object HidDescriptors {

    // Combined descriptor: Keyboard (ID 1) + Mouse (ID 2) + Gamepad (ID 3)
    val COMBINED: ByteArray = byteArrayOf(

        // ── Keyboard (Report ID 1) ──────────────────────────────
        0x05, 0x01,              // Usage Page (Generic Desktop)
        0x09, 0x06,              // Usage (Keyboard)
        0xA1.toByte(), 0x01,     // Collection (Application)
        0x85.toByte(), 0x01,     // Report ID (1)
        // Modifier keys — 8 bits
        0x05, 0x07,              // Usage Page (Keyboard/Keypad)
        0x19, 0xE0.toByte(),     // Usage Minimum (Left Control)
        0x29, 0xE7.toByte(),     // Usage Maximum (Right GUI)
        0x15, 0x00,              // Logical Minimum (0)
        0x25, 0x01,              // Logical Maximum (1)
        0x75, 0x01,              // Report Size (1)
        0x95.toByte(), 0x08,     // Report Count (8)
        0x81.toByte(), 0x02,     // Input (Data, Variable, Absolute)
        // Reserved byte
        0x75, 0x08,              // Report Size (8)
        0x95.toByte(), 0x01,     // Report Count (1)
        0x81.toByte(), 0x01,     // Input (Constant)
        // Key array — 6 simultaneous keycodes
        0x05, 0x07,              // Usage Page (Keyboard/Keypad)
        0x19, 0x00,              // Usage Minimum (0)
        0x29, 0x65,              // Usage Maximum (101)
        0x15, 0x00,              // Logical Minimum (0)
        0x25, 0x65,              // Logical Maximum (101)
        0x75, 0x08,              // Report Size (8)
        0x95.toByte(), 0x06,     // Report Count (6)
        0x81.toByte(), 0x00,     // Input (Data, Array)
        0xC0.toByte(),           // End Collection

        // ── Mouse (Report ID 2) ────────────────────────────────
        0x05, 0x01,              // Usage Page (Generic Desktop)
        0x09, 0x02,              // Usage (Mouse)
        0xA1.toByte(), 0x01,     // Collection (Application)
        0x85.toByte(), 0x02,     // Report ID (2)
        0x09, 0x01,              // Usage (Pointer)
        0xA1.toByte(), 0x00,     // Collection (Physical)
        // 3 buttons
        0x05, 0x09,              // Usage Page (Buttons)
        0x19, 0x01,              // Usage Minimum (1)
        0x29, 0x03,              // Usage Maximum (3)
        0x15, 0x00,              // Logical Minimum (0)
        0x25, 0x01,              // Logical Maximum (1)
        0x75, 0x01,              // Report Size (1)
        0x95.toByte(), 0x03,     // Report Count (3)
        0x81.toByte(), 0x02,     // Input (Data, Variable, Absolute)
        // Padding — 5 bits
        0x75, 0x05,              // Report Size (5)
        0x95.toByte(), 0x01,     // Report Count (1)
        0x81.toByte(), 0x01,     // Input (Constant)
        // X, Y, Wheel
        0x05, 0x01,              // Usage Page (Generic Desktop)
        0x09, 0x30,              // Usage (X)
        0x09, 0x31,              // Usage (Y)
        0x09, 0x38,              // Usage (Wheel)
        0x15, 0x81.toByte(),     // Logical Minimum (-127)
        0x25, 0x7F,              // Logical Maximum (127)
        0x75, 0x08,              // Report Size (8)
        0x95.toByte(), 0x03,     // Report Count (3)
        0x81.toByte(), 0x06,     // Input (Data, Variable, Relative)
        0xC0.toByte(),           // End Collection (Physical)
        0xC0.toByte(),           // End Collection (Application)

        // ── Gamepad (Report ID 3) ──────────────────────────────
        0x05, 0x01,              // Usage Page (Generic Desktop)
        0x09, 0x05,              // Usage (Gamepad)
        0xA1.toByte(), 0x01,     // Collection (Application)
        0x85.toByte(), 0x03,     // Report ID (3)
        // 16 buttons
        0x05, 0x09,              // Usage Page (Button)
        0x19, 0x01,              // Usage Minimum (1)
        0x29, 0x10,              // Usage Maximum (16)
        0x15, 0x00,              // Logical Minimum (0)
        0x25, 0x01,              // Logical Maximum (1)
        0x75, 0x01,              // Report Size (1)
        0x95.toByte(), 0x10,     // Report Count (16)
        0x81.toByte(), 0x02,     // Input (Data, Variable, Absolute)
        // 4 analog axes: LX, LY, RX, RY
        0x05, 0x01,              // Usage Page (Generic Desktop)
        0x09, 0x30,              // Usage (X — Left stick X)
        0x09, 0x31,              // Usage (Y — Left stick Y)
        0x09, 0x33,              // Usage (Rx — Right stick X)
        0x09, 0x34,              // Usage (Ry — Right stick Y)
        0x15, 0x81.toByte(),     // Logical Minimum (-127)
        0x25, 0x7F,              // Logical Maximum (127)
        0x75, 0x08,              // Report Size (8)
        0x95.toByte(), 0x04,     // Report Count (4)
        0x81.toByte(), 0x02,     // Input (Data, Variable, Absolute)
        0xC0.toByte()            // End Collection
    )
}