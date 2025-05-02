package org.lilith.kabuapp.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.lilith.kabuapp.KabuApp;
import org.lilith.kabuapp.R;
import org.lilith.kabuapp.data.model.Lesson;
import org.lilith.kabuapp.databinding.ActivityScheduleBinding;
import org.lilith.kabuapp.login.AuthController;
import org.lilith.kabuapp.login.Login;
import org.lilith.kabuapp.models.Callback;
import org.lilith.kabuapp.settings.Settings;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Schedule extends AppCompatActivity implements Callback, DateAdapter.OnDateSelectedListener, SwipeRefreshLayout.OnRefreshListener
{
    private ActivityScheduleBinding binding;
    private AuthController authController;
    private ScheduleController scheduleController;
    private ScheduleUiGenerator scheduleUiGenerator;
    private RecyclerView dateRecyclerView;
    private DateAdapter dateAdapter;
    private List<DateItem> dateItems;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authController = ((KabuApp) getApplication()).getAuthController();
        scheduleController = ((KabuApp) getApplication()).getScheduleController();
        scheduleUiGenerator = new ScheduleUiGenerator();

        scheduleController.updateScheduleIfOld(
                authController.getStateholder().getToken(), authController, this, new Object[1]);

        settingsHandler();

        setContentView(R.layout.activity_schedule);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_schedule);
        swipeRefreshLayout.setOnRefreshListener(this);

        dateRecyclerView = findViewById(R.id.recycler_view_date_selector);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        dateRecyclerView.setLayoutManager(layoutManager);

        dateItems = generateDateItems(LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1), 12);

        dateAdapter = new DateAdapter(this, dateItems, this);
        dateRecyclerView.setAdapter(dateAdapter);

        LocalDate initialSelectedDate = scheduleController.getSchedule().getSelectedDate();
        dateAdapter.setSelectedDate(initialSelectedDate);

        dateRecyclerView.post(() ->
        {
            int selectedPosition = dateAdapter.getSelectedItemPosition();
            if (selectedPosition != RecyclerView.NO_POSITION)
            {
                layoutManager.scrollToPositionWithOffset(selectedPosition, 0);
            }
        });

        updateSchedule();
    }

    @Override
    public void onRefresh()
    {
        scheduleController.updateSchedule(
                authController.getStateholder().getToken(), authController, this, new Object[1]);
        swipeRefreshLayout.setRefreshing(false);
    }

    public void callback(Object[] objects)
    {
        runOnUiThread(this::updateSchedule);
    }

    public void updateSchedule()
    {
        ViewGroup linearSchedule = findViewById(R.id.linear_schedule);
        linearSchedule.removeAllViews();
        if (scheduleController.getSchedule().getLessons().containsKey(scheduleController.getSchedule().getSelectedDate()))
        {
            for (Map<Short, Lesson> lessons : scheduleController.getSchedule().getLessons().get(scheduleController.getSchedule().getSelectedDate()).values())
            {
                Lesson lesson = lessons.get((short) 1);
                if (lessons.get((short) 1).getMaxGroup() == 1)
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
                else
                {
                    Lesson lesson2 = lessons.get((short) 2);
                    scheduleUiGenerator.addDoubleLessonElement(
                            this,
                            linearSchedule,
                            scheduleUiGenerator.mapBeginnToString(lesson.getBegin()),
                            scheduleUiGenerator.mapBeginnToString((short) (lesson.getEnd() + 1)),
                            lesson.getRoom(),
                            lesson2.getRoom(),
                            lesson.getTeacher(),
                            lesson2.getTeacher(),
                            lesson.getName(),
                            lesson2.getName());
                }
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

    private List<DateItem> generateDateItems(LocalDate startDate, int numberOfDays)
    {
        List<DateItem> items = new ArrayList<>();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM", Locale.getDefault());
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd");
        DateTimeFormatter weekdayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.getDefault());


        for (int i = 0; i < numberOfDays; i++)
        {
            LocalDate currentDate = startDate.plusDays(i);
            String month = currentDate.format(monthFormatter);
            String day = currentDate.format(dayFormatter);
            String weekday = currentDate.format(weekdayFormatter);
            items.add(new DateItem(currentDate, month, day, weekday, false));
        }
        return items;
    }

    @Override
    public void onDateSelected(LocalDate date)
    {
        scheduleController.getSchedule().setSelectedDate(date);
        updateSchedule();
    }
}