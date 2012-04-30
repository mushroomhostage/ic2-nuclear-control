#!/bin/sh
rm -rf net nuclearcontrol
mkdir -p net/minecraft/server
mkdir -p nuclearcontrol/
mv mod_IC2NuclearControl.class net/minecraft/server
mv *.class nuclearcontrol/
zip -r IC2NuclearControlv112-mcpc125-r1.zip net/ nuclearcontrol
