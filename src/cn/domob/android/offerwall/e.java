package cn.domob.android.offerwall;

import android.content.Context;
import android.database.Cursor;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.HashMap;

class e
  implements Runnable
{
  static final String a = "GET";
  static final String b = "POST";
  static final int c = 20000;
  private static m d = new m(e.class.getSimpleName());
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

  e(Context paramContext, String paramString, a parama)
  {
    this(paramContext, paramString, null, null, "GET", null, 20000, parama);
  }

  e(Context paramContext, String paramString1, String paramString2, a parama)
  {
    this(paramContext, paramString1, null, null, "POST", paramString2, 20000, parama);
  }

  e(Context paramContext, String paramString1, String paramString2, HashMap<String, String> paramHashMap, String paramString3, String paramString4, int paramInt, a parama)
  {
    this.f = paramContext;
    this.i = paramString2;
    this.j = paramHashMap;
    this.h = paramString3;
    this.l = paramString4;
    this.m = paramInt;
    this.n = parama;
    try
    {
      if (this.h == null)
        this.h = "GET";
      if ((paramString1 != null) && (paramString1.length() != 0))
        if ((this.h.equals("GET")) && (paramString4 != null))
        {
          if (paramString1.indexOf("?") == -1);
          for (this.g = new URL(paramString1 + "?" + paramString4); ; this.g = new URL(paramString1 + "&" + paramString4))
          {
            if (this.l != null)
              this.o = "application/x-www-form-urlencoded";
            h();
            return;
          }
        }
    }
    catch (MalformedURLException localMalformedURLException)
    {
      while (true)
      {
        d.e("Invalid URL string:" + paramString1 + ". Failed to init connector.");
        continue;
        this.g = new URL(paramString1);
        continue;
        d.e("Request URL is null or empty.");
      }
    }
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
        localCursor = f.z(this.f);
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
        d.e(this, "获取APN失败。");
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

  protected String a()
  {
    return this.p;
  }

  protected void a(String paramString)
  {
    this.p = paramString;
  }

  protected void b()
  {
    d.a(this, "Do HTTP connection.");
    new Thread(this).start();
  }

  // ERROR //
  protected void c()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aload_0
    //   3: getfield 116	cn/domob/android/offerwall/e:g	Ljava/net/URL;
    //   6: ifnull +815 -> 821
    //   9: getstatic 62	cn/domob/android/offerwall/e:d	Lcn/domob/android/offerwall/m;
    //   12: new 105	java/lang/StringBuilder
    //   15: dup
    //   16: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   19: ldc 241
    //   21: invokevirtual 110	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   24: aload_0
    //   25: getfield 116	cn/domob/android/offerwall/e:g	Ljava/net/URL;
    //   28: invokevirtual 242	java/net/URL:toString	()Ljava/lang/String;
    //   31: invokevirtual 110	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   34: invokevirtual 113	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   37: invokevirtual 244	cn/domob/android/offerwall/m:b	(Ljava/lang/String;)V
    //   40: iconst_1
    //   41: invokestatic 250	java/net/HttpURLConnection:setFollowRedirects	(Z)V
    //   44: aload_0
    //   45: getfield 160	cn/domob/android/offerwall/e:k	Ljava/net/Proxy;
    //   48: ifnull +207 -> 255
    //   51: aload_0
    //   52: aload_0
    //   53: getfield 116	cn/domob/android/offerwall/e:g	Ljava/net/URL;
    //   56: aload_0
    //   57: getfield 160	cn/domob/android/offerwall/e:k	Ljava/net/Proxy;
    //   60: invokevirtual 254	java/net/URL:openConnection	(Ljava/net/Proxy;)Ljava/net/URLConnection;
    //   63: checkcast 246	java/net/HttpURLConnection
    //   66: putfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   69: aload_0
    //   70: getfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   73: ifnull +748 -> 821
    //   76: aload_0
    //   77: getfield 75	cn/domob/android/offerwall/e:i	Ljava/lang/String;
    //   80: ifnull +27 -> 107
    //   83: aload_0
    //   84: getfield 75	cn/domob/android/offerwall/e:i	Ljava/lang/String;
    //   87: invokevirtual 91	java/lang/String:length	()I
    //   90: ifeq +17 -> 107
    //   93: aload_0
    //   94: getfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   97: ldc_w 258
    //   100: aload_0
    //   101: getfield 75	cn/domob/android/offerwall/e:i	Ljava/lang/String;
    //   104: invokevirtual 262	java/net/HttpURLConnection:setRequestProperty	(Ljava/lang/String;Ljava/lang/String;)V
    //   107: aload_0
    //   108: getfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   111: iconst_1
    //   112: invokevirtual 265	java/net/HttpURLConnection:setInstanceFollowRedirects	(Z)V
    //   115: aload_0
    //   116: getfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   119: aload_0
    //   120: getfield 83	cn/domob/android/offerwall/e:m	I
    //   123: invokevirtual 269	java/net/HttpURLConnection:setConnectTimeout	(I)V
    //   126: aload_0
    //   127: getfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   130: aload_0
    //   131: getfield 83	cn/domob/android/offerwall/e:m	I
    //   134: invokevirtual 272	java/net/HttpURLConnection:setReadTimeout	(I)V
    //   137: aload_0
    //   138: getfield 77	cn/domob/android/offerwall/e:j	Ljava/util/HashMap;
    //   141: ifnull +152 -> 293
    //   144: aload_0
    //   145: getfield 77	cn/domob/android/offerwall/e:j	Ljava/util/HashMap;
    //   148: invokevirtual 278	java/util/HashMap:keySet	()Ljava/util/Set;
    //   151: invokeinterface 284 1 0
    //   156: astore 18
    //   158: aload 18
    //   160: invokeinterface 289 1 0
    //   165: ifeq +128 -> 293
    //   168: aload 18
    //   170: invokeinterface 293 1 0
    //   175: checkcast 87	java/lang/String
    //   178: astore 19
    //   180: aload_0
    //   181: getfield 77	cn/domob/android/offerwall/e:j	Ljava/util/HashMap;
    //   184: aload 19
    //   186: invokevirtual 297	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   189: checkcast 87	java/lang/String
    //   192: astore 20
    //   194: aload 20
    //   196: ifnull -38 -> 158
    //   199: aload_0
    //   200: getfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   203: aload 19
    //   205: aload 20
    //   207: invokevirtual 300	java/net/HttpURLConnection:addRequestProperty	(Ljava/lang/String;Ljava/lang/String;)V
    //   210: goto -52 -> 158
    //   213: astore 6
    //   215: aconst_null
    //   216: astore 7
    //   218: getstatic 62	cn/domob/android/offerwall/e:d	Lcn/domob/android/offerwall/m;
    //   221: aload_0
    //   222: ldc_w 302
    //   225: invokevirtual 218	cn/domob/android/offerwall/m:e	(Ljava/lang/Object;Ljava/lang/String;)V
    //   228: getstatic 62	cn/domob/android/offerwall/e:d	Lcn/domob/android/offerwall/m;
    //   231: aload 6
    //   233: invokevirtual 221	cn/domob/android/offerwall/m:a	(Ljava/lang/Throwable;)V
    //   236: aload_1
    //   237: ifnull +7 -> 244
    //   240: aload_1
    //   241: invokevirtual 305	java/io/BufferedInputStream:close	()V
    //   244: aload 7
    //   246: ifnull +8 -> 254
    //   249: aload 7
    //   251: invokevirtual 308	java/io/ByteArrayOutputStream:close	()V
    //   254: return
    //   255: aload_0
    //   256: aload_0
    //   257: getfield 116	cn/domob/android/offerwall/e:g	Ljava/net/URL;
    //   260: invokevirtual 311	java/net/URL:openConnection	()Ljava/net/URLConnection;
    //   263: checkcast 246	java/net/HttpURLConnection
    //   266: putfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   269: goto -200 -> 69
    //   272: astore_2
    //   273: aconst_null
    //   274: astore_3
    //   275: aload_3
    //   276: ifnull +7 -> 283
    //   279: aload_3
    //   280: invokevirtual 305	java/io/BufferedInputStream:close	()V
    //   283: aload_1
    //   284: ifnull +7 -> 291
    //   287: aload_1
    //   288: invokevirtual 308	java/io/ByteArrayOutputStream:close	()V
    //   291: aload_2
    //   292: athrow
    //   293: aload_0
    //   294: getfield 79	cn/domob/android/offerwall/e:h	Ljava/lang/String;
    //   297: ldc 13
    //   299: invokevirtual 95	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   302: ifeq +201 -> 503
    //   305: aload_0
    //   306: getfield 81	cn/domob/android/offerwall/e:l	Ljava/lang/String;
    //   309: ifnull +194 -> 503
    //   312: getstatic 62	cn/domob/android/offerwall/e:d	Lcn/domob/android/offerwall/m;
    //   315: ldc_w 313
    //   318: invokevirtual 244	cn/domob/android/offerwall/m:b	(Ljava/lang/String;)V
    //   321: aload_0
    //   322: getfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   325: ldc 13
    //   327: invokevirtual 316	java/net/HttpURLConnection:setRequestMethod	(Ljava/lang/String;)V
    //   330: aload_0
    //   331: getfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   334: iconst_1
    //   335: invokevirtual 319	java/net/HttpURLConnection:setDoOutput	(Z)V
    //   338: aload_0
    //   339: getfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   342: ldc_w 321
    //   345: aload_0
    //   346: getfield 120	cn/domob/android/offerwall/e:o	Ljava/lang/String;
    //   349: invokevirtual 262	java/net/HttpURLConnection:setRequestProperty	(Ljava/lang/String;Ljava/lang/String;)V
    //   352: aload_0
    //   353: getfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   356: ldc_w 323
    //   359: aload_0
    //   360: getfield 81	cn/domob/android/offerwall/e:l	Ljava/lang/String;
    //   363: invokevirtual 91	java/lang/String:length	()I
    //   366: invokestatic 326	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   369: invokevirtual 262	java/net/HttpURLConnection:setRequestProperty	(Ljava/lang/String;Ljava/lang/String;)V
    //   372: aload_0
    //   373: getfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   376: invokevirtual 330	java/net/HttpURLConnection:getOutputStream	()Ljava/io/OutputStream;
    //   379: astore 16
    //   381: new 332	java/io/BufferedWriter
    //   384: dup
    //   385: new 334	java/io/OutputStreamWriter
    //   388: dup
    //   389: aload 16
    //   391: invokespecial 337	java/io/OutputStreamWriter:<init>	(Ljava/io/OutputStream;)V
    //   394: sipush 4096
    //   397: invokespecial 340	java/io/BufferedWriter:<init>	(Ljava/io/Writer;I)V
    //   400: astore 17
    //   402: aload 17
    //   404: ifnull +27 -> 431
    //   407: aload 17
    //   409: aload_0
    //   410: getfield 81	cn/domob/android/offerwall/e:l	Ljava/lang/String;
    //   413: invokevirtual 343	java/io/BufferedWriter:write	(Ljava/lang/String;)V
    //   416: aload 17
    //   418: invokevirtual 346	java/io/BufferedWriter:flush	()V
    //   421: aload 17
    //   423: invokevirtual 347	java/io/BufferedWriter:close	()V
    //   426: aload 16
    //   428: invokevirtual 350	java/io/OutputStream:close	()V
    //   431: aload_0
    //   432: aload_0
    //   433: getfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   436: invokevirtual 353	java/net/HttpURLConnection:getResponseCode	()I
    //   439: putfield 355	cn/domob/android/offerwall/e:q	I
    //   442: aload_0
    //   443: getfield 355	cn/domob/android/offerwall/e:q	I
    //   446: sipush 301
    //   449: if_icmpeq +13 -> 462
    //   452: aload_0
    //   453: getfield 355	cn/domob/android/offerwall/e:q	I
    //   456: sipush 302
    //   459: if_icmpne +63 -> 522
    //   462: aload_0
    //   463: new 103	java/net/URL
    //   466: dup
    //   467: aload_0
    //   468: getfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   471: ldc_w 357
    //   474: invokevirtual 361	java/net/HttpURLConnection:getHeaderField	(Ljava/lang/String;)Ljava/lang/String;
    //   477: invokespecial 114	java/net/URL:<init>	(Ljava/lang/String;)V
    //   480: invokevirtual 311	java/net/URL:openConnection	()Ljava/net/URLConnection;
    //   483: checkcast 246	java/net/HttpURLConnection
    //   486: putfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   489: aload_0
    //   490: aload_0
    //   491: getfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   494: invokevirtual 353	java/net/HttpURLConnection:getResponseCode	()I
    //   497: putfield 355	cn/domob/android/offerwall/e:q	I
    //   500: goto -58 -> 442
    //   503: getstatic 62	cn/domob/android/offerwall/e:d	Lcn/domob/android/offerwall/m;
    //   506: ldc_w 363
    //   509: invokevirtual 244	cn/domob/android/offerwall/m:b	(Ljava/lang/String;)V
    //   512: aload_0
    //   513: getfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   516: invokevirtual 366	java/net/HttpURLConnection:connect	()V
    //   519: goto -88 -> 431
    //   522: aload_0
    //   523: getfield 355	cn/domob/android/offerwall/e:q	I
    //   526: sipush 200
    //   529: if_icmplt +284 -> 813
    //   532: aload_0
    //   533: getfield 355	cn/domob/android/offerwall/e:q	I
    //   536: sipush 304
    //   539: if_icmpgt +274 -> 813
    //   542: getstatic 62	cn/domob/android/offerwall/e:d	Lcn/domob/android/offerwall/m;
    //   545: new 105	java/lang/StringBuilder
    //   548: dup
    //   549: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   552: ldc_w 368
    //   555: invokevirtual 110	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   558: aload_0
    //   559: getfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   562: invokevirtual 372	java/net/HttpURLConnection:getURL	()Ljava/net/URL;
    //   565: invokevirtual 375	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   568: invokevirtual 113	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   571: invokevirtual 244	cn/domob/android/offerwall/m:b	(Ljava/lang/String;)V
    //   574: new 304	java/io/BufferedInputStream
    //   577: dup
    //   578: aload_0
    //   579: getfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   582: invokevirtual 379	java/net/HttpURLConnection:getInputStream	()Ljava/io/InputStream;
    //   585: sipush 4096
    //   588: invokespecial 382	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;I)V
    //   591: astore_3
    //   592: sipush 4096
    //   595: newarray byte
    //   597: astore 14
    //   599: new 307	java/io/ByteArrayOutputStream
    //   602: dup
    //   603: sipush 4096
    //   606: invokespecial 384	java/io/ByteArrayOutputStream:<init>	(I)V
    //   609: astore 7
    //   611: aload_3
    //   612: aload 14
    //   614: invokevirtual 388	java/io/BufferedInputStream:read	([B)I
    //   617: istore 15
    //   619: iload 15
    //   621: iconst_m1
    //   622: if_icmpeq +16 -> 638
    //   625: aload 7
    //   627: aload 14
    //   629: iconst_0
    //   630: iload 15
    //   632: invokevirtual 391	java/io/ByteArrayOutputStream:write	([BII)V
    //   635: goto -24 -> 611
    //   638: aload_0
    //   639: aload 7
    //   641: invokevirtual 395	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   644: putfield 397	cn/domob/android/offerwall/e:s	[B
    //   647: aload_0
    //   648: new 87	java/lang/String
    //   651: dup
    //   652: aload_0
    //   653: getfield 397	cn/domob/android/offerwall/e:s	[B
    //   656: ldc_w 399
    //   659: invokespecial 402	java/lang/String:<init>	([BLjava/lang/String;)V
    //   662: putfield 404	cn/domob/android/offerwall/e:r	Ljava/lang/String;
    //   665: aload_3
    //   666: astore_1
    //   667: aload_0
    //   668: getfield 256	cn/domob/android/offerwall/e:e	Ljava/net/HttpURLConnection;
    //   671: invokevirtual 407	java/net/HttpURLConnection:disconnect	()V
    //   674: aload_1
    //   675: astore 10
    //   677: aload 7
    //   679: astore 11
    //   681: aload 10
    //   683: ifnull +8 -> 691
    //   686: aload 10
    //   688: invokevirtual 305	java/io/BufferedInputStream:close	()V
    //   691: aload 11
    //   693: ifnull -439 -> 254
    //   696: aload 11
    //   698: invokevirtual 308	java/io/ByteArrayOutputStream:close	()V
    //   701: return
    //   702: astore 12
    //   704: getstatic 62	cn/domob/android/offerwall/e:d	Lcn/domob/android/offerwall/m;
    //   707: aload 12
    //   709: invokevirtual 221	cn/domob/android/offerwall/m:a	(Ljava/lang/Throwable;)V
    //   712: return
    //   713: astore 13
    //   715: getstatic 62	cn/domob/android/offerwall/e:d	Lcn/domob/android/offerwall/m;
    //   718: aload 13
    //   720: invokevirtual 221	cn/domob/android/offerwall/m:a	(Ljava/lang/Throwable;)V
    //   723: goto -32 -> 691
    //   726: astore 9
    //   728: getstatic 62	cn/domob/android/offerwall/e:d	Lcn/domob/android/offerwall/m;
    //   731: aload 9
    //   733: invokevirtual 221	cn/domob/android/offerwall/m:a	(Ljava/lang/Throwable;)V
    //   736: goto -492 -> 244
    //   739: astore 8
    //   741: getstatic 62	cn/domob/android/offerwall/e:d	Lcn/domob/android/offerwall/m;
    //   744: aload 8
    //   746: invokevirtual 221	cn/domob/android/offerwall/m:a	(Ljava/lang/Throwable;)V
    //   749: return
    //   750: astore 5
    //   752: getstatic 62	cn/domob/android/offerwall/e:d	Lcn/domob/android/offerwall/m;
    //   755: aload 5
    //   757: invokevirtual 221	cn/domob/android/offerwall/m:a	(Ljava/lang/Throwable;)V
    //   760: goto -477 -> 283
    //   763: astore 4
    //   765: getstatic 62	cn/domob/android/offerwall/e:d	Lcn/domob/android/offerwall/m;
    //   768: aload 4
    //   770: invokevirtual 221	cn/domob/android/offerwall/m:a	(Ljava/lang/Throwable;)V
    //   773: goto -482 -> 291
    //   776: astore_2
    //   777: aconst_null
    //   778: astore_1
    //   779: goto -504 -> 275
    //   782: astore_2
    //   783: aload 7
    //   785: astore_1
    //   786: goto -511 -> 275
    //   789: astore_2
    //   790: aload_1
    //   791: astore_3
    //   792: aload 7
    //   794: astore_1
    //   795: goto -520 -> 275
    //   798: astore 6
    //   800: aload_3
    //   801: astore_1
    //   802: aconst_null
    //   803: astore 7
    //   805: goto -587 -> 218
    //   808: astore 6
    //   810: goto -592 -> 218
    //   813: aconst_null
    //   814: astore 7
    //   816: aconst_null
    //   817: astore_1
    //   818: goto -151 -> 667
    //   821: aconst_null
    //   822: astore 10
    //   824: aconst_null
    //   825: astore 11
    //   827: goto -146 -> 681
    //   830: astore 6
    //   832: aload_3
    //   833: astore_1
    //   834: goto -616 -> 218
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
    //   462	500	213	java/lang/Exception
    //   503	519	213	java/lang/Exception
    //   522	592	213	java/lang/Exception
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
    //   462	500	272	finally
    //   503	519	272	finally
    //   522	592	272	finally
    //   696	701	702	java/io/IOException
    //   686	691	713	java/io/IOException
    //   240	244	726	java/io/IOException
    //   249	254	739	java/io/IOException
    //   279	283	750	java/io/IOException
    //   287	291	763	java/io/IOException
    //   592	611	776	finally
    //   611	619	782	finally
    //   625	635	782	finally
    //   638	665	782	finally
    //   218	236	789	finally
    //   667	674	789	finally
    //   592	611	798	java/lang/Exception
    //   667	674	808	java/lang/Exception
    //   611	619	830	java/lang/Exception
    //   625	635	830	java/lang/Exception
    //   638	665	830	java/lang/Exception
  }

  protected byte[] d()
  {
    return this.s;
  }

  protected String e()
  {
    return this.r;
  }

  protected int f()
  {
    return this.q;
  }

  protected String g()
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
    public abstract void a(e parame);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.android.offerwall.e
 * JD-Core Version:    0.6.0
 */