package org.kabuapp.kabuapp.login;

import static org.kabuapp.kabuapp.ui.NoticeGenerator.setNotice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import org.kabuapp.kabuapp.KabuApp;
import org.kabuapp.kabuapp.R;
import org.kabuapp.kabuapp.databinding.ActivityLoginBinding;
import org.kabuapp.kabuapp.db.controller.AuthController;
import org.kabuapp.kabuapp.db.controller.SessionController;
import org.kabuapp.kabuapp.interfaces.Callback;
import org.kabuapp.kabuapp.schedule.ScheduleActivity;

public class LoginActivity extends AppCompatActivity implements Callback
{
    private ActivityLoginBinding binding;
    private AuthController authController;
    private SessionController sessionController;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authController = ((KabuApp) getApplication()).getAuthController();
        sessionController = ((KabuApp) getApplication()).getSessionController();

        setContentView(binding.getRoot());
        authHandler();
        setNotice(this, findViewById(R.id.notice_code_login));

        boolean isAddNewAccountFlow = getIntent().getBooleanExtra("ADD_NEW_ACCOUNT", false);
        if (isAddNewAccountFlow)
        {
            binding.loginButtonBack.setVisibility(View.VISIBLE);
            binding.loginButtonBack.setOnClickListener((v) ->
            {
                sessionController.loadSession(this, null, null);
            });
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if (authController.isInitialized())
        {
            var i = new Intent(this, ScheduleActivity.class);
            startActivity(i);
        }

        getDelegate().onStart();
    }

    private void authHandler()
    {
        final EditText username = binding.username;
        final EditText password = binding.password;
        final Button loginButton = binding.login;

        loginButton.setOnClickListener(v ->
        {
            Object[] args = { null };
            if (!authController.setCredentials(username.getText().toString(), password.getText().toString(), this, args))
            {
                if (binding.username.isFocused())
                {
                    binding.username.setError(getString(R.string.login_wrong));
                }
                else
                {
                    binding.password.setError(getString(R.string.login_wrong));
                }
            }
        });
    }

    public void callback(Object[] args)
    {
        if (args == null || args[0] == null)
        {
            Intent i = new Intent(this, ScheduleActivity.class);
            startActivity(i);
        }
        else
        {
            if (binding.username.isFocused())
            {
                binding.username.setError(getString(R.string.login_wrong));
            }
            else
            {
                binding.password.setError(getString(R.string.login_wrong));
            }
        }
    }
}