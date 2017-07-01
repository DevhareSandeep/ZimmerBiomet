package sg.totalebizsolutions.genie.misc.widgets;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class LinearDividerItemDecoration extends RecyclerView.ItemDecoration
{
  /* Properties */

  private Paint m_paint;

  /* Initializations */

  public LinearDividerItemDecoration ()
  {
    this(0xE1E1E1, 1.0f);
  }

  public LinearDividerItemDecoration (int dividerColor, float dividerWidth)
  {
    m_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    m_paint.setStyle(Paint.Style.STROKE);
    m_paint.setColor(dividerColor);
    m_paint.setStrokeWidth(dividerWidth);
  }

  /* Item decorator methods */

  @Override
  public void onDraw (Canvas c, RecyclerView parent, RecyclerView.State state)
  {
    int childCount = parent.getChildCount();
    if (childCount <= 1)
    {
      return;
    }

    final float offset = m_paint.getStrokeWidth() / 2.0f;
    for (int i = 0; i < childCount; i++)
    {
      View child = parent.getChildAt(i);
      c.drawLine(0, child.getBottom() + offset, parent.getWidth(), child.getBottom() + offset,
        m_paint);
    }
  }

  @Override
  public void getItemOffsets (Rect outRect, View view, RecyclerView parent,
    RecyclerView.State state)
  {
    outRect.bottom = (int) m_paint.getStrokeWidth();
  }

  /* Property methods */

  /**
   * Sets divider color.
   */
  public void setDividerColor (int color)
  {
    m_paint.setColor(color);
  }

  /**
   * Sets divider width.
   */
  public void setDividerWidth (float width)
  {
    m_paint.setStrokeWidth(width);
  }
}
