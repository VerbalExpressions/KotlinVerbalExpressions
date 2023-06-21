package buildsrc.conventions

import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget


/**
 * Base configuration for all Kotlin/Multiplatform conventions.
 *
 * This plugin does not enable any Kotlin target. To enable a target in a subproject, prefer applying specific Kotlin
 * target convention plugins.
 */

plugins {
    id("buildsrc.conventions.base")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}


kotlin {
    jvmToolchain(11)

    targets.configureEach {
        compilations.configureEach {
            kotlinOptions {
                // nothin' yet
            }
        }
    }

    // configure all Kotlin/JVM Tests to use JUnit
    targets.withType<KotlinJvmTarget>().configureEach {
        testRuns.configureEach {
            executionTask.configure {
                useJUnitPlatform()
            }
        }
    }

    sourceSets.configureEach {
        languageSettings {
//         languageVersion =
//         apiVersion =
        }
    }
}
