# Append to poky/meta/recipes-core/systemd/systemd-conf_1.0.bb
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
	file://10-journald-default-volatile.conf \
"

do_install:append() {
	install -D -m 0644 ${WORKDIR}/10-journald-default-volatile.conf ${D}${sysconfdir}/systemd/journald.conf.d/10-journald-default-volatile.conf
}

FILES:${PN}:append = " \
	${sysconfdir}/systemd/journald.conf.d/10-journald-default-volatile.conf \
"
