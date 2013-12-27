package com.worklight.wlclient.challengehandler;

import android.content.Context;
import android.content.Intent;
import com.worklight.common.WLUtils;
import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.challengehandler.WLChallengeHandler;
import com.worklight.wlclient.ui.UIActivity;
import java.util.Locale;
import java.util.ResourceBundle;
import org.json.JSONException;
import org.json.JSONObject;

public class RemoteDisableChallengeHandler extends WLChallengeHandler
{
  private static final String APPLICATION_DISABLED_ID = "WLClient.applicationDenied";
  private static final String CLOSE_ID = "WLClient.close";
  private static final String GET_NEW_VERSION_ID = "WLClient.getNewVersion";
  private static final String NOTIFICATION_TITLE_ID = "WLClient.notificationTitle";
  private static final String NOTIFY_MESAGE = "NOTIFY";
  private static final String PROTOCOL_DOWNLOAD_LINK = "downloadLink";
  private static final String PROTOCOL_ERROR_MESSAGE = "Protocol Error - could not parse JSON object";
  private static final String PROTOCOL_MESSAGE = "message";
  private static final String PROTOCOL_MESSAGE_ID = "messageId";
  private static final String PROTOCOL_MESSAGE_TYPE = "messageType";
  private static final String RESOURCE_BUNDLE = "com/worklight/wlclient/messages";

  public RemoteDisableChallengeHandler(String paramString)
  {
    super(paramString);
  }

  private void createAndShowDisableDialogue(String paramString1, String paramString2)
  {
    ResourceBundle localResourceBundle = ResourceBundle.getBundle("com/worklight/wlclient/messages", Locale.getDefault());
    Context localContext = WLClient.getInstance().getContext();
    Intent localIntent = new Intent(localContext, UIActivity.class);
    localIntent.putExtra("action", "wl_remoteDisableRealm");
    localIntent.putExtra("dialogue_message", paramString1);
    localIntent.putExtra("dialogue_title", localResourceBundle.getString("WLClient.applicationDenied"));
    localIntent.putExtra("positive_button_text", localResourceBundle.getString("WLClient.close"));
    if ((paramString2 != null) && (paramString2.length() != 0) && (!paramString2.equalsIgnoreCase("null")))
    {
      localIntent.putExtra("download_link", paramString2);
      localIntent.putExtra("neutral_button_text", localResourceBundle.getString("WLClient.getNewVersion"));
    }
    localContext.startActivity(localIntent);
  }

  private void createAndShowMessageDialogue(String paramString1, String paramString2)
  {
    ResourceBundle localResourceBundle = ResourceBundle.getBundle("com/worklight/wlclient/messages", Locale.getDefault());
    Context localContext = WLClient.getInstance().getContext();
    Intent localIntent = new Intent(localContext, UIActivity.class);
    localIntent.putExtra("action", "notify");
    localIntent.putExtra("dialogue_message", paramString1);
    localIntent.putExtra("dialogue_title", localResourceBundle.getString("WLClient.notificationTitle"));
    localIntent.putExtra("positive_button_text", localResourceBundle.getString("WLClient.close"));
    localContext.startActivity(localIntent);
  }

  private boolean isDisplayMessageDialogue(String paramString1, String paramString2, String paramString3)
  {
    if ((paramString3 == null) || (!paramString3.equalsIgnoreCase("NOTIFY")));
    do
      return true;
    while ((paramString1 == null) || (!paramString1.equalsIgnoreCase(paramString2)));
    return false;
  }

  public void handleChallenge(JSONObject paramJSONObject)
  {
    try
    {
      String str1 = paramJSONObject.getString("message");
      String str2 = paramJSONObject.getString("messageId");
      String str3 = paramJSONObject.getString("messageType");
      if (isDisplayMessageDialogue(WLUtils.readWLPref(WLClient.getInstance().getContext(), "messageId"), str2, str3))
      {
        createAndShowMessageDialogue(str1, str2);
        WLUtils.writeWLPref(WLClient.getInstance().getContext(), "messageId", str2);
        return;
      }
      submitChallengeAnswer(str2);
      return;
    }
    catch (JSONException localJSONException)
    {
      WLUtils.error("Protocol Error - could not parse JSON object", localJSONException);
    }
    throw new RuntimeException("Protocol Error - could not parse JSON object");
  }

  public void handleFailure(JSONObject paramJSONObject)
  {
    try
    {
      createAndShowDisableDialogue(paramJSONObject.getString("message"), paramJSONObject.getString("downloadLink"));
      return;
    }
    catch (JSONException localJSONException)
    {
      WLUtils.error("Protocol Error - could not parse JSON object", localJSONException);
    }
    throw new RuntimeException("Protocol Error - could not parse JSON object");
  }

  public void handleSuccess(JSONObject paramJSONObject)
  {
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.challengehandler.RemoteDisableChallengeHandler
 * JD-Core Version:    0.6.0
 */