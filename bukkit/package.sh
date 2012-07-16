#!/bin/sh
rm -rf net nuclearcontrol
mkdir -p net/minecraft/server
mkdir -p nuclearcontrol/
mv mod_IC2NuclearControl.class net/minecraft/server
# TODO: other classes
mv *.class nuclearcontrol/
zip -r mod_IC2NuclearControl-1.1.10-mcpc-r1.zip net/ nuclearcontrol
