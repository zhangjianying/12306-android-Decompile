package org.codehaus.jackson.map.util;

import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.type.JavaType;

public class RootNameLookup
{
  protected LRUMap<ClassKey, SerializedString> _rootNames;

  public SerializedString findRootName(Class<?> paramClass, MapperConfig<?> paramMapperConfig)
  {
    monitorenter;
    try
    {
      ClassKey localClassKey = new ClassKey(paramClass);
      SerializedString localSerializedString;
      if (this._rootNames == null)
      {
        this._rootNames = new LRUMap(20, 200);
        BasicBeanDescription localBasicBeanDescription = (BasicBeanDescription)paramMapperConfig.introspectClassAnnotations(paramClass);
        String str = paramMapperConfig.getAnnotationIntrospector().findRootName(localBasicBeanDescription.getClassInfo());
        if (str == null)
          str = paramClass.getSimpleName();
        localSerializedString = new SerializedString(str);
        this._rootNames.put(localClassKey, localSerializedString);
      }
      while (true)
      {
        return localSerializedString;
        localSerializedString = (SerializedString)this._rootNames.get(localClassKey);
        if (localSerializedString == null)
          break;
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public SerializedString findRootName(JavaType paramJavaType, MapperConfig<?> paramMapperConfig)
  {
    return findRootName(paramJavaType.getRawClass(), paramMapperConfig);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.util.RootNameLookup
 * JD-Core Version:    0.6.0
 */