SUMMARY = "GstInference"
DESCRIPTION = "A GStreamer deep learning inference framework"
HOMEPAGE = "https://developer.ridgerun.com/wiki/index.php?title=GstInference"
SECTION = "multimedia"
LICENSE = "LGPL-2.1-or-later"

LIC_FILES_CHKSUM = "file://COPYING;md5=b5ec61ada91a1aad4812666edfd8c57e"

DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base r2inference opencv"

SRCBRANCH ?= "main"
SRCREV = "d6a16e36232ba2adc653860c2a5642606537538d"
SRC_URI = "${AIOT_RITY_URI}/gst-inference.git;protocol=https;branch=${SRCBRANCH}"

S = "${WORKDIR}/git"

do_install:append() {
    install -d ${D}${datadir}/gstinference_example
    install -d ${D}${datadir}/gstinference_example/object_detection
    install -d ${D}${datadir}/gstinference_example/image_classification

    install -m 644 ${S}/gstinference_example/object_detection/labels_pascal.txt ${D}${datadir}/gstinference_example/object_detection
    install -m 644 ${S}/gstinference_example/object_detection/*.tflite ${D}${datadir}/gstinference_example/object_detection
    if [ -d "${S}/gstinference_example/object_detection/${SOC_FAMILY}" ]
    then
        install -m 644 ${S}/gstinference_example/object_detection/${SOC_FAMILY}/*.dla ${D}${datadir}/gstinference_example/object_detection
    fi

    install -m 644 ${S}/gstinference_example/image_classification/labels.txt ${D}${datadir}/gstinference_example/image_classification
    install -m 644 ${S}/gstinference_example/image_classification/*.tflite ${D}${datadir}/gstinference_example/image_classification
    if [ -d "${S}/gstinference_example/image_classification/${SOC_FAMILY}" ]
    then
        install -m 644 ${S}/gstinference_example/image_classification/${SOC_FAMILY}/*.dla ${D}${datadir}/gstinference_example/image_classification
    fi

}

FILES:${PN} += "${libdir}/gstreamer-1.0/libgstinferenceoverlayplugin.so"
FILES:${PN} += "${libdir}/gstreamer-1.0/libgstinference.so"

FILES:${PN} += "${datadir}/*"
inherit meson pkgconfig
