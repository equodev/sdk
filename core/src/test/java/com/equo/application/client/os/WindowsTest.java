package com.equo.application.client.os;

import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.equo.application.client.EquoApp;
import com.equo.application.client.os.CommonFolders.Dir;

public class WindowsTest extends CommonOSTest {
  public static final String TEMP_TEST = "Temp";
  public static final String CHROMIUM_CONFIG_DIR = "chromium";
  public static final String POLICIES_CONFIG_DIR = "policies";
  public static final String MANDATORY_CONFIG_DIR = "managed";
  private Windows win;

  @BeforeEach
  public void beforeEach() throws NoSuchFieldException, IllegalAccessException {
    Field appId = EquoApp.class.getDeclaredField("APP_ID");
    appId.setAccessible(true);
    appId.set(null, APP_ID);
    win = spy(Windows.class);
  }

  @Test
  public void testCacheDirHome() {
    testCacheDirHome(win, APP_ID, Dir.CACHE.getDir());
  }

  @Test
  public void testDataDirHome() {
    testDataDirHome(win, APP_ID, Dir.DATA.getDir());
  }

  @Test
  public void testStateDirHome() {
    testStateDirHome(win, APP_ID, Dir.STATE.getDir());
  }

  @Test
  public void testRuntimeDirHome() {
    testRuntimeDirHome(win, TEMP_TEST, APP_ID);
  }

  @Test
  public void testBookmarkDirHome() {
    testBookmarkDirHome(win, CHROMIUM_CONFIG_DIR, POLICIES_CONFIG_DIR, MANDATORY_CONFIG_DIR);
  }
}
