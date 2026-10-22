FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://0001-Fix-PolygonOffsetClampMinMax-test-fail-when-using-24.patch \
"

PACKAGECONFIG = "wayland"