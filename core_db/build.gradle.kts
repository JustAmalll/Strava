plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
}

dependencies {
    implementation(project(":shared_model"))
    implementation("androidx.security:security-crypto:1.1.0-alpha03")
}