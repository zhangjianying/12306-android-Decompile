package com.tl.uic.model;

import java.io.Serializable;

public enum ScreenviewType
  implements Serializable
{
  private int value;

  static
  {
    ScreenviewType[] arrayOfScreenviewType = new ScreenviewType[2];
    arrayOfScreenviewType[0] = LOAD;
    arrayOfScreenviewType[1] = UNLOAD;
    ENUM$VALUES = arrayOfScreenviewType;
  }

  private ScreenviewType(int arg3)
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
 * Qualified Name:     com.tl.uic.model.ScreenviewType
 * JD-Core Version:    0.6.0
 */