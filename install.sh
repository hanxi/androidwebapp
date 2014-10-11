rm -rf assets/*
cp -rf ~/Documents/2048-AI/* assets/
#cp -rf ~/Documents/InverterChinese/* assets/
#ant debug && adb uninstall com.hanxi.inverter && adb install bin/inverter-debug.apk
ant release && adb uninstall com.hanxi.webapp.auto2048 && adb install bin/androidwebapp-release.apk
