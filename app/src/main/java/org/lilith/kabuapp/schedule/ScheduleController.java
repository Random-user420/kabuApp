package org.lilith.kabuapp.schedule;

import org.lilith.kabuapp.api.DigikabuApiService;
import org.lilith.kabuapp.data.ScheduleMapper;
import org.lilith.kabuapp.data.model.Schedule;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ScheduleController {
    private DigikabuApiService apiService;
    private ScheduleMapper scheduleMapper;
    private Schedule schedule;

    public void updateSchedule(LocalDate date, int days, String token)
    {
        //TODO async
        scheduleMapper.mapApiResToSchedule(apiService.getSchedule(token, date, days), schedule);
        Logger.getLogger("ScheduleController").log(Level.INFO, "got schedule");
    }
}
