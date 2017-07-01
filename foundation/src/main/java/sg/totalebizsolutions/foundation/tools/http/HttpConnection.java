package sg.totalebizsolutions.foundation.tools.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.StringDef;
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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import sg.totalebizsolutions.foundation.BuildConfig;

public class HttpConnection
{
  /* Constants */

  public static final String METHOD_GET = "GET";
  public static final String METHOD_POST = "POST";
  public static final String METHOD_DELETE = "DELETE";
  public static final String METHOD_PUT = "PUT";

  @Retention(RetentionPolicy.SOURCE)
  @StringDef({METHOD_GET, METHOD_DELETE, METHOD_PUT, METHOD_DELETE})
  public @interface RequestMethod
  {
  }

  /* Properties */

  private Context m_context;

  private String m_baseUrl;
  private String m_requestBody;
  private String m_requestMethod = METHOD_GET;

  private Map<String, String> m_paramMap = new HashMap<>();
  private Map<String, String> m_propertyMap = new HashMap<>();

  private int m_requestTimeout = 60 * 1000;
  private int m_connectionTimeout = 60 * 1000;

    /* Initialization */

  /**
   * Generic constructor.
   *
   * @param context the reference {@link Context}
   */
  public HttpConnection (Context context, String baseUrl)
  {
    m_context = context;
    m_baseUrl = baseUrl;
  }

  /* Property methods */

  /**
   * Sets request method.
   */
  public void setRequestMethod (@RequestMethod String method)
  {
    m_requestMethod = method;
  }

  /**
   * Sets the rest request body content.
   */
  public void setRequestBody (String requestBody)
  {
    m_requestBody = requestBody;
    if (    requestBody != null
        && !METHOD_POST.equals(m_requestMethod)
        && !METHOD_PUT.equals(m_requestMethod))
    {
      m_requestMethod = METHOD_POST;
    }
  }

  /**
   * Sets the request timeout on each request.
   *
   * @param timeoutInMillis the timeout time in milliseconds to be set on each requests
   */
  public void setRequestTimeout (int timeoutInMillis)
  {
    m_requestTimeout = timeoutInMillis;
  }

  /**
   * Sets the connection timeout on each request.
   *
   * @param connectionTimeoutInMillis the timeout in milliseconds to be set on each requests
   */
  public void setConnectionTimeout (int connectionTimeoutInMillis)
  {
    m_connectionTimeout = connectionTimeoutInMillis;
  }

  /**
   * Sets the value of the specified request header field. The value will only be used by the
   * current URLConnection instance. This method can only be called before the connection is
   * established.
   */
  public void setRequestProperty (String field, String value)
  {
    m_propertyMap.put(field, value);
  }

  /**
   * Adds the specified request parameter specified by key on request.
   *
   * @param key the parameter key
   * @param value the value
   */
  public void addRequestParameter (String key, String value)
  {
    m_paramMap.put(key, value);
  }

  /**
   * Ads the map of key and value parameters.
   *
   * @param params the map of key of string values
   */
  public void addRequestParameters (Map<String, String> params)
  {
    if (params != null)
    {
      m_paramMap.putAll(params);
    }
  }

  /**
   * Triggers fetch response.
   */
  public Response getResponse ()
  {
    String data = null;
    int statusCode = Response.ResponseCodeSuccessful;

    if (!isNetworkAvailable())
    {
      statusCode = Response.ResponseCodeNoNetworkAvailable;
      return new Response(statusCode, null);
    }

    HttpURLConnection urlConnection = null;
    try
    {
      /* Setup URLConnection */

      urlConnection = (HttpURLConnection) buildBaseURLConnection(m_baseUrl);
      urlConnection.setRequestMethod(m_requestMethod);

      /* Append request properties. */
      appendRequestProperties(urlConnection);

      /* Post post request body to urlConnection */
      postContentBody(urlConnection, m_requestBody);

      /* Fetch and pass request callback instance for the download updates */
      InputStream inputStream = urlConnection.getInputStream();
      int contentLength = urlConnection.getContentLength();
      int responseStatus = urlConnection.getResponseCode();

      data = fetchData(inputStream, contentLength, null);

      statusCode = responseStatus;
    }
    catch (  SocketTimeoutException
           | SocketException e)
    {
      statusCode = Response.ResponseCodeRequestTimeout;
      e.printStackTrace();
    }
    /*
     * Catch reported existing Androids bug.
     */
    catch (UnknownHostException e)
    {
      statusCode = Response.ResponseCodeUnknownHost;
      e.printStackTrace();
    }
    /*
     * NOTE: If we do start to support error. This is where we should implement
     * it.
     */
    catch (Exception e)
    {
      statusCode = Response.ResponseCodeServerError;
      e.printStackTrace();
    }
    finally
    {
      if (urlConnection != null)
      {
        urlConnection.disconnect();
      }
    }

    return new Response(statusCode, data);
  }

