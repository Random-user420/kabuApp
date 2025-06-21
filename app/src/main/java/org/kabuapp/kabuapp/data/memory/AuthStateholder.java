package org.kabuapp.kabuapp.data.memory;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.Setter;

public class AuthStateholder implements Serializable
{
    private String username = "";
    private String password = "";
    private String token = "";
    private Map<String, UUID> users;
    @Getter @Setter
    private UUID dbId;

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
    public synchronized @NotNull Map<String, UUID> getUsers()
    {
        return users;
    }
    public synchronized void setUsers(Map<String, UUID> users)
    {
        this.users = users;
    }
}
