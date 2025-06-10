package org.kabuapp.kabuapp.db.model;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.kabuapp.kabuapp.db.dao.ExamDao;
import org.kabuapp.kabuapp.db.dao.LessonDao;
import org.kabuapp.kabuapp.db.dao.LifetimeDao;
import org.kabuapp.kabuapp.db.dao.UserDao;
import org.kabuapp.kabuapp.db.model.entity.Exam;
import org.kabuapp.kabuapp.db.model.entity.Lesson;
import org.kabuapp.kabuapp.db.model.entity.Lifetime;
import org.kabuapp.kabuapp.db.model.entity.User;

@Database(entities = {User.class, Lesson.class, Lifetime.class, Exam.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    private static volatile AppDatabase instance;
    public abstract UserDao userDao();
    public abstract LessonDao lessonDao();
    public abstract LifetimeDao lifetimeDao();
    public abstract ExamDao examDao();

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