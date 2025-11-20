package org.kabuapp.kabuapp.data.memory;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
public class AuthStateholder implements Serializable
{
    private String username = "";
    private String password = "";
    private String token = "";
    private Map<String, UUID> users;
    @Setter
    private UUID dbId;

    public synchronized void setUsername(String username)
    {
        this.username = username;
    }
    public synchronized void setPassword(String password)
    {
        this.password = password;
    }
    public synchronized void setToken(String token)
    {
        this.token = token;
    }
    public synchronized void setUsers(Map<String, UUID> users)
    {
        this.users = users;
    }
}
