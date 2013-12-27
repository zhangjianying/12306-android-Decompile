package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.util.Map;
import org.codehaus.jackson.JsonProcessingException;

abstract class PropertyValue
{
  public final PropertyValue next;
  public final Object value;

  protected PropertyValue(PropertyValue paramPropertyValue, Object paramObject)
  {
    this.next = paramPropertyValue;
    this.value = paramObject;
  }

  public abstract void assign(Object paramObject)
    throws IOException, JsonProcessingException;

  static final class Any extends PropertyValue
  {
    final SettableAnyProperty _property;
    final String _propertyName;

    public Any(PropertyValue paramPropertyValue, Object paramObject, SettableAnyProperty paramSettableAnyProperty, String paramString)
    {
      super(paramObject);
      this._property = paramSettableAnyProperty;
      this._propertyName = paramString;
    }

    public void assign(Object paramObject)
      throws IOException, JsonProcessingException
    {
      this._property.set(paramObject, this._propertyName, this.value);
    }
  }

  static final class Map extends PropertyValue
  {
    final Object _key;

    public Map(PropertyValue paramPropertyValue, Object paramObject1, Object paramObject2)
    {
      super(paramObject1);
      this._key = paramObject2;
    }

    public void assign(Object paramObject)
      throws IOException, JsonProcessingException
    {
      ((Map)paramObject).put(this._key, this.value);
    }
  }

  static final class Regular extends PropertyValue
  {
    final SettableBeanProperty _property;

    public Regular(PropertyValue paramPropertyValue, Object paramObject, SettableBeanProperty paramSettableBeanProperty)
    {
      super(paramObject);
      this._property = paramSettableBeanProperty;
    }

    public void assign(Object paramObject)
      throws IOException, JsonProcessingException
    {
      this._property.set(paramObject, this.value);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.PropertyValue
 * JD-Core Version:    0.6.0
 */