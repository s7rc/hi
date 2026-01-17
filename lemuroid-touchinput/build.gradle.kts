plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlinx-serialization")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs +=
            listOf(
                "-Xcontext-receivers",
            )
    }
    namespace = "com.swordfish.touchinput.controller"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = deps.versions.kotlinExtension
    }
}

dependencies {
    implementation(project(":retrograde-util"))

    implementation(platform(deps.libs.androidx.compose.composeBom))
    implementation(deps.libs.androidx.compose.geometry)
    implementation(deps.libs.androidx.compose.runtime)
    implementation(deps.libs.androidx.compose.material3)

    implementation(deps.libs.androidx.appcompat.constraintLayout)
    implementation(deps.libs.androidx.appcompat.appcompat)
    implementation(deps.libs.androidx.lifecycle.commonJava8)
    implementation(deps.libs.material)
    implementation(deps.libs.androidx.preferences.preferencesKtx)
    implementation(deps.libs.flowPreferences)
    implementation(deps.libs.kotlin.serialization)
    implementation(deps.libs.kotlin.serializationJson)

<<<<<<< HEAD
    // Use extracted JAR for compilation (Gradle allows .jar in libs, but not .aar)
    api("io.github.s7rc:padkit:VERSION_HERE")
    
    // Dependencies required by PadKit
    api(deps.libs.collectionsImmutable)
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
=======

    api(deps.libs.padkit)  // Use published version - local build too complex
    api(deps.libs.collectionsImmutable)
>>>>>>> parent of 03fdc5e (j)

    implementation(kotlin(deps.libs.kotlin.stdlib))

    kapt(deps.libs.androidx.lifecycle.processor)
}
