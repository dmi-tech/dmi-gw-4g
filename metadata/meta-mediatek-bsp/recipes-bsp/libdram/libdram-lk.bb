require libdram.inc

DEPENDS:mt8365 = "libbase-lk-prebuilt"
DEPENDS:mt8188 = "libbase-lk-prebuilt"
DEPENDS:mt8195 = "libbase-lk-prebuilt"
DEPENDS:mt8370 = "libbase-lk-prebuilt"

EXTRA_OEMESON:append = " -Dlk=true"

do_install:append() {
	mv ${D}/${libdir}/libdram.a ${D}/${libdir}/libdram-lk.a
}

FILES:${PN}-staticdev = "${libdir}/libdram-lk.a"
