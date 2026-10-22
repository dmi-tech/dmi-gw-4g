# Copyright (C) 2021 Kidd Chen <kidd-kw.chen@mediatek.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Mediatek Neuropilot packages"

inherit packagegroup

COMPATIBLE_MACHINE = "(mt8395|mt8390|mt8370)"

PACKAGES = " \
	${PN} \
"

RDEPENDS:${PN} = " \
	mtk-apusys-firmware \
	mtk-apusys-driver \
	mtk-apusys-middleware \
	mtk-apusys-tools \
	neuropilot-bin \
"
