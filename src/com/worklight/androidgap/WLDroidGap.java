package com.worklight.androidgap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import com.worklight.androidgap.analytics.WLAnalytics;
import com.worklight.androidgap.plugin.NativePage;
import com.worklight.androidgap.plugin.Push;
import com.worklight.androidgap.plugin.WLMenuItem;
import com.worklight.androidgap.plugin.WLOptionsMenu;
import com.worklight.androidgap.push.WLGCMIntentService.Message;
import com.worklight.common.WLConfig;
import com.worklight.common.WLUtils;
import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.cordova.CordovaChromeClient;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewClient;
import org.apache.cordova.DroidGap;
import org.apache.cordova.IceCreamCordovaWebViewClient;
import org.apache.cordova.api.PluginManager;
import org.json.JSONException;
import org.json.JSONObject;

public class WLDroidGap extends DroidGap
  implements WLDroidGapListener
{
  private static final String CLEAR_CACHE_NEXT_LOAD = "com.worklight.clearCacheNextLoad";
  private static final String ENABLE_SETTINGS_FLAG = "enableSettings";
  public static final String EXIT_ON_SKIN_LOADER = "exitOnSkinLoader";
  private static final String NATIVE_EMPTY_APP_HTML = "NativeEmptyApp.html";
  public static final String RESOURCES_CHECKSUM_PREF_KEY = "wlResourcesChecksum";
  public static final String SKIN_LOADER_HTML = "skinLoader.html";
  public static final String SKIN_NAME_PREF_KEY = "wlSkinName";
  public static final String WL_DEFAULT_SERVER_URL = "WLDefaultServerURL";
  private static boolean isForegound = false;
  private static WLConfig wlConfig;
  private boolean fatalErrorOccurred = false;
  private PrepareClientAsyncTask longPrepareTask;
  private WLOptionsMenu optionsMenu = null;

  private void doPrepareAssetsWork()
  {
    new PrepackagedAssetsManager().copyPrepackagedAssetsToLocalCopyIfNeeded();
    WLUtils.writeWLPref(this, "WLDefaultServerURL", getWLConfig().getDefaultRootUrl());
    WLUtils.writeWLPref(this, "exitOnSkinLoader", "false");
    WLUtils.writeWLPref(this, "enableSettings", getWLConfig().getSettingsFlag().toString());
    setForeGround(true);
  }

  public static WLConfig getWLConfig()
  {
    return wlConfig;
  }

  private boolean isClearCacheNextLoad()
  {
    return getPreferences(0).getBoolean("com.worklight.clearCacheNextLoad", false);
  }

  public static boolean isForeGround()
  {
    return isForegound;
  }

  private void removeSessionCookies(CordovaWebView paramCordovaWebView)
  {
    CookieSyncManager.createInstance(paramCordovaWebView.getContext());
    CookieManager.getInstance().removeSessionCookie();
  }

  private void setClearCacheNextLoad(boolean paramBoolean)
  {
    SharedPreferences.Editor localEditor = getPreferences(0).edit();
    localEditor.putBoolean("com.worklight.clearCacheNextLoad", paramBoolean);
    localEditor.commit();
  }

  private static void setForeGround(boolean paramBoolean)
  {
    isForegound = paramBoolean;
  }

  private void testResourcesChecksum()
  {
    if (!getWLConfig().getTestWebResourcesChecksumFlag().equals("true"))
    {
      WLUtils.debug("no need to check web resource integrity");
      return;
    }
    WLUtils.debug("start web resource integrity test");
    if (!WLUtils.checksumTestOnResources(getLocalStorageWebRoot(), this))
    {
      WLUtils.showDialog(this, "Error", "The application encountered an internal error. Please uninstall the application and then re-install it.", "Close", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          paramDialogInterface.dismiss();
          System.exit(0);
        }
      });
      this.fatalErrorOccurred = true;
      return;
    }
    WLUtils.debug("web resource integrity test: Success");
  }

  protected void bindBrowser(CordovaWebView paramCordovaWebView)
  {
    bindBrowser(paramCordovaWebView, true);
  }

  protected void bindBrowser(CordovaWebView paramCordovaWebView, boolean paramBoolean)
  {
    paramCordovaWebView.pluginManager.addService("AnalyticsConfigurator", "com.worklight.androidgap.plugin.AnalyticsConfigurator");
    paramCordovaWebView.pluginManager.addService("Logger", "com.worklight.androidgap.plugin.Logger");
    paramCordovaWebView.pluginManager.addService("Utils", "com.worklight.androidgap.plugin.Utils");
    paramCordovaWebView.pluginManager.addService("NativePage", "com.worklight.androidgap.plugin.NativePage");
    paramCordovaWebView.pluginManager.addService("NativeBusyIndicator", "com.worklight.androidgap.plugin.BusyIndicator");
    paramCordovaWebView.pluginManager.addService("SecurityPlugin", "com.worklight.androidgap.plugin.SecurityPlugin");
    paramCordovaWebView.pluginManager.addService("StoragePlugin", "com.worklight.androidgap.plugin.storage.StoragePlugin");
    paramCordovaWebView.pluginManager.addService("Push", "com.worklight.androidgap.plugin.Push");
    paramCordovaWebView.pluginManager.addService("WebResourcesDownloader", "com.worklight.androidgap.plugin.WebResourcesDownloaderPlugin");
    paramCordovaWebView.pluginManager.addService("NetworkDetector", "com.worklight.androidgap.plugin.NetworkDetector");
    paramCordovaWebView.pluginManager.addService("DeviceAuth", "com.worklight.androidgap.plugin.DeviceAuthPlugin");
    paramCordovaWebView.pluginManager.addService("WifiPlugin", "com.worklight.androidgap.plugin.WifiPlugin");
    paramCordovaWebView.pluginManager.addService("WLGeolocationPlugin", "com.worklight.androidgap.plugin.WLGeolocationPlugin");
    paramCordovaWebView.pluginManager.addService("ForegroundBinderPlugin", "com.worklight.androidgap.plugin.ForegroundBinderPlugin");
    paramCordovaWebView.pluginManager.addService("WLCustomDialog", "com.worklight.androidgap.plugin.WLCustomDialog");
    paramCordovaWebView.pluginManager.addService("NetworkStatus", "com.worklight.androidgap.plugin.WLNetworkManager");
    if (this.optionsMenu == null)
      this.optionsMenu = new WLOptionsMenu(this);
    paramCordovaWebView.addJavascriptInterface(this.optionsMenu, "NativeOptionsMenu");
    paramCordovaWebView.addJavascriptInterface(this, "WLCordovaSplashScreenDialog");
    if (paramBoolean)
      removeSessionCookies(paramCordovaWebView);
  }

  public String getAppWebUrl(String paramString)
  {
    String str = getWLConfig().getMainFilePath();
    if (new File(getLocalStorageWebRoot() + "/" + paramString + "/" + "NativeEmptyApp.html").exists())
      str = "NativeEmptyApp.html";
    return getWebUrl() + "/" + paramString + "/" + str;
  }

  protected String getIntentDataInJSONFormat(Intent paramIntent)
  {
    String str1 = "";
    if ((paramIntent != null) && (paramIntent.getExtras() != null))
    {
      Set localSet = paramIntent.getExtras().keySet();
      HashMap localHashMap = new HashMap();
      Iterator localIterator = localSet.iterator();
      while (localIterator.hasNext())
      {
        String str2 = (String)localIterator.next();
        localHashMap.put(str2, paramIntent.getExtras().get(str2));
      }
      str1 = new JSONObject(localHashMap).toString();
    }
    return str1;
  }

  public String getLocalStorageRoot()
  {
    return getApplicationContext().getFilesDir().getAbsolutePath();
  }

  public String getLocalStorageWebRoot()
  {
    return getLocalStorageRoot() + "/www";
  }

  public String getWebMainFilePath()
  {
    String str1 = wlConfig.getMainFileFromDescriptor();
    if ((str1.startsWith("http:")) || (str1.startsWith("https:")))
      return str1;
    String str2 = WLUtils.readWLPref(this, "wlSkinName");
    if (str2 != null);
    for (String str3 = getAppWebUrl(str2); ; str3 = getWebUrl() + "/" + "skinLoader.html")
      return str3;
  }

  public String getWebUrl()
  {
    return "file://" + getLocalStorageWebRoot();
  }

  public void init()
  {
    WLWebView localWLWebView = new WLWebView(this);
    if (Build.VERSION.SDK_INT < 11);
    for (Object localObject = new CordovaWebViewClient(this, localWLWebView); ; localObject = new IceCreamCordovaWebViewClient(this, localWLWebView))
    {
      super.init(localWLWebView, (CordovaWebViewClient)localObject, new CordovaChromeClient(this, localWLWebView));
      return;
    }
  }

  public void loadUrl(String paramString)
  {
    loadUrl(paramString, true);
  }

  public void loadUrl(String paramString, boolean paramBoolean)
  {
    if (!this.fatalErrorOccurred)
    {
      if (isClearCacheNextLoad())
      {
        this.appView.clearCache(true);
        setClearCacheNextLoad(false);
      }
      bindBrowser(this.appView, paramBoolean);
      super.loadUrl(paramString);
    }
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (paramInt1 == NativePage.NATIVE_ACTIVITY_REQ_CODE)
    {
      if (paramInt2 == -1)
        WLUtils.debug("NativePage returned OK result code");
      while (true)
      {
        sendJavascript("WL.NativePage.onNativePageClose(" + getIntentDataInJSONFormat(paramIntent) + ")");
        return;
        WLUtils.debug("Code returned from NativePage is " + paramInt2);
      }
    }
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    try
    {
      this.splashscreen = WLUtils.getResourceId(this, "drawable", "splash");
      if (this.splashscreen != 0)
        showSplashScreen(20000);
      init();
      WLAnalytics.initializeTealeaf(this);
      wlConfig = new WLConfig(getApplication());
      WebSettings localWebSettings = this.appView.getSettings();
      String str = localWebSettings.getUserAgentString();
      localWebSettings.setUserAgentString(str + "/Worklight/" + wlConfig.getPlatformVersion());
      this.longPrepareTask = new PrepareClientAsyncTask(null);
      this.longPrepareTask.execute(new Bundle[] { paramBundle });
      return;
    }
    catch (Exception localException)
    {
      while (true)
      {
        this.splashscreen = 0;
        WLUtils.debug("Application will not display splash screen because required icon is missing. Add splash.png icon to res/drawble folder");
      }
    }
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    return onPrepareOptionsMenu(paramMenu);
  }

  public void onDestroy()
  {
    super.onDestroy();
    System.runFinalizersOnExit(true);
    System.exit(0);
  }

  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    WLGCMIntentService.Message localMessage = (WLGCMIntentService.Message)paramIntent.getParcelableExtra("message");
    if (localMessage != null)
      ((Push)this.appView.pluginManager.getPlugin("Push")).dispatchPending(localMessage);
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    WLMenuItem localWLMenuItem = this.optionsMenu.getItemById(paramMenuItem.getItemId());
    if (localWLMenuItem != null)
    {
      sendJavascript(localWLMenuItem.getCallback());
      return true;
    }
    return false;
  }

  public void onPause()
  {
    try
    {
      super.onPause();
      setForeGround(false);
      return;
    }
    catch (Exception localException)
    {
      WLUtils.debug(localException.getMessage());
    }
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    boolean bool1 = true;
    if (!this.optionsMenu.isVisible())
      bool1 = false;
    do
    {
      do
        return bool1;
      while ((!this.optionsMenu.isInit()) || (!this.optionsMenu.hasChanged()));
      paramMenu.clear();
    }
    while (!this.optionsMenu.isVisible());
    Iterator localIterator = this.optionsMenu.getItems().iterator();
    if (localIterator.hasNext())
    {
      WLMenuItem localWLMenuItem = (WLMenuItem)localIterator.next();
      MenuItem localMenuItem = paramMenu.add(0, localWLMenuItem.getId(), 0, localWLMenuItem.getTitle());
      if ((localWLMenuItem.isEnabled()) && (this.optionsMenu.isEnabled()));
      for (boolean bool2 = bool1; ; bool2 = false)
      {
        localMenuItem.setEnabled(bool2);
        if (localWLMenuItem.getImagePath() == null)
          break;
        localMenuItem.setIcon(localWLMenuItem.getImage(this));
        break;
      }
    }
    this.optionsMenu.unsetChanged();
    return bool1;
  }

  public void onResume()
  {
    super.onResume();
    setForeGround(true);
  }

  public void onWLInitCompleted(Bundle paramBundle)
  {
  }

  @JavascriptInterface
  public void removeSplashScreen()
  {
    super.removeSplashScreen();
  }

  public void setClearCacheNextLoad()
  {
    setClearCacheNextLoad(true);
  }

  public class PrepackagedAssetsManager
  {
    private static final String APP_INSTALL_TIME_KEY = "appInstallTime";
    private long appInstallTime = -1L;

    public PrepackagedAssetsManager()
    {
    }

    // ERROR //
    private void copyAssetsToLocalCopy(android.content.res.AssetManager paramAssetManager, String paramString1, String paramString2)
    {
      // Byte code:
      //   0: aconst_null
      //   1: astore 4
      //   3: aconst_null
      //   4: astore 5
      //   6: new 35	java/io/File
      //   9: dup
      //   10: new 37	java/lang/StringBuilder
      //   13: dup
      //   14: invokespecial 38	java/lang/StringBuilder:<init>	()V
      //   17: aload_3
      //   18: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   21: ldc 44
      //   23: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   26: aload_2
      //   27: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   30: invokevirtual 48	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   33: invokespecial 51	java/io/File:<init>	(Ljava/lang/String;)V
      //   36: astore 6
      //   38: aload_1
      //   39: aload_2
      //   40: invokevirtual 57	android/content/res/AssetManager:open	(Ljava/lang/String;)Ljava/io/InputStream;
      //   43: astore 4
      //   45: new 59	java/io/FileOutputStream
      //   48: dup
      //   49: aload 6
      //   51: invokespecial 62	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
      //   54: astore 17
      //   56: aload 4
      //   58: aload 17
      //   60: invokestatic 68	com/worklight/common/WLUtils:copyFile	(Ljava/io/InputStream;Ljava/io/OutputStream;)V
      //   63: aload 17
      //   65: astore 5
      //   67: aload 4
      //   69: ifnull +8 -> 77
      //   72: aload 4
      //   74: invokevirtual 73	java/io/InputStream:close	()V
      //   77: aload 5
      //   79: ifnull +8 -> 87
      //   82: aload 5
      //   84: invokevirtual 76	java/io/OutputStream:close	()V
      //   87: return
      //   88: astore 11
      //   90: aload 6
      //   92: invokevirtual 80	java/io/File:mkdirs	()Z
      //   95: pop
      //   96: aload_1
      //   97: aload_2
      //   98: invokevirtual 84	android/content/res/AssetManager:list	(Ljava/lang/String;)[Ljava/lang/String;
      //   101: astore 13
      //   103: iconst_0
      //   104: istore 14
      //   106: iload 14
      //   108: aload 13
      //   110: arraylength
      //   111: if_icmpge -44 -> 67
      //   114: aload_0
      //   115: aload_1
      //   116: new 37	java/lang/StringBuilder
      //   119: dup
      //   120: invokespecial 38	java/lang/StringBuilder:<init>	()V
      //   123: aload_2
      //   124: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   127: ldc 44
      //   129: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   132: aload 13
      //   134: iload 14
      //   136: aaload
      //   137: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   140: invokevirtual 48	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   143: aload_3
      //   144: invokespecial 86	com/worklight/androidgap/WLDroidGap$PrepackagedAssetsManager:copyAssetsToLocalCopy	(Landroid/content/res/AssetManager;Ljava/lang/String;Ljava/lang/String;)V
      //   147: iinc 14 1
      //   150: goto -44 -> 106
      //   153: astore 16
      //   155: new 37	java/lang/StringBuilder
      //   158: dup
      //   159: invokespecial 38	java/lang/StringBuilder:<init>	()V
      //   162: ldc 88
      //   164: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   167: aload_2
      //   168: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   171: ldc 90
      //   173: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   176: aload 16
      //   178: invokevirtual 93	java/io/IOException:getMessage	()Ljava/lang/String;
      //   181: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   184: invokevirtual 48	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   187: aload 16
      //   189: invokestatic 97	com/worklight/common/WLUtils:debug	(Ljava/lang/String;Ljava/lang/Throwable;)V
      //   192: goto -115 -> 77
      //   195: astore 15
      //   197: new 37	java/lang/StringBuilder
      //   200: dup
      //   201: invokespecial 38	java/lang/StringBuilder:<init>	()V
      //   204: ldc 99
      //   206: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   209: aload 6
      //   211: invokevirtual 102	java/io/File:getAbsolutePath	()Ljava/lang/String;
      //   214: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   217: ldc 90
      //   219: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   222: aload 15
      //   224: invokevirtual 93	java/io/IOException:getMessage	()Ljava/lang/String;
      //   227: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   230: invokevirtual 48	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   233: aload 15
      //   235: invokestatic 97	com/worklight/common/WLUtils:debug	(Ljava/lang/String;Ljava/lang/Throwable;)V
      //   238: return
      //   239: astore 10
      //   241: new 104	java/lang/RuntimeException
      //   244: dup
      //   245: new 37	java/lang/StringBuilder
      //   248: dup
      //   249: invokespecial 38	java/lang/StringBuilder:<init>	()V
      //   252: ldc 106
      //   254: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   257: aload 6
      //   259: invokevirtual 110	java/io/File:getAbsoluteFile	()Ljava/io/File;
      //   262: invokevirtual 113	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   265: invokevirtual 48	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   268: aload 10
      //   270: invokespecial 115	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
      //   273: athrow
      //   274: astore 7
      //   276: aload 4
      //   278: ifnull +8 -> 286
      //   281: aload 4
      //   283: invokevirtual 73	java/io/InputStream:close	()V
      //   286: aload 5
      //   288: ifnull +8 -> 296
      //   291: aload 5
      //   293: invokevirtual 76	java/io/OutputStream:close	()V
      //   296: aload 7
      //   298: athrow
      //   299: astore 9
      //   301: new 37	java/lang/StringBuilder
      //   304: dup
      //   305: invokespecial 38	java/lang/StringBuilder:<init>	()V
      //   308: ldc 88
      //   310: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   313: aload_2
      //   314: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   317: ldc 90
      //   319: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   322: aload 9
      //   324: invokevirtual 93	java/io/IOException:getMessage	()Ljava/lang/String;
      //   327: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   330: invokevirtual 48	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   333: aload 9
      //   335: invokestatic 97	com/worklight/common/WLUtils:debug	(Ljava/lang/String;Ljava/lang/Throwable;)V
      //   338: goto -52 -> 286
      //   341: astore 8
      //   343: new 37	java/lang/StringBuilder
      //   346: dup
      //   347: invokespecial 38	java/lang/StringBuilder:<init>	()V
      //   350: ldc 99
      //   352: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   355: aload 6
      //   357: invokevirtual 102	java/io/File:getAbsolutePath	()Ljava/lang/String;
      //   360: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   363: ldc 90
      //   365: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   368: aload 8
      //   370: invokevirtual 93	java/io/IOException:getMessage	()Ljava/lang/String;
      //   373: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   376: invokevirtual 48	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   379: aload 8
      //   381: invokestatic 97	com/worklight/common/WLUtils:debug	(Ljava/lang/String;Ljava/lang/Throwable;)V
      //   384: goto -88 -> 296
      //   387: astore 7
      //   389: aload 17
      //   391: astore 5
      //   393: goto -117 -> 276
      //   396: astore 10
      //   398: aload 17
      //   400: astore 5
      //   402: goto -161 -> 241
      //   405: astore 18
      //   407: aload 17
      //   409: astore 5
      //   411: goto -321 -> 90
      //
      // Exception table:
      //   from	to	target	type
      //   38	56	88	java/io/IOException
      //   72	77	153	java/io/IOException
      //   82	87	195	java/io/IOException
      //   38	56	239	java/lang/Exception
      //   90	103	239	java/lang/Exception
      //   106	147	239	java/lang/Exception
      //   38	56	274	finally
      //   90	103	274	finally
      //   106	147	274	finally
      //   241	274	274	finally
      //   281	286	299	java/io/IOException
      //   291	296	341	java/io/IOException
      //   56	63	387	finally
      //   56	63	396	java/lang/Exception
      //   56	63	405	java/io/IOException
    }

    // ERROR //
    private boolean copyOrUnpackAssetsToLocalCopy()
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 15	com/worklight/androidgap/WLDroidGap$PrepackagedAssetsManager:this$0	Lcom/worklight/androidgap/WLDroidGap;
      //   4: invokevirtual 122	com/worklight/androidgap/WLDroidGap:getApplicationContext	()Landroid/content/Context;
      //   7: invokevirtual 128	android/content/Context:getAssets	()Landroid/content/res/AssetManager;
      //   10: astore_1
      //   11: new 35	java/io/File
      //   14: dup
      //   15: aload_0
      //   16: getfield 15	com/worklight/androidgap/WLDroidGap$PrepackagedAssetsManager:this$0	Lcom/worklight/androidgap/WLDroidGap;
      //   19: invokevirtual 131	com/worklight/androidgap/WLDroidGap:getLocalStorageWebRoot	()Ljava/lang/String;
      //   22: invokespecial 51	java/io/File:<init>	(Ljava/lang/String;)V
      //   25: invokestatic 135	com/worklight/common/WLUtils:deleteDirectory	(Ljava/io/File;)Z
      //   28: pop
      //   29: ldc2_w 136
      //   32: invokestatic 141	com/worklight/androidgap/WLDroidGap:access$200	()Lcom/worklight/common/WLConfig;
      //   35: invokevirtual 146	com/worklight/common/WLConfig:getWebResourcesUnpackedSize	()Ljava/lang/String;
      //   38: invokestatic 152	java/lang/Long:parseLong	(Ljava/lang/String;)J
      //   41: ladd
      //   42: lstore 4
      //   44: invokestatic 156	com/worklight/common/WLUtils:getFreeSpaceOnDevice	()J
      //   47: lstore 6
      //   49: lload 4
      //   51: lload 6
      //   53: lcmp
      //   54: ifle +57 -> 111
      //   57: aload_0
      //   58: lload 4
      //   60: invokestatic 160	java/lang/Long:valueOf	(J)Ljava/lang/Long;
      //   63: lload 6
      //   65: invokespecial 164	com/worklight/androidgap/WLDroidGap$PrepackagedAssetsManager:handleNotEnoughSpace	(Ljava/lang/Long;J)V
      //   68: aload_0
      //   69: getfield 15	com/worklight/androidgap/WLDroidGap$PrepackagedAssetsManager:this$0	Lcom/worklight/androidgap/WLDroidGap;
      //   72: iconst_1
      //   73: invokestatic 168	com/worklight/androidgap/WLDroidGap:access$302	(Lcom/worklight/androidgap/WLDroidGap;Z)Z
      //   76: pop
      //   77: iconst_0
      //   78: ireturn
      //   79: astore_2
      //   80: iconst_1
      //   81: anewarray 4	java/lang/Object
      //   84: astore_3
      //   85: aload_3
      //   86: iconst_0
      //   87: aload_0
      //   88: getfield 15	com/worklight/androidgap/WLDroidGap$PrepackagedAssetsManager:this$0	Lcom/worklight/androidgap/WLDroidGap;
      //   91: invokevirtual 131	com/worklight/androidgap/WLDroidGap:getLocalStorageWebRoot	()Ljava/lang/String;
      //   94: aastore
      //   95: ldc 170
      //   97: aload_3
      //   98: invokestatic 176	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
      //   101: invokestatic 179	com/worklight/common/WLUtils:error	(Ljava/lang/String;)V
      //   104: iconst_0
      //   105: invokestatic 185	java/lang/System:exit	(I)V
      //   108: goto -79 -> 29
      //   111: ldc 187
      //   113: invokestatic 189	com/worklight/common/WLUtils:debug	(Ljava/lang/String;)V
      //   116: aconst_null
      //   117: astore 8
      //   119: lconst_0
      //   120: lstore 9
      //   122: new 191	com/worklight/utils/MultifileAssetStream
      //   125: dup
      //   126: ldc 193
      //   128: aload_1
      //   129: invokespecial 196	com/worklight/utils/MultifileAssetStream:<init>	(Ljava/lang/String;Landroid/content/res/AssetManager;)V
      //   132: astore 11
      //   134: aload 11
      //   136: invokevirtual 200	java/io/InputStream:available	()I
      //   139: istore 28
      //   141: iload 28
      //   143: i2l
      //   144: lstore 9
      //   146: iconst_1
      //   147: istore 13
      //   149: lload 9
      //   151: lconst_0
      //   152: lcmp
      //   153: ifgt +259 -> 412
      //   156: new 191	com/worklight/utils/MultifileAssetStream
      //   159: dup
      //   160: ldc 202
      //   162: aload_1
      //   163: invokespecial 196	com/worklight/utils/MultifileAssetStream:<init>	(Ljava/lang/String;Landroid/content/res/AssetManager;)V
      //   166: astore 14
      //   168: aload 14
      //   170: invokevirtual 200	java/io/InputStream:available	()I
      //   173: istore 24
      //   175: iload 24
      //   177: i2l
      //   178: pop2
      //   179: iconst_0
      //   180: istore 13
      //   182: aload 14
      //   184: ifnull +174 -> 358
      //   187: ldc 204
      //   189: invokestatic 189	com/worklight/common/WLUtils:debug	(Ljava/lang/String;)V
      //   192: ldc 206
      //   194: invokevirtual 210	java/lang/String:getBytes	()[B
      //   197: invokestatic 216	com/worklight/utils/Base64:decode	([B)[B
      //   200: astore 15
      //   202: iload 13
      //   204: ifeq +55 -> 259
      //   207: aload 14
      //   209: aload 15
      //   211: invokestatic 222	com/worklight/utils/SecurityUtils:decryptData	(Ljava/io/InputStream;[B)Ljava/io/InputStream;
      //   214: astore 16
      //   216: aload 16
      //   218: new 35	java/io/File
      //   221: dup
      //   222: aload_0
      //   223: getfield 15	com/worklight/androidgap/WLDroidGap$PrepackagedAssetsManager:this$0	Lcom/worklight/androidgap/WLDroidGap;
      //   226: invokevirtual 131	com/worklight/androidgap/WLDroidGap:getLocalStorageWebRoot	()Ljava/lang/String;
      //   229: invokespecial 51	java/io/File:<init>	(Ljava/lang/String;)V
      //   232: invokestatic 226	com/worklight/common/WLUtils:unpack	(Ljava/io/InputStream;Ljava/io/File;)V
      //   235: ldc 228
      //   237: invokestatic 189	com/worklight/common/WLUtils:debug	(Ljava/lang/String;)V
      //   240: ldc 230
      //   242: invokestatic 189	com/worklight/common/WLUtils:debug	(Ljava/lang/String;)V
      //   245: iconst_1
      //   246: ireturn
      //   247: astore 29
      //   249: aload 8
      //   251: astore 11
      //   253: iconst_0
      //   254: istore 13
      //   256: goto -107 -> 149
      //   259: aload 14
      //   261: astore 16
      //   263: goto -47 -> 216
      //   266: astore 20
      //   268: new 37	java/lang/StringBuilder
      //   271: dup
      //   272: invokespecial 38	java/lang/StringBuilder:<init>	()V
      //   275: ldc 232
      //   277: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   280: aload 20
      //   282: invokevirtual 93	java/io/IOException:getMessage	()Ljava/lang/String;
      //   285: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   288: invokevirtual 48	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   291: astore 21
      //   293: aload 21
      //   295: invokestatic 179	com/worklight/common/WLUtils:error	(Ljava/lang/String;)V
      //   298: new 104	java/lang/RuntimeException
      //   301: dup
      //   302: aload 21
      //   304: invokespecial 233	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
      //   307: astore 22
      //   309: aload 22
      //   311: athrow
      //   312: astore 17
      //   314: new 37	java/lang/StringBuilder
      //   317: dup
      //   318: invokespecial 38	java/lang/StringBuilder:<init>	()V
      //   321: ldc 235
      //   323: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   326: aload 17
      //   328: invokevirtual 236	java/lang/Exception:getMessage	()Ljava/lang/String;
      //   331: invokevirtual 42	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   334: invokevirtual 48	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   337: astore 18
      //   339: aload 18
      //   341: invokestatic 179	com/worklight/common/WLUtils:error	(Ljava/lang/String;)V
      //   344: new 104	java/lang/RuntimeException
      //   347: dup
      //   348: aload 18
      //   350: invokespecial 233	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
      //   353: astore 19
      //   355: aload 19
      //   357: athrow
      //   358: aload_0
      //   359: aload_1
      //   360: ldc 238
      //   362: aload_0
      //   363: getfield 15	com/worklight/androidgap/WLDroidGap$PrepackagedAssetsManager:this$0	Lcom/worklight/androidgap/WLDroidGap;
      //   366: invokevirtual 241	com/worklight/androidgap/WLDroidGap:getLocalStorageRoot	()Ljava/lang/String;
      //   369: invokespecial 86	com/worklight/androidgap/WLDroidGap$PrepackagedAssetsManager:copyAssetsToLocalCopy	(Landroid/content/res/AssetManager;Ljava/lang/String;Ljava/lang/String;)V
      //   372: aload_0
      //   373: aload_1
      //   374: ldc 243
      //   376: aload_0
      //   377: getfield 15	com/worklight/androidgap/WLDroidGap$PrepackagedAssetsManager:this$0	Lcom/worklight/androidgap/WLDroidGap;
      //   380: invokevirtual 131	com/worklight/androidgap/WLDroidGap:getLocalStorageWebRoot	()Ljava/lang/String;
      //   383: invokespecial 86	com/worklight/androidgap/WLDroidGap$PrepackagedAssetsManager:copyAssetsToLocalCopy	(Landroid/content/res/AssetManager;Ljava/lang/String;Ljava/lang/String;)V
      //   386: goto -146 -> 240
      //   389: astore 27
      //   391: aload 11
      //   393: astore 14
      //   395: goto -213 -> 182
      //   398: astore 23
      //   400: goto -218 -> 182
      //   403: astore 12
      //   405: aload 11
      //   407: astore 8
      //   409: goto -160 -> 249
      //   412: aload 11
      //   414: astore 14
      //   416: goto -234 -> 182
      //
      // Exception table:
      //   from	to	target	type
      //   11	29	79	java/lang/Exception
      //   122	134	247	java/io/IOException
      //   207	216	266	java/io/IOException
      //   216	240	266	java/io/IOException
      //   207	216	312	java/lang/Exception
      //   216	240	312	java/lang/Exception
      //   156	168	389	java/io/IOException
      //   168	175	398	java/io/IOException
      //   134	141	403	java/io/IOException
    }

    private void copyPrepackagedAssetsToLocalCopyIfNeeded()
    {
      if (isNewPackage())
      {
        WLUtils.debug("New installation/upgrade detected, copying resources and saving new checksum");
        WLUtils.clearWLPref(WLDroidGap.this.getApplicationContext());
        if (copyOrUnpackAssetsToLocalCopy())
          setNewInstallTime();
      }
      else
      {
        WLDroidGap.this.testResourcesChecksum();
      }
    }

    private void handleNotEnoughSpace(Long paramLong, long paramLong1)
    {
      DecimalFormat localDecimalFormat = new DecimalFormat("#.##");
      WLUtils.error("Application installation failed because there is insufficient storage space available. Free " + localDecimalFormat.format(paramLong.longValue() / 1048576.0D) + " MB and try again");
      WLDroidGap.this.runOnUiThread(new Runnable(localDecimalFormat, paramLong)
      {
        public void run()
        {
          WLUtils.showDialog(WLDroidGap.this, "Error", "Application installation failed because there is insufficient storage space available. Free " + this.val$decimalFormat.format(this.val$spaceNeeded.longValue() / 1048576.0D) + " MB and try again.", "Exit", new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              paramDialogInterface.dismiss();
              System.exit(0);
            }
          });
        }
      });
    }

    private boolean isNewPackage()
    {
      try
      {
        this.appInstallTime = new File(WLDroidGap.this.getPackageManager().getApplicationInfo(WLDroidGap.this.getPackageName(), 0).sourceDir).lastModified();
        long l1 = WLDroidGap.this.getPreferences(0).getLong("appInstallTime", 0L);
        long l2 = this.appInstallTime;
        boolean bool = l2 < l1;
        int i = 0;
        if (bool)
          i = 1;
        return i;
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
      }
      throw new RuntimeException("PrepackagedAssetsManager Failure. Can't retrieve ApplicationInfo - something fundumental went wrong here.", localNameNotFoundException);
    }

    private void setNewInstallTime()
    {
      SharedPreferences.Editor localEditor = WLDroidGap.this.getPreferences(0).edit();
      localEditor.putLong("appInstallTime", this.appInstallTime);
      localEditor.commit();
    }

    class Checksum
      implements Comparable<Checksum>
    {
      private String checksum;
      private Long date;
      private String machine;

      public Checksum(JSONObject arg2)
        throws JSONException
      {
        Object localObject;
        this.checksum = localObject.getString("checksum");
        this.date = Long.valueOf(localObject.getLong("date"));
        this.machine = localObject.getString("machine");
      }

      public int compareTo(Checksum paramChecksum)
      {
        if (this.checksum.compareTo(paramChecksum.checksum) != 0)
          return this.date.compareTo(paramChecksum.date);
        return 0;
      }

      public String getChecksum()
      {
        return this.checksum;
      }

      public Long getDate()
      {
        return this.date;
      }

      public String getMachine()
      {
        return this.machine;
      }
    }
  }

  private class PrepareClientAsyncTask extends AsyncTask<Bundle, Void, Bundle>
  {
    private PrepareClientAsyncTask()
    {
    }

    protected Bundle doInBackground(Bundle[] paramArrayOfBundle)
    {
      WLDroidGap.this.doPrepareAssetsWork();
      return paramArrayOfBundle[0];
    }

    protected void onPostExecute(Bundle paramBundle)
    {
      WLDroidGap.this.onWLInitCompleted(paramBundle);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.WLDroidGap
 * JD-Core Version:    0.6.0
 */