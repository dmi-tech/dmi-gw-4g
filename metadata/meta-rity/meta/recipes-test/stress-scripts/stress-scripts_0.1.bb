SUMMARY = "Stress scripts used for burning MTK platforms"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://start-i350.py \
           file://start-i1200.py \
           file://start-genio-700.py \
           file://start-genio-510.py \
           file://disk.py \
           file://runner.py \
           file://read_temp.py \
           file://read_oops.py \
"

S = "${WORKDIR}"

python() {
    plat = d.getVar('SOC_FAMILY', True)
    if plat == 'mt8195':
        d.setVar('START_SCRIPT', 'start-i1200.py')
    elif plat == 'mt8365':
        d.setVar('START_SCRIPT', 'start-i350.py')
    elif plat == 'mt8188':
        d.setVar('START_SCRIPT', 'start-genio-700.py')
    elif plat == 'mt8370':
        d.setVar('START_SCRIPT', 'start-genio-510.py')
    else:
        bb.warn('Unrecognized SOC (%s), use the default script.' % plat)
        d.setVar('START_SCRIPT', 'start-i1200.py')
}

do_install() {
	install -d -m 0755 ${D}${ROOT_HOME}/stress_scripts
	install -m 0755 ${WORKDIR}/${START_SCRIPT} ${D}${ROOT_HOME}/stress_scripts/start.py
	install -m 0644 ${WORKDIR}/disk.py ${D}${ROOT_HOME}/stress_scripts/disk.py
	install -m 0644 ${WORKDIR}/runner.py ${D}${ROOT_HOME}/stress_scripts/runner.py
	install -m 0755 ${WORKDIR}/read_temp.py ${D}${ROOT_HOME}/stress_scripts/read_temp.py
	install -m 0755 ${WORKDIR}/read_oops.py ${D}${ROOT_HOME}/stress_scripts/read_oops.py
}

RDEPENDS:${PN} = " \
	iperf3 \
	fio \
	stress-ng \
	glmark2 \
	sysstat \
	gstreamer1.0-plugins-good-video4linux2 \
	gstreamer1.0-plugins-base-playback \
"

#RDEPENDS:${PN}:append:i350 = " \
#	${@bb.utils.contains('TFLITE_PREBUILT', '1', 'tensorflowlite-prebuilt', 'tensorflow-lite', d)} \
#"

#RDEPENDS:${PN}:append:i1200 = " \
#	${@bb.utils.contains('DISTRO_FEATURES', 'nda-mtk', 'ncc-tflite libneuron', '', d)} \
#"

#RDEPENDS:${PN}:append:genio-700 = " \
#	${@bb.utils.contains('DISTRO_FEATURES', 'nda-mtk', 'ncc-tflite libneuron', '', d)} \
#"

#RDEPENDS:${PN}:append:genio-510 = " \
#	${@bb.utils.contains('DISTRO_FEATURES', 'nda-mtk', 'ncc-tflite libneuron', '', d)} \
#"

FILES:${PN} = " \
	${ROOT_HOME}/stress_scripts/start.py \
	${ROOT_HOME}/stress_scripts/disk.py \
	${ROOT_HOME}/stress_scripts/runner.py \
	${ROOT_HOME}/stress_scripts/read_temp.py \
	${ROOT_HOME}/stress_scripts/read_oops.py \
"

