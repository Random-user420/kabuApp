package org.kabuapp.kabuapp.data.memory;


import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MemLifetime
{
    private long dbId;
    private LocalDateTime scheduleLastUpdate;
    private LocalDateTime examLastUpdate;
}
