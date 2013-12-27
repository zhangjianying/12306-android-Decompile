package org.apache.cordova.api;

import java.io.PrintStream;
import org.apache.cordova.CordovaWebView;

public class PluginEntry
{
  public boolean onload = false;
  public CordovaPlugin plugin = null;
  public String pluginClass = "";
  public String service = "";

  public PluginEntry(String paramString1, String paramString2, boolean paramBoolean)
  {
    this.service = paramString1;
    this.pluginClass = paramString2;
    this.onload = paramBoolean;
  }

  private Class getClassByName(String paramString)
    throws ClassNotFoundException
  {
    Class localClass = null;
    if (paramString != null)
      localClass = Class.forName(paramString);
    return localClass;
  }

  private boolean isCordovaPlugin(Class paramClass)
  {
    if (paramClass != null)
      return CordovaPlugin.class.isAssignableFrom(paramClass);
    return false;
  }

  public CordovaPlugin createPlugin(CordovaWebView paramCordovaWebView, CordovaInterface paramCordovaInterface)
  {
    if (this.plugin != null)
      return this.plugin;
    try
    {
      Class localClass = getClassByName(this.pluginClass);
      if (isCordovaPlugin(localClass))
      {
        this.plugin = ((CordovaPlugin)localClass.newInstance());
        this.plugin.initialize(paramCordovaInterface, paramCordovaWebView);
        CordovaPlugin localCordovaPlugin = this.plugin;
        return localCordovaPlugin;
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      System.out.println("Error adding plugin " + this.pluginClass + ".");
    }
    return null;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.api.PluginEntry
 * JD-Core Version:    0.6.0
 */