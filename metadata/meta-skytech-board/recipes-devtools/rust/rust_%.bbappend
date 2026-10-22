# Host GCC 15 (Ubuntu 26.04) no longer pulls <cstdint> in transitively, so the
# LLVM 17 headers used by rustc_llvm fail with "'uint64_t' was not declared".
CXXFLAGS:append:class-native = " -include cstdint"
