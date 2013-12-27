package org.codehaus.jackson.map.introspect;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.List<Ljava.lang.String;>;
import java.util.Map;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty;
import org.codehaus.jackson.map.BeanDescription;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.PropertyNamingStrategy;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.util.Annotations;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

public class BasicBeanDescription extends BeanDescription
{
  protected final AnnotationIntrospector _annotationIntrospector;
  protected TypeBindings _bindings;
  protected final AnnotatedClass _classInfo;
  protected final MapperConfig<?> _config;

  public BasicBeanDescription(MapperConfig<?> paramMapperConfig, JavaType paramJavaType, AnnotatedClass paramAnnotatedClass)
  {
    super(paramJavaType);
    this._config = paramMapperConfig;
    this._annotationIntrospector = paramMapperConfig.getAnnotationIntrospector();
    this._classInfo = paramAnnotatedClass;
  }

  public static String descFor(AnnotatedElement paramAnnotatedElement)
  {
    if ((paramAnnotatedElement instanceof Class))
      return "class " + ((Class)paramAnnotatedElement).getName();
    if ((paramAnnotatedElement instanceof Method))
    {
      Method localMethod = (Method)paramAnnotatedElement;
      return "method " + localMethod.getName() + " (from class " + localMethod.getDeclaringClass().getName() + ")";
    }
    if ((paramAnnotatedElement instanceof Constructor))
    {
      Constructor localConstructor = (Constructor)paramAnnotatedElement;
      return "constructor() (from class " + localConstructor.getDeclaringClass().getName() + ")";
    }
    return "unknown type [" + paramAnnotatedElement.getClass() + "]";
  }

  public static String manglePropertyName(String paramString)
  {
    int i = paramString.length();
    if (i == 0)
    {
      paramString = null;
      return paramString;
    }
    StringBuilder localStringBuilder = null;
    for (int j = 0; ; j++)
    {
      char c2;
      if (j < i)
      {
        char c1 = paramString.charAt(j);
        c2 = Character.toLowerCase(c1);
        if (c1 != c2);
      }
      else
      {
        if (localStringBuilder == null)
          break;
        return localStringBuilder.toString();
      }
      if (localStringBuilder == null)
        localStringBuilder = new StringBuilder(paramString);
      localStringBuilder.setCharAt(j, c2);
    }
  }

  public LinkedHashMap<String, AnnotatedField> _findPropertyFields(VisibilityChecker<?> paramVisibilityChecker, Collection<String> paramCollection, boolean paramBoolean)
  {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    PropertyNamingStrategy localPropertyNamingStrategy = this._config.getPropertyNamingStrategy();
    Iterator localIterator = this._classInfo.fields().iterator();
    label271: 
    while (localIterator.hasNext())
    {
      AnnotatedField localAnnotatedField1 = (AnnotatedField)localIterator.next();
      String str1;
      if (paramBoolean)
      {
        str1 = this._annotationIntrospector.findSerializablePropertyName(localAnnotatedField1);
        label69: if (str1 == null)
          break label232;
        if (str1.length() == 0)
        {
          str1 = localAnnotatedField1.getName();
          if (localPropertyNamingStrategy != null)
            str1 = localPropertyNamingStrategy.nameForField(this._config, localAnnotatedField1, str1);
        }
      }
      while (true)
      {
        if ((paramCollection != null) && (paramCollection.contains(str1)))
          break label271;
        AnnotatedField localAnnotatedField2 = (AnnotatedField)localLinkedHashMap.put(str1, localAnnotatedField1);
        if ((localAnnotatedField2 == null) || (localAnnotatedField2.getDeclaringClass() != localAnnotatedField1.getDeclaringClass()))
          break;
        String str2 = localAnnotatedField2.getFullName();
        String str3 = localAnnotatedField1.getFullName();
        throw new IllegalArgumentException("Multiple fields representing property \"" + str1 + "\": " + str2 + " vs " + str3);
        str1 = this._annotationIntrospector.findDeserializablePropertyName(localAnnotatedField1);
        break label69;
        label232: if (!paramVisibilityChecker.isFieldVisible(localAnnotatedField1))
          break;
        str1 = localAnnotatedField1.getName();
        if (localPropertyNamingStrategy == null)
          continue;
        str1 = localPropertyNamingStrategy.nameForField(this._config, localAnnotatedField1, str1);
      }
    }
    return localLinkedHashMap;
  }

