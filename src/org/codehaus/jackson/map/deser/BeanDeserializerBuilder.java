package org.codehaus.jackson.map.deser;

import java.util.HashMap;
import java.util.HashSet;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.deser.impl.BeanPropertyMap;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;

public class BeanDeserializerBuilder
{
  protected SettableAnyProperty _anySetter;
  protected HashMap<String, SettableBeanProperty> _backRefProperties;
  protected final BasicBeanDescription _beanDesc;
  protected CreatorContainer _creators;
  protected HashSet<String> _ignorableProps;
  protected boolean _ignoreAllUnknown;
  protected final HashMap<String, SettableBeanProperty> _properties = new HashMap();

  public BeanDeserializerBuilder(BasicBeanDescription paramBasicBeanDescription)
  {
    this._beanDesc = paramBasicBeanDescription;
  }

  public void addBackReferenceProperty(String paramString, SettableBeanProperty paramSettableBeanProperty)
  {
    if (this._backRefProperties == null)
      this._backRefProperties = new HashMap(4);
    this._backRefProperties.put(paramString, paramSettableBeanProperty);
  }

  public void addIgnorable(String paramString)
  {
    if (this._ignorableProps == null)
      this._ignorableProps = new HashSet();
    this._ignorableProps.add(paramString);
  }

  public void addOrReplaceProperty(SettableBeanProperty paramSettableBeanProperty, boolean paramBoolean)
  {
    this._properties.put(paramSettableBeanProperty.getName(), paramSettableBeanProperty);
  }

  public void addProperty(SettableBeanProperty paramSettableBeanProperty)
  {
    SettableBeanProperty localSettableBeanProperty = (SettableBeanProperty)this._properties.put(paramSettableBeanProperty.getName(), paramSettableBeanProperty);
    if ((localSettableBeanProperty != null) && (localSettableBeanProperty != paramSettableBeanProperty))
      throw new IllegalArgumentException("Duplicate property '" + paramSettableBeanProperty.getName() + "' for " + this._beanDesc.getType());
  }

  public JsonDeserializer<?> build(BeanProperty paramBeanProperty)
  {
    BeanPropertyMap localBeanPropertyMap = new BeanPropertyMap(this._properties.values());
    localBeanPropertyMap.assignIndexes();
    return new BeanDeserializer(this._beanDesc.getClassInfo(), this._beanDesc.getType(), paramBeanProperty, this._creators, localBeanPropertyMap, this._backRefProperties, this._ignorableProps, this._ignoreAllUnknown, this._anySetter);
  }

  public boolean hasProperty(String paramString)
  {
    return this._properties.containsKey(paramString);
  }

  public SettableBeanProperty removeProperty(String paramString)
  {
    return (SettableBeanProperty)this._properties.remove(paramString);
  }

  public void setAnySetter(SettableAnyProperty paramSettableAnyProperty)
  {
    if ((this._anySetter != null) && (paramSettableAnyProperty != null))
      throw new IllegalStateException("_anySetter already set to non-null");
    this._anySetter = paramSettableAnyProperty;
  }

  public void setCreators(CreatorContainer paramCreatorContainer)
  {
    this._creators = paramCreatorContainer;
  }

  public void setIgnoreUnknownProperties(boolean paramBoolean)
  {
    this._ignoreAllUnknown = paramBoolean;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.BeanDeserializerBuilder
 * JD-Core Version:    0.6.0
 */