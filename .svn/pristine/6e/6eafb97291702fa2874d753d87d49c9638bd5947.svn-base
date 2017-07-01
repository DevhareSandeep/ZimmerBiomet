package sg.totalebizsolutions.foundation.view.navigation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import sg.totalebizsolutions.foundation.view.BaseFragment;

public class NavigationItemFragment extends BaseFragment
{
  /* Constants */

  private static final String BundleStateIsRoot = "isRoot";

  /* Properties */

  private boolean m_isRoot;

  private NavigationBarHandler m_navBarHandler;
  private NavigationHandler m_navigationHandler;

  /* Fragment life-cycle methods */

  @Override
  public void onCreate (Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    if (savedInstanceState != null)
    {
      m_isRoot = savedInstanceState.getBoolean(BundleStateIsRoot, false);
    }
  }

  @Override
  public void onAttach (Context context)
  {
    super.onAttach(context);

    Fragment parentFragment = getParentFragment();
    if (   parentFragment != null
        && parentFragment instanceof NavigationHandler)
    {
      m_navigationHandler = (NavigationHandler) parentFragment;
    }

    if (   parentFragment != null
        && parentFragment instanceof NavigationBarHandler)
    {
      m_navBarHandler = (NavigationBarHandler) parentFragment;
    }
  }

  @Override
  public void onSaveInstanceState (Bundle outState)
  {
    super.onSaveInstanceState(outState);
    outState.putBoolean(BundleStateIsRoot, m_isRoot);
  }

  /* Navigation methods */

  /**
   * Contract method between {@link NavigationFragment} that defines whether
   * this is a root fragment.
   */
  void setIsRoot (boolean isRoot)
  {
    m_isRoot = isRoot;
  }

  /**
   * Whether this is a root fragment from the navigation stack.
   */
  protected boolean isRoot ()
  {
    return m_isRoot;
  }

  /**
   * Retrieves Toolbar instance.
   */
  public Toolbar getToolbar ()
  {
    return m_navBarHandler != null ? m_navBarHandler.getToolbar() : null;
  }

  /**
   * See {@link #setRootFragment(Fragment, String)}.
   */
  protected void setRootFragment (Fragment fragment)
  {
    setRootFragment(fragment, null);
  }

  /**
   * Replaces the current fragment and clears fragment stack.
   *
   * @param fragment the {@link Fragment} to attach
   * @param tag the String fragment tag. Later can be used to retrieve
   * fragment instance
   */
  protected void setRootFragment (Fragment fragment, String tag)
  {
    NavigationHandler navigationHandler = m_navigationHandler;
    if (navigationHandler != null)
    {
      navigationHandler.setRootFragment(fragment, tag);
    }
  }

  /**
   * See {@link #pushFragmentToNavigation(Fragment, String, boolean)};
   */
  protected void pushFragmentToNavigation (Fragment fragment)
  {
    pushFragmentToNavigation(fragment, null, true);
  }

  /**
   * Pushes a {@link Fragment} to the navigation stack.
   *
   * @param fragment the {@link Fragment} to push into
   * the navigation stack
   * @param tag the String fragment tag (Optional). Later to be used to retrieve
   * fragment instance
   * @param animated true if animated, otherwise, false
   */
  protected void pushFragmentToNavigation (Fragment fragment, String tag, boolean animated)
  {
    NavigationHandler navigationHandler = m_navigationHandler;
    if (navigationHandler != null)
    {
      navigationHandler.pushFragmentToNavigation(fragment, tag, animated);
    }
  }

  /**
   * See {@link #popFragmentFromNavigation(boolean)}.
   */
  protected void popFragmentFromNavigation ()
  {
    popFragmentFromNavigation(true);
  }

  /**
   * Pop the {@link Fragment} instance from the
   * navigation stack.
   *
   * @param animated true if animated, otherwise, false
   */
  protected void popFragmentFromNavigation (boolean animated)
  {
    NavigationHandler navigationHandler = m_navigationHandler;
    if (navigationHandler != null)
    {
      navigationHandler.popFragmentFromNavigation(animated);
    }
  }
}