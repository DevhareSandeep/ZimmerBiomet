package sg.totalebizsolutions.foundation.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.List;

/**
 * Defines a fragment that supports having child view controllers and
 * propagation of life-cycle triggers.
 */
public class BaseControllerFragment extends Fragment
{
  /* Properties */

  private RootFNViewController m_rootViewController =
      new RootFNViewController();

  /* Fragment life-cycle methods */

  @Override
  public void onCreate (Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    List<ViewController> childControllers = onCreateChildViewControllers
        (getActivity());
    m_rootViewController.setChildViewControllers(childControllers);
    m_rootViewController.onCreate(getActivity());
  }

  @Override
  public void onViewCreated (View view, Bundle savedInstanceState)
  {
    m_rootViewController.onCreateView(view);
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void onResume ()
  {
    super.onResume();
    m_rootViewController.onResume();
  }

  @Override
  public void onPause ()
  {
    m_rootViewController.onPause();
    super.onPause();
  }

  @Override
  public void onLowMemory ()
  {
    m_rootViewController.onLowMemory();
    super.onLowMemory();
  }

  @Override
  public void onDestroyView ()
  {
    m_rootViewController.onDestroyView();
    super.onDestroyView();
  }

  @Override
  public void onDestroy ()
  {
    m_rootViewController.onDestroy();
    super.onDestroy();
  }

  /* ViewController life-cycle methods */

  /**
   * Life-cycle method that gets called to initialize child view controllers.
   */
  public List<ViewController> onCreateChildViewControllers (Activity activity)
  {
    return null;
  }

  /* Controller methods */

  /**
   * Returns the list of child ViewController instances that were previously
   * added on this fragment.
   */
  public List<ViewController> getChildControllers ()
  {
    return m_rootViewController.getChildControllers();
  }
}
