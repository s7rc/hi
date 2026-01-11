# Using Your Built PadKit Library in Lemuroid

You have **3 options** to use your modified PadKit in Lemuroid:

---

## ✅ Option A: Publish to Local Maven (Recommended)

This method publishes your built PadKit to your local Maven repository (`~/.m2/repository/`) and configures Lemuroid to use it.

### Step 1: Publish PadKit to Local Maven

```bash
cd /home/sami/Documents/GitHub/PadKit
./gradlew publishToMavenLocal
```

This will publish your modified PadKit to: `~/.m2/repository/io/github/swordfish90/padkit/`

### Step 2: Configure Lemuroid to Use Local Maven

**Edit**: `/home/sami/Documents/GitHub/hi/build.gradle.kts`

Add `mavenLocal()` to the repositories block:

```kotlin
allprojects {
    repositories {
        mavenLocal()  // Add this FIRST so it takes priority
        google()
        mavenCentral()
        // ... other repositories
    }
}
```

If there's no `allprojects` block, add it to the root `build.gradle.kts`.

### Step 3: Update PadKit Version in Lemuroid

**Edit**: `/home/sami/Documents/GitHub/hi/buildSrc/src/main/java/deps.kt`

Find line 29 and change the version to match your build:

```kotlin
const val padkit = "YOUR_VERSION_HERE"  // e.g., "1.0.0-beta1" or whatever version your build uses
```

To check the version, run in PadKit directory:
```bash
cd /home/sami/Documents/GitHub/PadKit
./gradlew :lib:properties | grep "version:"
```

### Step 4: Revert to Using Published Dependency

**Edit**: `/home/sami/Documents/GitHub/hi/lemuroid-touchinput/build.gradle.kts`

Change back to:
```kotlin
api(deps.libs.padkit)  // Use Maven dependency (will pick up local Maven)
// api(project(":padkit-lib"))  // Comment out local project
```

### Step 5: Build Lemuroid

```bash
cd /home/sami/Documents/GitHub/hi
./gradlew clean
./gradlew :lemuroid-app:assembleDebug
```

---

## Option B: Use Local Project Module (Already Configured)

This is what I already set up for you - it builds the PadKit source code as part of Lemuroid's build.

**No additional steps needed!** Just build Lemuroid:

```bash
cd /home/sami/Documents/GitHub/hi
./gradlew :lemuroid-app:assembleDebug
```

**Pros**: 
- No need to publish separately
- Changes automatically included when you rebuild Lemuroid

**Cons**:
- Slower builds (compiles PadKit every time)
- Need to keep padkit-source in sync with your main PadKit repo

---

## Option C: Copy Built AAR Directly (Quick & Dirty)

If you have the built `.aar` file, you can include it directly.

### Step 1: Build PadKit

```bash
cd /home/sami/Documents/GitHub/PadKit
./gradlew :lib:assembleRelease
```

The AAR will be at: `lib/build/outputs/aar/lib-release.aar`

### Step 2: Copy AAR to Lemuroid

```bash
mkdir -p /home/sami/Documents/GitHub/hi/libs
cp /home/sami/Documents/GitHub/PadKit/lib/build/outputs/aar/lib-release.aar \
   /home/sami/Documents/GitHub/hi/libs/padkit-custom.aar
```

### Step 3: Configure Lemuroid to Use the AAR

**Edit**: `/home/sami/Documents/GitHub/hi/lemuroid-touchinput/build.gradle.kts`

Change the dependency to:

```kotlin
api(files("../libs/padkit-custom.aar"))
// api(deps.libs.padkit)  // Comment out
// api(project(":padkit-lib"))  // Comment out
```

**Note**: This method might cause issues with transitive dependencies. You may need to manually add PadKit's dependencies.

---

## 🎯 Recommended Approach

I recommend **Option A** (Local Maven) because:
- ✅ Clean separation between PadKit and Lemuroid
- ✅ Proper dependency management
- ✅ Easy to switch versions
- ✅ No need to keep source code in sync

After publishing to local Maven, Gradle will automatically use your local version instead of downloading from Maven Central.

---

## Verification

After building Lemuroid, check that it's using your modified PadKit:

1. Build and install: `./gradlew :lemuroid-app:installDebug`
2. Open a game with on-screen controls
3. Test the joystick - the inner circle should reach the edges properly

---

## Troubleshooting

### "Could not find padkit"
- Make sure you ran `publishToMavenLocal` in the PadKit directory
- Check that `mavenLocal()` is in the repositories list
- Verify the version matches in `deps.kt`

### Build Still Uses Old PadKit
- Run `./gradlew clean` in Lemuroid directory
- Delete `~/.gradle/caches/` to clear all cached dependencies
- Rebuild Lemuroid

### Version Conflicts
Check what version was published:
```bash
ls ~/.m2/repository/io/github/swordfish90/padkit/
```

Update `deps.kt` to use that exact version.
