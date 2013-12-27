package com.worklight.wlclient.challengehandler;

import android.content.Context;
import android.content.Intent;
import com.worklight.common.WLUtils;
import com.worklight.utils.SecurityUtils;
import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.challengehandler.WLChallengeHandler;
import com.worklight.wlclient.ui.UIActivity;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthenticityChallengeHandler extends WLChallengeHandler
{
  private static final String AUTH_FAIL_ID = "WLClient.authFailure";
  private static final String CLOSE_ID = "WLClient.close";
  private static final String INIT_FAILURE_TITLE_ID = "WLClient.wlclientInitFailure";
  private static final String RESOURCE_BUNDLE = "com/worklight/wlclient/messages";

  public AuthenticityChallengeHandler(String paramString)
  {
    super(paramString);
  }

  public void handleChallenge(JSONObject paramJSONObject)
  {
    try
    {
      String str1 = paramJSONObject.getString("WL-Challenge-Data");
      int i = str1.indexOf("+");
      String str2 = str1.substring(i + 1);
      String str3 = str1.substring(0, i);
      JSONArray localJSONArray1 = new JSONArray(Arrays.asList(str2.split("-")));
      JSONArray localJSONArray2 = new JSONArray();
      localJSONArray2.put(0, str3);
      localJSONArray2.put(1, localJSONArray1);
      submitChallengeAnswer(SecurityUtils.hashDataFromJSON(WLClient.getInstance().getContext(), localJSONArray2));
      return;
    }
    catch (JSONException localJSONException)
    {
      WLUtils.debug(localJSONException.getMessage());
      return;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      WLUtils.debug(localUnsupportedEncodingException.getMessage());
    }
  }

  public void handleFailure(JSONObject paramJSONObject)
  {
    ResourceBundle localResourceBundle = ResourceBundle.getBundle("com/worklight/wlclient/messages", Locale.getDefault());
    Context localContext = WLClient.getInstance().getContext();
    Intent localIntent = new Intent(localContext, UIActivity.class);
    localIntent.putExtra("action", "exit");
    localIntent.putExtra("dialogue_message", localResourceBundle.getString("WLClient.authFailure"));
    localIntent.putExtra("dialogue_title", localResourceBundle.getString("WLClient.wlclientInitFailure"));
    localIntent.putExtra("positive_button_text", localResourceBundle.getString("WLClient.close"));
    localContext.startActivity(localIntent);
  }

  public void handleSuccess(JSONObject paramJSONObject)
  {
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.challengehandler.AuthenticityChallengeHandler
 * JD-Core Version:    0.6.0
 */