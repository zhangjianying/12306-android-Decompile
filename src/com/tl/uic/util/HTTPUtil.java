package com.tl.uic.util;

import android.app.Application;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.tl.uic.EnvironmentalData;
import com.tl.uic.TLFCache;
import com.tl.uic.Tealeaf;
import com.tl.uic.http.TLDefaultHttpClient;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.GZIPOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;

public final class HTTPUtil
{
  private static final int BUFFER_SIZE = 1024;
  private static final int SUCCESS_IM_USED_RFC_3229 = 226;
  private static final int SUCCESS_OK = 200;
  public static final String TLF_SESSION_ID_FROM_PCA = "TLTSID";
  private static volatile HTTPUtil _myInstance;

  public static Boolean checkWhiteList()
  {
    Boolean localBoolean1 = Boolean.valueOf(false);
    String str1 = ConfigurationUtil.getString("KillSwitchUrl");
    Boolean localBoolean2 = ConfigurationUtil.getBoolean("KillSwitchEnabled");
    Boolean localBoolean3 = ConfigurationUtil.getBoolean("UseWhiteList");
    String str2 = ConfigurationUtil.getString("WhiteListParam");
    Boolean localBoolean4 = ConfigurationUtil.getBoolean("UseRandomSample");
    String str3 = ConfigurationUtil.getString("RandomSampleParam");
    int i = ConfigurationUtil.getInt("KillSwitchMaxNumberOfTries");
    long l = ConfigurationUtil.getLongMS("KillSwitchTimeInterval");
    StringBuffer localStringBuffer = new StringBuffer(str1);
    label107: String str4;
    int j;
    if (localBoolean2.booleanValue())
    {
      if (!str1.contains("?"))
        break label146;
      localStringBuffer.append('&');
      if (!localBoolean3.booleanValue())
        break label157;
      localStringBuffer.append(str2);
      localStringBuffer.append('=');
      localStringBuffer.append(Tealeaf.getPhoneId());
      str4 = getBodyFromUrl(localStringBuffer.toString());
      j = 0;
    }
    while (true)
    {
      if (j >= i)
      {
        return localBoolean1;
        label146: localStringBuffer.append('?');
        break;
        label157: if (!localBoolean4.booleanValue())
          break label107;
        localStringBuffer.append(str3);
        break label107;
      }
      if (str4 != null)
      {
        if ("1".equals(str4))
          localBoolean1 = Boolean.valueOf(true);
        j = i;
      }
      if (j < i);
      try
      {
        Thread.sleep(l);
        j++;
      }
      catch (Exception localException)
      {
        while (true)
          LogInternal.logException(localException);
      }
    }
  }

