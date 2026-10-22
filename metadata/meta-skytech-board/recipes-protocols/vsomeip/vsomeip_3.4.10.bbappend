FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Patch fix thiếu <iomanip> cho security_test_client.cpp
SRC_URI += " file://0001-network-tests-include-iomanip.patch "
