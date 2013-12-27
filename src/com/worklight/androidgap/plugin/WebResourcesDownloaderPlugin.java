package com.worklight.androidgap.plugin;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.worklight.androidgap.WLDroidGap;
import com.worklight.common.WLConfig;
import com.worklight.common.WLUtils;
import com.worklight.wlclient.WLCookieManager;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult.Status;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;

public class WebResourcesDownloaderPlugin extends CordovaPlugin
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
        new WebResourcesDownloader((WLDroidGap)this.cordova, str1, str2, bool, "directUpdateDownloadingMessage", str7, str3).execute(new Void[0]);
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
        new WebResourcesDownloader((WLDroidGap)this.cordova, str1, str2, false, "downloadingWebResourcesMessage", str6, str3).execute(new Void[0]);
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

  public class WebResourcesDownloader extends AsyncTask<Void, Integer, String>
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
    private String url = null;
    private WLConfig wlConfig;
    private WLDroidGap wlDroidGap;

    public WebResourcesDownloader(WLDroidGap paramString1, String paramString2, String paramBoolean, boolean paramString3, String paramString4, String paramString5, String arg8)
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

    private void downloadZipFile()
      throws IOException
    {
      Object localObject1 = null;
      try
      {
        HttpResponse localHttpResponse = sendRequest();
        HttpEntity localHttpEntity = localHttpResponse.getEntity();
        long l1 = localHttpEntity.getContentLength();
        this.progressDialog.setMax((int)(l1 / 1024L));
        BufferedInputStream localBufferedInputStream2 = new BufferedInputStream(localHttpEntity.getContent());
        try
        {
          if (!localHttpEntity.getContentType().getValue().equals("application/zip"))
          {
            String str = "The following message has been received from the server instead of the expected application update zip file: " + localHttpResponse.getStatusLine() + " " + WLUtils.convertStreamToString(localBufferedInputStream2);
            WLUtils.debug(str);
            IOException localIOException = new IOException(str);
            throw localIOException;
          }
        }
        finally
        {
          localBufferedInputStream1 = localBufferedInputStream2;
        }
        FileOutputStream localFileOutputStream;
        while (true)
        {
          if (localObject1 != null)
            localObject1.close();
          if (localBufferedInputStream1 != null)
          {
            localBufferedInputStream1.close();
            WLUtils.debug("Finish copy files to local storage from updated zip file...");
          }
          throw localObject2;
          WLUtils.debug("Start copy files to local storage from updated zip file...");
          localFileOutputStream = new FileOutputStream(this.wlDroidGap.getLocalStorageRoot() + "/" + "assets.zip");
          try
          {
            byte[] arrayOfByte = new byte[8192];
            long l2 = 0L;
            while (true)
            {
              int i = localBufferedInputStream2.read(arrayOfByte);
              if (i == -1)
                break;
              l2 += i;
              Integer[] arrayOfInteger = new Integer[1];
              arrayOfInteger[0] = Integer.valueOf((int)(l2 / 1024L));
              publishProgress(arrayOfInteger);
              localFileOutputStream.write(arrayOfByte, 0, i);
            }
          }
          finally
          {
            localObject1 = localFileOutputStream;
            localBufferedInputStream1 = localBufferedInputStream2;
          }
        }
        if (localFileOutputStream != null)
          localFileOutputStream.close();
        if (localBufferedInputStream2 != null)
        {
          localBufferedInputStream2.close();
          WLUtils.debug("Finish copy files to local storage from updated zip file...");
        }
        return;
      }
      finally
      {
        while (true)
        {
          BufferedInputStream localBufferedInputStream1 = null;
          localObject1 = null;
        }
      }
    }

    private void extractZipFile(String paramString1, String paramString2)
      throws IOException
    {
      try
      {
        WLUtils.deleteDirectory(new File(paramString2 + "/" + this.skinName));
        new File(paramString2 + "/" + this.skinName).mkdirs();
        localZipFile = new ZipFile(paramString1);
        Enumeration localEnumeration = localZipFile.entries();
        while (true)
        {
          if (!localEnumeration.hasMoreElements())
            break label238;
          localZipEntry = (ZipEntry)localEnumeration.nextElement();
          if (!localZipEntry.isDirectory())
            break;
          new File(paramString2 + "/" + localZipEntry.getName()).mkdirs();
        }
      }
      catch (Exception localException)
      {
        ZipFile localZipFile;
        while (true)
        {
          ZipEntry localZipEntry;
          WLUtils.error(localException.getLocalizedMessage());
          continue;
          InputStream localInputStream = localZipFile.getInputStream(localZipEntry);
          FileOutputStream localFileOutputStream = new FileOutputStream(paramString2 + "/" + localZipEntry.getName());
          WLUtils.copyFile(localInputStream, localFileOutputStream);
          localInputStream.close();
          localFileOutputStream.close();
        }
        label238: localZipFile.close();
      }
    }

    private String handleUpdateException(String paramString1, String paramString2, int paramInt)
    {
      WLUtils.log(paramString2, paramInt);
      this.isDirectUpdateSuccess = false;
      StringBuilder localStringBuilder = new StringBuilder().append("javascript:WL.App._showDirectUpdateErrorMessage(");
      if (paramString1 != null);
      for (String str = "WL.ClientMessages." + paramString1; ; str = "\"" + paramString2 + "\"")
        return str + ")";
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
      //   1: invokespecial 323	com/worklight/androidgap/plugin/WebResourcesDownloaderPlugin$WebResourcesDownloader:downloadZipFile	()V
      //   4: aload_0
      //   5: invokespecial 325	com/worklight/androidgap/plugin/WebResourcesDownloaderPlugin$WebResourcesDownloader:processZipFile	()V
      //   8: aload_0
      //   9: getfield 55	com/worklight/androidgap/plugin/WebResourcesDownloaderPlugin$WebResourcesDownloader:wlConfig	Lcom/worklight/common/WLConfig;
      //   12: invokevirtual 328	com/worklight/common/WLConfig:getTestWebResourcesChecksumFlag	()Ljava/lang/String;
      //   15: ldc_w 330
      //   18: invokevirtual 184	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   21: ifeq +40 -> 61
      //   24: aload_0
      //   25: getfield 48	com/worklight/androidgap/plugin/WebResourcesDownloaderPlugin$WebResourcesDownloader:wlDroidGap	Lcom/worklight/androidgap/WLDroidGap;
      //   28: invokevirtual 312	com/worklight/androidgap/WLDroidGap:getLocalStorageWebRoot	()Ljava/lang/String;
      //   31: invokestatic 334	com/worklight/common/WLUtils:computeChecksumOnResources	(Ljava/lang/String;)J
      //   34: invokestatic 339	java/lang/Long:toString	(J)Ljava/lang/String;
      //   37: ldc_w 341
      //   40: invokestatic 347	com/worklight/utils/SecurityUtils:hashData	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
      //   43: astore_3
      //   44: ldc_w 349
      //   47: invokestatic 202	com/worklight/common/WLUtils:debug	(Ljava/lang/String;)V
      //   50: aload_0
      //   51: getfield 48	com/worklight/androidgap/plugin/WebResourcesDownloaderPlugin$WebResourcesDownloader:wlDroidGap	Lcom/worklight/androidgap/WLDroidGap;
      //   54: ldc_w 351
      //   57: aload_3
      //   58: invokestatic 355	com/worklight/common/WLUtils:writeWLPref	(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;)V
      //   61: ldc_w 357
      //   64: areturn
      //   65: astore_1
      //   66: aload_0
      //   67: ldc_w 359
      //   70: ldc_w 361
      //   73: bipush 6
      //   75: invokespecial 363	com/worklight/androidgap/plugin/WebResourcesDownloaderPlugin$WebResourcesDownloader:handleUpdateException	(Ljava/lang/String;Ljava/lang/String;I)Ljava/lang/String;
      //   78: areturn
      //   79: astore_2
      //   80: aload_0
      //   81: ldc_w 365
      //   84: ldc_w 367
      //   87: bipush 6
      //   89: invokespecial 363	com/worklight/androidgap/plugin/WebResourcesDownloaderPlugin$WebResourcesDownloader:handleUpdateException	(Ljava/lang/String;Ljava/lang/String;I)Ljava/lang/String;
      //   92: areturn
      //
      // Exception table:
      //   from	to	target	type
      //   0	4	65	java/io/IOException
      //   4	8	79	java/io/IOException
    }

    private HttpResponse sendRequest()
      throws ClientProtocolException, IOException
    {
      DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
      HttpGet localHttpGet = new HttpGet(this.url);
      String str1 = this.wlConfig.getContext();
      String str2 = this.wlConfig.getAppURL().getHost();
      String str3;
      if ((str1 != null) && (str1.trim().length() > 1))
        str3 = str2 + str1;
      BasicHttpContext localBasicHttpContext;
      while (true)
      {
        this.cookies = (this.cookies + ";" + CookieManager.getInstance().getCookie(str3));
        WLCookieManager.setCookies(this.cookies, this.wlConfig.getAppURL().getHost());
        Set localSet = WLCookieManager.getCookies();
        localBasicHttpContext = new BasicHttpContext();
        if (localSet == null)
          break;
        BasicCookieStore localBasicCookieStore = new BasicCookieStore();
        Iterator localIterator = localSet.iterator();
        while (true)
          if (localIterator.hasNext())
          {
            localBasicCookieStore.addCookie((Cookie)localIterator.next());
            continue;
            str3 = str2;
            break;
          }
        localBasicHttpContext.setAttribute("http.cookie-store", localBasicCookieStore);
      }
      localHttpGet.addHeader("x-wl-app-version", this.wlConfig.getApplicationVersion());
      localHttpGet.addHeader("WL-Instance-Id", this.instanceAuthId);
      return localDefaultHttpClient.execute(localHttpGet, localBasicHttpContext);
    }

    protected String doInBackground(Void[] paramArrayOfVoid)
    {
      return runDirectUpdate();
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
        WebResourcesDownloaderPlugin.access$002(false);
      }
      throw localObject;
    }

    protected void onPreExecute()
    {
      WebResourcesDownloaderPlugin.access$002(true);
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
 * Qualified Name:     com.worklight.androidgap.plugin.WebResourcesDownloaderPlugin
 * JD-Core Version:    0.6.0
 */