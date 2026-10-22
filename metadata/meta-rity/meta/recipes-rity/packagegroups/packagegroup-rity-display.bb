# Copyright (C) 2020 Fabien Parent <fparent@baylibre.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Rity Display packages"

inherit packagegroup

PACKAGES = " \
	${PN} \
	${PN}-extended \
"

VULKAN_PKGS = " \
	vulkan-samples \
	vulkan-tools \
"

WAYLAND_PKGS = " \
	wayland \
	weston \
	weston-examples \
	weston-init \
"

RDEPENDS:${PN} = " \
	kmscube \
	${@bb.utils.contains("DISTRO_FEATURES", "vulkan", "${VULKAN_PKGS}", "", d)} \
	${@bb.utils.contains("DISTRO_FEATURES", "wayland", "${WAYLAND_PKGS}", "", d)} \
"

RDEPENDS:${PN}-extended = " \
	${PN} \
	gtk+3-demo \
"
