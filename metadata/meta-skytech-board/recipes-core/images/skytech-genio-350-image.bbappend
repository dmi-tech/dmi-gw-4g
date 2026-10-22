do_image_wic[depends] += "boot-placeholders:do_deploy"


FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://99-ethernet-dhcp.network \
"

IMAGE_INSTALL:append = " iproute2 ethtool iputils"

ROOTFS_POSTPROCESS_COMMAND += "setup_network;"

setup_network () {
    set -e
    install -d ${IMAGE_ROOTFS}/etc/systemd/system
    install -d ${IMAGE_ROOTFS}/etc/systemd/network
    install -d ${IMAGE_ROOTFS}/etc/systemd/system/multi-user.target.wants

    # CÀI TRỰC TIẾP TỪ THISDIR/files → KHÔNG PHỤ THUỘC WORKDIR
    install -m 0644 ${THISDIR}/files/99-ethernet-dhcp.network \
        ${IMAGE_ROOTFS}/etc/systemd/network/99-ethernet-dhcp.network

    # Enable services
    if [ -e ${IMAGE_ROOTFS}/lib/systemd/system/systemd-networkd.service ]; then
        ln -sf /lib/systemd/system/systemd-networkd.service \
            ${IMAGE_ROOTFS}/etc/systemd/system/multi-user.target.wants/systemd-networkd.service || true
    fi
    if [ -e ${IMAGE_ROOTFS}/lib/systemd/system/systemd-networkd-wait-online.service ]; then
        ln -sf /lib/systemd/system/systemd-networkd-wait-online.service \
            ${IMAGE_ROOTFS}/etc/systemd/system/multi-user.target.wants/systemd-networkd-wait-online.service || true
    fi
}
