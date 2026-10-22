Create your own layer
=====================

The RITY SDK is composed of several Yocto Layers:

* meta-arm: Layer maintained by ARM Inc., it provides the recipes for Trusted-Firmware A and OPTEE-OS.
* meta-mediatek-bsp: Layer maintained by BayLibre SAS, it provides the BSP support for the RITY SDK.
* meta-openembedded: Collection of Layers that provides many networking tools, python3 packages, and multimedia packages.
* meta-rity: Layer maintained by BayLibre SAS, it provides images to make it easier to test and evaluate the MediaTek BSP
* poky: integration of various component enabling to build customized embedded device images.

It is advised to never modify layers that you don't own because it will make maintenance much
harder when trying to update these layers. The recommended way is to instead
create a new Yocto layer that will complement (overlay) these other layers.
This helps keep everything well compartementalized and will make upgrade easier.

This chapter will provide a small guide on how build a new layer on top of the RITY
SDK. The code from this chapter can be found under: `meta-rity/meta-rity-skeleton`_.

.. _meta-rity/meta-rity-skeleton: https://gitlab.com/mediatek/aiot/rity/meta-rity/-/tree/kirkstone/meta-rity-skeleton

1. Basic layer structure
------------------------

The most basic layer is composed of the following files:

.. code-block::
	:emphasize-lines: 1-5

	meta-rity-skeleton
	└── conf
	    ├── bblayers.conf.sample
	    ├── layer.conf
	    └── local.conf.sample

In theory only the file layer.conf is required but in general it is often
very useful to ship the two sample files `bblayers.conf.sample` and
`local.conf.sample`.

1.1. layer.conf
^^^^^^^^^^^^^^^

The `layer.conf` file is mostly used to define the layer name, dependencies,
priority and where to find recipes inside it.

 .. literalinclude:: ../meta-rity-skeleton/conf/layer.conf
	:linenos:

If you copy this file in your own layer, make sure to replace everywhere
`rity-skeleton` to the name of your own layer.

1.2. bblayers.conf.sample and local.conf.sample
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Before being able to build an image, the :ref:`configuration must be generated<getting-started/building:generation of the build configuration>`.
When the script `src/poky/oe-init-build-env` is being sourced, it will
try to create the `build/conf/local.conf` and `build/conf/bblayers.conf` if
these files do not yet exist. By setting the `TEMPLATECONF` variable
you can specify where the script will look for when trying to find the sample
files.

2. Creating your own image recipe
---------------------------------

It is often useful to create your own image recipe in order to choose which
applications and daemon are going to be part of your image.

.. code-block::
	:emphasize-lines: 6-8

	meta-rity-skeleton
	├── conf
	│   ├── bblayers.conf.sample
	│   ├── layer.conf
	│   └── local.conf.sample
	└── recipes-skeleton
		└── images
			└── rity-skeleton-image.bb

.. literalinclude:: ../meta-rity-skeleton/recipes-skeleton/images/rity-skeleton-image.bb
	:linenos:
	:lines: 3-
	:caption: recipes-skeleton/images/rity-skeleton-image.bb

You can add packages to your image by adding them to the `IMAGE_INSTALL`
variable.

3. Modidying the kernel
-----------------------

.. code-block::
	:emphasize-lines: 6-15

	meta-rity-skeleton
	├── conf
	│   ├── bblayers.conf.sample
	│   ├── layer.conf
	│   └── local.conf.sample
	├── recipes-kernel
	│   ├── dtbo
	│   │   ├── dtbo
	│   │   │   └── skeleton.dts
	│   │   └── dtbo.bbappend
	│   └── linux-mtk
	│       ├── linux-mtk
	│       │   ├── 0001-arm64-dts-mediatek-add-MT8183-skeleton-dts.patch
	│       │   └── skeleton.cfg
	│       └── linux-mtk_%.bbappend
	└── recipes-skeleton
		└── images
			└── rity-skeleton-image.bb

3.1. Adding patches on top of `linux-mtk`
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

One way to modify the kernel is to apply patches on top of the `linux-mtk`
kernel. You can add custom patches by creating a `linux-mtk_%.bbappend` file as
below to modify the recipe provided by `meta-mediatek-bsp`.

.. literalinclude:: ../meta-rity-skeleton/recipes-kernel/linux-mtk/linux-mtk_%.bbappend
	:linenos:
	:lines: 3-
	:caption: recipes-kernel/linux-mtk/linux-mtk_%.bbappend

Here the patch `0001-arm64-dts-mediatek-add-MT8183-skeleton-dts.patch` was
added by setting the file into `SRC_URI`.

3.2. Changing the kernel configuration
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

If you need to enable/disable configuration in the kernel you can create a
configuration fragment.

.. code-block::
	:emphasize-lines: 14

	meta-rity-skeleton
	├── conf
	│   ├── bblayers.conf.sample
	│   ├── layer.conf
	│   └── local.conf.sample
	├── recipes-kernel
	│   ├── dtbo
	│   │   ├── dtbo
	│   │   │   └── skeleton.dts
	│   │   └── dtbo.bbappend
	│   └── linux-mtk
	│       ├── linux-mtk
	│       │   ├── 0001-arm64-dts-mediatek-add-MT8183-skeleton-dts.patch
	│       │   └── skeleton.cfg
	│       └── linux-mtk_%.bbappend
	└── recipes-skeleton
		└── images
			└── rity-skeleton-image.bb

