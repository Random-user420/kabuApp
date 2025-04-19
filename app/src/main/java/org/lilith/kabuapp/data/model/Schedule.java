package org.lilith.kabuapp.data.model;

import java.time.LocalDate;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Schedule
{
    private Map<LocalDate, Map<Short, Lesson>> lessons;
}
