#!/bin/sh
set -e

G=/sys/kernel/config/usb_gadget/g1
FFS_DIR=/dev/usb-ffs/adb
UDC=$(ls /sys/class/udc 2>/dev/null | head -n1)

start() {

  modprobe libcomposite 2>/dev/null || true
  modprobe usb_f_fs 2>/dev/null || true

  [ -d /sys/kernel/config ] || mount -t configfs none /sys/kernel/config

  mkdir -p "$G"
  echo 0x18d1 > "$G/idVendor"
  echo 0x4ee7 > "$G/idProduct"
  echo 0x0200 > "$G/bcdUSB"
  echo 0x0200 > "$G/bcdDevice"

  mkdir -p "$G/strings/0x409"
  echo "1234567890" > "$G/strings/0x409/serialnumber"
  echo "Custom Genio350" > "$G/strings/0x409/manufacturer"
  echo "ADB Gadget" > "$G/strings/0x409/product"

  mkdir -p "$G/configs/c.1/strings/0x409"
  echo "Config 1: ADB" > "$G/configs/c.1/strings/0x409/configuration"
  echo 250 > "$G/configs/c.1/MaxPower"

  mkdir -p "$G/functions/ffs.adb"
  mkdir -p "$FFS_DIR"
  mountpoint -q "$FFS_DIR" || mount -t functionfs adb "$FFS_DIR"
  ln -sf "$G/functions/ffs.adb" "$G/configs/c.1/"

  # Bind UDC
  if [ -n "$UDC" ]; then
    echo "$UDC" > "$G/UDC" 2>/dev/null || true
  else
    echo "No UDC present (check mtu3 in DTB/dr_mode=peripheral)"
  fi

  if command -v adbd >/dev/null 2>&1; then
    ( adbd & ) || true
  fi
}

stop() {
  [ -d "$G" ] || exit 0
  echo "" > "$G/UDC" 2>/dev/null || true
  rm -f "$G/configs/c.1/ffs.adb" 2>/dev/null || true
  umount "$FFS_DIR" 2>/dev/null || true
  rmdir "$G/functions/ffs.adb" 2>/dev/null || true
  rmdir "$G/configs/c.1/strings/0x409" 2>/dev/null || true
  rmdir "$G/configs/c.1" 2>/dev/null || true
  rmdir "$G/strings/0x409" 2>/dev/null || true
  rmdir "$G" 2>/dev/null || true
}

case "$1" in
  start) start ;;
  stop)  stop ;;
  restart) stop; start ;;
  *) echo "usage: $0 {start|stop|restart}"; exit 1;;
esac
