package com.worklight.androidgap.plugin;

import android.app.Activity;
import android.content.Intent;
import com.worklight.common.WLUtils;
import java.util.Iterator;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NativePage extends CordovaPlugin
{
  public static final String ACTION_SHOW = "show";
  public static int NATIVE_ACTIVITY_REQ_CODE = 54681;

  private Intent createIntentFromJSONData(String paramString)
    throws JSONException
  {
    Intent localIntent = new Intent();
    JSONObject localJSONObject = new JSONObject(paramString);
    Iterator localIterator = localJSONObject.keys();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      Object localObject = localJSONObject.get(str);
      if (localJSONObject.isNull(str))
      {
        localIntent.putExtra(str, null);
        continue;
      }
      if (localObject.getClass() == Boolean.class)
      {
        localIntent.putExtra(str, (Boolean)localObject);
        continue;
      }
      if (localObject.getClass() == Byte.class)
      {
        localIntent.putExtra(str, (Byte)localObject);
        continue;
      }
      if (localObject.getClass() == Character.class)
      {
        localIntent.putExtra(str, (Character)localObject);
        continue;
      }
      if (localObject.getClass() == Double.class)
      {
        localIntent.putExtra(str, (Double)localObject);
        continue;
      }
      if (localObject.getClass() == Float.class)
      {
        localIntent.putExtra(str, (Float)localObject);
        continue;
      }
      if (localObject.getClass() == Integer.class)
      {
        localIntent.putExtra(str, (Integer)localObject);
        continue;
      }
      if (localObject.getClass() == Long.class)
      {
        localIntent.putExtra(str, (Long)localObject);
        continue;
      }
      if (localObject.getClass() == Short.class)
      {
        localIntent.putExtra(str, (Short)localObject);
        continue;
      }
      if (localObject.getClass() == String.class)
      {
        localIntent.putExtra(str, (String)localObject);
        continue;
      }
      WLUtils.debug("NAtivePage.createIntentFromJSONData: " + localObject.getClass().toString() + " is not supported type.");
    }
    return localIntent;
  }

  private void show(JSONArray paramJSONArray)
    throws Exception
  {
    if (paramJSONArray.length() < 2)
      throw new Exception("Expected parameters: cookies, activityClassName");
    String str1 = paramJSONArray.getString(0);
    String str2 = paramJSONArray.getString(1);
    int i = paramJSONArray.length();
    String str3 = null;
    if (i > 2)
      str3 = paramJSONArray.getString(2);
    com.worklight.wlclient.api.WLCookieExtractor.cookies = str1;
    Intent localIntent = new Intent(this.cordova.getActivity().getApplicationContext(), Class.forName(str2));
    if ((str3 != null) && (str3.contains("{")) && (str3.contains("}")))
      localIntent.putExtras(createIntentFromJSONData(str3));
    this.cordova.startActivityForResult(null, localIntent, NATIVE_ACTIVITY_REQ_CODE);
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    try
    {
      if ("show".equals(paramString))
      {
        show(paramJSONArray);
        paramCallbackContext.success(PluginResult.Status.OK.name());
        return true;
      }
      paramCallbackContext.error("Invalid action: " + paramString);
      return true;
    }
    catch (Exception localException)
    {
      WLUtils.debug("NativePage.show failed. Reason is: " + localException.getMessage(), localException);
      paramCallbackContext.error("NativePage.show failed. Reason is: " + localException.getMessage());
    }
    return true;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.NativePage
 * JD-Core Version:    0.6.0
 */