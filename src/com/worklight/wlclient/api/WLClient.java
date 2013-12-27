package com.worklight.wlclient.api;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.worklight.common.WLConfig;
import com.worklight.common.WLUtils;
import com.worklight.wlclient.AsynchronousRequestSender;
import com.worklight.wlclient.WLCookieManager;
import com.worklight.wlclient.WLRequest;
import com.worklight.wlclient.WLRequestListener;
import com.worklight.wlclient.api.challengehandler.BaseChallengeHandler;
import com.worklight.wlclient.api.challengehandler.ChallengeHandler;
import com.worklight.wlclient.api.challengehandler.WLChallengeHandler;
import com.worklight.wlclient.challengehandler.AntiXSRFChallengeHandler;
import com.worklight.wlclient.challengehandler.AuthenticityChallengeHandler;
import com.worklight.wlclient.challengehandler.NoProvisioningChallengeHandler;
import com.worklight.wlclient.challengehandler.RemoteDisableChallengeHandler;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

public class WLClient
{
  private static final String ANTI_XSRF_REALM = "wl_antiXSRFRealm";
  private static final String AUTHENTICATE_REQUEST_PATH = "authenticate";
  private static final String AUTHENTICITY_REALM = "wl_authenticityRealm";
  private static final String CHALLENGE_HANDLER_NULL_ERROR = "Cannot register 'null' challenge handler";
  private static final String HEART_BEAT_ERROR = "WLClient not initialized - cannot send heart beat message before connect";
  private static final String HEART_BEAT_REQUEST_PATH = "heartbeat";
  private static final String INIT_REQUEST_PATH = "init";
  private static final String INVOKE_PROCEDURE_INIT_ERROR = "invokeProcedure() will not be executed because WLCLient is not initialized, ensure WLCLient.connect function has been called.";
  private static final String INVOKE_PROCEDURE_REQUEST_PATH = "query";
  private static final String INVOKE_PROCEDURE_RUN_ERROR = "Error during invocation of remote procedure, because responseListener parameter can't be null.";
  private static final String LOG_ACTIVITY_INIT_ERROR = "logActivity() will not be executed because WLCLient is not initialized, ensure WLCLient.connect function has been called.";
  private static final String LOG_ACTIVITY_REQUEST_PATH = "logactivity";
  private static final String NO_PROVISIONING_REALM = "wl_deviceNoProvisioningRealm";
  private static final String NO_REALM_REGISTER_ERROR = "Application will exit because the challengeHandler parameter for registerChallengeHandler (challengeHandler) has a null realm property. Call this API with a valid reference to challenge handler.";
  private static final String REMOTE_DISABLE_REALM = "wl_remoteDisableRealm";
  private static final String REQ_PATH_DELETE_USER_PREF = "deleteup";
  private static final String REQ_PATH_SET_USER_PREFS = "setup";
  private static final String SEND_INVOKE_PROCEDURE_REQUEST_PATH = "invoke";
  private static WLClient wlClient = null;
  private Hashtable<String, BaseChallengeHandler> chMap;
  private WLConfig config;
  private Context context;
  private Hashtable<String, String> globalHeaders;
  private int heartbeatInterval = 420;
  private HttpContext httpContext;
  private boolean isInitialized;
  private Timer timer;
  private HashMap<String, String> userPreferenceMap;
  private WLPush wlPush = null;

  private WLClient(Context paramContext)
  {
    this.config = new WLConfig(paramContext);
    this.httpContext = new BasicHttpContext();
    this.context = paramContext;
    this.chMap = new Hashtable();
    this.globalHeaders = new Hashtable();
    this.userPreferenceMap = new HashMap();
    registerDefaultChallengeHandlers();
  }

  public static WLClient createInstance(Context paramContext)
  {
    if (wlClient != null)
    {
      WLUtils.debug("WLClient has already been created.");
      releaseInstance();
    }
    wlClient = new WLClient(paramContext);
    CookieSyncManager.createInstance(paramContext);
    return wlClient;
  }

  public static WLClient getInstance()
  {
    if (wlClient == null)
      throw new RuntimeException("WLClient object has not been created. You must call WLClient.createInstance first.");
    return wlClient;
  }

  private String getWLServerURL()
  {
    String str1 = getConfig().getContext();
    String str2 = getConfig().getAppURL().getHost();
    if ((str1 != null) && (str1.trim().length() > 1))
      return str2 + str1;
    return str2;
  }

  public static boolean isApplicationSentToBackground(Context paramContext)
  {
    List localList = ((ActivityManager)paramContext.getSystemService("activity")).getRunningTasks(1);
    if ((!localList.isEmpty()) && (!((ActivityManager.RunningTaskInfo)localList.get(0)).topActivity.getPackageName().equals(paramContext.getPackageName())))
    {
      WLUtils.error("Application in background = true ");
      return true;
    }
    WLUtils.error("Application in background = false");
    return false;
  }

