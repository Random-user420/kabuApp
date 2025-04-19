package org.lilith.kabuapp.data.model;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import org.lilith.kabuapp.data.model.dao.LessonDao;
import org.lilith.kabuapp.data.model.dao.UserDao;
import org.lilith.kabuapp.data.model.entity.Lesson;
import org.lilith.kabuapp.data.model.entity.User;

@Database(entities = {User.class, Lesson.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract UserDao userDao();
    public abstract LessonDao lessonDao();
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context)
    {
        if (INSTANCE == null)
        {
            synchronized (AppDatabase.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "kabuApp-db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}