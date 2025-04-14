// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.51" apply false
    id("androidx.navigation.safeargs.kotlin") apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Existing dependencies
        classpath("com.android.tools.build:gradle:8.0.2")

        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44")
        // Add Safe Args classpath
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
    }
}


