package org.kabuapp.kabuapp.data.memory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MemExams
{
    private final Map<LocalDate, MemExam> exams = new HashMap<>();
    public synchronized Map<LocalDate, MemExam> getExams()
    {
        return exams;
    }
}
