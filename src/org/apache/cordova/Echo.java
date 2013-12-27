package org.apache.cordova;

import java.util.concurrent.ExecutorService;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONException;

public class Echo extends CordovaPlugin
{
  public boolean execute(String paramString, CordovaArgs paramCordovaArgs, CallbackContext paramCallbackContext)
    throws JSONException
  {
    if ("echo".equals(paramString))
    {
      boolean bool2 = paramCordovaArgs.isNull(0);
      String str2 = null;
      if (bool2);
      while (true)
      {
        paramCallbackContext.success(str2);
        return true;
        str2 = paramCordovaArgs.getString(0);
      }
    }
    if ("echoAsync".equals(paramString))
    {
      boolean bool1 = paramCordovaArgs.isNull(0);
      String str1 = null;
      if (bool1);
      while (true)
      {
        this.cordova.getThreadPool().execute(new Runnable(paramCallbackContext, str1)
        {
          public void run()
          {
            this.val$callbackContext.success(this.val$result);
          }
        });
        return true;
        str1 = paramCordovaArgs.getString(0);
      }
    }
    if ("echoArrayBuffer".equals(paramString))
    {
      paramCallbackContext.success(paramCordovaArgs.getArrayBuffer(0));
      return true;
    }
    return false;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.Echo
 * JD-Core Version:    0.6.0
 */