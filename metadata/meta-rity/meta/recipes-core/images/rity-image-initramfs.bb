# Simple initramfs image. Mostly used for live images.
DESCRIPTION = "Small image capable of booting a device. The kernel includes \
the Minimal RAM-based Initial Root Filesystem (initramfs), which finds the \
first 'init' program more efficiently."

INITRAMFS_SCRIPTS ?= "\
                      initramfs-framework-base \
                      initramfs-module-debug \
                      initramfs-module-simple-rootfs \
                      ${@bb.utils.contains('DISTRO_FEATURES', 'modimg', 'initramfs-module-modpart', '', d)} \
                     "

PACKAGE_INSTALL = "${INITRAMFS_SCRIPTS} ${VIRTUAL-RUNTIME_base-utils} base-passwd ${ROOTFS_BOOTSTRAP_INSTALL}"

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = ""

# Do not pullin any extra dependency
EXTRA_IMAGEDEPENDS = ""

export IMAGE_BASENAME = "${MLPREFIX}rity-image-initramfs"
IMAGE_NAME_SUFFIX ?= ""
IMAGE_LINGUAS = ""

LICENSE = "MIT"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
inherit core-image

# For cpio tarball, the value is only for reference. The size
# of resulting image may vary, depending on packages installed.
IMAGE_ROOTFS_SIZE = "8192"
IMAGE_ROOTFS_EXTRA_SPACE = "0"

# Use the same restriction as initramfs-module-install
COMPATIBLE_HOST = '(x86_64.*|i.86.*|arm.*|aarch64.*)-(linux.*|freebsd.*)'
