package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.Base64Variants;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.NumberInput;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ResolvableDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.TokenBuffer;

public abstract class StdDeserializer<T> extends JsonDeserializer<T>
{
  protected final Class<?> _valueClass;

  protected StdDeserializer(Class<?> paramClass)
  {
    this._valueClass = paramClass;
  }

  protected StdDeserializer(JavaType paramJavaType)
  {
    if (paramJavaType == null);
    for (Class localClass = null; ; localClass = paramJavaType.getRawClass())
    {
      this._valueClass = localClass;
      return;
    }
  }

  protected static final double parseDouble(String paramString)
    throws NumberFormatException
  {
    if ("2.2250738585072012e-308".equals(paramString))
      return 2.225073858507201E-308D;
    return Double.parseDouble(paramString);
  }

  protected final Boolean _parseBoolean(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    JsonToken localJsonToken = paramJsonParser.getCurrentToken();
    if (localJsonToken == JsonToken.VALUE_TRUE)
      return Boolean.TRUE;
    if (localJsonToken == JsonToken.VALUE_FALSE)
      return Boolean.FALSE;
    if (localJsonToken == JsonToken.VALUE_NULL)
      return null;
    if (localJsonToken == JsonToken.VALUE_NUMBER_INT)
    {
      if (paramJsonParser.getIntValue() == 0)
        return Boolean.FALSE;
      return Boolean.TRUE;
    }
    if (localJsonToken == JsonToken.VALUE_STRING)
    {
      String str = paramJsonParser.getText().trim();
      if ("true".equals(str))
        return Boolean.TRUE;
      if (("false".equals(str)) || (str.length() == 0))
        return Boolean.FALSE;
      throw paramDeserializationContext.weirdStringException(this._valueClass, "only \"true\" or \"false\" recognized");
    }
    throw paramDeserializationContext.mappingException(this._valueClass);
  }

  protected final boolean _parseBooleanPrimitive(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    JsonToken localJsonToken = paramJsonParser.getCurrentToken();
    if (localJsonToken == JsonToken.VALUE_TRUE);
    while (true)
    {
      return true;
      if (localJsonToken == JsonToken.VALUE_FALSE)
        return false;
      if (localJsonToken == JsonToken.VALUE_NULL)
        return false;
      if (localJsonToken == JsonToken.VALUE_NUMBER_INT)
        if (paramJsonParser.getIntValue() == 0)
          return false;
      if (localJsonToken != JsonToken.VALUE_STRING)
        break;
      String str = paramJsonParser.getText().trim();
      if ("true".equals(str))
        continue;
      if (("false".equals(str)) || (str.length() == 0))
        return Boolean.FALSE.booleanValue();
      throw paramDeserializationContext.weirdStringException(this._valueClass, "only \"true\" or \"false\" recognized");
    }
    throw paramDeserializationContext.mappingException(this._valueClass);
  }

  protected java.util.Date _parseDate(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    JsonToken localJsonToken = paramJsonParser.getCurrentToken();
    try
    {
      if (localJsonToken == JsonToken.VALUE_NUMBER_INT)
        return new java.util.Date(paramJsonParser.getLongValue());
      if (localJsonToken == JsonToken.VALUE_STRING)
      {
        String str = paramJsonParser.getText().trim();
        if (str.length() == 0)
          return null;
        return paramDeserializationContext.parseDate(str);
      }
      throw paramDeserializationContext.mappingException(this._valueClass);
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
    }
    throw paramDeserializationContext.weirdStringException(this._valueClass, "not a valid representation (error: " + localIllegalArgumentException.getMessage() + ")");
  }

