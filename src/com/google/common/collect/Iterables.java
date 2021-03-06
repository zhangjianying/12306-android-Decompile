package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
public final class Iterables
{
  public static <T> boolean addAll(Collection<T> paramCollection, Iterable<? extends T> paramIterable)
  {
    if ((paramIterable instanceof Collection))
      return paramCollection.addAll(Collections2.cast(paramIterable));
    return Iterators.addAll(paramCollection, paramIterable.iterator());
  }

  public static <T> boolean all(Iterable<T> paramIterable, Predicate<? super T> paramPredicate)
  {
    return Iterators.all(paramIterable.iterator(), paramPredicate);
  }

  public static <T> boolean any(Iterable<T> paramIterable, Predicate<? super T> paramPredicate)
  {
    return Iterators.any(paramIterable.iterator(), paramPredicate);
  }

  private static void checkNonnegativeIndex(int paramInt)
  {
    if (paramInt < 0)
      throw new IndexOutOfBoundsException("position cannot be negative: " + paramInt);
  }

  public static <T> Iterable<T> concat(Iterable<? extends Iterable<? extends T>> paramIterable)
  {
    Preconditions.checkNotNull(paramIterable);
    return new IterableWithToString(paramIterable)
    {
      public Iterator<T> iterator()
      {
        return Iterators.concat(Iterables.access$000(this.val$inputs));
      }
    };
  }

  public static <T> Iterable<T> concat(Iterable<? extends T> paramIterable1, Iterable<? extends T> paramIterable2)
  {
    Preconditions.checkNotNull(paramIterable1);
    Preconditions.checkNotNull(paramIterable2);
    return concat(Arrays.asList(new Iterable[] { paramIterable1, paramIterable2 }));
  }

  public static <T> Iterable<T> concat(Iterable<? extends T> paramIterable1, Iterable<? extends T> paramIterable2, Iterable<? extends T> paramIterable3)
  {
    Preconditions.checkNotNull(paramIterable1);
    Preconditions.checkNotNull(paramIterable2);
    Preconditions.checkNotNull(paramIterable3);
    return concat(Arrays.asList(new Iterable[] { paramIterable1, paramIterable2, paramIterable3 }));
  }

  public static <T> Iterable<T> concat(Iterable<? extends T> paramIterable1, Iterable<? extends T> paramIterable2, Iterable<? extends T> paramIterable3, Iterable<? extends T> paramIterable4)
  {
    Preconditions.checkNotNull(paramIterable1);
    Preconditions.checkNotNull(paramIterable2);
    Preconditions.checkNotNull(paramIterable3);
    Preconditions.checkNotNull(paramIterable4);
    return concat(Arrays.asList(new Iterable[] { paramIterable1, paramIterable2, paramIterable3, paramIterable4 }));
  }

  public static <T> Iterable<T> concat(Iterable<? extends T>[] paramArrayOfIterable)
  {
    return concat(ImmutableList.copyOf(paramArrayOfIterable));
  }

  public static <T> Iterable<T> consumingIterable(Iterable<T> paramIterable)
  {
    if ((paramIterable instanceof Queue))
      return new Iterable(paramIterable)
      {
        public Iterator<T> iterator()
        {
          return new Iterables.ConsumingQueueIterator((Queue)this.val$iterable, null);
        }
      };
    Preconditions.checkNotNull(paramIterable);
    return new Iterable(paramIterable)
    {
      public Iterator<T> iterator()
      {
        return Iterators.consumingIterator(this.val$iterable.iterator());
      }
    };
  }

  public static boolean contains(Iterable<?> paramIterable, @Nullable Object paramObject)
  {
    if ((paramIterable instanceof Collection))
    {
      Collection localCollection = (Collection)paramIterable;
      try
      {
        boolean bool = localCollection.contains(paramObject);
        return bool;
      }
      catch (NullPointerException localNullPointerException)
      {
        return false;
      }
      catch (ClassCastException localClassCastException)
      {
        return false;
      }
    }
    return Iterators.contains(paramIterable.iterator(), paramObject);
  }

  public static <T> Iterable<T> cycle(Iterable<T> paramIterable)
  {
    Preconditions.checkNotNull(paramIterable);
    return new Iterable(paramIterable)
    {
      public Iterator<T> iterator()
      {
        return Iterators.cycle(this.val$iterable);
      }

      public String toString()
      {
        return this.val$iterable.toString() + " (cycled)";
      }
    };
  }

