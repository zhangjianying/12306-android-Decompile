package cn.domob.ui;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class h
{
  public static float a(Context paramContext)
  {
    Display localDisplay = ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay();
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    localDisplay.getMetrics(localDisplayMetrics);
    return localDisplayMetrics.density;
  }

  public static boolean a(int paramInt)
  {
    if (paramInt < 0);
    String str;
    do
    {
      return true;
      str = paramInt + "";
    }
    while ((str == null) || (str.equals("")) || (str.length() == 0));
    return false;
  }

  public static boolean a(Object paramObject)
  {
    return (paramObject == null) || (paramObject.equals(""));
  }

  public static boolean a(String paramString)
  {
    return (paramString == null) || (paramString.equals("")) || (paramString.length() == 0);
  }

  public static float b(Context paramContext)
  {
    if (0.0F == 0.0F);
    while (true)
    {
      try
      {
        Display localDisplay = ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay();
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        localDisplay.getMetrics(localDisplayMetrics);
        f = localDisplayMetrics.density;
        return f;
      }
      catch (Exception localException)
      {
        return 0.0F;
      }
      float f = 0.0F;
    }
  }

  public static int c(Context paramContext)
  {
    Display localDisplay = ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay();
    if (localDisplay != null)
      return localDisplay.getWidth();
    return 0;
  }

  public static int d(Context paramContext)
  {
    Display localDisplay = ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay();
    if (localDisplay != null)
      return localDisplay.getHeight();
    return 0;
  }

  public static float e(Context paramContext)
  {
    boolean bool = 0.0F < 0.0F;
    float f = 0.0F;
    if (!bool);
    try
    {
      f = paramContext.getResources().getDisplayMetrics().density;
      return f;
    }
    catch (Exception localException)
    {
    }
    return 0.0F;
  }

  public static int f(Context paramContext)
  {
    return Math.round(c(paramContext) * (b(paramContext) / e(paramContext)));
  }

  public static int g(Context paramContext)
  {
    return Math.round(d(paramContext) * (b(paramContext) / e(paramContext)));
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.ui.h
 * JD-Core Version:    0.6.0
 */