package com.freetempo.yamviewer;

import org.json.JSONObject;

public class AlbumInfo {

    private String id, name, description, cover, diskSpace, password, version, visible, status;
    private String imageTable, albumStatus;
    private int photos, categoryId;
    private long sort, bloggerId, createdAt, updatedAt;

    public AlbumInfo(JSONObject jsonObject) {
        id = jsonObject.optString("id");
        name = jsonObject.optString("name");
        description = jsonObject.optString("description");
        sort = jsonObject.optLong("sort");
        cover = jsonObject.optString("cover");
        photos = jsonObject.optInt("photos");
        diskSpace = jsonObject.optString("disk_space");
        password = jsonObject.optString("password");
        version = jsonObject.optString("version");
        visible = jsonObject.optString("visible");
        status = jsonObject.optString("status");
        imageTable = jsonObject.optString("image_table");
        categoryId = jsonObject.optInt("category_id");
        bloggerId = jsonObject.optLong("blogger_id");
        createdAt = jsonObject.optLong("created_at");
        updatedAt = jsonObject.optLong("updated_at");
        albumStatus = jsonObject.optString("album_status");
    }

    // getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCover() {
        return cover;
    }

    public String getDiskSpace() {
        return diskSpace;
    }

    public String getPassword() {
        return password;
    }

    public String getVersion() {
        return version;
    }

    public String getVisible() {
        return visible;
    }

    public String getStatus() {
        return status;
    }

    public String getImageTable() {
        return imageTable;
    }

    public String getAlbumStatus() {
        return albumStatus;
    }

    public int getPhotos() {
        return photos;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public long getSort() {
        return sort;
    }

    public long getBloggerId() {
        return bloggerId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }
}
