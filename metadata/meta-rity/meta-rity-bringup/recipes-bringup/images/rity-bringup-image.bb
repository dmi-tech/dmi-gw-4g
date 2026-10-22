# Copyright (C) 2020 Fabien Parent <fparent@baylibre.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Rity Bringup Image"

IMAGE_INSTALL += "\
	packagegroup-base \
	packagegroup-core-boot \
	packagegroup-core-full-cmdline \
	packagegroup-rity-debug \
	packagegroup-rity-tools \
	u-boot-env \
"

require recipes-rity/images/rity-image.inc
