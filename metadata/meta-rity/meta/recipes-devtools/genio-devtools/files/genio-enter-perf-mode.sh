#!/usr/bin/env bash

echo "Script to setup CPU & GPU performance mode for Genio EVK running IoT Yocto"
echo "CAUTION: THIS DISABLES idle and throttling. It is possible to damage your hardware."
echo "These settings gets cleared after system reboot"
echo "See https://mediatek.gitlab.io/aiot/doc/aiot-dev-guide/master/sw/yocto/app-dev/gpu/gpu-common.html#performance-mode"

# Exit when error
set -e

# Set GPU to maximum frequency
# Find the MALI gpu node name (1300000.mali, 1300000.gpu, 13040000.mali, and so on)
MALI_DEVFREQ_DIR=`find /sys/class/misc/mali0/device/devfreq/ -iname "*.mali" -o -iname "*.gpu"`
echo "(Before) GPU Frequency = " `cat ${MALI_DEVFREQ_DIR}/cur_freq`
echo "Set GPU scaling governor from " `cat ${MALI_DEVFREQ_DIR}/governor` " to performance"
echo performance > ${MALI_DEVFREQ_DIR}/governor
echo "(After) GPU Frequency = " `cat ${MALI_DEVFREQ_DIR}/cur_freq`

# Set CPU frequency governor
for gov in /sys/devices/system/cpu/cpufreq/policy*/scaling_governor ; do
	echo "Set performance to " ${gov}
	echo performance > ${gov}
done

for idle_disable in  /sys/devices/system/cpu/cpu*/cpuidle/state*/disable ; do
	echo "Disable CPU idle: " ${idle_disable}
	echo 1 > ${idle_disable}
done

# Disable thermal
echo "Disable thermal zone /sys/class/thermal/thermal_zone0/mode"
echo disabled > /sys/class/thermal/thermal_zone0/mode
