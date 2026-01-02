#!/usr/bin/env python3
"""
Lemuroid Core Updater
Downloads and installs libretro cores for Android (armeabi-v7a, arm64-v8a, x86, x86_64)
Works with both regular Python and IPython
"""

import os
import pathlib
import subprocess
import sys

# Uncomment the cores that require an update
cores = [
    #"stella",
    #"fceumm",
    #"snes9x",
    #"genesis_plus_gx",
    #"gambatte",
    #"melonds",
    #"melondsds",
    #"mgba",
    #"mupen64plus_next_gles3",
    #"pcsx_rearmed",
    #"ppsspp",
    "fbneo",
    "desmume",
    "mame2003_plus",
    "prosystem",
    "handy",
    "mednafen_pce_fast",
    "dosbox_pure",
    "mednafen_ngp",
    "mednafen_wswan",
    "citra",
    "yabasanshiro",
    "mednafen_saturn",
    "yabause",
    "flycast",
    "mednafen_pcfx",
]

archs = ["armeabi-v7a", "arm64-v8a", "x86", "x86_64"]

delivery_install = "<dist:install-time/>"

delivery_on_demand = """
<dist:on-demand />
<dist:install-time>
    <dist:conditions>
        <dist:device-feature dist:name="android.software.leanback"/>
    </dist:conditions>
</dist:install-time>
"""

manifest_content = """
<manifest xmlns:dist="http://schemas.android.com/apk/distribution"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <application
        android:hasCode="false"
        android:extractNativeLibs="true" />

    <dist:module dist:title="@string/core_name_%s">
        <dist:delivery>
            %s
        </dist:delivery>
        <dist:fusing dist:include="true" />
    </dist:module>
</manifest>
"""

gradle_content = """
plugins {
    id("com.android.dynamic-feature")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    namespace = "com.swordfish.lemuroid.core.%s"
    defaultConfig {
        missingDimensionStrategy("opensource", "play")
        missingDimensionStrategy("cores", "dynamic")
    }
    packagingOptions {
        doNotStrip("*/*/*_libretro_android.so")
    }
}

dependencies {
    implementation(project(":lemuroid-app"))
    implementation(kotlin(deps.libs.kotlin.stdlib))
}
"""

lemuroid_core_names = {
    "melondsds": "melonds"
}

def run_command(cmd, cwd=None):
    """Run shell command - works on both Windows and Unix"""
    try:
        result = subprocess.run(cmd, shell=True, cwd=cwd, check=True, 
                                capture_output=True, text=True)
        return result.returncode == 0
    except subprocess.CalledProcessError as e:
        print(f"Command failed: {cmd}")
        print(f"Error: {e.stderr}")
        return False

def write_file(chunks, content):
    pathlib.Path(os.path.join(*chunks[:-1])).mkdir(parents=True, exist_ok=True)
    with open(os.path.join(*chunks), "w") as f:
        f.write(content)

def main():
    for libretro_core_name in cores:
        lemuroid_core_name = lemuroid_core_names.get(libretro_core_name, libretro_core_name)
        
        core = libretro_core_name + "_libretro_android.so.zip"
        libretro_so_name = libretro_core_name + "_libretro_android.so"
        lemuroid_so_name = lemuroid_core_name + "_libretro_android.so"
        core_folder = "lemuroid_core_" + lemuroid_core_name

        print(f"\n{'='*60}")
        print(f"Processing core: {lemuroid_core_name}")
        print(f"{'='*60}")

        # Remove old core folder
        if os.path.exists(core_folder):
            print(f"Removing old folder: {core_folder}")
            if sys.platform == "win32":
                run_command(f'rmdir /S /Q "{core_folder}"')
            else:
                run_command(f'rm -rf "{core_folder}"')
        
        install_time = delivery_on_demand

        write_file([core_folder, "build.gradle.kts"], gradle_content % lemuroid_core_name)
        write_file([core_folder, "src", "main", "AndroidManifest.xml"], 
                   manifest_content % (lemuroid_core_name, install_time))

        for arch in archs:
            print(f"\nDownloading {arch}...")
            
            arch_folder = os.path.join(core_folder, "src", "main", "jniLibs", arch)
            pathlib.Path(arch_folder).mkdir(parents=True, exist_ok=True)
            
            zip_path = os.path.join(arch_folder, core)
            download_url = f"https://buildbot.libretro.com/nightly/android/latest/{arch}/{core}"
            
            # Download
            if sys.platform == "win32":
                # Use PowerShell on Windows
                download_cmd = f'powershell -Command "Invoke-WebRequest -Uri \'{download_url}\' -OutFile \'{zip_path}\'"'
            else:
                # Use wget on Unix
                download_cmd = f"wget {download_url} -P {arch_folder}"
            
            if not run_command(download_cmd):
                print(f"Failed to download {arch}")
                continue
            
            # Unzip
            if sys.platform == "win32":
                unzip_cmd = f'powershell -Command "Expand-Archive -Path \'{zip_path}\' -DestinationPath \'{arch_folder}\' -Force"'
            else:
                unzip_cmd = f"unzip -o {zip_path} -d {arch_folder}"
            
            run_command(unzip_cmd)
            
            # Remove zip
            if os.path.exists(zip_path):
                os.remove(zip_path)
            
            # Rename .so file
            old_so = os.path.join(arch_folder, libretro_so_name)
            new_so = os.path.join(arch_folder, f"lib{lemuroid_so_name}")
            
            if os.path.exists(old_so):
                os.rename(old_so, new_so)
            
            # Create symlink in bundled-cores
            bundled_cores_so = os.path.join("bundled-cores", "src", "main", "jniLibs", 
                                           arch, f"lib{lemuroid_so_name}")
            
            # Remove old symlink/file
            if os.path.exists(bundled_cores_so):
                os.remove(bundled_cores_so)
            
            # Create symlink
            pathlib.Path(os.path.dirname(bundled_cores_so)).mkdir(parents=True, exist_ok=True)
            rel_path = os.path.relpath(new_so, os.path.dirname(bundled_cores_so))
            
            if sys.platform == "win32":
                # On Windows, copy instead of symlink (requires admin for symlinks)
                import shutil
                shutil.copy2(new_so, bundled_cores_so)
        print(f"\n[DONE] Completed: {lemuroid_core_name}")

if __name__ == "__main__":
    print("Lemuroid Core Updater")
    print("=" * 60)
    main()
    print("\n" + "=" * 60)
    print("All cores updated successfully!")
