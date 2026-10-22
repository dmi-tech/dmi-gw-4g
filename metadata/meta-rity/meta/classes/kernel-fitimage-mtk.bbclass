inherit kernel-fitimage

#
# Emit the fitImage ITS u-boot script section
#
# $1 ... .its filename
# $2 ... Image counter
# $3 ... Path to boot script image
fitimage_emit_section_boot_script() {

        bootscr_csum="${FIT_HASH_ALG}"
	bootscr_sign_algo="${FIT_SIGN_ALG}"
	bootscr_sign_keyname="${UBOOT_SIGN_IMG_KEYNAME}"

        cat << EOF >> ${1}
                default = "bootscr-${2}";
                bootscr-${2} {
                        description = "U-boot script";
                        data = /incbin/("${3}");
                        type = "script";
                        arch = "${UBOOT_ARCH}";
                        compression = "none";
                        hash-1 {
                                algo = "${bootscr_csum}";
                        };
                };
EOF

	if [ "${UBOOT_SIGN_ENABLE}" = "1" -a "${FIT_SIGN_INDIVIDUAL}" = "1" -a -n "${bootscr_sign_keyname}" ] ; then
		sed -i '$ d' ${1}
		cat << EOF >> ${1}
                        signature-1 {
                                algo = "${bootscr_csum},${bootscr_sign_algo}";
                                key-name-hint = "${bootscr_sign_keyname}";
                        };
                };
EOF
	fi
}

