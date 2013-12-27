package cn.domob.ui;

import android.content.Context;
import android.util.Log;
import java.lang.reflect.Field;

public class f
{
  private static o a = new o(f.class.getSimpleName());
  private static final String b = f.class.getName();
  private static f c;
  private static Class<?> e = null;
  private static Class<?> f = null;
  private static Class<?> g = null;
  private static Class<?> h = null;
  private static Class<?> i = null;
  private static Class<?> j = null;
  private static Class<?> k = null;
  private static Class<?> l = null;
  private Context d;

  private f(Context paramContext)
  {
    this.d = paramContext.getApplicationContext();
    try
    {
      f = Class.forName(this.d.getPackageName() + ".R$drawable");
    }
    catch (ClassNotFoundException localClassNotFoundException7)
    {
      try
      {
        g = Class.forName(this.d.getPackageName() + ".R$layout");
      }
      catch (ClassNotFoundException localClassNotFoundException7)
      {
        try
        {
          e = Class.forName(this.d.getPackageName() + ".R$id");
        }
        catch (ClassNotFoundException localClassNotFoundException7)
        {
          try
          {
            h = Class.forName(this.d.getPackageName() + ".R$anim");
          }
          catch (ClassNotFoundException localClassNotFoundException7)
          {
            try
            {
              i = Class.forName(this.d.getPackageName() + ".R$style");
            }
            catch (ClassNotFoundException localClassNotFoundException7)
            {
              try
              {
                j = Class.forName(this.d.getPackageName() + ".R$string");
              }
              catch (ClassNotFoundException localClassNotFoundException7)
              {
                try
                {
                  k = Class.forName(this.d.getPackageName() + ".R$color");
                }
                catch (ClassNotFoundException localClassNotFoundException7)
                {
                  try
                  {
                    while (true)
                    {
                      l = Class.forName(this.d.getPackageName() + ".R$array");
                      return;
                      localClassNotFoundException1 = localClassNotFoundException1;
                      a.e(b, localClassNotFoundException1.getMessage());
                      continue;
                      localClassNotFoundException2 = localClassNotFoundException2;
                      a.e(b, localClassNotFoundException2.getMessage());
                      continue;
                      localClassNotFoundException3 = localClassNotFoundException3;
                      a.e(b, localClassNotFoundException3.getMessage());
                      continue;
                      localClassNotFoundException4 = localClassNotFoundException4;
                      a.e(b, localClassNotFoundException4.getMessage());
                      continue;
                      localClassNotFoundException5 = localClassNotFoundException5;
                      a.e(b, localClassNotFoundException5.getMessage());
                      continue;
                      localClassNotFoundException6 = localClassNotFoundException6;
                      a.e(b, localClassNotFoundException6.getMessage());
                      continue;
                      localClassNotFoundException7 = localClassNotFoundException7;
                      a.e(b, localClassNotFoundException7.getMessage());
                    }
                  }
                  catch (ClassNotFoundException localClassNotFoundException8)
                  {
                    a.e(b, localClassNotFoundException8.getMessage());
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  private int a(Class<?> paramClass, String paramString)
  {
    if (paramClass == null)
    {
      a.e(b, "getRes(null," + paramString + ")");
      throw new IllegalArgumentException("ResClass is not initialized. Please make sure you have added neccessary resources. Also make sure you have " + this.d.getPackageName() + ".R$* configured in obfuscation. field=" + paramString);
    }
    try
    {
      int m = paramClass.getField(paramString).getInt(paramString);
      return m;
    }
    catch (Exception localException)
    {
      Log.e(b, "getRes(" + paramClass.getName() + ", " + paramString + ")");
      Log.e(b, "Error getting resource. Make sure you have copied all resources (res/) from SDK to your project. ");
      a.e(b, localException.getMessage());
    }
    return -1;
  }

  public static f a(Context paramContext)
  {
    if (c == null)
      c = new f(paramContext);
    return c;
  }

  public int a(String paramString)
  {
    return a(h, paramString);
  }

  public int b(String paramString)
  {
    return a(e, paramString);
  }

  public int c(String paramString)
  {
    return a(f, paramString);
  }

  public int d(String paramString)
  {
    return a(g, paramString);
  }

  public int e(String paramString)
  {
    return a(i, paramString);
  }

  public int f(String paramString)
  {
    return a(j, paramString);
  }

  public int g(String paramString)
  {
    return a(k, paramString);
  }

  public int h(String paramString)
  {
    return a(l, paramString);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.ui.f
 * JD-Core Version:    0.6.0
 */