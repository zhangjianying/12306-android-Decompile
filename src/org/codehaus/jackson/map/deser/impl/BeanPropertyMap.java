package org.codehaus.jackson.map.deser.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.codehaus.jackson.map.deser.SettableBeanProperty;

public final class BeanPropertyMap
{
  private final Bucket[] _buckets;
  private final int _hashMask;
  private final int _size;

  public BeanPropertyMap(Collection<SettableBeanProperty> paramCollection)
  {
    this._size = paramCollection.size();
    int i = findSize(this._size);
    this._hashMask = (i - 1);
    Bucket[] arrayOfBucket = new Bucket[i];
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      SettableBeanProperty localSettableBeanProperty = (SettableBeanProperty)localIterator.next();
      String str = localSettableBeanProperty.getName();
      int j = str.hashCode() & this._hashMask;
      arrayOfBucket[j] = new Bucket(arrayOfBucket[j], str, localSettableBeanProperty);
    }
    this._buckets = arrayOfBucket;
  }

  private SettableBeanProperty _findWithEquals(String paramString, int paramInt)
  {
    for (Bucket localBucket = this._buckets[paramInt]; localBucket != null; localBucket = localBucket.next)
      if (paramString.equals(localBucket.key))
        return localBucket.value;
    return null;
  }

  private static final int findSize(int paramInt)
  {
    if (paramInt <= 32);
    int j;
    for (int i = paramInt + paramInt; ; i = paramInt + (paramInt >> 2))
    {
      j = 2;
      while (j < i)
        j += j;
    }
    return j;
  }

  public Iterator<SettableBeanProperty> allProperties()
  {
    return new IteratorImpl(this._buckets);
  }

  public void assignIndexes()
  {
    int i = 0;
    Bucket[] arrayOfBucket = this._buckets;
    int j = arrayOfBucket.length;
    int k = 0;
    while (k < j)
    {
      Bucket localBucket = arrayOfBucket[k];
      int n;
      for (int m = i; localBucket != null; m = n)
      {
        SettableBeanProperty localSettableBeanProperty = localBucket.value;
        n = m + 1;
        localSettableBeanProperty.assignIndex(m);
        localBucket = localBucket.next;
      }
      k++;
      i = m;
    }
  }

  public SettableBeanProperty find(String paramString)
  {
    int i = paramString.hashCode() & this._hashMask;
    Bucket localBucket = this._buckets[i];
    if (localBucket == null)
      return null;
    if (localBucket.key == paramString)
      return localBucket.value;
    while (true)
    {
      localBucket = localBucket.next;
      if (localBucket == null)
        break;
      if (localBucket.key == paramString)
        return localBucket.value;
    }
    return _findWithEquals(paramString, i);
  }

  public void replace(SettableBeanProperty paramSettableBeanProperty)
  {
    String str = paramSettableBeanProperty.getName();
    int i = str.hashCode() & -1 + this._buckets.length;
    int j = 0;
    Bucket localBucket = this._buckets[i];
    Object localObject1 = null;
    if (localBucket != null)
    {
      if ((j == 0) && (localBucket.key.equals(str)))
        j = 1;
      for (Object localObject2 = localObject1; ; localObject2 = new Bucket(localObject1, localBucket.key, localBucket.value))
      {
        localBucket = localBucket.next;
        localObject1 = localObject2;
        break;
      }
    }
    if (j == 0)
      throw new NoSuchElementException("No entry '" + paramSettableBeanProperty + "' found, can't replace");
    this._buckets[i] = new Bucket(localObject1, str, paramSettableBeanProperty);
  }

  public int size()
  {
    return this._size;
  }

  private static final class Bucket
  {
    public final String key;
    public final Bucket next;
    public final SettableBeanProperty value;

    public Bucket(Bucket paramBucket, String paramString, SettableBeanProperty paramSettableBeanProperty)
    {
      this.next = paramBucket;
      this.key = paramString;
      this.value = paramSettableBeanProperty;
    }
  }

  private static final class IteratorImpl
    implements Iterator<SettableBeanProperty>
  {
    private final BeanPropertyMap.Bucket[] _buckets;
    private BeanPropertyMap.Bucket _currentBucket;
    private int _nextBucketIndex;

    public IteratorImpl(BeanPropertyMap.Bucket[] paramArrayOfBucket)
    {
      this._buckets = paramArrayOfBucket;
      int i = this._buckets.length;
      int j = 0;
      int k;
      if (j < i)
      {
        BeanPropertyMap.Bucket[] arrayOfBucket = this._buckets;
        k = j + 1;
        BeanPropertyMap.Bucket localBucket = arrayOfBucket[j];
        if (localBucket != null)
          this._currentBucket = localBucket;
      }
      while (true)
      {
        this._nextBucketIndex = k;
        return;
        j = k;
        break;
        k = j;
      }
    }

    public boolean hasNext()
    {
      return this._currentBucket != null;
    }

    public SettableBeanProperty next()
    {
      BeanPropertyMap.Bucket localBucket1 = this._currentBucket;
      if (localBucket1 == null)
        throw new NoSuchElementException();
      BeanPropertyMap.Bucket[] arrayOfBucket;
      int i;
      for (BeanPropertyMap.Bucket localBucket2 = localBucket1.next; (localBucket2 == null) && (this._nextBucketIndex < this._buckets.length); localBucket2 = arrayOfBucket[i])
      {
        arrayOfBucket = this._buckets;
        i = this._nextBucketIndex;
        this._nextBucketIndex = (i + 1);
      }
      this._currentBucket = localBucket2;
      return localBucket1.value;
    }

    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.impl.BeanPropertyMap
 * JD-Core Version:    0.6.0
 */