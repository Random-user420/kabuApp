package org.lilith.kabuapp.schedule;

import org.lilith.kabuapp.api.DigikabuApiService;
import org.lilith.kabuapp.api.UnauthorisedException;
import org.lilith.kabuapp.api.models.AuthCallback;
import org.lilith.kabuapp.data.ScheduleMapper;
import org.lilith.kabuapp.data.model.AppDatabase;
import org.lilith.kabuapp.data.model.Schedule;
import org.lilith.kabuapp.data.model.dao.UserDao;
import org.lilith.kabuapp.data.model.entity.Lesson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ScheduleController {
    private DigikabuApiService apiService;
    private ScheduleMapper scheduleMapper;
    private Schedule schedule;
    private AppDatabase db;

    public void updateSchedule(String token, AuthCallback re)
    {
        // note "anzahl" is buggy in api
        LocalDate beginn = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now().plusDays(8);
        try
        {
            for (LocalDate date = beginn; date.isBefore(end); date = date.plusDays(1)) {
                updateSchedule(date, 1, token);
            }
        }
        catch (UnauthorisedException ignored)
        {
            token = re.renewToken();
        }
        try
        {
            for (LocalDate date = beginn; date.isBefore(end); date = date.plusDays(1)) {
                updateSchedule(date, 1, token);
            }
        }
        catch (UnauthorisedException ignored)
        {
            Logger.getLogger("ScheduleController").log(Level.WARNING, "Still Unauth");
        }
    }

    public void updateSchedule(LocalDate date, int days, String token) throws UnauthorisedException {
        //TODO async
        scheduleMapper.mapApiResToSchedule(apiService.getSchedule(token, date, days), schedule);
        List<Lesson> dbLessons = scheduleMapper.mapScheduleToDb(schedule);
        new Thread(() -> { db.lessonDao().insertAll(dbLessons); }).start();
        Logger.getLogger("ScheduleController").log(Level.INFO, "got schedule");
    }

    public String getScheduleAsText()
    {
        StringBuilder text = new StringBuilder();
        for (Map<Short, org.lilith.kabuapp.data.model.Lesson> entry : schedule.getLessons().values())
        {
            for (org.lilith.kabuapp.data.model.Lesson lesson : entry.values())
            {
                text.append(lesson.getName())
                        .append(lesson.getRoom())
                        .append(lesson.getTeacher())
                        .append(lesson.getDate().getDayOfMonth())
                        .append(lesson.getDate().getMonthValue())
                        .append(lesson.getBegin())
                        .append(lesson.getEnd())
                        .append(lesson.getGroup())
                        .append(lesson.getMaxGroup());
            }
        }
        return text.toString();
    }

    public void getInitSchedule()
    {
        new Thread(() ->
        {
           List<Lesson> dbLessons = db.lessonDao().getAll();
           scheduleMapper.mapDbLessonToSchedule(dbLessons, schedule);
        });
    }
}
