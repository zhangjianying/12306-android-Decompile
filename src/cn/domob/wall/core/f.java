package cn.domob.wall.core;

import android.content.Context;
import android.database.Cursor;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.HashMap;

class f
  implements Runnable
{
  static final String a = "GET";
  static final String b = "POST";
  static final int c = 20000;
  private static p d = new p(f.class.getSimpleName());
  private static final int t = 4096;
  private static final String u = "ctwap";
  private HttpURLConnection e;
  private Context f;
  private URL g;
  private String h;
  private String i;
  private HashMap<String, String> j;
  private Proxy k;
  private String l;
  private int m;
  private a n;
  private String o;
  private String p;
  private int q;
  private String r;
  private byte[] s;

  public f(Context paramContext, String paramString, a parama)
  {
    this(paramContext, paramString, null, null, "GET", null, 20000, parama);
  }

  public f(Context paramContext, String paramString1, String paramString2, a parama)
  {
    this(paramContext, paramString1, null, null, "POST", paramString2, 20000, parama);
  }

  // ERROR //
  public f(Context paramContext, String paramString1, String paramString2, HashMap<String, String> paramHashMap, String paramString3, String paramString4, int paramInt, a parama)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 75	java/lang/Object:<init>	()V
    //   4: aload_0
    //   5: aload_1
    //   6: putfield 77	cn/domob/wall/core/f:f	Landroid/content/Context;
    //   9: aload_0
    //   10: aload_3
    //   11: putfield 79	cn/domob/wall/core/f:i	Ljava/lang/String;
    //   14: aload_0
    //   15: aload 4
    //   17: putfield 81	cn/domob/wall/core/f:j	Ljava/util/HashMap;
    //   20: aload_0
    //   21: aload 5
    //   23: putfield 83	cn/domob/wall/core/f:h	Ljava/lang/String;
    //   26: aload_0
    //   27: aload 6
    //   29: putfield 85	cn/domob/wall/core/f:l	Ljava/lang/String;
    //   32: aload_0
    //   33: iload 7
    //   35: putfield 87	cn/domob/wall/core/f:m	I
    //   38: aload_0
    //   39: aload 8
    //   41: putfield 89	cn/domob/wall/core/f:n	Lcn/domob/wall/core/f$a;
    //   44: aload_0
    //   45: getfield 77	cn/domob/wall/core/f:f	Landroid/content/Context;
    //   48: ifnonnull +3 -> 51
    //   51: aload_0
    //   52: getfield 83	cn/domob/wall/core/f:h	Ljava/lang/String;
    //   55: ifnonnull +9 -> 64
    //   58: aload_0
    //   59: ldc 10
    //   61: putfield 83	cn/domob/wall/core/f:h	Ljava/lang/String;
    //   64: aload_2
    //   65: ifnull +178 -> 243
    //   68: aload_2
    //   69: invokevirtual 95	java/lang/String:length	()I
    //   72: ifeq +171 -> 243
    //   75: aload_0
    //   76: getfield 83	cn/domob/wall/core/f:h	Ljava/lang/String;
    //   79: ldc 10
    //   81: invokevirtual 99	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   84: ifeq +144 -> 228
    //   87: aload 6
    //   89: ifnull +139 -> 228
    //   92: aload_2
    //   93: ldc 101
    //   95: invokevirtual 105	java/lang/String:indexOf	(Ljava/lang/String;)I
    //   98: iconst_m1
    //   99: if_icmpne +56 -> 155
    //   102: aload_0
    //   103: new 107	java/net/URL
    //   106: dup
    //   107: new 109	java/lang/StringBuilder
    //   110: dup
    //   111: invokespecial 110	java/lang/StringBuilder:<init>	()V
    //   114: aload_2
    //   115: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: ldc 101
    //   120: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   123: aload 6
    //   125: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   128: invokevirtual 117	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   131: invokespecial 118	java/net/URL:<init>	(Ljava/lang/String;)V
    //   134: putfield 120	cn/domob/wall/core/f:g	Ljava/net/URL;
    //   137: aload_0
    //   138: getfield 85	cn/domob/wall/core/f:l	Ljava/lang/String;
    //   141: ifnull +9 -> 150
    //   144: aload_0
    //   145: ldc 122
    //   147: putfield 124	cn/domob/wall/core/f:o	Ljava/lang/String;
    //   150: aload_0
    //   151: invokespecial 126	cn/domob/wall/core/f:h	()V
    //   154: return
    //   155: aload_0
    //   156: new 107	java/net/URL
    //   159: dup
    //   160: new 109	java/lang/StringBuilder
    //   163: dup
    //   164: invokespecial 110	java/lang/StringBuilder:<init>	()V
    //   167: aload_2
    //   168: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   171: ldc 128
    //   173: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   176: aload 6
    //   178: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   181: invokevirtual 117	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   184: invokespecial 118	java/net/URL:<init>	(Ljava/lang/String;)V
    //   187: putfield 120	cn/domob/wall/core/f:g	Ljava/net/URL;
    //   190: goto -53 -> 137
    //   193: astore 9
    //   195: getstatic 62	cn/domob/wall/core/f:d	Lcn/domob/wall/core/p;
    //   198: new 109	java/lang/StringBuilder
    //   201: dup
    //   202: invokespecial 110	java/lang/StringBuilder:<init>	()V
    //   205: ldc 130
    //   207: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   210: aload_2
    //   211: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   214: ldc 132
    //   216: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   219: invokevirtual 117	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   222: invokevirtual 134	cn/domob/wall/core/p:e	(Ljava/lang/String;)V
    //   225: goto -88 -> 137
    //   228: aload_0
    //   229: new 107	java/net/URL
    //   232: dup
    //   233: aload_2
    //   234: invokespecial 118	java/net/URL:<init>	(Ljava/lang/String;)V
    //   237: putfield 120	cn/domob/wall/core/f:g	Ljava/net/URL;
    //   240: goto -103 -> 137
    //   243: getstatic 62	cn/domob/wall/core/f:d	Lcn/domob/wall/core/p;
    //   246: ldc 136
    //   248: invokevirtual 134	cn/domob/wall/core/p:e	(Ljava/lang/String;)V
    //   251: goto -114 -> 137
    //   254: astore 11
    //   256: getstatic 62	cn/domob/wall/core/f:d	Lcn/domob/wall/core/p;
    //   259: ldc 138
    //   261: invokevirtual 140	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   264: getstatic 62	cn/domob/wall/core/f:d	Lcn/domob/wall/core/p;
    //   267: aload 11
    //   269: invokevirtual 143	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   272: return
    //   273: astore 10
    //   275: getstatic 62	cn/domob/wall/core/f:d	Lcn/domob/wall/core/p;
    //   278: ldc 138
    //   280: invokevirtual 140	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   283: getstatic 62	cn/domob/wall/core/f:d	Lcn/domob/wall/core/p;
    //   286: aload 10
    //   288: invokevirtual 143	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   291: return
    //
    // Exception table:
    //   from	to	target	type
    //   51	64	193	java/net/MalformedURLException
    //   68	87	193	java/net/MalformedURLException
    //   92	137	193	java/net/MalformedURLException
    //   155	190	193	java/net/MalformedURLException
    //   228	240	193	java/net/MalformedURLException
    //   243	251	193	java/net/MalformedURLException
    //   150	154	254	java/lang/Exception
    //   150	154	273	java/lang/Error
  }

  private final void a(String paramString, int paramInt)
  {
    d.b(this, "setProxy -- proxy:" + paramString + "| port:" + paramInt);
    this.k = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(paramString, paramInt));
  }

  private void h()
  {
    if (this.f == null);
    while (true)
    {
      return;
      Cursor localCursor = null;
      try
      {
        localCursor = l.t(this.f);
        if ((localCursor != null) && (localCursor.getCount() > 0))
        {
          localCursor.moveToFirst();
          String str1 = localCursor.getString(localCursor.getColumnIndexOrThrow("proxy"));
          int i1 = localCursor.getInt(localCursor.getColumnIndexOrThrow("port"));
          String str2 = localCursor.getString(localCursor.getColumnIndexOrThrow("apn"));
          d.b(this, "Current apnType is " + str2);
          d.b(this, "proxy:" + str1 + "| port:" + i1);
          if ((str1 != null) && (!str1.trim().equals("")) && (i1 != 0) && (!str2.equalsIgnoreCase("ctwap")))
          {
            d.b(this, "ad request use proxy");
            a(str1, i1);
          }
        }
        return;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        d.e(this, "获取APN失败");
        d.a(localIllegalArgumentException);
        return;
      }
      finally
      {
        if (localCursor != null)
          localCursor.close();
      }
    }
    throw localObject;
  }

  public String a()
  {
    return this.p;
  }

  public void a(String paramString)
  {
    this.p = paramString;
  }

  public void b()
  {
    d.a(this, "Do HTTP connection.");
    new Thread(this).start();
  }

  // ERROR //
  public void c()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aload_0
    //   3: getfield 120	cn/domob/wall/core/f:g	Ljava/net/URL;
    //   6: ifnull +823 -> 829
    //   9: getstatic 62	cn/domob/wall/core/f:d	Lcn/domob/wall/core/p;
    //   12: new 109	java/lang/StringBuilder
    //   15: dup
    //   16: invokespecial 110	java/lang/StringBuilder:<init>	()V
    //   19: ldc 246
    //   21: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   24: aload_0
    //   25: getfield 120	cn/domob/wall/core/f:g	Ljava/net/URL;
    //   28: invokevirtual 247	java/net/URL:toString	()Ljava/lang/String;
    //   31: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   34: invokevirtual 117	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   37: invokevirtual 140	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   40: iconst_1
    //   41: invokestatic 253	java/net/HttpURLConnection:setFollowRedirects	(Z)V
    //   44: aload_0
    //   45: getfield 171	cn/domob/wall/core/f:k	Ljava/net/Proxy;
    //   48: ifnull +207 -> 255
    //   51: aload_0
    //   52: aload_0
    //   53: getfield 120	cn/domob/wall/core/f:g	Ljava/net/URL;
    //   56: aload_0
    //   57: getfield 171	cn/domob/wall/core/f:k	Ljava/net/Proxy;
    //   60: invokevirtual 257	java/net/URL:openConnection	(Ljava/net/Proxy;)Ljava/net/URLConnection;
    //   63: checkcast 249	java/net/HttpURLConnection
    //   66: putfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   69: aload_0
    //   70: getfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   73: ifnull +756 -> 829
    //   76: aload_0
    //   77: getfield 79	cn/domob/wall/core/f:i	Ljava/lang/String;
    //   80: ifnull +27 -> 107
    //   83: aload_0
    //   84: getfield 79	cn/domob/wall/core/f:i	Ljava/lang/String;
    //   87: invokevirtual 95	java/lang/String:length	()I
    //   90: ifeq +17 -> 107
    //   93: aload_0
    //   94: getfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   97: ldc_w 261
    //   100: aload_0
    //   101: getfield 79	cn/domob/wall/core/f:i	Ljava/lang/String;
    //   104: invokevirtual 265	java/net/HttpURLConnection:setRequestProperty	(Ljava/lang/String;Ljava/lang/String;)V
    //   107: aload_0
    //   108: getfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   111: iconst_0
    //   112: invokevirtual 268	java/net/HttpURLConnection:setInstanceFollowRedirects	(Z)V
    //   115: aload_0
    //   116: getfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   119: aload_0
    //   120: getfield 87	cn/domob/wall/core/f:m	I
    //   123: invokevirtual 272	java/net/HttpURLConnection:setConnectTimeout	(I)V
    //   126: aload_0
    //   127: getfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   130: aload_0
    //   131: getfield 87	cn/domob/wall/core/f:m	I
    //   134: invokevirtual 275	java/net/HttpURLConnection:setReadTimeout	(I)V
    //   137: aload_0
    //   138: getfield 81	cn/domob/wall/core/f:j	Ljava/util/HashMap;
    //   141: ifnull +152 -> 293
    //   144: aload_0
    //   145: getfield 81	cn/domob/wall/core/f:j	Ljava/util/HashMap;
    //   148: invokevirtual 281	java/util/HashMap:keySet	()Ljava/util/Set;
    //   151: invokeinterface 287 1 0
    //   156: astore 18
    //   158: aload 18
    //   160: invokeinterface 292 1 0
    //   165: ifeq +128 -> 293
    //   168: aload 18
    //   170: invokeinterface 296 1 0
    //   175: checkcast 91	java/lang/String
    //   178: astore 19
    //   180: aload_0
    //   181: getfield 81	cn/domob/wall/core/f:j	Ljava/util/HashMap;
    //   184: aload 19
    //   186: invokevirtual 300	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   189: checkcast 91	java/lang/String
    //   192: astore 20
    //   194: aload 20
    //   196: ifnull -38 -> 158
    //   199: aload_0
    //   200: getfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   203: aload 19
    //   205: aload 20
    //   207: invokevirtual 303	java/net/HttpURLConnection:addRequestProperty	(Ljava/lang/String;Ljava/lang/String;)V
    //   210: goto -52 -> 158
    //   213: astore 6
    //   215: aconst_null
    //   216: astore 7
    //   218: getstatic 62	cn/domob/wall/core/f:d	Lcn/domob/wall/core/p;
    //   221: aload_0
    //   222: ldc_w 305
    //   225: invokevirtual 228	cn/domob/wall/core/p:e	(Ljava/lang/Object;Ljava/lang/String;)V
    //   228: getstatic 62	cn/domob/wall/core/f:d	Lcn/domob/wall/core/p;
    //   231: aload 6
    //   233: invokevirtual 143	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   236: aload_1
    //   237: ifnull +7 -> 244
    //   240: aload_1
    //   241: invokevirtual 308	java/io/BufferedInputStream:close	()V
    //   244: aload 7
    //   246: ifnull +8 -> 254
    //   249: aload 7
    //   251: invokevirtual 311	java/io/ByteArrayOutputStream:close	()V
    //   254: return
    //   255: aload_0
    //   256: aload_0
    //   257: getfield 120	cn/domob/wall/core/f:g	Ljava/net/URL;
    //   260: invokevirtual 314	java/net/URL:openConnection	()Ljava/net/URLConnection;
    //   263: checkcast 249	java/net/HttpURLConnection
    //   266: putfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   269: goto -200 -> 69
    //   272: astore_2
    //   273: aconst_null
    //   274: astore_3
    //   275: aload_3
    //   276: ifnull +7 -> 283
    //   279: aload_3
    //   280: invokevirtual 308	java/io/BufferedInputStream:close	()V
    //   283: aload_1
    //   284: ifnull +7 -> 291
    //   287: aload_1
    //   288: invokevirtual 311	java/io/ByteArrayOutputStream:close	()V
    //   291: aload_2
    //   292: athrow
    //   293: aload_0
    //   294: getfield 83	cn/domob/wall/core/f:h	Ljava/lang/String;
    //   297: ldc 13
    //   299: invokevirtual 99	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   302: ifeq +209 -> 511
    //   305: aload_0
    //   306: getfield 85	cn/domob/wall/core/f:l	Ljava/lang/String;
    //   309: ifnull +202 -> 511
    //   312: getstatic 62	cn/domob/wall/core/f:d	Lcn/domob/wall/core/p;
    //   315: ldc_w 316
    //   318: invokevirtual 140	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   321: aload_0
    //   322: getfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   325: ldc 13
    //   327: invokevirtual 319	java/net/HttpURLConnection:setRequestMethod	(Ljava/lang/String;)V
    //   330: aload_0
    //   331: getfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   334: iconst_1
    //   335: invokevirtual 322	java/net/HttpURLConnection:setDoOutput	(Z)V
    //   338: aload_0
    //   339: getfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   342: ldc_w 324
    //   345: aload_0
    //   346: getfield 124	cn/domob/wall/core/f:o	Ljava/lang/String;
    //   349: invokevirtual 265	java/net/HttpURLConnection:setRequestProperty	(Ljava/lang/String;Ljava/lang/String;)V
    //   352: aload_0
    //   353: getfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   356: ldc_w 326
    //   359: aload_0
    //   360: getfield 85	cn/domob/wall/core/f:l	Ljava/lang/String;
    //   363: invokevirtual 95	java/lang/String:length	()I
    //   366: invokestatic 329	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   369: invokevirtual 265	java/net/HttpURLConnection:setRequestProperty	(Ljava/lang/String;Ljava/lang/String;)V
    //   372: aload_0
    //   373: getfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   376: invokevirtual 333	java/net/HttpURLConnection:getOutputStream	()Ljava/io/OutputStream;
    //   379: astore 16
    //   381: new 335	java/io/BufferedWriter
    //   384: dup
    //   385: new 337	java/io/OutputStreamWriter
    //   388: dup
    //   389: aload 16
    //   391: invokespecial 340	java/io/OutputStreamWriter:<init>	(Ljava/io/OutputStream;)V
    //   394: sipush 4096
    //   397: invokespecial 343	java/io/BufferedWriter:<init>	(Ljava/io/Writer;I)V
    //   400: astore 17
    //   402: aload 17
    //   404: ifnull +27 -> 431
    //   407: aload 17
    //   409: aload_0
    //   410: getfield 85	cn/domob/wall/core/f:l	Ljava/lang/String;
    //   413: invokevirtual 346	java/io/BufferedWriter:write	(Ljava/lang/String;)V
    //   416: aload 17
    //   418: invokevirtual 349	java/io/BufferedWriter:flush	()V
    //   421: aload 17
    //   423: invokevirtual 350	java/io/BufferedWriter:close	()V
    //   426: aload 16
    //   428: invokevirtual 353	java/io/OutputStream:close	()V
    //   431: aload_0
    //   432: aload_0
    //   433: getfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   436: invokevirtual 356	java/net/HttpURLConnection:getResponseCode	()I
    //   439: putfield 358	cn/domob/wall/core/f:q	I
    //   442: aload_0
    //   443: getfield 358	cn/domob/wall/core/f:q	I
    //   446: sipush 301
    //   449: if_icmpeq +13 -> 462
    //   452: aload_0
    //   453: getfield 358	cn/domob/wall/core/f:q	I
    //   456: sipush 302
    //   459: if_icmpne +71 -> 530
    //   462: aload_0
    //   463: new 107	java/net/URL
    //   466: dup
    //   467: aload_0
    //   468: getfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   471: ldc_w 360
    //   474: invokevirtual 364	java/net/HttpURLConnection:getHeaderField	(Ljava/lang/String;)Ljava/lang/String;
    //   477: invokespecial 118	java/net/URL:<init>	(Ljava/lang/String;)V
    //   480: invokevirtual 314	java/net/URL:openConnection	()Ljava/net/URLConnection;
    //   483: checkcast 249	java/net/HttpURLConnection
    //   486: putfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   489: aload_0
    //   490: getfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   493: iconst_0
    //   494: invokevirtual 268	java/net/HttpURLConnection:setInstanceFollowRedirects	(Z)V
    //   497: aload_0
    //   498: aload_0
    //   499: getfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   502: invokevirtual 356	java/net/HttpURLConnection:getResponseCode	()I
    //   505: putfield 358	cn/domob/wall/core/f:q	I
    //   508: goto -66 -> 442
    //   511: getstatic 62	cn/domob/wall/core/f:d	Lcn/domob/wall/core/p;
    //   514: ldc_w 366
    //   517: invokevirtual 140	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   520: aload_0
    //   521: getfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   524: invokevirtual 369	java/net/HttpURLConnection:connect	()V
    //   527: goto -96 -> 431
    //   530: aload_0
    //   531: getfield 358	cn/domob/wall/core/f:q	I
    //   534: sipush 200
    //   537: if_icmplt +284 -> 821
    //   540: aload_0
    //   541: getfield 358	cn/domob/wall/core/f:q	I
    //   544: sipush 304
    //   547: if_icmpgt +274 -> 821
    //   550: getstatic 62	cn/domob/wall/core/f:d	Lcn/domob/wall/core/p;
    //   553: new 109	java/lang/StringBuilder
    //   556: dup
    //   557: invokespecial 110	java/lang/StringBuilder:<init>	()V
    //   560: ldc_w 371
    //   563: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   566: aload_0
    //   567: getfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   570: invokevirtual 375	java/net/HttpURLConnection:getURL	()Ljava/net/URL;
    //   573: invokevirtual 378	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   576: invokevirtual 117	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   579: invokevirtual 140	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   582: new 307	java/io/BufferedInputStream
    //   585: dup
    //   586: aload_0
    //   587: getfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   590: invokevirtual 382	java/net/HttpURLConnection:getInputStream	()Ljava/io/InputStream;
    //   593: sipush 4096
    //   596: invokespecial 385	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;I)V
    //   599: astore_3
    //   600: sipush 4096
    //   603: newarray byte
    //   605: astore 14
    //   607: new 310	java/io/ByteArrayOutputStream
    //   610: dup
    //   611: sipush 4096
    //   614: invokespecial 387	java/io/ByteArrayOutputStream:<init>	(I)V
    //   617: astore 7
    //   619: aload_3
    //   620: aload 14
    //   622: invokevirtual 391	java/io/BufferedInputStream:read	([B)I
    //   625: istore 15
    //   627: iload 15
    //   629: iconst_m1
    //   630: if_icmpeq +16 -> 646
    //   633: aload 7
    //   635: aload 14
    //   637: iconst_0
    //   638: iload 15
    //   640: invokevirtual 394	java/io/ByteArrayOutputStream:write	([BII)V
    //   643: goto -24 -> 619
    //   646: aload_0
    //   647: aload 7
    //   649: invokevirtual 398	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   652: putfield 400	cn/domob/wall/core/f:s	[B
    //   655: aload_0
    //   656: new 91	java/lang/String
    //   659: dup
    //   660: aload_0
    //   661: getfield 400	cn/domob/wall/core/f:s	[B
    //   664: ldc_w 402
    //   667: invokespecial 405	java/lang/String:<init>	([BLjava/lang/String;)V
    //   670: putfield 407	cn/domob/wall/core/f:r	Ljava/lang/String;
    //   673: aload_3
    //   674: astore_1
    //   675: aload_0
    //   676: getfield 259	cn/domob/wall/core/f:e	Ljava/net/HttpURLConnection;
    //   679: invokevirtual 410	java/net/HttpURLConnection:disconnect	()V
    //   682: aload_1
    //   683: astore 10
    //   685: aload 7
    //   687: astore 11
    //   689: aload 10
    //   691: ifnull +8 -> 699
    //   694: aload 10
    //   696: invokevirtual 308	java/io/BufferedInputStream:close	()V
    //   699: aload 11
    //   701: ifnull -447 -> 254
    //   704: aload 11
    //   706: invokevirtual 311	java/io/ByteArrayOutputStream:close	()V
    //   709: return
    //   710: astore 12
    //   712: getstatic 62	cn/domob/wall/core/f:d	Lcn/domob/wall/core/p;
    //   715: aload 12
    //   717: invokevirtual 143	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   720: return
    //   721: astore 13
    //   723: getstatic 62	cn/domob/wall/core/f:d	Lcn/domob/wall/core/p;
    //   726: aload 13
    //   728: invokevirtual 143	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   731: goto -32 -> 699
    //   734: astore 9
    //   736: getstatic 62	cn/domob/wall/core/f:d	Lcn/domob/wall/core/p;
    //   739: aload 9
    //   741: invokevirtual 143	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   744: goto -500 -> 244
    //   747: astore 8
    //   749: getstatic 62	cn/domob/wall/core/f:d	Lcn/domob/wall/core/p;
    //   752: aload 8
    //   754: invokevirtual 143	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   757: return
    //   758: astore 5
    //   760: getstatic 62	cn/domob/wall/core/f:d	Lcn/domob/wall/core/p;
    //   763: aload 5
    //   765: invokevirtual 143	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   768: goto -485 -> 283
    //   771: astore 4
    //   773: getstatic 62	cn/domob/wall/core/f:d	Lcn/domob/wall/core/p;
    //   776: aload 4
    //   778: invokevirtual 143	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   781: goto -490 -> 291
    //   784: astore_2
    //   785: aconst_null
    //   786: astore_1
    //   787: goto -512 -> 275
    //   790: astore_2
    //   791: aload 7
    //   793: astore_1
    //   794: goto -519 -> 275
    //   797: astore_2
    //   798: aload_1
    //   799: astore_3
    //   800: aload 7
    //   802: astore_1
    //   803: goto -528 -> 275
    //   806: astore 6
    //   808: aload_3
    //   809: astore_1
    //   810: aconst_null
    //   811: astore 7
    //   813: goto -595 -> 218
    //   816: astore 6
    //   818: goto -600 -> 218
    //   821: aconst_null
    //   822: astore 7
    //   824: aconst_null
    //   825: astore_1
    //   826: goto -151 -> 675
    //   829: aconst_null
    //   830: astore 10
    //   832: aconst_null
    //   833: astore 11
    //   835: goto -146 -> 689
    //   838: astore 6
    //   840: aload_3
    //   841: astore_1
    //   842: goto -624 -> 218
    //
    // Exception table:
    //   from	to	target	type
    //   2	69	213	java/lang/Exception
    //   69	107	213	java/lang/Exception
    //   107	158	213	java/lang/Exception
    //   158	194	213	java/lang/Exception
    //   199	210	213	java/lang/Exception
    //   255	269	213	java/lang/Exception
    //   293	402	213	java/lang/Exception
    //   407	431	213	java/lang/Exception
    //   431	442	213	java/lang/Exception
    //   442	462	213	java/lang/Exception
    //   462	508	213	java/lang/Exception
    //   511	527	213	java/lang/Exception
    //   530	600	213	java/lang/Exception
    //   2	69	272	finally
    //   69	107	272	finally
    //   107	158	272	finally
    //   158	194	272	finally
    //   199	210	272	finally
    //   255	269	272	finally
    //   293	402	272	finally
    //   407	431	272	finally
    //   431	442	272	finally
    //   442	462	272	finally
    //   462	508	272	finally
    //   511	527	272	finally
    //   530	600	272	finally
    //   704	709	710	java/io/IOException
    //   694	699	721	java/io/IOException
    //   240	244	734	java/io/IOException
    //   249	254	747	java/io/IOException
    //   279	283	758	java/io/IOException
    //   287	291	771	java/io/IOException
    //   600	619	784	finally
    //   619	627	790	finally
    //   633	643	790	finally
    //   646	673	790	finally
    //   218	236	797	finally
    //   675	682	797	finally
    //   600	619	806	java/lang/Exception
    //   675	682	816	java/lang/Exception
    //   619	627	838	java/lang/Exception
    //   633	643	838	java/lang/Exception
    //   646	673	838	java/lang/Exception
  }

  public byte[] d()
  {
    return this.s;
  }

  public String e()
  {
    return this.r;
  }

  public int f()
  {
    return this.q;
  }

  public String g()
  {
    return this.l;
  }

  public void run()
  {
    c();
    if (this.n != null)
      this.n.a(this);
  }

  static abstract interface a
  {
    public abstract void a(f paramf);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.f
 * JD-Core Version:    0.6.0
 */