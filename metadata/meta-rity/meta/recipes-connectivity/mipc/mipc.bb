# Copyright (C) 2022 Macpaul Lin <macpaul.lin@mediatek.com>
# Released under the MIT license (see COPYING.MIT for the terms)

# If you have bought RM500K from Quectel.
# Please contact Quectel to acquire Dial-up Utility
# The package name should be mipc or sample_datacall.
#
# Keep the source of this utiltiy in a git repository is suggested.
# The following varibles are required to complete this mipc.bb recipe.
# If you have questions, please refer to Yocto's document for detail.
#
#  - SRC_URI: URI to the repository of the source code is stored.
#  - SRCREV: The commit hash value in the source git repository to be used.
#  - LIC_FILES_CHKSUM: The checksum of the "LICENSE" file you added into source.
#
# If the source repository and this mipc.bb recipe is ready, add this package
# into 'src/meta-rity/meta/recipes-rity/packagegroups/packagegroup-rity-net.bb'
# to install it into the root-demo-image.


SUMMARY = "MediaTek MIPC tool for RM500K Dail-up"
SECTION = "network"
LICENSE = "LicenseRef-MediaTek-AIoT-SLA-1"

DEPENDS: = " libgcc glibc "

S = "${WORKDIR}/git"

TARGET_CC_ARCH += "${LDFLAGS}"

do_configure () {
	${MAKE} -C ${S} clean
}

do_compile () {
	unset CFLAGS CPPFLAGS CXXFLAGS
	oe_runmake 'CC=${CC}'
}

do_install () {
	install -d ${D}${sbindir}
	install -m 755 linux_out/sample_datacall_V1.2 ${D}${sbindir}

	install -d ${D}${libdir}
	install -m 644 ${S}/linux_out/libmipc_msg.a   ${D}${libdir}
	install -m 644 ${S}/linux_out/libmipc_msg.so  ${D}${libdir}
}

INSANE_SKIP_${PN} += " ldflags"
INSANE_SKIP_${PN}-dev += " ldflags"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""
