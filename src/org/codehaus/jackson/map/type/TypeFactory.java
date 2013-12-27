package org.codehaus.jackson.map.type;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.util.ArrayBuilders;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

public final class TypeFactory
{
  private static final JavaType[] NO_TYPES;

  @Deprecated
  public static final TypeFactory instance = new TypeFactory();
  protected final TypeModifier[] _modifiers;
  protected final TypeParser _parser;

  static
  {
    NO_TYPES = new JavaType[0];
  }

  private TypeFactory()
  {
    this._parser = new TypeParser(this);
    this._modifiers = null;
  }

  protected TypeFactory(TypeParser paramTypeParser, TypeModifier[] paramArrayOfTypeModifier)
  {
    this._parser = paramTypeParser;
    this._modifiers = paramArrayOfTypeModifier;
  }

  private JavaType _collectionType(Class<?> paramClass)
  {
    JavaType[] arrayOfJavaType = findTypeParameters(paramClass, Collection.class);
    if (arrayOfJavaType == null)
      return CollectionType.construct(paramClass, _unknownType());
    if (arrayOfJavaType.length != 1)
      throw new IllegalArgumentException("Strange Collection type " + paramClass.getName() + ": can not determine type parameters");
    return CollectionType.construct(paramClass, arrayOfJavaType[0]);
  }

  protected static HierarchicType _findSuperClassChain(Type paramType, Class<?> paramClass)
  {
    HierarchicType localHierarchicType1 = new HierarchicType(paramType);
    Class localClass = localHierarchicType1.getRawClass();
    if (localClass == paramClass)
      return localHierarchicType1;
    Type localType = localClass.getGenericSuperclass();
    if (localType != null)
    {
      HierarchicType localHierarchicType2 = _findSuperClassChain(localType, paramClass);
      if (localHierarchicType2 != null)
      {
        localHierarchicType2.setSubType(localHierarchicType1);
        localHierarchicType1.setSuperType(localHierarchicType2);
        return localHierarchicType1;
      }
    }
    return null;
  }

  protected static HierarchicType _findSuperInterfaceChain(Type paramType, Class<?> paramClass)
  {
    HierarchicType localHierarchicType1 = new HierarchicType(paramType);
    Class localClass = localHierarchicType1.getRawClass();
    if (localClass == paramClass)
      return localHierarchicType1;
    Type[] arrayOfType = localClass.getGenericInterfaces();
    if (arrayOfType != null)
    {
      int i = arrayOfType.length;
      for (int j = 0; j < i; j++)
      {
        HierarchicType localHierarchicType3 = _findSuperInterfaceChain(arrayOfType[j], paramClass);
        if (localHierarchicType3 == null)
          continue;
        localHierarchicType3.setSubType(localHierarchicType1);
        localHierarchicType1.setSuperType(localHierarchicType3);
        return localHierarchicType1;
      }
    }
    Type localType = localClass.getGenericSuperclass();
    if (localType != null)
    {
      HierarchicType localHierarchicType2 = _findSuperInterfaceChain(localType, paramClass);
      if (localHierarchicType2 != null)
      {
        localHierarchicType2.setSubType(localHierarchicType1);
        localHierarchicType1.setSuperType(localHierarchicType2);
        return localHierarchicType1;
      }
    }
    return null;
  }

  protected static HierarchicType _findSuperTypeChain(Class<?> paramClass1, Class<?> paramClass2)
  {
    if (paramClass2.isInterface())
      return _findSuperInterfaceChain(paramClass1, paramClass2);
    return _findSuperClassChain(paramClass1, paramClass2);
  }

  private JavaType _mapType(Class<?> paramClass)
  {
    JavaType[] arrayOfJavaType = findTypeParameters(paramClass, Map.class);
    if (arrayOfJavaType == null)
      return MapType.construct(paramClass, _unknownType(), _unknownType());
    if (arrayOfJavaType.length != 2)
      throw new IllegalArgumentException("Strange Map type " + paramClass.getName() + ": can not determine type parameters");
    return MapType.construct(paramClass, arrayOfJavaType[0], arrayOfJavaType[1]);
  }

