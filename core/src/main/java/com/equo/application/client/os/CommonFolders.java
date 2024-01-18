package com.equo.application.client.os;

import java.nio.file.Path;

public interface CommonFolders {
    String EQUO = "equo";

    enum Dir {
        CACHE("Cache"),
        CONFIG("Config"),
        DATA("Data"),
        STATE("State"),
        RUNTIME("Runtime");

        private final String dir;

        Dir(String dir) {
            this.dir = dir;
        }

        public String getDir() {
            return this.dir;
        }
    }

    default String userHome() {
        return System.getProperty("user.home");
    }

    Path cacheDirHome(String rootConfig);

    Path dataDirHome(String rootConfig);

    Path stateDirHome(String rootConfig);

    Path runtimeDirHome(String rootConfig);

    Path configDir();

    static CommonFolders getInstance() {
        if (OS.isWindows()) {
            return new Windows();
        } else if (OS.isMacintosh()) {
            return new Mac();
        } else {
            return new Linux();
        }
    }

}
