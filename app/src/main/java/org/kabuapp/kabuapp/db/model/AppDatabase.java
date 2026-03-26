package org.kabuapp.kabuapp.db.model;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import org.kabuapp.kabuapp.db.dao.ExamDao;
import org.kabuapp.kabuapp.db.dao.LessonDao;
import org.kabuapp.kabuapp.db.dao.LifetimeDao;
import org.kabuapp.kabuapp.db.dao.SettingsDao;
import org.kabuapp.kabuapp.db.dao.UserDao;
import org.kabuapp.kabuapp.db.model.entity.Exam;
import org.kabuapp.kabuapp.db.model.entity.Lesson;
import org.kabuapp.kabuapp.db.model.entity.Lifetime;
import org.kabuapp.kabuapp.db.model.entity.Settings;
import org.kabuapp.kabuapp.db.model.entity.User;

@Database(entities = { User.class, Lesson.class, Lifetime.class, Exam.class, Settings.class }, version = 2)
public abstract class AppDatabase extends RoomDatabase
{
    private static final Migration MIGRATION_1_2 = new Migration(1, 2)
    {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database)
        {
            database.execSQL("ALTER TABLE settings ADD COLUMN notificationNextDayExam INTEGER NOT NULL DEFAULT 0");
        }
    };
    private static volatile AppDatabase instance;
    public abstract UserDao userDao();
    public abstract ExamDao examDao();
    public abstract LessonDao lessonDao();
    public abstract LifetimeDao lifetimeDao();
    public abstract SettingsDao settingsDao();

    public static AppDatabase getDatabase(final Context context)
    {
        if (instance == null)
        {
            synchronized (AppDatabase.class)
            {
                if (instance == null)
                {
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "kabuApp-db")
                        .addMigrations(MIGRATION_1_2)
                        .build();
                }
            }
        }
        return instance;
    }
}