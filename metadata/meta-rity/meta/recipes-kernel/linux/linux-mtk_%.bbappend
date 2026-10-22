FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append=  " \
	file://usbnet.cfg \
	file://pm.cfg \
	file://overlayfs.cfg \
	file://iotop.cfg \
	file://bpf.cfg \
	file://usb-audio.cfg \
	file://exfat.cfg \
	${@bb.utils.contains('DISTRO_FEATURES', 'security', 'file://security.cfg', '', d)} \
	${@bb.utils.contains('DISTRO_FEATURES', 'nfs', 'file://nfs.cfg', '', d)} \
	${@bb.utils.contains('DISTRO_FEATURES', 'ubsan', 'file://ubsan.cfg', '', d)} \
	${@bb.utils.contains('DISTRO_FEATURES', 'kcov', 'file://kcov.cfg', '', d)} \
	${@bb.utils.contains('DISTRO_FEATURES', 'btf', 'file://btf.cfg', '', d)} \
"

KERNEL_DEBUG = "${@bb.utils.contains('DISTRO_FEATURES', 'btf', 'True', '', d)}"
