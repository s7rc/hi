#!/bin/bash
# Script to publish your modified PadKit and rebuild Lemuroid

set -e  # Exit on error

echo "=========================================="
echo "Publishing Modified PadKit to Local Maven"
echo "=========================================="

# Step 1: Publish PadKit to local Maven
cd /home/sami/Documents/GitHub/PadKit
echo "📦 Building and publishing PadKit..."
./gradlew publishToMavenLocal

# Get the published version
VERSION=$(./gradlew :lib:properties -q | grep "^version:" | awk '{print $2}')
echo "✅ Published PadKit version: $VERSION"

# Step 2: Update Lemuroid to use published dependency
cd /home/sami/Documents/GitHub/hi

echo ""
echo "=========================================="
echo "Updating Lemuroid Configuration"
echo "=========================================="

# Revert lemuroid-touchinput to use published dependency
echo "📝 Updating lemuroid-touchinput/build.gradle.kts..."
sed -i 's|api(project(":padkit-lib"))|// api(project(":padkit-lib"))  // Using local Maven instead|g' lemuroid-touchinput/build.gradle.kts
sed -i 's|// api(deps.libs.padkit)|api(deps.libs.padkit)|g' lemuroid-touchinput/build.gradle.kts

echo "✅ Configuration updated"

# Step 3: Show version info
echo ""
echo "=========================================="
echo " Version Information"
echo "=========================================="
echo "PadKit version published: $VERSION"
echo "Lemuroid expects version: $(grep 'const val padkit' buildSrc/src/main/java/deps.kt | sed 's/.*"\(.*\)".*/\1/')"
echo ""
echo "ℹ️  If versions don't match, update buildSrc/src/main/java/deps.kt line 29"

# Step 4: Clean and rebuild Lemuroid
echo ""
echo "=========================================="
echo "Building Lemuroid with Modified PadKit"
echo "=========================================="
echo "🧹 Cleaning previous builds..."
./gradlew clean

echo "🔨 Building Lemuroid..."
./gradlew :lemuroid-app:assembleDebug

echo ""
echo "=========================================="
echo "✅ BUILD COMPLETE!"
echo "=========================================="
echo ""
echo "APK location:"
echo "  lemuroid-app/build/outputs/apk/debug/lemuroid-app-debug.apk"
echo ""
echo "To install on device:"
echo "  ./gradlew :lemuroid-app:installDebug"
echo ""
