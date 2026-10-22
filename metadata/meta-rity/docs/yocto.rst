Short introduction to Yocto
===========================

The RITY SDK is using `Yocto <https://www.yoctoproject.org/>`_ as foundation.
This section will provide some basic knowledge to get started with Yocto.

Building instructions
---------------------

This section will explain the building instruction from
the :ref:`getting-started/building:Building the RITY SDK` section. This section is assuming that the
SDK source code has already been downloaded.

.. prompt:: bash $

	cd rity
	export TEMPLATECONF=${PWD}/src/meta-rity/meta/conf/
	source src/poky/oe-init-build-env
	DISTRO=rity-demo bitbake rity-demo-image

Loading the Yocto build environment
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

In order to be able to use the Yocto tools, you must first load the yocto
environment settings. To do this you need to run the following command:

.. prompt:: bash $

	export TEMPLATECONF=${PWD}/src/meta-rity/meta/conf/
	source src/poky/oe-init-build-env

Setting `TEMPLATECONF` is only needed the first time you will run the `source`
command. Sourcing the `oe-init-build-env` script will add the Yocto tools
in your `PATH` variable.

The first time you source the `oe-init-build-env` script, it will create the
following files.

.. code::

	rity
	└── build
	    └── conf
	        ├── bblayers.conf
	        ├── local.conf
	        └── templateconf.cfg

The file `templateconf.cfg` contains the value of the `TEMPLATECONF` variable.
The `bblayers.conf` and `local.conf` files are copied over from the sample
files present in the path pointed to by the `TEMPLATECONF` variable.

Building an image
^^^^^^^^^^^^^^^^^

The command to build an image is:

.. prompt:: bash $

	DISTRO=rity-demo bitbake MACHINE=<machine> rity-demo-image

`bitbake` is the tool used to build images and packages. Setting the DISTRO and
MACHINE variables on the command line allows to override their values set
in :ref:`yocto:local.conf`.

The image will be stored in `build/tmp/deploy/images/<machine>`.

Configuration files
-------------------

local.conf
^^^^^^^^^^

`local.conf` contains your local build configuration.

Instead of setting the `DISTRO` and `MACHINE` variable on the `bitbake` command
line, you can also set them in `local.conf`.

Please refer to the `official Yocto Documentation <https://docs.yoctoproject.org/singleindex.html#build-conf-local-conf>`__ to read more about `local.conf`.

bblayers.conf
^^^^^^^^^^^^^

The `bblayers.conf` file is mostly used to specify the path to all the `Yocto
meta layer` that `bitbake` will parse.

Please refer to the `official Yocto Documentation <https://docs.yoctoproject.org/singleindex.html#build-conf-bblayers-conf>`__ to read more about `bblayers.conf`.
