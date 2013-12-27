package org.codehaus.jackson.map.ser.impl;

import java.lang.reflect.Type;
import java.util.Collection;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.SerializerBase;
import org.codehaus.jackson.node.ObjectNode;

public abstract class StaticListSerializerBase<T extends Collection<?>> extends SerializerBase<T>
{
  protected final BeanProperty _property;

  protected StaticListSerializerBase(Class<?> paramClass, BeanProperty paramBeanProperty)
  {
    super(paramClass, false);
    this._property = paramBeanProperty;
  }

  protected abstract JsonNode contentSchema();

  public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
  {
    ObjectNode localObjectNode = createSchemaNode("array", true);
    localObjectNode.put("items", contentSchema());
    return localObjectNode;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.impl.StaticListSerializerBase
 * JD-Core Version:    0.6.0
 */