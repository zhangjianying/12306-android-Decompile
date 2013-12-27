package org.apache.cordova;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import java.io.File;
import java.util.HashMap;
import java.util.StringTokenizer;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.LOG;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint({"SetJavaScriptEnabled"})
public class InAppBrowser extends CordovaPlugin
{
  private static final String CLOSE_BUTTON_CAPTION = "closebuttoncaption";
  private static final String EXIT_EVENT = "exit";
  private static final String LOAD_ERROR_EVENT = "loaderror";
  private static final String LOAD_START_EVENT = "loadstart";
  private static final String LOAD_STOP_EVENT = "loadstop";
  private static final String LOCATION = "location";
  protected static final String LOG_TAG = "InAppBrowser";
  private static final String NULL = "null";
  private static final String SELF = "_self";
  private static final String SYSTEM = "_system";
  private long MAX_QUOTA = 104857600L;
  private String buttonLabel = "Done";
  private CallbackContext callbackContext;
  private Dialog dialog;
  private EditText edittext;
  private WebView inAppWebView;
  private boolean showLocationBar = true;

  private void closeDialog()
  {
    try
    {
      this.inAppWebView.loadUrl("about:blank");
      JSONObject localJSONObject = new JSONObject();
      localJSONObject.put("type", "exit");
      sendUpdate(localJSONObject, false);
      if (this.dialog != null)
        this.dialog.dismiss();
      return;
    }
    catch (JSONException localJSONException)
    {
      while (true)
        Log.d("InAppBrowser", "Should never happen");
    }
  }

  private boolean getShowLocationBar()
  {
    return this.showLocationBar;
  }

  private void goBack()
  {
    if (this.inAppWebView.canGoBack())
      this.inAppWebView.goBack();
  }

  private void goForward()
  {
    if (this.inAppWebView.canGoForward())
      this.inAppWebView.goForward();
  }

  private void navigate(String paramString)
  {
    ((InputMethodManager)this.cordova.getActivity().getSystemService("input_method")).hideSoftInputFromWindow(this.edittext.getWindowToken(), 0);
    if ((!paramString.startsWith("http")) && (!paramString.startsWith("file:")))
      this.inAppWebView.loadUrl("http://" + paramString);
    while (true)
    {
      this.inAppWebView.requestFocus();
      return;
      this.inAppWebView.loadUrl(paramString);
    }
  }

  private HashMap<String, Boolean> parseFeature(String paramString)
  {
    if (paramString.equals("null"))
    {
      localHashMap = null;
      return localHashMap;
    }
    HashMap localHashMap = new HashMap();
    StringTokenizer localStringTokenizer1 = new StringTokenizer(paramString, ",");
    label32: String str;
    while (localStringTokenizer1.hasMoreElements())
    {
      StringTokenizer localStringTokenizer2 = new StringTokenizer(localStringTokenizer1.nextToken(), "=");
      if (!localStringTokenizer2.hasMoreElements())
        continue;
      str = localStringTokenizer2.nextToken();
      if (str.equalsIgnoreCase("closebuttoncaption"))
      {
        this.buttonLabel = localStringTokenizer2.nextToken();
        continue;
      }
      if (!localStringTokenizer2.nextToken().equals("no"))
        break label121;
    }
    label121: for (Boolean localBoolean = Boolean.FALSE; ; localBoolean = Boolean.TRUE)
    {
      localHashMap.put(str, localBoolean);
      break label32;
      break;
    }
  }

  private void sendUpdate(JSONObject paramJSONObject, boolean paramBoolean)
  {
    sendUpdate(paramJSONObject, paramBoolean, PluginResult.Status.OK);
  }

  private void sendUpdate(JSONObject paramJSONObject, boolean paramBoolean, PluginResult.Status paramStatus)
  {
    PluginResult localPluginResult = new PluginResult(paramStatus, paramJSONObject);
    localPluginResult.setKeepCallback(paramBoolean);
    this.callbackContext.sendPluginResult(localPluginResult);
  }

