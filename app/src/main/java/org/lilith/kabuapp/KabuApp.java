package org.lilith.kabuapp;

import android.app.Application;
import android.os.StrictMode;
import com.google.android.material.color.DynamicColors;
import lombok.Getter;
import lombok.Setter;
import org.lilith.kabuapp.api.DigikabuApiService;
import org.lilith.kabuapp.data.ScheduleMapper;
import org.lilith.kabuapp.data.memory.AuthStateholder;
import org.lilith.kabuapp.data.model.AppDatabase;
import org.lilith.kabuapp.data.model.Schedule;
import org.lilith.kabuapp.login.AuthController;
import org.lilith.kabuapp.schedule.ScheduleController;

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
    private Schedule schedule;
    private ScheduleController scheduleController;
    private ScheduleMapper scheduleMapper;
    private ExecutorService executorService;

    @Override
    public void onCreate()
    {
        super.onCreate();

        //Until Threadding with login is implimented
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DynamicColors.applyToActivitiesIfAvailable(this);

        executorService = Executors.newCachedThreadPool();

        schedule = new Schedule();
        schedule.setSelectedDate(LocalDate.now());

        db = AppDatabase.getDatabase(getApplicationContext());
        digikabuApiService = new DigikabuApiService();
        scheduleMapper = new ScheduleMapper();
        scheduleController = new ScheduleController(digikabuApiService, scheduleMapper, schedule, db, executorService);
        authController = new AuthController(new AuthStateholder(), db, digikabuApiService, executorService);

        authController.getInitialUser();
        scheduleController.getDbSchedule();
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
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
