package org.lilith.kabuapp.data.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Entity(tableName = "users")
public class User {

    @Getter
    @Setter
    @NotNull
    @PrimaryKey()
    private UUID id;

    @Getter
    @Setter
    @ColumnInfo(name = "username")
    private String username;

    @Getter
    @Setter
    @ColumnInfo(name = "password")
    private String password;

    @Getter
    @Setter
    @ColumnInfo(name = "token")
    private String token;

}
