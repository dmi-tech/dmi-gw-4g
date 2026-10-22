# Copyright (C) 2020 Fabien Parent <fparent@baylibre.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Rity Multimedia packages"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

PACKAGES = " \
	${PN} \
	${PN}-extended \
"

RDEPENDS:${PN} = " \
	gstreamer1.0 \
	gstreamer1.0-plugins-base \
	gstreamer1.0-plugins-good \
	gstreamer1.0-plugins-bad \
	gstreamer1.0-libav \
	gst-instruments \
	libcamera \
	libcamera-gst \
	v4l-utils \
	media-ctl \
	yavta \
	uvc-gadget \
"

RDEPENDS:${PN}-extended = " \
	${PN} \
	ffmpeg \
	mpg123 \
	x264 \
	mtk-hdmirx-tool \
	graphviz \
"
