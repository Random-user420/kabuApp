package org.kabuapp.kabuapp.exam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.kabuapp.kabuapp.KabuApp;
import org.kabuapp.kabuapp.R;
import org.kabuapp.kabuapp.databinding.ActivityExamBinding;
import org.kabuapp.kabuapp.login.AuthController;
import org.kabuapp.kabuapp.schedule.ScheduleActivity;
import org.kabuapp.kabuapp.settings.SettingsActivity;

import java.util.concurrent.ExecutorService;


public class ExamActivity extends AppCompatActivity
{
    private ActivityExamBinding binding;
    private AuthController authController;
    private ExecutorService executorService;
    private ScrollView examScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        authController = ((KabuApp) getApplication()).getAuthController();
        executorService = ((KabuApp) getApplication()).getExecutorService();

        binding = ActivityExamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        examScrollView = findViewById(R.id.exam_scroll_view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        settingsHandler();
        setScheduleListener();
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