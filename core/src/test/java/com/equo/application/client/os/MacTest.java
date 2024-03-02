package com.equo.application.client.os;

import com.equo.application.client.EquoApp;
import com.equo.application.client.os.CommonFolders.Dir;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.file.Path;

import static com.equo.application.client.os.Mac.APP_SUPPORT;
import static com.equo.application.client.os.Mac.CACHES;
import static com.equo.application.client.os.Mac.LIBRARY;
import static com.equo.application.client.os.Mac.TEMP;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;

public class MacTest {
  private static final String APP_ID = "APP-ID";
  private Mac mac;

  @BeforeEach
  public void beforeEach() throws NoSuchFieldException, IllegalAccessException {
    Field appId = EquoApp.class.getDeclaredField("APP_ID");
    appId.setAccessible(true);
    appId.set(null, APP_ID);
    mac = spy(Mac.class);
  }

  @Test
  public void testCacheDirHome() {
    var expected = Path.of(LIBRARY, CACHES, APP_ID, Dir.CACHE.getDir()).toString();
    var result = mac.cacheDirHome().toString();
    assertTrue(result.contains(expected));
  }

  @Test
  public void testDataDirHome() {
    var expected = Path.of(LIBRARY, APP_SUPPORT, APP_ID, Dir.DATA.getDir()).toString();
    var result = mac.dataDirHome().toString();
    assertTrue(result.contains(expected));
  }

  @Test
  public void testStateDirHome() {
    var expected = Path.of(LIBRARY, APP_SUPPORT, APP_ID, Dir.STATE.getDir()).toString();
    var result = mac.stateDirHome().toString();
    assertTrue(result.contains(expected));
  }

  @Test
  public void testRuntimeDirHome() {
    var expected = Path.of(TEMP, APP_ID).toString();
    var result = mac.runtimeDirHome().toString();
    assertTrue(result.contains(expected));
  }
}
