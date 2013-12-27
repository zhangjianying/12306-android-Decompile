package org.codehaus.jackson.sym;

import java.util.Arrays;
import org.codehaus.jackson.util.InternCache;

public final class BytesToNameCanonicalizer
{
  protected static final int DEFAULT_TABLE_SIZE = 64;
  static final int INITIAL_COLLISION_LEN = 32;
  static final int LAST_VALID_BUCKET = 254;
  static final int MAX_ENTRIES_FOR_REUSE = 6000;
  protected static final int MAX_TABLE_SIZE = 65536;
  static final int MIN_HASH_SIZE = 16;
  private int _collCount;
  private int _collEnd;
  private Bucket[] _collList;
  private boolean _collListShared;
  private int _count;
  final boolean _intern;
  private int[] _mainHash;
  private int _mainHashMask;
  private boolean _mainHashShared;
  private Name[] _mainNames;
  private boolean _mainNamesShared;
  private transient boolean _needRehash;
  final BytesToNameCanonicalizer _parent;

  private BytesToNameCanonicalizer(int paramInt, boolean paramBoolean)
  {
    this._parent = null;
    this._intern = paramBoolean;
    if (paramInt < 16)
      paramInt = 16;
    while (true)
    {
      initTables(paramInt);
      return;
      if ((paramInt & paramInt - 1) == 0)
        continue;
      int i = 16;
      while (i < paramInt)
        i += i;
      paramInt = i;
    }
  }

  private BytesToNameCanonicalizer(BytesToNameCanonicalizer paramBytesToNameCanonicalizer, boolean paramBoolean)
  {
    this._parent = paramBytesToNameCanonicalizer;
    this._intern = paramBoolean;
    this._count = paramBytesToNameCanonicalizer._count;
    this._mainHashMask = paramBytesToNameCanonicalizer._mainHashMask;
    this._mainHash = paramBytesToNameCanonicalizer._mainHash;
    this._mainNames = paramBytesToNameCanonicalizer._mainNames;
    this._collList = paramBytesToNameCanonicalizer._collList;
    this._collCount = paramBytesToNameCanonicalizer._collCount;
    this._collEnd = paramBytesToNameCanonicalizer._collEnd;
    this._needRehash = false;
    this._mainHashShared = true;
    this._mainNamesShared = true;
    this._collListShared = true;
  }

  private void _addSymbol(int paramInt, Name paramName)
  {
    if (this._mainHashShared)
      unshareMain();
    if (this._needRehash)
      rehash();
    this._count = (1 + this._count);
    int i = paramInt & this._mainHashMask;
    int i1;
    if (this._mainNames[i] == null)
    {
      this._mainHash[i] = (paramInt << 8);
      if (this._mainNamesShared)
        unshareNames();
      this._mainNames[i] = paramName;
      int n = this._mainHash.length;
      if (this._count > n >> 1)
      {
        i1 = n >> 2;
        if (this._count <= n - i1)
          break label260;
        this._needRehash = true;
      }
    }
    label200: 
    do
    {
      return;
      if (this._collListShared)
        unshareCollision();
      this._collCount = (1 + this._collCount);
      int j = this._mainHash[i];
      int k = j & 0xFF;
      int m;
      if (k == 0)
        if (this._collEnd <= 254)
        {
          m = this._collEnd;
          this._collEnd = (1 + this._collEnd);
          if (m >= this._collList.length)
            expandCollision();
          this._mainHash[i] = (j & 0xFFFFFF00 | m + 1);
        }
      while (true)
      {
        this._collList[m] = new Bucket(paramName, this._collList[m]);
        break;
        m = findBestBucket();
        break label200;
        m = k - 1;
      }
    }
    while (this._collCount < i1);
    label260: this._needRehash = true;
  }

  public static final int calcHash(int paramInt)
  {
    int i = paramInt ^ paramInt >>> 16;
    return i ^ i >>> 8;
  }

  public static final int calcHash(int paramInt1, int paramInt2)
  {
    int i = paramInt2 + paramInt1 * 31;
    int j = i ^ i >>> 16;
    return j ^ j >>> 8;
  }

