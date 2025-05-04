package org.lilith.kabuapp.data.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
import org.lilith.kabuapp.data.model.entity.Lesson;

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
