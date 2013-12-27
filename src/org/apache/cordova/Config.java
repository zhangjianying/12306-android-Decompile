package org.apache.cordova;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.cordova.api.LOG;
import org.xmlpull.v1.XmlPullParserException;

public class Config
{
  public static final String TAG = "Config";
  private static Config self = null;
  private String startUrl;
  private ArrayList<Pattern> whiteList = new ArrayList();
  private HashMap<String, Boolean> whiteListCache = new HashMap();

  private Config()
  {
  }

  private Config(Activity paramActivity)
  {
    if (paramActivity == null)
      LOG.i("CordovaLog", "There is no activity. Is this on the lock screen?");
    while (true)
    {
      return;
      int i = paramActivity.getResources().getIdentifier("config", "xml", paramActivity.getPackageName());
      if (i == 0)
      {
        i = paramActivity.getResources().getIdentifier("cordova", "xml", paramActivity.getPackageName());
        LOG.i("CordovaLog", "config.xml missing, reverting to cordova.xml");
      }
      if (i == 0)
      {
        LOG.i("CordovaLog", "cordova.xml missing. Ignoring...");
        return;
      }
      XmlResourceParser localXmlResourceParser = paramActivity.getResources().getXml(i);
      int j = -1;
      while (j != 1)
      {
        String str1;
        boolean bool4;
        if (j == 2)
        {
          str1 = localXmlResourceParser.getName();
          if (!str1.equals("access"))
            break label209;
          String str7 = localXmlResourceParser.getAttributeValue(null, "origin");
          String str8 = localXmlResourceParser.getAttributeValue(null, "subdomains");
          if (str7 != null)
          {
            if ((str8 == null) || (str8.compareToIgnoreCase("true") != 0))
              break label203;
            bool4 = true;
            label180: _addWhiteListEntry(str7, bool4);
          }
        }
        try
        {
          while (true)
          {
            int k = localXmlResourceParser.next();
            j = k;
            break;
            label203: bool4 = false;
            break label180;
            label209: if (str1.equals("log"))
            {
              String str6 = localXmlResourceParser.getAttributeValue(null, "level");
              LOG.i("CordovaLog", "Found log level %s", new Object[] { str6 });
              if (str6 == null)
                continue;
              LOG.setLogLevel(str6);
              continue;
            }
            if (str1.equals("preference"))
            {
              String str3 = localXmlResourceParser.getAttributeValue(null, "name");
              if (str3.equals("splashscreen"))
              {
                String str5 = localXmlResourceParser.getAttributeValue(null, "value");
                if (str5 != null)
                  str5 = "splash";
                int i1 = paramActivity.getResources().getIdentifier(str5, "drawable", paramActivity.getPackageName());
                paramActivity.getIntent().putExtra(str3, i1);
                LOG.i("CordovaLog", "Found preference for %s=%s", new Object[] { str3, str5 });
                Log.d("CordovaLog", "Found preference for " + str3 + "=" + str5);
                continue;
              }
              if (str3.equals("backgroundColor"))
              {
                int n = localXmlResourceParser.getAttributeIntValue(null, "value", -16777216);
                paramActivity.getIntent().putExtra(str3, n);
                Object[] arrayOfObject2 = new Object[2];
                arrayOfObject2[0] = str3;
                arrayOfObject2[1] = Integer.valueOf(n);
                LOG.i("CordovaLog", "Found preference for %s=%d", arrayOfObject2);
                Log.d("CordovaLog", "Found preference for " + str3 + "=" + Integer.toString(n));
                continue;
              }
              if (str3.equals("loadUrlTimeoutValue"))
              {
                int m = localXmlResourceParser.getAttributeIntValue(null, "value", 20000);
                paramActivity.getIntent().putExtra(str3, m);
                Object[] arrayOfObject1 = new Object[2];
                arrayOfObject1[0] = str3;
                arrayOfObject1[1] = Integer.valueOf(m);
                LOG.i("CordovaLog", "Found preference for %s=%d", arrayOfObject1);
                Log.d("CordovaLog", "Found preference for " + str3 + "=" + Integer.toString(m));
                continue;
              }
              if (str3.equals("keepRunning"))
              {
                boolean bool3 = localXmlResourceParser.getAttributeValue(null, "value").equals("true");
                paramActivity.getIntent().putExtra(str3, bool3);
                continue;
              }
              if (str3.equals("InAppBrowserStorageEnabled"))
              {
                boolean bool2 = localXmlResourceParser.getAttributeValue(null, "value").equals("true");
                paramActivity.getIntent().putExtra(str3, bool2);
                continue;
              }
              if (str3.equals("disallowOverscroll"))
              {
                boolean bool1 = localXmlResourceParser.getAttributeValue(null, "value").equals("true");
                paramActivity.getIntent().putExtra(str3, bool1);
                continue;
              }
              String str4 = localXmlResourceParser.getAttributeValue(null, "value");
              paramActivity.getIntent().putExtra(str3, str4);
              LOG.i("CordovaLog", "Found preference for %s=%s", new Object[] { str3, str4 });
              Log.d("CordovaLog", "Found preference for " + str3 + "=" + str4);
              continue;
            }
            if (!str1.equals("content"))
              continue;
            String str2 = localXmlResourceParser.getAttributeValue(null, "src");
            LOG.i("CordovaLog", "Found start page location: %s", new Object[] { str2 });
            if (str2 == null)
              continue;
            if (Pattern.compile("^[a-z]+://").matcher(str2).find())
            {
              this.startUrl = str2;
              continue;
            }
            if (str2.charAt(0) == '/')
              str2 = str2.substring(1);
            this.startUrl = ("file:///android_asset/www/" + str2);
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

  private void _addWhiteListEntry(String paramString, boolean paramBoolean)
  {
    while (true)
    {
      try
      {
        if (paramString.compareTo("*") != 0)
          continue;
        LOG.d("Config", "Unlimited access to network resources");
        this.whiteList.add(Pattern.compile(".*"));
        return;
        if (!paramBoolean)
          break;
        if (paramString.startsWith("http"))
        {
          this.whiteList.add(Pattern.compile(paramString.replaceFirst("https?://", "^https?://(.*\\.)?")));
          LOG.d("Config", "Origin to allow with subdomains: %s", new Object[] { paramString });
          return;
        }
      }
      catch (Exception localException)
      {
        LOG.d("Config", "Failed to add origin %s", new Object[] { paramString });
        return;
      }
      this.whiteList.add(Pattern.compile("^https?://(.*\\.)?" + paramString));
    }
    if (paramString.startsWith("http"))
      this.whiteList.add(Pattern.compile(paramString.replaceFirst("https?://", "^https?://")));
    while (true)
    {
      LOG.d("Config", "Origin to allow: %s", new Object[] { paramString });
      return;
      this.whiteList.add(Pattern.compile("^https?://" + paramString));
    }
  }

  public static void addWhiteListEntry(String paramString, boolean paramBoolean)
  {
    if (self == null)
      return;
    self._addWhiteListEntry(paramString, paramBoolean);
  }

  public static String getStartUrl()
  {
    if ((self == null) || (self.startUrl == null))
      return "file:///android_asset/www/index.html";
    return self.startUrl;
  }

  public static void init()
  {
    if (self == null)
      self = new Config();
  }

  public static void init(Activity paramActivity)
  {
    if (self == null)
      self = new Config(paramActivity);
  }

  public static boolean isUrlWhiteListed(String paramString)
  {
    if (self == null);
    Iterator localIterator;
    do
      while (!localIterator.hasNext())
      {
        return false;
        if (self.whiteListCache.get(paramString) != null)
          return true;
        localIterator = self.whiteList.iterator();
      }
    while (!((Pattern)localIterator.next()).matcher(paramString).find());
    self.whiteListCache.put(paramString, Boolean.valueOf(true));
    return true;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.Config
 * JD-Core Version:    0.6.0
 */