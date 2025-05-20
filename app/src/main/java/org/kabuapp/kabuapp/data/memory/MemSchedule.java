package org.kabuapp.kabuapp.data.memory;

import java.time.LocalDate;
import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MemSchedule
{
    //date, begin, group
    private Map<LocalDate, Map<Short, Map<Short, MemLesson>>> lessons;
    private LocalDate selectedDate;

    public synchronized Map<LocalDate, Map<Short, Map<Short, MemLesson>>> getLessons()
    {
        return lessons;
    }

    public synchronized void setLessons(Map<LocalDate, Map<Short, Map<Short, MemLesson>>> lessons)
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
