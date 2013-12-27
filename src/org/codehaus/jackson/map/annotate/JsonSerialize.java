package org.codehaus.jackson.map.annotate;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.codehaus.jackson.annotate.JacksonAnnotation;
import org.codehaus.jackson.map.JsonSerializer;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.PARAMETER})
@JacksonAnnotation
public @interface JsonSerialize
{
  public abstract Class<?> as();

  public abstract Class<?> contentAs();

  public abstract Class<? extends JsonSerializer<?>> contentUsing();

  public abstract Inclusion include();

  public abstract Class<?> keyAs();

  public abstract Class<? extends JsonSerializer<?>> keyUsing();

  public abstract Typing typing();

  public abstract Class<? extends JsonSerializer<?>> using();

  public static enum Inclusion
  {
    static
    {
      NON_DEFAULT = new Inclusion("NON_DEFAULT", 2);
      Inclusion[] arrayOfInclusion = new Inclusion[3];
      arrayOfInclusion[0] = ALWAYS;
      arrayOfInclusion[1] = NON_NULL;
      arrayOfInclusion[2] = NON_DEFAULT;
      $VALUES = arrayOfInclusion;
    }
  }

  public static enum Typing
  {
    static
    {
      Typing[] arrayOfTyping = new Typing[2];
      arrayOfTyping[0] = DYNAMIC;
      arrayOfTyping[1] = STATIC;
      $VALUES = arrayOfTyping;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.annotate.JsonSerialize
 * JD-Core Version:    0.6.0
 */