package org.lilith.kabuapp.schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.lilith.kabuapp.api.DigikabuApiService;
import org.lilith.kabuapp.api.UnauthorisedException;
import org.lilith.kabuapp.api.models.AuthCallback;
import org.lilith.kabuapp.data.ScheduleMapper;
import org.lilith.kabuapp.data.model.AppDatabase;
import org.lilith.kabuapp.data.model.Schedule;
import org.lilith.kabuapp.data.model.entity.Lesson;
import org.lilith.kabuapp.data.model.entity.Lifetime;
import org.lilith.kabuapp.models.Callback;

@AllArgsConstructor
public class ScheduleController
{
    private DigikabuApiService apiService;
    private ScheduleMapper scheduleMapper;
    @Getter
    private Schedule schedule;
    private AppDatabase db;
    private ExecutorService executorService;

    public void updateScheduleIfOld(String token, AuthCallback re, Callback ce, Object[] objects)
    {
        executorService.execute(() ->
        {
            List<Lifetime> lifetimes = db.lifetimeDao().getAll();
            if (lifetimes.isEmpty())
            {
                db.lifetimeDao().insert(new Lifetime(0, LocalDateTime.now()));
                updateSchedule(token, re, ce, objects);
            }
            else if (lifetimes.get(0).getScheduleLastUpdate().isBefore(LocalDateTime.now().minusHours(2)))
            {
                updateSchedule(token, re, ce, objects);
                executorService.execute(() ->
                {
                    Lifetime lifetime = lifetimes.get(0);
                    lifetime.setScheduleLastUpdate(LocalDateTime.now());
                    db.lifetimeDao().update(lifetime);
                });
            }
        });
    }

    public void updateSchedule(String token, AuthCallback re, Callback ce, Object[] objects)
    {
        updateSchedule(token, re);
        ce.callback(objects);
    }

    public void updateSchedule(String tokenIn, AuthCallback re)
    {
        String token = tokenIn;
        LocalDate beginn = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1);
        try
        {
            updateSchedule(beginn, 5, token);
            updateSchedule(beginn.plusDays(7), 5, token);
        }
        catch (UnauthorisedException ignored)
        {
            token = re.renewToken();
        }
        try
        {
            updateSchedule(beginn, 5, token);
            updateSchedule(beginn.plusDays(7), 5, token);
        }
        catch (UnauthorisedException ignored)
        {
        }
    }

    public void updateSchedule(LocalDate date, int days, String token) throws UnauthorisedException
    {
        scheduleMapper.mapApiResToSchedule(apiService.getSchedule(token, date, days), schedule);
        List<Lesson> dbLessons = scheduleMapper.mapScheduleToDb(schedule);
        executorService.execute(() -> db.lessonDao().insertAll(dbLessons));
        Logger.getLogger("ScheduleController").log(Level.INFO, "got schedule");
    }

    public void getDbSchedule()
    {
        executorService.execute(() ->
        {
            scheduleMapper.mapDbLessonToSchedule(db.lessonDao().getAll(), schedule);
        });
    }

    public boolean isSchool(LocalDate date)
    {
        return schedule.getLessons().containsKey(date)
                && !schedule.getLessons().get(date).isEmpty()
                && schedule.getLessons().get(date).values().stream().findAny().isPresent()
                && !schedule.getLessons().get(date).values().stream().findAny().get().isEmpty()
                && !schedule.getLessons().get(date).values().stream().findAny().get().values().stream().findAny().isEmpty();
    }
}
