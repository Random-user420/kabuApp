package org.lilith.kabuapp.data.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Lesson {
    private short begin;
    private short end;
    private LocalDate date;
    private short group;
    private short maxGroup;
    private String name;
    private String teacher;
    private String room;
    //TODO
    //private Color color;
}
