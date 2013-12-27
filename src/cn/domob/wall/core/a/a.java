package cn.domob.wall.core.a;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import cn.domob.wall.core.AdInfo;
import cn.domob.wall.core.p;

public class a
{
  private static final String f = "url";
  private static final String g = "failsafe";
  private static final String h = "pkg";
  private static final String i = "activity";
  private static final String j = "param";
  p a = new p(a.class.getSimpleName());
  AdInfo b;
  private Context c;
  private Uri d;
  private a e;

  public a(Context paramContext, Uri paramUri, a parama, AdInfo paramAdInfo)
  {
    this.c = paramContext;
    this.d = paramUri;
    this.e = parama;
    this.b = paramAdInfo;
  }

  // ERROR //
  private Intent a(Uri paramUri)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc 8
    //   3: invokevirtual 65	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   6: astore_2
    //   7: aload_1
    //   8: ldc 14
    //   10: invokevirtual 65	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   13: astore_3
    //   14: aload_1
    //   15: ldc 17
    //   17: invokevirtual 65	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   20: astore 4
    //   22: aload_1
    //   23: ldc 20
    //   25: invokevirtual 65	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   28: astore 5
    //   30: aload_0
    //   31: getfield 48	cn/domob/wall/core/a/a:a	Lcn/domob/wall/core/p;
    //   34: ldc 67
    //   36: iconst_4
    //   37: anewarray 4	java/lang/Object
    //   40: dup
    //   41: iconst_0
    //   42: aload_2
    //   43: aastore
    //   44: dup
    //   45: iconst_1
    //   46: aload_3
    //   47: aastore
    //   48: dup
    //   49: iconst_2
    //   50: aload 4
    //   52: aastore
    //   53: dup
    //   54: iconst_3
    //   55: aload 5
    //   57: aastore
    //   58: invokestatic 73	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   61: invokevirtual 75	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   64: aload_2
    //   65: ifnull +155 -> 220
    //   68: new 77	android/content/Intent
    //   71: dup
    //   72: ldc 79
    //   74: aload_2
    //   75: invokestatic 83	android/net/Uri:parse	(Ljava/lang/String;)Landroid/net/Uri;
    //   78: invokespecial 86	android/content/Intent:<init>	(Ljava/lang/String;Landroid/net/Uri;)V
    //   81: astore 6
    //   83: aload 6
    //   85: ldc 87
    //   87: invokevirtual 91	android/content/Intent:addFlags	(I)Landroid/content/Intent;
    //   90: pop
    //   91: aload 6
    //   93: ifnull +280 -> 373
    //   96: aload 5
    //   98: ifnull +275 -> 373
    //   101: aload 5
    //   103: ldc 93
    //   105: invokevirtual 97	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   108: ifne +265 -> 373
    //   111: aload_0
    //   112: getfield 48	cn/domob/wall/core/a/a:a	Lcn/domob/wall/core/p;
    //   115: new 99	java/lang/StringBuilder
    //   118: dup
    //   119: invokespecial 100	java/lang/StringBuilder:<init>	()V
    //   122: ldc 102
    //   124: invokevirtual 106	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   127: aload 5
    //   129: invokevirtual 106	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   132: invokevirtual 109	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   135: invokevirtual 75	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   138: aload 5
    //   140: ldc 111
    //   142: invokevirtual 115	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   145: astore 7
    //   147: aload 7
    //   149: arraylength
    //   150: istore 8
    //   152: iconst_0
    //   153: istore 9
    //   155: iload 9
    //   157: iload 8
    //   159: if_icmpge +214 -> 373
    //   162: aload 7
    //   164: iload 9
    //   166: aaload
    //   167: ldc 117
    //   169: invokevirtual 115	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   172: astore 10
    //   174: aload 6
    //   176: aload 10
    //   178: iconst_0
    //   179: aaload
    //   180: aload 10
    //   182: iconst_1
    //   183: aaload
    //   184: invokevirtual 121	android/content/Intent:putExtra	(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;
    //   187: pop
    //   188: iinc 9 1
    //   191: goto -36 -> 155
    //   194: astore 15
    //   196: aconst_null
    //   197: astore 6
    //   199: aload_0
    //   200: getfield 48	cn/domob/wall/core/a/a:a	Lcn/domob/wall/core/p;
    //   203: ldc 123
    //   205: invokevirtual 125	cn/domob/wall/core/p:e	(Ljava/lang/String;)V
    //   208: aload_0
    //   209: getfield 48	cn/domob/wall/core/a/a:a	Lcn/domob/wall/core/p;
    //   212: aload 15
    //   214: invokevirtual 128	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   217: goto -126 -> 91
    //   220: aload_3
    //   221: ifnull +137 -> 358
    //   224: aload_3
    //   225: ldc 93
    //   227: invokevirtual 97	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   230: ifne +128 -> 358
    //   233: aload 4
    //   235: ifnull +74 -> 309
    //   238: aload 4
    //   240: ldc 93
    //   242: invokevirtual 97	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   245: ifne +64 -> 309
    //   248: aload_0
    //   249: getfield 48	cn/domob/wall/core/a/a:a	Lcn/domob/wall/core/p;
    //   252: ldc 130
    //   254: iconst_2
    //   255: anewarray 4	java/lang/Object
    //   258: dup
    //   259: iconst_0
    //   260: aload_3
    //   261: aastore
    //   262: dup
    //   263: iconst_1
    //   264: aload 4
    //   266: aastore
    //   267: invokestatic 73	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   270: invokevirtual 75	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   273: new 77	android/content/Intent
    //   276: dup
    //   277: invokespecial 131	android/content/Intent:<init>	()V
    //   280: astore 6
    //   282: aload 6
    //   284: new 133	android/content/ComponentName
    //   287: dup
    //   288: aload_3
    //   289: aload 4
    //   291: invokespecial 136	android/content/ComponentName:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   294: invokevirtual 140	android/content/Intent:setComponent	(Landroid/content/ComponentName;)Landroid/content/Intent;
    //   297: pop
    //   298: aload 6
    //   300: ldc 87
    //   302: invokevirtual 143	android/content/Intent:setFlags	(I)Landroid/content/Intent;
    //   305: pop
    //   306: goto -215 -> 91
    //   309: aload_0
    //   310: getfield 48	cn/domob/wall/core/a/a:a	Lcn/domob/wall/core/p;
    //   313: ldc 145
    //   315: iconst_1
    //   316: anewarray 4	java/lang/Object
    //   319: dup
    //   320: iconst_0
    //   321: aload_3
    //   322: aastore
    //   323: invokestatic 73	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   326: invokevirtual 75	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   329: aload_0
    //   330: getfield 50	cn/domob/wall/core/a/a:c	Landroid/content/Context;
    //   333: invokevirtual 151	android/content/Context:getPackageManager	()Landroid/content/pm/PackageManager;
    //   336: aload_3
    //   337: invokevirtual 157	android/content/pm/PackageManager:getLaunchIntentForPackage	(Ljava/lang/String;)Landroid/content/Intent;
    //   340: astore 6
    //   342: aload 6
    //   344: ifnull -253 -> 91
    //   347: aload 6
    //   349: ldc 87
    //   351: invokevirtual 143	android/content/Intent:setFlags	(I)Landroid/content/Intent;
    //   354: pop
    //   355: goto -264 -> 91
    //   358: aload_0
    //   359: getfield 48	cn/domob/wall/core/a/a:a	Lcn/domob/wall/core/p;
    //   362: ldc 159
    //   364: invokevirtual 125	cn/domob/wall/core/p:e	(Ljava/lang/String;)V
    //   367: aconst_null
    //   368: astore 6
    //   370: goto -279 -> 91
    //   373: aload 6
    //   375: areturn
    //   376: astore 15
    //   378: goto -179 -> 199
    //
    // Exception table:
    //   from	to	target	type
    //   68	83	194	java/lang/Exception
    //   83	91	376	java/lang/Exception
  }

  public void a()
  {
    Intent localIntent = a(this.d);
    String str;
    if (localIntent == null)
      try
      {
        throw new Exception("Action intent is null.");
      }
      catch (Exception localException1)
      {
        this.a.e("Failed to launch app with URL:" + this.d.toString());
        this.a.a(localException1);
        str = this.d.getQueryParameter("failsafe");
        if (this.e != null)
          this.e.a(str, this.b);
        if (str == null);
      }
    try
    {
      this.a.b("Backup action ----- Open landing page with URL:" + str);
      if (this.e != null)
        this.e.b(str, this.b);
      do
      {
        return;
        this.c.startActivity(localIntent);
      }
      while (this.e == null);
      this.e.d(this.b);
      return;
    }
    catch (Exception localException2)
    {
      this.a.e("Invalid failsafe URL.");
      this.a.a(localException2);
    }
  }

  public static abstract interface a
  {
    public abstract void a(String paramString, AdInfo paramAdInfo);

    public abstract void b(String paramString, AdInfo paramAdInfo);

    public abstract void d(AdInfo paramAdInfo);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.a.a
 * JD-Core Version:    0.6.0
 */