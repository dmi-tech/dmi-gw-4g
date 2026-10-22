FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# TODO: Currently we reuse config files from genio-700-evk.
#       It's subject to change in the future.
FILESEXTRAPATHS:prepend:genio-510-evk := "${THISDIR}/files/genio-700-evk:"
FILESEXTRAPATHS:prepend:genio-700-evk := "${THISDIR}/files/genio-700-evk:"
FILESEXTRAPATHS:prepend:genio-1200-evk := "${THISDIR}/files/genio-1200-evk:"
FILESEXTRAPATHS:prepend:genio-1200-evk-p1v1 := "${THISDIR}/files/genio-1200-evk:"

inherit systemd

SRC_URI:append = " \
	file://udhcpd.conf \
	file://usb.network \
	file://usbgadget.sh \
	file://usbgadget.service \
	file://eth.network \
	file://wireless.network \
	file://expand-rootfs.sh \
	file://expand-rootfs.service \
"

SRC_URI:append:genio-510-evk = " \
	file://usbgadget.conf \
	file://wwan-5g.sh \
	file://wwan-5g.service \
"

SRC_URI:append:genio-700-evk = " \
	file://usbgadget.conf \
	file://wwan-5g.sh \
	file://wwan-5g.service \
"

SRC_URI:append:genio-1200-evk = " \
	file://usbgadget.conf \
	file://usbmass.sh \
	file://usbmass.service \
	file://wwan-5g.sh \
	file://wwan-5g.service \
	file://91-caninterface.rules \
"

SRC_URI:append:genio-1200-evk-p1v1 = " \
	file://usbgadget.conf \
	file://usbmass.sh \
	file://usbmass.service \
	file://wwan-5g.sh \
	file://wwan-5g.service \
	file://91-caninterface.rules \
"

# Create symlink to /lib if multilib support is disabled
python () {
    d.setVar('SYMLIB64', '0')
    if not d.getVar('MULTILIBS', True):
        d.setVar('SYMLIB64', '1')
}

do_install:append() {
	install -d ${D}${systemd_unitdir}/system
	install -d ${D}${sysconfdir}/init.d

	install -d ${D}${sysconfdir}/systemd/network/
	install -m 0644 ${WORKDIR}/usb.network ${D}${sysconfdir}/systemd/network/
	install -m 0644 ${WORKDIR}/eth.network ${D}${sysconfdir}/systemd/network/
	install -m 0644 ${WORKDIR}/wireless.network ${D}${sysconfdir}/systemd/network/

	install -m 0644 ${WORKDIR}/usbgadget.service ${D}${systemd_unitdir}/system/
	install -m 0755 ${WORKDIR}/usbgadget.sh ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/usbgadget.sh ${D}${systemd_unitdir}

	install -m 0644 ${WORKDIR}/udhcpd.conf ${D}${sysconfdir}/udhcpd.conf

	if ${@bb.utils.contains('DISTRO_FEATURES', 'expand-rootfs', 'true', 'false', d)}; then
		# Expand rootfs on first startup for all Genio EVK
		install -m 0644 ${WORKDIR}/expand-rootfs.service ${D}${systemd_unitdir}/system/
		install -m 0755 ${WORKDIR}/expand-rootfs.sh ${D}${sbindir}/expand-rootfs.sh
	fi

	if [ "${SYMLIB64}" = "1" ]; then
		ln -sf lib/ ${D}/lib64
	fi

	if ${@bb.utils.contains('MACHINE_FEATURES', 'efi', 'true', 'false', d)}; then
		echo "LABEL=BOOT /boot vfat defaults 0 0" >> ${D}/${sysconfdir}/fstab
	fi
}

install_services_genio_700() {
	# Define default USB gadget port (ADB)
	install -m 0644 ${WORKDIR}/usbgadget.conf ${D}${sysconfdir}/usbgadget.conf

	# Create folder for services enabled by default
	install -d ${D}/${sysconfdir}/systemd/system/multi-user.target.wants/

	# WWAN 5G Card service
	install -m 0644 ${WORKDIR}/wwan-5g.service ${D}${systemd_unitdir}/system/
	install -m 0755 ${WORKDIR}/wwan-5g.sh ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/wwan-5g.sh ${D}${systemd_unitdir}
	# User should enable it manually or they sure it can be enabled by default.
	# ln -sfr ${D}/${systemd_system_unitdir}/wwan-5g.service ${D}/${sysconfdir}/systemd/system/multi-user.target.wants/wwan-5g.service
}

do_install:append:genio-510-evk() {
	install_services_genio_700
}

do_install:append:genio-700-evk() {
	install_services_genio_700
}

