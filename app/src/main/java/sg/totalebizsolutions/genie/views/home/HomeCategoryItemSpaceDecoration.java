package sg.totalebizsolutions.genie.views.home;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class HomeCategoryItemSpaceDecoration extends RecyclerView.ItemDecoration
{
  @Override
  public void getItemOffsets (Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state)
  {
    final float dpi = parent.getResources().getDisplayMetrics().density;
    final int spacing = (int) (dpi * 4);
    outRect.left = spacing;
    outRect.top = spacing;
    outRect.right = spacing;
    outRect.bottom = spacing;
  }
}
