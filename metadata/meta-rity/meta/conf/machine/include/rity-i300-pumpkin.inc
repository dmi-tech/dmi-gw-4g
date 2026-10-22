KERNEL_DEVICETREE_OVERLAYS_AUTOLOAD += " \
	${@bb.utils.contains("MACHINE_FEATURES", "vesper-hat", "vesper.dtbo", "", d)} \
	${@bb.utils.contains("I300_PUMPKIN_AUDIO_CONF", "i2s", "audio-i2s.dtbo", "", d)} \
"
