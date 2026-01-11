# Modified PadKit AAR Setup

This repository uses a **modified version of PadKit** with the joystick fix.

## What's Modified

The PadKit library has been modified to fix the joystick inner circle behavior:
- **Fixed**: Inner circle now properly scales based on `foregroundSize` 
- **Fixed**: Inner circle movement reaches the full extent of the outer circle
- **Result**: Joystick behaves like other emulators

## Location

The modified PadKit AAR files are located in:
```
libs/
├── lib-release.aar  (Used in builds)
└── lib-debug.aar    (For debugging)
```

## How It Works

The build configuration in `lemuroid-touchinput/build.gradle.kts` uses the local AAR:
```kotlin
api(files("../libs/lib-release.aar"))
```

This replaces the published PadKit version with our modified version.

## Building

### Local Build
```bash
./gradlew :lemuroid-app:assembleDebug
```

### GitHub Actions
The workflow automatically uses the AAR files from the `libs/` directory.
Push to GitHub and the Actions will build with the modified PadKit.

## Updating the Modified PadKit

If you need to update the PadKit AAR files:

1. Make changes in the PadKit source code repository
2. Build the new AAR:
   ```bash
   cd /path/to/PadKit
   ./gradlew :lib:assembleRelease
   ```
3. Copy the new AAR to this repo:
   ```bash
   cp lib/build/outputs/aar/lib-release.aar /path/to/lemuroid/libs/
   ```
4. Commit and push the updated AAR

## Dependencies

PadKit's transitive dependencies are manually declared in the build file:
- kotlinx-collections-immutable
- kotlinx-datetime
- Compose runtime and material3

These are required when using AAR files directly instead of the published library.
