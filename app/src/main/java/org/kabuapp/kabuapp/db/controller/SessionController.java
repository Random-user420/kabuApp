package org.kabuapp.kabuapp.db.controller;

import org.kabuapp.kabuapp.db.model.AppDatabase;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SessionController
{
    private AppDatabase db;
    private ExamController examController;
    private LifetimeController lifetimeController;
    private AuthController authController;
    private ScheduleController scheduleController;
    private ExecutorService executorService;

    public void loadSession()
    {
        executorService.execute(() ->
        {
            UUID userId = authController.getDbUser();
            examController.getDbExams(userId);
            lifetimeController.getDbLifetime(userId);
            scheduleController.getDbSchedule(userId);
        });
    }

    public void removeUser(UUID userId)
    {
        db.userDao().delete(userId);
        scheduleController.resetSchedule(authController.getId());
        examController.resetExams(authController.getId());
        lifetimeController.resetLifetimes(authController.getId());
    }

    public List<Map<UUID, String>> getUsers()
    {
        return db.userDao().getAll().stream().map(user -> Map.of(user.getId(), user.getUsername())).collect(Collectors.toList());
    }
}
