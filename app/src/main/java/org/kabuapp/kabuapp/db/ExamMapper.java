package org.kabuapp.kabuapp.db;

import org.kabuapp.kabuapp.api.models.ExamResponse;
import org.kabuapp.kabuapp.data.memory.MemExam;
import org.kabuapp.kabuapp.data.memory.MemExams;
import org.kabuapp.kabuapp.db.model.entity.Exam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExamMapper
{
    public void mapApiToExams(List<ExamResponse> responses, MemExams exams)
    {
        if (responses == null || responses.isEmpty() || exams == null)
        {
            return;
        }
        String lastInfo = "";
        MemExam lastExam = null;
        for (ExamResponse response : responses)
        {
            if (response.getInfo().isEmpty())
            {
                continue;
            }
            if (lastInfo.equals(response.getInfo()))
            {
                lastExam.addDuration();
            }
            else
            {
                MemExam exam = new MemExam(
                        UUID.randomUUID(),
                        LocalDate.of(
                                Integer.parseInt(response.getDate().substring(6)),
                                Integer.parseInt(response.getDate().substring(3, 5)),
                                Integer.parseInt(response.getDate().substring(0, 2))),
                        (short) 0,
                        response.getInfo());
                lastExam = exam;
                lastInfo = response.getInfo();
                exams.getExams().put(exam.getBeginn(), exam);
            }
        }
    }

    public List<Exam> mapExamsToDb(MemExams exams)
    {
        List<Exam> dbExams = new ArrayList<>(exams.getExams().size());
        exams.getExams().values().forEach(exam ->
        {
            Exam dbExam = new Exam(
                    exam.getDbId(),
                    exam.getBeginn(),
                    exam.getDuration(),
                    exam.getInfo());
            dbExams.add(dbExam);
        });
        return dbExams;
    }

    public void mapDbToExams(List<Exam> dbExams, MemExams exams)
    {
        dbExams.forEach(dbExam ->
        {
            MemExam exam = new MemExam(
                    dbExam.getId(),
                    dbExam.getDate(),
                    dbExam.getDuration(),
                    dbExam.getInfo());
            exams.getExams().put(exam.getBeginn(), exam);
        });
    }
}
