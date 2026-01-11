# AAR vs Source Code Fix

## The Problem

When trying to use the PadKit AAR file (`libs/lib-release.aar`), the **Release build failed** with:

```
Direct local .aar file dependencies are not supported when building an AAR.
The resulting AAR would be broken because the classes and Android resources 
from any local .aar file dependencies would not be packaged in the resulting AAR.
```

## Why This Happens

- `lemuroid-touchinput` is a **library module** that builds an AAR file
- You **cannot** include a local AAR file as a dependency when building another AAR
- This is a Gradle/Android limitation
- Debug builds appeared to work because they use build cache, but Release does a full rebuild

## The Solution

Use the **source code** from `padkit-source/lib` instead of the AAR file:

**Before** (AAR - doesn't work):
```kotlin
api(files("../libs/lib-release.aar"))
```

**After** (Source - works):
```kotlin
api(project(":padkit-lib"))
```

## Files Modified

1. **`settings.gradle.kts`** - Re-added `:padkit-lib` module pointing to `padkit-source/lib`
2. **`lemuroid-touchinput/build.gradle.kts`** - Changed from AAR to project dependency

## About the AAR Files

The AAR files in `libs/` are **not being used** anymore, but you can:
- Keep them for reference
- Delete them to clean up
- Use them if you ever need to test PadKit standalone

They're not needed for the Lemuroid build since we're using the source code directly.

## Benefits of Using Source

✅ Works for both Debug and Release builds  
✅ No AAR-in-AAR limitation  
✅ Easier to debug if needed  
✅ Automatically rebuilds if you modify PadKit source  

## Result

Both Debug and Release builds should now succeed! 🎉
