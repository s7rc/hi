plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    compileSdk = deps.android.compileSdkVersion
    namespace = "gg.padkit.lib"

    defaultConfig {
        minSdk = deps.android.minSdkVersion
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = deps.versions.kotlinExtension
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
            resources.srcDirs("src/commonMain/resources")
            // Only include the directories we need, skip haptics
            java.setSrcDirs(listOf(
                "src/commonMain/kotlin/gg/padkit/controls",
                "src/commonMain/kotlin/gg/padkit/handlers",
                "src/commonMain/kotlin/gg/padkit/ids",
                "src/commonMain/kotlin/gg/padkit/inputstate",
                "src/commonMain/kotlin/gg/padkit/inputevents",
                "src/commonMain/kotlin/gg/padkit/layouts",
                "src/commonMain/kotlin/gg/padkit/ui",
                "src/commonMain/kotlin/gg/padkit/utils",
                "src/androidMain/kotlin"
            ))
        }
    }
}

dependencies {
    implementation(platform(deps.libs.androidx.compose.composeBom))
    implementation(deps.libs.androidx.compose.runtime)
    implementation(deps.libs.androidx.compose.material3)
    
    implementation(deps.libs.collectionsImmutable)
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
}
