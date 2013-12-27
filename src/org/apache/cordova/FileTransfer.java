package org.apache.cordova;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FileTransfer extends CordovaPlugin
{
  public static int ABORTED_ERR = 0;
  private static final String BOUNDARY = "+++++";
  public static int CONNECTION_ERR = 0;
  private static final HostnameVerifier DO_NOT_VERIFY;
  public static int FILE_NOT_FOUND_ERR = 0;
  public static int INVALID_URL_ERR = 0;
  private static final String LINE_END = "\r\n";
  private static final String LINE_START = "--";
  private static final String LOG_TAG = "FileTransfer";
  private static final int MAX_BUFFER_SIZE = 16384;
  private static HashMap<String, RequestContext> activeRequests;
  private static final TrustManager[] trustAllCerts;

  static
  {
    CONNECTION_ERR = 3;
    ABORTED_ERR = 4;
    activeRequests = new HashMap();
    DO_NOT_VERIFY = new HostnameVerifier()
    {
      public boolean verify(String paramString, SSLSession paramSSLSession)
      {
        return true;
      }
    };
    TrustManager[] arrayOfTrustManager = new TrustManager[1];
    arrayOfTrustManager[0] = new X509TrustManager()
    {
      public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
        throws CertificateException
      {
      }

      public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
        throws CertificateException
      {
      }

      public X509Certificate[] getAcceptedIssuers()
      {
        return new X509Certificate[0];
      }
    };
    trustAllCerts = arrayOfTrustManager;
  }

  private void abort(String paramString)
  {
    RequestContext localRequestContext;
    JSONObject localJSONObject;
    synchronized (activeRequests)
    {
      localRequestContext = (RequestContext)activeRequests.remove(paramString);
      if (localRequestContext != null)
      {
        File localFile = localRequestContext.targetFile;
        if (localFile != null)
          localFile.delete();
        localJSONObject = createFileTransferError(ABORTED_ERR, localRequestContext.source, localRequestContext.target, null, Integer.valueOf(-1));
        monitorenter;
      }
    }
    try
    {
      localRequestContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, localJSONObject));
      localRequestContext.aborted = true;
      monitorexit;
      this.cordova.getThreadPool().execute(new Runnable(localRequestContext)
      {
        public void run()
        {
          synchronized (this.val$context)
          {
            FileTransfer.access$400(this.val$context.currentInputStream);
            FileTransfer.access$400(this.val$context.currentOutputStream);
            return;
          }
        }
      });
      return;
      localObject1 = finally;
      throw localObject1;
    }
    finally
    {
      monitorexit;
    }
    throw localObject2;
  }

  private static void addHeadersToRequest(URLConnection paramURLConnection, JSONObject paramJSONObject)
  {
    try
    {
      Iterator localIterator = paramJSONObject.keys();
      while (localIterator.hasNext())
      {
        String str = localIterator.next().toString();
        JSONArray localJSONArray = paramJSONObject.optJSONArray(str);
        if (localJSONArray == null)
        {
          localJSONArray = new JSONArray();
          localJSONArray.put(paramJSONObject.getString(str));
        }
        paramURLConnection.setRequestProperty(str, localJSONArray.getString(0));
        for (int i = 1; i < localJSONArray.length(); i++)
          paramURLConnection.addRequestProperty(str, localJSONArray.getString(i));
      }
    }
    catch (JSONException localJSONException)
    {
    }
  }

  // ERROR //
  private static JSONObject createFileTransferError(int paramInt, String paramString1, String paramString2, String paramString3, Integer paramInteger)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 5
    //   3: new 184	org/json/JSONObject
    //   6: dup
    //   7: invokespecial 235	org/json/JSONObject:<init>	()V
    //   10: astore 6
    //   12: aload 6
    //   14: ldc 237
    //   16: iload_0
    //   17: invokevirtual 240	org/json/JSONObject:put	(Ljava/lang/String;I)Lorg/json/JSONObject;
    //   20: pop
    //   21: aload 6
    //   23: ldc 241
    //   25: aload_1
    //   26: invokevirtual 244	org/json/JSONObject:put	(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
    //   29: pop
    //   30: aload 6
    //   32: ldc 245
    //   34: aload_2
    //   35: invokevirtual 244	org/json/JSONObject:put	(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
    //   38: pop
    //   39: aload_3
    //   40: ifnull +12 -> 52
    //   43: aload 6
    //   45: ldc 247
    //   47: aload_3
    //   48: invokevirtual 244	org/json/JSONObject:put	(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
    //   51: pop
    //   52: aload 4
    //   54: ifnull +13 -> 67
    //   57: aload 6
    //   59: ldc 249
    //   61: aload 4
    //   63: invokevirtual 244	org/json/JSONObject:put	(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
    //   66: pop
    //   67: aload 6
    //   69: areturn
    //   70: astore 7
    //   72: ldc 25
    //   74: aload 7
    //   76: invokevirtual 252	org/json/JSONException:getMessage	()Ljava/lang/String;
    //   79: aload 7
    //   81: invokestatic 258	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   84: pop
    //   85: aload 5
    //   87: areturn
    //   88: astore 7
    //   90: aload 6
    //   92: astore 5
    //   94: goto -22 -> 72
    //
    // Exception table:
    //   from	to	target	type
    //   3	12	70	org/json/JSONException
    //   12	39	88	org/json/JSONException
    //   43	52	88	org/json/JSONException
    //   57	67	88	org/json/JSONException
  }

  private static JSONObject createFileTransferError(int paramInt, String paramString1, String paramString2, URLConnection paramURLConnection)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Object localObject = null;
    int i = 0;
    if (paramURLConnection != null)
      try
      {
        boolean bool = paramURLConnection instanceof HttpURLConnection;
        localObject = null;
        i = 0;
        if (bool)
        {
          i = ((HttpURLConnection)paramURLConnection).getResponseCode();
          InputStream localInputStream = ((HttpURLConnection)paramURLConnection).getErrorStream();
          localObject = null;
          if (localInputStream != null)
          {
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream, "UTF-8"));
            String str1 = localBufferedReader.readLine();
            while (str1 != null)
            {
              localStringBuilder.append(str1);
              str1 = localBufferedReader.readLine();
              if (str1 == null)
                continue;
              localStringBuilder.append('\n');
            }
          }
        }
      }
      catch (IOException localIOException)
      {
        Log.w("FileTransfer", "Error getting HTTP status code from connection.", localIOException);
      }
    while (true)
    {
      return createFileTransferError(paramInt, paramString1, paramString2, (String)localObject, Integer.valueOf(i));
      String str2 = localStringBuilder.toString();
      localObject = str2;
    }
  }

  private void download(String paramString1, String paramString2, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    Log.d("FileTransfer", "download " + paramString1 + " to " + paramString2);
    boolean bool1 = paramJSONArray.optBoolean(2);
    String str = paramJSONArray.getString(3);
    JSONObject localJSONObject1 = paramJSONArray.optJSONObject(4);
    URL localURL;
    boolean bool2;
    try
    {
      localURL = new URL(paramString1);
      bool2 = localURL.getProtocol().equals("https");
      if (!Config.isUrlWhiteListed(paramString1))
      {
        Log.w("FileTransfer", "Source URL is not in white list: '" + paramString1 + "'");
        JSONObject localJSONObject2 = createFileTransferError(CONNECTION_ERR, paramString1, paramString2, null, Integer.valueOf(401));
        paramCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.IO_EXCEPTION, localJSONObject2));
        return;
      }
    }
    catch (MalformedURLException localMalformedURLException)
    {
      JSONObject localJSONObject3 = createFileTransferError(INVALID_URL_ERR, paramString1, paramString2, null, Integer.valueOf(0));
      Log.e("FileTransfer", localJSONObject3.toString(), localMalformedURLException);
      paramCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.IO_EXCEPTION, localJSONObject3));
      return;
    }
    RequestContext localRequestContext = new RequestContext(paramString1, paramString2, paramCallbackContext);
    synchronized (activeRequests)
    {
      activeRequests.put(str, localRequestContext);
      this.cordova.getThreadPool().execute(new Runnable(localRequestContext, paramString2, bool2, bool1, localURL, paramString1, localJSONObject1, str)
      {
        // ERROR //
        public void run()
        {
          // Byte code:
          //   0: aload_0
          //   1: getfield 32	org/apache/cordova/FileTransfer$4:val$context	Lorg/apache/cordova/FileTransfer$RequestContext;
          //   4: getfield 63	org/apache/cordova/FileTransfer$RequestContext:aborted	Z
          //   7: ifeq +4 -> 11
          //   10: return
          //   11: aconst_null
          //   12: astore_1
          //   13: aconst_null
          //   14: astore_2
          //   15: aconst_null
          //   16: astore_3
          //   17: aconst_null
          //   18: astore 4
          //   20: aload_0
          //   21: getfield 30	org/apache/cordova/FileTransfer$4:this$0	Lorg/apache/cordova/FileTransfer;
          //   24: aload_0
          //   25: getfield 34	org/apache/cordova/FileTransfer$4:val$target	Ljava/lang/String;
          //   28: invokestatic 67	org/apache/cordova/FileTransfer:access$800	(Lorg/apache/cordova/FileTransfer;Ljava/lang/String;)Ljava/io/File;
          //   31: astore 4
          //   33: aload_0
          //   34: getfield 32	org/apache/cordova/FileTransfer$4:val$context	Lorg/apache/cordova/FileTransfer$RequestContext;
          //   37: aload 4
          //   39: putfield 71	org/apache/cordova/FileTransfer$RequestContext:targetFile	Ljava/io/File;
          //   42: aload 4
          //   44: invokevirtual 77	java/io/File:getParentFile	()Ljava/io/File;
          //   47: invokevirtual 81	java/io/File:mkdirs	()Z
          //   50: pop
          //   51: aload_0
          //   52: getfield 36	org/apache/cordova/FileTransfer$4:val$useHttps	Z
          //   55: ifeq +378 -> 433
          //   58: aload_0
          //   59: getfield 38	org/apache/cordova/FileTransfer$4:val$trustEveryone	Z
          //   62: ifne +333 -> 395
          //   65: aload_0
          //   66: getfield 40	org/apache/cordova/FileTransfer$4:val$url	Ljava/net/URL;
          //   69: invokevirtual 87	java/net/URL:openConnection	()Ljava/net/URLConnection;
          //   72: checkcast 89	javax/net/ssl/HttpsURLConnection
          //   75: astore_1
          //   76: aload_1
          //   77: instanceof 91
          //   80: ifeq +12 -> 92
          //   83: aload_1
          //   84: checkcast 91	java/net/HttpURLConnection
          //   87: ldc 93
          //   89: invokevirtual 97	java/net/HttpURLConnection:setRequestMethod	(Ljava/lang/String;)V
          //   92: invokestatic 103	android/webkit/CookieManager:getInstance	()Landroid/webkit/CookieManager;
          //   95: aload_0
          //   96: getfield 42	org/apache/cordova/FileTransfer$4:val$source	Ljava/lang/String;
          //   99: invokevirtual 107	android/webkit/CookieManager:getCookie	(Ljava/lang/String;)Ljava/lang/String;
          //   102: astore 61
          //   104: aload 61
          //   106: ifnull +11 -> 117
          //   109: aload_1
          //   110: ldc 109
          //   112: aload 61
          //   114: invokevirtual 115	java/net/URLConnection:setRequestProperty	(Ljava/lang/String;Ljava/lang/String;)V
          //   117: aload_0
          //   118: getfield 44	org/apache/cordova/FileTransfer$4:val$headers	Lorg/json/JSONObject;
          //   121: ifnull +11 -> 132
          //   124: aload_1
          //   125: aload_0
          //   126: getfield 44	org/apache/cordova/FileTransfer$4:val$headers	Lorg/json/JSONObject;
          //   129: invokestatic 119	org/apache/cordova/FileTransfer:access$200	(Ljava/net/URLConnection;Lorg/json/JSONObject;)V
          //   132: aload_1
          //   133: invokevirtual 122	java/net/URLConnection:connect	()V
          //   136: ldc 124
          //   138: new 126	java/lang/StringBuilder
          //   141: dup
          //   142: invokespecial 127	java/lang/StringBuilder:<init>	()V
          //   145: ldc 129
          //   147: invokevirtual 133	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   150: aload_0
          //   151: getfield 40	org/apache/cordova/FileTransfer$4:val$url	Ljava/net/URL;
          //   154: invokevirtual 136	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
          //   157: invokevirtual 140	java/lang/StringBuilder:toString	()Ljava/lang/String;
          //   160: invokestatic 146	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
          //   163: pop
          //   164: new 148	org/apache/cordova/FileProgressResult
          //   167: dup
          //   168: invokespecial 149	org/apache/cordova/FileProgressResult:<init>	()V
          //   171: astore 63
          //   173: aload_1
          //   174: invokevirtual 152	java/net/URLConnection:getContentEncoding	()Ljava/lang/String;
          //   177: ifnonnull +19 -> 196
          //   180: aload 63
          //   182: iconst_1
          //   183: invokevirtual 156	org/apache/cordova/FileProgressResult:setLengthComputable	(Z)V
          //   186: aload 63
          //   188: aload_1
          //   189: invokevirtual 160	java/net/URLConnection:getContentLength	()I
          //   192: i2l
          //   193: invokevirtual 164	org/apache/cordova/FileProgressResult:setTotal	(J)V
          //   196: aconst_null
          //   197: astore 64
          //   199: aload_1
          //   200: invokestatic 168	org/apache/cordova/FileTransfer:access$500	(Ljava/net/URLConnection;)Ljava/io/InputStream;
          //   203: astore 64
          //   205: new 170	java/io/FileOutputStream
          //   208: dup
          //   209: aload 4
          //   211: invokespecial 173	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
          //   214: astore 67
          //   216: aload_0
          //   217: getfield 32	org/apache/cordova/FileTransfer$4:val$context	Lorg/apache/cordova/FileTransfer$RequestContext;
          //   220: astore 68
          //   222: aload 68
          //   224: monitorenter
          //   225: aload_0
          //   226: getfield 32	org/apache/cordova/FileTransfer$4:val$context	Lorg/apache/cordova/FileTransfer$RequestContext;
          //   229: getfield 63	org/apache/cordova/FileTransfer$RequestContext:aborted	Z
          //   232: ifeq +220 -> 452
          //   235: aload 68
          //   237: monitorexit
          //   238: aload_0
          //   239: getfield 32	org/apache/cordova/FileTransfer$4:val$context	Lorg/apache/cordova/FileTransfer$RequestContext;
          //   242: aconst_null
          //   243: putfield 177	org/apache/cordova/FileTransfer$RequestContext:currentInputStream	Ljava/io/InputStream;
          //   246: aload 64
          //   248: invokestatic 181	org/apache/cordova/FileTransfer:access$400	(Ljava/io/Closeable;)V
          //   251: aload 67
          //   253: invokestatic 181	org/apache/cordova/FileTransfer:access$400	(Ljava/io/Closeable;)V
          //   256: invokestatic 185	org/apache/cordova/FileTransfer:access$700	()Ljava/util/HashMap;
          //   259: astore 85
          //   261: aload 85
          //   263: monitorenter
          //   264: invokestatic 185	org/apache/cordova/FileTransfer:access$700	()Ljava/util/HashMap;
          //   267: aload_0
          //   268: getfield 46	org/apache/cordova/FileTransfer$4:val$objectId	Ljava/lang/String;
          //   271: invokevirtual 191	java/util/HashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
          //   274: pop
          //   275: aload 85
          //   277: monitorexit
          //   278: aload_1
          //   279: ifnull +35 -> 314
          //   282: aload_0
          //   283: getfield 38	org/apache/cordova/FileTransfer$4:val$trustEveryone	Z
          //   286: ifeq +28 -> 314
          //   289: aload_0
          //   290: getfield 36	org/apache/cordova/FileTransfer$4:val$useHttps	Z
          //   293: ifeq +21 -> 314
          //   296: aload_1
          //   297: checkcast 89	javax/net/ssl/HttpsURLConnection
          //   300: astore 91
          //   302: aload 91
          //   304: aload_2
          //   305: invokevirtual 195	javax/net/ssl/HttpsURLConnection:setHostnameVerifier	(Ljavax/net/ssl/HostnameVerifier;)V
          //   308: aload 91
          //   310: aload_3
          //   311: invokevirtual 199	javax/net/ssl/HttpsURLConnection:setSSLSocketFactory	(Ljavax/net/ssl/SSLSocketFactory;)V
          //   314: aconst_null
          //   315: astore 21
          //   317: iconst_0
          //   318: ifne +38 -> 356
          //   321: getstatic 205	org/apache/cordova/api/PluginResult$Status:ERROR	Lorg/apache/cordova/api/PluginResult$Status;
          //   324: astore 89
          //   326: getstatic 209	org/apache/cordova/FileTransfer:CONNECTION_ERR	I
          //   329: aload_0
          //   330: getfield 42	org/apache/cordova/FileTransfer$4:val$source	Ljava/lang/String;
          //   333: aload_0
          //   334: getfield 34	org/apache/cordova/FileTransfer$4:val$target	Ljava/lang/String;
          //   337: aload_1
          //   338: invokestatic 213	org/apache/cordova/FileTransfer:access$600	(ILjava/lang/String;Ljava/lang/String;Ljava/net/URLConnection;)Lorg/json/JSONObject;
          //   341: astore 90
          //   343: new 215	org/apache/cordova/api/PluginResult
          //   346: dup
          //   347: aload 89
          //   349: aload 90
          //   351: invokespecial 218	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;Lorg/json/JSONObject;)V
          //   354: astore 21
          //   356: aload 21
          //   358: invokevirtual 221	org/apache/cordova/api/PluginResult:getStatus	()I
          //   361: getstatic 224	org/apache/cordova/api/PluginResult$Status:OK	Lorg/apache/cordova/api/PluginResult$Status;
          //   364: invokevirtual 227	org/apache/cordova/api/PluginResult$Status:ordinal	()I
          //   367: if_icmpeq +14 -> 381
          //   370: aload 4
          //   372: ifnull +9 -> 381
          //   375: aload 4
          //   377: invokevirtual 230	java/io/File:delete	()Z
          //   380: pop
          //   381: aload_0
          //   382: getfield 32	org/apache/cordova/FileTransfer$4:val$context	Lorg/apache/cordova/FileTransfer$RequestContext;
          //   385: astore 22
          //   387: aload 22
          //   389: aload 21
          //   391: invokevirtual 234	org/apache/cordova/FileTransfer$RequestContext:sendPluginResult	(Lorg/apache/cordova/api/PluginResult;)V
          //   394: return
          //   395: aload_0
          //   396: getfield 40	org/apache/cordova/FileTransfer$4:val$url	Ljava/net/URL;
          //   399: invokevirtual 87	java/net/URL:openConnection	()Ljava/net/URLConnection;
          //   402: checkcast 89	javax/net/ssl/HttpsURLConnection
          //   405: astore 92
          //   407: aload 92
          //   409: invokestatic 238	org/apache/cordova/FileTransfer:access$000	(Ljavax/net/ssl/HttpsURLConnection;)Ljavax/net/ssl/SSLSocketFactory;
          //   412: astore_3
          //   413: aload 92
          //   415: invokevirtual 242	javax/net/ssl/HttpsURLConnection:getHostnameVerifier	()Ljavax/net/ssl/HostnameVerifier;
          //   418: astore_2
          //   419: aload 92
          //   421: invokestatic 245	org/apache/cordova/FileTransfer:access$100	()Ljavax/net/ssl/HostnameVerifier;
          //   424: invokevirtual 195	javax/net/ssl/HttpsURLConnection:setHostnameVerifier	(Ljavax/net/ssl/HostnameVerifier;)V
          //   427: aload 92
          //   429: astore_1
          //   430: goto -354 -> 76
          //   433: aload_0
          //   434: getfield 40	org/apache/cordova/FileTransfer$4:val$url	Ljava/net/URL;
          //   437: invokevirtual 87	java/net/URL:openConnection	()Ljava/net/URLConnection;
          //   440: astore 60
          //   442: aload 60
          //   444: astore_1
          //   445: aconst_null
          //   446: astore_2
          //   447: aconst_null
          //   448: astore_3
          //   449: goto -373 -> 76
          //   452: aload_0
          //   453: getfield 32	org/apache/cordova/FileTransfer$4:val$context	Lorg/apache/cordova/FileTransfer$RequestContext;
          //   456: aload 64
          //   458: putfield 177	org/apache/cordova/FileTransfer$RequestContext:currentInputStream	Ljava/io/InputStream;
          //   461: aload 68
          //   463: monitorexit
          //   464: sipush 16384
          //   467: newarray byte
          //   469: astore 70
          //   471: lconst_0
          //   472: lstore 71
          //   474: aload 64
          //   476: aload 70
          //   478: invokevirtual 251	java/io/InputStream:read	([B)I
          //   481: istore 73
          //   483: iload 73
          //   485: ifle +276 -> 761
          //   488: aload 67
          //   490: aload 70
          //   492: iconst_0
          //   493: iload 73
          //   495: invokevirtual 255	java/io/FileOutputStream:write	([BII)V
          //   498: lload 71
          //   500: iload 73
          //   502: i2l
          //   503: ladd
          //   504: lstore 71
          //   506: aload 63
          //   508: lload 71
          //   510: invokevirtual 258	org/apache/cordova/FileProgressResult:setLoaded	(J)V
          //   513: new 215	org/apache/cordova/api/PluginResult
          //   516: dup
          //   517: getstatic 224	org/apache/cordova/api/PluginResult$Status:OK	Lorg/apache/cordova/api/PluginResult$Status;
          //   520: aload 63
          //   522: invokevirtual 262	org/apache/cordova/FileProgressResult:toJSONObject	()Lorg/json/JSONObject;
          //   525: invokespecial 218	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;Lorg/json/JSONObject;)V
          //   528: astore 74
          //   530: aload 74
          //   532: iconst_1
          //   533: invokevirtual 265	org/apache/cordova/api/PluginResult:setKeepCallback	(Z)V
          //   536: aload_0
          //   537: getfield 32	org/apache/cordova/FileTransfer$4:val$context	Lorg/apache/cordova/FileTransfer$RequestContext;
          //   540: aload 74
          //   542: invokevirtual 234	org/apache/cordova/FileTransfer$RequestContext:sendPluginResult	(Lorg/apache/cordova/api/PluginResult;)V
          //   545: goto -71 -> 474
          //   548: astore 65
          //   550: aload 67
          //   552: astore 66
          //   554: aload_0
          //   555: getfield 32	org/apache/cordova/FileTransfer$4:val$context	Lorg/apache/cordova/FileTransfer$RequestContext;
          //   558: aconst_null
          //   559: putfield 177	org/apache/cordova/FileTransfer$RequestContext:currentInputStream	Ljava/io/InputStream;
          //   562: aload 64
          //   564: invokestatic 181	org/apache/cordova/FileTransfer:access$400	(Ljava/io/Closeable;)V
          //   567: aload 66
          //   569: invokestatic 181	org/apache/cordova/FileTransfer:access$400	(Ljava/io/Closeable;)V
          //   572: aload 65
          //   574: athrow
          //   575: astore 48
          //   577: getstatic 268	org/apache/cordova/FileTransfer:FILE_NOT_FOUND_ERR	I
          //   580: aload_0
          //   581: getfield 42	org/apache/cordova/FileTransfer$4:val$source	Ljava/lang/String;
          //   584: aload_0
          //   585: getfield 34	org/apache/cordova/FileTransfer$4:val$target	Ljava/lang/String;
          //   588: aload_1
          //   589: invokestatic 213	org/apache/cordova/FileTransfer:access$600	(ILjava/lang/String;Ljava/lang/String;Ljava/net/URLConnection;)Lorg/json/JSONObject;
          //   592: astore 49
          //   594: ldc 124
          //   596: aload 49
          //   598: invokevirtual 271	org/json/JSONObject:toString	()Ljava/lang/String;
          //   601: aload 48
          //   603: invokestatic 275	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
          //   606: pop
          //   607: new 215	org/apache/cordova/api/PluginResult
          //   610: dup
          //   611: getstatic 278	org/apache/cordova/api/PluginResult$Status:IO_EXCEPTION	Lorg/apache/cordova/api/PluginResult$Status;
          //   614: aload 49
          //   616: invokespecial 218	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;Lorg/json/JSONObject;)V
          //   619: astore 51
          //   621: invokestatic 185	org/apache/cordova/FileTransfer:access$700	()Ljava/util/HashMap;
          //   624: astore 52
          //   626: aload 52
          //   628: monitorenter
          //   629: invokestatic 185	org/apache/cordova/FileTransfer:access$700	()Ljava/util/HashMap;
          //   632: aload_0
          //   633: getfield 46	org/apache/cordova/FileTransfer$4:val$objectId	Ljava/lang/String;
          //   636: invokevirtual 191	java/util/HashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
          //   639: pop
          //   640: aload 52
          //   642: monitorexit
          //   643: aload_1
          //   644: ifnull +35 -> 679
          //   647: aload_0
          //   648: getfield 38	org/apache/cordova/FileTransfer$4:val$trustEveryone	Z
          //   651: ifeq +28 -> 679
          //   654: aload_0
          //   655: getfield 36	org/apache/cordova/FileTransfer$4:val$useHttps	Z
          //   658: ifeq +21 -> 679
          //   661: aload_1
          //   662: checkcast 89	javax/net/ssl/HttpsURLConnection
          //   665: astore 58
          //   667: aload 58
          //   669: aload_2
          //   670: invokevirtual 195	javax/net/ssl/HttpsURLConnection:setHostnameVerifier	(Ljavax/net/ssl/HostnameVerifier;)V
          //   673: aload 58
          //   675: aload_3
          //   676: invokevirtual 199	javax/net/ssl/HttpsURLConnection:setSSLSocketFactory	(Ljavax/net/ssl/SSLSocketFactory;)V
          //   679: aload 51
          //   681: ifnonnull +1026 -> 1707
          //   684: getstatic 205	org/apache/cordova/api/PluginResult$Status:ERROR	Lorg/apache/cordova/api/PluginResult$Status;
          //   687: astore 56
          //   689: getstatic 209	org/apache/cordova/FileTransfer:CONNECTION_ERR	I
          //   692: aload_0
          //   693: getfield 42	org/apache/cordova/FileTransfer$4:val$source	Ljava/lang/String;
          //   696: aload_0
          //   697: getfield 34	org/apache/cordova/FileTransfer$4:val$target	Ljava/lang/String;
          //   700: aload_1
          //   701: invokestatic 213	org/apache/cordova/FileTransfer:access$600	(ILjava/lang/String;Ljava/lang/String;Ljava/net/URLConnection;)Lorg/json/JSONObject;
          //   704: astore 57
          //   706: new 215	org/apache/cordova/api/PluginResult
          //   709: dup
          //   710: aload 56
          //   712: aload 57
          //   714: invokespecial 218	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;Lorg/json/JSONObject;)V
          //   717: astore 21
          //   719: aload 21
          //   721: invokevirtual 221	org/apache/cordova/api/PluginResult:getStatus	()I
          //   724: getstatic 224	org/apache/cordova/api/PluginResult$Status:OK	Lorg/apache/cordova/api/PluginResult$Status;
          //   727: invokevirtual 227	org/apache/cordova/api/PluginResult$Status:ordinal	()I
          //   730: if_icmpeq +14 -> 744
          //   733: aload 4
          //   735: ifnull +9 -> 744
          //   738: aload 4
          //   740: invokevirtual 230	java/io/File:delete	()Z
          //   743: pop
          //   744: aload_0
          //   745: getfield 32	org/apache/cordova/FileTransfer$4:val$context	Lorg/apache/cordova/FileTransfer$RequestContext;
          //   748: astore 22
          //   750: goto -363 -> 387
          //   753: astore 69
          //   755: aload 68
          //   757: monitorexit
          //   758: aload 69
          //   760: athrow
          //   761: aload_0
          //   762: getfield 32	org/apache/cordova/FileTransfer$4:val$context	Lorg/apache/cordova/FileTransfer$RequestContext;
          //   765: aconst_null
          //   766: putfield 177	org/apache/cordova/FileTransfer$RequestContext:currentInputStream	Ljava/io/InputStream;
          //   769: aload 64
          //   771: invokestatic 181	org/apache/cordova/FileTransfer:access$400	(Ljava/io/Closeable;)V
          //   774: aload 67
          //   776: invokestatic 181	org/apache/cordova/FileTransfer:access$400	(Ljava/io/Closeable;)V
          //   779: ldc 124
          //   781: new 126	java/lang/StringBuilder
          //   784: dup
          //   785: invokespecial 127	java/lang/StringBuilder:<init>	()V
          //   788: ldc_w 280
          //   791: invokevirtual 133	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   794: aload_0
          //   795: getfield 34	org/apache/cordova/FileTransfer$4:val$target	Ljava/lang/String;
          //   798: invokevirtual 133	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   801: invokevirtual 140	java/lang/StringBuilder:toString	()Ljava/lang/String;
          //   804: invokestatic 146	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
          //   807: pop
          //   808: aload 4
          //   810: invokestatic 286	org/apache/cordova/FileUtils:getEntry	(Ljava/io/File;)Lorg/json/JSONObject;
          //   813: astore 76
          //   815: new 215	org/apache/cordova/api/PluginResult
          //   818: dup
          //   819: getstatic 224	org/apache/cordova/api/PluginResult$Status:OK	Lorg/apache/cordova/api/PluginResult$Status;
          //   822: aload 76
          //   824: invokespecial 218	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;Lorg/json/JSONObject;)V
          //   827: astore 77
          //   829: invokestatic 185	org/apache/cordova/FileTransfer:access$700	()Ljava/util/HashMap;
          //   832: astore 78
          //   834: aload 78
          //   836: monitorenter
          //   837: invokestatic 185	org/apache/cordova/FileTransfer:access$700	()Ljava/util/HashMap;
          //   840: aload_0
          //   841: getfield 46	org/apache/cordova/FileTransfer$4:val$objectId	Ljava/lang/String;
          //   844: invokevirtual 191	java/util/HashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
          //   847: pop
          //   848: aload 78
          //   850: monitorexit
          //   851: aload_1
          //   852: ifnull +35 -> 887
          //   855: aload_0
          //   856: getfield 38	org/apache/cordova/FileTransfer$4:val$trustEveryone	Z
          //   859: ifeq +28 -> 887
          //   862: aload_0
          //   863: getfield 36	org/apache/cordova/FileTransfer$4:val$useHttps	Z
          //   866: ifeq +21 -> 887
          //   869: aload_1
          //   870: checkcast 89	javax/net/ssl/HttpsURLConnection
          //   873: astore 84
          //   875: aload 84
          //   877: aload_2
          //   878: invokevirtual 195	javax/net/ssl/HttpsURLConnection:setHostnameVerifier	(Ljavax/net/ssl/HostnameVerifier;)V
          //   881: aload 84
          //   883: aload_3
          //   884: invokevirtual 199	javax/net/ssl/HttpsURLConnection:setSSLSocketFactory	(Ljavax/net/ssl/SSLSocketFactory;)V
          //   887: aload 77
          //   889: ifnonnull +790 -> 1679
          //   892: getstatic 205	org/apache/cordova/api/PluginResult$Status:ERROR	Lorg/apache/cordova/api/PluginResult$Status;
          //   895: astore 82
          //   897: getstatic 209	org/apache/cordova/FileTransfer:CONNECTION_ERR	I
          //   900: aload_0
          //   901: getfield 42	org/apache/cordova/FileTransfer$4:val$source	Ljava/lang/String;
          //   904: aload_0
          //   905: getfield 34	org/apache/cordova/FileTransfer$4:val$target	Ljava/lang/String;
          //   908: aload_1
          //   909: invokestatic 213	org/apache/cordova/FileTransfer:access$600	(ILjava/lang/String;Ljava/lang/String;Ljava/net/URLConnection;)Lorg/json/JSONObject;
          //   912: astore 83
          //   914: new 215	org/apache/cordova/api/PluginResult
          //   917: dup
          //   918: aload 82
          //   920: aload 83
          //   922: invokespecial 218	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;Lorg/json/JSONObject;)V
          //   925: astore 21
          //   927: aload 21
          //   929: invokevirtual 221	org/apache/cordova/api/PluginResult:getStatus	()I
          //   932: getstatic 224	org/apache/cordova/api/PluginResult$Status:OK	Lorg/apache/cordova/api/PluginResult$Status;
          //   935: invokevirtual 227	org/apache/cordova/api/PluginResult$Status:ordinal	()I
          //   938: if_icmpeq +14 -> 952
          //   941: aload 4
          //   943: ifnull +9 -> 952
          //   946: aload 4
          //   948: invokevirtual 230	java/io/File:delete	()Z
          //   951: pop
          //   952: aload_0
          //   953: getfield 32	org/apache/cordova/FileTransfer$4:val$context	Lorg/apache/cordova/FileTransfer$RequestContext;
          //   956: astore 22
          //   958: goto -571 -> 387
          //   961: astore 37
          //   963: getstatic 209	org/apache/cordova/FileTransfer:CONNECTION_ERR	I
          //   966: aload_0
          //   967: getfield 42	org/apache/cordova/FileTransfer$4:val$source	Ljava/lang/String;
          //   970: aload_0
          //   971: getfield 34	org/apache/cordova/FileTransfer$4:val$target	Ljava/lang/String;
          //   974: aload_1
          //   975: invokestatic 213	org/apache/cordova/FileTransfer:access$600	(ILjava/lang/String;Ljava/lang/String;Ljava/net/URLConnection;)Lorg/json/JSONObject;
          //   978: astore 38
          //   980: ldc 124
          //   982: aload 38
          //   984: invokevirtual 271	org/json/JSONObject:toString	()Ljava/lang/String;
          //   987: aload 37
          //   989: invokestatic 275	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
          //   992: pop
          //   993: new 215	org/apache/cordova/api/PluginResult
          //   996: dup
          //   997: getstatic 278	org/apache/cordova/api/PluginResult$Status:IO_EXCEPTION	Lorg/apache/cordova/api/PluginResult$Status;
          //   1000: aload 38
          //   1002: invokespecial 218	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;Lorg/json/JSONObject;)V
          //   1005: astore 40
          //   1007: invokestatic 185	org/apache/cordova/FileTransfer:access$700	()Ljava/util/HashMap;
          //   1010: astore 41
          //   1012: aload 41
          //   1014: monitorenter
          //   1015: invokestatic 185	org/apache/cordova/FileTransfer:access$700	()Ljava/util/HashMap;
          //   1018: aload_0
          //   1019: getfield 46	org/apache/cordova/FileTransfer$4:val$objectId	Ljava/lang/String;
          //   1022: invokevirtual 191	java/util/HashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
          //   1025: pop
          //   1026: aload 41
          //   1028: monitorexit
          //   1029: aload_1
          //   1030: ifnull +35 -> 1065
          //   1033: aload_0
          //   1034: getfield 38	org/apache/cordova/FileTransfer$4:val$trustEveryone	Z
          //   1037: ifeq +28 -> 1065
          //   1040: aload_0
          //   1041: getfield 36	org/apache/cordova/FileTransfer$4:val$useHttps	Z
          //   1044: ifeq +21 -> 1065
          //   1047: aload_1
          //   1048: checkcast 89	javax/net/ssl/HttpsURLConnection
          //   1051: astore 47
          //   1053: aload 47
          //   1055: aload_2
          //   1056: invokevirtual 195	javax/net/ssl/HttpsURLConnection:setHostnameVerifier	(Ljavax/net/ssl/HostnameVerifier;)V
          //   1059: aload 47
          //   1061: aload_3
          //   1062: invokevirtual 199	javax/net/ssl/HttpsURLConnection:setSSLSocketFactory	(Ljavax/net/ssl/SSLSocketFactory;)V
          //   1065: aload 40
          //   1067: ifnonnull +633 -> 1700
          //   1070: getstatic 205	org/apache/cordova/api/PluginResult$Status:ERROR	Lorg/apache/cordova/api/PluginResult$Status;
          //   1073: astore 45
          //   1075: getstatic 209	org/apache/cordova/FileTransfer:CONNECTION_ERR	I
          //   1078: aload_0
          //   1079: getfield 42	org/apache/cordova/FileTransfer$4:val$source	Ljava/lang/String;
          //   1082: aload_0
          //   1083: getfield 34	org/apache/cordova/FileTransfer$4:val$target	Ljava/lang/String;
          //   1086: aload_1
          //   1087: invokestatic 213	org/apache/cordova/FileTransfer:access$600	(ILjava/lang/String;Ljava/lang/String;Ljava/net/URLConnection;)Lorg/json/JSONObject;
          //   1090: astore 46
          //   1092: new 215	org/apache/cordova/api/PluginResult
          //   1095: dup
          //   1096: aload 45
          //   1098: aload 46
          //   1100: invokespecial 218	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;Lorg/json/JSONObject;)V
          //   1103: astore 21
          //   1105: aload 21
          //   1107: invokevirtual 221	org/apache/cordova/api/PluginResult:getStatus	()I
          //   1110: getstatic 224	org/apache/cordova/api/PluginResult$Status:OK	Lorg/apache/cordova/api/PluginResult$Status;
          //   1113: invokevirtual 227	org/apache/cordova/api/PluginResult$Status:ordinal	()I
          //   1116: if_icmpeq +14 -> 1130
          //   1119: aload 4
          //   1121: ifnull +9 -> 1130
          //   1124: aload 4
          //   1126: invokevirtual 230	java/io/File:delete	()Z
          //   1129: pop
          //   1130: aload_0
          //   1131: getfield 32	org/apache/cordova/FileTransfer$4:val$context	Lorg/apache/cordova/FileTransfer$RequestContext;
          //   1134: astore 22
          //   1136: goto -749 -> 387
          //   1139: astore 27
          //   1141: ldc 124
          //   1143: aload 27
          //   1145: invokevirtual 289	org/json/JSONException:getMessage	()Ljava/lang/String;
          //   1148: aload 27
          //   1150: invokestatic 275	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
          //   1153: pop
          //   1154: new 215	org/apache/cordova/api/PluginResult
          //   1157: dup
          //   1158: getstatic 292	org/apache/cordova/api/PluginResult$Status:JSON_EXCEPTION	Lorg/apache/cordova/api/PluginResult$Status;
          //   1161: invokespecial 295	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;)V
          //   1164: astore 29
          //   1166: invokestatic 185	org/apache/cordova/FileTransfer:access$700	()Ljava/util/HashMap;
          //   1169: astore 30
          //   1171: aload 30
          //   1173: monitorenter
          //   1174: invokestatic 185	org/apache/cordova/FileTransfer:access$700	()Ljava/util/HashMap;
          //   1177: aload_0
          //   1178: getfield 46	org/apache/cordova/FileTransfer$4:val$objectId	Ljava/lang/String;
          //   1181: invokevirtual 191	java/util/HashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
          //   1184: pop
          //   1185: aload 30
          //   1187: monitorexit
          //   1188: aload_1
          //   1189: ifnull +35 -> 1224
          //   1192: aload_0
          //   1193: getfield 38	org/apache/cordova/FileTransfer$4:val$trustEveryone	Z
          //   1196: ifeq +28 -> 1224
          //   1199: aload_0
          //   1200: getfield 36	org/apache/cordova/FileTransfer$4:val$useHttps	Z
          //   1203: ifeq +21 -> 1224
          //   1206: aload_1
          //   1207: checkcast 89	javax/net/ssl/HttpsURLConnection
          //   1210: astore 36
          //   1212: aload 36
          //   1214: aload_2
          //   1215: invokevirtual 195	javax/net/ssl/HttpsURLConnection:setHostnameVerifier	(Ljavax/net/ssl/HostnameVerifier;)V
          //   1218: aload 36
          //   1220: aload_3
          //   1221: invokevirtual 199	javax/net/ssl/HttpsURLConnection:setSSLSocketFactory	(Ljavax/net/ssl/SSLSocketFactory;)V
          //   1224: aload 29
          //   1226: ifnonnull +467 -> 1693
          //   1229: getstatic 205	org/apache/cordova/api/PluginResult$Status:ERROR	Lorg/apache/cordova/api/PluginResult$Status;
          //   1232: astore 34
          //   1234: getstatic 209	org/apache/cordova/FileTransfer:CONNECTION_ERR	I
          //   1237: aload_0
          //   1238: getfield 42	org/apache/cordova/FileTransfer$4:val$source	Ljava/lang/String;
          //   1241: aload_0
          //   1242: getfield 34	org/apache/cordova/FileTransfer$4:val$target	Ljava/lang/String;
          //   1245: aload_1
          //   1246: invokestatic 213	org/apache/cordova/FileTransfer:access$600	(ILjava/lang/String;Ljava/lang/String;Ljava/net/URLConnection;)Lorg/json/JSONObject;
          //   1249: astore 35
          //   1251: new 215	org/apache/cordova/api/PluginResult
          //   1254: dup
          //   1255: aload 34
          //   1257: aload 35
          //   1259: invokespecial 218	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;Lorg/json/JSONObject;)V
          //   1262: astore 21
          //   1264: aload 21
          //   1266: invokevirtual 221	org/apache/cordova/api/PluginResult:getStatus	()I
          //   1269: getstatic 224	org/apache/cordova/api/PluginResult$Status:OK	Lorg/apache/cordova/api/PluginResult$Status;
          //   1272: invokevirtual 227	org/apache/cordova/api/PluginResult$Status:ordinal	()I
          //   1275: if_icmpeq +14 -> 1289
          //   1278: aload 4
          //   1280: ifnull +9 -> 1289
          //   1283: aload 4
          //   1285: invokevirtual 230	java/io/File:delete	()Z
          //   1288: pop
          //   1289: aload_0
          //   1290: getfield 32	org/apache/cordova/FileTransfer$4:val$context	Lorg/apache/cordova/FileTransfer$RequestContext;
          //   1293: astore 22
          //   1295: goto -908 -> 387
          //   1298: astore 14
          //   1300: getstatic 209	org/apache/cordova/FileTransfer:CONNECTION_ERR	I
          //   1303: aload_0
          //   1304: getfield 42	org/apache/cordova/FileTransfer$4:val$source	Ljava/lang/String;
          //   1307: aload_0
          //   1308: getfield 34	org/apache/cordova/FileTransfer$4:val$target	Ljava/lang/String;
          //   1311: aload_1
          //   1312: invokestatic 213	org/apache/cordova/FileTransfer:access$600	(ILjava/lang/String;Ljava/lang/String;Ljava/net/URLConnection;)Lorg/json/JSONObject;
          //   1315: astore 15
          //   1317: ldc 124
          //   1319: aload 15
          //   1321: invokevirtual 271	org/json/JSONObject:toString	()Ljava/lang/String;
          //   1324: aload 14
          //   1326: invokestatic 275	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
          //   1329: pop
          //   1330: new 215	org/apache/cordova/api/PluginResult
          //   1333: dup
          //   1334: getstatic 278	org/apache/cordova/api/PluginResult$Status:IO_EXCEPTION	Lorg/apache/cordova/api/PluginResult$Status;
          //   1337: aload 15
          //   1339: invokespecial 218	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;Lorg/json/JSONObject;)V
          //   1342: astore 17
          //   1344: invokestatic 185	org/apache/cordova/FileTransfer:access$700	()Ljava/util/HashMap;
          //   1347: astore 18
          //   1349: aload 18
          //   1351: monitorenter
          //   1352: invokestatic 185	org/apache/cordova/FileTransfer:access$700	()Ljava/util/HashMap;
          //   1355: aload_0
          //   1356: getfield 46	org/apache/cordova/FileTransfer$4:val$objectId	Ljava/lang/String;
          //   1359: invokevirtual 191	java/util/HashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
          //   1362: pop
          //   1363: aload 18
          //   1365: monitorexit
          //   1366: aload_1
          //   1367: ifnull +35 -> 1402
          //   1370: aload_0
          //   1371: getfield 38	org/apache/cordova/FileTransfer$4:val$trustEveryone	Z
          //   1374: ifeq +28 -> 1402
          //   1377: aload_0
          //   1378: getfield 36	org/apache/cordova/FileTransfer$4:val$useHttps	Z
          //   1381: ifeq +21 -> 1402
          //   1384: aload_1
          //   1385: checkcast 89	javax/net/ssl/HttpsURLConnection
          //   1388: astore 26
          //   1390: aload 26
          //   1392: aload_2
          //   1393: invokevirtual 195	javax/net/ssl/HttpsURLConnection:setHostnameVerifier	(Ljavax/net/ssl/HostnameVerifier;)V
          //   1396: aload 26
          //   1398: aload_3
          //   1399: invokevirtual 199	javax/net/ssl/HttpsURLConnection:setSSLSocketFactory	(Ljavax/net/ssl/SSLSocketFactory;)V
          //   1402: aload 17
          //   1404: ifnonnull +282 -> 1686
          //   1407: getstatic 205	org/apache/cordova/api/PluginResult$Status:ERROR	Lorg/apache/cordova/api/PluginResult$Status;
          //   1410: astore 24
          //   1412: getstatic 209	org/apache/cordova/FileTransfer:CONNECTION_ERR	I
          //   1415: aload_0
          //   1416: getfield 42	org/apache/cordova/FileTransfer$4:val$source	Ljava/lang/String;
          //   1419: aload_0
          //   1420: getfield 34	org/apache/cordova/FileTransfer$4:val$target	Ljava/lang/String;
          //   1423: aload_1
          //   1424: invokestatic 213	org/apache/cordova/FileTransfer:access$600	(ILjava/lang/String;Ljava/lang/String;Ljava/net/URLConnection;)Lorg/json/JSONObject;
          //   1427: astore 25
          //   1429: new 215	org/apache/cordova/api/PluginResult
          //   1432: dup
          //   1433: aload 24
          //   1435: aload 25
          //   1437: invokespecial 218	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;Lorg/json/JSONObject;)V
          //   1440: astore 21
          //   1442: aload 21
          //   1444: invokevirtual 221	org/apache/cordova/api/PluginResult:getStatus	()I
          //   1447: getstatic 224	org/apache/cordova/api/PluginResult$Status:OK	Lorg/apache/cordova/api/PluginResult$Status;
          //   1450: invokevirtual 227	org/apache/cordova/api/PluginResult$Status:ordinal	()I
          //   1453: if_icmpeq +14 -> 1467
          //   1456: aload 4
          //   1458: ifnull +9 -> 1467
          //   1461: aload 4
          //   1463: invokevirtual 230	java/io/File:delete	()Z
          //   1466: pop
          //   1467: aload_0
          //   1468: getfield 32	org/apache/cordova/FileTransfer$4:val$context	Lorg/apache/cordova/FileTransfer$RequestContext;
          //   1471: astore 22
          //   1473: goto -1086 -> 387
          //   1476: astore 5
          //   1478: invokestatic 185	org/apache/cordova/FileTransfer:access$700	()Ljava/util/HashMap;
          //   1481: astore 6
          //   1483: aload 6
          //   1485: monitorenter
          //   1486: invokestatic 185	org/apache/cordova/FileTransfer:access$700	()Ljava/util/HashMap;
          //   1489: aload_0
          //   1490: getfield 46	org/apache/cordova/FileTransfer$4:val$objectId	Ljava/lang/String;
          //   1493: invokevirtual 191	java/util/HashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
          //   1496: pop
          //   1497: aload 6
          //   1499: monitorexit
          //   1500: aload_1
          //   1501: ifnull +35 -> 1536
          //   1504: aload_0
          //   1505: getfield 38	org/apache/cordova/FileTransfer$4:val$trustEveryone	Z
          //   1508: ifeq +28 -> 1536
          //   1511: aload_0
          //   1512: getfield 36	org/apache/cordova/FileTransfer$4:val$useHttps	Z
          //   1515: ifeq +21 -> 1536
          //   1518: aload_1
          //   1519: checkcast 89	javax/net/ssl/HttpsURLConnection
          //   1522: astore 13
          //   1524: aload 13
          //   1526: aload_2
          //   1527: invokevirtual 195	javax/net/ssl/HttpsURLConnection:setHostnameVerifier	(Ljavax/net/ssl/HostnameVerifier;)V
          //   1530: aload 13
          //   1532: aload_3
          //   1533: invokevirtual 199	javax/net/ssl/HttpsURLConnection:setSSLSocketFactory	(Ljavax/net/ssl/SSLSocketFactory;)V
          //   1536: aconst_null
          //   1537: astore 9
          //   1539: iconst_0
          //   1540: ifne +38 -> 1578
          //   1543: getstatic 205	org/apache/cordova/api/PluginResult$Status:ERROR	Lorg/apache/cordova/api/PluginResult$Status;
          //   1546: astore 11
          //   1548: getstatic 209	org/apache/cordova/FileTransfer:CONNECTION_ERR	I
          //   1551: aload_0
          //   1552: getfield 42	org/apache/cordova/FileTransfer$4:val$source	Ljava/lang/String;
          //   1555: aload_0
          //   1556: getfield 34	org/apache/cordova/FileTransfer$4:val$target	Ljava/lang/String;
          //   1559: aload_1
          //   1560: invokestatic 213	org/apache/cordova/FileTransfer:access$600	(ILjava/lang/String;Ljava/lang/String;Ljava/net/URLConnection;)Lorg/json/JSONObject;
          //   1563: astore 12
          //   1565: new 215	org/apache/cordova/api/PluginResult
          //   1568: dup
          //   1569: aload 11
          //   1571: aload 12
          //   1573: invokespecial 218	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;Lorg/json/JSONObject;)V
          //   1576: astore 9
          //   1578: aload 9
          //   1580: invokevirtual 221	org/apache/cordova/api/PluginResult:getStatus	()I
          //   1583: getstatic 224	org/apache/cordova/api/PluginResult$Status:OK	Lorg/apache/cordova/api/PluginResult$Status;
          //   1586: invokevirtual 227	org/apache/cordova/api/PluginResult$Status:ordinal	()I
          //   1589: if_icmpeq +14 -> 1603
          //   1592: aload 4
          //   1594: ifnull +9 -> 1603
          //   1597: aload 4
          //   1599: invokevirtual 230	java/io/File:delete	()Z
          //   1602: pop
          //   1603: aload_0
          //   1604: getfield 32	org/apache/cordova/FileTransfer$4:val$context	Lorg/apache/cordova/FileTransfer$RequestContext;
          //   1607: aload 9
          //   1609: invokevirtual 234	org/apache/cordova/FileTransfer$RequestContext:sendPluginResult	(Lorg/apache/cordova/api/PluginResult;)V
          //   1612: aload 5
          //   1614: athrow
          //   1615: astore 7
          //   1617: aload 6
          //   1619: monitorexit
          //   1620: aload 7
          //   1622: athrow
          //   1623: astore 53
          //   1625: aload 52
          //   1627: monitorexit
          //   1628: aload 53
          //   1630: athrow
          //   1631: astore 42
          //   1633: aload 41
          //   1635: monitorexit
          //   1636: aload 42
          //   1638: athrow
          //   1639: astore 31
          //   1641: aload 30
          //   1643: monitorexit
          //   1644: aload 31
          //   1646: athrow
          //   1647: astore 19
          //   1649: aload 18
          //   1651: monitorexit
          //   1652: aload 19
          //   1654: athrow
          //   1655: astore 86
          //   1657: aload 85
          //   1659: monitorexit
          //   1660: aload 86
          //   1662: athrow
          //   1663: astore 79
          //   1665: aload 78
          //   1667: monitorexit
          //   1668: aload 79
          //   1670: athrow
          //   1671: astore 65
          //   1673: aconst_null
          //   1674: astore 66
          //   1676: goto -1122 -> 554
          //   1679: aload 77
          //   1681: astore 21
          //   1683: goto -756 -> 927
          //   1686: aload 17
          //   1688: astore 21
          //   1690: goto -248 -> 1442
          //   1693: aload 29
          //   1695: astore 21
          //   1697: goto -433 -> 1264
          //   1700: aload 40
          //   1702: astore 21
          //   1704: goto -599 -> 1105
          //   1707: aload 51
          //   1709: astore 21
          //   1711: goto -992 -> 719
          //
          // Exception table:
          //   from	to	target	type
          //   216	225	548	finally
          //   464	471	548	finally
          //   474	483	548	finally
          //   488	498	548	finally
          //   506	545	548	finally
          //   758	761	548	finally
          //   20	76	575	java/io/FileNotFoundException
          //   76	92	575	java/io/FileNotFoundException
          //   92	104	575	java/io/FileNotFoundException
          //   109	117	575	java/io/FileNotFoundException
          //   117	132	575	java/io/FileNotFoundException
          //   132	196	575	java/io/FileNotFoundException
          //   238	256	575	java/io/FileNotFoundException
          //   395	427	575	java/io/FileNotFoundException
          //   433	442	575	java/io/FileNotFoundException
          //   554	575	575	java/io/FileNotFoundException
          //   761	829	575	java/io/FileNotFoundException
          //   225	238	753	finally
          //   452	464	753	finally
          //   755	758	753	finally
          //   20	76	961	java/io/IOException
          //   76	92	961	java/io/IOException
          //   92	104	961	java/io/IOException
          //   109	117	961	java/io/IOException
          //   117	132	961	java/io/IOException
          //   132	196	961	java/io/IOException
          //   238	256	961	java/io/IOException
          //   395	427	961	java/io/IOException
          //   433	442	961	java/io/IOException
          //   554	575	961	java/io/IOException
          //   761	829	961	java/io/IOException
          //   20	76	1139	org/json/JSONException
          //   76	92	1139	org/json/JSONException
          //   92	104	1139	org/json/JSONException
          //   109	117	1139	org/json/JSONException
          //   117	132	1139	org/json/JSONException
          //   132	196	1139	org/json/JSONException
          //   238	256	1139	org/json/JSONException
          //   395	427	1139	org/json/JSONException
          //   433	442	1139	org/json/JSONException
          //   554	575	1139	org/json/JSONException
          //   761	829	1139	org/json/JSONException
          //   20	76	1298	java/lang/Throwable
          //   76	92	1298	java/lang/Throwable
          //   92	104	1298	java/lang/Throwable
          //   109	117	1298	java/lang/Throwable
          //   117	132	1298	java/lang/Throwable
          //   132	196	1298	java/lang/Throwable
          //   238	256	1298	java/lang/Throwable
          //   395	427	1298	java/lang/Throwable
          //   433	442	1298	java/lang/Throwable
          //   554	575	1298	java/lang/Throwable
          //   761	829	1298	java/lang/Throwable
          //   20	76	1476	finally
          //   76	92	1476	finally
          //   92	104	1476	finally
          //   109	117	1476	finally
          //   117	132	1476	finally
          //   132	196	1476	finally
          //   238	256	1476	finally
          //   395	427	1476	finally
          //   433	442	1476	finally
          //   554	575	1476	finally
          //   577	621	1476	finally
          //   761	829	1476	finally
          //   963	1007	1476	finally
          //   1141	1166	1476	finally
          //   1300	1344	1476	finally
          //   1486	1500	1615	finally
          //   1617	1620	1615	finally
          //   629	643	1623	finally
          //   1625	1628	1623	finally
          //   1015	1029	1631	finally
          //   1633	1636	1631	finally
          //   1174	1188	1639	finally
          //   1641	1644	1639	finally
          //   1352	1366	1647	finally
          //   1649	1652	1647	finally
          //   264	278	1655	finally
          //   1657	1660	1655	finally
          //   837	851	1663	finally
          //   1665	1668	1663	finally
          //   199	216	1671	finally
        }
      });
      return;
    }
  }

  private static String getArgument(JSONArray paramJSONArray, int paramInt, String paramString)
  {
    String str = paramString;
    if (paramJSONArray.length() >= paramInt)
    {
      str = paramJSONArray.optString(paramInt);
      if ((str == null) || ("null".equals(str)))
        str = paramString;
    }
    return str;
  }

  private File getFileFromPath(String paramString)
    throws FileNotFoundException
  {
    if (paramString.startsWith("file://"));
    for (File localFile = new File(paramString.substring("file://".length())); localFile.getParent() == null; localFile = new File(paramString))
      throw new FileNotFoundException();
    return localFile;
  }

  private static InputStream getInputStream(URLConnection paramURLConnection)
    throws IOException
  {
    if (Build.VERSION.SDK_INT < 11)
      return new DoneHandlerInputStream(paramURLConnection.getInputStream());
    return paramURLConnection.getInputStream();
  }

  private InputStream getPathFromUri(String paramString)
    throws FileNotFoundException
  {
    if (paramString.startsWith("content:"))
    {
      Uri localUri = Uri.parse(paramString);
      return this.cordova.getActivity().getContentResolver().openInputStream(localUri);
    }
    if (paramString.startsWith("file://"))
    {
      int i = paramString.indexOf("?");
      if (i == -1)
        return new FileInputStream(paramString.substring(7));
      return new FileInputStream(paramString.substring(7, i));
    }
    return new FileInputStream(paramString);
  }

  private static void safeClose(Closeable paramCloseable)
  {
    if (paramCloseable != null);
    try
    {
      paramCloseable.close();
      return;
    }
    catch (IOException localIOException)
    {
    }
  }

  private static SSLSocketFactory trustAllHosts(HttpsURLConnection paramHttpsURLConnection)
  {
    SSLSocketFactory localSSLSocketFactory = paramHttpsURLConnection.getSSLSocketFactory();
    try
    {
      SSLContext localSSLContext = SSLContext.getInstance("TLS");
      localSSLContext.init(null, trustAllCerts, new SecureRandom());
      paramHttpsURLConnection.setSSLSocketFactory(localSSLContext.getSocketFactory());
      return localSSLSocketFactory;
    }
    catch (Exception localException)
    {
      Log.e("FileTransfer", localException.getMessage(), localException);
    }
    return localSSLSocketFactory;
  }

  // ERROR //
  private void upload(String paramString1, String paramString2, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    // Byte code:
    //   0: ldc 25
    //   2: new 260	java/lang/StringBuilder
    //   5: dup
    //   6: invokespecial 261	java/lang/StringBuilder:<init>	()V
    //   9: ldc_w 471
    //   12: invokevirtual 289	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   15: aload_1
    //   16: invokevirtual 289	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   19: ldc_w 306
    //   22: invokevirtual 289	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   25: aload_2
    //   26: invokevirtual 289	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   29: invokevirtual 298	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   32: invokestatic 310	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   35: pop
    //   36: aload_3
    //   37: iconst_2
    //   38: ldc_w 473
    //   41: invokestatic 475	org/apache/cordova/FileTransfer:getArgument	(Lorg/json/JSONArray;ILjava/lang/String;)Ljava/lang/String;
    //   44: astore 6
    //   46: aload_3
    //   47: iconst_3
    //   48: ldc_w 477
    //   51: invokestatic 475	org/apache/cordova/FileTransfer:getArgument	(Lorg/json/JSONArray;ILjava/lang/String;)Ljava/lang/String;
    //   54: astore 7
    //   56: aload_3
    //   57: iconst_4
    //   58: ldc_w 479
    //   61: invokestatic 475	org/apache/cordova/FileTransfer:getArgument	(Lorg/json/JSONArray;ILjava/lang/String;)Ljava/lang/String;
    //   64: astore 8
    //   66: aload_3
    //   67: iconst_5
    //   68: invokevirtual 318	org/json/JSONArray:optJSONObject	(I)Lorg/json/JSONObject;
    //   71: ifnonnull +389 -> 460
    //   74: new 184	org/json/JSONObject
    //   77: dup
    //   78: invokespecial 235	org/json/JSONObject:<init>	()V
    //   81: astore 9
    //   83: aload_3
    //   84: bipush 6
    //   86: invokevirtual 314	org/json/JSONArray:optBoolean	(I)Z
    //   89: istore 10
    //   91: aload_3
    //   92: bipush 7
    //   94: invokevirtual 314	org/json/JSONArray:optBoolean	(I)Z
    //   97: ifne +12 -> 109
    //   100: aload_3
    //   101: bipush 7
    //   103: invokevirtual 482	org/json/JSONArray:isNull	(I)Z
    //   106: ifeq +364 -> 470
    //   109: iconst_1
    //   110: istore 11
    //   112: aload_3
    //   113: bipush 8
    //   115: invokevirtual 318	org/json/JSONArray:optJSONObject	(I)Lorg/json/JSONObject;
    //   118: ifnonnull +358 -> 476
    //   121: aload 9
    //   123: ldc_w 484
    //   126: invokevirtual 487	org/json/JSONObject:optJSONObject	(Ljava/lang/String;)Lorg/json/JSONObject;
    //   129: astore 12
    //   131: aload_3
    //   132: bipush 9
    //   134: invokevirtual 221	org/json/JSONArray:getString	(I)Ljava/lang/String;
    //   137: astore 13
    //   139: ldc 25
    //   141: new 260	java/lang/StringBuilder
    //   144: dup
    //   145: invokespecial 261	java/lang/StringBuilder:<init>	()V
    //   148: ldc_w 489
    //   151: invokevirtual 289	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   154: aload 6
    //   156: invokevirtual 289	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   159: invokevirtual 298	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   162: invokestatic 310	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   165: pop
    //   166: ldc 25
    //   168: new 260	java/lang/StringBuilder
    //   171: dup
    //   172: invokespecial 261	java/lang/StringBuilder:<init>	()V
    //   175: ldc_w 491
    //   178: invokevirtual 289	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   181: aload 7
    //   183: invokevirtual 289	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   186: invokevirtual 298	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   189: invokestatic 310	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   192: pop
    //   193: ldc 25
    //   195: new 260	java/lang/StringBuilder
    //   198: dup
    //   199: invokespecial 261	java/lang/StringBuilder:<init>	()V
    //   202: ldc_w 493
    //   205: invokevirtual 289	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   208: aload 8
    //   210: invokevirtual 289	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   213: invokevirtual 298	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   216: invokestatic 310	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   219: pop
    //   220: ldc 25
    //   222: new 260	java/lang/StringBuilder
    //   225: dup
    //   226: invokespecial 261	java/lang/StringBuilder:<init>	()V
    //   229: ldc_w 495
    //   232: invokevirtual 289	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   235: aload 9
    //   237: invokevirtual 498	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   240: invokevirtual 298	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   243: invokestatic 310	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   246: pop
    //   247: ldc 25
    //   249: new 260	java/lang/StringBuilder
    //   252: dup
    //   253: invokespecial 261	java/lang/StringBuilder:<init>	()V
    //   256: ldc_w 500
    //   259: invokevirtual 289	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   262: iload 10
    //   264: invokevirtual 503	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
    //   267: invokevirtual 298	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   270: invokestatic 310	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   273: pop
    //   274: ldc 25
    //   276: new 260	java/lang/StringBuilder
    //   279: dup
    //   280: invokespecial 261	java/lang/StringBuilder:<init>	()V
    //   283: ldc_w 505
    //   286: invokevirtual 289	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   289: iload 11
    //   291: invokevirtual 503	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
    //   294: invokevirtual 298	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   297: invokestatic 310	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   300: pop
    //   301: ldc 25
    //   303: new 260	java/lang/StringBuilder
    //   306: dup
    //   307: invokespecial 261	java/lang/StringBuilder:<init>	()V
    //   310: ldc_w 507
    //   313: invokevirtual 289	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   316: aload 12
    //   318: invokevirtual 498	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   321: invokevirtual 298	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   324: invokestatic 310	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   327: pop
    //   328: ldc 25
    //   330: new 260	java/lang/StringBuilder
    //   333: dup
    //   334: invokespecial 261	java/lang/StringBuilder:<init>	()V
    //   337: ldc_w 509
    //   340: invokevirtual 289	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   343: aload 13
    //   345: invokevirtual 289	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   348: invokevirtual 298	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   351: invokestatic 310	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   354: pop
    //   355: new 320	java/net/URL
    //   358: dup
    //   359: aload_2
    //   360: invokespecial 322	java/net/URL:<init>	(Ljava/lang/String;)V
    //   363: astore 22
    //   365: aload 22
    //   367: invokevirtual 325	java/net/URL:getProtocol	()Ljava/lang/String;
    //   370: ldc_w 327
    //   373: invokevirtual 333	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   376: istore 23
    //   378: new 70	org/apache/cordova/FileTransfer$RequestContext
    //   381: dup
    //   382: aload_1
    //   383: aload_2
    //   384: aload 4
    //   386: invokespecial 355	org/apache/cordova/FileTransfer$RequestContext:<init>	(Ljava/lang/String;Ljava/lang/String;Lorg/apache/cordova/api/CallbackContext;)V
    //   389: astore 24
    //   391: getstatic 49	org/apache/cordova/FileTransfer:activeRequests	Ljava/util/HashMap;
    //   394: astore 25
    //   396: aload 25
    //   398: monitorenter
    //   399: getstatic 49	org/apache/cordova/FileTransfer:activeRequests	Ljava/util/HashMap;
    //   402: aload 13
    //   404: aload 24
    //   406: invokevirtual 358	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   409: pop
    //   410: aload 25
    //   412: monitorexit
    //   413: aload_0
    //   414: getfield 119	org/apache/cordova/FileTransfer:cordova	Lorg/apache/cordova/api/CordovaInterface;
    //   417: invokeinterface 125 1 0
    //   422: new 511	org/apache/cordova/FileTransfer$1
    //   425: dup
    //   426: aload_0
    //   427: aload 24
    //   429: iload 23
    //   431: iload 10
    //   433: aload 22
    //   435: aload_2
    //   436: aload 12
    //   438: aload 9
    //   440: aload 6
    //   442: aload 7
    //   444: aload 8
    //   446: aload_1
    //   447: iload 11
    //   449: aload 13
    //   451: invokespecial 514	org/apache/cordova/FileTransfer$1:<init>	(Lorg/apache/cordova/FileTransfer;Lorg/apache/cordova/FileTransfer$RequestContext;ZZLjava/net/URL;Ljava/lang/String;Lorg/json/JSONObject;Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;)V
    //   454: invokeinterface 136 2 0
    //   459: return
    //   460: aload_3
    //   461: iconst_5
    //   462: invokevirtual 318	org/json/JSONArray:optJSONObject	(I)Lorg/json/JSONObject;
    //   465: astore 9
    //   467: goto -384 -> 83
    //   470: iconst_0
    //   471: istore 11
    //   473: goto -361 -> 112
    //   476: aload_3
    //   477: bipush 8
    //   479: invokevirtual 318	org/json/JSONArray:optJSONObject	(I)Lorg/json/JSONObject;
    //   482: astore 12
    //   484: goto -353 -> 131
    //   487: astore 28
    //   489: getstatic 38	org/apache/cordova/FileTransfer:INVALID_URL_ERR	I
    //   492: aload_1
    //   493: aload_2
    //   494: aconst_null
    //   495: iconst_0
    //   496: invokestatic 92	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   499: invokestatic 96	org/apache/cordova/FileTransfer:createFileTransferError	(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;)Lorg/json/JSONObject;
    //   502: astore 29
    //   504: ldc 25
    //   506: aload 29
    //   508: invokevirtual 352	org/json/JSONObject:toString	()Ljava/lang/String;
    //   511: aload 28
    //   513: invokestatic 258	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   516: pop
    //   517: aload 4
    //   519: new 98	org/apache/cordova/api/PluginResult
    //   522: dup
    //   523: getstatic 348	org/apache/cordova/api/PluginResult$Status:IO_EXCEPTION	Lorg/apache/cordova/api/PluginResult$Status;
    //   526: aload 29
    //   528: invokespecial 107	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;Lorg/json/JSONObject;)V
    //   531: invokevirtual 351	org/apache/cordova/api/CallbackContext:sendPluginResult	(Lorg/apache/cordova/api/PluginResult;)V
    //   534: return
    //   535: astore 26
    //   537: aload 25
    //   539: monitorexit
    //   540: aload 26
    //   542: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   355	365	487	java/net/MalformedURLException
    //   399	413	535	finally
    //   537	540	535	finally
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    if ((paramString.equals("upload")) || (paramString.equals("download")))
    {
      String str1 = paramJSONArray.getString(0);
      String str2 = paramJSONArray.getString(1);
      if (paramString.equals("upload"))
        try
        {
          upload(URLDecoder.decode(str1, "UTF-8"), str2, paramJSONArray, paramCallbackContext);
          return true;
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException)
        {
          paramCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.MALFORMED_URL_EXCEPTION, "UTF-8 error."));
          return true;
        }
      download(str1, str2, paramJSONArray, paramCallbackContext);
      return true;
    }
    if (paramString.equals("abort"))
    {
      abort(paramJSONArray.getString(0));
      paramCallbackContext.success();
      return true;
    }
    return false;
  }

  private static final class DoneHandlerInputStream extends FilterInputStream
  {
    private boolean done;

    public DoneHandlerInputStream(InputStream paramInputStream)
    {
      super();
    }

    public int read()
      throws IOException
    {
      int i;
      if (this.done)
      {
        i = -1;
        if (i != -1)
          break label31;
      }
      label31: for (boolean bool = true; ; bool = false)
      {
        this.done = bool;
        return i;
        i = super.read();
        break;
      }
    }

    public int read(byte[] paramArrayOfByte)
      throws IOException
    {
      int i;
      if (this.done)
      {
        i = -1;
        if (i != -1)
          break label32;
      }
      label32: for (boolean bool = true; ; bool = false)
      {
        this.done = bool;
        return i;
        i = super.read(paramArrayOfByte);
        break;
      }
    }

    public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      int i;
      if (this.done)
      {
        i = -1;
        if (i != -1)
          break label40;
      }
      label40: for (boolean bool = true; ; bool = false)
      {
        this.done = bool;
        return i;
        i = super.read(paramArrayOfByte, paramInt1, paramInt2);
        break;
      }
    }
  }

  private static final class RequestContext
  {
    boolean aborted;
    CallbackContext callbackContext;
    InputStream currentInputStream;
    OutputStream currentOutputStream;
    String source;
    String target;
    File targetFile;

    RequestContext(String paramString1, String paramString2, CallbackContext paramCallbackContext)
    {
      this.source = paramString1;
      this.target = paramString2;
      this.callbackContext = paramCallbackContext;
    }

    void sendPluginResult(PluginResult paramPluginResult)
    {
      monitorenter;
      try
      {
        if (!this.aborted)
          this.callbackContext.sendPluginResult(paramPluginResult);
        return;
      }
      finally
      {
        monitorexit;
      }
      throw localObject;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.FileTransfer
 * JD-Core Version:    0.6.0
 */