  public TypeBindings bindingsForBeanType()
  {
    if (this._bindings == null)
      this._bindings = new TypeBindings(this._config.getTypeFactory(), this._type);
    return this._bindings;
  }

  public AnnotatedMethod findAnyGetter()
    throws IllegalArgumentException
  {
    Object localObject = null;
    Iterator localIterator = this._classInfo.memberMethods().iterator();
    while (localIterator.hasNext())
    {
      AnnotatedMethod localAnnotatedMethod = (AnnotatedMethod)localIterator.next();
      if (!this._annotationIntrospector.hasAnyGetterAnnotation(localAnnotatedMethod))
        continue;
      if (localObject != null)
        throw new IllegalArgumentException("Multiple methods with 'any-getter' annotation (" + localObject.getName() + "(), " + localAnnotatedMethod.getName() + ")");
      if (!Map.class.isAssignableFrom(localAnnotatedMethod.getRawType()))
        throw new IllegalArgumentException("Invalid 'any-getter' annotation on method " + localAnnotatedMethod.getName() + "(): return type is not instance of java.util.Map");
      localObject = localAnnotatedMethod;
    }
    return localObject;
  }

  public AnnotatedMethod findAnySetter()
    throws IllegalArgumentException
  {
    Object localObject = null;
    Iterator localIterator = this._classInfo.memberMethods().iterator();
    while (localIterator.hasNext())
    {
      AnnotatedMethod localAnnotatedMethod = (AnnotatedMethod)localIterator.next();
      if (!this._annotationIntrospector.hasAnySetterAnnotation(localAnnotatedMethod))
        continue;
      if (localObject != null)
        throw new IllegalArgumentException("Multiple methods with 'any-setter' annotation (" + localObject.getName() + "(), " + localAnnotatedMethod.getName() + ")");
      int i = localAnnotatedMethod.getParameterCount();
      if (i != 2)
        throw new IllegalArgumentException("Invalid 'any-setter' annotation on method " + localAnnotatedMethod.getName() + "(): takes " + i + " parameters, should take 2");
      Class localClass = localAnnotatedMethod.getParameterClass(0);
      if ((localClass != String.class) && (localClass != Object.class))
        throw new IllegalArgumentException("Invalid 'any-setter' annotation on method " + localAnnotatedMethod.getName() + "(): first argument not of type String or Object, but " + localClass.getName());
      localObject = localAnnotatedMethod;
    }
    return localObject;
  }

  public Map<String, AnnotatedMember> findBackReferenceProperties()
  {
    HashMap localHashMap = null;
    Iterator localIterator1 = this._classInfo.memberMethods().iterator();
    while (localIterator1.hasNext())
    {
      AnnotatedMethod localAnnotatedMethod = (AnnotatedMethod)localIterator1.next();
      if (localAnnotatedMethod.getParameterCount() != 1)
        continue;
      AnnotationIntrospector.ReferenceProperty localReferenceProperty2 = this._annotationIntrospector.findReferenceType(localAnnotatedMethod);
      if ((localReferenceProperty2 == null) || (!localReferenceProperty2.isBackReference()))
        continue;
      if (localHashMap == null)
        localHashMap = new HashMap();
      if (localHashMap.put(localReferenceProperty2.getName(), localAnnotatedMethod) == null)
        continue;
      throw new IllegalArgumentException("Multiple back-reference properties with name '" + localReferenceProperty2.getName() + "'");
    }
    Iterator localIterator2 = this._classInfo.fields().iterator();
    while (localIterator2.hasNext())
    {
      AnnotatedField localAnnotatedField = (AnnotatedField)localIterator2.next();
      AnnotationIntrospector.ReferenceProperty localReferenceProperty1 = this._annotationIntrospector.findReferenceType(localAnnotatedField);
      if ((localReferenceProperty1 == null) || (!localReferenceProperty1.isBackReference()))
        continue;
      if (localHashMap == null)
        localHashMap = new HashMap();
      if (localHashMap.put(localReferenceProperty1.getName(), localAnnotatedField) == null)
        continue;
      throw new IllegalArgumentException("Multiple back-reference properties with name '" + localReferenceProperty1.getName() + "'");
    }
    return localHashMap;
  }

