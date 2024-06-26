package com.equo.application.client.os;

import java.nio.file.Path;

import com.equo.application.client.EquoApp;

/**
 * The {@code Windows} class represents Windows-specific directories and their paths in the file
 * system. It extends the {@code CommonFolders} class and provides methods to retrieve paths for
 * cache, data, state, runtime, and config directories on Windows systems.
 */
public class Windows extends CommonFolders {

  public static final String TEMP = "Temp";
  public static final String LOCALAPPDATA = "LOCALAPPDATA";
  public static final String APPDATA = "AppData";
  public static final String LOCAL = "Local";

  private String localAppData() {
    String localAppData = System.getenv(LOCALAPPDATA);
    if (localAppData == null || localAppData.isBlank()) {
      localAppData = Path.of(userHome(), APPDATA, LOCAL).toString();
    }
    return localAppData;
  }

  @Override
  public Path cacheDirHome() {
    return Path.of(localAppData(), EquoApp.getAppName(), Dir.CACHE.getDir());
  }

  @Override
  public Path dataDirHome() {
    return Path.of(localAppData(), EquoApp.getAppName(), Dir.DATA.getDir());
  }

  @Override
  public Path stateDirHome() {
    return Path.of(localAppData(), EquoApp.getAppName(), Dir.STATE.getDir());
  }

  @Override
  public Path runtimeDirHome() {
    return Path.of(localAppData(), TEMP, EquoApp.getAppName());
  }

  @Override
  public Path configDir() {
    return Path.of(localAppData(), EQUO, Dir.CONFIG.getDir(), EquoApp.getAppName());
  }

}
