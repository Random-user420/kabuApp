package org.kabuapp.kabuapp.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.kabuapp.kabuapp.db.model.entity.Exam;

import java.util.List;
import java.util.UUID;

@Dao
public interface ExamDao
{
    @Query("SELECT * FROM exams WHERE userId = :userId")
    List<Exam> get(UUID userId);

    @Query("SELECT * FROM exams")
    List<Exam> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Exam> exams);

    @Update
    void update(Exam exam);

    @Query("DELETE FROM exams WHERE userId = :userId")
    void deletePerUser(UUID userId);

    @Query("DELETE FROM exams")
    void deleteAll();
}