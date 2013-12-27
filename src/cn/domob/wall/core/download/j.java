package cn.domob.wall.core.download;

import cn.domob.wall.core.p;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class j
{
  private static final String a = "ImageGetFromHttp";
  private static p b = new p(DActivity.class.getSimpleName());

  // ERROR //
  public static android.graphics.Bitmap a(String paramString)
  {
    // Byte code:
    //   0: new 39	org/apache/http/impl/client/DefaultHttpClient
    //   3: dup
    //   4: invokespecial 40	org/apache/http/impl/client/DefaultHttpClient:<init>	()V
    //   7: astore_1
    //   8: new 42	org/apache/http/client/methods/HttpGet
    //   11: dup
    //   12: aload_0
    //   13: invokespecial 43	org/apache/http/client/methods/HttpGet:<init>	(Ljava/lang/String;)V
    //   16: astore_2
    //   17: aload_1
    //   18: aload_2
    //   19: invokeinterface 49 2 0
    //   24: astore 7
    //   26: aload 7
    //   28: invokeinterface 55 1 0
    //   33: invokeinterface 61 1 0
    //   38: istore 8
    //   40: iload 8
    //   42: sipush 200
    //   45: if_icmpeq +53 -> 98
    //   48: getstatic 28	cn/domob/wall/core/download/j:b	Lcn/domob/wall/core/p;
    //   51: ldc 8
    //   53: new 63	java/lang/StringBuilder
    //   56: dup
    //   57: invokespecial 64	java/lang/StringBuilder:<init>	()V
    //   60: ldc 66
    //   62: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   65: iload 8
    //   67: invokevirtual 73	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   70: ldc 75
    //   72: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   75: aload_0
    //   76: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   79: invokevirtual 78	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   82: invokevirtual 82	cn/domob/wall/core/p:d	(Ljava/lang/Object;Ljava/lang/String;)V
    //   85: aload_1
    //   86: invokeinterface 86 1 0
    //   91: invokeinterface 91 1 0
    //   96: aconst_null
    //   97: areturn
    //   98: aload 7
    //   100: invokeinterface 95 1 0
    //   105: astore 9
    //   107: aload 9
    //   109: ifnull +113 -> 222
    //   112: aload 9
    //   114: invokeinterface 101 1 0
    //   119: astore 12
    //   121: aload 12
    //   123: astore 11
    //   125: new 103	cn/domob/wall/core/download/j$a
    //   128: dup
    //   129: aload 11
    //   131: invokespecial 106	cn/domob/wall/core/download/j$a:<init>	(Ljava/io/InputStream;)V
    //   134: invokestatic 112	android/graphics/BitmapFactory:decodeStream	(Ljava/io/InputStream;)Landroid/graphics/Bitmap;
    //   137: astore 13
    //   139: aload 11
    //   141: ifnull +8 -> 149
    //   144: aload 11
    //   146: invokevirtual 117	java/io/InputStream:close	()V
    //   149: aload 9
    //   151: invokeinterface 120 1 0
    //   156: aload_1
    //   157: invokeinterface 86 1 0
    //   162: invokeinterface 91 1 0
    //   167: aload 13
    //   169: areturn
    //   170: astore 10
    //   172: aconst_null
    //   173: astore 11
    //   175: aload 11
    //   177: ifnull +8 -> 185
    //   180: aload 11
    //   182: invokevirtual 117	java/io/InputStream:close	()V
    //   185: aload 9
    //   187: invokeinterface 120 1 0
    //   192: aload 10
    //   194: athrow
    //   195: astore 6
    //   197: aload_2
    //   198: invokevirtual 123	org/apache/http/client/methods/HttpGet:abort	()V
    //   201: getstatic 28	cn/domob/wall/core/download/j:b	Lcn/domob/wall/core/p;
    //   204: aload 6
    //   206: invokevirtual 126	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   209: aload_1
    //   210: invokeinterface 86 1 0
    //   215: invokeinterface 91 1 0
    //   220: aconst_null
    //   221: areturn
    //   222: aload_1
    //   223: invokeinterface 86 1 0
    //   228: invokeinterface 91 1 0
    //   233: aconst_null
    //   234: areturn
    //   235: astore 5
    //   237: aload_2
    //   238: invokevirtual 123	org/apache/http/client/methods/HttpGet:abort	()V
    //   241: getstatic 28	cn/domob/wall/core/download/j:b	Lcn/domob/wall/core/p;
    //   244: ldc 8
    //   246: new 63	java/lang/StringBuilder
    //   249: dup
    //   250: invokespecial 64	java/lang/StringBuilder:<init>	()V
    //   253: ldc 128
    //   255: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   258: aload_0
    //   259: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   262: invokevirtual 78	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   265: invokevirtual 82	cn/domob/wall/core/p:d	(Ljava/lang/Object;Ljava/lang/String;)V
    //   268: aload_1
    //   269: invokeinterface 86 1 0
    //   274: invokeinterface 91 1 0
    //   279: aconst_null
    //   280: areturn
    //   281: astore 4
    //   283: aload_2
    //   284: invokevirtual 123	org/apache/http/client/methods/HttpGet:abort	()V
    //   287: getstatic 28	cn/domob/wall/core/download/j:b	Lcn/domob/wall/core/p;
    //   290: aload 4
    //   292: invokevirtual 126	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   295: aload_1
    //   296: invokeinterface 86 1 0
    //   301: invokeinterface 91 1 0
    //   306: aconst_null
    //   307: areturn
    //   308: astore_3
    //   309: aload_1
    //   310: invokeinterface 86 1 0
    //   315: invokeinterface 91 1 0
    //   320: aload_3
    //   321: athrow
    //   322: astore 10
    //   324: goto -149 -> 175
    //
    // Exception table:
    //   from	to	target	type
    //   112	121	170	finally
    //   17	40	195	java/io/IOException
    //   48	85	195	java/io/IOException
    //   98	107	195	java/io/IOException
    //   144	149	195	java/io/IOException
    //   149	156	195	java/io/IOException
    //   180	185	195	java/io/IOException
    //   185	195	195	java/io/IOException
    //   17	40	235	java/lang/IllegalStateException
    //   48	85	235	java/lang/IllegalStateException
    //   98	107	235	java/lang/IllegalStateException
    //   144	149	235	java/lang/IllegalStateException
    //   149	156	235	java/lang/IllegalStateException
    //   180	185	235	java/lang/IllegalStateException
    //   185	195	235	java/lang/IllegalStateException
    //   17	40	281	java/lang/Exception
    //   48	85	281	java/lang/Exception
    //   98	107	281	java/lang/Exception
    //   144	149	281	java/lang/Exception
    //   149	156	281	java/lang/Exception
    //   180	185	281	java/lang/Exception
    //   185	195	281	java/lang/Exception
    //   17	40	308	finally
    //   48	85	308	finally
    //   98	107	308	finally
    //   144	149	308	finally
    //   149	156	308	finally
    //   180	185	308	finally
    //   185	195	308	finally
    //   197	209	308	finally
    //   237	268	308	finally
    //   283	295	308	finally
    //   125	139	322	finally
  }

  static class a extends FilterInputStream
  {
    public a(InputStream paramInputStream)
    {
      super();
    }

    public long skip(long paramLong)
      throws IOException
    {
      long l1 = 0L;
      while (true)
      {
        if (l1 < paramLong)
        {
          l2 = this.in.skip(paramLong - l1);
          if (l2 != 0L)
            break label39;
          if (read() >= 0);
        }
        else
        {
          return l1;
        }
        long l2 = 1L;
        label39: l1 = l2 + l1;
      }
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.download.j
 * JD-Core Version:    0.6.0
 */