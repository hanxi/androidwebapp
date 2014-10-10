#cp -rf ~/Downloads/2048-minimax/* assets/
cp -rf ~/Documents/InverterChinese/* assets/
#ant debug && adb uninstall com.hanxi.inverter && adb install bin/inverter-debug.apk
ant release && adb uninstall com.hanxi.inverter && adb install bin/inverter-release.apk
