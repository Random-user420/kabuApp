package org.kabuapp.kabuapp.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.kabuapp.kabuapp.db.model.entity.Exam;

import java.util.List;

@Dao
public interface ExamDao
{
    @Query("SELECT * FROM exams")
    List<Exam> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Exam> exams);

    @Query("DELETE FROM exams")
    void deleteAll();
}