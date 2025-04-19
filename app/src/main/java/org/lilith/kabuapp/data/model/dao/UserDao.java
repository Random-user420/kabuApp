package org.lilith.kabuapp.data.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import org.lilith.kabuapp.data.model.entity.User;

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
