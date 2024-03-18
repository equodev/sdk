package com.equo.application.client.os;

import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.equo.application.client.EquoApp;

public class LinuxTest extends CommonOSTest {
  private static final String CACHE_TEST = ".cache";
  private static final String LOCAL_TEST = ".local";
  private static final String SHARE_TEST = "share";
  private static final String STATE_TEST = "state";
  private static final String RUN_TEST = "/run";
  public static final String CHROMIUM_CONFIG_DIR = "chromium";
  public static final String POLICIES_CONFIG_DIR = "policies";
  public static final String MANDATORY_CONFIG_DIR = "managed";
  private Linux linux;

  @BeforeEach
  public void beforeEach() throws NoSuchFieldException, IllegalAccessException {
    Field appId = EquoApp.class.getDeclaredField("APP_ID");
    appId.setAccessible(true);
    appId.set(null, APP_ID);
    linux = spy(Linux.class);
  }

  @Test
  public void testCacheDirHome() {
    testCacheDirHome(linux, CACHE_TEST, APP_ID);
  }

  @Test
  public void testDataDirHome() {
    testDataDirHome(linux, LOCAL_TEST, SHARE_TEST, APP_ID);
  }

  @Test
  public void testStateDirHome() {
    testStateDirHome(linux, LOCAL_TEST, STATE_TEST, APP_ID);
  }

  @Test
  public void testRuntimeDirHome() {
    testRuntimeDirHome(linux, RUN_TEST, APP_ID);
  }

  @Test
  public void testBookmarkDirHome() {
    testBookmarkDirHome(linux, CHROMIUM_CONFIG_DIR, POLICIES_CONFIG_DIR, MANDATORY_CONFIG_DIR);
  }
}
