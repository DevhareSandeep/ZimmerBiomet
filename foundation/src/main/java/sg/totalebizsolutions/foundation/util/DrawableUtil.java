package sg.totalebizsolutions.foundation.util;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;

public final class DrawableUtil
{
  public static final int[] DisableState = new int[]{-android.R.attr.state_enabled};
  public static final int[] NormalState = new int[]{android.R.attr.state_enabled};

  public static final int[] PressState = new int[]{
    android.R.attr.state_pressed, android.R.attr.state_enabled};
  public static final int[] SelectedState = new int[]{
    android.R.attr.state_selected, android.R.attr.state_enabled};

  /**
   * Produces an alpha tint color of the specified color.
   *
   * @param color the color.
   * @return the alpha color of the specified color.
   */
  public static int createAlphaTint(int color)
  {
    return Color.argb(
      Math.round(Color.alpha(color) * (77.0f / 255.0f)),
      Color.red(color),
      Color.green(color),
      Color.blue(color));
  }

  /**
   * Produces a state drawable based on the given parameters.
   *
   * @param res the Resource instance to inflate the drawables
   * @param normalTint the integer ARGB color of the default_dark tint
   * @return the Drawable instance
   */
  public static Drawable createDrawable(Resources res, int normalTint)
  {
    int alphaTint = createAlphaTint(normalTint);
    return createDrawable(res, normalTint, alphaTint, alphaTint);
  }

  /**
   * Produces a state drawable based on the given parameters.
   *
   * @param res the Resource instance to inflate the drawables
   * @param normalTint the integer ARGB color of the default_dark tint
   * @param selectedTint the integer ARGB color of the selected tint
   * @return the Drawable instance
   */
  public static Drawable createDrawable(Resources res, int normalTint, int selectedTint)
  {
    int alphaTint = createAlphaTint(normalTint);
    return createDrawable(res, normalTint, alphaTint, selectedTint);
  }

  /**
   * Produces a state drawable based on the given parameters.
   *
   * @param res the Resource instance to inflate the drawables
   * @param normalTint the integer ARGB color of the default_dark tint
   * @param selectedTint the integer ARGB color of the selected tint
   * @param pressedTint the integer ARGB color of the pressed tint
   * @return the Drawable instance
   */
  public static Drawable createDrawable(Resources res, int normalTint, int selectedTint,
    int pressedTint)
  {
    StateListDrawable stateDrawable = new StateListDrawable();

    /* Disabled state */
    {
      Drawable drawable = new ColorDrawable(normalTint);
      stateDrawable.addState(DisableState, drawable);
    }

    /* Selected state */
    {
      Drawable drawable = new ColorDrawable(selectedTint);
      stateDrawable.addState(SelectedState, drawable);
    }

    /* Press state */
    {
      Drawable drawable = new ColorDrawable(pressedTint);
      stateDrawable.addState(PressState, drawable);
    }

    /* Normal state */
    {
      Drawable drawable = new ColorDrawable(normalTint);
      stateDrawable.addState(NormalState, drawable);
    }
    return stateDrawable;
  }

  /**
   * Produces a default_dark button state drawable based on the given parameters.
   *
   * @param res the Resource instance to inflate the drawable
   * @param drawableResID the resource ID of the drawable
   * @return the Drawable instance
   */
  public static Drawable createResourceDrawable(Resources res, int drawableResID)
  {

    StateListDrawable stateDrawable = new StateListDrawable();

    /* Disabled state */
    {
      Drawable drawable = res.getDrawable(drawableResID);
      Drawable layerDrawable = new InternalLayerDrawable(new Drawable[]{drawable}, 0, 77);

      stateDrawable.addState(DisableState, layerDrawable);
    }

    /* Press state */
    {
      int pressTint = Color.argb(153, 0, 0, 0);

      Drawable drawable = res.getDrawable(drawableResID);
      Drawable layerDrawable = new InternalLayerDrawable(new Drawable[]{drawable}, pressTint);

      stateDrawable.addState(PressState, layerDrawable);
    }

    /* Normal state */
    {
      Drawable drawable = res.getDrawable(drawableResID);
      Drawable layerDrawable = new InternalLayerDrawable(new Drawable[]{drawable}, 0);

      stateDrawable.addState(NormalState, layerDrawable);
    }
    return stateDrawable;
  }

