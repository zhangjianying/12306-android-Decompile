package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class Hashing
{
  static int smear(int paramInt)
  {
    int i = paramInt ^ (paramInt >>> 20 ^ paramInt >>> 12);
    return i ^ i >>> 7 ^ i >>> 4;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Hashing
 * JD-Core Version:    0.6.0
 */