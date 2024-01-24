package com.equo.application.client.os;

import java.nio.file.Path;

public class Linux implements CommonFolders {
    public static final String CACHE = ".cache";
    public static final String CONFIG = ".config";
    public static final String LOCAL = ".local";
    public static final String SHARE = "share";
    public static final String STATE = "state";
    public static final String RUN = "/run";

    public static final String XDG_CACHE_HOME = "XDG_CACHE_HOME";
    public static final String XDG_DATA_HOME = "XDG_DATA_HOME";
    public static final String XDG_STATE_HOME = "XDG_STATE_HOME";

    private String getCacheHome() {
        String cacheHome = System.getenv(XDG_CACHE_HOME);
        if (cacheHome == null || cacheHome.trim().isEmpty()) {
            cacheHome = Path.of(userHome(), CACHE).toString();
        }
        return cacheHome;
    }

    private String getDataHome() {
        String dataHome = System.getenv(XDG_DATA_HOME);
        if (dataHome == null || dataHome.trim().isEmpty()) {
            dataHome = Path.of(userHome(), LOCAL, SHARE).toString();
        }
        return dataHome;
    }

    private String getStateHome() {
        String stateHome = System.getenv(XDG_STATE_HOME);
        if (stateHome == null || stateHome.trim().isEmpty()) {
            stateHome = Path.of(userHome(), LOCAL, STATE).toString();
        }
        return stateHome;
    }

    @Override
    public Path cacheDirHome(String rootConfig) {
        return Path.of(getCacheHome(), rootConfig);
    }

    @Override
    public Path dataDirHome(String rootConfig) {
        return Path.of(getDataHome(), rootConfig);
    }

    @Override
    public Path stateDirHome(String rootConfig) {
        return Path.of(getStateHome(), rootConfig);
    }

    @Override
    public Path runtimeDirHome(String rootConfig) {
        return Path.of(RUN, rootConfig);
    }

    @Override
    public Path configDir() {
        return Path.of(userHome(), CONFIG, EQUO);
    }

}
