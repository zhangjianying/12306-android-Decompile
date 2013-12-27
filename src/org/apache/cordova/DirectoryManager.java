package org.apache.cordova;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import java.io.File;

public class DirectoryManager
{
  private static final String LOG_TAG = "DirectoryManager";

  private static File constructFilePaths(String paramString1, String paramString2)
  {
    if (paramString2.startsWith(paramString1))
      return new File(paramString2);
    return new File(paramString1 + "/" + paramString2);
  }

  private static long freeSpaceCalculation(String paramString)
  {
    StatFs localStatFs = new StatFs(paramString);
    return localStatFs.getBlockSize() * localStatFs.getAvailableBlocks() / 1024L;
  }

  protected static long getFreeDiskSpace(boolean paramBoolean)
  {
    long l;
    if (Environment.getExternalStorageState().equals("mounted"))
      l = freeSpaceCalculation(Environment.getExternalStorageDirectory().getPath());
    while (true)
    {
      return l;
      if (!paramBoolean)
        break;
      l = freeSpaceCalculation("/");
    }
    return -1L;
  }

  protected static String getTempDirectoryPath(Context paramContext)
  {
    if (Environment.getExternalStorageState().equals("mounted"));
    for (File localFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + paramContext.getPackageName() + "/cache/"); ; localFile = paramContext.getCacheDir())
    {
      if (!localFile.exists())
        localFile.mkdirs();
      return localFile.getAbsolutePath();
    }
  }

  protected static boolean testFileExists(String paramString)
  {
    if ((testSaveLocationExists()) && (!paramString.equals("")))
      return constructFilePaths(Environment.getExternalStorageDirectory().toString(), paramString).exists();
    return false;
  }

  protected static boolean testSaveLocationExists()
  {
    return Environment.getExternalStorageState().equals("mounted");
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.DirectoryManager
 * JD-Core Version:    0.6.0
 */