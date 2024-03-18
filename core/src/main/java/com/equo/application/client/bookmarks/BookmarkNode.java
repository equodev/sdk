package com.equo.application.client.bookmarks;

import java.util.Objects;

public class BookmarkNode extends Bookmark {
    private String url;

    /**
     * Returns the value of the bookmark url.
     *
     * @return The value of the "url".
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the bookmark url.
     *
     * @param url The "url" parameter is a string that represents the URL to be set.
     *
     * @throws IllegalArgumentException if the URL is null or empty.
     */
    public void setUrl(String url) {
        if (Objects.isNull(url) || url.isEmpty()) {
            throw new IllegalArgumentException("The URL cannot be null or empty.");
        }
        this.url = url;
    }

    public BookmarkNode(String url) {
        setUrl(url);
    }

    public BookmarkNode(String name, String url) {
        super(name);
        setUrl(url);
    }

    @Override
    public String toString() {
        return BookmarkNode.class.getSimpleName() + "{" + "name='" + getName() + '\'' + ", url='" + getUrl() + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BookmarkNode))
            return false;
        BookmarkNode that = (BookmarkNode) o;
        return Objects.equals(getUrl(), that.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUrl());
    }
}
