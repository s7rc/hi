# PadKit Joystick Fix - Instructions

## What Was Fixed

Fixed the joystick inner circle behavior so it properly stays within the outer circle boundary and reaches the edges when swiping in any direction.

### The Problem
- When you lowered the inner circle size (`foregroundSize`) in Lemuroid, the joystick used a fixed offset multiplier (`0.25f`)
- This meant the inner circle couldn't reach the opposite edge when you swiped
- It felt slow and unresponsive compared to other emulators

### The Solution
- Replaced the fixed `0.25f` multiplier with a dynamic calculation: `(1f - foregroundSize) / 2f`
- Now the inner circle movement scales properly based on the available ring space
- The inner circle reaches the full extent of the outer circle regardless of its size

## Files Modified

### 1. PadKit Source Code
**File**: `/home/sami/Documents/GitHub/PadKit/lib/src/commonMain/kotlin/gg/padkit/controls/ControlAnalog.kt`
- Added dynamic offset calculation based on `foregroundSize`
- This is your main PadKit repository

### 2. Lemuroid's Local PadKit Copy
**File**: `/home/sami/Documents/GitHub/hi/padkit-source/lib/src/commonMain/kotlin/gg/padkit/controls/ControlAnalog.kt`
- Applied the same fix to Lemuroid's local copy
- This ensures Lemuroid uses the fixed version

### 3. Lemuroid Build Configuration
**Files Modified**:
- `/home/sami/Documents/GitHub/hi/settings.gradle.kts`
  - Added `:padkit-lib` as a local module
  - Configured it to point to `padkit-source/lib`
  
- `/home/sami/Documents/GitHub/hi/lemuroid-touchinput/build.gradle.kts`
  - Changed from `api(deps.libs.padkit)` (published v1.0.0-beta1)
  - To `api(project(":padkit-lib"))` (your local modified version)

## How to Build Lemuroid with the Fix

### Option 1: Build the App (Recommended)
```bash
cd /home/sami/Documents/GitHub/hi
./gradlew :lemuroid-app:assembleDebug
```

The APK will be at: `lemuroid-app/build/outputs/apk/debug/lemuroid-app-debug.apk`

### Option 2: Build and Install Directly
```bash
cd /home/sami/Documents/GitHub/hi
./gradlew :lemuroid-app:installDebug
```

This will build and install directly to your connected Android device.

## Verifying the Fix

After installing Lemuroid:
1. Open a game and use the on-screen joystick
2. The inner circle should now move smoothly within the outer circle
3. When you swipe to the opposite direction, the inner circle should reach the edge
4. It should feel responsive like other emulators

## Reverting to Published PadKit (If Needed)

If you want to go back to using the published PadKit library:

**Edit**: `/home/sami/Documents/GitHub/hi/lemuroid-touchinput/build.gradle.kts`

Change:
```kotlin
api(project(":padkit-lib"))  // Use local modified PadKit
// api(deps.libs.padkit)  // Commented out: was using published version
```

Back to:
```kotlin
api(deps.libs.padkit)  // Use published version
```

## Technical Details

### The Code Change

**Before** (fixed offset):
```kotlin
Box(
    modifier = Modifier
        .fillMaxSize(foregroundSize)
        .offset(
            maxWidth * safePosition.x * 0.25f,
            -maxHeight * safePosition.y * 0.25f,
        ),
) {
    foreground(pressedState)
}
```

**After** (dynamic offset):
```kotlin
// Calculate max offset based on the ring width between background and foreground
// This ensures the inner circle stays within the outer circle boundary
val maxOffsetFraction = (1f - foregroundSize) / 2f

Box(
    modifier = Modifier
        .fillMaxSize(foregroundSize)
        .offset(
            maxWidth * safePosition.x * maxOffsetFraction,
            -maxHeight * safePosition.y * maxOffsetFraction,
        ),
) {
    foreground(pressedState)
}
```

### How the Formula Works

`maxOffsetFraction = (1f - foregroundSize) / 2f`

Examples:
- `foregroundSize = 1.0` (inner = outer) → `maxOffsetFraction = 0` → no movement ✓
- `foregroundSize = 0.66` (default) → `maxOffsetFraction = 0.17` → appropriate range ✓
- `foregroundSize = 0.5` (small inner) → `maxOffsetFraction = 0.25` → larger range ✓

The smaller the inner circle, the more space it has to move within the ring.

## Troubleshooting

### Build Errors
If you get build errors about PadKit:
1. Make sure `padkit-source/lib` directory exists and has all the source files
2. Run `./gradlew clean` to clear cached builds
3. Try building again

### Joystick Still Not Working
1. Make sure you installed the newly built APK, not the old one
2. Check that the changes were applied to the correct file
3. Verify the app is using the local PadKit by checking build logs

## Questions?

The fix is applied to both:
- ✅ Your main PadKit repository (`/home/sami/Documents/GitHub/PadKit`)
- ✅ Lemuroid's local PadKit copy (`/home/sami/Documents/GitHub/hi/padkit-source`)

Lemuroid is now configured to use the local modified version instead of the published library.
