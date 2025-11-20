package org.kabuapp.kabuapp.data.memory;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@AllArgsConstructor
public class MemExam
{
    @Setter
    private UUID dbId;
    private LocalDate beginn;
    private short duration;
    private String info;

    public synchronized void addDuration()
    {
        this.duration++;
    }
}
