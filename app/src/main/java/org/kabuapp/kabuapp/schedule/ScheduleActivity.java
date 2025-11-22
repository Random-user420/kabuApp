package org.kabuapp.kabuapp.schedule;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.kabuapp.kabuapp.KabuApp;
import org.kabuapp.kabuapp.R;
import org.kabuapp.kabuapp.data.memory.MemLesson;
import org.kabuapp.kabuapp.databinding.ActivityScheduleBinding;
import org.kabuapp.kabuapp.db.controller.ScheduleController;
import org.kabuapp.kabuapp.exam.ExamActivity;
import org.kabuapp.kabuapp.db.controller.ExamController;
import org.kabuapp.kabuapp.db.controller.AuthController;
import org.kabuapp.kabuapp.login.LoginActivity;
import org.kabuapp.kabuapp.interfaces.Callback;
import org.kabuapp.kabuapp.settings.SettingsActivity;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class ScheduleActivity extends AppCompatActivity implements Callback, DateAdapter.OnDateSelectedListener, SwipeRefreshLayout.OnRefreshListener
{
    private static final int SWIPE_VELOCITY_THRESHOLD_DP = 69;
    private static final int SWIPE_THRESHOLD_DP = 69;
    private ScheduleUiGenerator scheduleUiGenerator;
    private ScheduleController scheduleController;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DateTimeFormatter weekdayFormatter;
    private LinearLayoutManager layoutManager;
    private DateTimeFormatter monthFormatter;
    private GestureDetector gestureDetector;
    private ExecutorService executorService;
    private ActivityScheduleBinding binding;
    private DateTimeFormatter dayFormatter;
    private AuthController authController;
    private List<DateItem> dateItems;
    private DateAdapter dateAdapter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ((KabuApp) getApplication()).getScheduleUpdateTask().setRef(this);
        EdgeToEdge.enable(this);

        authController = ((KabuApp) getApplication()).getAuthController();
        scheduleController = ((KabuApp) getApplication()).getScheduleController();
        scheduleController.updateSchedule(authController.getToken(), authController, this, new Object[1], Duration.ofHours(2), authController.getId(), !scheduleController.getSchedule().getLessons().isEmpty());
        executorService = ((KabuApp) getApplication()).getExecutorService();
        scheduleUiGenerator = new ScheduleUiGenerator();
        ExamController examController = ((KabuApp) getApplication()).getExamController();

        monthFormatter = DateTimeFormatter.ofPattern("MMM", Locale.getDefault());
        dayFormatter = DateTimeFormatter.ofPattern("dd");
        weekdayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.getDefault());

        binding = ActivityScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_schedule);
        ScrollView scheduleScrollView = findViewById(R.id.schedule_scroll_view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        gestureDetector = new GestureDetector(this, new ScheduleGestureListener());

        if (scheduleScrollView != null)
        {
            scheduleScrollView.setOnTouchListener((v, event) ->
            {
                if (gestureDetector.onTouchEvent(event))
                {
                    return true;
                }
                return v.onTouchEvent(event);
            });
        }

        settingsHandler();
        examHandler();

        examController.updateExams(authController.getToken(), authController, null, null, Duration.ofHours(1), authController.getId());

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_schedule);
        swipeRefreshLayout.setOnRefreshListener(this);

        RecyclerView dateRecyclerView = findViewById(R.id.recycler_view_date_selector);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        dateRecyclerView.setLayoutManager(layoutManager);

        dateItems = generateDateItems(LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1), 14, scheduleController);

        dateAdapter = new DateAdapter(this, dateItems, this);
        dateRecyclerView.setAdapter(dateAdapter);
        if (scheduleController.getSchedule().getSelectedDate().isEqual(LocalDate.now()))
        {
            scheduleController.getSchedule().setSelectedDate(getDate(dateItems));
        }

        dateAdapter.setSelectedDate(scheduleController.getSchedule().getSelectedDate());

        dateRecyclerView.post(() ->
        {
            int selectedPosition = dateAdapter.getSelectedItemPosition();
            if (selectedPosition != RecyclerView.NO_POSITION)
            {
                layoutManager.scrollToPositionWithOffset(selectedPosition, 0);
            }
        });

        checkIfNoSchool();
        updateScheduleLoop();
    }

    @Override
    public void onRefresh()
    {
        scheduleController.updateSchedule(authController.getToken(), authController, this, new Object[1], Duration.ofMinutes(5), authController.getId(), true);
        swipeRefreshLayout.setRefreshing(false);
    }

    public void callback(Object[] objects)
    {
        updateSchedule();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if (!authController.isInitialized())
        {
            var i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

        getDelegate().onStart();
    }

    private void updateSchedule() {
        ViewGroup linearSchedule = findViewById(R.id.linear_schedule);
        runOnUiThread(linearSchedule::removeAllViews);
        if (scheduleController.getSchedule().getLessons() == null) {
            return;
        }
        Map<LocalDate, List<MemLesson>> lessons = new HashMap<>(scheduleController.getSchedule().getLessons());
        if (lessons.containsKey(LocalDate.now()) && lessons.get(LocalDate.now()) != null
                && lessons.get(LocalDate.now()).stream().noneMatch(this::isInLesson)) {
            addNullLessonAtCurrentTime(lessons.get(LocalDate.now()));
        }
        runOnUiThread(() -> {
            linearSchedule.removeAllViews();
            if (lessons.containsKey(scheduleController.getSchedule().getSelectedDate())) {
                lessons.get(scheduleController.getSchedule().getSelectedDate()).forEach(lesson ->
                        scheduleUiGenerator.addLessonElement(this, linearSchedule, lesson));
            }
            lessons.keySet().forEach(date -> {
                if (dateAdapter.getDateList().stream().noneMatch(dateItem -> dateItem.getDate().isEqual(date))) {
                    dateAdapter.addDate(generateDateItems(date, 1, scheduleController).get(0));
                }
            });
        });
    }

    private void addNullLessonAtCurrentTime(List<MemLesson> lessons) {
        int i = -1;
        for (int k = 0; k < lessons.size(); k++) {
            if (scheduleUiGenerator.toLocaleDate(lessons.get(k).getEnd()).isBefore(LocalTime.now())) {
                i = k;
            } else {
                break;
            }
        }
        if (i != -1 && i != lessons.size() - 1) {
            lessons.add(i + 1, new MemLesson((short) -1, (short) -1, null, (short) -1, (short) -1, null, null, null, null));
        }
    }

    private boolean isInLesson(MemLesson lesson) {
        return scheduleUiGenerator.toLocaleDate(lesson.getBegin()) == null
                || (scheduleUiGenerator.toLocaleDate(lesson.getBegin()).isBefore(LocalTime.now())
                && scheduleUiGenerator.toLocaleDate((short) (lesson.getEnd() + 1)).isAfter(LocalTime.now()));
    }

    private void updateScheduleLoop()
    {
        executorService.execute(() ->
        {
            while (!this.isDestroyed())
            {
                updateSchedule();
                try
                {
                    Thread.sleep(8000);
                }
                catch (InterruptedException ignored)
                {
                }
            }
        });
    }

    private void settingsHandler()
    {
        binding.barSettings.setOnClickListener(v ->
        {
            var i = new Intent(this, SettingsActivity.class);
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

    private List<DateItem> generateDateItems(LocalDate startDate, int numberOfDays, ScheduleController scheduleController)
    {
        List<DateItem> items = new ArrayList<>();

        for (int i = 0; i < numberOfDays; i++)
        {
            LocalDate currentDate = startDate.plusDays(i);
            if (!scheduleController.isSchool(currentDate))
            {
                continue;
            }
            String month = currentDate.format(monthFormatter);
            String day = currentDate.format(dayFormatter);
            String weekday = currentDate.format(weekdayFormatter);
            items.add(new DateItem(currentDate, month, day, weekday, false));
        }
        if (items.isEmpty())
        {
            LocalDate currentDate = LocalDate.now();
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

    private void changeSelectedDate(LocalDate newDate)
    {
        int newPosition = IntStream.range(0, dateItems.size()).filter(i ->
                dateItems.get(i).getDate().equals(newDate)).findFirst().orElse(-1);

        if (newPosition != -1)
        {
            scheduleController.getSchedule().setSelectedDate(newDate);
            dateAdapter.setSelectedDate(newDate);
            updateSchedule();

        }
    }

    private LocalDate getDate(List<DateItem> dateItems)
    {
        LocalDate date = LocalDate.now();
        if (date.getDayOfWeek().equals(DayOfWeek.SUNDAY) && !scheduleController.isSchool(date))
        {
            date = date.plusDays(1);
        }
        LocalDate finalDate = date;
        if (dateItems.stream().noneMatch(item -> item.getDate().equals(finalDate)) && !dateItems.isEmpty())
        {
            for (LocalDate dateItem : dateItems.stream().map(DateItem::getDate).collect(Collectors.toList()))
            {
                if (dateItem.isAfter(date))
                {
                    return dateItem;
                }
            }
            return dateItems.get(dateItems.size() - 1).getDate();
        }
        return date;
    }

    private class ScheduleGestureListener extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            boolean result = false;
            try
            {
                if (e1 == null || e2 == null)
                {
                    return false;
                }

                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();

                float density = getResources().getDisplayMetrics().density;
                int swipeThresholdPx = (int) (SWIPE_THRESHOLD_DP * density);
                int swipeVelocityThresholdPx = (int) (SWIPE_VELOCITY_THRESHOLD_DP * density);

                if (Math.abs(diffX) > Math.abs(diffY)
                        && Math.abs(diffX) > swipeThresholdPx
                        && Math.abs(velocityX) > swipeVelocityThresholdPx)
                {
                    if (diffX > 0)
                    {
                        onSwipeRight();
                    }
                    else
                    {
                        onSwipeLeft();
                    }
                    result = true;
                }
            }
            catch (Exception exception)
            {
                Logger.getLogger("GestureListener").log(Level.SEVERE, "Error in onFling", exception);
            }
            return result;
        }
    }

    private void onSwipeRight()
    {
        int days = 1;
        for (;days < 15; days++)
        {
            if (scheduleController.isSchool(scheduleController.getSchedule().getSelectedDate().minusDays(days)))
            {
                break;
            }
        }
        changeSelectedDate(scheduleController.getSchedule().getSelectedDate().minusDays(days));
    }

    private void onSwipeLeft()
    {
        int days = 1;
        for (;days < 15; days++)
        {
            if (scheduleController.isSchool(scheduleController.getSchedule().getSelectedDate().plusDays(days)))
            {
                break;
            }
        }
        changeSelectedDate(scheduleController.getSchedule().getSelectedDate().plusDays(days));
    }

    private void checkIfNoSchool()
    {
        boolean isSchool = scheduleController.getSchedule().getLessons().values().stream().mapToLong(Collection::size).sum() > 0;
        if (!isSchool)
        {
            findViewById(R.id.noSchoolHint).setVisibility(VISIBLE);
        }
        else
        {
            findViewById(R.id.noSchoolHint).setVisibility(GONE);
        }
    }
}