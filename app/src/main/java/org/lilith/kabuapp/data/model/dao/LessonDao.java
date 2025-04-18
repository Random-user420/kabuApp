package org.lilith.kabuapp.data.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.lilith.kabuapp.data.model.entity.Lesson;

import java.util.List;

@Dao
public interface LessonDao {
    @Query("SELECT * FROM schedule")
    List<Lesson> getAll();

    @Insert
    void insert(Lesson Lesson);

    @Update
    void update(Lesson Lesson);
}
