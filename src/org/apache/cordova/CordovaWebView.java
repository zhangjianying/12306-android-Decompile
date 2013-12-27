package org.apache.cordova;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebHistoryItem;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.FrameLayout.LayoutParams;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.LOG;
import org.apache.cordova.api.PluginManager;
import org.apache.cordova.api.PluginResult;

public class CordovaWebView extends WebView
{
  static final FrameLayout.LayoutParams COVER_SCREEN_GRAVITY_CENTER = new FrameLayout.LayoutParams(-1, -1, 17);
  public static final String TAG = "CordovaWebView";
  private boolean bound;
  private CordovaChromeClient chromeClient;
  private CordovaInterface cordova;
  ExposedJsApi exposedJsApi;
  private boolean handleButton = false;
  NativeToJsMessageQueue jsMessageQueue;
  private ArrayList<Integer> keyDownCodes = new ArrayList();
  private ArrayList<Integer> keyUpCodes = new ArrayList();
  private long lastMenuEventTime = 0L;
  int loadUrlTimeout = 0;
  private View mCustomView;
  private WebChromeClient.CustomViewCallback mCustomViewCallback;
  private ActivityResult mResult = null;
  private boolean paused;
  public PluginManager pluginManager;
  private BroadcastReceiver receiver;
  private String url;
  CordovaWebViewClient viewClient;

  public CordovaWebView(Context paramContext)
  {
    super(paramContext);
    if (CordovaInterface.class.isInstance(paramContext))
      this.cordova = ((CordovaInterface)paramContext);
    while (true)
    {
      loadConfiguration();
      setup();
      return;
      Log.d("CordovaWebView", "Your activity must implement CordovaInterface to work");
    }
  }

