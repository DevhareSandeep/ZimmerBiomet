package sg.totalebizsolutions.foundation.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class BaseFragment extends BaseControllerFragment
{
  /* Properties */

  private Handler m_handler;

  /* Fragment life-cycle methods */

  @Override
  public void onCreate (Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    m_handler = new Handler();
  }

  @Override
  public void onDestroyView ()
  {
    hideKeyboard();
    super.onDestroyView();
  }

  @Override
  public void onDestroy ()
  {
    if (m_handler != null)
    {
      m_handler.removeCallbacksAndMessages(null);
      m_handler = null;
    }
    super.onDestroy();
  }

  /* Custom callback methods */

  protected void hideKeyboard ()
  {
    View curFocus;
    Activity activity = getActivity();
    if (   activity != null
        && (curFocus = activity.getCurrentFocus()) != null)
    {
      InputMethodManager imm = (InputMethodManager)
          activity.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(curFocus.getWindowToken(), 0);
    }
  }

  /**
   * Called when a key was pressed down and not handled by any of the views.
   */
  public boolean onKeyDown (int keyCode, KeyEvent event)
  {
    return false;
  }

  /* Property methods */

  /**
   * Retrieve attached fragment with the specified string tag.
   *
   * @param tag the String fragment tag used when fragment is attached
   * @return the Fragment instance
   */
  protected Fragment retrieveFragmentByTag (String tag)
  {
    return getChildFragmentManager().findFragmentByTag(tag);
  }

  /**
   * Retrieve attached fragment with the specified id.
   *
   * @param id the Integer fragment id used when fragment is attached
   * @return the Fragment instance
   */
  protected Fragment retrieveFragmentByid (int id)
  {
    return getChildFragmentManager().findFragmentById(id);
  }

  /* Thread confinement methods */

  /**
   * Schedules the {@link Runnable} instance to be executed on the main
   * thread.<br/>NOTE: Any Pending or to be scheduled runnable instances will
   * be disregard when fragment is destroyed.
   */
  protected void syncCallback (Runnable runnable)
  {
    if (m_handler != null)
    {
      m_handler.post(runnable);
    }
  }

  /**
   * Schedules the {@link Runnable} instance that will be executed at the
   * given time delay in milliseconds on the main thread.<br/>NOTE: Any Pending
   * or to be scheduled runnable instances will be disregard.
   */
  protected void syncCallbackDelayed (Runnable runnable, long delayInMillis)
  {
    if (m_handler != null)
    {
      m_handler.postDelayed(runnable, delayInMillis);
    }
  }
}
