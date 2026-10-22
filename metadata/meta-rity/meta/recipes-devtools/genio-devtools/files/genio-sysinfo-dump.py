#!/usr/bin/env python3

import os, sys
import argparse
import subprocess

def checkargs(args):
    return True

def do_cmd_dump(cmd, f):
    try:
        p = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)

        line = p.stdout.readline()
        while line:
            line = line.decode('utf-8').rstrip('\r\n')
            print(line, file=f)
            line = p.stdout.readline()

        p.communicate()
    except subprocess.CalledProcessError as e:
        print('Fail to run cmd: %s' % str(e), file=sys.stderr)
        return False

    return True

def print_title(title, f):
    msg_sz = len(title) + 4 * 2
    hdr_sz = msg_sz + 2

    print('', file=f)
    print('#' * hdr_sz, file=f)
    print('#%s%s%s#' % (' ' * 4, title.upper(), ' ' * 4), file=f)
    print('#' * hdr_sz, file=f)

def dump_board(f):
    print_title('BOARD', f)
    cmd = 'strings /sys/firmware/devicetree/base/model'
    if not do_cmd_dump(cmd, f):
        return False

    return True

def dump_buildinfo(f):
    print_title('BUILDINFO', f)
    cmd = 'cat /etc/buildinfo'
    if not do_cmd_dump(cmd, f):
        return False

    return True

def dump_os_release(f):
    print_title('OS_RELEASE', f)
    cmd = 'cat /etc/os-release'
    if not do_cmd_dump(cmd, f):
        return False

    return True

def dump_kernelver(f):
    print_title('KERNEL_VER', f)
    cmd = 'cat /proc/version'
    if not do_cmd_dump(cmd, f):
        return False

    return True

def dump_uptime(f):
    print_title('UPTIME', f)
    cmd = 'uptime'
    if not do_cmd_dump(cmd, f):
        return False

    return True

def dump_mem_free(f):
    print_title('MEMFREE', f)
    cmd = 'free'
    if not do_cmd_dump(cmd, f):
        return False

    return True

def dump_disk_free(f):
    print_title('DISKFREE', f)
    cmd = 'df'
    if not do_cmd_dump(cmd, f):
        return False

    return True

def dump_mount(f):
    print_title('MOUNTS', f)
    cmd = 'mount'
    if not do_cmd_dump(cmd, f):
        return False

    return True

def dump_processlist(f):
    print_title('PROCESS_LIST', f)
    cmd = 'ps aux'
    if not do_cmd_dump(cmd, f):
        return False

    return True

def dump_uboot_env(f):
    print_title('UBOOTENV', f)
    cmd = 'fw_printenv'
    if not do_cmd_dump(cmd, f):
        return False

    return True

def dump_network(f):
    print_title('NETWORK', f)
    if not do_cmd_dump('ip addr', f):
        return False

    print('', file=f)
    if not do_cmd_dump('ip route', f):
        return False

    print('', file=f)
    cmd = "cat /etc/resolv.conf | sed -e '/^#/d'"
    if not do_cmd_dump(cmd, f):
        return False

    return True

def dump_irq(f):
    print_title('IRQ', f)
    cmd = 'cat /proc/interrupts'
    if not do_cmd_dump(cmd, f):
        return False

    return True

def dump_iomem(f):
    print_title('IOMEM', f)
    cmd = 'cat /proc/iomem'
    if not do_cmd_dump(cmd, f):
        return False

    return True

def dump_dmesg(f):
    print_title('KERNELMSG (LAST 100)', f)
    cmd = 'dmesg --ctime | tail -n 100'
    if not do_cmd_dump(cmd, f):
        return False

    return True


if __name__ == '__main__':

    desc = 'The system info dump script for Genio platform'
    p = argparse.ArgumentParser(description=desc)
    p.add_argument('--output', '-o', metavar='FILE', help='The output file path (default: sysinfo.dump)', default='sysinfo.dump')
    args = p.parse_args()

    if not checkargs(args):
        sys.exit(1)

    print('Dump system info to the file %s' % args.output)
    has_error = False
    with open(args.output, 'w') as f:

        if not dump_board(f):
            has_error = True

        if not dump_buildinfo(f):
            has_error = True

        if not dump_os_release(f):
            has_error = True

        if not dump_kernelver(f):
            has_error = True

        if not dump_uptime(f):
            has_error = True

        if not dump_mem_free(f):
            has_error = True

        if not dump_disk_free(f):
            has_error = True

        if not dump_mount(f):
            has_error = True

        if not dump_network(f):
            has_error = True

        if not dump_processlist(f):
            has_error = True

        if not dump_uboot_env(f):
            has_error = True

        if not dump_iomem(f):
            has_error = True

        if not dump_irq(f):
            has_error = True

        if not dump_dmesg(f):
            has_error = True
