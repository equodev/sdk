package com.equo.application.client.bookmarks.adapters;

import com.equo.application.client.bookmarks.Bookmark;
import com.equo.application.client.bookmarks.BookmarkFolder;
import com.equo.application.client.bookmarks.BookmarkNode;
import com.equo.application.client.bookmarks.BookmarkTopLevelName;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.List;

public class BookmarkAdapter implements JsonSerializer<Bookmark>, JsonDeserializer<Bookmark> {
    private static final String TOPLEVEL_NAME = "toplevel_name";
    private static final String NAME = "name";
    private static final String URL = "url";
    private static final String CHILDREN = "children";

    @Override
    public JsonElement serialize(Bookmark bookmark, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        String property = TOPLEVEL_NAME;
        if (!(bookmark instanceof BookmarkTopLevelName)) {
            property = NAME;
            if (bookmark instanceof BookmarkNode) {
                jsonObject.add(URL, new JsonPrimitive(((BookmarkNode) bookmark).getUrl()));
            }
            if (bookmark instanceof BookmarkFolder) {
                List<Bookmark> children = ((BookmarkFolder) bookmark).getChildren();
                jsonObject.add(CHILDREN, new Gson().toJsonTree(children).getAsJsonArray());
            }
        }
        jsonObject.add(property, new JsonPrimitive(bookmark.getName()));
        return jsonObject;
    }

    @Override
    public Bookmark deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        if (jsonObject.get(TOPLEVEL_NAME) != null) {
            return context.deserialize(jsonObject, BookmarkTopLevelName.class);
        }
        if (jsonObject.get(URL) != null) {
            return context.deserialize(jsonObject, BookmarkNode.class);
        }
        return context.deserialize(jsonObject, BookmarkFolder.class);
    }
}
