package org.codehaus.jackson.sym;

import java.util.Arrays;
import org.codehaus.jackson.util.InternCache;

public final class CharsToNameCanonicalizer
{
  protected static final int DEFAULT_TABLE_SIZE = 64;
  static final int MAX_ENTRIES_FOR_REUSE = 12000;
  protected static final int MAX_TABLE_SIZE = 65536;
  static final CharsToNameCanonicalizer sBootstrapSymbolTable = new CharsToNameCanonicalizer();
  protected Bucket[] _buckets;
  protected final boolean _canonicalize;
  protected boolean _dirty;
  protected int _indexMask;
  protected final boolean _intern;
  protected CharsToNameCanonicalizer _parent;
  protected int _size;
  protected int _sizeThreshold;
  protected String[] _symbols;

  private CharsToNameCanonicalizer()
  {
    this._canonicalize = true;
    this._intern = true;
    this._dirty = true;
    initTables(64);
  }

  private CharsToNameCanonicalizer(CharsToNameCanonicalizer paramCharsToNameCanonicalizer, boolean paramBoolean1, boolean paramBoolean2, String[] paramArrayOfString, Bucket[] paramArrayOfBucket, int paramInt)
  {
    this._parent = paramCharsToNameCanonicalizer;
    this._canonicalize = paramBoolean1;
    this._intern = paramBoolean2;
    this._symbols = paramArrayOfString;
    this._buckets = paramArrayOfBucket;
    this._size = paramInt;
    int i = paramArrayOfString.length;
    this._sizeThreshold = (i - (i >> 2));
    this._indexMask = (i - 1);
    this._dirty = false;
  }

  public static int calcHash(String paramString)
  {
    int i = paramString.charAt(0);
    int j = 1;
    int k = paramString.length();
    while (j < k)
    {
      i = i * 31 + paramString.charAt(j);
      j++;
    }
    return i;
  }

  public static int calcHash(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    int i = paramArrayOfChar[0];
    for (int j = 1; j < paramInt2; j++)
      i = i * 31 + paramArrayOfChar[j];
    return i;
  }

  private void copyArrays()
  {
    String[] arrayOfString = this._symbols;
    int i = arrayOfString.length;
    this._symbols = new String[i];
    System.arraycopy(arrayOfString, 0, this._symbols, 0, i);
    Bucket[] arrayOfBucket = this._buckets;
    int j = arrayOfBucket.length;
    this._buckets = new Bucket[j];
    System.arraycopy(arrayOfBucket, 0, this._buckets, 0, j);
  }

  public static CharsToNameCanonicalizer createRoot()
  {
    return sBootstrapSymbolTable.makeOrphan();
  }

  private void initTables(int paramInt)
  {
    this._symbols = new String[paramInt];
    this._buckets = new Bucket[paramInt >> 1];
    this._indexMask = (paramInt - 1);
    this._size = 0;
    this._sizeThreshold = (paramInt - (paramInt >> 2));
  }

  private CharsToNameCanonicalizer makeOrphan()
  {
    return new CharsToNameCanonicalizer(null, true, true, this._symbols, this._buckets, this._size);
  }

