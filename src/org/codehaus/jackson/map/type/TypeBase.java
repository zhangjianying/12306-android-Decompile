package org.codehaus.jackson.map.type;

import org.codehaus.jackson.type.JavaType;

public abstract class TypeBase extends JavaType
{
  volatile String _canonicalName;

  protected TypeBase(Class<?> paramClass, int paramInt)
  {
    super(paramClass, paramInt);
  }

  protected static StringBuilder _classSignature(Class<?> paramClass, StringBuilder paramStringBuilder, boolean paramBoolean)
  {
    if (paramClass.isPrimitive())
      if (paramClass == Boolean.TYPE)
        paramStringBuilder.append('Z');
    do
    {
      return paramStringBuilder;
      if (paramClass == Byte.TYPE)
      {
        paramStringBuilder.append('B');
        return paramStringBuilder;
      }
      if (paramClass == Short.TYPE)
      {
        paramStringBuilder.append('S');
        return paramStringBuilder;
      }
      if (paramClass == Character.TYPE)
      {
        paramStringBuilder.append('C');
        return paramStringBuilder;
      }
      if (paramClass == Integer.TYPE)
      {
        paramStringBuilder.append('I');
        return paramStringBuilder;
      }
      if (paramClass == Long.TYPE)
      {
        paramStringBuilder.append('J');
        return paramStringBuilder;
      }
      if (paramClass == Float.TYPE)
      {
        paramStringBuilder.append('F');
        return paramStringBuilder;
      }
      if (paramClass == Double.TYPE)
      {
        paramStringBuilder.append('D');
        return paramStringBuilder;
      }
      if (paramClass == Void.TYPE)
      {
        paramStringBuilder.append('V');
        return paramStringBuilder;
      }
      throw new IllegalStateException("Unrecognized primitive type: " + paramClass.getName());
      paramStringBuilder.append('L');
      String str = paramClass.getName();
      int i = 0;
      int j = str.length();
      while (i < j)
      {
        char c = str.charAt(i);
        if (c == '.')
          c = '/';
        paramStringBuilder.append(c);
        i++;
      }
    }
    while (!paramBoolean);
    paramStringBuilder.append(';');
    return paramStringBuilder;
  }

  protected abstract String buildCanonicalName();

  protected final JavaType copyHandlers(JavaType paramJavaType)
  {
    this._valueHandler = paramJavaType.getValueHandler();
    this._typeHandler = paramJavaType.getTypeHandler();
    return this;
  }

  public abstract StringBuilder getErasedSignature(StringBuilder paramStringBuilder);

  public abstract StringBuilder getGenericSignature(StringBuilder paramStringBuilder);

  public String toCanonical()
  {
    String str = this._canonicalName;
    if (str == null)
      str = buildCanonicalName();
    return str;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.type.TypeBase
 * JD-Core Version:    0.6.0
 */