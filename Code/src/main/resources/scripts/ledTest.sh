#!/bin/bash
runs=10
sleepDuration=.25
sleepBetweenBoards=1.0
for ((i = 0 ; i <= $runs; i++));
do
    echo "Loop no. $i"
  	i2cset -y 1 0x20 255
  	sleep $sleepDuration
  	i2cset -y 1 0x20 254
  	sleep $sleepDuration
  	i2cset -y 1 0x20 253
  	sleep $sleepDuration
  	i2cset -y 1 0x20 251
  	sleep $sleepDuration
  	i2cset -y 1 0x20 247
  	sleep $sleepDuration
  	i2cset -y 1 0x20 239
  	sleep $sleepDuration
  	i2cset -y 1 0x20 223
  	sleep $sleepDuration
  	i2cset -y 1 0x20 191
  	sleep $sleepDuration
  	i2cset -y 1 0x20 127

    sleep $sleepBetweenBoards

  	i2cset -y 1 0x21 255
  	sleep $sleepDuration
  	i2cset -y 1 0x21 254
  	sleep $sleepDuration
  	i2cset -y 1 0x21 253
  	sleep $sleepDuration
  	i2cset -y 1 0x21 251
  	sleep $sleepDuration
  	i2cset -y 1 0x21 247
  	sleep $sleepDuration
  	i2cset -y 1 0x21 239
  	sleep $sleepDuration
  	i2cset -y 1 0x21 223
  	sleep $sleepDuration
  	i2cset -y 1 0x21 191
  	sleep $sleepDuration
  	i2cset -y 1 0x21 127


    sleep $sleepBetweenBoards

  	i2cset -y 1 0x22 255
  	sleep $sleepDuration
  	i2cset -y 1 0x22 254
  	sleep $sleepDuration
  	i2cset -y 1 0x22 253
  	sleep $sleepDuration
  	i2cset -y 1 0x22 251
  	sleep $sleepDuration
  	i2cset -y 1 0x22 247
  	sleep $sleepDuration
  	i2cset -y 1 0x22 239
  	sleep $sleepDuration
  	i2cset -y 1 0x22 223
  	sleep $sleepDuration
  	i2cset -y 1 0x22 191
  	sleep $sleepDuration
  	i2cset -y 1 0x22 127

    sleep $sleepBetweenBoards

  	i2cset -y 1 0x23 255
  	sleep $sleepDuration
  	i2cset -y 1 0x23 254
  	sleep $sleepDuration
  	i2cset -y 1 0x23 253
  	sleep $sleepDuration
  	i2cset -y 1 0x23 251
  	sleep $sleepDuration
  	i2cset -y 1 0x23 247
  	sleep $sleepDuration
  	i2cset -y 1 0x23 239
  	sleep $sleepDuration
  	i2cset -y 1 0x23 223
  	sleep $sleepDuration
  	i2cset -y 1 0x23 191
  	sleep $sleepDuration
  	i2cset -y 1 0x23 127

    sleep $sleepBetweenBoards
done