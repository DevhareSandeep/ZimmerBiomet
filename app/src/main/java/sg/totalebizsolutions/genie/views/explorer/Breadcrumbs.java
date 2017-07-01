package sg.totalebizsolutions.genie.views.explorer;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sg.totalebizsolutions.genie.R;
import sg.totalebizsolutions.genie.databinding.BreadcrumbsBinding;
import sg.totalebizsolutions.genie.util.TypefaceFactory;

public class Breadcrumbs extends RelativeLayout
{
  /* Properties */

  private String m_category;
  private int m_currStackIndex = -1;

  private BreadcrumbListener m_listener;

  private BreadcrumbsBinding m_binding;

  private List<TextView> m_viewStacks = new ArrayList<>();
  private SparseArray<List<String>> m_tabStacks = new SparseArray<>();

  /* Initializations */

  public Breadcrumbs (Context context)
  {
    super(context);
    initView(context);
  }

  public Breadcrumbs (Context context, AttributeSet attrs)
  {
    super(context, attrs);
    initView(context);
  }

  public Breadcrumbs (Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    initView(context);
  }

  private void initView (Context context)
  {
    setSaveEnabled(true);
    setClipChildren(false);

    LayoutInflater inflater = LayoutInflater.from(context);
    BreadcrumbsBinding binding =
        DataBindingUtil.inflate(inflater, R.layout.breadcrumbs, this, true);
    m_binding = binding;

    binding.homeImageView.setOnClickListener(this::informHomeIsClicked);

    binding.categoryTextView.setTag(-1);
    binding.categoryTextView.setOnClickListener(this::informBreadcrumbClicked);
  }

  /* Life-cycle methods */

  @Override
  protected Parcelable onSaveInstanceState ()
  {
    Parcelable superState = super.onSaveInstanceState();
    SavedState ss = new SavedState(superState);
    ss.tabStack = m_tabStacks;
    return ss;
  }

  @Override
  protected void onRestoreInstanceState (Parcelable state)
  {
    SavedState ss = (SavedState) state;
    super.onRestoreInstanceState(ss.getSuperState());
    m_tabStacks = ss.tabStack;
    updateViewStack(m_currStackIndex);
  }

  /* Property methods */

  /**
   * Retrieve stack.
   */
  public List<String> getStack ()
  {
    List<String> stack = new ArrayList<>(getStack(m_currStackIndex));
    stack.add(0, m_category);
    return stack;
  }

  /**
   * Sets breadcrumb listener instance.
   */
  public void setBreadcrumbListener (BreadcrumbListener listener)
  {
    m_listener = listener;
  }

  /**
   * Set category title.
   */
  public void setCategory (String categoryTitle, int backgroundResID)
  {
    m_category = categoryTitle;
    m_binding.categoryTextView.setText(categoryTitle);
    m_binding.categoryTextView.setBackgroundResource(backgroundResID);
  }

  public void setStackIndex (int stackIndex)
  {
    final boolean update = stackIndex != m_currStackIndex;
    m_currStackIndex = stackIndex;

    if (update)
    {
      updateViewStack(stackIndex);
    }
  }

  private void updateViewStack (int stackIndex)
  {
    final List<String> stack = getStack(stackIndex);
    final List<TextView> viewStack = m_viewStacks;

    final int stackSize = stack.size();
    final int viewStackSize = viewStack.size();

    for (int i = 0; i < stackSize; i++)
    {
      final String text = stack.get(i);

      if (i < viewStackSize)
      {
        TextView textView = viewStack.get(i);
        textView.setText(text);
      }
      else
      {
        appendBreadcrumb(stackIndex, i, text);
      }
    }

      /* Remove excess view. */
    if (stackSize < viewStackSize)
    {
      for (int i = viewStackSize - 1; i >= stackSize; i--)
      {
        View viewToRemove = m_viewStacks.remove(i);
        m_binding.container.removeView(viewToRemove);
      }
    }
  }

  /**
   * Push breadcrump to stack.
   */
  public void push (String text)
  {
    final int index = getStackSize(m_currStackIndex);
    getStack(m_currStackIndex).add(text);
    appendBreadcrumb(m_currStackIndex, index, text);
  }

  /**
   * Pops top breadcrump from stack.
   */
  public void pop ()
  {
    final int size = getStackSize(m_currStackIndex);
    final int indexToRemove = size - 1;
    removeBreadcrumb(m_currStackIndex, indexToRemove);
  }

