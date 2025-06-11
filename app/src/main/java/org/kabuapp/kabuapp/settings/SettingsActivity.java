package org.kabuapp.kabuapp.settings;

import static org.kabuapp.kabuapp.ui.NoticeGenerator.setNotice;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import org.kabuapp.kabuapp.KabuApp;
import org.kabuapp.kabuapp.R;
import org.kabuapp.kabuapp.databinding.SettingsActivityBinding;
import org.kabuapp.kabuapp.db.model.AppDatabase;
import org.kabuapp.kabuapp.db.model.entity.Lifetime;
import org.kabuapp.kabuapp.exam.ExamActivity;
import org.kabuapp.kabuapp.exam.ExamController;
import org.kabuapp.kabuapp.login.AuthController;
import org.kabuapp.kabuapp.login.LoginActivity;
import org.kabuapp.kabuapp.schedule.ScheduleActivity;
import org.kabuapp.kabuapp.schedule.ScheduleController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class SettingsActivity extends AppCompatActivity
{
    private SettingsActivityBinding binding;
    private AuthController authController;
    private ScheduleController scheduleController;
    private ExamController examController;
    private LocalDateTime scheduleLifetime;
    private LocalDateTime examLifetime;
    private ExecutorService executorService;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        binding = SettingsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authController = ((KabuApp) getApplication()).getAuthController();
        scheduleController = ((KabuApp) getApplication()).getScheduleController();
        examController = ((KabuApp) getApplication()).getExamController();
        executorService = ((KabuApp) getApplication()).getExecutorService();
        db = ((KabuApp) getApplication()).getDb();

        getLifetimes();

        resetUserHandler();

        setScheduleListener();
        examHandler();
        debugSwitchListener();
        setNotice(this, findViewById(R.id.notice_code_settings));
    }

    private void resetUserHandler()
    {
        binding.logout.setOnClickListener(v ->
        {
            authController.setCredentials("", "", "");
            scheduleController.resetSchedule();
            examController.resetExams();
            var i = new Intent(this, LoginActivity.class);
            startActivity(i);
        });
    }

    private void debugSwitchListener()
    {
        binding.debugSwitch.setOnCheckedChangeListener((c, ac) ->
        {
            if (ac)
            {
                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
                if (examLifetime != null && scheduleLifetime != null)
                {
                    binding.debugScheduleLifetime.setText(Html.fromHtml(
                            getApplicationContext().getString(R.string.schedule_title) + "<br/>" + formatter.format(scheduleLifetime)));
                    binding.debugExamLifetime.setText(Html.fromHtml(
                            getApplicationContext().getString(R.string.exam_title) + "<br/>" + formatter.format(examLifetime)));
                }
                binding.debugToken.setText(authController.getStateholder().getToken());
            }
            else
            {
                binding.debugScheduleLifetime.setText("");
                binding.debugExamLifetime.setText("");
                binding.debugToken.setText("");
            }
        });
    }

    private void getLifetimes()
    {
        executorService.execute(() ->
        {
            List<Lifetime> lifetimes = db.lifetimeDao().getAll();
            if (!lifetimes.isEmpty())
            {
                scheduleLifetime = lifetimes.get(0).getScheduleLastUpdate();
                examLifetime = lifetimes.get(0).getExamLastUpdate();
            }
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

    private void examHandler()
    {
        binding.barExam.setOnClickListener(v ->
        {
            var i = new Intent(this, ExamActivity.class);
            startActivity(i);
        });
    }
}