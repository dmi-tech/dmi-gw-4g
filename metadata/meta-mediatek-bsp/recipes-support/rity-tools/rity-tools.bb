# Copyright (C) 2021 Fabien Parent <fparent@baylibre.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "RITY tools"
LICENSE = "CLOSED"

RITY_JSON = "rity.json"
RITY_JSON:nor-boot = "rity_nor.json"

SRC_URI = " \
	file://${RITY_JSON} \
"

inherit deploy

do_deploy() {
	install -m 0644 ${WORKDIR}/${RITY_JSON} ${DEPLOYDIR}/rity.json
	ln -fs rity.json ${DEPLOYDIR}/partitions.json
}
addtask do_deploy after do_install
