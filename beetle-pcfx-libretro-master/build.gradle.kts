plugins {
    id("com.android.library")
}

android {
    namespace = "com.libretro.beetle_pcfx"
    compileSdk = 34
    ndkVersion = "25.1.8937393"

    defaultConfig {
        minSdk = 21
        targetSdk = 34
        externalNativeBuild {
            ndkBuild {
                arguments("NDK_PROJECT_PATH=jni")
                // Core flags handled in Android.mk
            }
        }
    }

    externalNativeBuild {
        ndkBuild {
            path("jni/Android.mk")
        }
    }
    
    // Explicitly set source sets if needed, but externalNativeBuild usually handles it
}
