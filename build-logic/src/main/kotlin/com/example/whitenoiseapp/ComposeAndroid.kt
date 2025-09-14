package com.example.whitenoiseapp

import org.gradle.api.Project

internal fun Project.configureComposeAndroid() {
    val composeModules = setOf("core", "app")

    if (name in composeModules) {
        androidExtension.apply {
            buildFeatures {
                compose = true
            }

            composeOptions {
                kotlinCompilerExtensionVersion = "1.5.14"
            }
        }
    }
}