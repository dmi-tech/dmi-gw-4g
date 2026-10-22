SUMMARY = "Custom 16GB eMMC image for Genio 350-EVK"
DESCRIPTION = "Custom image with USB Gadget ADB, SSH, and full development tools"
LICENSE = "MIT"

inherit core-image
inherit extrausers

# Base rootfs structure
IMAGE_INSTALL = " \
    packagegroup-core-boot \
    packagegroup-base-extended \
    ${CORE_IMAGE_EXTRA_INSTALL} \
"

# =================== ESSENTIAL SYSTEM TOOLS ===================
IMAGE_INSTALL:append = " \
    packagegroup-skytech-genio-350-connectivity \
    packagegroup-rity-display \
    libdrm-tests wayland-utils \
    busybox \
    util-linux \
    e2fsprogs e2fsprogs-resize2fs \
    dosfstools parted \
    coreutils findutils grep sed tar gzip bzip2 xz zip unzip \
    shadow sudo \
    bash bash-completion \
"

# =================== NETWORK & SSH ===================
IMAGE_FEATURES += "ssh-server-openssh debug-tweaks"
IMAGE_INSTALL:append = " \
    iproute2 iptables iputils ethtool \
    curl wget \
    tcpdump nmap \
    util-linux-blkid \
    gstreamer1.0-plugins-base-apps \
"

# cần ifconfig/route:
IMAGE_INSTALL:append = " net-tools "

# =================== USB GADGET ADB ===================
IMAGE_INSTALL:append = " \
    usb-gadget-adb \
    android-tools-adbd \
    android-tools \
    kmod \
    ${@bb.utils.contains('DISTRO_FEATURES', 'usbhost', 'usbutils', '', d)} \
"

# ===== FILESYSTEM & STORAGE =====
IMAGE_INSTALL:append = " \
    mtd-utils mtd-utils-ubifs \
"

# =================== HARDWARE INTERFACE TOOLS ===================
IMAGE_INSTALL:append = " \
    i2c-tools spidev-test can-utils \
    libgpiod-tools \
    pciutils \
"

# =================== DEBUGGING & DIAGNOSTICS ===================
IMAGE_INSTALL:append = " \
    strace ltrace gdb perf htop \
    procps psmisc \
    minicom picocom lrzsz screen \
"

# =================== DEV ENVIRONMENT & PYTHON ===================
IMAGE_INSTALL:append = " \
    git cmake make pkgconfig \
    python3 python3-pip python3-setuptools python3-wheel jq \
"

# =================== IMAGE SETTINGS ===================
IMAGE_ROOTFS_EXTRA_SPACE = "4096"
IMAGE_FSTYPES += " wic.img tar.xz"

# Optional read-only or systemd tweaks
IMAGE_FEATURES:append = " ${@bb.utils.contains('ENABLE_ROOTFS_CHECK', 'yes', 'read-only-rootfs', '' ,d)}"
