package org.lilith.kabuapp.data.model;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Lesson
{
    private short begin;
    private short end;
    private LocalDate date;
    private short group;
    private short maxGroup;
    private String name;
    private String teacher;
    private String room;
    private UUID dbId;
    //TODO
    //private Color color;
}
