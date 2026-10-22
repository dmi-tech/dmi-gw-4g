# Copyright (C) 2021 Ryan Cho <ryan.cho@mediatek.com>
# Released under the MIT license (see COPYING.MIT for the terms)

require libbase-prebuilt.inc

do_install() {
	install -d ${D}${libdir}
	install -m 644 ${S}/${SOC_FAMILY}/libbase-lk.a ${D}${libdir}
}
