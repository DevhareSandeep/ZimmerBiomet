package sg.totalebizsolutions.foundation.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class BitmapCache
{
  private static final LruCache<String, Bitmap> m_lruCache;
  static
  {
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final int cacheSize = maxMemory / 8;
    m_lruCache = new LruCache<String, Bitmap>(cacheSize)
    {
      @Override
      protected int sizeOf (String key, Bitmap value)
      {
        return value.getByteCount() / 1024;
      }
    };
  }

  /**
   * Cached the given bitmap mapped to specified key.
   */
  public static void cache (String key, Bitmap bitmap)
  {
    m_lruCache.put(key, bitmap);
  }

  /**
   * Retrieved any cached bitmap mapped to specified key.
   */
  public static Bitmap getCache (String key)
  {
    return m_lruCache.get(key);
  }

  /**
   * Evicts all cached bitmap from memory.
   */
  public static void evictAll ()
  {
    m_lruCache.evictAll();
  }
}
