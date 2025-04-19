package org.lilith.kabuapp.data.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import org.lilith.kabuapp.data.model.entity.Lesson;

@Dao
public interface LessonDao
{
    @Query("SELECT * FROM schedule")
    List<Lesson> getAll();
    @Insert
    void insert(Lesson Lesson);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Lesson> lessons);
    @Update
    void update(Lesson Lesson);
}
