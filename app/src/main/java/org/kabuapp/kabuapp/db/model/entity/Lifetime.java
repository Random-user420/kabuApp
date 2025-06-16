package org.kabuapp.kabuapp.db.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.kabuapp.kabuapp.db.LocalDateTimeConverter;

import java.time.LocalDateTime;
import java.util.UUID;

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
    @NonNull
    @PrimaryKey()
    private UUID id;
    @ColumnInfo(name = "userId")
    private UUID userId;
    @ColumnInfo(name = "schedule")
    private LocalDateTime scheduleLastUpdate;
    @ColumnInfo(name = "exam")
    private LocalDateTime examLastUpdate;
}
