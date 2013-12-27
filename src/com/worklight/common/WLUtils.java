package com.worklight.common;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import com.worklight.androidgap.NoSuchResourceException;
import com.worklight.androidgap.WLDroidGap;
import com.worklight.utils.SecurityUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WLUtils
{
  public static final int ANDROID_BUFFER_8K = 8192;
  public static final String LOG_CAT = "WLDroidGap";
  public static final String WL_CHALLENGE_DATA = "WL-Challenge-Data";
  public static final String WL_CHALLENGE_RESPONSE_DATA = "WL-Challenge-Response-Data";
  public static final String WL_INSTANCE_AUTH_ID = "WL-Instance-Id";
  public static final String WL_PREFS = "WLPrefs";

  public static String byteArrayToHexString(byte[] paramArrayOfByte)
  {
    StringBuilder localStringBuilder = new StringBuilder(2 * paramArrayOfByte.length);
    Formatter localFormatter = new Formatter(localStringBuilder);
    int i = paramArrayOfByte.length;
    for (int j = 0; j < i; j++)
    {
      byte b = paramArrayOfByte[j];
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Byte.valueOf(b);
      localFormatter.format("%02x", arrayOfObject);
    }
    localFormatter.close();
    return localStringBuilder.toString();
  }

  public static boolean checkIfMediaFile(File paramFile)
  {
    String[] arrayOfString = WLDroidGap.getWLConfig().getMediaExtensions();
    int i = 0;
    String str2;
    int j;
    if (arrayOfString != null)
    {
      String str1 = paramFile.getPath();
      str2 = str1.substring(1 + str1.lastIndexOf("."), str1.length());
      j = arrayOfString.length;
    }
    for (int k = 0; ; k++)
    {
      i = 0;
      if (k < j)
      {
        if (!arrayOfString[k].equals(str2))
          continue;
        i = 1;
      }
      return i;
    }
  }

  public static boolean checksumTestOnResources(String paramString, Context paramContext)
  {
    String str1 = SecurityUtils.hashData(Long.toString(computeChecksumOnResources(paramString)), "SHA-1");
    String str2 = readWLPref(paramContext, "wlResourcesChecksum");
    if (!str1.equals(str2))
    {
      debug(String.format("Checksomes are different, current checksum is %s, last checksum was %s", new Object[] { str1, str2 }));
      if (str2 == null)
      {
        debug("save web resources checksum on device");
        writeWLPref(paramContext, "wlResourcesChecksum", str1);
      }
    }
    else
    {
      return true;
    }
    error("Application failed to load, because its checksum " + str2 + " does not match " + str1 + ". This may indicate unintended change to the application.");
    return false;
  }

  public static void clearWLPref(Context paramContext)
  {
    SharedPreferences.Editor localEditor = paramContext.getSharedPreferences("WLPrefs", 0).edit();
    localEditor.clear();
    localEditor.commit();
  }

  public static long computeChecksumOnResources(String paramString)
  {
    List localList = getTree(new File(paramString));
    Collections.sort(localList);
    CRC32 localCRC32 = new CRC32();
    Iterator localIterator = localList.iterator();
    while (true)
      if (localIterator.hasNext())
      {
        File localFile = (File)localIterator.next();
        if ((WLDroidGap.getWLConfig().getMediaExtensions() != null) && (checkIfMediaFile(localFile)))
          continue;
        try
        {
          byte[] arrayOfByte2 = read(localFile);
          arrayOfByte1 = arrayOfByte2;
          localCRC32.update(arrayOfByte1, 0, arrayOfByte1.length);
        }
        catch (IOException localIOException)
        {
          while (true)
          {
            error("Application failed to load, because checksum was not calculated for file " + localFile.getName() + " with " + localIOException.getMessage(), localIOException);
            byte[] arrayOfByte1 = null;
          }
        }
      }
    return localCRC32.getValue();
  }

  public static String convertGZIPStreamToString(InputStream paramInputStream)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    try
    {
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(paramInputStream)));
      while (true)
      {
        String str = localBufferedReader.readLine();
        if (str == null)
          break;
        localStringBuilder.append(str + "\n");
      }
    }
    catch (IOException localIOException2)
    {
      throw new RuntimeException("Error reding input stream (" + paramInputStream + ").", localIOException2);
    }
    finally
    {
    }
    try
    {
      paramInputStream.close();
      throw localObject;
      try
      {
        paramInputStream.close();
        return localStringBuilder.toString();
      }
      catch (IOException localIOException3)
      {
        while (true)
          debug("Failed to close input stream because " + localIOException3.getMessage(), localIOException3);
      }
    }
    catch (IOException localIOException1)
    {
      while (true)
        debug("Failed to close input stream because " + localIOException1.getMessage(), localIOException1);
    }
  }

  public static List<String> convertJSONArrayToList(JSONArray paramJSONArray)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    while (i < paramJSONArray.length())
      try
      {
        localArrayList.add((String)paramJSONArray.get(i));
        i++;
      }
      catch (JSONException localJSONException)
      {
        throw new RuntimeException(localJSONException);
      }
    return localArrayList;
  }

  public static String convertStreamToString(InputStream paramInputStream)
  {
    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream));
    StringBuilder localStringBuilder = new StringBuilder();
    try
    {
      while (true)
      {
        String str = localBufferedReader.readLine();
        if (str == null)
          break;
        localStringBuilder.append(str + "\n");
      }
    }
    catch (IOException localIOException2)
    {
      throw new RuntimeException("Error reding input stream (" + paramInputStream + ").", localIOException2);
    }
    finally
    {
    }
    try
    {
      paramInputStream.close();
      throw localObject;
      try
      {
        paramInputStream.close();
        return localStringBuilder.toString();
      }
      catch (IOException localIOException3)
      {
        while (true)
          debug("Failed to close input stream because " + localIOException3.getMessage(), localIOException3);
      }
    }
    catch (IOException localIOException1)
    {
      while (true)
        debug("Failed to close input stream because " + localIOException1.getMessage(), localIOException1);
    }
  }

  public static JSONObject convertStringToJSON(String paramString)
    throws JSONException
  {
    return new JSONObject(paramString.substring(paramString.indexOf("{"), 1 + paramString.lastIndexOf("}")));
  }

  public static void copyFile(File paramFile1, File paramFile2)
    throws IOException
  {
    FileInputStream localFileInputStream = new FileInputStream(paramFile1);
    if (!paramFile2.exists())
    {
      if (!paramFile1.isDirectory())
        break label88;
      paramFile2.mkdirs();
    }
    FileOutputStream localFileOutputStream;
    while (true)
    {
      localFileOutputStream = new FileOutputStream(paramFile2);
      try
      {
        byte[] arrayOfByte = new byte[8192];
        while (true)
        {
          int i = localFileInputStream.read(arrayOfByte);
          if (i == -1)
            break;
          localFileOutputStream.write(arrayOfByte, 0, i);
        }
      }
      catch (IOException localIOException)
      {
        throw localIOException;
      }
      finally
      {
        localFileInputStream.close();
        localFileOutputStream.close();
      }
      label88: File localFile = new File(paramFile2.getParent());
      if (!localFile.exists())
        localFile.mkdirs();
      paramFile2.createNewFile();
    }
    localFileInputStream.close();
    localFileOutputStream.close();
  }

  public static void copyFile(InputStream paramInputStream, OutputStream paramOutputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[8192];
    while (true)
    {
      int i = paramInputStream.read(arrayOfByte);
      if (i == -1)
        break;
      paramOutputStream.write(arrayOfByte, 0, i);
    }
    paramOutputStream.flush();
  }

  public static void debug(String paramString)
  {
    Log.d("WLDroidGap", getMsgNotNull(paramString));
  }

  public static void debug(String paramString, Throwable paramThrowable)
  {
    Log.d("WLDroidGap", getMsgNotNull(paramString), paramThrowable);
  }

  public static boolean deleteDirectory(File paramFile)
    throws Exception
  {
    while (true)
    {
      int i;
      try
      {
        if (paramFile.exists())
        {
          File[] arrayOfFile = paramFile.listFiles();
          i = 0;
          if (i < arrayOfFile.length)
          {
            if (!arrayOfFile[i].isDirectory())
              continue;
            deleteDirectory(arrayOfFile[i]);
            break label83;
            arrayOfFile[i].delete();
          }
        }
      }
      catch (Exception localException)
      {
        throw new Exception("Error occurred while trying to delete directory " + paramFile);
      }
      return paramFile.delete();
      label83: i++;
    }
  }

  public static void error(String paramString)
  {
    Log.e("WLDroidGap", getMsgNotNull(paramString));
  }

  public static void error(String paramString, Throwable paramThrowable)
  {
    Log.e("WLDroidGap", getMsgNotNull(paramString), paramThrowable);
  }

  // ERROR //
  public static Drawable findDrawableAsset(WLDroidGap paramWLDroidGap, String paramString)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: new 31	java/lang/StringBuilder
    //   5: dup
    //   6: invokespecial 138	java/lang/StringBuilder:<init>	()V
    //   9: aload_0
    //   10: invokevirtual 385	com/worklight/androidgap/WLDroidGap:getLocalStorageWebRoot	()Ljava/lang/String;
    //   13: invokevirtual 144	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   16: ldc_w 387
    //   19: invokevirtual 144	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   22: aload_1
    //   23: invokevirtual 144	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   26: invokevirtual 58	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   29: astore_3
    //   30: new 310	java/io/FileInputStream
    //   33: dup
    //   34: new 74	java/io/File
    //   37: dup
    //   38: aload_3
    //   39: invokespecial 178	java/io/File:<init>	(Ljava/lang/String;)V
    //   42: invokespecial 313	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   45: astore 4
    //   47: aload 4
    //   49: aload_1
    //   50: invokestatic 393	android/graphics/drawable/Drawable:createFromStream	(Ljava/io/InputStream;Ljava/lang/String;)Landroid/graphics/drawable/Drawable;
    //   53: astore 10
    //   55: aload 10
    //   57: astore 8
    //   59: aload 4
    //   61: ifnull +8 -> 69
    //   64: aload 4
    //   66: invokevirtual 269	java/io/InputStream:close	()V
    //   69: aload 8
    //   71: areturn
    //   72: astore 11
    //   74: new 31	java/lang/StringBuilder
    //   77: dup
    //   78: invokespecial 138	java/lang/StringBuilder:<init>	()V
    //   81: ldc_w 395
    //   84: invokevirtual 144	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   87: aload_3
    //   88: invokevirtual 144	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   91: invokevirtual 58	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   94: aload 11
    //   96: invokestatic 231	com/worklight/common/WLUtils:error	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   99: aload 8
    //   101: areturn
    //   102: astore 12
    //   104: new 31	java/lang/StringBuilder
    //   107: dup
    //   108: invokespecial 138	java/lang/StringBuilder:<init>	()V
    //   111: ldc_w 397
    //   114: invokevirtual 144	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   117: aload_1
    //   118: invokevirtual 144	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   121: invokevirtual 58	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   124: invokestatic 151	com/worklight/common/WLUtils:error	(Ljava/lang/String;)V
    //   127: aconst_null
    //   128: astore 8
    //   130: aload_2
    //   131: ifnull -62 -> 69
    //   134: aload_2
    //   135: invokevirtual 269	java/io/InputStream:close	()V
    //   138: aconst_null
    //   139: areturn
    //   140: astore 9
    //   142: new 31	java/lang/StringBuilder
    //   145: dup
    //   146: invokespecial 138	java/lang/StringBuilder:<init>	()V
    //   149: ldc_w 395
    //   152: invokevirtual 144	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   155: aload_3
    //   156: invokevirtual 144	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   159: invokevirtual 58	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   162: aload 9
    //   164: invokestatic 231	com/worklight/common/WLUtils:error	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   167: aconst_null
    //   168: areturn
    //   169: astore 6
    //   171: aload_2
    //   172: ifnull +7 -> 179
    //   175: aload_2
    //   176: invokevirtual 269	java/io/InputStream:close	()V
    //   179: aload 6
    //   181: athrow
    //   182: astore 7
    //   184: new 31	java/lang/StringBuilder
    //   187: dup
    //   188: invokespecial 138	java/lang/StringBuilder:<init>	()V
    //   191: ldc_w 395
    //   194: invokevirtual 144	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   197: aload_3
    //   198: invokevirtual 144	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   201: invokevirtual 58	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   204: aload 7
    //   206: invokestatic 231	com/worklight/common/WLUtils:error	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   209: goto -30 -> 179
    //   212: astore 6
    //   214: aload 4
    //   216: astore_2
    //   217: goto -46 -> 171
    //   220: astore 5
    //   222: aload 4
    //   224: astore_2
    //   225: goto -121 -> 104
    //
    // Exception table:
    //   from	to	target	type
    //   64	69	72	java/io/IOException
    //   30	47	102	java/io/IOException
    //   134	138	140	java/io/IOException
    //   30	47	169	finally
    //   104	127	169	finally
    //   175	179	182	java/io/IOException
    //   47	55	212	finally
    //   47	55	220	java/io/IOException
  }

  public static long getFreeSpaceOnDevice()
  {
    StatFs localStatFs = new StatFs(Environment.getDataDirectory().getPath());
    return localStatFs.getBlockSize() * localStatFs.getAvailableBlocks();
  }

  public static String getFullAppName(Context paramContext)
  {
    return paramContext.getPackageName() + "." + paramContext.getString(getResourceId(paramContext, "string", "app_name"));
  }

  private static String getMsgNotNull(String paramString)
  {
    if (paramString == null)
      paramString = "null";
    return paramString;
  }

  public static int getResourceId(Context paramContext, String paramString1, String paramString2)
    throws NoSuchResourceException
  {
    int i = -1;
    try
    {
      Class[] arrayOfClass = Class.forName(paramContext.getPackageName() + ".R").getDeclaredClasses();
      for (int j = 0; ; j++)
      {
        if (j < arrayOfClass.length)
        {
          if (!arrayOfClass[j].getSimpleName().equals(paramString1))
            continue;
          int k = arrayOfClass[j].getField(paramString2).getInt(null);
          i = k;
        }
        return i;
      }
    }
    catch (Exception localException)
    {
    }
    throw new NoSuchResourceException("Failed to find resource R." + paramString1 + "." + paramString2, localException);
  }

  public static String getResourceString(String paramString, Activity paramActivity)
  {
    Class localClass = null;
    if (0 == 0);
    try
    {
      localClass = Class.forName(paramActivity.getPackageName() + ".R$string");
      String str = paramActivity.getResources().getString(((Integer)localClass.getDeclaredField(paramString).get(null)).intValue());
      return str;
    }
    catch (Exception localException)
    {
      Log.e(paramActivity.getPackageName(), localException.getMessage(), localException);
      paramActivity.finish();
    }
    return "";
  }

  public static List<File> getTree(File paramFile)
  {
    return getTree(paramFile, new ArrayList());
  }

  private static List<File> getTree(File paramFile, List<File> paramList)
  {
    File[] arrayOfFile = paramFile.listFiles();
    int i = arrayOfFile.length;
    int j = 0;
    if (j < i)
    {
      File localFile = arrayOfFile[j];
      if (localFile.isDirectory())
        getTree(localFile, paramList);
      while (true)
      {
        j++;
        break;
        paramList.add(localFile);
      }
    }
    return paramList;
  }

  public static byte[] hexStringToByteArray(String paramString)
  {
    int i = paramString.length();
    byte[] arrayOfByte = new byte[i / 2];
    for (int j = 0; j < i; j += 2)
      arrayOfByte[(j / 2)] = (byte)((Character.digit(paramString.charAt(j), 16) << 4) + Character.digit(paramString.charAt(j + 1), 16));
    return arrayOfByte;
  }

  public static void info(String paramString)
  {
    Log.i("WLDroidGap", getMsgNotNull(paramString));
  }

  public static void info(String paramString, Throwable paramThrowable)
  {
    Log.i("WLDroidGap", getMsgNotNull(paramString), paramThrowable);
  }

  public static boolean isStringEmpty(String paramString)
  {
    return (paramString == null) || (paramString.length() == 0);
  }

  public static void log(String paramString, int paramInt)
  {
    Log.println(paramInt, "WLDroidGap", getMsgNotNull(paramString));
  }

  // ERROR //
  public static byte[] read(File paramFile)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 526	java/io/File:length	()J
    //   4: l2i
    //   5: newarray byte
    //   7: astore_1
    //   8: new 310	java/io/FileInputStream
    //   11: dup
    //   12: aload_0
    //   13: invokespecial 313	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   16: astore_2
    //   17: aload_2
    //   18: aload_1
    //   19: invokevirtual 341	java/io/InputStream:read	([B)I
    //   22: iconst_m1
    //   23: if_icmpne +30 -> 53
    //   26: new 176	java/io/IOException
    //   29: dup
    //   30: ldc_w 528
    //   33: invokespecial 529	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   36: athrow
    //   37: astore_3
    //   38: aload_2
    //   39: astore 4
    //   41: aload 4
    //   43: ifnull +8 -> 51
    //   46: aload 4
    //   48: invokevirtual 269	java/io/InputStream:close	()V
    //   51: aload_3
    //   52: athrow
    //   53: aload_2
    //   54: ifnull +7 -> 61
    //   57: aload_2
    //   58: invokevirtual 269	java/io/InputStream:close	()V
    //   61: aload_1
    //   62: areturn
    //   63: astore 6
    //   65: aload_1
    //   66: areturn
    //   67: astore 5
    //   69: goto -18 -> 51
    //   72: astore_3
    //   73: aconst_null
    //   74: astore 4
    //   76: goto -35 -> 41
    //
    // Exception table:
    //   from	to	target	type
    //   17	37	37	finally
    //   57	61	63	java/io/IOException
    //   46	51	67	java/io/IOException
    //   8	17	72	finally
  }

  public static String readWLPref(Context paramContext, String paramString)
  {
    return paramContext.getSharedPreferences("WLPrefs", 0).getString(paramString, null);
  }

  public static Drawable scaleImage(Drawable paramDrawable, float paramFloat1, float paramFloat2)
  {
    BitmapDrawable localBitmapDrawable = null;
    if (paramDrawable != null)
    {
      Bitmap localBitmap = ((BitmapDrawable)paramDrawable).getBitmap();
      int i = localBitmap.getWidth();
      int j = localBitmap.getHeight();
      Matrix localMatrix = new Matrix();
      localMatrix.postScale(paramFloat1, paramFloat2);
      localBitmapDrawable = new BitmapDrawable(Bitmap.createBitmap(localBitmap, 0, 0, i, j, localMatrix, true));
    }
    return localBitmapDrawable;
  }

  public static void showDialog(Context paramContext, String paramString1, String paramString2, String paramString3)
  {
    showDialog(paramContext, paramString1, paramString2, paramString3, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.dismiss();
      }
    });
  }

  public static void showDialog(Context paramContext, String paramString1, String paramString2, String paramString3, DialogInterface.OnClickListener paramOnClickListener)
  {
    ((Activity)paramContext).runOnUiThread(new Runnable(paramContext, paramString1, paramString2, paramString3, paramOnClickListener)
    {
      public void run()
      {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.val$context);
        localBuilder.setTitle(this.val$title);
        localBuilder.setMessage(this.val$message);
        localBuilder.setCancelable(false);
        localBuilder.setPositiveButton(this.val$buttonText, this.val$buttonClickListener);
        localBuilder.create();
        localBuilder.show();
      }
    });
  }

  public static void unpack(InputStream paramInputStream, File paramFile)
    throws IOException
  {
    ZipInputStream localZipInputStream = new ZipInputStream(paramInputStream);
    while (true)
    {
      ZipEntry localZipEntry = localZipInputStream.getNextEntry();
      if (localZipEntry == null)
        break;
      String str = localZipEntry.getName();
      if ((str.startsWith("/")) || (str.startsWith("\\")))
        str = str.substring(1);
      File localFile1 = new File(paramFile.getPath() + File.separator + str);
      if (localZipEntry.isDirectory())
      {
        if (localFile1.exists())
          continue;
        localFile1.mkdirs();
        continue;
      }
      File localFile2 = localFile1.getParentFile();
      if (!localFile2.exists())
        localFile2.mkdirs();
      FileOutputStream localFileOutputStream = new FileOutputStream(localFile1);
      copyFile(localZipInputStream, localFileOutputStream);
      localFileOutputStream.flush();
      localFileOutputStream.close();
    }
  }

  public static void warning(String paramString)
  {
    Log.w("WLDroidGap", getMsgNotNull(paramString));
  }

  public static void warning(String paramString, Throwable paramThrowable)
  {
    Log.w("WLDroidGap", getMsgNotNull(paramString), paramThrowable);
  }

  public static void writeWLPref(Context paramContext, String paramString1, String paramString2)
  {
    SharedPreferences.Editor localEditor = paramContext.getSharedPreferences("WLPrefs", 0).edit();
    localEditor.putString(paramString1, paramString2);
    localEditor.commit();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.common.WLUtils
 * JD-Core Version:    0.6.0
 */