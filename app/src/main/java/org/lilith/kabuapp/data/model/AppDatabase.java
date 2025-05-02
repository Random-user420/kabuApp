package org.lilith.kabuapp.data.model;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import org.lilith.kabuapp.data.model.dao.LessonDao;
import org.lilith.kabuapp.data.model.dao.LifetimeDao;
import org.lilith.kabuapp.data.model.dao.UserDao;
import org.lilith.kabuapp.data.model.entity.Lesson;
import org.lilith.kabuapp.data.model.entity.Lifetime;
import org.lilith.kabuapp.data.model.entity.User;

@Database(entities = {User.class, Lesson.class, Lifetime.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    private static volatile AppDatabase instance;
    public abstract UserDao userDao();
    public abstract LessonDao lessonDao();
    public abstract LifetimeDao lifetimeDao();

    public static AppDatabase getDatabase(final Context context)
    {
        if (instance == null)
        {
            synchronized (AppDatabase.class)
            {
                if (instance == null)
                {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "kabuApp-db")
                            .build();
                }
            }
        }
        return instance;
    }
}