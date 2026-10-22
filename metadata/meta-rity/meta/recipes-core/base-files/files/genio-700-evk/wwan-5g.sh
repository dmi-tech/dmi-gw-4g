#!/bin/sh
# Control script for WWAN-5G M.2 module on board.

# define GPIO number for pin RESET and FULL_CARD_POWER_OFF
RESET=113
FULL_CARD_POWER_OFF=71

start()
{
    # Wait 50ms after VCC and USB hub is ready
    sleep 0.05
    # RESET pull high
    gpioset 0 $RESET=1 
    # Wait 100ms
    sleep 0.1
    # FULL_CARD_POWER_OFF pull high
    gpioset 0 $FULL_CARD_POWER_OFF=1 
}

stop()
{
    # Wait 100ms after USB hub is removed
    sleep 0.1
    # FULL_CARD_POWER_OFF pull low
    gpioset 0 $FULL_CARD_POWER_OFF=0 
    # Wait 3s
    sleep 3
    # RESET pull low
    gpioset 0 $RESET=0 
}

restart()
{
    # FULL_CARD_POWER_OFF pull low
    gpioset 0 $FULL_CARD_POWER_OFF=0 
    # Wait 50ms
    sleep 0.05
    # RESET pull low
    gpioset 0 $RESET=0 
    # Wait 400ms
    sleep 0.4
    # RESET pull high
    gpioset 0 $RESET=1 
    # Wait 100ms
    sleep 0.1
    # FULL_CARD_POWER_OFF pull high
    gpioset 0 $FULL_CARD_POWER_OFF=1 
}

if [ "$1" = "--start" ]; then
    start
elif [ "$1" = "--stop" ]; then
    stop
elif [ "$1" = "--restart" ]; then
    restart
fi
