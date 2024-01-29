package com.equo.application.client.os;

import java.nio.file.Path;

public class Mac implements CommonFolders {

    public static final String LIBRARY = "Library";
    public static final String CACHES = "Caches";
    public static final String APP_SUPPORT = "Application Support";
    public static final String TEMP = "/tmp";

    private String getLibraryPath() {
        return Path.of(userHome(), LIBRARY).toString();
    }

    private String getAppSupportPath() {
        return Path.of(getLibraryPath(), APP_SUPPORT).toString();
    }

    private String getCachePath() {
        return Path.of(getLibraryPath(), CACHES).toString();
    }

    @Override
    public Path cacheDirHome(String rootConfig) {
        return Path.of(getCachePath(), rootConfig, Dir.CACHE.getDir());
    }

    @Override
    public Path dataDirHome(String rootConfig) {
        return Path.of(getAppSupportPath(), rootConfig, Dir.DATA.getDir());
    }

    @Override
    public Path stateDirHome(String rootConfig) {
        return Path.of(getAppSupportPath(), rootConfig, Dir.STATE.getDir());
    }

    @Override
    public Path runtimeDirHome(String rootConfig) {
        return Path.of(TEMP, rootConfig);
    }

    @Override
    public Path configDir(String rootConfig) {
        return Path.of(getAppSupportPath(), EQUO, Dir.CONFIG.getDir(), rootConfig);
    }

}