  protected final Double _parseDouble(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    JsonToken localJsonToken1 = paramJsonParser.getCurrentToken();
    Double localDouble1;
    if ((localJsonToken1 == JsonToken.VALUE_NUMBER_INT) || (localJsonToken1 == JsonToken.VALUE_NUMBER_FLOAT))
      localDouble1 = Double.valueOf(paramJsonParser.getDoubleValue());
    JsonToken localJsonToken2;
    do
    {
      while (true)
      {
        return localDouble1;
        if (localJsonToken1 != JsonToken.VALUE_STRING)
          break;
        String str = paramJsonParser.getText().trim();
        int i = str.length();
        localDouble1 = null;
        if (i == 0)
          continue;
        switch (str.charAt(0))
        {
        default:
        case 'I':
        case 'N':
        case '-':
        }
        try
        {
          do
          {
            do
            {
              do
              {
                Double localDouble2 = Double.valueOf(parseDouble(str));
                return localDouble2;
              }
              while ((!"Infinity".equals(str)) && (!"INF".equals(str)));
              return Double.valueOf((1.0D / 0.0D));
            }
            while (!"NaN".equals(str));
            return Double.valueOf((0.0D / 0.0D));
          }
          while ((!"-Infinity".equals(str)) && (!"-INF".equals(str)));
          return Double.valueOf((-1.0D / 0.0D));
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          throw paramDeserializationContext.weirdStringException(this._valueClass, "not a valid Double value");
        }
      }
      localJsonToken2 = JsonToken.VALUE_NULL;
      localDouble1 = null;
    }
    while (localJsonToken1 == localJsonToken2);
    throw paramDeserializationContext.mappingException(this._valueClass);
  }

  protected final double _parseDoublePrimitive(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    double d1 = 0.0D;
    JsonToken localJsonToken = paramJsonParser.getCurrentToken();
    if ((localJsonToken == JsonToken.VALUE_NUMBER_INT) || (localJsonToken == JsonToken.VALUE_NUMBER_FLOAT))
      d1 = paramJsonParser.getDoubleValue();
    do
      while (true)
      {
        return d1;
        if (localJsonToken != JsonToken.VALUE_STRING)
          break;
        String str = paramJsonParser.getText().trim();
        if (str.length() == 0)
          continue;
        switch (str.charAt(0))
        {
        default:
        case 'I':
        case 'N':
        case '-':
        }
        try
        {
          do
          {
            do
            {
              do
              {
                double d2 = parseDouble(str);
                return d2;
              }
              while ((!"Infinity".equals(str)) && (!"INF".equals(str)));
              return (1.0D / 0.0D);
            }
            while (!"NaN".equals(str));
            return (0.0D / 0.0D);
          }
          while ((!"-Infinity".equals(str)) && (!"-INF".equals(str)));
          return (-1.0D / 0.0D);
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          throw paramDeserializationContext.weirdStringException(this._valueClass, "not a valid double value");
        }
      }
    while (localJsonToken == JsonToken.VALUE_NULL);
    throw paramDeserializationContext.mappingException(this._valueClass);
  }

  protected final Float _parseFloat(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    JsonToken localJsonToken1 = paramJsonParser.getCurrentToken();
    Float localFloat1;
    if ((localJsonToken1 == JsonToken.VALUE_NUMBER_INT) || (localJsonToken1 == JsonToken.VALUE_NUMBER_FLOAT))
      localFloat1 = Float.valueOf(paramJsonParser.getFloatValue());
    JsonToken localJsonToken2;
    do
    {
      while (true)
      {
        return localFloat1;
        if (localJsonToken1 != JsonToken.VALUE_STRING)
          break;
        String str = paramJsonParser.getText().trim();
        int i = str.length();
        localFloat1 = null;
        if (i == 0)
          continue;
        switch (str.charAt(0))
        {
        default:
        case 'I':
        case 'N':
        case '-':
        }
        try
        {
          do
          {
            do
            {
              do
              {
                Float localFloat2 = Float.valueOf(Float.parseFloat(str));
                return localFloat2;
              }
              while ((!"Infinity".equals(str)) && (!"INF".equals(str)));
              return Float.valueOf((1.0F / 1.0F));
            }
            while (!"NaN".equals(str));
            return Float.valueOf((0.0F / 0.0F));
          }
          while ((!"-Infinity".equals(str)) && (!"-INF".equals(str)));
          return Float.valueOf((1.0F / -1.0F));
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          throw paramDeserializationContext.weirdStringException(this._valueClass, "not a valid Float value");
        }
      }
      localJsonToken2 = JsonToken.VALUE_NULL;
      localFloat1 = null;
    }
    while (localJsonToken1 == localJsonToken2);
    throw paramDeserializationContext.mappingException(this._valueClass);
  }

