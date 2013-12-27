package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;

final class SortedLists
{
  static <E> int binarySearch(List<? extends E> paramList, @Nullable E paramE, Comparator<? super E> paramComparator, Relation paramRelation)
  {
    return binarySearch(paramList, paramE, paramComparator, paramRelation, true);
  }

  static <E> int binarySearch(List<? extends E> paramList, @Nullable E paramE, Comparator<? super E> paramComparator, Relation paramRelation, boolean paramBoolean)
  {
    Preconditions.checkNotNull(paramComparator);
    Preconditions.checkNotNull(paramRelation);
    int i = 0;
    int j = -1 + paramList.size();
    while (i <= j)
    {
      int k = i + (j - i) / 2;
      int m = paramComparator.compare(paramE, paramList.get(k));
      if (m < 0)
      {
        j = k - 1;
        continue;
      }
      if (m > 0)
      {
        i = k + 1;
        continue;
      }
      return paramRelation.exactMatchFound(paramList, paramE, i, k, j, paramComparator, paramBoolean);
    }
    return paramRelation.exactMatchNotFound(paramList, paramE, i, paramComparator);
  }

  static abstract enum Relation
  {
    static
    {
      FLOOR = new Relation("FLOOR", 1)
      {
        <E> int exactMatchFound(List<? extends E> paramList, E paramE, int paramInt1, int paramInt2, int paramInt3, Comparator<? super E> paramComparator, boolean paramBoolean)
        {
          if (!paramBoolean)
            return paramInt2;
          int i = paramInt2;
          while (paramInt1 < i)
          {
            int j = paramInt1 + (i - paramInt1) / 2;
            if (paramComparator.compare(paramList.get(j), paramE) < 0)
            {
              paramInt1 = j + 1;
              continue;
            }
            i = j;
          }
          return paramInt1;
        }

        <E> int exactMatchNotFound(List<? extends E> paramList, E paramE, int paramInt, Comparator<? super E> paramComparator)
        {
          return paramInt - 1;
        }

        SortedLists.Relation reverse()
        {
          return CEILING;
        }
      };
      EQUAL = new Relation("EQUAL", 2)
      {
        <E> int exactMatchFound(List<? extends E> paramList, E paramE, int paramInt1, int paramInt2, int paramInt3, Comparator<? super E> paramComparator, boolean paramBoolean)
        {
          return paramInt2;
        }

        <E> int exactMatchNotFound(List<? extends E> paramList, E paramE, int paramInt, Comparator<? super E> paramComparator)
        {
          return -1;
        }

        SortedLists.Relation reverse()
        {
          return this;
        }
      };
      CEILING = new Relation("CEILING", 3)
      {
        <E> int exactMatchFound(List<? extends E> paramList, E paramE, int paramInt1, int paramInt2, int paramInt3, Comparator<? super E> paramComparator, boolean paramBoolean)
        {
          if (!paramBoolean)
            return paramInt2;
          int i = paramInt2;
          while (i < paramInt3)
          {
            int j = i + (1 + (paramInt3 - i)) / 2;
            if (paramComparator.compare(paramList.get(j), paramE) > 0)
            {
              paramInt3 = j - 1;
              continue;
            }
            i = j;
          }
          return i;
        }

        <E> int exactMatchNotFound(List<? extends E> paramList, E paramE, int paramInt, Comparator<? super E> paramComparator)
        {
          return paramInt;
        }

        SortedLists.Relation reverse()
        {
          return FLOOR;
        }
      };
      HIGHER = new Relation("HIGHER", 4)
      {
        <E> int exactMatchFound(List<? extends E> paramList, E paramE, int paramInt1, int paramInt2, int paramInt3, Comparator<? super E> paramComparator, boolean paramBoolean)
        {
          return 1 + CEILING.exactMatchFound(paramList, paramE, paramInt1, paramInt2, paramInt3, paramComparator, paramBoolean);
        }

        <E> int exactMatchNotFound(List<? extends E> paramList, E paramE, int paramInt, Comparator<? super E> paramComparator)
        {
          return paramInt;
        }

        SortedLists.Relation reverse()
        {
          return LOWER;
        }
      };
      Relation[] arrayOfRelation = new Relation[5];
      arrayOfRelation[0] = LOWER;
      arrayOfRelation[1] = FLOOR;
      arrayOfRelation[2] = EQUAL;
      arrayOfRelation[3] = CEILING;
      arrayOfRelation[4] = HIGHER;
      $VALUES = arrayOfRelation;
    }

    abstract <E> int exactMatchFound(List<? extends E> paramList, @Nullable E paramE, int paramInt1, int paramInt2, int paramInt3, Comparator<? super E> paramComparator, boolean paramBoolean);

    abstract <E> int exactMatchNotFound(List<? extends E> paramList, @Nullable E paramE, int paramInt, Comparator<? super E> paramComparator);

    abstract Relation reverse();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.SortedLists
 * JD-Core Version:    0.6.0
 */