  /**
   * Asynchronously fetch response.
   *
   * @param callback the {@link HttpCallback} instance that will be informed when the
   * request is completed
   */
  public void getResponseAsync (final HttpCallback callback)
  {
    new Thread()
    {
      @Override
      public void run ()
      {
        final Response response = getResponse();
        if (callback != null)
        {
          callback.onRequestCompleted(response);
        }
      }
    }.start();
  }

  /* Internal methods */

  /**
   * Common method to build {@link HttpURLConnection} instance initialized with the specified
   * parameters.
   *
   * @param stringUrl the reference string URL of the connection
   * @return the {@link HttpURLConnection} instance
   * @throws IOException thrown when an error occur when opening the connection
   */
  protected URLConnection buildBaseURLConnection (String stringUrl) throws IOException
  {
    Uri.Builder builder = Uri.parse(stringUrl).buildUpon();
    for (Map.Entry<String, String> param : m_paramMap.entrySet())
    {
      builder.appendQueryParameter(param.getKey(), param.getValue());
    }

    log(String.format("%s: %s", m_requestMethod, builder.toString()));

    URL url = new URL(builder.toString());
    URLConnection conn = url.openConnection();

    conn.setDoInput(true);
    conn.setDoOutput(true);
    conn.setUseCaches(false);

    System.setProperty("http.keepAlive", "false");

    /* Set connection timeout. */
    conn.setReadTimeout(m_requestTimeout);
    conn.setConnectTimeout(m_connectionTimeout);

    return conn;
  }

  /**
   * Method to append all request properties. See {@link #setRequestProperty(String, String)}.
   */
  private void appendRequestProperties (HttpURLConnection urlConnection)
  {
    for (Map.Entry<String, String> property : m_propertyMap.entrySet())
    {
      urlConnection.setRequestProperty(property.getKey(), property.getValue());
    }
  }

  /**
   * Common method to post content data to request body.
   *
   * @param urlConnection the {@link URLConnection} instance where the data will be uploaded
   * @param contentBody the string content body to post
   * @throws IOException thrown when no output stream could be created
   */
  private void postContentBody (HttpURLConnection urlConnection,
    String contentBody) throws IOException
  {
    if (contentBody == null)
    {
      return;
    }

    OutputStream out = null;
    BufferedWriter writer = null;
    try
    {
      out = new BufferedOutputStream(urlConnection.getOutputStream());
      writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
      writer.write(contentBody);
      writer.flush();

      log("RequestBody: " + contentBody);
    }
    finally
    {
      closeStream(writer);
      closeStream(out);
    }
  }

  /**
   * Common method to read data from the {@link InputStream} passing the {@link
   * HttpCallback} instance that will be informed of the download updates.
   *
   * @param inputStream the {@link InputStream} where the data will be read
   * @param contentLength the content length that is usable on progress updates
   * @param callback the {@link HttpCallback} instance if instance of {@link
   * HttpProgressCallback} will be informed of the download updates
   * @return the raw String data fetched
   * @throws IOException thrown when an error occurs while trying to connect to the resource or
   * while reading the response
   */
  private String fetchData (InputStream inputStream, int contentLength,
    HttpCallback callback) throws IOException
  {
    contentLength = Math.max(contentLength, 1);

    /* Evaluate the need to trigger progress updates */
    HttpProgressCallback progressCallback = null;
    if (   callback != null
        && callback instanceof HttpProgressCallback)
    {
      progressCallback = (HttpProgressCallback) callback;
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
            progressCallback.onProgressUpdate(Math.round(100.0f * partialLength / contentLength));
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

  public static class Response
  {
    public static final int ResponseCodeNoNetworkAvailable = 0;
    public static final int ResponseCodeSuccessful = 200;
    public static final int ResponseCodeCreated = 201;
    public static final int ResponseCodeError = 400;
    public static final int ResponseCodeRequestTimeout = 408;
    public static final int ResponseCodeServerError = 500;
    public static final int ResponseCodeUnknownHost = 599;

    public final int statusCode;
    public final String data;

    public Response (int statusCode, String data)
    {
      this.statusCode = statusCode;
      this.data = data;
    }
  }

  /**
   * Interface definition that will be inform when the request is completed.
   */
  public interface HttpCallback
  {
    /**
     * Called when the fetch request is finished.
     */
    void onRequestCompleted (Response response);
  }

  /**
   * See {@link HttpCallback}. Interface definition current progress of the request.
   */
  public interface HttpProgressCallback extends HttpCallback
  {
    /**
     * Called to inform the progress of the request. This is usually called every time it receives a
     * response data chunk.
     *
     * @param progress the reference progress value of the request download. (value from 0-100).
     */
    void onProgressUpdate (int progress);
  }

  /* Utility methods */

  /**
   * Whether the device is connected to a network.
   *
   * @return true if connected to a network, otherwise, false
   */
  public boolean isNetworkAvailable ()
  {
    ConnectivityManager cm = (ConnectivityManager)
      m_context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
      Log.d("HttpConnectionF", log);
    }
  }
}
