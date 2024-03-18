package com.equo.application.client.bookmarks;

import java.util.Objects;

public class BookmarkTopLevelName extends Bookmark {
    private String toplevel_name;

    public BookmarkTopLevelName(String name) {
        this.toplevel_name = name;
    }

    /**
     * Sets the value of the "toplevel_name" to the given name.
     *
     * @param name Represents the new name to be set.
     */
    @Override
    public void setName(String name) {
        this.toplevel_name = name;
    }

    /**
     * Returns the value of the "toplevel_name".
     *
     * @return The value of the "toplevel_name".
     */
    @Override
    public String getName() {
        return toplevel_name;
    }

    @Override
    public String toString() {
        return BookmarkTopLevelName.class.getSimpleName() + "{" + "toplevel_name='" + toplevel_name + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BookmarkTopLevelName))
            return false;
        BookmarkTopLevelName that = (BookmarkTopLevelName) o;
        return Objects.equals(toplevel_name, that.toplevel_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toplevel_name);
    }
}
