SUMMARY = "MediaTek Bluetooth Vendor Lib"
LICENSE = "CLOSED"

inherit autotools-brokensep

SRC_URI = "${AIOT_NDA_URI}/neptune/bt_others/vendor_lib.git;protocol=https;branch=main;"
SRCREV = "edd3c702f8d91d855c8f02cc09ffe2b6bad8012e"


S = "${WORKDIR}/git"

FILES_${PN} += "${libdir}"
FILES_${PN}-dev = ""
TARGET_CC_ARCH += "${LDFLAGS}"
INSANE_SKIP_${PN} += "ldflags"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

RPROVIDES:${PN} += " \
	bt-vendor \
"

do_install() {
    install -d ${D}${libdir}
    install -m 755 libbt-vendor.so ${D}${libdir}/
}