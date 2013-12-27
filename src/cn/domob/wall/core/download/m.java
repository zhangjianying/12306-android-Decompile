package cn.domob.wall.core.download;

import java.util.LinkedHashMap;
import java.util.Map;

public class m<K, V>
{
  private final LinkedHashMap<K, V> a;
  private int b;
  private int c;
  private int d;
  private int e;
  private int f;
  private int g;
  private int h;

  public m(int paramInt)
  {
    if (paramInt <= 0)
      throw new IllegalArgumentException("maxSize <= 0");
    this.c = paramInt;
    this.a = new LinkedHashMap(0, 0.75F, true);
  }

  // ERROR //
  private void a(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 40	cn/domob/wall/core/download/m:b	I
    //   6: iflt +20 -> 26
    //   9: aload_0
    //   10: getfield 38	cn/domob/wall/core/download/m:a	Ljava/util/LinkedHashMap;
    //   13: invokevirtual 44	java/util/LinkedHashMap:isEmpty	()Z
    //   16: ifeq +48 -> 64
    //   19: aload_0
    //   20: getfield 40	cn/domob/wall/core/download/m:b	I
    //   23: ifeq +41 -> 64
    //   26: new 46	java/lang/IllegalStateException
    //   29: dup
    //   30: new 48	java/lang/StringBuilder
    //   33: dup
    //   34: invokespecial 49	java/lang/StringBuilder:<init>	()V
    //   37: aload_0
    //   38: invokevirtual 53	java/lang/Object:getClass	()Ljava/lang/Class;
    //   41: invokevirtual 59	java/lang/Class:getName	()Ljava/lang/String;
    //   44: invokevirtual 63	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   47: ldc 65
    //   49: invokevirtual 63	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   52: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   55: invokespecial 69	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   58: athrow
    //   59: astore_2
    //   60: aload_0
    //   61: monitorexit
    //   62: aload_2
    //   63: athrow
    //   64: aload_0
    //   65: getfield 40	cn/domob/wall/core/download/m:b	I
    //   68: iload_1
    //   69: if_icmple +13 -> 82
    //   72: aload_0
    //   73: getfield 38	cn/domob/wall/core/download/m:a	Ljava/util/LinkedHashMap;
    //   76: invokevirtual 44	java/util/LinkedHashMap:isEmpty	()Z
    //   79: ifeq +6 -> 85
    //   82: aload_0
    //   83: monitorexit
    //   84: return
    //   85: aload_0
    //   86: getfield 38	cn/domob/wall/core/download/m:a	Ljava/util/LinkedHashMap;
    //   89: invokevirtual 73	java/util/LinkedHashMap:entrySet	()Ljava/util/Set;
    //   92: invokeinterface 79 1 0
    //   97: invokeinterface 85 1 0
    //   102: checkcast 87	java/util/Map$Entry
    //   105: astore_3
    //   106: aload_3
    //   107: invokeinterface 90 1 0
    //   112: astore 4
    //   114: aload_3
    //   115: invokeinterface 93 1 0
    //   120: astore 5
    //   122: aload_0
    //   123: getfield 38	cn/domob/wall/core/download/m:a	Ljava/util/LinkedHashMap;
    //   126: aload 4
    //   128: invokevirtual 97	java/util/LinkedHashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
    //   131: pop
    //   132: aload_0
    //   133: aload_0
    //   134: getfield 40	cn/domob/wall/core/download/m:b	I
    //   137: aload_0
    //   138: aload 4
    //   140: aload 5
    //   142: invokespecial 100	cn/domob/wall/core/download/m:c	(Ljava/lang/Object;Ljava/lang/Object;)I
    //   145: isub
    //   146: putfield 40	cn/domob/wall/core/download/m:b	I
    //   149: aload_0
    //   150: iconst_1
    //   151: aload_0
    //   152: getfield 102	cn/domob/wall/core/download/m:f	I
    //   155: iadd
    //   156: putfield 102	cn/domob/wall/core/download/m:f	I
    //   159: aload_0
    //   160: monitorexit
    //   161: aload_0
    //   162: iconst_1
    //   163: aload 4
    //   165: aload 5
    //   167: aconst_null
    //   168: invokevirtual 105	cn/domob/wall/core/download/m:a	(ZLjava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
    //   171: goto -171 -> 0
    //
    // Exception table:
    //   from	to	target	type
    //   2	26	59	finally
    //   26	59	59	finally
    //   60	62	59	finally
    //   64	82	59	finally
    //   82	84	59	finally
    //   85	161	59	finally
  }

