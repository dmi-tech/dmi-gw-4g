FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://0001-backend-drm-Always-mark-linear-modifier-as-supported.patch \
    file://0002-GENIO-Hack-to-force-DRM_FORMAT_MOD_INVALID-as-DRM_FO.patch \
"

