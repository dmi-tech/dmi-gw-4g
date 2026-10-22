#!/bin/sh

# Find 'rootfs' parent kernel device name such as /dev/mmcblk0
# For example
# $ lsblk -l -p -o PKNAME,NAME,LABEL | grep -i rootfs
# /dev/mmcblk0 /dev/mmcblk0p10 rootfs
ROOTFS_INFO=`lsblk -l -p -o PKNAME,NAME,LABEL | grep -i rootfs`
ROOTFS_DEV=`echo $ROOTFS_INFO | awk '{print $1}'`
ROOTFS_PARTITION_DEV=`echo $ROOTFS_INFO | awk '{print $2}'`
ROOTFS_PARTITION_INDEX=`parted $ROOTFS_DEV print | grep rootfs | awk '{print $1}'`

echo "Detected rootfs device:${ROOTFS_DEV}"

# Prepare partition resize, fix potential rootfs GPT issue
parted ---pretend-input-tty $ROOTFS_DEV print <<__END__
Fix
__END__

echo "Expand rootfs partition device $ROOTFS_PARTITION_DEV index $ROOTFS_PARTITION_INDEX"
parted $ROOTFS_DEV resizepart $ROOTFS_PARTITION_INDEX 100%
echo "Expand rootfs device $ROOTFS_PARTITION_DEV"
resize2fs $ROOTFS_PARTITION_DEV
