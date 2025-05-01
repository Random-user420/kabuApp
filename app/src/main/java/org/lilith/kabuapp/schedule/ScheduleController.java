package org.lilith.kabuapp.schedule;

import java.time.LocalDate;
import java.util.List;
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
import org.lilith.kabuapp.models.Callback;

@AllArgsConstructor
public class ScheduleController
{
    private DigikabuApiService apiService;
    private ScheduleMapper scheduleMapper;
    @Getter
    private Schedule schedule;
    private AppDatabase db;

    public void updateSchedule(String token, AuthCallback re, Callback ce, Object[] objects)
    {
        updateSchedule(token, re);
        ce.callback(objects);
    }

    public void updateSchedule(String tokenIn, AuthCallback re)
    {
        String token = tokenIn;
        LocalDate beginn = LocalDate.now().minusDays(7);
        try
        {
            updateSchedule(beginn, 7, token);
            updateSchedule(beginn.plusDays(7), 7, token);
        }
        catch (UnauthorisedException ignored)
        {
            token = re.renewToken();
        }
        try
        {
            updateSchedule(beginn, 7, token);
            updateSchedule(beginn.plusDays(7), 7, token);
        }
        catch (UnauthorisedException ignored)
        {
        }
    }

    public void updateSchedule(LocalDate date, int days, String token) throws UnauthorisedException
    {
        scheduleMapper.mapApiResToSchedule(apiService.getSchedule(token, date, days), schedule);
        List<Lesson> dbLessons = scheduleMapper.mapScheduleToDb(schedule);
        new Thread(() -> db.lessonDao().insertAll(dbLessons)).start();
        Logger.getLogger("ScheduleController").log(Level.INFO, "got schedule");
    }
}
