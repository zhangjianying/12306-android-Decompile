package org.codehaus.jackson.map.ser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.util.EnumValues;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.JavaType;

@JacksonStdImpl
public class EnumSerializer extends ScalarSerializerBase<Enum<?>>
{
  protected final EnumValues _values;

  public EnumSerializer(EnumValues paramEnumValues)
  {
    super(Enum.class, false);
    this._values = paramEnumValues;
  }

  public static EnumSerializer construct(Class<Enum<?>> paramClass, SerializationConfig paramSerializationConfig, BasicBeanDescription paramBasicBeanDescription)
  {
    AnnotationIntrospector localAnnotationIntrospector = paramSerializationConfig.getAnnotationIntrospector();
    if (paramSerializationConfig.isEnabled(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING));
    for (EnumValues localEnumValues = EnumValues.constructFromToString(paramClass, localAnnotationIntrospector); ; localEnumValues = EnumValues.constructFromName(paramClass, localAnnotationIntrospector))
      return new EnumSerializer(localEnumValues);
  }

  public EnumValues getEnumValues()
  {
    return this._values;
  }

  public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
  {
    ObjectNode localObjectNode = createSchemaNode("string", true);
    if ((paramType != null) && (paramSerializerProvider.constructType(paramType).isEnumType()))
    {
      ArrayNode localArrayNode = localObjectNode.putArray("enum");
      Iterator localIterator = this._values.values().iterator();
      while (localIterator.hasNext())
        localArrayNode.add(((SerializedString)localIterator.next()).getValue());
    }
    return localObjectNode;
  }

  public final void serialize(Enum<?> paramEnum, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonGenerationException
  {
    paramJsonGenerator.writeString(this._values.serializedValueFor(paramEnum));
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.EnumSerializer
 * JD-Core Version:    0.6.0
 */