  public List<String> findCreatorPropertyNames()
  {
    Object localObject = null;
    for (int i = 0; i < 2; i++)
    {
      if (i == 0);
      for (List localList = getConstructors(); ; localList = getFactoryMethods())
      {
        Iterator localIterator = localList.iterator();
        while (localIterator.hasNext())
        {
          AnnotatedWithParams localAnnotatedWithParams = (AnnotatedWithParams)localIterator.next();
          int j = localAnnotatedWithParams.getParameterCount();
          if (j < 1)
            continue;
          String str = this._annotationIntrospector.findPropertyNameForParam(localAnnotatedWithParams.getParameter(0));
          if (str == null)
            continue;
          if (localObject == null)
            localObject = new ArrayList();
          ((List)localObject).add(str);
          for (int k = 1; k < j; k++)
            ((List)localObject).add(this._annotationIntrospector.findPropertyNameForParam(localAnnotatedWithParams.getParameter(k)));
        }
      }
    }
    if (localObject == null)
      localObject = Collections.emptyList();
    return (List<String>)localObject;
  }

  public Constructor<?> findDefaultConstructor()
  {
    AnnotatedConstructor localAnnotatedConstructor = this._classInfo.getDefaultConstructor();
    if (localAnnotatedConstructor == null)
      return null;
    return localAnnotatedConstructor.getAnnotated();
  }

  public LinkedHashMap<String, AnnotatedField> findDeserializableFields(VisibilityChecker<?> paramVisibilityChecker, Collection<String> paramCollection)
  {
    return _findPropertyFields(paramVisibilityChecker, paramCollection, false);
  }

  public Method findFactoryMethod(Class<?>[] paramArrayOfClass)
  {
    Iterator localIterator = this._classInfo.getStaticMethods().iterator();
    while (localIterator.hasNext())
    {
      AnnotatedMethod localAnnotatedMethod = (AnnotatedMethod)localIterator.next();
      if (!isFactoryMethod(localAnnotatedMethod))
        continue;
      Class localClass = localAnnotatedMethod.getParameterClass(0);
      int i = paramArrayOfClass.length;
      for (int j = 0; j < i; j++)
        if (localClass.isAssignableFrom(paramArrayOfClass[j]))
          return localAnnotatedMethod.getAnnotated();
    }
    return null;
  }

