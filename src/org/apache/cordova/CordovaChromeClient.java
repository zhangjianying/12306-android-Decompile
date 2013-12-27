package org.apache.cordova;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.LOG;
import org.json.JSONArray;
import org.json.JSONException;

public class CordovaChromeClient extends WebChromeClient
{
  public static final int FILECHOOSER_RESULTCODE = 5173;
  private static final String LOG_TAG = "CordovaChromeClient";
  private long MAX_QUOTA = 104857600L;
  private String TAG = "CordovaLog";
  private CordovaWebView appView;
  private CordovaInterface cordova;
  public ValueCallback<Uri> mUploadMessage;
  private View mVideoProgressView;

  public CordovaChromeClient(CordovaInterface paramCordovaInterface)
  {
    this.cordova = paramCordovaInterface;
  }

  public CordovaChromeClient(CordovaInterface paramCordovaInterface, CordovaWebView paramCordovaWebView)
  {
    this.cordova = paramCordovaInterface;
    this.appView = paramCordovaWebView;
  }

  public ValueCallback<Uri> getValueCallback()
  {
    return this.mUploadMessage;
  }

  public View getVideoLoadingProgressView()
  {
    if (this.mVideoProgressView == null)
    {
      LinearLayout localLinearLayout = new LinearLayout(this.appView.getContext());
      localLinearLayout.setOrientation(1);
      RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
      localLayoutParams.addRule(13);
      localLinearLayout.setLayoutParams(localLayoutParams);
      ProgressBar localProgressBar = new ProgressBar(this.appView.getContext());
      LinearLayout.LayoutParams localLayoutParams1 = new LinearLayout.LayoutParams(-2, -2);
      localLayoutParams1.gravity = 17;
      localProgressBar.setLayoutParams(localLayoutParams1);
      localLinearLayout.addView(localProgressBar);
      this.mVideoProgressView = localLinearLayout;
    }
    return this.mVideoProgressView;
  }

  public void onConsoleMessage(String paramString1, int paramInt, String paramString2)
  {
    if (Build.VERSION.SDK_INT == 7)
    {
      String str = this.TAG;
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = paramString2;
      arrayOfObject[1] = Integer.valueOf(paramInt);
      arrayOfObject[2] = paramString1;
      LOG.d(str, "%s: Line %d : %s", arrayOfObject);
      super.onConsoleMessage(paramString1, paramInt, paramString2);
    }
  }

  @TargetApi(8)
  public boolean onConsoleMessage(ConsoleMessage paramConsoleMessage)
  {
    if (paramConsoleMessage.message() != null)
      LOG.d(this.TAG, paramConsoleMessage.message());
    return super.onConsoleMessage(paramConsoleMessage);
  }

