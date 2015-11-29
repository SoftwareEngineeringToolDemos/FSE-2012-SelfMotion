#Install Java
echo "Installing Java"
wget --no-check-certificate https://github.com/aglover/ubuntu-equip/raw/master/equip_java8.sh
sudo bash equip_java8.sh
rm equip_java8.sh

#Download ShopReview code
echo "Downloading ShopReview code"
wget --no-check-certificate 'https://docs.google.com/uc?export=download&id=0Bwnh9aHCFlSxTUc2OVhQZjFrN3c' -O ShopReview.zip
unzip ShopReview.zip
chmod -R 777 ShopReview
mv ShopReview Desktop
rm ShopReview.zip

#Install Eclipse 
echo "Installing eclipse"
wget --no-check-certificate 'https://www.dropbox.com/s/dd627d3ujxg70b1/eclipse.tar.gz?dl=1' -O eclipse.tar.gz
tar -xzvf eclipse.tar.gz
mv eclipse Desktop
rm eclipse.tar.gz

#Download android-sdks
echo "Downloading android-sdks"
wget --no-check-certificate 'https://www.dropbox.com/s/b07oiozy2hkv038/android-sdks.tar.gz?dl=1' -O android-sdks.tar.gz
echo "Extract android-sdks"
tar -xzvf android-sdks.tar.gz
rm android-sdks.tar.gz

#Make Eclipse shortcut
echo "Making an eclipse shortcut"
#sudo touch ~/Desktop/Eclipse.desktop
echo -e "[Desktop Entry]\nType=Application\nTerminal=false\nName=Eclipse\nIcon=Desktop/eclipse/icon.xpm\nExec=Desktop/eclipse/eclipse\nX-GNOME-Autostart-enabled=true" | sudo tee Desktop/Eclipse.desktop
sudo chmod +x Desktop/Eclipse.desktop

#Make Eclipse autostart
mkdir .config/autostart/
echo "Making eclipse start on boot"
echo -e "[Desktop Entry]\nType=Application\nTerminal=false\nName=Eclipse\nIcon=Desktop/eclipse/icon.xpm\nExec=Desktop/eclipse/eclipse\nX-GNOME-Autostart-enabled=true" | sudo tee .config/autostart/Eclipse.desktop
sudo chmod +x .config/autostart/Eclipse.desktop

#Set launcher icons
gsettings set com.canonical.Unity.Launcher favorites "['unity://running-apps','application://nautilus.desktop','unity://expo-icon','unity://devices']"

#Download workspace
echo "Downloading workspace"
wget --no-check-certificate 'https://www.dropbox.com/s/0ao8gsqt4mzayak/workspace.tar.gz?dl=1' -O workspace.tar.gz
echo "Extract workspace"
tar -xzvf workspace.tar.gz
rm workspace.tar.gz

#Make the emulator run on 32bit
echo -e "export ANDROID_EMULATOR_FORCE_32BIT=true" | sudo tee .profile -a

#Download emulator
echo "Downloading emulator"
wget --no-check-certificate 'https://docs.google.com/uc?export=download&id=0Bwnh9aHCFlSxempGSmxMbEtJOUE' -O .android.zip
unzip -o .android.zip
rm .android.zip

#Make emulator autostart
echo "Making emulator start on boot"
echo -e "[Desktop Entry]\nType=Application\nTerminal=false\nName=emulator\nExec=android-sdks/tools/emulator -avd AndroidDevice\nX-GNOME-Autostart-enabled=true" | sudo tee .config/autostart/emulator.desktop
sudo chmod +x .config/autostart/emulator.desktop

#Add Readme.txt
echo "Downloading Readme.txt"
wget https://github.com/SoftwareEngineeringToolDemos/FSE-2012-SelfMotion/raw/master/build-vm/Readme.txt
echo "Moving Readme.txt to Desktop"
mv Readme.txt Desktop

#Add Instructions.txt
echo "Downloading Insructions.txt"
wget https://github.com/SoftwareEngineeringToolDemos/FSE-2012-SelfMotion/raw/master/build-vm/Instructions.txt
echo "Moving Instructions.txt to Desktop"
mv Instructions.txt Desktop

#Add License.txt
echo "Downloading License.txt"
wget https://github.com/SoftwareEngineeringToolDemos/FSE-2012-SelfMotion/raw/master/build-vm/License.txt
echo "Moving License.txt to Desktop"
mv License.txt Desktop

#Add Youtube Link - https://youtu.be/cVsFrIDQukY
echo "Creating a youtube link"
#sudo touch Desktop/SelfMotionDemo.desktop
echo -e "[Desktop Entry]\nEncoding=UTF-8\nName=Link to SelfMotion Demo\nType=Link\nURL=https://youtu.be/U8GaODpFvB8\nIcon=text-html" | sudo tee Desktop/SelfMotionDemo.desktop

#Shutdown
sudo shutdown -h now
