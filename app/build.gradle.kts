plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.smsforwarder.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.smsforwarder.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("com.google.accompanist:batteries-extension:0.35.0")
    // For SMS handling
    implementation("androidx.annotation:annotation:1.6.0")
}