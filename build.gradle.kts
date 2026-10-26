// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        // newer Kotlin than the one bundled with AGP (needed by LiteRT 2.x)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.21")
    }
}

plugins {
    id("com.android.application") version "9.4.1" apply false
}
