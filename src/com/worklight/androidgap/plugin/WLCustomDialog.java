package com.worklight.androidgap.plugin;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.apache.cordova.Notification;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;

public class WLCustomDialog extends Notification
{
  public void confirm(String paramString1, String paramString2, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
  {
    monitorenter;
    try
    {
      1 local1 = new Runnable(this.cordova, paramString1, paramString2, paramJSONArray, paramCallbackContext)
      {
        public void run()
        {
          AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.val$cordova.getActivity());
          localBuilder.setMessage(this.val$message);
          localBuilder.setTitle(this.val$title);
          localBuilder.setCancelable(false);
          if (this.val$buttonLabels.length() > 0);
          try
          {
            localBuilder.setNegativeButton(this.val$buttonLabels.getString(0), new DialogInterface.OnClickListener()
            {
              public void onClick(DialogInterface paramDialogInterface, int paramInt)
              {
                paramDialogInterface.dismiss();
                WLCustomDialog.1.this.val$callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, 1));
              }
            });
            label72: if (this.val$buttonLabels.length() > 1);
            try
            {
              localBuilder.setNeutralButton(this.val$buttonLabels.getString(1), new DialogInterface.OnClickListener()
              {
                public void onClick(DialogInterface paramDialogInterface, int paramInt)
                {
                  paramDialogInterface.dismiss();
                  WLCustomDialog.1.this.val$callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, 2));
                }
              });
              label104: if (this.val$buttonLabels.length() > 2);
              try
              {
                localBuilder.setPositiveButton(this.val$buttonLabels.getString(2), new DialogInterface.OnClickListener()
                {
                  public void onClick(DialogInterface paramDialogInterface, int paramInt)
                  {
                    paramDialogInterface.dismiss();
                    WLCustomDialog.1.this.val$callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, 3));
                  }
                });
                label136: localBuilder.create();
                localBuilder.show();
                return;
              }
              catch (Exception localException1)
              {
                break label136;
              }
            }
            catch (Exception localException2)
            {
              break label104;
            }
          }
          catch (Exception localException3)
          {
            break label72;
          }
        }
      };
      this.cordova.getActivity().runOnUiThread(local1);
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.WLCustomDialog
 * JD-Core Version:    0.6.0
 */