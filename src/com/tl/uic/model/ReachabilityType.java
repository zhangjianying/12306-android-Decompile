package com.tl.uic.model;

import java.io.Serializable;

public enum ReachabilityType
  implements Serializable
{
  private int value;

  static
  {
    NotReachable = new ReachabilityType("NotReachable", 1, 1);
    ReachableViaWIFI = new ReachabilityType("ReachableViaWIFI", 2, 2);
    ReachableViaWWAN = new ReachabilityType("ReachableViaWWAN", 3, 3);
    ReachabilityType[] arrayOfReachabilityType = new ReachabilityType[4];
    arrayOfReachabilityType[0] = Unknown;
    arrayOfReachabilityType[1] = NotReachable;
    arrayOfReachabilityType[2] = ReachableViaWIFI;
    arrayOfReachabilityType[3] = ReachableViaWWAN;
    ENUM$VALUES = arrayOfReachabilityType;
  }

  private ReachabilityType(int arg3)
  {
    int j;
    this.value = j;
  }

  public int getValue()
  {
    return this.value;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.ReachabilityType
 * JD-Core Version:    0.6.0
 */