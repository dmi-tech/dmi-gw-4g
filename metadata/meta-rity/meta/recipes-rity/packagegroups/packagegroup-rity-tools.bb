# Copyright (C) 2020 Fabien Parent <fparent@baylibre.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Rity Tools packages"

inherit packagegroup

PACKAGES = " \
	${PN} \
	${PN}-extended \
"

RDEPENDS:${PN} = " \
	android-tools-adbd \
	htop \
	ldd \
	pm-utils \
	rng-tools \
	usbutils \
	u-boot-fw-utils \
	watchdog \
	wireless-regdb-static \
	serialcheck \
	spidev-test \
	dmidecode \
	evtest \
	libdrm-tests \
	read-edid \
	dosfstools \
	fio \
	init-ifupdown \
	networkmanager \
	networkmanager-nmcli \
	dtc \
"

RDEPENDS:${PN}-extended = " \
	${PN} \
	lsof \
	sed \
	zip \
	e2fsprogs-resize2fs \
	can-utils \
"
