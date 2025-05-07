package org.kabuapp.kabuapp.data.memory;

import java.io.Serializable;
import org.jetbrains.annotations.NotNull;


public class AuthStateholder implements Serializable
{
    @NotNull
    private String username = "";
    @NotNull
    private String password = "";
    @NotNull
    private String token = "";

    public synchronized @NotNull String getUsername()
    {
        return username;
    }

    public synchronized void setUsername(@NotNull String username)
    {
        this.username = username;
    }

    public synchronized @NotNull String getPassword()
    {
        return password;
    }

    public synchronized void setPassword(@NotNull String password)
    {
        this.password = password;
    }

    public synchronized @NotNull String getToken()
    {
        return token;
    }

    public synchronized void setToken(@NotNull String token)
    {
        this.token = token;
    }
}
