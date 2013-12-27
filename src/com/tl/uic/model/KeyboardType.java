package com.tl.uic.model;

import java.io.Serializable;

public enum KeyboardType
  implements Serializable
{
  private int value;

  static
  {
    NO_KEYS = new KeyboardType("NO_KEYS", 1, 1);
    QWERTY = new KeyboardType("QWERTY", 2, 2);
    UNDEFINED = new KeyboardType("UNDEFINED", 3, 3);
    KeyboardType[] arrayOfKeyboardType = new KeyboardType[4];
    arrayOfKeyboardType[0] = TWELVE_KEYS;
    arrayOfKeyboardType[1] = NO_KEYS;
    arrayOfKeyboardType[2] = QWERTY;
    arrayOfKeyboardType[3] = UNDEFINED;
    ENUM$VALUES = arrayOfKeyboardType;
  }

  private KeyboardType(int arg3)
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
 * Qualified Name:     com.tl.uic.model.KeyboardType
 * JD-Core Version:    0.6.0
 */