package cn.domob.wall.core.download;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import cn.domob.wall.core.p;

public class g
{
  private static p a = new p(g.class.getSimpleName());
  private static final String b = "wifi";
  private static final String c = "ctwap";

  protected static String a(Context paramContext)
  {
    String str;
    if (paramContext.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == -1)
    {
      a.e(g.class.getSimpleName(), "Cannot access user's network type.  Permissions are not set.");
      str = "";
    }
    int i;
    while (true)
    {
      return str;
      NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
      if (localNetworkInfo == null)
        break label74;
      i = localNetworkInfo.getType();
      if (i != 0)
        break;
      str = localNetworkInfo.getSubtypeName();
      if (str == null)
        return "GPRS";
    }
    if (i == 1)
      return "wifi";
    label74: return "";
  }

  // ERROR //
  protected static java.net.Proxy b(Context paramContext)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic 79	cn/domob/wall/core/download/g:c	(Landroid/content/Context;)Landroid/database/Cursor;
    //   4: astore 5
    //   6: aload 5
    //   8: astore_2
    //   9: aload_2
    //   10: ifnull +227 -> 237
    //   13: aload_2
    //   14: invokeinterface 84 1 0
    //   19: ifle +218 -> 237
    //   22: aload_2
    //   23: invokeinterface 88 1 0
    //   28: pop
    //   29: aload_2
    //   30: aload_2
    //   31: ldc 90
    //   33: invokeinterface 93 2 0
    //   38: invokeinterface 97 2 0
    //   43: astore 7
    //   45: aload_2
    //   46: aload_2
    //   47: ldc 99
    //   49: invokeinterface 93 2 0
    //   54: invokeinterface 103 2 0
    //   59: istore 8
    //   61: aload_2
    //   62: aload_2
    //   63: ldc 105
    //   65: invokeinterface 93 2 0
    //   70: invokeinterface 97 2 0
    //   75: astore 9
    //   77: getstatic 29	cn/domob/wall/core/download/g:a	Lcn/domob/wall/core/p;
    //   80: ldc 2
    //   82: invokevirtual 23	java/lang/Class:getSimpleName	()Ljava/lang/String;
    //   85: new 107	java/lang/StringBuilder
    //   88: dup
    //   89: invokespecial 108	java/lang/StringBuilder:<init>	()V
    //   92: ldc 110
    //   94: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   97: aload 7
    //   99: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   102: ldc 116
    //   104: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   107: iload 8
    //   109: invokevirtual 119	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   112: ldc 121
    //   114: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   117: aload 9
    //   119: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   122: invokevirtual 124	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   125: invokevirtual 126	cn/domob/wall/core/p:a	(Ljava/lang/Object;Ljava/lang/String;)V
    //   128: aload 7
    //   130: ifnull +162 -> 292
    //   133: aload 7
    //   135: ldc 48
    //   137: invokevirtual 132	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   140: ifne +152 -> 292
    //   143: iload 8
    //   145: ifeq +147 -> 292
    //   148: aload 9
    //   150: ldc 13
    //   152: invokevirtual 136	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   155: ifne +137 -> 292
    //   158: getstatic 29	cn/domob/wall/core/download/g:a	Lcn/domob/wall/core/p;
    //   161: ldc 2
    //   163: invokevirtual 23	java/lang/Class:getSimpleName	()Ljava/lang/String;
    //   166: new 107	java/lang/StringBuilder
    //   169: dup
    //   170: invokespecial 108	java/lang/StringBuilder:<init>	()V
    //   173: ldc 138
    //   175: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   178: aload 7
    //   180: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   183: ldc 140
    //   185: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   188: iload 8
    //   190: invokevirtual 119	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   193: invokevirtual 124	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   196: invokevirtual 126	cn/domob/wall/core/p:a	(Ljava/lang/Object;Ljava/lang/String;)V
    //   199: new 142	java/net/Proxy
    //   202: dup
    //   203: getstatic 148	java/net/Proxy$Type:HTTP	Ljava/net/Proxy$Type;
    //   206: new 150	java/net/InetSocketAddress
    //   209: dup
    //   210: aload 7
    //   212: iload 8
    //   214: invokespecial 153	java/net/InetSocketAddress:<init>	(Ljava/lang/String;I)V
    //   217: invokespecial 156	java/net/Proxy:<init>	(Ljava/net/Proxy$Type;Ljava/net/SocketAddress;)V
    //   220: astore 10
    //   222: aload 10
    //   224: astore_3
    //   225: aload_2
    //   226: ifnull +9 -> 235
    //   229: aload_2
    //   230: invokeinterface 159 1 0
    //   235: aload_3
    //   236: areturn
    //   237: aconst_null
    //   238: astore_3
    //   239: aload_2
    //   240: ifnull -5 -> 235
    //   243: aload_2
    //   244: invokeinterface 159 1 0
    //   249: aconst_null
    //   250: areturn
    //   251: astore_1
    //   252: getstatic 29	cn/domob/wall/core/download/g:a	Lcn/domob/wall/core/p;
    //   255: aload_1
    //   256: invokevirtual 162	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   259: aconst_null
    //   260: astore_3
    //   261: goto -36 -> 225
    //   264: astore 4
    //   266: aconst_null
    //   267: astore_2
    //   268: getstatic 29	cn/domob/wall/core/download/g:a	Lcn/domob/wall/core/p;
    //   271: aload 4
    //   273: invokevirtual 162	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   276: aconst_null
    //   277: astore_3
    //   278: goto -53 -> 225
    //   281: astore 4
    //   283: goto -15 -> 268
    //   286: astore_1
    //   287: aconst_null
    //   288: astore_2
    //   289: goto -37 -> 252
    //   292: aconst_null
    //   293: astore 10
    //   295: goto -73 -> 222
    //
    // Exception table:
    //   from	to	target	type
    //   13	128	251	java/lang/Exception
    //   133	143	251	java/lang/Exception
    //   148	222	251	java/lang/Exception
    //   243	249	251	java/lang/Exception
    //   0	6	264	java/lang/Error
    //   13	128	281	java/lang/Error
    //   133	143	281	java/lang/Error
    //   148	222	281	java/lang/Error
    //   243	249	281	java/lang/Error
    //   0	6	286	java/lang/Exception
  }

  private static Cursor c(Context paramContext)
  {
    String str = a(paramContext);
    if ((str != null) && (str.equals("wifi")))
    {
      a.a(g.class.getSimpleName(), "Download network is wifi, don't read apn.");
      return null;
    }
    Uri localUri = Uri.parse("content://telephony/carriers/preferapn");
    return paramContext.getContentResolver().query(localUri, null, null, null, null);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.download.g
 * JD-Core Version:    0.6.0
 */