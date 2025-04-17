package org.lilith.kabuapp.data.model;

import java.util.Map;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Schedule {
    private Map<LocalDate, Map<Short, Lesson>> lessons;
}
