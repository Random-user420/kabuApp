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
import org.kabuapp.kabuapp.interfaces.Callback;
import org.kabuapp.kabuapp.db.controller.AuthController;
import org.kabuapp.kabuapp.schedule.ScheduleActivity;
import org.kabuapp.kabuapp.settings.SettingsActivity;

import java.time.Duration;
import java.util.Comparator;


public class ExamActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, Callback
{
    private ActivityExamBinding binding;
    private ExamUiGenerator uiGenerator;
    private AuthController authController;
    private ExamController examController;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        authController = ((KabuApp) getApplication()).getAuthController();
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
            examController.getExams().getExams().values()
                    .stream().sorted(Comparator.comparing(MemExam::getBeginn))
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