package org.lilith.kabuapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.lilith.kabuapp.KabuApp;
import org.lilith.kabuapp.R;
import org.lilith.kabuapp.databinding.ActivityLoginBinding;
import org.lilith.kabuapp.models.Callback;
import org.lilith.kabuapp.schedule.Schedule;
import org.lilith.kabuapp.settings.Settings;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Login extends AppCompatActivity implements Callback
{
    private ActivityLoginBinding binding;
    private AuthController authController;

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

        setContentView(binding.getRoot());
        authHandler();
        settingsHandler();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (authController.isInitalized())
        {
            var i = new Intent(this, Schedule.class);
            startActivity(i);
        }

        getDelegate().onStart();
    }

    private void authHandler()
    {
        final EditText username = binding.username;
        final EditText password = binding.password;
        final Button loginButton = binding.login;

        loginButton.setOnClickListener(v -> {
            Object[] args = {};
            if (!authController.setCredentials(username.getText().toString(), password.getText().toString(), this, args))
            {
                Logger.getLogger("Login").log(Level.INFO, "No username or password");
            }
        });
    }

    public void callback(Object[] args)
    {
        Intent i = new Intent(this, Schedule.class);
        startActivity(i);
    }

    private void settingsHandler()
    {
        binding.settingsButton.setOnClickListener(v ->
        {
            var i = new Intent(this, Settings.class);
            startActivity(i);
        });
    }
}