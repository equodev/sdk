package com.equo.application.client.os;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public abstract class CommonOSTest {
    protected static final String APP_ID = "APP-ID";
    private final String PATH_ERROR = "PATH_ERROR";
    
    public void testDirHome(Path result, String... expectedPaths) {
        var expected = Stream.of(expectedPaths)
            .reduce((p1, p2) -> Paths.get(p1, p2).toString())
            .orElse(PATH_ERROR);
        assertThat(result.toString()).isNotBlank().contains(expected);
    }

    public void testCacheDirHome(CommonFolders os, String... expectedPaths) {
        testDirHome(os.cacheDirHome(), expectedPaths);
    }

    public void testDataDirHome(CommonFolders os, String... expectedPaths) {
        testDirHome(os.dataDirHome(), expectedPaths);
        
    }

    public void testStateDirHome(CommonFolders os, String... expectedPaths) {
        testDirHome(os.stateDirHome(), expectedPaths);
    }

    public void testRuntimeDirHome(CommonFolders os, String... expectedPaths) {
        testDirHome(os.runtimeDirHome(), expectedPaths);
    }

    public void testBookmarkDirHome(CommonFolders os, String... expectedPaths) {
        testDirHome(os.bookmarkDir(), expectedPaths);
    }
}
