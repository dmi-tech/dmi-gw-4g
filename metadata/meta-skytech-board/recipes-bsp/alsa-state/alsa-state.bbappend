# Match the analog-only MT8365 card; the EVK state contains an HDMI PCM
# channel-map control that does not exist when the HDMI DAI is removed.
# Keep the plug PCM in S16_LE, the INT ADDA playback format advertised by
# the machine's backend, even when the decoder supplies floating-point audio.
FILESEXTRAPATHS:prepend:skytech-genio-350 := "${THISDIR}/skytech-genio-350:"
