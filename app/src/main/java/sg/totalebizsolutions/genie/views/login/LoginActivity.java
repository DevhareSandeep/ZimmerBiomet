package sg.totalebizsolutions.genie.views.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;

import sg.totalebizsolutions.foundation.util.DrawableUtil;
import sg.totalebizsolutions.foundation.util.Validation;
import sg.totalebizsolutions.foundation.util.ViewHelper;
import sg.totalebizsolutions.foundation.view.BaseActivity;
import sg.totalebizsolutions.genie.R;
import sg.totalebizsolutions.genie.ZimmerActivity;
import sg.totalebizsolutions.genie.databinding.LoginActivityBinding;
import sg.totalebizsolutions.genie.util.AlertDialogFactory;
import sg.totalebizsolutions.genie.views.privacy.PrivacyPolicyActivity;

public class LoginActivity extends BaseActivity {
    /* Properties */
    public static String PACKAGE_NAME;
    private LoginActivityBinding m_binding;
    private static final String PREVIOUSLY_LAUNCHED = "previously_launched";
  /* Life-cycle methods */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Resources res = getResources();
        PACKAGE_NAME = getApplicationContext().getPackageName();
        LoginActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.login_activity);
        m_binding = binding;

        ViewHelper.setBackgroundDrawable(
                binding.loginButton,
                DrawableUtil.createResourceDrawable(res, R.drawable.button_red_bg));
       /* if (BuildConfig.DEBUG) {
            m_binding.usernameEditText.setText("Zimmerbiomet");
            m_binding.passwordEditText.setText("sportsmedicine");
        }*/
        binding.loginButton.setOnClickListener((v) ->
        {
            login();
        });
    }

  /* Internal methods */

    private void login() {
        String username = m_binding.usernameEditText.getText().toString();
        String password = m_binding.passwordEditText.getText().toString();
        if (!Validation.isEqual(username, "Tester1")
                || !Validation.isEqual(password, "Tester1")) {
            AlertDialogFactory.buildAlertDialog(this, 0, R.string.login_invalid).show();
            return;
        }
        AlertDialogFactory.buildProgressDialog(this, R.string.login_progress).show();

        runInUIWithDelay(() ->
        {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            boolean previouslyStarted = prefs.getBoolean(PREVIOUSLY_LAUNCHED, false);
            if (!previouslyStarted) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean(PREVIOUSLY_LAUNCHED, Boolean.TRUE);
                edit.commit();
                showPrivacyPolicy();
            } else {
                Intent intent = new Intent(LoginActivity.this, ZimmerActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.activity_show, R.anim.activity_exit);
            }

        }, 2000L);
    }

    private void showPrivacyPolicy() {
        Intent intent = new Intent(LoginActivity.this, PrivacyPolicyActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.activity_show, R.anim.activity_exit);
    }
}
