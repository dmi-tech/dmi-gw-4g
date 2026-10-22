SUMMARY = "UVC gadget test app"
DESCRIPTION = "USB UVC Library (https://github.com/wlhe/uvc-gadget)"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ffa10f40b98be2c2bc9608f56827ed23"

RDEPENDS:${PN} = "uvc-gadget"

TARGET_CC_ARCH += "${LDFLAGS}"

INSANE_SKIP_${PN} += "ldflags"

SRC_URI = " \
	file://uvc-gadget.c \
	file://uvc.h \
	file://Makefile \
	file://LICENSE \
"

S = "${WORKDIR}"

do_compile() {
	make
}

do_install() {
	install -d ${D}${bindir}/
	install -m 0755 ${S}/uvc-gadget ${D}${bindir}/
}

FILES:${PN} = "${bindir}/uvc-gadget"
