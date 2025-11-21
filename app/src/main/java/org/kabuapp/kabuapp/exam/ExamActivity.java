package org.kabuapp.kabuapp.exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.kabuapp.kabuapp.KabuApp;
import org.kabuapp.kabuapp.R;
import org.kabuapp.kabuapp.data.memory.MemExam;
import org.kabuapp.kabuapp.databinding.ActivityExamBinding;
import org.kabuapp.kabuapp.db.controller.ExamController;
import org.kabuapp.kabuapp.db.controller.SettingsController;
import org.kabuapp.kabuapp.interfaces.Callback;
import org.kabuapp.kabuapp.db.controller.AuthController;
import org.kabuapp.kabuapp.schedule.ScheduleActivity;
import org.kabuapp.kabuapp.settings.SettingsActivity;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ExamActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, Callback
{
    private SwipeRefreshLayout swipeRefreshLayout;
    private AuthController authController;
    private ExamController examController;
    private ExamUiGenerator uiGenerator;
    private ActivityExamBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        authController = ((KabuApp) getApplication()).getAuthController();
        examController = ((KabuApp) getApplication()).getExamController();
        SettingsController settingsController = ((KabuApp) getApplication()).getSettingsController();
        DateTimeFormatter dateTimeFormatter;
        if (settingsController.isIsoDate())
        {
            dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        }
        else
        {
            dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
        }
        uiGenerator = new ExamUiGenerator(dateTimeFormatter);

        binding = ActivityExamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_exam);
        swipeRefreshLayout.setOnRefreshListener(this);

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
            Map<LocalDate, MemExam> exams = new HashMap<>(examController.getExams().getExams());
            exams.computeIfAbsent(LocalDate.now(), k -> new MemExam(null, LocalDate.now(), (short) -1, null));
            exams.values().stream().sorted(Comparator.comparing(MemExam::getBeginn))
                    .forEach(exam ->
                    uiGenerator.addExamElement(
                            this,
                            linearExams,
                            exam));
        }
    }

    @Override
    public void onRefresh()
    {
        swipeRefreshLayout.setRefreshing(false);
        examController.updateExams(authController.getToken(), authController, this, new Object[1], Duration.ofMinutes(5), authController.getId());
    }

    public void callback(Object[] objects)
    {
        runOnUiThread(this::updateExams);
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