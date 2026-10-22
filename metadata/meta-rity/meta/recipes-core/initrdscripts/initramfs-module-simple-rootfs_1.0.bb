SUMMARY = "initramfs-framework module for mounting rootfs in simplified way"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
RDEPENDS:${PN} = "initramfs-framework-base"

PR = "r0"

inherit allarch

#FILESEXTRAPATHS:prepend := "${THISDIR}/initramfs-framework:"
SRC_URI = "file://simple-rootfs"

S = "${WORKDIR}"

# The script should be run before 90-rootfs to overwrite the default
# behavior.
do_install() {
    install -d ${D}/init.d
    install -m 0755 ${WORKDIR}/simple-rootfs ${D}/init.d/86-simplerootfs
}

FILES:${PN} = "/init.d/86-simplerootfs"
