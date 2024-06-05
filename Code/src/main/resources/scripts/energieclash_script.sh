#!/bin/sh

#for Windows compatibility -> else scripts cannot be changed on a Windows PC and exported to the PI!
dos2unix "$0"

chmod +x /home/pi/deploy/energieclash_autostart.sh
exec sudo /home/pi/deploy/energieclash_autostart.sh
exit 0