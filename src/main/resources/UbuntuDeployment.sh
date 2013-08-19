#!/bin/bash

sudo apt-get install openjdk-7-jdk

dirname='crosser'
if [ -e /opt/$dirname ]; then
  sudo rm -rf /opt/$dirname
fi
sudo mkdir -p /opt/$dirname

archivename='crosser-0.2.1.tar.gz'
script='crosser.sh'
sudo tar xzvf $archivename
sudo mv $dirname /opt/
sudo chown -R root:root /opt/$dirname
sudo chmod +x /opt/$dirname/$script

echo 'Done.'
exit
