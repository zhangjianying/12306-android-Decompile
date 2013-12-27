package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Beta
@GwtCompatible
public abstract class CharMatcher
  implements Predicate<Character>
{
  public static final CharMatcher ANY;
  public static final CharMatcher ASCII;
  public static final CharMatcher BREAKING_WHITESPACE;
  private static final String BREAKING_WHITESPACE_CHARS = "\t\n\013\f\r     　";
  public static final CharMatcher DIGIT;
  public static final CharMatcher INVISIBLE;
  public static final CharMatcher JAVA_DIGIT;
  public static final CharMatcher JAVA_ISO_CONTROL;
  public static final CharMatcher JAVA_LETTER;
  public static final CharMatcher JAVA_LETTER_OR_DIGIT;
  public static final CharMatcher JAVA_LOWER_CASE;
  public static final CharMatcher JAVA_UPPER_CASE;
  public static final CharMatcher JAVA_WHITESPACE;
  public static final CharMatcher NONE;
  private static final String NON_BREAKING_WHITESPACE_CHARS = " ᠎ ";
  public static final CharMatcher SINGLE_WIDTH;
  public static final CharMatcher WHITESPACE = anyOf("\t\n\013\f\r     　 ᠎ ").or(inRange(' ', ' ')).precomputed();

  static
  {
    BREAKING_WHITESPACE = anyOf("\t\n\013\f\r     　").or(inRange(' ', ' ')).or(inRange(' ', ' ')).precomputed();
    ASCII = inRange('\000', '');
    CharMatcher localCharMatcher = inRange('0', '9');
    for (char c : "٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".toCharArray())
      localCharMatcher = localCharMatcher.or(inRange(c, (char)(c + '\t')));
    DIGIT = localCharMatcher.precomputed();
    JAVA_WHITESPACE = inRange('\t', '\r').or(inRange('\034', ' ')).or(is(' ')).or(is('᠎')).or(inRange(' ', ' ')).or(inRange(' ', '​')).or(inRange(' ', ' ')).or(is(' ')).or(is('　')).precomputed();
    JAVA_DIGIT = new CharMatcher()
    {
      public boolean matches(char paramChar)
      {
        return Character.isDigit(paramChar);
      }
    };
    JAVA_LETTER = new CharMatcher()
    {
      public boolean matches(char paramChar)
      {
        return Character.isLetter(paramChar);
      }
    };
    JAVA_LETTER_OR_DIGIT = new CharMatcher()
    {
      public boolean matches(char paramChar)
      {
        return Character.isLetterOrDigit(paramChar);
      }
    };
    JAVA_UPPER_CASE = new CharMatcher()
    {
      public boolean matches(char paramChar)
      {
        return Character.isUpperCase(paramChar);
      }
    };
    JAVA_LOWER_CASE = new CharMatcher()
    {
      public boolean matches(char paramChar)
      {
        return Character.isLowerCase(paramChar);
      }
    };
    JAVA_ISO_CONTROL = inRange('\000', '\037').or(inRange('', ''));
    INVISIBLE = inRange('\000', ' ').or(inRange('', ' ')).or(is('­')).or(inRange('؀', '؃')).or(anyOf("۝܏ ឴឵᠎")).or(inRange(' ', '‏')).or(inRange(' ', ' ')).or(inRange(' ', '⁤')).or(inRange('⁪', '⁯')).or(is('　')).or(inRange(55296, 63743)).or(anyOf("﻿￹￺￻")).precomputed();
    SINGLE_WIDTH = inRange('\000', 'ӹ').or(is('־')).or(inRange('א', 'ת')).or(is('׳')).or(is('״')).or(inRange('؀', 'ۿ')).or(inRange('ݐ', 'ݿ')).or(inRange('฀', '๿')).or(inRange('Ḁ', '₯')).or(inRange('℀', '℺')).or(inRange(64336, 65023)).or(inRange(65136, 65279)).or(inRange(65377, 65500)).precomputed();
    ANY = new CharMatcher()
    {
      public CharMatcher and(CharMatcher paramCharMatcher)
      {
        return (CharMatcher)Preconditions.checkNotNull(paramCharMatcher);
      }

      public String collapseFrom(CharSequence paramCharSequence, char paramChar)
      {
        if (paramCharSequence.length() == 0)
          return "";
        return String.valueOf(paramChar);
      }

      public int countIn(CharSequence paramCharSequence)
      {
        return paramCharSequence.length();
      }

      public int indexIn(CharSequence paramCharSequence)
      {
        if (paramCharSequence.length() == 0)
          return -1;
        return 0;
      }

      public int indexIn(CharSequence paramCharSequence, int paramInt)
      {
        int i = paramCharSequence.length();
        Preconditions.checkPositionIndex(paramInt, i);
        if (paramInt == i)
          paramInt = -1;
        return paramInt;
      }

      public int lastIndexIn(CharSequence paramCharSequence)
      {
        return -1 + paramCharSequence.length();
      }

      public boolean matches(char paramChar)
      {
        return true;
      }

      public boolean matchesAllOf(CharSequence paramCharSequence)
      {
        Preconditions.checkNotNull(paramCharSequence);
        return true;
      }

      public boolean matchesNoneOf(CharSequence paramCharSequence)
      {
        return paramCharSequence.length() == 0;
      }

      public CharMatcher negate()
      {
        return NONE;
      }

      public CharMatcher or(CharMatcher paramCharMatcher)
      {
        Preconditions.checkNotNull(paramCharMatcher);
        return this;
      }

      public CharMatcher precomputed()
      {
        return this;
      }

      public String removeFrom(CharSequence paramCharSequence)
      {
        Preconditions.checkNotNull(paramCharSequence);
        return "";
      }

      public String replaceFrom(CharSequence paramCharSequence, char paramChar)
      {
        char[] arrayOfChar = new char[paramCharSequence.length()];
        Arrays.fill(arrayOfChar, paramChar);
        return new String(arrayOfChar);
      }

      public String replaceFrom(CharSequence paramCharSequence1, CharSequence paramCharSequence2)
      {
        StringBuilder localStringBuilder = new StringBuilder(paramCharSequence1.length() * paramCharSequence2.length());
        for (int i = 0; i < paramCharSequence1.length(); i++)
          localStringBuilder.append(paramCharSequence2);
        return localStringBuilder.toString();
      }

      public String trimFrom(CharSequence paramCharSequence)
      {
        Preconditions.checkNotNull(paramCharSequence);
        return "";
      }
    };
    NONE = new CharMatcher()
    {
      public CharMatcher and(CharMatcher paramCharMatcher)
      {
        Preconditions.checkNotNull(paramCharMatcher);
        return this;
      }

      public String collapseFrom(CharSequence paramCharSequence, char paramChar)
      {
        return paramCharSequence.toString();
      }

      public int countIn(CharSequence paramCharSequence)
      {
        Preconditions.checkNotNull(paramCharSequence);
        return 0;
      }

      public int indexIn(CharSequence paramCharSequence)
      {
        Preconditions.checkNotNull(paramCharSequence);
        return -1;
      }

      public int indexIn(CharSequence paramCharSequence, int paramInt)
      {
        Preconditions.checkPositionIndex(paramInt, paramCharSequence.length());
        return -1;
      }

      public int lastIndexIn(CharSequence paramCharSequence)
      {
        Preconditions.checkNotNull(paramCharSequence);
        return -1;
      }

      public boolean matches(char paramChar)
      {
        return false;
      }

      public boolean matchesAllOf(CharSequence paramCharSequence)
      {
        return paramCharSequence.length() == 0;
      }

      public boolean matchesNoneOf(CharSequence paramCharSequence)
      {
        Preconditions.checkNotNull(paramCharSequence);
        return true;
      }

      public CharMatcher negate()
      {
        return ANY;
      }

      public CharMatcher or(CharMatcher paramCharMatcher)
      {
        return (CharMatcher)Preconditions.checkNotNull(paramCharMatcher);
      }

      public CharMatcher precomputed()
      {
        return this;
      }

      public String removeFrom(CharSequence paramCharSequence)
      {
        return paramCharSequence.toString();
      }

      public String replaceFrom(CharSequence paramCharSequence, char paramChar)
      {
        return paramCharSequence.toString();
      }

      public String replaceFrom(CharSequence paramCharSequence1, CharSequence paramCharSequence2)
      {
        Preconditions.checkNotNull(paramCharSequence2);
        return paramCharSequence1.toString();
      }

      void setBits(CharMatcher.LookupTable paramLookupTable)
      {
      }

      public String trimFrom(CharSequence paramCharSequence)
      {
        return paramCharSequence.toString();
      }
    };
  }

  public static CharMatcher anyOf(CharSequence paramCharSequence)
  {
    switch (paramCharSequence.length())
    {
    default:
      char[] arrayOfChar = paramCharSequence.toString().toCharArray();
      Arrays.sort(arrayOfChar);
      return new CharMatcher(arrayOfChar)
      {
        public boolean matches(char paramChar)
        {
          return Arrays.binarySearch(this.val$chars, paramChar) >= 0;
        }

        void setBits(CharMatcher.LookupTable paramLookupTable)
        {
          char[] arrayOfChar = this.val$chars;
          int i = arrayOfChar.length;
          for (int j = 0; j < i; j++)
            paramLookupTable.set(arrayOfChar[j]);
        }
      };
    case 0:
      return NONE;
    case 1:
      return is(paramCharSequence.charAt(0));
    case 2:
    }
    return new CharMatcher(paramCharSequence.charAt(0), paramCharSequence.charAt(1))
    {
      public boolean matches(char paramChar)
      {
        return (paramChar == this.val$match1) || (paramChar == this.val$match2);
      }

      public CharMatcher precomputed()
      {
        return this;
      }

      void setBits(CharMatcher.LookupTable paramLookupTable)
      {
        paramLookupTable.set(this.val$match1);
        paramLookupTable.set(this.val$match2);
      }
    };
  }

  public static CharMatcher forPredicate(Predicate<? super Character> paramPredicate)
  {
    Preconditions.checkNotNull(paramPredicate);
    if ((paramPredicate instanceof CharMatcher))
      return (CharMatcher)paramPredicate;
    return new CharMatcher(paramPredicate)
    {
      public boolean apply(Character paramCharacter)
      {
        return this.val$predicate.apply(Preconditions.checkNotNull(paramCharacter));
      }

      public boolean matches(char paramChar)
      {
        return this.val$predicate.apply(Character.valueOf(paramChar));
      }
    };
  }

  public static CharMatcher inRange(char paramChar1, char paramChar2)
  {
    if (paramChar2 >= paramChar1);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      return new CharMatcher(paramChar1, paramChar2)
      {
        public boolean matches(char paramChar)
        {
          return (this.val$startInclusive <= paramChar) && (paramChar <= this.val$endInclusive);
        }

        public CharMatcher precomputed()
        {
          return this;
        }

        void setBits(CharMatcher.LookupTable paramLookupTable)
        {
          char c2;
          for (char c1 = this.val$startInclusive; ; c1 = c2)
          {
            paramLookupTable.set(c1);
            c2 = (char)(c1 + '\001');
            if (c1 == this.val$endInclusive)
              return;
          }
        }
      };
    }
  }

  public static CharMatcher is(char paramChar)
  {
    return new CharMatcher(paramChar)
    {
      public CharMatcher and(CharMatcher paramCharMatcher)
      {
        if (paramCharMatcher.matches(this.val$match))
          return this;
        return NONE;
      }

      public boolean matches(char paramChar)
      {
        return paramChar == this.val$match;
      }

      public CharMatcher negate()
      {
        return isNot(this.val$match);
      }

      public CharMatcher or(CharMatcher paramCharMatcher)
      {
        if (paramCharMatcher.matches(this.val$match))
          return paramCharMatcher;
        return super.or(paramCharMatcher);
      }

      public CharMatcher precomputed()
      {
        return this;
      }

      public String replaceFrom(CharSequence paramCharSequence, char paramChar)
      {
        return paramCharSequence.toString().replace(this.val$match, paramChar);
      }

      void setBits(CharMatcher.LookupTable paramLookupTable)
      {
        paramLookupTable.set(this.val$match);
      }
    };
  }

  public static CharMatcher isNot(char paramChar)
  {
    return new CharMatcher(paramChar)
    {
      public CharMatcher and(CharMatcher paramCharMatcher)
      {
        if (paramCharMatcher.matches(this.val$match))
          paramCharMatcher = super.and(paramCharMatcher);
        return paramCharMatcher;
      }

      public boolean matches(char paramChar)
      {
        return paramChar != this.val$match;
      }

      public CharMatcher negate()
      {
        return is(this.val$match);
      }

      public CharMatcher or(CharMatcher paramCharMatcher)
      {
        if (paramCharMatcher.matches(this.val$match))
          this = ANY;
        return this;
      }
    };
  }

  public static CharMatcher noneOf(CharSequence paramCharSequence)
  {
    return anyOf(paramCharSequence).negate();
  }

  public CharMatcher and(CharMatcher paramCharMatcher)
  {
    CharMatcher[] arrayOfCharMatcher = new CharMatcher[2];
    arrayOfCharMatcher[0] = this;
    arrayOfCharMatcher[1] = ((CharMatcher)Preconditions.checkNotNull(paramCharMatcher));
    return new And(Arrays.asList(arrayOfCharMatcher));
  }

  public boolean apply(Character paramCharacter)
  {
    return matches(paramCharacter.charValue());
  }

  public String collapseFrom(CharSequence paramCharSequence, char paramChar)
  {
    int i = indexIn(paramCharSequence);
    if (i == -1)
      return paramCharSequence.toString();
    StringBuilder localStringBuilder = new StringBuilder(paramCharSequence.length()).append(paramCharSequence.subSequence(0, i)).append(paramChar);
    int j = 1;
    int k = i + 1;
    if (k < paramCharSequence.length())
    {
      char c = paramCharSequence.charAt(k);
      if (apply(Character.valueOf(c)))
        if (j == 0)
          localStringBuilder.append(paramChar);
      for (j = 1; ; j = 0)
      {
        k++;
        break;
        localStringBuilder.append(c);
      }
    }
    return localStringBuilder.toString();
  }

  public int countIn(CharSequence paramCharSequence)
  {
    int i = 0;
    for (int j = 0; j < paramCharSequence.length(); j++)
    {
      if (!matches(paramCharSequence.charAt(j)))
        continue;
      i++;
    }
    return i;
  }

  public int indexIn(CharSequence paramCharSequence)
  {
    int i = paramCharSequence.length();
    for (int j = 0; j < i; j++)
      if (matches(paramCharSequence.charAt(j)))
        return j;
    return -1;
  }

  public int indexIn(CharSequence paramCharSequence, int paramInt)
  {
    int i = paramCharSequence.length();
    Preconditions.checkPositionIndex(paramInt, i);
    for (int j = paramInt; j < i; j++)
      if (matches(paramCharSequence.charAt(j)))
        return j;
    return -1;
  }

  public int lastIndexIn(CharSequence paramCharSequence)
  {
    for (int i = -1 + paramCharSequence.length(); i >= 0; i--)
      if (matches(paramCharSequence.charAt(i)))
        return i;
    return -1;
  }

  public abstract boolean matches(char paramChar);

  public boolean matchesAllOf(CharSequence paramCharSequence)
  {
    for (int i = -1 + paramCharSequence.length(); i >= 0; i--)
      if (!matches(paramCharSequence.charAt(i)))
        return false;
    return true;
  }

  public boolean matchesAnyOf(CharSequence paramCharSequence)
  {
    return !matchesNoneOf(paramCharSequence);
  }

  public boolean matchesNoneOf(CharSequence paramCharSequence)
  {
    return indexIn(paramCharSequence) == -1;
  }

  public CharMatcher negate()
  {
    return new CharMatcher(this)
    {
      public int countIn(CharSequence paramCharSequence)
      {
        return paramCharSequence.length() - this.val$original.countIn(paramCharSequence);
      }

      public boolean matches(char paramChar)
      {
        return !this.val$original.matches(paramChar);
      }

      public boolean matchesAllOf(CharSequence paramCharSequence)
      {
        return this.val$original.matchesNoneOf(paramCharSequence);
      }

      public boolean matchesNoneOf(CharSequence paramCharSequence)
      {
        return this.val$original.matchesAllOf(paramCharSequence);
      }

      public CharMatcher negate()
      {
        return this.val$original;
      }
    };
  }

  public CharMatcher or(CharMatcher paramCharMatcher)
  {
    CharMatcher[] arrayOfCharMatcher = new CharMatcher[2];
    arrayOfCharMatcher[0] = this;
    arrayOfCharMatcher[1] = ((CharMatcher)Preconditions.checkNotNull(paramCharMatcher));
    return new Or(Arrays.asList(arrayOfCharMatcher));
  }

  public CharMatcher precomputed()
  {
    return Platform.precomputeCharMatcher(this);
  }

  CharMatcher precomputedInternal()
  {
    LookupTable localLookupTable = new LookupTable(null);
    setBits(localLookupTable);
    return new CharMatcher(localLookupTable)
    {
      public boolean matches(char paramChar)
      {
        return this.val$table.get(paramChar);
      }

      public CharMatcher precomputed()
      {
        return this;
      }
    };
  }

  public String removeFrom(CharSequence paramCharSequence)
  {
    String str = paramCharSequence.toString();
    int i = indexIn(str);
    if (i == -1)
      return str;
    char[] arrayOfChar = str.toCharArray();
    int j = 1;
    i++;
    while (true)
    {
      if (i == arrayOfChar.length)
        return new String(arrayOfChar, 0, i - j);
      if (matches(arrayOfChar[i]))
      {
        j++;
        break;
      }
      arrayOfChar[(i - j)] = arrayOfChar[i];
      i++;
    }
  }

  public String replaceFrom(CharSequence paramCharSequence, char paramChar)
  {
    String str = paramCharSequence.toString();
    int i = indexIn(str);
    if (i == -1)
      return str;
    char[] arrayOfChar = str.toCharArray();
    arrayOfChar[i] = paramChar;
    for (int j = i + 1; j < arrayOfChar.length; j++)
    {
      if (!matches(arrayOfChar[j]))
        continue;
      arrayOfChar[j] = paramChar;
    }
    return new String(arrayOfChar);
  }

  public String replaceFrom(CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    int i = paramCharSequence2.length();
    String str;
    if (i == 0)
      str = removeFrom(paramCharSequence1);
    int j;
    do
    {
      return str;
      if (i == 1)
        return replaceFrom(paramCharSequence1, paramCharSequence2.charAt(0));
      str = paramCharSequence1.toString();
      j = indexIn(str);
    }
    while (j == -1);
    int k = str.length();
    StringBuilder localStringBuilder = new StringBuilder(16 + k * 3 / 2);
    int m = 0;
    do
    {
      localStringBuilder.append(str, m, j);
      localStringBuilder.append(paramCharSequence2);
      m = j + 1;
      j = indexIn(str, m);
    }
    while (j != -1);
    localStringBuilder.append(str, m, k);
    return localStringBuilder.toString();
  }

  public String retainFrom(CharSequence paramCharSequence)
  {
    return negate().removeFrom(paramCharSequence);
  }

  void setBits(LookupTable paramLookupTable)
  {
    int j;
    for (int i = 0; ; i = j)
    {
      if (matches(i))
        paramLookupTable.set(i);
      j = (char)(i + 1);
      if (i == 65535)
        return;
    }
  }

  public String trimAndCollapseFrom(CharSequence paramCharSequence, char paramChar)
  {
    int i = negate().indexIn(paramCharSequence);
    if (i == -1)
      return "";
    StringBuilder localStringBuilder = new StringBuilder(paramCharSequence.length());
    int j = 0;
    int k = i;
    if (k < paramCharSequence.length())
    {
      char c = paramCharSequence.charAt(k);
      if (apply(Character.valueOf(c)))
        j = 1;
      while (true)
      {
        k++;
        break;
        if (j != 0)
        {
          localStringBuilder.append(paramChar);
          j = 0;
        }
        localStringBuilder.append(c);
      }
    }
    return localStringBuilder.toString();
  }

  public String trimFrom(CharSequence paramCharSequence)
  {
    int i = paramCharSequence.length();
    int j = 0;
    if ((j >= i) || (!matches(paramCharSequence.charAt(j))));
    for (int k = i - 1; ; k--)
      if ((k <= j) || (!matches(paramCharSequence.charAt(k))))
      {
        return paramCharSequence.subSequence(j, k + 1).toString();
        j++;
        break;
      }
  }

  public String trimLeadingFrom(CharSequence paramCharSequence)
  {
    int i = paramCharSequence.length();
    for (int j = 0; ; j++)
      if ((j >= i) || (!matches(paramCharSequence.charAt(j))))
        return paramCharSequence.subSequence(j, i).toString();
  }

  public String trimTrailingFrom(CharSequence paramCharSequence)
  {
    for (int i = -1 + paramCharSequence.length(); ; i--)
      if ((i < 0) || (!matches(paramCharSequence.charAt(i))))
        return paramCharSequence.subSequence(0, i + 1).toString();
  }

  private static class And extends CharMatcher
  {
    List<CharMatcher> components;

    And(List<CharMatcher> paramList)
    {
      this.components = paramList;
    }

    public CharMatcher and(CharMatcher paramCharMatcher)
    {
      ArrayList localArrayList = new ArrayList(this.components);
      localArrayList.add(Preconditions.checkNotNull(paramCharMatcher));
      return new And(localArrayList);
    }

    public boolean matches(char paramChar)
    {
      Iterator localIterator = this.components.iterator();
      while (localIterator.hasNext())
        if (!((CharMatcher)localIterator.next()).matches(paramChar))
          return false;
      return true;
    }
  }

  private static final class LookupTable
  {
    int[] data = new int[2048];

    boolean get(char paramChar)
    {
      return (this.data[(paramChar >> '\005')] & '\001' << paramChar) != 0;
    }

    void set(char paramChar)
    {
      int[] arrayOfInt = this.data;
      int i = paramChar >> '\005';
      arrayOfInt[i] |= '\001' << paramChar;
    }
  }

  private static class Or extends CharMatcher
  {
    List<CharMatcher> components;

    Or(List<CharMatcher> paramList)
    {
      this.components = paramList;
    }

    public boolean matches(char paramChar)
    {
      Iterator localIterator = this.components.iterator();
      while (localIterator.hasNext())
        if (((CharMatcher)localIterator.next()).matches(paramChar))
          return true;
      return false;
    }

    public CharMatcher or(CharMatcher paramCharMatcher)
    {
      ArrayList localArrayList = new ArrayList(this.components);
      localArrayList.add(Preconditions.checkNotNull(paramCharMatcher));
      return new Or(localArrayList);
    }

    void setBits(CharMatcher.LookupTable paramLookupTable)
    {
      Iterator localIterator = this.components.iterator();
      while (localIterator.hasNext())
        ((CharMatcher)localIterator.next()).setBits(paramLookupTable);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.CharMatcher
 * JD-Core Version:    0.6.0
 */