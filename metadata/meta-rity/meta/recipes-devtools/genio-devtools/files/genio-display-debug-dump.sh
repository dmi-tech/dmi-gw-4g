#!/bin/sh

echo "Genio Display Debug Dump Script"
set -x

# TODO: request user to enable DRM debug log with:
# echo 0x157 > /sys/module/drm/parameters/debug

cat /etc/build
uname -a
journalctl -t weston -t kernel -p debug -o short-monotonic --no-pager
systemctl stop weston
modetest -M mediatek
zcat /proc/config.gz | grep -i -e DRM -e vdo -e MTK -e mediatek
