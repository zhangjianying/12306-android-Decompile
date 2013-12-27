package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.util.ObjectBuffer;

@JacksonStdImpl
public class UntypedObjectDeserializer extends StdDeserializer<Object>
{
  public UntypedObjectDeserializer()
  {
    super(Object.class);
  }

  public Object deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[paramJsonParser.getCurrentToken().ordinal()])
    {
    default:
      throw paramDeserializationContext.mappingException(Object.class);
    case 1:
      return paramJsonParser.getText();
    case 2:
      if (paramDeserializationContext.isEnabled(DeserializationConfig.Feature.USE_BIG_INTEGER_FOR_INTS))
        return paramJsonParser.getBigIntegerValue();
      return paramJsonParser.getNumberValue();
    case 3:
      if (paramDeserializationContext.isEnabled(DeserializationConfig.Feature.USE_BIG_DECIMAL_FOR_FLOATS))
        return paramJsonParser.getDecimalValue();
      return Double.valueOf(paramJsonParser.getDoubleValue());
    case 4:
      return Boolean.TRUE;
    case 5:
      return Boolean.FALSE;
    case 6:
      return paramJsonParser.getEmbeddedObject();
    case 7:
      return null;
    case 8:
      return mapArray(paramJsonParser, paramDeserializationContext);
    case 9:
    case 10:
    }
    return mapObject(paramJsonParser, paramDeserializationContext);
  }

  public Object deserializeWithType(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, TypeDeserializer paramTypeDeserializer)
    throws IOException, JsonProcessingException
  {
    JsonToken localJsonToken = paramJsonParser.getCurrentToken();
    switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[localJsonToken.ordinal()])
    {
    default:
      throw paramDeserializationContext.mappingException(Object.class);
    case 8:
    case 9:
    case 10:
      return paramTypeDeserializer.deserializeTypedFromAny(paramJsonParser, paramDeserializationContext);
    case 1:
      return paramJsonParser.getText();
    case 2:
      if (paramDeserializationContext.isEnabled(DeserializationConfig.Feature.USE_BIG_INTEGER_FOR_INTS))
        return paramJsonParser.getBigIntegerValue();
      return Integer.valueOf(paramJsonParser.getIntValue());
    case 3:
      if (paramDeserializationContext.isEnabled(DeserializationConfig.Feature.USE_BIG_DECIMAL_FOR_FLOATS))
        return paramJsonParser.getDecimalValue();
      return Double.valueOf(paramJsonParser.getDoubleValue());
    case 4:
      return Boolean.TRUE;
    case 5:
      return Boolean.FALSE;
    case 6:
      return paramJsonParser.getEmbeddedObject();
    case 7:
    }
    return null;
  }

  protected List<Object> mapArray(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    if (paramJsonParser.nextToken() == JsonToken.END_ARRAY)
      return new ArrayList(4);
    ObjectBuffer localObjectBuffer = paramDeserializationContext.leaseObjectBuffer();
    Object[] arrayOfObject = localObjectBuffer.resetAndStart();
    int i = 0;
    int j = 0;
    while (true)
    {
      Object localObject = deserialize(paramJsonParser, paramDeserializationContext);
      j++;
      if (i >= arrayOfObject.length)
      {
        arrayOfObject = localObjectBuffer.appendCompletedChunk(arrayOfObject);
        i = 0;
      }
      int k = i + 1;
      arrayOfObject[i] = localObject;
      if (paramJsonParser.nextToken() == JsonToken.END_ARRAY)
      {
        ArrayList localArrayList = new ArrayList(1 + (j + (j >> 3)));
        localObjectBuffer.completeAndClearBuffer(arrayOfObject, k, localArrayList);
        return localArrayList;
      }
      i = k;
    }
  }

  protected Map<String, Object> mapObject(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    JsonToken localJsonToken = paramJsonParser.getCurrentToken();
    if (localJsonToken == JsonToken.START_OBJECT)
      localJsonToken = paramJsonParser.nextToken();
    if (localJsonToken != JsonToken.FIELD_NAME)
      return new LinkedHashMap(4);
    String str1 = paramJsonParser.getText();
    paramJsonParser.nextToken();
    Object localObject1 = deserialize(paramJsonParser, paramDeserializationContext);
    if (paramJsonParser.nextToken() != JsonToken.FIELD_NAME)
    {
      LinkedHashMap localLinkedHashMap1 = new LinkedHashMap(4);
      localLinkedHashMap1.put(str1, localObject1);
      return localLinkedHashMap1;
    }
    String str2 = paramJsonParser.getText();
    paramJsonParser.nextToken();
    Object localObject2 = deserialize(paramJsonParser, paramDeserializationContext);
    if (paramJsonParser.nextToken() != JsonToken.FIELD_NAME)
    {
      LinkedHashMap localLinkedHashMap2 = new LinkedHashMap(4);
      localLinkedHashMap2.put(str1, localObject1);
      localLinkedHashMap2.put(str2, localObject2);
      return localLinkedHashMap2;
    }
    LinkedHashMap localLinkedHashMap3 = new LinkedHashMap();
    localLinkedHashMap3.put(str1, localObject1);
    localLinkedHashMap3.put(str2, localObject2);
    do
    {
      String str3 = paramJsonParser.getText();
      paramJsonParser.nextToken();
      localLinkedHashMap3.put(str3, deserialize(paramJsonParser, paramDeserializationContext));
    }
    while (paramJsonParser.nextToken() != JsonToken.END_OBJECT);
    return localLinkedHashMap3;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.UntypedObjectDeserializer
 * JD-Core Version:    0.6.0
 */