require recipes-bsp/mtk-secure-boot-tools/mtk-secure-boot-config.inc

do_populate_sysroot() {
	install -d ${SYSROOT_DESTDIR}${sysconfdir}/secure
	install ${SBC_KEY} ${SYSROOT_DESTDIR}${sysconfdir}/secure/sbc.pem
	install ${WORKDIR}/key.ini ${SYSROOT_DESTDIR}${sysconfdir}/secure/key.ini
	install ${WORKDIR}/pl_gfh_config_pss.ini ${SYSROOT_DESTDIR}${sysconfdir}/secure
	if [ "${@oe.utils.conditional('DA_SIGN_ENABLE', '1', '1', '', d)}" = "1" ]; then
		install ${DA_KEY} ${SYSROOT_DESTDIR}${sysconfdir}/secure/da.pem
		install ${WORKDIR}/secure/auth_sv5.auth ${SYSROOT_DESTDIR}${sysconfdir}/secure
	fi

	if [ "${@oe.utils.conditional('BL2_SIGN_ENABLE', '1', '1', '', d)}" = "1" ]; then
		install ${WORKDIR}/secure/efuse.cfg ${SYSROOT_DESTDIR}${sysconfdir}/secure
	fi
}
