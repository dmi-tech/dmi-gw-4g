LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DESCRIPTION = "Genio Debug Tools and Scripts"

SRC_URI = " \
	file://genio-display-debug-dump.sh \
	file://genio-sysinfo-dump.py \
	file://genio-enter-perf-mode.sh \
"



RDEPENDS:${PN} = " \
"

do_install() {
	install -d -m 0755 ${D}${ROOT_HOME}/debug/
	install -m 0755 ${WORKDIR}/genio-display-debug-dump.sh ${D}${ROOT_HOME}/debug/
	install -m 0755 ${WORKDIR}/genio-sysinfo-dump.py ${D}${ROOT_HOME}/debug/
	install -m 0755 ${WORKDIR}/genio-enter-perf-mode.sh ${D}${ROOT_HOME}/debug/
}

FILES:${PN} = "${ROOT_HOME}/debug/*"
