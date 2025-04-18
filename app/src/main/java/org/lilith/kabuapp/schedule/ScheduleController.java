package org.lilith.kabuapp.schedule;

import org.lilith.kabuapp.api.DigikabuApiService;
import org.lilith.kabuapp.data.ScheduleMapper;
import org.lilith.kabuapp.data.model.AppDatabase;
import org.lilith.kabuapp.data.model.Schedule;
import org.lilith.kabuapp.data.model.dao.UserDao;
import org.lilith.kabuapp.data.model.entity.Lesson;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ScheduleController {
    private DigikabuApiService apiService;
    private ScheduleMapper scheduleMapper;
    private Schedule schedule;
    private AppDatabase db;

    public void updateSchedule(LocalDate date, int days, String token)
    {
        //TODO async
        scheduleMapper.mapApiResToSchedule(apiService.getSchedule(token, date, days), schedule);
        List<Lesson> dbLessons = scheduleMapper.mapScheduleToDb(schedule);
        new Thread(() ->  {  db.lessonDao().insertAll(dbLessons);  }).start();
        Logger.getLogger("ScheduleController").log(Level.INFO, "got schedule");
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
