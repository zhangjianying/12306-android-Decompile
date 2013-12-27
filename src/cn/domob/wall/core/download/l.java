package cn.domob.wall.core.download;

import android.content.Context;
import android.graphics.Bitmap;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class l
{
  private static final int a = 15;
  private static m<String, Bitmap> b;
  private static LinkedHashMap<String, SoftReference<Bitmap>> c;

  public l(Context paramContext)
  {
    b = new m(8388608)
    {
      protected int a(String paramString, Bitmap paramBitmap)
      {
        if (paramBitmap != null)
          return paramBitmap.getRowBytes() * paramBitmap.getHeight();
        return 0;
      }

      protected void a(boolean paramBoolean, String paramString, Bitmap paramBitmap1, Bitmap paramBitmap2)
      {
        if (paramBitmap1 != null)
          l.b().put(paramString, new SoftReference(paramBitmap1));
      }
    };
    c = new LinkedHashMap(15, 0.75F, true)
    {
      private static final long serialVersionUID = 6040103833179403725L;

      protected boolean removeEldestEntry(Map.Entry<String, SoftReference<Bitmap>> paramEntry)
      {
        return size() > 15;
      }
    };
  }

  public Bitmap a(String paramString)
  {
    synchronized (b)
    {
      Bitmap localBitmap1 = (Bitmap)b.a(paramString);
      if (localBitmap1 != null)
      {
        b.b(paramString);
        b.b(paramString, localBitmap1);
        return localBitmap1;
      }
      synchronized (c)
      {
        SoftReference localSoftReference = (SoftReference)c.get(paramString);
        if (localSoftReference == null)
          break label133;
        Bitmap localBitmap2 = (Bitmap)localSoftReference.get();
        if (localBitmap2 != null)
        {
          b.b(paramString, localBitmap2);
          c.remove(paramString);
          return localBitmap2;
        }
      }
    }
    c.remove(paramString);
    label133: monitorexit;
    return null;
  }

  public void a()
  {
    c.clear();
  }

  public void a(String paramString, Bitmap paramBitmap)
  {
    if (paramBitmap != null)
      synchronized (b)
      {
        b.b(paramString, paramBitmap);
        return;
      }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.download.l
 * JD-Core Version:    0.6.0
 */