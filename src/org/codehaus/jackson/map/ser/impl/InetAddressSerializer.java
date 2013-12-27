package org.codehaus.jackson.map.ser.impl;

import java.io.IOException;
import java.net.InetAddress;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.ser.ScalarSerializerBase;

public class InetAddressSerializer extends ScalarSerializerBase<InetAddress>
{
  public static final InetAddressSerializer instance = new InetAddressSerializer();

  public InetAddressSerializer()
  {
    super(InetAddress.class);
  }

  public void serialize(InetAddress paramInetAddress, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonGenerationException
  {
    String str = paramInetAddress.toString().trim();
    int i = str.indexOf('/');
    if (i >= 0)
      if (i != 0)
        break label43;
    label43: for (str = str.substring(1); ; str = str.substring(0, i))
    {
      paramJsonGenerator.writeString(str);
      return;
    }
  }

  public void serializeWithType(InetAddress paramInetAddress, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer)
    throws IOException, JsonGenerationException
  {
    paramTypeSerializer.writeTypePrefixForScalar(paramInetAddress, paramJsonGenerator, InetAddress.class);
    serialize(paramInetAddress, paramJsonGenerator, paramSerializerProvider);
    paramTypeSerializer.writeTypeSuffixForScalar(paramInetAddress, paramJsonGenerator);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.impl.InetAddressSerializer
 * JD-Core Version:    0.6.0
 */