  /**
   * Produces a state drawable based on the given parameters.
   *
   * @param res the Resource instance to inflate the drawable
   * @param drawableResID the resource ID of the drawable
   * @param pressedDrawableResID the resource ID of the press state drawable
   * @return the Drawable instance
   */
  public static Drawable createResourceDrawable (Resources res, int drawableResID,
    int pressedDrawableResID)
  {

    StateListDrawable stateDrawable = new StateListDrawable();

    /* Disabled state */
    {
      Drawable drawable = res.getDrawable(drawableResID);
      Drawable layerDrawable = new InternalLayerDrawable(new Drawable[]{drawable}, 0, 77);

      stateDrawable.addState(DisableState, layerDrawable);
    }

    /* Press state */
    {
      Drawable drawable = res.getDrawable(pressedDrawableResID);
      stateDrawable.addState(PressState, drawable);
    }

    /* Normal state */
    {
      Drawable drawable = res.getDrawable(drawableResID);
      stateDrawable.addState(NormalState, drawable);
    }
    return stateDrawable;
  }

  /**
   * Produces a default_dark button state drawable based on the given parameters.
   *
   * @param res the Resource instance to inflate the drawable
   * @param drawableResID the resource ID of the drawable
   * @return the Drawable instance
   */
  public static Drawable createTintedResourceDrawable(Resources res, int drawableResID,
                                                      int tint)
  {

    StateListDrawable stateDrawable = new StateListDrawable();

    /* Disabled state */
    {
      Drawable drawable = res.getDrawable(drawableResID);
      Drawable layerDrawable = new InternalLayerDrawable(new Drawable[]{drawable}, 0, 77);

      stateDrawable.addState(DisableState, layerDrawable);
    }

    /* Press state */
    {
      Drawable drawable = res.getDrawable(drawableResID);
      Drawable layerDrawable = new InternalLayerDrawable(new Drawable[]{drawable}, tint);

      stateDrawable.addState(PressState, layerDrawable);
    }

    /* Normal state */
    {
      Drawable drawable = res.getDrawable(drawableResID);
      Drawable layerDrawable = new InternalLayerDrawable(new Drawable[]{drawable}, 0);

      stateDrawable.addState(NormalState, layerDrawable);
    }
    return stateDrawable;
  }

  /**
   * Produces a default_dark color state list based on the given parameters.
   *
   * @param color the integer ARGB color of the color tint
   * @return the {@link ColorStateList} instance
   */
  public static ColorStateList createColorStateList(int color)
  {
    /* Disable color state */
    int disableColor = Color.argb(
      77,
      Color.red(color),
      Color.green(color),
      Color.blue(color));

    /* Press color state */
    float[] hsv = new float[3];
    Color.colorToHSV(color, hsv);
    hsv[2] *= 0.4f;

    int pressedColor = Color.HSVToColor(hsv);

    int[] colors = new int[]{disableColor, pressedColor, color};
    int[][] states = new int[][]{DisableState, PressState, NormalState};

    return new ColorStateList(states, colors);
  }

  /**
   * Produces a default_dark color state list based on the given parameters.
   *
   * @param normalColor the integer ARGB color of the color tint
   * @param pressedColor the integer ARGB color of the pressed tint
   * @param disableColor the inter ARFB color of the disabled tint
   * @return the {@link ColorStateList} instance
   */
  public static ColorStateList createColorStateList(int normalColor, int pressedColor,
    int disableColor)
  {
    int[] colors = new int[]{disableColor, pressedColor, pressedColor, normalColor};
    int[][] states = new int[][]{DisableState, PressState, SelectedState, NormalState};

    return new ColorStateList(states, colors);
  }

  /**
   * Produces a default_dark button state drawable based on the given parameters.
   *
   * @param res the Resource instance to inflate the drawable
   * @param bitmap the resource bitmap
   * @return the Drawable instance
   */
  public static final Drawable createBitmapDrawable(Resources res, Bitmap bitmap)
  {
    StateListDrawable stateDrawable = new StateListDrawable();
    Bitmap newBitmap = Bitmap.createBitmap(bitmap);

    /* Disabled state */
    {
      BitmapDrawable drawable = new BitmapDrawable(res, newBitmap);
      Drawable layerDrawable = new InternalLayerDrawable(new Drawable[]{drawable}, 0, 77);

      stateDrawable.addState(DisableState, layerDrawable);
    }

    /* Press state */
    {
      int pressTint = Color.argb(153, 0, 0, 0);

      Drawable drawable = new BitmapDrawable(res, newBitmap);
      Drawable layerDrawable = new InternalLayerDrawable(new Drawable[]{drawable}, pressTint);

      stateDrawable.addState(PressState, layerDrawable);
    }

    /* Normal state */
    {
      Drawable drawable = new BitmapDrawable(res, newBitmap);
      stateDrawable.addState(NormalState, drawable);
    }
    return stateDrawable;
  }

