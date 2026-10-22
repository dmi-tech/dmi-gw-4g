GRUB_BUILDIN:append = " gzio"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
        file://grub.cfg \
"

do_configure:prepend() {
	# check if INITRAMFS_IMAGE or INITRAMFS_IMAGE_BUNDLE is enabled
	if [ ! -z "${INITRAMFS_IMAGE}" ] || [ "${INITRAMFS_IMAGE_BUNDLE}" = "1" ]; then
		bbfatal "EFI boot does not support the use of either INITRAMFS_IMAGE_BUNDLE or INITRAMFS_IMAGE!"
	fi
}
do_deploy:append() {
	install -m 0644 ${WORKDIR}/grub.cfg ${DEPLOYDIR}/grub.cfg
}
