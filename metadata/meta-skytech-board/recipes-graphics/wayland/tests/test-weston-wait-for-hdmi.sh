#!/bin/sh

set -eu

script_dir=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
recipe_dir=$(CDPATH= cd -- "${script_dir}/.." && pwd)
helper="${recipe_dir}/files/weston-wait-for-hdmi"
service="${recipe_dir}/files/weston.service"
recipe="${recipe_dir}/weston-init.bbappend"
test_root=$(mktemp -d)
helper_pid=

cleanup() {
    if [ -n "${helper_pid}" ]; then
        kill "${helper_pid}" 2>/dev/null || true
        wait "${helper_pid}" 2>/dev/null || true
    fi
    rm -rf "${test_root}"
}
trap cleanup EXIT HUP INT TERM

fail() {
    echo "FAIL: $*" >&2
    exit 1
}

mkdir -p "${test_root}/drm/card0-HDMI-A-1"
printf '%s\n' disconnected > "${test_root}/drm/card0-HDMI-A-1/status"

cat > "${test_root}/fake-weston" <<'EOF'
#!/bin/sh
printf '%s\n' "$@" > "${WESTON_TEST_OUTPUT}"
EOF
chmod +x "${test_root}/fake-weston"

if WESTON_DRM_CLASS="${test_root}/drm" "${helper}" > "${test_root}/no-command.log" 2>&1; then
    fail "helper accepted an empty Weston command"
else
    result=$?
    [ "${result}" -eq 64 ] || fail "empty Weston command returned ${result}, expected 64"
fi

WESTON_DRM_CLASS="${test_root}/drm" \
WESTON_WAIT_INTERVAL=1 \
WESTON_TEST_OUTPUT="${test_root}/argv" \
    "${helper}" "${test_root}/fake-weston" --socket=wayland-1 &
helper_pid=$!

sleep 1
kill -0 "${helper_pid}" 2>/dev/null || fail "helper exited while HDMI was disconnected"
[ ! -e "${test_root}/argv" ] || fail "Weston was started while HDMI was disconnected"

printf '%s\n' connected > "${test_root}/drm/card0-HDMI-A-1/status"

attempt=0
while kill -0 "${helper_pid}" 2>/dev/null && [ "${attempt}" -lt 4 ]; do
    sleep 1
    attempt=$((attempt + 1))
done
wait "${helper_pid}" || fail "helper/Weston command failed after HDMI connected"
helper_pid=

[ -f "${test_root}/argv" ] || fail "Weston was not started after HDMI connected"
[ "$(cat "${test_root}/argv")" = "--socket=wayland-1" ] || fail "Weston arguments were not preserved"

grep -q '^StartLimitIntervalSec=60$' "${service}" || fail "service has no 60-second start limit"
grep -q '^StartLimitBurst=3$' "${service}" || fail "service has no three-attempt burst limit"
grep -q '^Restart=always$' "${service}" || fail "service does not return to HDMI wait after every Weston exit"
grep -q '^ExecStart=/usr/bin/weston-wait-for-hdmi /usr/bin/weston --socket=wayland-1$' "${service}" || fail "service does not launch Weston through helper"
if grep -q -- '--log=' "${service}"; then
    fail "service still directs Weston logs to a file"
fi
grep -q 'file://weston-wait-for-hdmi' "${recipe}" || fail "helper is missing from SRC_URI"
grep -q 'install -m 0755.*weston-wait-for-hdmi' "${recipe}" || fail "helper is not installed executable"

echo "PASS: weston HDMI wait helper and service policy"
