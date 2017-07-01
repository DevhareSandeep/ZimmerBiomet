package sg.totalebizsolutions.foundation.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import sg.totalebizsolutions.foundation.R;

public class BaseActivity extends AppCompatActivity
{
  /* Properties */

  private static final String EXTRA_PRESENTED = "present";

  private Handler m_internalHandler;

  /* Fragment life-cycle methods */

  @Override
  protected void onCreate (Bundle savedInstanceState)
  {

    super.onCreate(savedInstanceState);

    m_internalHandler = new Handler();
  }

  @Override
  protected void onDestroy ()
  {
    if (m_internalHandler != null)
    {
      m_internalHandler.removeCallbacksAndMessages(null);
      m_internalHandler = null;
    }
    super.onDestroy();
  }

  @Override
  public void finish ()
  {
    super.finish();
    boolean isPresented = isPresented();
    if (isPresented)
    {
      overridePendingTransition(R.anim.activity_dismiss_enter, R.anim.activity_dismiss_exit);
    }
  }

  /* Property methods */

  protected boolean isPresented ()
  {
    return getIntent().getBooleanExtra(EXTRA_PRESENTED, false);
  }

  public void presentActivity (Intent intent)
  {
    intent.putExtra(EXTRA_PRESENTED, true);
    startActivity(intent);
    overridePendingTransition(R.anim.activity_present_enter, R.anim.activity_present_exit);
  }

  /**
   * Retrieve attached fragment with the specified string tag.
   *
   * @param tag the String fragment tag used when fragment is attached
   * @return the Fragment instance
   */
  protected Fragment retrieveFragmentByTag (String tag)
  {
    return getSupportFragmentManager().findFragmentByTag(tag);
  }

  /**
   * Retrieve attached fragment with the specified id.
   *
   * @param id the Integer fragment id used when fragment is attached
   * @return the Fragment instance
   */
  protected Fragment retrieveFragmentByID (int id)
  {
    return getSupportFragmentManager().findFragmentById(id);
  }

  /* Thread confinement methods */

  /**
   * Schedules the {@link Runnable} instance to be executed on the main
   * thread.<br/>NOTE: Any Pending or to be scheduled runnable instances will be
   * disregard when activity is destroyed.
   */
  protected void runInUI (Runnable runnable)
  {
    if (m_internalHandler != null)
    {
      m_internalHandler.post(runnable);
    }
  }

  /**
   * Schedules the {@link Runnable} instance that will be executed at the
   * given time delay in milliseconds on the main thread.<br/>NOTE: Any
   * Pending or to  be scheduled runnable instances will be disregard
   * when activity is destroyed.
   */
  protected void runInUIWithDelay (Runnable runnable, long delayInMillis)
  {
    if (m_internalHandler != null)
    {
      m_internalHandler.postDelayed(runnable, delayInMillis);
    }
  }
}
