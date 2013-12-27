package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.io.IOException;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public class Joiner
{
  private final String separator;

  private Joiner(Joiner paramJoiner)
  {
    this.separator = paramJoiner.separator;
  }

  private Joiner(String paramString)
  {
    this.separator = ((String)Preconditions.checkNotNull(paramString));
  }

  private static Iterable<Object> iterable(Object paramObject1, Object paramObject2, Object[] paramArrayOfObject)
  {
    Preconditions.checkNotNull(paramArrayOfObject);
    return new AbstractList(paramArrayOfObject, paramObject1, paramObject2)
    {
      public Object get(int paramInt)
      {
        switch (paramInt)
        {
        default:
          return this.val$rest[(paramInt - 2)];
        case 0:
          return this.val$first;
        case 1:
        }
        return this.val$second;
      }

      public int size()
      {
        return 2 + this.val$rest.length;
      }
    };
  }

  public static Joiner on(char paramChar)
  {
    return new Joiner(String.valueOf(paramChar));
  }

  public static Joiner on(String paramString)
  {
    return new Joiner(paramString);
  }

  public <A extends Appendable> A appendTo(A paramA, Iterable<?> paramIterable)
    throws IOException
  {
    Preconditions.checkNotNull(paramA);
    Iterator localIterator = paramIterable.iterator();
    if (localIterator.hasNext())
    {
      paramA.append(toString(localIterator.next()));
      while (localIterator.hasNext())
      {
        paramA.append(this.separator);
        paramA.append(toString(localIterator.next()));
      }
    }
    return paramA;
  }

  public final <A extends Appendable> A appendTo(A paramA, @Nullable Object paramObject1, @Nullable Object paramObject2, Object[] paramArrayOfObject)
    throws IOException
  {
    return appendTo(paramA, iterable(paramObject1, paramObject2, paramArrayOfObject));
  }

  public final <A extends Appendable> A appendTo(A paramA, Object[] paramArrayOfObject)
    throws IOException
  {
    return appendTo(paramA, Arrays.asList(paramArrayOfObject));
  }

  public final StringBuilder appendTo(StringBuilder paramStringBuilder, Iterable<?> paramIterable)
  {
    try
    {
      appendTo(paramStringBuilder, paramIterable);
      return paramStringBuilder;
    }
    catch (IOException localIOException)
    {
    }
    throw new AssertionError(localIOException);
  }

  public final StringBuilder appendTo(StringBuilder paramStringBuilder, @Nullable Object paramObject1, @Nullable Object paramObject2, Object[] paramArrayOfObject)
  {
    return appendTo(paramStringBuilder, iterable(paramObject1, paramObject2, paramArrayOfObject));
  }

  public final StringBuilder appendTo(StringBuilder paramStringBuilder, Object[] paramArrayOfObject)
  {
    return appendTo(paramStringBuilder, Arrays.asList(paramArrayOfObject));
  }

  public final String join(Iterable<?> paramIterable)
  {
    return appendTo(new StringBuilder(), paramIterable).toString();
  }

  public final String join(@Nullable Object paramObject1, @Nullable Object paramObject2, Object[] paramArrayOfObject)
  {
    return join(iterable(paramObject1, paramObject2, paramArrayOfObject));
  }

  public final String join(Object[] paramArrayOfObject)
  {
    return join(Arrays.asList(paramArrayOfObject));
  }

  public Joiner skipNulls()
  {
    return new Joiner(this)
    {
      public <A extends Appendable> A appendTo(A paramA, Iterable<?> paramIterable)
        throws IOException
      {
        Preconditions.checkNotNull(paramA, "appendable");
        Preconditions.checkNotNull(paramIterable, "parts");
        Iterator localIterator = paramIterable.iterator();
        while (localIterator.hasNext())
        {
          Object localObject2 = localIterator.next();
          if (localObject2 == null)
            continue;
          paramA.append(Joiner.this.toString(localObject2));
        }
        while (localIterator.hasNext())
        {
          Object localObject1 = localIterator.next();
          if (localObject1 == null)
            continue;
          paramA.append(Joiner.this.separator);
          paramA.append(Joiner.this.toString(localObject1));
        }
        return paramA;
      }

      public Joiner useForNull(String paramString)
      {
        Preconditions.checkNotNull(paramString);
        throw new UnsupportedOperationException("already specified skipNulls");
      }

      public Joiner.MapJoiner withKeyValueSeparator(String paramString)
      {
        Preconditions.checkNotNull(paramString);
        throw new UnsupportedOperationException("can't use .skipNulls() with maps");
      }
    };
  }

  CharSequence toString(Object paramObject)
  {
    if ((paramObject instanceof CharSequence))
      return (CharSequence)paramObject;
    return paramObject.toString();
  }

  public Joiner useForNull(String paramString)
  {
    Preconditions.checkNotNull(paramString);
    return new Joiner(this, paramString)
    {
      public Joiner skipNulls()
      {
        throw new UnsupportedOperationException("already specified useForNull");
      }

      CharSequence toString(Object paramObject)
      {
        if (paramObject == null)
          return this.val$nullText;
        return Joiner.this.toString(paramObject);
      }

      public Joiner useForNull(String paramString)
      {
        Preconditions.checkNotNull(paramString);
        throw new UnsupportedOperationException("already specified useForNull");
      }
    };
  }

  public MapJoiner withKeyValueSeparator(String paramString)
  {
    return new MapJoiner(this, paramString, null);
  }

  public static final class MapJoiner
  {
    private final Joiner joiner;
    private final String keyValueSeparator;

    private MapJoiner(Joiner paramJoiner, String paramString)
    {
      this.joiner = paramJoiner;
      this.keyValueSeparator = ((String)Preconditions.checkNotNull(paramString));
    }

    public <A extends Appendable> A appendTo(A paramA, Map<?, ?> paramMap)
      throws IOException
    {
      Preconditions.checkNotNull(paramA);
      Iterator localIterator = paramMap.entrySet().iterator();
      if (localIterator.hasNext())
      {
        Map.Entry localEntry1 = (Map.Entry)localIterator.next();
        paramA.append(this.joiner.toString(localEntry1.getKey()));
        paramA.append(this.keyValueSeparator);
        paramA.append(this.joiner.toString(localEntry1.getValue()));
        while (localIterator.hasNext())
        {
          paramA.append(this.joiner.separator);
          Map.Entry localEntry2 = (Map.Entry)localIterator.next();
          paramA.append(this.joiner.toString(localEntry2.getKey()));
          paramA.append(this.keyValueSeparator);
          paramA.append(this.joiner.toString(localEntry2.getValue()));
        }
      }
      return paramA;
    }

    public StringBuilder appendTo(StringBuilder paramStringBuilder, Map<?, ?> paramMap)
    {
      try
      {
        appendTo(paramStringBuilder, paramMap);
        return paramStringBuilder;
      }
      catch (IOException localIOException)
      {
      }
      throw new AssertionError(localIOException);
    }

    public String join(Map<?, ?> paramMap)
    {
      return appendTo(new StringBuilder(), paramMap).toString();
    }

    public MapJoiner useForNull(String paramString)
    {
      return new MapJoiner(this.joiner.useForNull(paramString), this.keyValueSeparator);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Joiner
 * JD-Core Version:    0.6.0
 */