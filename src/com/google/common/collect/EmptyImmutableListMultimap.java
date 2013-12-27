package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable=true)
class EmptyImmutableListMultimap extends ImmutableListMultimap<Object, Object>
{
  static final EmptyImmutableListMultimap INSTANCE = new EmptyImmutableListMultimap();
  private static final long serialVersionUID;

  private EmptyImmutableListMultimap()
  {
    super(ImmutableMap.of(), 0);
  }

  private Object readResolve()
  {
    return INSTANCE;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.EmptyImmutableListMultimap
 * JD-Core Version:    0.6.0
 */