# Copyright (C) 2020 Fabien Parent <fparent@baylibre.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Rity Development packages"

inherit packagegroup

PACKAGES = " \
	${PN} \
	${PN}-extended \
"

RDEPENDS:${PN} = " \
	curl \
	gdb \
	ldd \
	rsync \
	strace \
	valgrind \
	opencl-icd-loader-dev \
	clinfo \
	eglinfo \
"

RDEPENDS:${PN}-extended = " \
	${PN} \
"
