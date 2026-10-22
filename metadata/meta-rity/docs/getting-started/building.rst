Building the RITY SDK
=====================

The RITY SDK must be built on a Linux-based OS. Please use one of the
officially `supported Linux Distributions <https://docs.yoctoproject.org/ref-manual/system-requirements.html#supported-linux-distributions>`_ by the Yocto Project.

Downloading the RITY SDK
------------------------

To download the RITY SDK, please run the following commands:

.. parsed-literal::

	$ mkdir rity; cd rity
	$ repo init -u https://gitlab.com/mediatek/aiot/bsp/manifest.git -b |release|
	$ repo sync

The commands above will download all the yocto layers of the RITY SDK into
the `rity/src/` directory.

.. note::

	The repo command can be installed using instructions from its official
	`documentation <https://gerrit.googlesource.com/git-repo/+/HEAD/README.md#install>`_

Generation of the build configuration
-------------------------------------

In order to be able to build the RITY SDK, one must first generate
the configuration. Please run the following commands to generate the
build configuration:

.. prompt:: bash $

	export TEMPLATECONF=${PWD}/src/meta-rity/meta/conf/
	source src/poky/oe-init-build-env

This operation needs to be done only once and will
create the following files:

.. code::

	rity
	└── build
	    └── conf
	        ├── bblayers.conf
	        ├── local.conf
	        └── templateconf.cfg

Building an image
-----------------

To build an image you need to run the bitbake commands as follows:

.. prompt:: bash $

	DISTRO=rity-demo MACHINE=<machine> bitbake rity-demo-image

The command above will build the `rity-demo-image`.

The image can be found in `rity/build/tmp/deploy/images/<machine>/`

The available values for `<machine>` can be found in the :doc:`BSP documentation <bsp:boards/index>`.

.. note::

	If you have signed a NDA with MediaTek, it's possible to enable more features by adding ``NDA_BUILD = "1"`` to your ``local.conf``

Images
------

The RITY SDK is providing the following images:

* rity-demo-image
* rity-bringup-image

rity-demo-image
^^^^^^^^^^^^^^^

The RITY demo image is used to demonstrate the RITY BSP. The image contains
a set of tools, applications, configurations that are targetted at showing
the RITY BSP for evaluation. The demo image also demonstrate how to build
a custom image on top of the RITY BSP.

`rity-demo-image` is designed to be build with the `DISTRO` variable set
to the following:

.. code::

	DISTRO=rity-demo

For example you can use the following command to build the `rity-demo-image`:

.. prompt:: bash $

	DISTRO=rity-demo MACHINE=<machine> bitbake rity-demo-image

rity-bringup-image
^^^^^^^^^^^^^^^^^^

The RITY bringup image is mostly used for BSP development and board bringup.
The image contains the bare minimum of tools necessary to validate software
and hardware.

`rity-bringup-image` is designed to be build with the `DISTRO` variable set
to the following:

.. code::

	DISTRO=rity-bringup

For example you can use the following command to build the `rity-bringup-image`:

.. prompt:: bash $

	DISTRO=rity-bringup MACHINE=<machine> bitbake rity-bringup-image
