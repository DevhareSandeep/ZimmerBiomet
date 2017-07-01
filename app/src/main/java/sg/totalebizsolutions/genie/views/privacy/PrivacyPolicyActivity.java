package sg.totalebizsolutions.genie.views.privacy;

import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import sg.totalebizsolutions.foundation.util.DrawableUtil;
import sg.totalebizsolutions.foundation.util.ViewHelper;
import sg.totalebizsolutions.foundation.view.BaseActivity;
import sg.totalebizsolutions.genie.R;
import sg.totalebizsolutions.genie.ZimmerActivity;
import sg.totalebizsolutions.genie.databinding.PrivacyPolicyActivityBinding;

public class PrivacyPolicyActivity extends BaseActivity
{
  //private static final String PREVIOUSLY_LAUNCHED = "previously_launched";
  /* Life-cycle methods */

  @Override
  protected void onCreate (Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    PrivacyPolicyActivityBinding binding =
        DataBindingUtil.setContentView(this, R.layout.privacy_policy_activity);

    final Resources res = getResources();

    ViewHelper.setBackgroundDrawable(
        binding.doneButton,
        DrawableUtil.createResourceDrawable(res, R.drawable.button_red_bg));

    binding.doneButton.setOnClickListener((v) ->
    {
      Intent intent = new Intent(PrivacyPolicyActivity.this, ZimmerActivity.class);
      startActivity(intent);
      finish();
      overridePendingTransition(R.anim.activity_show, R.anim.activity_exit);

    });
  }


  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

}
