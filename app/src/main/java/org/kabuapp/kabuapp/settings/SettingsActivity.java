package org.kabuapp.kabuapp.settings;

import static org.kabuapp.kabuapp.ui.NoticeGenerator.setNotice;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import org.kabuapp.kabuapp.KabuApp;
import org.kabuapp.kabuapp.R;
import org.kabuapp.kabuapp.databinding.SettingsActivityBinding;
import org.kabuapp.kabuapp.exam.ExamActivity;
import org.kabuapp.kabuapp.login.AuthController;
import org.kabuapp.kabuapp.login.LoginActivity;
import org.kabuapp.kabuapp.schedule.ScheduleActivity;
import org.kabuapp.kabuapp.schedule.ScheduleController;

public class SettingsActivity extends AppCompatActivity
{
    private SettingsActivityBinding binding;
    private AuthController authController;
    private ScheduleController scheduleController;

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
                binding.debugUsername.setText(authController.getStateholder().getUsername());
                binding.debugPassword.setText(authController.getStateholder().getPassword());
                binding.debugToken.setText(authController.getStateholder().getToken());
            }
            else
            {

                binding.debugUsername.setText("");
                binding.debugPassword.setText("");
                binding.debugToken.setText("");
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