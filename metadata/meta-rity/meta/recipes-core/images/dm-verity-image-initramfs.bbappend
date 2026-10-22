FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

PACKAGE_INSTALL:remove = " \
    base-files \
    base-passwd \
    lvm2-udevrules \
"

inherit deploy

INITRAMFS_MAXSIZE = "524288"

deploy_verity_hash:append() {
    rm -f ${IMAGE_ROOTFS}/boot/*
}

