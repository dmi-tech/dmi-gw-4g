SUMMARY = "USB Gadget ADB service"
DESCRIPTION = "Userspace script to enable ADB over USB"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PV = "1.0"
PR = "r2"

SRC_URI = " \
    file://usb-gadget-adb.sh \
    file://usb-gadget-adb-init \
    file://adb_keys \
"

S = "${WORKDIR}"

RDEPENDS:${PN} = "bash coreutils initscripts"
RRECOMMENDS:${PN} += "android-tools-adbd"

inherit update-rc.d
INITSCRIPT_NAME = "usb-gadget-adb"
INITSCRIPT_PARAMS = "defaults 20 80"

do_install() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/usb-gadget-adb-init ${D}${sysconfdir}/init.d/usb-gadget-adb

    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/usb-gadget-adb.sh ${D}${bindir}/

    install -d ${D}${sysconfdir}/adb
    install -d ${D}/data/misc/adb

    install -m 0644 ${WORKDIR}/adb_keys ${D}/data/misc/adb/adb_keys

    echo "ro.adb.secure=0" > ${D}${sysconfdir}/adb/adb.prop
    echo "persist.sys.usb.config=adb" >> ${D}${sysconfdir}/adb/adb.prop
    echo "ro.debuggable=1" >> ${D}${sysconfdir}/adb/adb.prop

    echo "USB_GADGET_ADB_VERSION=${PV}-${PR}" > ${D}${sysconfdir}/adb/version
}

FILES:${PN} = " \
    ${sysconfdir}/init.d/usb-gadget-adb \
    ${bindir}/usb-gadget-adb.sh \
    ${sysconfdir}/adb \
    ${sysconfdir}/adb/* \
    /data/misc/adb/adb_keys \
"
