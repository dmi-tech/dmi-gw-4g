# Copyright (C) 2022 Macross Chen <macross.chen@mediatek.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Mediatek Video Packages"

inherit packagegroup

PACKAGES = " \
	${PN} \
"

RDEPENDS:${PN} = " \
	mtk-vcu-driver-mt8395 \
	mtk-vcu-driver-mt8365 \
	mtk-vcodec-driver-mt8395 \
	mtk-vcodec-driver-mt8365 \
	mtk-mdp-driver-mt8365 \
	mtk-vpud \
"

RDEPENDS:${PN}:append:mt8365 = " \
	${VIRTUAL-RUNTIME_mdpd} \
"

