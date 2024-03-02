package com.equo.application.client.os;

import com.equo.application.client.EquoApp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.file.Path;

import static com.equo.application.client.os.Linux.CACHE;
import static com.equo.application.client.os.Linux.LOCAL;
import static com.equo.application.client.os.Linux.RUN;
import static com.equo.application.client.os.Linux.SHARE;
import static com.equo.application.client.os.Linux.STATE;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;

public class LinuxTest {
  private static final String APP_ID = "APP-ID";
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
    var expected = Path.of(CACHE, APP_ID).toString();
    var result = linux.cacheDirHome().toString();
    assertTrue(result.contains(expected));
  }

  @Test
  public void testDataDirHome() {
    var expected = Path.of(LOCAL, SHARE, APP_ID).toString();
    var result = linux.dataDirHome().toString();
    assertTrue(result.contains(expected));
  }

  @Test
  public void testStateDirHome() {
    var expected = Path.of(LOCAL, STATE, APP_ID).toString();
    var result = linux.stateDirHome().toString();
    assertTrue(result.contains(expected));
  }

  @Test
  public void testRuntimeDirHome() {
    var expected = Path.of(RUN, APP_ID).toString();
    var result = linux.runtimeDirHome().toString();
    assertTrue(result.contains(expected));
  }

}
