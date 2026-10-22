DESCRIPTION = "The recipe is used for collecting firmware artifacts and generating \
a partition image, which is then used by wic tool to produce disk image."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit deploy
require board-assets-common.inc

DEPENDS = "dosfstools-native mtools-native"
DEPENDS:nor-boot = "mtd-utils-native"

DEPENDS += " \
	${@bb.utils.contains("DISTRO_FEATURES", "secure-boot", "dtc-native u-boot-tools-native", "", d)} \
"

SRC_URI += " \
	${@bb.utils.contains("DISTRO_FEATURES", "secure-boot", "file://fdt.its", "", d)} \
"

FW_IMAGE_FS = "${WORKDIR}/fwimage/${DTB_PATH}"
USE_YOCTO_DTB = "${@bb.utils.contains('DISTRO_FEATURES', 'prebuilt-dtb', '0', '1', d)}"

FW_FDT_ITS = "${FW_IMAGE_FS}/fdt.its"
UBOOT_MKIMAGE ?= "uboot-mkimage"
UBOOT_MKIMAGE_CMD = "${@bb.utils.contains("DISTRO_FEATURES", "secure-boot", "${UBOOT_MKIMAGE} -F -k ${UBOOT_SIGN_KEYDIR}", "${UBOOT_MKIMAGE}", d)}"

#
# It's always recommended to create firmware-part.bbappend in your custom
# layer and add your files. But for testing purpose, you can put your device
# tree files (*.dtb/*.dtbo) under the 'files' folder, and modify the
# recipe to add the file names in 'SRC_URI'.
#
#SRC_URI = " \
#    file://foo.dtbo \
#"

#
# The default firmware partition size is 32MB.
# After changing the value, please also update rity.wks.in
# to reflect the change.
#
BLK_NUM = "32768"

collect_artifacts() {
	if [ "${USE_YOCTO_DTB}" = "1" ]; then
		for d in ${KERNEL_DEVICETREE}; do
			cp ${DEPLOY_DIR_IMAGE}/`basename $d` ${FW_IMAGE_FS}
		done
		cp ${DEPLOY_DIR_IMAGE}/devicetree/*.dtbo ${FW_IMAGE_FS}
	else
		for f in `ls ${WORKDIR} | grep "\.dtb\|\.dtbo"`; do
			cp ${WORKDIR}/$f ${FW_IMAGE_FS}
		done
	fi

	if [ ${@bb.utils.contains("DISTRO_FEATURES", "secure-boot", "1", "0", d)} = "1" ]; then
		generate_fdt
	fi
}

generate_fdt() {
	for f in `ls ${FW_IMAGE_FS} | grep "\.dtb\|\.dtbo"`; do
		cp ${WORKDIR}/fdt.its ${FW_FDT_ITS}
		sed -i 's/FDTBIN/'"$f"'/g' ${FW_FDT_ITS}
		${UBOOT_MKIMAGE_CMD} -f ${FW_FDT_ITS} ${FW_IMAGE_FS}/$f
	done

	test -e ${FW_FDT_ITS} && rm -f ${FW_FDT_ITS}
}

do_deploy() {
	#
	# Step 1: Populate firmware directory structure
	#
	mkdir -p ${FW_IMAGE_FS}

	#
	# Step 2: Copy firmware files
	#
	collect_artifacts

	#
	# Step 3: Create fs image
	#
	FW_IMAGE="${WORKDIR}/firmware.vfat"

	test -e $FW_IMAGE && rm -f $FW_IMAGE
	mkfs.vfat -n "firmware" -S ${WIC_SECTOR_SIZE} -C $FW_IMAGE ${BLK_NUM}
	mcopy -i $FW_IMAGE -s ${WORKDIR}/fwimage/* ::/
	cp $FW_IMAGE ${DEPLOYDIR}
}

do_deploy:nor-boot() {
	#
	# Step 1: Populate firmware directory structure
	#
	mkdir -p ${FW_IMAGE_FS}

	#
	# Step 2: Copy firmware files
	#
	collect_artifacts

	#
	# Step 3: Create fs image
	#
	FW_IMAGE="${WORKDIR}/firmware.jffs2"

	test -e $FW_IMAGE && rm -f $FW_IMAGE
	mkfs.jffs2 --root=${WORKDIR}/fwimage --output=$FW_IMAGE --eraseblock=${MTD_ERASEBLOCK_SIZE} --little-endian --pad
	cp $FW_IMAGE ${DEPLOYDIR}
}

do_deploy[depends] += "${@bb.utils.contains('USE_YOCTO_DTB', '1', 'virtual/kernel:do_deploy virtual/dtb:do_deploy', '', d)}"
addtask do_deploy after do_compile
