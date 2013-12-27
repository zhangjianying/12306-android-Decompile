package cn.domob.wall.core.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import cn.domob.wall.core.AdInfo;
import cn.domob.wall.core.p;
import java.util.Hashtable;

public class a extends BroadcastReceiver
{
  private static p a = new p(a.class.getSimpleName());
  private static a b;
  private static Hashtable<String, AdInfo> d;
  private a c;

  public static a a()
  {
    if (b != null)
      return b;
    a.e("ActionReceiver needs to be initialized first.");
    return null;
  }

  public static void a(Context paramContext, a parama)
  {
    if (b == null)
    {
      b = new a();
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addAction("android.intent.action.PACKAGE_ADDED");
      localIntentFilter.addDataScheme("package");
      paramContext.getApplicationContext().registerReceiver(b, localIntentFilter);
      b.c = parama;
      a.b("Finish to init action receiver.");
      return;
    }
    a.b("Action receiver has already been initialized.");
  }

  public void a(AdInfo paramAdInfo)
  {
    if (d == null)
      d = new Hashtable();
    String str = paramAdInfo.getAdPackageName();
    if (str != null)
    {
      d.put(str, paramAdInfo);
      return;
    }
    a.e("There is no package name in ad response.");
  }

  protected void a(String paramString)
  {
    if (d == null)
      d = new Hashtable();
    if (d.containsKey(paramString))
    {
      a.a("Remove info : " + d.get(paramString));
      d.remove(paramString);
      return;
    }
    a.e("There is no key like " + paramString + " in regPkgTalbe.");
  }

