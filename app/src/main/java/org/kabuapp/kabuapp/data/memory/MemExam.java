package org.kabuapp.kabuapp.data.memory;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class MemExam
{
    @Getter
    @Setter
    private UUID dbId;
    private LocalDate beginn;
    private short duration;
    private String info;

    public synchronized LocalDate getBeginn()
    {
        return beginn;
    }

    public synchronized void setBeginn(LocalDate beginn)
    {
        this.beginn = beginn;
    }

    public synchronized short getDuration()
    {
        return duration;
    }

    public synchronized void setDuration(short duration)
    {
        this.duration = duration;
    }

    public synchronized void addDuration()
    {
        this.duration++;
    }

    public synchronized String getInfo()
    {
        return info;
    }

    public synchronized void setInfo(String info)
    {
        this.info = info;
    }
}
