package com.worklight.androidgap.plugin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.IBinder;
import android.util.Log;
import com.worklight.androidgap.WLForegroundService.ForegroundBinder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

public class ForegroundBinderPlugin extends CordovaPlugin
{
  public static final String TAG = "WLForegroundBinder";
  private Context ctx;
  private Integer notificationIdentifier;
  private ServiceConnection serviceConnection;

  private void bindService(JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    Log.d("WLForegroundBinder", "trying to bind service");
    Intent localIntent = new Intent();
    Context localContext = this.ctx.getApplicationContext();
    localIntent.setClassName(localContext, this.ctx.getPackageName() + ".ForegroundService");
    if (this.serviceConnection != null)
    {
      paramCallbackContext.error("already bound to service");
      return;
    }
    String str1 = paramJSONArray.getString(0);
    String str2 = paramJSONArray.getString(1);
    String str3 = paramJSONArray.getString(2);
    String str4 = paramJSONArray.getString(3);
    String str5 = paramJSONArray.getString(4);
    int i = paramJSONArray.getInt(5);
    Object localObject1 = this.cordova.getActivity().getClass();
    try
    {
      if (paramJSONArray.length() >= 7)
      {
        Class localClass = Class.forName(paramJSONArray.getString(6), false, localContext.getClassLoader());
        localObject1 = localClass;
      }
      localObject2 = localObject1;
      j = 17301579;
      this.notificationIdentifier = Integer.valueOf(i);
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      try
      {
        Object localObject2;
        int m = localContext.getResources().getIdentifier(str5, "drawable", localContext.getPackageName());
        int j = m;
        int k = j;
        try
        {
          if (!localContext.bindService(localIntent, new ServiceConnection(k, str2, localObject2, str3, str4, i)
          {
            public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
            {
              ForegroundBinderPlugin.access$102(ForegroundBinderPlugin.this, this);
              Notification localNotification = new Notification(this.val$iconId, this.val$popupText, System.currentTimeMillis());
              PendingIntent localPendingIntent = PendingIntent.getActivity(ForegroundBinderPlugin.this.ctx, 0, new Intent(ForegroundBinderPlugin.this.ctx, this.val$activityClass), 0);
              localNotification.setLatestEventInfo(ForegroundBinderPlugin.this.ctx, this.val$titleText, this.val$explainText, localPendingIntent);
              localNotification.flags = (0x2 | localNotification.flags);
              ((WLForegroundService.ForegroundBinder)paramIBinder).makeForeground(this.val$notificationId, localNotification);
            }

            public void onServiceDisconnected(ComponentName paramComponentName)
            {
              Log.d("WLForegroundBinder", "service disconnected");
              ForegroundBinderPlugin.this.cancelNotification();
            }
          }
          , 1))
            break label340;
          paramCallbackContext.success();
          return;
        }
        catch (Exception localException2)
        {
          paramCallbackContext.error("Could not bind to foreground service, reason: " + localException2.getMessage());
          return;
        }
        localClassNotFoundException = localClassNotFoundException;
        Log.e(str1, "Class not found: " + paramJSONArray.getString(6) + ". Using default.");
      }
      catch (Exception localException1)
      {
        while (true)
          Log.e(str1, "Icon not found: " + str5);
        label340: paramCallbackContext.error("Could not bind to foreground service");
      }
    }
  }

  private void cancelNotification()
  {
    if (this.notificationIdentifier != null)
      ((NotificationManager)this.ctx.getSystemService("notification")).cancel(this.notificationIdentifier.intValue());
    this.notificationIdentifier = null;
  }

  private static String toAllCaps(String paramString)
  {
    Matcher localMatcher = Pattern.compile("[A-Z]?[a-z]*").matcher(paramString);
    StringBuilder localStringBuilder = new StringBuilder();
    while (localMatcher.find())
    {
      localStringBuilder.append(localMatcher.group());
      if (localMatcher.hitEnd())
        continue;
      localStringBuilder.append("_");
    }
    return localStringBuilder.toString().toUpperCase();
  }

  private void unbindService(CallbackContext paramCallbackContext)
  {
    if (this.serviceConnection != null)
    {
      this.ctx.getApplicationContext().unbindService(this.serviceConnection);
      cancelNotification();
      paramCallbackContext.success();
      this.serviceConnection = null;
      return;
    }
    paramCallbackContext.error("no connection to service exists");
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    Log.d("WLForegroundBinder", "execute called");
    this.ctx = this.cordova.getActivity();
    try
    {
      Action localAction = Action.valueOf(toAllCaps(paramString));
      switch (2.$SwitchMap$com$worklight$androidgap$plugin$ForegroundBinderPlugin$Action[localAction.ordinal()])
      {
      case 1:
        bindService(paramJSONArray, paramCallbackContext);
        return true;
      case 2:
        unbindService(paramCallbackContext);
        return false;
      }
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
    }
    return false;
  }

  public void onDestroy()
  {
    cancelNotification();
  }

  static enum Action
  {
    static
    {
      Action[] arrayOfAction = new Action[2];
      arrayOfAction[0] = BIND_TO_SERVICE;
      arrayOfAction[1] = UNBIND_FROM_SERVICE;
      $VALUES = arrayOfAction;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.ForegroundBinderPlugin
 * JD-Core Version:    0.6.0
 */