  public LinkedHashMap<String, AnnotatedMethod> findGetters(VisibilityChecker<?> paramVisibilityChecker, Collection<String> paramCollection)
  {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    PropertyNamingStrategy localPropertyNamingStrategy = this._config.getPropertyNamingStrategy();
    Iterator localIterator = this._classInfo.memberMethods().iterator();
    label130: label305: label327: 
    while (localIterator.hasNext())
    {
      AnnotatedMethod localAnnotatedMethod1 = (AnnotatedMethod)localIterator.next();
      if (localAnnotatedMethod1.getParameterCount() != 0)
        continue;
      String str1 = this._annotationIntrospector.findGettablePropertyName(localAnnotatedMethod1);
      String str2;
      if (str1 != null)
      {
        if (str1.length() == 0)
        {
          str1 = okNameForAnyGetter(localAnnotatedMethod1, localAnnotatedMethod1.getName());
          if (str1 == null)
            str1 = localAnnotatedMethod1.getName();
          if (localPropertyNamingStrategy != null)
            str1 = localPropertyNamingStrategy.nameForGetterMethod(this._config, localAnnotatedMethod1, str1);
        }
        if ((paramCollection == null) || (!paramCollection.contains(str1)))
        {
          AnnotatedMethod localAnnotatedMethod2 = (AnnotatedMethod)localLinkedHashMap.put(str1, localAnnotatedMethod1);
          if (localAnnotatedMethod2 == null)
            continue;
          String str3 = localAnnotatedMethod2.getFullName();
          String str4 = localAnnotatedMethod1.getFullName();
          throw new IllegalArgumentException("Conflicting getter definitions for property \"" + str1 + "\": " + str3 + " vs " + str4);
        }
      }
      else
      {
        str2 = localAnnotatedMethod1.getName();
        if (!str2.startsWith("get"))
          break label305;
        if (!paramVisibilityChecker.isGetterVisible(localAnnotatedMethod1))
          continue;
      }
      for (str1 = okNameForGetter(localAnnotatedMethod1, str2); ; str1 = okNameForIsGetter(localAnnotatedMethod1, str2))
      {
        if ((str1 == null) || (this._annotationIntrospector.hasAnyGetterAnnotation(localAnnotatedMethod1)))
          break label327;
        if (localPropertyNamingStrategy == null)
          break label130;
        str1 = localPropertyNamingStrategy.nameForGetterMethod(this._config, localAnnotatedMethod1, str1);
        break label130;
        break;
        if (!paramVisibilityChecker.isIsGetterVisible(localAnnotatedMethod1))
          break;
      }
    }
    return localLinkedHashMap;
  }

  public AnnotatedMethod findJsonValueMethod()
  {
    Object localObject = null;
    Iterator localIterator = this._classInfo.memberMethods().iterator();
    while (localIterator.hasNext())
    {
      AnnotatedMethod localAnnotatedMethod = (AnnotatedMethod)localIterator.next();
      if (!this._annotationIntrospector.hasAsValueAnnotation(localAnnotatedMethod))
        continue;
      if (localObject != null)
        throw new IllegalArgumentException("Multiple methods with active 'as-value' annotation (" + localObject.getName() + "(), " + localAnnotatedMethod.getName() + ")");
      if (!ClassUtil.hasGetterSignature(localAnnotatedMethod.getAnnotated()))
        throw new IllegalArgumentException("Method " + localAnnotatedMethod.getName() + "() marked with an 'as-value' annotation, but does not have valid getter signature (non-static, takes no args, returns a value)");
      localObject = localAnnotatedMethod;
    }
    return localObject;
  }

  public AnnotatedMethod findMethod(String paramString, Class<?>[] paramArrayOfClass)
  {
    return this._classInfo.findMethod(paramString, paramArrayOfClass);
  }

  public LinkedHashMap<String, AnnotatedField> findSerializableFields(VisibilityChecker<?> paramVisibilityChecker, Collection<String> paramCollection)
  {
    return _findPropertyFields(paramVisibilityChecker, paramCollection, true);
  }

  public JsonSerialize.Inclusion findSerializationInclusion(JsonSerialize.Inclusion paramInclusion)
  {
    return this._annotationIntrospector.findSerializationInclusion(this._classInfo, paramInclusion);
  }

