package org.codehaus.jackson.map.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ClassUtil
{
  private static void _addSuperTypes(Class<?> paramClass1, Class<?> paramClass2, Collection<Class<?>> paramCollection, boolean paramBoolean)
  {
    if ((paramClass1 == paramClass2) || (paramClass1 == null) || (paramClass1 == Object.class));
    while (true)
    {
      return;
      if (!paramBoolean)
        break;
      if (paramCollection.contains(paramClass1))
        continue;
      paramCollection.add(paramClass1);
    }
    Class[] arrayOfClass = paramClass1.getInterfaces();
    int i = arrayOfClass.length;
    for (int j = 0; j < i; j++)
      _addSuperTypes(arrayOfClass[j], paramClass2, paramCollection, true);
    _addSuperTypes(paramClass1.getSuperclass(), paramClass2, paramCollection, true);
  }

  public static String canBeABeanType(Class<?> paramClass)
  {
    if (paramClass.isAnnotation())
      return "annotation";
    if (paramClass.isArray())
      return "array";
    if (paramClass.isEnum())
      return "enum";
    if (paramClass.isPrimitive())
      return "primitive";
    return null;
  }

  public static void checkAndFixAccess(Member paramMember)
  {
    AccessibleObject localAccessibleObject = (AccessibleObject)paramMember;
    Class localClass;
    try
    {
      localAccessibleObject.setAccessible(true);
      return;
    }
    catch (SecurityException localSecurityException)
    {
      while (localAccessibleObject.isAccessible());
      localClass = paramMember.getDeclaringClass();
    }
    throw new IllegalArgumentException("Can not access " + paramMember + " (from class " + localClass.getName() + "; failed to set access: " + localSecurityException.getMessage());
  }

  public static <T> T createInstance(Class<T> paramClass, boolean paramBoolean)
    throws IllegalArgumentException
  {
    Constructor localConstructor = findConstructor(paramClass, paramBoolean);
    if (localConstructor == null)
      throw new IllegalArgumentException("Class " + paramClass.getName() + " has no default (no arg) constructor");
    try
    {
      Object localObject = localConstructor.newInstance(new Object[0]);
      return localObject;
    }
    catch (Exception localException)
    {
      unwrapAndThrowAsIAE(localException, "Failed to instantiate class " + paramClass.getName() + ", problem: " + localException.getMessage());
    }
    return null;
  }

  public static Object defaultValue(Class<?> paramClass)
  {
    if (paramClass == Integer.TYPE)
      return Integer.valueOf(0);
    if (paramClass == Long.TYPE)
      return Long.valueOf(0L);
    if (paramClass == Boolean.TYPE)
      return Boolean.FALSE;
    if (paramClass == Double.TYPE)
      return Double.valueOf(0.0D);
    if (paramClass == Float.TYPE)
      return Float.valueOf(0.0F);
    if (paramClass == Byte.TYPE)
      return Byte.valueOf(0);
    if (paramClass == Short.TYPE)
      return Short.valueOf(0);
    if (paramClass == Character.TYPE)
      return Character.valueOf('\000');
    throw new IllegalArgumentException("Class " + paramClass.getName() + " is not a primitive type");
  }

  public static <T> Constructor<T> findConstructor(Class<T> paramClass, boolean paramBoolean)
    throws IllegalArgumentException
  {
    Constructor localConstructor;
    try
    {
      localConstructor = paramClass.getDeclaredConstructor(new Class[0]);
      if (paramBoolean)
      {
        checkAndFixAccess(localConstructor);
        return localConstructor;
      }
      if (!Modifier.isPublic(localConstructor.getModifiers()))
        throw new IllegalArgumentException("Default constructor for " + paramClass.getName() + " is not accessible (non-public?): not allowed to try modify access via Reflection: can not instantiate type");
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      return null;
    }
    catch (Exception localException)
    {
      while (true)
        unwrapAndThrowAsIAE(localException, "Failed to find default constructor of class " + paramClass.getName() + ", problem: " + localException.getMessage());
    }
    return localConstructor;
  }

  public static Class<? extends Enum<?>> findEnumType(Class<?> paramClass)
  {
    if (paramClass.getSuperclass() != Enum.class)
      paramClass = paramClass.getSuperclass();
    return paramClass;
  }

  public static Class<? extends Enum<?>> findEnumType(Enum<?> paramEnum)
  {
    Class localClass = paramEnum.getClass();
    if (localClass.getSuperclass() != Enum.class)
      localClass = localClass.getSuperclass();
    return localClass;
  }

  public static Class<? extends Enum<?>> findEnumType(EnumMap<?, ?> paramEnumMap)
  {
    if (!paramEnumMap.isEmpty())
      return findEnumType((Enum)paramEnumMap.keySet().iterator().next());
    return EnumTypeLocator.instance.enumTypeFor(paramEnumMap);
  }

  public static Class<? extends Enum<?>> findEnumType(EnumSet<?> paramEnumSet)
  {
    if (!paramEnumSet.isEmpty())
      return findEnumType((Enum)paramEnumSet.iterator().next());
    return EnumTypeLocator.instance.enumTypeFor(paramEnumSet);
  }

  public static List<Class<?>> findSuperTypes(Class<?> paramClass1, Class<?> paramClass2)
  {
    return findSuperTypes(paramClass1, paramClass2, new ArrayList());
  }

  public static List<Class<?>> findSuperTypes(Class<?> paramClass1, Class<?> paramClass2, List<Class<?>> paramList)
  {
    _addSuperTypes(paramClass1, paramClass2, paramList, false);
    return paramList;
  }

  public static String getClassDescription(Object paramObject)
  {
    if (paramObject == null)
      return "unknown";
    if ((paramObject instanceof Class));
    for (Class localClass = (Class)paramObject; ; localClass = paramObject.getClass())
      return localClass.getName();
  }

  public static Throwable getRootCause(Throwable paramThrowable)
  {
    while (paramThrowable.getCause() != null)
      paramThrowable = paramThrowable.getCause();
    return paramThrowable;
  }

  public static boolean hasGetterSignature(Method paramMethod)
  {
    if (Modifier.isStatic(paramMethod.getModifiers()));
    Class[] arrayOfClass;
    do
    {
      return false;
      arrayOfClass = paramMethod.getParameterTypes();
    }
    while (((arrayOfClass != null) && (arrayOfClass.length != 0)) || (Void.TYPE == paramMethod.getReturnType()));
    return true;
  }

  public static boolean isCollectionMapOrArray(Class<?> paramClass)
  {
    if (paramClass.isArray());
    do
      return true;
    while ((Collection.class.isAssignableFrom(paramClass)) || (Map.class.isAssignableFrom(paramClass)));
    return false;
  }

  public static boolean isConcrete(Class<?> paramClass)
  {
    return (0x600 & paramClass.getModifiers()) == 0;
  }

  public static boolean isConcrete(Member paramMember)
  {
    return (0x600 & paramMember.getModifiers()) == 0;
  }

  public static String isLocalType(Class<?> paramClass)
  {
    try
    {
      if (paramClass.getEnclosingMethod() != null)
        return "local/anonymous";
      if ((paramClass.getEnclosingClass() != null) && (!Modifier.isStatic(paramClass.getModifiers())))
        return "non-static member class";
    }
    catch (NullPointerException localNullPointerException)
    {
      return null;
    }
    catch (SecurityException localSecurityException)
    {
      label33: break label33;
    }
  }

  public static boolean isProxyType(Class<?> paramClass)
  {
    if (Proxy.isProxyClass(paramClass));
    String str;
    do
    {
      return true;
      str = paramClass.getName();
    }
    while ((str.startsWith("net.sf.cglib.proxy.")) || (str.startsWith("org.hibernate.proxy.")));
    return false;
  }

  public static void throwAsIAE(Throwable paramThrowable)
  {
    throwAsIAE(paramThrowable, paramThrowable.getMessage());
  }

  public static void throwAsIAE(Throwable paramThrowable, String paramString)
  {
    if ((paramThrowable instanceof RuntimeException))
      throw ((RuntimeException)paramThrowable);
    if ((paramThrowable instanceof Error))
      throw ((Error)paramThrowable);
    throw new IllegalArgumentException(paramString, paramThrowable);
  }

  public static void throwRootCause(Throwable paramThrowable)
    throws Exception
  {
    Throwable localThrowable = getRootCause(paramThrowable);
    if ((localThrowable instanceof Exception))
      throw ((Exception)localThrowable);
    throw ((Error)localThrowable);
  }

  public static void unwrapAndThrowAsIAE(Throwable paramThrowable)
  {
    throwAsIAE(getRootCause(paramThrowable));
  }

  public static void unwrapAndThrowAsIAE(Throwable paramThrowable, String paramString)
  {
    throwAsIAE(getRootCause(paramThrowable), paramString);
  }

  public static Class<?> wrapperType(Class<?> paramClass)
  {
    if (paramClass == Integer.TYPE)
      return Integer.class;
    if (paramClass == Long.TYPE)
      return Long.class;
    if (paramClass == Boolean.TYPE)
      return Boolean.class;
    if (paramClass == Double.TYPE)
      return Double.class;
    if (paramClass == Float.TYPE)
      return Float.class;
    if (paramClass == Byte.TYPE)
      return Byte.class;
    if (paramClass == Short.TYPE)
      return Short.class;
    if (paramClass == Character.TYPE)
      return Character.class;
    throw new IllegalArgumentException("Class " + paramClass.getName() + " is not a primitive type");
  }

  private static class EnumTypeLocator
  {
    static final EnumTypeLocator instance = new EnumTypeLocator();
    private final Field enumMapTypeField = locateField(EnumMap.class, "elementType", Class.class);
    private final Field enumSetTypeField = locateField(EnumSet.class, "elementType", Class.class);

    private Object get(Object paramObject, Field paramField)
    {
      try
      {
        Object localObject = paramField.get(paramObject);
        return localObject;
      }
      catch (Exception localException)
      {
      }
      throw new IllegalArgumentException(localException);
    }

    private static Field locateField(Class<?> paramClass1, String paramString, Class<?> paramClass2)
    {
      Field[] arrayOfField = paramClass1.getDeclaredFields();
      int i = arrayOfField.length;
      int j = 0;
      Object localObject = null;
      int k;
      if (j < i)
      {
        Field localField2 = arrayOfField[j];
        if ((paramString.equals(localField2.getName())) && (localField2.getType() == paramClass2))
          localObject = localField2;
      }
      else
      {
        if (localObject != null)
          break label110;
        k = arrayOfField.length;
      }
      for (int m = 0; ; m++)
      {
        if (m >= k)
          break label110;
        Field localField1 = arrayOfField[m];
        if (localField1.getType() != paramClass2)
          continue;
        if (localObject != null)
        {
          return null;
          j++;
          break;
        }
        localObject = localField1;
      }
      label110: if (localObject != null);
      try
      {
        localObject.setAccessible(true);
        label121: return localObject;
      }
      catch (Throwable localThrowable)
      {
        break label121;
      }
    }

    public Class<? extends Enum<?>> enumTypeFor(EnumMap<?, ?> paramEnumMap)
    {
      if (this.enumMapTypeField != null)
        return (Class)get(paramEnumMap, this.enumMapTypeField);
      throw new IllegalStateException("Can not figure out type for EnumMap (odd JDK platform?)");
    }

    public Class<? extends Enum<?>> enumTypeFor(EnumSet<?> paramEnumSet)
    {
      if (this.enumSetTypeField != null)
        return (Class)get(paramEnumSet, this.enumSetTypeField);
      throw new IllegalStateException("Can not figure out type for EnumSet (odd JDK platform?)");
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.util.ClassUtil
 * JD-Core Version:    0.6.0
 */