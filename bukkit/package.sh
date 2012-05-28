#!/bin/sh
rm -rf net nuclearcontrol
mkdir -p net/minecraft/server
mkdir -p nuclearcontrol/
mv mod_IC2NuclearControl.class net/minecraft/server
mv *.class nuclearcontrol/
zip -r mod_IC2NuclearControl-1.1.6-mcpc1.2.5-r3.zip net/ nuclearcontrol
