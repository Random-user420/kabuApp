package org.kabuapp.kabuapp.lifetime;

import org.kabuapp.kabuapp.data.memory.MemLifetime;
import org.kabuapp.kabuapp.db.model.AppDatabase;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class LifetimeController
{
    private AppDatabase db;
    private ExecutorService executorService;
    @Getter
    private MemLifetime memLifetime;

    public void updateLifetime(Lifetime type)
    {
        switch (type)
        {
            case LIFETIME -> memLifetime.setExamLastUpdate(LocalDateTime.now());
            case SCHEDULE -> memLifetime.setScheduleLastUpdate(LocalDateTime.now());
        }
    }

    public boolean isLifetimeExpired(Duration duration, Lifetime type)
    {
        return switch (type)
        {
            case LIFETIME -> memLifetime.getExamLastUpdate() == null  || memLifetime.getExamLastUpdate().isBefore(LocalDateTime.now().minus(duration));
            case SCHEDULE -> memLifetime.getScheduleLastUpdate() == null  || memLifetime.getScheduleLastUpdate().isBefore(LocalDateTime.now().minus(duration));
        };
    }

    public void resetLifetimes()
    {
        memLifetime.setScheduleLastUpdate(null);
        memLifetime.setExamLastUpdate(null);
        saveLifetimeToDb();
    }

    public void saveLifetimeToDb()
    {
        executorService.execute(() ->
        {
            List<org.kabuapp.kabuapp.db.model.entity.Lifetime> lifetimeList = db.lifetimeDao().getAll();
            if (lifetimeList != null && !lifetimeList.isEmpty())
            {
                org.kabuapp.kabuapp.db.model.entity.Lifetime lifetime = lifetimeList.get(0);
                lifetime.setExamLastUpdate(memLifetime.getExamLastUpdate());
                lifetime.setScheduleLastUpdate(memLifetime.getScheduleLastUpdate());
                db.lifetimeDao().update(lifetime);
            }
        });
    }

    public void getLifetimeFromDb()
    {
        executorService.execute(() ->
        {
            List<org.kabuapp.kabuapp.db.model.entity.Lifetime> lifetimeList = db.lifetimeDao().getAll();
            if (lifetimeList != null && !lifetimeList.isEmpty())
            {
                memLifetime.setDbId(lifetimeList.get(0).getId());
                memLifetime.setScheduleLastUpdate(lifetimeList.get(0).getScheduleLastUpdate());
                memLifetime.setExamLastUpdate(lifetimeList.get(0).getExamLastUpdate());
            }
        });
    }
}
