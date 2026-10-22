LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=0162574e7503bb231524b8203915d2c6"
SUMMARY = "Developer tool to stress GPU using OpenCL for MediaTek Genio SoC"

SRC_URI = "${AIOT_RITY_URI}/genio-stress-gpu;protocol=https;branch=main"
SRCREV = "2212186452fe18d4b38438b9debe8f3676412a84"

S = "${WORKDIR}/git"

# BASE_VER is manually extracted from MAJOR_VER and MINOR_VER in gpu-stressor-opencl.cpp
BASE_VER = "1.0"
PKGV = "${BASE_VER}+git"

# opencl-icd-loader: for linking to libOpenCL.so
# opencl-headers: for OpenCL header files (CL/OpenCL.h)
DEPENDS = "opencl-icd-loader opencl-headers"

# libOpenCL.so should be responsible for searching and loading
# the actual libmali.so installed in Genio EVK.
RDEPENDS:${PN} += " opencl-icd-loader"

do_compile () {
	# Build the default target
	oe_runmake
}

do_install () {
	# The Makefile does not define a target named "install",
	# so assign install path here.
	install -d -m 0755 ${D}${bindir}
	install -m 0755 ${B}/genio-stress-gpu ${D}${bindir}
}

FILES:${PN} = "${bindir}/genio-stress-gpu"
