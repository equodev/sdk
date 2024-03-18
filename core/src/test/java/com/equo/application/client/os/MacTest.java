package com.equo.application.client.os;

import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.equo.application.client.EquoApp;
import com.equo.application.client.os.CommonFolders.Dir;

public class MacTest extends CommonOSTest {
  public static final String LIBRARY_TEST = "Library";
  public static final String CACHES_TEST = "Caches";
  public static final String APP_SUPPORT_TEST = "Application Support";
  public static final String TEMP_TEST = "/tmp";
  public static final String CHROMIUM_CONFIG_DIR = "chromium";
  public static final String POLICIES_CONFIG_DIR = "policies";
  public static final String MANDATORY_CONFIG_DIR = "managed";
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
    testCacheDirHome(mac, LIBRARY_TEST, CACHES_TEST, APP_ID, Dir.CACHE.getDir());
  }

  @Test
  public void testDataDirHome() {
    testDataDirHome(mac, LIBRARY_TEST, APP_SUPPORT_TEST, APP_ID, Dir.DATA.getDir());
  }

  @Test
  public void testStateDirHome() {
    testStateDirHome(mac, LIBRARY_TEST, APP_SUPPORT_TEST, APP_ID, Dir.STATE.getDir());
  }

  @Test
  public void testRuntimeDirHome() {
    testRuntimeDirHome(mac, TEMP_TEST, APP_ID);
  }

  @Test
  public void testBookmarkDirHome() {
    testBookmarkDirHome(mac, CHROMIUM_CONFIG_DIR, POLICIES_CONFIG_DIR, MANDATORY_CONFIG_DIR);
  }
}
