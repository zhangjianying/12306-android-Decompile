package org.codehaus.jackson.map.ser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.schema.SchemaAware;
import org.codehaus.jackson.type.JavaType;

public abstract class SerializerBase<T> extends JsonSerializer<T>
  implements SchemaAware
{
  protected final Class<T> _handledType;

  protected SerializerBase(Class<T> paramClass)
  {
    this._handledType = paramClass;
  }

  protected SerializerBase(Class<?> paramClass, boolean paramBoolean)
  {
    this._handledType = paramClass;
  }

  protected SerializerBase(JavaType paramJavaType)
  {
    this._handledType = paramJavaType.getRawClass();
  }

  protected ObjectNode createObjectNode()
  {
    return JsonNodeFactory.instance.objectNode();
  }

  protected ObjectNode createSchemaNode(String paramString)
  {
    ObjectNode localObjectNode = createObjectNode();
    localObjectNode.put("type", paramString);
    return localObjectNode;
  }

  protected ObjectNode createSchemaNode(String paramString, boolean paramBoolean)
  {
    ObjectNode localObjectNode = createSchemaNode(paramString);
    if (!paramBoolean)
      if (paramBoolean)
        break label27;
    label27: for (boolean bool = true; ; bool = false)
    {
      localObjectNode.put("required", bool);
      return localObjectNode;
    }
  }

  public abstract JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    throws JsonMappingException;

  public final Class<T> handledType()
  {
    return this._handledType;
  }

  protected boolean isDefaultSerializer(JsonSerializer<?> paramJsonSerializer)
  {
    return (paramJsonSerializer != null) && (paramJsonSerializer.getClass().getAnnotation(JacksonStdImpl.class) != null);
  }

  public abstract void serialize(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonGenerationException;

  @Deprecated
  public void wrapAndThrow(Throwable paramThrowable, Object paramObject, int paramInt)
    throws IOException
  {
    wrapAndThrow(null, paramThrowable, paramObject, paramInt);
  }

  @Deprecated
  public void wrapAndThrow(Throwable paramThrowable, Object paramObject, String paramString)
    throws IOException
  {
    wrapAndThrow(null, paramThrowable, paramObject, paramString);
  }

  public void wrapAndThrow(SerializerProvider paramSerializerProvider, Throwable paramThrowable, Object paramObject, int paramInt)
    throws IOException
  {
    while (((paramThrowable instanceof InvocationTargetException)) && (paramThrowable.getCause() != null))
      paramThrowable = paramThrowable.getCause();
    if ((paramThrowable instanceof Error))
      throw ((Error)paramThrowable);
    int i;
    if ((paramSerializerProvider == null) || (paramSerializerProvider.isEnabled(SerializationConfig.Feature.WRAP_EXCEPTIONS)))
      i = 1;
    while (true)
      if ((paramThrowable instanceof IOException))
      {
        if ((i != 0) && ((paramThrowable instanceof JsonMappingException)))
          break;
        throw ((IOException)paramThrowable);
        i = 0;
        continue;
      }
      else
      {
        if ((i != 0) || (!(paramThrowable instanceof RuntimeException)))
          break;
        throw ((RuntimeException)paramThrowable);
      }
    throw JsonMappingException.wrapWithPath(paramThrowable, paramObject, paramInt);
  }

  public void wrapAndThrow(SerializerProvider paramSerializerProvider, Throwable paramThrowable, Object paramObject, String paramString)
    throws IOException
  {
    while (((paramThrowable instanceof InvocationTargetException)) && (paramThrowable.getCause() != null))
      paramThrowable = paramThrowable.getCause();
    if ((paramThrowable instanceof Error))
      throw ((Error)paramThrowable);
    int i;
    if ((paramSerializerProvider == null) || (paramSerializerProvider.isEnabled(SerializationConfig.Feature.WRAP_EXCEPTIONS)))
      i = 1;
    while (true)
      if ((paramThrowable instanceof IOException))
      {
        if ((i != 0) && ((paramThrowable instanceof JsonMappingException)))
          break;
        throw ((IOException)paramThrowable);
        i = 0;
        continue;
      }
      else
      {
        if ((i != 0) || (!(paramThrowable instanceof RuntimeException)))
          break;
        throw ((RuntimeException)paramThrowable);
      }
    throw JsonMappingException.wrapWithPath(paramThrowable, paramObject, paramString);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.SerializerBase
 * JD-Core Version:    0.6.0
 */