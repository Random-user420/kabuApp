package org.kabuapp.kabuapp.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.kabuapp.kabuapp.db.model.entity.Settings;

@Dao
public interface SettingsDao
{
    @Query("SELECT * FROM settings")
    Settings get();
    @Insert
    void insert(Settings settings);
    @Update
    void update(Settings settings);
}
