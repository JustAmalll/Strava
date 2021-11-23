plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
}

dependencies {
    implementation(project(":core_data"))
    implementation(project(":shared_model"))
    //ViewBindDelegate
    implementation("com.github.kirich1409:viewbindingpropertydelegate:1.4.4")
    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:1.4.6")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.4.0")
}