package sg.totalebizsolutions.genie.util;

import android.util.Log;

import sg.totalebizsolutions.genie.BuildConfig;

public class Logger
{
  private static final String TAG = "Zimmer";
  private static final boolean EnableDebug = BuildConfig.DEBUG;

  public static void debug (String message)
  {
    if (EnableDebug)
    {
      Log.d(TAG, message);
    }
  }

  public static void info (String message)
  {
    if (EnableDebug)
    {
      Log.i(TAG, message);
    }
  }
}