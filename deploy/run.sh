#!/bin/bash
./runCrosser.sh -f $1 -o $2 && ./runR.RScript $2
