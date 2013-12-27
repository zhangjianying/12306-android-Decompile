package cn.domob.android.offerwall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import java.util.Hashtable;

public class d extends BroadcastReceiver
{
  private static m a = new m(d.class.getSimpleName());
  private static d b;
  private static a c;
  private static Hashtable<String, g> d;

  protected static d a()
  {
    if (b != null)
      return b;
    a.e("ActionReceiver needs to be initialized first.");
    return null;
  }

  protected static void a(Context paramContext, a parama)
  {
    if (b == null)
    {
      b = new d();
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addAction("android.intent.action.PACKAGE_ADDED");
      localIntentFilter.addDataScheme("package");
      paramContext.getApplicationContext().registerReceiver(b, localIntentFilter);
      c = parama;
      a.b("Finish to init action receiver.");
      return;
    }
    a.b("Action receiver has already been initialized.");
  }

  protected void a(g paramg)
  {
    if (d == null)
      d = new Hashtable();
    String str = paramg.c();
    if (str != null)
    {
      d.put(str, paramg);
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
    //   0: getstatic 75	cn/domob/android/offerwall/d:d	Ljava/util/Hashtable;
    //   3: ifnonnull +4 -> 7
    //   6: return
    //   7: aload_2
    //   8: invokevirtual 131	android/content/Intent:getAction	()Ljava/lang/String;
    //   11: astore 4
    //   13: getstatic 29	cn/domob/android/offerwall/d:a	Lcn/domob/android/offerwall/m;
    //   16: new 94	java/lang/StringBuilder
    //   19: dup
    //   20: invokespecial 95	java/lang/StringBuilder:<init>	()V
    //   23: ldc 133
    //   25: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: aload_2
    //   29: invokevirtual 134	android/content/Intent:toString	()Ljava/lang/String;
    //   32: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   35: invokevirtual 111	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   38: invokevirtual 113	cn/domob/android/offerwall/m:a	(Ljava/lang/String;)V
    //   41: ldc 46
    //   43: aload 4
    //   45: invokevirtual 139	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   48: ifeq +188 -> 236
    //   51: aload_2
    //   52: invokevirtual 142	android/content/Intent:getDataString	()Ljava/lang/String;
    //   55: bipush 8
    //   57: invokevirtual 146	java/lang/String:substring	(I)Ljava/lang/String;
    //   60: astore 5
    //   62: aload_1
    //   63: invokevirtual 150	android/content/Context:getPackageManager	()Landroid/content/pm/PackageManager;
    //   66: aload 5
    //   68: iconst_0
    //   69: invokevirtual 156	android/content/pm/PackageManager:getPackageInfo	(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;
    //   72: getfield 162	android/content/pm/PackageInfo:packageName	Ljava/lang/String;
    //   75: astore 7
    //   77: getstatic 75	cn/domob/android/offerwall/d:d	Ljava/util/Hashtable;
    //   80: aload 7
    //   82: invokevirtual 92	java/util/Hashtable:containsKey	(Ljava/lang/Object;)Z
    //   85: ifeq +125 -> 210
    //   88: getstatic 66	cn/domob/android/offerwall/d:c	Lcn/domob/android/offerwall/d$a;
    //   91: ifnull +22 -> 113
    //   94: getstatic 66	cn/domob/android/offerwall/d:c	Lcn/domob/android/offerwall/d$a;
    //   97: getstatic 75	cn/domob/android/offerwall/d:d	Ljava/util/Hashtable;
    //   100: aload 7
    //   102: invokevirtual 105	java/util/Hashtable:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   105: checkcast 80	cn/domob/android/offerwall/g
    //   108: invokeinterface 166 2 0
    //   113: getstatic 170	cn/domob/android/a/a:b	Ljava/util/Hashtable;
    //   116: aload 7
    //   118: invokevirtual 105	java/util/Hashtable:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   121: checkcast 172	java/lang/Integer
    //   124: astore 8
    //   126: aload 8
    //   128: ifnull +29 -> 157
    //   131: getstatic 170	cn/domob/android/a/a:b	Ljava/util/Hashtable;
    //   134: aload 7
    //   136: invokevirtual 105	java/util/Hashtable:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   139: pop
    //   140: aload_1
    //   141: ldc 174
    //   143: invokevirtual 178	android/content/Context:getSystemService	(Ljava/lang/String;)Ljava/lang/Object;
    //   146: checkcast 180	android/app/NotificationManager
    //   149: aload 8
    //   151: invokevirtual 184	java/lang/Integer:intValue	()I
    //   154: invokevirtual 188	android/app/NotificationManager:cancel	(I)V
    //   157: aload_0
    //   158: aload 7
    //   160: invokevirtual 189	cn/domob/android/offerwall/d:a	(Ljava/lang/String;)V
    //   163: return
    //   164: astore_3
    //   165: getstatic 29	cn/domob/android/offerwall/d:a	Lcn/domob/android/offerwall/m;
    //   168: aload_3
    //   169: invokevirtual 192	cn/domob/android/offerwall/m:a	(Ljava/lang/Throwable;)V
    //   172: return
    //   173: astore 6
    //   175: getstatic 29	cn/domob/android/offerwall/d:a	Lcn/domob/android/offerwall/m;
    //   178: new 94	java/lang/StringBuilder
    //   181: dup
    //   182: invokespecial 95	java/lang/StringBuilder:<init>	()V
    //   185: ldc 194
    //   187: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   190: ldc 196
    //   192: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   195: invokevirtual 111	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   198: invokevirtual 39	cn/domob/android/offerwall/m:e	(Ljava/lang/String;)V
    //   201: getstatic 29	cn/domob/android/offerwall/d:a	Lcn/domob/android/offerwall/m;
    //   204: aload 6
    //   206: invokevirtual 192	cn/domob/android/offerwall/m:a	(Ljava/lang/Throwable;)V
    //   209: return
    //   210: getstatic 29	cn/domob/android/offerwall/d:a	Lcn/domob/android/offerwall/m;
    //   213: new 94	java/lang/StringBuilder
    //   216: dup
    //   217: invokespecial 95	java/lang/StringBuilder:<init>	()V
    //   220: ldc 198
    //   222: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   225: aload 7
    //   227: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   230: invokevirtual 111	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   233: invokevirtual 39	cn/domob/android/offerwall/m:e	(Ljava/lang/String;)V
    //   236: return
    //
    // Exception table:
    //   from	to	target	type
    //   0	6	164	java/lang/Exception
    //   7	62	164	java/lang/Exception
    //   62	77	164	java/lang/Exception
    //   77	113	164	java/lang/Exception
    //   113	126	164	java/lang/Exception
    //   131	157	164	java/lang/Exception
    //   157	163	164	java/lang/Exception
    //   175	209	164	java/lang/Exception
    //   210	236	164	java/lang/Exception
    //   62	77	173	android/content/pm/PackageManager$NameNotFoundException
  }

  static abstract interface a
  {
    public abstract void a(g paramg);

    public abstract void b(g paramg);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.android.offerwall.d
 * JD-Core Version:    0.6.0
 */