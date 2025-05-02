package org.lilith.kabuapp.data.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.lilith.kabuapp.data.LocalDateTimeConverter;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity(tableName = "lifetimes")
@TypeConverters({LocalDateTimeConverter.class})
public class Lifetime
{
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "schedule")
    private LocalDateTime scheduleLastUpdate;
}
