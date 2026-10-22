SUMMARY = "Connectivity, modem, multimedia, and storage utilities for Skytech Genio 350"
DESCRIPTION = "Runtime utilities for USB 4G modems, PPP, ECM/NCM, multimedia, and SD cards."
LICENSE = "MIT"

inherit packagegroup

RDEPENDS:${PN} = " \
    ppp \
    ppp-tools \
    quectel-eg800k-support \
    neoway-n706b-support \
    modemmanager \
    libqmi \
    libmbim \
    usb-modeswitch \
    usb-modeswitch-data \
    networkmanager \
    networkmanager-nmcli \
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
    gstreamer1.0-plugins-base-apps \
    gstreamer1.0-plugins-good \
    gstreamer1.0-plugins-bad \
    gstreamer1.0-plugins-ugly \
    gstreamer1.0-libav \
    ffmpeg \
    v4l-utils \
    usbutils \
    net-tools \
    iproute2 \
    ethtool \
    iputils \
    procps \
    htop \
    util-linux \
    dosfstools \
    e2fsprogs \
    e2fsprogs-resize2fs \
    parted \
"
