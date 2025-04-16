package org.lilith.kabuapp;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

import org.lilith.kabuapp.api.DigikabuApiService;
import org.lilith.kabuapp.data.memory.AuthStateholder;
import org.lilith.kabuapp.data.model.AppDatabase;
import org.lilith.kabuapp.login.AuthController;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KabuApp extends Application
{

    private AppDatabase db;
    private AuthController authController;
    private DigikabuApiService digikabuApiService;

    @Override
    public void onCreate()
    {
        super.onCreate();

        DynamicColors.applyToActivitiesIfAvailable(this);

        db = AppDatabase.getDatabase(getApplicationContext());
        digikabuApiService = new DigikabuApiService();
        authController = new AuthController(new AuthStateholder(), db, digikabuApiService);

        authController.getInitialUser();
    }
}
