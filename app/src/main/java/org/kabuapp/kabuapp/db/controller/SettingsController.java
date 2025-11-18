package org.kabuapp.kabuapp.db.controller;

import org.kabuapp.kabuapp.data.memory.MemSettings;
import org.kabuapp.kabuapp.db.model.AppDatabase;
import org.kabuapp.kabuapp.db.model.entity.Settings;

import java.util.Objects;
import java.util.concurrent.ExecutorService;

public class SettingsController {
    private MemSettings settings;
    private ExecutorService executorService;
    private AppDatabase db;

    public SettingsController(ExecutorService executorService, AppDatabase db) {
        this.executorService = executorService;
        this.db = db;
    }

    public boolean isIsoDate() {
        return settings.isIsoDate();
    }

    public void setIsoDate(boolean isoDate) {
        if (!Objects.equals(isoDate, settings.isIsoDate())) {
            settings.setIsoDate(isoDate);
            updateSettings();
        }
    }

    private void updateSettings() {
        executorService.execute(() -> db.settingsDao().update(toSettings()));
    }

    public void loadSettings() {
        executorService.execute(() -> {
            Settings dbSettings = db.settingsDao().get();
            if (dbSettings == null) {
                dbSettings = new Settings(0, false);
                db.settingsDao().insert(dbSettings);
            }
            toMemSettings(dbSettings);
        });
    }

    private Settings toSettings()
    {
        return new Settings(0, settings.isIsoDate());
    }

    private void toMemSettings(Settings dbSettings)
    {
        settings = new MemSettings(dbSettings.isIsoDate());
    }
}
