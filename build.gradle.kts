plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    kotlin("plugin.serialization") version "1.4.21"
    id("maven-publish")
    id("com.chromaticnoise.multiplatform-swiftpackage") version "2.0.3"
}

version = "1.0"

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    val publicationsFromMainHost =
        listOf(jvm(), js()).map { it.name } + "kotlinMultiplatform"
    publishing {
        publications {
            matching { it.name in publicationsFromMainHost }.all {
                val targetPublication = this@all
                tasks.withType<AbstractPublishToMaven>()
                    .matching { it.publication == targetPublication }
                    .configureEach { onlyIf { findProperty("isMainHost") == "true" } }
            }
            register<MavenPublication>("release") {
                groupId = "github.com.LeviDanCoder"
                artifactId = "KotlinApplicationApi"
                version = "1.0"

                afterEvaluate {
                    from(components["release"])
                }
            }
        }
    }

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "https://github.com/LeviDanCoder/KotlinMultiplatformApiLibrary"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "shared"
        }
    }

    multiplatformSwiftPackage {
        swiftToolsVersion("5.3")
        targetPlatforms {
            iOS { v("13") }
        }
    }

    sourceSets {
        val ktorVersion = "2.0.3"
        val commonMain by getting {
            dependencies {
                implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")

                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")

                implementation("io.ktor:ktor-client-logging:$ktorVersion")


            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation ("io.ktor:ktor-client-android:$ktorVersion")
                implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.3")

            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependencies{
                implementation ("io.ktor:ktor-client-ios:$ktorVersion")
            }
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 23
        targetSdk = 32
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}