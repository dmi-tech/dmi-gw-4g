# Copyright (C) 2018 Fabien Parent <fparent@baylibre.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Rity Zeroconf packages"

inherit packagegroup

PACKAGES = " \
	${PN} \
	${PN}-extended \
"

ZEROCONF_PKGS = " \
	avahi-autoipd \
	avahi-daemon \
	avahi-utils \
"

RDEPENDS:${PN} = " \
	${@bb.utils.contains("DISTRO_FEATURES", "zeroconf", "${ZEROCONF_PKGS}", "", d)} \
"

RDEPENDS:${PN}-extended = " \
	${PN} \
"
