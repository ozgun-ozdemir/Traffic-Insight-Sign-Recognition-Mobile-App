plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.trafficsigns"
    compileSdk {
        version = release(37) {
            minorApiLevel = 2
        }
    }

    defaultConfig {
        applicationId = "com.example.trafficsigns"
        minSdk = 29
        targetSdk = 37
        versionCode = 2
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
    }
    androidResources {
        // model is memory-mapped, so keep it uncompressed
        noCompress += "tflite"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.19.1")
    implementation("androidx.appcompat:appcompat:1.8.0")
    implementation("androidx.activity:activity-ktx:1.13.0")
    implementation("com.google.android.material:material:1.14.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.2")
    implementation("com.google.ai.edge.litert:litert:2.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")

}
