#!/bin/sh

#for Windows compatibility -> else scripts cannot be changed on a Windows PC and exported to the PI!
dos2unix "$0"

DISPLAY=:0 XAUTHORITY=/home/pi/.Xauthority sudo -E java -XX:+UseZGC -Xmx500M --module-path /opt/javafx-sdk/lib:/home/pi/deploy --add-modules javafx.controls -Dglass.platform=gtk --module ch.graueenergie/ch.graueenergie.energieclash.controller.GameStarter