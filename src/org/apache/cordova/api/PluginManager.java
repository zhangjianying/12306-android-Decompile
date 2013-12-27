package org.apache.cordova.api;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.webkit.WebResourceResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

public class PluginManager
{
  private static String TAG = "PluginManager";
  private final CordovaWebView app;
  private final CordovaInterface ctx;
  private final HashMap<String, PluginEntry> entries = new HashMap();
  private boolean firstRun;
  protected HashMap<String, String> urlMap = new HashMap();

  public PluginManager(CordovaWebView paramCordovaWebView, CordovaInterface paramCordovaInterface)
  {
    this.ctx = paramCordovaInterface;
    this.app = paramCordovaWebView;
    this.firstRun = true;
  }

  private void pluginConfigurationMissing()
  {
    LOG.e(TAG, "=====================================================================================");
    LOG.e(TAG, "ERROR: plugin.xml is missing.  Add res/xml/plugins.xml to your project.");
    LOG.e(TAG, "https://git-wip-us.apache.org/repos/asf?p=incubator-cordova-android.git;a=blob;f=framework/res/xml/plugins.xml");
    LOG.e(TAG, "=====================================================================================");
  }

  public void addService(String paramString1, String paramString2)
  {
    addService(new PluginEntry(paramString1, paramString2, false));
  }

  public void addService(PluginEntry paramPluginEntry)
  {
    this.entries.put(paramPluginEntry.service, paramPluginEntry);
  }

  public void clearPluginObjects()
  {
    Iterator localIterator = this.entries.values().iterator();
    while (localIterator.hasNext())
      ((PluginEntry)localIterator.next()).plugin = null;
  }

  public boolean exec(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    CordovaPlugin localCordovaPlugin = getPlugin(paramString1);
    if (localCordovaPlugin == null)
    {
      Log.d(TAG, "exec() call to unknown plugin: " + paramString1);
      PluginResult localPluginResult3 = new PluginResult(PluginResult.Status.CLASS_NOT_FOUND_EXCEPTION);
      this.app.sendPluginResult(localPluginResult3, paramString3);
      return true;
    }
    CallbackContext localCallbackContext;
    try
    {
      localCallbackContext = new CallbackContext(paramString3, this.app);
      if (!localCordovaPlugin.execute(paramString2, paramString4, localCallbackContext))
      {
        PluginResult localPluginResult2 = new PluginResult(PluginResult.Status.INVALID_ACTION);
        this.app.sendPluginResult(localPluginResult2, paramString3);
        return true;
      }
    }
    catch (JSONException localJSONException)
    {
      PluginResult localPluginResult1 = new PluginResult(PluginResult.Status.JSON_EXCEPTION);
      this.app.sendPluginResult(localPluginResult1, paramString3);
      return true;
    }
    boolean bool = localCallbackContext.isFinished();
    return bool;
  }

  @Deprecated
  public boolean exec(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean)
  {
    return exec(paramString1, paramString2, paramString3, paramString4);
  }

  public CordovaPlugin getPlugin(String paramString)
  {
    PluginEntry localPluginEntry = (PluginEntry)this.entries.get(paramString);
    CordovaPlugin localCordovaPlugin;
    if (localPluginEntry == null)
      localCordovaPlugin = null;
    do
    {
      return localCordovaPlugin;
      localCordovaPlugin = localPluginEntry.plugin;
    }
    while (localCordovaPlugin != null);
    return localPluginEntry.createPlugin(this.app, this.ctx);
  }

  public void init()
  {
    LOG.d(TAG, "init()");
    if (this.firstRun)
    {
      loadPlugins();
      this.firstRun = false;
    }
    while (true)
    {
      startupPlugins();
      return;
      onPause(false);
      onDestroy();
      clearPluginObjects();
    }
  }

