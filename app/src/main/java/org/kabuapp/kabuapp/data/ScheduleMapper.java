package org.kabuapp.kabuapp.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.kabuapp.kabuapp.api.models.LessonResponse;
import org.kabuapp.kabuapp.data.memory.Lesson;
import org.kabuapp.kabuapp.data.memory.Schedule;

public class ScheduleMapper
{
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

        if (lessonResponses == null)
        {
            return;
        }
        lessonResponses.stream().map(lessonResponse -> new Lesson(
                (short) lessonResponse.getAnfStd(),
                (short) lessonResponse.getEndStd(),
                LocalDate.parse(lessonResponse.getDatum(), formatter),
                Short.parseShort(String.valueOf(lessonResponse.getGruppe().charAt(0))),
                Short.parseShort(String.valueOf(lessonResponse.getGruppe().charAt(2))),
                lessonResponse.getUFachBez(),
                lessonResponse.getLehrer(),
                lessonResponse.getRaumLongtext(),
                null))
                .forEach(lesson ->
                {
                    if (!schedule.getLessons().containsKey(lesson.getDate()))
                    {
                        schedule.getLessons().put(lesson.getDate(), new LinkedHashMap<>());
                    }
                    if (!schedule.getLessons().get(lesson.getDate()).containsKey(lesson.getBegin()))
                    {
                        schedule.getLessons().get(lesson.getDate()).put(lesson.getBegin(), new LinkedHashMap<>());
                    }
                    else if (schedule.getLessons().get(lesson.getDate()) != null
                            && schedule.getLessons().get(lesson.getDate()).get(lesson.getBegin()) != null
                            && schedule.getLessons().get(lesson.getDate()).get(lesson.getBegin()).get(lesson.getGroup()) != null)
                    {
                        lesson.setDbId(schedule.getLessons().get(lesson.getDate()).get(lesson.getBegin()).get(lesson.getGroup()).getDbId());
                    }
                    schedule.getLessons().get(lesson.getDate()).get(lesson.getBegin()).put(lesson.getGroup(), lesson);
                });
    }

    public List<org.kabuapp.kabuapp.data.model.entity.Lesson> mapScheduleToDb(Schedule schedule)
    {
        List<org.kabuapp.kabuapp.data.model.entity.Lesson> dbLessons = new ArrayList<>();
        if (schedule != null && schedule.getLessons() != null)
        {
            schedule.getLessons().keySet().forEach(date ->
            {
                schedule.getLessons().get(date).keySet().forEach(begin ->
                {
                    schedule.getLessons().get(date).get(begin).keySet().forEach(group ->
                    {
                        Lesson lesson = schedule.getLessons().get(date).get(begin).get(group);
                        if (lesson.getDbId() == null)
                        {
                            lesson.setDbId(UUID.randomUUID());
                        }
                        org.kabuapp.kabuapp.data.model.entity.Lesson dbLesson = new org.kabuapp.kabuapp.data.model.entity.Lesson(
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
                    });
                });
            });
        }
        return dbLessons;
    }

    public void mapDbLessonToSchedule(List<org.kabuapp.kabuapp.data.model.entity.Lesson> dbLessons, Schedule schedule)
    {
        if (schedule.getLessons() == null)
        {
            schedule.setLessons(new LinkedHashMap<>());
        }
        dbLessons.stream().map(dbLesson -> new Lesson(
                dbLesson.getBegin(),
                dbLesson.getEnd(),
                dbLesson.getDate(),
                dbLesson.getGroup(),
                dbLesson.getMaxGroup(),
                dbLesson.getName(),
                dbLesson.getTeacher(),
                dbLesson.getRoom(),
                dbLesson.getId()))
                .forEach(lesson ->
                {
                    if (!schedule.getLessons().containsKey(lesson.getDate()))
                    {
                        schedule.getLessons().put(lesson.getDate(), new LinkedHashMap<>());
                    }
                    if (!schedule.getLessons().get(lesson.getDate()).containsKey(lesson.getBegin()))
                    {
                        schedule.getLessons().get(lesson.getDate()).put(lesson.getBegin(), new LinkedHashMap<>());
                    }
                    schedule.getLessons().get(lesson.getDate()).get(lesson.getBegin()).put(lesson.getGroup(), lesson);
                });
    }
}
