FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

inherit systemd

SRC_URI:append = " \
    file://weston.service \
    file://weston-wait-for-hdmi \
    file://weston.ini \
    file://tmpfiles.d/weston.conf \
"

# Tạo sẵn file cho lệnh sed của recipe gốc, tránh lỗi "No such file or directory"
do_install:prepend() {
    install -d ${D}/usr/lib/systemd/system
    if [ ! -f ${D}/usr/lib/systemd/system/weston.service ]; then
        touch ${D}/usr/lib/systemd/system/weston.service
    fi
}

# Sau khi do_install gốc chạy xong, chép lại file config/service của mình
do_install:append() {
    # weston.ini
    install -d ${D}${sysconfdir}/xdg/weston
    install -m 0644 ${WORKDIR}/weston.ini \
        ${D}${sysconfdir}/xdg/weston/weston.ini

    # weston.service (ghi đè file mà sed đã sửa)
    install -d ${D}/usr/lib/systemd/system
    install -m 0644 ${WORKDIR}/weston.service \
        ${D}/usr/lib/systemd/system/weston.service

    # Wait for HDMI without repeatedly initializing and tearing down DRM/Mali.
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/weston-wait-for-hdmi \
        ${D}${bindir}/weston-wait-for-hdmi

    # tmpfiles.d
    install -d ${D}${sysconfdir}/tmpfiles.d
    install -m 0644 ${WORKDIR}/tmpfiles.d/weston.conf \
        ${D}${sysconfdir}/tmpfiles.d/weston.conf
}

SYSTEMD_SERVICE:${PN} = "weston.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"
