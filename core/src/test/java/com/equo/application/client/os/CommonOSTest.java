package com.equo.application.client.os;

import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class CommonOSTest {
    protected static final String APP_ID = "APP-ID";
    private final String PATH_ERROR = "PATH_ERROR";

    public void testCacheDirHome(CommonFolders os, String... path) {
        var expected = Stream.of(path).reduce((p1, p2) -> Paths.get(p1, p2).toString()).orElse(PATH_ERROR);
        var result = os.cacheDirHome().toString();
        assertThat(result).isNotBlank().contains(expected);
    }
    public void testDataDirHome(CommonFolders os, String... path) {
        var expected = Stream.of(path).reduce((p1, p2) -> Paths.get(p1, p2).toString()).orElse(PATH_ERROR);
        var result = os.dataDirHome().toString();
        assertThat(result).isNotBlank().contains(expected);
    }
    public void testStateDirHome(CommonFolders os, String... path) {
        var expected = Stream.of(path).reduce((p1, p2) -> Paths.get(p1, p2).toString()).orElse(PATH_ERROR);
        var result = os.stateDirHome().toString();
        assertThat(result).isNotBlank().contains(expected);
    }
    public void testRuntimeDirHome(CommonFolders os, String... path) {
        var expected = Stream.of(path).reduce((p1, p2) -> Paths.get(p1, p2).toString()).orElse(PATH_ERROR);
        var result = os.runtimeDirHome().toString();
        assertThat(result).isNotBlank().contains(expected);
    }
}
