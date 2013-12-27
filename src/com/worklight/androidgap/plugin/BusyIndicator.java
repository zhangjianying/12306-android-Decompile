package com.worklight.androidgap.plugin;

import android.app.Activity;
import android.app.ProgressDialog;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

public class BusyIndicator extends CordovaPlugin
{
  public static final String ACTION_HIDE = "hide";
  public static final String ACTION_SHOW = "show";
  private boolean isShowing = false;
  private ProgressDialog spinnerDialog = null;

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    try
    {
      if ("show".equals(paramString))
      {
        show(paramJSONArray.getString(0));
        paramCallbackContext.success("true");
        return true;
      }
      if ("hide".equals(paramString))
      {
        hide();
        paramCallbackContext.success("true");
        return true;
      }
    }
    catch (JSONException localJSONException)
    {
      paramCallbackContext.error("Action: " + paramString + " failed. JSON Error is: " + localJSONException.getLocalizedMessage());
      return true;
    }
    paramCallbackContext.error("Invalid action: " + paramString);
    return true;
  }

  public void hide()
  {
    if (this.spinnerDialog != null)
    {
      this.spinnerDialog.dismiss();
      this.spinnerDialog = null;
    }
    this.isShowing = false;
  }

  public boolean isShowing()
  {
    return this.isShowing;
  }

  public void show(String paramString)
  {
    if (this.spinnerDialog != null)
    {
      this.spinnerDialog.dismiss();
      this.spinnerDialog = null;
    }
    1 local1 = new Runnable(paramString)
    {
      public void run()
      {
        BusyIndicator.access$002(BusyIndicator.this, new ProgressDialog(BusyIndicator.this.cordova.getActivity()));
        BusyIndicator.this.spinnerDialog.setCancelable(false);
        BusyIndicator.this.spinnerDialog.setCanceledOnTouchOutside(false);
        BusyIndicator.this.spinnerDialog.setIndeterminate(true);
        BusyIndicator.this.spinnerDialog.setMessage(this.val$text + " ");
        BusyIndicator.this.spinnerDialog.show();
        BusyIndicator.access$102(BusyIndicator.this, true);
      }
    };
    this.cordova.getActivity().runOnUiThread(local1);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.BusyIndicator
 * JD-Core Version:    0.6.0
 */