  public static final int calcHash(int[] paramArrayOfInt, int paramInt)
  {
    int i = paramArrayOfInt[0];
    for (int j = 1; j < paramInt; j++)
      i = i * 31 + paramArrayOfInt[j];
    int k = i ^ i >>> 16;
    return k ^ k >>> 8;
  }

  private static Name constructName(int paramInt1, String paramString, int paramInt2, int paramInt3)
  {
    if (paramInt3 == 0)
      return new Name1(paramString, paramInt1, paramInt2);
    return new Name2(paramString, paramInt1, paramInt2, paramInt3);
  }

  private static Name constructName(int paramInt1, String paramString, int[] paramArrayOfInt, int paramInt2)
  {
    if (paramInt2 < 4);
    int[] arrayOfInt;
    int i;
    switch (paramInt2)
    {
    default:
      arrayOfInt = new int[paramInt2];
      i = 0;
    case 1:
    case 2:
    case 3:
    }
    while (i < paramInt2)
    {
      arrayOfInt[i] = paramArrayOfInt[i];
      i++;
      continue;
      return new Name1(paramString, paramInt1, paramArrayOfInt[0]);
      return new Name2(paramString, paramInt1, paramArrayOfInt[0], paramArrayOfInt[1]);
      return new Name3(paramString, paramInt1, paramArrayOfInt[0], paramArrayOfInt[1], paramArrayOfInt[2]);
    }
    return new NameN(paramString, paramInt1, arrayOfInt, paramInt2);
  }

  public static BytesToNameCanonicalizer createRoot()
  {
    return new BytesToNameCanonicalizer(64, true);
  }

  private void expandCollision()
  {
    Bucket[] arrayOfBucket = this._collList;
    int i = arrayOfBucket.length;
    this._collList = new Bucket[i + i];
    System.arraycopy(arrayOfBucket, 0, this._collList, 0, i);
  }

  private int findBestBucket()
  {
    Bucket[] arrayOfBucket = this._collList;
    int i = 2147483647;
    int j = -1;
    int k = 0;
    int m = this._collEnd;
    while (k < m)
    {
      int n = arrayOfBucket[k].length();
      if (n < i)
      {
        if (n == 1)
          return k;
        i = n;
        j = k;
      }
      k++;
    }
    return j;
  }

  public static Name getEmptyName()
  {
    return Name1.getEmptyName();
  }

  private void initTables(int paramInt)
  {
    this._count = 0;
    this._mainHash = new int[paramInt];
    this._mainNames = new Name[paramInt];
    this._mainHashShared = false;
    this._mainNamesShared = false;
    this._mainHashMask = (paramInt - 1);
    this._collListShared = true;
    this._collList = null;
    this._collEnd = 0;
    this._needRehash = false;
  }

  private void markAsShared()
  {
    this._mainHashShared = true;
    this._mainNamesShared = true;
    this._collListShared = true;
  }

  private void mergeChild(BytesToNameCanonicalizer paramBytesToNameCanonicalizer)
  {
    monitorenter;
    while (true)
    {
      try
      {
        int i = paramBytesToNameCanonicalizer._count;
        int j = this._count;
        if (i <= j)
          return;
        if (paramBytesToNameCanonicalizer.size() > 6000)
        {
          initTables(64);
          continue;
        }
      }
      finally
      {
        monitorexit;
      }
      this._count = paramBytesToNameCanonicalizer._count;
      this._mainHash = paramBytesToNameCanonicalizer._mainHash;
      this._mainNames = paramBytesToNameCanonicalizer._mainNames;
      this._mainHashShared = true;
      this._mainNamesShared = true;
      this._mainHashMask = paramBytesToNameCanonicalizer._mainHashMask;
      this._collList = paramBytesToNameCanonicalizer._collList;
      this._collCount = paramBytesToNameCanonicalizer._collCount;
      this._collEnd = paramBytesToNameCanonicalizer._collEnd;
    }
  }

  private void nukeSymbols()
  {
    this._count = 0;
    Arrays.fill(this._mainHash, 0);
    Arrays.fill(this._mainNames, null);
    Arrays.fill(this._collList, null);
    this._collCount = 0;
    this._collEnd = 0;
  }