  @Deprecated
  public static JavaType arrayType(Class<?> paramClass)
  {
    return instance.constructArrayType(instance.constructType(paramClass));
  }

  @Deprecated
  public static JavaType arrayType(JavaType paramJavaType)
  {
    return instance.constructArrayType(paramJavaType);
  }

  @Deprecated
  public static JavaType collectionType(Class<? extends Collection> paramClass, Class<?> paramClass1)
  {
    return instance.constructCollectionType(paramClass, instance.constructType(paramClass1));
  }

  @Deprecated
  public static JavaType collectionType(Class<? extends Collection> paramClass, JavaType paramJavaType)
  {
    return instance.constructCollectionType(paramClass, paramJavaType);
  }

  public static TypeFactory defaultInstance()
  {
    return instance;
  }

  @Deprecated
  public static JavaType fastSimpleType(Class<?> paramClass)
  {
    return instance.uncheckedSimpleType(paramClass);
  }

  @Deprecated
  public static JavaType[] findParameterTypes(Class<?> paramClass1, Class<?> paramClass2)
  {
    return instance.findTypeParameters(paramClass1, paramClass2);
  }

  @Deprecated
  public static JavaType[] findParameterTypes(Class<?> paramClass1, Class<?> paramClass2, TypeBindings paramTypeBindings)
  {
    return instance.findTypeParameters(paramClass1, paramClass2, paramTypeBindings);
  }

  @Deprecated
  public static JavaType[] findParameterTypes(JavaType paramJavaType, Class<?> paramClass)
  {
    return instance.findTypeParameters(paramJavaType, paramClass);
  }

  public static JavaType fromCanonical(String paramString)
    throws IllegalArgumentException
  {
    return instance.constructFromCanonical(paramString);
  }

  @Deprecated
  public static JavaType fromClass(Class<?> paramClass)
  {
    return instance._fromClass(paramClass, null);
  }

  @Deprecated
  public static JavaType fromType(Type paramType)
  {
    return instance._constructType(paramType, null);
  }

  @Deprecated
  public static JavaType fromTypeReference(TypeReference<?> paramTypeReference)
  {
    return type(paramTypeReference.getType());
  }

  @Deprecated
  public static JavaType mapType(Class<? extends Map> paramClass, Class<?> paramClass1, Class<?> paramClass2)
  {
    return instance.constructMapType(paramClass, type(paramClass1), instance.constructType(paramClass2));
  }

  @Deprecated
  public static JavaType mapType(Class<? extends Map> paramClass, JavaType paramJavaType1, JavaType paramJavaType2)
  {
    return instance.constructMapType(paramClass, paramJavaType1, paramJavaType2);
  }

  @Deprecated
  public static JavaType parametricType(Class<?> paramClass, Class<?>[] paramArrayOfClass)
  {
    return instance.constructParametricType(paramClass, paramArrayOfClass);
  }

  @Deprecated
  public static JavaType parametricType(Class<?> paramClass, JavaType[] paramArrayOfJavaType)
  {
    return instance.constructParametricType(paramClass, paramArrayOfJavaType);
  }

  public static Class<?> rawClass(Type paramType)
  {
    if ((paramType instanceof Class))
      return (Class)paramType;
    return defaultInstance().constructType(paramType).getRawClass();
  }

  @Deprecated
  public static JavaType specialize(JavaType paramJavaType, Class<?> paramClass)
  {
    return instance.constructSpecializedType(paramJavaType, paramClass);
  }

  @Deprecated
  public static JavaType type(Type paramType)
  {
    return instance._constructType(paramType, null);
  }

  @Deprecated
  public static JavaType type(Type paramType, Class<?> paramClass)
  {
    return instance.constructType(paramType, paramClass);
  }

  @Deprecated
  public static JavaType type(Type paramType, TypeBindings paramTypeBindings)
  {
    return instance._constructType(paramType, paramTypeBindings);
  }

  @Deprecated
  public static JavaType type(Type paramType, JavaType paramJavaType)
  {
    return instance.constructType(paramType, paramJavaType);
  }

  @Deprecated
  public static JavaType type(TypeReference<?> paramTypeReference)
  {
    return instance.constructType(paramTypeReference.getType());
  }

