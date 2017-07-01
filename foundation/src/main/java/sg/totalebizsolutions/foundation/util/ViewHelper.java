package sg.totalebizsolutions.foundation.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewParent;

/**
 * Utility class for backward-compatibility fashion.
 */
public class ViewHelper
{
  /**
   * Sets view background.
   */
  public static void setBackgroundDrawable (View view, Drawable drawable)
  {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
    {
      view.setBackground(drawable);
    }
    else
    {
      view.setBackgroundDrawable(drawable);
    }
  }

  /**
   * See @{@link Resources#getColor(int, Resources.Theme)}.
   */
  public static int getColor (Context context, int colorResId)
  {
    int color;
    Resources res = context.getResources();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    {
      color = res.getColor(colorResId, context.getTheme());
    }
    else
    {
      color = res.getColor(colorResId);
    }
    return color;
  }

  /**
   * See @{@link Resources#getDrawable(int, Resources.Theme)}.
   */
  public static Drawable getDrawable (Context context, int drawableResId)
  {
    Drawable drawable;
    Resources res = context.getResources();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    {
      drawable = res.getDrawable(drawableResId, context.getTheme());
    }
    else
    {
      drawable = res.getDrawable(drawableResId);
    }
    return drawable;
  }

  public static void requestDisallowInctercept (View view)
  {
    ViewParent parent = view.getParent();
    if (parent != null)
    {
      parent.requestDisallowInterceptTouchEvent(true);
      if (parent instanceof View)
      {
        requestDisallowInctercept((View) parent);
      }
    }
  }
}
