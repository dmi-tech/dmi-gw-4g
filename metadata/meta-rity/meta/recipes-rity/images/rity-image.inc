# Copyright (C) 2020 Fabien Parent <fparent@baylibre.com>
# Released under the MIT license (see COPYING.MIT for the terms)

inherit core-image
inherit image-buildinfo

do_image_wic[depends] += " \
	${@bb.utils.contains("MACHINE_FEATURES", 'efi', 'firmware-part:do_deploy', '', d)} \
	bootassets-part:do_deploy \
	capsule-part:do_deploy \
"

my_postprocess_rootfs() {
	if ${@bb.utils.contains('DISTRO_FEATURES','wayland','true','false',d)}; then
		echo "mkdir -p /run/user/\$(id -u)" >> ${IMAGE_ROOTFS}/etc/profile
		echo "chmod 0700 /run/user/\$(id -u)" >> ${IMAGE_ROOTFS}/etc/profile
		echo "export XDG_RUNTIME_DIR=/run/user/\$(id -u)" >> ${IMAGE_ROOTFS}/etc/profile
	fi

	# Create symlinks with the string ".rootfs" stripped out
	# in filenames to be compatible with genio-tools.
	linkName=${IMAGE_LINK_NAME}
	stripName=${linkName%.rootfs}
	ln -sf ${IMAGE_NAME}.manifest ${IMGDEPLOYDIR}/${stripName}.manifest
	ln -sf ${IMAGE_NAME}.testdata.json ${IMGDEPLOYDIR}/${stripName}.testdata.json
}

my_preprocess_image() {
	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		# Disable systemd-networkd
		rm -rf ${IMAGE_ROOTFS}/etc/systemd/system/sockets.target.wants/systemd-networkd.socket
		rm -rf ${IMAGE_ROOTFS}/etc/systemd/system/multi-user.target.wants/systemd-networkd.service
		rm -rf ${IMAGE_ROOTFS}/etc/systemd/system/network-online.target.wants/systemd-networkd-wait-online.service
		rm -rf ${IMAGE_ROOTFS}/etc/systemd/system/sysinit.target.wants/systemd-network-generator.service
		rm -rf ${IMAGE_ROOTFS}/etc/systemd/system/dbus-org.freedesktop.network1.service
	fi
}

my_postprocess_image() {
	linkName=${IMAGE_LINK_NAME}
	stripName=${linkName%.rootfs}
	ln -sf ${IMAGE_NAME}.wic.img ${IMGDEPLOYDIR}/${stripName}.wic.img
}

ROOTFS_POSTPROCESS_COMMAND += "my_postprocess_rootfs; "

# Note 'my_preprocess_image' must be run after 'systemd_preset_all' (in image.bbclass).
# After preprocessing, run 'reproducible_final_image_task' to reset mtime.
IMAGE_PREPROCESS_COMMAND:append = " my_preprocess_image; reproducible_final_image_task;"

IMAGE_POSTPROCESS_COMMAND += "my_postprocess_image; "
