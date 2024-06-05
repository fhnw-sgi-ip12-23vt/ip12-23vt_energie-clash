#!/bin/bash


#####################################################################
# Enable Autostart
##################
# Define the folder and the file
folder_path="/lib/systemd/system"
file_path="/home/pi/deploy"
file_name="energieclash.service"


# Check if the file exists in the folder
if [ ! -f "$folder_path/$file_name" ]; then
    # File does not exist, move it to the folder
    mv "$file_path/$file_name" "$folder_path"
    if [ $? -eq 0 ]; then
        echo "File moved to $folder_path"
        else
            echo "Failed to move file to $folder_path"
        fi
else
    # File exists, delete it
    rm "$folder_path/$file_name"
    if [ $? -eq 0 ]; then
            echo "Existing file deleted from $folder_path"
    fi
    mv "$file_path/$file_name" "$folder_path"
    if [ $? -eq 0 ]; then
            echo "File moved to $folder_path"
            else
                echo "Failed to move file to $folder_path"
            fi
fi

# enables the Systemd Service
systemctl enable "$file_name"
if [ $? -eq 0 ]; then
      echo "$file_name has been enabled"
      else
          echo "Failed to enable $file_name"
      fi
###########################################################################################
# Script to execute Application:
################################
#Check if the script exists in the .config folder
if [ ! -f "/home/pi/.config/energieclash_script.sh" ]; then
    # File does not exist, move it to the folder
    mv  "/home/pi/deploy/energieclash_script.sh" "/home/pi/.config/energieclash_script.sh"
     if [ $? -eq 0 ]; then
           echo "energieclash_script.sh moved to .config folder"
           else
               echo "Failed to move energieclash_script.sh to .config folder"
           fi
else
    # File exists, delete it
    rm "/home/pi/.config/energieclash_script.sh"
  if [ $? -eq 0 ]; then
      echo "Existing file deleted from .config folder"
      else
          echo "Failed to delete existing file from .config folder"
      fi
      # move script from deploy folder to .config folder
    mv "/home/pi/deploy/energieclash_script.sh" "/home/pi/.config"
    if [ $? -eq 0 ]; then
          echo "energieclash_script.sh has been moved to .config folder"
          else
              echo "Failed to move energieclash_script.sh to .config folder"
          fi
fi

# Make script executable
sudo chmod +x "/home/pi/.config/energieclash_script.sh"
if [ $? -eq 0 ]; then
      echo "energieclash_script.sh has been made executable"
      else
          echo "Failed to make energieclash_script executable"
      fi
echo "End of installscript"

#######################################################################
# Monitor settings:
###################
#Add Option to boot/config to set autodetect Display off and overscan off
CONFIG_FILE="/boot/config.txt"
OPTIONS=("disable_overscan=1" "display_auto_detect=0")

# Function to add an option if it is not already present in the config file
add_option_if_missing() {
    local option="$1"
    if grep -q "^${option}" "$CONFIG_FILE"; then
        echo "Option ${option} is already present in ${CONFIG_FILE}."
    else
        echo "${option}" | sudo tee -a "$CONFIG_FILE" > /dev/null
        echo "Option ${option} added to ${CONFIG_FILE}."
    fi
}

# Loop through all options and add them if they are missing
for option in "${OPTIONS[@]}"; do
    add_option_if_missing "$option"
done

#####################################################################
# Mv Monitor settings .config to etc/X11/xorg.conf.d folder
###########################################################
XORG_CONF_SOURCE="/home/pi/deploy/xorg.conf"
XORG_CONF_DEST_DIR="/etc/X11/xorg.conf.d"
XORG_CONF_DEST="$XORG_CONF_DEST_DIR/xorg.conf"

# Check if the destination directory exists, if not, create it
if [ ! -d "$XORG_CONF_DEST_DIR" ]; then
    echo "Creating directory $XORG_CONF_DEST_DIR."
    sudo mkdir -p "$XORG_CONF_DEST_DIR"
fi

# Check if the xorg.conf file is already in the destination directory - Remove it
if [ -f "$XORG_CONF_DEST" ]; then
    echo "xorg.conf already exists in $XORG_CONF_DEST_DIR. Removing it."
    sudo rm "$XORG_CONF_DEST"
    echo "xorg.conf removed from $XORG_CONF_DEST_DIR."
fi
# Move xorg.conf to the destination directory
if [ -f "$XORG_CONF_SOURCE" ]; then
      echo "Moving xorg.conf to $XORG_CONF_DEST_DIR."
      sudo mv "$XORG_CONF_SOURCE" "$XORG_CONF_DEST"
      echo "xorg.conf moved to $XORG_CONF_DEST_DIR."
else
      echo "xorg.conf not found in /home/pi/deploy."
fi

####################################################################
# Add lxrandr settings to autostart folder
##########################################
#!/bin/bash

# Source file and destination directory
SOURCE_FILE="/home/pi/deploy/lxrandr-autostart.desktop"
DEST_DIR="home/pi/.config/autostart/lxrandr"

# Check if the destination directory exists, if not, create it
if [ ! -d "$DEST_DIR" ]; then
    echo "Creating directory $DEST_DIR."
    mkdir -p "$DEST_DIR"
fi

# Check if the source file exists
if [ -f "$SOURCE_FILE" ]; then
    # Move the file to the destination directory
    mv "$SOURCE_FILE" "$DEST_DIR"
    echo "Moved $SOURCE_FILE to $DEST_DIR."
else
    echo "$SOURCE_FILE not found."
fi
##################################################
echo "End of script"