  public static JavaType unknownType()
  {
    return defaultInstance()._unknownType();
  }

  public JavaType _constructType(Type paramType, TypeBindings paramTypeBindings)
  {
    JavaType localJavaType;
    if ((paramType instanceof Class))
    {
      Class localClass = (Class)paramType;
      if (paramTypeBindings == null)
        paramTypeBindings = new TypeBindings(this, localClass);
      localJavaType = _fromClass(localClass, paramTypeBindings);
    }
    while ((this._modifiers != null) && (!localJavaType.isContainerType()))
    {
      TypeModifier[] arrayOfTypeModifier = this._modifiers;
      int i = arrayOfTypeModifier.length;
      int j = 0;
      while (true)
        if (j < i)
        {
          localJavaType = arrayOfTypeModifier[j].modifyType(localJavaType, paramType, paramTypeBindings, this);
          j++;
          continue;
          if ((paramType instanceof ParameterizedType))
          {
            localJavaType = _fromParamType((ParameterizedType)paramType, paramTypeBindings);
            break;
          }
          if ((paramType instanceof GenericArrayType))
          {
            localJavaType = _fromArrayType((GenericArrayType)paramType, paramTypeBindings);
            break;
          }
          if ((paramType instanceof TypeVariable))
          {
            localJavaType = _fromVariable((TypeVariable)paramType, paramTypeBindings);
            break;
          }
          if ((paramType instanceof WildcardType))
          {
            localJavaType = _fromWildcard((WildcardType)paramType, paramTypeBindings);
            break;
          }
          throw new IllegalArgumentException("Unrecognized Type: " + paramType.toString());
        }
    }
    return localJavaType;
  }

  protected JavaType _fromArrayType(GenericArrayType paramGenericArrayType, TypeBindings paramTypeBindings)
  {
    return ArrayType.construct(_constructType(paramGenericArrayType.getGenericComponentType(), paramTypeBindings));
  }

  protected JavaType _fromClass(Class<?> paramClass, TypeBindings paramTypeBindings)
  {
    if (paramClass.isArray())
      return ArrayType.construct(_constructType(paramClass.getComponentType(), null));
    if (paramClass.isEnum())
      return new SimpleType(paramClass);
    if (Map.class.isAssignableFrom(paramClass))
      return _mapType(paramClass);
    if (Collection.class.isAssignableFrom(paramClass))
      return _collectionType(paramClass);
    return new SimpleType(paramClass);
  }

  protected JavaType _fromParamType(ParameterizedType paramParameterizedType, TypeBindings paramTypeBindings)
  {
    Class localClass = (Class)paramParameterizedType.getRawType();
    Type[] arrayOfType = paramParameterizedType.getActualTypeArguments();
    int i;
    JavaType[] arrayOfJavaType1;
    if (arrayOfType == null)
    {
      i = 0;
      if (i != 0)
        break label124;
      arrayOfJavaType1 = NO_TYPES;
    }
    JavaType[] arrayOfJavaType3;
    while (true)
    {
      if (!Map.class.isAssignableFrom(localClass))
        break label175;
      arrayOfJavaType3 = findTypeParameters(constructSimpleType(localClass, arrayOfJavaType1), Map.class);
      if (arrayOfJavaType3.length == 2)
        break label162;
      throw new IllegalArgumentException("Could not find 2 type parameters for Map class " + localClass.getName() + " (found " + arrayOfJavaType3.length + ")");
      i = arrayOfType.length;
      break;
      label124: arrayOfJavaType1 = new JavaType[i];
      for (int j = 0; j < i; j++)
        arrayOfJavaType1[j] = _constructType(arrayOfType[j], paramTypeBindings);
    }
    label162: return MapType.construct(localClass, arrayOfJavaType3[0], arrayOfJavaType3[1]);
    label175: if (Collection.class.isAssignableFrom(localClass))
    {
      JavaType[] arrayOfJavaType2 = findTypeParameters(constructSimpleType(localClass, arrayOfJavaType1), Collection.class);
      if (arrayOfJavaType2.length != 1)
        throw new IllegalArgumentException("Could not find 1 type parameter for Collection class " + localClass.getName() + " (found " + arrayOfJavaType2.length + ")");
      return CollectionType.construct(localClass, arrayOfJavaType2[0]);
    }
    if (i == 0)
      return new SimpleType(localClass);
    return constructSimpleType(localClass, arrayOfJavaType1);
  }