  public static <T> Iterable<T> cycle(T[] paramArrayOfT)
  {
    return cycle(Lists.newArrayList(paramArrayOfT));
  }

  public static boolean elementsEqual(Iterable<?> paramIterable1, Iterable<?> paramIterable2)
  {
    return Iterators.elementsEqual(paramIterable1.iterator(), paramIterable2.iterator());
  }

  public static <T> Iterable<T> filter(Iterable<T> paramIterable, Predicate<? super T> paramPredicate)
  {
    Preconditions.checkNotNull(paramIterable);
    Preconditions.checkNotNull(paramPredicate);
    return new IterableWithToString(paramIterable, paramPredicate)
    {
      public Iterator<T> iterator()
      {
        return Iterators.filter(this.val$unfiltered.iterator(), this.val$predicate);
      }
    };
  }

  @GwtIncompatible("Class.isInstance")
  public static <T> Iterable<T> filter(Iterable<?> paramIterable, Class<T> paramClass)
  {
    Preconditions.checkNotNull(paramIterable);
    Preconditions.checkNotNull(paramClass);
    return new IterableWithToString(paramIterable, paramClass)
    {
      public Iterator<T> iterator()
      {
        return Iterators.filter(this.val$unfiltered.iterator(), this.val$type);
      }
    };
  }

  public static <T> T find(Iterable<T> paramIterable, Predicate<? super T> paramPredicate)
  {
    return Iterators.find(paramIterable.iterator(), paramPredicate);
  }

  public static <T> T find(Iterable<T> paramIterable, Predicate<? super T> paramPredicate, @Nullable T paramT)
  {
    return Iterators.find(paramIterable.iterator(), paramPredicate, paramT);
  }

  public static int frequency(Iterable<?> paramIterable, @Nullable Object paramObject)
  {
    if ((paramIterable instanceof Multiset))
      return ((Multiset)paramIterable).count(paramObject);
    if ((paramIterable instanceof Set))
    {
      if (((Set)paramIterable).contains(paramObject))
        return 1;
      return 0;
    }
    return Iterators.frequency(paramIterable.iterator(), paramObject);
  }

  public static <T> T get(Iterable<T> paramIterable, int paramInt)
  {
    Preconditions.checkNotNull(paramIterable);
    if ((paramIterable instanceof List))
      return ((List)paramIterable).get(paramInt);
    if ((paramIterable instanceof Collection))
      Preconditions.checkElementIndex(paramInt, ((Collection)paramIterable).size());
    while (true)
    {
      return Iterators.get(paramIterable.iterator(), paramInt);
      checkNonnegativeIndex(paramInt);
    }
  }

  public static <T> T get(Iterable<T> paramIterable, int paramInt, @Nullable T paramT)
  {
    Preconditions.checkNotNull(paramIterable);
    checkNonnegativeIndex(paramInt);
    try
    {
      Object localObject = get(paramIterable, paramInt);
      return localObject;
    }
    catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
    {
    }
    return paramT;
  }

  public static <T> T getFirst(Iterable<T> paramIterable, @Nullable T paramT)
  {
    return Iterators.getNext(paramIterable.iterator(), paramT);
  }

  public static <T> T getLast(Iterable<T> paramIterable)
  {
    if ((paramIterable instanceof List))
    {
      List localList = (List)paramIterable;
      if (localList.isEmpty())
        throw new NoSuchElementException();
      return getLastInNonemptyList(localList);
    }
    if ((paramIterable instanceof SortedSet))
      return ((SortedSet)paramIterable).last();
    return Iterators.getLast(paramIterable.iterator());
  }

  public static <T> T getLast(Iterable<T> paramIterable, @Nullable T paramT)
  {
    if (((paramIterable instanceof Collection)) && (((Collection)paramIterable).isEmpty()))
      return paramT;
    if ((paramIterable instanceof List))
      return getLastInNonemptyList((List)paramIterable);
    if ((paramIterable instanceof SortedSet))
      return ((SortedSet)paramIterable).last();
    return Iterators.getLast(paramIterable.iterator(), paramT);
  }

