package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@GwtCompatible(emulated=true)
public final class Splitter
{
  private final int limit;
  private final boolean omitEmptyStrings;
  private final Strategy strategy;
  private final CharMatcher trimmer;

  private Splitter(Strategy paramStrategy)
  {
    this(paramStrategy, false, CharMatcher.NONE, 2147483647);
  }

  private Splitter(Strategy paramStrategy, boolean paramBoolean, CharMatcher paramCharMatcher, int paramInt)
  {
    this.strategy = paramStrategy;
    this.omitEmptyStrings = paramBoolean;
    this.trimmer = paramCharMatcher;
    this.limit = paramInt;
  }

  public static Splitter fixedLength(int paramInt)
  {
    if (paramInt > 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool, "The length may not be less than 1");
      return new Splitter(new Strategy(paramInt)
      {
        public Splitter.SplittingIterator iterator(Splitter paramSplitter, CharSequence paramCharSequence)
        {
          return new Splitter.SplittingIterator(paramSplitter, paramCharSequence)
          {
            public int separatorEnd(int paramInt)
            {
              return paramInt;
            }

            public int separatorStart(int paramInt)
            {
              int i = paramInt + Splitter.4.this.val$length;
              if (i < this.toSplit.length())
                return i;
              return -1;
            }
          };
        }
      });
    }
  }

  public static Splitter on(char paramChar)
  {
    return on(CharMatcher.is(paramChar));
  }

  public static Splitter on(CharMatcher paramCharMatcher)
  {
    Preconditions.checkNotNull(paramCharMatcher);
    return new Splitter(new Strategy(paramCharMatcher)
    {
      public Splitter.SplittingIterator iterator(Splitter paramSplitter, CharSequence paramCharSequence)
      {
        return new Splitter.SplittingIterator(paramSplitter, paramCharSequence)
        {
          int separatorEnd(int paramInt)
          {
            return paramInt + 1;
          }

          int separatorStart(int paramInt)
          {
            return Splitter.1.this.val$separatorMatcher.indexIn(this.toSplit, paramInt);
          }
        };
      }
    });
  }

  public static Splitter on(String paramString)
  {
    if (paramString.length() != 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool, "The separator may not be the empty string.");
      return new Splitter(new Strategy(paramString)
      {
        public Splitter.SplittingIterator iterator(Splitter paramSplitter, CharSequence paramCharSequence)
        {
          return new Splitter.SplittingIterator(paramSplitter, paramCharSequence)
          {
            public int separatorEnd(int paramInt)
            {
              return paramInt + Splitter.2.this.val$separator.length();
            }

            public int separatorStart(int paramInt)
            {
              int i = Splitter.2.this.val$separator.length();
              int j = paramInt;
              int k = this.toSplit.length() - i;
              if (j <= k)
                for (int m = 0; ; m++)
                {
                  if (m >= i)
                    break label83;
                  if (this.toSplit.charAt(m + j) == Splitter.2.this.val$separator.charAt(m))
                    continue;
                  j++;
                  break;
                }
              j = -1;
              label83: return j;
            }
          };
        }
      });
    }
  }

  @GwtIncompatible("java.util.regex")
  public static Splitter on(Pattern paramPattern)
  {
    Preconditions.checkNotNull(paramPattern);
    if (!paramPattern.matcher("").matches());
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool, "The pattern may not match the empty string: %s", new Object[] { paramPattern });
      return new Splitter(new Strategy(paramPattern)
      {
        public Splitter.SplittingIterator iterator(Splitter paramSplitter, CharSequence paramCharSequence)
        {
          return new Splitter.SplittingIterator(paramSplitter, paramCharSequence, this.val$separatorPattern.matcher(paramCharSequence))
          {
            public int separatorEnd(int paramInt)
            {
              return this.val$matcher.end();
            }

            public int separatorStart(int paramInt)
            {
              if (this.val$matcher.find(paramInt))
                return this.val$matcher.start();
              return -1;
            }
          };
        }
      });
    }
  }

  @GwtIncompatible("java.util.regex")
  public static Splitter onPattern(String paramString)
  {
    return on(Pattern.compile(paramString));
  }

  @Beta
  public Splitter limit(int paramInt)
  {
    if (paramInt > 0);
    for (boolean bool = true; ; bool = false)
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      Preconditions.checkArgument(bool, "must be greater then zero: %s", arrayOfObject);
      return new Splitter(this.strategy, this.omitEmptyStrings, this.trimmer, paramInt);
    }
  }

  public Splitter omitEmptyStrings()
  {
    return new Splitter(this.strategy, true, this.trimmer, this.limit);
  }

  public Iterable<String> split(CharSequence paramCharSequence)
  {
    Preconditions.checkNotNull(paramCharSequence);
    return new Iterable(paramCharSequence)
    {
      public Iterator<String> iterator()
      {
        return Splitter.this.strategy.iterator(Splitter.this, this.val$sequence);
      }
    };
  }

  public Splitter trimResults()
  {
    return trimResults(CharMatcher.WHITESPACE);
  }

  public Splitter trimResults(CharMatcher paramCharMatcher)
  {
    Preconditions.checkNotNull(paramCharMatcher);
    return new Splitter(this.strategy, this.omitEmptyStrings, paramCharMatcher, this.limit);
  }

  private static abstract class AbstractIterator<T>
    implements Iterator<T>
  {
    T next;
    State state = State.NOT_READY;

    protected abstract T computeNext();

    protected final T endOfData()
    {
      this.state = State.DONE;
      return null;
    }

    public final boolean hasNext()
    {
      if (this.state != State.FAILED);
      for (boolean bool1 = true; ; bool1 = false)
      {
        Preconditions.checkState(bool1);
        int i = Splitter.6.$SwitchMap$com$google$common$base$Splitter$AbstractIterator$State[this.state.ordinal()];
        boolean bool2 = false;
        switch (i)
        {
        default:
          bool2 = tryToComputeNext();
        case 1:
          return bool2;
        case 2:
        }
      }
      return true;
    }

    public final T next()
    {
      if (!hasNext())
        throw new NoSuchElementException();
      this.state = State.NOT_READY;
      return this.next;
    }

    public void remove()
    {
      throw new UnsupportedOperationException();
    }

    boolean tryToComputeNext()
    {
      this.state = State.FAILED;
      this.next = computeNext();
      if (this.state != State.DONE)
      {
        this.state = State.READY;
        return true;
      }
      return false;
    }

    static enum State
    {
      static
      {
        NOT_READY = new State("NOT_READY", 1);
        DONE = new State("DONE", 2);
        FAILED = new State("FAILED", 3);
        State[] arrayOfState = new State[4];
        arrayOfState[0] = READY;
        arrayOfState[1] = NOT_READY;
        arrayOfState[2] = DONE;
        arrayOfState[3] = FAILED;
        $VALUES = arrayOfState;
      }
    }
  }

  private static abstract class SplittingIterator extends Splitter.AbstractIterator<String>
  {
    int limit;
    int offset = 0;
    final boolean omitEmptyStrings;
    final CharSequence toSplit;
    final CharMatcher trimmer;

    protected SplittingIterator(Splitter paramSplitter, CharSequence paramCharSequence)
    {
      super();
      this.trimmer = paramSplitter.trimmer;
      this.omitEmptyStrings = paramSplitter.omitEmptyStrings;
      this.limit = paramSplitter.limit;
      this.toSplit = paramCharSequence;
    }

    protected String computeNext()
    {
      while (this.offset != -1)
      {
        int i = this.offset;
        int j = separatorStart(this.offset);
        int k;
        if (j == -1)
        {
          k = this.toSplit.length();
          this.offset = -1;
        }
        while ((i < k) && (this.trimmer.matches(this.toSplit.charAt(i))))
        {
          i++;
          continue;
          k = j;
          this.offset = separatorEnd(j);
        }
        while ((k > i) && (this.trimmer.matches(this.toSplit.charAt(k - 1))))
          k--;
        if ((this.omitEmptyStrings) && (i == k))
          continue;
        if (this.limit == 1)
        {
          k = this.toSplit.length();
          this.offset = -1;
          while ((k > i) && (this.trimmer.matches(this.toSplit.charAt(k - 1))))
            k--;
        }
        this.limit = (-1 + this.limit);
        return this.toSplit.subSequence(i, k).toString();
      }
      return (String)endOfData();
    }

    abstract int separatorEnd(int paramInt);

    abstract int separatorStart(int paramInt);
  }

  private static abstract interface Strategy
  {
    public abstract Iterator<String> iterator(Splitter paramSplitter, CharSequence paramCharSequence);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Splitter
 * JD-Core Version:    0.6.0
 */