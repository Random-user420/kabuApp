package org.kabuapp.kabuapp.db.controller;

import org.kabuapp.kabuapp.api.DigikabuApiService;
import org.kabuapp.kabuapp.api.exceptions.UnauthorisedException;
import org.kabuapp.kabuapp.api.models.ExamResponse;
import org.kabuapp.kabuapp.data.memory.MemExams;
import org.kabuapp.kabuapp.db.ExamMapper;
import org.kabuapp.kabuapp.db.model.AppDatabase;
import org.kabuapp.kabuapp.db.model.DbType;
import org.kabuapp.kabuapp.interfaces.AuthCallback;
import org.kabuapp.kabuapp.interfaces.Callback;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ExamController
{
    @Getter
    private MemExams exams;
    private ExamMapper examMapper;
    private LifetimeController lifetimeController;
    private DigikabuApiService apiService;
    private ExecutorService executorService;
    private AppDatabase db;

    public void updateExams(String token, AuthCallback re, Callback ce, Object[] objects, Duration duration, UUID userId)
    {
        executorService.execute(() ->
        {
            if (lifetimeController.isLifetimeExpired(duration, DbType.EXAM))
            {
                updateExams(token, re, userId);
                if (ce != null)
                {
                    ce.callback(objects);
                }
                lifetimeController.updateLifetime(DbType.EXAM);
                lifetimeController.saveLifetimeToDb(userId);
            }
        });
    }

    private void updateExams(String token, AuthCallback re, UUID userId)
    {
        executorService.execute(() -> db.examDao().deletePerUser(userId));
        LocalDate date = LocalDate.now();
        try
        {
            updateExams(date, token, userId, (short) 3);
        }
        catch (UnauthorisedException ignored)
        {
            token = re.renewToken();
        }
        try
        {
            updateExams(date, token, userId, (short) 3);
        }
        catch (UnauthorisedException ignored)
        {
        }
    }

    private void updateExams(LocalDate date, String token, UUID userId, short months) throws UnauthorisedException
    {
        Set<LocalDate> datesToRemove = exams.getExams().keySet().stream()
                .filter(key -> key.isBefore(date.withDayOfMonth(1)))
                .collect(Collectors.toSet());
        datesToRemove.forEach(exams.getExams()::remove);
        for (int i = 0; i < months; i++)
        {
            if (date.plusMonths(i).getMonthValue() != 8)
            {
                updateExams(date.plusMonths(i).getMonthValue(), token, userId);
            }
        }
    }

    public void getDbExams(UUID userId)
    {
        executorService.execute(() -> examMapper.mapDbToExams(db.examDao().get(userId), exams));
    }

    private void updateExams(int month, String token, UUID userId) throws UnauthorisedException
    {
        List<ExamResponse> responses = apiService.getExams(token, month);
        if (responses == null)
        {
            return;
        }
        examMapper.mapApiToExams(responses, exams);
        executorService.execute(() -> db.examDao().insertAll(examMapper.mapExamsToDb(exams, userId)));
    }

    public void resetExams(UUID userId)
    {
        resetState();
        executorService.execute(() -> db.examDao().deletePerUser(userId));
    }

    public void resetState()
    {
        exams.getExams().clear();
    }
}
