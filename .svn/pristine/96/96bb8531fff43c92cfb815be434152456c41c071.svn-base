package sg.totalebizsolutions.foundation.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility class use for handling numbers(Conversion and formatting). <br/><br/> NOTE: For format
 * method with no rounding mode specified, the default rounding mode is set to {@link
 * RoundingMode#HALF_UP}.
 */
public class NumberUtil
{
  /* Static properties */

  private static NumberFormat sm_formatter;

  /**
   * Returns a locale specific {@link NumberFormat} instance.
   */
  private static NumberFormat getFormatterInstance ()
  {
    NumberFormat formatter = sm_formatter;

    if (formatter == null)
    {
      formatter = NumberFormat.getNumberInstance(Locale.US);
      sm_formatter = formatter;
    }
    return formatter;
  }

  /* Static methods */

  /**
   * Strips trailing zero of the specified decimal value. Returns null by default.
   */
  public static String stripTrailingZero (BigDecimal decimal)
  {
    return decimal != null ? decimal.stripTrailingZeros().toPlainString() : null;
  }

  /**
   * Converts a string represented number to integer. Return a value of 0 if string number format is
   * invalid.
   */
  public static int convertToInt (String stringNumber)
  {
    return parse(stringNumber, 0).intValue();
  }

  /**
   * Converts a string represented number to float.Return a value of 0.00 if string number format is
   * invalid.
   */
  public static float convertToFloat (String stringNumber)
  {
    return convertToFloat(stringNumber, 0);
  }

  /**
   * Converts a string represented number to float.Return a value of 0.00 if string number format is
   * invalid.
   */
  public static float convertToFloat (String stringNumber, float defaultValue)
  {
    return parse(stringNumber, defaultValue).floatValue();
  }

  /**
   * Converts a string represented number to double. Return a value of 0.00 if string number format
   * is invalid.
   */
  public static double convertToDouble (String stringNumber)
  {
    return convertToDouble(stringNumber, 0);
  }

  /**
   * Converts a string represented number to double. If string number format is invalid, will return
   * the default value as specified.
   */
  public static double convertToDouble (String stringNumber,
    double defaultValue)
  {
    return parse(stringNumber, defaultValue).doubleValue();
  }

  /**
   * Converts a string represented number to {@link BigDecimal}. Return a value of {@link
   * BigDecimal#ZERO} if string number format is invalid.
   */
  public static BigDecimal convertToBigDecimal (String stringNumber)
  {
    return convertToBigDecimal(stringNumber, BigDecimal.ZERO);
  }

  /**
   * Converts a string represented number to {@link BigDecimal}. Returns the default value specified
   * if string number format is invalid.
   */
  public static BigDecimal convertToBigDecimal (String stringNumber, BigDecimal defaultValue)
  {
    BigDecimal returnVal;
    if (   stringNumber == null
        || stringNumber.isEmpty())
    {
      returnVal = defaultValue;
    }
    else
    {
      try
      {
        returnVal = new BigDecimal(stringNumber);
      }
      catch (NumberFormatException e)
      {
        returnVal = defaultValue;
      }
    }
    return returnVal;
  }

  /**
   * Formats a string represented number to 2 decimal point string number representation. See also
   * {@link #format(String, int)}.
   */
  public static String format (String stringNumber)
  {
    return format(stringNumber, 2);
  }

  /**
   * Formats a string represented number to a string number representation with the number of
   * decimal points specified. See also {@link #format(String, int, RoundingMode)}.
   */
  public static String format (String stringNumber, int decimalPoints)
  {
    return format(stringNumber, decimalPoints, RoundingMode.HALF_UP);
  }

  /**
   * Formats a string represented number to a string number representation with the number of
   * decimal points and rounding mode specified.
   */
  public static String format (String stringNumber, int decimalPoints, RoundingMode roundingMode)
  {
    return format(convertToDouble(stringNumber), decimalPoints, roundingMode);
  }

  /**
   * Formats a string represented number to pretty 2 decimal point string number representation. See
   * also {@link #formatPretty(String, int)}.
   */
  public static String formatPretty (String stringNumber)
  {
    return formatPretty(stringNumber, 2);
  }

  /**
   * Formats a string represented number to a pretty string number representation with the number of
   * decimal points specified(##,###,###.###). See also {@link #formatPretty(String, int,
   * RoundingMode)}.
   */
  public static String formatPretty (String stringNumber, int decimalPoints)
  {
    return formatPretty(stringNumber, decimalPoints, RoundingMode.HALF_UP);
  }

  /**
   * Formats a string represented number to a pretty string number representation with the number of
   * decimal points(##,###,###.###) and rounding mode specified.
   */
  public static String formatPretty (String stringNumber, int decimalPoints,
    RoundingMode roundingMode)
  {
    return formatPretty(convertToDouble(stringNumber), decimalPoints, roundingMode);
  }

  /**
   * Formats a {@link BigDecimal} value to string to a 2 decimal string number representation. See
   * also {@link #format(BigDecimal, int)}.
   */
  public static String format (BigDecimal decimal)
  {
    return format(decimal, 2);
  }

  /**
   * Formats a {@link BigDecimal} value to a string number representation with the number of decimal
   * points specified. See also {@link #format(BigDecimal, int, RoundingMode)}.
   */
  public static String format (BigDecimal decimal, int decimalPoints)
  {
    return format(decimal, decimalPoints, RoundingMode.HALF_UP);
  }

  /**
   * Formats a {@link BigDecimal} value to a string number representation with the number of decimal
   * points and rounding mode specified.
   */
  public static String format (BigDecimal decimal, int decimalPoints, RoundingMode roundingMode)
  {
    if (decimal == null)
    {
      return null;
    }
    return format(decimal.doubleValue(), decimalPoints, roundingMode, false);
  }

  /**
   * Formats a {@link BigDecimal} value to string to a pretty 2 decimal string number
   * representation(##,###.##). See also {@link #formatPretty(BigDecimal, int)}.
   */
  public static String formatPretty (BigDecimal decimal)
  {
    return formatPretty(decimal, 2);
  }

  /**
   * Formats a {@link BigDecimal} value to a pretty string number representation with the number of
   * decimal points specified(#,###,###.###). See also {@link #formatPretty(BigDecimal, int,
   * RoundingMode)}.
   */
  public static String formatPretty (BigDecimal decimal, int decimalPoints)
  {
    return formatPretty(decimal, decimalPoints, RoundingMode.HALF_UP);
  }

  /**
   * Formats a {@link BigDecimal} value to a pretty string number representation with the number of
   * decimal points(#,###,###.###) and rounding mode specified.
   */
  public static String formatPretty (BigDecimal decimal, int decimalPoints,
    RoundingMode roundingMode)
  {
    if (decimal == null)
    {
      return null;
    }
    return format(decimal.doubleValue(), decimalPoints, roundingMode, true);
  }

  /**
   * Formats a double value to a 2 decimal string number representation. See also {@link
   * #format(double, int)}.
   */
  public static String format (double decimal)
  {
    return format(decimal, 2);
  }

  /**
   * Formats double value to a pretty string number representation with the number of decimal points
   * specified. See also {@link #format(double, int, RoundingMode)}.
   */
  public static String format (double decimal, int decimalPoints)
  {
    return format(decimal, decimalPoints, RoundingMode.HALF_UP);
  }

  /**
   * Formats double value to a pretty string number representation with the number of decimal points
   * and rounding mode specified.
   */
  public static String format (double decimal, int decimalPoints,
    RoundingMode roundingMode)
  {
    return format(decimal, decimalPoints, roundingMode, false);
  }

  /**
   * Formats decimal value to a 2 decimal pretty string number representation(##,###.##). See also
   * {@link #formatPretty(double, int)}.
   */
  public static String formatPretty (double decimal)
  {
    return formatPretty(decimal, 2);
  }

  /**
   * Formats a decimal value to a pretty string number representation with the number of decimal
   * points specified(#,###,###.###). See also {@link #formatPretty(double, int, RoundingMode)}.
   */
  public static String formatPretty (double decimal, int decimalPoints)
  {
    return formatPretty(decimal, decimalPoints, RoundingMode.HALF_UP);
  }

  /**
   * Formats a decimal value to a pretty string number representation with the number of decimal
   * points(#,###,###.###) and rounding mode specified.
   */
  public static String formatPretty (double decimal, int decimalPoints,
    RoundingMode roundingMode)
  {
    return format(decimal, decimalPoints, roundingMode, true);
  }

  /**
   * Common method to format value.
   *
   * @param decimal the double value to format
   * @param decimalPoints the reference minimum number of fraction digits
   * @param roundingMode the reference reference round mode to be used for the formatter
   * @param useGroupSeparator whether the returned group separated
   */
  private static String format (double decimal, int decimalPoints, RoundingMode roundingMode,
    boolean useGroupSeparator)
  {
    NumberFormat format = getFormatterInstance();
    format.setMinimumFractionDigits(decimalPoints);
    format.setMaximumFractionDigits(decimalPoints);
    format.setRoundingMode(roundingMode);
    format.setGroupingUsed(useGroupSeparator);
    return format.format(decimal);
  }

  /**
   * Common method to parse a string represented number.
   */
  private static Number parse (String stringNumber, double defaultValue)
  {
    Number number;
    if (   stringNumber == null
        || stringNumber.isEmpty())
    {
      number = defaultValue;
    }
    else
    {
      try
      {
        NumberFormat format = getFormatterInstance();
        number = format.parse(stringNumber);
      }
      catch (Exception e)
      {
        number = defaultValue;
      }
    }
    return number;
  }
}