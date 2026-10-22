# Copyright (C) 2021 Fabien Parent <fparent@baylibre.com>
# Released under the MIT license (see COPYING.MIT for the terms)

require recipes-security/optee/optee-examples.inc

SRC_URI = "git://github.com/linaro-swg/optee_examples.git;branch=master;protocol=https"
SRCREV = "f301ee9df2129c0db683e726c91dc2cefe4cdb65"

COMPATIBLE_MACHINE = "mt*"
