package org.kabuapp.kabuapp.data.memory;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;

@AllArgsConstructor
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

    public synchronized short getBegin()
    {
        return begin;
    }

    public synchronized void setBegin(short begin)
    {
        this.begin = begin;
    }

    public synchronized short getEnd()
    {
        return end;
    }

    public synchronized void setEnd(short end)
    {
        this.end = end;
    }

    public synchronized LocalDate getDate()
    {
        return date;
    }

    public synchronized void setDate(LocalDate date)
    {
        this.date = date;
    }

    public synchronized short getGroup()
    {
        return group;
    }

    public synchronized void setGroup(short group)
    {
        this.group = group;
    }

    public synchronized short getMaxGroup()
    {
        return maxGroup;
    }

    public synchronized void setMaxGroup(short maxGroup)
    {
        this.maxGroup = maxGroup;
    }

    public synchronized String getName()
    {
        return name;
    }

    public synchronized void setName(String name)
    {
        this.name = name;
    }

    public synchronized String getTeacher()
    {
        return teacher;
    }

    public synchronized void setTeacher(String teacher)
    {
        this.teacher = teacher;
    }

    public synchronized String getRoom()
    {
        return room;
    }

    public synchronized void setRoom(String room)
    {
        this.room = room;
    }

    public synchronized UUID getDbId()
    {
        return dbId;
    }

    public synchronized void setDbId(UUID dbId)
    {
        this.dbId = dbId;
    }
}
