package com.tl.uic.model;

import java.io.Serializable;

public enum KeyboardStateType
  implements Serializable
{
  private int value;

  static
  {
    KeyboardStateType[] arrayOfKeyboardStateType = new KeyboardStateType[3];
    arrayOfKeyboardStateType[0] = HIDDEN_FALSE;
    arrayOfKeyboardStateType[1] = HIDDEN_TRUE;
    arrayOfKeyboardStateType[2] = UNDEFINED;
    ENUM$VALUES = arrayOfKeyboardStateType;
  }

  private KeyboardStateType(int arg3)
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
 * Qualified Name:     com.tl.uic.model.KeyboardStateType
 * JD-Core Version:    0.6.0
 */