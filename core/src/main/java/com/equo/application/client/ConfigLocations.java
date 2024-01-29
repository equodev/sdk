package com.equo.application.client;

import com.equo.application.client.os.CommonFolders;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.equo.application.client.os.CommonFolders.Dir;

public class ConfigLocations {

	private static String rootConfig = "equoapp";
	private static final CommonFolders commonFolders = CommonFolders.getInstance();

	private static final Map<Dir, Path> dirs = new HashMap<>() {{
		put(Dir.CACHE, cacheHome());
		put(Dir.DATA, dataHome());
		put(Dir.STATE, stateHome());
		put(Dir.RUNTIME, runtimeHome());
	}};

	private static final Map<Dir, Path> overrideDirs = new HashMap<>();

	private static void setDirHomes() {
		dirs.put(Dir.CACHE, cacheHome());
		dirs.put(Dir.DATA, dataHome());
		dirs.put(Dir.STATE, stateHome());
		dirs.put(Dir.RUNTIME, runtimeHome());
	}

	private static Path cacheHome() {
		return commonFolders.cacheDirHome(rootConfig);
	}

	private static Path dataHome() {
		return commonFolders.dataDirHome(rootConfig);
	}

	private static Path stateHome() {
		return commonFolders.stateDirHome(rootConfig);
	}

	private static Path runtimeHome() {
		return commonFolders.runtimeDirHome(rootConfig);
	}

	private static Path getDir(Dir dir) {
		Path path = overrideDirs.get(dir);
		if (path == null) {
			path = dirs.get(dir);
		}
		return path;
	}

	/**
	 * The function sets the root configuration and updates the directory homes.
	 *
	 * @param newRootConfig The new value for the root configuration.
	 */
	public static void setRootConfig(String newRootConfig) {
		rootConfig = newRootConfig;
		setDirHomes();
	}

	/**
	 * The function updates the directory path for the the cache directory path.
	 *
	 * @param newPath Represents the new path that will be used to override the
	 * cache directory path.
	 */
	public static void overrideCacheHome(Path newPath) {
		overrideDirs.put(Dir.CACHE, newPath);
	}

	/**
	 * The function updates the directory path for the data directory path.
	 *
	 * @param newPath Represents the new path that will be used to override the
	 * data home directory.
	 */
	public static void overrideDataHome(Path newPath) {
		overrideDirs.put(Dir.DATA, newPath);
	}

	/**
	 * The function updates the directory path for the state directory path.
	 *
	 * @param newPath Represents the new path that will be used to override the
	 * state directory.
	 */
	public static void overrideStateHome(Path newPath) {
		overrideDirs.put(Dir.STATE, newPath);
	}

	/**
	 * The function updates the directory path for the runtime directory path.
	 *
	 * @param newPath Represents the new path that will be used to override the
	 * runtime directory.
	 */
	public static void overrideRuntimeHome(Path newPath) {
		overrideDirs.put(Dir.RUNTIME, newPath);
	}

	/**
	 * The function returns the path to the cache directory.
	 *
	 * @return The directory path for the cache directory.
	 */
	public static Path getDirCacheHome() {
		return getDir(Dir.CACHE);
	}

	/**
	 * The function returns the path to the data directory.
	 *
	 * @return The directory path for the data directory.
	 */
	public static Path getDirDataHome() {
		return getDir(Dir.DATA);
	}

	/**
	 * The function returns the path to the state directory.
	 *
	 * @return The directory path for the state directory.
	 */
	public static Path getDirStateHome() {
		return getDir(Dir.STATE);
	}

	/**
	 * The function returns the path to the runtime directory.
	 *
	 * @return The directory path for the runtime directory.
	 */
	public static Path getDirRuntimeHome() {
		return getDir(Dir.RUNTIME);
	}
}
