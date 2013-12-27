package com.worklight.androidgap.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gcm.GCMBaseIntentService;
import com.worklight.androidgap.WLDroidGap;
import com.worklight.common.WLUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class WLGCMIntentService extends GCMBaseIntentService
{
  public static final String GCM_ERROR = ".C2DM_ERROR";
  public static final String GCM_EXTRA_ALERT = "alert";
  public static final String GCM_EXTRA_BADGE = "badge";
  public static final String GCM_EXTRA_ERROR_ID = "errorId";
  public static final String GCM_EXTRA_MESSAGE = "message";
  public static final String GCM_EXTRA_PAYLOAD = "payload";
  public static final String GCM_EXTRA_REGISTRATION_ID = "registrationId";
  public static final String GCM_EXTRA_SOUND = "sound";
  public static final String GCM_MESSAGE = ".C2DM_MESSAGE";
  public static final String GCM_NOTIFICATION = ".NOTIFICATION";
  public static final String GCM_REGISTERED = ".C2DM_REGISTERED";
  public static final String GCM_UNREGISTERED = ".C2DM_UNREGISTERED";
  private static int RES_PUSH_NOTIFICATION_ICON = -1;
  private static int RES_PUSH_NOTIFICATION_TITLE = -1;
  private BroadcastReceiver resultReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ((getResultCode() == 1) || (!WLDroidGap.isForeGround()))
        WLGCMIntentService.this.onUnhandled(paramContext, paramIntent);
    }
  };

  public WLGCMIntentService()
  {
  }

  public WLGCMIntentService(String paramString)
  {
    super(new String[] { paramString });
  }

  private void generateNotification(Context paramContext, String paramString1, String paramString2, String paramString3, int paramInt, String paramString4, Intent paramIntent)
  {
    Notification localNotification = new Notification(RES_PUSH_NOTIFICATION_ICON, paramString1, System.currentTimeMillis());
    localNotification.setLatestEventInfo(paramContext, paramString2, paramString3, PendingIntent.getActivity(paramContext, 0, paramIntent, 134217728));
    localNotification.number = paramInt;
    localNotification.flags = (0x10 | localNotification.flags);
    ((NotificationManager)paramContext.getSystemService("notification")).notify(1, localNotification);
    if ((paramString4 != null) && (!"".equals(paramString4.trim())))
      playNotificationSound(paramContext, paramString4);
  }

  private void playNotificationSound(Context paramContext, String paramString)
  {
    String str = paramString;
    try
    {
      if (str.contains("."))
        str = str.substring(0, str.indexOf("."));
      Uri localUri2 = Uri.parse("android.resource://" + getPackageName() + "/" + WLUtils.getResourceId(paramContext, "raw", str));
      localUri1 = localUri2;
      if (localUri1 == null)
        localUri1 = RingtoneManager.getDefaultUri(2);
      if (localUri1 != null)
      {
        Ringtone localRingtone = RingtoneManager.getRingtone(paramContext, localUri1);
        if (localRingtone != null)
        {
          localRingtone.setStreamType(5);
          localRingtone.play();
        }
      }
      return;
    }
    catch (Exception localException)
    {
      while (true)
      {
        WLUtils.error("Push notification sound will not be used because sound file name \"" + str + "\" was not found. Add \"" + str + "\" to native/res/raw folder.");
        Uri localUri1 = null;
      }
    }
  }

  protected String getNotificationTitle(Context paramContext)
  {
    return paramContext.getString(RES_PUSH_NOTIFICATION_TITLE);
  }

  public void notify(Context paramContext, String paramString)
  {
    generateNotification(paramContext, paramString, getNotificationTitle(paramContext), paramString, 0, "1", null);
  }

  public void notify(Context paramContext, String paramString1, int paramInt, String paramString2, Intent paramIntent)
  {
    generateNotification(paramContext, paramString1, getNotificationTitle(paramContext), paramString1, paramInt, paramString2, paramIntent);
  }

  public void onError(Context paramContext, String paramString)
  {
    WLUtils.warning("Push notification will not work, because register/unregister to GCM service returned error " + paramString + ".");
    Intent localIntent = new Intent(WLUtils.getFullAppName(paramContext) + ".C2DM_ERROR");
    localIntent.putExtra("errorId", paramString);
    paramContext.sendBroadcast(localIntent);
  }

  protected void onMessage(Context paramContext, Intent paramIntent)
  {
    WLUtils.debug("Received a message from the GCM server");
    Message localMessage = new Message();
    Bundle localBundle = paramIntent.getExtras();
    Message.access$002(localMessage, localBundle.getString("alert"));
    try
    {
      Message.access$102(localMessage, Integer.parseInt(localBundle.getString("badge"), 10));
      Message.access$202(localMessage, localBundle.getString("sound"));
      Message.access$302(localMessage, localBundle.getString("payload"));
      Intent localIntent = new Intent(WLUtils.getFullAppName(paramContext) + ".C2DM_MESSAGE");
      localIntent.putExtra("message", localMessage);
      paramContext.sendOrderedBroadcast(localIntent, null, this.resultReceiver, null, 1, null, null);
      return;
    }
    catch (Exception localException)
    {
      while (true)
        WLUtils.warning("Unable to update badge while received push notification, becasue failed to parse badge number " + localBundle.getString("badge") + ", badge must be an integer number.");
    }
  }

  protected boolean onRecoverableError(Context paramContext, String paramString)
  {
    WLUtils.warning("Push notification will not work, because register/unregister to GCM service returned error " + paramString + ".");
    return super.onRecoverableError(paramContext, paramString);
  }

  public void onRegistered(Context paramContext, String paramString)
  {
    setResources();
    WLUtils.debug("Registered at the GCM server with registration id " + paramString);
    Intent localIntent = new Intent(WLUtils.getFullAppName(paramContext) + ".C2DM_REGISTERED");
    localIntent.putExtra("registrationId", paramString);
    paramContext.sendBroadcast(localIntent);
  }

  protected void onUnhandled(Context paramContext, Intent paramIntent)
  {
    setResources();
    String str = paramIntent.getAction();
    if ((WLUtils.getFullAppName(paramContext) + ".C2DM_MESSAGE").equals(str))
    {
      Message localMessage = (Message)paramIntent.getParcelableExtra("message");
      WLUtils.debug("Showing notification for unhandled " + localMessage.toString());
      Intent localIntent = new Intent(WLUtils.getFullAppName(paramContext) + ".NOTIFICATION");
      localIntent.putExtra("message", localMessage);
      notify(paramContext, localMessage.alert, localMessage.badge, localMessage.sound, localIntent);
    }
  }

  protected void onUnregistered(Context paramContext, String paramString)
  {
    WLUtils.debug("Unregistered at the GCM server");
    paramContext.sendBroadcast(new Intent(WLUtils.getFullAppName(paramContext) + ".C2DM_UNREGISTERED"));
  }

  protected void setBroadcastReceiver(BroadcastReceiver paramBroadcastReceiver)
  {
    this.resultReceiver = paramBroadcastReceiver;
  }

  protected void setNotificationIcon(int paramInt)
  {
    RES_PUSH_NOTIFICATION_ICON = paramInt;
  }

  protected void setResources()
  {
    if ((RES_PUSH_NOTIFICATION_ICON == -1) || (RES_PUSH_NOTIFICATION_TITLE == -1));
    try
    {
      RES_PUSH_NOTIFICATION_ICON = WLUtils.getResourceId(getApplicationContext(), "drawable", "push");
      RES_PUSH_NOTIFICATION_TITLE = WLUtils.getResourceId(getApplicationContext(), "string", "push_notification_title");
      return;
    }
    catch (Exception localException)
    {
      WLUtils.error("Push notification icon or title may not be displayed properly, because resource was not found. Add icon to native/res/drawable, or add <string =\"push_notification_title\">title</string> in native/res/values/strings.xml" + localException.getMessage(), localException);
    }
  }

  public static class Message
    implements Parcelable
  {
    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator()
    {
      public WLGCMIntentService.Message createFromParcel(Parcel paramParcel)
      {
        return new WLGCMIntentService.Message(paramParcel, null);
      }

      public WLGCMIntentService.Message[] newArray(int paramInt)
      {
        return new WLGCMIntentService.Message[paramInt];
      }
    };
    private String alert;
    private int badge;
    private String payload;
    private String sound;

    public Message()
    {
      this.alert = null;
      this.badge = 1;
      this.sound = null;
      this.payload = null;
    }

    private Message(Parcel paramParcel)
    {
      this.alert = paramParcel.readString();
      this.badge = paramParcel.readInt();
      this.sound = paramParcel.readString();
      this.payload = paramParcel.readString();
    }

    public int describeContents()
    {
      return 0;
    }

    public JSONObject getPayload()
    {
      try
      {
        JSONObject localJSONObject = new JSONObject(this.payload);
        return localJSONObject;
      }
      catch (JSONException localJSONException)
      {
      }
      return new JSONObject();
    }

    public JSONObject getProps()
    {
      JSONObject localJSONObject = new JSONObject();
      try
      {
        Object localObject1;
        if (this.alert == null)
        {
          localObject1 = JSONObject.NULL;
          localJSONObject.put("alert", localObject1);
          localJSONObject.put("badge", this.badge);
          if (this.sound != null)
            break label69;
        }
        label69: for (Object localObject2 = JSONObject.NULL; ; localObject2 = this.sound)
        {
          localJSONObject.put("sound", localObject2);
          return localJSONObject;
          localObject1 = this.alert;
          break;
        }
      }
      catch (JSONException localJSONException)
      {
      }
      throw new RuntimeException(localJSONException.getMessage(), localJSONException);
    }

    public String toString()
    {
      Object[] arrayOfObject = new Object[4];
      arrayOfObject[0] = this.alert;
      arrayOfObject[1] = Integer.valueOf(this.badge);
      arrayOfObject[2] = this.sound;
      arrayOfObject[3] = this.payload;
      return String.format("Message(alert=%s, badge=%d, sound=%s, payload=%s)", arrayOfObject);
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(this.alert);
      paramParcel.writeInt(this.badge);
      paramParcel.writeString(this.sound);
      paramParcel.writeString(this.payload);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.push.WLGCMIntentService
 * JD-Core Version:    0.6.0
 */