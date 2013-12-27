package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractMapEntry<K, V>
  implements Map.Entry<K, V>
{
  public boolean equals(@Nullable Object paramObject)
  {
    boolean bool1 = paramObject instanceof Map.Entry;
    int i = 0;
    if (bool1)
    {
      Map.Entry localEntry = (Map.Entry)paramObject;
      boolean bool2 = Objects.equal(getKey(), localEntry.getKey());
      i = 0;
      if (bool2)
      {
        boolean bool3 = Objects.equal(getValue(), localEntry.getValue());
        i = 0;
        if (bool3)
          i = 1;
      }
    }
    return i;
  }

  public abstract K getKey();

  public abstract V getValue();

  public int hashCode()
  {
    Object localObject1 = getKey();
    Object localObject2 = getValue();
    int i;
    int j;
    if (localObject1 == null)
    {
      i = 0;
      j = 0;
      if (localObject2 != null)
        break label36;
    }
    while (true)
    {
      return j ^ i;
      i = localObject1.hashCode();
      break;
      label36: j = localObject2.hashCode();
    }
  }

  public V setValue(V paramV)
  {
    throw new UnsupportedOperationException();
  }

  public String toString()
  {
    return getKey() + "=" + getValue();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.AbstractMapEntry
 * JD-Core Version:    0.6.0
 */