  private void rehash()
  {
    this._needRehash = false;
    this._mainNamesShared = false;
    int i = this._mainHash.length;
    int j = i + i;
    if (j > 65536)
      nukeSymbols();
    int k;
    label330: 
    do
    {
      int n;
      do
      {
        return;
        this._mainHash = new int[j];
        this._mainHashMask = (j - 1);
        Name[] arrayOfName = this._mainNames;
        this._mainNames = new Name[j];
        k = 0;
        for (int m = 0; m < i; m++)
        {
          Name localName2 = arrayOfName[m];
          if (localName2 == null)
            continue;
          k++;
          int i7 = localName2.hashCode();
          int i8 = i7 & this._mainHashMask;
          this._mainNames[i8] = localName2;
          this._mainHash[i8] = (i7 << 8);
        }
        n = this._collEnd;
      }
      while (n == 0);
      this._collCount = 0;
      this._collEnd = 0;
      this._collListShared = false;
      Bucket[] arrayOfBucket1 = this._collList;
      this._collList = new Bucket[arrayOfBucket1.length];
      for (int i1 = 0; i1 < n; i1++)
      {
        Bucket localBucket1 = arrayOfBucket1[i1];
        while (localBucket1 != null)
        {
          k++;
          Name localName1 = localBucket1._name;
          int i2 = localName1.hashCode();
          int i3 = i2 & this._mainHashMask;
          int i4 = this._mainHash[i3];
          if (this._mainNames[i3] == null)
          {
            this._mainHash[i3] = (i2 << 8);
            this._mainNames[i3] = localName1;
            localBucket1 = localBucket1._next;
            continue;
          }
          this._collCount = (1 + this._collCount);
          int i5 = i4 & 0xFF;
          int i6;
          if (i5 == 0)
            if (this._collEnd <= 254)
            {
              i6 = this._collEnd;
              this._collEnd = (1 + this._collEnd);
              if (i6 >= this._collList.length)
                expandCollision();
              this._mainHash[i3] = (i4 & 0xFFFFFF00 | i6 + 1);
            }
          while (true)
          {
            Bucket[] arrayOfBucket2 = this._collList;
            Bucket localBucket2 = new Bucket(localName1, this._collList[i6]);
            arrayOfBucket2[i6] = localBucket2;
            break;
            i6 = findBestBucket();
            break label330;
            i6 = i5 - 1;
          }
        }
      }
    }
    while (k == this._count);
    throw new RuntimeException("Internal error: count after rehash " + k + "; should be " + this._count);
  }

  private void unshareCollision()
  {
    Bucket[] arrayOfBucket = this._collList;
    if (arrayOfBucket == null)
      this._collList = new Bucket[32];
    while (true)
    {
      this._collListShared = false;
      return;
      int i = arrayOfBucket.length;
      this._collList = new Bucket[i];
      System.arraycopy(arrayOfBucket, 0, this._collList, 0, i);
    }
  }

  private void unshareMain()
  {
    int[] arrayOfInt = this._mainHash;
    int i = this._mainHash.length;
    this._mainHash = new int[i];
    System.arraycopy(arrayOfInt, 0, this._mainHash, 0, i);
    this._mainHashShared = false;
  }

  private void unshareNames()
  {
    Name[] arrayOfName = this._mainNames;
    int i = arrayOfName.length;
    this._mainNames = new Name[i];
    System.arraycopy(arrayOfName, 0, this._mainNames, 0, i);
    this._mainNamesShared = false;
  }

  public Name addName(String paramString, int paramInt1, int paramInt2)
  {
    if (this._intern)
      paramString = InternCache.instance.intern(paramString);
    if (paramInt2 == 0);
    for (int i = calcHash(paramInt1); ; i = calcHash(paramInt1, paramInt2))
    {
      Name localName = constructName(i, paramString, paramInt1, paramInt2);
      _addSymbol(i, localName);
      return localName;
    }
  }

  public Name addName(String paramString, int[] paramArrayOfInt, int paramInt)
  {
    if (this._intern)
      paramString = InternCache.instance.intern(paramString);
    int i = calcHash(paramArrayOfInt, paramInt);
    Name localName = constructName(i, paramString, paramArrayOfInt, paramInt);
    _addSymbol(i, localName);
    return localName;
  }

