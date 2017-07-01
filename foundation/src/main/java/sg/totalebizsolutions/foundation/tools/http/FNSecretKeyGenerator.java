package sg.totalebizsolutions.foundation.tools.http;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Class that generates secret key based on the specified passwords. As
 * advised, password should be build at runtime for better security reasons.
 */
public class FNSecretKeyGenerator
{
  /* Constants */

  private static final int ITERATION_COUNT = 1000;
  private static final int KEY_LENGTH = 512;

  private static final byte[] SALT = new byte[]{(byte) 0xA12, (byte) 0x84,
      (byte) 0xFFD2, (byte) 0x021, (byte) 0x21, (byte) 0x101,
  };

  /**
   * Generates secret key based on the specified password chunks.
   */
  public static SecretKey generateKey (String... password)
      throws KeyGenException
  {
    return generateKey(SALT, password);
  }

  /**
   * Generates a Secret key based on the specified password, salt.
   */
  public static SecretKey generateKey (byte[] salt, String... password)
      throws KeyGenException
  {
    char[] bytePassword = buildPassword(password);

    SecretKey secretKey = null;
    try
    {
      SecretKeyFactory secretKeyFactory =
          SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

      KeySpec keySpec =
          new PBEKeySpec(bytePassword, salt, ITERATION_COUNT, KEY_LENGTH);

      secretKey = secretKeyFactory.generateSecret(keySpec);
    } catch (NoSuchAlgorithmException|InvalidKeySpecException e)
    {
      throw new KeyGenException(e.getMessage(), e.getCause());
    }
    return secretKey;
  }

  /**
   * Build the sets password chunks into byte character array.
   */
  private static char[] buildPassword (String... passwordChunk)
  {
    StringBuilder buffer = new StringBuilder();

    int len = passwordChunk.length;
    for (int i = 0 ; i < len; i++)
    {
      buffer.append(passwordChunk[i]);
    }
    return buffer.toString().toCharArray();
  }

  public static class KeyGenException extends Exception
  {
    public KeyGenException (String detailMessage, Throwable throwable)
    {
      super(detailMessage, throwable);
    }
  }
}
