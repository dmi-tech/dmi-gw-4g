FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
	file://NetworkManager.conf \
	file://usb0.nmconnection \
	file://10-globally-managed-devices.conf \
"

do_install:append() {
	install -d ${D}${sysconfdir}/NetworkManager
	install -m 0644 ${WORKDIR}/NetworkManager.conf \
			${D}${sysconfdir}/NetworkManager/
	install -m 0600 ${WORKDIR}/usb0.nmconnection \
			${D}${sysconfdir}/NetworkManager/system-connections
	install -m 0600 ${WORKDIR}/10-globally-managed-devices.conf \
			${D}${sysconfdir}/NetworkManager/conf.d
}

FILES:${PN} += " \
	${sysconfdir}/NetworkManager/NetworkManager.conf \
	${sysconfdir}/NetworkManager/system-connections/usb0.nmconnection \
	${sysconfdir}/NetworkManager/system-connections/conf.d/10-globally-managed-devices.conf \
"
