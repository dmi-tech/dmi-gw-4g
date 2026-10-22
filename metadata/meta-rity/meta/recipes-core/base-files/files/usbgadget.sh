#! /bin/sh

USB_GADGET_DEV=/dev/usb-ffs
USB_GADGET_G1=/sys/kernel/config/usb_gadget/g1
USB_GADGET_G1_CONF=$USB_GADGET_G1/configs/c.1
USB_GADGET_G1_FUNC=$USB_GADGET_G1/functions
USB_GADGET_ADB=adb
USB_GADGET_RNDIS=rndis
USB_GADGET_BCDUSB=0x0200
USB_GADGET_BCDDEV=0x0233

# The VID 0x0e8d is MediaTek.
#
# Note:
# - The PID MUST match its functions,
#   if you add a new USB gadget function implementation
#   without changing
#   the USB PID, host pc drivers may behave incorrectly.
#
#   For example, if you choose to remove the ADB(MI_02)
#   gadget function but keep the RNDIS function,
#   you should change the PID from
#   0x2005 (RNDIS+ADB) to 0x2004(RNDIS).
#
# - Changing the PID itself does not enable USB gadget
#   functions magically. You need to implement the USB
#   gadget function first, and then change the PID accordingly
#
# - The PID anv VID are solely provided by MediaTek for engineering
#   development & evaluation purposes. You should apply your own
#   PID and VID as required by USB-IF (https://usb.org)
#
# Known registered MediaTek-only PID & its mapped functions,
# including products that are not developed by IoT Yocto.
#
# 0x2002    UMS (Mass storage)
# 0x2003    UMS (MI_00) + ADB (MI_01)
# 0x2004    RNDIS
# 0x2005    RNDIS (MI_00) + ADB (MI_02)
# 0x2006    UMS (MI_00) + ADB (MI_01) + ACM (MI_02)
# 0x2007    ACM
# 0x2008    MTP
# 0x2009    MTP (MI_00) + ADB (MI_01)
# 0x200A    MTP (MI_00) + ADB (MI_01) + ACM (MI_02)
# 0x200B    PTP
# 0x200C    PTP (MI_00) + ADB (MI_01)
# 0x200D    PTP (MI_00) + ADB (MI_01) + ACM (MI_02)
# 0x200E    ADB (MI_00) + ACM (MI_01)
# 0x200F    UMS (MI_00) + ACM (MI_01)
# 0x2010    RNDIS (MI_00) + ADB (MI_02) + ACM (MI_03)
# 0x2011    RNDIS (MI_00) + ACM (MI_02)
# 0x2012    MTP (MI_00) + ACM (MI_01)
# 0x2013    PTP (MI_00) + ACM (MI_01)
# 0x2014    PTP (MI_00) + UMS (MI_01) + ADB (MI_02)
# 0x2015    PTP (MI_00) + UMS (MI_01)
# 0x2016    MTP (MI_00) + UMS (MI_01)
# 0x2017    MTP (MI_00) + UMS (MI_01) + ADB (MI_02)
# 0x2018    MTP (MI_00) + UMS (MI_01) + ACM (MI_02)
# 0x2019    MTP (MI_00) + UMS (MI_01) + ADB (MI_02) + ACM (MI_03)
# 0x201A    PTP (MI_00) + UMS (MI_01) + ACM (MI_02)
# 0x201B    PTP (MI_00) + UMS (MI_01) + ADB (MI_02) + ACM (MI_03)
USB_GADGET_VID=0x0e8d
USB_GADGET_PID=0x2005
USB_GADGET_DEFAULT_SN=0123456789
USB_GADGET_MANUFACT="Mediatek Inc."
USB_GADGET_PRODUCT="Genio"
FILE=/etc/usbgadget.conf

create_gadget()
{
    modprobe libcomposite
    modprobe usb_f_rndis
    modprobe g_ffs

    mkdir -p $USB_GADGET_G1
    mkdir -p $USB_GADGET_G1/strings/0x409
    mkdir $USB_GADGET_G1_FUNC/rndis.usb0
    mkdir $USB_GADGET_G1_FUNC/ffs.adb

    mkdir -p $USB_GADGET_G1/strings/0x409
    mkdir -p $USB_GADGET_G1_CONF/strings/0x409

    mkdir $USB_GADGET_DEV -m 770
    mkdir $USB_GADGET_DEV/adb -m 770
}

set_gadget_sn()
{
    # Priority:
    #  1. Use `aiot-flash --serialno` first
    #  2. Load hardware id from optee-ewriter if possible
    #  3. Use static default

    # load from uboot env
    local uboot_serial=`fw_printenv -n serial#`

    # Grab 8-byte hardware identifier with the "ewriter" utility.
    # this requires OP-TEE and optee-ewriter package.
    # The offset 12 and 13 are applicable to Genio SoCs only.
    local optee_hwid=`for i in {12..13}; do ewriter 0 $i 4 | head -n 2 | tail -n 1 | head -c -1; done`

    if [ -n "$uboot_serial" ]
    then
        echo "$uboot_serial" > strings/0x409/serialnumber
        return 0
    fi

    if [ -n "$optee_hwid" ]
    then
        echo "$optee_hwid" > strings/0x409/serialnumber
        return 0
    fi

    echo "$USB_GADGET_DEFAULT_SN" > strings/0x409/serialnumber
}

set_gadget()
{
    cd $USB_GADGET_G1

    echo "$USB_GADGET_BCDUSB" > bcdUSB
    echo "$USB_GADGET_BCDDEV" > bcdDevice
    echo "$USB_GADGET_VID" > idVendor
    echo "$USB_GADGET_PID" > idProduct

    echo "$USB_GADGET_MANUFACT" > strings/0x409/manufacturer
    echo "$USB_GADGET_PRODUCT" > strings/0x409/product

    set_gadget_sn

    echo 1 > os_desc/use
    echo "MSFT100" > os_desc/qw_sign

    echo $USB_GADGET_RNDIS+$USB_GADGET_ADB > configs/c.1/strings/0x409/configuration

    ln -s functions/rndis.usb0 configs/c.1
    ln -s functions/ffs.adb configs/c.1

    mount -o uid=2000,gid=2000 -t functionfs adb /dev/usb-ffs/adb
    cd -
}

start()
{
    if [ ! -f "$USB_GADGET_G1/UDC" ]
    then
        create_gadget
    fi

    set_gadget

    start-stop-daemon --start --background --oknodo --quiet --exec /usr/bin/adbd
    sleep 1

    if [ -f "$FILE" ]; then
        cat $FILE > $USB_GADGET_G1/UDC
    else
        echo `ls -1 /sys/class/udc | head -1 | tr -d '\r\n|\n\r|\n|\r'` > $USB_GADGET_G1/UDC
    fi

    nmcli connection up usb0
    start-stop-daemon --start --pidfile /run/udhcpd.pid --exec /usr/sbin/udhcpd -- /etc/udhcpd.conf
}

stop()
{
    echo "" > $USB_GADGET_G1/UDC
    nmcli connection down usb0
    start-stop-daemon --stop --pidfile /run/udhcpd.pid --exec /usr/sbin/udhcpd -- /etc/udhcpd.conf
    start-stop-daemon --stop --oknodo --quiet --exec /usr/bin/adbd

    rm $USB_GADGET_G1_CONF/rndis.usb0
    rm $USB_GADGET_G1_CONF/ffs.adb
    umount /dev/usb-ffs/adb
}


if [ "$1" = "--start" ]; then
    start
elif [ "$1" = "--stop" ]; then
    stop
fi