  protected JavaType _fromParameterizedClass(Class<?> paramClass, List<JavaType> paramList)
  {
    if (paramClass.isArray())
      return ArrayType.construct(_constructType(paramClass.getComponentType(), null));
    if (paramClass.isEnum())
      return new SimpleType(paramClass);
    if (Map.class.isAssignableFrom(paramClass))
    {
      if (paramList.size() > 0)
      {
        JavaType localJavaType1 = (JavaType)paramList.get(0);
        if (paramList.size() >= 2);
        for (JavaType localJavaType2 = (JavaType)paramList.get(1); ; localJavaType2 = _unknownType())
          return MapType.construct(paramClass, localJavaType1, localJavaType2);
      }
      return _mapType(paramClass);
    }
    if (Collection.class.isAssignableFrom(paramClass))
    {
      if (paramList.size() >= 1)
        return CollectionType.construct(paramClass, (JavaType)paramList.get(0));
      return _collectionType(paramClass);
    }
    if (paramList.size() == 0)
      return new SimpleType(paramClass);
    return constructSimpleType(paramClass, (JavaType[])paramList.toArray(new JavaType[paramList.size()]));
  }

  protected JavaType _fromVariable(TypeVariable<?> paramTypeVariable, TypeBindings paramTypeBindings)
  {
    JavaType localJavaType;
    if (paramTypeBindings == null)
      localJavaType = _unknownType();
    String str;
    do
    {
      return localJavaType;
      str = paramTypeVariable.getName();
      localJavaType = paramTypeBindings.findType(str);
    }
    while (localJavaType != null);
    Type[] arrayOfType = paramTypeVariable.getBounds();
    paramTypeBindings._addPlaceholder(str);
    return _constructType(arrayOfType[0], paramTypeBindings);
  }

  protected JavaType _fromWildcard(WildcardType paramWildcardType, TypeBindings paramTypeBindings)
  {
    return _constructType(paramWildcardType.getUpperBounds()[0], paramTypeBindings);
  }

  protected JavaType _resolveVariableViaSubTypes(HierarchicType paramHierarchicType, String paramString, TypeBindings paramTypeBindings)
  {
    if ((paramHierarchicType != null) && (paramHierarchicType.isGeneric()))
    {
      TypeVariable[] arrayOfTypeVariable = paramHierarchicType.getRawClass().getTypeParameters();
      int i = 0;
      int j = arrayOfTypeVariable.length;
      while (i < j)
      {
        if (paramString.equals(arrayOfTypeVariable[i].getName()))
        {
          Type localType = paramHierarchicType.asGeneric().getActualTypeArguments()[i];
          if ((localType instanceof TypeVariable))
            return _resolveVariableViaSubTypes(paramHierarchicType.getSubType(), ((TypeVariable)localType).getName(), paramTypeBindings);
          return _constructType(localType, paramTypeBindings);
        }
        i++;
      }
    }
    return _unknownType();
  }

  protected JavaType _unknownType()
  {
    return new SimpleType(Object.class, null, null);
  }

  protected ArrayType constructArrayType(Class<?> paramClass)
  {
    return ArrayType.construct(_constructType(paramClass, null));
  }

  protected ArrayType constructArrayType(JavaType paramJavaType)
  {
    return ArrayType.construct(paramJavaType);
  }

  public CollectionLikeType constructCollectionLikeType(Class<?> paramClass1, Class<?> paramClass2)
  {
    return CollectionLikeType.construct(paramClass1, constructType(paramClass2));
  }

  public CollectionLikeType constructCollectionLikeType(Class<?> paramClass, JavaType paramJavaType)
  {
    return CollectionLikeType.construct(paramClass, paramJavaType);
  }

  public CollectionType constructCollectionType(Class<? extends Collection> paramClass, Class<?> paramClass1)
  {
    return CollectionType.construct(paramClass, constructType(paramClass1));
  }