  protected final float _parseFloatPrimitive(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    JsonToken localJsonToken1 = paramJsonParser.getCurrentToken();
    float f1;
    if ((localJsonToken1 == JsonToken.VALUE_NUMBER_INT) || (localJsonToken1 == JsonToken.VALUE_NUMBER_FLOAT))
      f1 = paramJsonParser.getFloatValue();
    JsonToken localJsonToken2;
    do
    {
      while (true)
      {
        return f1;
        if (localJsonToken1 != JsonToken.VALUE_STRING)
          break;
        String str = paramJsonParser.getText().trim();
        int i = str.length();
        f1 = 0.0F;
        if (i == 0)
          continue;
        switch (str.charAt(0))
        {
        default:
        case 'I':
        case 'N':
        case '-':
        }
        try
        {
          do
          {
            do
            {
              do
              {
                float f2 = Float.parseFloat(str);
                return f2;
              }
              while ((!"Infinity".equals(str)) && (!"INF".equals(str)));
              return (1.0F / 1.0F);
            }
            while (!"NaN".equals(str));
            return (0.0F / 0.0F);
          }
          while ((!"-Infinity".equals(str)) && (!"-INF".equals(str)));
          return (1.0F / -1.0F);
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          throw paramDeserializationContext.weirdStringException(this._valueClass, "not a valid float value");
        }
      }
      localJsonToken2 = JsonToken.VALUE_NULL;
      f1 = 0.0F;
    }
    while (localJsonToken1 == localJsonToken2);
    throw paramDeserializationContext.mappingException(this._valueClass);
  }

  protected final int _parseIntPrimitive(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    JsonToken localJsonToken1 = paramJsonParser.getCurrentToken();
    int i;
    if ((localJsonToken1 == JsonToken.VALUE_NUMBER_INT) || (localJsonToken1 == JsonToken.VALUE_NUMBER_FLOAT))
      i = paramJsonParser.getIntValue();
    label154: JsonToken localJsonToken2;
    do
    {
      while (true)
      {
        return i;
        if (localJsonToken1 != JsonToken.VALUE_STRING)
          break;
        String str = paramJsonParser.getText().trim();
        int j;
        long l;
        try
        {
          j = str.length();
          if (j <= 9)
            break label154;
          l = Long.parseLong(str);
          if ((l < -2147483648L) || (l > 2147483647L))
            throw paramDeserializationContext.weirdStringException(this._valueClass, "Overflow: numeric value (" + str + ") out of range of int (" + -2147483648 + " - " + 2147483647 + ")");
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          throw paramDeserializationContext.weirdStringException(this._valueClass, "not a valid int value");
        }
        return (int)l;
        i = 0;
        if (j == 0)
          continue;
        int k = NumberInput.parseInt(str);
        return k;
      }
      localJsonToken2 = JsonToken.VALUE_NULL;
      i = 0;
    }
    while (localJsonToken1 == localJsonToken2);
    throw paramDeserializationContext.mappingException(this._valueClass);
  }

  protected final Integer _parseInteger(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    JsonToken localJsonToken1 = paramJsonParser.getCurrentToken();
    Integer localInteger1;
    if ((localJsonToken1 == JsonToken.VALUE_NUMBER_INT) || (localJsonToken1 == JsonToken.VALUE_NUMBER_FLOAT))
      localInteger1 = Integer.valueOf(paramJsonParser.getIntValue());
    label164: JsonToken localJsonToken2;
    do
    {
      while (true)
      {
        return localInteger1;
        if (localJsonToken1 != JsonToken.VALUE_STRING)
          break;
        String str = paramJsonParser.getText().trim();
        int i;
        long l;
        try
        {
          i = str.length();
          if (i <= 9)
            break label164;
          l = Long.parseLong(str);
          if ((l < -2147483648L) || (l > 2147483647L))
            throw paramDeserializationContext.weirdStringException(this._valueClass, "Overflow: numeric value (" + str + ") out of range of Integer (" + -2147483648 + " - " + 2147483647 + ")");
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          throw paramDeserializationContext.weirdStringException(this._valueClass, "not a valid Integer value");
        }
        int j = (int)l;
        return Integer.valueOf(j);
        localInteger1 = null;
        if (i == 0)
          continue;
        Integer localInteger2 = Integer.valueOf(NumberInput.parseInt(str));
        return localInteger2;
      }
      localJsonToken2 = JsonToken.VALUE_NULL;
      localInteger1 = null;
    }
    while (localJsonToken1 == localJsonToken2);
    throw paramDeserializationContext.mappingException(this._valueClass);
  }

