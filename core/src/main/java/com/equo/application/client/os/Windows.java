package com.equo.application.client.os;

import java.nio.file.Path;

public class Windows implements CommonFolders {

    public static final String TEMP = "Temp";
    public static final String LOCALAPPDATA = "LOCALAPPDATA";
    public static final String APPDATA = "AppData";
    public static final String LOCAL = "Local";

    private String localAppData() {
        String localAppData = System.getenv(LOCALAPPDATA);
        if (localAppData == null || localAppData.trim().isEmpty()) {
            localAppData = Path.of(userHome(), APPDATA, LOCAL).toString();
        }
        return localAppData;
    }

    @Override
    public Path cacheDirHome(String rootConfig) {
        return Path.of(localAppData(), rootConfig, Dir.CACHE.getDir());
    }

    @Override
    public Path dataDirHome(String rootConfig) {
        return Path.of(localAppData(), rootConfig, Dir.DATA.getDir());
    }

    @Override
    public Path stateDirHome(String rootConfig) {
        return Path.of(localAppData(), rootConfig, Dir.STATE.getDir());
    }

    @Override
    public Path runtimeDirHome(String rootConfig) {
        return Path.of(localAppData(), TEMP, rootConfig);
    }

    @Override
    public Path configDir(String rootConfig) {
        return Path.of(localAppData(), EQUO, Dir.CONFIG.getDir(), rootConfig);
    }

}
