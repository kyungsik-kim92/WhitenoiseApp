plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidHilt") {
            id = "whitenoiseapp.android.hilt"
            implementationClass = "com.example.whitenoiseapp.HiltAndroidPlugin"
        }
    }


}