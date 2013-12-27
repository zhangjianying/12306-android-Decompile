package com.worklight.wlclient;

import android.content.Context;
import android.content.Intent;
import com.worklight.common.WLConfig;
import com.worklight.common.WLUtils;
import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLErrorCode;
import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLRequestOptions;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.challengehandler.ChallengeHandler;
import com.worklight.wlclient.api.challengehandler.WLChallengeHandler;
import com.worklight.wlclient.ui.UIActivity;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import org.apache.http.Header;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WLRequest
{
  private static final String ACCESS_DENIED_ID = "WLClient.accessDenied";
  private static final String AUTH_FAIL_ID = "WLClient.authFailure";
  private static final String CLOSE_BUTTON_ID = "WLClient.close";
  private static final String ERROR_ID = "WLClient.error";
  private static final String RESOURCE_BUNDLE = "com/worklight/wlclient/messages";
  private WLConfig config;
  private Context context;
  private HttpContext httpContext;
  private HttpPost postRequest;
  private WLRequestListener requestListener;
  private WLRequestOptions requestOptions;
  private String requestURL = null;
  private HashMap<String, Object> wlAnswers = new HashMap();

  public WLRequest(WLRequestListener paramWLRequestListener, HttpContext paramHttpContext, WLRequestOptions paramWLRequestOptions, WLConfig paramWLConfig, Context paramContext)
  {
    this.requestListener = paramWLRequestListener;
    this.httpContext = paramHttpContext;
    this.requestOptions = paramWLRequestOptions;
    this.config = paramWLConfig;
    this.context = paramContext;
  }

  private void addExpectedAnswers(HttpPost paramHttpPost)
  {
    if (this.wlAnswers.isEmpty())
      return;
    JSONObject localJSONObject = new JSONObject();
    Iterator localIterator = this.wlAnswers.entrySet().iterator();
    while (true)
      if (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        String str = (String)localEntry.getKey();
        Object localObject = localEntry.getValue();
        if (localObject == null)
          break;
        try
        {
          localJSONObject.accumulate(str, localObject);
        }
        catch (JSONException localJSONException)
        {
          WLUtils.error(localJSONException.getMessage(), localJSONException);
          throw new RuntimeException(localJSONException);
        }
      }
    paramHttpPost.addHeader("Authorization", localJSONObject.toString());
    this.wlAnswers.clear();
  }

  private void addExtraHeaders(HttpPost paramHttpPost)
  {
    ArrayList localArrayList = this.requestOptions.getHeaders();
    if (localArrayList == null);
    while (true)
    {
      return;
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
        paramHttpPost.addHeader((Header)localIterator.next());
    }
  }

  private void addHeaders(WLConfig paramWLConfig, HttpPost paramHttpPost)
  {
    paramHttpPost.addHeader("X-Requested-With", "XMLHttpRequest");
    paramHttpPost.addHeader("x-wl-app-version", paramWLConfig.getApplicationVersion());
  }

  private void addParams(WLRequestOptions paramWLRequestOptions, HttpPost paramHttpPost)
  {
    ArrayList localArrayList = new ArrayList();
    if ((paramWLRequestOptions.getParameters() != null) && (!paramWLRequestOptions.getParameters().isEmpty()))
    {
      Iterator localIterator = paramWLRequestOptions.getParameters().keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        localArrayList.add(new BasicNameValuePair(str, (String)paramWLRequestOptions.getParameters().get(str)));
      }
    }
    localArrayList.add(new BasicNameValuePair("isAjaxRequest", "true"));
    localArrayList.add(new BasicNameValuePair("x", String.valueOf(Math.random())));
    try
    {
      UrlEncodedFormEntity localUrlEncodedFormEntity = new UrlEncodedFormEntity(localArrayList, "UTF-8");
      paramHttpPost.setEntity(localUrlEncodedFormEntity);
      return;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      WLUtils.error(localUnsupportedEncodingException.getMessage(), localUnsupportedEncodingException);
    }
    throw new RuntimeException(localUnsupportedEncodingException);
  }

  private boolean checkResponseForChallenges(WLResponse paramWLResponse)
  {
    ResourceBundle localResourceBundle = ResourceBundle.getBundle("com/worklight/wlclient/messages", Locale.getDefault());
    if (isWl401(paramWLResponse))
    {
      JSONObject localJSONObject4 = paramWLResponse.getResponseJSON();
      ArrayList localArrayList = new ArrayList();
      while (true)
      {
        JSONObject localJSONObject6;
        WLChallengeHandler localWLChallengeHandler2;
        try
        {
          JSONObject localJSONObject5 = localJSONObject4.getJSONObject("challenges");
          JSONArray localJSONArray2 = localJSONObject5.names();
          int j = 0;
          if (j >= localJSONArray2.length())
            continue;
          localArrayList.add(localJSONArray2.getString(j));
          j++;
          continue;
          setExpectedAnswers(localArrayList);
          Iterator localIterator = localArrayList.iterator();
          if (localIterator.hasNext())
          {
            String str3 = (String)localIterator.next();
            localJSONObject6 = localJSONObject5.getJSONObject(str3);
            localWLChallengeHandler2 = WLClient.getInstance().getWLChallengeHandler(str3);
            if (localWLChallengeHandler2 != null)
              break label231;
            WLUtils.error("Application will exit, because unexpected challenge handler arrived while using realm " + str3 + ". Register the challenge handler using registerChallengeHandler().");
            showErrorDialogue(localResourceBundle.getString("WLClient.error"), localResourceBundle.getString("WLClient.authFailure"), localResourceBundle.getString("WLClient.close"));
            continue;
          }
        }
        catch (JSONException localJSONException2)
        {
          WLUtils.debug("Wrong JSON arrived when processing a challenge in a 401 response. With " + localJSONException2.getMessage(), localJSONException2);
        }
        return true;
        label231: localWLChallengeHandler2.startHandleChallenge(this, localJSONObject6);
      }
    }
    JSONObject localJSONObject1;
    if (isWl403(paramWLResponse))
      localJSONObject1 = paramWLResponse.getResponseJSON();
    while (true)
    {
      int i;
      try
      {
        JSONObject localJSONObject2 = localJSONObject1.getJSONObject("WL-Authentication-Failure");
        JSONArray localJSONArray1 = localJSONObject2.names();
        i = 0;
        if (i < localJSONArray1.length())
        {
          String str1 = localJSONArray1.getString(i);
          JSONObject localJSONObject3 = localJSONObject2.getJSONObject(str1);
          WLChallengeHandler localWLChallengeHandler1 = WLClient.getInstance().getWLChallengeHandler(str1);
          if (localWLChallengeHandler1 == null)
            continue;
          localWLChallengeHandler1.handleFailure(localJSONObject3);
          localWLChallengeHandler1.clearWaitingList();
          break label476;
          StringBuilder localStringBuilder = new StringBuilder(localResourceBundle.getString("WLClient.accessDenied"));
          String str2 = localJSONObject3.getString("reason");
          if (str2 == null)
            continue;
          localStringBuilder.append("\nReason: " + str2);
          WLUtils.error("The application will exit, because connect to Worklight server failed due to missing challenge handler to handle " + str1);
          showErrorDialogue(localResourceBundle.getString("WLClient.error"), localStringBuilder.toString(), localResourceBundle.getString("WLClient.close"));
        }
      }
      catch (JSONException localJSONException1)
      {
        WLUtils.debug("Wrong JSON arrived when processing a challenge in a 403 response. with " + localJSONException1.getMessage(), localJSONException1);
      }
      return true;
      return handleCustomChallenges(paramWLResponse);
      label476: i++;
    }
  }

  private void checkResponseForSuccesses(WLResponse paramWLResponse)
  {
    JSONObject localJSONObject1 = paramWLResponse.getResponseJSON();
    if (localJSONObject1 == null);
    while (true)
    {
      return;
      try
      {
        if (!localJSONObject1.has("WL-Authentication-Success"))
          continue;
        JSONObject localJSONObject2 = localJSONObject1.getJSONObject("WL-Authentication-Success");
        if (localJSONObject2 == null)
          continue;
        JSONArray localJSONArray = localJSONObject2.names();
        for (int i = 0; i < localJSONObject2.length(); i++)
        {
          String str = localJSONArray.getString(i);
          JSONObject localJSONObject3 = localJSONObject2.getJSONObject(str);
          WLChallengeHandler localWLChallengeHandler = WLClient.getInstance().getWLChallengeHandler(str);
          if (localWLChallengeHandler == null)
            continue;
          localWLChallengeHandler.handleSuccess(localJSONObject3);
          localWLChallengeHandler.releaseWaitingList();
        }
      }
      catch (Exception localException)
      {
        triggerUnexpectedOnFailure(localException);
      }
    }
  }

  private boolean handleCustomChallenges(WLResponse paramWLResponse)
  {
    ChallengeHandler localChallengeHandler = WLClient.getInstance().getChallengeHandler(paramWLResponse);
    int i = 0;
    if (localChallengeHandler != null)
    {
      localChallengeHandler.startHandleChallenge(this, paramWLResponse);
      i = 1;
    }
    return i;
  }

  private boolean isWl401(WLResponse paramWLResponse)
  {
    if (paramWLResponse.getStatus() == 401)
    {
      Header localHeader = paramWLResponse.getHeader("WWW-Authenticate");
      if ((localHeader != null) && (localHeader.getValue().equalsIgnoreCase("WL-Composite-Challenge")))
        return true;
    }
    return false;
  }

  private boolean isWl403(WLResponse paramWLResponse)
  {
    try
    {
      int i = paramWLResponse.getStatus();
      int j = 0;
      if (i == 403)
      {
        JSONObject localJSONObject = paramWLResponse.getResponseJSON();
        j = 0;
        if (localJSONObject != null)
        {
          Object localObject = paramWLResponse.getResponseJSON().get("WL-Authentication-Failure");
          j = 0;
          if (localObject != null)
            j = 1;
        }
      }
      return j;
    }
    catch (JSONException localJSONException)
    {
      WLUtils.error(localJSONException.getMessage(), localJSONException);
    }
    return false;
  }

  private void processFailureResponse(WLResponse paramWLResponse)
  {
    try
    {
      WLCookieManager.handleResponseHeaders(paramWLResponse.getHeaders(), new URI(this.requestURL));
      WLFailResponse localWLFailResponse = new WLFailResponse(paramWLResponse);
      localWLFailResponse.setOptions(this.requestOptions);
      this.requestListener.onFailure(localWLFailResponse);
      return;
    }
    catch (URISyntaxException localURISyntaxException)
    {
      triggerUnexpectedOnFailure(localURISyntaxException);
    }
  }

  private void processSuccessResponse(WLResponse paramWLResponse)
  {
    try
    {
      WLCookieManager.handleResponseHeaders(paramWLResponse.getHeaders(), new URI(this.requestURL));
      this.requestListener.onSuccess(paramWLResponse);
      return;
    }
    catch (URISyntaxException localURISyntaxException)
    {
      triggerUnexpectedOnFailure(localURISyntaxException);
    }
  }

  private void resendIfNeeded()
  {
    int i = 1;
    if (this.wlAnswers == null)
      i = 1;
    while (true)
    {
      if (i != 0)
        resendRequest();
      return;
      Iterator localIterator = this.wlAnswers.values().iterator();
      if (!localIterator.hasNext())
        continue;
      if (localIterator.next() != null)
        break;
      i = 0;
    }
  }

  private void resendRequest()
  {
    if (this.requestURL != null)
    {
      sendRequest(this.requestURL);
      return;
    }
    WLUtils.debug("resendRequest failed: requestURL is null.");
  }

  private void sendRequest(String paramString)
  {
    this.postRequest = new HttpPost(paramString);
    addHeaders(this.config, this.postRequest);
    addExtraHeaders(this.postRequest);
    addParams(this.requestOptions, this.postRequest);
    WLClient.getInstance().addGlobalHeadersToRequest(this.postRequest);
    addExpectedAnswers(this.postRequest);
    try
    {
      AsynchronousRequestSender.getInstance().sendRequestAsync(this);
      return;
    }
    catch (Exception localException)
    {
      WLUtils.error(localException.getMessage(), localException);
      triggerUnexpectedOnFailure(localException);
    }
  }

  private void setExpectedAnswers(List<String> paramList)
  {
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      this.wlAnswers.put(str, null);
    }
  }

  private void showErrorDialogue(String paramString1, String paramString2, String paramString3)
  {
    Context localContext = WLClient.getInstance().getContext();
    Intent localIntent = new Intent(localContext, UIActivity.class);
    localIntent.putExtra("action", "exit");
    localIntent.putExtra("dialogue_message", paramString2);
    localIntent.putExtra("dialogue_title", paramString1);
    localIntent.putExtra("positive_button_text", paramString3);
    localContext.startActivity(localIntent);
  }

  private void triggerUnexpectedOnFailure(Exception paramException)
  {
    getRequestListener().onFailure(new WLFailResponse(WLErrorCode.UNEXPECTED_ERROR, paramException.getMessage(), getOptions()));
  }

  public WLConfig getConfig()
  {
    return this.config;
  }

  public Context getContext()
  {
    return this.context;
  }

  public HttpContext getHttpContext()
  {
    return this.httpContext;
  }

  public WLRequestOptions getOptions()
  {
    return this.requestOptions;
  }

  public HttpPost getPostRequest()
  {
    return this.postRequest;
  }

  public WLRequestListener getRequestListener()
  {
    return this.requestListener;
  }

  public void makeRequest(String paramString)
  {
    makeRequest(paramString, false);
  }

  public void makeRequest(String paramString, boolean paramBoolean)
  {
    this.requestURL = null;
    if (!paramBoolean);
    for (this.requestURL = (this.config.getAppURL().toExternalForm() + paramString); ; this.requestURL = (this.config.getRootURL() + "/" + paramString))
    {
      sendRequest(this.requestURL);
      return;
    }
  }

  public void removeExpectedAnswer(String paramString)
  {
    this.wlAnswers.remove(paramString);
    resendIfNeeded();
  }

  // ERROR //
  public void requestFinished(WLResponse paramWLResponse)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial 542	com/worklight/wlclient/WLRequest:checkResponseForSuccesses	(Lcom/worklight/wlclient/api/WLResponse;)V
    //   5: aload_0
    //   6: aload_1
    //   7: invokespecial 544	com/worklight/wlclient/WLRequest:checkResponseForChallenges	(Lcom/worklight/wlclient/api/WLResponse;)Z
    //   10: istore 4
    //   12: iload 4
    //   14: ifne +127 -> 141
    //   17: aload_1
    //   18: invokevirtual 364	com/worklight/wlclient/api/WLResponse:getStatus	()I
    //   21: sipush 200
    //   24: if_icmpne +148 -> 172
    //   27: aload_1
    //   28: invokevirtual 239	com/worklight/wlclient/api/WLResponse:getResponseJSON	()Lorg/json/JSONObject;
    //   31: astore 6
    //   33: aload 6
    //   35: ifnull +101 -> 136
    //   38: aload 6
    //   40: ldc_w 546
    //   43: invokevirtual 380	org/json/JSONObject:get	(Ljava/lang/String;)Ljava/lang/Object;
    //   46: astore 7
    //   48: aload 7
    //   50: ifnull +86 -> 136
    //   53: aconst_null
    //   54: astore 8
    //   56: aload 7
    //   58: checkcast 68	org/json/JSONObject
    //   61: astore 9
    //   63: ldc_w 548
    //   66: invokestatic 554	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   69: pop
    //   70: invokestatic 270	com/worklight/wlclient/api/WLClient:getInstance	()Lcom/worklight/wlclient/api/WLClient;
    //   73: invokevirtual 558	com/worklight/wlclient/api/WLClient:getPush	()Lcom/worklight/wlclient/api/WLPush;
    //   76: astore 12
    //   78: aload 9
    //   80: ldc_w 560
    //   83: invokevirtual 380	org/json/JSONObject:get	(Ljava/lang/String;)Ljava/lang/Object;
    //   86: checkcast 95	java/lang/String
    //   89: astore 8
    //   91: aload 9
    //   93: ldc_w 562
    //   96: invokevirtual 380	org/json/JSONObject:get	(Ljava/lang/String;)Ljava/lang/Object;
    //   99: checkcast 251	org/json/JSONArray
    //   102: astore 14
    //   104: aload 12
    //   106: invokevirtual 567	com/worklight/wlclient/api/WLPush:clearSubscribedEventSources	()V
    //   109: aload 14
    //   111: ifnull +18 -> 129
    //   114: aload 14
    //   116: invokevirtual 255	org/json/JSONArray:length	()I
    //   119: ifle +10 -> 129
    //   122: aload 12
    //   124: aload 14
    //   126: invokevirtual 571	com/worklight/wlclient/api/WLPush:updateSubscribedEventSources	(Lorg/json/JSONArray;)V
    //   129: aload 12
    //   131: aload 8
    //   133: invokevirtual 574	com/worklight/wlclient/api/WLPush:updateToken	(Ljava/lang/String;)V
    //   136: aload_0
    //   137: aload_1
    //   138: invokespecial 576	com/worklight/wlclient/WLRequest:processSuccessResponse	(Lcom/worklight/wlclient/api/WLResponse;)V
    //   141: return
    //   142: astore_2
    //   143: aload_0
    //   144: aload_2
    //   145: invokespecial 354	com/worklight/wlclient/WLRequest:triggerUnexpectedOnFailure	(Ljava/lang/Exception;)V
    //   148: return
    //   149: astore_3
    //   150: aload_0
    //   151: aload_3
    //   152: invokespecial 354	com/worklight/wlclient/WLRequest:triggerUnexpectedOnFailure	(Ljava/lang/Exception;)V
    //   155: return
    //   156: astore 10
    //   158: ldc_w 578
    //   161: invokestatic 289	com/worklight/common/WLUtils:error	(Ljava/lang/String;)V
    //   164: goto -28 -> 136
    //   167: astore 5
    //   169: goto -33 -> 136
    //   172: aload_0
    //   173: aload_1
    //   174: invokespecial 580	com/worklight/wlclient/WLRequest:processFailureResponse	(Lcom/worklight/wlclient/api/WLResponse;)V
    //   177: return
    //   178: astore 13
    //   180: aconst_null
    //   181: astore 14
    //   183: goto -79 -> 104
    //
    // Exception table:
    //   from	to	target	type
    //   0	5	142	java/lang/Exception
    //   5	12	149	java/lang/Exception
    //   63	78	156	java/lang/ClassNotFoundException
    //   78	104	156	java/lang/ClassNotFoundException
    //   104	109	156	java/lang/ClassNotFoundException
    //   114	129	156	java/lang/ClassNotFoundException
    //   129	136	156	java/lang/ClassNotFoundException
    //   27	33	167	org/json/JSONException
    //   38	48	167	org/json/JSONException
    //   56	63	167	org/json/JSONException
    //   63	78	167	org/json/JSONException
    //   104	109	167	org/json/JSONException
    //   114	129	167	org/json/JSONException
    //   129	136	167	org/json/JSONException
    //   158	164	167	org/json/JSONException
    //   78	104	178	org/json/JSONException
  }

  public void submitAnswer(String paramString, Object paramObject)
  {
    this.wlAnswers.put(paramString, paramObject);
    resendIfNeeded();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.WLRequest
 * JD-Core Version:    0.6.0
 */