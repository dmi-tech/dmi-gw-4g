SUMMARY = "Placeholders for bootassets.vfat, capsule.vfat, userdata_6g.ext4 used by WIC"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# mkfs.vfat (dosfstools), mkfs.ext4 (e2fsprogs)
DEPENDS = "dosfstools-native e2fsprogs-native"

# Kích thước placeholder (điều chỉnh nếu wks yêu cầu khác)
BOOTASSETS_SIZE ?= "8M"
CAPSULE_SIZE    ?= "16M"
USERDATA_SIZE   ?= "6G"

S = "${WORKDIR}"
B = "${WORKDIR}/build"

do_compile() {
    set -eux
    mkdir -p "${B}"

    # 1) bootassets.vfat
    truncate -s ${BOOTASSETS_SIZE} "${B}/bootassets.vfat"
    mkfs.vfat -n BOOTASSETS "${B}/bootassets.vfat"

    # 2) capsule.vfat
    truncate -s ${CAPSULE_SIZE} "${B}/capsule.vfat"
    mkfs.vfat -n CAPSULE "${B}/capsule.vfat"

    # 3) userdata_6g.ext4
    #   - tạo file zero dung lượng 6G, mkfs.ext4 gắn nhãn USERDATA
    truncate -s ${USERDATA_SIZE} "${B}/userdata_6g.ext4"
    mkfs.ext4 -F -L USERDATA "${B}/userdata_6g.ext4"
}

do_install[noexec] = "1"

do_deploy() {
    install -d "${DEPLOY_DIR_IMAGE}"
    install -m 0644 "${B}/bootassets.vfat"     "${DEPLOY_DIR_IMAGE}/bootassets.vfat"
    install -m 0644 "${B}/capsule.vfat"        "${DEPLOY_DIR_IMAGE}/capsule.vfat"
    install -m 0644 "${B}/userdata_6g.ext4"    "${DEPLOY_DIR_IMAGE}/userdata_6g.ext4"
}
addtask deploy after do_compile before do_build
