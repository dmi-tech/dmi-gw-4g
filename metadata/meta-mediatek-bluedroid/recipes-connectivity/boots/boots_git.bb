SUMMARY = "MediaTek Bluetooth RF Certification Tool"
LICENSE = "CLOSED"

inherit autotools-brokensep

SRC_URI = "${AIOT_NDA_URI}/neptune/bt_others/boots.git;protocol=https;branch=main;"
SRCREV = "436f5c78be1a2b54cd0f60061f5c742b165474e9"


S = "${WORKDIR}/git"

TARGET_CC_ARCH += "${LDFLAGS}"
INSANE_SKIP_${PN} = "ldflags"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 boots ${D}${bindir}
    install -m 0755 boots_srv ${D}${bindir}
}