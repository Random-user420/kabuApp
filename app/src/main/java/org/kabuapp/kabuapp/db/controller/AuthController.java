package org.kabuapp.kabuapp.db.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;

import org.kabuapp.kabuapp.api.exceptions.BadRequestException;
import org.kabuapp.kabuapp.api.DigikabuApiService;
import org.kabuapp.kabuapp.interfaces.AuthCallback;
import org.kabuapp.kabuapp.data.memory.AuthStateholder;
import org.kabuapp.kabuapp.db.model.AppDatabase;
import org.kabuapp.kabuapp.db.model.entity.User;
import org.kabuapp.kabuapp.interfaces.Callback;

@AllArgsConstructor
public class AuthController implements AuthCallback
{
    private AuthStateholder stateholder;
    private AppDatabase db;
    private DigikabuApiService digikabuApiService;
    private ExecutorService executorService;

    public String renewToken()
    {
        auth(null, null);
        return stateholder.getToken();
    }

    public UUID getId()
    {
        return stateholder.getDbId();
    }

    public String getToken()
    {
        return stateholder.getToken();
    }

    public void removeUser(UUID id)
    {
        if (stateholder.getUsers().entrySet().stream().filter(entry ->
                entry.getValue().equals(id)).findAny().isEmpty())
        {
            return;
        }
        stateholder.getUsers().remove(stateholder.getUsers().entrySet().stream().filter(entry -> entry.getValue().equals(id)).findAny().get().getKey());
        stateholder.setUsername(null);
        stateholder.setPassword(null);
        stateholder.setToken(null);
    }

    public void resetState()
    {
        stateholder.setUsername(null);
        stateholder.setPassword(null);
        stateholder.setToken(null);
        stateholder.setDbId(UUID.randomUUID());
    }

    public boolean setCredentials(String username, String password, Callback callback, Object[] args)
    {
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty())
        {
            stateholder.setUsername(username);
            stateholder.setPassword(password);
            auth(callback, args);
            stateholder.getUsers().put(username, stateholder.getDbId());
            return true;
        }
        return false;
    }

    public void setCredentials(String username, String password, String token)
    {
        stateholder.setUsername(username);
        stateholder.setPassword(password);
        stateholder.setToken(token);
        executorService.execute(this::save);
    }

    public void auth(Callback callback, Object[] args)
    {
        try
        {
            String token = digikabuApiService.auth(stateholder.getUsername(), stateholder.getPassword());
            if (token == null)
            {
                Logger.getLogger("AuthController").log(Level.WARNING, "Token return is null");
                return;
            }
            stateholder.setToken(token);
            executorService.execute(this::save);
            if (callback != null)
            {
                callback.callback(args);
            }
        }
        catch (BadRequestException e)
        {
            if (args != null && args.length > 0)
            {
                args[0] = false;
                callback.callback((args));
            }
        }
    }

    private void save()
    {
        User existingUser = db.userDao().get(stateholder.getDbId());
        if (existingUser == null)
        {
            User user = new User(UUID.randomUUID(), stateholder.getUsername(), stateholder.getPassword(), stateholder.getToken(), true);
            stateholder.setDbId(user.getId());
            db.userDao().insert(user);
        }
        else
        {
            existingUser.setUsername(stateholder.getUsername());
            existingUser.setPassword(stateholder.getPassword());
            existingUser.setToken(stateholder.getToken());
            db.userDao().update(existingUser);
        }
    }

    public UUID getDbUser()
    {
        List<User> users = db.userDao().getAll();
        users.stream().filter(user -> Boolean.TRUE.equals(user.getStandard()) && !user.getUsername().isEmpty()).findAny().ifPresentOrElse(user ->
        {
            stateholder.setUsername(user.getUsername());
            stateholder.setPassword(user.getPassword());
            stateholder.setToken(user.getToken());
            stateholder.setDbId(user.getId());
        }, () -> users.stream().filter(user -> !user.getUsername().isEmpty()).findFirst().ifPresent(user ->
        {
            stateholder.setUsername(user.getUsername());
            stateholder.setPassword(user.getPassword());
            stateholder.setToken(user.getToken());
            stateholder.setDbId(user.getId());
        }));
        return stateholder.getDbId();
    }

    public void getDbUsers()
    {
        executorService.execute(() ->
        {
            List<User> users = db.userDao().getAll();
            Map<String, UUID> userMap = new LinkedHashMap<>();
            users.forEach(user -> userMap.put(user.getUsername(), user.getId()));
            stateholder.setUsers(userMap);
        });
    }

    public UUID getDbUserByNameAndLoad(String name)
    {
        UUID id = stateholder.getUsers().get(name);
        List<User> users = db.userDao().getAll();

        users.stream().filter(user -> Boolean.TRUE.equals(user.getStandard())).findAny().ifPresent(user ->
        {
            user.setStandard(false);
            db.userDao().update(user);
        });
        users.stream().filter(user -> user.getId().equals(id)).findAny().ifPresent(user ->
        {
            user.setStandard(true);
            db.userDao().update(user);
            stateholder.setUsername(user.getUsername());
            stateholder.setPassword(user.getPassword());
            stateholder.setToken(user.getToken());
            stateholder.setDbId(user.getId());
        });
        return stateholder.getDbId();
    }

    public boolean isInitialized()
    {
        return  stateholder.getUsername() != null &&
                !stateholder.getUsername().isEmpty() &&
                !stateholder.getPassword().isEmpty() &&
                !stateholder.getToken().isEmpty();
    }

    public List<String> getUsers()
    {
        return new ArrayList<>(stateholder.getUsers().keySet());
    }

    public String getUser()
    {
        return stateholder.getUsername();
    }
}
