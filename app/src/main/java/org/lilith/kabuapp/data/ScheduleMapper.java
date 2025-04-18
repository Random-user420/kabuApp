package org.lilith.kabuapp.data;

import org.lilith.kabuapp.api.models.LessonResponse;
import org.lilith.kabuapp.data.model.Lesson;
import org.lilith.kabuapp.data.model.Schedule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ScheduleMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMAN);

    public void mapApiResToSchedule(List<LessonResponse> lessonResponses, Schedule schedule)
    {
        if (schedule.getLessons() == null)
        {
            schedule.setLessons(new LinkedHashMap<>());
        }
        if (schedule.getLessons() == null)
        {
            return;
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
                    lessonResponse.getRaumLongtext(),
                    null);

            if (!schedule.getLessons().containsKey(lesson.getDate()))
            {
                schedule.getLessons().put(lesson.getDate(), new LinkedHashMap<>());
            }

            else if (schedule.getLessons().get(lesson.getDate()) != null && schedule.getLessons().get(lesson.getDate()).get(lesson.getBegin()) != null)
            {
                lesson.setDbId(schedule.getLessons().get(lesson.getDate()).get(lesson.getBegin()).getDbId());
            }

            schedule.getLessons().get(lesson.getDate()).put(lesson.getBegin(), lesson);
        }
    }

    public List<org.lilith.kabuapp.data.model.entity.Lesson> mapScheduleToDb(Schedule schedule)
    {
        List<org.lilith.kabuapp.data.model.entity.Lesson> dbLessons = new ArrayList<>();
        if (schedule != null && schedule.getLessons() != null)
        {
            for (LocalDate date : schedule.getLessons().keySet())
            {
                for (short begin : schedule.getLessons().get(date).keySet()) {
                    Lesson lesson = schedule.getLessons().get(date).get(begin);
                    if (lesson.getDbId() == null) {
                        lesson.setDbId(UUID.randomUUID());
                    }
                    org.lilith.kabuapp.data.model.entity.Lesson dbLesson = new org.lilith.kabuapp.data.model.entity.Lesson(
                            lesson.getDbId(),
                            lesson.getBegin(),
                            lesson.getEnd(),
                            lesson.getDate(),
                            lesson.getGroup(),
                            lesson.getMaxGroup(),
                            lesson.getName(),
                            lesson.getTeacher(),
                            lesson.getRoom()
                    );
                    dbLessons.add(dbLesson);
                }
            }
        }
        return dbLessons;
    }

    public void mapDbLessonToSchedule(List<org.lilith.kabuapp.data.model.entity.Lesson> dbLessons, Schedule schedule)
    {
        if (schedule.getLessons() == null)
        {
            schedule.setLessons(new LinkedHashMap<>());
        }
        for (org.lilith.kabuapp.data.model.entity.Lesson dbLesson : dbLessons)
        {
            Lesson lesson = new Lesson(
                    dbLesson.getBegin(),
                    dbLesson.getEnd(),
                    dbLesson.getDate(),
                    dbLesson.getGroup(),
                    dbLesson.getMaxGroup(),
                    dbLesson.getName(),
                    dbLesson.getTeacher(),
                    dbLesson.getRoom(),
                    dbLesson.getId()
            );
            if (!schedule.getLessons().containsKey(lesson.getDate()))
            {
                schedule.getLessons().put(lesson.getDate(), new LinkedHashMap<>());
            }
            schedule.getLessons().get(lesson.getDate()).put(lesson.getBegin(), lesson);
        }
    }
}
