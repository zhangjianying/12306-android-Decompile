package org.codehaus.jackson.map.ser.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ser.SerializerCache.TypeKey;

public class JsonSerializerMap
{
  private final Bucket[] _buckets;
  private final int _size;

  public JsonSerializerMap(Map<SerializerCache.TypeKey, JsonSerializer<Object>> paramMap)
  {
    int i = findSize(paramMap.size());
    this._size = i;
    int j = i - 1;
    Bucket[] arrayOfBucket = new Bucket[i];
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      SerializerCache.TypeKey localTypeKey = (SerializerCache.TypeKey)localEntry.getKey();
      int k = j & localTypeKey.hashCode();
      arrayOfBucket[k] = new Bucket(arrayOfBucket[k], localTypeKey, (JsonSerializer)localEntry.getValue());
    }
    this._buckets = arrayOfBucket;
  }

  private static final int findSize(int paramInt)
  {
    if (paramInt <= 64);
    int j;
    for (int i = paramInt + paramInt; ; i = paramInt + (paramInt >> 2))
    {
      j = 8;
      while (j < i)
        j += j;
    }
    return j;
  }

  public JsonSerializer<Object> find(SerializerCache.TypeKey paramTypeKey)
  {
    int i = paramTypeKey.hashCode() & -1 + this._buckets.length;
    Bucket localBucket = this._buckets[i];
    if (localBucket == null)
      return null;
    if (paramTypeKey.equals(localBucket.key))
      return localBucket.value;
    do
    {
      localBucket = localBucket.next;
      if (localBucket == null)
        break;
    }
    while (!paramTypeKey.equals(localBucket.key));
    return localBucket.value;
  }

  public int size()
  {
    return this._size;
  }

  private static final class Bucket
  {
    public final SerializerCache.TypeKey key;
    public final Bucket next;
    public final JsonSerializer<Object> value;

    public Bucket(Bucket paramBucket, SerializerCache.TypeKey paramTypeKey, JsonSerializer<Object> paramJsonSerializer)
    {
      this.next = paramBucket;
      this.key = paramTypeKey;
      this.value = paramJsonSerializer;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.impl.JsonSerializerMap
 * JD-Core Version:    0.6.0
 */