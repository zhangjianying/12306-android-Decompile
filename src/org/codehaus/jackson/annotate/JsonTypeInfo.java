package org.codehaus.jackson.annotate;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.PARAMETER})
@JacksonAnnotation
public @interface JsonTypeInfo
{
  public abstract As include();

  public abstract String property();

  public abstract Id use();

  public static enum As
  {
    static
    {
      WRAPPER_ARRAY = new As("WRAPPER_ARRAY", 2);
      As[] arrayOfAs = new As[3];
      arrayOfAs[0] = PROPERTY;
      arrayOfAs[1] = WRAPPER_OBJECT;
      arrayOfAs[2] = WRAPPER_ARRAY;
      $VALUES = arrayOfAs;
    }
  }

  public static enum Id
  {
    private final String _defaultPropertyName;

    static
    {
      CLASS = new Id("CLASS", 1, "@class");
      MINIMAL_CLASS = new Id("MINIMAL_CLASS", 2, "@c");
      NAME = new Id("NAME", 3, "@type");
      CUSTOM = new Id("CUSTOM", 4, null);
      Id[] arrayOfId = new Id[5];
      arrayOfId[0] = NONE;
      arrayOfId[1] = CLASS;
      arrayOfId[2] = MINIMAL_CLASS;
      arrayOfId[3] = NAME;
      arrayOfId[4] = CUSTOM;
      $VALUES = arrayOfId;
    }

    private Id(String paramString)
    {
      this._defaultPropertyName = paramString;
    }

    public String getDefaultPropertyName()
    {
      return this._defaultPropertyName;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.annotate.JsonTypeInfo
 * JD-Core Version:    0.6.0
 */