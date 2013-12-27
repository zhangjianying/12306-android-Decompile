package com.worklight.androidgap.plugin;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.text.ClipboardManager;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;
import com.worklight.androidgap.WLDroidGap;
import com.worklight.common.WLUtils;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginManager;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils extends CordovaPlugin
{
  private static final String MARKET_ANDROID_WEB_URL = "https://market.android.com/";
  private static final String MARKET_URL_PREFIX = "market://";
  private static final String RESULT_ERROR = "result:error";
  private static final String RESULT_HEIGHT = "height";
  private static final String RESULT_WIDTH = "width";

  private boolean callback(String paramString, CallbackContext paramCallbackContext)
  {
    if ("result:error".equals(paramString))
    {
      paramCallbackContext.error(PluginResult.Status.ERROR.name());
      return true;
    }
    paramCallbackContext.success(paramString);
    return true;
  }

  private String clearHistory()
  {
    this.cordova.getActivity().runOnUiThread(new Runnable()
    {
      public void run()
      {
        ((WLDroidGap)Utils.this.cordova.getActivity()).clearHistory();
      }
    });
    return PluginResult.Status.OK.name();
  }

  private String clearSeassionCookie()
  {
    CookieSyncManager.createInstance(this.cordova.getActivity());
    CookieManager.getInstance().removeSessionCookie();
    return PluginResult.Status.OK.name();
  }

  private String copyToClipboard(String paramString)
  {
    ((ClipboardManager)this.cordova.getActivity().getSystemService("clipboard")).setText(paramString);
    WLUtils.debug("Copied text: " + paramString);
    return showToast("The text was sucessfully copied.");
  }

  private JSONObject getInitParamaters(JSONArray paramJSONArray)
    throws JSONException
  {
    if ((paramJSONArray == null) || (paramJSONArray.length() == 0))
      return null;
    JSONObject localJSONObject = new JSONObject();
    String[] arrayOfString = paramJSONArray.getString(0).split(",");
    for (int i = 0; i < arrayOfString.length; i++)
    {
      String str = WLUtils.readWLPref(this.cordova.getActivity(), arrayOfString[i].trim());
      localJSONObject.put(arrayOfString[i], str);
    }
    localJSONObject.put("freeSpace", WLUtils.getFreeSpaceOnDevice());
    return localJSONObject;
  }

  private JSONObject getScreenHeight()
    throws JSONException
  {
    Display localDisplay = ((WindowManager)this.cordova.getActivity().getSystemService("window")).getDefaultDisplay();
    JSONObject localJSONObject = new JSONObject();
    localJSONObject.put("height", localDisplay.getHeight());
    return localJSONObject;
  }

  private JSONObject getScreenSize()
    throws JSONException
  {
    Display localDisplay = ((WindowManager)this.cordova.getActivity().getSystemService("window")).getDefaultDisplay();
    JSONObject localJSONObject = new JSONObject();
    localJSONObject.put("height", localDisplay.getHeight());
    localJSONObject.put("width", localDisplay.getWidth());
    return localJSONObject;
  }

  private JSONObject getScreenWidth()
    throws JSONException
  {
    Display localDisplay = ((WindowManager)this.cordova.getActivity().getSystemService("window")).getDefaultDisplay();
    JSONObject localJSONObject = new JSONObject();
    localJSONObject.put("width", localDisplay.getWidth());
    return localJSONObject;
  }

  private void loadSkin(String paramString1, String paramString2)
  {
    WLDroidGap localWLDroidGap = (WLDroidGap)this.cordova.getActivity();
    if (!new File(localWLDroidGap.getLocalStorageWebRoot() + "/" + paramString1).exists())
    {
      WLUtils.warning("\"default\" skin will be used, because skin named " + paramString1 + " was not found. Add a skin or change android/js/skinLoader.js to return existing skin.");
      paramString1 = "default";
    }
    localWLDroidGap.runOnUiThread(new Runnable(localWLDroidGap, localWLDroidGap.getWebUrl() + "/" + paramString1 + "/" + paramString2)
    {
      public void run()
      {
        this.val$activity.loadUrl(this.val$url);
      }
    });
  }

  private String openURL(String paramString)
  {
    try
    {
      if (paramString.indexOf("market://") > -1);
      Intent localIntent2;
      for (Object localObject = new Intent("android.intent.action.VIEW", Uri.parse(paramString)); ; localObject = localIntent2)
      {
        this.cordova.getActivity().startActivity((Intent)localObject);
        return PluginResult.Status.OK.name();
        localIntent2 = new Intent("android.intent.action.VIEW", Uri.parse(new URL(paramString).toExternalForm()));
      }
    }
    catch (MalformedURLException localMalformedURLException)
    {
      WLUtils.error("WL.App.openURL failed to open web page with URL " + paramString + ", because of incorrect URL format.");
      return "result:error";
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      while (true)
      {
        if (paramString.indexOf("market://") > -1)
        {
          Intent localIntent1 = new Intent("android.intent.action.VIEW", Uri.parse(paramString.replace("market://", "https://market.android.com/")));
          this.cordova.getActivity().startActivity(localIntent1);
          return PluginResult.Status.OK.name();
        }
        WLUtils.error("WL.App.openURL failed to open web page with URL " + paramString + ", because of incorrect URL format.");
      }
    }
  }

  private String showToast(String paramString)
  {
    Toast.makeText(this.cordova.getActivity(), paramString, 1).show();
    return PluginResult.Status.OK.name();
  }

  private String webViewReload()
  {
    this.cordova.getActivity().runOnUiThread(new Runnable()
    {
      public void run()
      {
        Utils.this.webView.reload();
      }
    });
    return PluginResult.Status.OK.name();
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    ACTION localACTION = ACTION.fromString(paramString);
    if (localACTION != null)
    {
      switch (4.$SwitchMap$com$worklight$androidgap$plugin$Utils$ACTION[localACTION.ordinal()])
      {
      default:
        paramCallbackContext.error("Invalid action: " + paramString);
        return true;
      case 1:
        WLUtils.writeWLPref(this.cordova.getActivity(), paramJSONArray.getString(0), paramJSONArray.getString(1));
        return callback(PluginResult.Status.OK.name(), paramCallbackContext);
      case 2:
        paramCallbackContext.success(WLUtils.readWLPref(this.cordova.getActivity(), paramJSONArray.getString(0)));
        return true;
      case 3:
        loadSkin(paramJSONArray.getString(0), paramJSONArray.getString(1));
        return callback(PluginResult.Status.OK.name(), paramCallbackContext);
      case 4:
        paramCallbackContext.success(getScreenHeight());
        return true;
      case 5:
        paramCallbackContext.success(getScreenWidth());
        return true;
      case 6:
        paramCallbackContext.success(getScreenSize());
        return true;
      case 7:
        return callback(openURL(paramJSONArray.getString(0)), paramCallbackContext);
      case 8:
        return callback(Locale.getDefault().toString(), paramCallbackContext);
      case 9:
        return callback(clearSeassionCookie(), paramCallbackContext);
      case 10:
        return callback(copyToClipboard(paramJSONArray.getString(0)), paramCallbackContext);
      case 11:
        return callback(showToast(paramJSONArray.getString(0)), paramCallbackContext);
      case 12:
        return callback(clearHistory(), paramCallbackContext);
      case 13:
        return callback(webViewReload(), paramCallbackContext);
      case 14:
        return callback(Long.toString(WLUtils.getFreeSpaceOnDevice()), paramCallbackContext);
      case 15:
        paramCallbackContext.success(getInitParamaters(paramJSONArray));
        return true;
      case 16:
      }
      return callback(reloadApp(), paramCallbackContext);
    }
    paramCallbackContext.error("Null action");
    return true;
  }

  String reloadApp()
  {
    BusyIndicator localBusyIndicator = (BusyIndicator)this.webView.pluginManager.getPlugin("NativeBusyIndicator");
    if (localBusyIndicator.isShowing())
      localBusyIndicator.hide();
    clearSeassionCookie();
    WLUtils.writeWLPref(this.cordova.getActivity(), "exitOnSkinLoader", "false");
    return webViewReload();
  }

  public static enum ACTION
  {
    static
    {
      readPref = new ACTION("readPref", 1);
      loadSkin = new ACTION("loadSkin", 2);
      getScreenHeight = new ACTION("getScreenHeight", 3);
      getScreenWidth = new ACTION("getScreenWidth", 4);
      getScreenSize = new ACTION("getScreenSize", 5);
      openURL = new ACTION("openURL", 6);
      getDeviceLocale = new ACTION("getDeviceLocale", 7);
      clearSessionCookies = new ACTION("clearSessionCookies", 8);
      copyToClipboard = new ACTION("copyToClipboard", 9);
      toast = new ACTION("toast", 10);
      clearHistory = new ACTION("clearHistory", 11);
      reload = new ACTION("reload", 12);
      getAvailableSpace = new ACTION("getAvailableSpace", 13);
      getInitParameters = new ACTION("getInitParameters", 14);
      reloadApp = new ACTION("reloadApp", 15);
      ACTION[] arrayOfACTION = new ACTION[16];
      arrayOfACTION[0] = writePref;
      arrayOfACTION[1] = readPref;
      arrayOfACTION[2] = loadSkin;
      arrayOfACTION[3] = getScreenHeight;
      arrayOfACTION[4] = getScreenWidth;
      arrayOfACTION[5] = getScreenSize;
      arrayOfACTION[6] = openURL;
      arrayOfACTION[7] = getDeviceLocale;
      arrayOfACTION[8] = clearSessionCookies;
      arrayOfACTION[9] = copyToClipboard;
      arrayOfACTION[10] = toast;
      arrayOfACTION[11] = clearHistory;
      arrayOfACTION[12] = reload;
      arrayOfACTION[13] = getAvailableSpace;
      arrayOfACTION[14] = getInitParameters;
      arrayOfACTION[15] = reloadApp;
      $VALUES = arrayOfACTION;
    }

    public static ACTION fromString(String paramString)
    {
      try
      {
        ACTION localACTION = valueOf(paramString);
        return localACTION;
      }
      catch (Exception localException)
      {
      }
      return null;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.Utils
 * JD-Core Version:    0.6.0
 */