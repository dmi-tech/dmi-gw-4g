#!/bin/sh

# Command line parameters
ritydir=$1
machine=$2
imagename=$3

if [ $# -lt 3 ]; then
	echo "Usage: $0 /path/to/rity machine image_name"
	echo
	echo "This script allows you to copy the kernel configuration. This can"
	echo "be used as source config when building a linux kernel outside of"
	echo "yocto."
	echo
	echo "For this script to work, a yocto image should have be built for"
	echo "the targetted machine"
	echo
	echo "Example: $0 ~/rity/ i500-pumpkin core-image-weston"
	exit 1
fi

get_var_from_json() {
	local machinedir="$ritydir/build/tmp/deploy/images/$machine"
	local testdata_file="$machinedir/$imagename-$machine.testdata.json"
	grep "$1" "$testdata_file" | sed -n "s/    \"$1\": \"\(.*\)\",/\1/pg"
}

setup_env() {
	kernel_builddir="$ritydir/build/"$(get_var_from_json STAGING_KERNEL_BUILDDIR)
	if [ ! -d "$kernel_builddir" ]; then
		echo "Linux directory could not be found: '$kernel_builddir'. Please make sure $imagename has been fully built once."
		exit 1
	fi
}

copy_kernel_config() {
	local config="$kernel_builddir/.config"

	if [ ! -f "$config" ]; then
		echo "Could not find .config file: $config"
		exit 1
	fi

	echo "Copying kernel config to $machine-config..."
	cp "$config" "$machine-config"
}

setup_env
copy_kernel_config
