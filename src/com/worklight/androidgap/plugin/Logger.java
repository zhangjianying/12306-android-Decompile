package com.worklight.androidgap.plugin;

import android.util.Log;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

public class Logger extends CordovaPlugin
{
  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    try
    {
      ACTION_LOG localACTION_LOG = ACTION_LOG.fromString(paramString);
      if (localACTION_LOG == null)
      {
        paramCallbackContext.error("Invalid action: " + paramString);
        return true;
      }
      localACTION_LOG.log(this.cordova.getClass().getSimpleName(), paramJSONArray.getString(0));
      paramCallbackContext.success("true");
      return true;
    }
    catch (JSONException localJSONException)
    {
      paramCallbackContext.error("Action: " + paramString + " failed. JSON Error is: " + localJSONException.getLocalizedMessage());
    }
    return true;
  }

  public static abstract enum ACTION_LOG
  {
    static
    {
      INFO = new ACTION_LOG("INFO", 1)
      {
        public void log(String paramString1, String paramString2)
        {
          Log.i(paramString1, paramString2);
        }
      };
      WARN = new ACTION_LOG("WARN", 2)
      {
        public void log(String paramString1, String paramString2)
        {
          Log.w(paramString1, paramString2);
        }
      };
      ERROR = new ACTION_LOG("ERROR", 3)
      {
        public void log(String paramString1, String paramString2)
        {
          Log.e(paramString1, paramString2);
        }
      };
      DEBUG = new ACTION_LOG("DEBUG", 4)
      {
        public void log(String paramString1, String paramString2)
        {
          Log.d(paramString1, paramString2);
        }
      };
      ACTION_LOG[] arrayOfACTION_LOG = new ACTION_LOG[5];
      arrayOfACTION_LOG[0] = LOG;
      arrayOfACTION_LOG[1] = INFO;
      arrayOfACTION_LOG[2] = WARN;
      arrayOfACTION_LOG[3] = ERROR;
      arrayOfACTION_LOG[4] = DEBUG;
      $VALUES = arrayOfACTION_LOG;
    }

    public static ACTION_LOG fromString(String paramString)
    {
      try
      {
        ACTION_LOG localACTION_LOG = valueOf(paramString);
        return localACTION_LOG;
      }
      catch (Exception localException)
      {
      }
      return null;
    }

    public abstract void log(String paramString1, String paramString2);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.Logger
 * JD-Core Version:    0.6.0
 */