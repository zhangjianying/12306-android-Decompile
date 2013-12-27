package org.codehaus.jackson.map.util;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializableWithType;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

public class JSONPObject
  implements JsonSerializableWithType
{
  protected final String _function;
  protected final JavaType _serializationType;
  protected final Object _value;

  public JSONPObject(String paramString, Object paramObject)
  {
    this(paramString, paramObject, (JavaType)null);
  }

  @Deprecated
  public JSONPObject(String paramString, Object paramObject, Class<?> paramClass)
  {
    this._function = paramString;
    this._value = paramObject;
    if (paramClass == null);
    for (JavaType localJavaType = null; ; localJavaType = TypeFactory.defaultInstance().constructType(paramClass))
    {
      this._serializationType = localJavaType;
      return;
    }
  }

  public JSONPObject(String paramString, Object paramObject, JavaType paramJavaType)
  {
    this._function = paramString;
    this._value = paramObject;
    this._serializationType = paramJavaType;
  }

  public String getFunction()
  {
    return this._function;
  }

  public JavaType getSerializationType()
  {
    return this._serializationType;
  }

  public Object getValue()
  {
    return this._value;
  }

  public void serialize(JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonProcessingException
  {
    paramJsonGenerator.writeRaw(this._function);
    paramJsonGenerator.writeRaw('(');
    if (this._value == null)
      paramSerializerProvider.defaultSerializeNull(paramJsonGenerator);
    while (true)
    {
      paramJsonGenerator.writeRaw(')');
      return;
      if (this._serializationType != null)
      {
        paramSerializerProvider.findTypedValueSerializer(this._serializationType, true, null).serialize(this._value, paramJsonGenerator, paramSerializerProvider);
        continue;
      }
      paramSerializerProvider.findTypedValueSerializer(this._value.getClass(), true, null).serialize(this._value, paramJsonGenerator, paramSerializerProvider);
    }
  }

  public void serializeWithType(JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer)
    throws IOException, JsonProcessingException
  {
    serialize(paramJsonGenerator, paramSerializerProvider);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.util.JSONPObject
 * JD-Core Version:    0.6.0
 */