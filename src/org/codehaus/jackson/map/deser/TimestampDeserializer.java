package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;

public class TimestampDeserializer extends StdScalarDeserializer<Timestamp>
{
  public TimestampDeserializer()
  {
    super(Timestamp.class);
  }

  public Timestamp deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    return new Timestamp(_parseDate(paramJsonParser, paramDeserializationContext).getTime());
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.TimestampDeserializer
 * JD-Core Version:    0.6.0
 */