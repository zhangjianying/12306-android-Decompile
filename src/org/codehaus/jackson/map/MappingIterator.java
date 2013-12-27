package org.codehaus.jackson.map;

import java.io.IOException;
import java.util.Iterator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.type.JavaType;

public class MappingIterator<T>
  implements Iterator<T>
{
  protected static final MappingIterator<?> EMPTY_ITERATOR = new MappingIterator(null, null, null, null);
  protected final DeserializationContext _context;
  protected final JsonDeserializer<T> _deserializer;
  protected final JsonParser _parser;
  protected final JavaType _type;

  protected MappingIterator(JavaType paramJavaType, JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, JsonDeserializer<?> paramJsonDeserializer)
  {
    this._type = paramJavaType;
    this._parser = paramJsonParser;
    this._context = paramDeserializationContext;
    this._deserializer = paramJsonDeserializer;
    if ((paramJsonParser != null) && (paramJsonParser.getCurrentToken() == JsonToken.START_ARRAY) && (!paramJsonParser.getParsingContext().inRoot()))
      paramJsonParser.clearCurrentToken();
  }

  protected static <T> MappingIterator<T> emptyIterator()
  {
    return EMPTY_ITERATOR;
  }

  public boolean hasNext()
  {
    try
    {
      boolean bool = hasNextValue();
      return bool;
    }
    catch (JsonMappingException localJsonMappingException)
    {
      throw new RuntimeJsonMappingException(localJsonMappingException.getMessage(), localJsonMappingException);
    }
    catch (IOException localIOException)
    {
    }
    throw new RuntimeException(localIOException.getMessage(), localIOException);
  }

  public boolean hasNextValue()
    throws IOException
  {
    if (this._parser == null);
    JsonToken localJsonToken;
    do
    {
      return false;
      if (this._parser.getCurrentToken() != null)
        break;
      localJsonToken = this._parser.nextToken();
      if (localJsonToken != null)
        continue;
      this._parser.close();
      return false;
    }
    while (localJsonToken == JsonToken.END_ARRAY);
    return true;
  }

  public T next()
  {
    try
    {
      Object localObject = nextValue();
      return localObject;
    }
    catch (JsonMappingException localJsonMappingException)
    {
      throw new RuntimeJsonMappingException(localJsonMappingException.getMessage(), localJsonMappingException);
    }
    catch (IOException localIOException)
    {
    }
    throw new RuntimeException(localIOException.getMessage(), localIOException);
  }

  public T nextValue()
    throws IOException
  {
    Object localObject = this._deserializer.deserialize(this._parser, this._context);
    this._parser.clearCurrentToken();
    return localObject;
  }

  public void remove()
  {
    throw new UnsupportedOperationException();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.MappingIterator
 * JD-Core Version:    0.6.0
 */