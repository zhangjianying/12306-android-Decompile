package org.codehaus.jackson.map.deser;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;

public abstract class FromStringDeserializer<T> extends StdScalarDeserializer<T>
{
  protected FromStringDeserializer(Class<?> paramClass)
  {
    super(paramClass);
  }

  public static Iterable<FromStringDeserializer<?>> all()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new UUIDDeserializer());
    localArrayList.add(new URLDeserializer());
    localArrayList.add(new URIDeserializer());
    localArrayList.add(new CurrencyDeserializer());
    localArrayList.add(new PatternDeserializer());
    localArrayList.add(new LocaleDeserializer());
    localArrayList.add(new InetAddressDeserializer());
    localArrayList.add(new TimeZoneDeserializer());
    return localArrayList;
  }

  protected abstract T _deserialize(String paramString, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException;

  protected T _deserializeEmbedded(Object paramObject, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    throw paramDeserializationContext.mappingException("Don't know how to convert embedded Object of type " + paramObject.getClass().getName() + " into " + this._valueClass.getName());
  }

  public final T deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    String str;
    Object localObject2;
    if (paramJsonParser.getCurrentToken() == JsonToken.VALUE_STRING)
    {
      str = paramJsonParser.getText().trim();
      int i = str.length();
      localObject2 = null;
      if (i != 0);
    }
    while (true)
    {
      return localObject2;
      try
      {
        Object localObject3 = _deserialize(str, paramDeserializationContext);
        localObject2 = localObject3;
        if (localObject2 != null)
          continue;
        label55: throw paramDeserializationContext.weirdStringException(this._valueClass, "not a valid textual representation");
        if (paramJsonParser.getCurrentToken() == JsonToken.VALUE_EMBEDDED_OBJECT)
        {
          Object localObject1 = paramJsonParser.getEmbeddedObject();
          localObject2 = null;
          if (localObject1 == null)
            continue;
          if (this._valueClass.isAssignableFrom(localObject1.getClass()))
            return localObject1;
          return _deserializeEmbedded(localObject1, paramDeserializationContext);
        }
        throw paramDeserializationContext.mappingException(this._valueClass);
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        break label55;
      }
    }
  }

  public static class CurrencyDeserializer extends FromStringDeserializer<Currency>
  {
    public CurrencyDeserializer()
    {
      super();
    }

    protected Currency _deserialize(String paramString, DeserializationContext paramDeserializationContext)
      throws IllegalArgumentException
    {
      return Currency.getInstance(paramString);
    }
  }

  protected static class InetAddressDeserializer extends FromStringDeserializer<InetAddress>
  {
    public InetAddressDeserializer()
    {
      super();
    }

    protected InetAddress _deserialize(String paramString, DeserializationContext paramDeserializationContext)
      throws IOException
    {
      return InetAddress.getByName(paramString);
    }
  }

  protected static class LocaleDeserializer extends FromStringDeserializer<Locale>
  {
    public LocaleDeserializer()
    {
      super();
    }

    protected Locale _deserialize(String paramString, DeserializationContext paramDeserializationContext)
      throws IOException
    {
      int i = paramString.indexOf('_');
      if (i < 0)
        return new Locale(paramString);
      String str1 = paramString.substring(0, i);
      String str2 = paramString.substring(i + 1);
      int j = str2.indexOf('_');
      if (j < 0)
        return new Locale(str1, str2);
      return new Locale(str1, str2.substring(0, j), str2.substring(j + 1));
    }
  }

  public static class PatternDeserializer extends FromStringDeserializer<Pattern>
  {
    public PatternDeserializer()
    {
      super();
    }

    protected Pattern _deserialize(String paramString, DeserializationContext paramDeserializationContext)
      throws IllegalArgumentException
    {
      return Pattern.compile(paramString);
    }
  }

  protected static class TimeZoneDeserializer extends FromStringDeserializer<TimeZone>
  {
    public TimeZoneDeserializer()
    {
      super();
    }

    protected TimeZone _deserialize(String paramString, DeserializationContext paramDeserializationContext)
      throws IOException
    {
      return TimeZone.getTimeZone(paramString);
    }
  }

  public static class URIDeserializer extends FromStringDeserializer<URI>
  {
    public URIDeserializer()
    {
      super();
    }

    protected URI _deserialize(String paramString, DeserializationContext paramDeserializationContext)
      throws IllegalArgumentException
    {
      return URI.create(paramString);
    }
  }

  public static class URLDeserializer extends FromStringDeserializer<URL>
  {
    public URLDeserializer()
    {
      super();
    }

    protected URL _deserialize(String paramString, DeserializationContext paramDeserializationContext)
      throws IOException
    {
      return new URL(paramString);
    }
  }

  public static class UUIDDeserializer extends FromStringDeserializer<UUID>
  {
    public UUIDDeserializer()
    {
      super();
    }

    protected UUID _deserialize(String paramString, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      return UUID.fromString(paramString);
    }

    protected UUID _deserializeEmbedded(Object paramObject, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      if ((paramObject instanceof byte[]))
      {
        byte[] arrayOfByte = (byte[])(byte[])paramObject;
        if (arrayOfByte.length != 16)
          paramDeserializationContext.mappingException("Can only construct UUIDs from 16 byte arrays; got " + arrayOfByte.length + " bytes");
        DataInputStream localDataInputStream = new DataInputStream(new ByteArrayInputStream(arrayOfByte));
        return new UUID(localDataInputStream.readLong(), localDataInputStream.readLong());
      }
      super._deserializeEmbedded(paramObject, paramDeserializationContext);
      return null;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.FromStringDeserializer
 * JD-Core Version:    0.6.0
 */