#!/bin/bash
# Publish modified PadKit to GitHub Packages

set -e

OWNER="YOUR_GITHUB_USERNAME"  # Change this to your GitHub username
REPO="hi"                     # Your repository name
VERSION="1.0.0-custom"        # Custom version for your modified PadKit

echo "=========================================="
echo "Publishing Modified PadKit to GitHub Packages"
echo "=========================================="
echo ""
echo "Repository: $OWNER/$REPO"
echo "Version: $VERSION"
echo ""

# Navigate to PadKit directory
cd /home/sami/Documents/GitHub/PadKit

# Build the library
echo "Building PadKit..."
./gradlew :lib:assembleRelease

# Publish to GitHub Packages
echo "Publishing to GitHub Packages..."
echo "You'll need a GitHub Personal Access Token with 'write:packages' permission"
echo ""
read -p "Enter your GitHub username: " GH_USERNAME
read -sp "Enter your GitHub Personal Access Token: " GH_TOKEN
echo ""

./gradlew publish \
  -Pversion=$VERSION \
  -PgithubUsername=$GH_USERNAME \
  -PgithubToken=$GH_TOKEN

echo ""
echo "=========================================="
echo "✅ Published!"
echo "=========================================="
echo ""
echo "Now update Lemuroid to use your custom version:"
echo "1. Edit buildSrc/src/main/java/deps.kt"
echo "2. Change: const val padkit = \"$VERSION\""
echo "3. Add GitHub Packages repository to build.gradle.kts"
echo ""
