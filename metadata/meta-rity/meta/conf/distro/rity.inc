require conf/distro/poky.conf

DISTRO = "rity"
DISTRO_NAME = "Rity"
DISTRO_VERSION = "25.0-release"

# set WKS_FILE according to features
python() {
    if bb.utils.contains('MACHINE_FEATURES', 'efi', True, False, d):
        wks_file = "rity-efi.wks.in"
    elif bb.utils.contains('MACHINE_FEATURES', 'nor-boot', True, False, d):
        wks_file = "rity-nofw.wks.in"
    else:
        wks_file = "rity.wks.in"

    # Set the WKS_FILE variable in the BitBake environment
    d.setVar('WKS_FILE', wks_file)
}
IMAGE_HOME_SIZE = "200M"

# Boot configuration tweaks
KERNEL_CLASSES = "kernel-fitimage-mtk kernel-mod-image"
UBOOT_ENV_SUFFIX = "script"
UBOOT_ENV = "boot"
include conf/machine/include/rity-${MACHINE}.inc
