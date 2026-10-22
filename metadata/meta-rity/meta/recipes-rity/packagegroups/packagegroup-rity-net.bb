# Copyright (C) 2020 Fabien Parent <fparent@baylibre.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Rity Network packages"

inherit packagegroup

PACKAGES = " \
	${PN} \
	${PN}-extended \
"

RDEPENDS:${PN} = " \
	networkmanager \
	networkmanager-nmcli \
	iperf3 \
	iproute2 \
"

RDEPENDS:${PN}-extended = " \
	${PN} \
	hostapd \
"
