# Copyright (C) 2020 Fabien Parent <fparent@baylibre.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Rity OP-TEE packages"

inherit packagegroup

PACKAGES = " \
	${PN} \
	${PN}-extended \
"

RDEPENDS:${PN} = " \
	optee-client \
	optee-examples \
	optee-test \
"

RDEPENDS:${PN}-extended = " \
	${PN} \
"
