package com.equo.application.client.os;

import com.equo.application.client.EquoApp;
import com.equo.application.client.os.CommonFolders.Dir;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.file.Path;

import static com.equo.application.client.os.Windows.TEMP;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;

public class WindowsTest {
  private static final String APP_ID = "APP-ID";
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
    var expected = Path.of(APP_ID, Dir.CACHE.getDir()).toString();
    var result = win.cacheDirHome().toString();
    assertTrue(result.contains(expected));
  }

  @Test
  public void testDataDirHome() {
    var expected = Path.of(APP_ID, Dir.DATA.getDir()).toString();
    var result = win.dataDirHome().toString();
    assertTrue(result.contains(expected));
  }

  @Test
  public void testStateDirHome() {
    var expected = Path.of(APP_ID, Dir.STATE.getDir()).toString();
    var result = win.stateDirHome().toString();
    assertTrue(result.contains(expected));
  }

  @Test
  public void testRuntimeDirHome() {
    var expected = Path.of(TEMP, APP_ID).toString();
    var result = win.runtimeDirHome().toString();
    assertTrue(result.contains(expected));
  }
}
