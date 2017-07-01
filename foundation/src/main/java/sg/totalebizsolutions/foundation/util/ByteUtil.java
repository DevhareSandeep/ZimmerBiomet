package sg.totalebizsolutions.foundation.util;

import java.util.Arrays;

public class ByteUtil
{
  /**
   * Returns a combined byte array composing the sets of byte array provided.
   */
  public static byte[] concat (byte[]... byteArrays)
  {
    int len = 0;
    for (byte[] b : byteArrays)
    {
      if (b == null)
      {
        continue;
      }
      len += b.length;
    }

    int pos = 0;
    byte[] result = new byte[len];
    for (byte[] b : byteArrays)
    {
      if (b == null)
      {
        continue;
      }
      System.arraycopy(b, 0, result, pos, b.length);
      pos += b.length;
    }
    return result;
  }

  /**
   * Splits the given byte array into a fixed no of byte size.
   */
  public static byte[][] split (byte[] byteArray, int byteSize)
  {
    int len = byteArray.length;
    int size = len / byteSize + (len % byteSize == 0 ? 0 : 1);
    byte[][] result = new byte[size][byteSize];

    int pos = 0;
    for (int i = 0; i < size; i++)
    {
      result[i] = Arrays.copyOfRange(byteArray, pos, pos + Math.min(byteSize, len - pos));

      pos += byteSize;
    }
    return result;
  }

  /**
   * Converts the given byte array into hex string data representation.
   */
  public static String toHexString (byte... byteArray)
  {
    StringBuilder builder = new StringBuilder();
    for (byte b : byteArray)
    {
      builder.append(String.format("%02X ", b));
    }
    return builder.toString();
  }
}
