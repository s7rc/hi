# Release Build Signing Fix

## Issue
Release builds were failing on GitHub Actions with:
```
Keystore file '/home/runner/work/hi/hi/release.jks' not found for signing config 'release'.
```

## Root Cause
- Release builds require signing with a keystore file (`release.jks`)
- This file exists locally but is **not committed** to git (and shouldn't be for security)
- GitHub Actions doesn't have access to this file
- Debug builds work because they use a default `debug.keystore`

## Fix Applied
Made the release signing configuration **conditional** in `lemuroid-app/build.gradle.kts`:

```kotlin
val releaseKeystoreFile = file("$rootDir/release.jks")
signingConfig = if (releaseKeystoreFile.exists()) {
    signingConfigs["release"]  // Use proper signing locally
} else {
    println("WARNING: release.jks not found, using debug signing")
    signingConfigs["debug"]     // Fallback for CI
}
```

## Behavior

### Local Builds (where release.jks exists)
- ✅ Uses proper release signing
- ✅ Creates production-ready APKs

### GitHub Actions (no release.jks)
- ✅ Falls back to debug signing
- ✅ Build succeeds and creates APK
- ⚠️ APK is signed with debug key (fine for testing, not for production)

## Files Modified
- `lemuroid-app/build.gradle.kts` - Made release signing conditional

## Production Deployment
If you need properly signed release APKs from GitHub Actions, you would need to:
1. Encode the keystore as base64
2. Store it in GitHub Secrets
3. Decode it during the build workflow

For now, the release APK from GitHub Actions will be debug-signed, which is fine for testing.
