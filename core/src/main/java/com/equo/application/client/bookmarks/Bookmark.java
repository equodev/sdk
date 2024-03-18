package com.equo.application.client.bookmarks;

public abstract class Bookmark {
    private String name = "";

    public Bookmark() {
    }

    public Bookmark(String name) {
        this.name = name;
    }

    /**
     * @return the name of the Bookmark.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the Bookmark.
     *
     * @param name Represents the name of the Bookmark.
     */
    public void setName(String name) {
        this.name = name;
    }

}
