# DMI 4G Gateway — Genio 350

Source Yocto cho board Skytech Genio 350, được nhập từ workspace
`dmi@worker-05:/mnt/sdb1/yocto` ngày 22/10/2026.
Đây là snapshot source đang dùng để build; lịch sử Git upstream không nằm
trong lần nhập đầu tiên này.

- Poky 5.0.8 (scarthgap), BitBake 2.8.0.
- Machine: `skytech-genio-350`.
- Distro: `distro-skytech-genio-350`.
- Image: `skytech-genio-350-image`.

## Nội dung

- `metadata/`: các layer Yocto, BSP MediaTek/RITY và layer tùy chỉnh
  `meta-skytech-board`.
- `build/conf/`: cấu hình của build image hiện tại.
- `conf/`: cấu hình build tại thư mục gốc và
  `worker-05-parallel.inc` dùng chung.
- Downloads, sstate, cache, log và image đã build được bỏ qua trong Git.
  Hai model TensorFlow Lite trong layer upstream `meta-nn` được giữ lại.

## Build trên worker-05

```bash
cd /mnt/sdb1/yocto
test -x /home/dmi/.local/bin/python3.12
source metadata/poky/oe-init-build-env build
bitbake skytech-genio-350-image
```

BitBake launcher hiện dùng Python 3.12 tại
`/home/dmi/.local/bin/python3.12`, đồng thời đưa thư mục interpreter này
lên đầu PATH cho tiến trình con. Đây là cấu hình đang dùng trên worker-05
để tránh lỗi multiprocessing của Python 3.14.

Nếu clone sang vị trí khác, cập nhật các đường dẫn
`/mnt/sdb1/yocto` trong `build/conf/bblayers.conf` và đường dẫn Python
trong `metadata/poky/bitbake/bin/bitbake` theo máy build.
`conf/worker-05-parallel.inc` đặt 16 task/thread và giữ các giới hạn
riêng của GCC, CMake và glibc; không đặt ngưỡng pressure CPU/RAM/I/O.

Image được xuất tại:

```text
build/tmp/deploy/images/skytech-genio-350/
```

Các định dạng chính: `.wic.img`, `.tar.xz`, `.aiotflash.tar`.
Log build gần nhất tại thời điểm nhập source ghi nhận 8801 task đều thành công.

## Kiểm tra layer tùy chỉnh

```bash
sh metadata/meta-skytech-board/recipes-core/images/tests/test-no-duplicate-adbd.sh
sh metadata/meta-skytech-board/recipes-graphics/wayland/tests/test-weston-wait-for-hdmi.sh
```

## Upstream và giấy phép

Giữ nguyên các thông báo bản quyền và giấy phép theo từng layer/package.
Các khóa mẫu/debug trong layer upstream được giữ nguyên từ source công khai;
chúng không phải khóa riêng được tạo cho sản phẩm DMI.
