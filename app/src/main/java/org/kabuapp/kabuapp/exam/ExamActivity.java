package org.kabuapp.kabuapp.exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.kabuapp.kabuapp.KabuApp;
import org.kabuapp.kabuapp.R;
import org.kabuapp.kabuapp.databinding.ActivityExamBinding;
import org.kabuapp.kabuapp.schedule.ScheduleActivity;
import org.kabuapp.kabuapp.settings.SettingsActivity;



public class ExamActivity extends AppCompatActivity
{
    private ActivityExamBinding binding;
    private ExamUiGenerator uiGenerator;
    private ExamController examController;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        examController = ((KabuApp) getApplication()).getExamController();
        uiGenerator = new ExamUiGenerator();

        binding = ActivityExamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        settingsHandler();
        setScheduleListener();
        updateExams();
    }

    private void updateExams()
    {
        ViewGroup linearExams = findViewById(R.id.linear_exams);
        linearExams.removeAllViews();
        if (examController.getExams().getExams() != null && !examController.getExams().getExams().isEmpty())
        {
            examController.getExams().getExams().values().forEach(exam ->
            {
                uiGenerator.addExamElement(
                        this,
                        linearExams,
                        exam);
            });
        }
    }

    private void settingsHandler()
    {
        binding.barSettings.setOnClickListener(v ->
        {
            var i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        });
    }

    private void setScheduleListener()
    {
        binding.barSchedule.setOnClickListener((v) ->
        {
            var i = new Intent(this, ScheduleActivity.class);
            startActivity(i);
        });
    }
}