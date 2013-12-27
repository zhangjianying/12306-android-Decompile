package com.tl.uic.util;

import android.os.AsyncTask;
import com.tl.uic.EnvironmentalData;
import com.tl.uic.TLFCache;
import com.tl.uic.model.MessageFormat;
import com.tl.uic.model.Session;
import com.tl.uic.model.TLFCacheFile;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

public class PostTask extends AsyncTask<Void, Void, Boolean>
{
  private static final int BUFFER_SIZE = 40;

  private void logResult(String paramString1, String paramString2, Boolean paramBoolean)
  {
    StringBuffer localStringBuffer = new StringBuffer(40);
    if (localStringBuffer.length() >= 1)
      localStringBuffer.delete(0, localStringBuffer.length());
    localStringBuffer.append("PostTask with session id:");
    localStringBuffer.append(paramString1);
    LogInternal.log(localStringBuffer.toString());
    if (localStringBuffer.length() >= 1)
      localStringBuffer.delete(0, localStringBuffer.length());
    if (paramBoolean.booleanValue())
      localStringBuffer.append("PostTask image:");
    while (true)
    {
      localStringBuffer.append(paramString2);
      LogInternal.log(localStringBuffer.toString());
      return;
      localStringBuffer.append("PostTask JSON:");
    }
  }

  private Boolean sendMessageFormat()
  {
    Boolean localBoolean1 = Boolean.valueOf(true);
    if ((TLFCache.getEnvironmentalData() != null) && (TLFCache.getEnvironmentalData().getConnectionReceiver() != null) && (!TLFCache.getEnvironmentalData().getConnectionReceiver().isOnline().booleanValue()) && (TLFCache.getMessages() == null) && (TLFCache.getMessages().size() > 0))
      return Boolean.valueOf(false);
    Boolean localBoolean2 = ConfigurationUtil.getBoolean("CompressPostMessage");
    String str = ConfigurationUtil.getString("PostMessageUrl");
    List localList = TLFCache.messageFormats();
    Date localDate;
    Iterator localIterator;
    if ((localList != null) && (!localList.isEmpty()))
    {
      localDate = new Date();
      localIterator = localList.iterator();
    }
    label397: 
    while (true)
    {
      if (!localIterator.hasNext())
      {
        GCUtil.clean(localList);
        return localBoolean1;
      }
      TLFCacheFile localTLFCacheFile = (TLFCacheFile)localIterator.next();
      if ((localBoolean1.booleanValue()) && (localTLFCacheFile.isImage().booleanValue()))
      {
        localBoolean1 = HTTPUtil.sendHttpImagePost(str, localTLFCacheFile.getSessionId(), localTLFCacheFile.getDirectory(), localTLFCacheFile.getFileName());
        if (localBoolean1.booleanValue())
        {
          logResult(localTLFCacheFile.getSessionId(), localTLFCacheFile.getFileName(), Boolean.valueOf(true));
          FileUtil.deleteFile(localTLFCacheFile.getDirectory(), localTLFCacheFile.getFileName());
        }
      }
      while (true)
      {
        if (localBoolean1.booleanValue())
          break label397;
        if (ConfigurationUtil.getBoolean("HasToPersistLocalCache").booleanValue())
        {
          FileUtil.saveObject(localTLFCacheFile, "TLFCache", "cache_" + localDate.getTime());
          LogInternal.log("Saving to device.");
        }
        LogInternal.log("Could not send message.");
        break;
        if ((!localBoolean1.booleanValue()) || ((MessageFormat)localTLFCacheFile.getData() == null) || (((MessageFormat)localTLFCacheFile.getData()).getSession() == null) || (((MessageFormat)localTLFCacheFile.getData()).getSession().getMessages() == null) || (((MessageFormat)localTLFCacheFile.getData()).getSession().getMessages().size() <= 0))
          continue;
        localBoolean1 = HTTPUtil.sendHttpPost(str, localTLFCacheFile.getSessionId(), ((MessageFormat)localTLFCacheFile.getData()).getJSON().toString(), localBoolean2);
        logResult(localTLFCacheFile.getSessionId(), ((MessageFormat)localTLFCacheFile.getData()).getJSON().toString(), Boolean.valueOf(false));
      }
    }
  }

  protected final Boolean doInBackground(Void[] paramArrayOfVoid)
  {
    Boolean localBoolean1 = Boolean.valueOf(false);
    try
    {
      localBoolean1 = sendMessageFormat();
      TLFCache.closePostTask();
      Boolean localBoolean2 = Boolean.valueOf(true);
      return localBoolean2;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localBoolean1;
  }

  public final Boolean flush()
  {
    return sendMessageFormat();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.util.PostTask
 * JD-Core Version:    0.6.0
 */