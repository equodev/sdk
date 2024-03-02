package com.equo.application.client.os;

import java.nio.file.Path;

/**
 * The {@code CommonFolders} class represents common directories and their paths
 * in a file system. It provides methods to retrieve paths for cache, data, state, runtime,
 * and config directories. This class also handles the instantiation of platform-specific
 * implementations based on the operating system.
 */
public abstract class CommonFolders {
  public static final String EQUO = "equo";
  private static CommonFolders instance = null;

  /**
   * Dir represents various types of directories.
   */
  public enum Dir {
    CACHE("Cache"), CONFIG("Config"), DATA("Data"), STATE("State"), RUNTIME("Runtime");

    private final String dir;

    Dir(String dir) {
      this.dir = dir;
    }

    /**
     * Returns the value of the enum instance.
     * @return The method is returning the value of the enum instance.
     */
    public String getDir() {
      return this.dir;
    }
  }

  /**
   * Returns the path of the user's home directory.
   * @return The value of the system property "user.home", which represents the user's home
   *     directory.
   */
  protected String userHome() {
    return System.getProperty("user.home");
  }

  /**
   * Returns the path of the cache directory based on the rootConfig.
   * @return The path of the cache directory based on the rootConfig.
   */
  public abstract Path cacheDirHome();

  /**
   * Returns the path of the data directory based on the rootConfig.
   * @return The path of the data directory based on the rootConfig.
   */
  public abstract Path dataDirHome();

  /**
   * Returns the path of the state directory based on the rootConfig.
   * @return The path of the state directory based on the rootConfig.
   */
  public abstract Path stateDirHome();

  /**
   * Returns the path of the runtime directory based on the rootConfig.
   * @return The path of the runtime directory based on the rootConfig.
   */
  public abstract Path runtimeDirHome();

  /**
   * Returns the path of the config directory based on the rootConfig.
   * @return The path of the config directory based on the rootConfig.
   */
  public abstract Path configDir();

  /**
   * Returns an instance of a class based on the operating system being used.
   * @return The method returns an instance of the `CommonFolders` class, depending on the operating
   *     system in use. If it's Windows, it returns an instance of the `Windows` class; if
   *     it's Macintosh, it returns an instance of the `Mac` class. Otherwise, it returns an
   *     instance of the `Linux` class.
   */
  public static CommonFolders getInstance() {
    if (instance == null) {
      if (OS.isWindows()) {
        instance = new Windows();
      } else if (OS.isMacintosh()) {
        instance = new Mac();
      } else {
        instance = new Linux();
      }
    }
    return instance;
  }

}
