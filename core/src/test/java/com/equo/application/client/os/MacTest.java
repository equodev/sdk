package com.equo.application.client.os;

import com.equo.application.client.EquoApp;
import com.equo.application.client.os.CommonFolders.Dir;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static com.equo.application.client.os.Mac.APP_SUPPORT;
import static com.equo.application.client.os.Mac.CACHES;
import static com.equo.application.client.os.Mac.LIBRARY;
import static com.equo.application.client.os.Mac.TEMP;
import static org.mockito.Mockito.spy;

public class MacTest extends CommonOSTest {
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
    testCacheDirHome(mac, LIBRARY, CACHES, APP_ID, Dir.CACHE.getDir());
  }

  @Test
  public void testDataDirHome() {
    testDataDirHome(mac, LIBRARY, APP_SUPPORT, APP_ID, Dir.DATA.getDir());
  }

  @Test
  public void testStateDirHome() {
    testStateDirHome(mac, LIBRARY, APP_SUPPORT, APP_ID, Dir.STATE.getDir());
  }

  @Test
  public void testRuntimeDirHome() {
    testRuntimeDirHome(mac, TEMP, APP_ID);
  }
}
