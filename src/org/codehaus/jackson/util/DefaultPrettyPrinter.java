package org.codehaus.jackson.util;

import java.io.IOException;
import java.util.Arrays;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.PrettyPrinter;
import org.codehaus.jackson.impl.Indenter;

public class DefaultPrettyPrinter
  implements PrettyPrinter
{
  protected Indenter _arrayIndenter = new FixedSpaceIndenter();
  protected int _nesting = 0;
  protected Indenter _objectIndenter = new Lf2SpacesIndenter();
  protected boolean _spacesInObjectEntries = true;

  public void beforeArrayValues(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException
  {
    this._arrayIndenter.writeIndentation(paramJsonGenerator, this._nesting);
  }

  public void beforeObjectEntries(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException
  {
    this._objectIndenter.writeIndentation(paramJsonGenerator, this._nesting);
  }

  public void indentArraysWith(Indenter paramIndenter)
  {
    if (paramIndenter == null)
      paramIndenter = new NopIndenter();
    this._arrayIndenter = paramIndenter;
  }

  public void indentObjectsWith(Indenter paramIndenter)
  {
    if (paramIndenter == null)
      paramIndenter = new NopIndenter();
    this._objectIndenter = paramIndenter;
  }

  public void spacesInObjectEntries(boolean paramBoolean)
  {
    this._spacesInObjectEntries = paramBoolean;
  }

  public void writeArrayValueSeparator(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException
  {
    paramJsonGenerator.writeRaw(',');
    this._arrayIndenter.writeIndentation(paramJsonGenerator, this._nesting);
  }

  public void writeEndArray(JsonGenerator paramJsonGenerator, int paramInt)
    throws IOException, JsonGenerationException
  {
    if (!this._arrayIndenter.isInline())
      this._nesting = (-1 + this._nesting);
    if (paramInt > 0)
      this._arrayIndenter.writeIndentation(paramJsonGenerator, this._nesting);
    while (true)
    {
      paramJsonGenerator.writeRaw(']');
      return;
      paramJsonGenerator.writeRaw(' ');
    }
  }

  public void writeEndObject(JsonGenerator paramJsonGenerator, int paramInt)
    throws IOException, JsonGenerationException
  {
    if (!this._objectIndenter.isInline())
      this._nesting = (-1 + this._nesting);
    if (paramInt > 0)
      this._objectIndenter.writeIndentation(paramJsonGenerator, this._nesting);
    while (true)
    {
      paramJsonGenerator.writeRaw('}');
      return;
      paramJsonGenerator.writeRaw(' ');
    }
  }

  public void writeObjectEntrySeparator(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException
  {
    paramJsonGenerator.writeRaw(',');
    this._objectIndenter.writeIndentation(paramJsonGenerator, this._nesting);
  }

  public void writeObjectFieldValueSeparator(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException
  {
    if (this._spacesInObjectEntries)
    {
      paramJsonGenerator.writeRaw(" : ");
      return;
    }
    paramJsonGenerator.writeRaw(':');
  }

  public void writeRootValueSeparator(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException
  {
    paramJsonGenerator.writeRaw(' ');
  }

  public void writeStartArray(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException
  {
    if (!this._arrayIndenter.isInline())
      this._nesting = (1 + this._nesting);
    paramJsonGenerator.writeRaw('[');
  }

  public void writeStartObject(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException
  {
    paramJsonGenerator.writeRaw('{');
    if (!this._objectIndenter.isInline())
      this._nesting = (1 + this._nesting);
  }

  public static class FixedSpaceIndenter
    implements Indenter
  {
    public boolean isInline()
    {
      return true;
    }

    public void writeIndentation(JsonGenerator paramJsonGenerator, int paramInt)
      throws IOException, JsonGenerationException
    {
      paramJsonGenerator.writeRaw(' ');
    }
  }

  public static class Lf2SpacesIndenter
    implements Indenter
  {
    static final char[] SPACES;
    static final int SPACE_COUNT = 64;
    static final String SYSTEM_LINE_SEPARATOR;

    static
    {
      try
      {
        String str2 = System.getProperty("line.separator");
        str1 = str2;
        if (str1 == null)
          str1 = "\n";
        SYSTEM_LINE_SEPARATOR = str1;
        SPACES = new char[64];
        Arrays.fill(SPACES, ' ');
        return;
      }
      catch (Throwable localThrowable)
      {
        while (true)
          String str1 = null;
      }
    }

    public boolean isInline()
    {
      return false;
    }

    public void writeIndentation(JsonGenerator paramJsonGenerator, int paramInt)
      throws IOException, JsonGenerationException
    {
      paramJsonGenerator.writeRaw(SYSTEM_LINE_SEPARATOR);
      int i = paramInt + paramInt;
      while (i > 64)
      {
        paramJsonGenerator.writeRaw(SPACES, 0, 64);
        i -= SPACES.length;
      }
      paramJsonGenerator.writeRaw(SPACES, 0, i);
    }
  }

  public static class NopIndenter
    implements Indenter
  {
    public boolean isInline()
    {
      return true;
    }

    public void writeIndentation(JsonGenerator paramJsonGenerator, int paramInt)
    {
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.util.DefaultPrettyPrinter
 * JD-Core Version:    0.6.0
 */