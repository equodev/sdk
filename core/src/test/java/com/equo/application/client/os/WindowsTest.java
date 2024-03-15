package com.equo.application.client.os;

import com.equo.application.client.EquoApp;
import com.equo.application.client.os.CommonFolders.Dir;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static com.equo.application.client.os.Windows.TEMP;
import static org.mockito.Mockito.spy;

public class WindowsTest extends CommonOSTest {
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
    testRuntimeDirHome(win, TEMP, APP_ID);
  }
}
