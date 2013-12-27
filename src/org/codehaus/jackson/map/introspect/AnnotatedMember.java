package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Member;
import org.codehaus.jackson.map.util.ClassUtil;

public abstract class AnnotatedMember extends Annotated
{
  public final void fixAccess()
  {
    ClassUtil.checkAndFixAccess(getMember());
  }

  public abstract Class<?> getDeclaringClass();

  public abstract Member getMember();
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.AnnotatedMember
 * JD-Core Version:    0.6.0
 */