  /* Internal methods */

  private List<String> getStack (int index)
  {
    List<String> stack = m_tabStacks.get(index);
    if (stack == null)
    {
      m_tabStacks.put(index, stack = new ArrayList<>());
    }
    return stack;
  }

  /**
   * Get stack size for the specified stack index.
   */
  private int getStackSize (int stackIndex)
  {
    return getStack(stackIndex).size();
  }

  /**
   * Append breadcrumb.
   */
  private TextView appendBreadcrumb (int stackIndex, int index, String text)
  {
    final Context context = getContext();

    TextView textView = new TextView(context);
    textView.setTag(index);
    textView.setText(text);
    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12.0f);
    textView.setTypeface(TypefaceFactory.getTypeFaceForId(context, TypefaceFactory.RobotoRegular));
    textView.setBackgroundResource(R.drawable.breadcrumbs_item_bg);
    textView.setOnClickListener(this::informBreadcrumbClicked);
    textView.setGravity(Gravity.CENTER);

    float dpi = getResources().getDisplayMetrics().density;
    int leftMargin = (int) -(18 * dpi);

    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.MATCH_PARENT);
    params.leftMargin = leftMargin;

    textView.setMinWidth((int) (90 * dpi));

    m_binding.container.addView(textView, -1, params);

    /* Append to view stack. */
    m_viewStacks.add(textView);

    /* Scroll to view. */
    m_binding.horizontalScrollView.post(() ->
    {
      m_binding.horizontalScrollView.smoothScrollTo(textView.getRight(), 0);
    });

    return textView;
  }

  /**
   * Remove breadcrumb from specified index to stack.
   */
  private int removeBreadcrumb (int stackIndex, int fromIndex)
  {
    int stackSize = getStackSize(stackIndex);
    /* Sanity checking. */
    if (   fromIndex < 0
        || fromIndex >= stackSize)
    {
      return 0;
    }

    final List<String> stack = getStack(stackIndex);

    for (int i = stackSize - 1; i >= fromIndex; i--)
    {
      stack.remove(i);
      View viewToRemove = m_viewStacks.remove(i);
      m_binding.container.removeView(viewToRemove);
    }
    return stackSize - fromIndex;
  }

  private void informHomeIsClicked (View v)
  {
    if (m_listener != null)
    {
      m_listener.onHomeClicked();
    }
  }

  private void informBreadcrumbClicked (View view)
  {
    int index = (int) view.getTag();
    if (m_listener != null)
    {
      m_listener.onBreadcrumbClicked(index);
    }
  }

  interface BreadcrumbListener
  {
    /**
     * Called when home is clicked.
     */
    void onHomeClicked();

    /**
     * Called when a no. of breadcrumbs are removed from stack.
     */
    void onBreadcrumbClicked(int index);
  }

  private static class SavedState extends BaseSavedState
  {
    SparseArray<List<String>> tabStack = new SparseArray<>();

    SavedState (Parcelable superState)
    {
      super(superState);
    }

    private SavedState (Parcel in)
    {
      super(in);
      String value = in.readString();
      if (value != null)
      {
        String[] stacks = value.split(",");
        int i = 0;
        for (String s : stacks)
        {
          String[] stackTags = s.split(";");
          tabStack.put(i, Arrays.asList(stackTags));
          i++;
        }
      }
    }

    @Override
    public void writeToParcel (Parcel out, int flags)
    {
      super.writeToParcel(out, flags);

      StringBuilder builder = new StringBuilder();

      for (int tab = 0; tab < tabStack.size(); tab++)
      {
        List<String> stack = tabStack.valueAt(tab);

        int size = stack.size();
        for (int index = 0; index < stack.size(); index++)
        {
          String tag = stack.get(index);

          builder.append(tag);
          builder.append(";");
        }
        if (size > 0)
        {
          builder.deleteCharAt(builder.length() - 1);
        }

        builder.append(",");
      }

      if (builder.length() > 0)
      {
        builder.deleteCharAt(builder.length() - 1);
      }
      out.writeString(builder.toString());
    }

    public static final Creator<SavedState> CREATOR
        = new Creator<SavedState>()
    {
      public SavedState createFromParcel (Parcel in)
      {
        return new SavedState(in);
      }

      public SavedState[] newArray (int size)
      {
        return new SavedState[size];
      }
    };
  }
}
