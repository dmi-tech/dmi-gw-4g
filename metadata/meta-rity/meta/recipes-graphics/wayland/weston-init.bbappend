# The service will be started by udev rules
# (see 71-weston-drm.rules in meta-mediatek-bsp)
SYSTEMD_SERVICE:${PN} = ""

do_install:append() {
    # Only modify systemd service if systemd is enabled AND file exists
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        if [ -f "${D}${systemd_system_unitdir}/weston.service" ]; then
            # Change the session user from weston to root
            sed -i -e 's#^\(User\)=weston#\1=root#' ${D}${systemd_system_unitdir}/weston.service
            sed -i -e 's#^\(Group\)=weston#\1=root#' ${D}${systemd_system_unitdir}/weston.service
            sed -i -e 's#^\(WorkingDirectory\)=/home/weston#\1=${ROOT_HOME}#' ${D}${systemd_system_unitdir}/weston.service
        fi
    fi
}

pkg_postinst:${PN}:append() {
    # export WAYLAND_DISPLAY for root
    install -d -m 0755 $D${ROOT_HOME}

    if [ ! -e $D${ROOT_HOME}/.profile ] || ! grep -q "WAYLAND_DISPLAY" $D${ROOT_HOME}/.profile; then
        echo "export WAYLAND_DISPLAY=\"wayland-1\"" >> $D${ROOT_HOME}/.profile
    fi
}
