FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

BRINGUP_CFG = " \
	file://bringup.cfg \
	file://cdc-acm.cfg \
"

SRC_URI:append = ' \
	${@bb.utils.contains("DISTRO_FEATURES", "bringup", "${BRINGUP_CFG}", "", d)} \
'