  public void onExceededDatabaseQuota(String paramString1, String paramString2, long paramLong1, long paramLong2, long paramLong3, WebStorage.QuotaUpdater paramQuotaUpdater)
  {
    String str1 = this.TAG;
    Object[] arrayOfObject1 = new Object[3];
    arrayOfObject1[0] = Long.valueOf(paramLong2);
    arrayOfObject1[1] = Long.valueOf(paramLong1);
    arrayOfObject1[2] = Long.valueOf(paramLong3);
    LOG.d(str1, "DroidGap:  onExceededDatabaseQuota estimatedSize: %d  currentQuota: %d  totalUsedQuota: %d", arrayOfObject1);
    if (paramLong2 < this.MAX_QUOTA)
    {
      String str2 = this.TAG;
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = Long.valueOf(paramLong2);
      LOG.d(str2, "calling quotaUpdater.updateQuota newQuota: %d", arrayOfObject2);
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

  public void onHideCustomView()
  {
    this.appView.hideCustomView();
  }

  public boolean onJsAlert(WebView paramWebView, String paramString1, String paramString2, JsResult paramJsResult)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.cordova.getActivity());
    localBuilder.setMessage(paramString2);
    localBuilder.setTitle("Alert");
    localBuilder.setCancelable(true);
    localBuilder.setPositiveButton(17039370, new DialogInterface.OnClickListener(paramJsResult)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        this.val$result.confirm();
      }
    });
    localBuilder.setOnCancelListener(new DialogInterface.OnCancelListener(paramJsResult)
    {
      public void onCancel(DialogInterface paramDialogInterface)
      {
        this.val$result.cancel();
      }
    });
    localBuilder.setOnKeyListener(new DialogInterface.OnKeyListener(paramJsResult)
    {
      public boolean onKey(DialogInterface paramDialogInterface, int paramInt, KeyEvent paramKeyEvent)
      {
        if (paramInt == 4)
        {
          this.val$result.confirm();
          return false;
        }
        return true;
      }
    });
    localBuilder.create();
    localBuilder.show();
    return true;
  }

  public boolean onJsConfirm(WebView paramWebView, String paramString1, String paramString2, JsResult paramJsResult)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.cordova.getActivity());
    localBuilder.setMessage(paramString2);
    localBuilder.setTitle("Confirm");
    localBuilder.setCancelable(true);
    localBuilder.setPositiveButton(17039370, new DialogInterface.OnClickListener(paramJsResult)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        this.val$result.confirm();
      }
    });
    localBuilder.setNegativeButton(17039360, new DialogInterface.OnClickListener(paramJsResult)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        this.val$result.cancel();
      }
    });
    localBuilder.setOnCancelListener(new DialogInterface.OnCancelListener(paramJsResult)
    {
      public void onCancel(DialogInterface paramDialogInterface)
      {
        this.val$result.cancel();
      }
    });
    localBuilder.setOnKeyListener(new DialogInterface.OnKeyListener(paramJsResult)
    {
      public boolean onKey(DialogInterface paramDialogInterface, int paramInt, KeyEvent paramKeyEvent)
      {
        if (paramInt == 4)
        {
          this.val$result.cancel();
          return false;
        }
        return true;
      }
    });
    localBuilder.create();
    localBuilder.show();
    return true;
  }

  public boolean onJsPrompt(WebView paramWebView, String paramString1, String paramString2, String paramString3, JsPromptResult paramJsPromptResult)
  {
    int i;
    if (!paramString1.startsWith("file://"))
    {
      boolean bool = Config.isUrlWhiteListed(paramString1);
      i = 0;
      if (!bool);
    }
    else
    {
      i = 1;
    }
    if ((i != 0) && (paramString3 != null) && (paramString3.length() > 3) && (paramString3.substring(0, 4).equals("gap:")));
    while (true)
    {
      try
      {
        JSONArray localJSONArray = new JSONArray(paramString3.substring(4));
        String str2 = localJSONArray.getString(0);
        String str3 = localJSONArray.getString(1);
        String str4 = localJSONArray.getString(2);
        String str5 = this.appView.exposedJsApi.exec(str2, str3, str4, paramString2);
        if (str5 != null)
          continue;
        str5 = "";
        paramJsPromptResult.confirm(str5);
        return true;
      }
      catch (JSONException localJSONException)
      {
        localJSONException.printStackTrace();
        continue;
      }
      if ((i != 0) && (paramString3 != null) && (paramString3.equals("gap_bridge_mode:")))
      {
        this.appView.exposedJsApi.setNativeToJsBridgeMode(Integer.parseInt(paramString2));
        paramJsPromptResult.confirm("");
        continue;
      }
      if ((i != 0) && (paramString3 != null) && (paramString3.equals("gap_poll:")))
      {
        String str1 = this.appView.exposedJsApi.retrieveJsMessages();
        if (str1 == null)
          str1 = "";
        paramJsPromptResult.confirm(str1);
        continue;
      }
      if ((paramString3 != null) && (paramString3.equals("gap_init:")))
      {
        paramJsPromptResult.confirm("OK");
        continue;
      }
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.cordova.getActivity());
      localBuilder.setMessage(paramString2);
      EditText localEditText = new EditText(this.cordova.getActivity());
      if (paramString3 != null)
        localEditText.setText(paramString3);
      localBuilder.setView(localEditText);
      localBuilder.setCancelable(false);
      localBuilder.setPositiveButton(17039370, new DialogInterface.OnClickListener(localEditText, paramJsPromptResult)
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          String str = this.val$input.getText().toString();
          this.val$res.confirm(str);
        }
      });
      localBuilder.setNegativeButton(17039360, new DialogInterface.OnClickListener(paramJsPromptResult)
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          this.val$res.cancel();
        }
      });
      localBuilder.create();
      localBuilder.show();
    }
  }

  public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
  {
    this.appView.showCustomView(paramView, paramCustomViewCallback);
  }

  public void openFileChooser(ValueCallback<Uri> paramValueCallback)
  {
    openFileChooser(paramValueCallback, "*/*");
  }

  public void openFileChooser(ValueCallback<Uri> paramValueCallback, String paramString)
  {
    openFileChooser(paramValueCallback, paramString, null);
  }

  public void openFileChooser(ValueCallback<Uri> paramValueCallback, String paramString1, String paramString2)
  {
    this.mUploadMessage = paramValueCallback;
    Intent localIntent = new Intent("android.intent.action.GET_CONTENT");
    localIntent.addCategory("android.intent.category.OPENABLE");
    localIntent.setType("*/*");
    this.cordova.getActivity().startActivityForResult(Intent.createChooser(localIntent, "File Browser"), 5173);
  }

  public void setWebView(CordovaWebView paramCordovaWebView)
  {
    this.appView = paramCordovaWebView;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.CordovaChromeClient
 * JD-Core Version:    0.6.0
 */