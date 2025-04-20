package org.lilith.kabuapp.data.model;

import java.time.LocalDate;
import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Schedule
{
    private Map<LocalDate, Map<Short, Lesson>> lessons;

    public synchronized Map<LocalDate, Map<Short, Lesson>> getLessons()
    {
        return lessons;
    }

    public synchronized void setLessons(Map<LocalDate, Map<Short, Lesson>> lessons)
    {
        this.lessons = lessons;
    }
}
