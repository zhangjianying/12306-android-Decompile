package org.codehaus.jackson.map.deser;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.util.ClassUtil;

public class CreatorContainer
{
  final BasicBeanDescription _beanDesc;
  final boolean _canFixAccess;
  protected Constructor<?> _defaultConstructor;
  AnnotatedConstructor _delegatingConstructor;
  AnnotatedMethod _delegatingFactory;
  AnnotatedConstructor _intConstructor;
  AnnotatedMethod _intFactory;
  AnnotatedConstructor _longConstructor;
  AnnotatedMethod _longFactory;
  AnnotatedConstructor _propertyBasedConstructor;
  SettableBeanProperty[] _propertyBasedConstructorProperties = null;
  AnnotatedMethod _propertyBasedFactory;
  SettableBeanProperty[] _propertyBasedFactoryProperties = null;
  AnnotatedConstructor _strConstructor;
  AnnotatedMethod _strFactory;

  public CreatorContainer(BasicBeanDescription paramBasicBeanDescription, boolean paramBoolean)
  {
    this._beanDesc = paramBasicBeanDescription;
    this._canFixAccess = paramBoolean;
  }

  public void addDelegatingConstructor(AnnotatedConstructor paramAnnotatedConstructor)
  {
    this._delegatingConstructor = verifyNonDup(paramAnnotatedConstructor, this._delegatingConstructor, "long");
  }

  public void addDelegatingFactory(AnnotatedMethod paramAnnotatedMethod)
  {
    this._delegatingFactory = verifyNonDup(paramAnnotatedMethod, this._delegatingFactory, "long");
  }

  public void addIntConstructor(AnnotatedConstructor paramAnnotatedConstructor)
  {
    this._intConstructor = verifyNonDup(paramAnnotatedConstructor, this._intConstructor, "int");
  }

  public void addIntFactory(AnnotatedMethod paramAnnotatedMethod)
  {
    this._intFactory = verifyNonDup(paramAnnotatedMethod, this._intFactory, "int");
  }

  public void addLongConstructor(AnnotatedConstructor paramAnnotatedConstructor)
  {
    this._longConstructor = verifyNonDup(paramAnnotatedConstructor, this._longConstructor, "long");
  }

  public void addLongFactory(AnnotatedMethod paramAnnotatedMethod)
  {
    this._longFactory = verifyNonDup(paramAnnotatedMethod, this._longFactory, "long");
  }

  public void addPropertyConstructor(AnnotatedConstructor paramAnnotatedConstructor, SettableBeanProperty[] paramArrayOfSettableBeanProperty)
  {
    this._propertyBasedConstructor = verifyNonDup(paramAnnotatedConstructor, this._propertyBasedConstructor, "property-based");
    if (paramArrayOfSettableBeanProperty.length > 1)
    {
      HashMap localHashMap = new HashMap();
      int i = 0;
      int j = paramArrayOfSettableBeanProperty.length;
      while (i < j)
      {
        String str = paramArrayOfSettableBeanProperty[i].getName();
        Integer localInteger = (Integer)localHashMap.put(str, Integer.valueOf(i));
        if (localInteger != null)
          throw new IllegalArgumentException("Duplicate creator property \"" + str + "\" (index " + localInteger + " vs " + i + ")");
        i++;
      }
    }
    this._propertyBasedConstructorProperties = paramArrayOfSettableBeanProperty;
  }

  public void addPropertyFactory(AnnotatedMethod paramAnnotatedMethod, SettableBeanProperty[] paramArrayOfSettableBeanProperty)
  {
    this._propertyBasedFactory = verifyNonDup(paramAnnotatedMethod, this._propertyBasedFactory, "property-based");
    this._propertyBasedFactoryProperties = paramArrayOfSettableBeanProperty;
  }

  public void addStringConstructor(AnnotatedConstructor paramAnnotatedConstructor)
  {
    this._strConstructor = verifyNonDup(paramAnnotatedConstructor, this._strConstructor, "String");
  }

  public void addStringFactory(AnnotatedMethod paramAnnotatedMethod)
  {
    this._strFactory = verifyNonDup(paramAnnotatedMethod, this._strFactory, "String");
  }

  public Creator.Delegating delegatingCreator()
  {
    if ((this._delegatingConstructor == null) && (this._delegatingFactory == null))
      return null;
    return new Creator.Delegating(this._beanDesc, this._delegatingConstructor, this._delegatingFactory);
  }

  public Constructor<?> getDefaultConstructor()
  {
    return this._defaultConstructor;
  }

  public Creator.NumberBased numberCreator()
  {
    if ((this._intConstructor == null) && (this._intFactory == null) && (this._longConstructor == null) && (this._longFactory == null))
      return null;
    return new Creator.NumberBased(this._beanDesc.getBeanClass(), this._intConstructor, this._intFactory, this._longConstructor, this._longFactory);
  }

  public Creator.PropertyBased propertyBasedCreator()
  {
    if ((this._propertyBasedConstructor == null) && (this._propertyBasedFactory == null))
      return null;
    return new Creator.PropertyBased(this._propertyBasedConstructor, this._propertyBasedConstructorProperties, this._propertyBasedFactory, this._propertyBasedFactoryProperties);
  }

  public void setDefaultConstructor(Constructor<?> paramConstructor)
  {
    this._defaultConstructor = paramConstructor;
  }

  public Creator.StringBased stringCreator()
  {
    if ((this._strConstructor == null) && (this._strFactory == null))
      return null;
    return new Creator.StringBased(this._beanDesc.getBeanClass(), this._strConstructor, this._strFactory);
  }

  protected AnnotatedConstructor verifyNonDup(AnnotatedConstructor paramAnnotatedConstructor1, AnnotatedConstructor paramAnnotatedConstructor2, String paramString)
  {
    if (paramAnnotatedConstructor2 != null)
      throw new IllegalArgumentException("Conflicting " + paramString + " constructors: already had " + paramAnnotatedConstructor2 + ", encountered " + paramAnnotatedConstructor1);
    if (this._canFixAccess)
      ClassUtil.checkAndFixAccess(paramAnnotatedConstructor1.getAnnotated());
    return paramAnnotatedConstructor1;
  }

  protected AnnotatedMethod verifyNonDup(AnnotatedMethod paramAnnotatedMethod1, AnnotatedMethod paramAnnotatedMethod2, String paramString)
  {
    if (paramAnnotatedMethod2 != null)
      throw new IllegalArgumentException("Conflicting " + paramString + " factory methods: already had " + paramAnnotatedMethod2 + ", encountered " + paramAnnotatedMethod1);
    if (this._canFixAccess)
      ClassUtil.checkAndFixAccess(paramAnnotatedMethod1.getAnnotated());
    return paramAnnotatedMethod1;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.CreatorContainer
 * JD-Core Version:    0.6.0
 */