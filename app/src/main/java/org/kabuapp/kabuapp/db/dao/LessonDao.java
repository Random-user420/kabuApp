package org.kabuapp.kabuapp.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
import org.kabuapp.kabuapp.db.model.entity.Lesson;

@Dao
public interface LessonDao
{
    @Query("SELECT * FROM schedule")
    List<Lesson> getAll();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Lesson> lessons);
    @Query("DELETE FROM schedule")
    void deleteAll();
}
