package sg.totalebizsolutions.foundation.tools.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class definition that handles {@link URLConnection}. See {@link
 * FNHttpRequestConnection#requestGet(String,
 * HttpRequestCallback)}
 * and {@link FNHttpRequestConnection#requestPost(String, Map,
 * HttpRequestCallback)} for the type of requests you
 * want.
 *
 * <p>
 * All request by default contains request timeout of 5 minutes and a minute
 * connection timeout. See {@link FNHttpRequestConnection#setRequestTimeout(int)}
 * and {@link FNHttpRequestConnection#setConnectionTimeout(int)}
 * if you want to modify the default values.
 * </p>
 *
 * <p> Set callback to
 * {@link HttpRequestProgressCallback}
 * if you want to listen to the request updates.
 * </p>
 *
 * <p> For HTTPS, see {@link FNHttpsRequestConnection}.
 * </p>
 *
 * <p> NOTE: This is not safe to use on main
 * thread.
 * </p>
 */
public class FNHttpRequestConnection
{
  /* Constants */

  private static final String RequestMethodGet = "GET";
  private static final String RequestMethodPost = "POST";

  /* Properties */

  private int m_requestTimeout = 5 * 60 * 1000;
  private int m_connectionTimeout = 2 * 60 * 1000;

  private Context m_context;
  private List<RequestProperty> m_requestProperties = new ArrayList<>();

  /* Initialization */

  /**
   * Generic constructor.
   *
   * @param context
   *          the reference {@link Context}
   */
  public FNHttpRequestConnection (Context context)
  {
    m_context = context;
  }

  /* Request methods */

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
   * Request GET method connection to the specified string URL.
   *
   * @param url
   *          the raw String url to connect to
   * @param callback
   *          the {@link HttpRequestCallback} instance
   *          that will be informed when the request is completed
   */
  public void requestGet (String url, HttpRequestCallback callback)
  {
    String data = null;
    int statusCode = HttpRequestCallback.ResponseCodeSuccessful;

    if (!isNetworkAvailable())
    {
      statusCode = HttpRequestCallback.ResponseCodeNoNetworkAvailable;
    }

    /* Break request if criteria not met */
    if (statusCode != HttpRequestCallback.ResponseCodeSuccessful)
    {
      if (callback != null)
      {
        callback.onRequestCompleted(statusCode, null);
      }
      return;
    }

    HttpURLConnection urlConnection = null;
    try
    {
      /* Setup URLConnection */
      urlConnection = (HttpURLConnection) buildBaseURLConnection(url);
      urlConnection.setRequestMethod(RequestMethodGet);

      log("GET request: " + url);

      /* Append request properties. */
      appendRequestProperties(urlConnection);

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
    catch (SocketTimeoutException|SocketException e)
    {
      statusCode = HttpRequestCallback.ResponseCodeRequestTimeout;
      e.printStackTrace();
    }
    /*
     * Catch reported existing Androids bug.
     */
    catch (UnknownHostException e)
    {
      statusCode = HttpRequestCallback.ResponseCodeUnknownHost;
      e.printStackTrace();
    }
    /*
     * NOTE: If we do start to support error. This is where we should implement
     * it.
     */
    catch (IOException e)
    {
      statusCode = HttpRequestCallback.ResponseCodeError;
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

  /**
   * Request POST method connection to the specified string URL and Map of
   * String of String data values that will be uploaded to a web server.
   *
   * @param url
   *          the raw String url to connect to
   * @param paramMap
   *          the Map of String of String values that will be uploaded to a web
   *          server
   * @param callback
   *          the {@link HttpRequestCallback} instance
   *          that will be informed when the request is completed
   */
  public void requestPost (String url, Map<String, String> paramMap,
      HttpRequestCallback callback)
  {
    String data = null;
    int statusCode = HttpRequestCallback.ResponseCodeSuccessful;

    if (!isNetworkAvailable())
    {
      statusCode = HttpRequestCallback.ResponseCodeNoNetworkAvailable;
    }

    /* Break request if criteria not met */
    if (statusCode != HttpRequestCallback.ResponseCodeSuccessful)
    {
      if (callback != null)
      {
        callback.onRequestCompleted(statusCode, null);
      }
      return;
    }

    HttpURLConnection urlConnection = null;
    try
    {
      /* Setup URLConnectio */
      urlConnection = (HttpURLConnection) buildBaseURLConnection(url);
      urlConnection.setRequestMethod(RequestMethodPost);

      log("POST request: " + url);

      /* Setup URLConnection post methods */
      urlConnection.setDoInput(true);
      urlConnection.setDoOutput(true);

      /* Append request properties. */
      appendRequestProperties(urlConnection);

      /* Post post parameter content to urlConnection */
      postContentBody(urlConnection, paramMap);

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
    catch (SocketTimeoutException|SocketException e)
    {
      statusCode = HttpRequestCallback.ResponseCodeRequestTimeout;
      e.printStackTrace();
    }
    catch (UnknownHostException e)
    {
      statusCode = HttpRequestCallback.ResponseCodeUnknownHost;
      e.printStackTrace();
    }
    /*
     * NOTE: If we do start to support error. This is where we should implement
     * it.
     */
    catch (IOException e)
    {
      statusCode = HttpRequestCallback.ResponseCodeError;
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

  /* Internal method */

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
   * @throws MalformedURLException
   *           thrown when URL cannot be parse
   */
  protected URLConnection buildBaseURLConnection (String stringUrl)
      throws IOException
  {
    URL url = new URL(stringUrl);
    URLConnection conn = url.openConnection();
    System.setProperty("http.keepAlive", "false");

    /* Set connection timeout. */
    conn.setReadTimeout(m_requestTimeout);
    conn.setConnectTimeout(m_connectionTimeout);

    return conn;
  }

  /**
   * Common method to post content data to request body.
   *
   * @param urlConnection
   *          the {@link URLConnection} instance where the data will
   *          be uploaded
   * @param contentParamMap
   *          the Map of String of String values map to upload
   * @throws IOException thrown when no output stream could be created
   */
  private void postContentBody (HttpURLConnection urlConnection,
      Map<String, String> contentParamMap) throws IOException
  {
    if (contentParamMap == null)
    {
      return;
    }

    OutputStream out = null;
    BufferedWriter writer = null;
    try
    {
      /* Create post content */
      StringBuilder builder = new StringBuilder();
      for (Map.Entry<String, String> entry : contentParamMap.entrySet())
      {
        String key = entry.getKey();
        String value = entry.getValue();
        if (   key == null
            || key.length() == 0)
        {
          continue;
        }

        builder.append(URLEncoder.encode(key, "UTF-8"));
        builder.append("=");
        builder.append(URLEncoder.encode(value == null ? "" : value, "UTF-8"));
        builder.append("&");
      }

      if (builder.length() > 0)
      {
        builder.deleteCharAt(builder.length() - 1);
      }

      urlConnection.setFixedLengthStreamingMode(builder.length());
      out = urlConnection.getOutputStream();
      writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
      writer.write(builder.toString());
      writer.flush();

      log("POST body: " + builder.toString());
    }
    finally
    {
      closeStream(writer);
      closeStream(out);
    }
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
   * Common method to read data from the {@link InputStream} passing the
   * {@link HttpRequestCallback} instance that will be
   * informed of the download updates.
   *
   * @param inputStream
   *          the {@link InputStream} where the data will be read
   * @param contentLength
   *          the content length that is usable on progress updates
   * @param callback
   *          the {@link HttpRequestCallback} instance if
   *          instance of
   *          {@link HttpRequestProgressCallback} will be
   *          informed of the
   *          download updates
   * @return the raw String data fetched
   * @throws IOException
   *           thrown when an error occurs while trying to connect to the
   *           resource or while reading the response
   */
  private String fetchData (InputStream inputStream, int contentLength,
      HttpRequestCallback callback) throws IOException
  {
    contentLength = Math.max(contentLength, 1);

    /* Evaluate the need to trigger progress updates */
    HttpRequestProgressCallback progressCallback = null;
    if (   callback != null
        && callback instanceof HttpRequestProgressCallback)
    {
      progressCallback = (HttpRequestProgressCallback) callback;
    }

    /* Start download data */

    String data = null;
    Reader reader = null;
    try
    {
      StringBuilder builder = new StringBuilder();
      reader = new InputStreamReader(inputStream);

      char[] charBuffer = new char[1024];
      int count = -1;
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
  public interface HttpRequestCallback
  {
    int ResponseCodeNoNetworkAvailable = 0;
    int ResponseCodeSuccessful = 200;
    int ResponseCodeError = 400;
    int ResponseCodeRequestTimeout = 408;
    int ResponseCodeUnknownHost = 500;

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
   * See {@link HttpRequestCallback}. Interface definition
   * current progress of the request.
   */
  public interface HttpRequestProgressCallback
                  extends HttpRequestCallback
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

  /**
   * Utility method for logging.
   */
  private void log (String log)
  {
    Log.d("GitHub", log);
  }
}
