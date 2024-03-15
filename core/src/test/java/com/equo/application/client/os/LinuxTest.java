package com.equo.application.client.os;

import com.equo.application.client.EquoApp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static com.equo.application.client.os.Linux.CACHE;
import static com.equo.application.client.os.Linux.LOCAL;
import static com.equo.application.client.os.Linux.RUN;
import static com.equo.application.client.os.Linux.SHARE;
import static com.equo.application.client.os.Linux.STATE;
import static org.mockito.Mockito.spy;

public class LinuxTest extends CommonOSTest {
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
    testCacheDirHome(linux, CACHE, APP_ID);
  }

  @Test
  public void testDataDirHome() {
    testDataDirHome(linux, LOCAL, SHARE, APP_ID);
  }

  @Test
  public void testStateDirHome() {
    testStateDirHome(linux, LOCAL, STATE, APP_ID);
  }

  @Test
  public void testRuntimeDirHome() {
    testRuntimeDirHome(linux, RUN, APP_ID);
  }

}
