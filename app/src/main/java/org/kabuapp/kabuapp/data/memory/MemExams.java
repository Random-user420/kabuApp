package org.kabuapp.kabuapp.data.memory;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class MemExams
{
    private Map<LocalDate, MemExam> exams;

    public MemExams()
    {
        this.exams = new LinkedHashMap<>();
    }

    public synchronized Map<LocalDate, MemExam> getExams()
    {
        return exams;
    }

    public synchronized void reset()
    {
        exams = new LinkedHashMap<>();
    }
}
