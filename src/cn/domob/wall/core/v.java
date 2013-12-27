package cn.domob.wall.core;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

public class v
{
  private static p a = new p(v.class.getSimpleName());
  private static final String b = "DES";
  private static final String c = "com.android.browser";
  private static final String d = "com.google.android.browser";
  private static final String e = "com.android.browser.BrowserActivity";
  private static final char[] f = { 100, 111, 109, 111, 98 };

  public static Intent a(Context paramContext, Uri paramUri)
  {
    Intent localIntent;
    if ((paramContext != null) && (paramUri != null))
    {
      if (!s.a(paramContext, "com.android.browser"))
        break label52;
      localIntent = new Intent("android.intent.action.VIEW", paramUri);
      localIntent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
    }
    while (true)
    {
      if (paramContext.getPackageManager().resolveActivity(localIntent, 65536) == null)
      {
        return null;
        label52: if (s.a(paramContext, "com.google.android.browser"))
        {
          localIntent = new Intent("android.intent.action.VIEW", paramUri);
          localIntent.setClassName("com.google.android.browser", "com.android.browser.BrowserActivity");
          continue;
        }
      }
      else
      {
        return localIntent;
      }
      localIntent = null;
    }
  }

  public static String a()
  {
    return new String(f);
  }

  public static String a(String paramString1, String paramString2)
  {
    try
    {
      byte[] arrayOfByte1 = paramString1.getBytes("UTF-8");
      byte[] arrayOfByte2 = new byte[16];
      System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, Math.min(arrayOfByte1.length, 16));
      byte[] arrayOfByte3 = paramString2.getBytes("UTF-8");
      SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte2, "AES");
      Cipher localCipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
      localCipher.init(1, localSecretKeySpec);
      String str = new String(a.b(localCipher.doFinal(arrayOfByte3), 2));
      return str;
    }
    catch (Exception localException)
    {
    }
    return "";
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
    StringBuilder localStringBuilder = new StringBuilder("DrwSDK 缺少权限：\n");
    if ((-1 == paramContext.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE")) && (s.a(3, false)))
    {
      Log.e("DrwSDK", "you must have android.permission.WRITE_EXTERNAL_STORAGE permission !");
      localStringBuilder.append("android.permission.WRITE_EXTERNAL_STORAGE \n");
    }
    for (int i = 1; ; i = 0)
    {
      if (-1 == paramContext.checkCallingOrSelfPermission("android.permission.INTERNET"))
      {
        Log.e("DrwSDK", "you must have android.permission.INTERNET permission !");
        localStringBuilder.append("android.permission.INTERNET \n");
        i = 1;
      }
      if (-1 == paramContext.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE"))
      {
        Log.e("DrwSDK", "you must have android.permission.READ_PHONE_STATE permission !");
        localStringBuilder.append("android.permission.READ_PHONE_STATE \n");
        i = 1;
      }
      if (-1 == paramContext.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE"))
      {
        Log.e("DrwSDK", "you must have android.permission.ACCESS_NETWORK_STATE permission !");
        localStringBuilder.append("android.permission.ACCESS_NETWORK_STATE \n");
        i = 1;
      }
      if (-1 == paramContext.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION"))
      {
        Log.e("DrwSDK", "you must have android.permission.ACCESS_COARSE_LOCATION permission !");
        localStringBuilder.append("android.permission.ACCESS_COARSE_LOCATION \n");
        i = 1;
      }
      if (i != 0)
        a(paramContext, "DrwSDK", localStringBuilder.toString());
      return i == 0;
    }
  }

  public static boolean a(Context paramContext, String paramString)
  {
    try
    {
      int i = paramContext.checkCallingOrSelfPermission(paramString);
      int j = 0;
      if (i == 0)
        j = 1;
      return j;
    }
    catch (Exception localException)
    {
      a.a(localException);
    }
    return false;
  }

  public static boolean a(String paramString)
  {
    return (paramString == null) || (paramString.length() == 0);
  }

  public static Drawable b(Context paramContext, String paramString)
    throws Exception
  {
    a.b(" load source file:" + paramString);
    try
    {
      BitmapDrawable localBitmapDrawable = new BitmapDrawable(BitmapFactory.decodeFile(paramString));
      return localBitmapDrawable;
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
      System.gc();
      a.d("out of memory");
      return null;
    }
    catch (Exception localException)
    {
      a.a(localException);
      a.e("Failed to load source file:" + paramString);
    }
    return null;
  }

  public static String b()
  {
    try
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(Integer.valueOf("010101".substring(0, 2))).append(".").append(Integer.valueOf("010101".substring(2, 4))).append(".").append(Integer.valueOf("010101".substring(4, 6)));
      String str = localStringBuilder.toString();
      return str;
    }
    catch (Exception localException)
    {
      a.a(localException);
    }
    return "010101";
  }

  public static String b(String paramString1, String paramString2)
  {
    try
    {
      DESKeySpec localDESKeySpec = new DESKeySpec(paramString1.getBytes());
      SecretKey localSecretKey = SecretKeyFactory.getInstance("DES").generateSecret(localDESKeySpec);
      Cipher localCipher = Cipher.getInstance("DES");
      localCipher.init(1, localSecretKey, new IvParameterSpec("12345678".getBytes()));
      String str = a.a(localCipher.doFinal(paramString2.getBytes()), 2);
      return str;
    }
    catch (Exception localException)
    {
      a.b("des encode error");
    }
    return null;
  }

  public static HashMap<String, String> b(String paramString)
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

  public static boolean b(Context paramContext)
  {
    if (paramContext.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") == 0)
    {
      a.c("access wifi state is enabled");
      return true;
    }
    return false;
  }

  public static String c()
  {
    try
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("20131031".substring(0, 4)).append("-").append("20131031".substring(4, 6)).append("-").append("20131031".substring(6, 8));
      String str = localStringBuilder.toString();
      return str;
    }
    catch (Exception localException)
    {
      a.a(localException);
    }
    return "20131031";
  }

  public static String c(String paramString)
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

  public static boolean c(Context paramContext)
  {
    return ((WifiManager)paramContext.getSystemService("wifi")).getWifiState() == 3;
  }

  public static boolean c(Context paramContext, String paramString)
  {
    if ((paramString != null) && (!paramString.equals("")))
      return s.a(paramContext, paramString);
    return false;
  }

  public static String d()
  {
    return new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINESE).format(new Date());
  }

  // ERROR //
  public static String d(String paramString)
  {
    // Byte code:
    //   0: new 464	java/io/FileInputStream
    //   3: dup
    //   4: aload_0
    //   5: invokespecial 465	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   8: astore_1
    //   9: sipush 1024
    //   12: newarray byte
    //   14: astore 7
    //   16: ldc_w 410
    //   19: invokestatic 415	java/security/MessageDigest:getInstance	(Ljava/lang/String;)Ljava/security/MessageDigest;
    //   22: astore 8
    //   24: aload_1
    //   25: aload 7
    //   27: invokevirtual 471	java/io/InputStream:read	([B)I
    //   30: istore 9
    //   32: iload 9
    //   34: ifle +65 -> 99
    //   37: aload 8
    //   39: aload 7
    //   41: iconst_0
    //   42: iload 9
    //   44: invokevirtual 474	java/security/MessageDigest:update	([BII)V
    //   47: goto -23 -> 24
    //   50: astore 4
    //   52: getstatic 37	cn/domob/wall/core/v:a	Lcn/domob/wall/core/p;
    //   55: ldc 134
    //   57: new 140	java/lang/StringBuilder
    //   60: dup
    //   61: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   64: ldc_w 476
    //   67: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   70: aload 4
    //   72: invokevirtual 479	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   75: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   78: invokevirtual 212	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   81: invokevirtual 482	cn/domob/wall/core/p:e	(Ljava/lang/Object;Ljava/lang/String;)V
    //   84: aload_1
    //   85: ifnull +7 -> 92
    //   88: aload_1
    //   89: invokevirtual 485	java/io/InputStream:close	()V
    //   92: ldc 134
    //   94: astore 5
    //   96: aload 5
    //   98: areturn
    //   99: aload 8
    //   101: invokevirtual 424	java/security/MessageDigest:digest	()[B
    //   104: ldc 134
    //   106: invokestatic 426	cn/domob/wall/core/v:a	([BLjava/lang/String;)Ljava/lang/String;
    //   109: astore 10
    //   111: aload 10
    //   113: astore 5
    //   115: aload_1
    //   116: ifnull -20 -> 96
    //   119: aload_1
    //   120: invokevirtual 485	java/io/InputStream:close	()V
    //   123: aload 5
    //   125: areturn
    //   126: astore 11
    //   128: getstatic 37	cn/domob/wall/core/v:a	Lcn/domob/wall/core/p;
    //   131: aload 11
    //   133: invokevirtual 181	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   136: aload 5
    //   138: areturn
    //   139: astore 6
    //   141: getstatic 37	cn/domob/wall/core/v:a	Lcn/domob/wall/core/p;
    //   144: aload 6
    //   146: invokevirtual 181	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   149: goto -57 -> 92
    //   152: astore_2
    //   153: aconst_null
    //   154: astore_1
    //   155: aload_1
    //   156: ifnull +7 -> 163
    //   159: aload_1
    //   160: invokevirtual 485	java/io/InputStream:close	()V
    //   163: aload_2
    //   164: athrow
    //   165: astore_3
    //   166: getstatic 37	cn/domob/wall/core/v:a	Lcn/domob/wall/core/p;
    //   169: aload_3
    //   170: invokevirtual 181	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
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

  public static boolean d(Context paramContext)
  {
    NetworkInfo localNetworkInfo;
    if (paramContext != null)
    {
      localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
      if (localNetworkInfo == null);
    }
    for (boolean bool = localNetworkInfo.isAvailable(); ; bool = false)
    {
      if (bool)
      {
        a.b("Check your network connection is normal");
        return bool;
      }
      a.b("Check the network connection is abnormal");
      return bool;
    }
  }

  public static boolean e(String paramString)
  {
    if (paramString == null)
      return false;
    return Pattern.compile("[0-9]*").matcher(paramString).matches();
  }

  public static String f(String paramString)
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = new String(f);
    return String.format(paramString, arrayOfObject);
  }

  public static boolean g(String paramString)
  {
    return (paramString == null) || (paramString.length() == 0);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.v
 * JD-Core Version:    0.6.0
 */