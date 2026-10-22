# Copyright (C) 2020 Fabien Parent <fparent@baylibre.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Rity Debug packages"

inherit packagegroup

PACKAGES = " \
	${PN} \
	${PN}-extended \
"

RDEPENDS:${PN} = " \
	binutils \
	devmem2 \
	gdb \
	i2c-tools \
	i2c-tools-misc \
	libgpiod-tools \
	os-release \
	strace \
	stress-ng \
	stressapptest \
	powertop \
	perf \
	bcc \
	bpftrace \
	iotop \
	bpftool \
"

RDEPENDS:${PN}-extended = " \
	${PN} \
	genio-devtools \
	genio-stress-gpu \
"
