package com.equo.application.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigLocationsTest {
  private static final String TEST_APP_ID = "TEST-APP-ID";

  @AfterEach
  public void afterEach() throws NoSuchFieldException, IllegalAccessException {
    Field appId = EquoApp.class.getDeclaredField("APP_ID");
    appId.setAccessible(true);
    appId.set(null, null);
  }

  @Test
  public void testGetDirCache() {
    EquoApp.setAppName(TEST_APP_ID);
    Path path = Path.of("CacheHome");
    ConfigLocations.overrideCacheHome(path);
    assertThat(path).isNotNull().isEqualTo(ConfigLocations.cacheHome());
  }

  @Test
  public void testGetDirData() {
    EquoApp.setAppName(TEST_APP_ID);
    Path path = Path.of("DataHome");
    ConfigLocations.overrideDataHome(path);
    assertThat(path).isNotNull().isEqualTo(ConfigLocations.dataHome());
  }

  @Test
  public void testGetDirState() {
    EquoApp.setAppName(TEST_APP_ID);
    Path path = Path.of("StateHome");
    ConfigLocations.overrideStateHome(path);
    assertThat(path).isNotNull().isEqualTo(ConfigLocations.stateHome());
  }

  @Test
  public void testGetDirRuntime() {
    EquoApp.setAppName(TEST_APP_ID);
    Path path = Path.of("RuntimeHome");
    ConfigLocations.overrideRuntimeHome(path);
    assertThat(path).isNotNull().isEqualTo(ConfigLocations.runtimeHome());
  }
}
