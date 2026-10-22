# WARNING: please update the rity docs if you modify this file

DESCRIPTION = "RITY Skeleton Image"

IMAGE_INSTALL += " \
	packagegroup-base \
	packagegroup-core-boot \
	packagegroup-core-full-cmdline \
	packagegroup-rity-zeroconf \
"

require recipes-rity/images/rity-image.inc