  private void registerDefaultChallengeHandlers()
  {
    registerChallengeHandler(new AntiXSRFChallengeHandler("wl_antiXSRFRealm"));
    registerChallengeHandler(new NoProvisioningChallengeHandler("wl_deviceNoProvisioningRealm"));
    registerChallengeHandler(new RemoteDisableChallengeHandler("wl_remoteDisableRealm"));
    registerChallengeHandler(new AuthenticityChallengeHandler("wl_authenticityRealm"));
  }

  private static void releaseInstance()
  {
    AsynchronousRequestSender.releaseHttpClient();
    wlClient = null;
  }

  private boolean updateCookiesFromWebView()
  {
    String str1 = getWLServerURL();
    String str2 = CookieManager.getInstance().getCookie(str1);
    boolean bool = WLUtils.isStringEmpty(str2);
    int i = 0;
    if (!bool)
    {
      WLCookieManager.setCookies(str2, str1);
      Set localSet = WLCookieManager.getCookies();
      if (localSet != null)
      {
        BasicCookieStore localBasicCookieStore = new BasicCookieStore();
        Iterator localIterator = localSet.iterator();
        while (localIterator.hasNext())
          localBasicCookieStore.addCookie((Cookie)localIterator.next());
        this.httpContext.setAttribute("http.cookie-store", localBasicCookieStore);
        i = 1;
      }
    }
    else
    {
      return i;
    }
    WLUtils.debug("No Cookies found for url " + this.config.getAppURL().getHost() + " in WebView.");
    return false;
  }

  public HttpContext GetHttpContext()
  {
    return this.httpContext;
  }

  public void addGlobalHeader(String paramString1, String paramString2)
  {
    this.globalHeaders.put(paramString1, paramString2);
  }

