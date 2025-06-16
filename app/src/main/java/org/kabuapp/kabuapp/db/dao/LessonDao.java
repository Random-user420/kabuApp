package org.kabuapp.kabuapp.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.UUID;

import org.kabuapp.kabuapp.db.model.entity.Lesson;

@Dao
public interface LessonDao
{
    @Query("SELECT * FROM schedule WHERE userId = :userId")
    List<Lesson> get(UUID userId);
    @Query("SELECT * FROM schedule")
    List<Lesson> getAll();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Lesson> lessons);
    @Update
    void update(Lesson lesson);
    @Query("DELETE FROM schedule")
    void deleteAll();
    @Query("DELETE FROM schedule WHERE userId = :userId")
    void deletePerUser(UUID userId);
}
