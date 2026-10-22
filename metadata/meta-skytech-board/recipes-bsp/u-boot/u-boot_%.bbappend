FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI:append = " file://0001-genio-350-evk-fix-memory-to-2GiB.patch"
UBOOT_CONFIG_FRAGMENTS += "file://0002-bootdelay.cfg file://0003-console.cfg"

# Rity install/deploy share boot.script.bin and mkimage's temporary files.
# Generate the script during install before deploy can read or regenerate it.
addtask deploy after do_install
