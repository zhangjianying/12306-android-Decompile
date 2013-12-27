package com.worklight.androidgap.plugin;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION;
import com.google.android.gcm.GCMRegistrar;
import com.worklight.androidgap.WLDroidGap;
import com.worklight.androidgap.push.GCMErrorCode;
import com.worklight.androidgap.push.WLGCMIntentService.Message;
import com.worklight.common.WLConfig;
import com.worklight.common.WLUtils;
import java.util.ArrayList;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Push extends CordovaPlugin
{
  private static final String FROM_NOTIFICATION_BAR = "notificationBar";
  private static final String WL_CLIENT_PUSH_ONMESSAGE = "WL.Client.Push.__onmessage";
  private String badGCMSetupMsg = null;
  private String deviceToken = null;
  private boolean isGCMSetupValid = true;
  private String messageCallback = null;
  private BroadcastReceiver onMessageReceiver;
  private BroadcastReceiver onRegisterErrorReceiver;
  private BroadcastReceiver onRegisterReceiver;
  private ArrayList<WLGCMIntentService.Message> pending = new ArrayList();

  private void cancelAllNotification()
  {
    ((NotificationManager)this.cordova.getActivity().getSystemService("notification")).cancelAll();
  }

  private boolean dispatch(JSONArray paramJSONArray)
  {
    try
    {
      String str3 = paramJSONArray.getString(0);
      str1 = str3;
      WLUtils.debug("Push.dispatch(): method=" + str1);
      if (str1 != null)
        str1 = str1.trim();
      if ("".equals(str1))
        str1 = null;
      if (((this.messageCallback == null) && (str1 != null)) || ((this.messageCallback != null) && (!this.messageCallback.equals(str1))))
      {
        this.messageCallback = str1;
        StringBuilder localStringBuilder = new StringBuilder().append("Javascript script requests ");
        if (str1 == null)
        {
          str2 = "to stop dispatching";
          WLUtils.debug(str2);
        }
      }
      else
      {
        dispatchPending();
        return true;
      }
    }
    catch (JSONException localJSONException)
    {
      while (true)
      {
        WLUtils.debug("Push.dispatch(): Unable to get method name from args");
        String str1 = null;
        continue;
        String str2 = "dispatching to " + str1;
      }
    }
  }

  private void dispatchPending()
  {
    WLUtils.debug("dispatchPending called. Number of pending messages: " + this.pending.size());
    if ((this.cordova != null) && (this.cordova.getActivity().hasWindowFocus()))
      this.messageCallback = "WL.Client.Push.__onmessage";
    WLUtils.debug("dispatchPending webView=" + this.webView + " messageCallback=" + this.messageCallback);
    if ((this.webView == null) || (this.messageCallback == null));
    while (true)
    {
      return;
      StringBuilder localStringBuilder = new StringBuilder();
      synchronized (this.pending)
      {
        int i = this.pending.size();
        WLGCMIntentService.Message localMessage = null;
        if (i > 0)
          localMessage = (WLGCMIntentService.Message)this.pending.remove(0);
        if (localMessage == null)
          continue;
        WLUtils.debug("Dispatching to javascript " + localMessage.toString());
        localStringBuilder.setLength(0);
        localStringBuilder.append(this.messageCallback);
        localStringBuilder.append('(');
        localStringBuilder.append(localMessage.getProps().toString());
        localStringBuilder.append(',');
        localStringBuilder.append(localMessage.getPayload().toString());
        localStringBuilder.append(");");
        this.webView.sendJavascript(localStringBuilder.toString());
      }
    }
  }

  private boolean subscribe(JSONArray paramJSONArray, CallbackContext paramCallbackContext)
  {
    if (paramCallbackContext == null)
    {
      WLUtils.error("Push notification will not be served by the application because Worklight runtime failed to register a callback function.");
      return false;
    }
    try
    {
      this.cordova.getActivity().unregisterReceiver(this.onRegisterReceiver);
      label27: if (this.onRegisterReceiver == null)
        this.onRegisterReceiver = new BroadcastReceiver(paramCallbackContext)
        {
          public void onReceive(Context paramContext, Intent paramIntent)
          {
            Push.access$202(Push.this, paramIntent.getStringExtra("registrationId"));
            PluginResult localPluginResult = new PluginResult(PluginResult.Status.OK, Push.this.deviceToken);
            localPluginResult.setKeepCallback(false);
            this.val$ctx.sendPluginResult(localPluginResult);
            Push.this.cordova.getActivity().unregisterReceiver(Push.this.onRegisterReceiver);
            Push.this.cordova.getActivity().unregisterReceiver(Push.this.onRegisterErrorReceiver);
          }
        };
      this.cordova.getActivity().registerReceiver(this.onRegisterReceiver, new IntentFilter(WLUtils.getFullAppName(this.cordova.getActivity()) + ".C2DM_REGISTERED"));
      if (this.onRegisterErrorReceiver == null)
        this.onRegisterErrorReceiver = new BroadcastReceiver(paramCallbackContext)
        {
          public void onReceive(Context paramContext, Intent paramIntent)
          {
            String str1 = paramIntent.getStringExtra("errorId");
            try
            {
              GCMErrorCode localGCMErrorCode2 = GCMErrorCode.valueOf(str1);
              localGCMErrorCode1 = localGCMErrorCode2;
              String str2 = localGCMErrorCode1.getDescription();
              if (GCMErrorCode.INVALID_SENDER.toString().equals(str1))
                str2 = str2 + "\nCheck the pushSender attributes in the application-descriptor.xml file. \nUsing a browser login into the 'Google APIs Console page' that will be used for sending push messages.\nThe 'Project Number' should be set as the pushSender 'senderId'.\nNavigate to the API Access tab. The 'API key for server apps' should be set as the pushSender 'key'.";
              WLUtils.debug(str2 + ". Notifying javascript about unsuccessful registration to the GCM service.");
              PluginResult localPluginResult = new PluginResult(PluginResult.Status.ERROR, localGCMErrorCode1.getDescription());
              localPluginResult.setKeepCallback(false);
              this.val$ctx.sendPluginResult(localPluginResult);
              Push.this.cordova.getActivity().unregisterReceiver(Push.this.onRegisterReceiver);
              Push.this.cordova.getActivity().unregisterReceiver(Push.this.onRegisterErrorReceiver);
              return;
            }
            catch (Exception localException)
            {
              while (true)
              {
                GCMErrorCode localGCMErrorCode1 = GCMErrorCode.UNEXPECTED;
                WLUtils.debug("Push Service: additional information for unexpected error: " + str1);
              }
            }
          }
        };
      this.cordova.getActivity().registerReceiver(this.onRegisterErrorReceiver, new IntentFilter(WLUtils.getFullAppName(this.cordova.getActivity()) + ".C2DM_ERROR"));
      Activity localActivity = this.cordova.getActivity();
      String[] arrayOfString = new String[1];
      arrayOfString[0] = WLDroidGap.getWLConfig().getGCMSender();
      GCMRegistrar.register(localActivity, arrayOfString);
      PluginResult localPluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
      localPluginResult.setKeepCallback(true);
      paramCallbackContext.sendPluginResult(localPluginResult);
      return true;
    }
    catch (Exception localException)
    {
      break label27;
    }
  }

  private void unregisterReceivers()
  {
    if (this.cordova != null);
    try
    {
      this.cordova.getActivity().unregisterReceiver(this.onRegisterReceiver);
    }
    catch (Exception localException1)
    {
      try
      {
        while (true)
        {
          this.cordova.getActivity().unregisterReceiver(this.onMessageReceiver);
          return;
          localException1 = localException1;
          WLUtils.debug("unregisterReceivers:" + localException1.getLocalizedMessage());
        }
      }
      catch (Exception localException2)
      {
        WLUtils.debug("unregisterReceivers:" + localException2.getLocalizedMessage());
      }
    }
  }

  private void validateGCMSetup()
  {
    int i = Build.VERSION.SDK_INT;
    if (i < 8)
    {
      this.badGCMSetupMsg = ("Your device does not support GCM. GCM is available for Android 2.2+ (API level 8+). Your device API level is: " + i);
      this.isGCMSetupValid = false;
    }
    try
    {
      GCMRegistrar.checkDevice(this.cordova.getActivity());
    }
    catch (UnsupportedOperationException localUnsupportedOperationException)
    {
      try
      {
        while (true)
        {
          GCMRegistrar.checkManifest(this.cordova.getActivity());
          return;
          localUnsupportedOperationException = localUnsupportedOperationException;
          this.badGCMSetupMsg = ("Your device does not support GCM. " + localUnsupportedOperationException.getMessage() + ". If you are using an android emulator make sure it has Google APIs (Google Inc).");
          this.isGCMSetupValid = false;
        }
      }
      catch (IllegalStateException localIllegalStateException)
      {
        this.badGCMSetupMsg = ("Your application manifest is not properly set up for GCM. " + localIllegalStateException.getMessage() + ". Refer to the Google Android documentation for instructons how to setup up your application manifest for using GCM.");
        this.isGCMSetupValid = false;
      }
    }
  }

  public void dispatchPending(WLGCMIntentService.Message paramMessage)
  {
    synchronized (this.pending)
    {
      this.pending.add(paramMessage);
      this.messageCallback = "WL.Client.Push.__onmessage";
      dispatchPending();
      return;
    }
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    if (!this.isGCMSetupValid)
    {
      paramCallbackContext.error(this.badGCMSetupMsg);
      return true;
    }
    ACTION localACTION = ACTION.fromString(paramString);
    if (localACTION != null)
    {
      switch (4.$SwitchMap$com$worklight$androidgap$plugin$Push$ACTION[localACTION.ordinal()])
      {
      default:
        paramCallbackContext.error("Invalid action: " + paramString);
        return true;
      case 1:
        return subscribe(paramJSONArray, paramCallbackContext);
      case 2:
        paramCallbackContext.success();
        return dispatch(paramJSONArray);
      case 3:
      }
      paramCallbackContext.success(this.deviceToken);
      return true;
    }
    paramCallbackContext.error("Null action");
    return true;
  }

  public void initialize(CordovaInterface paramCordovaInterface, CordovaWebView paramCordovaWebView)
  {
    super.initialize(paramCordovaInterface, paramCordovaWebView);
    validateGCMSetup();
    if (this.isGCMSetupValid)
    {
      unregisterReceivers();
      if (paramCordovaInterface != null)
      {
        if (this.onMessageReceiver == null)
          this.onMessageReceiver = new BroadcastReceiver()
          {
            public void onReceive(Context paramContext, Intent paramIntent)
            {
              WLUtils.debug("Queuing message for dispatch to javascript");
              synchronized (Push.this.pending)
              {
                Push.this.pending.add((WLGCMIntentService.Message)paramIntent.getParcelableExtra("message"));
                Push.this.dispatchPending();
                if (!paramIntent.getBooleanExtra("notificationBar", false))
                  setResultCode(-1);
                return;
              }
            }
          };
        paramCordovaInterface.getActivity().registerReceiver(this.onMessageReceiver, new IntentFilter(WLUtils.getFullAppName(paramCordovaInterface.getActivity()) + ".C2DM_MESSAGE"));
        Activity localActivity = (Activity)paramCordovaInterface;
        if ((WLUtils.getFullAppName(this.cordova.getActivity()) + ".NOTIFICATION").equals(localActivity.getIntent().getAction()))
        {
          WLUtils.debug("Activity started from message notification");
          localActivity.getIntent().putExtra("notificationBar", true);
          this.onMessageReceiver.onReceive(this.cordova.getActivity(), localActivity.getIntent());
        }
      }
      dispatchPending();
    }
  }

  public void onDestroy()
  {
    unregisterReceivers();
    super.onDestroy();
  }

  public void onResume(boolean paramBoolean)
  {
    super.onResume(paramBoolean);
    cancelAllNotification();
  }

  private static enum ACTION
  {
    static
    {
      dispatch = new ACTION("dispatch", 1);
      token = new ACTION("token", 2);
      ACTION[] arrayOfACTION = new ACTION[3];
      arrayOfACTION[0] = subscribe;
      arrayOfACTION[1] = dispatch;
      arrayOfACTION[2] = token;
      $VALUES = arrayOfACTION;
    }

    public static ACTION fromString(String paramString)
    {
      try
      {
        ACTION localACTION = valueOf(paramString);
        return localACTION;
      }
      catch (Exception localException)
      {
      }
      return null;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.Push
 * JD-Core Version:    0.6.0
 */