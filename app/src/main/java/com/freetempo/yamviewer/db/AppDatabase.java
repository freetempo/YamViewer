package com.freetempo.yamviewer.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {SearchHistoryEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    private static final Object sLock = new Object();
    public abstract SearchHistoryDao searchHistoryDao();

    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "searchHistory.db")
                                .allowMainThreadQueries()
                                .build();
            }
            return INSTANCE;
        }
    }
}

