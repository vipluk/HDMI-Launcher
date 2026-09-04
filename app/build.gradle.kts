plugins {
    id("com.android.application")
}

android {
    namespace = "com.lukas.hdmilauncher"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lukas.hdmilauncher"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