  public CollectionType constructCollectionType(Class<? extends Collection> paramClass, JavaType paramJavaType)
  {
    return CollectionType.construct(paramClass, paramJavaType);
  }

  public JavaType constructFromCanonical(String paramString)
    throws IllegalArgumentException
  {
    return this._parser.parse(paramString);
  }

  public MapLikeType constructMapLikeType(Class<?> paramClass1, Class<?> paramClass2, Class<?> paramClass3)
  {
    return MapType.construct(paramClass1, constructType(paramClass2), constructType(paramClass3));
  }

  public MapLikeType constructMapLikeType(Class<?> paramClass, JavaType paramJavaType1, JavaType paramJavaType2)
  {
    return MapLikeType.construct(paramClass, paramJavaType1, paramJavaType2);
  }

  public MapType constructMapType(Class<? extends Map> paramClass, Class<?> paramClass1, Class<?> paramClass2)
  {
    return MapType.construct(paramClass, constructType(paramClass1), constructType(paramClass2));
  }

  public MapType constructMapType(Class<? extends Map> paramClass, JavaType paramJavaType1, JavaType paramJavaType2)
  {
    return MapType.construct(paramClass, paramJavaType1, paramJavaType2);
  }

  public JavaType constructParametricType(Class<?> paramClass, Class<?>[] paramArrayOfClass)
  {
    int i = paramArrayOfClass.length;
    JavaType[] arrayOfJavaType = new JavaType[i];
    for (int j = 0; j < i; j++)
      arrayOfJavaType[j] = _fromClass(paramArrayOfClass[j], null);
    return constructParametricType(paramClass, arrayOfJavaType);
  }

  public JavaType constructParametricType(Class<?> paramClass, JavaType[] paramArrayOfJavaType)
  {
    if (paramClass.isArray())
    {
      if (paramArrayOfJavaType.length != 1)
        throw new IllegalArgumentException("Need exactly 1 parameter type for arrays (" + paramClass.getName() + ")");
      return constructArrayType(paramArrayOfJavaType[0]);
    }
    if (Map.class.isAssignableFrom(paramClass))
    {
      if (paramArrayOfJavaType.length != 2)
        throw new IllegalArgumentException("Need exactly 2 parameter types for Map types (" + paramClass.getName() + ")");
      return constructMapType(paramClass, paramArrayOfJavaType[0], paramArrayOfJavaType[1]);
    }
    if (Collection.class.isAssignableFrom(paramClass))
    {
      if (paramArrayOfJavaType.length != 1)
        throw new IllegalArgumentException("Need exactly 1 parameter type for Collection types (" + paramClass.getName() + ")");
      return constructCollectionType(paramClass, paramArrayOfJavaType[0]);
    }
    return constructSimpleType(paramClass, paramArrayOfJavaType);
  }

  public JavaType constructSimpleType(Class<?> paramClass, JavaType[] paramArrayOfJavaType)
  {
    TypeVariable[] arrayOfTypeVariable = paramClass.getTypeParameters();
    if (arrayOfTypeVariable.length != paramArrayOfJavaType.length)
      throw new IllegalArgumentException("Parameter type mismatch for " + paramClass.getName() + ": expected " + arrayOfTypeVariable.length + " parameters, was given " + paramArrayOfJavaType.length);
    String[] arrayOfString = new String[arrayOfTypeVariable.length];
    int i = 0;
    int j = arrayOfTypeVariable.length;
    while (i < j)
    {
      arrayOfString[i] = arrayOfTypeVariable[i].getName();
      i++;
    }
    return new SimpleType(paramClass, arrayOfString, paramArrayOfJavaType);
  }

