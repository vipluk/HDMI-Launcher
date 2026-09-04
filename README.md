# HDMI Launcher for Allwinner Android Projectors

A lightweight, zero-overhead Android application that acts as a direct HDMI input shortcut for budget Android projectors powered by Allwinner SoCs.

---

## 📽️ About

Most budget Android projectors (including the **Magcubic HY320**, **Magcubic HY300**, **Transspeed**, **Hotack**, and other devices built on Allwinner chipsets such as the **Allwinner H713**) share a common Android firmware base supplied by Allwinner.

By default, switching to the HDMI input often requires navigating through clumsy stock menus or deep settings. **HDMI Launcher** provides a 1-click shortcut right on your home screen or Android TV Leanback launcher that instantly switches the projector to the HDMI input and terminates itself cleanly in the background.

---

## ✨ Features

- **Instant HDMI Switching**: Immediately launches the projector's native HDMI input receiver upon launch.
- **Smart Fallback Mechanism**: Cycles through known Allwinner HDMI input receiver components:
  1. `com.softwinner.awlivetv / .MainActivity` (used on Magcubic HY320, HY300, etc.)
  2. `com.softwinner.awsource / .MainActivity` (used on alternative Allwinner firmware builds)
- **Leanback / Android TV Support**: Includes a 16:9 banner (`@drawable/banner`) for Android TV launchers, projector home screen docks, and standard launcher grids.
- **Ultra Lightweight**: Weighs under 50 KB, uses zero external runtime dependencies, leaves no resident background processes or battery drain.
- **Translucent Transition**: Employs `Theme.Translucent.NoTitleBar` to ensure a smooth, flicker-free jump into HDMI mode.

---

## 📺 Supported Devices

Tested and designed for projectors running Android firmware based on Allwinner platforms (such as the Allwinner H713 SoC):
- **Magcubic**: HY320, HY300, HY300 Pro
- **Transspeed**: Android Projector models (HY300/HY320 variants)
- **Hotack**: D035, H713-based smart projectors
- Any other projector or Android TV box using Allwinner's `awlivetv` or `awsource` packages.

---

## 📥 Installation

1. Download the latest `HDMI_Launcher.apk` from the [Releases](https://github.com/vipluk/HDMI-Launcher/releases) section.
2. Transfer the APK to your projector using one of the following methods:
   - **USB Drive**: Copy `HDMI_Launcher.apk` to a FAT32/exFAT USB flash drive, plug it into the projector, and open it using the projector's File Manager.
   - **ADB (Android Debug Bridge)**:
     ```bash
     adb connect <projector-ip>:5555
     adb install -r HDMI_Launcher.apk
     ```
   - **Local Network / Downloader App**: Use apps like *Send Files to TV* or browser downloaders.
3. The **HDMI** icon and Leanback banner will now appear on your projector's home screen. Click it anytime to switch directly to HDMI input!

---

## 🛠️ Building from Source

Requirements:
- JDK 17
- Android SDK (compileSdk 34, minSdk 21)

```bash
# Clone the repository
git clone https://github.com/vipluk/HDMI-Launcher.git
cd HDMI-Launcher

# Build the release APK
./gradlew assembleRelease
```

The resulting binary will be generated at:
```text
app/build/outputs/apk/release/HDMI_Launcher.apk
```

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
