# Copyright (C) 2021 Kidd Chen <kidd-kw.chen@mediatek.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Graphics Comformance Test"

inherit packagegroup

PACKAGES = " \
	${PN} \
"

RDEPENDS:${PN} = " \
	opengl-es-cts \
	vulkan-cts \
"
