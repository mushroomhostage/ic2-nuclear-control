#!/bin/sh
rm -rf net nuclearcontrol
mkdir -p net/minecraft/server
mkdir -p nuclearcontrol/
mkdir -p nuclearcontrol/panel
cp panel/*.class nuclearcontrol/panel/
mv mod_IC2NuclearControl.class net/minecraft/server
mv *.class nuclearcontrol/
zip -r mod_IC2NuclearControl-1.1.11-mcpc-r1.zip net/ nuclearcontrol/
