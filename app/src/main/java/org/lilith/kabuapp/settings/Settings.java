package org.lilith.kabuapp.settings;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import org.lilith.kabuapp.KabuApp;
import org.lilith.kabuapp.R;
import org.lilith.kabuapp.databinding.SettingsActivityBinding;
import org.lilith.kabuapp.login.AuthController;

public class Settings extends AppCompatActivity
{
    private SettingsActivityBinding binding;
    private AuthController authController;

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

        resetUserHandler();

        binding.debugUsername.setText(authController.getStateholder().getUsername());
        binding.debugPassword.setText(authController.getStateholder().getPassword());
        binding.debugToken.setText(authController.getStateholder().getToken());
    }

    private void resetUserHandler()
    {
        binding.resetUser.setOnClickListener(v -> authController.setCredentials("", "", ""));
    }
}