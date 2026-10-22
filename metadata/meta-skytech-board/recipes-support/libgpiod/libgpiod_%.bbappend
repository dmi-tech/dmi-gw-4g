FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://gpiofind"

do_install:append() {
    install -m 0755 ${WORKDIR}/gpiofind ${D}${bindir}/gpiofind
}

# libgpiod 2.x removed gpiofind. Keep the legacy command in the tools package
# for applications and operators that still expect the libgpiod 1.x interface.
FILES:${PN}-tools += "${bindir}/gpiofind"
