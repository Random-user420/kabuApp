package org.kabuapp.kabuapp.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.kabuapp.kabuapp.db.model.entity.Lifetime;

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
