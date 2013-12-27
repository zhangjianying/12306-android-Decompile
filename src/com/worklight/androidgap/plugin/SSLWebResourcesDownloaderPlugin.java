package com.worklight.androidgap.plugin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.worklight.androidgap.WLDroidGap;
import com.worklight.common.SSLConfig;
import com.worklight.common.WLConfig;
import com.worklight.common.WLSSLSocketFactory;
import com.worklight.common.WLUtils;
import com.worklight.wlclient.WLCookieManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.KeyStore;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult.Status;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;

public class SSLWebResourcesDownloaderPlugin extends CordovaPlugin
{
  private static final String ACTION_SWITCH_APP = "switchApp";
  private static final String ACTION_UPDATE_APP = "updateApp";
  private static boolean isUpdating = false;

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    try
    {
      if ((!"updateApp".equals(paramString)) && (!"switchApp".equals(paramString)))
      {
        paramCallbackContext.error("Invalid action: " + paramString);
        return true;
      }
      String str1 = CookieManager.getInstance().getCookie(new WLConfig(this.cordova.getActivity()).getAppURL().toString());
      String str2 = paramJSONArray.getString(0);
      if (str2 != null)
        str2 = str2.trim();
      boolean bool = paramJSONArray.getString(1).equals("true");
      String str3 = WLUtils.readWLPref(this.cordova.getActivity(), "wlSkinName");
      if ("updateApp".equals(paramString))
      {
        String str7 = new WLConfig(this.cordova.getActivity()).getAppURL().toString() + "updates?action=getzip&skin=" + str3;
        new SSLWebResourcesDownloader((WLDroidGap)this.cordova, str1, str2, bool, "directUpdateDownloadingMessage", str7, str3).execute(new Void[0]);
      }
      while (true)
      {
        paramCallbackContext.success(PluginResult.Status.OK.name());
        return true;
        if (!"switchApp".equals(paramString))
          continue;
        String str4 = paramJSONArray.getString(1);
        String str5 = paramJSONArray.getString(2);
        String str6 = new WLConfig(this.cordova.getActivity()).getRootURL().toString() + "/dev/appdata?appId=" + str4 + "&appVer=" + str5 + "&appEnv=android&skin=" + str3;
        new SSLWebResourcesDownloader((WLDroidGap)this.cordova, str1, str2, false, "downloadingWebResourcesMessage", str6, str3).execute(new Void[0]);
      }
    }
    catch (JSONException localJSONException)
    {
      paramCallbackContext.error("Action: " + paramString + " failed. " + localJSONException.getMessage());
    }
    return true;
  }

  public boolean isUpdating()
  {
    return isUpdating;
  }

  public class SSLWebResourcesDownloader extends AsyncTask<Void, Integer, String>
  {
    private static final String CONTENT_TYPE_APPLICATION_ZIP = "application/zip";
    private static final String DOWNLOADED_ZIP_FILE_NAME = "assets.zip";
    private static final int HONEYCOMB = 11;
    private String cookies;
    private String downloadingMessage;
    private String instanceAuthId = null;
    private boolean isDirectUpdateSuccess = true;
    private ProgressDialog progressDialog;
    private boolean shouldUpdateSilently = false;
    private String skinName;
    private SSLConfig sslConfig;
    private String url = null;
    private WLConfig wlConfig;
    private WLDroidGap wlDroidGap;

    public SSLWebResourcesDownloader(WLDroidGap paramString1, String paramString2, String paramBoolean, boolean paramString3, String paramString4, String paramString5, String arg8)
    {
      this.wlDroidGap = paramString1;
      this.wlConfig = new WLConfig(this.wlDroidGap);
      this.cookies = paramString2;
      this.instanceAuthId = paramBoolean;
      this.downloadingMessage = WLUtils.getResourceString(paramString4, paramString1);
      Object localObject;
      this.skinName = localObject;
      this.url = paramString5;
      this.shouldUpdateSilently = paramString3;
      this.sslConfig = new SSLConfig(paramString1.getContext());
    }

    private void createProgressDialog()
    {
      this.progressDialog = new ProgressDialog(this.wlDroidGap);
      this.progressDialog.setMessage(this.downloadingMessage);
      this.progressDialog.setIndeterminate(false);
      this.progressDialog.setProgressStyle(1);
      if (Build.VERSION.SDK_INT >= 11);
      try
      {
        this.progressDialog.getClass().getDeclaredMethod("setProgressNumberFormat", new Class[] { String.class }).invoke(this.progressDialog, new Object[] { "%1d/%2d kb" });
        return;
      }
      catch (Exception localException)
      {
        WLUtils.warning("Progress bar will be displayed without units, because " + localException.getMessage());
      }
    }

    // ERROR //
    private void downloadZipFile()
      throws IOException
    {
      // Byte code:
      //   0: aconst_null
      //   1: astore_1
      //   2: aload_0
      //   3: invokespecial 159	com/worklight/androidgap/plugin/SSLWebResourcesDownloaderPlugin$SSLWebResourcesDownloader:sendRequest	()Lorg/apache/http/HttpResponse;
      //   6: astore 4
      //   8: aload 4
      //   10: invokeinterface 165 1 0
      //   15: astore 5
      //   17: aload 5
      //   19: invokeinterface 171 1 0
      //   24: lstore 6
      //   26: aload_0
      //   27: getfield 90	com/worklight/androidgap/plugin/SSLWebResourcesDownloaderPlugin$SSLWebResourcesDownloader:progressDialog	Landroid/app/ProgressDialog;
      //   30: lload 6
      //   32: ldc2_w 172
      //   35: ldiv
      //   36: l2i
      //   37: invokevirtual 176	android/app/ProgressDialog:setMax	(I)V
      //   40: new 178	java/io/BufferedInputStream
      //   43: dup
      //   44: aload 5
      //   46: invokeinterface 182 1 0
      //   51: invokespecial 185	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
      //   54: astore 8
      //   56: aload 5
      //   58: invokeinterface 189 1 0
      //   63: invokeinterface 194 1 0
      //   68: ldc 9
      //   70: invokevirtual 198	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   73: ifne +86 -> 159
      //   76: new 133	java/lang/StringBuilder
      //   79: dup
      //   80: ldc 200
      //   82: invokespecial 138	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
      //   85: aload 4
      //   87: invokeinterface 204 1 0
      //   92: invokevirtual 207	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   95: ldc 209
      //   97: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   100: aload 8
      //   102: invokestatic 213	com/worklight/common/WLUtils:convertStreamToString	(Ljava/io/InputStream;)Ljava/lang/String;
      //   105: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   108: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   111: astore 15
      //   113: aload 15
      //   115: invokestatic 216	com/worklight/common/WLUtils:debug	(Ljava/lang/String;)V
      //   118: new 155	java/io/IOException
      //   121: dup
      //   122: aload 15
      //   124: invokespecial 217	java/io/IOException:<init>	(Ljava/lang/String;)V
      //   127: astore 16
      //   129: aload 16
      //   131: athrow
      //   132: astore_2
      //   133: aload 8
      //   135: astore_3
      //   136: aload_1
      //   137: ifnull +7 -> 144
      //   140: aload_1
      //   141: invokevirtual 222	java/io/FileOutputStream:close	()V
      //   144: aload_3
      //   145: ifnull +12 -> 157
      //   148: aload_3
      //   149: invokevirtual 225	java/io/InputStream:close	()V
      //   152: ldc 227
      //   154: invokestatic 216	com/worklight/common/WLUtils:debug	(Ljava/lang/String;)V
      //   157: aload_2
      //   158: athrow
      //   159: ldc 229
      //   161: invokestatic 216	com/worklight/common/WLUtils:debug	(Ljava/lang/String;)V
      //   164: new 219	java/io/FileOutputStream
      //   167: dup
      //   168: new 133	java/lang/StringBuilder
      //   171: dup
      //   172: aload_0
      //   173: getfield 50	com/worklight/androidgap/plugin/SSLWebResourcesDownloaderPlugin$SSLWebResourcesDownloader:wlDroidGap	Lcom/worklight/androidgap/WLDroidGap;
      //   176: invokevirtual 232	com/worklight/androidgap/WLDroidGap:getLocalStorageRoot	()Ljava/lang/String;
      //   179: invokestatic 236	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
      //   182: invokespecial 138	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
      //   185: ldc 238
      //   187: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   190: ldc 12
      //   192: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   195: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   198: invokespecial 239	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
      //   201: astore 9
      //   203: sipush 8192
      //   206: newarray byte
      //   208: astore 10
      //   210: lconst_0
      //   211: lstore 11
      //   213: aload 8
      //   215: aload 10
      //   217: invokevirtual 243	java/io/InputStream:read	([B)I
      //   220: istore 13
      //   222: iload 13
      //   224: iconst_m1
      //   225: if_icmpne +29 -> 254
      //   228: aload 9
      //   230: ifnull +8 -> 238
      //   233: aload 9
      //   235: invokevirtual 222	java/io/FileOutputStream:close	()V
      //   238: aload 8
      //   240: ifnull +13 -> 253
      //   243: aload 8
      //   245: invokevirtual 225	java/io/InputStream:close	()V
      //   248: ldc 227
      //   250: invokestatic 216	com/worklight/common/WLUtils:debug	(Ljava/lang/String;)V
      //   253: return
      //   254: lload 11
      //   256: iload 13
      //   258: i2l
      //   259: ladd
      //   260: lstore 11
      //   262: iconst_1
      //   263: anewarray 245	java/lang/Integer
      //   266: astore 14
      //   268: aload 14
      //   270: iconst_0
      //   271: lload 11
      //   273: ldc2_w 172
      //   276: ldiv
      //   277: l2i
      //   278: invokestatic 248	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   281: aastore
      //   282: aload_0
      //   283: aload 14
      //   285: invokevirtual 252	com/worklight/androidgap/plugin/SSLWebResourcesDownloaderPlugin$SSLWebResourcesDownloader:publishProgress	([Ljava/lang/Object;)V
      //   288: aload 9
      //   290: aload 10
      //   292: iconst_0
      //   293: iload 13
      //   295: invokevirtual 256	java/io/FileOutputStream:write	([BII)V
      //   298: goto -85 -> 213
      //   301: astore_2
      //   302: aload 9
      //   304: astore_1
      //   305: aload 8
      //   307: astore_3
      //   308: goto -172 -> 136
      //   311: astore_2
      //   312: aconst_null
      //   313: astore_3
      //   314: aconst_null
      //   315: astore_1
      //   316: goto -180 -> 136
      //
      // Exception table:
      //   from	to	target	type
      //   56	132	132	finally
      //   159	203	132	finally
      //   203	210	301	finally
      //   213	222	301	finally
      //   262	298	301	finally
      //   2	56	311	finally
    }

    private void extractZipFile(String paramString1, String paramString2)
      throws IOException
    {
      try
      {
        WLUtils.deleteDirectory(new File(paramString2 + "/" + this.skinName));
        new File(paramString2 + "/" + this.skinName).mkdirs();
        localZipFile = new ZipFile(paramString1);
        localEnumeration = localZipFile.entries();
        if (!localEnumeration.hasMoreElements())
        {
          localZipFile.close();
          return;
        }
      }
      catch (Exception localException)
      {
        while (true)
        {
          ZipFile localZipFile;
          Enumeration localEnumeration;
          WLUtils.error(localException.getLocalizedMessage());
          continue;
          ZipEntry localZipEntry = (ZipEntry)localEnumeration.nextElement();
          if (localZipEntry.isDirectory())
          {
            new File(paramString2 + "/" + localZipEntry.getName()).mkdirs();
            continue;
          }
          InputStream localInputStream = localZipFile.getInputStream(localZipEntry);
          FileOutputStream localFileOutputStream = new FileOutputStream(paramString2 + "/" + localZipEntry.getName());
          WLUtils.copyFile(localInputStream, localFileOutputStream);
          localInputStream.close();
          localFileOutputStream.close();
        }
      }
    }

    private String handleUpdateException(String paramString1, String paramString2, int paramInt)
    {
      WLUtils.log(paramString2, paramInt);
      this.progressDialog.dismiss();
      this.isDirectUpdateSuccess = false;
      WLDroidGap localWLDroidGap = this.wlDroidGap;
      StringBuilder localStringBuilder = new StringBuilder("javascript:WL.App._showDirectUpdateErrorMessage(");
      if (paramString1 != null);
      for (String str = "WL.ClientMessages." + paramString1; ; str = "\"" + paramString2 + "\"")
      {
        localWLDroidGap.loadUrl(str + ")");
        return "OK";
      }
    }

    private void processZipFile()
      throws IOException
    {
      String str = this.wlDroidGap.getLocalStorageRoot() + "/" + "assets.zip";
      extractZipFile(str, this.wlDroidGap.getLocalStorageWebRoot());
      new File(str).delete();
      this.wlDroidGap.setClearCacheNextLoad();
    }

    // ERROR //
    private String runDirectUpdate()
    {
      // Byte code:
      //   0: aload_0
      //   1: invokespecial 347	com/worklight/androidgap/plugin/SSLWebResourcesDownloaderPlugin$SSLWebResourcesDownloader:downloadZipFile	()V
      //   4: iconst_0
      //   5: invokestatic 352	com/worklight/androidgap/plugin/SSLWebResourcesDownloaderPlugin:access$0	(Z)V
      //   8: aload_0
      //   9: invokespecial 354	com/worklight/androidgap/plugin/SSLWebResourcesDownloaderPlugin$SSLWebResourcesDownloader:processZipFile	()V
      //   12: aload_0
      //   13: getfield 57	com/worklight/androidgap/plugin/SSLWebResourcesDownloaderPlugin$SSLWebResourcesDownloader:wlConfig	Lcom/worklight/common/WLConfig;
      //   16: invokevirtual 357	com/worklight/common/WLConfig:getTestWebResourcesChecksumFlag	()Ljava/lang/String;
      //   19: ldc_w 359
      //   22: invokevirtual 198	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   25: ifeq +42 -> 67
      //   28: aload_0
      //   29: getfield 50	com/worklight/androidgap/plugin/SSLWebResourcesDownloaderPlugin$SSLWebResourcesDownloader:wlDroidGap	Lcom/worklight/androidgap/WLDroidGap;
      //   32: invokevirtual 334	com/worklight/androidgap/WLDroidGap:getLocalStorageWebRoot	()Ljava/lang/String;
      //   35: invokestatic 363	com/worklight/common/WLUtils:computeChecksumOnResources	(Ljava/lang/String;)J
      //   38: invokestatic 368	java/lang/Long:toString	(J)Ljava/lang/String;
      //   41: ldc_w 370
      //   44: invokestatic 376	com/worklight/utils/SecurityUtils:hashData	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
      //   47: astore 7
      //   49: ldc_w 378
      //   52: invokestatic 216	com/worklight/common/WLUtils:debug	(Ljava/lang/String;)V
      //   55: aload_0
      //   56: getfield 50	com/worklight/androidgap/plugin/SSLWebResourcesDownloaderPlugin$SSLWebResourcesDownloader:wlDroidGap	Lcom/worklight/androidgap/WLDroidGap;
      //   59: ldc_w 380
      //   62: aload 7
      //   64: invokestatic 384	com/worklight/common/WLUtils:writeWLPref	(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;)V
      //   67: ldc_w 328
      //   70: areturn
      //   71: astore 4
      //   73: aload_0
      //   74: aconst_null
      //   75: aload 4
      //   77: invokevirtual 385	org/apache/http/client/ClientProtocolException:getMessage	()Ljava/lang/String;
      //   80: bipush 6
      //   82: invokespecial 387	com/worklight/androidgap/plugin/SSLWebResourcesDownloaderPlugin$SSLWebResourcesDownloader:handleUpdateException	(Ljava/lang/String;Ljava/lang/String;I)Ljava/lang/String;
      //   85: astore 5
      //   87: iconst_0
      //   88: invokestatic 352	com/worklight/androidgap/plugin/SSLWebResourcesDownloaderPlugin:access$0	(Z)V
      //   91: aload 5
      //   93: areturn
      //   94: astore_2
      //   95: aload_0
      //   96: ldc_w 389
      //   99: ldc_w 391
      //   102: bipush 6
      //   104: invokespecial 387	com/worklight/androidgap/plugin/SSLWebResourcesDownloaderPlugin$SSLWebResourcesDownloader:handleUpdateException	(Ljava/lang/String;Ljava/lang/String;I)Ljava/lang/String;
      //   107: astore_3
      //   108: iconst_0
      //   109: invokestatic 352	com/worklight/androidgap/plugin/SSLWebResourcesDownloaderPlugin:access$0	(Z)V
      //   112: aload_3
      //   113: areturn
      //   114: astore_1
      //   115: iconst_0
      //   116: invokestatic 352	com/worklight/androidgap/plugin/SSLWebResourcesDownloaderPlugin:access$0	(Z)V
      //   119: aload_1
      //   120: athrow
      //   121: astore 6
      //   123: aload_0
      //   124: ldc_w 393
      //   127: ldc_w 395
      //   130: bipush 6
      //   132: invokespecial 387	com/worklight/androidgap/plugin/SSLWebResourcesDownloaderPlugin$SSLWebResourcesDownloader:handleUpdateException	(Ljava/lang/String;Ljava/lang/String;I)Ljava/lang/String;
      //   135: areturn
      //
      // Exception table:
      //   from	to	target	type
      //   0	4	71	org/apache/http/client/ClientProtocolException
      //   0	4	94	java/io/IOException
      //   0	4	114	finally
      //   73	87	114	finally
      //   95	108	114	finally
      //   8	12	121	java/io/IOException
    }

    private HttpResponse sendRequest()
      throws ClientProtocolException, IOException
    {
      int i;
      if (this.url.toLowerCase().indexOf("https") >= 0)
        i = 1;
      while (true)
      {
        SchemeRegistry localSchemeRegistry;
        label134: DefaultHttpClient localDefaultHttpClient;
        label180: HttpGet localHttpGet;
        String str2;
        String str3;
        label252: BasicHttpContext localBasicHttpContext;
        BasicCookieStore localBasicCookieStore;
        Iterator localIterator;
        if ((i != 0) && (this.sslConfig.isPrivateCA()))
        {
          Log.d("SSL", "send SSL Request in direct update");
          localSchemeRegistry = new SchemeRegistry();
          localSchemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), Integer.parseInt(this.wlConfig.getPort())));
          if (this.sslConfig.isIgnoreSSLCertificate())
          {
            Log.d("SSL", "Not found client trust keyStore, ignore certificate verify.");
            localSchemeRegistry.register(new Scheme("https", new WLSSLSocketFactory(this.sslConfig), this.sslConfig.getHttpsPort()));
            BasicHttpParams localBasicHttpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(localBasicHttpParams, 5000);
            HttpConnectionParams.setSoTimeout(localBasicHttpParams, 10000);
            localDefaultHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(localBasicHttpParams, localSchemeRegistry), localBasicHttpParams);
            localHttpGet = new HttpGet(this.url);
            String str1 = this.wlConfig.getContext();
            str2 = this.wlConfig.getAppURL().getHost();
            if ((str1 == null) || (str1.trim().length() <= 1))
              break label600;
            str3 = str2 + str1;
            String str4 = this.cookies;
            StringBuilder localStringBuilder = new StringBuilder(String.valueOf(str4));
            this.cookies = (";" + CookieManager.getInstance().getCookie(str3));
            WLCookieManager.setCookies(this.cookies, this.wlConfig.getAppURL().getHost());
            Set localSet = WLCookieManager.getCookies();
            localBasicHttpContext = new BasicHttpContext();
            if (localSet != null)
            {
              localBasicCookieStore = new BasicCookieStore();
              localIterator = localSet.iterator();
              if (localIterator.hasNext())
                break label607;
              localBasicHttpContext.setAttribute("http.cookie-store", localBasicCookieStore);
            }
            localHttpGet.addHeader("x-wl-app-version", this.wlConfig.getApplicationVersion());
            localHttpGet.addHeader("WL-Instance-Id", this.instanceAuthId);
          }
        }
        try
        {
          while (true)
          {
            HttpResponse localHttpResponse2 = localDefaultHttpClient.execute(localHttpGet, localBasicHttpContext);
            localHttpResponse1 = localHttpResponse2;
            return localHttpResponse1;
            i = 0;
            break;
            Log.d("SSL", "Do cerfificate verify by ketStore " + this.sslConfig.getKeystore());
            try
            {
              KeyStore localKeyStore = KeyStore.getInstance("BKS");
              Resources localResources = this.sslConfig.getContext().getResources();
              localKeyStore.load(localResources.openRawResource(localResources.getIdentifier(this.sslConfig.getKeystore(), "raw", this.sslConfig.getContext().getPackageName())), this.sslConfig.getKeystorePassword().toCharArray());
              SSLSocketFactory localSSLSocketFactory = new SSLSocketFactory(localKeyStore);
              localSchemeRegistry.register(new Scheme("https", localSSLSocketFactory, this.sslConfig.getHttpsPort()));
            }
            catch (Exception localException2)
            {
              Log.e("SSL", "load certificate keystore failed. " + localException2.getMessage());
            }
          }
          break label134;
          localDefaultHttpClient = new DefaultHttpClient();
          break label180;
          label600: str3 = str2;
          break label252;
          label607: localBasicCookieStore.addCookie((Cookie)localIterator.next());
        }
        catch (Exception localException1)
        {
          do
            HttpResponse localHttpResponse1 = null;
          while (i == 0);
        }
      }
      throw new ClientProtocolException("SSL request error. " + localException1.getMessage());
    }

    protected String doInBackground(Void[] paramArrayOfVoid)
    {
      return runDirectUpdate();
    }

    public boolean isInWhiteList(String paramString)
    {
      int i = 1;
      String[] arrayOfString = new String[2];
      arrayOfString[0] = "198.216.28.169:8480";
      arrayOfString[i] = "www.test.com";
      String str = null;
      if (paramString != null)
      {
        int m = paramString.indexOf("//");
        int n = paramString.indexOf("/", m + 2);
        str = paramString.substring(m + 2, n);
      }
      int j;
      if (str != null)
        j = arrayOfString.length;
      for (int k = 0; ; k++)
      {
        if (k >= j)
          i = 0;
        do
          return i;
        while (-1 != arrayOfString[k].indexOf(str));
      }
    }

    protected void onPostExecute(String paramString)
    {
      try
      {
        if (this.isDirectUpdateSuccess)
        {
          CookieSyncManager.createInstance(this.wlDroidGap);
          CookieManager.getInstance().removeSessionCookie();
          WLUtils.writeWLPref(this.wlDroidGap, "exitOnSkinLoader", "false");
          this.wlDroidGap.loadUrl(this.wlDroidGap.getWebMainFilePath());
          this.progressDialog.dismiss();
        }
        while (true)
        {
          return;
          this.progressDialog.dismiss();
          this.wlDroidGap.loadUrl(paramString);
        }
      }
      catch (Throwable localThrowable)
      {
        WLUtils.error(localThrowable.getMessage());
        return;
      }
      finally
      {
        SSLWebResourcesDownloaderPlugin.isUpdating = false;
      }
      throw localObject;
    }

    protected void onPreExecute()
    {
      SSLWebResourcesDownloaderPlugin.isUpdating = true;
      createProgressDialog();
      if (!this.shouldUpdateSilently)
        this.progressDialog.show();
    }

    protected void onProgressUpdate(Integer[] paramArrayOfInteger)
    {
      this.progressDialog.setProgress(paramArrayOfInteger[0].intValue());
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.SSLWebResourcesDownloaderPlugin
 * JD-Core Version:    0.6.0
 */