# Bắt buộc có OpenSSL host (native) để build fiptool
DEPENDS:append = " openssl-native"

# KHÔNG export LDFLAGS/LDLIBS toàn cục để tránh rò sang link target (BL31)

do_compile:prepend() {
    set -eux

    MF="${S}/tools/fiptool/Makefile"
    if [ ! -f "${MF}" ]; then
        echo "ERROR: Not found ${MF}" >&2
        exit 1
    fi

    echo "=== DMI PATCH: replace -lcrypto with absolute libcrypto.so path ==="
    echo "RECIPE_SYSROOT_NATIVE=${RECIPE_SYSROOT_NATIVE}"

    OPENSSL_DIR="${RECIPE_SYSROOT_NATIVE}/usr"

    # Tìm libcrypto.so trong sysroot native (ưu tiên lib64)
    CRYPTO_SO=""
    for p in "${OPENSSL_DIR}/lib64/libcrypto.so" \
             "${OPENSSL_DIR}/lib64/libcrypto.so."* \
             "${OPENSSL_DIR}/lib/libcrypto.so" \
             "${OPENSSL_DIR}/lib/libcrypto.so."* ; do
        if [ -f "$p" ]; then
            CRYPTO_SO="$p"
            break
        fi
    done

    if [ -z "${CRYPTO_SO}" ]; then
        echo "ERROR: libcrypto.so not found under ${OPENSSL_DIR}/lib{,64}" >&2
        ls -l "${OPENSSL_DIR}"/lib* || true
        exit 1
    fi

    INC="${OPENSSL_DIR}/include"
    RPATH_DIR="$(dirname "${CRYPTO_SO}")"

    # 1) Thêm include path cho host compile
    # 2) Thêm rpath để lúc chạy fiptool (host) tìm được .so trong sysroot native
    # 3) Thay -lcrypto bằng đường dẫn tuyệt đối tới libcrypto.so
    #    - Nếu trong Makefile có 'LDLIBS ?= -lcrypto' hoặc có '-lcrypto' ở chỗ khác -> thay hết
    {
      echo ""
      echo "# --- injected by meta-custom-board bbappend ---"
      echo "CFLAGS  += -I${INC}"
      echo "LDFLAGS += -Wl,-rpath,${RPATH_DIR}"
      echo "# --- end injected ---"
      echo ""
    } >> "${MF}"

    # Thay mọi '-lcrypto' thành file tuyệt đối
    sed -i "s|-lcrypto|${CRYPTO_SO}|g" "${MF}"

    # Xoá build cũ để chắc chắn relink với LDLIBS/LDFLAGS mới
    rm -f "${S}/tools/fiptool/fiptool" \
          "${S}/tools/fiptool/fiptool.o" \
          "${S}/tools/fiptool/tbbr_config.o" || true

    # In đoạn cuối để xác nhận
    tail -n 30 "${MF}" || true
}

# Đảm bảo dùng gcc host và bật verbose để thấy lệnh link của fiptool
EXTRA_OEMAKE:append = " HOSTCC='gcc' HOSTLD='gcc' V=1"
