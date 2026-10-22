DESCRIPTION = "The recipe is used for collecting board assets and generating \
a tarball, which is used for flashing rity boot firmware image."
LICENSE = "MIT"

inherit deploy
require board-assets-common.inc
DEPENDS = "python3-pysimg-native"

ASSETS_WIC_IMG = "rity-board-assets.wic"
IMG_EXT = "vfat"

# For boot firmware image
BOOTFW_IMAGE_DIR = "${WORKDIR}/rity-bootfirmware"
BOOTFW_GENIMAGE_WIC_IMG = "${PN}.wic"
BOOTFW_WIC_IMG = "${PN}-${MACHINE}.wic"
GENIMAGE_CFG = "${THISDIR}/files/rity-bootfirmware.genimage.cfg"
BOOTFW_IMG_RITY_JSON = "bootfirmware.json"
BOOTFW_IMG_RITY_JSON_IN = "${THISDIR}/files/${BOOTFW_IMG_RITY_JSON}.in"

collect_artifacts() {
	cp ${DEPLOY_DIR_IMAGE}/bl2.img ${BOOTFW_IMAGE_DIR}
	cp ${DEPLOY_DIR_IMAGE}/bl2.cap ${BOOTFW_IMAGE_DIR}
	cp ${DEPLOY_DIR_IMAGE}/fip.bin ${BOOTFW_IMAGE_DIR}
	cp ${DEPLOY_DIR_IMAGE}/fip.cap ${BOOTFW_IMAGE_DIR}
	cp ${DEPLOY_DIR_IMAGE}/u-boot-initial-env ${BOOTFW_IMAGE_DIR}
	cp ${DEPLOY_DIR_IMAGE}/lk.bin ${BOOTFW_IMAGE_DIR}
	cp ${DEPLOY_DIR_IMAGE}/firmware.${IMG_EXT} ${BOOTFW_IMAGE_DIR}
	cp ${DEPLOY_DIR_IMAGE}/bootassets.${IMG_EXT} ${BOOTFW_IMAGE_DIR}
	cp ${DEPLOY_DIR_IMAGE}/capsule.${IMG_EXT} ${BOOTFW_IMAGE_DIR}
	cp ${DEPLOY_DIR_IMAGE}/fw_env.config ${BOOTFW_IMAGE_DIR}

	if [ -n "${MACHINE_DTB}" ]; then
		sed -i -e 's#^fdtfile=.\+#fdtfile=${MACHINE_DTB}#' ${BOOTFW_IMAGE_DIR}/u-boot-initial-env
	fi

	if [ -n "${DTB_PATH}" ]; then
		sed -i -e 's#^dtb_path=.\+#dtb_path=${DTB_PATH}#' ${BOOTFW_IMAGE_DIR}/u-boot-initial-env
	fi
}

# For boot firmware (.wic.img) (eMMC)
do_img2simg() {
	${bindir}/truncate -s 4096 "${BOOTFW_WIC_IMG}.img"
	img2simg -b 4096 -D 00000000 -o ${BOOTFW_IMAGE_DIR}/${BOOTFW_WIC_IMG}.img ${BOOTFW_IMAGE_DIR}/${BOOTFW_GENIMAGE_WIC_IMG}

	rm ${BOOTFW_IMAGE_DIR}/${BOOTFW_GENIMAGE_WIC_IMG}
}

do_genimage() {
	mkdir -p ${IMAGE_ROOTFS}
	mkdir -p ${WORKDIR}/${PN}-${PV}/root
	${bindir}/genimage --conf ${GENIMAGE_CFG} --inputpath ${BOOTFW_IMAGE_DIR} --outputpath ${BOOTFW_IMAGE_DIR}
	do_img2simg
}

do_boot_firmware_rityjson() {
	cp ${BOOTFW_IMG_RITY_JSON_IN} ${BOOTFW_IMAGE_DIR}/${BOOTFW_IMG_RITY_JSON}

	if [ -n "${MACHINE}" ]; then
		sed -i -e 's#"machine": null#"machine": "${MACHINE}"#' ${BOOTFW_IMAGE_DIR}/${BOOTFW_IMG_RITY_JSON}
	fi
}

do_boot_firmware() {
	do_genimage
	do_boot_firmware_rityjson

	# For SystemReady: board assets image need boot from 'usb0' by default.
	# If UFS storage is supported, enable boot option: scsi2
	if echo "${MACHINE}" | grep -q "ufs"; then
		sed -i -e 's#^boot_targets=.\+#boot_targets=usb0 mmc0 scsi2#' ${BOOTFW_IMAGE_DIR}/u-boot-initial-env
	else
		sed -i -e 's#^boot_targets=.\+#boot_targets=usb0 mmc0#' ${BOOTFW_IMAGE_DIR}/u-boot-initial-env
	fi
}

do_deploy() {
	rm -Rf ${BOOTFW_IMAGE_DIR}
	mkdir -p ${BOOTFW_IMAGE_DIR}

	collect_artifacts

	cd ${WORKDIR}

	# Convert board assets to boot firmware image (eMMC)
	do_boot_firmware
	tar zcf ${DEPLOYDIR}/rity-bootfirmware.tar.gz `basename ${BOOTFW_IMAGE_DIR}`
}

do_deploy[depends] += " \
    virtual/lk:do_deploy \
    virtual/bl2:do_deploy \
    virtual/bootloader:do_deploy \
    trusted-firmware-a:do_deploy \
    firmware-part:do_deploy \
    bootassets-part:do_deploy \
    capsule-part:do_deploy \
"
addtask do_deploy after do_compile