  private String updateUrl(String paramString)
  {
    if (Uri.parse(paramString).isRelative())
      paramString = this.webView.getUrl().substring(0, 1 + this.webView.getUrl().lastIndexOf("/")) + paramString;
    return paramString;
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    PluginResult.Status localStatus = PluginResult.Status.OK;
    String str1 = "";
    this.callbackContext = paramCallbackContext;
    try
    {
      if (paramString.equals("open"))
      {
        String str6 = paramJSONArray.getString(0);
        str7 = paramJSONArray.optString(1);
        if ((str7 == null) || (str7.equals("")) || (str7.equals("null")))
          break label579;
        localHashMap = parseFeature(paramJSONArray.optString(2));
        Log.d("InAppBrowser", "target = " + str7);
        str8 = updateUrl(str6);
        if ("_self".equals(str7))
        {
          Log.d("InAppBrowser", "in self");
          if ((str8.startsWith("file://")) || (str8.startsWith("javascript:")) || (Config.isUrlWhiteListed(str8)))
            this.webView.loadUrl(str8);
          while (true)
          {
            PluginResult localPluginResult1 = new PluginResult(localStatus, str1);
            localPluginResult1.setKeepCallback(true);
            this.callbackContext.sendPluginResult(localPluginResult1);
            break label586;
            boolean bool = str8.startsWith("tel:");
            if (!bool)
              break;
            try
            {
              Intent localIntent = new Intent("android.intent.action.DIAL");
              localIntent.setData(Uri.parse(str8));
              this.cordova.getActivity().startActivity(localIntent);
            }
            catch (ActivityNotFoundException localActivityNotFoundException)
            {
              LOG.e("InAppBrowser", "Error dialing " + str8 + ": " + localActivityNotFoundException.toString());
            }
          }
        }
      }
    }
    catch (JSONException localJSONException)
    {
      while (true)
      {
        HashMap localHashMap;
        String str8;
        this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
        break;
        str1 = showWebPage(str8, localHashMap);
        continue;
        if ("_system".equals(str7))
        {
          Log.d("InAppBrowser", "in system");
          str1 = openExternal(str8);
          continue;
        }
        Log.d("InAppBrowser", "in blank");
        str1 = showWebPage(str8, localHashMap);
        continue;
        if (paramString.equals("close"))
        {
          closeDialog();
          PluginResult localPluginResult3 = new PluginResult(PluginResult.Status.OK);
          localPluginResult3.setKeepCallback(false);
          this.callbackContext.sendPluginResult(localPluginResult3);
          continue;
        }
        if (paramString.equals("injectScriptCode"))
        {
          String str2 = paramJSONArray.getString(0);
          JSONArray localJSONArray = new JSONArray();
          localJSONArray.put(str2);
          String str3 = localJSONArray.toString();
          String str4 = str3.substring(1, -1 + str3.length());
          String str5 = "(function(d){var c=d.createElement('script');c.type='text/javascript';c.innerText=" + str4 + ";d.getElementsByTagName('head')[0].appendChild(c);})(document)";
          this.inAppWebView.loadUrl("javascript:" + str5);
          PluginResult localPluginResult2 = new PluginResult(PluginResult.Status.OK);
          this.callbackContext.sendPluginResult(localPluginResult2);
          continue;
        }
        localStatus = PluginResult.Status.INVALID_ACTION;
        continue;
        label579: String str7 = "_self";
      }
    }
    label586: return true;
  }

