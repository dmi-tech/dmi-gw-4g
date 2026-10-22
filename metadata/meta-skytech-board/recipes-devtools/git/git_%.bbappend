# Pseudo on this host cannot resolve tar's relative openat() calls when both
# Git's source and build directories are ignored during do_install.
python __anonymous() {
    ignored = d.getVar("PSEUDO_IGNORE_PATHS").split(",")
    tracked = {d.getVar("S"), d.getVar("B")}
    d.setVar("PSEUDO_IGNORE_PATHS", ",".join(path for path in ignored if path not in tracked))
    d.delVarFlag("do_install", "fakeroot")
}
