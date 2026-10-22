FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " file://10-watchdog.conf"

do_install:append() {
	install -d ${D}${nonarch_base_libdir}/systemd/system.conf.d/
	install -m 0644 ${WORKDIR}/10-watchdog.conf ${D}${nonarch_base_libdir}/systemd/system.conf.d/
}
