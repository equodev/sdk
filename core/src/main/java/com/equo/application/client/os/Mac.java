package com.equo.application.client.os;

import java.nio.file.Path;

import com.equo.application.client.EquoApp;

/**
 * The {@code Mac} class represents Mac-specific directories and their paths in the file system. It
 * extends the {@code CommonFolders} class and provides methods to retrieve paths for cache, data,
 * state, runtime, and config directories on Mac systems.
 */
public class Mac extends CommonFolders {

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
  public Path cacheDirHome() {
    return Path.of(getCachePath(), EquoApp.getAppName(), Dir.CACHE.getDir());
  }

  @Override
  public Path dataDirHome() {
    return Path.of(getAppSupportPath(), EquoApp.getAppName(), Dir.DATA.getDir());
  }

  @Override
  public Path stateDirHome() {
    return Path.of(getAppSupportPath(), EquoApp.getAppName(), Dir.STATE.getDir());
  }

  @Override
  public Path runtimeDirHome() {
    return Path.of(TEMP, EquoApp.getAppName());
  }

  @Override
  public Path configDir() {
    return Path.of(getAppSupportPath(), EQUO, Dir.CONFIG.getDir(), EquoApp.getAppName());
  }

  @Override
  public Path bookmarkDir() {
    return Path.of(configDir().toString(), CHROMIUM_CONFIG_DIR, POLICIES_CONFIG_DIR,
        MANDATORY_CONFIG_DIR);
  }

}
