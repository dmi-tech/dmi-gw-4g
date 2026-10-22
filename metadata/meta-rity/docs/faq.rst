Frequently Asked Questions
==========================

1. My rootfs is too small, how to make it larger?
-------------------------------------------------

The BSP does not specify any storage size, so the rootfs will be just large
enough to contain the content built by the yocto image. If you need to
increase the size the size of the rootfs you can add the following to your
`local.conf`:

.. code::

	IMAGE_ROOTFS_EXTRA_SPACE = "500000" # KBytes

`IMAGE_ROOTFS_EXTRA_SPACE`_ can be set to increase
the amount of free disk space in the rootfs partition.

For more information you can check the `Yocto Reference Manual`_.

.. _IMAGE_ROOTFS_EXTRA_SPACE: https://www.yoctoproject.org/docs/latest/ref-manual/ref-manual.html#var-IMAGE_ROOTFS_EXTRA_SPACE
.. _Yocto Reference Manual: https://www.yoctoproject.org/docs/latest/ref-manual/ref-manual.html#idm46031661356992

2. My home partition is too small, how to make it larger?
---------------------------------------------------------

You can set the home partition size by setting the following variable in
your `local.conf`:

.. code::

	IMAGE_HOME_SIZE = "500M"

3. How to add additional applications to my image
-------------------------------------------------

If you want to add new applications to your image you can set the following
variable in your `local.conf`:

.. code::

	IMAGE_INSTALL:append = " \
		gdb                  \
		htop                 \
	"

The example above will add `gdb` and `htop` to the image that gets built by
`bitbake`.

4. Connecting to the board by ssh over USB does not work on Windows
-------------------------------------------------------------------

By default the BSP uses the USB gadget `adb+rndis` to provide both debug and
network interface over USB. Install Google's USB driver for `adb` interface,
and Windows built-in `rndis` driver.

For more information you can check the `Get the Google USB Driver`_.

.. _Get the Google USB Driver: https://developer.android.com/studio/run/win-usb

5. Configuring an USB gadget port for debugging
-----------------------------------------------

Since some boards support multiple gadget ports, such as the Genio 700
which has two udc device nodes in the
`/sys/class/udc` path (e.g. 11201000.usb1 and 112b1000.usb),
we need to specify a default interface to enable the `adb` functionality.
For this, we have introduced a new mechanism that allows `usbgadget.service`
to set the correct port according to the configuration file.
Users can specify the default gadget port by configuring `/etc/usbgadget.conf`.

.. code::

    example:
        File: /etc/usbgadget.conf
        Content: 112b1000.usb ($BASEADDR.$NODENAME)

6. System service for resetting USB hub on genio-700-evk board
--------------------------------------------------------------

According to the datasheet, the Genio 700 USB hub requires a minimum reset time of 3ms.
this can be generated using a programmable delay supervision device.
To meet this requirement, we have added a new startup service `usbhub.service`,
which uses the command `gpioset` to control gpio enable/disable to reset
the USB hub so that the usb hub can be run successfully.

For more information you can check the `TUSB8020B Two-Port USB 3.0 Hub datasheet`_.

.. _TUSB8020B Two-Port USB 3.0 Hub datasheet: https://www.ti.com/lit/gpn/tusb8020b

6. System service for wwan-5g card on genio-700-evk and genio-1200-evk board
----------------------------------------------------------------------------

The wwan-5g and usbhub services are used to manage a WWAN 5G card (Quectel
RM500K) that is connected to the system via an M.2 slot. The wwan-5g service
is responsible for controlling the WWAN 5G card, while the usbhub service
manages the USB hub that the card is connected to. A startup service
`wwan-5g.service` uses the command `gpioset` to control `RESET` and
`FULL_CARD_POWER_OFF` pins to enable/disable the card.

The wwan5g service depends on the usbhub service in order to function properly.
Please ensure that they are running in the correct order.

Note: genio-1200-evk uses 2 transisters to control the module instead of using
direct pin control as genio-700-evk. The enable/disable definition of pinctrls
were different from each board.
