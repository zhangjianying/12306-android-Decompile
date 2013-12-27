package org.apache.cordova;

import android.app.Activity;
import java.util.HashMap;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.LOG;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class App extends CordovaPlugin
{
  public void backHistory()
  {
    this.cordova.getActivity().runOnUiThread(new Runnable()
    {
      public void run()
      {
        App.this.webView.backHistory();
      }
    });
  }

  public void clearCache()
  {
    this.webView.clearCache(true);
  }

  public void clearHistory()
  {
    this.webView.clearHistory();
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    PluginResult.Status localStatus = PluginResult.Status.OK;
    while (true)
    {
      try
      {
        if (!paramString.equals("clearCache"))
          continue;
        clearCache();
        paramCallbackContext.sendPluginResult(new PluginResult(localStatus, ""));
        return true;
        if (paramString.equals("show"))
        {
          this.cordova.getActivity().runOnUiThread(new Runnable()
          {
            public void run()
            {
              App.this.webView.postMessage("spinner", "stop");
            }
          });
          continue;
        }
      }
      catch (JSONException localJSONException)
      {
        paramCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
        return false;
      }
      if (paramString.equals("loadUrl"))
      {
        loadUrl(paramJSONArray.getString(0), paramJSONArray.optJSONObject(1));
        continue;
      }
      if (paramString.equals("cancelLoadUrl"))
        continue;
      if (paramString.equals("clearHistory"))
      {
        clearHistory();
        continue;
      }
      if (paramString.equals("backHistory"))
      {
        backHistory();
        continue;
      }
      if (paramString.equals("overrideButton"))
      {
        overrideButton(paramJSONArray.getString(0), paramJSONArray.getBoolean(1));
        continue;
      }
      if (paramString.equals("overrideBackbutton"))
      {
        overrideBackbutton(paramJSONArray.getBoolean(0));
        continue;
      }
      if (!paramString.equals("exitApp"))
        continue;
      exitApp();
    }
  }

  public void exitApp()
  {
    this.webView.postMessage("exit", null);
  }

  public boolean isBackbuttonOverridden()
  {
    return this.webView.isBackButtonBound();
  }

  public void loadUrl(String paramString, JSONObject paramJSONObject)
    throws JSONException
  {
    LOG.d("App", "App.loadUrl(" + paramString + "," + paramJSONObject + ")");
    HashMap localHashMap = new HashMap();
    boolean bool1 = false;
    boolean bool2 = false;
    int i = 0;
    if (paramJSONObject != null)
    {
      JSONArray localJSONArray = paramJSONObject.names();
      int j = 0;
      if (j < localJSONArray.length())
      {
        String str = localJSONArray.getString(j);
        if (str.equals("wait"))
          i = paramJSONObject.getInt(str);
        while (true)
        {
          j++;
          break;
          if (str.equalsIgnoreCase("openexternal"))
          {
            bool2 = paramJSONObject.getBoolean(str);
            continue;
          }
          if (str.equalsIgnoreCase("clearhistory"))
          {
            bool1 = paramJSONObject.getBoolean(str);
            continue;
          }
          Object localObject2 = paramJSONObject.get(str);
          if (localObject2 == null)
            continue;
          if (localObject2.getClass().equals(String.class))
          {
            localHashMap.put(str, (String)localObject2);
            continue;
          }
          if (localObject2.getClass().equals(Boolean.class))
          {
            localHashMap.put(str, (Boolean)localObject2);
            continue;
          }
          if (!localObject2.getClass().equals(Integer.class))
            continue;
          localHashMap.put(str, (Integer)localObject2);
        }
      }
    }
    if (i > 0);
    try
    {
      monitorenter;
      long l = i;
      try
      {
        wait(l);
        monitorexit;
        this.webView.showWebPage(paramString, bool2, bool1, localHashMap);
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

  public void overrideBackbutton(boolean paramBoolean)
  {
    LOG.i("App", "WARNING: Back Button Default Behaviour will be overridden.  The backbutton event will be fired!");
    this.webView.bindButton(paramBoolean);
  }

  public void overrideButton(String paramString, boolean paramBoolean)
  {
    LOG.i("DroidGap", "WARNING: Volume Button Default Behaviour will be overridden.  The volume event will be fired!");
    this.webView.bindButton(paramString, paramBoolean);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.App
 * JD-Core Version:    0.6.0
 */