  // ERROR //
  public void onReceive(Context paramContext, android.content.Intent paramIntent)
  {
    // Byte code:
    //   0: getstatic 75	cn/domob/wall/core/download/a:d	Ljava/util/Hashtable;
    //   3: ifnonnull +4 -> 7
    //   6: return
    //   7: aload_2
    //   8: invokevirtual 132	android/content/Intent:getAction	()Ljava/lang/String;
    //   11: astore 4
    //   13: getstatic 29	cn/domob/wall/core/download/a:a	Lcn/domob/wall/core/p;
    //   16: new 95	java/lang/StringBuilder
    //   19: dup
    //   20: invokespecial 96	java/lang/StringBuilder:<init>	()V
    //   23: ldc 134
    //   25: invokevirtual 102	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: aload_2
    //   29: invokevirtual 135	android/content/Intent:toString	()Ljava/lang/String;
    //   32: invokevirtual 102	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   35: invokevirtual 112	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   38: invokevirtual 114	cn/domob/wall/core/p:a	(Ljava/lang/String;)V
    //   41: ldc 46
    //   43: aload 4
    //   45: invokevirtual 140	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   48: ifeq +316 -> 364
    //   51: aload_2
    //   52: invokevirtual 143	android/content/Intent:getDataString	()Ljava/lang/String;
    //   55: bipush 8
    //   57: invokevirtual 147	java/lang/String:substring	(I)Ljava/lang/String;
    //   60: astore 5
    //   62: aload_1
    //   63: invokevirtual 151	android/content/Context:getPackageManager	()Landroid/content/pm/PackageManager;
    //   66: aload 5
    //   68: iconst_0
    //   69: invokevirtual 157	android/content/pm/PackageManager:getPackageInfo	(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;
    //   72: getfield 163	android/content/pm/PackageInfo:packageName	Ljava/lang/String;
    //   75: astore 7
    //   77: getstatic 75	cn/domob/wall/core/download/a:d	Ljava/util/Hashtable;
    //   80: aload 7
    //   82: invokevirtual 93	java/util/Hashtable:containsKey	(Ljava/lang/Object;)Z
    //   85: ifeq +253 -> 338
    //   88: aload_0
    //   89: getfield 66	cn/domob/wall/core/download/a:c	Lcn/domob/wall/core/download/a$a;
    //   92: ifnull +23 -> 115
    //   95: aload_0
    //   96: getfield 66	cn/domob/wall/core/download/a:c	Lcn/domob/wall/core/download/a$a;
    //   99: getstatic 75	cn/domob/wall/core/download/a:d	Ljava/util/Hashtable;
    //   102: aload 7
    //   104: invokevirtual 106	java/util/Hashtable:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   107: checkcast 80	cn/domob/wall/core/AdInfo
    //   110: invokeinterface 168 2 0
    //   115: getstatic 75	cn/domob/wall/core/download/a:d	Ljava/util/Hashtable;
    //   118: aload 7
    //   120: invokevirtual 106	java/util/Hashtable:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   123: ifnull +168 -> 291
    //   126: getstatic 29	cn/domob/wall/core/download/a:a	Lcn/domob/wall/core/p;
    //   129: new 95	java/lang/StringBuilder
    //   132: dup
    //   133: invokespecial 96	java/lang/StringBuilder:<init>	()V
    //   136: ldc 170
    //   138: invokevirtual 102	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   141: aload 7
    //   143: invokevirtual 102	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   146: invokevirtual 112	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   149: invokevirtual 114	cn/domob/wall/core/p:a	(Ljava/lang/String;)V
    //   152: aload_1
    //   153: invokevirtual 151	android/content/Context:getPackageManager	()Landroid/content/pm/PackageManager;
    //   156: aload 7
    //   158: invokevirtual 174	android/content/pm/PackageManager:getLaunchIntentForPackage	(Ljava/lang/String;)Landroid/content/Intent;
    //   161: astore 10
    //   163: aload 10
    //   165: ifnull +97 -> 262
    //   168: aload 10
    //   170: ldc 175
    //   172: invokevirtual 179	android/content/Intent:setFlags	(I)Landroid/content/Intent;
    //   175: pop
    //   176: aload_1
    //   177: aload 10
    //   179: invokevirtual 183	android/content/Context:startActivity	(Landroid/content/Intent;)V
    //   182: aload_0
    //   183: getfield 66	cn/domob/wall/core/download/a:c	Lcn/domob/wall/core/download/a$a;
    //   186: ifnull +23 -> 209
    //   189: aload_0
    //   190: getfield 66	cn/domob/wall/core/download/a:c	Lcn/domob/wall/core/download/a$a;
    //   193: getstatic 75	cn/domob/wall/core/download/a:d	Ljava/util/Hashtable;
    //   196: aload 7
    //   198: invokevirtual 106	java/util/Hashtable:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   201: checkcast 80	cn/domob/wall/core/AdInfo
    //   204: invokeinterface 186 2 0
    //   209: aload_0
    //   210: aload 7
    //   212: invokevirtual 187	cn/domob/wall/core/download/a:a	(Ljava/lang/String;)V
    //   215: return
    //   216: astore_3
    //   217: getstatic 29	cn/domob/wall/core/download/a:a	Lcn/domob/wall/core/p;
    //   220: aload_3
    //   221: invokevirtual 190	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   224: return
    //   225: astore 6
    //   227: getstatic 29	cn/domob/wall/core/download/a:a	Lcn/domob/wall/core/p;
    //   230: new 95	java/lang/StringBuilder
    //   233: dup
    //   234: invokespecial 96	java/lang/StringBuilder:<init>	()V
    //   237: ldc 192
    //   239: invokevirtual 102	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   242: ldc 194
    //   244: invokevirtual 102	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   247: invokevirtual 112	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   250: invokevirtual 39	cn/domob/wall/core/p:e	(Ljava/lang/String;)V
    //   253: getstatic 29	cn/domob/wall/core/download/a:a	Lcn/domob/wall/core/p;
    //   256: aload 6
    //   258: invokevirtual 190	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   261: return
    //   262: getstatic 29	cn/domob/wall/core/download/a:a	Lcn/domob/wall/core/p;
    //   265: new 95	java/lang/StringBuilder
    //   268: dup
    //   269: invokespecial 96	java/lang/StringBuilder:<init>	()V
    //   272: ldc 192
    //   274: invokevirtual 102	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   277: aload 7
    //   279: invokevirtual 102	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   282: invokevirtual 112	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   285: invokevirtual 39	cn/domob/wall/core/p:e	(Ljava/lang/String;)V
    //   288: goto -79 -> 209
    //   291: getstatic 198	cn/domob/wall/core/download/b:b	Ljava/util/Hashtable;
    //   294: aload 7
    //   296: invokevirtual 106	java/util/Hashtable:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   299: checkcast 200	java/lang/Integer
    //   302: astore 8
    //   304: aload 8
    //   306: ifnull -97 -> 209
    //   309: getstatic 198	cn/domob/wall/core/download/b:b	Ljava/util/Hashtable;
    //   312: aload 7
    //   314: invokevirtual 106	java/util/Hashtable:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   317: pop
    //   318: aload_1
    //   319: ldc 202
    //   321: invokevirtual 206	android/content/Context:getSystemService	(Ljava/lang/String;)Ljava/lang/Object;
    //   324: checkcast 208	android/app/NotificationManager
    //   327: aload 8
    //   329: invokevirtual 212	java/lang/Integer:intValue	()I
    //   332: invokevirtual 216	android/app/NotificationManager:cancel	(I)V
    //   335: goto -126 -> 209
    //   338: getstatic 29	cn/domob/wall/core/download/a:a	Lcn/domob/wall/core/p;
    //   341: new 95	java/lang/StringBuilder
    //   344: dup
    //   345: invokespecial 96	java/lang/StringBuilder:<init>	()V
    //   348: ldc 218
    //   350: invokevirtual 102	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   353: aload 7
    //   355: invokevirtual 102	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   358: invokevirtual 112	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   361: invokevirtual 39	cn/domob/wall/core/p:e	(Ljava/lang/String;)V
    //   364: return
    //
    // Exception table:
    //   from	to	target	type
    //   0	6	216	java/lang/Exception
    //   7	62	216	java/lang/Exception
    //   62	77	216	java/lang/Exception
    //   77	115	216	java/lang/Exception
    //   115	163	216	java/lang/Exception
    //   168	209	216	java/lang/Exception
    //   209	215	216	java/lang/Exception
    //   227	261	216	java/lang/Exception
    //   262	288	216	java/lang/Exception
    //   291	304	216	java/lang/Exception
    //   309	335	216	java/lang/Exception
    //   338	364	216	java/lang/Exception
    //   62	77	225	android/content/pm/PackageManager$NameNotFoundException
  }

  public static abstract interface a
  {
    public abstract void p(AdInfo paramAdInfo);

    public abstract void q(AdInfo paramAdInfo);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.download.a
 * JD-Core Version:    0.6.0
 */