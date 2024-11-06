// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

//Add Google Maven Repository and Google Services Plugin
buildscript {
    repositories {
        // Add these if they aren't already present
        google()
        mavenCentral()
        dependencies {
            classpath("com.google.gms:google-services:4.4.2")
        }
    }
}

////Ensure google() is included in the allprojects repositories
//allprojects {
//    repositories {
//        google()
//        mavenCentral()
//    }
//}
