package org.kabuapp.kabuapp.data.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.kabuapp.kabuapp.data.model.entity.Lifetime;

import java.util.List;

@Dao
public interface LifetimeDao
{
    @Query("SELECT * FROM lifetimes")
    List<Lifetime> getAll();
    @Insert
    void insert(Lifetime lifetime);
    @Update
    void update(Lifetime lifetime);
}
