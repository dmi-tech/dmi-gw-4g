#!/bin/sh
# Control script for WWAN-5G M.2 module on board.
# Note: genio-1200-evk uses 2 transisters to control the module
#       instead of using direct pin control as genio-700-evk.

# define GPIO number for pin RESET, FULL_CARD_POWER_OFF, DPR
# DPR pin is used for SAR check and adjustment TX power.
RESET=53
FULL_CARD_POWER_OFF=15
DPR=14

start()
{
    # Set DPR high
    gpioset 0 $DPR=1
    # Wait 50ms after VCC and USB hub is ready
    sleep 0.05
    # RESET pull high
    gpioset 0 $RESET=0
    # Wait 100ms
    sleep 0.1
    # FULL_CARD_POWER_OFF pull high
    gpioset 0 $FULL_CARD_POWER_OFF=0
}

stop()
{
    # Set DPR low
    gpioset 0 $DPR=0
    # Wait 100ms after USB hub is removed
    sleep 0.1
    # FULL_CARD_POWER_OFF pull low
    gpioset 0 $FULL_CARD_POWER_OFF=1
    # Wait 3s
    sleep 3
    # RESET pull low
    gpioset 0 $RESET=1
}

restart()
{
    # FULL_CARD_POWER_OFF pull low
    gpioset 0 $FULL_CARD_POWER_OFF=1
    # Wait 50ms
    sleep 0.05
    # RESET pull low
    gpioset 0 $RESET=1
    # Wait 400ms
    sleep 0.4
    # RESET pull high
    gpioset 0 $RESET=0
    # Wait 100ms
    sleep 0.1
    # FULL_CARD_POWER_OFF pull high
    gpioset 0 $FULL_CARD_POWER_OFF=0
}

if [ "$1" = "--start" ]; then
    start
elif [ "$1" = "--stop" ]; then
    stop
elif [ "$1" = "--restart" ]; then
    restart
fi
