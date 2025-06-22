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

    public synchronized String getUsername()
    {
        return username;
    }
    public synchronized void setUsername(String username)
    {
        this.username = username;
    }
    public synchronized String getPassword()
    {
        return password;
    }
    public synchronized void setPassword(String password)
    {
        this.password = password;
    }
    public synchronized String getToken()
    {
        return token;
    }
    public synchronized void setToken(String token)
    {
        this.token = token;
    }
    public synchronized Map<String, UUID> getUsers()
    {
        return users;
    }
    public synchronized void setUsers(Map<String, UUID> users)
    {
        this.users = users;
    }
}
