plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)

    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.24")
    implementation("com.android.tools.build:gradle:8.7.3")
}

gradlePlugin {
    plugins {
        register("androidHilt") {
            id = "whitenoiseapp.hilt.android"
            implementationClass = "com.example.whitenoiseapp.HiltAndroidPlugin"
        }
    }


}