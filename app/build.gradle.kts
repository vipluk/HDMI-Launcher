plugins {
    id("com.android.application")
}

android {
    namespace = "com.vipluk.hdmilauncher"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vipluk.hdmilauncher"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        manifestPlaceholders["appLabel"] = "HDMI"
    }

    buildFeatures {
        buildConfig = true
    }

    flavorDimensions += "mode"

    productFlavors {
        create("standard") {
            dimension = "mode"
            buildConfigField("long", "DELAY_MS", "0L")
            manifestPlaceholders["appLabel"] = "HDMI"
        }
        create("delayed5s") {
            dimension = "mode"
            applicationIdSuffix = ".delay5s"
            versionNameSuffix = "-5s"
            buildConfigField("long", "DELAY_MS", "5000L")
            manifestPlaceholders["appLabel"] = "HDMI (5s)"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
        debug {
            isMinifyEnabled = false
        }
    }

    applicationVariants.all {
        val currentFlavor = flavorName
        outputs.all {
            val output = this as? com.android.build.gradle.internal.api.BaseVariantOutputImpl
            if (currentFlavor == "delayed5s") {
                output?.outputFileName = "HDMI_Launcher-5s.apk"
            } else {
                output?.outputFileName = "HDMI_Launcher.apk"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
