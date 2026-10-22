#! /bin/sh

start()
{
    # Mount configfs if it does not already exist
    if [ ! -d /sys/kernel/config/usb_gadget ]; then
        mount none /sys/kernel/config -t configfs
    fi

    mkdir /sys/kernel/config/usb_gadget/g_mass
    cd /sys/kernel/config/usb_gadget/g_mass

    echo "0x0200" > bcdUSB
    echo "0xEF" > bDeviceClass
    echo "2" > bDeviceSubClass
    echo "1" > bDeviceProtocol
    echo "0x0e8d" > idVendor
    echo "0x0002" > idProduct
    echo "0x0233" > bcdDevice

    mkdir strings/0x409
    echo "Mediatek USB Mass Storage" > strings/0x409/product
    echo "0x123456789" > strings/0x409/serialnumber
    echo "Mediatek" > ./strings/0x409/manufacturer

    mkdir functions/mass_storage.0
    mkdir -p /var/usbmass
    dd if=/dev/zero of=/var/usbmass/lun0.img bs=1M count=128 # 128 MB
    echo /var/usbmass/lun0.img > functions/mass_storage.0/lun.0/file
    echo "Mediatek USB Disk" > functions/mass_storage.0/lun.0/inquiry_string

    mkdir configs/c.1
    echo "0x80" > configs/c.1/bmAttributes
    echo "250" > configs/c.1/MaxPower

    mkdir configs/c.1/strings/0x409
    echo "mass_storage" > configs/c.1/strings/0x409/configuration

    ln -s functions/mass_storage.0 configs/c.1
    echo "112a1000.usb" > UDC
}

stop()
{
    echo "" > /sys/kernel/config/usb_gadget/g_mass/UDC
    rm /sys/kernel/config/usb_gadget/g_mass/configs/c.1/mass_storage.0
    rmdir /sys/kernel/config/usb_gadget/g_mass/configs/c.1/strings/0x409/
    rmdir /sys/kernel/config/usb_gadget/g_mass/configs/c.1/

    rmdir /sys/kernel/config/usb_gadget/g_mass/functions/mass_storage.0/
    rmdir /sys/kernel/config/usb_gadget/g_mass/strings/0x409/
    rmdir /sys/kernel/config/usb_gadget/g_mass/
}

if [ "$1" = "--start" ]; then
    start
elif [ "$1" = "--stop" ]; then
    stop
fi