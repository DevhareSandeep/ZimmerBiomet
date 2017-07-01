package sg.totalebizsolutions.foundation.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JSONUtil
{
  /**
   * Retrieve long value from specified object identified by key. Returns 0L by default if key does
   * not exists.
   */
  public static long getLong (JSONObject object, String key)
  {
    return object.optLong(toPascalCase(key));
  }

  /**
   * Retrieve boolean value from specified object identified by key. Returns false by default if key
   * does not exists.
   */
  public static boolean getBoolean (JSONObject object, String key)
  {
    return object.optBoolean(toPascalCase(key));
  }

  /**
   * Retrieve integer value from specified object identified by key. Returns 0 by default if key
   * does not exists.
   */
  public static int getInt (JSONObject object, String key)
  {
    return object.optInt(toPascalCase(key));
  }

  /**
   * Retrieve String value from specified object identified by key. Returns empty string by default
   * if key does not exists.
   */
  public static String getString (JSONObject object, String key)
  {
    return object.optString(toPascalCase(key));
  }

  /**
   * Retrieve double value from specified object identified by key. Returns 0.0 string by default
   * if key does not exists.
   */
  public static double getDouble (JSONObject object, String key)
  {
    return object.optDouble(toPascalCase(key), 0);
  }

  /**
   * Utility method that puts the specified data integer identified by key to json object. If data
   * is null, it will be replaced with an empty string.
   */
  public static void put (JSONObject object, String key, Integer data)
  {
    if (data == null)
    {
      data = 0;
    }
    internalPut(object, key, data);
  }

  /**
   * Utility method that puts the specified data identified by key to json object. If data is null,
   * it will be replaced with an empty string.
   */
  public static void put (JSONObject object, String key, String data)
  {
    if (   data == null
        || data.trim().isEmpty())
    {
      data = "";
    }
    internalPut(object, key, data);
  }

  /**
   * Utility method that puts the specified data identified by key to json object. If data is null,
   * it will be replaced with a default value of 0L.
   */
  public static void put (JSONObject object, String key, Long data)
  {
    if (data == null)
    {
      data = 0L;
    }
    internalPut(object, key, data);
  }

  /**
   * Utility method that puts the specified data identified by key to json object. If data is null,
   * it will be replaced with a default value of 0.0f.
   */
  public static void put (JSONObject object, String key, Float data)
  {
    if (data == null)
    {
      data = 0f;
    }
    internalPut(object, key, data);
  }

  /**
   * Utility method that puts the specified data identified by key to json object. If data is null,
   * it will be replaced with a default value of 0.0.
   */
  public static void put (JSONObject object, String key, Double data)
  {
    if (data == null)
    {
      data = 0.0;
    }
    internalPut(object, key, data);
  }

  /**
   * Converts a raw jSON object to an actual {@link Map}.
   *
   * @param raw
   *          the raw jSON object string
   * @return the corresponding {@link Map} instance
   * @throws JSONException
   *           if the raw jSON object string was malformed.
   */
  public static Map<String, Object> convertToMap (String raw) throws JSONException
  {
    Map<String, Object> resultMap = new HashMap<>();
    JSONObject jObject = new JSONObject(raw);
    Iterator<?> iterator = jObject.keys();

    while (iterator.hasNext())
    {
      String key = (String) iterator.next();
      Object item;
      String stringItem = jObject.getString(key);

      if (stringItem.startsWith("{") && stringItem.endsWith("}"))
      {
        item = convertToMap(stringItem);
      }
      else if (stringItem.startsWith("[") && stringItem.endsWith("]"))
      {
        item = convertToList(stringItem);
      }
      else
      {
        item = jObject.get(key);
      }
      resultMap.put(key, item);
    }
    return resultMap;
  }

  /**
   * Converts a raw jSON array to an actual {@link List}.
   *
   * @param raw
   *          the raw jSON array string
   * @return the corresponding {@link List} instance
   * @throws JSONException
   *           if the raw jSON array string was malformed.
   */
  public static List<Object> convertToList (String raw) throws JSONException
  {
    List<Object> resultList = new ArrayList<>();
    JSONArray jArray = new JSONArray(raw);

    for (int k = 0; k < jArray.length(); k++)
    {
      Object item;
      String stringItem = jArray.getString(k);

      if (stringItem.startsWith("{") && stringItem.endsWith("}"))
      {
        item = convertToMap(stringItem);
      }
      else if (stringItem.startsWith("[") && stringItem.endsWith("]"))
      {
        item = convertToList(stringItem);
      }
      else
      {
        item = jArray.get(k);
      }
      resultList.add(item);
    }
    return resultList;
  }

  /**
   * Converts a {@link Map} to a {@link JSONObject}.
   *
   * @param rawMap
   *          the {@link Map} of strings to objects
   * @return the corresponding {@link JSONObject} instance
   * @throws JSONException
   *           if there are null keys on both the root map or child maps
   */
  @SuppressWarnings("unchecked")
  public static final JSONObject convertFromMap (Map<String, Object> rawMap) throws JSONException
  {
    JSONObject jObject = new JSONObject();

    for (String key : rawMap.keySet())
    {
      Object item = rawMap.get(key);

      if (item instanceof Map<?, ?>)
      {
        item = convertFromMap((Map<String, Object>) item);
      }
      else if (item instanceof List<?>)
      {
        item = convertFromList((List<Object>) item);
      }
      internalPut(jObject, key, item);
    }
    return jObject;
  }

  /**
   * Converts a {@link List} to a {@link JSONArray}.
   *
   * @param rawList
   *          the {@link List} of objects
   * @return the corresponding {@link JSONArray} instance
   * @throws JSONException
   *           if there are null keys on child maps
   */
  @SuppressWarnings("unchecked")
  public static JSONArray convertFromList (List<Object> rawList) throws JSONException
  {
    JSONArray jArray = new JSONArray();

    for (Object item : rawList)
    {
      if (item instanceof Map<?, ?>)
      {
        item = convertFromMap((Map<String, Object>) item);
      }
      else if (item instanceof List<?>)
      {
        item = convertFromList((List<Object>) item);
      }
      jArray.put(item);
    }
    return jArray;
  }

  /**
   * Common method to put object value. Ignores if data provided is null.
   */
  public static void internalPut (JSONObject object, String key, Object data)
  {
    if (data == null)
    {
      return;
    }

    try
    {
      object.put(toPascalCase(key), data);
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
  }

  private static final StringBuilder sm_builder = new StringBuilder();
  public static synchronized String toPascalCase (String value)
  {
    if (   value == null
        || value.isEmpty())
    {
      return value;
    }

    if (sm_builder.length() > 0)
    {
      sm_builder.delete(0, sm_builder.length());
    }

    sm_builder.append(value.substring(0, 1).toUpperCase());
    if (value.length() >= 2)
    {
      sm_builder.append(value.substring(1));
    }
    return sm_builder.toString();
  }
}
