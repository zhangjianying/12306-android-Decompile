package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
public final class Predicates
{
  private static final Joiner COMMA_JOINER = Joiner.on(",");

  @GwtCompatible(serializable=true)
  public static <T> Predicate<T> alwaysFalse()
  {
    return ObjectPredicate.ALWAYS_FALSE.withNarrowedType();
  }

  @GwtCompatible(serializable=true)
  public static <T> Predicate<T> alwaysTrue()
  {
    return ObjectPredicate.ALWAYS_TRUE.withNarrowedType();
  }

  public static <T> Predicate<T> and(Predicate<? super T> paramPredicate1, Predicate<? super T> paramPredicate2)
  {
    return new AndPredicate(asList((Predicate)Preconditions.checkNotNull(paramPredicate1), (Predicate)Preconditions.checkNotNull(paramPredicate2)), null);
  }

  public static <T> Predicate<T> and(Iterable<? extends Predicate<? super T>> paramIterable)
  {
    return new AndPredicate(defensiveCopy(paramIterable), null);
  }

  public static <T> Predicate<T> and(Predicate<? super T>[] paramArrayOfPredicate)
  {
    return new AndPredicate(defensiveCopy(paramArrayOfPredicate), null);
  }

  private static <T> List<Predicate<? super T>> asList(Predicate<? super T> paramPredicate1, Predicate<? super T> paramPredicate2)
  {
    return Arrays.asList(new Predicate[] { paramPredicate1, paramPredicate2 });
  }

  public static <A, B> Predicate<A> compose(Predicate<B> paramPredicate, Function<A, ? extends B> paramFunction)
  {
    return new CompositionPredicate(paramPredicate, paramFunction, null);
  }

  @GwtIncompatible("java.util.regex.Pattern")
  public static Predicate<CharSequence> contains(Pattern paramPattern)
  {
    return new ContainsPatternPredicate(paramPattern);
  }

  @GwtIncompatible("java.util.regex.Pattern")
  public static Predicate<CharSequence> containsPattern(String paramString)
  {
    return new ContainsPatternPredicate(paramString);
  }

