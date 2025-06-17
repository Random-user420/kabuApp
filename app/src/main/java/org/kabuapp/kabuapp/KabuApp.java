package org.kabuapp.kabuapp;

import android.app.Application;
import android.os.StrictMode;
import com.google.android.material.color.DynamicColors;
import lombok.Getter;
import lombok.Setter;
import org.kabuapp.kabuapp.api.DigikabuApiService;
import org.kabuapp.kabuapp.data.memory.MemExams;
import org.kabuapp.kabuapp.data.memory.MemLifetime;
import org.kabuapp.kabuapp.db.ExamMapper;
import org.kabuapp.kabuapp.db.ScheduleMapper;
import org.kabuapp.kabuapp.data.memory.AuthStateholder;
import org.kabuapp.kabuapp.db.controller.SessionController;
import org.kabuapp.kabuapp.db.model.AppDatabase;
import org.kabuapp.kabuapp.data.memory.MemSchedule;
import org.kabuapp.kabuapp.db.controller.ExamController;
import org.kabuapp.kabuapp.db.controller.LifetimeController;
import org.kabuapp.kabuapp.db.controller.AuthController;
import org.kabuapp.kabuapp.db.controller.ScheduleController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class KabuApp extends Application
{
    private AppDatabase db;
    private AuthController authController;
    private DigikabuApiService digikabuApiService;
    private MemSchedule schedule;
    private ScheduleController scheduleController;
    private ExamController examController;
    private LifetimeController lifetimeController;
    private ExamMapper examMapper;
    private ScheduleMapper scheduleMapper;
    private SessionController sessionController;
    private ExecutorService executorService;

    @Override
    public void onCreate()
    {
        super.onCreate();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DynamicColors.applyToActivitiesIfAvailable(this);

        executorService = Executors.newCachedThreadPool();

        schedule = new MemSchedule();
        schedule.setSelectedDate(LocalDate.now());

        db = AppDatabase.getDatabase(getApplicationContext());
        digikabuApiService = new DigikabuApiService();
        scheduleMapper = new ScheduleMapper();
        examMapper = new ExamMapper();

        lifetimeController = new LifetimeController(db, executorService, new MemLifetime());
        scheduleController = new ScheduleController(digikabuApiService, scheduleMapper, lifetimeController, schedule, db, executorService);
        authController = new AuthController(new AuthStateholder(), db, digikabuApiService, executorService);
        examController = new ExamController(new MemExams(), examMapper, lifetimeController, digikabuApiService, executorService, db);
        sessionController = new SessionController(db, examController, lifetimeController, authController, scheduleController, executorService);

        sessionController.loadSession();
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        try
        {
            digikabuApiService.closeHttpClient();
        }
        catch (IOException ignored)
        {
        }
        if (executorService != null && !executorService.isShutdown())
        {
            executorService.shutdown();
            try
            {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS))
                {
                    executorService.shutdownNow();
                }
            }
            catch (InterruptedException e)
            {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
