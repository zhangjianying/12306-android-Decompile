package cn.domob.wall.core;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.ImageView;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class u
{
  static u a;
  private static final p b = new p(u.class.getSimpleName());
  private static final String e = Environment.getExternalStorageDirectory() + "/wallImages/";
  private ExecutorService c = Executors.newFixedThreadPool(5);
  private HashMap<String, SoftReference<Drawable>> d = new HashMap();

  static u a()
  {
    monitorenter;
    try
    {
      if (a == null)
        a = new u();
      u localu = a;
      return localu;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  // ERROR //
  private String a(Context paramContext, byte[] paramArrayOfByte, String paramString)
  {
    // Byte code:
    //   0: aload_2
    //   1: ifnonnull +13 -> 14
    //   4: getstatic 31	cn/domob/wall/core/u:b	Lcn/domob/wall/core/p;
    //   7: ldc 80
    //   9: invokevirtual 82	cn/domob/wall/core/p:d	(Ljava/lang/String;)V
    //   12: aconst_null
    //   13: areturn
    //   14: new 84	java/io/File
    //   17: dup
    //   18: getstatic 55	cn/domob/wall/core/u:e	Ljava/lang/String;
    //   21: invokespecial 85	java/io/File:<init>	(Ljava/lang/String;)V
    //   24: astore 4
    //   26: aload 4
    //   28: invokevirtual 89	java/io/File:exists	()Z
    //   31: ifne +9 -> 40
    //   34: aload 4
    //   36: invokevirtual 92	java/io/File:mkdir	()Z
    //   39: pop
    //   40: new 33	java/lang/StringBuilder
    //   43: dup
    //   44: invokespecial 35	java/lang/StringBuilder:<init>	()V
    //   47: getstatic 55	cn/domob/wall/core/u:e	Ljava/lang/String;
    //   50: invokevirtual 50	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   53: aload_3
    //   54: invokestatic 97	cn/domob/wall/core/v:c	(Ljava/lang/String;)Ljava/lang/String;
    //   57: invokevirtual 50	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   60: invokevirtual 53	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   63: astore 5
    //   65: new 84	java/io/File
    //   68: dup
    //   69: aload 5
    //   71: invokespecial 85	java/io/File:<init>	(Ljava/lang/String;)V
    //   74: astore 6
    //   76: new 99	java/io/FileOutputStream
    //   79: dup
    //   80: aload 6
    //   82: invokespecial 102	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   85: astore 7
    //   87: aload 7
    //   89: aload_2
    //   90: invokevirtual 106	java/io/FileOutputStream:write	([B)V
    //   93: aload 7
    //   95: invokevirtual 109	java/io/FileOutputStream:close	()V
    //   98: aload 5
    //   100: areturn
    //   101: astore 13
    //   103: getstatic 31	cn/domob/wall/core/u:b	Lcn/domob/wall/core/p;
    //   106: aload 13
    //   108: invokevirtual 112	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   111: aconst_null
    //   112: areturn
    //   113: astore 12
    //   115: getstatic 31	cn/domob/wall/core/u:b	Lcn/domob/wall/core/p;
    //   118: aload 12
    //   120: invokevirtual 112	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   123: goto -25 -> 98
    //   126: astore 10
    //   128: getstatic 31	cn/domob/wall/core/u:b	Lcn/domob/wall/core/p;
    //   131: aload 10
    //   133: invokevirtual 112	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   136: aload 7
    //   138: invokevirtual 109	java/io/FileOutputStream:close	()V
    //   141: aconst_null
    //   142: areturn
    //   143: astore 11
    //   145: getstatic 31	cn/domob/wall/core/u:b	Lcn/domob/wall/core/p;
    //   148: aload 11
    //   150: invokevirtual 112	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   153: aconst_null
    //   154: areturn
    //   155: astore 8
    //   157: aload 7
    //   159: invokevirtual 109	java/io/FileOutputStream:close	()V
    //   162: aload 8
    //   164: athrow
    //   165: astore 9
    //   167: getstatic 31	cn/domob/wall/core/u:b	Lcn/domob/wall/core/p;
    //   170: aload 9
    //   172: invokevirtual 112	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   175: goto -13 -> 162
    //
    // Exception table:
    //   from	to	target	type
    //   76	87	101	java/io/FileNotFoundException
    //   93	98	113	java/io/IOException
    //   87	93	126	java/io/IOException
    //   136	141	143	java/io/IOException
    //   87	93	155	finally
    //   128	136	155	finally
    //   157	162	165	java/io/IOException
  }

  BitmapDrawable a(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte != null);
    try
    {
      BitmapDrawable localBitmapDrawable = new BitmapDrawable(BitmapFactory.decodeByteArray(paramArrayOfByte, 0, paramArrayOfByte.length));
      return localBitmapDrawable;
      return null;
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
      System.gc();
      return null;
    }
    catch (Exception localException)
    {
      b.a(localException);
    }
    return null;
  }

  Drawable a(Context paramContext, String paramString)
  {
    b.b("Synchronization request image resources url=" + paramString);
    Drawable localDrawable = b(paramContext, paramString);
    if (localDrawable != null)
    {
      b.b("Success in the local image resources, and generates drawable objects");
      t.a().c(paramString);
      return localDrawable;
    }
    b.b("Can not be found locally valid image resources, began to try online access to");
    f localf = new f(paramContext, paramString, null);
    localf.c();
    byte[] arrayOfByte = localf.d();
    String str = a(paramContext, arrayOfByte, localf.a());
    if (str != null)
    {
      b.b("Successfully loaded the picture from the server");
      try
      {
        t.a().a(paramString, str, arrayOfByte.length);
        this.d.put(paramString, new SoftReference(a(arrayOfByte)));
        return (Drawable)((SoftReference)this.d.get(paramString)).get();
      }
      catch (Exception localException)
      {
        while (true)
          b.a(localException);
      }
    }
    b.b("failed loaded the picture from the server");
    return null;
  }

  void a(Context paramContext, String paramString, ImageView paramImageView, int paramInt)
  {
    b.b("Asynchronous request image resources url=" + paramString);
    Drawable localDrawable = b(paramContext, paramString);
    if (localDrawable == null)
    {
      b.b("Local not find the image, open thread, try to access through the network");
      b localb = new b(paramContext, paramString, paramImageView, paramInt);
      this.c.execute(localb);
      return;
    }
    b.b("Successfully find the picture from SD, so do not need to download");
    ((Activity)paramContext).runOnUiThread(new Runnable(paramImageView, localDrawable)
    {
      public void run()
      {
        this.a.setImageDrawable(this.b);
        u.b().b("Open the UI main thread, assigned to the ImageView object");
      }
    });
    t.a().c(paramString);
  }

  protected Drawable b(Context paramContext, String paramString)
  {
    SoftReference localSoftReference;
    if (this.d.containsKey(paramString))
    {
      localSoftReference = (SoftReference)this.d.get(paramString);
      if (localSoftReference == null)
        break label161;
    }
    label161: for (Drawable localDrawable1 = (Drawable)localSoftReference.get(); ; localDrawable1 = null)
    {
      if (localDrawable1 != null)
      {
        return localDrawable1;
        localDrawable1 = null;
      }
      String str = t.a().a(paramString);
      if ((str == null) || (str.length() == 0))
      {
        b.a(String.format("image %s is in DB but the local path is null.", new Object[] { paramString }));
        return localDrawable1;
      }
      if (new File(str).exists())
        try
        {
          Drawable localDrawable2 = v.b(paramContext, str);
          this.d.put(paramString, new SoftReference(localDrawable2));
          return localDrawable2;
        }
        catch (Exception localException)
        {
          t.a().d(paramString);
          b.a(localException);
          return null;
        }
      t.a().b(paramString);
      return localDrawable1;
    }
  }

  class a
    implements f.a
  {
    Context a;
    String b;
    ImageView c;
    int d;

    a(Context paramString, String paramImageView, ImageView paramInt, int arg5)
    {
      this.a = paramString;
      this.b = paramImageView;
      this.c = paramInt;
      int i;
      this.d = i;
    }

    void a()
    {
      f localf = new f(this.a, this.b, this);
      localf.a(this.b);
      localf.run();
    }

    public void a(f paramf)
    {
      if (paramf.f() == 200)
      {
        String str1 = paramf.a();
        u.b().b("Download finish:" + str1);
        byte[] arrayOfByte = paramf.d();
        String str2 = u.a(u.this, this.a, arrayOfByte, paramf.a());
        if (str2 != null)
          u.b().b("Image saved:" + str2);
        while (true)
        {
          try
          {
            p localp = u.b();
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Integer.valueOf(arrayOfByte.length / 1024);
            localp.b(String.format("the size of the image is %s KB", arrayOfObject));
            t.a().a(str1, str2, arrayOfByte.length);
            ((Activity)this.a).runOnUiThread(new Runnable(arrayOfByte, str1)
            {
              public void run()
              {
                BitmapDrawable localBitmapDrawable = u.this.a(this.a);
                if (localBitmapDrawable != null)
                {
                  u.a(u.this).put(this.b, new SoftReference(localBitmapDrawable));
                  u.a.this.c.setImageDrawable(localBitmapDrawable);
                  return;
                }
                u.a.this.c.setImageResource(u.a.this.d);
              }
            });
            return;
          }
          catch (Exception localException)
          {
            u.b().a(localException);
            continue;
          }
          u.b().e("Error in saving image.");
        }
      }
      u.b().d("Failed to get image from server and the ResponseCode is " + paramf.f());
      ((Activity)this.a).runOnUiThread(new Runnable()
      {
        public void run()
        {
          u.a.this.c.setImageResource(u.a.this.d);
        }
      });
    }
  }

  class b extends Thread
  {
    u.a a;

    b(Context paramString, String paramImageView, ImageView paramInt, int arg5)
    {
      int i;
      this.a = new u.a(u.this, paramString, paramImageView, paramInt, i);
    }

    public void run()
    {
      try
      {
        this.a.a();
        return;
      }
      catch (OutOfMemoryError localOutOfMemoryError)
      {
        System.gc();
      }
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.u
 * JD-Core Version:    0.6.0
 */