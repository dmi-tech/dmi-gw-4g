SUMMARY = "initramfs-framework module for mounting module partition"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
RDEPENDS:${PN} = "initramfs-framework-base"

PR = "r0"

inherit allarch

SRC_URI = "file://modpart"

S = "${WORKDIR}"

# The script should be run after rootfs is mounted.
do_install() {
    install -d ${D}/init.d
    install -m 0755 ${WORKDIR}/modpart ${D}/init.d/91-modpart
}

FILES:${PN} = "/init.d/91-modpart"
