package org.lilith.kabuapp.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.lilith.kabuapp.KabuApp;
import org.lilith.kabuapp.R;
import org.lilith.kabuapp.data.model.Lesson;
import org.lilith.kabuapp.databinding.ActivityScheduleBinding;
import org.lilith.kabuapp.login.AuthController;
import org.lilith.kabuapp.login.Login;
import org.lilith.kabuapp.models.Callback;
import org.lilith.kabuapp.settings.Settings;

import java.time.LocalDate;


public class Schedule extends AppCompatActivity implements Callback
{
    private ActivityScheduleBinding binding;
    private AuthController authController;
    private ScheduleController scheduleController;
    private ScheduleUiGenerator scheduleUiGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authController = ((KabuApp) getApplication()).getAuthController();
        scheduleController = ((KabuApp) getApplication()).getScheduleController();
        scheduleUiGenerator = new ScheduleUiGenerator();

        scheduleController.updateSchedule(
                authController.getStateholder().getToken(), authController, this, new Object[1]);

        settingsHandler();
    }

    public void callback(Object[] objects)
    {
        ViewGroup linearSchedule = binding.linearSchedule;
        Lesson cashed = null;
        for (Lesson lesson : scheduleController.getSchedule().getLessons().get(LocalDate.now().minusDays(2)).values())
        {
            if (lesson.getMaxGroup() == 1)
            {
                scheduleUiGenerator.addSingleLessonElement(
                        this,
                        linearSchedule,
                        scheduleUiGenerator.mapBeginnToString(lesson.getBegin()),
                        scheduleUiGenerator.mapBeginnToString((short) (lesson.getEnd() + 1)),
                        lesson.getRoom(),
                        lesson.getTeacher(),
                        lesson.getName());
            }
            else if (lesson.getMaxGroup() != lesson.getGroup())
            {
                cashed = lesson;
            }
            else
            {
                scheduleUiGenerator.addDoubleLessonElement(
                        this,
                        linearSchedule,
                        scheduleUiGenerator.mapBeginnToString(lesson.getBegin()),
                        scheduleUiGenerator.mapBeginnToString((short) (lesson.getEnd() + 1)),
                        cashed.getRoom(),
                        lesson.getRoom(),
                        cashed.getTeacher(),
                        lesson.getTeacher(),
                        cashed.getName(),
                        lesson.getName());
            }
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if (!authController.isInitialized())
        {
            var i = new Intent(this, Login.class);
            startActivity(i);
        }

        getDelegate().onStart();
    }

    private void settingsHandler()
    {
        binding.barSettings.setOnClickListener(v ->
        {
            var i = new Intent(this, Settings.class);
            startActivity(i);
        });
    }
}