SUMMARY = "Neoway N706B modem integration"
DESCRIPTION = "USB serial binding, ModemManager discovery, and NetworkManager helper for Neoway N706B modems."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://78-mm-skytech-n706b.rules \
    file://n706b-usb-setup \
    file://n706b-connect \
    file://n706b-usb-setup.service \
"

S = "${WORKDIR}"

inherit systemd

SYSTEMD_SERVICE:${PN} = "n706b-usb-setup.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

RDEPENDS:${PN} = " \
    kernel-module-option \
    kmod \
    modemmanager \
    networkmanager-nmcli \
"

do_install() {
    install -d ${D}${nonarch_libdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/78-mm-skytech-n706b.rules \
        ${D}${nonarch_libdir}/udev/rules.d/

    install -d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/n706b-usb-setup ${D}${sbindir}/
    install -m 0755 ${WORKDIR}/n706b-connect ${D}${sbindir}/

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/n706b-usb-setup.service \
        ${D}${systemd_system_unitdir}/
}

FILES:${PN} = " \
    ${nonarch_libdir}/udev/rules.d/78-mm-skytech-n706b.rules \
    ${sbindir}/n706b-usb-setup \
    ${sbindir}/n706b-connect \
    ${systemd_system_unitdir}/n706b-usb-setup.service \
"
