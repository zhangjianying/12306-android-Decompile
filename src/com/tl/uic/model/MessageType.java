package com.tl.uic.model;

import java.io.Serializable;

public enum MessageType
  implements Serializable
{
  private int value;

  static
  {
    CONNECTION = new MessageType("CONNECTION", 2, 3);
    CONTROL = new MessageType("CONTROL", 3, 4);
    CUSTOM_EVENT = new MessageType("CUSTOM_EVENT", 4, 5);
    EXCEPTION = new MessageType("EXCEPTION", 5, 6);
    MessageType[] arrayOfMessageType = new MessageType[6];
    arrayOfMessageType[0] = CLIENT_STATE;
    arrayOfMessageType[1] = SCREENVIEW;
    arrayOfMessageType[2] = CONNECTION;
    arrayOfMessageType[3] = CONTROL;
    arrayOfMessageType[4] = CUSTOM_EVENT;
    arrayOfMessageType[5] = EXCEPTION;
    ENUM$VALUES = arrayOfMessageType;
  }

  private MessageType(int arg3)
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
 * Qualified Name:     com.tl.uic.model.MessageType
 * JD-Core Version:    0.6.0
 */