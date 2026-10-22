DESCRIPTION = "The recipe is used for collecting boot assets (e.g. boot logos) and generating \
a partition image, which is then used by wic tool to produce disk image."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit deploy

DEPENDS = "dosfstools-native mtools-native"
DEPENDS:nor-boot = ""
BOOTASSETS_IMAGE_FS = "${WORKDIR}/bootassetsimage"

#
# It's always recommended to create bootassets-part.bbappend in your custom
# layer and add your files. But for testing purpose, you can put your boot
# assets (e.g. boot_logo.bin) under the 'files' folder, and modify the
# recipe to add the file names in 'SRC_URI'.
#
SRC_URI = " \
    file://logo.bmp \
"

#
# The default bootassets partition size is 32MB.
# If you change the value, please also update rity.wks.in
# to reflect the change.
#
BLK_NUM = "32768"

#
# Put your boot assets copying scripts here
#
collect_artifacts() {
	cp ${WORKDIR}/logo.bmp ${BOOTASSETS_IMAGE_FS}
	echo "Put boot assets here" > ${BOOTASSETS_IMAGE_FS}/note
}

do_deploy() {
	mkdir -p ${BOOTASSETS_IMAGE_FS}
	collect_artifacts

	BOOTASSETS_IMAGE="${WORKDIR}/bootassets.vfat"

	test -e $BOOTASSETS_IMAGE && rm -f $BOOTASSETS_IMAGE
	mkfs.vfat -n "bootassets" -S ${WIC_SECTOR_SIZE} -C $BOOTASSETS_IMAGE ${BLK_NUM}
	mcopy -i $BOOTASSETS_IMAGE -s ${WORKDIR}/bootassetsimage/* ::/
	cp $BOOTASSETS_IMAGE ${DEPLOYDIR}
}

#
# For the board equipped with NOR flash, we doesn't use a filesystem
# to store boot assets, instead we flash the image to a given
# offset in the NOR, which is easier for u-boot to load it at runtime.
#
do_deploy:nor-boot() {
	cp ${WORKDIR}/logo.bmp ${DEPLOYDIR}/bootassets.img
}

addtask do_deploy after do_compile
