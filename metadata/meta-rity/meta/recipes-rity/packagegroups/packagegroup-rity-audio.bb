# Copyright (C) 2020 Fabien Parent <fparent@baylibre.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Rity Audio packages"

inherit packagegroup

PACKAGES = " \
	${PN} \
	${PN}-extended \
"

RDEPENDS:${PN} = " \
	alsa-state \
	alsa-utils \
"

PIPEWIRE_PKGS = " \
	pipewire \
	pipewire-spa-tools \
	pipewire-tools \
	pipewire-alsa \
	pipewire-pulse \
	pipewire-v4l2 \
	pipewire-modules-rt \
	pipewire-modules-protocol-native \
	pipewire-modules-client-node \
	pipewire-modules-client-device \
	pipewire-modules-adapter \
	pipewire-modules-metadata \
	pipewire-modules-session-manager \
	pipewire-modules-profiler \
	pipewire-modules-spa-device-factory \
	pipewire-modules-portal \
	pipewire-modules-access \
	pipewire-modules-link-factory \
	pipewire-spa-plugins-dbus \
	pipewire-spa-plugins-bluez5 \
	pipewire-spa-plugins-codec-bluez5-aac \
	pipewire-spa-plugins-codec-bluez5-lc3 \
	pipewire-spa-plugins-v4l2 \
	pipewire-spa-plugins-alsa \
	wireplumber \
	wireplumber-modules-lua-scripting \
	wireplumber-modules-default-nodes-api \
	wireplumber-modules-meta \
	wireplumber-modules-file-monitor-api \
	wireplumber-modules-si-audio-adapter \
	wireplumber-modules-si-standard-link \
	wireplumber-modules-si-node \
	wireplumber-modules-mixer-api \
	wireplumber-modules-logind \
"


RDEPENDS:${PN}-extended = " \
	${PN} \
	${@bb.utils.contains("DISTRO_FEATURES", "pipewire", "${PIPEWIRE_PKGS}", "", d)} \
"
