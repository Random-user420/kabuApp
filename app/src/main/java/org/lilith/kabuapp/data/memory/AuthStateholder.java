package org.lilith.kabuapp.data.memory;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class AuthStateholder implements Serializable
{
    @NotNull
    private String username = "";
    @NotNull
    private String password = "";
    @NotNull
    private String token = "";
}