  public Name findName(int paramInt)
  {
    int i = calcHash(paramInt);
    int j = i & this._mainHashMask;
    int k = this._mainHash[j];
    if ((i ^ k >> 8) << 8 == 0)
    {
      Name localName = this._mainNames[j];
      if (localName == null)
        localName = null;
      do
        return localName;
      while (localName.equals(paramInt));
    }
    while (true)
    {
      int m = k & 0xFF;
      if (m <= 0)
        break;
      int n = m - 1;
      Bucket localBucket = this._collList[n];
      if (localBucket == null)
        break;
      return localBucket.find(i, paramInt, 0);
      if (k == 0)
        return null;
    }
    return null;
  }

  public Name findName(int paramInt1, int paramInt2)
  {
    int i = calcHash(paramInt1, paramInt2);
    int j = i & this._mainHashMask;
    int k = this._mainHash[j];
    if ((i ^ k >> 8) << 8 == 0)
    {
      Name localName = this._mainNames[j];
      if (localName == null)
        localName = null;
      do
        return localName;
      while (localName.equals(paramInt1, paramInt2));
    }
    while (true)
    {
      int m = k & 0xFF;
      if (m <= 0)
        break;
      int n = m - 1;
      Bucket localBucket = this._collList[n];
      if (localBucket == null)
        break;
      return localBucket.find(i, paramInt1, paramInt2);
      if (k == 0)
        return null;
    }
    return null;
  }

  public Name findName(int[] paramArrayOfInt, int paramInt)
  {
    int i = calcHash(paramArrayOfInt, paramInt);
    int j = i & this._mainHashMask;
    int k = this._mainHash[j];
    if ((i ^ k >> 8) << 8 == 0)
    {
      Name localName = this._mainNames[j];
      if ((localName == null) || (localName.equals(paramArrayOfInt, paramInt)))
        return localName;
    }
    else if (k == 0)
    {
      return null;
    }
    int m = k & 0xFF;
    if (m > 0)
    {
      int n = m - 1;
      Bucket localBucket = this._collList[n];
      if (localBucket != null)
        return localBucket.find(i, paramArrayOfInt, paramInt);
    }
    return null;
  }

  public BytesToNameCanonicalizer makeChild(boolean paramBoolean1, boolean paramBoolean2)
  {
    monitorenter;
    try
    {
      BytesToNameCanonicalizer localBytesToNameCanonicalizer = new BytesToNameCanonicalizer(this, paramBoolean2);
      monitorexit;
      return localBytesToNameCanonicalizer;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public boolean maybeDirty()
  {
    return !this._mainHashShared;
  }

  public void release()
  {
    if ((maybeDirty()) && (this._parent != null))
    {
      this._parent.mergeChild(this);
      markAsShared();
    }
  }

  public int size()
  {
    return this._count;
  }

  static final class Bucket
  {
    protected final Name _name;
    protected final Bucket _next;

    Bucket(Name paramName, Bucket paramBucket)
    {
      this._name = paramName;
      this._next = paramBucket;
    }

    public Name find(int paramInt1, int paramInt2, int paramInt3)
    {
      Name localName;
      if ((this._name.hashCode() == paramInt1) && (this._name.equals(paramInt2, paramInt3)))
      {
        localName = this._name;
        return localName;
      }
      for (Bucket localBucket = this._next; ; localBucket = localBucket._next)
      {
        if (localBucket == null)
          break label79;
        localName = localBucket._name;
        if ((localName.hashCode() == paramInt1) && (localName.equals(paramInt2, paramInt3)))
          break;
      }
      label79: return null;
    }

    public Name find(int paramInt1, int[] paramArrayOfInt, int paramInt2)
    {
      Name localName;
      if ((this._name.hashCode() == paramInt1) && (this._name.equals(paramArrayOfInt, paramInt2)))
      {
        localName = this._name;
        return localName;
      }
      for (Bucket localBucket = this._next; ; localBucket = localBucket._next)
      {
        if (localBucket == null)
          break label79;
        localName = localBucket._name;
        if ((localName.hashCode() == paramInt1) && (localName.equals(paramArrayOfInt, paramInt2)))
          break;
      }
      label79: return null;
    }

    public int length()
    {
      int i = 1;
      for (Bucket localBucket = this._next; localBucket != null; localBucket = localBucket._next)
        i++;
      return i;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.sym.BytesToNameCanonicalizer
 * JD-Core Version:    0.6.0
 */