  public void addGlobalHeadersToRequest(HttpPost paramHttpPost)
  {
    Iterator localIterator = this.globalHeaders.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      paramHttpPost.addHeader((String)localEntry.getKey(), (String)localEntry.getValue());
    }
  }

  public void checkForNotifications()
  {
    if (!this.isInitialized)
      return;
    new WLRequest(new WLRequestListener()
    {
      public void onFailure(WLFailResponse paramWLFailResponse)
      {
      }

      public void onSuccess(WLResponse paramWLResponse)
      {
      }
    }
    , this.httpContext, new WLRequestOptions(), this.config, this.context).makeRequest("authenticate");
  }

  public void connect(WLResponseListener paramWLResponseListener)
  {
    WLCookieManager.clearCookies();
    if (updateCookiesFromWebView())
    {
      String str = WLUtils.readWLPref(getContext(), "WL-Instance-Id");
      if (!WLUtils.isStringEmpty(str))
        getInstance().addGlobalHeader("WL-Instance-Id", str);
    }
    WLRequestOptions localWLRequestOptions = new WLRequestOptions();
    localWLRequestOptions.addParameter("action", "test");
    localWLRequestOptions.setResponseListener(paramWLResponseListener);
    new WLRequest(new InitRequestListener(), this.httpContext, localWLRequestOptions, this.config, this.context).makeRequest("init");
  }

  public ChallengeHandler getChallengeHandler(WLResponse paramWLResponse)
  {
    Iterator localIterator = this.chMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      BaseChallengeHandler localBaseChallengeHandler = (BaseChallengeHandler)((Map.Entry)localIterator.next()).getValue();
      if (!(localBaseChallengeHandler instanceof ChallengeHandler))
        continue;
      ChallengeHandler localChallengeHandler = (ChallengeHandler)localBaseChallengeHandler;
      if (localChallengeHandler.isCustomResponse(paramWLResponse))
        return localChallengeHandler;
    }
    return null;
  }

  public WLConfig getConfig()
  {
    return this.config;
  }

  public Context getContext()
  {
    return this.context;
  }

  public WLPush getPush()
  {
    if (this.wlPush == null)
      this.wlPush = new WLPush(this.httpContext, this.config, this.context);
    return this.wlPush;
  }

  public WLChallengeHandler getWLChallengeHandler(String paramString)
  {
    if (paramString == null)
      return null;
    BaseChallengeHandler localBaseChallengeHandler = (BaseChallengeHandler)this.chMap.get(paramString);
    if ((localBaseChallengeHandler != null) && ((localBaseChallengeHandler instanceof WLChallengeHandler)))
      return (WLChallengeHandler)localBaseChallengeHandler;
    return null;
  }

  public void init(WLResponseListener paramWLResponseListener)
  {
    connect(paramWLResponseListener);
  }

  public void invokeProcedure(WLProcedureInvocationData paramWLProcedureInvocationData, WLResponseListener paramWLResponseListener)
  {
    invokeProcedure(paramWLProcedureInvocationData, paramWLResponseListener, null);
  }

  public void invokeProcedure(WLProcedureInvocationData paramWLProcedureInvocationData, WLResponseListener paramWLResponseListener, WLRequestOptions paramWLRequestOptions)
  {
    if (!this.isInitialized)
    {
      WLUtils.error("invokeProcedure() will not be executed because WLCLient is not initialized, ensure WLCLient.connect function has been called.");
      return;
    }
    if (paramWLResponseListener == null)
      throw new IllegalArgumentException("Error during invocation of remote procedure, because responseListener parameter can't be null.");
    if (paramWLRequestOptions == null)
      paramWLRequestOptions = new WLRequestOptions();
    paramWLRequestOptions.setResponseListener(paramWLResponseListener);
    paramWLRequestOptions.addParameters(paramWLProcedureInvocationData.getInvocationDataMap());
    InvokeProcedureRequestListener localInvokeProcedureRequestListener = new InvokeProcedureRequestListener();
    HttpContext localHttpContext = this.httpContext;
    WLConfig localWLConfig = this.config;
    Context localContext = this.context;
    new WLRequest(localInvokeProcedureRequestListener, localHttpContext, paramWLRequestOptions, localWLConfig, localContext).makeRequest("query");
  }

  public void logActivity(String paramString)
  {
    if (!this.isInitialized)
    {
      WLUtils.error("logActivity() will not be executed because WLCLient is not initialized, ensure WLCLient.connect function has been called.");
      return;
    }
    if (paramString == null)
      throw new RuntimeException("ActivityType cannot be null");
    WLRequestOptions localWLRequestOptions = new WLRequestOptions();
    localWLRequestOptions.addParameter("activity", paramString);
    new WLRequest(new LogActivityListener(), this.httpContext, localWLRequestOptions, this.config, this.context).makeRequest("logactivity");
  }

  public void registerChallengeHandler(BaseChallengeHandler paramBaseChallengeHandler)
  {
    if (paramBaseChallengeHandler == null)
    {
      WLUtils.error("Cannot register 'null' challenge handler");
      throw new RuntimeException("Cannot register 'null' challenge handler");
    }
    String str = paramBaseChallengeHandler.getRealm();
    if (str == null)
    {
      WLUtils.error("Application will exit because the challengeHandler parameter for registerChallengeHandler (challengeHandler) has a null realm property. Call this API with a valid reference to challenge handler.");
      throw new RuntimeException("Application will exit because the challengeHandler parameter for registerChallengeHandler (challengeHandler) has a null realm property. Call this API with a valid reference to challenge handler.");
    }
    this.chMap.put(str, paramBaseChallengeHandler);
  }

  public void removeGlobalHeader(String paramString)
  {
    this.globalHeaders.remove(paramString);
  }

  void sendHeartBeat()
  {
    if ((this.timer == null) && (this.heartbeatInterval > 0))
    {
      this.timer = new Timer();
      this.timer.scheduleAtFixedRate(new HeartBeatTask(this.context), 1000 * this.heartbeatInterval, 1000 * this.heartbeatInterval);
    }
  }

  public void sendInvoke(WLProcedureInvocationData paramWLProcedureInvocationData, WLResponseListener paramWLResponseListener, WLRequestOptions paramWLRequestOptions)
  {
    if (paramWLResponseListener == null)
      throw new IllegalArgumentException("Error during invocation of remote procedure, because responseListener parameter can't be null.");
    if (paramWLRequestOptions == null)
      paramWLRequestOptions = new WLRequestOptions();
    paramWLRequestOptions.setResponseListener(paramWLResponseListener);
    paramWLRequestOptions.addParameters(paramWLProcedureInvocationData.getInvocationDataMap());
    InvokeProcedureRequestListener localInvokeProcedureRequestListener = new InvokeProcedureRequestListener();
    HttpContext localHttpContext = this.httpContext;
    WLConfig localWLConfig = this.config;
    Context localContext = this.context;
    new WLRequest(localInvokeProcedureRequestListener, localHttpContext, paramWLRequestOptions, localWLConfig, localContext).makeRequest("invoke", true);
  }

  public void setHeartBeatInterval(int paramInt)
  {
    this.heartbeatInterval = paramInt;
    if (this.timer != null)
    {
      this.timer.cancel();
      this.timer = null;
    }
    sendHeartBeat();
  }

  protected void setInitialized(boolean paramBoolean)
  {
    this.isInitialized = paramBoolean;
  }

  class HeartBeatTask extends TimerTask
  {
    private Context context = null;

    HeartBeatTask(Context arg2)
    {
      Object localObject;
      this.context = localObject;
    }

    public void run()
    {
      if (!WLClient.this.isInitialized)
        throw new RuntimeException("WLClient not initialized - cannot send heart beat message before connect");
      if (WLClient.isApplicationSentToBackground(this.context));
      WLRequestOptions localWLRequestOptions;
      WLClient.HeartbeatListener localHeartbeatListener;
      WLClient localWLClient;
      do
      {
        return;
        localWLRequestOptions = new WLRequestOptions();
        localHeartbeatListener = new WLClient.HeartbeatListener(WLClient.this);
        localWLClient = WLClient.getInstance();
      }
      while (localWLClient == null);
      new WLRequest(localHeartbeatListener, localWLClient.GetHttpContext(), localWLRequestOptions, localWLClient.getConfig(), localWLClient.getContext()).makeRequest("heartbeat");
    }
  }

  class HeartbeatListener
    implements WLRequestListener
  {
    HeartbeatListener()
    {
    }

    public void onFailure(WLFailResponse paramWLFailResponse)
    {
      WLUtils.debug("Failed to send heartbeat.");
    }

    public void onSuccess(WLResponse paramWLResponse)
    {
      WLUtils.debug("Heartbeat sent successfully");
    }
  }

  class InitRequestListener
    implements WLRequestListener
  {
    public InitRequestListener()
    {
    }

    public void onFailure(WLFailResponse paramWLFailResponse)
    {
      paramWLFailResponse.getOptions().getResponseListener().onFailure(paramWLFailResponse);
    }

    public void onSuccess(WLResponse paramWLResponse)
    {
      WLClient.getInstance().setInitialized(true);
      WLClient localWLClient = WLClient.getInstance();
      if (localWLClient != null)
      {
        localWLClient.sendHeartBeat();
        try
        {
          JSONObject localJSONObject = paramWLResponse.getResponseJSON().getJSONObject("userPrefs");
          if (localJSONObject != null)
          {
            Iterator localIterator = localJSONObject.keys();
            while (true)
            {
              boolean bool = localIterator.hasNext();
              if (bool)
                try
                {
                  String str1 = (String)localIterator.next();
                  String str2 = (String)localJSONObject.get(str1);
                  WLClient.this.userPreferenceMap.put(str1, str2);
                }
                catch (JSONException localJSONException2)
                {
                  throw new RuntimeException(localJSONException2.getMessage());
                }
            }
          }
        }
        catch (JSONException localJSONException1)
        {
          localJSONException1.printStackTrace();
        }
      }
      paramWLResponse.getOptions().getResponseListener().onSuccess(paramWLResponse);
    }
  }

  class InvokeProcedureRequestListener
    implements WLRequestListener
  {
    InvokeProcedureRequestListener()
    {
    }

    public void onFailure(WLFailResponse paramWLFailResponse)
    {
      WLProcedureInvocationFailResponse localWLProcedureInvocationFailResponse = new WLProcedureInvocationFailResponse(paramWLFailResponse);
      paramWLFailResponse.getOptions().getResponseListener().onFailure(localWLProcedureInvocationFailResponse);
    }

    public void onSuccess(WLResponse paramWLResponse)
    {
      WLProcedureInvocationResult localWLProcedureInvocationResult = new WLProcedureInvocationResult(paramWLResponse);
      if (localWLProcedureInvocationResult.isSuccessful())
      {
        localWLProcedureInvocationResult.getOptions().getResponseListener().onSuccess(localWLProcedureInvocationResult);
        return;
      }
      WLProcedureInvocationFailResponse localWLProcedureInvocationFailResponse = new WLProcedureInvocationFailResponse(paramWLResponse);
      localWLProcedureInvocationFailResponse.setErrorCode(WLErrorCode.PROCEDURE_ERROR);
      localWLProcedureInvocationFailResponse.getOptions().getResponseListener().onFailure(localWLProcedureInvocationFailResponse);
    }
  }

  class LogActivityListener
    implements WLRequestListener
  {
    LogActivityListener()
    {
    }

    public void onFailure(WLFailResponse paramWLFailResponse)
    {
      WLUtils.error("Activity will not be logged in Worklight server using logActivity() because of " + paramWLFailResponse.getErrorMsg());
    }

    public void onSuccess(WLResponse paramWLResponse)
    {
      WLUtils.debug("logActivity success. Response from server is " + paramWLResponse.toString());
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.api.WLClient
 * JD-Core Version:    0.6.0
 */