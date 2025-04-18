package org.lilith.kabuapp.schedule;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.lilith.kabuapp.KabuApp;
import org.lilith.kabuapp.R;
import org.lilith.kabuapp.databinding.ActivityScheduleBinding;
import org.lilith.kabuapp.login.AuthController;
import org.lilith.kabuapp.login.Login;
import org.lilith.kabuapp.settings.Settings;


public class Schedule extends AppCompatActivity
{
    private ActivityScheduleBinding binding;
    private AuthController authController;

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

        settingsHandler();
    }

    @Override
    protected void onStart() {
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