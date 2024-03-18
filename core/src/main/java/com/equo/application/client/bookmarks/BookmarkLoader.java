package com.equo.application.client.bookmarks;

import com.equo.application.client.bookmarks.adapters.BookmarkAdapter;
import com.equo.application.client.bookmarks.adapters.BookmarkManagerAdapter;
import com.equo.application.client.os.CommonFolders;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static com.equo.application.client.bookmarks.BookmarkPaths.getBookmarkFilepath;

public class BookmarkLoader {
    private static final String MANAGED_BOOKMARKS = "ManagedBookmarks";
    private static final String INITIAL_CONTENT = String.format("{\"%s\":[]}", MANAGED_BOOKMARKS);

    private static void createBookmarkFile() {
        var bookmarkDir = CommonFolders.getInstance().bookmarkDir();
        try {
            Files.createDirectories(bookmarkDir);
            Files.createFile(getBookmarkFilepath());
        } catch (IOException ignored) {
        }
    }

    private static void writeBookmarkFile(String content) {
        if (!Files.exists(getBookmarkFilepath())) {
            createBookmarkFile();
        }
        try {
            Files.write(getBookmarkFilepath(), content.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception ignored) {
        }
    }

    private static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Bookmark.class, new BookmarkAdapter());
        gsonBuilder.registerTypeAdapter(BookmarkManager.class, new BookmarkManagerAdapter());
        return gsonBuilder.create();
    }

    /**
     * Loads a JSON file containing bookmark data and returns it as a BookmarkManager object.
     *
     * @return The method is returning an instance of the `BookmarkManager` class.
     *
     * @throws IOException if an I/O error occurs reading from the file
     */
    public static BookmarkManager load() throws IOException {
        String fileContent = INITIAL_CONTENT;
        if (Files.exists(getBookmarkFilepath())) {
            List<String> lines;
            try {
                lines = Files.readAllLines(getBookmarkFilepath());
            } catch (IOException e) {
                throw new IOException("A problem occurred while reading the file: " + getBookmarkFilepath());
            }
            fileContent = String.join("", lines);
        }
        return createGson().fromJson(fileContent, BookmarkManager.class);
    }

    /**
     * Saves a BookmarkManager object as a JSON string to a file.
     *
     * @param bookmarkManager An object of type BookmarkManager that contains the bookmarks to be saved.
     */
    public static void save(BookmarkManager bookmarkManager) {
        writeBookmarkFile(createGson().toJson(bookmarkManager));
    }
}
