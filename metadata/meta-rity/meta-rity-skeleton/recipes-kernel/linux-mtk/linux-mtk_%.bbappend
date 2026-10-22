# WARNING: please update the rity docs if you modify this file

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:i500-skeleton =  " \
	file://0001-arm64-dts-mediatek-add-MT8183-skeleton-dts.patch \
	file://skeleton.cfg \
"
