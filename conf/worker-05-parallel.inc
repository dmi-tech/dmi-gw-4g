# Parallel build configuration for worker-05: i5-12600K, 16 logical CPUs, ~15 GiB RAM.
BB_NUMBER_THREADS = "16"
BB_NUMBER_PARSE_THREADS = "16"
PARALLEL_MAKE = "-j 16"
PARALLEL_MAKEINST = "${PARALLEL_MAKE}"

# Retain the previously configured GCC ceiling for this worker.
PARALLEL_MAKE:pn-gcc-cross-aarch64 = "-j 10"
PARALLEL_MAKEINST:pn-gcc-cross-aarch64 = "-j 10"
PARALLEL_MAKE:pn-gcc-cross = "-j 10"
PARALLEL_MAKEINST:pn-gcc-cross = "-j 10"

# The previous local.conf records races in these recipes at higher parallelism.
PARALLEL_MAKE:pn-cmake-native = "-j 4"
# glibc passes its job limit through PARALLELMFLAGS, not make's outer -j.
PARALLEL_MAKE:pn-glibc = ""
EGLIBCPARALLELISM:pn-glibc = 'PARALLELMFLAGS="-j 4"'
