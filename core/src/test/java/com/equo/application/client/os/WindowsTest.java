package com.equo.application.client.os;

import com.equo.application.client.os.CommonFolders.Dir;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static com.equo.application.client.os.Windows.TEMP;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;

public class WindowsTest {
    private static final String ROOT_CONFIG_TEST = "rootConfigTest";
    private Windows win;

    @BeforeEach
    public void beforeEach() {
        win = spy(Windows.class);
    }

    @Test
    public void testCacheDirHome() {
        var expected = Path.of(ROOT_CONFIG_TEST, Dir.CACHE.getDir()).toString();
        var result = win.cacheDirHome(ROOT_CONFIG_TEST).toString();
        assertTrue(result.contains(expected));
    }

    @Test
    public void testDataDirHome() {
        var expected = Path.of(ROOT_CONFIG_TEST, Dir.DATA.getDir()).toString();
        var result = win.dataDirHome(ROOT_CONFIG_TEST).toString();
        assertTrue(result.contains(expected));
    }

    @Test
    public void testStateDirHome() {
        var expected = Path.of(ROOT_CONFIG_TEST, Dir.STATE.getDir()).toString();
        var result = win.stateDirHome(ROOT_CONFIG_TEST).toString();
        assertTrue(result.contains(expected));
    }

    @Test
    public void testRuntimeDirHome() {
        var expected = Path.of(TEMP, ROOT_CONFIG_TEST).toString();
        var result = win.runtimeDirHome(ROOT_CONFIG_TEST).toString();
        assertTrue(result.contains(expected));
    }
}
