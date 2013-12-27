package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.lang.reflect.Method;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.annotate.JsonCachable;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.util.ClassUtil;

@JsonCachable
public class EnumDeserializer extends StdScalarDeserializer<Enum<?>>
{
  final EnumResolver<?> _resolver;

  public EnumDeserializer(EnumResolver<?> paramEnumResolver)
  {
    super(Enum.class);
    this._resolver = paramEnumResolver;
  }

  public static JsonDeserializer<?> deserializerForCreator(DeserializationConfig paramDeserializationConfig, Class<?> paramClass, AnnotatedMethod paramAnnotatedMethod)
  {
    if (paramAnnotatedMethod.getParameterType(0) != String.class)
      throw new IllegalArgumentException("Parameter #0 type for factory method (" + paramAnnotatedMethod + ") not suitable, must be java.lang.String");
    if (paramDeserializationConfig.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS))
      ClassUtil.checkAndFixAccess(paramAnnotatedMethod.getMember());
    return new FactoryBasedDeserializer(paramClass, paramAnnotatedMethod);
  }

  public Enum<?> deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    JsonToken localJsonToken = paramJsonParser.getCurrentToken();
    if (localJsonToken == JsonToken.VALUE_STRING)
    {
      String str = paramJsonParser.getText();
      Enum localEnum2 = this._resolver.findEnum(str);
      if (localEnum2 == null)
        throw paramDeserializationContext.weirdStringException(this._resolver.getEnumClass(), "value not one of declared Enum instance names");
      return localEnum2;
    }
    if (localJsonToken == JsonToken.VALUE_NUMBER_INT)
    {
      if (paramDeserializationContext.isEnabled(DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS))
        throw paramDeserializationContext.mappingException("Not allowed to deserialize Enum value out of JSON number (disable DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS to allow)");
      int i = paramJsonParser.getIntValue();
      Enum localEnum1 = this._resolver.getEnum(i);
      if (localEnum1 == null)
        throw paramDeserializationContext.weirdNumberException(this._resolver.getEnumClass(), "index value outside legal index range [0.." + this._resolver.lastValidIndex() + "]");
      return localEnum1;
    }
    throw paramDeserializationContext.mappingException(this._resolver.getEnumClass());
  }

  protected static class FactoryBasedDeserializer extends StdScalarDeserializer<Object>
  {
    protected final Class<?> _enumClass;
    protected final Method _factory;

    public FactoryBasedDeserializer(Class<?> paramClass, AnnotatedMethod paramAnnotatedMethod)
    {
      super();
      this._enumClass = paramClass;
      this._factory = paramAnnotatedMethod.getAnnotated();
    }

    public Object deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      if (paramJsonParser.getCurrentToken() != JsonToken.VALUE_STRING)
        throw paramDeserializationContext.mappingException(this._enumClass);
      String str = paramJsonParser.getText();
      try
      {
        Object localObject = this._factory.invoke(this._enumClass, new Object[] { str });
        return localObject;
      }
      catch (Exception localException)
      {
        ClassUtil.unwrapAndThrowAsIAE(localException);
      }
      return null;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.EnumDeserializer
 * JD-Core Version:    0.6.0
 */