package sg.totalebizsolutions.foundation.tools.http;

import android.content.Context;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Class definitions that handles {@link URLConnection} with "https"
 * schemes, which allows to override {@link HostnameVerifier} and
 * {@link javax.net.ssl.SSLSocketFactory}.
 *
 * <p>
 * NOTE: Custom {@link javax.net.ssl.SSLSocketFactory} not supported yet.
 * </p>
 */
public class FNHttpsRequestConnection extends FNHttpRequestConnection
{
  /* Properties */

  private HostnameVerifier m_hostnameVerifier;

  /* Initializations */

  /**
   * Generic constructor.
   */
  public FNHttpsRequestConnection (Context context)
  {
    super(context);
  }

  /* Http request connection methods */

  @Override
  protected URLConnection buildBaseURLConnection (String stringUrl)
      throws IOException, MalformedURLException
  {
    HttpsURLConnection conn = (HttpsURLConnection)
        super.buildBaseURLConnection(stringUrl);

    HostnameVerifier hostnameVerifier = m_hostnameVerifier;
    if (hostnameVerifier != null)
    {
      conn.setHostnameVerifier(hostnameVerifier);
    }
    return conn;
  }

  /* Static property methods */

  /**
   * Returns the default {@link HostnameVerifier}.
   */
  public static final HostnameVerifier getDefaultHostnameVerifier ()
  {
    return HttpsURLConnection.getDefaultHostnameVerifier();
  }

  /* Property methods */

  /**
   * Sets the url connection {@link HostnameVerifier}. See also
   * {@link HttpsURLConnection}.
   */
  public void setHostnameVerifier (HostnameVerifier hostnameVerifier)
  {
    if (hostnameVerifier == null)
    {
      throw new IllegalStateException("Hostname verifier should not be null.");
    }
    m_hostnameVerifier = hostnameVerifier;
  }
}
