package com.worklight.androidgap.plugin.storage;

import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;

public abstract interface ActionDispatcher
{
  public abstract PluginResult dispatch(CordovaInterface paramCordovaInterface, JSONArray paramJSONArray)
    throws Throwable;

  public abstract String getName();
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.ActionDispatcher
 * JD-Core Version:    0.6.0
 */