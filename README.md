# HDMI Launcher for Allwinner Android Projectors

<p align="center">
  <img src="assets/banner.png" alt="HDMI Launcher Banner" width="600" />
</p>

<p align="center">
  <img src="assets/icon.png" alt="HDMI Launcher Icon" width="120" />
</p>

<p align="center">
  <strong>A lightweight, zero-overhead Android application acting as a direct HDMI input shortcut for budget Android projectors powered by Allwinner SoCs.</strong>
</p>

---

## About

Most budget Android projectors (including the **Magcubic HY320**, **Magcubic HY300**, **Transspeed**, **Hotack**, and other devices built on Allwinner chipsets such as the **Allwinner H713**) share a common Android firmware base supplied by Allwinner.

By default, switching to the HDMI input requires navigating through clumsy stock menus or deep settings. **HDMI Launcher** provides a 1-click shortcut right on your home screen or Android TV Leanback launcher that instantly switches the projector to the HDMI input and terminates itself cleanly in the background.

---

## Primary Use Case: Automatic HDMI on Boot (Dedicated Display Mode)

The key motivation behind this app is turning your smart projector into a **hassle-free, dedicated HDMI monitor or TV screen**:

1. **The Problem**: Stock Allwinner projector firmware forces the device to boot into the cluttered Android home screen every time it is powered on. There is no native setting to default directly to the HDMI input.
2. **The Solution**: By pairing **HDMI Launcher** with an autostart utility such as [**Launch-On-Boot** (ITVlab/Launch-On-Boot)](https://github.com/ITVlab/Launch-On-Boot):
   - Set **HDMI Launcher** as the startup application in Launch-On-Boot.
   - When you turn on your projector, it boots up and immediately triggers the HDMI input switch automatically.
   - **No remote required** — ideal for setups with a PC, PlayStation, Xbox, Nintendo Switch, Apple TV, or streaming box where you just want the projector to act as a regular monitor.

---

## Features

- **Instant HDMI Switching**: Directly triggers the projector's native HDMI video feed.
- **Smart Fallback Mechanism**: Cycles through known Allwinner HDMI input receiver components:
  1. `com.softwinner.awlivetv / .MainActivity` (used on Magcubic HY320, HY300, etc.)
  2. `com.softwinner.awsource / .MainActivity` (used on alternative Allwinner firmware builds)
- **Leanback / Android TV Support**: Includes a 16:9 banner (`@drawable/banner`) for Android TV launchers, projector home screen docks, and standard launcher grids.
- **Ultra Lightweight**: Weighs ~44 KB, zero external dependencies, leaves no resident background processes or battery drain.
- **Translucent Transition**: Employs `Theme.Translucent.NoTitleBar` to ensure a smooth, flicker-free jump into HDMI mode.

---

## Application Graphics

| Android TV Leanback Banner (16:9) | Launcher App Icon |
| :---: | :---: |
| <img src="assets/banner.png" alt="Leanback Banner" width="340" /> | <img src="assets/icon.png" alt="Launcher Icon" width="140" /> |
| Displayed on Android TV home screens | Displayed in standard launcher app drawers |

---

## Supported Devices

Tested and designed for projectors running Android firmware based on Allwinner platforms (such as the Allwinner H713 SoC):
- **Magcubic**: HY320, HY300, HY300 Pro
- **Transspeed**: Android Projector models (HY300/HY320 variants)
- **Hotack**: D035, H713-based smart projectors
- Any other projector or Android TV box using Allwinner's `awlivetv` or `awsource` packages.

---

## Installation & Setup

### 1. Choose & Install Your APK Flavor
Two APK variants are provided in the [Releases](https://github.com/vipluk/HDMI-Launcher/releases) section:

- **`HDMI_Launcher.apk` (Instant)**: Immediately switches to HDMI upon launch. Best used as a standard desktop or dock shortcut for quick switching with your remote.
- **`HDMI_Launcher-5s.apk` (5s Delayed)**: Waits 5 seconds before switching to HDMI. **Recommended for autostart setups (Launch-On-Boot)** to give Android and Allwinner video drivers enough time to fully stabilize after power-on before opening the HDMI feed.

> [!TIP]
> Both versions can be installed side-by-side on the same projector without conflicts (the 5s version appears in your launcher as **HDMI (5s)**).

1. Download your preferred APK from [Releases](https://github.com/vipluk/HDMI-Launcher/releases).
2. Transfer the APK to your projector:
   - **USB Drive**: Copy the APK to a USB flash drive, plug it into the projector, and install it via the file manager.
   - **ADB (Android Debug Bridge)**:
     ```bash
     adb connect <projector-ip>:5555
     adb install -r HDMI_Launcher.apk
     # or for the 5s variant:
     adb install -r HDMI_Launcher-5s.apk
     ```

### 2. (Optional) Configure Automatic Boot to HDMI
To automatically switch to HDMI every time the projector boots:
1. Install [Launch-On-Boot](https://github.com/ITVlab/Launch-On-Boot).
2. Open Launch-On-Boot, grant required permissions, and select **HDMI** (or **HDMI (5s)**) as the target application.
3. Enable "Launch on boot".
4. Power cycle your projector to verify it switches directly to HDMI!

---

## Building from Source

Requirements:
- JDK 17
- Android SDK (compileSdk 34, minSdk 21)

```bash
# Clone the repository
git clone https://github.com/vipluk/HDMI-Launcher.git
cd HDMI-Launcher

# Build both release APKs
./gradlew assembleRelease
```

The resulting binaries will be generated at:
```text
app/build/outputs/apk/standard/release/HDMI_Launcher.apk
app/build/outputs/apk/delayed5s/release/HDMI_Launcher-5s.apk
```

---

## License

This project is open source and available under the [MIT License](LICENSE).