  public LinkedHashMap<String, AnnotatedMethod> findSetters(VisibilityChecker<?> paramVisibilityChecker)
  {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    PropertyNamingStrategy localPropertyNamingStrategy = this._config.getPropertyNamingStrategy();
    Iterator localIterator = this._classInfo.memberMethods().iterator();
    while (localIterator.hasNext())
    {
      AnnotatedMethod localAnnotatedMethod1 = (AnnotatedMethod)localIterator.next();
      if (localAnnotatedMethod1.getParameterCount() != 1)
        continue;
      String str1 = this._annotationIntrospector.findSettablePropertyName(localAnnotatedMethod1);
      if (str1 != null)
        if (str1.length() == 0)
        {
          str1 = okNameForSetter(localAnnotatedMethod1);
          if (str1 == null)
            str1 = localAnnotatedMethod1.getName();
          if (localPropertyNamingStrategy != null)
            str1 = localPropertyNamingStrategy.nameForSetterMethod(this._config, localAnnotatedMethod1, str1);
        }
      AnnotatedMethod localAnnotatedMethod2;
      while (true)
      {
        localAnnotatedMethod2 = (AnnotatedMethod)localLinkedHashMap.put(str1, localAnnotatedMethod1);
        if (localAnnotatedMethod2 == null)
          break;
        if (localAnnotatedMethod2.getDeclaringClass() != localAnnotatedMethod1.getDeclaringClass())
          break label262;
        String str2 = localAnnotatedMethod2.getFullName();
        String str3 = localAnnotatedMethod1.getFullName();
        throw new IllegalArgumentException("Conflicting setter definitions for property \"" + str1 + "\": " + str2 + " vs " + str3);
        if (!paramVisibilityChecker.isSetterVisible(localAnnotatedMethod1))
          break;
        str1 = okNameForSetter(localAnnotatedMethod1);
        if (str1 == null)
          break;
        if (localPropertyNamingStrategy == null)
          continue;
        str1 = localPropertyNamingStrategy.nameForSetterMethod(this._config, localAnnotatedMethod1, str1);
      }
      label262: localLinkedHashMap.put(str1, localAnnotatedMethod2);
    }
    return localLinkedHashMap;
  }

  public Constructor<?> findSingleArgConstructor(Class<?>[] paramArrayOfClass)
  {
    Iterator localIterator = this._classInfo.getConstructors().iterator();
    while (localIterator.hasNext())
    {
      AnnotatedConstructor localAnnotatedConstructor = (AnnotatedConstructor)localIterator.next();
      if (localAnnotatedConstructor.getParameterCount() != 1)
        continue;
      Class localClass = localAnnotatedConstructor.getParameterClass(0);
      int i = paramArrayOfClass.length;
      for (int j = 0; j < i; j++)
        if (paramArrayOfClass[j] == localClass)
          return localAnnotatedConstructor.getAnnotated();
    }
    return null;
  }

  public Annotations getClassAnnotations()
  {
    return this._classInfo.getAnnotations();
  }

  public AnnotatedClass getClassInfo()
  {
    return this._classInfo;
  }

  public List<AnnotatedConstructor> getConstructors()
  {
    return this._classInfo.getConstructors();
  }

