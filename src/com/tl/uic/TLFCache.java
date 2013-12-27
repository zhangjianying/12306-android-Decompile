package com.tl.uic;

import android.os.Handler;
import com.tl.uic.model.ClientMessageHeader;
import com.tl.uic.model.JSONMessage;
import com.tl.uic.model.MessageFormat;
import com.tl.uic.model.Session;
import com.tl.uic.model.TLFCacheFile;
import com.tl.uic.util.ConfigurationUtil;
import com.tl.uic.util.FileUtil;
import com.tl.uic.util.JsonUtil;
import com.tl.uic.util.LogInternal;
import com.tl.uic.util.PostTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.List<Lcom.tl.uic.model.TLFCacheFile;>;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public final class TLFCache
{
  private static final int PERCENT = 100;
  private static Session _currentSession;
  private static long _currentSessionStartedDate;
  private static int _messageVersion;
  private static volatile TLFCache _myInstance;
  private static PostTask _postTask;
  private static TimerTask _postTimerTask;
  private static long currentBufferSize = 0L;
  private static String currentSessionId;
  private static EnvironmentalData environmentalData;
  private static int logLevel;

  static
  {
    _messageVersion = 0;
  }

  private static Boolean addJSONMessage(JSONMessage paramJSONMessage)
  {
    Boolean.valueOf(false);
    long l1;
    if ((getMessages() != null) || (currentBufferSize >= getTLFCacheFileMaxBytesSize()) || (!saveToCache(Boolean.valueOf(false)).booleanValue()))
    {
      cleanByCacheLevel();
      l1 = getTLFCacheFileMaxBytesSize() - getTLFCacheFileMaxBytesSize() * getBufferPercent() / 100L;
      if (currentBufferSize > l1)
      {
        if (!getHasToPersistLocalCache().booleanValue())
          break label203;
        saveToCache(Boolean.valueOf(false));
      }
    }
    while (true)
    {
      if ((getMessages() == null) && (_currentSession != null))
        _currentSession.setMessages(new ArrayList());
      long l2 = sizeOfObject(paramJSONMessage);
      paramJSONMessage.setSize(l2);
      Boolean localBoolean = Boolean.valueOf(getMessages().add(paramJSONMessage));
      StringBuffer localStringBuffer = new StringBuffer("Added:");
      localStringBuffer.append(paramJSONMessage.toString());
      LogInternal.log(localStringBuffer.toString());
      currentBufferSize = l2 + currentBufferSize;
      return localBoolean;
      label203: 
      do
      {
        currentBufferSize -= ((JSONMessage)getMessages().get(0)).getSize();
        getMessages().remove(0);
      }
      while (currentBufferSize > l1);
    }
  }

  public static Boolean addMessage(ClientMessageHeader paramClientMessageHeader)
  {
    if (!JsonUtil.testMessageType(paramClientMessageHeader, Boolean.valueOf(false)).booleanValue())
      return Boolean.valueOf(false);
    return addJSONMessage(new JSONMessage(paramClientMessageHeader));
  }

  public static Boolean addMessage(String paramString)
  {
    if (!JsonUtil.testMessageType(paramString, Boolean.valueOf(false)).booleanValue())
      return Boolean.valueOf(false);
    JSONMessage localJSONMessage = new JSONMessage();
    localJSONMessage.setJsonData(paramString);
    localJSONMessage.setLogLevel(1);
    return addJSONMessage(localJSONMessage);
  }

  private static void cleanByCacheLevel()
  {
    if ((getMessages() != null) && (getMessages().size() > 0));
    for (int i = -1 + getMessages().size(); ; i--)
    {
      if (i < 0)
        return;
      if ((getMessages().size() < i) || (((JSONMessage)getMessages().get(i)).getLogLevel() <= getCacheLevel()))
        continue;
      currentBufferSize -= ((JSONMessage)getMessages().get(i)).getSize();
      LogInternal.log("removed message byte size: " + ((JSONMessage)getMessages().get(i)).getSize());
      getMessages().remove(i);
    }
  }

  public static Boolean closePostTask()
  {
    if (_postTask != null);
    synchronized (_postTask)
    {
      _postTask = null;
      return Boolean.valueOf(true);
    }
  }

  private static MessageFormat createMessageFormat()
  {
    if ((_currentSession == null) || (getMessages() == null) || (getMessages().size() <= 0))
      return null;
    MessageFormat localMessageFormat = new MessageFormat();
    localMessageFormat.setMessageVersion(Tealeaf.getMessageVersion());
    int i = _messageVersion;
    _messageVersion = i + 1;
    localMessageFormat.setSerialNumber(i);
    _currentSession.setClientEnvironment(environmentalData.createClientEnvironment());
    localMessageFormat.setSession(_currentSession);
    return localMessageFormat;
  }

  private static String createSessionId()
  {
    return UUID.randomUUID().toString().replace("-", "").toUpperCase(Locale.ENGLISH);
  }

  private static void flushPostTask()
  {
    new Handler().post(new Runnable()
    {
      public void run()
      {
        try
        {
          TLFCache.closePostTask();
          TLFCache._postTask = new PostTask();
          TLFCache._postTask.execute(new Void[0]);
          return;
        }
        catch (Exception localException)
        {
          LogInternal.logException(localException);
        }
      }
    });
  }

  public static int getBufferLimit()
  {
    return ConfigurationUtil.getInt("BufferLimit");
  }

  public static int getBufferPercent()
  {
    return ConfigurationUtil.getInt("BufferPercent");
  }

  public static int getCacheLevel()
  {
    return ConfigurationUtil.getInt("CachingLevel");
  }

  public static long getCachedFileMaxBytesSize()
  {
    return ConfigurationUtil.getLong("CachedFileMaxBytesSize");
  }

  public static long getCurrentBufferSize()
  {
    return currentBufferSize;
  }

  public static String getCurrentSessionId()
  {
    return currentSessionId;
  }

  public static EnvironmentalData getEnvironmentalData()
  {
    return environmentalData;
  }

  public static Boolean getHasToPersistLocalCache()
  {
    return ConfigurationUtil.getBoolean("HasToPersistLocalCache");
  }

  public static TLFCache getInstance()
  {
    if (_myInstance == null)
      monitorenter;
    try
    {
      _myInstance = new TLFCache();
      return _myInstance;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public static int getLogLevel()
  {
    return logLevel;
  }

  public static List<JSONMessage> getMessages()
  {
    if (_currentSession == null)
      return null;
    return _currentSession.getMessages();
  }

  public static long getPostMessageMaxBytesSize()
  {
    return ConfigurationUtil.getLong("PostMessageMaxBytesSize");
  }

  public static long getTLFCacheFileMaxBytesSize()
  {
    if (getCachedFileMaxBytesSize() > getPostMessageMaxBytesSize())
      return getPostMessageMaxBytesSize();
    return getCachedFileMaxBytesSize();
  }

  public static List<TLFCacheFile> messageFormats()
  {
    Object localObject = new ArrayList();
    if (getHasToPersistLocalCache().booleanValue())
      localObject = FileUtil.getObjects("TLFCache");
    if (((List)localObject).isEmpty())
    {
      MessageFormat localMessageFormat = createMessageFormat();
      if ((localMessageFormat != null) && (localMessageFormat.getSession() != null) && (localMessageFormat.getSession().getSessionID() != null))
        ((List)localObject).add(new TLFCacheFile(localMessageFormat, localMessageFormat.getSession().getSessionID()));
    }
    return (List<TLFCacheFile>)localObject;
  }

  public static Boolean onPause()
  {
    if (environmentalData != null)
      environmentalData.onPause();
    return Boolean.valueOf(true);
  }

  public static Boolean onResume()
  {
    if (environmentalData != null)
      environmentalData.onResume();
    return Boolean.valueOf(true);
  }

  public static Boolean saveToCache(Boolean paramBoolean)
  {
    Boolean localBoolean1 = paramBoolean;
    Object localObject = Boolean.valueOf(false);
    MessageFormat localMessageFormat = createMessageFormat();
    if (localMessageFormat != null)
    {
      if (!getHasToPersistLocalCache().booleanValue())
        break label129;
      Date localDate = new Date();
      localObject = FileUtil.saveObject(new TLFCacheFile(localMessageFormat, localMessageFormat.getSession().getSessionID()), "TLFCache", "cache_" + localDate.getTime());
    }
    while (true)
    {
      if ((_postTimerTask != null) && (localBoolean1.booleanValue()));
      try
      {
        _postTimerTask.cancel();
        flushPostTask();
        Boolean localBoolean2 = Boolean.valueOf(true);
        localObject = localBoolean2;
        if (((Boolean)localObject).booleanValue())
        {
          getMessages().clear();
          currentBufferSize = 0L;
        }
        return localObject;
        label129: localBoolean1 = Boolean.valueOf(true);
      }
      catch (Exception localException)
      {
        while (true)
          LogInternal.logException(localException);
      }
    }
  }

  public static void setCurrentLogLevel(Boolean paramBoolean1, Boolean paramBoolean2, Boolean paramBoolean3)
  {
    if (paramBoolean1.booleanValue())
    {
      if (paramBoolean2.booleanValue())
        logLevel = ConfigurationUtil.getInt("PostMessageLevelWiFi");
      do
        return;
      while (!paramBoolean3.booleanValue());
      logLevel = ConfigurationUtil.getInt("PostMessageLevelCellular");
      return;
    }
    logLevel = ConfigurationUtil.getInt("LoggingLevel");
  }

  private static void setupPostTask()
  {
    Handler localHandler = new Handler();
    Timer localTimer = new Timer();
    _postTimerTask = new TimerTask(localHandler)
    {
      public void run()
      {
        TLFCache.this.post(new Runnable()
        {
          public void run()
          {
            try
            {
              if (TLFCache._postTask == null)
              {
                TLFCache._postTask = new PostTask();
                TLFCache._postTask.execute(new Void[0]);
              }
              return;
            }
            catch (Exception localException)
            {
              LogInternal.logException(localException);
            }
          }
        });
      }
    };
    localTimer.schedule(_postTimerTask, 0L, ConfigurationUtil.getLongMS("PostMessageTimeIntervals"));
  }

  public static long sizeOfObject(Object paramObject)
  {
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localByteArrayOutputStream);
      localObjectOutputStream.writeObject(paramObject);
      localObjectOutputStream.flush();
      localObjectOutputStream.close();
      localByteArrayOutputStream.close();
      int i = localByteArrayOutputStream.toByteArray().length;
      return i;
    }
    catch (IOException localIOException)
    {
      LogInternal.logException(localIOException);
    }
    return 0L;
  }

  public static Boolean startSession(String paramString)
  {
    if (!ConfigurationUtil.getBoolean("ManualPostEnabled").booleanValue())
      setupPostTask();
    if (_currentSession != null)
      saveToCache(Boolean.valueOf(false));
    _currentSessionStartedDate = new Date().getTime();
    if (paramString == null)
      paramString = createSessionId();
    currentSessionId = paramString;
    _currentSession = new Session();
    _currentSession.setSessionID(currentSessionId);
    _currentSession.setSessionStartTime(_currentSessionStartedDate);
    if (environmentalData == null)
      environmentalData = new EnvironmentalData(Tealeaf.getApplication());
    return Boolean.valueOf(true);
  }

  public static Boolean terminate()
  {
    _messageVersion = 0;
    _currentSession.clean();
    _currentSession = null;
    _currentSessionStartedDate = 0L;
    environmentalData.terminate();
    environmentalData = null;
    logLevel = 0;
    currentBufferSize = 0L;
    if (_postTimerTask != null)
    {
      _postTimerTask.cancel();
      flushPostTask();
      _postTimerTask = null;
    }
    return Boolean.valueOf(true);
  }

  public static long timestampFromSession()
  {
    return new Date().getTime() - _currentSessionStartedDate;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.TLFCache
 * JD-Core Version:    0.6.0
 */