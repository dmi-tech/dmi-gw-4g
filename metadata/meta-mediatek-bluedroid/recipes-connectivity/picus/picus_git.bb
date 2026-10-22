SUMMARY = "MediaTek Bluetooth Firmware Debug Tool"
LICENSE = "CLOSED"

inherit autotools-brokensep

SRC_URI = "${AIOT_NDA_URI}/neptune/bt_others/picus.git;protocol=https;branch=main;"
SRCREV = "331a6a5b744f9356cb9ffdceee7c6f8417634a19"


S = "${WORKDIR}/git"

TARGET_CC_ARCH += "${LDFLAGS}"
INSANE_SKIP_${PN} = "ldflags"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 picus ${D}${bindir}
}
