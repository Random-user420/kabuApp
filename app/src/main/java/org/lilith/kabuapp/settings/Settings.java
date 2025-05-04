package org.lilith.kabuapp.settings;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import org.lilith.kabuapp.KabuApp;
import org.lilith.kabuapp.R;
import org.lilith.kabuapp.databinding.SettingsActivityBinding;
import org.lilith.kabuapp.login.AuthController;
import org.lilith.kabuapp.login.Login;
import org.lilith.kabuapp.schedule.Schedule;

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

        setScheduleListener();
        debugSwitchListener();
        setNotice();
    }

    private void setNotice()
    {
        TextView myTextView = findViewById(R.id.notice_code);

        String prefix = getString(R.string.notice_code);
        String linkText = getString(R.string.github_text);
        String url = getString(R.string.github_link);

        String fullText = prefix + linkText + ".";

        SpannableString spannableString = new SpannableString(fullText);
        spannableString.setSpan(new URLSpan(url), fullText.indexOf(linkText), fullText.indexOf(linkText) + linkText.length(), Spanned.SPAN_COMPOSING);

        myTextView.setText(spannableString);

        myTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void resetUserHandler()
    {
        binding.logout.setOnClickListener(v ->
        {
            authController.setCredentials("", "", "");
            var i = new Intent(this, Login.class);
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
            var i = new Intent(this, Schedule.class);
            startActivity(i);
        });
    }
}