  private static <T> T getLastInNonemptyList(List<T> paramList)
  {
    return paramList.get(-1 + paramList.size());
  }

  public static <T> T getOnlyElement(Iterable<T> paramIterable)
  {
    return Iterators.getOnlyElement(paramIterable.iterator());
  }

  public static <T> T getOnlyElement(Iterable<T> paramIterable, @Nullable T paramT)
  {
    return Iterators.getOnlyElement(paramIterable.iterator(), paramT);
  }

  public static <T> int indexOf(Iterable<T> paramIterable, Predicate<? super T> paramPredicate)
  {
    return Iterators.indexOf(paramIterable.iterator(), paramPredicate);
  }

  public static <T> boolean isEmpty(Iterable<T> paramIterable)
  {
    return !paramIterable.iterator().hasNext();
  }

  private static <T> UnmodifiableIterator<Iterator<? extends T>> iterators(Iterable<? extends Iterable<? extends T>> paramIterable)
  {
    return new UnmodifiableIterator(paramIterable.iterator())
    {
      public boolean hasNext()
      {
        return this.val$iterableIterator.hasNext();
      }

      public Iterator<? extends T> next()
      {
        return ((Iterable)this.val$iterableIterator.next()).iterator();
      }
    };
  }

  public static <T> Iterable<T> limit(Iterable<T> paramIterable, int paramInt)
  {
    Preconditions.checkNotNull(paramIterable);
    if (paramInt >= 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool, "limit is negative");
      return new IterableWithToString(paramIterable, paramInt)
      {
        public Iterator<T> iterator()
        {
          return Iterators.limit(this.val$iterable.iterator(), this.val$limitSize);
        }
      };
    }
  }

  public static <T> Iterable<List<T>> paddedPartition(Iterable<T> paramIterable, int paramInt)
  {
    Preconditions.checkNotNull(paramIterable);
    if (paramInt > 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      return new IterableWithToString(paramIterable, paramInt)
      {
        public Iterator<List<T>> iterator()
        {
          return Iterators.paddedPartition(this.val$iterable.iterator(), this.val$size);
        }
      };
    }
  }

  public static <T> Iterable<List<T>> partition(Iterable<T> paramIterable, int paramInt)
  {
    Preconditions.checkNotNull(paramIterable);
    if (paramInt > 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      return new IterableWithToString(paramIterable, paramInt)
      {
        public Iterator<List<T>> iterator()
        {
          return Iterators.partition(this.val$iterable.iterator(), this.val$size);
        }
      };
    }
  }

  static boolean remove(Iterable<?> paramIterable, @Nullable Object paramObject)
  {
    Iterator localIterator = paramIterable.iterator();
    while (localIterator.hasNext())
    {
      if (!Objects.equal(localIterator.next(), paramObject))
        continue;
      localIterator.remove();
      return true;
    }
    return false;
  }

  public static boolean removeAll(Iterable<?> paramIterable, Collection<?> paramCollection)
  {
    if ((paramIterable instanceof Collection))
      return ((Collection)paramIterable).removeAll((Collection)Preconditions.checkNotNull(paramCollection));
    return Iterators.removeAll(paramIterable.iterator(), paramCollection);
  }

  public static <T> boolean removeIf(Iterable<T> paramIterable, Predicate<? super T> paramPredicate)
  {
    if (((paramIterable instanceof RandomAccess)) && ((paramIterable instanceof List)))
      return removeIfFromRandomAccessList((List)paramIterable, (Predicate)Preconditions.checkNotNull(paramPredicate));
    return Iterators.removeIf(paramIterable.iterator(), paramPredicate);
  }

  private static <T> boolean removeIfFromRandomAccessList(List<T> paramList, Predicate<? super T> paramPredicate)
  {
    int i = 0;
    int j = 0;
    while (i < paramList.size())
    {
      Object localObject = paramList.get(i);
      if (!paramPredicate.apply(localObject))
      {
        if (i > j)
          paramList.set(j, localObject);
        j++;
      }
      i++;
    }
    paramList.subList(j, paramList.size()).clear();
    return i != j;
  }

  public static boolean retainAll(Iterable<?> paramIterable, Collection<?> paramCollection)
  {
    if ((paramIterable instanceof Collection))
      return ((Collection)paramIterable).retainAll((Collection)Preconditions.checkNotNull(paramCollection));
    return Iterators.retainAll(paramIterable.iterator(), paramCollection);
  }

  @Deprecated
  public static <T> Iterable<T> reverse(List<T> paramList)
  {
    return Lists.reverse(paramList);
  }

  public static int size(Iterable<?> paramIterable)
  {
    if ((paramIterable instanceof Collection))
      return ((Collection)paramIterable).size();
    return Iterators.size(paramIterable.iterator());
  }

  public static <T> Iterable<T> skip(Iterable<T> paramIterable, int paramInt)
  {
    Preconditions.checkNotNull(paramIterable);
    if (paramInt >= 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool, "number to skip cannot be negative");
      if (!(paramIterable instanceof List))
        break;
      return new IterableWithToString(paramInt, (List)paramIterable)
      {
        public Iterator<T> iterator()
        {
          if (this.val$numberToSkip >= this.val$list.size())
            return Iterators.emptyIterator();
          return this.val$list.subList(this.val$numberToSkip, this.val$list.size()).iterator();
        }
      };
    }
    return new IterableWithToString(paramIterable, paramInt)
    {
      public Iterator<T> iterator()
      {
        Iterator localIterator = this.val$iterable.iterator();
        Iterators.skip(localIterator, this.val$numberToSkip);
        return new Iterator(localIterator)
        {
          boolean atStart = true;

          public boolean hasNext()
          {
            return this.val$iterator.hasNext();
          }

          public T next()
          {
            if (!hasNext())
              throw new NoSuchElementException();
            try
            {
              Object localObject2 = this.val$iterator.next();
              return localObject2;
            }
            finally
            {
              this.atStart = false;
            }
            throw localObject1;
          }

          public void remove()
          {
            if (this.atStart)
              throw new IllegalStateException();
            this.val$iterator.remove();
          }
        };
      }
    };
  }

  static Object[] toArray(Iterable<?> paramIterable)
  {
    return toCollection(paramIterable).toArray();
  }

  @GwtIncompatible("Array.newInstance(Class, int)")
  public static <T> T[] toArray(Iterable<? extends T> paramIterable, Class<T> paramClass)
  {
    Collection localCollection = toCollection(paramIterable);
    return localCollection.toArray(ObjectArrays.newArray(paramClass, localCollection.size()));
  }

  private static <E> Collection<E> toCollection(Iterable<E> paramIterable)
  {
    if ((paramIterable instanceof Collection))
      return (Collection)paramIterable;
    return Lists.newArrayList(paramIterable.iterator());
  }

  public static String toString(Iterable<?> paramIterable)
  {
    return Iterators.toString(paramIterable.iterator());
  }

  public static <F, T> Iterable<T> transform(Iterable<F> paramIterable, Function<? super F, ? extends T> paramFunction)
  {
    Preconditions.checkNotNull(paramIterable);
    Preconditions.checkNotNull(paramFunction);
    return new IterableWithToString(paramIterable, paramFunction)
    {
      public Iterator<T> iterator()
      {
        return Iterators.transform(this.val$fromIterable.iterator(), this.val$function);
      }
    };
  }

  public static <T> Iterable<T> unmodifiableIterable(Iterable<T> paramIterable)
  {
    Preconditions.checkNotNull(paramIterable);
    return new Iterable(paramIterable)
    {
      public Iterator<T> iterator()
      {
        return Iterators.unmodifiableIterator(this.val$iterable.iterator());
      }

      public String toString()
      {
        return this.val$iterable.toString();
      }
    };
  }

  private static class ConsumingQueueIterator<T> extends AbstractIterator<T>
  {
    private final Queue<T> queue;

    private ConsumingQueueIterator(Queue<T> paramQueue)
    {
      this.queue = paramQueue;
    }

    public T computeNext()
    {
      try
      {
        Object localObject = this.queue.remove();
        return localObject;
      }
      catch (NoSuchElementException localNoSuchElementException)
      {
      }
      return endOfData();
    }
  }

  static abstract class IterableWithToString<E>
    implements Iterable<E>
  {
    public String toString()
    {
      return Iterables.toString(this);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Iterables
 * JD-Core Version:    0.6.0
 */