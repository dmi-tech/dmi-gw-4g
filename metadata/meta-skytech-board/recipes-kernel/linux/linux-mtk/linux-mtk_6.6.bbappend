FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://enable-hdmi.cfg \
    file://enable-audio.cfg \
    file://enable-4g.cfg \
    file://0001-arm64-dts-mt8365-evk-limit-memory-to-2GiB.patch \
    file://0002-arm64-dts-mt8365-evk-drop-HDMI-dai-link-from-sound-card.patch \
"

KERNEL_CONFIG_FRAGMENTS += "enable-hdmi.cfg enable-audio.cfg enable-4g.cfg"