You can add the configuration fragment by adding it to the `SRC_URI` variable
of the `linux-mtk_%.bbappend` file.

.. literalinclude:: ../meta-rity-skeleton/recipes-kernel/linux-mtk/linux-mtk_%.bbappend
	:linenos:
	:caption: recipes-kernel/linux-mtk/linux-mtk_%.bbappend
	:lines: 5-8
	:lineno-start: 3
	:emphasize-lines: 3

.. literalinclude:: ../meta-rity-skeleton/recipes-kernel/linux-mtk/linux-mtk/skeleton.cfg
	:linenos:
	:lines: 3-
	:caption: recipes-kernel/linux-mtk/linux-mtk/skeleton.cfg

3.3. Creating an overlay
^^^^^^^^^^^^^^^^^^^^^^^^

Overlays allow you to selectively modify the device-tree. It is often used
to add functionality to board, such as optional peripherals like DSI displays.

.. code-block::
	:emphasize-lines: 7-10

	meta-rity-skeleton
	├── conf
	│   ├── bblayers.conf.sample
	│   ├── layer.conf
	│   └── local.conf.sample
	├── recipes-kernel
	│   ├── dtbo
	│   │   ├── dtbo
	│   │   │   └── skeleton.dts
	│   │   └── dtbo.bbappend
	│   └── linux-mtk
	│       ├── linux-mtk
	│       │   ├── 0001-arm64-dts-mediatek-add-MT8183-skeleton-dts.patch
	│       │   └── skeleton.cfg
	│       └── linux-mtk_%.bbappend
	└── recipes-skeleton
		└── images
			└── rity-skeleton-image.bb

.. literalinclude:: ../meta-rity-skeleton/recipes-kernel/dtbo/dtbo.bbappend
	:linenos:
	:lines: 3-
	:caption: recipes-kernel/dtbo/dtbo.bbappend

.. warning::

	It is hightly recommended to use the SRC_URI_<machine> syntax like on line 4
	in order to only ship the device-tree for that specific machine. Not
	setting the machine would mean the overlay would be ship for **every**
	machine.

4. Create a new machine
-----------------------

.. code-block::
	:emphasize-lines: 6-7

	meta-rity-skeleton
	├── conf
	│   ├── bblayers.conf.sample
	│   ├── layer.conf
	│   ├── local.conf.sample
	│   └── machine
	│       └── i500-skeleton.conf
	├── recipes-kernel
	│   ├── dtbo
	│   │   ├── dtbo
	│   │   │   └── skeleton.dts
	│   │   └── dtbo.bbappend
	│   └── linux-mtk
	│       ├── linux-mtk
	│       │   ├── 0001-arm64-dts-mediatek-add-MT8183-skeleton-dts.patch
	│       │   └── skeleton.cfg
	│       └── linux-mtk_%.bbappend
	└── recipes-skeleton
		└── images
			└── rity-skeleton-image.bb

A new machine can be created by adding a new file in `conf/machine/`. The
MediaTek BSP is defining machine under the following pattern "<SoC>-<Board>".

.. literalinclude:: ../meta-rity-skeleton/conf/machine/i500-skeleton.conf
	:linenos:
	:lines: 3-
	:caption: conf/machine/i500-skeleton.conf

You can specify the desired kernel device by setting `KERNEL_DEVICETREE`. For
the `meta-rity-skeleton` layer, this device-tree is defined in the kernel
patch `recipes-kernel/linux-mtk/linux-mtk/0001-arm64-dts-mediatek-add-MT8183-skeleton-dts.patch`.

5. Create a custom DISTRO
-------------------------

.. code-block::
	:emphasize-lines: 4-5

	meta-rity-skeleton
	├── conf
	│   ├── bblayers.conf.sample
	│   ├── distro
	│   │   └── rity-skeleton.conf
	│   ├── layer.conf
	│   ├── local.conf.sample
	│   └── machine
	│       └── i500-skeleton.conf
	├── recipes-kernel
	│   ├── dtbo
	│   │   ├── dtbo
	│   │   │   └── skeleton.dts
	│   │   └── dtbo.bbappend
	│   └── linux-mtk
	│       ├── linux-mtk
	│       │   ├── 0001-arm64-dts-mediatek-add-MT8183-skeleton-dts.patch
	│       │   └── skeleton.cfg
	│       └── linux-mtk_%.bbappend
	└── recipes-skeleton
		└── images
			└── rity-skeleton-image.bb


.. literalinclude:: ../meta-rity-skeleton/conf/distro/rity-skeleton.conf
	:linenos:
	:lines: 3-
	:caption: conf/distro/rity-skeleton.conf

In order to use your new distro, you can modify the `DISTRO` variable in your
`local.conf.sample`.
