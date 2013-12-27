package com.phonegap.plugins.childBrowser;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cn.domob.ui.DViewManager;
import cn.domob.ui.DViewManager.GetDataListener;
import com.MobileTicket.MobileTicket;
import java.net.URLEncoder;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.LegacyContext;
import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChildBrowser extends Plugin
{
  private static int CLOSE_EVENT = 0;
  private static int DIALOGOK_EVENT = 0;
  private static int LOCATION_CHANGED_EVENT = 0;
  protected static final String LOG_TAG = "ChildBrowser";
  private static int ORDERCOMPLETE_EVENT;
  private static int ORDERLIST_EVENT;
  private static int event_flag = 0;
  private AlertDialog aDialog;
  private RelativeLayout adsView;
  private String appId = "";
  private String browserCallbackId = null;
  private String counterMsg = "";
  private Dialog dialog;
  private EditText edittext;
  private String interfaceName = "";
  private String interfaceVersion = "";
  private String merSignMsg = "";
  private ProgressDialog spinnerDialog;
  private TextView textView;
  private String tranData = "";
  private String transType = "";
  private WebView webview;

  static
  {
    ORDERLIST_EVENT = 2;
    ORDERCOMPLETE_EVENT = 3;
    DIALOGOK_EVENT = 4;
  }

  private void closeDialog()
  {
    if (this.dialog != null)
      this.dialog.dismiss();
  }

  private void sendUpdate(JSONObject paramJSONObject, boolean paramBoolean)
  {
    if (this.browserCallbackId != null)
    {
      Log.d("ChildBrowser", this.browserCallbackId);
      PluginResult localPluginResult = new PluginResult(PluginResult.Status.OK, paramJSONObject);
      localPluginResult.setKeepCallback(paramBoolean);
      success(localPluginResult, this.browserCallbackId);
    }
  }

  public PluginResult execute(String paramString1, JSONArray paramJSONArray, String paramString2)
  {
    PluginResult.Status localStatus = PluginResult.Status.OK;
    String str1 = "";
    String str3;
    try
    {
      if (!paramString1.equals("showWebPage"))
        break label129;
      this.browserCallbackId = paramString2;
      if ((this.dialog != null) && (this.dialog.isShowing()))
        return new PluginResult(PluginResult.Status.ERROR, "ChildBrowser is already open");
      str3 = showWebPage(paramJSONArray.getString(0), paramJSONArray.optJSONObject(1));
      if (str3.length() > 0)
      {
        PluginResult localPluginResult4 = new PluginResult(PluginResult.Status.ERROR, str3);
        return localPluginResult4;
      }
    }
    catch (JSONException localJSONException)
    {
      return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
    }
    PluginResult localPluginResult5 = new PluginResult(localStatus, str3);
    localPluginResult5.setKeepCallback(true);
    return localPluginResult5;
    label129: if (paramString1.equals("showAdsView"))
    {
      this.browserCallbackId = paramString2;
      if ((this.dialog != null) && (this.dialog.isShowing()))
        return new PluginResult(PluginResult.Status.ERROR, "ChildBrowser is already open");
      String str2 = showAdsWebPage(paramJSONArray.getString(0), paramJSONArray.optJSONObject(1));
      if (str2.length() > 0)
        return new PluginResult(PluginResult.Status.ERROR, str2);
      PluginResult localPluginResult3 = new PluginResult(localStatus, str2);
      localPluginResult3.setKeepCallback(true);
      return localPluginResult3;
    }
    if (paramString1.equals("openDialog"))
    {
      this.browserCallbackId = paramString2;
      openDialog(paramJSONArray.optJSONObject(1));
      if (str1.length() > 0)
        return new PluginResult(PluginResult.Status.ERROR, str1);
      PluginResult localPluginResult2 = new PluginResult(localStatus, str1);
      localPluginResult2.setKeepCallback(true);
      return localPluginResult2;
    }
    if (paramString1.equals("updateDialog"))
    {
      StringBuilder localStringBuilder = new StringBuilder(this.counterMsg);
      localStringBuilder.append(paramJSONArray.getString(0));
      updateDialog(localStringBuilder.toString());
    }
    while (true)
    {
      return new PluginResult(localStatus, str1);
      if (paramString1.equals("closeDialog"))
      {
        Log.d("ChildBrowser", "alert dismiss");
        this.aDialog.cancel();
        continue;
      }
      if (paramString1.equals("close"))
      {
        closeDialog();
        JSONObject localJSONObject = new JSONObject();
        localJSONObject.put("type", CLOSE_EVENT);
        PluginResult localPluginResult1 = new PluginResult(localStatus, localJSONObject);
        localPluginResult1.setKeepCallback(false);
        return localPluginResult1;
      }
      if (paramString1.equals("openExternal"))
      {
        str1 = openExternal(paramJSONArray.getString(0), paramJSONArray.optBoolean(1));
        if (str1.length() <= 0)
          continue;
        localStatus = PluginResult.Status.ERROR;
        continue;
      }
      localStatus = PluginResult.Status.INVALID_ACTION;
    }
  }

  public String openDialog(JSONObject paramJSONObject)
  {
    String str1 = paramJSONObject.optString("message");
    String str2 = paramJSONObject.optString("buttonText");
    String str3 = paramJSONObject.optString("title");
    this.counterMsg = str1;
    1 local1 = new Runnable(str3, str1, str2)
    {
      public void run()
      {
        ChildBrowser.this.showDialog(ChildBrowser.this.ctx.getContext(), this.val$title, this.val$message, this.val$buttonText);
      }
    };
    this.ctx.runOnUiThread(local1);
    return "";
  }

  // ERROR //
  public String openExternal(String paramString, boolean paramBoolean)
  {
    // Byte code:
    //   0: iload_2
    //   1: ifeq +94 -> 95
    //   4: new 298	android/content/Intent
    //   7: dup
    //   8: invokespecial 299	android/content/Intent:<init>	()V
    //   11: aload_0
    //   12: getfield 288	com/phonegap/plugins/childBrowser/ChildBrowser:ctx	Lorg/apache/cordova/api/LegacyContext;
    //   15: invokevirtual 303	org/apache/cordova/api/LegacyContext:getContext	()Landroid/content/Context;
    //   18: ldc_w 305
    //   21: invokevirtual 309	android/content/Intent:setClass	(Landroid/content/Context;Ljava/lang/Class;)Landroid/content/Intent;
    //   24: astore 7
    //   26: aload 7
    //   28: aload_1
    //   29: invokestatic 315	android/net/Uri:parse	(Ljava/lang/String;)Landroid/net/Uri;
    //   32: invokevirtual 319	android/content/Intent:setData	(Landroid/net/Uri;)Landroid/content/Intent;
    //   35: pop
    //   36: aload 7
    //   38: ldc_w 321
    //   41: aload_1
    //   42: invokevirtual 325	android/content/Intent:putExtra	(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;
    //   45: pop
    //   46: aload 7
    //   48: ldc_w 327
    //   51: ldc_w 328
    //   54: invokevirtual 331	android/content/Intent:putExtra	(Ljava/lang/String;I)Landroid/content/Intent;
    //   57: pop
    //   58: aload 7
    //   60: ldc_w 333
    //   63: ldc_w 335
    //   66: invokevirtual 325	android/content/Intent:putExtra	(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;
    //   69: pop
    //   70: aload 7
    //   72: ldc_w 337
    //   75: iconst_1
    //   76: invokevirtual 340	android/content/Intent:putExtra	(Ljava/lang/String;Z)Landroid/content/Intent;
    //   79: pop
    //   80: aload_0
    //   81: getfield 288	com/phonegap/plugins/childBrowser/ChildBrowser:ctx	Lorg/apache/cordova/api/LegacyContext;
    //   84: invokevirtual 303	org/apache/cordova/api/LegacyContext:getContext	()Landroid/content/Context;
    //   87: aload 7
    //   89: invokevirtual 346	android/content/Context:startActivity	(Landroid/content/Intent;)V
    //   92: ldc 59
    //   94: areturn
    //   95: new 298	android/content/Intent
    //   98: dup
    //   99: ldc_w 348
    //   102: invokespecial 349	android/content/Intent:<init>	(Ljava/lang/String;)V
    //   105: astore_3
    //   106: aload_3
    //   107: aload_1
    //   108: invokestatic 315	android/net/Uri:parse	(Ljava/lang/String;)Landroid/net/Uri;
    //   111: invokevirtual 319	android/content/Intent:setData	(Landroid/net/Uri;)Landroid/content/Intent;
    //   114: pop
    //   115: aload_3
    //   116: astore 7
    //   118: goto -38 -> 80
    //   121: astore 4
    //   123: ldc 13
    //   125: new 223	java/lang/StringBuilder
    //   128: dup
    //   129: ldc_w 351
    //   132: invokespecial 226	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   135: aload_1
    //   136: invokevirtual 230	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   139: ldc_w 353
    //   142: invokevirtual 230	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   145: aload 4
    //   147: invokevirtual 354	android/content/ActivityNotFoundException:toString	()Ljava/lang/String;
    //   150: invokevirtual 230	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   153: invokevirtual 234	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   156: invokestatic 144	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   159: pop
    //   160: aload 4
    //   162: invokevirtual 354	android/content/ActivityNotFoundException:toString	()Ljava/lang/String;
    //   165: areturn
    //   166: astore 4
    //   168: goto -45 -> 123
    //
    // Exception table:
    //   from	to	target	type
    //   4	80	121	android/content/ActivityNotFoundException
    //   80	92	121	android/content/ActivityNotFoundException
    //   95	106	121	android/content/ActivityNotFoundException
    //   106	115	166	android/content/ActivityNotFoundException
  }

  public void reqAdWall()
  {
    MobileTicket.mDViewManager.setAddViewListener(new DViewManager.GetDataListener()
    {
      public void onFailReceiveData()
      {
        if ((ChildBrowser.this.spinnerDialog != null) && (ChildBrowser.this.spinnerDialog.isShowing()))
          ChildBrowser.this.spinnerDialog.dismiss();
        MobileTicket.mDViewManager.doEntranceClickReport();
        ChildBrowser.this.showErrorDialog();
      }

      public void onSuccessReceiveData(int paramInt)
      {
        if (MobileTicket.mDViewManager.getView() != null)
        {
          ChildBrowser.this.adsView.addView(MobileTicket.mDViewManager.getView());
          if ((ChildBrowser.this.spinnerDialog != null) && (ChildBrowser.this.spinnerDialog.isShowing()))
            ChildBrowser.this.spinnerDialog.dismiss();
        }
        while (true)
        {
          MobileTicket.mDViewManager.doEntranceClickReport();
          return;
          if ((ChildBrowser.this.spinnerDialog != null) && (ChildBrowser.this.spinnerDialog.isShowing()))
            ChildBrowser.this.spinnerDialog.dismiss();
          ChildBrowser.this.showErrorDialog();
        }
      }
    });
    MobileTicket.mDViewManager.addView();
  }

  public String showAdsWebPage(String paramString, JSONObject paramJSONObject)
  {
    5 local5 = new Runnable()
    {
      public void run()
      {
        ChildBrowser.this.dialog = new Dialog(ChildBrowser.this.ctx.getContext(), 16973830);
        ChildBrowser.this.dialog.getWindow().getAttributes().windowAnimations = 16973826;
        ChildBrowser.this.dialog.requestWindowFeature(1);
        ChildBrowser.this.dialog.setCancelable(true);
        LinearLayout localLinearLayout = (LinearLayout)((LayoutInflater)ChildBrowser.this.ctx.getContext().getSystemService("layout_inflater")).inflate(2130903055, null);
        ChildBrowser.this.adsView = ((RelativeLayout)localLinearLayout.findViewById(2131230791));
        if (MobileTicket.mDViewManager.getView() == null)
          ChildBrowser.this.reqAdWall();
        while (true)
        {
          ((Button)localLinearLayout.findViewById(2131230789)).setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramView)
            {
              ChildBrowser.event_flag = 2;
              ChildBrowser.this.closeDialog();
            }
          });
          WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
          localLayoutParams.copyFrom(ChildBrowser.this.dialog.getWindow().getAttributes());
          localLayoutParams.width = -1;
          localLayoutParams.height = -1;
          ChildBrowser.this.dialog.setContentView(localLinearLayout);
          ChildBrowser.this.dialog.show();
          ChildBrowser.this.dialog.getWindow().setAttributes(localLayoutParams);
          ChildBrowser.this.dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
          {
            public void onDismiss(DialogInterface paramDialogInterface)
            {
              ChildBrowser.this.adsView.removeAllViews();
              MobileTicket.mDViewManager.doExitReport();
            }
          });
          if (MobileTicket.mDViewManager.getView() == null)
          {
            ChildBrowser.this.spinnerDialog = new ProgressDialog(ChildBrowser.this.cordova.getActivity());
            ChildBrowser.this.spinnerDialog.setCancelable(true);
            ChildBrowser.this.spinnerDialog.setCanceledOnTouchOutside(false);
            ChildBrowser.this.spinnerDialog.setIndeterminate(true);
            ChildBrowser.this.spinnerDialog.setMessage("加载中... ");
            ChildBrowser.this.spinnerDialog.show();
          }
          return;
          ChildBrowser.this.adsView.addView(MobileTicket.mDViewManager.getView());
          if ((ChildBrowser.this.spinnerDialog != null) && (ChildBrowser.this.spinnerDialog.isShowing()))
            ChildBrowser.this.spinnerDialog.dismiss();
          MobileTicket.mDViewManager.doEntranceClickReport();
        }
      }
    };
    this.ctx.runOnUiThread(local5);
    return "";
  }

  public void showAndroidDialog(Context paramContext, String paramString1, String paramString2, String paramString3, DialogInterface.OnClickListener paramOnClickListener)
  {
    this.textView = new TextView(paramContext);
    this.textView.setText(paramString2);
    this.textView.setTextSize(16.0F);
    this.textView.setTextColor(-1);
    this.aDialog = new AlertDialog.Builder(paramContext).setTitle(paramString1).setCancelable(false).setPositiveButton(paramString3, paramOnClickListener).setView(this.textView).create();
    this.aDialog.show();
  }

  public void showDialog(Context paramContext, String paramString1, String paramString2, String paramString3)
  {
    showAndroidDialog(paramContext, paramString1, paramString2, paramString3, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        Log.d("ChildBrowser", "onClick*************");
        paramDialogInterface.cancel();
        try
        {
          JSONObject localJSONObject = new JSONObject();
          localJSONObject.put("type", ChildBrowser.DIALOGOK_EVENT);
          ChildBrowser.this.sendUpdate(localJSONObject, false);
          Log.d("ChildBrowser", "sendUpdate**********");
          return;
        }
        catch (JSONException localJSONException)
        {
          Log.d("ChildBrowser", "Should never happen");
        }
      }
    });
  }

  public void showErrorDialog()
  {
    new AlertDialog.Builder(this.ctx.getContext()).setTitle("温馨提示").setMessage("哎呀，您的网络有问题，请检查网络连接。").setNegativeButton("取消", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
      }
    }).setPositiveButton("重试", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        ChildBrowser.this.reqAdWall();
      }
    }).show();
  }

  public String showWebPage(String paramString, JSONObject paramJSONObject)
  {
    if (paramJSONObject != null)
    {
      this.interfaceName = paramJSONObject.optString("interfaceName");
      this.interfaceVersion = paramJSONObject.optString("interfaceVersion");
      this.tranData = paramJSONObject.optString("tranData");
      this.merSignMsg = paramJSONObject.optString("merSignMsg");
      this.appId = paramJSONObject.optString("appId");
      this.transType = paramJSONObject.optString("transType");
      Log.d("childbrowser-appid", this.appId);
      Log.d("childbrowser-interfaceName", this.interfaceName);
    }
    4 local4 = new Runnable(paramString)
    {
      public void run()
      {
        ChildBrowser.this.dialog = new Dialog(ChildBrowser.this.ctx.getContext(), 16973830);
        ChildBrowser.this.dialog.getWindow().getAttributes().windowAnimations = 16973826;
        ChildBrowser.this.dialog.requestWindowFeature(1);
        ChildBrowser.this.dialog.setCancelable(true);
        ChildBrowser.this.dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
          public void onDismiss(DialogInterface paramDialogInterface)
          {
            try
            {
              JSONObject localJSONObject = new JSONObject();
              if (ChildBrowser.event_flag == 2)
              {
                localJSONObject.put("type", ChildBrowser.ORDERLIST_EVENT);
                return;
              }
              if (ChildBrowser.event_flag == 3)
              {
                localJSONObject.put("type", ChildBrowser.ORDERCOMPLETE_EVENT);
                return;
              }
            }
            catch (JSONException localJSONException)
            {
              Log.d("ChildBrowser", "Should never happen");
            }
          }
        });
        LinearLayout localLinearLayout = (LinearLayout)((LayoutInflater)ChildBrowser.this.ctx.getContext().getSystemService("layout_inflater")).inflate(2130903056, null);
        ChildBrowser.this.webview = ((WebView)localLinearLayout.findViewById(2131230793));
        WebSettings localWebSettings = ChildBrowser.this.webview.getSettings();
        localWebSettings.setJavaScriptEnabled(true);
        localWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        localWebSettings.setBuiltInZoomControls(true);
        localWebSettings.setPluginsEnabled(true);
        localWebSettings.setDomStorageEnabled(true);
        ChildBrowser.this.webview.addJavascriptInterface(new ChildBrowser.DemoJavaScriptInterface(ChildBrowser.this), "Merchants");
        ChildBrowser.this.webview.setDownloadListener(new ChildBrowser.MyWebViewDownLoadListener(ChildBrowser.this, null));
        ((Button)localLinearLayout.findViewById(2131230789)).setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            try
            {
              JSONObject localJSONObject = new JSONObject();
              ChildBrowser.event_flag = 2;
              localJSONObject.put("type", ChildBrowser.ORDERLIST_EVENT);
              ChildBrowser.this.sendUpdate(localJSONObject, false);
              return;
            }
            catch (Exception localException)
            {
              Log.d("ChildBrowser", "Should never happen");
            }
          }
        });
        ChildBrowser.this.edittext = new EditText(ChildBrowser.this.ctx.getContext());
        RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
        localLayoutParams.addRule(1, 1);
        localLayoutParams.addRule(0, 5);
        ChildBrowser.this.edittext.setVisibility(8);
        ChildBrowser.this.edittext.setLayoutParams(localLayoutParams);
        ChildBrowser.this.edittext.setId(4);
        ChildBrowser.this.edittext.setSingleLine(true);
        ChildBrowser.this.edittext.setText(this.val$url);
        ChildBrowser.this.edittext.setInputType(16);
        ChildBrowser.this.edittext.setImeOptions(2);
        ChildBrowser.this.edittext.setInputType(0);
        ((Button)localLinearLayout.findViewById(2131230792)).setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            try
            {
              JSONObject localJSONObject = new JSONObject();
              ChildBrowser.event_flag = 3;
              localJSONObject.put("type", ChildBrowser.ORDERCOMPLETE_EVENT);
              ChildBrowser.this.sendUpdate(localJSONObject, false);
              return;
            }
            catch (JSONException localJSONException)
            {
              Log.d("ChildBrowser", "Should never happen");
            }
          }
        });
        ChildBrowser.this.webview.setWebChromeClient(new WebChromeClient()
        {
          public void onReceivedTitle(WebView paramWebView, String paramString)
          {
            if (paramWebView.getUrl().contains("/epayPage/epay?"))
              Log.e("childbrowser", "payment done /epayPage/epay");
            try
            {
              Thread.currentThread();
              Thread.sleep(1000L);
              JSONObject localJSONObject = new JSONObject();
              ChildBrowser.event_flag = 3;
              localJSONObject.put("type", ChildBrowser.ORDERCOMPLETE_EVENT);
              ChildBrowser.this.sendUpdate(localJSONObject, false);
              return;
            }
            catch (Exception localException)
            {
              Log.d("ChildBrowser", "Should never happen");
            }
          }
        });
        ChildBrowser.ChildBrowserClient localChildBrowserClient = new ChildBrowser.ChildBrowserClient(ChildBrowser.this, ChildBrowser.this.edittext);
        ChildBrowser.this.webview.setWebViewClient(localChildBrowserClient);
        try
        {
          String str = "interfaceName=" + ChildBrowser.this.interfaceName + "&interfaceVersion=" + ChildBrowser.this.interfaceVersion + "&tranData=" + URLEncoder.encode(ChildBrowser.this.tranData, "UTF-8") + "&merSignMsg=" + URLEncoder.encode(ChildBrowser.this.merSignMsg, "UTF-8") + "&appId=" + ChildBrowser.this.appId + "&transType=" + ChildBrowser.this.transType;
          ChildBrowser.this.webview.postUrl(this.val$url, str.getBytes());
          ChildBrowser.this.webview.setId(6);
          ChildBrowser.this.webview.getSettings().setLoadWithOverviewMode(true);
          ChildBrowser.this.webview.getSettings().setUseWideViewPort(true);
          ChildBrowser.this.webview.requestFocus();
          ChildBrowser.this.webview.requestFocusFromTouch();
          WindowManager.LayoutParams localLayoutParams1 = new WindowManager.LayoutParams();
          localLayoutParams1.copyFrom(ChildBrowser.this.dialog.getWindow().getAttributes());
          localLayoutParams1.width = -1;
          localLayoutParams1.height = -1;
          ChildBrowser.this.dialog.setContentView(localLinearLayout);
          ChildBrowser.this.dialog.show();
          ChildBrowser.this.dialog.getWindow().setAttributes(localLayoutParams1);
          return;
        }
        catch (Exception localException)
        {
          while (true)
          {
            Log.e("childbrowser", "error when post payment url");
            localException.printStackTrace();
          }
        }
      }
    };
    this.ctx.runOnUiThread(local4);
    return "";
  }

  public String updateDialog(String paramString)
  {
    3 local3 = new Runnable(paramString)
    {
      public void run()
      {
        ChildBrowser.this.textView.setText(this.val$msg);
      }
    };
    this.ctx.runOnUiThread(local3);
    return "";
  }

  public class ChildBrowserClient extends WebViewClient
  {
    EditText edittext;

    public ChildBrowserClient(EditText arg2)
    {
      Object localObject;
      this.edittext = localObject;
    }

    public void onPageStarted(WebView paramWebView, String paramString, Bitmap paramBitmap)
    {
      super.onPageStarted(paramWebView, paramString, paramBitmap);
      String str;
      if ((paramString.startsWith("http:")) || (paramString.startsWith("https:")) || (paramString.startsWith("file:")))
        str = paramString;
      while (true)
      {
        if (!str.equals(this.edittext.getText().toString()))
          this.edittext.setText(str);
        try
        {
          JSONObject localJSONObject = new JSONObject();
          localJSONObject.put("type", ChildBrowser.LOCATION_CHANGED_EVENT);
          localJSONObject.put("location", paramString);
          ChildBrowser.this.sendUpdate(localJSONObject, true);
          return;
          str = "http://" + paramString;
        }
        catch (JSONException localJSONException)
        {
          Log.d("ChildBrowser", "This should never happen");
        }
      }
    }

    public void onReceivedSslError(WebView paramWebView, SslErrorHandler paramSslErrorHandler, SslError paramSslError)
    {
      Log.d("SSL", "error");
      paramSslErrorHandler.proceed();
    }
  }

  final class DemoJavaScriptInterface
  {
    DemoJavaScriptInterface()
    {
    }

    public String isInstallCashier(String paramString)
    {
      Log.d("ChildBrowser", paramString);
      try
      {
        PackageInfo localPackageInfo = ChildBrowser.this.ctx.getContext().getPackageManager().getPackageInfo("com.unionpay.mobilepay.mpos.Activity", 0);
        if (localPackageInfo == null)
          return "0";
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        localNameNotFoundException.printStackTrace();
        return "0";
      }
      Intent localIntent = new Intent();
      localIntent.setAction("android.intent.action.VIEW");
      localIntent.setData(Uri.parse(paramString));
      ChildBrowser.this.ctx.startActivity(localIntent);
      return "1";
    }
  }

  private class MyWebViewDownLoadListener
    implements DownloadListener
  {
    private MyWebViewDownLoadListener()
    {
    }

    public void onDownloadStart(String paramString1, String paramString2, String paramString3, String paramString4, long paramLong)
    {
      Log.d("ChildBrowser", "***************onDownloadStart*************");
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(paramString1));
      ChildBrowser.this.ctx.startActivity(localIntent);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.phonegap.plugins.childBrowser.ChildBrowser
 * JD-Core Version:    0.6.0
 */