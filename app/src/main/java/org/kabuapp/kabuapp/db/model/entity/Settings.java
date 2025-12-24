package org.kabuapp.kabuapp.db.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity(tableName = "settings")
public class Settings
{
    @PrimaryKey()
    private int id;
    @ColumnInfo(name = "isoDate")
    private boolean isoDate;
}
