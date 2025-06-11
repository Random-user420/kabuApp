package org.kabuapp.kabuapp.exam;

import org.kabuapp.kabuapp.api.DigikabuApiService;
import org.kabuapp.kabuapp.api.exceptions.UnauthorisedException;
import org.kabuapp.kabuapp.api.models.ExamResponse;
import org.kabuapp.kabuapp.data.memory.MemExams;
import org.kabuapp.kabuapp.db.ExamMapper;
import org.kabuapp.kabuapp.db.model.AppDatabase;
import org.kabuapp.kabuapp.db.model.entity.Lifetime;
import org.kabuapp.kabuapp.interfaces.AuthCallback;
import org.kabuapp.kabuapp.interfaces.Callback;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ExamController
{
    @Getter
    private MemExams exams;
    private ExamMapper examMapper;
    private DigikabuApiService apiService;
    private ExecutorService executorService;
    private AppDatabase db;

    public void updateExams(String token, AuthCallback re, Callback ce, Object[] objects, LocalDateTime time)
    {
        executorService.execute(() ->
        {
            List<Lifetime> lifetimes = db.lifetimeDao().getAll();
            if (lifetimes.isEmpty())
            {
                executorService.execute(() -> db.lifetimeDao().insert(new Lifetime(0, null, LocalDateTime.now())));
                updateExams(token, re, ce, objects);
            }
            else if (lifetimes.get(0).getExamLastUpdate() == null || lifetimes.get(0).getExamLastUpdate().isBefore(time))
            {
                updateExams(token, re, ce, objects);
                executorService.execute(() ->
                {
                    Lifetime lifetime = lifetimes.get(0);
                    lifetime.setExamLastUpdate(LocalDateTime.now());
                    db.lifetimeDao().update(lifetime);
                });
            }
        });
    }

    private void updateExams(String token, AuthCallback re, Callback ce, Object[] objects)
    {
        updateExams(token, re);
        if (ce != null)
        {
            ce.callback(objects);
        }
    }

    private void updateExams(String tokenIn, AuthCallback re)
    {
        executorService.execute(() -> db.examDao().deleteAll());
        String token = tokenIn;
        LocalDate date = LocalDate.now();
        try
        {
            if (date.getMonthValue() != 8)
            {
                updateExams(date.getMonthValue(), token);
            }
            if (date.getMonthValue() != 7)
            {
                updateExams(date.getMonthValue() + 1, token);
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
                updateExams(date.getMonthValue(), token);
            }
            if (date.getMonthValue() != 7)
            {
                updateExams(date.getMonthValue() + 1, token);
            }
        }
        catch (UnauthorisedException ignored)
        {
        }
    }

    public void getDbExams()
    {
        executorService.execute(() -> examMapper.mapDbToExams(db.examDao().getAll(), exams));
    }

    private void updateExams(int month, String token) throws UnauthorisedException
    {
        List<ExamResponse> responses = apiService.getExams(token, month);
        if (responses == null)
        {
            return;
        }
        examMapper.mapApiToExams(responses, exams);
        executorService.execute(() -> db.examDao().insertAll(examMapper.mapExamsToDb(exams)));
    }

    public void resetExams()
    {
        exams.reset();
        executorService.execute(() -> db.lessonDao().deleteAll());
    }
}
