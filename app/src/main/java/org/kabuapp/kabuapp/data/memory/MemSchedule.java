package org.kabuapp.kabuapp.data.memory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MemSchedule
{
    private Map<LocalDate, List<MemLesson>> lessons;
    private LocalDate selectedDate;

    public synchronized Map<LocalDate, List<MemLesson>> getLessons()
    {
        return lessons;
    }

    public synchronized void setLessons(Map<LocalDate, List<MemLesson>> lessons)
    {
        this.lessons = lessons;
    }

    public synchronized LocalDate getSelectedDate()
    {
        return selectedDate;
    }

    public synchronized void setSelectedDate(LocalDate selectedDate)
    {
        this.selectedDate = selectedDate;
    }
}
