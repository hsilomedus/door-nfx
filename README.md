door-nfx
========
Door lock node JavaFX8 software running on RaspberryPi with a PN532 NFC Module

========
Hardware
--------
Adafruit PiTFT: https://learn.adafruit.com/adafruit-pitft-28-inch-resistive-touchscreen-display-raspberry-pi/overview

Processed ITEAD PN532 NFC Module: http://hsilomedus.me/index.php/itead-pn532-nfc-module-and-raspberrypi-via-i2c-and-java/
OS Image
--------
Used OS image: http://adafruit-download.s3.amazonaws.com/PiTFT28R_raspbian140909_2014_09_18.zip

described here: https://learn.adafruit.com/adafruit-pitft-28-inch-resistive-touchscreen-display-raspberry-pi/easy-install

========
Post changes:
-------------
Installed fbcp:
```
sudo apt-get install cmake
git clone https://github.com/tasanakorn/rpi-fbcp
cd rpi-fbcp/
mkdir build
cd build/
cmake ..
make
sudo install fbcp /usr/local/bin/fbcp
```

In /boot/cmdline.txt: removed fbcon map and fbcon font

In /boot/config.txt:
```
disable_overscan=1
framebuffer_width=480
framebuffer_height=640
 #set specific CVT mode
hdmi_cvt 480 640 60 1 0 0 0
 #set CVT as default
hdmi_group=2
hdmi_mode=87
hdmi_force_hotplug=1
```

In /etc/modprobe.d/adafruit.conf
```
rotate=0
```

In /etc/modules, add:
```
i2c-bcm2708
i2c-dev
```

in /etc/modprobe.d/raspi-blacklist.conf: comment out i2c

Usage
-----
1. Git clone the repository
2. chmod +x build.sh run.sh
3. ./build.sh
4. ./run.sh