FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://20-journald-runtime-limit.conf \
"

do_install:append() {
    install -D -m 0644 ${WORKDIR}/20-journald-runtime-limit.conf \
        ${D}${sysconfdir}/systemd/journald.conf.d/20-journald-runtime-limit.conf
}

FILES:${PN}:append = " \
    ${sysconfdir}/systemd/journald.conf.d/20-journald-runtime-limit.conf \
"
