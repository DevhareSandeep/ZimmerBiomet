package sg.totalebizsolutions.foundation.util;

public class Validation
{
  public static boolean isEmpty (String value)
  {
    return value == null || value.isEmpty();
  }

  public static boolean isEqual (String x, String y)
  {
    return   x != null
          && y != null
          && x.equalsIgnoreCase(y);
  }

  public static boolean contains (String base, String y)
  {
    return   base != null
          && y != null
          && base.toLowerCase().contains(y.toLowerCase());
  }
}
