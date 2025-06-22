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
import java.util.UUID;
import java.util.concurrent.ExecutorService;

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

    private void updateExams(String tokenIn, AuthCallback re, UUID userId)
    {
        executorService.execute(() -> db.examDao().deleteAll());
        String token = tokenIn;
        LocalDate date = LocalDate.now();
        try
        {
            if (date.getMonthValue() != 8)
            {
                updateExams(date.getMonthValue(), token, userId);
            }
            if (date.getMonthValue() != 7)
            {
                updateExams(date.getMonthValue() + 1, token, userId);
            }
        }
        catch (UnauthorisedException ignored)
        {
            token = re.renewToken();
        }
        try
        {
            if (date.getMonthValue() != 8)
            {
                updateExams(date.getMonthValue(), token, userId);
            }
            if (date.getMonthValue() != 7)
            {
                updateExams(date.getMonthValue() + 1, token, userId);
            }
        }
        catch (UnauthorisedException ignored)
        {
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
        exams.reset();
    }
}
