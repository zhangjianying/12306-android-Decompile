package org.codehaus.jackson.annotate;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
@JacksonAnnotation
public @interface JsonAutoDetect
{
  public abstract Visibility creatorVisibility();

  public abstract Visibility fieldVisibility();

  public abstract Visibility getterVisibility();

  public abstract Visibility isGetterVisibility();

  public abstract Visibility setterVisibility();

  public abstract JsonMethod[] value();

  public static enum Visibility
  {
    static
    {
      NONE = new Visibility("NONE", 4);
      DEFAULT = new Visibility("DEFAULT", 5);
      Visibility[] arrayOfVisibility = new Visibility[6];
      arrayOfVisibility[0] = ANY;
      arrayOfVisibility[1] = NON_PRIVATE;
      arrayOfVisibility[2] = PROTECTED_AND_PUBLIC;
      arrayOfVisibility[3] = PUBLIC_ONLY;
      arrayOfVisibility[4] = NONE;
      arrayOfVisibility[5] = DEFAULT;
      $VALUES = arrayOfVisibility;
    }

    public boolean isVisible(Member paramMember)
    {
      int i = 1;
      switch (JsonAutoDetect.1.$SwitchMap$org$codehaus$jackson$annotate$JsonAutoDetect$Visibility[ordinal()])
      {
      default:
        i = 0;
      case 1:
      case 2:
      case 3:
      case 4:
        do
        {
          do
          {
            return i;
            return false;
          }
          while (!Modifier.isPrivate(paramMember.getModifiers()));
          return false;
        }
        while (Modifier.isProtected(paramMember.getModifiers()));
      case 5:
      }
      return Modifier.isPublic(paramMember.getModifiers());
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.annotate.JsonAutoDetect
 * JD-Core Version:    0.6.0
 */