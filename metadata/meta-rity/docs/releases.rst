RITY releases
=============

The RITY SDK supports the following Yocto release:

	* Yocto 4.0, codename: Kirkstone (LTS)

RITY is getting a new release about every 3 months, the latest one is
|release|.

Download the RITY SDK
---------------------

The RITY SDK can be downloaded using `repo` as follows:

.. parsed-literal::

    $ mkdir rity; cd rity
    $ repo init -u https://gitlab.com/mediatek/aiot/bsp/manifest.git -b |release|
    $ repo sync

You can replace '|release|' with any of the :ref:`releases:releases tags`
or :ref:`releases:development branches`.

Development branches
--------------------

If you wish to use bleeding edge code you can use the RITY development branches,
It is not recommended to use development branches unless you need
a feature that is not yet in any releases or wish to participate in the
development of the RITY SDK.

+-----------------+---------------+
| Branch name     | Status        |
+=================+===============+
| rity/kirkstone  | Active        |
+-----------------+---------------+
| rity/honister   | Obsolete      |
+-----------------+---------------+
| rity/hardknott  | Obsolete      |
+-----------------+---------------+
| rity/dunfell    | Obsolete      |
+-----------------+---------------+

Only one development branch is active at a time. The other branches will
receive occasional backport from the active branch.

Releases tags
----------------

List of all the RITY releases:

+--------------------+---------------+-----------+-----------------------------------------------------------------------------------------------------+
| Tag                | Yocto Release | Status    |                                   Links                                                             |
+====================+===============+===========+=====================================================================================================+
| rity-v21.3         | dunfell       |           | `rity-v21.3 <https://gitlab.com/mediatek/aiot/rity/meta-rity/-/tree/rity-v21.3>`_                   |
+--------------------+---------------+-----------+-----------------------------------------------------------------------------------------------------+
|rity-v21.3.1        | dunfell       |           | `rity-v21.3.1 <https://gitlab.com/mediatek/aiot/rity/meta-rity/-/tree/rity-v21.3.1>`_               |
+--------------------+---------------+-----------+-----------------------------------------------------------------------------------------------------+
| rity-dunfell-v22.0 | dunfell       | Supported |  `rity-dunfell-v22.0 <https://gitlab.com/mediatek/aiot/rity/meta-rity/-/tree/rity-dunfell-v22.0>`_  |
+--------------------+---------------+-----------+-----------------------------------------------------------------------------------------------------+

