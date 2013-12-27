package org.apache.cordova;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.widget.EditText;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Notification extends CordovaPlugin
{
  public int confirmResult = -1;
  public ProgressDialog progressDialog = null;
  public ProgressDialog spinnerDialog = null;

  public void activityStart(String paramString1, String paramString2)
  {
    monitorenter;
    try
    {
      if (this.spinnerDialog != null)
      {
        this.spinnerDialog.dismiss();
        this.spinnerDialog = null;
      }
      4 local4 = new Runnable(this.cordova, paramString1, paramString2)
      {
        public void run()
        {
          Notification.this.spinnerDialog = ProgressDialog.show(this.val$cordova.getActivity(), this.val$title, this.val$message, true, true, new DialogInterface.OnCancelListener()
          {
            public void onCancel(DialogInterface paramDialogInterface)
            {
              Notification.this.spinnerDialog = null;
            }
          });
        }
      };
      this.cordova.getActivity().runOnUiThread(local4);
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void activityStop()
  {
    monitorenter;
    try
    {
      if (this.spinnerDialog != null)
      {
        this.spinnerDialog.dismiss();
        this.spinnerDialog = null;
      }
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

  public void alert(String paramString1, String paramString2, String paramString3, CallbackContext paramCallbackContext)
  {
    monitorenter;
    try
    {
      1 local1 = new Runnable(this.cordova, paramString1, paramString2, paramString3, paramCallbackContext)
      {
        public void run()
        {
          AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.val$cordova.getActivity());
          localBuilder.setMessage(this.val$message);
          localBuilder.setTitle(this.val$title);
          localBuilder.setCancelable(true);
          localBuilder.setPositiveButton(this.val$buttonLabel, new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              paramDialogInterface.dismiss();
              Notification.1.this.val$callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, 0));
            }
          });
          localBuilder.setOnCancelListener(new DialogInterface.OnCancelListener()
          {
            public void onCancel(DialogInterface paramDialogInterface)
            {
              paramDialogInterface.dismiss();
              Notification.1.this.val$callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, 0));
            }
          });
          localBuilder.create();
          localBuilder.show();
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

  public void beep(long paramLong)
  {
    Uri localUri = RingtoneManager.getDefaultUri(2);
    Ringtone localRingtone = RingtoneManager.getRingtone(this.cordova.getActivity().getBaseContext(), localUri);
    if (localRingtone != null)
    {
      long l1 = 0L;
      while (l1 < paramLong)
      {
        localRingtone.play();
        long l2 = 5000L;
        while ((localRingtone.isPlaying()) && (l2 > 0L))
        {
          l2 -= 100L;
          try
          {
            Thread.sleep(100L);
          }
          catch (InterruptedException localInterruptedException)
          {
          }
        }
        l1 += 1L;
      }
    }
  }

  public void confirm(String paramString1, String paramString2, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
  {
    monitorenter;
    try
    {
      2 local2 = new Runnable(this.cordova, paramString1, paramString2, paramJSONArray, paramCallbackContext)
      {
        public void run()
        {
          AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.val$cordova.getActivity());
          localBuilder.setMessage(this.val$message);
          localBuilder.setTitle(this.val$title);
          localBuilder.setCancelable(true);
          if (this.val$buttonLabels.length() > 0);
          try
          {
            localBuilder.setNegativeButton(this.val$buttonLabels.getString(0), new DialogInterface.OnClickListener()
            {
              public void onClick(DialogInterface paramDialogInterface, int paramInt)
              {
                paramDialogInterface.dismiss();
                Notification.2.this.val$callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, 1));
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
                  Notification.2.this.val$callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, 2));
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
                    Notification.2.this.val$callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, 3));
                  }
                });
                label136: localBuilder.setOnCancelListener(new DialogInterface.OnCancelListener()
                {
                  public void onCancel(DialogInterface paramDialogInterface)
                  {
                    paramDialogInterface.dismiss();
                    Notification.2.this.val$callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, 0));
                  }
                });
                localBuilder.create();
                localBuilder.show();
                return;
              }
              catch (JSONException localJSONException1)
              {
                break label136;
              }
            }
            catch (JSONException localJSONException2)
            {
              break label104;
            }
          }
          catch (JSONException localJSONException3)
          {
            break label72;
          }
        }
      };
      this.cordova.getActivity().runOnUiThread(local2);
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

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    if (paramString.equals("beep"))
      beep(paramJSONArray.getLong(0));
    while (true)
    {
      paramCallbackContext.success();
      return true;
      if (paramString.equals("vibrate"))
      {
        vibrate(paramJSONArray.getLong(0));
        continue;
      }
      if (paramString.equals("alert"))
      {
        alert(paramJSONArray.getString(0), paramJSONArray.getString(1), paramJSONArray.getString(2), paramCallbackContext);
        return true;
      }
      if (paramString.equals("confirm"))
      {
        confirm(paramJSONArray.getString(0), paramJSONArray.getString(1), paramJSONArray.getJSONArray(2), paramCallbackContext);
        return true;
      }
      if (paramString.equals("prompt"))
      {
        prompt(paramJSONArray.getString(0), paramJSONArray.getString(1), paramJSONArray.getJSONArray(2), paramCallbackContext);
        return true;
      }
      if (paramString.equals("activityStart"))
      {
        activityStart(paramJSONArray.getString(0), paramJSONArray.getString(1));
        continue;
      }
      if (paramString.equals("activityStop"))
      {
        activityStop();
        continue;
      }
      if (paramString.equals("progressStart"))
      {
        progressStart(paramJSONArray.getString(0), paramJSONArray.getString(1));
        continue;
      }
      if (paramString.equals("progressValue"))
      {
        progressValue(paramJSONArray.getInt(0));
        continue;
      }
      if (!paramString.equals("progressStop"))
        break;
      progressStop();
    }
    return false;
  }

  public void progressStart(String paramString1, String paramString2)
  {
    monitorenter;
    try
    {
      if (this.progressDialog != null)
      {
        this.progressDialog.dismiss();
        this.progressDialog = null;
      }
      5 local5 = new Runnable(this, this.cordova, paramString1, paramString2)
      {
        public void run()
        {
          this.val$notification.progressDialog = new ProgressDialog(this.val$cordova.getActivity());
          this.val$notification.progressDialog.setProgressStyle(1);
          this.val$notification.progressDialog.setTitle(this.val$title);
          this.val$notification.progressDialog.setMessage(this.val$message);
          this.val$notification.progressDialog.setCancelable(true);
          this.val$notification.progressDialog.setMax(100);
          this.val$notification.progressDialog.setProgress(0);
          this.val$notification.progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
          {
            public void onCancel(DialogInterface paramDialogInterface)
            {
              Notification.5.this.val$notification.progressDialog = null;
            }
          });
          this.val$notification.progressDialog.show();
        }
      };
      this.cordova.getActivity().runOnUiThread(local5);
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void progressStop()
  {
    monitorenter;
    try
    {
      if (this.progressDialog != null)
      {
        this.progressDialog.dismiss();
        this.progressDialog = null;
      }
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

  public void progressValue(int paramInt)
  {
    monitorenter;
    try
    {
      if (this.progressDialog != null)
        this.progressDialog.setProgress(paramInt);
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

  public void prompt(String paramString1, String paramString2, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
  {
    monitorenter;
    try
    {
      CordovaInterface localCordovaInterface = this.cordova;
      3 local3 = new Runnable(localCordovaInterface, paramString1, paramString2, new EditText(localCordovaInterface.getActivity()), paramJSONArray, paramCallbackContext)
      {
        public void run()
        {
          AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.val$cordova.getActivity());
          localBuilder.setMessage(this.val$message);
          localBuilder.setTitle(this.val$title);
          localBuilder.setCancelable(true);
          localBuilder.setView(this.val$promptInput);
          JSONObject localJSONObject = new JSONObject();
          if (this.val$buttonLabels.length() > 0);
          try
          {
            localBuilder.setNegativeButton(this.val$buttonLabels.getString(0), new DialogInterface.OnClickListener(localJSONObject)
            {
              public void onClick(DialogInterface paramDialogInterface, int paramInt)
              {
                paramDialogInterface.dismiss();
                try
                {
                  this.val$result.put("buttonIndex", 1);
                  this.val$result.put("input1", Notification.3.this.val$promptInput.getText());
                  Notification.3.this.val$callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, this.val$result));
                  return;
                }
                catch (JSONException localJSONException)
                {
                  while (true)
                    localJSONException.printStackTrace();
                }
              }
            });
            label92: if (this.val$buttonLabels.length() > 1);
            try
            {
              localBuilder.setNeutralButton(this.val$buttonLabels.getString(1), new DialogInterface.OnClickListener(localJSONObject)
              {
                public void onClick(DialogInterface paramDialogInterface, int paramInt)
                {
                  paramDialogInterface.dismiss();
                  try
                  {
                    this.val$result.put("buttonIndex", 2);
                    this.val$result.put("input1", Notification.3.this.val$promptInput.getText());
                    Notification.3.this.val$callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, this.val$result));
                    return;
                  }
                  catch (JSONException localJSONException)
                  {
                    while (true)
                      localJSONException.printStackTrace();
                  }
                }
              });
              label126: if (this.val$buttonLabels.length() > 2);
              try
              {
                localBuilder.setPositiveButton(this.val$buttonLabels.getString(2), new DialogInterface.OnClickListener(localJSONObject)
                {
                  public void onClick(DialogInterface paramDialogInterface, int paramInt)
                  {
                    paramDialogInterface.dismiss();
                    try
                    {
                      this.val$result.put("buttonIndex", 3);
                      this.val$result.put("input1", Notification.3.this.val$promptInput.getText());
                      Notification.3.this.val$callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, this.val$result));
                      return;
                    }
                    catch (JSONException localJSONException)
                    {
                      while (true)
                        localJSONException.printStackTrace();
                    }
                  }
                });
                label160: localBuilder.setOnCancelListener(new DialogInterface.OnCancelListener(localJSONObject)
                {
                  public void onCancel(DialogInterface paramDialogInterface)
                  {
                    paramDialogInterface.dismiss();
                    try
                    {
                      this.val$result.put("buttonIndex", 0);
                      this.val$result.put("input1", Notification.3.this.val$promptInput.getText());
                      Notification.3.this.val$callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, this.val$result));
                      return;
                    }
                    catch (JSONException localJSONException)
                    {
                      while (true)
                        localJSONException.printStackTrace();
                    }
                  }
                });
                localBuilder.create();
                localBuilder.show();
                return;
              }
              catch (JSONException localJSONException1)
              {
                break label160;
              }
            }
            catch (JSONException localJSONException2)
            {
              break label126;
            }
          }
          catch (JSONException localJSONException3)
          {
            break label92;
          }
        }
      };
      this.cordova.getActivity().runOnUiThread(local3);
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

  public void vibrate(long paramLong)
  {
    if (paramLong == 0L)
      paramLong = 500L;
    ((Vibrator)this.cordova.getActivity().getSystemService("vibrator")).vibrate(paramLong);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.Notification
 * JD-Core Version:    0.6.0
 */