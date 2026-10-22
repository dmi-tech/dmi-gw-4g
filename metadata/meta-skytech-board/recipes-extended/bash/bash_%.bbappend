# Host GCC 15 defaults to C23, where "xmalloc()" means "no arguments", so the
# build-time helper mkbuiltins (compiled with BUILD_CC) fails to compile.
BUILD_CFLAGS:append = " -std=gnu17"
