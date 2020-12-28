package com.freetempo.yamviewer.db;

//import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface SearchHistoryDao {

    @Query("SELECT * FROM SearchData")
    List<SearchHistoryEntity> loadAllSearchHistory();

//    @Query("SELECT * FROM SearchData")
//    LiveData<List<SearchHistoryEntity>> loadAllSearchHistoryLive();

    @Insert(onConflict = REPLACE)
    void insertSearchHistory(SearchHistoryEntity... searchHistoryEntities);

    @Delete
    void deleteTSearchHistory(SearchHistoryEntity... searchHistoryEntities);

    @Query("DELETE FROM SearchData")
    void clearAllSearchHistory();

}
