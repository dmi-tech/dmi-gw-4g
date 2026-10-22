SUMMARY = "LIBUTILS"
DESCRIPTION = "LIBUTILS"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=c1a3ff0b97f199c7ebcfdd4d3fed238e"

SRC_URI = " git://android.googlesource.com/platform/system/core;protocol=https;nobranch=1 \
	    file://0001-libcutils-add-libcutils-Makefile.patch \
	    file://0001-libcutils-fix-builderror.patch \
"

SRCREV = "1dea9a052b7f214c10a77d5ed6ffd3602722a817"

S = "${WORKDIR}/git"

do_compile() {
	cd ${S}/libcutils && make
}

do_install() {
	cd ${S}/libcutils && make install DESTDIR="${D}" INCDIR="${includedir}" \
	SRCDIR="${S}" LIBDIR="${libdir}"
	install -d ${D}${includedir}/cutils
	install -m 0644 ${S}/include/cutils/trace.h ${D}${includedir}/cutils
}

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
ALLOW_EMPTY_${PN} = "1"
FILES_${PN} += "${libdir}"
FILES_${PN}-dev += "${includedir}"
INSANE_SKIP:${PN} += "already-stripped"
INSANE_SKIP:${PN} += "file-rdeps"
INSANE_SKIP:${PN} += "ldflags"
INSANE_SKIP:${PN}-dev += "file-rdeps"
INSANE_SKIP:${PN}-dev += "dev-elf"
INSANE_SKIP:${PN}-dev += "ldflags"