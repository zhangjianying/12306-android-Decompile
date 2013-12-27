package com.worklight.utils;

import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Vector;

public class MultifileAssetStream extends SequenceInputStream
{
  public MultifileAssetStream(String paramString, AssetManager paramAssetManager)
    throws IOException
  {
    super(getAssetFileStreams(paramString, paramAssetManager).elements());
  }

  private static String getAssetFileName(String paramString, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder().append(paramString).append(".");
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(paramInt);
    return String.format("%03d", arrayOfObject);
  }

  private static Vector<InputStream> getAssetFileStreams(String paramString, AssetManager paramAssetManager)
    throws IOException
  {
    int i = 1;
    int j = 1;
    Vector localVector = new Vector();
    while (i != 0)
    {
      InputStream localInputStream = getFileInputStream(getAssetFileName(paramString, j), paramAssetManager);
      if ((j == 0) && (localInputStream == null))
        localInputStream = getFileInputStream(paramString, paramAssetManager);
      if (localInputStream != null)
      {
        localVector.add(localInputStream);
        j++;
        continue;
      }
      i = 0;
    }
    if (localVector.size() == 0)
      throw new IOException("Unable to open any files with that base name");
    return localVector;
  }

  private static InputStream getFileInputStream(String paramString, AssetManager paramAssetManager)
  {
    try
    {
      InputStream localInputStream = paramAssetManager.open(paramString);
      return localInputStream;
    }
    catch (IOException localIOException)
    {
    }
    return null;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.utils.MultifileAssetStream
 * JD-Core Version:    0.6.0
 */