  // ERROR //
  public String openExternal(String paramString)
  {
    // Byte code:
    //   0: new 354	android/content/Intent
    //   3: dup
    //   4: ldc_w 415
    //   7: invokespecial 358	android/content/Intent:<init>	(Ljava/lang/String;)V
    //   10: astore_2
    //   11: aload_2
    //   12: aload_1
    //   13: invokestatic 290	android/net/Uri:parse	(Ljava/lang/String;)Landroid/net/Uri;
    //   16: invokevirtual 362	android/content/Intent:setData	(Landroid/net/Uri;)Landroid/content/Intent;
    //   19: pop
    //   20: aload_0
    //   21: getfield 166	org/apache/cordova/InAppBrowser:cordova	Lorg/apache/cordova/api/CordovaInterface;
    //   24: invokeinterface 172 1 0
    //   29: aload_2
    //   30: invokevirtual 366	android/app/Activity:startActivity	(Landroid/content/Intent;)V
    //   33: ldc_w 318
    //   36: areturn
    //   37: astore_3
    //   38: ldc 29
    //   40: new 204	java/lang/StringBuilder
    //   43: dup
    //   44: invokespecial 205	java/lang/StringBuilder:<init>	()V
    //   47: ldc_w 417
    //   50: invokevirtual 211	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   53: aload_1
    //   54: invokevirtual 211	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   57: ldc_w 419
    //   60: invokevirtual 211	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: aload_3
    //   64: invokevirtual 371	android/content/ActivityNotFoundException:toString	()Ljava/lang/String;
    //   67: invokevirtual 211	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   70: invokevirtual 215	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   73: invokestatic 154	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   76: pop
    //   77: aload_3
    //   78: invokevirtual 371	android/content/ActivityNotFoundException:toString	()Ljava/lang/String;
    //   81: areturn
    //   82: astore_3
    //   83: goto -45 -> 38
    //
    // Exception table:
    //   from	to	target	type
    //   0	11	37	android/content/ActivityNotFoundException
    //   11	33	82	android/content/ActivityNotFoundException
  }

