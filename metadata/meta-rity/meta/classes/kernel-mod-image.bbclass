DEPENDS:append = " depmodwrapper-cross e2fsprogs-native "
MODULE_IMAGE_DEPLOY = "${@bb.utils.contains("DISTRO_FEATURES", "modimg", "1", "0", d)}"

kernel_do_deploy:append() {
	if [ ${MODULE_IMAGE_DEPLOY} = "1" ] && (grep -q -i -e '^CONFIG_MODULES=y$' .config); then
		MOD_IMAGE_FS="${WORKDIR}/modimage"

		#
		# Step 1: Populate folder
		#
		rm -rf $MOD_IMAGE_FS
		mkdir -p $MOD_IMAGE_FS
		cp -a ${D}${nonarch_base_libdir} $MOD_IMAGE_FS

		# Setup folder structure for overlayfs
		mkdir -p $MOD_IMAGE_FS/lib/modules/upper
		mkdir -p $MOD_IMAGE_FS/lib/modules/work
		mv $MOD_IMAGE_FS/lib/modules/${KERNEL_VERSION} $MOD_IMAGE_FS/lib/modules/upper
		ln -sfr $MOD_IMAGE_FS/lib/modules/upper/${KERNEL_VERSION} $MOD_IMAGE_FS/lib/modules/${KERNEL_VERSION}

		#
		# Step 2: Create ext4 fs image
		#

		# The image might be used with overlayfs, and since not every
		# filesystem supports overlayfs, we hard-code ext4 for now.
		FS_SIZE=`du -s $MOD_IMAGE_FS/lib/modules | awk '{print $1}'`

		# Preserve extra 30% space
		FS_SIZE=`echo "$FS_SIZE * 130 / 100" | bc -q`
		# The minimal spare blocks needed for ext4 filesystem (from image_types.bbclass)
		COUNT="60"
		FS_IMG="$deployDir/modules-${MODULE_TARBALL_NAME}.modimg.ext4"
		bbdebug 1 "Mod fs size: $FS_SIZE"
		bbdebug 1 "Generating mod image $FS_IMG"
		dd if=/dev/zero of="$FS_IMG" seek=$FS_SIZE count=$COUNT bs=1024
		mkfs.ext4 $FS_IMG -d $MOD_IMAGE_FS/lib/modules -L modules
		fsck.ext4 -pvfD $FS_IMG || [ $? -le 3 ]
		if [ -n "${MODULE_TARBALL_LINK_NAME}" ] ; then
			ln -sf modules-${MODULE_TARBALL_NAME}.modimg.ext4 $deployDir/modules-${MODULE_TARBALL_LINK_NAME}.modimg.ext4
		fi
	fi
}
