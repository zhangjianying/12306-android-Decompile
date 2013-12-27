package org.codehaus.jackson;

public class JsonGenerationException extends JsonProcessingException
{
  static final long serialVersionUID = 123L;

  public JsonGenerationException(String paramString)
  {
    super(paramString, (JsonLocation)null);
  }

  public JsonGenerationException(String paramString, Throwable paramThrowable)
  {
    super(paramString, (JsonLocation)null, paramThrowable);
  }

  public JsonGenerationException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.JsonGenerationException
 * JD-Core Version:    0.6.0
 */