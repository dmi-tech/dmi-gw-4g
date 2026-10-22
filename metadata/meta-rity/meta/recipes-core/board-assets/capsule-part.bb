DESCRIPTION = "The recipe is used for collecting EFI capsules and generating \
a partition image, which is then used by wic tool to produce disk image."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit deploy

DEPENDS = "dosfstools-native mtools-native"
CAPSULE_IMAGE_FS = "${WORKDIR}/capsule/EFI/UpdateCapsule"

#
# The default capsule filesystem size is 100MB.
# Currently capsule is designed to be stored in the EFI system
# partition (ESP), so after changing the value, please also
# update rity.wks.in to reflect the change.
#
BLK_NUM = "102400"

add_capsule() {
	mkdir -p ${CAPSULE_IMAGE_FS}
	mkfs.vfat -n "capsule" -S ${WIC_SECTOR_SIZE} -C ${WORKDIR}/capsule.vfat ${BLK_NUM}
	mcopy -i ${WORKDIR}/capsule.vfat -s ${WORKDIR}/capsule/* ::/
	mv ${WORKDIR}/capsule.vfat ${DEPLOYDIR}
}

do_deploy() {
	add_capsule
}

addtask do_deploy after do_compile