  public JavaType constructSpecializedType(JavaType paramJavaType, Class<?> paramClass)
  {
    if (((paramJavaType instanceof SimpleType)) && ((paramClass.isArray()) || (Map.class.isAssignableFrom(paramClass)) || (Collection.class.isAssignableFrom(paramClass))))
    {
      if (!paramJavaType.getRawClass().isAssignableFrom(paramClass))
        throw new IllegalArgumentException("Class " + paramClass.getClass().getName() + " not subtype of " + paramJavaType);
      JavaType localJavaType = instance._fromClass(paramClass, new TypeBindings(this, paramJavaType.getRawClass()));
      Object localObject1 = paramJavaType.getValueHandler();
      if (localObject1 != null)
        localJavaType.setValueHandler(localObject1);
      Object localObject2 = paramJavaType.getTypeHandler();
      if (localObject2 != null)
        localJavaType = localJavaType.withTypeHandler(localObject2);
      return localJavaType;
    }
    return paramJavaType.narrowBy(paramClass);
  }

  public JavaType constructType(Type paramType)
  {
    return _constructType(paramType, null);
  }

  public JavaType constructType(Type paramType, Class<?> paramClass)
  {
    return _constructType(paramType, new TypeBindings(this, paramClass));
  }

  public JavaType constructType(Type paramType, TypeBindings paramTypeBindings)
  {
    return _constructType(paramType, paramTypeBindings);
  }

  public JavaType constructType(Type paramType, JavaType paramJavaType)
  {
    return _constructType(paramType, new TypeBindings(this, paramJavaType));
  }

  public JavaType constructType(TypeReference<?> paramTypeReference)
  {
    return _constructType(paramTypeReference.getType(), null);
  }

  public JavaType[] findTypeParameters(Class<?> paramClass1, Class<?> paramClass2)
  {
    return findTypeParameters(paramClass1, paramClass2, new TypeBindings(this, paramClass1));
  }

  public JavaType[] findTypeParameters(Class<?> paramClass1, Class<?> paramClass2, TypeBindings paramTypeBindings)
  {
    HierarchicType localHierarchicType1 = _findSuperTypeChain(paramClass1, paramClass2);
    if (localHierarchicType1 == null)
      throw new IllegalArgumentException("Class " + paramClass1.getName() + " is not a subtype of " + paramClass2.getName());
    HierarchicType localHierarchicType2 = localHierarchicType1;
    while (localHierarchicType2.getSuperType() != null)
    {
      localHierarchicType2 = localHierarchicType2.getSuperType();
      Class localClass = localHierarchicType2.getRawClass();
      TypeBindings localTypeBindings = new TypeBindings(this, localClass);
      if (localHierarchicType2.isGeneric())
      {
        Type[] arrayOfType = localHierarchicType2.asGeneric().getActualTypeArguments();
        TypeVariable[] arrayOfTypeVariable = localClass.getTypeParameters();
        int i = arrayOfType.length;
        for (int j = 0; j < i; j++)
          localTypeBindings.addBinding(arrayOfTypeVariable[j].getName(), instance._constructType(arrayOfType[j], paramTypeBindings));
      }
      paramTypeBindings = localTypeBindings;
    }
    if (!localHierarchicType2.isGeneric())
      return null;
    return paramTypeBindings.typesAsArray();
  }

  public JavaType[] findTypeParameters(JavaType paramJavaType, Class<?> paramClass)
  {
    Class localClass = paramJavaType.getRawClass();
    if (localClass == paramClass)
    {
      int i = paramJavaType.containedTypeCount();
      JavaType[] arrayOfJavaType;
      if (i == 0)
        arrayOfJavaType = null;
      while (true)
      {
        return arrayOfJavaType;
        arrayOfJavaType = new JavaType[i];
        for (int j = 0; j < i; j++)
          arrayOfJavaType[j] = paramJavaType.containedType(j);
      }
    }
    return findTypeParameters(localClass, paramClass, new TypeBindings(this, paramJavaType));
  }

  public JavaType uncheckedSimpleType(Class<?> paramClass)
  {
    return new SimpleType(paramClass, null, null);
  }

  public TypeFactory withModifier(TypeModifier paramTypeModifier)
  {
    if (this._modifiers == null)
      return new TypeFactory(this._parser, new TypeModifier[] { paramTypeModifier });
    return new TypeFactory(this._parser, (TypeModifier[])ArrayBuilders.insertInListNoDup(this._modifiers, paramTypeModifier));
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.type.TypeFactory
 * JD-Core Version:    0.6.0
 */