  protected final Long _parseLong(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    JsonToken localJsonToken1 = paramJsonParser.getCurrentToken();
    Long localLong1;
    if ((localJsonToken1 == JsonToken.VALUE_NUMBER_INT) || (localJsonToken1 == JsonToken.VALUE_NUMBER_FLOAT))
      localLong1 = Long.valueOf(paramJsonParser.getLongValue());
    JsonToken localJsonToken2;
    do
    {
      while (true)
      {
        return localLong1;
        if (localJsonToken1 != JsonToken.VALUE_STRING)
          break;
        String str = paramJsonParser.getText().trim();
        int i = str.length();
        localLong1 = null;
        if (i == 0)
          continue;
        try
        {
          Long localLong2 = Long.valueOf(NumberInput.parseLong(str));
          return localLong2;
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          throw paramDeserializationContext.weirdStringException(this._valueClass, "not a valid Long value");
        }
      }
      localJsonToken2 = JsonToken.VALUE_NULL;
      localLong1 = null;
    }
    while (localJsonToken1 == localJsonToken2);
    throw paramDeserializationContext.mappingException(this._valueClass);
  }

  protected final long _parseLongPrimitive(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    long l1 = 0L;
    JsonToken localJsonToken = paramJsonParser.getCurrentToken();
    if ((localJsonToken == JsonToken.VALUE_NUMBER_INT) || (localJsonToken == JsonToken.VALUE_NUMBER_FLOAT))
      l1 = paramJsonParser.getLongValue();
    do
      while (true)
      {
        return l1;
        if (localJsonToken != JsonToken.VALUE_STRING)
          break;
        String str = paramJsonParser.getText().trim();
        if (str.length() == 0)
          continue;
        try
        {
          long l2 = NumberInput.parseLong(str);
          return l2;
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          throw paramDeserializationContext.weirdStringException(this._valueClass, "not a valid long value");
        }
      }
    while (localJsonToken == JsonToken.VALUE_NULL);
    throw paramDeserializationContext.mappingException(this._valueClass);
  }

  protected final Short _parseShort(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    JsonToken localJsonToken = paramJsonParser.getCurrentToken();
    if (localJsonToken == JsonToken.VALUE_NULL)
      return null;
    if ((localJsonToken == JsonToken.VALUE_NUMBER_INT) || (localJsonToken == JsonToken.VALUE_NUMBER_FLOAT))
      return Short.valueOf(paramJsonParser.getShortValue());
    int i = _parseIntPrimitive(paramJsonParser, paramDeserializationContext);
    if ((i < -32768) || (i > 32767))
      throw paramDeserializationContext.weirdStringException(this._valueClass, "overflow, value can not be represented as 16-bit value");
    return Short.valueOf((short)i);
  }

  protected final short _parseShortPrimitive(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    int i = _parseIntPrimitive(paramJsonParser, paramDeserializationContext);
    if ((i < -32768) || (i > 32767))
      throw paramDeserializationContext.weirdStringException(this._valueClass, "overflow, value can not be represented as 16-bit value");
    return (short)i;
  }

  public Object deserializeWithType(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, TypeDeserializer paramTypeDeserializer)
    throws IOException, JsonProcessingException
  {
    return paramTypeDeserializer.deserializeTypedFromAny(paramJsonParser, paramDeserializationContext);
  }

  protected JsonDeserializer<Object> findDeserializer(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    return paramDeserializerProvider.findValueDeserializer(paramDeserializationConfig, paramJavaType, paramBeanProperty);
  }

  public Class<?> getValueClass()
  {
    return this._valueClass;
  }

  public JavaType getValueType()
  {
    return null;
  }

