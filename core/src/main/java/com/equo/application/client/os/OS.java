package com.equo.application.client.os;

public class OS {
    private enum OSType {
        OSUndefined,
        OSLinux,
        OSWindows,
        OSMacintosh,
        OSUnknown,
    }

    private static OSType osType = OSType.OSUndefined;

    public static boolean isWindows() {
        return getOSType() == OSType.OSWindows;
    }

    public static boolean isMacintosh() {
        return getOSType() == OSType.OSMacintosh;
    }

    public static boolean isLinux() {
        return getOSType() == OSType.OSLinux;
    }

    private static OSType getOSType() {
        if (osType == OSType.OSUndefined) {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.startsWith("windows"))
                osType = OSType.OSWindows;
            else if (os.startsWith("linux"))
                osType = OSType.OSLinux;
            else if (os.startsWith("mac"))
                osType = OSType.OSMacintosh;
            else
                osType = OSType.OSUnknown;
        }
        return osType;
    }
}
