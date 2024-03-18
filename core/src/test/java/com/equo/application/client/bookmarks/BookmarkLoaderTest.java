package com.equo.application.client.bookmarks;

import com.equo.application.client.EquoApp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;

import static com.equo.application.client.bookmarks.BookmarkPaths.getBookmarkFilepath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class BookmarkLoaderTest {
    private static final String TEST_APP_ID = "test-app-id";

    @BeforeEach
    public void beforeEach() throws NoSuchFieldException, IllegalAccessException {
        Field appId = EquoApp.class.getDeclaredField("APP_ID");
        appId.setAccessible(true);
        appId.set(null, TEST_APP_ID);
    }

    @Test
    void testCheckIfFileWasCreated() {
        BookmarkManager bookmarkManager = null;
        try {
            BookmarkLoader.save(new BookmarkManager());
            bookmarkManager = BookmarkLoader.load();
        } catch (IOException e) {
            fail();
        }
        assertEquals(new BookmarkManager(), bookmarkManager);
    }

    @Test
    void testManagedBookmarksLoadedWhenIsEmpty() throws IOException {
        Files.deleteIfExists(getBookmarkFilepath());
        BookmarkManager bookmarkManager = BookmarkLoader.load();
        assertEquals(0, bookmarkManager.getChildren().size());
    }

    @Test
    void testManagedBookmarksLoadedWhenHasItems() throws IOException {
        BookmarkManager bookmarkManager = new BookmarkManager();
        bookmarkManager.setChildren(new ArrayList<>() {{
            add(new BookmarkNode("url"));
            add(new BookmarkTopLevelName("Top levelName"));
            add(new BookmarkFolder());
        }});
        BookmarkLoader.save(bookmarkManager);
        BookmarkManager bookmarkManagerLoaded = BookmarkLoader.load();
        assertEquals(bookmarkManager.getChildren().size(), bookmarkManagerLoaded.getChildren().size());
        assertEquals(bookmarkManager.getChildren().get(0), bookmarkManagerLoaded.getChildren().get(0));
        assertEquals(bookmarkManager.getChildren().get(1), bookmarkManagerLoaded.getChildren().get(1));
        assertEquals(bookmarkManager.getChildren().get(2), bookmarkManagerLoaded.getChildren().get(2));
    }
}