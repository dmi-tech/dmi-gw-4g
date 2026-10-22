do_install:append() {
    sed -i \
        -e 's,EXTRA_ARGS=\"-r /dev/hwrng\",EXTRA_ARGS=\"-r /dev/hwrng -x jitter\",g' \
        ${D}${sysconfdir}/default/rng-tools
}
