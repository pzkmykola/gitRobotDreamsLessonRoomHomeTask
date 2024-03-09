buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply false
    id ("com.google.gms.google-services") version "4.4.1" apply false
    id ("org.jetbrains.kotlin.kapt") version "1.9.10"
    id ("com.google.dagger.hilt.android") version "2.50" apply false
}