  public void loadPlugins()
  {
    int i = this.ctx.getActivity().getResources().getIdentifier("config", "xml", this.ctx.getActivity().getPackageName());
    if (i == 0)
    {
      i = this.ctx.getActivity().getResources().getIdentifier("plugins", "xml", this.ctx.getActivity().getPackageName());
      LOG.i(TAG, "Using plugins.xml instead of config.xml.  plugins.xml will eventually be deprecated");
    }
    if (i == 0)
      pluginConfigurationMissing();
    while (true)
    {
      return;
      XmlResourceParser localXmlResourceParser = this.ctx.getActivity().getResources().getXml(i);
      int j = -1;
      String str1 = "";
      String str2 = "";
      int k = 0;
      while (j != 1)
      {
        String str3;
        if (j == 2)
        {
          str3 = localXmlResourceParser.getName();
          if (str3.equals("plugin"))
          {
            str1 = localXmlResourceParser.getAttributeValue(null, "name");
            str2 = localXmlResourceParser.getAttributeValue(null, "value");
            addService(new PluginEntry(str1, str2, "true".equals(localXmlResourceParser.getAttributeValue(null, "onload"))));
          }
        }
        try
        {
          while (true)
          {
            int m = localXmlResourceParser.next();
            j = m;
            break;
            if (str3.equals("url-filter"))
            {
              this.urlMap.put(localXmlResourceParser.getAttributeValue(null, "value"), str1);
              continue;
            }
            if (str3.equals("feature"))
            {
              k = 1;
              localXmlResourceParser.getAttributeValue(null, "name");
              continue;
            }
            if ((!str3.equals("param")) || (k == 0))
              continue;
            String str4 = localXmlResourceParser.getAttributeValue(null, "name");
            if (str4.equals("service"))
              str1 = localXmlResourceParser.getAttributeValue(null, "value");
            while ((str1.length() > 0) && (str2.length() > 0))
            {
              addService(new PluginEntry(str1, str2, "true".equals(localXmlResourceParser.getAttributeValue(null, "onload"))));
              str1 = "";
              str2 = "";
              break;
              if (!str4.equals("package"))
                continue;
              str2 = localXmlResourceParser.getAttributeValue(null, "value");
            }
            if ((j != 3) || (!localXmlResourceParser.getName().equals("feature")))
              continue;
            str1 = "";
            str2 = "";
            k = 0;
          }
        }
        catch (XmlPullParserException localXmlPullParserException)
        {
          localXmlPullParserException.printStackTrace();
        }
        catch (IOException localIOException)
        {
          localIOException.printStackTrace();
        }
      }
    }
  }

  public void onDestroy()
  {
    Iterator localIterator = this.entries.values().iterator();
    while (localIterator.hasNext())
    {
      PluginEntry localPluginEntry = (PluginEntry)localIterator.next();
      if (localPluginEntry.plugin == null)
        continue;
      localPluginEntry.plugin.onDestroy();
    }
  }

  public void onNewIntent(Intent paramIntent)
  {
    Iterator localIterator = this.entries.values().iterator();
    while (localIterator.hasNext())
    {
      PluginEntry localPluginEntry = (PluginEntry)localIterator.next();
      if (localPluginEntry.plugin == null)
        continue;
      localPluginEntry.plugin.onNewIntent(paramIntent);
    }
  }

  public boolean onOverrideUrlLoading(String paramString)
  {
    Iterator localIterator = this.urlMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      if (paramString.startsWith((String)localEntry.getKey()))
        return getPlugin((String)localEntry.getValue()).onOverrideUrlLoading(paramString);
    }
    return false;
  }

  public void onPause(boolean paramBoolean)
  {
    Iterator localIterator = this.entries.values().iterator();
    while (localIterator.hasNext())
    {
      PluginEntry localPluginEntry = (PluginEntry)localIterator.next();
      if (localPluginEntry.plugin == null)
        continue;
      localPluginEntry.plugin.onPause(paramBoolean);
    }
  }

  public void onReset()
  {
    Iterator localIterator = this.entries.values().iterator();
    while (localIterator.hasNext())
    {
      CordovaPlugin localCordovaPlugin = ((PluginEntry)localIterator.next()).plugin;
      if (localCordovaPlugin == null)
        continue;
      localCordovaPlugin.onReset();
    }
  }

  public void onResume(boolean paramBoolean)
  {
    Iterator localIterator = this.entries.values().iterator();
    while (localIterator.hasNext())
    {
      PluginEntry localPluginEntry = (PluginEntry)localIterator.next();
      if (localPluginEntry.plugin == null)
        continue;
      localPluginEntry.plugin.onResume(paramBoolean);
    }
  }

  public Object postMessage(String paramString, Object paramObject)
  {
    Object localObject1 = this.ctx.onMessage(paramString, paramObject);
    if (localObject1 != null)
      return localObject1;
    Iterator localIterator = this.entries.values().iterator();
    while (localIterator.hasNext())
    {
      PluginEntry localPluginEntry = (PluginEntry)localIterator.next();
      if (localPluginEntry.plugin == null)
        continue;
      Object localObject2 = localPluginEntry.plugin.onMessage(paramString, paramObject);
      if (localObject2 != null)
        return localObject2;
    }
    return null;
  }

  public WebResourceResponse shouldInterceptRequest(String paramString)
  {
    Iterator localIterator = this.urlMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      if (paramString.startsWith((String)localEntry.getKey()))
        return getPlugin((String)localEntry.getValue()).shouldInterceptRequest(paramString);
    }
    return null;
  }

  public void startupPlugins()
  {
    Iterator localIterator = this.entries.values().iterator();
    while (localIterator.hasNext())
    {
      PluginEntry localPluginEntry = (PluginEntry)localIterator.next();
      if (!localPluginEntry.onload)
        continue;
      localPluginEntry.createPlugin(this.app, this.ctx);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.api.PluginManager
 * JD-Core Version:    0.6.0
 */