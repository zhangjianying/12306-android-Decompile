package com.tl.uic.model;

import java.io.Serializable;

public enum OrientationType
  implements Serializable
{
  private int value;

  static
  {
    OrientationType[] arrayOfOrientationType = new OrientationType[4];
    arrayOfOrientationType[0] = LANDSCAPE;
    arrayOfOrientationType[1] = PORTRAIT;
    arrayOfOrientationType[2] = SQUARE;
    arrayOfOrientationType[3] = UNDEFINED;
    ENUM$VALUES = arrayOfOrientationType;
  }

  private OrientationType(int arg3)
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
 * Qualified Name:     com.tl.uic.model.OrientationType
 * JD-Core Version:    0.6.0
 */