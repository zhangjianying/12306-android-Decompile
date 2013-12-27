package com.google.common.io;

import java.io.IOException;

public abstract interface InputSupplier<T>
{
  public abstract T getInput()
    throws IOException;
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.io.InputSupplier
 * JD-Core Version:    0.6.0
 */