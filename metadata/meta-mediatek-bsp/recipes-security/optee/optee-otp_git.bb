SUMMARY = "OP-TEE OTP"
DESCRIPTION = "An application to emulate an OTP storage"

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e06794dca4078e0008c8cd23edeea1dd"

DEPENDS = "optee-client optee-os-tadevkit \
           python3-cryptography-native \
           libgcc"

inherit python3native features_check

require recipes-security/optee/optee.inc

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

SRC_URI = "${AIOT_BSP_URI}/optee-otp.git;branch=main;;protocol=https"
SRCREV = "59aa96d2183353bdadb6a50f07e68597261268c2"

COMPATIBLE_MACHINE = "mt*"

REQUIRED_MACHINE_FEATURES = "optee-rpmb"

EXTRA_OEMAKE += '\
    TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta \
    TA_CROSS_COMPILE=${TARGET_PREFIX} \
    CROSS_COMPILE=${TARGET_PREFIX} \
    CFLAGS="${CFLAGS} --sysroot=${STAGING_DIR_HOST} -I${WORKDIR}/optee-os -I${S}/ta/include" \
'
do_compile[cleandirs] = "${B}"

do_compile() {
    oe_runmake -C ${S}/ta O=${B}/ta
    oe_runmake -C ${S}/host OUTPUT_DIR=${B}/host
}

do_install () {
    mkdir -p ${D}${bindir}
    mkdir -p ${D}${nonarch_base_libdir}/optee_armtz
    install -D -p -m0755 ${B}/host/optee_otp ${D}${bindir}
    install -D -p -m0444 ${B}/ta/*.stripped.elf ${D}${nonarch_base_libdir}/optee_armtz
    install -D -p -m0444 ${B}/ta/*.ta ${D}${nonarch_base_libdir}/optee_armtz
}

FILES:${PN} += " ${nonarch_base_libdir}/optee_armtz/*.ta"
FILES:${PN}-dev += " ${nonarch_base_libdir}/optee_armtz/*.elf"

INSANE_SKIP:${PN} += "ldflags"

# This is specific to the machine
PACKAGE_ARCH = "${MACHINE_ARCH}"
