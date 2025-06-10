package org.kabuapp.kabuapp.db.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;
import org.kabuapp.kabuapp.db.LocalDateConverter;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity(tableName = "exams")
@TypeConverters({LocalDateConverter.class})
public class Exam
{
    @NotNull
    @PrimaryKey()
    private UUID id;
    @ColumnInfo(name = "date")
    private LocalDate date;
    @ColumnInfo(name = "duration")
    private Short duration;
    @ColumnInfo(name = "info")
    private String info;
}
