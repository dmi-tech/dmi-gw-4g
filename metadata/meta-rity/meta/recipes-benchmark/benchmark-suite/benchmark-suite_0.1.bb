SUMMARY = "Benchmark suite for AIOT Yocto platforms"
HOMEPAGE = "https://gitlab.com/mediatek/aiot/bsp/benchmark_suite"
SECTION = "benchmark/tests"
LICENSE = "GPL-2.0-only & GPL-2.0-or-later & GPL-3.0-or-later & Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=941d6d45d5f146ffe07fe4d50168f754"

SRC_URI = " \
    ${AIOT_BSP_URI}/benchmark_suite;protocol=https;branch=main \
    file://COPYING \
"
SRCREV = "d389930c3bc040b5f36936faccf3f4b373503463"

S = "${WORKDIR}/git"

TARGET_CC_ARCH += "${LDFLAGS}"

do_copy_license () {
    cp ${WORKDIR}/COPYING ${S}
}

do_configure () {
    PLAT="${SOC_FAMILY}"
    NUM_CORES=4
    if [ "$PLAT" = "mt8195" ] || [ "$PLAT" = "mt8188" ]; then
        NUM_CORES=8
    elif [ "$PLAT" = "mt8370" ]; then
        NUM_CORES=6
    fi

    echo "export NUM_CORES=$NUM_CORES" >> ${S}/common
    echo "export USE_PREBUILT_BINARIES=1" >> ${S}/common
}

do_compile () {
    export CROSS_COMPILE="${TARGET_ARCH}-poky-linux-"
    ./build.sh
}

do_install () {
    install -d ${D}${ROOT_HOME}/benchmark_suite
    cp -r ${S}/_build/benchmark_${TARGET_ARCH}/* ${D}${ROOT_HOME}/benchmark_suite
}

DEPENDS = " \
    unzip-native \
    cmake-native \
"

RDEPENDS:${PN} = " \
    bash \
    perf \
    python3-numpy \
"

FILES:${PN} = "${ROOT_HOME}/benchmark_suite"

# Skip package QA of checking runtime dependencies on prebuilt binaries
INSANE_SKIP:${PN} += "file-rdeps"

addtask do_copy_license after do_unpack before do_populate_lic