  static <T> List<T> defensiveCopy(Iterable<T> paramIterable)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramIterable.iterator();
    while (localIterator.hasNext())
      localArrayList.add(Preconditions.checkNotNull(localIterator.next()));
    return localArrayList;
  }

  private static <T> List<T> defensiveCopy(T[] paramArrayOfT)
  {
    return defensiveCopy(Arrays.asList(paramArrayOfT));
  }

  public static <T> Predicate<T> equalTo(@Nullable T paramT)
  {
    if (paramT == null)
      return isNull();
    return new IsEqualToPredicate(paramT, null);
  }

  public static <T> Predicate<T> in(Collection<? extends T> paramCollection)
  {
    return new InPredicate(paramCollection, null);
  }

  @GwtIncompatible("Class.isInstance")
  public static Predicate<Object> instanceOf(Class<?> paramClass)
  {
    return new InstanceOfPredicate(paramClass, null);
  }

  @GwtCompatible(serializable=true)
  public static <T> Predicate<T> isNull()
  {
    return ObjectPredicate.IS_NULL.withNarrowedType();
  }

  public static <T> Predicate<T> not(Predicate<T> paramPredicate)
  {
    return new NotPredicate(paramPredicate);
  }

  @GwtCompatible(serializable=true)
  public static <T> Predicate<T> notNull()
  {
    return ObjectPredicate.NOT_NULL.withNarrowedType();
  }

  public static <T> Predicate<T> or(Predicate<? super T> paramPredicate1, Predicate<? super T> paramPredicate2)
  {
    return new OrPredicate(asList((Predicate)Preconditions.checkNotNull(paramPredicate1), (Predicate)Preconditions.checkNotNull(paramPredicate2)), null);
  }

  public static <T> Predicate<T> or(Iterable<? extends Predicate<? super T>> paramIterable)
  {
    return new OrPredicate(defensiveCopy(paramIterable), null);
  }

  public static <T> Predicate<T> or(Predicate<? super T>[] paramArrayOfPredicate)
  {
    return new OrPredicate(defensiveCopy(paramArrayOfPredicate), null);
  }

  private static class AndPredicate<T>
    implements Predicate<T>, Serializable
  {
    private static final long serialVersionUID;
    private final List<? extends Predicate<? super T>> components;

    private AndPredicate(List<? extends Predicate<? super T>> paramList)
    {
      this.components = paramList;
    }

    public boolean apply(T paramT)
    {
      Iterator localIterator = this.components.iterator();
      while (localIterator.hasNext())
        if (!((Predicate)localIterator.next()).apply(paramT))
          return false;
      return true;
    }

    public boolean equals(@Nullable Object paramObject)
    {
      if ((paramObject instanceof AndPredicate))
      {
        AndPredicate localAndPredicate = (AndPredicate)paramObject;
        return this.components.equals(localAndPredicate.components);
      }
      return false;
    }

    public int hashCode()
    {
      return 306654252 + this.components.hashCode();
    }

    public String toString()
    {
      return "And(" + Predicates.COMMA_JOINER.join(this.components) + ")";
    }
  }

  private static class CompositionPredicate<A, B>
    implements Predicate<A>, Serializable
  {
    private static final long serialVersionUID;
    final Function<A, ? extends B> f;
    final Predicate<B> p;

    private CompositionPredicate(Predicate<B> paramPredicate, Function<A, ? extends B> paramFunction)
    {
      this.p = ((Predicate)Preconditions.checkNotNull(paramPredicate));
      this.f = ((Function)Preconditions.checkNotNull(paramFunction));
    }

    public boolean apply(A paramA)
    {
      return this.p.apply(this.f.apply(paramA));
    }

    public boolean equals(@Nullable Object paramObject)
    {
      boolean bool1 = paramObject instanceof CompositionPredicate;
      int i = 0;
      if (bool1)
      {
        CompositionPredicate localCompositionPredicate = (CompositionPredicate)paramObject;
        boolean bool2 = this.f.equals(localCompositionPredicate.f);
        i = 0;
        if (bool2)
        {
          boolean bool3 = this.p.equals(localCompositionPredicate.p);
          i = 0;
          if (bool3)
            i = 1;
        }
      }
      return i;
    }

    public int hashCode()
    {
      return this.f.hashCode() ^ this.p.hashCode();
    }

    public String toString()
    {
      return this.p.toString() + "(" + this.f.toString() + ")";
    }
  }

  @GwtIncompatible("Only used by other GWT-incompatible code.")
  private static class ContainsPatternPredicate
    implements Predicate<CharSequence>, Serializable
  {
    private static final long serialVersionUID;
    final Pattern pattern;

    ContainsPatternPredicate(String paramString)
    {
      this(Pattern.compile(paramString));
    }

    ContainsPatternPredicate(Pattern paramPattern)
    {
      this.pattern = ((Pattern)Preconditions.checkNotNull(paramPattern));
    }

    public boolean apply(CharSequence paramCharSequence)
    {
      return this.pattern.matcher(paramCharSequence).find();
    }

    public boolean equals(@Nullable Object paramObject)
    {
      boolean bool1 = paramObject instanceof ContainsPatternPredicate;
      int i = 0;
      if (bool1)
      {
        ContainsPatternPredicate localContainsPatternPredicate = (ContainsPatternPredicate)paramObject;
        boolean bool2 = Objects.equal(this.pattern.pattern(), localContainsPatternPredicate.pattern.pattern());
        i = 0;
        if (bool2)
        {
          boolean bool3 = Objects.equal(Integer.valueOf(this.pattern.flags()), Integer.valueOf(localContainsPatternPredicate.pattern.flags()));
          i = 0;
          if (bool3)
            i = 1;
        }
      }
      return i;
    }

    public int hashCode()
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = this.pattern.pattern();
      arrayOfObject[1] = Integer.valueOf(this.pattern.flags());
      return Objects.hashCode(arrayOfObject);
    }

    public String toString()
    {
      return Objects.toStringHelper(this).add("pattern", this.pattern).add("pattern.flags", Integer.toHexString(this.pattern.flags())).toString();
    }
  }

  private static class InPredicate<T>
    implements Predicate<T>, Serializable
  {
    private static final long serialVersionUID;
    private final Collection<?> target;

    private InPredicate(Collection<?> paramCollection)
    {
      this.target = ((Collection)Preconditions.checkNotNull(paramCollection));
    }

    public boolean apply(T paramT)
    {
      try
      {
        boolean bool = this.target.contains(paramT);
        return bool;
      }
      catch (NullPointerException localNullPointerException)
      {
        return false;
      }
      catch (ClassCastException localClassCastException)
      {
      }
      return false;
    }

    public boolean equals(@Nullable Object paramObject)
    {
      if ((paramObject instanceof InPredicate))
      {
        InPredicate localInPredicate = (InPredicate)paramObject;
        return this.target.equals(localInPredicate.target);
      }
      return false;
    }

    public int hashCode()
    {
      return this.target.hashCode();
    }

    public String toString()
    {
      return "In(" + this.target + ")";
    }
  }

  private static class InstanceOfPredicate
    implements Predicate<Object>, Serializable
  {
    private static final long serialVersionUID;
    private final Class<?> clazz;

    private InstanceOfPredicate(Class<?> paramClass)
    {
      this.clazz = ((Class)Preconditions.checkNotNull(paramClass));
    }

    public boolean apply(@Nullable Object paramObject)
    {
      return Platform.isInstance(this.clazz, paramObject);
    }

    public boolean equals(@Nullable Object paramObject)
    {
      boolean bool = paramObject instanceof InstanceOfPredicate;
      int i = 0;
      if (bool)
      {
        InstanceOfPredicate localInstanceOfPredicate = (InstanceOfPredicate)paramObject;
        Class localClass1 = this.clazz;
        Class localClass2 = localInstanceOfPredicate.clazz;
        i = 0;
        if (localClass1 == localClass2)
          i = 1;
      }
      return i;
    }

    public int hashCode()
    {
      return this.clazz.hashCode();
    }

    public String toString()
    {
      return "IsInstanceOf(" + this.clazz.getName() + ")";
    }
  }

  private static class IsEqualToPredicate<T>
    implements Predicate<T>, Serializable
  {
    private static final long serialVersionUID;
    private final T target;

    private IsEqualToPredicate(T paramT)
    {
      this.target = paramT;
    }

    public boolean apply(T paramT)
    {
      return this.target.equals(paramT);
    }

    public boolean equals(@Nullable Object paramObject)
    {
      if ((paramObject instanceof IsEqualToPredicate))
      {
        IsEqualToPredicate localIsEqualToPredicate = (IsEqualToPredicate)paramObject;
        return this.target.equals(localIsEqualToPredicate.target);
      }
      return false;
    }

    public int hashCode()
    {
      return this.target.hashCode();
    }

    public String toString()
    {
      return "IsEqualTo(" + this.target + ")";
    }
  }

  private static class NotPredicate<T>
    implements Predicate<T>, Serializable
  {
    private static final long serialVersionUID;
    final Predicate<T> predicate;

    NotPredicate(Predicate<T> paramPredicate)
    {
      this.predicate = ((Predicate)Preconditions.checkNotNull(paramPredicate));
    }

    public boolean apply(T paramT)
    {
      return !this.predicate.apply(paramT);
    }

    public boolean equals(@Nullable Object paramObject)
    {
      if ((paramObject instanceof NotPredicate))
      {
        NotPredicate localNotPredicate = (NotPredicate)paramObject;
        return this.predicate.equals(localNotPredicate.predicate);
      }
      return false;
    }

    public int hashCode()
    {
      return 0xFFFFFFFF ^ this.predicate.hashCode();
    }

    public String toString()
    {
      return "Not(" + this.predicate.toString() + ")";
    }
  }

  static abstract enum ObjectPredicate
    implements Predicate<Object>
  {
    static
    {
      ALWAYS_FALSE = new ObjectPredicate("ALWAYS_FALSE", 1)
      {
        public boolean apply(@Nullable Object paramObject)
        {
          return false;
        }
      };
      IS_NULL = new ObjectPredicate("IS_NULL", 2)
      {
        public boolean apply(@Nullable Object paramObject)
        {
          return paramObject == null;
        }
      };
      NOT_NULL = new ObjectPredicate("NOT_NULL", 3)
      {
        public boolean apply(@Nullable Object paramObject)
        {
          return paramObject != null;
        }
      };
      ObjectPredicate[] arrayOfObjectPredicate = new ObjectPredicate[4];
      arrayOfObjectPredicate[0] = ALWAYS_TRUE;
      arrayOfObjectPredicate[1] = ALWAYS_FALSE;
      arrayOfObjectPredicate[2] = IS_NULL;
      arrayOfObjectPredicate[3] = NOT_NULL;
      $VALUES = arrayOfObjectPredicate;
    }

    <T> Predicate<T> withNarrowedType()
    {
      return this;
    }
  }

  private static class OrPredicate<T>
    implements Predicate<T>, Serializable
  {
    private static final long serialVersionUID;
    private final List<? extends Predicate<? super T>> components;

    private OrPredicate(List<? extends Predicate<? super T>> paramList)
    {
      this.components = paramList;
    }

    public boolean apply(T paramT)
    {
      Iterator localIterator = this.components.iterator();
      while (localIterator.hasNext())
        if (((Predicate)localIterator.next()).apply(paramT))
          return true;
      return false;
    }

    public boolean equals(@Nullable Object paramObject)
    {
      if ((paramObject instanceof OrPredicate))
      {
        OrPredicate localOrPredicate = (OrPredicate)paramObject;
        return this.components.equals(localOrPredicate.components);
      }
      return false;
    }

    public int hashCode()
    {
      return 87855567 + this.components.hashCode();
    }

    public String toString()
    {
      return "Or(" + Predicates.COMMA_JOINER.join(this.components) + ")";
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Predicates
 * JD-Core Version:    0.6.0
 */