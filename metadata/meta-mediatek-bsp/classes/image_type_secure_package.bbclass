inherit image_types

CONVERSION_CMD:zip = "touch ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.${type}; cd ${SECURE_SYSROOT}/${IMAGE_NAME}/secure; zip ${ZIP_COMPRESSION_LEVEL} -r ${IMGDEPLOYDIR}/secure-${MACHINE}.zip .; cd -"

SECURE_SYSROOT = "${WORKDIR}/tmp-secure"
do_image_secure[dirs] = "${SECURE_SYSROOT}"
do_image_secure[cleandirs] = "${SECURE_SYSROOT}"
do_image_secure[depends] += "secure-zip:do_deploy"

IMAGE_CMD:secure() {
    tmp_pack_dir=${SECURE_SYSROOT}/${IMAGE_NAME}
    mkdir -p ${tmp_pack_dir}
    cp -a ${DEPLOY_DIR_IMAGE}/secure ${tmp_pack_dir}
}
