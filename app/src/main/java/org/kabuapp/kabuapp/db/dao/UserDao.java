package org.kabuapp.kabuapp.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import org.kabuapp.kabuapp.db.model.entity.User;

@Dao
public interface UserDao
{
    @Query("SELECT * FROM users")
    List<User> getAll();
    @Insert
    void insert(User user);
    @Update
    void update(User user);
}
