package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.primitives.Booleans;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import java.util.Comparator;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ComparisonChain
{
  private static final ComparisonChain ACTIVE = new ComparisonChain()
  {
    ComparisonChain classify(int paramInt)
    {
      if (paramInt < 0)
        return ComparisonChain.LESS;
      if (paramInt > 0)
        return ComparisonChain.GREATER;
      return ComparisonChain.ACTIVE;
    }

    public ComparisonChain compare(double paramDouble1, double paramDouble2)
    {
      return classify(Double.compare(paramDouble1, paramDouble2));
    }

    public ComparisonChain compare(float paramFloat1, float paramFloat2)
    {
      return classify(Float.compare(paramFloat1, paramFloat2));
    }

    public ComparisonChain compare(int paramInt1, int paramInt2)
    {
      return classify(Ints.compare(paramInt1, paramInt2));
    }

    public ComparisonChain compare(long paramLong1, long paramLong2)
    {
      return classify(Longs.compare(paramLong1, paramLong2));
    }

    public ComparisonChain compare(Comparable paramComparable1, Comparable paramComparable2)
    {
      return classify(paramComparable1.compareTo(paramComparable2));
    }

    public <T> ComparisonChain compare(@Nullable T paramT1, @Nullable T paramT2, Comparator<T> paramComparator)
    {
      return classify(paramComparator.compare(paramT1, paramT2));
    }

    public ComparisonChain compare(boolean paramBoolean1, boolean paramBoolean2)
    {
      return classify(Booleans.compare(paramBoolean1, paramBoolean2));
    }

    public int result()
    {
      return 0;
    }
  };
  private static final ComparisonChain GREATER;
  private static final ComparisonChain LESS = new InactiveComparisonChain(-1);

  static
  {
    GREATER = new InactiveComparisonChain(1);
  }

  public static ComparisonChain start()
  {
    return ACTIVE;
  }

  public abstract ComparisonChain compare(double paramDouble1, double paramDouble2);

  public abstract ComparisonChain compare(float paramFloat1, float paramFloat2);

  public abstract ComparisonChain compare(int paramInt1, int paramInt2);

  public abstract ComparisonChain compare(long paramLong1, long paramLong2);

  public abstract ComparisonChain compare(Comparable<?> paramComparable1, Comparable<?> paramComparable2);

  public abstract <T> ComparisonChain compare(@Nullable T paramT1, @Nullable T paramT2, Comparator<T> paramComparator);

  public abstract ComparisonChain compare(boolean paramBoolean1, boolean paramBoolean2);

  public abstract int result();

  private static final class InactiveComparisonChain extends ComparisonChain
  {
    final int result;

    InactiveComparisonChain(int paramInt)
    {
      super();
      this.result = paramInt;
    }

    public ComparisonChain compare(double paramDouble1, double paramDouble2)
    {
      return this;
    }

    public ComparisonChain compare(float paramFloat1, float paramFloat2)
    {
      return this;
    }

    public ComparisonChain compare(int paramInt1, int paramInt2)
    {
      return this;
    }

    public ComparisonChain compare(long paramLong1, long paramLong2)
    {
      return this;
    }

    public ComparisonChain compare(@Nullable Comparable paramComparable1, @Nullable Comparable paramComparable2)
    {
      return this;
    }

    public <T> ComparisonChain compare(@Nullable T paramT1, @Nullable T paramT2, @Nullable Comparator<T> paramComparator)
    {
      return this;
    }

    public ComparisonChain compare(boolean paramBoolean1, boolean paramBoolean2)
    {
      return this;
    }

    public int result()
    {
      return this.result;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ComparisonChain
 * JD-Core Version:    0.6.0
 */