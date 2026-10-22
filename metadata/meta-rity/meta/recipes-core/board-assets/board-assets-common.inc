
#
# To support your custom board, please remember to define following
# two variables in your local.conf file. These two variable are used
# for modifying u-boot-initial-env before packaging, so that the
# u-boot is able to correctly find your custom device tree files.
#
# The main device tree file of your custom board
#MACHINE_DTB = "your_board.dtb"
#
# The search path of device tree files in the firmware partition.
# Note all device tree files (dtb/dtbo) should be put under the
# search path.
# The search path should be in following format:
#   /FIRMWARE/your_vendor/your_board/
DTB_PATH ?= "/FIRMWARE/mediatek/${MACHINE}/"
