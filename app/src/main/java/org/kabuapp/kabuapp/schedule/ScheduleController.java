package org.kabuapp.kabuapp.schedule;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.kabuapp.kabuapp.api.DigikabuApiService;
import org.kabuapp.kabuapp.api.exceptions.UnauthorisedException;
import org.kabuapp.kabuapp.api.models.LessonResponse;
import org.kabuapp.kabuapp.interfaces.AuthCallback;
import org.kabuapp.kabuapp.db.ScheduleMapper;
import org.kabuapp.kabuapp.db.model.AppDatabase;
import org.kabuapp.kabuapp.data.memory.MemSchedule;
import org.kabuapp.kabuapp.interfaces.Callback;
import org.kabuapp.kabuapp.lifetime.LifetimeController;

@AllArgsConstructor
public class ScheduleController
{
    private DigikabuApiService apiService;
    private ScheduleMapper scheduleMapper;
    private LifetimeController lifetimeController;
    @Getter
    private MemSchedule schedule;
    private AppDatabase db;
    private ExecutorService executorService;

    public void updateSchedule(String token, AuthCallback re, Callback ce, Object[] objects, Duration duration)
    {
        executorService.execute(() ->
        {
            if (lifetimeController.isLifetimeExpired(duration, org.kabuapp.kabuapp.lifetime.Lifetime.SCHEDULE))
            {
                updateSchedule(token, re);
                if (ce != null)
                {
                    ce.callback(objects);
                }
                lifetimeController.updateLifetime(org.kabuapp.kabuapp.lifetime.Lifetime.SCHEDULE);
                lifetimeController.saveLifetimeToDb();
            }
        });
    }

    private void updateSchedule(String tokenIn, AuthCallback re)
    {
        executorService.execute(() -> db.lessonDao().deleteAll());
        String token = tokenIn;
        LocalDate begin = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1);
        try
        {
            updateSchedule(begin, 7, token);
            updateSchedule(begin.plusDays(7), 7, token);
        }
        catch (UnauthorisedException ignored)
        {
            token = re.renewToken();
        }
        try
        {
            updateSchedule(begin, 7, token);
            updateSchedule(begin.plusDays(7), 7, token);
        }
        catch (UnauthorisedException ignored)
        {
        }
    }

    private void updateSchedule(LocalDate date, int days, String token) throws UnauthorisedException
    {
        List<LessonResponse> responses = apiService.getSchedule(token, date, days);
        if (responses == null)
        {
            return;
        }
        scheduleMapper.mapApiResToSchedule(responses, schedule);
        executorService.execute(() -> db.lessonDao().insertAll(scheduleMapper.mapScheduleToDb(schedule)));
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
