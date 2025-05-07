package org.kabuapp.kabuapp.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LessonResponse
{
    private String datum;
    private int anfStd;
    private int endStd;
    private String lehrer;
    private String uFachBez;
    private String raumLongtext;
    private String gruppe;
}