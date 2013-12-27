package org.apache.cordova;

import android.webkit.JavascriptInterface;
import org.apache.cordova.api.PluginManager;
import org.json.JSONException;

class ExposedJsApi
{
  private NativeToJsMessageQueue jsMessageQueue;
  private PluginManager pluginManager;

  public ExposedJsApi(PluginManager paramPluginManager, NativeToJsMessageQueue paramNativeToJsMessageQueue)
  {
    this.pluginManager = paramPluginManager;
    this.jsMessageQueue = paramNativeToJsMessageQueue;
  }

  @JavascriptInterface
  public String exec(String paramString1, String paramString2, String paramString3, String paramString4)
    throws JSONException
  {
    this.jsMessageQueue.setPaused(true);
    try
    {
      this.pluginManager.exec(paramString1, paramString2, paramString3, paramString4);
      String str = this.jsMessageQueue.popAndEncode();
      return str;
    }
    finally
    {
      this.jsMessageQueue.setPaused(false);
    }
    throw localObject;
  }

  @JavascriptInterface
  public String retrieveJsMessages()
  {
    return this.jsMessageQueue.popAndEncode();
  }

  @JavascriptInterface
  public void setNativeToJsBridgeMode(int paramInt)
  {
    this.jsMessageQueue.setBridgeMode(paramInt);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.ExposedJsApi
 * JD-Core Version:    0.6.0
 */