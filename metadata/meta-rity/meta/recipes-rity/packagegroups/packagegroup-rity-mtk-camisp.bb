# Copyright (C) 2022 Andy Hsieh <andy.hsieh@mediatek.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Mediatek Camera ISP Packages"

inherit packagegroup

PACKAGES = " \
	${PN} \
"

# Public Release (Non-NDA): The package includes only `mtk-camisp-driver`,
# which allows direct dumping of the YUV sensor data to the system memory.
#
# Private Release (Requires NDA): The package includes additional components,
# `mtk-camisp-prebuilts` and `mtk-vcu-driver-mt8395`, to support advanced
# processing of Bayer RAW sensor data using the on-chip ISP. Users looking
# to leverage the ISP features must obtain NDA permission and enable them by
# setting the configuration `NDA_BUILD` to "1".

RDEPENDS:${PN} = " \
	mtk-camisp-driver \
	${@bb.utils.contains("DISTRO_FEATURES", "nda-mtk", "mtk-camisp-prebuilts", "", d)} \
	${@bb.utils.contains("DISTRO_FEATURES", "nda-mtk", "mtk-vcu-driver-mt8395", "", d)} \
"
