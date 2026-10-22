FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
	file://mediatek.conf \
"
do_configure:append() {
	cat ${WORKDIR}/mediatek.conf >> ${B}/hostapd/.config
}