do_install:append:genio-1200-evk() {
	# Define default USB gadget port (ADB)
	install -m 0644 ${WORKDIR}/usbgadget.conf ${D}${sysconfdir}/usbgadget.conf

	# Create folder for services enabled by default
	install -d ${D}/${sysconfdir}/systemd/system/multi-user.target.wants/

	# USB Mass Storage service
	install -m 0644 ${WORKDIR}/usbmass.service ${D}${systemd_unitdir}/system/
	install -m 0755 ${WORKDIR}/usbmass.sh ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/usbmass.sh ${D}${systemd_unitdir}
	ln -sfr ${D}/${systemd_system_unitdir}/usbmass.service ${D}/${sysconfdir}/systemd/system/multi-user.target.wants/usbmass.service

	# WWAN 5G Card service
	install -m 0644 ${WORKDIR}/wwan-5g.service ${D}${systemd_unitdir}/system/
	install -m 0755 ${WORKDIR}/wwan-5g.sh ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/wwan-5g.sh ${D}${systemd_unitdir}
	# User should enable it manually or they sure it can be enabled by default.
	# ln -sfr ${D}/${systemd_system_unitdir}/wwan-5g.service ${D}/${sysconfdir}/systemd/system/multi-user.target.wants/wwan-5g.service

	# can bus
	install -D -p -m0644 ${WORKDIR}/91-caninterface.rules \
		${D}${sysconfdir}/udev/rules.d/91-caninterface.rules
}

do_install:append:genio-1200-evk-p1v1() {
	# Define default USB gadget port (ADB)
	install -m 0644 ${WORKDIR}/usbgadget.conf ${D}${sysconfdir}/usbgadget.conf

	# Create folder for services enabled by default
	install -d ${D}/${sysconfdir}/systemd/system/multi-user.target.wants/

	# USB Mass Storage service
	install -m 0644 ${WORKDIR}/usbmass.service ${D}${systemd_unitdir}/system/
	install -m 0755 ${WORKDIR}/usbmass.sh ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/usbmass.sh ${D}${systemd_unitdir}
	ln -sfr ${D}/${systemd_system_unitdir}/usbmass.service ${D}/${sysconfdir}/systemd/system/multi-user.target.wants/usbmass.service

	# WWAN 5G Card service
	install -m 0644 ${WORKDIR}/wwan-5g.service ${D}${systemd_unitdir}/system/
	install -m 0755 ${WORKDIR}/wwan-5g.sh ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/wwan-5g.sh ${D}${systemd_unitdir}
	# User should enable it manually or they sure it can be enabled by default.
	# ln -sfr ${D}/${systemd_system_unitdir}/wwan-5g.service ${D}/${sysconfdir}/systemd/system/multi-user.target.wants/wwan-5g.service

	# can bus
	install -D -p -m0644 ${WORKDIR}/91-caninterface.rules \
		${D}${sysconfdir}/udev/rules.d/91-caninterface.rules
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = " \
	usbgadget.service \
	${@bb.utils.contains('DISTRO_FEATURES', 'expand-rootfs', 'expand-rootfs.service', '', d)} \
"

FILES:${PN} += " \
	${systemd_unitdir}/usbgadget.sh \
	${systemd_unitdir}/system/usbgadget.service \
	${sysconfdir}/systemd/system/multi-user.target.wants/usbgadget.service \
	${sysconfdir}/systemd/network/usb.network \
	${sysconfdir}/systemd/network/eth.network \
	${sysconfdir}/systemd/network/wireless.network \
	${sysconfdir}/udhcpd.conf \
	${@bb.utils.contains('DISTRO_FEATURES', 'expand-rootfs', '${sbindir}/expand-rootfs.sh', '', d)} \
	${@bb.utils.contains('DISTRO_FEATURES', 'expand-rootfs', '${systemd_unitdir}/system/expand-rootfs.service', '', d)} \
"
FILES:${PN}:append:genio-510-evk = " \
	${sysconfdir}/usbgadget.conf \
	${systemd_unitdir}/system/wwan-5g.service \
"
FILES:${PN}:append:genio-700-evk = " \
	${sysconfdir}/usbgadget.conf \
	${systemd_unitdir}/system/wwan-5g.service \
"
FILES:${PN}:append:genio-1200-evk = " \
	${sysconfdir}/usbgadget.conf \
	${systemd_unitdir}/system/usbmass.service \
	${sysconfdir}/systemd/system/multi-user.target.wants/usbmass.service \
	${systemd_unitdir}/system/wwan-5g.service \
"
FILES:${PN}:append:genio-1200-evk-p1v1 = " \
	${sysconfdir}/usbgadget.conf \
	${systemd_unitdir}/system/usbmass.service \
	${sysconfdir}/systemd/system/multi-user.target.wants/usbmass.service \
	${systemd_unitdir}/system/wwan-5g.service \
"

# WWAN 5G Card service section:
#   User should enable it manually or they sure it can be enabled by default.

# genio-700-evk
# FILES:${PN}:append:genio-700-evk += " \
# 	${sysconfdir}/systemd/system/multi-user.target.wants/wwan-5g.service \
# "
#
# genio-i1200-evk
# FILES:${PN}:append:genio-1200-evk += " \
# 	${sysconfdir}/systemd/system/multi-user.target.wants/wwan-5g.service \
# "
