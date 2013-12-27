package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.util.HashSet;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.deser.impl.BeanPropertyMap;
import org.codehaus.jackson.type.JavaType;

public class ThrowableDeserializer extends BeanDeserializer
{
  protected static final String PROP_NAME_MESSAGE = "message";

  public ThrowableDeserializer(BeanDeserializer paramBeanDeserializer)
  {
    super(paramBeanDeserializer);
  }

  public Object deserializeFromObject(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    Object localObject;
    if (this._propertyBasedCreator != null)
      localObject = _deserializeUsingPropertyBased(paramJsonParser, paramDeserializationContext);
    while (true)
    {
      return localObject;
      if (this._delegatingCreator != null)
        return this._delegatingCreator.deserialize(paramJsonParser, paramDeserializationContext);
      if (this._beanType.isAbstract())
        throw JsonMappingException.from(paramJsonParser, "Can not instantiate abstract type " + this._beanType + " (need to add/enable type information?)");
      if (this._stringCreator == null)
        throw new JsonMappingException("Can not deserialize Throwable of type " + this._beanType + " without having either single-String-arg constructor; or explicit @JsonCreator");
      localObject = null;
      Object[] arrayOfObject = null;
      int i = 0;
      if (paramJsonParser.getCurrentToken() != JsonToken.END_OBJECT)
      {
        String str = paramJsonParser.getCurrentName();
        SettableBeanProperty localSettableBeanProperty = this._beanProperties.find(str);
        paramJsonParser.nextToken();
        if (localSettableBeanProperty != null)
          if (localObject != null)
            localSettableBeanProperty.deserializeAndSet(paramJsonParser, paramDeserializationContext, localObject);
        while (true)
        {
          paramJsonParser.nextToken();
          break;
          if (arrayOfObject == null)
          {
            int i2 = this._beanProperties.size();
            arrayOfObject = new Object[i2 + i2];
          }
          int i1 = i + 1;
          arrayOfObject[i] = localSettableBeanProperty;
          i = i1 + 1;
          arrayOfObject[i1] = localSettableBeanProperty.deserialize(paramJsonParser, paramDeserializationContext);
          continue;
          if ("message".equals(str))
          {
            localObject = this._stringCreator.construct(paramJsonParser.getText());
            if (arrayOfObject == null)
              continue;
            int m = 0;
            int n = i;
            while (m < n)
            {
              ((SettableBeanProperty)arrayOfObject[m]).set(localObject, arrayOfObject[(m + 1)]);
              m += 2;
            }
            arrayOfObject = null;
            continue;
          }
          if ((this._ignorableProps != null) && (this._ignorableProps.contains(str)))
          {
            paramJsonParser.skipChildren();
            continue;
          }
          if (this._anySetter != null)
          {
            this._anySetter.deserializeAndSet(paramJsonParser, paramDeserializationContext, localObject, str);
            continue;
          }
          handleUnknownProperty(paramJsonParser, paramDeserializationContext, localObject, str);
        }
      }
      if (localObject != null)
        continue;
      localObject = this._stringCreator.construct(null);
      if (arrayOfObject == null)
        continue;
      int j = 0;
      int k = i;
      while (j < k)
      {
        ((SettableBeanProperty)arrayOfObject[j]).set(localObject, arrayOfObject[(j + 1)]);
        j += 2;
      }
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.ThrowableDeserializer
 * JD-Core Version:    0.6.0
 */