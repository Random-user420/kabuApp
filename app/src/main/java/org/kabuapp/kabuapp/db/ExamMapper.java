package org.kabuapp.kabuapp.db;

import org.kabuapp.kabuapp.api.models.ExamResponse;
import org.kabuapp.kabuapp.data.memory.MemExam;
import org.kabuapp.kabuapp.data.memory.MemExams;

import java.time.LocalDate;
import java.util.List;

public class ExamMapper
{
    public void mapApiToExams(List<ExamResponse> responses, MemExams exams)
    {
        if (responses == null || responses.isEmpty() || exams == null)
        {
            return;
        }
        responses.forEach(response ->
        {
            MemExam exam = new MemExam(
                    LocalDate.of(
                        Integer.parseInt(response.getDate().substring(6)),
                        Integer.parseInt(response.getDate().substring(3, 5)),
                        Integer.parseInt(response.getDate().substring(0, 2))),
                    (short) 0,
                    response.getInfo());
        });
    }
}
