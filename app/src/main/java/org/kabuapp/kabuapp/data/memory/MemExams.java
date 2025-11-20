package org.kabuapp.kabuapp.data.memory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemExams
{
    private final Map<LocalDate, MemExam> exams = new HashMap<>();
}
