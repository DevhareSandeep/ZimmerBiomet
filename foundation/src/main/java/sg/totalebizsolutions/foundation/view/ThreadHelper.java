package sg.totalebizsolutions.foundation.view;

import android.os.Handler;

public class ThreadHelper
{
  /* Properties */

  private Handler m_handler;

  /* Initializations */

  public ThreadHelper ()
  {
    m_handler = new Handler();
  }

  /**
   * Remove and kill any scheduled runnable intances submitted to be executed
   * by handler.
   */
  public void destroy ()
  {
    if (m_handler != null)
    {
      m_handler.removeCallbacksAndMessages(null);
      m_handler = null;
    }
  }

  /**
   * Schedules the {@link Runnable} instance to be executed by the handler.
   * <br/>NOTE: Any Pending or to be scheduled runnable instances will
   * be disregard when already destroyed.
   */
  public void syncCallback (Runnable runnable)
  {
    if (m_handler != null)
    {
      m_handler.post(runnable);
    }
  }

  /**
   * Schedules the {@link Runnable} instance that will be executed at the
   * given time delay in milliseconds by handler.<br/>NOTE: Any Pending
   * or to be scheduled runnable instances will be disregard.
   */
  public void syncCallbackDelayed (Runnable runnable, long delayInMillis)
  {
    if (m_handler != null)
    {
      m_handler.postDelayed(runnable, delayInMillis);
    }
  }
}
