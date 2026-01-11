#!/bin/bash
# Script to commit and push the modified PadKit changes to GitHub

set -e

cd /home/sami/Documents/GitHub/hi

echo "=========================================="
echo "Preparing to Commit Modified PadKit"
echo "=========================================="
echo ""

# Check git status
echo "📋 Current git status:"
git status

echo ""
echo "=========================================="
echo "Files to be committed:"
echo "=========================================="
echo "✓ libs/lib-release.aar"
echo "✓ libs/lib-debug.aar"
echo "✓ libs/README.md"
echo "✓ libs/.gitignore"
echo "✓ lemuroid-touchinput/build.gradle.kts"
echo "✓ settings.gradle.kts"
echo ""

# Stage the files
echo "📦 Staging files..."
git add libs/*.aar
git add libs/README.md
git add libs/.gitignore
git add lemuroid-touchinput/build.gradle.kts
git add settings.gradle.kts

echo "✅ Files staged"
echo ""

# Show what will be committed
echo "=========================================="
echo "Changes to be committed:"
echo "=========================================="
git status --short

echo ""
echo "=========================================="
echo "Commit & Push Options:"
echo "=========================================="
echo ""
echo "Option 1 - Use prepared commit message:"
echo "  git commit -F COMMIT_MESSAGE.txt"
echo ""
echo "Option 2 - Write your own commit message:"
echo "  git commit -m \"Your message here\""
echo ""
echo "Then push to GitHub:"
echo "  git push origin main"
echo "  (or replace 'main' with your branch name)"
echo ""
echo "=========================================="
echo ""

read -p "Would you like to commit now with the prepared message? (y/n) " -n 1 -r
echo ""

if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "📝 Committing with prepared message..."
    git commit -F COMMIT_MESSAGE.txt
    echo "✅ Committed!"
    echo ""
    echo "Current branch:"
    git branch --show-current
    echo ""
    read -p "Push to GitHub now? (y/n) " -n 1 -r
    echo ""
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        BRANCH=$(git branch --show-current)
        echo "🚀 Pushing to origin/$BRANCH..."
        git push origin "$BRANCH"
        echo ""
        echo "=========================================="
        echo "✅ PUSHED TO GITHUB!"
        echo "=========================================="
        echo ""
        echo "GitHub Actions will now build your app with the modified PadKit."
        echo "Check the Actions tab in your GitHub repository to see the build progress."
    else
        echo "Skipped push. You can push manually with:"
        echo "  git push origin $(git branch --show-current)"
    fi
else
    echo "Skipped commit. Files are staged and ready."
    echo "Commit manually with:"
    echo "  git commit -m \"Your message\""
fi

echo ""
