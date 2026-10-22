#!/bin/sh

# ----- XDG_RUNTIME_DIR -----
if [ -z "$XDG_RUNTIME_DIR" ]; then
    uid="$(id -u)"
    export XDG_RUNTIME_DIR="/run/user/$uid"
fi

if [ ! -d "$XDG_RUNTIME_DIR" ]; then
    mkdir -p "$XDG_RUNTIME_DIR" 2>/dev/null || true
    chmod 700 "$XDG_RUNTIME_DIR" 2>/dev/null || true
fi

# ----- WAYLAND_DISPLAY -----
if [ -z "$WAYLAND_DISPLAY" ]; then
    if [ -S "$XDG_RUNTIME_DIR/wayland-1" ]; then
        export WAYLAND_DISPLAY=wayland-1
    elif [ -S "$XDG_RUNTIME_DIR/wayland-0" ]; then
        export WAYLAND_DISPLAY=wayland-0
    fi
fi
