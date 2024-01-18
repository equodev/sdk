package com.equo.application.client;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigLocationsTest {
	@Test
	public void testGetDirCache() {
		Path path = Path.of("CacheHome");
		ConfigLocations.overrideCacheHome(path);
		assertEquals(path, ConfigLocations.getDirCacheHome());
	}

	@Test
	public void testGetDirData() {
		Path path = Path.of("DataHome");
		ConfigLocations.overrideDataHome(path);
		assertEquals(path, ConfigLocations.getDirDataHome());
	}

	@Test
	public void testGetDirState() {
		Path path = Path.of("StateHome");
		ConfigLocations.overrideStateHome(path);
		assertEquals(path, ConfigLocations.getDirStateHome());
	}

	@Test
	public void testGetDirRuntime() {
		Path path = Path.of("RuntimeHome");
		ConfigLocations.overrideRuntimeHome(path);
		assertEquals(path, ConfigLocations.getDirRuntimeHome());
	}
}
