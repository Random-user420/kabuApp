package org.lilith.kabuapp.data.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.lilith.kabuapp.data.model.entity.Lifetime;

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
