DESCRIPTION = "Prebuilt libraries of Mediatek NeuroPilot"
LICENSE = "LicenseRef-MediaTek-AIoT-SLA-1"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c25f59288708e3fd9961c9e6142aafee"

COMPATIBLE_MACHINE = "mt8395|mt8390|mt8370"

DEPENDS += " libcxx ncurses "
RDEPENDS:${PN} += " libcxx ncurses libstdc++ zlib "

PROVIDES = " \
	virtual/libneuron \
	virtual/ncc-tflite \
"

RPROVIDES:${PN} = " \
	libneuron \
	ncc-tflite \
"

BRANCH = "${DISTRO_CODENAME}"

SRCREV = "e1f3131415fca5510f4aab9302e98003bdf9261b"
SRC_URI += "${AIOT_RITY_URI}/mtk-neuropilot-prebuilts.git;protocol=https;branch=${BRANCH} \
"

S = "${WORKDIR}/git"

NP_VERSION = "6"

do_configure[noexec] = "1"
do_buildclean[noexec] = "1"

do_install() {
	# Header files
	install -d ${D}/${includedir}/neuron/api
	install ${S}/neuron/${NP_VERSION}/usr/include/neuron/api/*.h ${D}/${includedir}/neuron/api

	# Shared libraries
	install -d ${D}/${libdir}
	install -m 0644 ${S}/neuron/${NP_VERSION}/usr/lib64/*.so* ${D}/${libdir}

	# Tools such as off-line compilers
	install -d ${D}/${sbindir}
	install ${S}/neuron/${NP_VERSION}/usr/sbin/* ${D}/${sbindir}

	# Demo application
	install -d ${D}${datadir}/demo_dla
	install ${S}/sample/usr/share/demo_dla/* ${D}${datadir}/demo_dla
	install -d ${D}${datadir}/benchmark_dla
	install ${S}/sample/usr/share/benchmark_dla/* ${D}${datadir}/benchmark_dla
}

FILES:${PN} += " \
	${datadir} ${libdir}/*.so \
"

FILES_SOLIBSDEV = ""

INHIBIT_SYSROOT_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
INSANE_SKIP:${PN} += "already-stripped dev-so"
