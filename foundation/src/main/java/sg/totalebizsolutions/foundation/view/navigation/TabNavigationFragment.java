package sg.totalebizsolutions.foundation.view.navigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import sg.totalebizsolutions.foundation.R;

public abstract class TabNavigationFragment extends NavigationFragment
{
  /* Properties */

  private static final String STATE_STACK = "tabStack";
  private static final String STATE_SELECTED_TAB = "selectedTab";

  private TabAdapter m_adapter;

  private int m_selectedTab;
  private List<Stack<String>> m_tabStack = null;

  /* Life-cycle callback. */

  @Override
  public void onViewCreated (View view, Bundle savedInstanceState)
  {
    super.onViewCreated(view, savedInstanceState);
    if (m_adapter != null)
    {
      setTabAdapter(m_adapter);
    }
    switchTab(m_selectedTab);
  }

  @Override
  public void onViewStateRestored (@Nullable Bundle savedInstanceState)
  {
    super.onViewStateRestored(savedInstanceState);

    if (savedInstanceState != null)
    {
      m_selectedTab = savedInstanceState.getInt(STATE_SELECTED_TAB, 0);

      String stack = savedInstanceState.getString(STATE_STACK);
      if (stack != null)
      {
        String[] stacks = stack.split(",");
        int i = 0;
        for (String s : stacks)
        {
          String[] stackTags = s.split(";");
          m_tabStack.get(i).addAll(Arrays.asList(stackTags));
          i++;
        }
      }
    }
  }

  @Override
  public void onSaveInstanceState (Bundle outState)
  {
    /* Save selected tab. */
    outState.putInt(STATE_SELECTED_TAB, m_selectedTab);

    /* Save fragment tag stack. */

    StringBuilder builder = new StringBuilder();

    for (Stack<String> stack : m_tabStack)
    {
      int size = stack.size();
      for (int i = 0; i < stack.size(); i++)
      {
        String tag = stack.get(i);

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
    outState.putString(STATE_STACK, builder.toString());

    super.onSaveInstanceState(outState);
  }

  public void setTabAdapter (TabAdapter adapter)
  {
    m_adapter = adapter;
    if (getView() == null)
    {
      return;
    }

    initTabStack();
    setupTabView();
  }

  /* Internal methods */

  private void initTabStack ()
  {
    if (m_tabStack == null)
    {
      int count = getItemCount();
      m_tabStack = new ArrayList<>(count);
      for (int i = 0; i < count; i++)
      {
        m_tabStack.add(new Stack<String>());
      }
    }
  }

  private int getItemCount ()
  {
    return m_adapter != null ? m_adapter.getItemCount() : 0;
  }

  private void setupTabView ()
  {
    View tabContentView;
    View view = getView();
    if (    view == null
        || (tabContentView = view.findViewById(R.id.tab_container)) == null)
    {
      return;
    }
    else if (!(tabContentView instanceof LinearLayout))
    {
      throw new RuntimeException("Tab container must be an instance of LinearLayout.");
    }

    int count = getItemCount();
    for (int i = 0; i < count; i++)
    {
      View itemView = m_adapter.getView(i, (ViewGroup) tabContentView);
      itemView.setEnabled(m_selectedTab != i);
      itemView.setTag(i);

      View.OnClickListener onClickListener = new View.OnClickListener()
      {
        @Override
        public void onClick (View v)
        {
          switchTab((int) v.getTag());
        }
      };
      itemView.setOnClickListener(onClickListener);

      LinearLayout.LayoutParams params =
          new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
      ((ViewGroup) tabContentView).addView(itemView, params);
    }
  }

  /**
   * Callback method that will be called when a tab has been selected.
   */
  protected void onTabSelected (int index)
  {
  }

  private void switchTab (int index)
  {
    if (   getItemCount() <= 0
        || getView() == null)
    {
      return;
    }

    FragmentManager manager = getChildFragmentManager();
    FragmentTransaction trx = manager.beginTransaction();

    /* Retrieve current fragment instance and hide if able. */
    Fragment curFragment = getCurrentFragment();
    if (curFragment != null)
    {
      trx.detach(curFragment);
    }

    m_selectedTab = index;

    Stack<String> stack = m_tabStack.get(index);
    if (stack.isEmpty())
    {
      Fragment fragment = m_adapter.getFragment(index);

      String tag = generateDefaultTagForIndex(0);

      trx.replace(R.id.content, fragment, tag);
      stack.push(tag);
    }
    else
    {
      String tag = stack.get(stack.size() - 1);
      Fragment fragment = retrieveFragmentByTag(tag);
      trx.attach(fragment);
    }

    trx.commit();

    /* Disable selected tab. */
    ViewGroup tabContentView = (ViewGroup) getView().findViewById(R.id.tab_container);
    int childCount = tabContentView.getChildCount();
    for (int i = 0; i < childCount; i++)
    {
      boolean selected = i == m_selectedTab;
      View view = tabContentView.findViewWithTag(i);
      view.setEnabled(!selected);
      view.setSelected(selected);
    }

    onTabSelected(index);
  }

  @Override
  protected Stack<String> getStack ()
  {
    int itemCount = getItemCount();
    return itemCount <= 0 ? new Stack<String>() : m_tabStack.get(m_selectedTab);
  }

  /**
   * Returns the current fragment.
   */
  protected Fragment getCurrentFragment ()
  {
    Fragment fragment = null;
    Stack<String> tagBackStack = getStack();
    if (!tagBackStack.isEmpty())
    {
      String topTag = tagBackStack.get(tagBackStack.size() - 1);
      fragment = retrieveFragmentByTag(topTag);
    }
    return fragment;
  }

  /* Navigation methods */

  /**
   * Returns a default string fragment tag for the specified index from the
   * stack.
   */
  protected String generateDefaultTagForIndex (int stackIndex)
  {
    return String.format(Locale.US, "s%d-%d", m_selectedTab, stackIndex);
  }

  public static abstract class TabAdapter
  {
    /**
     * Returns the total number of tab items.
     */
    public abstract int getItemCount ();

    /**
     * Returns tab title for the specified index;
     */
    public abstract String getTitle (int index);

    /**
     * Get the fragment that will be displayed initially for the specified position.
     */
    public abstract Fragment getFragment (int position);

    /**
     * Get the view that displays the item for the specified position.
     */
    public abstract View getView (int position, ViewGroup parent);
  }
}
