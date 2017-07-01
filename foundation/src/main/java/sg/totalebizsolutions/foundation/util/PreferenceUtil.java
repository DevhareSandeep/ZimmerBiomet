package sg.totalebizsolutions.foundation.util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;
import java.util.List;

public class PreferenceUtil
{
  private static final String SEPARATOR = "!@#;";

  public static boolean getBoolProperty (SharedPreferences prefs, String key, boolean defValue)
  {
    return prefs.getBoolean(key, defValue);
  }

  public static void setBoolProperty (SharedPreferences prefs, String key, boolean value)
  {
    Editor editor = prefs.edit();
    editor.putBoolean(key, value);
    editor.apply();
  }


  public static String getStringProperty (SharedPreferences prefs, String key, String defValue)
  {
    return prefs.getString(key, defValue);
  }

  public static void setStringProperty (SharedPreferences prefs, String key, String value)
  {
    Editor editor = prefs.edit();
    if (value == null)
    {
      editor.remove(key);
    }
    else
    {
      editor.putString(key, value);
    }
    editor.apply();
  }

  public static List<String> getStringProperty (SharedPreferences prefs, String key)
  {
    List<String> result = new ArrayList<>();

    String value = getStringProperty(prefs, key, null);
    if (   value != null
        && value.length() > 0)
    {
      String[] values = value.split(SEPARATOR);
      for (String v : values)
      {
        result.add(v);
      }
    }
    return result;
  }

  public static void setStringProperty (SharedPreferences prefs, String key, List<String> values)
  {
    StringBuilder builder = new StringBuilder();
    if (values != null)
    {
      for (String value : values)
      {
        builder.append(value);
        builder.append(SEPARATOR);
      }

      final int len = builder.length();
      if (len > 0)
      {
        builder.delete(len - SEPARATOR.length(), len);
      }
    }
    setStringProperty(prefs, key, builder.toString());
  }

  public static boolean getStringProperty (SharedPreferences pres, String key, boolean defValue)
  {
    return pres.getBoolean(key, defValue);
  }

  public static void setStringProperty (SharedPreferences prefs, String key, boolean value)
  {
    Editor editor = prefs.edit();
    editor.putBoolean(key, value);
    editor.apply();
  }

  public static int getStringProperty (SharedPreferences prefs, String key, int defValue)
  {
    return prefs.getInt(key, defValue);
  }

  public static void setStringProperty (SharedPreferences prefs, String key, int value)
  {
    Editor editor = prefs.edit();
    editor.putInt(key, value);
    editor.apply();
  }
}
