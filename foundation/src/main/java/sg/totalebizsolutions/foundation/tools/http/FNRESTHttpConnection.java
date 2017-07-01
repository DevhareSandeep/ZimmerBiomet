package sg.totalebizsolutions.foundation.tools.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import sg.totalebizsolutions.foundation.BuildConfig;


public class FNRESTHttpConnection
{
  /* Constants */

  public static final String METHOD_GET = "GET";
  public static final String METHOD_POST = "POST";
  public static final String METHOD_DELETE = "DELETE";
  public static final String METHOD_PUT = "PUT";

  /* Properties */

  private Context m_context;

  private String m_requestBody;
  private String m_requestMethod = METHOD_GET;

  private int m_requestTimeout = 60 * 1000;
  private int m_connectionTimeout = 60 * 1000;

  private List<RequestProperty> m_requestProperties = new ArrayList<>();

    /* Initialization */

  /**
   * Generic constructor.
   *
   * @param context
   *          the reference {@link Context}
   */
  public FNRESTHttpConnection (Context context)
  {
    m_context = context;
  }

  /* Property methods */

  /**
   * Sets request method.
   */
  public void setRequestMethod (String method)
  {
    m_requestMethod = method;
  }

  /**
   * Sets the rest request body content.
   */
  public void setRequestBody (String requestBody)
  {
    m_requestBody = requestBody;
  }

  /**
   * Sets the request timeout on each request.
   *
   * @param timeoutInMillis
   *          the timeout time in milliseconds to be set on each requests
   */
  public void setRequestTimeout (int timeoutInMillis)
  {
    m_requestTimeout = timeoutInMillis;
  }

  /**
   * Sets the connection timeout on each request.
   *
   * @param connectionTimeoutInMillis
   *          the timeout in milliseconds to be set on each requests
   */
  public void setConnectionTimeout (int connectionTimeoutInMillis)
  {
    m_connectionTimeout = connectionTimeoutInMillis;
  }

  /**
   * Sets the value of the specified request header field. The value will
   * only be used by the current URLConnection instance. This method can only
   * be called before the connection is established.
   */
  public void setRequestProperty (String field, String value)
  {
    m_requestProperties.add(new RequestProperty(field, value));
  }

  /**
   * Request connection to the specified string URL.
   *
   * @param url
   *          the raw String url to connect to
   * @param callback
   *          the {@link RESTHttpRequestCallback} instance
   *          that will be informed when the request is completed
   */
  public void request (String url, RESTHttpRequestCallback callback)
  {
    String data = null;
    int statusCode = RESTHttpRequestCallback.ResponseCodeSuccessful;

    if (!isNetworkAvailable())
    {
      statusCode = RESTHttpRequestCallback.ResponseCodeNoNetworkAvailable;

      if (callback != null)
      {
        callback.onRequestCompleted(statusCode, null);
      }
      return;
    }

    HttpsURLConnection urlConnection = null;
    try
    {
      /* Setup URLConnection */

      SSLContext sslcontext = SSLContext.getInstance("TLSv1.2");
      sslcontext.init(null, null, null);

      SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(
          sslcontext.getSocketFactory());

      HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);

      urlConnection = (HttpsURLConnection) buildBaseURLConnection(url);
      urlConnection.setRequestMethod(m_requestMethod);

      log("GET request: " + url);

      /* Append request properties. */
      appendRequestProperties(urlConnection);

      /* Post post request body to urlConnection */
      postContentBody(urlConnection, m_requestBody);

      /* Fetch and pass request callback instance for the download updates */
      InputStream inputStream = urlConnection.getInputStream();
      int contentLength = urlConnection.getContentLength();
      int responseStatus = urlConnection.getResponseCode();

      data = fetchData(
          inputStream,
          contentLength,
          callback);

      statusCode = responseStatus;
    }
    catch (SocketTimeoutException |SocketException e)
    {
      statusCode = RESTHttpRequestCallback.ResponseCodeRequestTimeout;
      e.printStackTrace();
    }
    /*
     * Catch reported existing Androids bug.
     */
    catch (UnknownHostException e)
    {
      statusCode = RESTHttpRequestCallback.ResponseCodeUnknownHost;
      e.printStackTrace();
    }
    /*
     * NOTE: If we do start to support error. This is where we should implement
     * it.
     */
    catch (Exception e)
    {
      statusCode = RESTHttpRequestCallback.ResponseCodeServerError;
      e.printStackTrace();
    }
    finally
    {
      if (urlConnection != null)
      {
        urlConnection.disconnect();
      }
    }

