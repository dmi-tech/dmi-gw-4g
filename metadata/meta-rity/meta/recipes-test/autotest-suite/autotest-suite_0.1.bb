SUMMARY = " Linux test suite for IOT Yocto platforms"
HOMEPAGE = "https://gitlab.com/mediatek/aiot/bsp/genio-linux-test"
SECTION = "console/tests"
LICENSE = "MIT & GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=a1313728de19d51b0f9c66ca39e1af1a"

SRC_URI = " \
    ${AIOT_BSP_URI}/genio-linux-test;protocol=https;branch=main \
    file://COPYING \
"
SRCREV = "670a7b5fcc134c69aa2859347ab5b246a5488884"

S = "${WORKDIR}/git"

do_copy_license () {
    cp ${WORKDIR}/COPYING ${S}
}

do_install () {
    install -d ${D}${ROOT_HOME}/genio-linux-test
    cp -r ${S}/* ${D}${ROOT_HOME}/genio-linux-test
}

FILES:${PN} = "${ROOT_HOME}/genio-linux-test"

RDEPENDS:${PN} = " \
    bash \
    python3 \
"

addtask do_copy_license after do_unpack before do_populate_lic
