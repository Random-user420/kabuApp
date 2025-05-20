package org.kabuapp.kabuapp.schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.kabuapp.kabuapp.api.DigikabuApiService;
import org.kabuapp.kabuapp.api.UnauthorisedException;
import org.kabuapp.kabuapp.interfaces.AuthCallback;
import org.kabuapp.kabuapp.data.ScheduleMapper;
import org.kabuapp.kabuapp.data.model.AppDatabase;
import org.kabuapp.kabuapp.data.memory.Schedule;
import org.kabuapp.kabuapp.data.model.entity.Lesson;
import org.kabuapp.kabuapp.data.model.entity.Lifetime;
import org.kabuapp.kabuapp.interfaces.Callback;

@AllArgsConstructor
public class ScheduleController
{
    private DigikabuApiService apiService;
    private ScheduleMapper scheduleMapper;
    @Getter
    private Schedule schedule;
    private AppDatabase db;
    private ExecutorService executorService;

    public void updateSchedule(String token, AuthCallback re, Callback ce, Object[] objects, LocalDateTime time)
    {
        executorService.execute(() ->
        {
            List<Lifetime> lifetimes = db.lifetimeDao().getAll();
            if (lifetimes.isEmpty())
            {
                executorService.execute(() -> db.lifetimeDao().insert(new Lifetime(0, LocalDateTime.now())));
                updateSchedule(token, re, ce, objects);
            }
            else if (lifetimes.get(0).getScheduleLastUpdate().isBefore(time))
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

    private void updateSchedule(String token, AuthCallback re, Callback ce, Object[] objects)
    {
        updateSchedule(token, re);
        ce.callback(objects);
    }

    private void updateSchedule(String tokenIn, AuthCallback re)
    {
        executorService.execute(() -> db.lessonDao().deleteAll());
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

    private void updateSchedule(LocalDate date, int days, String token) throws UnauthorisedException
    {
        scheduleMapper.mapApiResToSchedule(apiService.getSchedule(token, date, days), schedule);
        List<Lesson> dbLessons = scheduleMapper.mapScheduleToDb(schedule);
        executorService.execute(() -> db.lessonDao().insertAll(dbLessons));
    }

    public void getDbSchedule()
    {
        executorService.execute(() -> scheduleMapper.mapDbLessonToSchedule(db.lessonDao().getAll(), schedule));
    }

    public boolean isSchool(LocalDate date)
    {
        return schedule.getLessons() != null
                && schedule.getLessons().containsKey(date)
                && !schedule.getLessons().get(date).isEmpty()
                && schedule.getLessons().get(date).values().stream().findAny().isPresent()
                && !schedule.getLessons().get(date).values().stream().findAny().get().isEmpty()
                && !schedule.getLessons().get(date).values().stream().findAny().get().values().stream().findAny().isEmpty();
    }

    public void resetSchedule()
    {
        schedule.setLessons(new HashMap<>());
        executorService.execute(() -> db.lessonDao().deleteAll());
    }
}
