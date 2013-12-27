package com.tl.uic.util;

import android.app.Application;
import android.content.Context;
import com.tl.uic.Tealeaf;
import com.tl.uic.model.TLFCacheFile;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class FileUtil
{
  private static volatile FileUtil _myInstance;

  // ERROR //
  public static java.lang.Boolean deleteFile(String paramString1, String paramString2)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: iconst_0
    //   3: invokestatic 20	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   6: astore_3
    //   7: new 22	java/io/File
    //   10: dup
    //   11: invokestatic 28	com/tl/uic/Tealeaf:getApplication	()Landroid/app/Application;
    //   14: invokevirtual 34	android/app/Application:getApplicationContext	()Landroid/content/Context;
    //   17: aload_0
    //   18: iconst_0
    //   19: invokevirtual 40	android/content/Context:getDir	(Ljava/lang/String;I)Ljava/io/File;
    //   22: aload_1
    //   23: invokespecial 43	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   26: astore 4
    //   28: aload 4
    //   30: ifnonnull +11 -> 41
    //   33: aload 4
    //   35: invokevirtual 47	java/io/File:exists	()Z
    //   38: ifeq +14 -> 52
    //   41: aload 4
    //   43: invokevirtual 50	java/io/File:delete	()Z
    //   46: pop
    //   47: iconst_1
    //   48: invokestatic 20	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   51: astore_3
    //   52: new 52	java/lang/StringBuilder
    //   55: dup
    //   56: ldc 54
    //   58: invokespecial 57	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   61: aload_3
    //   62: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   65: astore 10
    //   67: aload_3
    //   68: invokevirtual 64	java/lang/Boolean:booleanValue	()Z
    //   71: ifeq +40 -> 111
    //   74: new 52	java/lang/StringBuilder
    //   77: dup
    //   78: ldc 66
    //   80: invokespecial 57	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   83: aload 4
    //   85: invokevirtual 70	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   88: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   91: invokevirtual 76	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   94: astore 11
    //   96: aload 10
    //   98: aload 11
    //   100: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   103: invokevirtual 76	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   106: invokestatic 81	com/tl/uic/util/LogInternal:log	(Ljava/lang/String;)V
    //   109: aload_3
    //   110: areturn
    //   111: ldc 83
    //   113: astore 11
    //   115: goto -19 -> 96
    //   118: astore 5
    //   120: new 52	java/lang/StringBuilder
    //   123: dup
    //   124: ldc 85
    //   126: invokespecial 57	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   129: astore 6
    //   131: aload_2
    //   132: ifnonnull +25 -> 157
    //   135: ldc 83
    //   137: astore 7
    //   139: aload 5
    //   141: aload 6
    //   143: aload 7
    //   145: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   148: invokevirtual 76	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   151: invokestatic 89	com/tl/uic/util/LogInternal:logException	(Ljava/lang/Throwable;Ljava/lang/String;)Ljava/lang/Boolean;
    //   154: pop
    //   155: aload_3
    //   156: areturn
    //   157: aload_2
    //   158: invokevirtual 70	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   161: astore 7
    //   163: goto -24 -> 139
    //   166: astore 5
    //   168: aload 4
    //   170: astore_2
    //   171: goto -51 -> 120
    //
    // Exception table:
    //   from	to	target	type
    //   7	28	118	java/lang/Exception
    //   33	41	166	java/lang/Exception
    //   41	52	166	java/lang/Exception
    //   52	96	166	java/lang/Exception
    //   96	109	166	java/lang/Exception
  }

  public static FileUtil getInstance()
  {
    if (_myInstance == null)
      monitorenter;
    try
    {
      _myInstance = new FileUtil();
      return _myInstance;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  // ERROR //
  public static Object getObject(File paramFile)
  {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull +9 -> 10
    //   4: aconst_null
    //   5: astore 7
    //   7: aload 7
    //   9: areturn
    //   10: aconst_null
    //   11: astore_1
    //   12: aconst_null
    //   13: astore_2
    //   14: aload_0
    //   15: invokevirtual 47	java/io/File:exists	()Z
    //   18: istore 9
    //   20: aconst_null
    //   21: astore 7
    //   23: aconst_null
    //   24: astore_1
    //   25: aconst_null
    //   26: astore_2
    //   27: iload 9
    //   29: ifeq +41 -> 70
    //   32: new 98	java/io/FileInputStream
    //   35: dup
    //   36: aload_0
    //   37: invokespecial 101	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   40: astore 10
    //   42: new 103	java/io/ObjectInputStream
    //   45: dup
    //   46: aload 10
    //   48: invokespecial 106	java/io/ObjectInputStream:<init>	(Ljava/io/InputStream;)V
    //   51: astore 11
    //   53: aload 11
    //   55: invokevirtual 110	java/io/ObjectInputStream:readObject	()Ljava/lang/Object;
    //   58: astore 12
    //   60: aload 12
    //   62: astore 7
    //   64: aload 11
    //   66: astore_2
    //   67: aload 10
    //   69: astore_1
    //   70: aload_1
    //   71: ifnull +7 -> 78
    //   74: aload_1
    //   75: invokevirtual 113	java/io/FileInputStream:close	()V
    //   78: aload_2
    //   79: ifnull -72 -> 7
    //   82: aload_2
    //   83: invokevirtual 114	java/io/ObjectInputStream:close	()V
    //   86: aload 7
    //   88: areturn
    //   89: astore 13
    //   91: aload 13
    //   93: invokestatic 117	com/tl/uic/util/LogInternal:logException	(Ljava/lang/Throwable;)V
    //   96: aload 7
    //   98: areturn
    //   99: astore 5
    //   101: aload 5
    //   103: new 52	java/lang/StringBuilder
    //   106: dup
    //   107: ldc 119
    //   109: invokespecial 57	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   112: aload_0
    //   113: invokevirtual 70	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   116: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   119: invokevirtual 76	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   122: invokestatic 89	com/tl/uic/util/LogInternal:logException	(Ljava/lang/Throwable;Ljava/lang/String;)Ljava/lang/Boolean;
    //   125: pop
    //   126: aload_1
    //   127: ifnull +7 -> 134
    //   130: aload_1
    //   131: invokevirtual 113	java/io/FileInputStream:close	()V
    //   134: aconst_null
    //   135: astore 7
    //   137: aload_2
    //   138: ifnull -131 -> 7
    //   141: aload_2
    //   142: invokevirtual 114	java/io/ObjectInputStream:close	()V
    //   145: aconst_null
    //   146: areturn
    //   147: astore 8
    //   149: aload 8
    //   151: invokestatic 117	com/tl/uic/util/LogInternal:logException	(Ljava/lang/Throwable;)V
    //   154: aconst_null
    //   155: areturn
    //   156: astore_3
    //   157: aload_1
    //   158: ifnull +7 -> 165
    //   161: aload_1
    //   162: invokevirtual 113	java/io/FileInputStream:close	()V
    //   165: aload_2
    //   166: ifnull +7 -> 173
    //   169: aload_2
    //   170: invokevirtual 114	java/io/ObjectInputStream:close	()V
    //   173: aload_3
    //   174: athrow
    //   175: astore 4
    //   177: aload 4
    //   179: invokestatic 117	com/tl/uic/util/LogInternal:logException	(Ljava/lang/Throwable;)V
    //   182: goto -9 -> 173
    //   185: astore_3
    //   186: aload 10
    //   188: astore_1
    //   189: aconst_null
    //   190: astore_2
    //   191: goto -34 -> 157
    //   194: astore_3
    //   195: aload 11
    //   197: astore_2
    //   198: aload 10
    //   200: astore_1
    //   201: goto -44 -> 157
    //   204: astore 5
    //   206: aload 10
    //   208: astore_1
    //   209: aconst_null
    //   210: astore_2
    //   211: goto -110 -> 101
    //   214: astore 5
    //   216: aload 11
    //   218: astore_2
    //   219: aload 10
    //   221: astore_1
    //   222: goto -121 -> 101
    //
    // Exception table:
    //   from	to	target	type
    //   74	78	89	java/lang/Exception
    //   82	86	89	java/lang/Exception
    //   14	20	99	java/lang/Exception
    //   32	42	99	java/lang/Exception
    //   130	134	147	java/lang/Exception
    //   141	145	147	java/lang/Exception
    //   14	20	156	finally
    //   32	42	156	finally
    //   101	126	156	finally
    //   161	165	175	java/lang/Exception
    //   169	173	175	java/lang/Exception
    //   42	53	185	finally
    //   53	60	194	finally
    //   42	53	204	java/lang/Exception
    //   53	60	214	java/lang/Exception
  }

  public static List<TLFCacheFile> getObjects(String paramString)
  {
    int i = 0;
    ArrayList localArrayList = new ArrayList();
    File[] arrayOfFile = Tealeaf.getApplication().getApplicationContext().getDir(paramString, 0).listFiles();
    Arrays.sort(arrayOfFile, new Comparator()
    {
      public int compare(Object paramObject1, Object paramObject2)
      {
        return Long.valueOf(((File)paramObject1).lastModified()).compareTo(Long.valueOf(((File)paramObject2).lastModified()));
      }
    });
    int j = arrayOfFile.length;
    while (true)
    {
      if (i >= j)
        return localArrayList;
      File localFile = arrayOfFile[i];
      int k = localFile.getName().lastIndexOf(".");
      if (!"png".equals(localFile.getName().substring(k + 1, localFile.getName().length())))
      {
        Object localObject = getObject(localFile);
        if ((localObject instanceof TLFCacheFile))
          localArrayList.add((TLFCacheFile)localObject);
        localFile.delete();
      }
      i++;
    }
  }

  // ERROR //
  public static java.lang.Boolean saveObject(Object paramObject, String paramString1, String paramString2)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aconst_null
    //   3: astore 4
    //   5: aconst_null
    //   6: astore 5
    //   8: iconst_1
    //   9: invokestatic 20	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   12: astore 6
    //   14: new 22	java/io/File
    //   17: dup
    //   18: invokestatic 28	com/tl/uic/Tealeaf:getApplication	()Landroid/app/Application;
    //   21: invokevirtual 34	android/app/Application:getApplicationContext	()Landroid/content/Context;
    //   24: aload_1
    //   25: iconst_0
    //   26: invokevirtual 40	android/content/Context:getDir	(Ljava/lang/String;I)Ljava/io/File;
    //   29: aload_2
    //   30: invokespecial 43	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   33: astore 7
    //   35: new 175	java/io/FileOutputStream
    //   38: dup
    //   39: aload 7
    //   41: invokespecial 176	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   44: astore 8
    //   46: new 178	java/io/ObjectOutputStream
    //   49: dup
    //   50: aload 8
    //   52: invokespecial 181	java/io/ObjectOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   55: astore 9
    //   57: aload 9
    //   59: aload_0
    //   60: invokevirtual 185	java/io/ObjectOutputStream:writeObject	(Ljava/lang/Object;)V
    //   63: new 52	java/lang/StringBuilder
    //   66: dup
    //   67: ldc 187
    //   69: invokespecial 57	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   72: aload 6
    //   74: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   77: astore 21
    //   79: aload 6
    //   81: invokevirtual 64	java/lang/Boolean:booleanValue	()Z
    //   84: ifeq +75 -> 159
    //   87: new 52	java/lang/StringBuilder
    //   90: dup
    //   91: ldc 189
    //   93: invokespecial 57	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   96: aload 7
    //   98: invokevirtual 70	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   101: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   104: invokevirtual 76	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   107: astore 22
    //   109: aload 21
    //   111: aload 22
    //   113: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   116: invokevirtual 76	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   119: invokestatic 81	com/tl/uic/util/LogInternal:log	(Ljava/lang/String;)V
    //   122: aload 9
    //   124: ifnull +8 -> 132
    //   127: aload 9
    //   129: invokevirtual 190	java/io/ObjectOutputStream:close	()V
    //   132: aload 8
    //   134: ifnull +8 -> 142
    //   137: aload 8
    //   139: invokevirtual 191	java/io/FileOutputStream:close	()V
    //   142: aload 6
    //   144: invokevirtual 64	java/lang/Boolean:booleanValue	()Z
    //   147: ifne +182 -> 329
    //   150: aload 7
    //   152: invokevirtual 50	java/io/File:delete	()Z
    //   155: pop
    //   156: aload 6
    //   158: areturn
    //   159: ldc 83
    //   161: astore 22
    //   163: goto -54 -> 109
    //   166: astore 10
    //   168: iconst_0
    //   169: invokestatic 20	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   172: astore 6
    //   174: new 52	java/lang/StringBuilder
    //   177: dup
    //   178: ldc 193
    //   180: invokespecial 57	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   183: astore 14
    //   185: aload_3
    //   186: ifnonnull +75 -> 261
    //   189: ldc 83
    //   191: astore 16
    //   193: aload 10
    //   195: aload 14
    //   197: aload 16
    //   199: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   202: invokevirtual 76	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   205: invokestatic 89	com/tl/uic/util/LogInternal:logException	(Ljava/lang/Throwable;Ljava/lang/String;)Ljava/lang/Boolean;
    //   208: pop
    //   209: aload_1
    //   210: aload_2
    //   211: invokestatic 195	com/tl/uic/util/FileUtil:deleteFile	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Boolean;
    //   214: pop
    //   215: aload 5
    //   217: ifnull +8 -> 225
    //   220: aload 5
    //   222: invokevirtual 190	java/io/ObjectOutputStream:close	()V
    //   225: aload 4
    //   227: ifnull +8 -> 235
    //   230: aload 4
    //   232: invokevirtual 191	java/io/FileOutputStream:close	()V
    //   235: aload 6
    //   237: invokevirtual 64	java/lang/Boolean:booleanValue	()Z
    //   240: ifne -84 -> 156
    //   243: aload_3
    //   244: invokevirtual 50	java/io/File:delete	()Z
    //   247: pop
    //   248: aload 6
    //   250: areturn
    //   251: astore 19
    //   253: aload 19
    //   255: invokestatic 117	com/tl/uic/util/LogInternal:logException	(Ljava/lang/Throwable;)V
    //   258: aload 6
    //   260: areturn
    //   261: aload_3
    //   262: invokevirtual 70	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   265: astore 15
    //   267: aload 15
    //   269: astore 16
    //   271: goto -78 -> 193
    //   274: astore 11
    //   276: aload 5
    //   278: ifnull +8 -> 286
    //   281: aload 5
    //   283: invokevirtual 190	java/io/ObjectOutputStream:close	()V
    //   286: aload 4
    //   288: ifnull +8 -> 296
    //   291: aload 4
    //   293: invokevirtual 191	java/io/FileOutputStream:close	()V
    //   296: aload 6
    //   298: invokevirtual 64	java/lang/Boolean:booleanValue	()Z
    //   301: ifne +8 -> 309
    //   304: aload_3
    //   305: invokevirtual 50	java/io/File:delete	()Z
    //   308: pop
    //   309: aload 11
    //   311: athrow
    //   312: astore 12
    //   314: aload 12
    //   316: invokestatic 117	com/tl/uic/util/LogInternal:logException	(Ljava/lang/Throwable;)V
    //   319: goto -10 -> 309
    //   322: astore 23
    //   324: aload 23
    //   326: invokestatic 117	com/tl/uic/util/LogInternal:logException	(Ljava/lang/Throwable;)V
    //   329: aload 6
    //   331: areturn
    //   332: astore 11
    //   334: aload 7
    //   336: astore_3
    //   337: aconst_null
    //   338: astore 4
    //   340: aconst_null
    //   341: astore 5
    //   343: goto -67 -> 276
    //   346: astore 11
    //   348: aload 8
    //   350: astore 4
    //   352: aload 7
    //   354: astore_3
    //   355: aconst_null
    //   356: astore 5
    //   358: goto -82 -> 276
    //   361: astore 11
    //   363: aload 9
    //   365: astore 5
    //   367: aload 8
    //   369: astore 4
    //   371: aload 7
    //   373: astore_3
    //   374: goto -98 -> 276
    //   377: astore 10
    //   379: aload 7
    //   381: astore_3
    //   382: aconst_null
    //   383: astore 4
    //   385: aconst_null
    //   386: astore 5
    //   388: goto -220 -> 168
    //   391: astore 10
    //   393: aload 8
    //   395: astore 4
    //   397: aload 7
    //   399: astore_3
    //   400: aconst_null
    //   401: astore 5
    //   403: goto -235 -> 168
    //   406: astore 10
    //   408: aload 9
    //   410: astore 5
    //   412: aload 8
    //   414: astore 4
    //   416: aload 7
    //   418: astore_3
    //   419: goto -251 -> 168
    //
    // Exception table:
    //   from	to	target	type
    //   14	35	166	java/lang/Exception
    //   220	225	251	java/lang/Exception
    //   230	235	251	java/lang/Exception
    //   235	248	251	java/lang/Exception
    //   14	35	274	finally
    //   168	185	274	finally
    //   193	215	274	finally
    //   261	267	274	finally
    //   281	286	312	java/lang/Exception
    //   291	296	312	java/lang/Exception
    //   296	309	312	java/lang/Exception
    //   127	132	322	java/lang/Exception
    //   137	142	322	java/lang/Exception
    //   142	156	322	java/lang/Exception
    //   35	46	332	finally
    //   46	57	346	finally
    //   57	109	361	finally
    //   109	122	361	finally
    //   35	46	377	java/lang/Exception
    //   46	57	391	java/lang/Exception
    //   57	109	406	java/lang/Exception
    //   109	122	406	java/lang/Exception
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.util.FileUtil
 * JD-Core Version:    0.6.0
 */