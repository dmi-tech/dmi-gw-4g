#!/bin/sh

set -eu

script_dir=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
image_dir=$(CDPATH= cd -- "${script_dir}/.." && pwd)
layer_dir=$(CDPATH= cd -- "${image_dir}/../.." && pwd)
image_recipe="${image_dir}/skytech-genio-350-image.bb"
image_append="${image_dir}/skytech-genio-350-image.bbappend"
duplicate_service="${image_dir}/files/adbd-tcp.service"
journald_append="${layer_dir}/recipes-core/systemd/systemd-conf_%.bbappend"
journald_limit="${layer_dir}/recipes-core/systemd/systemd-conf/20-journald-runtime-limit.conf"

fail() {
    echo "FAIL: $*" >&2
    exit 1
}

# The supported ADB path is the USB gadget service already supplied by the image.
grep -q '^[[:space:]]*usb-gadget-adb \\' "${image_recipe}" || \
    fail "USB gadget ADB package was removed from the image"
grep -q '^[[:space:]]*android-tools-adbd \\' "${image_recipe}" || \
    fail "adbd package was removed from the image"

# A second adbd instance must not be installed or enabled by image post-processing.
[ ! -e "${duplicate_service}" ] || fail "obsolete adbd-tcp systemd unit still exists"
if grep -q 'adbd-tcp\.service' "${image_append}"; then
    fail "image recipe still references or enables the duplicate adbd service"
fi

# Bound volatile journal memory even if another service starts logging repeatedly.
[ -f "${journald_append}" ] || fail "board-specific systemd-conf append is missing"
[ -f "${journald_limit}" ] || fail "journald runtime limit drop-in is missing"
grep -q 'file://20-journald-runtime-limit\.conf' "${journald_append}" || \
    fail "journald runtime limit is missing from SRC_URI"
grep -q 'journald\.conf\.d/20-journald-runtime-limit\.conf' "${journald_append}" || \
    fail "journald runtime limit is not installed"
grep -q '^RuntimeMaxUse=32M$' "${journald_limit}" || \
    fail "journald RuntimeMaxUse is not capped at 32M"

echo "PASS: duplicate adbd service absent and volatile journal bounded"
