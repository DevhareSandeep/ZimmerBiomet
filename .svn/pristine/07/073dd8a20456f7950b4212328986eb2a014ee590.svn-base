package sg.totalebizsolutions.foundation.util;

public class IntUtil
{
  /**
   * Returns a 4-element byte representation of {@code value}.
   */
  public static byte[] toByteArray (int value)
  {
    return toByteArray(value, 4);
  }

  /**
   * Returns a n-element byte representation of (@code value).
   */
  public static byte[] toByteArray (int value, int byteSize)
  {
    byte[] b = new byte[byteSize];
    for (int i = 0; i < byteSize; i++)
    {
      b[i] = (byte) (value & 0xFF);
      value >>= 8;
    }
    return b;
  }

  /**
   * Returns the equivalent int value of specified byte array.
   */
  public static int fromBytes (byte... b)
  {
    int value = 0;
    for (int i = 0; i < b.length; i++)
    {
      value = value | (b[i] & 0xFF) << (8 * i);
    }
    return value;
  }
}