  private void mergeChild(CharsToNameCanonicalizer paramCharsToNameCanonicalizer)
  {
    monitorenter;
    try
    {
      if (paramCharsToNameCanonicalizer.size() > 12000)
        initTables(64);
      while (true)
      {
        this._dirty = false;
        do
          return;
        while (paramCharsToNameCanonicalizer.size() <= size());
        this._symbols = paramCharsToNameCanonicalizer._symbols;
        this._buckets = paramCharsToNameCanonicalizer._buckets;
        this._size = paramCharsToNameCanonicalizer._size;
        this._sizeThreshold = paramCharsToNameCanonicalizer._sizeThreshold;
        this._indexMask = paramCharsToNameCanonicalizer._indexMask;
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  private void rehash()
  {
    int i = this._symbols.length;
    int j = i + i;
    if (j > 65536)
    {
      this._size = 0;
      Arrays.fill(this._symbols, null);
      Arrays.fill(this._buckets, null);
      this._dirty = true;
    }
    int k;
    label155: 
    do
    {
      return;
      String[] arrayOfString = this._symbols;
      Bucket[] arrayOfBucket = this._buckets;
      this._symbols = new String[j];
      this._buckets = new Bucket[j >> 1];
      this._indexMask = (j - 1);
      this._sizeThreshold += this._sizeThreshold;
      k = 0;
      int m = 0;
      if (m < i)
      {
        String str2 = arrayOfString[m];
        int i4;
        if (str2 != null)
        {
          k++;
          i4 = calcHash(str2) & this._indexMask;
          if (this._symbols[i4] != null)
            break label155;
          this._symbols[i4] = str2;
        }
        while (true)
        {
          m++;
          break;
          int i5 = i4 >> 1;
          this._buckets[i5] = new Bucket(str2, this._buckets[i5]);
        }
      }
      int n = i >> 1;
      for (int i1 = 0; i1 < n; i1++)
      {
        Bucket localBucket = arrayOfBucket[i1];
        if (localBucket == null)
          continue;
        k++;
        String str1 = localBucket.getSymbol();
        int i2 = calcHash(str1) & this._indexMask;
        if (this._symbols[i2] == null)
          this._symbols[i2] = str1;
        while (true)
        {
          localBucket = localBucket.getNext();
          break;
          int i3 = i2 >> 1;
          this._buckets[i3] = new Bucket(str1, this._buckets[i3]);
        }
      }
    }
    while (k == this._size);
    throw new Error("Internal error on SymbolTable.rehash(): had " + this._size + " entries; now have " + k + ".");
  }

  public String findSymbol(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3)
  {
    String str1;
    if (paramInt2 < 1)
      str1 = "";
    int i;
    int k;
    label65: label81: String str2;
    while (true)
    {
      return str1;
      if (!this._canonicalize)
        return new String(paramArrayOfChar, paramInt1, paramInt2);
      i = paramInt3 & this._indexMask;
      str1 = this._symbols[i];
      if (str1 != null)
      {
        if (str1.length() == paramInt2)
        {
          k = 0;
          if (str1.charAt(k) == paramArrayOfChar[(paramInt1 + k)])
            break;
          if (k == paramInt2)
            break label205;
        }
        Bucket localBucket = this._buckets[(i >> 1)];
        if (localBucket != null)
        {
          str1 = localBucket.find(paramArrayOfChar, paramInt1, paramInt2);
          if (str1 != null)
            continue;
        }
      }
      if (this._dirty)
        break label207;
      copyArrays();
      this._dirty = true;
      label134: this._size = (1 + this._size);
      str2 = new String(paramArrayOfChar, paramInt1, paramInt2);
      if (this._intern)
        str2 = InternCache.instance.intern(str2);
      if (this._symbols[i] != null)
        break label238;
      this._symbols[i] = str2;
    }
    while (true)
    {
      return str2;
      k++;
      if (k < paramInt2)
        break label65;
      break label81;
      label205: break;
      label207: if (this._size < this._sizeThreshold)
        break label134;
      rehash();
      i = calcHash(paramArrayOfChar, paramInt1, paramInt2) & this._indexMask;
      break label134;
      label238: int j = i >> 1;
      this._buckets[j] = new Bucket(str2, this._buckets[j]);
    }
  }

  public CharsToNameCanonicalizer makeChild(boolean paramBoolean1, boolean paramBoolean2)
  {
    monitorenter;
    try
    {
      CharsToNameCanonicalizer localCharsToNameCanonicalizer = new CharsToNameCanonicalizer(this, paramBoolean1, paramBoolean2, this._symbols, this._buckets, this._size);
      monitorexit;
      return localCharsToNameCanonicalizer;
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
    return this._dirty;
  }

  public void release()
  {
    if (!maybeDirty());
    do
      return;
    while (this._parent == null);
    this._parent.mergeChild(this);
    this._dirty = false;
  }

  public int size()
  {
    return this._size;
  }

  static final class Bucket
  {
    private final String _symbol;
    private final Bucket mNext;

    public Bucket(String paramString, Bucket paramBucket)
    {
      this._symbol = paramString;
      this.mNext = paramBucket;
    }

    public String find(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    {
      String str = this._symbol;
      for (Bucket localBucket = this.mNext; ; localBucket = localBucket.getNext())
      {
        if (str.length() == paramInt2)
        {
          int i = 0;
          if (str.charAt(i) != paramArrayOfChar[(paramInt1 + i)]);
          while (true)
          {
            if (i != paramInt2)
              break label61;
            return str;
            i++;
            if (i < paramInt2)
              break;
          }
        }
        label61: if (localBucket == null)
          return null;
        str = localBucket.getSymbol();
      }
    }

    public Bucket getNext()
    {
      return this.mNext;
    }

    public String getSymbol()
    {
      return this._symbol;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.sym.CharsToNameCanonicalizer
 * JD-Core Version:    0.6.0
 */