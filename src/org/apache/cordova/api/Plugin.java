package org.apache.cordova.api;

import java.util.concurrent.ExecutorService;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Deprecated
public abstract class Plugin extends CordovaPlugin
{
  public LegacyContext ctx;

  public void error(String paramString1, String paramString2)
  {
    this.webView.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, paramString1), paramString2);
  }

  public void error(PluginResult paramPluginResult, String paramString)
  {
    this.webView.sendPluginResult(paramPluginResult, paramString);
  }

  public void error(JSONObject paramJSONObject, String paramString)
  {
    this.webView.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, paramJSONObject), paramString);
  }

  public abstract PluginResult execute(String paramString1, JSONArray paramJSONArray, String paramString2);

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    String str = paramCallbackContext.getCallbackId();
    if (!isSynch(paramString));
    for (int i = 1; i != 0; i = 0)
    {
      this.cordova.getThreadPool().execute(new Runnable(paramString, paramJSONArray, str)
      {
        public void run()
        {
          try
          {
            PluginResult localPluginResult2 = Plugin.this.execute(this.val$action, this.val$args, this.val$callbackId);
            localPluginResult1 = localPluginResult2;
            Plugin.this.sendPluginResult(localPluginResult1, this.val$callbackId);
            return;
          }
          catch (Throwable localThrowable)
          {
            while (true)
              PluginResult localPluginResult1 = new PluginResult(PluginResult.Status.ERROR, localThrowable.getMessage());
          }
        }
      });
      return true;
    }
    PluginResult localPluginResult = execute(paramString, paramJSONArray, str);
    if (localPluginResult == null)
      localPluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
    paramCallbackContext.sendPluginResult(localPluginResult);
    return true;
  }

  public void initialize(CordovaInterface paramCordovaInterface, CordovaWebView paramCordovaWebView)
  {
    super.initialize(paramCordovaInterface, paramCordovaWebView);
    setContext(paramCordovaInterface);
    setView(paramCordovaWebView);
  }

  public boolean isSynch(String paramString)
  {
    return false;
  }

  public void sendJavascript(String paramString)
  {
    this.webView.sendJavascript(paramString);
  }

  public void sendPluginResult(PluginResult paramPluginResult, String paramString)
  {
    this.webView.sendPluginResult(paramPluginResult, paramString);
  }

  public void setContext(CordovaInterface paramCordovaInterface)
  {
    this.cordova = paramCordovaInterface;
    this.ctx = new LegacyContext(this.cordova);
  }

  public void setView(CordovaWebView paramCordovaWebView)
  {
    this.webView = paramCordovaWebView;
  }

  public void success(String paramString1, String paramString2)
  {
    this.webView.sendPluginResult(new PluginResult(PluginResult.Status.OK, paramString1), paramString2);
  }

  public void success(PluginResult paramPluginResult, String paramString)
  {
    this.webView.sendPluginResult(paramPluginResult, paramString);
  }

  public void success(JSONObject paramJSONObject, String paramString)
  {
    this.webView.sendPluginResult(new PluginResult(PluginResult.Status.OK, paramJSONObject), paramString);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.api.Plugin
 * JD-Core Version:    0.6.0
 */