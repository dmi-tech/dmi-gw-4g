FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
	file://genio-350-evk-interfaces \
	file://genio-510-evk-interfaces \
	file://genio-700-evk-interfaces \
	file://genio-1200-evk-interfaces \
	file://i1200-demo-interfaces \
"

do_install:append:genio-350-evk() {
	install -d ${D}${sysconfdir}/network/
	install -m 0644 ${WORKDIR}/genio-350-evk-interfaces ${D}${sysconfdir}/network/interfaces
}

do_install:append:genio-510-evk() {
	install -d ${D}${sysconfdir}/network/
	install -m 0644 ${WORKDIR}/genio-510-evk-interfaces ${D}${sysconfdir}/network/interfaces
}

do_install:append:genio-700-evk() {
	install -d ${D}${sysconfdir}/network/
	install -m 0644 ${WORKDIR}/genio-700-evk-interfaces ${D}${sysconfdir}/network/interfaces
}

do_install:append:genio-1200-evk() {
	install -d ${D}${sysconfdir}/network/
	install -m 0644 ${WORKDIR}/genio-1200-evk-interfaces ${D}${sysconfdir}/network/interfaces
}

do_install:append:genio-1200-evk-p1v1() {
	install -d ${D}${sysconfdir}/network/
	install -m 0644 ${WORKDIR}/genio-1200-evk-interfaces ${D}${sysconfdir}/network/interfaces
}

do_install:append:genio-1200-evk-ufs() {
	install -d ${D}${sysconfdir}/network/
	install -m 0644 ${WORKDIR}/genio-1200-evk-interfaces ${D}${sysconfdir}/network/interfaces
}

do_install:append:i1200-demo() {
	install -d ${D}${sysconfdir}/network/
	install -m 0644 ${WORKDIR}/i1200-demo-interfaces ${D}${sysconfdir}/network/interfaces
}

FILES:${PN} += " \
	${sysconfdir}/network/interfaces \
"
