# Copyright (C) 2021 Julien Stephan <jstephan@baylibre.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Rity AI-ML packages"

inherit packagegroup

PACKAGES = " \
	${PN} \
	${PN}-extended \
"

RDEPENDS:${PN} = " \
	${@bb.utils.contains('TFLITE_PREBUILT', '1', 'tensorflowlite-prebuilt', 'tensorflow-lite', d)}  \
	armnn \
	nnstreamer \
"

RDEPENDS:${PN}-extended = " \
	${PN} \
"
