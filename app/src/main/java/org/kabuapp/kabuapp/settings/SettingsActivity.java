package org.kabuapp.kabuapp.settings;

import static org.kabuapp.kabuapp.ui.NoticeGenerator.setNotice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.text.HtmlCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import org.kabuapp.kabuapp.KabuApp;
import org.kabuapp.kabuapp.R;
import org.kabuapp.kabuapp.databinding.ActivitySettingsBinding;
import org.kabuapp.kabuapp.db.controller.SessionController;
import org.kabuapp.kabuapp.exam.ExamActivity;
import org.kabuapp.kabuapp.db.controller.LifetimeController;
import org.kabuapp.kabuapp.db.controller.AuthController;
import org.kabuapp.kabuapp.login.LoginActivity;
import org.kabuapp.kabuapp.schedule.ScheduleActivity;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    private ActivitySettingsBinding binding;
    private AuthController authController;
    private LifetimeController lifetimeController;
    private SessionController sessionController;
    private ExecutorService executorService;
    private Spinner accountSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authController = ((KabuApp) getApplication()).getAuthController();
        lifetimeController = ((KabuApp) getApplication()).getLifetimeController();
        sessionController = ((KabuApp) getApplication()).getSessionController();
        executorService = ((KabuApp) getApplication()).getExecutorService();

        accountSpinner = findViewById(R.id.account_spinner);
        accountSpinner.setOnItemSelectedListener(this);
        refreshAccountsSpinner();

        findViewById(R.id.add_account_button).setOnClickListener(v -> onAddAccountClicked());
        findViewById(R.id.delete_account_button).setOnClickListener(v -> onDeleteAccountClicked());

        setScheduleListener();
        examHandler();
        debugSwitchListener();
        setNotice(this, findViewById(R.id.notice_code_settings));
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (!authController.isInitialized())
        {
            var i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        getDelegate().onStart();
    }

    private void refreshAccountsSpinner()
    {
        List<String> usernames = authController.getUsers();
        String currentUsername = authController.getUser();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                usernames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        accountSpinner.setAdapter(adapter);

        if (currentUsername != null && !usernames.isEmpty())
        {
            int selectionPosition = adapter.getPosition(currentUsername);
            if (selectionPosition >= 0)
            {
                accountSpinner.setSelection(selectionPosition);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        String selectedUsername = (String) parent.getItemAtPosition(position);
        String currentActiveUsername = authController.getUser();

        if (!selectedUsername.equals(currentActiveUsername))
        {
            executorService.execute(() ->
            {
                sessionController.switchAccount(selectedUsername, objects -> runOnUiThread(() ->
                {
                    Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }));
            });
        }
    }


    private void onDeleteAccountClicked()
    {
        executorService.execute(() ->
        {
            sessionController.removeUser(authController.getId(), objects ->
            {
                runOnUiThread(this::refreshAccountsSpinner);
                if (authController.getUser() == null && !authController.getUsers().isEmpty())
                {
                    sessionController.switchAccount(authController.getUsers().get(0), null);
                }
                else
                {
                    runOnUiThread(() ->
                    {
                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    });
                }
            });
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
    }

    private void onAddAccountClicked()
    {
        sessionController.resetSate();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("ADD_NEW_ACCOUNT", true);
        startActivity(intent);
    }

    private void debugSwitchListener()
    {
        binding.debugSwitch.setOnCheckedChangeListener((c, ac) ->
        {
            if (ac && lifetimeController.getMemLifetime().getExamLastUpdate() != null && lifetimeController.getMemLifetime().getScheduleLastUpdate() != null)
            {
                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
                binding.debugScheduleLifetime.setText(HtmlCompat.fromHtml(getApplicationContext().getString(R.string.schedule_title) + "<br/>" +
                        formatter.format(lifetimeController.getMemLifetime().getScheduleLastUpdate()), HtmlCompat.FROM_HTML_MODE_LEGACY));
                binding.debugExamLifetime.setText(HtmlCompat.fromHtml( getApplicationContext().getString(R.string.exam_title) + "<br/>" +
                                formatter.format(lifetimeController.getMemLifetime().getExamLastUpdate()), HtmlCompat.FROM_HTML_MODE_LEGACY));
            }
            else
            {
                binding.debugScheduleLifetime.setText("");
                binding.debugExamLifetime.setText("");
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