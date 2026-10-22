FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " file://xdg-runtime-dir.sh"

do_install:append () {
    install -d ${D}${sysconfdir}/profile.d
    install -m 0755 ${WORKDIR}/xdg-runtime-dir.sh ${D}${sysconfdir}/profile.d/
}
