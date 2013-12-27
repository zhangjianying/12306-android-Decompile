package cn.domob.android.offerwall;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

public class l
{
  private static m a = new m(l.class.getSimpleName());

  public static Drawable a(Context paramContext, String paramString)
  {
    try
    {
      BitmapDrawable localBitmapDrawable = new BitmapDrawable(BitmapFactory.decodeStream(paramContext.getAssets().open(paramString)));
      return localBitmapDrawable;
    }
    catch (IOException localIOException)
    {
      a.a(localIOException);
      a.e("Failed to load source file:" + paramString);
    }
    return null;
  }

  public static String a()
  {
    try
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(Integer.valueOf("020000".substring(0, 2))).append(".").append(Integer.valueOf("020000".substring(2, 4))).append(".").append(Integer.valueOf("020000".substring(4, 6)));
      String str = localStringBuilder.toString();
      return str;
    }
    catch (Exception localException)
    {
      a.a(localException);
    }
    return "020000";
  }

  public static String a(HashMap<String, String> paramHashMap)
  {
    ArrayList localArrayList;
    StringBuilder localStringBuilder;
    try
    {
      localArrayList = new ArrayList();
      localStringBuilder = new StringBuilder();
      Iterator localIterator = paramHashMap.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str3 = (String)localIterator.next();
        localArrayList.add(new BasicNameValuePair(str3, (String)paramHashMap.get(str3)));
      }
    }
    catch (Exception localException)
    {
      a.a(localException);
      return null;
    }
    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(new UrlEncodedFormEntity(localArrayList, "UTF-8").getContent()));
    while (true)
    {
      String str1 = localBufferedReader.readLine();
      if (str1 == null)
        break;
      localStringBuilder.append(str1);
    }
    String str2 = localStringBuilder.toString();
    return str2;
  }

  private static String a(byte[] paramArrayOfByte, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = paramArrayOfByte.length;
    int j = 0;
    if (j < i)
    {
      String str = Integer.toHexString(0xFF & paramArrayOfByte[j]);
      if (str.length() == 1)
        localStringBuilder.append("0").append(str);
      while (true)
      {
        j++;
        break;
        localStringBuilder.append(str);
      }
    }
    return localStringBuilder.toString();
  }

  public static HashMap<String, String> a(String paramString)
  {
    HashMap localHashMap = new HashMap();
    String[] arrayOfString1;
    int i;
    if (paramString != null)
    {
      arrayOfString1 = paramString.split("&");
      i = arrayOfString1.length;
    }
    for (int j = 0; ; j++)
      if (j < i)
      {
        String[] arrayOfString2 = arrayOfString1[j].split("=");
        try
        {
          if (arrayOfString2.length == 2)
          {
            localHashMap.put(URLDecoder.decode(arrayOfString2[0], "UTF-8"), URLDecoder.decode(arrayOfString2[1], "UTF-8"));
            continue;
          }
          localHashMap.put(URLDecoder.decode(arrayOfString2[0], "UTF-8"), "");
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException)
        {
          a.a(localUnsupportedEncodingException);
          a.e("URL decode params String error:" + paramString);
        }
      }
      else
      {
        return localHashMap;
      }
  }

  public static void a(Context paramContext, String paramString1, String paramString2)
  {
    new AlertDialog.Builder(paramContext).setTitle(paramString1).setMessage(paramString2).setNegativeButton("确定", new DialogInterface.OnClickListener(paramContext)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        ((Activity)this.a).finish();
      }
    }).show();
  }

  public static boolean a(Context paramContext)
  {
    StringBuilder localStringBuilder = new StringBuilder("DomobSDK 缺少权限：\n");
    if ((-1 == paramContext.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE")) && (f.a(3, false)))
    {
      Log.e("DomobSDK", "you must have android.permission.WRITE_EXTERNAL_STORAGE permission !");
      localStringBuilder.append("android.permission.WRITE_EXTERNAL_STORAGE \n");
    }
    for (int i = 1; ; i = 0)
    {
      if (-1 == paramContext.checkCallingOrSelfPermission("android.permission.INTERNET"))
      {
        Log.e("DomobSDK", "you must have android.permission.INTERNET permission !");
        localStringBuilder.append("android.permission.INTERNET \n");
        i = 1;
      }
      if (-1 == paramContext.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE"))
      {
        Log.e("DomobSDK", "you must have android.permission.READ_PHONE_STATE permission !");
        localStringBuilder.append("android.permission.READ_PHONE_STATE \n");
        i = 1;
      }
      if (-1 == paramContext.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE"))
      {
        Log.e("DomobSDK", "you must have android.permission.ACCESS_NETWORK_STATE permission !");
        localStringBuilder.append("android.permission.ACCESS_NETWORK_STATE \n");
        i = 1;
      }
      if (-1 == paramContext.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION"))
      {
        Log.e("DomobSDK", "you must have android.permission.ACCESS_COARSE_LOCATION permission !");
        localStringBuilder.append("android.permission.ACCESS_COARSE_LOCATION \n");
        i = 1;
      }
      if (i != 0)
        a(paramContext, "DomobSDK", localStringBuilder.toString());
      return i == 0;
    }
  }

  public static String b()
  {
    try
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("20120809".substring(0, 4)).append("-").append("20120809".substring(4, 6)).append("-").append("20120809".substring(6, 8));
      String str = localStringBuilder.toString();
      return str;
    }
    catch (Exception localException)
    {
      a.a(localException);
    }
    return "20120809";
  }

  public static String b(Context paramContext, String paramString)
  {
    String str1 = Build.MODEL;
    String str2 = "1.5";
    if (Build.VERSION.RELEASE.length() > 0)
      str2 = Build.VERSION.RELEASE;
    String str3 = String.format("<script>var SDK_VERSION = '%s'; var DEVICE = '%s'; var OS = '%s'; var OS_VERSION = '%s'; var CARRIER = '%s'; var NETWORK = '%s';</script>", new Object[] { "020000", str1, "android", str2, f.q(paramContext), f.o(paramContext) });
    return str3 + paramString;
  }

  public static String b(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0))
      return "";
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.reset();
      localMessageDigest.update(paramString.getBytes("UTF-8"));
      String str = a(localMessageDigest.digest(), "");
      return str;
    }
    catch (Exception localException)
    {
      a.a(localException);
    }
    return "";
  }

  // ERROR //
  public static String c(String paramString)
  {
    // Byte code:
    //   0: new 334	java/io/FileInputStream
    //   3: dup
    //   4: aload_0
    //   5: invokespecial 335	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   8: astore_1
    //   9: sipush 1024
    //   12: newarray byte
    //   14: astore 7
    //   16: ldc_w 308
    //   19: invokestatic 314	java/security/MessageDigest:getInstance	(Ljava/lang/String;)Ljava/security/MessageDigest;
    //   22: astore 8
    //   24: aload_1
    //   25: aload 7
    //   27: invokevirtual 341	java/io/InputStream:read	([B)I
    //   30: istore 9
    //   32: iload 9
    //   34: ifle +65 -> 99
    //   37: aload 8
    //   39: aload 7
    //   41: iconst_0
    //   42: iload 9
    //   44: invokevirtual 344	java/security/MessageDigest:update	([BII)V
    //   47: goto -23 -> 24
    //   50: astore 4
    //   52: getstatic 22	cn/domob/android/offerwall/l:a	Lcn/domob/android/offerwall/m;
    //   55: ldc 191
    //   57: new 55	java/lang/StringBuilder
    //   60: dup
    //   61: invokespecial 56	java/lang/StringBuilder:<init>	()V
    //   64: ldc_w 346
    //   67: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   70: aload 4
    //   72: invokevirtual 349	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   75: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   78: invokevirtual 65	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   81: invokevirtual 352	cn/domob/android/offerwall/m:e	(Ljava/lang/Object;Ljava/lang/String;)V
    //   84: aload_1
    //   85: ifnull +7 -> 92
    //   88: aload_1
    //   89: invokevirtual 355	java/io/InputStream:close	()V
    //   92: ldc 191
    //   94: astore 5
    //   96: aload 5
    //   98: areturn
    //   99: aload 8
    //   101: invokevirtual 329	java/security/MessageDigest:digest	()[B
    //   104: ldc 191
    //   106: invokestatic 331	cn/domob/android/offerwall/l:a	([BLjava/lang/String;)Ljava/lang/String;
    //   109: astore 10
    //   111: aload 10
    //   113: astore 5
    //   115: aload_1
    //   116: ifnull -20 -> 96
    //   119: aload_1
    //   120: invokevirtual 355	java/io/InputStream:close	()V
    //   123: aload 5
    //   125: areturn
    //   126: astore 11
    //   128: getstatic 22	cn/domob/android/offerwall/l:a	Lcn/domob/android/offerwall/m;
    //   131: aload 11
    //   133: invokevirtual 53	cn/domob/android/offerwall/m:a	(Ljava/lang/Throwable;)V
    //   136: aload 5
    //   138: areturn
    //   139: astore 6
    //   141: getstatic 22	cn/domob/android/offerwall/l:a	Lcn/domob/android/offerwall/m;
    //   144: aload 6
    //   146: invokevirtual 53	cn/domob/android/offerwall/m:a	(Ljava/lang/Throwable;)V
    //   149: goto -57 -> 92
    //   152: astore_2
    //   153: aconst_null
    //   154: astore_1
    //   155: aload_1
    //   156: ifnull +7 -> 163
    //   159: aload_1
    //   160: invokevirtual 355	java/io/InputStream:close	()V
    //   163: aload_2
    //   164: athrow
    //   165: astore_3
    //   166: getstatic 22	cn/domob/android/offerwall/l:a	Lcn/domob/android/offerwall/m;
    //   169: aload_3
    //   170: invokevirtual 53	cn/domob/android/offerwall/m:a	(Ljava/lang/Throwable;)V
    //   173: goto -10 -> 163
    //   176: astore_2
    //   177: goto -22 -> 155
    //   180: astore 4
    //   182: aconst_null
    //   183: astore_1
    //   184: goto -132 -> 52
    //
    // Exception table:
    //   from	to	target	type
    //   9	24	50	java/lang/Exception
    //   24	32	50	java/lang/Exception
    //   37	47	50	java/lang/Exception
    //   99	111	50	java/lang/Exception
    //   119	123	126	java/io/IOException
    //   88	92	139	java/io/IOException
    //   0	9	152	finally
    //   159	163	165	java/io/IOException
    //   9	24	176	finally
    //   24	32	176	finally
    //   37	47	176	finally
    //   52	84	176	finally
    //   99	111	176	finally
    //   0	9	180	java/lang/Exception
  }

  public static boolean d(String paramString)
  {
    return Pattern.compile("[0-9]*").matcher(paramString).matches();
  }

  public static boolean e(String paramString)
  {
    return (paramString == null) || (paramString.length() == 0);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.android.offerwall.l
 * JD-Core Version:    0.6.0
 */