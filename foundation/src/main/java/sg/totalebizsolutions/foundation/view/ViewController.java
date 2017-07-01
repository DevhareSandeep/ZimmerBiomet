package sg.totalebizsolutions.foundation.view;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An abstract class that supports the child controller handling, adheres to
 * common android Fragment life-cycle triggers and propagates life-cycle
 * triggers to their sub-controllers. This class is used to segregate
 * sub-implementations of activities and fragments. It is also important to note
 * that you can only manipulate the child controller list on its internal
 * dormant state (before onCreate, after onDestroy).
 */
public abstract class ViewController
{
  /* Properties */

  private Handler m_handler;
  private Activity m_activity;
  private LifeCycleState m_state = LifeCycleState.OnDestroy;
  private List<ViewController> m_childControllers = new ArrayList<>();

  /* Property methods */

  /**
   * Returns a reference to a Activity instance, or null if this view controller
   * is not bound to one.
   */
  protected Activity getActivity ()
  {
    return m_activity;
  }

  /**
   * Returns a reference to a Resources instance, or null if this view
   * controller is not bound to a Activity instance.
   */
  protected Resources getResources ()
  {
    return m_activity != null ? m_activity.getResources() : null;
  }

  /* Life-cycle methods */

  /**
   * Life-cycle method that gets triggered on activity / fragment onCreate().
   */
  protected void onCreate (Activity activity)
  {
    m_activity = activity;

    /* Trigger create child view controller callback. */
    createChildViewControllers(activity);

    LifeCycleState state = LifeCycleState.OnCreate;
    List<ViewController> controllers = m_childControllers;
    for (ViewController controller : controllers)
    {
      controller.onCreate(activity);
      if (!state.equals(controller.m_state))
      {
        throw createStateException(controller, state);
      }
    }
    m_state = state;

    m_handler = new Handler();
  }

  /**
   * Internal method to rigger {@link #onCreateViewChildControllers
   * (Activity)} method.
   */
  private void createChildViewControllers (Activity activity)
  {
    List<ViewController> childControllers =
        onCreateViewChildControllers(activity);
    if (    childControllers != null
        && !childControllers.isEmpty())
    {
      m_childControllers.addAll(childControllers);
    }
  }

  /**
   * Life-cycle method that gets called to initialize child view controllers.
   */
  protected List<ViewController> onCreateViewChildControllers (
      Activity activity)
  {
    return null;
  }

  /**
   * Life-cycle method that gets triggered on activity / fragment onCreate() /
   * onCreateView().
   */
  protected void onCreateView (View view)
  {
    LifeCycleState state = LifeCycleState.OnCreateView;
    List<ViewController> controllers = m_childControllers;
    for (ViewController controller : controllers)
    {
      controller.onCreateView(view);
      if (!state.equals(controller.m_state))
      {
        throw createStateException(controller, state);
      }
    }
    m_state = state;
  }

  /**
   * Life-cycle method that gets triggered on activity / fragment onResume().
   */
  protected void onResume ()
  {
    LifeCycleState state = LifeCycleState.OnResume;
    List<ViewController> controllers = m_childControllers;
    for (ViewController controller : controllers)
    {
      controller.onResume();
      if (!state.equals(controller.m_state))
      {
        throw createStateException(controller, state);
      }
    }
    m_state = state;
  }

  /**
   * Life-cycle method that gets triggered on activity / fragment onPause().
   */
  protected void onPause ()
  {
    LifeCycleState state = LifeCycleState.OnPause;
    List<ViewController> controllers = m_childControllers;
    for (ViewController controller : controllers)
    {
      controller.onPause();
      if (!state.equals(controller.m_state))
      {
        throw createStateException(controller, state);
      }
    }
    m_state = state;
  }

  /**
   * Life-cycle method that gets triggered on activity / fragment onLowMemory().
   */
  protected void onLowMemory ()
  {
    LifeCycleState state = LifeCycleState.OnLowMemory;
    List<ViewController> controllers = m_childControllers;
    for (ViewController controller : controllers)
    {
      controller.onLowMemory();
      if (!state.equals(controller.m_state))
      {
        throw createStateException(controller, state);
      }
    }
    m_state = state;
  }

  /**
   * Life-cycle method that gets triggered on activity / fragment onDestroy() /
   * onDestroyView().
   */
  protected void onDestroyView ()
  {
    LifeCycleState state = LifeCycleState.OnDestroyView;
    List<ViewController> controllers = m_childControllers;
    for (ViewController controller : controllers)
    {
      controller.onDestroyView();
      if (!state.equals(controller.m_state))
      {
        throw createStateException(controller, state);
      }
    }
    m_state = state;
  }

  /**
   * Life-cycle method that should be triggered on activity / fragment
   * onDestroy().
   */
  protected void onDestroy ()
  {
    LifeCycleState state = LifeCycleState.OnDestroy;
    List<ViewController> controllers = m_childControllers;
    for (ViewController controller : controllers)
    {
      controller.onDestroy();
      if (!state.equals(controller.m_state))
      {
        throw createStateException(controller, state);
      }
    }
    m_state = state;
    m_activity = null;

    if (m_handler != null)
    {
      m_handler.removeCallbacksAndMessages(null);
      m_handler = null;
    }
  }

  /* Child controller methods */

  /**
   * Returns the list of child ViewController instances that were previously
   * added on this controller.
   */
  protected List<ViewController> getChildControllers ()
  {
    return Collections.unmodifiableList(m_childControllers);
  }

  /* Convenience methods. */

  /**
   * Produces a generic child state IllegalStateException instance.
   */
  private static final IllegalStateException createStateException (
      ViewController controller, LifeCycleState state)
  {
    return new IllegalStateException(String.format(
        "View controller (%s) did not trigger super.%s().",
        controller,
        state));
  }

  /**
   * Adds the list of view controllers.
   */
  void addChildViewControllers (List<ViewController> controllers)
  {
    if (    controllers != null
        && !controllers.isEmpty())
    {
      m_childControllers.addAll(controllers);
    }
  }

  /* LifeCycleState definition */

  /**
   * Internal definition of view controller life-cycle states.
   */
  private enum LifeCycleState
  {
    OnCreate(0),
    OnCreateView(1),
    OnResume(2),
    OnPause(3),
    OnLowMemory(4),
    OnDestroyView(5),
    OnDestroy(6),;

    /**
     * The integer raw state.
     */
    @SuppressWarnings("unused")
    public final int state;

    /**
     * Internal constructor.
     */
    LifeCycleState (int value)
    {
      state = value;
    }
  }

    /* Thread confinement methods */

  /**
   * Schedules the {@link Runnable} instance to be executed on the main
   * thread.<br/>NOTE: Any Pending or to be scheduled runnable instances will be
   * disregard when controller is destroyed.
   */
  protected void syncCallback (Runnable runnable)
  {
    if (m_handler != null)
    {
      m_handler.post(runnable);
    }
  }

  /**
   * Schedules the {@link Runnable} instance that will be executed at the
   * given time delay in milliseconds on the main thread.<br/>NOTE: Any
   * Pending or to  be scheduled runnable instances will be disregard
   * when controller is destroyed.
   */
  protected void syncCallbackDelayed (Runnable runnable, long delayInMillis)
  {
    if (m_handler != null)
    {
      m_handler.postDelayed(runnable, delayInMillis);
    }
  }
}
