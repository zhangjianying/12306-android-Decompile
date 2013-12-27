package com.worklight.wlclient;

import com.worklight.common.WLUtils;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.CookieSpecBase;

public class WLCookieManager
{
  private static Set<Cookie> cookies;
  private static String instanceAuthId = null;

  public static void addCookies(WLRequest paramWLRequest)
  {
    if ((cookies != null) && (!cookies.isEmpty()))
    {
      BrowserCompatSpec localBrowserCompatSpec = new BrowserCompatSpec();
      ArrayList localArrayList = new ArrayList();
      localArrayList.addAll(getCookies());
      List localList = localBrowserCompatSpec.formatCookies(localArrayList);
      paramWLRequest.getPostRequest().setHeader((Header)localList.get(0));
    }
  }

  public static void clearCookies()
  {
    instanceAuthId = null;
    if (cookies != null)
      cookies.clear();
  }

  public static Set<Cookie> getCookies()
  {
    return cookies;
  }

  public static String getInstanceAuthId()
  {
    return instanceAuthId;
  }

  public static void handleResponseHeaders(Header[] paramArrayOfHeader, URI paramURI)
  {
    if (cookies == null)
      cookies = new HashSet();
    CookieOrigin localCookieOrigin = new CookieOrigin(paramURI.getHost(), paramURI.getPort(), "/apps/services", false);
    BrowserCompatSpec localBrowserCompatSpec = new BrowserCompatSpec();
    int i = 0;
    while (true)
      if (i < paramArrayOfHeader.length)
      {
        if (paramArrayOfHeader[i].getName().toLowerCase().equals("set-cookie"));
        try
        {
          List localList = localBrowserCompatSpec.parse(paramArrayOfHeader[i], localCookieOrigin);
          cookies.addAll(localList);
          i++;
        }
        catch (MalformedCookieException localMalformedCookieException)
        {
          StringBuilder localStringBuilder = new StringBuilder().append("Response ");
          if (paramURI != null);
          for (String str = paramURI.getPath(); ; str = "")
          {
            WLUtils.error(str + " from Worklight server failed because cookies could not be extracted from http header " + paramArrayOfHeader[i].getName() + " with " + localMalformedCookieException.getMessage(), localMalformedCookieException);
            throw new RuntimeException(localMalformedCookieException);
          }
        }
      }
  }

  public static void setCookies(String paramString1, String paramString2)
  {
    if ((paramString1 == null) || (paramString1.length() == 0))
    {
      WLUtils.debug(String.format("setCookies() can't parse cookieString which is null or empty.", new Object[0]));
      return;
    }
    if ((paramString2 == null) || (paramString2.length() == 0))
    {
      WLUtils.debug(String.format("setCookies() can't create cookies with a null or empty domain.", new Object[0]));
      return;
    }
    HashSet localHashSet = new HashSet();
    String[] arrayOfString1 = paramString1.split(";");
    int i = 0;
    if (i < arrayOfString1.length)
    {
      String[] arrayOfString2 = arrayOfString1[i].trim().split("=");
      if (arrayOfString2.length == 2)
      {
        BasicClientCookie localBasicClientCookie = new BasicClientCookie(arrayOfString2[0].trim(), arrayOfString2[1].trim());
        localBasicClientCookie.setDomain(paramString2);
        localHashSet.add(localBasicClientCookie);
      }
      while (true)
      {
        i++;
        break;
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = arrayOfString1[i];
        WLUtils.debug(String.format("setCookies() can't parse cookie %s.", arrayOfObject));
      }
    }
    cookies = localHashSet;
  }

  public static void setInstanceAuthId(String paramString)
  {
    instanceAuthId = paramString;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.WLCookieManager
 * JD-Core Version:    0.6.0
 */