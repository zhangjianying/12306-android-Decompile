package org.apache.cordova.api;

import android.annotation.TargetApi;
import android.content.Intent;
import android.webkit.WebResourceResponse;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

public class CordovaPlugin
{
  public CordovaInterface cordova;
  public String id;
  public CordovaWebView webView;

  static
  {
    if (!CordovaPlugin.class.desiredAssertionStatus());
    for (boolean bool = true; ; bool = false)
    {
      $assertionsDisabled = bool;
      return;
    }
  }

  public boolean execute(String paramString1, String paramString2, CallbackContext paramCallbackContext)
    throws JSONException
  {
    return execute(paramString1, new JSONArray(paramString2), paramCallbackContext);
  }

  public boolean execute(String paramString, CordovaArgs paramCordovaArgs, CallbackContext paramCallbackContext)
    throws JSONException
  {
    return false;
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    return execute(paramString, new CordovaArgs(paramJSONArray), paramCallbackContext);
  }

  public void initialize(CordovaInterface paramCordovaInterface, CordovaWebView paramCordovaWebView)
  {
    assert (this.cordova == null);
    this.cordova = paramCordovaInterface;
    this.webView = paramCordovaWebView;
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
  }

  public void onDestroy()
  {
  }

  public Object onMessage(String paramString, Object paramObject)
  {
    return null;
  }

  public void onNewIntent(Intent paramIntent)
  {
  }

  public boolean onOverrideUrlLoading(String paramString)
  {
    return false;
  }

  public void onPause(boolean paramBoolean)
  {
  }

  public void onReset()
  {
  }

  public void onResume(boolean paramBoolean)
  {
  }

  @TargetApi(11)
  public WebResourceResponse shouldInterceptRequest(String paramString)
  {
    return null;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.api.CordovaPlugin
 * JD-Core Version:    0.6.0
 */