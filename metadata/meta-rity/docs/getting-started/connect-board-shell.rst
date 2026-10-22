Connect to the board shell
==========================

UART
----

Most development boards have an UART connected to a FTDI chip which expose
the UART serial console as an USB serial device.


Prerequisites
^^^^^^^^^^^^^

Before you can connect to the device you must check that your user account if
part of the `dialout` group:

.. parsed-literal::

	$ groups | grep dialout
	**dialout** cdrom floppy sudo audio dip video plugdev netdev bluetooth

If your users is not part of the `dialout` group, then you must add yourself
to that group:

.. prompt:: bash $

	sudo usermod -a -G dialout $USER

This last command requires you to log out and log back in to your account to be
in effect.

Connect to UART shell
^^^^^^^^^^^^^^^^^^^^^

You can now connect to the UART using the following command:

.. prompt:: bash $

	picocom -b 921600 /dev/ttyUSB0

or using

.. prompt:: bash $

	screen /dev/ttyUSB0 921600

.. note::

	In general the baudrate for MediaTek boards is `921600`, but it can
	sometimes differ, please refer to the
	:doc:`BSP documentation <bsp:boards/index>` to check the baudrate
	for a particular board.

.. note::

	Depending on your system, you may have more than one USB serial device
	connected, which means the board might not be connected to /dev/ttyUSB0,
	in that case please try other ttyUSB devices (i.e. /dev/ttyUSB1,
	/dev/ttyUSB2, ...)

SSH over USB
------------

RITY is shipping a SSH server and is configured to expose an SSH connection
over USB. Please check the :doc:`BSP documentation <bsp:boards/index>` to see which USB port can be used
for SSH connections.

Connecting using the board IP
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

All the boards are configured with the following static IP: `192.168.96.1`.

You can connect to the board using the following command:

.. prompt:: bash $ auto

	$ ssh root@192.168.96.1
	root@i300a-pumpkin:~#

Connecting using the board avahi name
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

RITY SDK is also including by default the
`avahi daemon <https://www.avahi.org/>`_ to provide `zeroconf <https://en.wikipedia.org/wiki/Zero-configuration_networking>`_.

The name of the board is by default the value of the `MACHINE` variable that
was used to build the SDK. The `MACHINE` variable is used to choose the board
for which the SDK is being built for. Please check the
:doc:`BSP documentation <bsp:boards/index>` to see the list of all
the boards/MACHINE available.

For instance you can connect to the `i500-pumpkin` board using the following
command:

.. prompt:: bash $ auto

	$ ssh root@pumpkin-i500.local
	root@i500-pumpkin:~#

.. hint::

	It is recommended to change the avahi name of the board to something
	unique if several people work on the same board and in the same network.
