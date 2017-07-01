package sg.totalebizsolutions.foundation.view.navigation;

import android.support.v4.app.Fragment;

public interface NavigationHandler
{
  /**
   * Replaces the current fragment and clears fragment stack.
   *
   * @param fragment the {@link Fragment} to attach
   * @param tag the String fragment tag. Later can be used to retrieve fragment instance
   */
  void setRootFragment (Fragment fragment, String tag);

  /**
   * Pushes the {@link Fragment} to the navigation stack.
   *
   * @param fragment the {@link Fragment} to push into the navigation stack
   * @param tag the String fragment tag (Optional). Later to be used to retrieve fragment instance
   * @param animated true if animated, otherwise, false
   */
  void pushFragmentToNavigation (Fragment fragment, String tag, boolean animated);

  /**
   * Pops the current {@link Fragment} from the stack.
   *
   * @param animated true if animated, otherwise, false
   */
  void popFragmentFromNavigation (boolean animated);
}
