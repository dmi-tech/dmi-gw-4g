# Copyright (C) 2021 Fabien Parent <fparent@baylibre.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Service for MT7663 Wi-Fi/Bluetooth chip"
LICENSE = "CLOSED"

inherit systemd
inherit update-rc.d

SRC_URI = " \
	file://mt7663.conf \
	file://mt7663.service \
	file://mt7663.sh \
"

do_install:append() {
	install -d ${D}${sysconfdir}/init.d
	install -m 0644 ${WORKDIR}/mt7663.sh ${D}${sysconfdir}/init.d

	install -d ${D}${sysconfdir}/modprobe.d
	install -m 0644 ${WORKDIR}/mt7663.conf ${D}${sysconfdir}/modprobe.d

	if [ "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}" = "systemd" ]; then
		install -d ${D}${systemd_system_unitdir}
		install -m 0644 ${WORKDIR}/mt7663.service ${D}${systemd_system_unitdir}
		install -m 0755 ${WORKDIR}/mt7663.sh ${D}${systemd_unitdir}
	fi
}

SYSTEMD_PACKAGES = "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${PN}', '', d)}"
SYSTEMD_SERVICE:${PN} = "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'mt7663.service', '', d)}"

INITSCRIPT_NAME = "mt7663.sh"
INITSCRIPT_PARAMS = "start 00 5 2 3 ."

FILES:${PN} += " \
	${systemd_system_unitdir}/mt7663.service \
	${systemd_unitdir}/mt7663.sh \
	${sysconfdir}/modprobe.d/mt7663.conf \
	${sysconfdir}/init.d/mt7663.sh \
"
