package com.freetempo.yamviewer;

import org.json.JSONObject;

public class PhotoItem {

    private String id, name, desc, url, status;
    private boolean isCover;
    // private String tags;

    public PhotoItem(JSONObject object) {
        id = object.optString("id");
        name = object.optString("name");
        desc = object.optString("desc");
        url = object.optString("url");
        // TODO: get tags:    "tags":[]
        // JSONArray tagsArray = object.optJSONArray("tags");
        isCover = object.optBoolean("cover");
        status = object.optString("status");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getUrl() {
        return url;
    }

    public String getStatus() {
        return status;
    }

    public boolean isCover() {
        return isCover;
    }
}