    if (callback != null)
    {
      callback.onRequestCompleted(statusCode, data);
    }
  }

  /* Internal methods */

  /**
   * Common method to build {@link HttpURLConnection} instance
   * initialized with
   * the specified parameters.
   *
   * @param stringUrl
   *          the reference string URL of the connection
   * @return the {@link HttpURLConnection} instance
   * @throws IOException
   *           thrown when an error occur when opening the connection
   */
  protected URLConnection buildBaseURLConnection (String stringUrl)
      throws IOException
  {
    URL url = new URL(stringUrl);
    URLConnection conn = url.openConnection();
    conn.setDoInput (true);
    conn.setDoOutput(true);
    conn.setUseCaches(false);

    System.setProperty("http.keepAlive", "false");

    /* Set connection timeout. */
    conn.setReadTimeout(m_requestTimeout);
    conn.setConnectTimeout(m_connectionTimeout);

    return conn;
  }

  /**
   * Common method to append all request properties. See {@link
   * #setRequestProperty(String, String)}.
   */
  private void appendRequestProperties (HttpURLConnection urlConnection)
  {
    for (RequestProperty requestProperty : m_requestProperties)
    {
      urlConnection.setRequestProperty(requestProperty.field,
          requestProperty.value);
    }
  }

  /**
   * Common method to post content data to request body.
   *
   * @param urlConnection
   *          the {@link URLConnection} instance where the data will
   *          be uploaded
   * @param contentBody
   *          the string content body to post
   * @throws IOException thrown when no output stream could be created
   */
  private void postContentBody (HttpURLConnection urlConnection,
     String contentBody) throws IOException
  {
    if (contentBody == null)
    {
      return;
    }
    contentBody = contentBody.replace("\n", "\\n");

    OutputStream out = null;
    BufferedWriter writer = null;
    try
    {
      out = new BufferedOutputStream(urlConnection.getOutputStream());
      writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
      writer.write(contentBody);
      writer.flush();

      log("POST body: " + contentBody);
    }
    finally
    {
      closeStream(writer);
      closeStream(out);
    }
  }

  /**
   * Common method to read data from the {@link InputStream} passing the
   * {@link RESTHttpRequestCallback} instance that will be
   * informed of the download updates.
   *
   * @param inputStream
   *          the {@link InputStream} where the data will be read
   * @param contentLength
   *          the content length that is usable on progress updates
   * @param callback
   *          the {@link RESTHttpRequestCallback} instance if
   *          instance of
   *          {@link RESTHttpRequestProgressCallback} will be
   *          informed of the
   *          download updates
   * @return the raw String data fetched
   * @throws IOException
   *           thrown when an error occurs while trying to connect to the
   *           resource or while reading the response
   */
  private String fetchData (InputStream inputStream, int contentLength,
      RESTHttpRequestCallback callback) throws IOException
  {
    contentLength = Math.max(contentLength, 1);

    /* Evaluate the need to trigger progress updates */
    RESTHttpRequestProgressCallback progressCallback = null;
    if (   callback != null
        && callback instanceof RESTHttpRequestProgressCallback)
    {
      progressCallback = (RESTHttpRequestProgressCallback) callback;
    }

    /* Start download data */

    String data = null;
    Reader reader = null;
    try
    {
      StringBuilder builder = new StringBuilder();
      reader = new InputStreamReader(inputStream);

      char[] charBuffer = new char[1024];
      int count;
      while ((count = reader.read(charBuffer)) > -1)
      {
        if (count > 0)
        {
          builder.append(charBuffer, 0, count);
        }

        /* Trigger progress updates when able */
        if (progressCallback != null)
        {
          int partialLength = builder.length();
          contentLength = Math.max(partialLength, contentLength);
          try
          {
            progressCallback.onProgressUpdate(Math.round(100.0f
                * partialLength / contentLength));
          }
          catch (Exception e)
          {
            /* Catch callback trigger exceptions. */
            e.printStackTrace();
          }
        }
      }
      data = builder.toString();
      log("Response: " + data);
    }
    finally
    {
      closeStream(reader);
    }
    return data;
  }

  /* Interface definition */

  /**
   * Interface definition that will be inform when the request is completed.
   */
  public interface RESTHttpRequestCallback
  {
    int ResponseCodeNoNetworkAvailable = 0;
    int ResponseCodeSuccessful = 200;
    int ResponseCodeError = 400;
    int ResponseCodeRequestTimeout = 408;
    int ResponseCodeServerError = 500;
    int ResponseCodeUnknownHost = 599;

    /**
     * Called when the fetch request is finished.
     *
     * @param statusCode
     *          the reference response status code of the request
     * @param data
     *          the String raw data, nullable depending on the result of the
     *          request
     */
    void onRequestCompleted(int statusCode, String data);
  }

  /**
   * See {@link RESTHttpRequestCallback}. Interface definition
   * current progress of the request.
   */
  public interface RESTHttpRequestProgressCallback
           extends RESTHttpRequestCallback
  {
    /**
     * Called to inform the progress of the request. This is usually called
     * every time it receives a response data chunk.
     *
     * @param progress
     *          the reference progress value of the request download. (value
     *          from 0-100).
     */
    void onProgressUpdate(int progress);
  }

  /* Header class definition */

  private class RequestProperty
  {
    String field;
    String value;

    public RequestProperty (String field, String value)
    {
      this.field = field;
      this.value = value;
    }
  }

  /* Utility methods */

  /**
   * Whether the device is connected to a network.
   *
   * @return true if connected to a network, otherwise, false
   */
  public boolean isNetworkAvailable ()
  {
    ConnectivityManager cm = (ConnectivityManager) m_context
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
    return   networkInfo != null
          && networkInfo.isConnected();
  }

  /**
   * Common method to close stream instances.
   */
  private void closeStream (Closeable stream)
  {
    /* Close stream if possible */
    if (stream != null)
    {
      try
      {
        stream.close();
      }
      catch (IOException e)
      {
        /*
         * Do nothing. At this point, we don't need to worry about the stream
         * instance's current state.
         */
      }
    }
  }

  /**
   * Utility method for logging.
   */
  private void log (String log)
  {
    if (BuildConfig.DEBUG)
    {
      Log.d("RESTHttpConnection", log);
    }
  }
}
