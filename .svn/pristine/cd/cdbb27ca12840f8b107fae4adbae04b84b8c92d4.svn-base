package sg.totalebizsolutions.foundation.tools;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;

import java.util.List;

public class LocationService
{
  /* Constants */

  private static final int TWO_MINUTES = 1000 * 60 * 2;
  private static final long MIN_TIME = 5000L; // in milliseconds
  private static final long MIN_DISTANCE = 5; // in meters

  /* Properties */

  private LocationListener m_listener;
  private LocationManager m_locationManager;

  /* Initialization */

  public LocationService (Context context)
  {
    m_locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
  }

  /* Static methods */

  /**
   * Returns whether specified location provider is enabled.
   */
  public static boolean isLocationProviderEnabled (Context context, String provider)
  {
    LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    boolean isEnabled = false;
    try
    {
      isEnabled = lm.isProviderEnabled(provider);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return isEnabled;
  }

  /**
   * Returns a Location indicating the data from the last known location fix obtained.
   */
  @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
  public Location getLastKnownLocation ()
  {
    Location lastKnownLocation = null;
    List<String> providers = getProviders(false);
    if (!providers.isEmpty())
    {
      for (String provider : providers)
      {
        Location location = m_locationManager.getLastKnownLocation(provider);
        if (   location != null
            && isBetterLocation(location, lastKnownLocation))
        {
          lastKnownLocation = location;
        }
      }
    }
    return lastKnownLocation;
  }

  /**
   * Returns the information associated with the location provider of the given name, or null if no
   * provider exists by that name.
   */
  public LocationProvider getProvider (String provider)
  {
    return m_locationManager.getProvider(provider);
  }

  /**
   * Returns the list of location providers.
   *
   * @param enableOnly if true then only the providers which are currently enabled are returned.
   */
  public List<String> getProviders (boolean enableOnly)
  {
    return m_locationManager.getProviders(enableOnly);
  }

  @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
  public void requestSingaleUpdate (LocationListener listener)
  {
    internalRequestUpdates(listener, true);
  }

  /**
   * Triggers to start listening for location updates. Make sure to check GPS/NETWORK providers
   * has been enabled.
   */
  @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
  public void requestLocationUpdates (LocationListener listener)
  {
    internalRequestUpdates(listener, false);
  }

  /**
   * Internal method implementation for requesting location updates.
   */
  @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
  private void internalRequestUpdates (final LocationListener listener, boolean singleUpdateOnly)
  {
    /* Stop updates previously started if able. */
    stopUpdates();

    m_listener = new LocationListener()
    {
      @Override
      public void onLocationChanged (Location location)
      {
        listener.onLocationChanged(location);
      }

      @Override
      public void onStatusChanged (String s, int i, Bundle bundle)
      {
        listener.onStatusChanged(s, i, bundle);
      }

      @Override
      public void onProviderEnabled (String s)
      {
        listener.onProviderEnabled(s);
      }

      @Override
      public void onProviderDisabled (String s)
      {
        listener.onProviderDisabled(s);
      }
    };

    if (singleUpdateOnly)
    {
      m_locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, m_listener, null);
      m_locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, m_listener, null);
    }
    else
    {
      m_locationManager.requestLocationUpdates(
        LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, m_listener);
      m_locationManager.requestLocationUpdates(
        LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, m_listener);
    }
  }

  /**
   * Triggers to stop listening for location updates if able.
   */
  @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
  public void stopUpdates ()
  {
    if (m_listener == null)
    {
      return;
    }
    m_locationManager.removeUpdates(m_listener);
    m_listener = null;
  }

  /**
   * Determines whether one Location reading is better than the current Location fix.
   *
   * @param location
   *   The new Location that you want to evaluate
   * @param currentBestLocation
   *   The current Location fix, to which you want to compare the new one
   */
  private boolean isBetterLocation (Location location, Location currentBestLocation)
  {
    if (currentBestLocation == null)
    {
      /* A new location is always better than no location. */
      return true;
    }

    /*  Check whether the new location fix is newer or older. */
    long timeDelta = location.getTime() - currentBestLocation.getTime();
    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
    boolean isNewer = timeDelta > 0;

    /* If it's been more than two minutes since the current location, use the new location
     * because the user has likely moved.
     */
    if (isSignificantlyNewer)
    {
      /*  If the new location is more than two minutes older, it must be worse. */
      return true;
    }
    else if (isSignificantlyOlder)
    {
      return false;
    }

    /* Check whether the new location fix is more or less accurate. */
    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
    boolean isLessAccurate = accuracyDelta > 0;
    boolean isMoreAccurate = accuracyDelta < 0;
    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

    /* Check if the old and new location are from the same provider. */
    boolean isFromSameProvider = isSameProvider(location.getProvider(),
      currentBestLocation.getProvider());

    /* Determine location quality using a combination of timeliness and accuracy. */
    if (isMoreAccurate)
    {
      return true;
    }
    else if (isNewer && !isLessAccurate)
    {
      return true;
    }
    else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider)
    {
      return true;
    }
    return false;
  }

  /**
   * Checks whether two providers are the same
   */
  private boolean isSameProvider (String provider1, String provider2)
  {
    if (provider1 == null)
    {
      return provider2 == null;
    }
    return provider1.equals(provider2);
  }
}
