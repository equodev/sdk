package com.equo.application.client.bookmarks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookmarkFolder extends Bookmark {
    protected List<Bookmark> children = new ArrayList<>();

    protected BookmarkFolder() {
        super();
    }

    protected BookmarkFolder(String name) {
        super(name);
    }

    /**
     * Creates a new bookmark folder with the given name.
     *
     * @param name The name of the bookmark folder that you want to create.
     *
     * @return A new instance of the BookmarkFolder class with the specified name.
     */
    public static BookmarkFolder create(String name) {
        return new BookmarkFolder(name);
    }

    /**
     * Creates a new bookmark folder.
     *
     * @return A new instance of the BookmarkFolder class.
     */
    public static BookmarkFolder create() {
        return new BookmarkFolder();
    }

    /**
     * Returns a new ArrayList containing all the children of a bookmark.
     *
     * @return A new ArrayList containing the children of the current object.
     */
    public List<Bookmark> getChildren() {
        return new ArrayList<>(children);
    }

    /**
     * Sets the list of children bookmarks for a given bookmark.
     *
     * @param children is a List of objects of type Bookmark.
     */
    public void setChildren(List<Bookmark> children) {
        this.children = children;
    }

    private int size() {
        return children.size();
    }

    /**
     * Adds a bookmark at a specific index in a bookmark folder and returns the updated folder.
     *
     * @param index    The index parameter represents the position at which the bookmark should be added within the
     *                 bookmark folder. It is an integer value that specifies the index position, starting from 0 for
     *                 the first bookmark.
     * @param bookmark The `bookmark` parameter is an object of type `Bookmark`.
     *
     * @return The method is returning the updated `BookmarkFolder` object after adding the bookmark at a specified
     * index.
     */
    private BookmarkFolder addAt(int index, Bookmark bookmark) {
        children.add(index, bookmark);
        return this;
    }

    private BookmarkFolder prepend(Bookmark bookmark) {
        addAt(0, bookmark);
        return this;
    }

    private BookmarkFolder add(Bookmark bookmark) {
        children.add(bookmark);
        return this;
    }

    /**
     * Adds a new bookmark node at a specified index in a bookmark folder and returns the updated folder.
     *
     * @param index The index parameter represents the position at which the new bookmark node should be added within
     *              the bookmark folder. It is an integer value that specifies the index position, starting from 0 for
     *              the first bookmark.
     * @param url   The URL of the bookmark. It is a string that represents the web address of the bookmarked page.
     * @param name  The name parameter is a string that represents the name or title of the bookmark.
     *
     * @return The method is returning the updated `BookmarkFolder` object after adding the bookmark at a specified
     * index.
     */
    public BookmarkFolder addBookmarkAt(int index, String url, String name) {
        addAt(index, new BookmarkNode(name, url));
        return this;
    }

    /**
     * Adds a new bookmark node at a specified index in a bookmark folder and returns the updated folder.
     *
     * @param index The index parameter represents the position at which the new bookmark node should be added within
     *              the bookmark folder. It is an integer value that specifies the index position, starting from 0 for
     *              the first bookmark.
     * @param url   The URL of the bookmark. It is a string that represents the web address of the bookmarked page.
     *
     * @return The method is returning the updated `BookmarkFolder` object after adding the bookmark at a specified
     * index.
     */
    public BookmarkFolder addBookmarkAt(int index, String url) {
        addAt(index, new BookmarkNode(url));
        return this;
    }

    /**
     * Adds a new bookmark node to the beginning of a bookmark folder and returns the updated folder.
     *
     * @param url  The URL of the bookmark. It is a string that represents the web address of the bookmarked page.
     * @param name The name parameter is a string that represents the name or title of the bookmark.
     *
     * @return The method is returning the updated `BookmarkFolder` object after adding the bookmark at a specified
     * index.
     */
    public BookmarkFolder prependBookmark(String url, String name) {
        prepend(new BookmarkNode(name, url));
        return this;
    }

    /**
     * Adds a new bookmark node to the beginning of a bookmark folder and returns the updated folder.
     *
     * @param url The URL of the bookmark. It is a string that represents the web address of the bookmarked page.
     *
     * @return The method is returning the updated `BookmarkFolder` object after adding the bookmark at a specified
     * index.
     */
    public BookmarkFolder prependBookmark(String url) {
        prepend(new BookmarkNode(url));
        return this;
    }

    /**
     * Adds a new bookmark node to the end of a bookmark folder and returns the updated folder.
     *
     * @param url  The URL of the bookmark. It is a string that represents the web address of the bookmarked page.
     * @param name The name parameter is a string that represents the name or title of the bookmark.
     *
     * @return The method is returning the updated `BookmarkFolder` object after adding the bookmark at a specified
     * index.
     */
    public BookmarkFolder addBookmark(String url, String name) {
        add(new BookmarkNode(name, url));
        return this;
    }

    /**
     * Adds a new bookmark node to the end of a bookmark folder and returns the updated folder.
     *
     * @param url The URL of the bookmark. It is a string that represents the web address of the bookmarked page.
     *
     * @return The method is returning the updated `BookmarkFolder` object after adding the bookmark at a specified
     * index.
     */
    public BookmarkFolder addBookmark(String url) {
        add(new BookmarkNode(url));
        return this;
    }

    /**
     * Adds a bookmark folder at a specified index and returns the updated bookmark folder.
     *
     * @param index          The index parameter represents the position at which the new bookmark folder should be
     *                       added within the bookmark folder. It is an integer value that specifies the index position,
     *                       starting from 0 for the first bookmark.
     * @param bookmarkFolder The `bookmarkFolder` parameter is an instance of the `BookmarkFolder` class. It represents
     *                       the bookmark folder that you want to add to the current bookmark folder.
     *
     * @return The method is returning the updated `BookmarkFolder` object after adding the bookmark at a specified
     * index.
     */
    public BookmarkFolder addFolderAt(int index, BookmarkFolder bookmarkFolder) {
        addAt(index, bookmarkFolder);
        return this;
    }

    /**
     * Adds a bookmark folder to the beginning of a list and returns the updated list.
     *
     * @param bookmarkFolder The `bookmarkFolder` parameter is an instance of the `BookmarkFolder` class. It represents
     *                       the bookmark folder that you want to add to the current bookmark folder.
     *
     * @return The method is returning the updated `BookmarkFolder` object after adding the bookmark at a specified
     * index.
     */
    public BookmarkFolder prependFolder(BookmarkFolder bookmarkFolder) {
        prepend(bookmarkFolder);
        return this;
    }

    /**
     * Adds a bookmark folder to the end of a list and returns the updated list.
     *
     * @param bookmarkFolder The `bookmarkFolder` parameter is an instance of the `BookmarkFolder` class. It represents
     *                       the bookmark folder that you want to add to the current bookmark folder.
     *
     * @return The method is returning the updated `BookmarkFolder` object after adding the bookmark at a specified
     * index.
     */
    public BookmarkFolder addFolder(BookmarkFolder bookmarkFolder) {
        add(bookmarkFolder);
        return this;
    }

    /**
     * Removes a bookmark at a specified index and returns the updated folder.
     *
     * @param index The index parameter represents the position of the bookmark to be removed from the bookmarks list.
     *
     * @return The method is returning the updated `BookmarkFolder` object after removing the bookmark at a specified
     * index.
     */
    public BookmarkFolder removeAt(int index) {
        children.remove(index);
        return this;
    }

    /**
     * Removes the first bookmark from a BookmarkFolder object and returns the updated folder.
     *
     * @return The method is returning the updated `BookmarkFolder` object after removing the first bookmark.
     */
    public BookmarkFolder shift() {
        removeAt(0);
        return this;
    }

    /**
     * Removes the last bookmark from a BookmarkFolder object and returns the updated folder.
     *
     * @return The method is returning the updated `BookmarkFolder` object after removing the last bookmark.
     */
    public BookmarkFolder pop() {
        removeAt(size() - 1);
        return this;
    }

    /**
     * Moves a bookmark from one index to another in a bookmark folder.
     *
     * @param index    The index parameter represents the current position of the bookmark that needs to be moved. It is
     *                 the index of the bookmark in the current folder.
     * @param newIndex The `newIndex` parameter is the new position where the bookmark should be moved to within the
     *                 bookmark folder.
     *
     * @return The method is returning the updated `BookmarkFolder` object after moving the bookmark to new position.
     */
    public BookmarkFolder moveAt(int index, int newIndex) {
        addAt(newIndex, children.remove(index));
        return this;
    }

    /**
     * Moves a bookmark from a specified index to the first position in a bookmark folder.
     *
     * @param index The index parameter represents the position of the bookmark within the bookmark folder that you want
     *              to move to the first position.
     *
     * @return The method is returning the updated `BookmarkFolder` object after moving the bookmark to first position.
     */
    public BookmarkFolder moveToFirst(int index) {
        prepend(children.remove(index));
        return this;
    }

    /**
     * Moves a bookmark from a specified index to the last position in a bookmark folder.
     *
     * @param index The index parameter represents the position of the bookmark within the bookmark folder that you want
     *              to move to the last position.
     *
     * @return The method is returning the updated `BookmarkFolder` object after moving the bookmark to last position.
     */
    public BookmarkFolder moveToLast(int index) {
        add(children.remove(index));
        return this;
    }

    @Override
    public String toString() {
        return BookmarkFolder.class.getSimpleName() + "{" + "name='" + getName() + '\'' + ", children=" + getChildren() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BookmarkFolder))
            return false;
        BookmarkFolder that = (BookmarkFolder) o;
        return Objects.equals(getChildren(), that.getChildren());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChildren());
    }
}
