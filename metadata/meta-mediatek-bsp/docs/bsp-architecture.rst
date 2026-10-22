BSP architecture
================

The RITY BSP has the following booting architecture:

.. mermaid::

   graph TB
      subgraph SoC
         bl1[ROM Code]
      end

      subgraph MediaTek BSP
         bl2["ARM Trusted Firmware (BL2)"]
         bl31["ARM Trusted Firmware (BL31)"]
         bl32["OP-TEE (BL32)"]
         bl33["Das U-Boot (BL33)"]
         linux[Linux]
      end

      subgraph Userspace
         yocto[Yocto]
         android[Android]
      end

      bl1 --> bl2
      bl2 --1--> bl31
      bl31 -.2.-> bl2
      bl2 --3--> bl32
      bl32 -.4.-> bl2
	  bl2 --5--> bl33
      bl33 --> linux
      linux --> yocto
      linux --> android

MediaTek SoC's ROM code
-----------------------

The MediaTek SoC ROM code will try to boot from different medias:

* eMMC
* NAND
* USB
* UFS

The RITY SDK only allows to boot from eMMC. NAND and UFS are not yet
supported. USB is only used as part of the flashing process but cannot be
used alone to boot to a Linux userspace.

eMMC Boot
^^^^^^^^^

When booting the SoC will chose which media to boot from. If the eMMC boot
is chosen the bootrom will look at the first boot partition to look for a
second stage bootloader.

In case the ROM code fails to find a valid boot partition or valid second
stage bootloader, it will automatically go into :ref:`bsp-architecture:USB Boot`.

USB Boot
^^^^^^^^

In RITY, the ROM code USB boot is used only for flashing.

BL2 (TF-A)
----------

The second stage bootloader is based on `Trusted Firmware A (TF-A) <https://www.trustedfirmware.org/projects/tf-a/>`_.
The RITY BSP ships pre-built binaries for second stage bootloaders.

BL2 first initializes the hardware:

* Initialize the system timer
* Initialize and turn on the PLLs
* Initialize the PMIC wrap component in order to be able to talk to the PMIC
* Sends a `baseband power-up` signal to the PMIC to notify him of a sucessfull power up.
* Initialize the DDR
* Initialize the eMMC

Once the hardware is initialized, BL2 will read 4MB of memory stored at
offset 0x80000 of the eMMC. This section should contains the `fip.bin` file.

Firmware Image Package (FIP) is a packaging format used by TF-A to package
firmware images. In RITY the FIP contains BL31, BL32, BL33 and optionally
some certificates when secure boot is enabled.

BL2 will read the `fip` package and execute each of the binary it contains.

BL31 (TF-A)
-----------

BL31 is provided by TF-A and provides the `PSCI routines <https://developer.arm.com/architectures/system-architectures/software-standards/psci>`_.
Unlike BL2 which won't get used anymore once BL33 is loaded, BL31 will stay
in DDR until a power off or a reboot.

Once BL31 is finished to initialize, it will jump back to BL2.

Source code: https://gitlab.com/mediatek/aiot/bsp/trusted-firmware-a

BL32 (OP-TEE)
-------------

BL32 is the secure OS that runs in `TrustZone <https://developer.arm.com/ip-products/security-ip/trustzone>`_.
RITY BSP is using by default `OP-TEE <https://www.op-tee.org/>`_ trusted OS.

Like BL31, OP-TEE OS stays in DDR until a power off or a reboot.

Source code: https://gitlab.com/mediatek/aiot/bsp/optee-os

BL33 (U-Boot)
-------------

U-Boot is the third and last stage bootloader in the RITY boot architecture. It
is used to load and boot the kernel from the `kernel` partition.

In RITY the kernel must be encapsulated into a Flattened Image Tree (FIT) image.
The FIT image contains the kernel binary, a Device Tree Blob (DTB), and
optionally some Device Tree Blob Overlay (DTBO).

The DTBOs are used to provide optional features to a board, for instance for
cameras, display, or any daughterboard. U-Boot looks at its `boot_conf`
environment variable in order to know which (if any) overlay it must load and
merge to the main DTB. It is possible to set this `boot_conf` variable from
the U-Boot shell, or when flashing.

U-Boot can also be used to flash (via fastboot), or to boot the kernel and/or
rootfs using an USB connection. Please refer to the boards documentation in
order to know which features are supported for your specific board.

Source code: https://gitlab.com/mediatek/aiot/bsp/u-boot

Linux
-----

The `Linux kernel <https://www.kernel.org/>`_ is the main OS running. It will
load the root filesystem (rootfs) and starts running the `init` process.

The following versions of Linux are currently supported:

+----------------+---------------+
| Kernel version | Branch name   |
+================+===============+
|      v5.10     | mtk-v5.10     |
+----------------+---------------+
|      v5.15     | mtk-v5.15-dev |
+----------------+---------------+

By default the latest Linux release will be built, if you wan to use an older
supported kernel you can set the `PREFERRED_VERSION_linux-mtk` variable
in your `local.conf`.

For example if you want to use the v5.10 branch you can set:

.. code-block::

	PREFERRED_VERSION_linux-mtk = "5.10%"
