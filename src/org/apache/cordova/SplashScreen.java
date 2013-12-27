package org.apache.cordova;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;

public class SplashScreen extends CordovaPlugin
{
  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
  {
    if (paramString.equals("hide"))
      this.webView.postMessage("splashscreen", "hide");
    while (true)
    {
      paramCallbackContext.success();
      return true;
      if (!paramString.equals("show"))
        break;
      this.webView.postMessage("splashscreen", "show");
    }
    return false;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.SplashScreen
 * JD-Core Version:    0.6.0
 */