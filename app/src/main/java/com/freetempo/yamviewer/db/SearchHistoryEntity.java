package com.freetempo.yamviewer.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "SearchData")
public class SearchHistoryEntity {

    @PrimaryKey
    @NonNull
    public String userName;

}

