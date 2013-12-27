package org.codehaus.jackson.map;

public class RuntimeJsonMappingException extends RuntimeException
{
  public RuntimeJsonMappingException(String paramString)
  {
    super(paramString);
  }

  public RuntimeJsonMappingException(String paramString, JsonMappingException paramJsonMappingException)
  {
    super(paramString, paramJsonMappingException);
  }

  public RuntimeJsonMappingException(JsonMappingException paramJsonMappingException)
  {
    super(paramJsonMappingException);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.RuntimeJsonMappingException
 * JD-Core Version:    0.6.0
 */