package org.apache.cordova.api;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;
import java.util.concurrent.ExecutorService;

@Deprecated
public class LegacyContext
  implements CordovaInterface
{
  private static final String LOG_TAG = "Deprecation Notice";
  private CordovaInterface cordova;

  public LegacyContext(CordovaInterface paramCordovaInterface)
  {
    this.cordova = paramCordovaInterface;
  }

  @Deprecated
  public boolean bindService(Intent paramIntent, ServiceConnection paramServiceConnection, int paramInt)
  {
    Log.i("Deprecation Notice", "Replace ctx.bindService() with cordova.getActivity().bindService()");
    return this.cordova.getActivity().bindService(paramIntent, paramServiceConnection, paramInt);
  }

  @Deprecated
  public void cancelLoadUrl()
  {
    Log.i("Deprecation Notice", "Replace ctx.cancelLoadUrl() with cordova.cancelLoadUrl()");
  }

  @Deprecated
  public Activity getActivity()
  {
    Log.i("Deprecation Notice", "Replace ctx.getActivity() with cordova.getActivity()");
    return this.cordova.getActivity();
  }

  @Deprecated
  public Context getApplicationContext()
  {
    Log.i("Deprecation Notice", "Replace ctx.getApplicationContext() with cordova.getActivity().getApplicationContext()");
    return this.cordova.getActivity().getApplicationContext();
  }

  @Deprecated
  public AssetManager getAssets()
  {
    Log.i("Deprecation Notice", "Replace ctx.getAssets() with cordova.getActivity().getAssets()");
    return this.cordova.getActivity().getAssets();
  }

  @Deprecated
  public Context getContext()
  {
    Log.i("Deprecation Notice", "Replace ctx.getContext() with cordova.getContext()");
    return this.cordova.getActivity();
  }

  @Deprecated
  public PackageManager getPackageManager()
  {
    Log.i("Deprecation Notice", "Replace ctx.getPackageManager() with cordova.getActivity().getPackageManager()");
    return this.cordova.getActivity().getPackageManager();
  }

  @Deprecated
  public Resources getResources()
  {
    Log.i("Deprecation Notice", "Replace ctx.getResources() with cordova.getActivity().getResources()");
    return this.cordova.getActivity().getResources();
  }

  @Deprecated
  public SharedPreferences getSharedPreferences(String paramString, int paramInt)
  {
    Log.i("Deprecation Notice", "Replace ctx.getSharedPreferences() with cordova.getActivity().getSharedPreferences()");
    return this.cordova.getActivity().getSharedPreferences(paramString, paramInt);
  }

  @Deprecated
  public Object getSystemService(String paramString)
  {
    Log.i("Deprecation Notice", "Replace ctx.getSystemService() with cordova.getActivity().getSystemService()");
    return this.cordova.getActivity().getSystemService(paramString);
  }

  public ExecutorService getThreadPool()
  {
    Log.i("Deprecation Notice", "Replace ctx.getThreadPool() with cordova.getThreadPool()");
    return this.cordova.getThreadPool();
  }

  @Deprecated
  public Object onMessage(String paramString, Object paramObject)
  {
    Log.i("Deprecation Notice", "Replace ctx.onMessage() with cordova.onMessage()");
    return this.cordova.onMessage(paramString, paramObject);
  }

  @Deprecated
  public void runOnUiThread(Runnable paramRunnable)
  {
    Log.i("Deprecation Notice", "Replace ctx.runOnUiThread() with cordova.getActivity().runOnUiThread()");
    this.cordova.getActivity().runOnUiThread(paramRunnable);
  }

  @Deprecated
  public void setActivityResultCallback(CordovaPlugin paramCordovaPlugin)
  {
    Log.i("Deprecation Notice", "Replace ctx.setActivityResultCallback() with cordova.setActivityResultCallback()");
    this.cordova.setActivityResultCallback(paramCordovaPlugin);
  }

  @Deprecated
  public void startActivity(Intent paramIntent)
  {
    Log.i("Deprecation Notice", "Replace ctx.startActivity() with cordova.getActivity().startActivity()");
    this.cordova.getActivity().startActivity(paramIntent);
  }

  @Deprecated
  public void startActivityForResult(CordovaPlugin paramCordovaPlugin, Intent paramIntent, int paramInt)
  {
    Log.i("Deprecation Notice", "Replace ctx.startActivityForResult() with cordova.startActivityForResult()");
    this.cordova.startActivityForResult(paramCordovaPlugin, paramIntent, paramInt);
  }

  @Deprecated
  public ComponentName startService(Intent paramIntent)
  {
    Log.i("Deprecation Notice", "Replace ctx.startService() with cordova.getActivity().startService()");
    return this.cordova.getActivity().startService(paramIntent);
  }

  @Deprecated
  public void unbindService(ServiceConnection paramServiceConnection)
  {
    Log.i("Deprecation Notice", "Replace ctx.unbindService() with cordova.getActivity().unbindService()");
    this.cordova.getActivity().unbindService(paramServiceConnection);
  }

  @Deprecated
  public void unregisterReceiver(BroadcastReceiver paramBroadcastReceiver)
  {
    Log.i("Deprecation Notice", "Replace ctx.unregisterReceiver() with cordova.getActivity().unregisterReceiver()");
    this.cordova.getActivity().unregisterReceiver(paramBroadcastReceiver);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.api.LegacyContext
 * JD-Core Version:    0.6.0
 */