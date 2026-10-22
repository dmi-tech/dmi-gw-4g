SUMMARY = "LIBUTILS"
DESCRIPTION = "LIBUTILS"
DEPENDS = "libcutils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=c1a3ff0b97f199c7ebcfdd4d3fed238e"

SRC_URI = " git://android.googlesource.com/platform/system/core;protocol=https;nobranch=1 \
	    file://0001-libutils-add-libutils-Makefile.patch \
	    file://0002-libutils-disable-android-function.patch \
	    file://0003-libutils-fix-add-binder-build-issue.patch \
"

SRCREV = "20ac1203a3201ac3e6d05a19325f5569033f3d08"

S = "${WORKDIR}/git"

do_compile() {
	cd ${S}/libutils && make
}

do_install() {
	cd ${S}/libutils && make install DESTDIR="${D}" INCDIR="${includedir}" \
	SRCDIR="${S}" LIBDIR="${libdir}"
}

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
ALLOW_EMPTY_${PN} = "1"
FILES_${PN} = "${libdir}/*"
FILES_${PN}-dev = "${includedir}/*"
INSANE_SKIP_${PN}-dev += "ldflags"
INSANE_SKIP:${PN}-dev += "dev-elf"
TARGET_CC_ARCH += "${LDFLAGS}"