package com.worklight.wlclient.api;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION;
import com.google.android.gcm.GCMRegistrar;
import com.worklight.androidgap.push.GCMErrorCode;
import com.worklight.androidgap.push.WLGCMIntentService.Message;
import com.worklight.common.WLConfig;
import com.worklight.common.WLUtils;
import com.worklight.wlclient.WLRequest;
import com.worklight.wlclient.WLRequestListener;
import com.worklight.wlclient.push.GCMIntentService;
import com.worklight.wlclient.ui.UIActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WLPush
{
  private static final String ERROR_ID = "WLClient.error";
  private static final String FROM_NOTIFICATION_BAR = "notificationBar";
  private static final String NOT_UPDATE_FAILURE = "WLClient.notificationUpdateFailure";
  private static final String OK = "WLClient.ok";
  private static final String RESOURCE_BUNDLE = "com/worklight/wlclient/messages";
  private Activity activity;
  private WLConfig config;
  private Context context;
  private String deviceToken;
  private HttpContext httpContext;
  private boolean isTokenUpdatedOnServer;
  private BroadcastReceiver onMessage;
  private WLOnReadyToSubscribeListener onReadyToSubscribeListener;
  private BroadcastReceiver onRegister;
  private BroadcastReceiver onRegisterError;
  private ArrayList<WLGCMIntentService.Message> pending;
  private ArrayList<JSONObject> pendingPushEvents;
  private HashMap<String, RegisteredEventSource> registeredEventSources;
  private String serverToken;
  private ArrayList<String> subscribedEventSources;

  // ERROR //
  WLPush(HttpContext paramHttpContext, WLConfig paramWLConfig, Context paramContext)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 55	java/lang/Object:<init>	()V
    //   4: aload_0
    //   5: iconst_0
    //   6: putfield 57	com/worklight/wlclient/api/WLPush:isTokenUpdatedOnServer	Z
    //   9: aload_0
    //   10: new 59	java/util/ArrayList
    //   13: dup
    //   14: invokespecial 60	java/util/ArrayList:<init>	()V
    //   17: putfield 62	com/worklight/wlclient/api/WLPush:subscribedEventSources	Ljava/util/ArrayList;
    //   20: aload_0
    //   21: new 59	java/util/ArrayList
    //   24: dup
    //   25: invokespecial 60	java/util/ArrayList:<init>	()V
    //   28: putfield 64	com/worklight/wlclient/api/WLPush:pendingPushEvents	Ljava/util/ArrayList;
    //   31: aload_0
    //   32: new 59	java/util/ArrayList
    //   35: dup
    //   36: invokespecial 60	java/util/ArrayList:<init>	()V
    //   39: putfield 66	com/worklight/wlclient/api/WLPush:pending	Ljava/util/ArrayList;
    //   42: aload_0
    //   43: aconst_null
    //   44: putfield 68	com/worklight/wlclient/api/WLPush:serverToken	Ljava/lang/String;
    //   47: aload_0
    //   48: aconst_null
    //   49: putfield 70	com/worklight/wlclient/api/WLPush:deviceToken	Ljava/lang/String;
    //   52: aload_0
    //   53: aconst_null
    //   54: putfield 72	com/worklight/wlclient/api/WLPush:onReadyToSubscribeListener	Lcom/worklight/wlclient/api/WLOnReadyToSubscribeListener;
    //   57: aload_0
    //   58: new 74	com/worklight/wlclient/api/WLPush$3
    //   61: dup
    //   62: aload_0
    //   63: invokespecial 77	com/worklight/wlclient/api/WLPush$3:<init>	(Lcom/worklight/wlclient/api/WLPush;)V
    //   66: putfield 79	com/worklight/wlclient/api/WLPush:onRegister	Landroid/content/BroadcastReceiver;
    //   69: aload_0
    //   70: new 81	com/worklight/wlclient/api/WLPush$4
    //   73: dup
    //   74: aload_0
    //   75: invokespecial 82	com/worklight/wlclient/api/WLPush$4:<init>	(Lcom/worklight/wlclient/api/WLPush;)V
    //   78: putfield 84	com/worklight/wlclient/api/WLPush:onRegisterError	Landroid/content/BroadcastReceiver;
    //   81: aload_0
    //   82: new 86	com/worklight/wlclient/api/WLPush$5
    //   85: dup
    //   86: aload_0
    //   87: invokespecial 87	com/worklight/wlclient/api/WLPush$5:<init>	(Lcom/worklight/wlclient/api/WLPush;)V
    //   90: putfield 89	com/worklight/wlclient/api/WLPush:onMessage	Landroid/content/BroadcastReceiver;
    //   93: aload_3
    //   94: ifnonnull +13 -> 107
    //   97: new 91	java/lang/RuntimeException
    //   100: dup
    //   101: ldc 93
    //   103: invokespecial 96	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   106: athrow
    //   107: getstatic 102	android/os/Build$VERSION:SDK_INT	I
    //   110: bipush 8
    //   112: if_icmpge +46 -> 158
    //   115: new 104	java/lang/StringBuilder
    //   118: dup
    //   119: invokespecial 105	java/lang/StringBuilder:<init>	()V
    //   122: ldc 107
    //   124: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   127: getstatic 114	android/os/Build$VERSION:RELEASE	Ljava/lang/String;
    //   130: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   133: ldc 116
    //   135: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   138: invokevirtual 120	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   141: astore 10
    //   143: aload 10
    //   145: invokestatic 125	com/worklight/common/WLUtils:error	(Ljava/lang/String;)V
    //   148: new 91	java/lang/RuntimeException
    //   151: dup
    //   152: aload 10
    //   154: invokespecial 96	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   157: athrow
    //   158: aload_0
    //   159: aload_3
    //   160: checkcast 127	android/app/Activity
    //   163: putfield 129	com/worklight/wlclient/api/WLPush:activity	Landroid/app/Activity;
    //   166: aload_0
    //   167: getfield 129	com/worklight/wlclient/api/WLPush:activity	Landroid/app/Activity;
    //   170: invokestatic 135	com/google/android/gcm/GCMRegistrar:checkDevice	(Landroid/content/Context;)V
    //   173: aload_0
    //   174: getfield 129	com/worklight/wlclient/api/WLPush:activity	Landroid/app/Activity;
    //   177: invokestatic 138	com/google/android/gcm/GCMRegistrar:checkManifest	(Landroid/content/Context;)V
    //   180: aload_3
    //   181: ldc 140
    //   183: ldc 142
    //   185: invokestatic 146	com/worklight/common/WLUtils:getResourceId	(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;)I
    //   188: pop
    //   189: aload_0
    //   190: aload_1
    //   191: putfield 148	com/worklight/wlclient/api/WLPush:httpContext	Lorg/apache/http/protocol/HttpContext;
    //   194: aload_0
    //   195: aload_2
    //   196: putfield 150	com/worklight/wlclient/api/WLPush:config	Lcom/worklight/common/WLConfig;
    //   199: aload_0
    //   200: aload_3
    //   201: putfield 152	com/worklight/wlclient/api/WLPush:context	Landroid/content/Context;
    //   204: aload_0
    //   205: invokevirtual 155	com/worklight/wlclient/api/WLPush:unregisterReceivers	()V
    //   208: aload_0
    //   209: getfield 129	com/worklight/wlclient/api/WLPush:activity	Landroid/app/Activity;
    //   212: aload_0
    //   213: getfield 89	com/worklight/wlclient/api/WLPush:onMessage	Landroid/content/BroadcastReceiver;
    //   216: new 157	android/content/IntentFilter
    //   219: dup
    //   220: new 104	java/lang/StringBuilder
    //   223: dup
    //   224: invokespecial 105	java/lang/StringBuilder:<init>	()V
    //   227: aload_0
    //   228: getfield 129	com/worklight/wlclient/api/WLPush:activity	Landroid/app/Activity;
    //   231: invokestatic 161	com/worklight/common/WLUtils:getFullAppName	(Landroid/content/Context;)Ljava/lang/String;
    //   234: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   237: ldc 163
    //   239: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   242: invokevirtual 120	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   245: invokespecial 164	android/content/IntentFilter:<init>	(Ljava/lang/String;)V
    //   248: invokevirtual 168	android/app/Activity:registerReceiver	(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;)Landroid/content/Intent;
    //   251: pop
    //   252: new 104	java/lang/StringBuilder
    //   255: dup
    //   256: invokespecial 105	java/lang/StringBuilder:<init>	()V
    //   259: aload_0
    //   260: getfield 129	com/worklight/wlclient/api/WLPush:activity	Landroid/app/Activity;
    //   263: invokestatic 161	com/worklight/common/WLUtils:getFullAppName	(Landroid/content/Context;)Ljava/lang/String;
    //   266: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   269: ldc 170
    //   271: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   274: invokevirtual 120	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   277: aload_0
    //   278: getfield 129	com/worklight/wlclient/api/WLPush:activity	Landroid/app/Activity;
    //   281: invokevirtual 174	android/app/Activity:getIntent	()Landroid/content/Intent;
    //   284: invokevirtual 179	android/content/Intent:getAction	()Ljava/lang/String;
    //   287: invokevirtual 185	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   290: ifeq +40 -> 330
    //   293: ldc 187
    //   295: invokestatic 190	com/worklight/common/WLUtils:debug	(Ljava/lang/String;)V
    //   298: aload_0
    //   299: getfield 129	com/worklight/wlclient/api/WLPush:activity	Landroid/app/Activity;
    //   302: invokevirtual 174	android/app/Activity:getIntent	()Landroid/content/Intent;
    //   305: ldc 11
    //   307: iconst_1
    //   308: invokevirtual 194	android/content/Intent:putExtra	(Ljava/lang/String;Z)Landroid/content/Intent;
    //   311: pop
    //   312: aload_0
    //   313: getfield 89	com/worklight/wlclient/api/WLPush:onMessage	Landroid/content/BroadcastReceiver;
    //   316: aload_0
    //   317: getfield 129	com/worklight/wlclient/api/WLPush:activity	Landroid/app/Activity;
    //   320: aload_0
    //   321: getfield 129	com/worklight/wlclient/api/WLPush:activity	Landroid/app/Activity;
    //   324: invokevirtual 174	android/app/Activity:getIntent	()Landroid/content/Intent;
    //   327: invokevirtual 200	android/content/BroadcastReceiver:onReceive	(Landroid/content/Context;Landroid/content/Intent;)V
    //   330: aload_0
    //   331: invokespecial 203	com/worklight/wlclient/api/WLPush:cancelAllNotification	()V
    //   334: aload_0
    //   335: invokespecial 206	com/worklight/wlclient/api/WLPush:dispatchPending	()V
    //   338: return
    //   339: astore 4
    //   341: new 91	java/lang/RuntimeException
    //   344: dup
    //   345: new 104	java/lang/StringBuilder
    //   348: dup
    //   349: invokespecial 105	java/lang/StringBuilder:<init>	()V
    //   352: ldc 208
    //   354: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   357: aload 4
    //   359: invokevirtual 211	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   362: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   365: invokevirtual 120	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   368: invokespecial 96	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   371: athrow
    //   372: astore 5
    //   374: new 91	java/lang/RuntimeException
    //   377: dup
    //   378: new 104	java/lang/StringBuilder
    //   381: dup
    //   382: invokespecial 105	java/lang/StringBuilder:<init>	()V
    //   385: ldc 213
    //   387: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   390: aload 5
    //   392: invokevirtual 211	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   395: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   398: invokevirtual 120	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   401: invokespecial 96	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   404: athrow
    //   405: astore 6
    //   407: new 91	java/lang/RuntimeException
    //   410: dup
    //   411: ldc 215
    //   413: invokespecial 96	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   416: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   166	173	339	java/lang/Exception
    //   173	180	372	java/lang/Exception
    //   180	189	405	java/lang/Exception
  }

  private void cancelAllNotification()
  {
    ((NotificationManager)this.activity.getSystemService("notification")).cancelAll();
  }

  private void dispatch()
  {
    dispatchPending();
  }

  private void dispatchPending()
  {
    while (true)
    {
      WLGCMIntentService.Message localMessage;
      synchronized (this.pending)
      {
        int i = this.pending.size();
        localMessage = null;
        if (i <= 0)
          continue;
        localMessage = (WLGCMIntentService.Message)this.pending.remove(0);
        if (localMessage == null)
          return;
      }
      onMessage(localMessage.getProps(), localMessage.getPayload());
    }
  }

  private void dispatchPendings()
  {
    Iterator localIterator = this.pendingPushEvents.iterator();
    while (localIterator.hasNext())
    {
      JSONObject localJSONObject = (JSONObject)localIterator.next();
      try
      {
        String str = localJSONObject.getString("alias");
        if ((!this.subscribedEventSources.contains(str)) || (!this.registeredEventSources.containsKey(str)) || (((RegisteredEventSource)this.registeredEventSources.get(str)).eventSourceListener == null))
          continue;
        ((RegisteredEventSource)this.registeredEventSources.get(str)).eventSourceListener.onReceive(localJSONObject.getString("props"), localJSONObject.getString("payload"));
        localIterator.remove();
      }
      catch (JSONException localJSONException)
      {
        WLUtils.error("Failed processing pending push events." + localJSONException.getMessage());
      }
    }
  }

  private boolean hasPendings()
  {
    return (this.pendingPushEvents != null) && (this.pendingPushEvents.size() > 0);
  }

  private boolean isAbleToSubscribe(String paramString, boolean paramBoolean)
  {
    if (!isDeviceSupportPush())
    {
      WLUtils.error("The current Android version " + Build.VERSION.RELEASE + " does not support push notifications.");
      return false;
    }
    if (!this.isTokenUpdatedOnServer)
    {
      WLUtils.error("Can't subscribe, notification token is not updated on the server");
      return false;
    }
    if ((!paramBoolean) && ((this.registeredEventSources == null) || (this.registeredEventSources.get(paramString) == null)))
    {
      WLUtils.error("No registered push event source for alias '" + paramString + "'.");
      return false;
    }
    return true;
  }

  private boolean isDeviceSupportPush()
  {
    boolean bool = Double.valueOf(Build.VERSION.RELEASE.substring(0, 3)).doubleValue() < 2.2D;
    int i = 0;
    if (!bool)
      i = 1;
    return i;
  }

  private void onMessage(JSONObject paramJSONObject1, JSONObject paramJSONObject2)
  {
    try
    {
      String str = (String)paramJSONObject2.get("alias");
      WLUtils.debug("WLPush received notification for alias: " + str);
      if ((this.subscribedEventSources.contains(str)) && (this.registeredEventSources.containsKey(str)) && (((RegisteredEventSource)this.registeredEventSources.get(str)).eventSourceListener != null))
      {
        ((RegisteredEventSource)this.registeredEventSources.get(str)).eventSourceListener.onReceive(paramJSONObject1.toString(), paramJSONObject2.toString());
        return;
      }
      this.pendingPushEvents.add(new JSONObject("{\"alias\":" + str + ", \"props\":" + paramJSONObject1.toString() + ", \"payload\":" + paramJSONObject2.toString() + "}"));
      return;
    }
    catch (Exception localException)
    {
      WLUtils.error("Failed processing pending push events, because " + localException.getMessage());
    }
  }

  private void showErrorDialogue(String paramString1, String paramString2, String paramString3)
  {
    Intent localIntent = new Intent(this.context, UIActivity.class);
    localIntent.putExtra("action", "exit");
    localIntent.putExtra("dialogue_message", paramString2);
    localIntent.putExtra("dialogue_title", paramString1);
    localIntent.putExtra("positive_button_text", paramString3);
    this.context.startActivity(localIntent);
  }

  private void updateRegisteredEventSources(String paramString1, String paramString2, String paramString3, WLEventSourceListener paramWLEventSourceListener)
  {
    if (this.registeredEventSources == null)
      this.registeredEventSources = new HashMap();
    RegisteredEventSource localRegisteredEventSource = (RegisteredEventSource)this.registeredEventSources.get(paramString1);
    if (localRegisteredEventSource == null)
      localRegisteredEventSource = new RegisteredEventSource();
    localRegisteredEventSource.setAdapter(paramString2);
    localRegisteredEventSource.setEventSource(paramString3);
    localRegisteredEventSource.setEventSourceListener(paramWLEventSourceListener);
    this.registeredEventSources.put(paramString1, localRegisteredEventSource);
  }

  private void updateTokenCallback(String paramString)
  {
    if ((this.serverToken == null) || (!this.serverToken.equals(paramString)))
    {
      WLUtils.debug("Push notification device token has changed, Updating on server [serverToken: " + this.serverToken + ", deviceToken: " + paramString + "]");
      WLRequestOptions localWLRequestOptions = new WLRequestOptions();
      localWLRequestOptions.addParameter("updateToken", paramString);
      new WLRequest(new UpdateTokenListener(), this.httpContext, localWLRequestOptions, this.config, this.context).makeRequest("notifications");
    }
    while (true)
    {
      this.serverToken = null;
      return;
      this.isTokenUpdatedOnServer = true;
      if (this.onReadyToSubscribeListener != null)
        this.onReadyToSubscribeListener.onReadyToSubscribe();
      if (!hasPendings())
        continue;
      dispatchPendings();
    }
  }

  public void clearSubscribedEventSources()
  {
    WLUtils.debug("Clearing notification subscriptions.");
    this.subscribedEventSources.clear();
  }

  WLOnReadyToSubscribeListener getOnReadyToSubscribeListener()
  {
    return this.onReadyToSubscribeListener;
  }

  public boolean isForeground()
  {
    return GCMIntentService.isAppForeground();
  }

  public boolean isPushSupported()
  {
    return isDeviceSupportPush();
  }

  public boolean isSubscribed(String paramString)
  {
    return (this.subscribedEventSources != null) && (this.subscribedEventSources.contains(paramString));
  }

  public void registerEventSourceCallback(String paramString1, String paramString2, String paramString3, WLEventSourceListener paramWLEventSourceListener)
  {
    if ((WLUtils.isStringEmpty(paramString1)) || (WLUtils.isStringEmpty(paramString2)) || (WLUtils.isStringEmpty(paramString3)))
      WLUtils.error("Cannot register to event source callback with alias '" + paramString1 + "', adapter '" + paramString2 + "' and eventSource '" + paramString3 + "'. Use concrete values which are not null or empty.");
    do
    {
      return;
      if ((this.registeredEventSources == null) || (this.registeredEventSources.get(paramString1) == null))
        continue;
      WLUtils.error("Cannot register to event source callback with alias '" + paramString1 + "', because it is already registered.");
      return;
    }
    while (!isAbleToSubscribe(paramString1, true));
    updateRegisteredEventSources(paramString1, paramString2, paramString3, paramWLEventSourceListener);
  }

  public void setForeground(boolean paramBoolean)
  {
    GCMIntentService.setAppForeground(paramBoolean);
  }

  public void setOnReadyToSubscribeListener(WLOnReadyToSubscribeListener paramWLOnReadyToSubscribeListener)
  {
    this.onReadyToSubscribeListener = paramWLOnReadyToSubscribeListener;
  }

  public void subscribe(String paramString, WLPushOptions paramWLPushOptions, WLResponseListener paramWLResponseListener)
  {
    if (WLUtils.isStringEmpty(paramString))
      WLUtils.error("Cannot subscribe to event source with alias '" + paramString + "', because it is either null or empty.");
    do
      return;
    while (!isAbleToSubscribe(paramString, false));
    RegisteredEventSource localRegisteredEventSource = (RegisteredEventSource)this.registeredEventSources.get(paramString);
    if (localRegisteredEventSource == null)
    {
      WLUtils.error("Event source with alias '" + paramString + "' is not registered. Register before subscribing to event source.");
      return;
    }
    if (paramWLPushOptions == null)
      paramWLPushOptions = new WLPushOptions();
    WLRequestOptions localWLRequestOptions = new WLRequestOptions();
    if (paramWLResponseListener == null)
      paramWLResponseListener = new WLResponseListener()
      {
        public void onFailure(WLFailResponse paramWLFailResponse)
        {
          WLUtils.error("WLPush.subscribe: error subscribing for notifications");
        }

        public void onSuccess(WLResponse paramWLResponse)
        {
        }
      };
    localWLRequestOptions.setResponseListener(paramWLResponseListener);
    SubscribeRequestListener localSubscribeRequestListener = new SubscribeRequestListener(paramString);
    StringBuffer localStringBuffer = new StringBuffer();
    Map localMap = paramWLPushOptions.getSubscriptionParameters();
    localStringBuffer.append("{");
    if ((localMap != null) && (!localMap.isEmpty()))
    {
      Iterator localIterator = localMap.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        localStringBuffer.append("\"");
        localStringBuffer.append(str);
        localStringBuffer.append("\":\"");
        localStringBuffer.append((String)localMap.get(str));
        localStringBuffer.append("\"");
        if (!localIterator.hasNext())
          continue;
        localStringBuffer.append(",");
      }
    }
    localStringBuffer.append("}");
    localWLRequestOptions.addParameter("subscribe", localStringBuffer.toString());
    localWLRequestOptions.addParameter("alias", paramString);
    localWLRequestOptions.addParameter("adapter", localRegisteredEventSource.adapter);
    localWLRequestOptions.addParameter("eventSource", localRegisteredEventSource.eventSource);
    new WLRequest(localSubscribeRequestListener, this.httpContext, localWLRequestOptions, this.config, this.context).makeRequest("notifications");
    dispatch();
  }

  public void unregisterReceivers()
  {
    if (this.activity != null);
    try
    {
      this.activity.unregisterReceiver(this.onRegister);
    }
    catch (Exception localException2)
    {
      try
      {
        this.activity.unregisterReceiver(this.onRegisterError);
      }
      catch (Exception localException2)
      {
        try
        {
          while (true)
          {
            this.activity.unregisterReceiver(this.onMessage);
            if (GCMRegistrar.isRegistered(this.context))
              GCMRegistrar.onDestroy(this.context);
            return;
            localException1 = localException1;
            WLUtils.warning("unregisterReceivers:" + localException1.getMessage());
            continue;
            localException2 = localException2;
            WLUtils.warning("unregisterReceivers:" + localException2.getMessage());
          }
        }
        catch (Exception localException3)
        {
          while (true)
            WLUtils.warning("unregisterReceivers:" + localException3.getMessage());
        }
      }
    }
  }

  public void unsubscribe(String paramString, WLResponseListener paramWLResponseListener)
  {
    if (WLUtils.isStringEmpty(paramString))
      WLUtils.error("Cannot unsubscribe from event source with alias '" + paramString + "'.Please check if the input alias is valid.");
    do
      return;
    while (!isAbleToSubscribe(paramString, false));
    if (paramWLResponseListener == null)
      paramWLResponseListener = new WLResponseListener()
      {
        public void onFailure(WLFailResponse paramWLFailResponse)
        {
          WLUtils.error("WLPush.unsubscribe: error unsubscribing from notifications");
        }

        public void onSuccess(WLResponse paramWLResponse)
        {
        }
      };
    RegisteredEventSource localRegisteredEventSource = (RegisteredEventSource)this.registeredEventSources.get(paramString);
    WLRequestOptions localWLRequestOptions = new WLRequestOptions();
    localWLRequestOptions.setResponseListener(paramWLResponseListener);
    UnSubscribeRequestListener localUnSubscribeRequestListener = new UnSubscribeRequestListener(paramString);
    localWLRequestOptions.addParameter("unsubscribe", "{}");
    localWLRequestOptions.addParameter("alias", paramString);
    localWLRequestOptions.addParameter("adapter", localRegisteredEventSource.adapter);
    localWLRequestOptions.addParameter("eventSource", localRegisteredEventSource.eventSource);
    new WLRequest(localUnSubscribeRequestListener, this.httpContext, localWLRequestOptions, this.config, this.context).makeRequest("notifications");
  }

  public void updateSubscribedEventSources(JSONArray paramJSONArray)
  {
    WLUtils.debug("Updating notification subscriptions.");
    int i = 0;
    try
    {
      int j = paramJSONArray.length();
      while (i < j)
      {
        String str = (String)((JSONObject)paramJSONArray.get(i)).get("alias");
        if (!this.subscribedEventSources.contains(str))
          this.subscribedEventSources.add(str);
        i++;
      }
    }
    catch (JSONException localJSONException)
    {
      WLUtils.error("Updating notification subscriptions failed, because " + localJSONException.getMessage());
    }
  }

  public void updateToken(String paramString)
  {
    this.serverToken = paramString;
    try
    {
      this.activity.unregisterReceiver(this.onRegister);
      try
      {
        label16: this.activity.unregisterReceiver(this.onRegisterError);
        label27: this.activity.registerReceiver(this.onRegister, new IntentFilter(WLUtils.getFullAppName(this.activity) + ".C2DM_REGISTERED"));
        this.activity.registerReceiver(this.onRegisterError, new IntentFilter(WLUtils.getFullAppName(this.activity) + ".C2DM_ERROR"));
        WLUtils.debug("Registering at the GCM server.");
        Activity localActivity = this.activity;
        String[] arrayOfString = new String[1];
        arrayOfString[0] = this.config.getGCMSender();
        GCMRegistrar.register(localActivity, arrayOfString);
        return;
      }
      catch (Exception localException2)
      {
        break label27;
      }
    }
    catch (Exception localException1)
    {
      break label16;
    }
  }

  class RegisteredEventSource
  {
    private String adapter;
    private String eventSource;
    private WLEventSourceListener eventSourceListener;

    RegisteredEventSource()
    {
    }

    public String getAdapter()
    {
      return this.adapter;
    }

    public String getEventSource()
    {
      return this.eventSource;
    }

    public WLEventSourceListener getEventSourceListener()
    {
      return this.eventSourceListener;
    }

    public void setAdapter(String paramString)
    {
      this.adapter = paramString;
    }

    public void setEventSource(String paramString)
    {
      this.eventSource = paramString;
    }

    public void setEventSourceListener(WLEventSourceListener paramWLEventSourceListener)
    {
      this.eventSourceListener = paramWLEventSourceListener;
    }
  }

  class SubscribeRequestListener
    implements WLRequestListener
  {
    private String alias;

    public SubscribeRequestListener(String arg2)
    {
      Object localObject;
      this.alias = localObject;
    }

    public void onFailure(WLFailResponse paramWLFailResponse)
    {
      paramWLFailResponse.getOptions().getResponseListener().onFailure(paramWLFailResponse);
    }

    public void onSuccess(WLResponse paramWLResponse)
    {
      if (!WLPush.this.subscribedEventSources.contains(this.alias))
        WLPush.this.subscribedEventSources.add(this.alias);
      paramWLResponse.getOptions().getResponseListener().onSuccess(paramWLResponse);
      if (WLPush.this.hasPendings())
        WLPush.this.dispatchPendings();
    }
  }

  class UnSubscribeRequestListener
    implements WLRequestListener
  {
    private String alias;

    public UnSubscribeRequestListener(String arg2)
    {
      Object localObject;
      this.alias = localObject;
    }

    public void onFailure(WLFailResponse paramWLFailResponse)
    {
      paramWLFailResponse.getOptions().getResponseListener().onFailure(paramWLFailResponse);
    }

    public void onSuccess(WLResponse paramWLResponse)
    {
      if (WLPush.this.subscribedEventSources.contains(this.alias))
        WLPush.this.subscribedEventSources.remove(this.alias);
      paramWLResponse.getOptions().getResponseListener().onSuccess(paramWLResponse);
    }
  }

  class UpdateTokenListener
    implements WLRequestListener
  {
    public UpdateTokenListener()
    {
    }

    public void onFailure(WLFailResponse paramWLFailResponse)
    {
      WLPush.access$1402(WLPush.this, false);
      WLUtils.error("Failed to update token on server");
    }

    public void onSuccess(WLResponse paramWLResponse)
    {
      WLPush.access$1402(WLPush.this, true);
      if (WLPush.this.onReadyToSubscribeListener != null)
        WLPush.this.onReadyToSubscribeListener.onReadyToSubscribe();
      while (true)
      {
        if (WLPush.this.hasPendings())
          WLPush.this.dispatchPendings();
        return;
        WLUtils.debug("onReadyToSubscribeListener is NULL");
      }
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.api.WLPush
 * JD-Core Version:    0.6.0
 */