plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(project(":shared_model"))
    implementation(project(":core_db"))
    //Dagger-Hilt
    val dagger_version = rootProject.extra["dagger_version"]
    val hilt_work_version = rootProject.extra["hilt_work_version"]
    implementation("com.google.dagger:hilt-android:$dagger_version")
    kapt("com.google.dagger:hilt-android-compiler:$dagger_version")
    kapt("androidx.hilt:hilt-compiler:$hilt_work_version")
    implementation("androidx.hilt:hilt-work:$hilt_work_version")
    kapt("androidx.hilt:hilt-compiler:$hilt_work_version")
}