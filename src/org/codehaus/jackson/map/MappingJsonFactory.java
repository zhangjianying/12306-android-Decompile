package org.codehaus.jackson.map;

import java.io.IOException;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.format.InputAccessor;
import org.codehaus.jackson.format.MatchStrength;

public class MappingJsonFactory extends JsonFactory
{
  public MappingJsonFactory()
  {
    this(null);
  }

  public MappingJsonFactory(ObjectMapper paramObjectMapper)
  {
    super(paramObjectMapper);
    if (paramObjectMapper == null)
      setCodec(new ObjectMapper(this));
  }

  public final ObjectMapper getCodec()
  {
    return (ObjectMapper)this._objectCodec;
  }

  public String getFormatName()
  {
    return "JSON";
  }

  public MatchStrength hasFormat(InputAccessor paramInputAccessor)
    throws IOException
  {
    return hasJSONFormat(paramInputAccessor);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.MappingJsonFactory
 * JD-Core Version:    0.6.0
 */