  public List<AnnotatedMethod> getFactoryMethods()
  {
    List localList = this._classInfo.getStaticMethods();
    if (localList.isEmpty())
      return localList;
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      AnnotatedMethod localAnnotatedMethod = (AnnotatedMethod)localIterator.next();
      if (!isFactoryMethod(localAnnotatedMethod))
        continue;
      localArrayList.add(localAnnotatedMethod);
    }
    return localArrayList;
  }

  public boolean hasKnownClassAnnotations()
  {
    return this._classInfo.hasAnnotations();
  }

  public Object instantiateBean(boolean paramBoolean)
  {
    AnnotatedConstructor localAnnotatedConstructor = this._classInfo.getDefaultConstructor();
    if (localAnnotatedConstructor == null)
      return null;
    if (paramBoolean)
      localAnnotatedConstructor.fixAccess();
    Object localObject1;
    try
    {
      Object localObject2 = localAnnotatedConstructor.getAnnotated().newInstance(new Object[0]);
      return localObject2;
    }
    catch (Exception localException)
    {
      for (localObject1 = localException; ((Throwable)localObject1).getCause() != null; localObject1 = ((Throwable)localObject1).getCause());
      if ((localObject1 instanceof Error))
        throw ((Error)localObject1);
      if ((localObject1 instanceof RuntimeException))
        throw ((RuntimeException)localObject1);
    }
    throw new IllegalArgumentException("Failed to instantiate bean of type " + this._classInfo.getAnnotated().getName() + ": (" + localObject1.getClass().getName() + ") " + ((Throwable)localObject1).getMessage(), (Throwable)localObject1);
  }

  protected boolean isCglibGetCallbacks(AnnotatedMethod paramAnnotatedMethod)
  {
    Class localClass = paramAnnotatedMethod.getRawType();
    if ((localClass == null) || (!localClass.isArray()));
    String str;
    do
    {
      Package localPackage;
      do
      {
        return false;
        localPackage = localClass.getComponentType().getPackage();
      }
      while (localPackage == null);
      str = localPackage.getName();
    }
    while ((!str.startsWith("net.sf.cglib")) && (!str.startsWith("org.hibernate.repackage.cglib")));
    return true;
  }

  protected boolean isFactoryMethod(AnnotatedMethod paramAnnotatedMethod)
  {
    Class localClass = paramAnnotatedMethod.getRawType();
    if (!getBeanClass().isAssignableFrom(localClass));
    do
    {
      return false;
      if (this._annotationIntrospector.hasCreatorAnnotation(paramAnnotatedMethod))
        return true;
    }
    while (!"valueOf".equals(paramAnnotatedMethod.getName()));
    return true;
  }

  protected boolean isGroovyMetaClassGetter(AnnotatedMethod paramAnnotatedMethod)
  {
    Class localClass = paramAnnotatedMethod.getRawType();
    if ((localClass == null) || (localClass.isArray()));
    Package localPackage;
    do
    {
      return false;
      localPackage = localClass.getPackage();
    }
    while ((localPackage == null) || (!localPackage.getName().startsWith("groovy.lang")));
    return true;
  }

  protected boolean isGroovyMetaClassSetter(AnnotatedMethod paramAnnotatedMethod)
  {
    Package localPackage = paramAnnotatedMethod.getParameterClass(0).getPackage();
    int i = 0;
    if (localPackage != null)
    {
      boolean bool = localPackage.getName().startsWith("groovy.lang");
      i = 0;
      if (bool)
        i = 1;
    }
    return i;
  }

  protected String mangleGetterName(Annotated paramAnnotated, String paramString)
  {
    return manglePropertyName(paramString);
  }

  protected String mangleSetterName(Annotated paramAnnotated, String paramString)
  {
    return manglePropertyName(paramString);
  }

  public String okNameForAnyGetter(AnnotatedMethod paramAnnotatedMethod, String paramString)
  {
    String str = okNameForIsGetter(paramAnnotatedMethod, paramString);
    if (str == null)
      str = okNameForGetter(paramAnnotatedMethod, paramString);
    return str;
  }

  public String okNameForGetter(AnnotatedMethod paramAnnotatedMethod, String paramString)
  {
    if (paramString.startsWith("get"))
    {
      if (!"getCallbacks".equals(paramString))
        break label30;
      if (!isCglibGetCallbacks(paramAnnotatedMethod))
        break label48;
    }
    label30: 
    do
      return null;
    while (("getMetaClass".equals(paramString)) && (isGroovyMetaClassGetter(paramAnnotatedMethod)));
    label48: return mangleGetterName(paramAnnotatedMethod, paramString.substring(3));
  }

  public String okNameForIsGetter(AnnotatedMethod paramAnnotatedMethod, String paramString)
  {
    if (paramString.startsWith("is"))
    {
      Class localClass = paramAnnotatedMethod.getRawType();
      if ((localClass == Boolean.class) || (localClass == Boolean.TYPE));
    }
    else
    {
      return null;
    }
    return mangleGetterName(paramAnnotatedMethod, paramString.substring(2));
  }

  public String okNameForSetter(AnnotatedMethod paramAnnotatedMethod)
  {
    String str1 = paramAnnotatedMethod.getName();
    String str2;
    if (str1.startsWith("set"))
    {
      str2 = mangleSetterName(paramAnnotatedMethod, str1.substring(3));
      if (str2 != null)
        break label32;
    }
    label32: 
    do
      return null;
    while (("metaClass".equals(str2)) && (isGroovyMetaClassSetter(paramAnnotatedMethod)));
    return str2;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.BasicBeanDescription
 * JD-Core Version:    0.6.0
 */