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

        /**
         * Returns the value of the enum instance.
         *
         * @return The method is returning the value of the enum instance.
         */
        public String getDir() {
            return this.dir;
        }
    }

    /**
     * Returns the path of the user's home directory.
     *
     * @return The value of the system property "user.home", which represents the user's home directory.
     */
    default String userHome() {
        return System.getProperty("user.home");
    }

    /**
     * Returns the path of the cache directory based on the rootConfig.
     *
     * @param rootConfig The rootConfig directory where the cache app directory will be created.
     *
     * @return The path of the cache directory based on the rootConfig.
     */
    Path cacheDirHome(String rootConfig);

    /**
     * Returns the path of the data directory based on the rootConfig.
     *
     * @param rootConfig The rootConfig directory where the data app directory will be created.
     *
     * @return The path of the data directory based on the rootConfig.
     */
    Path dataDirHome(String rootConfig);

    /**
     * Returns the path of the state directory based on the rootConfig.
     *
     * @param rootConfig The rootConfig directory where the state app directory will be created.
     *
     * @return The path of the state directory based on the rootConfig.
     */
    Path stateDirHome(String rootConfig);

    /**
     * Returns the path of the runtime directory based on the rootConfig.
     *
     * @param rootConfig The rootConfig directory where the runtime app directory will be created.
     *
     * @return The path of the runtime directory based on the rootConfig.
     */
    Path runtimeDirHome(String rootConfig);

    /**
     * Returns the path of the config directory based on the rootConfig.
     *
     * @param rootConfig The rootConfig directory where the config app directory will be created.
     *
     * @return The path of the config directory based on the rootConfig.
     */
    Path configDir(String rootConfig);

    /**
     * Returns an instance of a class based on the operating system being used.
     *
     * @return The method returns an instance of the `CommonFolders` class, depending on the operating system in use. If
     * it's Windows, it returns an instance of the `Windows` class; if it's Macintosh, it returns an instance of the
     * `Mac` class. Otherwise, it returns an instance of the `Linux` class.
     */
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
