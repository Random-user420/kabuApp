package org.lilith.kabuapp.data;

import org.lilith.kabuapp.api.models.LessonResponse;
import org.lilith.kabuapp.data.model.Lesson;
import org.lilith.kabuapp.data.model.Schedule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class ScheduleMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMAN);

    public void mapSchedule(List<LessonResponse> lessonResponses, Schedule schedule)
    {
        if (schedule.getLessons() == null)
        {
            schedule.setLessons(new LinkedHashMap<>());
        }

        for (LessonResponse lessonResponse : lessonResponses)
        {
            Lesson lesson = new Lesson(
                    (short) lessonResponse.getAnfStd(),
                    (short) lessonResponse.getEndStd(),
                    LocalDate.parse(lessonResponse.getDatum(), formatter),
                    Short.parseShort(String.valueOf(lessonResponse.getGruppe().charAt(0))),
                    Short.parseShort(String.valueOf(lessonResponse.getGruppe().charAt(1))),
                    lessonResponse.getUFachBez(),
                    lessonResponse.getLehrer(),
                    lessonResponse.getRaumLongtext());
            if (!schedule.getLessons().containsKey(lesson.getDate()))
            {
                schedule.getLessons().put(lesson.getDate(), new LinkedHashMap<>());
            }
            schedule.getLessons().get(lesson.getDate()).put(lesson.getBegin(), lesson);
        }
    }
}
