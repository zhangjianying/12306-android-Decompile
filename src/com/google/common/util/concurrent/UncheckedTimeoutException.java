package com.google.common.util.concurrent;

public class UncheckedTimeoutException extends RuntimeException
{
  private static final long serialVersionUID;

  public UncheckedTimeoutException()
  {
  }

  public UncheckedTimeoutException(String paramString)
  {
    super(paramString);
  }

  public UncheckedTimeoutException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }

  public UncheckedTimeoutException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.UncheckedTimeoutException
 * JD-Core Version:    0.6.0
 */