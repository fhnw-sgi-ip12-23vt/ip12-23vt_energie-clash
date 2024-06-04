#!/bin/bash
runs=10000
error_count=0
for ((i = 1 ; i <= $runs; i++));
do
	echo "Loop no. $i"
	i2cset -y 1 0x20 0
	i2cset -y 1 0x21 0
	i2cset -y 1 0x22 0
	i2cset -y 1 0x23 0
	if [ $? -ne 0 ]; then
		((error_count++))
	fi
	sleep 0.01
	i2cset -y 1 0x20 255
	i2cset -y 1 0x21 255
	i2cset -y 1 0x22 255
	i2cset -y 1 0x23 255
	if [ $? -ne 0 ]; then
		((error_count++))
	fi
	sleep 0.01
done
echo "Write failed $error_count out of $(($runs * 2))"