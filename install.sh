cp -rf /Users/hrj/tools/pyfm/gst-python-1.2.0/tmp/inverter/* assets/
ant debug
adb uninstall com.hanxi.inverter && adb install bin/inverter-debug.apk
