package com.equo.application.client.bookmarks.adapters;

import com.equo.application.client.bookmarks.Bookmark;
import com.equo.application.client.bookmarks.BookmarkManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookmarkManagerAdapter implements JsonSerializer<BookmarkManager>, JsonDeserializer<BookmarkManager> {
    private static final String MANAGED_BOOKMARKS = "ManagedBookmarks";

    @Override
    public JsonElement serialize(BookmarkManager bookmarkManager, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(MANAGED_BOOKMARKS, new Gson().toJsonTree(bookmarkManager.getChildren()).getAsJsonArray());
        return jsonObject;
    }

    @Override
    public BookmarkManager deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        List<Bookmark> bookmarks = new ArrayList<>();
        JsonArray jsonArray = jsonObject.get(MANAGED_BOOKMARKS).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray.asList()) {
            bookmarks.add(context.deserialize(jsonElement, Bookmark.class));
        }
        var managedBookmarks = BookmarkManager.create();
        managedBookmarks.setChildren(bookmarks);
        return managedBookmarks;
    }
}
