package com.equo.application.client.bookmarks;

import com.equo.application.client.os.CommonFolders;

import java.nio.file.Path;
import java.nio.file.Paths;

public class BookmarkPaths {
    private static final String BOOKMARKS_FILE = "bookmarks.json";

    /**
     * Returns the filepath for a bookmark file.
     *
     * @return The filepath for a bookmark file.
     */
    public static Path getBookmarkFilepath() {
        CommonFolders commonFolders = CommonFolders.getInstance();
        var bookmarkDir = commonFolders.bookmarkDir().toString();
        return Paths.get(bookmarkDir, BOOKMARKS_FILE);
    }

}
