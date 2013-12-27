package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable=true)
final class EmptyImmutableMultiset extends ImmutableMultiset<Object>
{
  static final EmptyImmutableMultiset INSTANCE = new EmptyImmutableMultiset();
  private static final long serialVersionUID;

  private EmptyImmutableMultiset()
  {
    super(ImmutableMap.of(), 0);
  }

  Object readResolve()
  {
    return INSTANCE;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.EmptyImmutableMultiset
 * JD-Core Version:    0.6.0
 */