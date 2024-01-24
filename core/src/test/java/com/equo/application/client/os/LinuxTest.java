package com.equo.application.client.os;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static com.equo.application.client.os.Linux.CACHE;
import static com.equo.application.client.os.Linux.LOCAL;
import static com.equo.application.client.os.Linux.RUN;
import static com.equo.application.client.os.Linux.SHARE;
import static com.equo.application.client.os.Linux.STATE;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;

public class LinuxTest {
    private static final String ROOT_CONFIG_TEST = "rootConfigTest";
    private Linux linux;

    @BeforeEach
    public void beforeEach() {
        linux = spy(Linux.class);
    }

    @Test
    public void testCacheDirHome() {
        var expected = Path.of(CACHE, ROOT_CONFIG_TEST).toString();
        var result = linux.cacheDirHome(ROOT_CONFIG_TEST).toString();
        assertTrue(result.contains(expected));
    }

    @Test
    public void testDataDirHome() {
        var expected = Path.of(LOCAL, SHARE, ROOT_CONFIG_TEST).toString();
        var result = linux.dataDirHome(ROOT_CONFIG_TEST).toString();
        assertTrue(result.contains(expected));
    }

    @Test
    public void testStateDirHome() {
        var expected = Path.of(LOCAL, STATE, ROOT_CONFIG_TEST).toString();
        var result = linux.stateDirHome(ROOT_CONFIG_TEST).toString();
        assertTrue(result.contains(expected));
    }

    @Test
    public void testRuntimeDirHome() {
        var expected = Path.of(RUN, ROOT_CONFIG_TEST).toString();
        var result = linux.runtimeDirHome(ROOT_CONFIG_TEST).toString();
        assertTrue(result.contains(expected));
    }

}
