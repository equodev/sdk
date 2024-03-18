package com.equo.application.client.bookmarks;

public class BookmarkManager extends BookmarkFolder {
    public static BookmarkManager create() {
        return new BookmarkManager();
    }

    public static BookmarkManager create(String setTopLevelName) {
        return new BookmarkManager().setTopLevelName(setTopLevelName);
    }

    @Override
    public String toString() {
        return BookmarkManager.class.getSimpleName() + "=" + children;
    }

    /**
     * Sets the top level name of a managed bookmark, either by updating an existing top level name or adding a new one
     * if it doesn't exist.
     *
     * @param topLevelName The `topLevelName` parameter is a string that represents the name of the top-level bookmark.
     *
     * @return The method is returning a reference to the updated BookmarkManager object.
     */
    public BookmarkManager setTopLevelName(String topLevelName) {
        for (Bookmark bookmark : children) {
            if (bookmark instanceof BookmarkTopLevelName) {
                // It indicates that a topLevelName already exists. In this
                // case, I proceed to call setName to change the name and exit.
                BookmarkTopLevelName bookmarkTopLevelName = (BookmarkTopLevelName) bookmark;
                bookmarkTopLevelName.setName(topLevelName);
                return this;
            }
        }
        // If a bookmarkTopLevelName does not exist, add a new one at the first
        // position in the list.
        children.add(0, new BookmarkTopLevelName(topLevelName));
        return this;
    }
}