  protected void handleUnknownProperty(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Object paramObject, String paramString)
    throws IOException, JsonProcessingException
  {
    if (paramObject == null)
      paramObject = getValueClass();
    if (paramDeserializationContext.handleUnknownProperty(paramJsonParser, this, paramObject, paramString))
      return;
    reportUnknownProperty(paramDeserializationContext, paramObject, paramString);
    paramJsonParser.skipChildren();
  }

  protected boolean isDefaultSerializer(JsonDeserializer<?> paramJsonDeserializer)
  {
    return (paramJsonDeserializer != null) && (paramJsonDeserializer.getClass().getAnnotation(JacksonStdImpl.class) != null);
  }

  protected void reportUnknownProperty(DeserializationContext paramDeserializationContext, Object paramObject, String paramString)
    throws IOException, JsonProcessingException
  {
    if (paramDeserializationContext.isEnabled(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES))
      throw paramDeserializationContext.unknownFieldException(paramObject, paramString);
  }

  public static final class AtomicBooleanDeserializer extends StdScalarDeserializer<AtomicBoolean>
  {
    public AtomicBooleanDeserializer()
    {
      super();
    }

    public AtomicBoolean deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      return new AtomicBoolean(_parseBooleanPrimitive(paramJsonParser, paramDeserializationContext));
    }
  }

  public static class AtomicReferenceDeserializer extends StdScalarDeserializer<AtomicReference<?>>
    implements ResolvableDeserializer
  {
    protected final BeanProperty _property;
    protected final JavaType _referencedType;
    protected JsonDeserializer<?> _valueDeserializer;

    public AtomicReferenceDeserializer(JavaType paramJavaType, BeanProperty paramBeanProperty)
    {
      super();
      this._referencedType = paramJavaType;
      this._property = paramBeanProperty;
    }

    public AtomicReference<?> deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      return new AtomicReference(this._valueDeserializer.deserialize(paramJsonParser, paramDeserializationContext));
    }

    public void resolve(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider)
      throws JsonMappingException
    {
      this._valueDeserializer = paramDeserializerProvider.findValueDeserializer(paramDeserializationConfig, this._referencedType, this._property);
    }
  }

  @JacksonStdImpl
  public static class BigDecimalDeserializer extends StdScalarDeserializer<BigDecimal>
  {
    public BigDecimalDeserializer()
    {
      super();
    }

    public BigDecimal deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      JsonToken localJsonToken = paramJsonParser.getCurrentToken();
      if ((localJsonToken == JsonToken.VALUE_NUMBER_INT) || (localJsonToken == JsonToken.VALUE_NUMBER_FLOAT))
        return paramJsonParser.getDecimalValue();
      if (localJsonToken == JsonToken.VALUE_STRING)
      {
        String str = paramJsonParser.getText().trim();
        if (str.length() == 0)
          return null;
        try
        {
          BigDecimal localBigDecimal = new BigDecimal(str);
          return localBigDecimal;
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          throw paramDeserializationContext.weirdStringException(this._valueClass, "not a valid representation");
        }
      }
      throw paramDeserializationContext.mappingException(this._valueClass);
    }
  }

  @JacksonStdImpl
  public static class BigIntegerDeserializer extends StdScalarDeserializer<BigInteger>
  {
    public BigIntegerDeserializer()
    {
      super();
    }

    public BigInteger deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      JsonToken localJsonToken = paramJsonParser.getCurrentToken();
      if (localJsonToken == JsonToken.VALUE_NUMBER_INT)
        switch (StdDeserializer.1.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[paramJsonParser.getNumberType().ordinal()])
        {
        default:
        case 1:
        case 2:
        }
      String str;
      while (true)
      {
        str = paramJsonParser.getText().trim();
        if (str.length() != 0)
          break;
        return null;
        return BigInteger.valueOf(paramJsonParser.getLongValue());
        if (localJsonToken == JsonToken.VALUE_NUMBER_FLOAT)
          return paramJsonParser.getDecimalValue().toBigInteger();
        if (localJsonToken == JsonToken.VALUE_STRING)
          continue;
        throw paramDeserializationContext.mappingException(this._valueClass);
      }
      try
      {
        BigInteger localBigInteger = new BigInteger(str);
        return localBigInteger;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
      }
      throw paramDeserializationContext.weirdStringException(this._valueClass, "not a valid representation");
    }
  }

  @JacksonStdImpl
  public static final class BooleanDeserializer extends StdDeserializer.PrimitiveOrWrapperDeserializer<Boolean>
  {
    public BooleanDeserializer(Class<Boolean> paramClass, Boolean paramBoolean)
    {
      super(paramBoolean);
    }

    public Boolean deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      return _parseBoolean(paramJsonParser, paramDeserializationContext);
    }

    public Boolean deserializeWithType(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, TypeDeserializer paramTypeDeserializer)
      throws IOException, JsonProcessingException
    {
      return _parseBoolean(paramJsonParser, paramDeserializationContext);
    }
  }

  @JacksonStdImpl
  public static final class ByteDeserializer extends StdDeserializer.PrimitiveOrWrapperDeserializer<Byte>
  {
    public ByteDeserializer(Class<Byte> paramClass, Byte paramByte)
    {
      super(paramByte);
    }

    public Byte deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      int i = _parseIntPrimitive(paramJsonParser, paramDeserializationContext);
      if ((i < -128) || (i > 127))
        throw paramDeserializationContext.weirdStringException(this._valueClass, "overflow, value can not be represented as 8-bit value");
      return Byte.valueOf((byte)i);
    }
  }

  @JacksonStdImpl
  public static class CalendarDeserializer extends StdScalarDeserializer<Calendar>
  {
    Class<? extends Calendar> _calendarClass;

    public CalendarDeserializer()
    {
      this(null);
    }

    public CalendarDeserializer(Class<? extends Calendar> paramClass)
    {
      super();
      this._calendarClass = paramClass;
    }

    public Calendar deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      java.util.Date localDate = _parseDate(paramJsonParser, paramDeserializationContext);
      if (localDate == null)
        return null;
      if (this._calendarClass == null)
        return paramDeserializationContext.constructCalendar(localDate);
      try
      {
        Calendar localCalendar = (Calendar)this._calendarClass.newInstance();
        localCalendar.setTimeInMillis(localDate.getTime());
        return localCalendar;
      }
      catch (Exception localException)
      {
      }
      throw paramDeserializationContext.instantiationException(this._calendarClass, localException);
    }
  }

  @JacksonStdImpl
  public static final class CharacterDeserializer extends StdDeserializer.PrimitiveOrWrapperDeserializer<Character>
  {
    public CharacterDeserializer(Class<Character> paramClass, Character paramCharacter)
    {
      super(paramCharacter);
    }

    public Character deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      JsonToken localJsonToken = paramJsonParser.getCurrentToken();
      if (localJsonToken == JsonToken.VALUE_NUMBER_INT)
      {
        int i = paramJsonParser.getIntValue();
        if ((i >= 0) && (i <= 65535))
          return Character.valueOf((char)i);
      }
      else if (localJsonToken == JsonToken.VALUE_STRING)
      {
        String str = paramJsonParser.getText();
        if (str.length() == 1)
          return Character.valueOf(str.charAt(0));
      }
      throw paramDeserializationContext.mappingException(this._valueClass);
    }
  }

  @JacksonStdImpl
  public static final class ClassDeserializer extends StdScalarDeserializer<Class<?>>
  {
    public ClassDeserializer()
    {
      super();
    }

    public Class<?> deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      if (paramJsonParser.getCurrentToken() == JsonToken.VALUE_STRING)
        try
        {
          Class localClass = Class.forName(paramJsonParser.getText());
          return localClass;
        }
        catch (ClassNotFoundException localClassNotFoundException)
        {
          throw paramDeserializationContext.instantiationException(this._valueClass, localClassNotFoundException);
        }
      throw paramDeserializationContext.mappingException(this._valueClass);
    }
  }

  @JacksonStdImpl
  public static final class DoubleDeserializer extends StdDeserializer.PrimitiveOrWrapperDeserializer<Double>
  {
    public DoubleDeserializer(Class<Double> paramClass, Double paramDouble)
    {
      super(paramDouble);
    }

    public Double deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      return _parseDouble(paramJsonParser, paramDeserializationContext);
    }

    public Double deserializeWithType(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, TypeDeserializer paramTypeDeserializer)
      throws IOException, JsonProcessingException
    {
      return _parseDouble(paramJsonParser, paramDeserializationContext);
    }
  }

  @JacksonStdImpl
  public static final class FloatDeserializer extends StdDeserializer.PrimitiveOrWrapperDeserializer<Float>
  {
    public FloatDeserializer(Class<Float> paramClass, Float paramFloat)
    {
      super(paramFloat);
    }

    public Float deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      return _parseFloat(paramJsonParser, paramDeserializationContext);
    }
  }

  @JacksonStdImpl
  public static final class IntegerDeserializer extends StdDeserializer.PrimitiveOrWrapperDeserializer<Integer>
  {
    public IntegerDeserializer(Class<Integer> paramClass, Integer paramInteger)
    {
      super(paramInteger);
    }

    public Integer deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      return _parseInteger(paramJsonParser, paramDeserializationContext);
    }

    public Integer deserializeWithType(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, TypeDeserializer paramTypeDeserializer)
      throws IOException, JsonProcessingException
    {
      return _parseInteger(paramJsonParser, paramDeserializationContext);
    }
  }

  @JacksonStdImpl
  public static final class LongDeserializer extends StdDeserializer.PrimitiveOrWrapperDeserializer<Long>
  {
    public LongDeserializer(Class<Long> paramClass, Long paramLong)
    {
      super(paramLong);
    }

    public Long deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      return _parseLong(paramJsonParser, paramDeserializationContext);
    }
  }

  @JacksonStdImpl
  public static final class NumberDeserializer extends StdScalarDeserializer<Number>
  {
    public NumberDeserializer()
    {
      super();
    }

    public Number deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      JsonToken localJsonToken = paramJsonParser.getCurrentToken();
      if (localJsonToken == JsonToken.VALUE_NUMBER_INT)
      {
        if (paramDeserializationContext.isEnabled(DeserializationConfig.Feature.USE_BIG_INTEGER_FOR_INTS))
          return paramJsonParser.getBigIntegerValue();
        return paramJsonParser.getNumberValue();
      }
      if (localJsonToken == JsonToken.VALUE_NUMBER_FLOAT)
      {
        if (paramDeserializationContext.isEnabled(DeserializationConfig.Feature.USE_BIG_DECIMAL_FOR_FLOATS))
          return paramJsonParser.getDecimalValue();
        return Double.valueOf(paramJsonParser.getDoubleValue());
      }
      if (localJsonToken == JsonToken.VALUE_STRING)
      {
        String str = paramJsonParser.getText().trim();
        try
        {
          if (str.indexOf('.') < 0)
            break label135;
          if (paramDeserializationContext.isEnabled(DeserializationConfig.Feature.USE_BIG_DECIMAL_FOR_FLOATS))
          {
            BigDecimal localBigDecimal = new BigDecimal(str);
            return localBigDecimal;
          }
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          throw paramDeserializationContext.weirdStringException(this._valueClass, "not a valid number");
        }
        return new Double(str);
        label135: if (paramDeserializationContext.isEnabled(DeserializationConfig.Feature.USE_BIG_INTEGER_FOR_INTS))
          return new BigInteger(str);
        long l = Long.parseLong(str);
        if ((l <= 2147483647L) && (l >= -2147483648L))
          return Integer.valueOf((int)l);
        Long localLong = Long.valueOf(l);
        return localLong;
      }
      throw paramDeserializationContext.mappingException(this._valueClass);
    }

    public Object deserializeWithType(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, TypeDeserializer paramTypeDeserializer)
      throws IOException, JsonProcessingException
    {
      switch (StdDeserializer.1.$SwitchMap$org$codehaus$jackson$JsonToken[paramJsonParser.getCurrentToken().ordinal()])
      {
      default:
        return paramTypeDeserializer.deserializeTypedFromScalar(paramJsonParser, paramDeserializationContext);
      case 1:
      case 2:
      case 3:
      }
      return deserialize(paramJsonParser, paramDeserializationContext);
    }
  }

  protected static abstract class PrimitiveOrWrapperDeserializer<T> extends StdScalarDeserializer<T>
  {
    final T _nullValue;

    protected PrimitiveOrWrapperDeserializer(Class<T> paramClass, T paramT)
    {
      super();
      this._nullValue = paramT;
    }

    public final T getNullValue()
    {
      return this._nullValue;
    }
  }

  @JacksonStdImpl
  public static final class ShortDeserializer extends StdDeserializer.PrimitiveOrWrapperDeserializer<Short>
  {
    public ShortDeserializer(Class<Short> paramClass, Short paramShort)
    {
      super(paramShort);
    }

    public Short deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      return _parseShort(paramJsonParser, paramDeserializationContext);
    }
  }

  public static class SqlDateDeserializer extends StdScalarDeserializer<java.sql.Date>
  {
    public SqlDateDeserializer()
    {
      super();
    }

    public java.sql.Date deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      java.util.Date localDate = _parseDate(paramJsonParser, paramDeserializationContext);
      if (localDate == null)
        return null;
      return new java.sql.Date(localDate.getTime());
    }
  }

  public static class StackTraceElementDeserializer extends StdScalarDeserializer<StackTraceElement>
  {
    public StackTraceElementDeserializer()
    {
      super();
    }

    public StackTraceElement deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      if (paramJsonParser.getCurrentToken() == JsonToken.START_OBJECT)
      {
        String str1 = "";
        String str2 = "";
        String str3 = "";
        int i = -1;
        while (true)
        {
          JsonToken localJsonToken = paramJsonParser.nextValue();
          if (localJsonToken == JsonToken.END_OBJECT)
            break;
          String str4 = paramJsonParser.getCurrentName();
          if ("className".equals(str4))
          {
            str1 = paramJsonParser.getText();
            continue;
          }
          if ("fileName".equals(str4))
          {
            str3 = paramJsonParser.getText();
            continue;
          }
          if ("lineNumber".equals(str4))
          {
            if (localJsonToken.isNumeric())
            {
              i = paramJsonParser.getIntValue();
              continue;
            }
            throw JsonMappingException.from(paramJsonParser, "Non-numeric token (" + localJsonToken + ") for property 'lineNumber'");
          }
          if ("methodName".equals(str4))
          {
            str2 = paramJsonParser.getText();
            continue;
          }
          if ("nativeMethod".equals(str4))
            continue;
          handleUnknownProperty(paramJsonParser, paramDeserializationContext, this._valueClass, str4);
        }
        return new StackTraceElement(str1, str2, str3, i);
      }
      throw paramDeserializationContext.mappingException(this._valueClass);
    }
  }

  @JacksonStdImpl
  public static final class StringDeserializer extends StdScalarDeserializer<String>
  {
    public StringDeserializer()
    {
      super();
    }

    public String deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      JsonToken localJsonToken = paramJsonParser.getCurrentToken();
      if (localJsonToken == JsonToken.VALUE_STRING)
        return paramJsonParser.getText();
      if (localJsonToken == JsonToken.VALUE_EMBEDDED_OBJECT)
      {
        Object localObject = paramJsonParser.getEmbeddedObject();
        if (localObject == null)
          return null;
        if ((localObject instanceof byte[]))
          return Base64Variants.getDefaultVariant().encode((byte[])(byte[])localObject, false);
        return localObject.toString();
      }
      if (localJsonToken.isScalarValue())
        return paramJsonParser.getText();
      throw paramDeserializationContext.mappingException(this._valueClass);
    }

    public String deserializeWithType(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, TypeDeserializer paramTypeDeserializer)
      throws IOException, JsonProcessingException
    {
      return deserialize(paramJsonParser, paramDeserializationContext);
    }
  }

  @JacksonStdImpl
  public static class TokenBufferDeserializer extends StdScalarDeserializer<TokenBuffer>
  {
    public TokenBufferDeserializer()
    {
      super();
    }

    public TokenBuffer deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      TokenBuffer localTokenBuffer = new TokenBuffer(paramJsonParser.getCodec());
      localTokenBuffer.copyCurrentStructure(paramJsonParser);
      return localTokenBuffer;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.StdDeserializer
 * JD-Core Version:    0.6.0
 */