  /**
   * Produces a default_dark button state drawable based on the given parameters.
   *
   * @param res the Resource instance to inflate the drawable
   * @param bitmap the resource bitmap
   * @param selectedBitmap the resource selected bitmap
   * @return the Drawable instance
   */
  public static Drawable createBitmapDrawable(Resources res, Bitmap bitmap, Bitmap selectedBitmap)
  {
    StateListDrawable stateDrawable = new StateListDrawable();
    Bitmap newBitmap = Bitmap.createBitmap(bitmap);
    Bitmap selectedNewBitmap = Bitmap.createBitmap(selectedBitmap);

    /* Disabled state */
    {
      BitmapDrawable drawable = new BitmapDrawable(res, newBitmap);
      Drawable layerDrawable = new InternalLayerDrawable(new Drawable[]{drawable}, 0, 77);

      stateDrawable.addState(DisableState, layerDrawable);
    }

    /* Press state */
    {
      Drawable selectedDrawable = new BitmapDrawable(res, selectedNewBitmap);
      stateDrawable.addState(PressState, selectedDrawable);
    }

    /* Normal state */
    {
      Drawable drawable = new BitmapDrawable(res, newBitmap);
      stateDrawable.addState(NormalState, drawable);
    }
    return stateDrawable;
  }

  /**
   * Produces a determinate progress bar drawable based on the given parameters.
   *
   * @param res the Resource instance to inflate the drawables
   * @param bgResID the resource ID of the background drawable
   * @param overlayResID the resource ID of the overlay drawable, scales accordingly
   * @param maskResID the resource ID of the mask drawable, scales accordingly
   * @param maskTint the integer ARGB color of the tint
   * @return the Drawable instance
   */
  public static final Drawable createProgressBarDrawable(Resources res,
    int bgResID, int overlayResID, int maskResID, int maskTint)
  {
    Drawable[] layers = new Drawable[2];

    /* Background layer */
    {
      layers[0] = res.getDrawable(bgResID);
    }

    /* Progress layer */
    {
      Drawable overlayDrawable = res.getDrawable(overlayResID);
      Drawable maskDrawable = res.getDrawable(maskResID);

      Drawable layerDrawable = new InternalLayerDrawable(new Drawable[]{maskDrawable,
          overlayDrawable}, maskTint);

      layers[1] = new ScaleDrawable(layerDrawable,
        Gravity.LEFT | Gravity.FILL_VERTICAL, 1.0f, 0.0f);
    }

    LayerDrawable progressDrawable = new LayerDrawable(layers);
    progressDrawable.setId(0, android.R.id.background);
    progressDrawable.setId(1, android.R.id.progress);
    return progressDrawable;
  }

  /**
   * Produces a indeterminate progress bar drawable based on the given
   * parameters.
   *
   * @param res the Resource instance to inflate the drawables
   * @param bgResID the resource ID of the background drawable
   * @param clipResID the resource ID of the drawable that clips the tiled drawable,
   * scales accordingly
   * @param tileResID the resource ID of the tiled drawable, scales accordingly
   * @param tileTint the integer ARGB color of the tint of the tiled drawable
   * @return the Drawable instance
   */
  public static final Drawable createForeverBarDrawable(Resources res,
    int bgResID, int clipResID, int tileResID, int tileTint)
  {
    Drawable[] layers = new Drawable[2];

    /* Background layer */
    {
      layers[0] = res.getDrawable(bgResID);
    }

    /* Progress layer */
    {
      Drawable clipDrawable = res.getDrawable(clipResID);

      Drawable overlayDrawable = res.getDrawable(tileResID);
      overlayDrawable.setColorFilter(tileTint, PorterDuff.Mode.SRC_ATOP);
      overlayDrawable.setAlpha(230);
      if (overlayDrawable instanceof BitmapDrawable)
      {
        ((BitmapDrawable) overlayDrawable).setTileModeXY(
          Shader.TileMode.REPEAT,
          Shader.TileMode.REPEAT);
      }

      layers[1] = new ForeverDrawable(clipDrawable, overlayDrawable);
    }

    LayerDrawable progressDrawable = new LayerDrawable(layers);
    progressDrawable.setId(0, android.R.id.background);
    progressDrawable.setId(1, android.R.id.progress);
    return progressDrawable;
  }

  /* InternalLayerDrawable definitions */

  /**
   * Internal class that extends LayerDrawable and hacks setting of color
   * filter and alpha.
   */
  private static class InternalLayerDrawable extends LayerDrawable
  {
    private int m_maskTint;
    private int m_alpha = 255;

