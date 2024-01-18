package com.equo.application.client.os;

import com.equo.application.client.os.CommonFolders.Dir;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static com.equo.application.client.os.Mac.APP_SUPPORT;
import static com.equo.application.client.os.Mac.CACHES;
import static com.equo.application.client.os.Mac.LIBRARY;
import static com.equo.application.client.os.Mac.TEMP;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;

public class MacTest {
    private static final String ROOT_CONFIG_TEST = "rootConfigTest";
    private Mac mac;

    @BeforeEach
    public void beforeEach() {
        mac = spy(Mac.class);
    }

    @Test
    public void testCacheDirHome() {
        var expected = Path.of(LIBRARY, CACHES, ROOT_CONFIG_TEST, Dir.CACHE.getDir()).toString();
        var result = mac.cacheDirHome(ROOT_CONFIG_TEST).toString();
        assertTrue(result.contains(expected));
    }

    @Test
    public void testDataDirHome() {
        var expected = Path.of(LIBRARY, APP_SUPPORT, ROOT_CONFIG_TEST, Dir.DATA.getDir()).toString();
        var result = mac.dataDirHome(ROOT_CONFIG_TEST).toString();
        assertTrue(result.contains(expected));
    }

    @Test
    public void testStateDirHome() {
        var expected = Path.of(LIBRARY, APP_SUPPORT, ROOT_CONFIG_TEST, Dir.STATE.getDir()).toString();
        var result = mac.stateDirHome(ROOT_CONFIG_TEST).toString();
        assertTrue(result.contains(expected));
    }

    @Test
    public void testRuntimeDirHome() {
        var expected = Path.of(TEMP, ROOT_CONFIG_TEST).toString();
        var result = mac.runtimeDirHome(ROOT_CONFIG_TEST).toString();
        assertTrue(result.contains(expected));
    }
}