  public String showWebPage(String paramString, HashMap<String, Boolean> paramHashMap)
  {
    this.showLocationBar = true;
    if (paramHashMap != null)
    {
      Boolean localBoolean = (Boolean)paramHashMap.get("location");
      if (localBoolean != null)
        this.showLocationBar = localBoolean.booleanValue();
    }
    1 local1 = new Runnable(paramString, this.webView)
    {
      private int dpToPixels(int paramInt)
      {
        return (int)TypedValue.applyDimension(1, paramInt, InAppBrowser.this.cordova.getActivity().getResources().getDisplayMetrics());
      }

      public void run()
      {
        InAppBrowser.access$002(InAppBrowser.this, new Dialog(InAppBrowser.this.cordova.getActivity(), 16973830));
        InAppBrowser.this.dialog.getWindow().getAttributes().windowAnimations = 16973826;
        InAppBrowser.this.dialog.requestWindowFeature(1);
        InAppBrowser.this.dialog.setCancelable(true);
        Dialog localDialog = InAppBrowser.this.dialog;
        1 local1 = new DialogInterface.OnDismissListener()
        {
          public void onDismiss(DialogInterface paramDialogInterface)
          {
            try
            {
              JSONObject localJSONObject = new JSONObject();
              localJSONObject.put("type", "exit");
              InAppBrowser.this.sendUpdate(localJSONObject, false);
              return;
            }
            catch (JSONException localJSONException)
            {
              Log.d("InAppBrowser", "Should never happen");
            }
          }
        };
        localDialog.setOnDismissListener(local1);
        LinearLayout localLinearLayout = new LinearLayout(InAppBrowser.this.cordova.getActivity());
        localLinearLayout.setOrientation(1);
        RelativeLayout localRelativeLayout1 = new RelativeLayout(InAppBrowser.this.cordova.getActivity());
        localRelativeLayout1.setLayoutParams(new RelativeLayout.LayoutParams(-1, dpToPixels(44)));
        localRelativeLayout1.setPadding(dpToPixels(2), dpToPixels(2), dpToPixels(2), dpToPixels(2));
        localRelativeLayout1.setHorizontalGravity(3);
        localRelativeLayout1.setVerticalGravity(48);
        RelativeLayout localRelativeLayout2 = new RelativeLayout(InAppBrowser.this.cordova.getActivity());
        localRelativeLayout2.setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));
        localRelativeLayout2.setHorizontalGravity(3);
        localRelativeLayout2.setVerticalGravity(16);
        localRelativeLayout2.setId(1);
        Button localButton1 = new Button(InAppBrowser.this.cordova.getActivity());
        RelativeLayout.LayoutParams localLayoutParams1 = new RelativeLayout.LayoutParams(-2, -1);
        localLayoutParams1.addRule(5);
        localButton1.setLayoutParams(localLayoutParams1);
        localButton1.setContentDescription("Back Button");
        localButton1.setId(2);
        localButton1.setText("<");
        2 local2 = new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            InAppBrowser.this.goBack();
          }
        };
        localButton1.setOnClickListener(local2);
        Button localButton2 = new Button(InAppBrowser.this.cordova.getActivity());
        RelativeLayout.LayoutParams localLayoutParams2 = new RelativeLayout.LayoutParams(-2, -1);
        localLayoutParams2.addRule(1, 2);
        localButton2.setLayoutParams(localLayoutParams2);
        localButton2.setContentDescription("Forward Button");
        localButton2.setId(3);
        localButton2.setText(">");
        3 local3 = new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            InAppBrowser.this.goForward();
          }
        };
        localButton2.setOnClickListener(local3);
        InAppBrowser.access$402(InAppBrowser.this, new EditText(InAppBrowser.this.cordova.getActivity()));
        RelativeLayout.LayoutParams localLayoutParams3 = new RelativeLayout.LayoutParams(-1, -1);
        localLayoutParams3.addRule(1, 1);
        localLayoutParams3.addRule(0, 5);
        InAppBrowser.this.edittext.setLayoutParams(localLayoutParams3);
        InAppBrowser.this.edittext.setId(4);
        InAppBrowser.this.edittext.setSingleLine(true);
        InAppBrowser.this.edittext.setText(this.val$url);
        InAppBrowser.this.edittext.setInputType(16);
        InAppBrowser.this.edittext.setImeOptions(2);
        InAppBrowser.this.edittext.setInputType(0);
        EditText localEditText = InAppBrowser.this.edittext;
        4 local4 = new View.OnKeyListener()
        {
          public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
          {
            if ((paramKeyEvent.getAction() == 0) && (paramInt == 66))
            {
              InAppBrowser.this.navigate(InAppBrowser.this.edittext.getText().toString());
              return true;
            }
            return false;
          }
        };
        localEditText.setOnKeyListener(local4);
        Button localButton3 = new Button(InAppBrowser.this.cordova.getActivity());
        RelativeLayout.LayoutParams localLayoutParams4 = new RelativeLayout.LayoutParams(-2, -1);
        localLayoutParams4.addRule(11);
        localButton3.setLayoutParams(localLayoutParams4);
        localButton2.setContentDescription("Close Button");
        localButton3.setId(5);
        localButton3.setText(InAppBrowser.this.buttonLabel);
        5 local5 = new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            InAppBrowser.this.closeDialog();
          }
        };
        localButton3.setOnClickListener(local5);
        InAppBrowser.access$802(InAppBrowser.this, new WebView(InAppBrowser.this.cordova.getActivity()));
        InAppBrowser.this.inAppWebView.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        InAppBrowser.this.inAppWebView.setWebChromeClient(new InAppBrowser.InAppChromeClient(InAppBrowser.this));
        InAppBrowser.InAppBrowserClient localInAppBrowserClient = new InAppBrowser.InAppBrowserClient(InAppBrowser.this, this.val$thatWebView, InAppBrowser.this.edittext);
        InAppBrowser.this.inAppWebView.setWebViewClient(localInAppBrowserClient);
        WebSettings localWebSettings = InAppBrowser.this.inAppWebView.getSettings();
        localWebSettings.setJavaScriptEnabled(true);
        localWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        localWebSettings.setBuiltInZoomControls(true);
        localWebSettings.setPluginsEnabled(true);
        Bundle localBundle = InAppBrowser.this.cordova.getActivity().getIntent().getExtras();
        if (localBundle == null);
        for (boolean bool = true; ; bool = localBundle.getBoolean("InAppBrowserStorageEnabled", true))
        {
          if (bool)
          {
            localWebSettings.setDatabasePath(InAppBrowser.this.cordova.getActivity().getApplicationContext().getDir("inAppBrowserDB", 0).getPath());
            localWebSettings.setDatabaseEnabled(true);
          }
          localWebSettings.setDomStorageEnabled(true);
          InAppBrowser.this.inAppWebView.loadUrl(this.val$url);
          InAppBrowser.this.inAppWebView.setId(6);
          InAppBrowser.this.inAppWebView.getSettings().setLoadWithOverviewMode(true);
          InAppBrowser.this.inAppWebView.getSettings().setUseWideViewPort(true);
          InAppBrowser.this.inAppWebView.requestFocus();
          InAppBrowser.this.inAppWebView.requestFocusFromTouch();
          localRelativeLayout2.addView(localButton1);
          localRelativeLayout2.addView(localButton2);
          localRelativeLayout1.addView(localRelativeLayout2);
          localRelativeLayout1.addView(InAppBrowser.this.edittext);
          localRelativeLayout1.addView(localButton3);
          if (InAppBrowser.this.getShowLocationBar())
            localLinearLayout.addView(localRelativeLayout1);
          localLinearLayout.addView(InAppBrowser.this.inAppWebView);
          WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
          localLayoutParams.copyFrom(InAppBrowser.this.dialog.getWindow().getAttributes());
          localLayoutParams.width = -1;
          localLayoutParams.height = -1;
          InAppBrowser.this.dialog.setContentView(localLinearLayout);
          InAppBrowser.this.dialog.show();
          InAppBrowser.this.dialog.getWindow().setAttributes(localLayoutParams);
          return;
        }
      }
    };
    this.cordova.getActivity().runOnUiThread(local1);
    return "";
  }

  public class InAppBrowserClient extends WebViewClient
  {
    EditText edittext;
    CordovaWebView webView;

    public InAppBrowserClient(CordovaWebView paramEditText, EditText arg3)
    {
      this.webView = paramEditText;
      Object localObject;
      this.edittext = localObject;
    }

    public void onPageFinished(WebView paramWebView, String paramString)
    {
      super.onPageFinished(paramWebView, paramString);
      try
      {
        JSONObject localJSONObject = new JSONObject();
        localJSONObject.put("type", "loadstop");
        localJSONObject.put("url", paramString);
        InAppBrowser.this.sendUpdate(localJSONObject, true);
        return;
      }
      catch (JSONException localJSONException)
      {
        Log.d("InAppBrowser", "Should never happen");
      }
    }

    public void onPageStarted(WebView paramWebView, String paramString, Bitmap paramBitmap)
    {
      super.onPageStarted(paramWebView, paramString, paramBitmap);
      String str1 = "";
      if ((paramString.startsWith("http:")) || (paramString.startsWith("https:")) || (paramString.startsWith("file:")))
        str1 = paramString;
      while (true)
      {
        if (!str1.equals(this.edittext.getText().toString()))
          this.edittext.setText(str1);
        try
        {
          JSONObject localJSONObject = new JSONObject();
          localJSONObject.put("type", "loadstart");
          localJSONObject.put("url", str1);
          InAppBrowser.this.sendUpdate(localJSONObject, true);
          return;
          if (paramString.startsWith("tel:"))
          {
            try
            {
              Intent localIntent3 = new Intent("android.intent.action.DIAL");
              localIntent3.setData(Uri.parse(paramString));
              InAppBrowser.this.cordova.getActivity().startActivity(localIntent3);
            }
            catch (ActivityNotFoundException localActivityNotFoundException3)
            {
              LOG.e("InAppBrowser", "Error dialing " + paramString + ": " + localActivityNotFoundException3.toString());
            }
            continue;
          }
          if ((paramString.startsWith("geo:")) || (paramString.startsWith("mailto:")) || (paramString.startsWith("market:")))
          {
            try
            {
              Intent localIntent1 = new Intent("android.intent.action.VIEW");
              localIntent1.setData(Uri.parse(paramString));
              InAppBrowser.this.cordova.getActivity().startActivity(localIntent1);
            }
            catch (ActivityNotFoundException localActivityNotFoundException1)
            {
              LOG.e("InAppBrowser", "Error with " + paramString + ": " + localActivityNotFoundException1.toString());
            }
            continue;
          }
          if (paramString.startsWith("sms:"))
            while (true)
            {
              Intent localIntent2;
              int i;
              try
              {
                localIntent2 = new Intent("android.intent.action.VIEW");
                i = paramString.indexOf('?');
                if (i != -1)
                  break label460;
                str2 = paramString.substring(4);
                localIntent2.setData(Uri.parse("sms:" + str2));
                localIntent2.putExtra("address", str2);
                localIntent2.setType("vnd.android-dir/mms-sms");
                InAppBrowser.this.cordova.getActivity().startActivity(localIntent2);
              }
              catch (ActivityNotFoundException localActivityNotFoundException2)
              {
                LOG.e("InAppBrowser", "Error sending sms " + paramString + ":" + localActivityNotFoundException2.toString());
              }
              break;
              label460: String str2 = paramString.substring(4, i);
              String str3 = Uri.parse(paramString).getQuery();
              if ((str3 == null) || (!str3.startsWith("body=")))
                continue;
              localIntent2.putExtra("sms_body", str3.substring(5));
            }
          str1 = "http://" + paramString;
        }
        catch (JSONException localJSONException)
        {
          Log.d("InAppBrowser", "Should never happen");
        }
      }
    }

    public void onReceivedError(WebView paramWebView, int paramInt, String paramString1, String paramString2)
    {
      super.onReceivedError(paramWebView, paramInt, paramString1, paramString2);
      try
      {
        JSONObject localJSONObject = new JSONObject();
        localJSONObject.put("type", "loaderror");
        localJSONObject.put("url", paramString2);
        localJSONObject.put("code", paramInt);
        localJSONObject.put("message", paramString1);
        InAppBrowser.this.sendUpdate(localJSONObject, true, PluginResult.Status.ERROR);
        return;
      }
      catch (JSONException localJSONException)
      {
        Log.d("InAppBrowser", "Should never happen");
      }
    }
  }

  public class InAppChromeClient extends WebChromeClient
  {
    public InAppChromeClient()
    {
    }

    public void onExceededDatabaseQuota(String paramString1, String paramString2, long paramLong1, long paramLong2, long paramLong3, WebStorage.QuotaUpdater paramQuotaUpdater)
    {
      Object[] arrayOfObject1 = new Object[3];
      arrayOfObject1[0] = Long.valueOf(paramLong2);
      arrayOfObject1[1] = Long.valueOf(paramLong1);
      arrayOfObject1[2] = Long.valueOf(paramLong3);
      LOG.d("InAppBrowser", "onExceededDatabaseQuota estimatedSize: %d  currentQuota: %d  totalUsedQuota: %d", arrayOfObject1);
      if (paramLong2 < InAppBrowser.this.MAX_QUOTA)
      {
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = Long.valueOf(paramLong2);
        LOG.d("InAppBrowser", "calling quotaUpdater.updateQuota newQuota: %d", arrayOfObject2);
        paramQuotaUpdater.updateQuota(paramLong2);
        return;
      }
      paramQuotaUpdater.updateQuota(paramLong1);
    }

    public void onGeolocationPermissionsShowPrompt(String paramString, GeolocationPermissions.Callback paramCallback)
    {
      super.onGeolocationPermissionsShowPrompt(paramString, paramCallback);
      paramCallback.invoke(paramString, true, false);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.InAppBrowser
 * JD-Core Version:    0.6.0
 */