  public static String convertStreamToString(InputStream paramInputStream)
    throws IOException
  {
    String str = "";
    StringWriter localStringWriter;
    char[] arrayOfChar;
    if (paramInputStream != null)
    {
      localStringWriter = new StringWriter();
      arrayOfChar = new char[1024];
    }
    try
    {
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream, "UTF-8"));
      while (true)
      {
        int i = localBufferedReader.read(arrayOfChar);
        if (i == -1)
        {
          paramInputStream.close();
          str = localStringWriter.toString();
          return str;
        }
        localStringWriter.write(arrayOfChar, 0, i);
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        LogInternal.logException(localException);
        paramInputStream.close();
      }
    }
    finally
    {
      paramInputStream.close();
    }
    throw localObject;
  }

  public static AbstractHttpEntity createEntity(String paramString, Boolean paramBoolean)
    throws IOException
  {
    if (paramBoolean.booleanValue())
    {
      byte[] arrayOfByte = paramString.getBytes("UTF-8");
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(arrayOfByte.length);
      GZIPOutputStream localGZIPOutputStream = new GZIPOutputStream(localByteArrayOutputStream);
      localGZIPOutputStream.write(arrayOfByte);
      localGZIPOutputStream.finish();
      localGZIPOutputStream.flush();
      localGZIPOutputStream.close();
      return new ByteArrayEntity(localByteArrayOutputStream.toByteArray());
    }
    return new StringEntity(paramString);
  }

  private static String getBodyFromUrl(String paramString)
  {
    try
    {
      String str = convertStreamToString(new URL(paramString).openConnection().getInputStream());
      return str;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return null;
  }

  public static String getCookieValue(URLConnection paramURLConnection, String paramString)
  {
    if (((paramURLConnection == null) || (paramURLConnection.getHeaderFields() == null)) && (paramString == null))
      return null;
    String str2;
    do
    {
      Iterator localIterator1 = paramURLConnection.getHeaderFields().entrySet().iterator();
      Iterator localIterator2;
      while (!localIterator2.hasNext())
      {
        Map.Entry localEntry;
        do
        {
          if (!localIterator1.hasNext())
            return null;
          localEntry = (Map.Entry)localIterator1.next();
        }
        while ((localEntry == null) || (localEntry.getKey() == null) || (!"Set-Cookie".equalsIgnoreCase((String)localEntry.getKey())));
        localIterator2 = ((List)localEntry.getValue()).iterator();
      }
      String str1 = (String)localIterator2.next();
      str2 = str1.substring(0, str1.indexOf(";"));
    }
    while (!str2.substring(0, str2.indexOf("=")).equals(paramString));
    return str2.substring(1 + str2.indexOf("="), str2.length());
  }

  public static HTTPUtil getInstance()
  {
    if (_myInstance == null)
      monitorenter;
    try
    {
      _myInstance = new HTTPUtil();
      return _myInstance;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public static Boolean sendHttpImagePost(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    if (paramString4 == null)
      return Boolean.valueOf(false);
    return sendPost(paramString1, null, Boolean.valueOf(false), Boolean.valueOf(false), paramString3, paramString4, paramString2);
  }

  public static Boolean sendHttpPost(String paramString1, String paramString2, String paramString3, Boolean paramBoolean)
  {
    if (paramString3 == null)
      return Boolean.valueOf(false);
    return sendPost(paramString1, paramString3, paramBoolean, Boolean.valueOf(true), null, null, paramString2);
  }

  private static Boolean sendPost(String paramString1, String paramString2, Boolean paramBoolean1, Boolean paramBoolean2, String paramString3, String paramString4, String paramString5)
  {
    if ((paramString2 == null) && (paramString4 == null) && (TLFCache.getEnvironmentalData() == null) && (TLFCache.getEnvironmentalData().getConnectionReceiver() == null) && (!TLFCache.getEnvironmentalData().getConnectionReceiver().isOnline().booleanValue()))
      return Boolean.valueOf(false);
    label227: int i;
    try
    {
      BasicHttpParams localBasicHttpParams = new BasicHttpParams();
      HttpConnectionParams.setConnectionTimeout(localBasicHttpParams, ConfigurationUtil.getInt("PostMessageTimeout"));
      HttpConnectionParams.setSoTimeout(localBasicHttpParams, ConfigurationUtil.getInt("PostMessageSocketTimeout"));
      HttpProtocolParams.setVersion(localBasicHttpParams, HttpVersion.HTTP_1_0);
      HttpProtocolParams.setUseExpectContinue(localBasicHttpParams, false);
      TLDefaultHttpClient localTLDefaultHttpClient = new TLDefaultHttpClient(localBasicHttpParams, paramString5);
      Display localDisplay = ((WindowManager)Tealeaf.getApplication().getApplicationContext().getSystemService("window")).getDefaultDisplay();
      String str;
      HttpPost localHttpPost;
      Object localObject;
      HttpResponse localHttpResponse;
      CookieSyncManager localCookieSyncManager;
      CookieManager localCookieManager;
      Iterator localIterator;
      if (paramBoolean2.booleanValue())
      {
        str = paramString1;
        localHttpPost = new HttpPost(str);
        localHttpPost.setHeader("Accept-Language", "en-US");
        if (JsonUtil.testMessageType(paramString2, Boolean.valueOf(true)).booleanValue())
          localHttpPost.addHeader(ConfigurationUtil.getString("MessageTypeHeader"), "true");
        if (!paramBoolean2.booleanValue())
          break label400;
        localObject = createEntity(paramString2, paramBoolean1);
        localHttpPost.setHeader("Content-Type", "text/json; charset=UTF-8");
        if (paramBoolean1.booleanValue())
          localHttpPost.setHeader("Content-Encoding", "gzip");
        localHttpPost.setEntity((HttpEntity)localObject);
        localHttpResponse = localTLDefaultHttpClient.execute(localHttpPost);
        List localList = localTLDefaultHttpClient.getCookieStore().getCookies();
        if (!localList.isEmpty())
        {
          localCookieSyncManager = CookieSyncManager.getInstance();
          localCookieManager = CookieManager.getInstance();
          if (localCookieManager != null)
            localIterator = localList.iterator();
        }
      }
      while (true)
      {
        if (!localIterator.hasNext())
        {
          localCookieSyncManager.sync();
          i = localHttpResponse.getStatusLine().getStatusCode();
          if ((i < 200) || (i > 226))
            break label560;
          return Boolean.valueOf(true);
          str = paramString1 + "?width=" + localDisplay.getWidth() + "&height=" + localDisplay.getHeight() + "&orientation=" + localDisplay.getOrientation();
          break;
          label400: localHttpPost.setHeader("X-Tealeaf-Mobile-Image", "Embedded");
          localHttpPost.setHeader("X-Tealeaf-Filename", paramString4);
          localObject = new FileEntity(new File(Tealeaf.getApplication().getApplicationContext().getDir(paramString3, 0), paramString4), "image/png");
          break label227;
        }
        Cookie localCookie = (Cookie)localIterator.next();
        localCookieManager.setCookie(paramString1, localCookie.getName() + "=" + localCookie.getValue() + "; Domain=" + localCookie.getDomain());
      }
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException, "url:" + paramString1);
    }
    while (true)
    {
      return Boolean.valueOf(false);
      label560: LogInternal.log("Got back status code:" + i + " from url:" + paramString1);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.util.HTTPUtil
 * JD-Core Version:    0.6.0
 */