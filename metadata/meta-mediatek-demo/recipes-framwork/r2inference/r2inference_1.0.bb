SUMMARY = "RidgeRun's Inference Library"
DESCRIPTION = "A machine learning library for easy inference integration"
HOMEPAGE = "https://developer.ridgerun.com/wiki/index.php?title=R2Inference"
SECTION = "multimedia"
LICENSE = "LGPL-2.1-only"

LIC_FILES_CHKSUM = "file://COPYING;md5=a079f37f0484c6a88e7b23a94d6326c5"

NEURON_PLATFORM = "${@bb.utils.contains_any('SOC_FAMILY',  'mt8195 mt8188', '1', '0', d)}"

DEPENDS = "${@ "glib-2.0 flatbuffers virtual/libneuron" if d.getVar('NEURON_PLATFORM') == '1' and d.getVar('NDA_BUILD') == '1' else "glib-2.0 flatbuffers" }"
DEPENDS += "${@ "tensorflowlite-prebuilt" if d.getVar('TFLITE_PREBUILT') == '1' else "tensorflow-lite" }"

SRCBRANCH ?= "main"
SRCREV = "3ef4de2351cd5ca1b44f24ccf2ed08973ee3ca6e"
SRC_URI = "${AIOT_RITY_URI}/r2inference.git;protocol=https;branch=${SRCBRANCH}"

EXTRA_OEMESON += "${@ " -Denable-tflite=true -Denable-neuronrt=true -Denable-tests=disabled -Denable-docs=disabled " if d.getVar('NEURON_PLATFORM') == '1' and d.getVar('NDA_BUILD') == '1' else " -Denable-tflite=true  -Denable-tests=disabled -Denable-docs=disabled " }"

S = "${WORKDIR}/git"

FILES:${PN} += "${libdir}/libr2inference.so"

inherit meson pkgconfig 


