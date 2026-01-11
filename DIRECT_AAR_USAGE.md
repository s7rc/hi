# Direct AAR Usage - The Right Way

## How It Works

We're using **dependency substitution** to replace the published PadKit with your AAR files.

### The Setup

1. **`lemuroid-touchinput`** - Uses published dependency:
   ```kotlin
   api(deps.libs.padkit)  // Version 1.0.0-beta1
   ```
   This compiles fine because it's using a normal Maven dependency.

2. **`lemuroid-app`** - Substitutes with AAR:
   ```kotlin
   configurations.all {
       resolutionStrategy {
           dependencySubstitution {
               substitute(module("io.github.swordfish90:padkit"))
                   .using(files("$rootDir/libs/lib-release.aar"))
           }
       }
   }
   ```
   When building the final APK, Gradle replaces the published PadKit with your AAR!

### Why This Works

- ✅ Library modules compile fine (using published dependency)
- ✅ Final APK uses your modified AAR
- ✅ No "AAR in AAR" errors
- ✅ AAR files stay in `libs/` directory
- ✅ Works on GitHub Actions (AAR files committed to repo)

### File Structure

```
hi/
├── libs/
│   ├── lib-release.aar  ← Your modified PadKit (used)
│   └── lib-debug.aar    ← Debug version
├── lemuroid-touchinput/
│   └── build.gradle.kts  → Uses: deps.libs.padkit
└── lemuroid-app/
    └── build.gradle.kts  → Substitutes with AAR!
```

## Result

✅ Both Debug and Release builds use your AAR files  
✅ GitHub Actions will use your committed AAR files  
✅ No Maven publishing needed  
✅ Simple and direct!
