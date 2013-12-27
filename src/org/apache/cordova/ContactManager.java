package org.apache.cordova;

import android.os.Build.VERSION;
import android.util.Log;
import java.util.concurrent.ExecutorService;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactManager extends CordovaPlugin
{
  public static final int INVALID_ARGUMENT_ERROR = 1;
  public static final int IO_ERROR = 4;
  private static final String LOG_TAG = "Contact Query";
  public static final int NOT_SUPPORTED_ERROR = 5;
  public static final int PENDING_OPERATION_ERROR = 3;
  public static final int PERMISSION_DENIED_ERROR = 20;
  public static final int TIMEOUT_ERROR = 2;
  public static final int UNKNOWN_ERROR;
  private ContactAccessor contactAccessor;

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    if (Build.VERSION.RELEASE.startsWith("1."))
    {
      paramCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, 5));
      return true;
    }
    if (this.contactAccessor == null)
      this.contactAccessor = new ContactAccessorSdk5(this.webView, this.cordova);
    if (paramString.equals("search"))
    {
      JSONArray localJSONArray = paramJSONArray.getJSONArray(0);
      JSONObject localJSONObject2 = paramJSONArray.getJSONObject(1);
      this.cordova.getThreadPool().execute(new Runnable(localJSONArray, localJSONObject2, paramCallbackContext)
      {
        public void run()
        {
          JSONArray localJSONArray = ContactManager.this.contactAccessor.search(this.val$filter, this.val$options);
          this.val$callbackContext.success(localJSONArray);
        }
      });
      return true;
    }
    if (paramString.equals("save"))
    {
      JSONObject localJSONObject1 = paramJSONArray.getJSONObject(0);
      this.cordova.getThreadPool().execute(new Runnable(localJSONObject1, paramCallbackContext)
      {
        public void run()
        {
          String str = ContactManager.this.contactAccessor.save(this.val$contact);
          Object localObject = null;
          if (str != null);
          try
          {
            JSONObject localJSONObject = ContactManager.this.contactAccessor.getContactById(str);
            localObject = localJSONObject;
            if (localObject != null)
            {
              this.val$callbackContext.success(localObject);
              return;
            }
          }
          catch (JSONException localJSONException)
          {
            while (true)
            {
              Log.e("Contact Query", "JSON fail.", localJSONException);
              localObject = null;
            }
            this.val$callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, 0));
          }
        }
      });
      return true;
    }
    if (paramString.equals("remove"))
    {
      String str = paramJSONArray.getString(0);
      this.cordova.getThreadPool().execute(new Runnable(str, paramCallbackContext)
      {
        public void run()
        {
          if (ContactManager.this.contactAccessor.remove(this.val$contactId))
          {
            this.val$callbackContext.success();
            return;
          }
          this.val$callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, 0));
        }
      });
      return true;
    }
    return false;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.ContactManager
 * JD-Core Version:    0.6.0
 */