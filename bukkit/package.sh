#!/bin/sh
rm -rf net nuclearcontrol
mkdir -p net/minecraft/server
mkdir -p nuclearcontrol/
mv mod_IC2NuclearControl.class net/minecraft/server
mv *.class nuclearcontrol/
zip -r mod_IC2NuclearControl-1.1.7-mcpc1.2.5-r1.zip net/ nuclearcontrol
