package org.codehaus.jackson.map.jsontype.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;

public class StdSubtypeResolver extends SubtypeResolver
{
  protected LinkedHashSet<NamedType> _registeredSubtypes;

  protected void _collectAndResolve(AnnotatedClass paramAnnotatedClass, NamedType paramNamedType, MapperConfig<?> paramMapperConfig, AnnotationIntrospector paramAnnotationIntrospector, HashMap<NamedType, NamedType> paramHashMap)
  {
    if (!paramNamedType.hasName())
    {
      String str = paramAnnotationIntrospector.findTypeName(paramAnnotatedClass);
      if (str != null)
        paramNamedType = new NamedType(paramNamedType.getType(), str);
    }
    if (paramHashMap.containsKey(paramNamedType))
      if ((paramNamedType.hasName()) && (!((NamedType)paramHashMap.get(paramNamedType)).hasName()))
        paramHashMap.put(paramNamedType, paramNamedType);
    while (true)
    {
      return;
      paramHashMap.put(paramNamedType, paramNamedType);
      List localList = paramAnnotationIntrospector.findSubtypes(paramAnnotatedClass);
      if ((localList == null) || (localList.isEmpty()))
        continue;
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        NamedType localNamedType = (NamedType)localIterator.next();
        AnnotatedClass localAnnotatedClass = AnnotatedClass.constructWithoutSuperTypes(localNamedType.getType(), paramAnnotationIntrospector, paramMapperConfig);
        if (!localNamedType.hasName())
          localNamedType = new NamedType(localNamedType.getType(), paramAnnotationIntrospector.findTypeName(localAnnotatedClass));
        _collectAndResolve(localAnnotatedClass, localNamedType, paramMapperConfig, paramAnnotationIntrospector, paramHashMap);
      }
    }
  }

  public Collection<NamedType> collectAndResolveSubtypes(AnnotatedClass paramAnnotatedClass, MapperConfig<?> paramMapperConfig, AnnotationIntrospector paramAnnotationIntrospector)
  {
    HashMap localHashMap = new HashMap();
    if (this._registeredSubtypes != null)
    {
      Class localClass = paramAnnotatedClass.getRawType();
      Iterator localIterator = this._registeredSubtypes.iterator();
      while (localIterator.hasNext())
      {
        NamedType localNamedType = (NamedType)localIterator.next();
        if (!localClass.isAssignableFrom(localNamedType.getType()))
          continue;
        _collectAndResolve(AnnotatedClass.constructWithoutSuperTypes(localNamedType.getType(), paramAnnotationIntrospector, paramMapperConfig), localNamedType, paramMapperConfig, paramAnnotationIntrospector, localHashMap);
      }
    }
    _collectAndResolve(paramAnnotatedClass, new NamedType(paramAnnotatedClass.getRawType(), null), paramMapperConfig, paramAnnotationIntrospector, localHashMap);
    return new ArrayList(localHashMap.values());
  }

  public Collection<NamedType> collectAndResolveSubtypes(AnnotatedMember paramAnnotatedMember, MapperConfig<?> paramMapperConfig, AnnotationIntrospector paramAnnotationIntrospector)
  {
    HashMap localHashMap = new HashMap();
    if (this._registeredSubtypes != null)
    {
      Class localClass = paramAnnotatedMember.getRawType();
      Iterator localIterator2 = this._registeredSubtypes.iterator();
      while (localIterator2.hasNext())
      {
        NamedType localNamedType3 = (NamedType)localIterator2.next();
        if (!localClass.isAssignableFrom(localNamedType3.getType()))
          continue;
        _collectAndResolve(AnnotatedClass.constructWithoutSuperTypes(localNamedType3.getType(), paramAnnotationIntrospector, paramMapperConfig), localNamedType3, paramMapperConfig, paramAnnotationIntrospector, localHashMap);
      }
    }
    List localList = paramAnnotationIntrospector.findSubtypes(paramAnnotatedMember);
    if (localList != null)
    {
      Iterator localIterator1 = localList.iterator();
      while (localIterator1.hasNext())
      {
        NamedType localNamedType2 = (NamedType)localIterator1.next();
        _collectAndResolve(AnnotatedClass.constructWithoutSuperTypes(localNamedType2.getType(), paramAnnotationIntrospector, paramMapperConfig), localNamedType2, paramMapperConfig, paramAnnotationIntrospector, localHashMap);
      }
    }
    NamedType localNamedType1 = new NamedType(paramAnnotatedMember.getRawType(), null);
    _collectAndResolve(AnnotatedClass.constructWithoutSuperTypes(paramAnnotatedMember.getRawType(), paramAnnotationIntrospector, paramMapperConfig), localNamedType1, paramMapperConfig, paramAnnotationIntrospector, localHashMap);
    return new ArrayList(localHashMap.values());
  }

  public void registerSubtypes(Class<?>[] paramArrayOfClass)
  {
    NamedType[] arrayOfNamedType = new NamedType[paramArrayOfClass.length];
    int i = 0;
    int j = paramArrayOfClass.length;
    while (i < j)
    {
      arrayOfNamedType[i] = new NamedType(paramArrayOfClass[i]);
      i++;
    }
    registerSubtypes(arrayOfNamedType);
  }

  public void registerSubtypes(NamedType[] paramArrayOfNamedType)
  {
    if (this._registeredSubtypes == null)
      this._registeredSubtypes = new LinkedHashSet();
    int i = paramArrayOfNamedType.length;
    for (int j = 0; j < i; j++)
    {
      NamedType localNamedType = paramArrayOfNamedType[j];
      this._registeredSubtypes.add(localNamedType);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.jsontype.impl.StdSubtypeResolver
 * JD-Core Version:    0.6.0
 */