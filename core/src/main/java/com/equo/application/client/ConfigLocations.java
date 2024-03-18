package com.equo.application.client;

import static com.equo.application.client.os.CommonFolders.Dir;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.equo.application.client.os.CommonFolders;

/**
 * The {@code ConfigLocations} class manages the configuration directories for various purposes.
 */
public class ConfigLocations {
  private static final CommonFolders commonFolders = CommonFolders.getInstance();

  private static final Map<Dir, Path> dirs = new HashMap<>() {{
      put(Dir.CACHE, commonFolders.cacheDirHome());
      put(Dir.DATA, commonFolders.dataDirHome());
      put(Dir.STATE, commonFolders.stateDirHome());
      put(Dir.RUNTIME, commonFolders.runtimeDirHome());
    }};

  private static final Map<Dir, Path> overrideDirs = new HashMap<>();

  private static Path getDir(Dir dir) {
    Path path = overrideDirs.get(dir);
    if (path == null) {
      path = dirs.get(dir);
    }
    return path;
  }

  /**
   * Updates the directory path for the cache directory path.
   * @param newPath Represents the new path that will be used to override the cache directory path.
   */
  public static void overrideCacheHome(Path newPath) {
    overrideDirs.put(Dir.CACHE, newPath);
  }

  /**
   * Updates the directory path for the data directory path.
   * @param newPath Represents the new path that will be used to override the data home directory.
   */
  public static void overrideDataHome(Path newPath) {
    overrideDirs.put(Dir.DATA, newPath);
  }

  /**
   * Updates the directory path for the state directory path.
   * @param newPath Represents the new path that will be used to override the state directory.
   */
  public static void overrideStateHome(Path newPath) {
    overrideDirs.put(Dir.STATE, newPath);
  }

  /**
   * Updates the directory path for the runtime directory path.
   * @param newPath Represents the new path that will be used to override the runtime directory.
   */
  public static void overrideRuntimeHome(Path newPath) {
    overrideDirs.put(Dir.RUNTIME, newPath);
  }

  /**
   * Returns the path to the cache directory.
   * @return The directory path for the cache directory.
   */
  public static Path cacheHome() {
    return getDir(Dir.CACHE);
  }

  /**
   * Returns the path to the data directory.
   * @return The directory path for the data directory.
   */
  public static Path dataHome() {
    return getDir(Dir.DATA);
  }

  /**
   * Returns the path to the state directory.
   * @return The directory path for the state directory.
   */
  public static Path stateHome() {
    return getDir(Dir.STATE);
  }

  /**
   * Returns the path to the runtime directory.
   * @return The directory path for the runtime directory.
   */
  public static Path runtimeHome() {
    return getDir(Dir.RUNTIME);
  }
}