#
# Assemble fitImage
#
# $1 ... .its filename
# $2 ... fitImage name
# $3 ... include ramdisk
fitimage_assemble() {
	kernelcount=1
	dtbcount=""
	DTBS=""
	ramdiskcount=${3}
	setupcount=""
	bootscr_id=""
	rm -f ${1} arch/${ARCH}/boot/${2}

	if [ ! -z "${UBOOT_SIGN_IMG_KEYNAME}" -a "${UBOOT_SIGN_KEYNAME}" = "${UBOOT_SIGN_IMG_KEYNAME}" ]; then
		bbfatal "Keys used to sign images and configuration nodes must be different."
	fi

	fitimage_emit_fit_header ${1}

	#
	# Step 1: Prepare a kernel image section.
	#
	fitimage_emit_section_maint ${1} imagestart

	uboot_prep_kimage

	#
	# Step 2: Prepare a u-boot script section
	#

	if [ -n "${UBOOT_ENV}" ] && [ -d "${STAGING_DIR_HOST}/boot" ]; then
		if [ -e "${STAGING_DIR_HOST}/boot/${UBOOT_ENV_BINARY}" ]; then
			cp ${STAGING_DIR_HOST}/boot/${UBOOT_ENV_BINARY} ${B}
			bootscr_id="${UBOOT_ENV_BINARY}"
			fitimage_emit_section_boot_script ${1} "${bootscr_id}" ${UBOOT_ENV_BINARY}
		else
			bbwarn "${STAGING_DIR_HOST}/boot/${UBOOT_ENV_BINARY} not found."
		fi
	fi

	if [ "${INITRAMFS_IMAGE_BUNDLE}" = "1" ]; then
		# By default we look for Image.gz under the build folder.
		# To change compression format, in addition to changing the script here,
		# you also need to update KERNEL_EXTRA_ARGS in kernel recipe.
		initramfs_bundle_path="arch/"${UBOOT_ARCH}"/boot/"${KERNEL_IMAGETYPE_REPLACEMENT}".gz"
		if [ -e "${initramfs_bundle_path}" ]; then

			#
			# Include the kernel/rootfs bundle.
			#

			fitimage_emit_section_kernel ${1} "${kernelcount}" "${initramfs_bundle_path}" "${linux_comp}"
		else
			bbwarn "${initramfs_bundle_path} not found."
		fi
	else
		fitimage_emit_section_kernel ${1} "${kernelcount}" linux.bin "${linux_comp}"
	fi

	#
	# Step 3: Prepare a DTB image section
	#

	if [ -n "${KERNEL_DEVICETREE}" ]; then
		dtbcount=1
		for DTB in ${KERNEL_DEVICETREE}; do
			if echo ${DTB} | grep -q '/dts/'; then
				bbwarn "${DTB} contains the full path to the the dts file, but only the dtb name should be used."
				DTB=`basename ${DTB} | sed 's,\.dts$,.dtb,g'`
			fi

			# Skip ${DTB} if it's also provided in ${EXTERNAL_KERNEL_DEVICETREE}
			if [ -n "${EXTERNAL_KERNEL_DEVICETREE}" ] && [ -s ${EXTERNAL_KERNEL_DEVICETREE}/${DTB} ]; then
				continue
			fi

			DTB_PATH="arch/${ARCH}/boot/dts/${DTB}"
			if [ ! -e "${DTB_PATH}" ]; then
				DTB_PATH="arch/${ARCH}/boot/${DTB}"
			fi

			DTB=$(echo "${DTB}" | tr '/' '_')
			DTBS="${DTBS} ${DTB}"
			fitimage_emit_section_dtb ${1} ${DTB} ${DTB_PATH}
		done
	fi

	if [ -n "${EXTERNAL_KERNEL_DEVICETREE}" ]; then
		dtbcount=1
		for DTB in $(find "${EXTERNAL_KERNEL_DEVICETREE}" \( -name '*.dtb' -o -name '*.dtbo' \) -printf '%P\n' | sort); do
			DTB=$(echo "${DTB}" | tr '/' '_')
			DTBS="${DTBS} ${DTB}"
			fitimage_emit_section_dtb ${1} ${DTB} "${EXTERNAL_KERNEL_DEVICETREE}/${DTB}"
		done
	fi

	#
	# Step 4: Prepare a setup section. (For x86)
	#
	if [ -e arch/${ARCH}/boot/setup.bin ]; then
		setupcount=1
		fitimage_emit_section_setup ${1} "${setupcount}" arch/${ARCH}/boot/setup.bin
	fi

	#
	# Step 5: Prepare a ramdisk section.
	#
	if [ "x${ramdiskcount}" = "x1" ] && [ "${INITRAMFS_IMAGE_BUNDLE}" != "1" ]; then
		# Find and use the first initramfs image archive type we find
		for img in cpio.lz4 cpio.lzo cpio.lzma cpio.xz cpio.zst cpio.gz ext2.gz cpio; do
			initramfs_path="${DEPLOY_DIR_IMAGE}/${INITRAMFS_IMAGE_NAME}.${img}"
			echo "Using $initramfs_path"
			if [ -e "${initramfs_path}" ]; then
				fitimage_emit_section_ramdisk ${1} "${ramdiskcount}" "${initramfs_path}"
				break
			fi
		done
	fi

	fitimage_emit_section_maint ${1} sectend

	# Force the first Kernel and DTB in the default config
	kernelcount=1
	if [ -n "${dtbcount}" ]; then
		dtbcount=1
	fi

	#
	# Step 6: Prepare a configurations section
	#
	fitimage_emit_section_maint ${1} confstart

	# kernel-fitimage.bbclass currently only supports a single kernel (no less or
	# more) to be added to the FIT image along with 0 or more device trees and
	# 0 or 1 ramdisk.
        # It is also possible to include an initramfs bundle (kernel and rootfs in one binary)
        # When the initramfs bundle is used ramdisk is disabled.
	# If a device tree is to be part of the FIT image, then select
	# the default configuration to be used is based on the dtbcount. If there is
	# no dtb present than select the default configuation to be based on
	# the kernelcount.
	if [ -n "${DTBS}" ]; then
		i=1
		for DTB in ${DTBS}; do
			dtb_ext=${DTB##*.}
			if [ "${dtb_ext}" = "dtbo" ]; then
				fitimage_emit_section_config ${1} "" "${DTB}" "" "${bootscr_id}" "" "`expr ${i} = ${dtbcount}`"
			else
				fitimage_emit_section_config ${1} "${kernelcount}" "${DTB}" "${ramdiskcount}" "${bootscr_id}" "${setupcount}" "`expr ${i} = ${dtbcount}`"
			fi
			i=`expr ${i} + 1`
		done
	else
		defaultconfigcount=1
		fitimage_emit_section_config ${1} "${kernelcount}" "" "${ramdiskcount}" "${bootscr_id}"  "${setupcount}" "${defaultconfigcount}"
	fi

	fitimage_emit_section_maint ${1} sectend

	fitimage_emit_section_maint ${1} fitend

	#
	# Step 7: Assemble the image
	#
	${UBOOT_MKIMAGE} \
		${@'-D "${UBOOT_MKIMAGE_DTCOPTS}"' if len('${UBOOT_MKIMAGE_DTCOPTS}') else ''} \
		-f ${1} \
		arch/${ARCH}/boot/${2}

	#
	# Step 8: Sign the image and add public key to U-Boot dtb
	#
	if [ "x${UBOOT_SIGN_ENABLE}" = "x1" ] ; then
		if [ -n "${UBOOT_DTB_BINARY}" ]; then
			# The u-boot.dtb is a symlink to UBOOT_DTB_IMAGE, so we need copy
			# both of them, and don't dereference the symlink.
			cp -P ${STAGING_DATADIR}/u-boot*.dtb ${B}
			fdt_add_pubkey -a "${FIT_HASH_ALG},${FIT_SIGN_ALG}" -k "${UBOOT_SIGN_KEYDIR}" -n ${UBOOT_SIGN_KEYNAME} -r conf ${B}/${UBOOT_DTB_BINARY}
			fdt_add_pubkey -a "${FIT_HASH_ALG},${FIT_SIGN_ALG}" -k "${UBOOT_SIGN_KEYDIR}" -n ${UBOOT_SIGN_IMG_KEYNAME} -r image ${B}/${UBOOT_DTB_BINARY}
		fi
		${UBOOT_MKIMAGE_SIGN} \
			${@'-D "${UBOOT_MKIMAGE_DTCOPTS}"' if len('${UBOOT_MKIMAGE_DTCOPTS}') else ''} \
			-F -k "${UBOOT_SIGN_KEYDIR}" \
			-r arch/${ARCH}/boot/${2} \
			${UBOOT_MKIMAGE_SIGN_ARGS}
	fi
}
