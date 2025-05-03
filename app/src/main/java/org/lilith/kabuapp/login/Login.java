package org.lilith.kabuapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lilith.kabuapp.KabuApp;
import org.lilith.kabuapp.R;
import org.lilith.kabuapp.databinding.ActivityLoginBinding;
import org.lilith.kabuapp.interfaces.Callback;
import org.lilith.kabuapp.schedule.Schedule;
import org.lilith.kabuapp.settings.Settings;


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
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if (authController.isInitialized())
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

        loginButton.setOnClickListener(v ->
        {
            Object[] args = { null };
            if (!authController.setCredentials(username.getText().toString(), password.getText().toString(), this, args))
            {
                Logger.getLogger("Login").log(Level.INFO, "No username or password");
            }
        });
    }

    public void callback(Object[] args)
    {
        if (args == null || args[0] == null)
        {
            Intent i = new Intent(this, Schedule.class);
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