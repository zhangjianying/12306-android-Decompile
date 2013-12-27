package com.tl.uic.util;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.view.View;
import com.tl.uic.TLFCache;
import com.tl.uic.Tealeaf;
import com.tl.uic.model.TLFCacheFile;
import java.util.Date;

public final class ImageUtil
{
  private static final int PERCENT = 100;
  private static volatile ImageUtil _myInstance;

  public static ImageUtil getInstance()
  {
    if (_myInstance == null)
      monitorenter;
    try
    {
      _myInstance = new ImageUtil();
      return _myInstance;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  private static View getMainView(View paramView)
  {
    View localView = paramView;
    if (localView == null)
      return localView;
    try
    {
      boolean bool;
      do
      {
        localView = localView.getRootView();
        if (localView.getRootView() == null)
          break;
        bool = localView.getClass().getName().contains("DecorView");
      }
      while (!bool);
      return localView;
    }
    catch (Exception localException)
    {
      while (true)
      {
        localView = paramView;
        LogInternal.logException(localException);
      }
    }
  }

  // ERROR //
  private static Boolean saveImageAsPNG(Bitmap paramBitmap, Context paramContext, String paramString1, String paramString2)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 4
    //   3: aconst_null
    //   4: astore 5
    //   6: iconst_0
    //   7: invokestatic 60	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   10: astore 6
    //   12: new 62	java/io/File
    //   15: dup
    //   16: aload_1
    //   17: aload_2
    //   18: iconst_0
    //   19: invokevirtual 68	android/content/Context:getDir	(Ljava/lang/String;I)Ljava/io/File;
    //   22: aload_3
    //   23: invokespecial 71	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   26: astore 7
    //   28: new 73	java/io/FileOutputStream
    //   31: dup
    //   32: aload 7
    //   34: invokespecial 76	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   37: astore 8
    //   39: aload_0
    //   40: invokevirtual 82	android/graphics/Bitmap:getHeight	()I
    //   43: aload_0
    //   44: invokevirtual 82	android/graphics/Bitmap:getHeight	()I
    //   47: iconst_2
    //   48: idiv
    //   49: isub
    //   50: istore 19
    //   52: aload_0
    //   53: aload_0
    //   54: invokevirtual 85	android/graphics/Bitmap:getWidth	()I
    //   57: aload_0
    //   58: invokevirtual 85	android/graphics/Bitmap:getWidth	()I
    //   61: iconst_2
    //   62: idiv
    //   63: isub
    //   64: iload 19
    //   66: iconst_0
    //   67: invokestatic 89	android/graphics/Bitmap:createScaledBitmap	(Landroid/graphics/Bitmap;IIZ)Landroid/graphics/Bitmap;
    //   70: getstatic 95	android/graphics/Bitmap$CompressFormat:PNG	Landroid/graphics/Bitmap$CompressFormat;
    //   73: bipush 100
    //   75: aload 8
    //   77: invokevirtual 99	android/graphics/Bitmap:compress	(Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
    //   80: invokestatic 60	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   83: astore 6
    //   85: new 101	java/lang/StringBuilder
    //   88: dup
    //   89: ldc 103
    //   91: invokespecial 106	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   94: aload 7
    //   96: invokevirtual 109	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   99: invokevirtual 113	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   102: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   105: invokestatic 119	com/tl/uic/util/LogInternal:log	(Ljava/lang/String;)V
    //   108: aload 8
    //   110: ifnull +8 -> 118
    //   113: aload 8
    //   115: invokevirtual 122	java/io/FileOutputStream:close	()V
    //   118: aload 6
    //   120: invokevirtual 126	java/lang/Boolean:booleanValue	()Z
    //   123: ifne +147 -> 270
    //   126: aload 7
    //   128: invokevirtual 129	java/io/File:delete	()Z
    //   131: pop
    //   132: aload 6
    //   134: areturn
    //   135: astore 9
    //   137: new 101	java/lang/StringBuilder
    //   140: dup
    //   141: ldc 131
    //   143: invokespecial 106	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   146: astore 10
    //   148: aload 4
    //   150: ifnonnull +60 -> 210
    //   153: ldc 133
    //   155: astore 15
    //   157: aload 9
    //   159: aload 10
    //   161: aload 15
    //   163: invokevirtual 113	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   166: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   169: invokestatic 136	com/tl/uic/util/LogInternal:logException	(Ljava/lang/Throwable;Ljava/lang/String;)Ljava/lang/Boolean;
    //   172: pop
    //   173: aload 5
    //   175: ifnull +8 -> 183
    //   178: aload 5
    //   180: invokevirtual 122	java/io/FileOutputStream:close	()V
    //   183: aload 6
    //   185: invokevirtual 126	java/lang/Boolean:booleanValue	()Z
    //   188: ifne -56 -> 132
    //   191: aload 4
    //   193: invokevirtual 129	java/io/File:delete	()Z
    //   196: pop
    //   197: aload 6
    //   199: areturn
    //   200: astore 17
    //   202: aload 17
    //   204: invokestatic 52	com/tl/uic/util/LogInternal:logException	(Ljava/lang/Throwable;)V
    //   207: aload 6
    //   209: areturn
    //   210: aload 4
    //   212: invokevirtual 109	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   215: astore 14
    //   217: aload 14
    //   219: astore 15
    //   221: goto -64 -> 157
    //   224: astore 11
    //   226: aload 5
    //   228: ifnull +8 -> 236
    //   231: aload 5
    //   233: invokevirtual 122	java/io/FileOutputStream:close	()V
    //   236: aload 6
    //   238: invokevirtual 126	java/lang/Boolean:booleanValue	()Z
    //   241: ifne +9 -> 250
    //   244: aload 4
    //   246: invokevirtual 129	java/io/File:delete	()Z
    //   249: pop
    //   250: aload 11
    //   252: athrow
    //   253: astore 12
    //   255: aload 12
    //   257: invokestatic 52	com/tl/uic/util/LogInternal:logException	(Ljava/lang/Throwable;)V
    //   260: goto -10 -> 250
    //   263: astore 20
    //   265: aload 20
    //   267: invokestatic 52	com/tl/uic/util/LogInternal:logException	(Ljava/lang/Throwable;)V
    //   270: aload 6
    //   272: areturn
    //   273: astore 11
    //   275: aload 7
    //   277: astore 4
    //   279: aconst_null
    //   280: astore 5
    //   282: goto -56 -> 226
    //   285: astore 11
    //   287: aload 8
    //   289: astore 5
    //   291: aload 7
    //   293: astore 4
    //   295: goto -69 -> 226
    //   298: astore 9
    //   300: aload 7
    //   302: astore 4
    //   304: aconst_null
    //   305: astore 5
    //   307: goto -170 -> 137
    //   310: astore 9
    //   312: aload 8
    //   314: astore 5
    //   316: aload 7
    //   318: astore 4
    //   320: goto -183 -> 137
    //
    // Exception table:
    //   from	to	target	type
    //   12	28	135	java/lang/Exception
    //   178	183	200	java/lang/Exception
    //   183	197	200	java/lang/Exception
    //   12	28	224	finally
    //   137	148	224	finally
    //   157	173	224	finally
    //   210	217	224	finally
    //   231	236	253	java/lang/Exception
    //   236	250	253	java/lang/Exception
    //   113	118	263	java/lang/Exception
    //   118	132	263	java/lang/Exception
    //   28	39	273	finally
    //   39	108	285	finally
    //   28	39	298	java/lang/Exception
    //   39	108	310	java/lang/Exception
  }

  public static Boolean snapShot(View paramView, String paramString1, String paramString2)
  {
    if (Tealeaf.isEnabled().booleanValue());
    for (boolean bool = snapShotHelper(Tealeaf.getApplication().getApplicationContext(), paramView, paramString1, new StringBuffer(paramString2)).booleanValue(); ; bool = false)
      return Boolean.valueOf(bool);
  }

  private static Boolean snapShotHelper(Context paramContext, View paramView, String paramString, StringBuffer paramStringBuffer)
  {
    Boolean localBoolean = Boolean.valueOf(false);
    try
    {
      Date localDate = new Date();
      View localView = getMainView(paramView);
      if (localView == null)
        return localBoolean;
      Bitmap localBitmap = Bitmap.createBitmap(localView.getWidth(), localView.getHeight(), Bitmap.Config.RGB_565);
      localView.draw(new Canvas(localBitmap));
      localView.setDrawingCacheEnabled(false);
      paramStringBuffer.append("_" + localDate.getTime() + ".png");
      Tealeaf.logCustomEvent("Screenshot Taken for file: " + paramStringBuffer);
      localBoolean = saveImageAsPNG(localBitmap, paramContext, paramString, paramStringBuffer.toString());
      if (localBoolean.booleanValue())
      {
        TLFCacheFile localTLFCacheFile = new TLFCacheFile();
        localTLFCacheFile.setDirectory(paramString);
        localTLFCacheFile.setFileName(paramStringBuffer.toString());
        localTLFCacheFile.setSessionId(TLFCache.getCurrentSessionId());
        localTLFCacheFile.isImage(Boolean.valueOf(true));
        TLFCache.saveToCache(Boolean.valueOf(false));
        FileUtil.saveObject(localTLFCacheFile, paramString, "imageFileName_" + localDate.getTime());
      }
      localBitmap.recycle();
      return localBoolean;
    }
    catch (Exception localException)
    {
      while (true)
        LogInternal.logException(localException);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.util.ImageUtil
 * JD-Core Version:    0.6.0
 */