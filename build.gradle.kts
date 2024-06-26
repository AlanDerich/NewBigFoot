buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
    }
}

plugins {
    id("com.android.application") version "8.5.0-rc01" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}