  public CordovaWebView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    if (CordovaInterface.class.isInstance(paramContext))
      this.cordova = ((CordovaInterface)paramContext);
    while (true)
    {
      setWebChromeClient(new CordovaChromeClient(this.cordova, this));
      initWebViewClient(this.cordova);
      loadConfiguration();
      setup();
      return;
      Log.d("CordovaWebView", "Your activity must implement CordovaInterface to work");
    }
  }

  public CordovaWebView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    if (CordovaInterface.class.isInstance(paramContext))
      this.cordova = ((CordovaInterface)paramContext);
    while (true)
    {
      setWebChromeClient(new CordovaChromeClient(this.cordova, this));
      loadConfiguration();
      setup();
      return;
      Log.d("CordovaWebView", "Your activity must implement CordovaInterface to work");
    }
  }

  @TargetApi(11)
  public CordovaWebView(Context paramContext, AttributeSet paramAttributeSet, int paramInt, boolean paramBoolean)
  {
    super(paramContext, paramAttributeSet, paramInt, paramBoolean);
    if (CordovaInterface.class.isInstance(paramContext))
      this.cordova = ((CordovaInterface)paramContext);
    while (true)
    {
      setWebChromeClient(new CordovaChromeClient(this.cordova));
      initWebViewClient(this.cordova);
      loadConfiguration();
      setup();
      return;
      Log.d("CordovaWebView", "Your activity must implement CordovaInterface to work");
    }
  }

  private void exposeJsInterface()
  {
    int i = Build.VERSION.SDK_INT;
    if ((i >= 11) && (i <= 13));
    for (int j = 1; (j != 0) || (i < 9); j = 0)
    {
      Log.i("CordovaWebView", "Disabled addJavascriptInterface() bridge since Android version is old.");
      return;
    }
    if ((i < 11) && (Build.MANUFACTURER.equals("unknown")))
    {
      Log.i("CordovaWebView", "Disabled addJavascriptInterface() bridge callback due to a bug on the 2.3 emulator");
      return;
    }
    addJavascriptInterface(this.exposedJsApi, "_cordovaNative");
  }

  private void initWebViewClient(CordovaInterface paramCordovaInterface)
  {
    if ((Build.VERSION.SDK_INT < 11) || (Build.VERSION.SDK_INT > 17))
    {
      setWebViewClient(new CordovaWebViewClient(this.cordova, this));
      return;
    }
    setWebViewClient(new IceCreamCordovaWebViewClient(this.cordova, this));
  }

  private void loadConfiguration()
  {
    if ("true".equals(getProperty("fullscreen", "false")))
    {
      this.cordova.getActivity().getWindow().clearFlags(2048);
      this.cordova.getActivity().getWindow().setFlags(1024, 1024);
    }
  }

  @SuppressLint({"NewApi"})
  private void setup()
  {
    setInitialScale(0);
    setVerticalScrollBarEnabled(false);
    requestFocusFromTouch();
    WebSettings localWebSettings = getSettings();
    localWebSettings.setJavaScriptEnabled(true);
    localWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    localWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
    try
    {
      Class[] arrayOfClass = new Class[1];
      arrayOfClass[0] = Boolean.TYPE;
      Method localMethod = WebSettings.class.getMethod("setNavDump", arrayOfClass);
      if (Build.VERSION.SDK_INT < 11)
      {
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Boolean.valueOf(true);
        localMethod.invoke(localWebSettings, arrayOfObject);
      }
      localWebSettings.setSaveFormData(false);
      localWebSettings.setSavePassword(false);
      if (Build.VERSION.SDK_INT > 15)
        Level16Apis.enableUniversalAccess(localWebSettings);
      String str = this.cordova.getActivity().getApplicationContext().getDir("database", 0).getPath();
      if (Build.VERSION.SDK_INT < 11)
      {
        localWebSettings.setDatabaseEnabled(true);
        localWebSettings.setDatabasePath(str);
      }
      localWebSettings.setGeolocationDatabasePath(str);
      localWebSettings.setDomStorageEnabled(true);
      localWebSettings.setGeolocationEnabled(true);
      localWebSettings.setAppCacheMaxSize(5242880L);
      localWebSettings.setAppCachePath(this.cordova.getActivity().getApplicationContext().getDir("database", 0).getPath());
      localWebSettings.setAppCacheEnabled(true);
      updateUserAgentString();
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addAction("android.intent.action.CONFIGURATION_CHANGED");
      if (this.receiver == null)
      {
        this.receiver = new BroadcastReceiver()
        {
          public void onReceive(Context paramContext, Intent paramIntent)
          {
            CordovaWebView.this.updateUserAgentString();
          }
        };
        this.cordova.getActivity().registerReceiver(this.receiver, localIntentFilter);
      }
      this.pluginManager = new PluginManager(this, this.cordova);
      this.jsMessageQueue = new NativeToJsMessageQueue(this, this.cordova);
      this.exposedJsApi = new ExposedJsApi(this.pluginManager, this.jsMessageQueue);
      exposeJsInterface();
      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      while (true)
        Log.d("CordovaWebView", "We are on a modern version of Android, we will deprecate HTC 2.3 devices in 2.8");
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      while (true)
        Log.d("CordovaWebView", "Doing the NavDump failed with bad arguments");
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      while (true)
        Log.d("CordovaWebView", "This should never happen: IllegalAccessException means this isn't Android anymore");
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      while (true)
        Log.d("CordovaWebView", "This should never happen: InvocationTargetException means this isn't Android anymore.");
    }
  }

  private void updateUserAgentString()
  {
    getSettings().getUserAgentString();
  }

  public boolean backHistory()
  {
    if (super.canGoBack())
    {
      printBackForwardList();
      super.goBack();
      return true;
    }
    return false;
  }

  public void bindButton(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramBoolean1)
    {
      this.keyDownCodes.add(Integer.valueOf(paramInt));
      return;
    }
    this.keyUpCodes.add(Integer.valueOf(paramInt));
  }

  public void bindButton(String paramString, boolean paramBoolean)
  {
    if (paramString.compareTo("volumeup") == 0)
      this.keyDownCodes.add(Integer.valueOf(24));
    do
      return;
    while (paramString.compareTo("volumedown") != 0);
    this.keyDownCodes.add(Integer.valueOf(25));
  }

  public void bindButton(boolean paramBoolean)
  {
    this.bound = paramBoolean;
  }

  public String getProperty(String paramString1, String paramString2)
  {
    Bundle localBundle = this.cordova.getActivity().getIntent().getExtras();
    if (localBundle == null);
    Object localObject;
    do
    {
      return paramString2;
      localObject = localBundle.get(paramString1);
    }
    while (localObject == null);
    return localObject.toString();
  }

  public CordovaChromeClient getWebChromeClient()
  {
    return this.chromeClient;
  }

  public boolean hadKeyEvent()
  {
    return this.handleButton;
  }

  public void handleDestroy()
  {
    loadUrlIntoView("javascript:try{cordova.require('cordova/channel').onDestroy.fire();}catch(e){console.log('exception firing destroy event from native');};");
    loadUrl("about:blank");
    if (this.pluginManager != null)
      this.pluginManager.onDestroy();
    if (this.receiver != null);
    try
    {
      this.cordova.getActivity().unregisterReceiver(this.receiver);
      return;
    }
    catch (Exception localException)
    {
      Log.e("CordovaWebView", "Error unregistering configuration receiver: " + localException.getMessage(), localException);
    }
  }

  public void handlePause(boolean paramBoolean)
  {
    LOG.d("CordovaWebView", "Handle the pause");
    loadUrl("javascript:try{cordova.fireDocumentEvent('pause');}catch(e){console.log('exception firing pause event from native');};");
    if (this.pluginManager != null)
      this.pluginManager.onPause(paramBoolean);
    if (!paramBoolean)
      pauseTimers();
    this.paused = true;
  }

  public void handleResume(boolean paramBoolean1, boolean paramBoolean2)
  {
    loadUrl("javascript:try{cordova.fireDocumentEvent('resume');}catch(e){console.log('exception firing resume event from native');};");
    if (this.pluginManager != null)
      this.pluginManager.onResume(paramBoolean1);
    resumeTimers();
    this.paused = false;
  }

  public void hideCustomView()
  {
    Log.d("CordovaWebView", "Hidding Custom View");
    if (this.mCustomView == null)
      return;
    this.mCustomView.setVisibility(8);
    ((ViewGroup)getParent()).removeView(this.mCustomView);
    this.mCustomView = null;
    this.mCustomViewCallback.onCustomViewHidden();
    setVisibility(0);
  }

  public boolean isBackButtonBound()
  {
    return this.bound;
  }

  public boolean isCustomViewShowing()
  {
    return this.mCustomView != null;
  }

  public boolean isPaused()
  {
    return this.paused;
  }

  public void loadUrl(String paramString)
  {
    if ((paramString.equals("about:blank")) || (paramString.startsWith("javascript:")))
    {
      loadUrlNow(paramString);
      return;
    }
    String str = getProperty("url", null);
    if (str == null)
    {
      loadUrlIntoView(paramString);
      return;
    }
    loadUrlIntoView(str);
  }

  public void loadUrl(String paramString, int paramInt)
  {
    String str = getProperty("url", null);
    if (str == null)
    {
      loadUrlIntoView(paramString, paramInt);
      return;
    }
    loadUrlIntoView(str);
  }

  public void loadUrlIntoView(String paramString)
  {
    LOG.d("CordovaWebView", ">>> loadUrl(" + paramString + ")");
    this.url = paramString;
    this.pluginManager.init();
    int i = this.loadUrlTimeout;
    3 local3 = new Runnable(Integer.parseInt(getProperty("loadUrlTimeoutValue", "20000")), this, i, new Runnable(this, paramString)
    {
      public void run()
      {
        this.val$me.stopLoading();
        LOG.e("CordovaWebView", "CordovaWebView: TIMEOUT ERROR!");
        if (CordovaWebView.this.viewClient != null)
          CordovaWebView.this.viewClient.onReceivedError(this.val$me, -6, "The connection to the server was unsuccessful.", this.val$url);
      }
    })
    {
      public void run()
      {
        try
        {
          monitorenter;
          try
          {
            wait(this.val$loadUrlTimeoutValue);
            monitorexit;
            if (this.val$me.loadUrlTimeout == this.val$currentLoadUrlTimeout)
              this.val$me.cordova.getActivity().runOnUiThread(this.val$loadError);
            return;
          }
          finally
          {
            monitorexit;
          }
        }
        catch (InterruptedException localInterruptedException)
        {
          while (true)
            localInterruptedException.printStackTrace();
        }
      }
    };
    this.cordova.getActivity().runOnUiThread(new Runnable(local3, this, paramString)
    {
      public void run()
      {
        new Thread(this.val$timeoutCheck).start();
        this.val$me.loadUrlNow(this.val$url);
      }
    });
  }

  public void loadUrlIntoView(String paramString, int paramInt)
  {
    if ((paramString.startsWith("javascript:")) || (canGoBack()));
    while (true)
    {
      loadUrlIntoView(paramString);
      return;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = paramString;
      arrayOfObject[1] = Integer.valueOf(paramInt);
      LOG.d("CordovaWebView", "DroidGap.loadUrl(%s, %d)", arrayOfObject);
      postMessage("splashscreen", "show");
    }
  }

  void loadUrlNow(String paramString)
  {
    if ((LOG.isLoggable(3)) && (!paramString.startsWith("javascript:")))
      LOG.d("CordovaWebView", ">>> loadUrlNow()");
    if ((paramString.startsWith("file://")) || (paramString.startsWith("javascript:")) || (Config.isUrlWhiteListed(paramString)))
      super.loadUrl(paramString);
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (this.keyDownCodes.contains(Integer.valueOf(paramInt)))
      if (paramInt == 25)
      {
        LOG.d("CordovaWebView", "Down Key Hit");
        loadUrl("javascript:cordova.fireDocumentEvent('volumedownbutton');");
      }
    while (true)
    {
      return true;
      if (paramInt == 24)
      {
        LOG.d("CordovaWebView", "Up Key Hit");
        loadUrl("javascript:cordova.fireDocumentEvent('volumeupbutton');");
        return true;
      }
      return super.onKeyDown(paramInt, paramKeyEvent);
      if (paramInt == 4)
      {
        int i;
        if (startOfHistory())
        {
          boolean bool = this.bound;
          i = 0;
          if (!bool);
        }
        else
        {
          i = 1;
        }
        return i;
      }
      if (paramInt != 82)
        break;
      View localView = getFocusedChild();
      if (localView == null)
        continue;
      ((InputMethodManager)this.cordova.getActivity().getSystemService("input_method")).hideSoftInputFromWindow(localView.getWindowToken(), 0);
      this.cordova.getActivity().openOptionsMenu();
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool = true;
    if (paramInt == 4)
      if (this.mCustomView != null)
        hideCustomView();
    do
    {
      while (true)
      {
        bool = super.onKeyUp(paramInt, paramKeyEvent);
        do
        {
          return bool;
          if (!this.bound)
            continue;
          loadUrl("javascript:cordova.fireDocumentEvent('backbutton');");
          return bool;
        }
        while (backHistory());
        this.cordova.getActivity().finish();
      }
      if (paramInt == 82)
      {
        if (this.lastMenuEventTime < paramKeyEvent.getEventTime())
          loadUrl("javascript:cordova.fireDocumentEvent('menubutton');");
        this.lastMenuEventTime = paramKeyEvent.getEventTime();
        return super.onKeyUp(paramInt, paramKeyEvent);
      }
      if (paramInt != 84)
        continue;
      loadUrl("javascript:cordova.fireDocumentEvent('searchbutton');");
      return bool;
    }
    while (!this.keyUpCodes.contains(Integer.valueOf(paramInt)));
    return super.onKeyUp(paramInt, paramKeyEvent);
  }

  public void onNewIntent(Intent paramIntent)
  {
    if (this.pluginManager != null)
      this.pluginManager.onNewIntent(paramIntent);
  }

  public void postMessage(String paramString, Object paramObject)
  {
    if (this.pluginManager != null)
      this.pluginManager.postMessage(paramString, paramObject);
  }

  public void printBackForwardList()
  {
    WebBackForwardList localWebBackForwardList = copyBackForwardList();
    int i = localWebBackForwardList.getSize();
    for (int j = 0; j < i; j++)
    {
      String str = localWebBackForwardList.getItemAtIndex(j).getUrl();
      LOG.d("CordovaWebView", "The URL at index: " + Integer.toString(j) + "is " + str);
    }
  }

  public WebBackForwardList restoreState(Bundle paramBundle)
  {
    WebBackForwardList localWebBackForwardList = super.restoreState(paramBundle);
    Log.d("CordovaWebView", "WebView restoration crew now restoring!");
    this.pluginManager.init();
    return localWebBackForwardList;
  }

  public void sendJavascript(String paramString)
  {
    this.jsMessageQueue.addJavaScript(paramString);
  }

  public void sendPluginResult(PluginResult paramPluginResult, String paramString)
  {
    this.jsMessageQueue.addPluginResult(paramPluginResult, paramString);
  }

  public void setWebChromeClient(CordovaChromeClient paramCordovaChromeClient)
  {
    this.chromeClient = paramCordovaChromeClient;
    super.setWebChromeClient(paramCordovaChromeClient);
  }

  public void setWebViewClient(CordovaWebViewClient paramCordovaWebViewClient)
  {
    this.viewClient = paramCordovaWebViewClient;
    super.setWebViewClient(paramCordovaWebViewClient);
  }

  public void showCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
  {
    Log.d("CordovaWebView", "showing Custom View");
    if (this.mCustomView != null)
    {
      paramCustomViewCallback.onCustomViewHidden();
      return;
    }
    this.mCustomView = paramView;
    this.mCustomViewCallback = paramCustomViewCallback;
    ViewGroup localViewGroup = (ViewGroup)getParent();
    localViewGroup.addView(paramView, COVER_SCREEN_GRAVITY_CENTER);
    setVisibility(8);
    localViewGroup.setVisibility(0);
    localViewGroup.bringToFront();
  }

  public void showWebPage(String paramString, boolean paramBoolean1, boolean paramBoolean2, HashMap<String, Object> paramHashMap)
  {
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = paramString;
    arrayOfObject[1] = Boolean.valueOf(paramBoolean1);
    arrayOfObject[2] = Boolean.valueOf(paramBoolean2);
    LOG.d("CordovaWebView", "showWebPage(%s, %b, %b, HashMap", arrayOfObject);
    if (paramBoolean2)
      clearHistory();
    if (!paramBoolean1)
    {
      if ((paramString.startsWith("file://")) || (Config.isUrlWhiteListed(paramString)))
      {
        loadUrl(paramString);
        return;
      }
      LOG.w("CordovaWebView", "showWebPage: Cannot load URL into webview since it is not in white list.  Loading into browser instead. (URL=" + paramString + ")");
      try
      {
        Intent localIntent2 = new Intent("android.intent.action.VIEW");
        localIntent2.setData(Uri.parse(paramString));
        this.cordova.getActivity().startActivity(localIntent2);
        return;
      }
      catch (ActivityNotFoundException localActivityNotFoundException2)
      {
        LOG.e("CordovaWebView", "Error loading url " + paramString, localActivityNotFoundException2);
        return;
      }
    }
    try
    {
      Intent localIntent1 = new Intent("android.intent.action.VIEW");
      localIntent1.setData(Uri.parse(paramString));
      this.cordova.getActivity().startActivity(localIntent1);
      return;
    }
    catch (ActivityNotFoundException localActivityNotFoundException1)
    {
      LOG.e("CordovaWebView", "Error loading url " + paramString, localActivityNotFoundException1);
    }
  }

  public boolean startOfHistory()
  {
    WebHistoryItem localWebHistoryItem = copyBackForwardList().getItemAtIndex(0);
    boolean bool = false;
    if (localWebHistoryItem != null)
    {
      String str1 = localWebHistoryItem.getUrl();
      String str2 = getUrl();
      LOG.d("CordovaWebView", "The current URL is: " + str2);
      LOG.d("CordovaWebView", "The URL at item 0 is:" + str1);
      bool = str2.equals(str1);
    }
    return bool;
  }

  public void storeResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mResult = new ActivityResult(paramInt1, paramInt2, paramIntent);
  }

  class ActivityResult
  {
    Intent incoming;
    int request;
    int result;

    public ActivityResult(int paramInt1, int paramIntent, Intent arg4)
    {
      this.request = paramInt1;
      this.result = paramIntent;
      Object localObject;
      this.incoming = localObject;
    }
  }

  @TargetApi(16)
  private static class Level16Apis
  {
    static void enableUniversalAccess(WebSettings paramWebSettings)
    {
      paramWebSettings.setAllowUniversalAccessFromFileURLs(true);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.CordovaWebView
 * JD-Core Version:    0.6.0
 */