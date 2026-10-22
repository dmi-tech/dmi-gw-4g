SUMMARY = "Empty bootassets FAT image placeholder for WIC"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# mkfs.vfat
DEPENDS = "dosfstools-native"

# Kích thước 8MiB là đủ làm placeholder (tùy wks bạn có thể tăng/giảm)
BOOTASSETS_SIZE ?= "8M"
BOOTASSETS_NAME ?= "bootassets.vfat"

S = "${WORKDIR}"
B = "${WORKDIR}/build"

do_compile() {
    set -eux
    mkdir -p "${B}"
    truncate -s ${BOOTASSETS_SIZE} "${B}/${BOOTASSETS_NAME}"
    mkfs.vfat -n BOOTASSETS "${B}/${BOOTASSETS_NAME}"
}

do_install[noexec] = "1"

do_deploy() {
    install -d "${DEPLOY_DIR_IMAGE}"
    install -m 0644 "${B}/${BOOTASSETS_NAME}" "${DEPLOY_DIR_IMAGE}/${BOOTASSETS_NAME}"
}
addtask deploy after do_compile before do_build
