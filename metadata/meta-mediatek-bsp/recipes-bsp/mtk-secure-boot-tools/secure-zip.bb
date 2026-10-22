require recipes-bsp/mtk-secure-boot-tools/mtk-secure-boot-config.inc

inherit deploy

do_deploy() {
    install -d ${DEPLOYDIR}/
    cp -r -f ${WORKDIR}/secure/ ${DEPLOYDIR}/
}

addtask do_deploy after do_install