    /**
     * Constructor.
     *
     * @param layers array of Drawable instances (first drawable is the mask)
     * @param maskTint integer value of tint color
     */
    public InternalLayerDrawable(Drawable[] layers, int maskTint)
    {
      super(layers);
      m_maskTint = maskTint;
    }

    /**
     * Constructor.
     *
     * @param layers array of Drawable instances (first drawable is the mask)
     * @param maskTint integer value of tint color
     * @param alpha integer value of alpha
     */
    public InternalLayerDrawable(Drawable[] layers, int maskTint, int alpha)
    {
      super(layers);
      m_maskTint = maskTint;
      m_alpha = alpha;
    }

    @Override
    public void draw(Canvas canvas)
    {
      int count = getNumberOfLayers();
      for (int i = 0; i < count; i++)
      {
        Drawable drawable = getDrawable(i);
        if (i == 0)
        {
          drawable.setColorFilter(m_maskTint, PorterDuff.Mode.SRC_ATOP);
        }
        drawable.draw(canvas);
      }
    }

    @Override
    public void setAlpha(int alpha)
    {
      /* Apply a different alpha to child drawables. */
      super.setAlpha(Math.round(alpha * (m_alpha / 255.0f)));
    }
  }

  /* ForeverDrawable definition */

  /**
   * Internal class that mimics a barber pole animation depending on the current
   * level of the drawable state.
   */
  private static class ForeverDrawable extends Drawable
  {
    private static final float MAX_LEVEL = 10000;

    private Drawable m_tileDrawable;
    private Drawable m_clipDrawable;

    private Bitmap m_tileBitmap;
    private Canvas m_tileCanvas;

    private Bitmap m_clipBitmap;
    private Paint m_clipPaint;

    /**
     * Constructor.
     *
     * @param clipDrawable the Drawable instance that clips the tiled drawable
     * @param tileDrawable the Drawable instance that is tiled and moved
     */
    public ForeverDrawable(Drawable clipDrawable, Drawable tileDrawable)
    {
      m_clipDrawable = clipDrawable;
      m_tileDrawable = tileDrawable;

      Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
      paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
      m_clipPaint = paint;
    }

    @Override
    public void draw(Canvas canvas)
    {
      Rect rect = getBounds();
      int width = rect.width();
      int height = rect.height();

      /* Lazy load tile canvas and bitmap. */
      Canvas tileCanvas = m_tileCanvas;
      Bitmap tileBitmap = m_tileBitmap;
      if (tileBitmap == null
        || tileBitmap.getWidth() != width
        || tileBitmap.getHeight() != height)
      {
        if (tileBitmap != null)
        {
          tileBitmap.recycle();
        }

        tileBitmap =
          Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        m_tileBitmap = tileBitmap;

        tileCanvas = new Canvas(tileBitmap);
        m_tileCanvas = tileCanvas;
      }

      /* Draw tile. */

      tileCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

      Drawable tile = m_tileDrawable;
      int delta = tile.getIntrinsicWidth();
      int diff = Math.round(delta * (getLevel() / MAX_LEVEL));

      tileCanvas.save();
      tileCanvas.translate(diff - delta, 0);
      tile.setBounds(0, 0, width + delta, height);
      tile.draw(tileCanvas);
      tileCanvas.restore();

      /* Lazy load clip bitmap. */
      Bitmap clipBitmap = m_clipBitmap;
      if (   clipBitmap == null
          || clipBitmap.getWidth() != width
          || clipBitmap.getHeight() != height)
      {
        if (clipBitmap != null)
        {
          clipBitmap.recycle();
        }

        clipBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas clipCanvas = new Canvas(clipBitmap);
        clipCanvas.drawARGB(0, 0, 0, 0);

        m_clipDrawable.setBounds(0, 0, width, height);
        m_clipDrawable.draw(clipCanvas);

        m_clipBitmap = clipBitmap;
      }

      /* Draw clip. */
      tileCanvas.drawBitmap(clipBitmap, 0, 0, m_clipPaint);

      /* Draw to actual canvas. */
      canvas.drawBitmap(tileBitmap, 0, 0, null);
    }

    @Override
    protected boolean onLevelChange(int level)
    {
      m_tileDrawable.setLevel(level);
      m_clipDrawable.setLevel(level);
      invalidateSelf();
      return true;
    }

    @Override
    public int getOpacity()
    {
      return PixelFormat.TRANSPARENT;
    }

    @Override
    public void setAlpha(int alpha)
    {
      /* Do nothing */
    }

    @Override
    public void setColorFilter(ColorFilter cf)
    {
      /* Do nothing */
    }
  }
}
