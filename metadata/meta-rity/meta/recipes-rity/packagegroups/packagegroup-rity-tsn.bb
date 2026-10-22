# Copyright (C) 2022 MediaTek Inc.
# Author: Biao Huang <biao.huang@mediatek.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Rity TSN package"

inherit packagegroup

COMPATIBLE_MACHINE = "(mt8395|mt8390|mt8370)"

PACKAGES = "${PN}"

RDEPENDS:${PN} = " \
	linuxptp \
	iproute2-tc \
	tcpdump \
	scheduled-tx-tools \
	libopen62541 \
	real-time-edge-sysrepo \
	openssh-sftp-server \
	openssh-keygen \
"
