package org.lilith.kabuapp;

import android.app.Application;
import android.os.StrictMode;

import com.google.android.material.color.DynamicColors;

import org.lilith.kabuapp.api.DigikabuApiService;
import org.lilith.kabuapp.data.ScheduleMapper;
import org.lilith.kabuapp.data.memory.AuthStateholder;
import org.lilith.kabuapp.data.model.AppDatabase;
import org.lilith.kabuapp.data.model.Schedule;
import org.lilith.kabuapp.login.AuthController;
import org.lilith.kabuapp.schedule.ScheduleController;

import lombok.Getter;
import lombok.Setter;

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
    //NOT FOR PRODUCTION!!!
    private final boolean fakeService = true;

    @Override
    public void onCreate()
    {
        super.onCreate();

        //Until Threadding with login is implimented
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DynamicColors.applyToActivitiesIfAvailable(this);

        schedule = new Schedule();
        db = AppDatabase.getDatabase(getApplicationContext());
        digikabuApiService = new DigikabuApiService();
        scheduleMapper = new ScheduleMapper();
        scheduleController = new ScheduleController(digikabuApiService, scheduleMapper, schedule);
        authController = new AuthController(new AuthStateholder(), db, digikabuApiService, fakeService);

        authController.getInitialUser();
    }
}
