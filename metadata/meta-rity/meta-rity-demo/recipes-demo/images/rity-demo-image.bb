# Copyright (C) 2020 Fabien Parent <fparent@baylibre.com>
# Released under the MIT license (see COPYING.MIT for the terms)

require rity-bsp-image.bb

DESCRIPTION = "Rity Demo Image"

#FIXME: Removed pkggroups
IMAGE_INSTALL += "\
	packagegroup-rity-graphics \
	rity-demo-opencl \
	python3-pip \
	python3-opencv \
	opkg \
	benchmark-suite \
	ltp-dev \
	ltp-dbg \
	stress-scripts \
	packagegroup-rity-ai-ml \
"

#FIXME: Removed pkggroups
# r2inference
# gstinference
IMAGE_INSTALL:append:genio-700 = " \
	packagegroup-rity-tsn \
"

#FIXME: Removed pkggroups
# packagegroup-rity-nnapi
# r2inference
# gstinference
IMAGE_INSTALL:append:i350 = " \
"

#FIXME: Removed pkggroups
# r2inference
# gstinference
IMAGE_INSTALL:append:i1200 = " \
	packagegroup-rity-tsn \
"

#FIXME: Removed pkggroups
# r2inference
# gstinference
IMAGE_INSTALL:append:genio-510 = " \
	packagegroup-rity-tsn \
"

