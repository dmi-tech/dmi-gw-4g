SUMMARY = "Quectel EG800K modem integration"
DESCRIPTION = "Udev port mapping and NetworkManager helper for Quectel EG800K USB modems."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://78-mm-skytech-eg800k.rules \
    file://eg800k-connect \
"

S = "${WORKDIR}"

RDEPENDS:${PN} = " \
    kernel-module-option \
    modemmanager \
    networkmanager-nmcli \
"

do_install() {
    install -d ${D}${nonarch_libdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/78-mm-skytech-eg800k.rules \
        ${D}${nonarch_libdir}/udev/rules.d/

    install -d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/eg800k-connect ${D}${sbindir}/
}

FILES:${PN} = " \
    ${nonarch_libdir}/udev/rules.d/78-mm-skytech-eg800k.rules \
    ${sbindir}/eg800k-connect \
"

