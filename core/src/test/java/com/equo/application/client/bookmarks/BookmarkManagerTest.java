package com.equo.application.client.bookmarks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class BookmarkManagerTest {

    @Test
    void testSetTopLevelNameIfDontExists() {
        String newTopLevelName = "newTopLevelName";
        BookmarkManager bookmarkManager = (BookmarkManager) BookmarkManager.create()
                .setTopLevelName("newTopLevelName")
                .addBookmark("test");
        for (Bookmark bookmark : bookmarkManager.getChildren()) {
            if (bookmark instanceof BookmarkTopLevelName) {
                BookmarkTopLevelName bookmarkTopLevelName = (BookmarkTopLevelName) bookmark;
                if (bookmarkTopLevelName.getName().equals(newTopLevelName)) {
                    assertTrue(true);
                }
                return;
            }
        }
        fail();
    }
}