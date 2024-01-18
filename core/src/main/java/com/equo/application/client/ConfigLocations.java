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

	public static void setRootConfig(String newRootConfig) {
		rootConfig = newRootConfig;
		setDirHomes();
	}

	public static void overrideCacheHome(Path newPath) {
		overrideDirs.put(Dir.CACHE, newPath);
	}

	public static void overrideDataHome(Path newPath) {
		overrideDirs.put(Dir.DATA, newPath);
	}

	public static void overrideStateHome(Path newPath) {
		overrideDirs.put(Dir.STATE, newPath);
	}

	public static void overrideRuntimeHome(Path newPath) {
		overrideDirs.put(Dir.RUNTIME, newPath);
	}

	public static Path getDirCacheHome() {
		return getDir(Dir.CACHE);
	}

	public static Path getDirDataHome() {
		return getDir(Dir.DATA);
	}

	public static Path getDirStateHome() {
		return getDir(Dir.STATE);
	}

	public static Path getDirRuntimeHome() {
		return getDir(Dir.RUNTIME);
	}
}