  private int c(K paramK, V paramV)
  {
    int i = a(paramK, paramV);
    if (i < 0)
      throw new IllegalStateException("Negative size: " + paramK + "=" + paramV);
    return i;
  }

  protected int a(K paramK, V paramV)
  {
    return 1;
  }

  public final V a(K paramK)
  {
    if (paramK == null)
      throw new NullPointerException("key == null");
    monitorenter;
    Object localObject3;
    try
    {
      Object localObject2 = this.a.get(paramK);
      if (localObject2 != null)
      {
        this.g = (1 + this.g);
        return localObject2;
      }
      this.h = (1 + this.h);
      monitorexit;
      localObject3 = c(paramK);
      if (localObject3 == null)
        return null;
    }
    finally
    {
      monitorexit;
    }
    monitorenter;
    try
    {
      this.e = (1 + this.e);
      Object localObject5 = this.a.put(paramK, localObject3);
      if (localObject5 != null)
        this.a.put(paramK, localObject5);
      while (true)
      {
        monitorexit;
        if (localObject5 == null)
          break;
        a(false, paramK, localObject3, localObject5);
        return localObject5;
        this.b += c(paramK, localObject3);
      }
    }
    finally
    {
      monitorexit;
    }
    a(this.c);
    return localObject3;
  }

  public final void a()
  {
    a(-1);
  }

  protected void a(boolean paramBoolean, K paramK, V paramV1, V paramV2)
  {
  }

  public final int b()
  {
    monitorenter;
    try
    {
      int i = this.b;
      monitorexit;
      return i;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public final V b(K paramK)
  {
    if (paramK == null)
      throw new NullPointerException("key == null");
    monitorenter;
    try
    {
      Object localObject2 = this.a.remove(paramK);
      if (localObject2 != null)
        this.b -= c(paramK, localObject2);
      monitorexit;
      if (localObject2 != null)
        a(false, paramK, localObject2, null);
      return localObject2;
    }
    finally
    {
      monitorexit;
    }
    throw localObject1;
  }

  public final V b(K paramK, V paramV)
  {
    if ((paramK == null) || (paramV == null))
      throw new NullPointerException("key == null || value == null");
    monitorenter;
    try
    {
      this.d = (1 + this.d);
      this.b += c(paramK, paramV);
      Object localObject2 = this.a.put(paramK, paramV);
      if (localObject2 != null)
        this.b -= c(paramK, localObject2);
      monitorexit;
      if (localObject2 != null)
        a(false, paramK, localObject2, paramV);
      a(this.c);
      return localObject2;
    }
    finally
    {
      monitorexit;
    }
    throw localObject1;
  }

  public final int c()
  {
    monitorenter;
    try
    {
      int i = this.c;
      monitorexit;
      return i;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  protected V c(K paramK)
  {
    return null;
  }

  public final int d()
  {
    monitorenter;
    try
    {
      int i = this.g;
      monitorexit;
      return i;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public final int e()
  {
    monitorenter;
    try
    {
      int i = this.h;
      monitorexit;
      return i;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public final int f()
  {
    monitorenter;
    try
    {
      int i = this.e;
      monitorexit;
      return i;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public final int g()
  {
    monitorenter;
    try
    {
      int i = this.d;
      monitorexit;
      return i;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public final int h()
  {
    monitorenter;
    try
    {
      int i = this.f;
      monitorexit;
      return i;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public final Map<K, V> i()
  {
    monitorenter;
    try
    {
      LinkedHashMap localLinkedHashMap = new LinkedHashMap(this.a);
      monitorexit;
      return localLinkedHashMap;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public final String toString()
  {
    monitorenter;
    try
    {
      int i = this.g + this.h;
      int j = 0;
      if (i != 0)
        j = 100 * this.g / i;
      Object[] arrayOfObject = new Object[4];
      arrayOfObject[0] = Integer.valueOf(this.c);
      arrayOfObject[1] = Integer.valueOf(this.g);
      arrayOfObject[2] = Integer.valueOf(this.h);
      arrayOfObject[3] = Integer.valueOf(j);
      String str = String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", arrayOfObject);
      return str;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.download.m
 * JD-Core Version:    0.6.0
 */