package org.kabuapp.kabuapp.data.memory;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemLesson
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

    public synchronized void setEnd(short end)
    {
        this.end = end;
    }
    public synchronized void setDbId(UUID dbId)
    {
        this.dbId = dbId;
    }

    public boolean isFollowingLessonTo(MemLesson lesson)
    {
        return this.getName().equals(lesson.getName())
                && this.getRoom().equals(lesson.getRoom())
                && this.getTeacher().equals(lesson.getTeacher())
                && this.getMaxGroup() == lesson.getMaxGroup()
                && this.getGroup() == lesson.getGroup()
                && this.getBegin() == (lesson.getEnd() + 1);
    }
}
