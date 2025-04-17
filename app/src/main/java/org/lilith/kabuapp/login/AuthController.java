package org.lilith.kabuapp.login;



import org.lilith.kabuapp.api.BadRequestException;
import org.lilith.kabuapp.api.DigikabuApiService;
import org.lilith.kabuapp.data.memory.AuthStateholder;
import org.lilith.kabuapp.data.model.AppDatabase;
import org.lilith.kabuapp.data.model.entity.User;
import org.lilith.kabuapp.models.Callback;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class AuthController {
    @Getter
    private AuthStateholder stateholder;
    private AppDatabase db;
    private DigikabuApiService digikabuApiService;
    private final boolean fakeService;
    public boolean setCredentials(String username, String password)
    {
        return setCredentials(username, password, (Callback) null, (Object[]) null);
    }

    public boolean setCredentials(String username, String password, Callback callback, Object[] args)
    {
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty())
        {
            stateholder.setUsername(username);
            stateholder.setPassword(password);
            auth(callback, args);
            return true;
        }
        return false;
    }

    public void setCredentials(String username, String password, String token)
    {
        stateholder.setUsername(username);
        stateholder.setPassword(password);
        stateholder.setToken(token);
        new Thread(this::save).start();
    }

    public void auth(Callback callback, Object[] args)
    {
        if (!fakeService)
        try
        {
            String token = digikabuApiService.auth(stateholder.getUsername(), stateholder.getPassword());
            if (token == null)
            {
                Logger.getLogger("AuthController").log(Level.WARNING, "Something else ig");
                return;
            }
            stateholder.setToken(token);
            new Thread(this::save).start();
            if (callback != null)
            {
                callback.callback(args);
            }
        }
        catch (BadRequestException e)
        {
            Logger.getLogger("AuthController").log(Level.WARNING, e.toString());
        }
        else {
            stateholder.setToken("FAKE");
            new Thread(this::save).start();
            if (callback != null)
            {
                callback.callback(args);
            }
        }
    }

    private void save()
    {
        List<User> existingUsers = db.userDao().getAll();
        if (existingUsers.isEmpty())
        {
            User user = new User(UUID.randomUUID(), stateholder.getUsername(), stateholder.getPassword(), stateholder.getToken());
            db.userDao().insert(user);
        }
        else
        {
            User existingUser = existingUsers.get(0);
            existingUser.setUsername(stateholder.getUsername());
            existingUser.setPassword(stateholder.getPassword());
            existingUser.setToken(stateholder.getToken());
            db.userDao().update(existingUser);
        }
    }

    public void getInitialUser()
    {
        new Thread(this::getAsyncInitialUser).start();
    }

    private void getAsyncInitialUser()
    {
        if (db.userDao().getAll().isEmpty())
        {
            return;
        }
        User user = db.userDao().getAll().get(0);
        if (user != null)
        {
            stateholder.setUsername(user.getUsername());
            stateholder.setPassword(user.getPassword());
            stateholder.setToken(user.getToken());
        }
    }

    public boolean isInitialized()
    {
        return !stateholder.getUsername().isEmpty() &&
                !stateholder.getPassword().isEmpty() &&
                !stateholder.getToken().isEmpty();
    }
}
