package org.codehaus.jackson.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.io.IOContext;

public abstract class ReaderBasedParserBase extends JsonNumericParserBase
{
  protected char[] _inputBuffer;
  protected Reader _reader;

  protected ReaderBasedParserBase(IOContext paramIOContext, int paramInt, Reader paramReader)
  {
    super(paramIOContext, paramInt);
    this._reader = paramReader;
    this._inputBuffer = paramIOContext.allocTokenBuffer();
  }

  protected void _closeInput()
    throws IOException
  {
    if (this._reader != null)
    {
      if ((this._ioContext.isResourceManaged()) || (isEnabled(JsonParser.Feature.AUTO_CLOSE_SOURCE)))
        this._reader.close();
      this._reader = null;
    }
  }

  protected final boolean _matchToken(String paramString, int paramInt)
    throws IOException, JsonParseException
  {
    int i = paramString.length();
    do
    {
      if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
        _reportInvalidEOFInValue();
      if (this._inputBuffer[this._inputPtr] != paramString.charAt(paramInt))
        _reportInvalidToken(paramString.substring(0, paramInt), "'null', 'true', 'false' or NaN");
      this._inputPtr = (1 + this._inputPtr);
      paramInt++;
    }
    while (paramInt < i);
    if ((this._inputPtr >= this._inputEnd) && (!loadMore()));
    do
      return true;
    while (!Character.isJavaIdentifierPart(this._inputBuffer[this._inputPtr]));
    this._inputPtr = (1 + this._inputPtr);
    _reportInvalidToken(paramString.substring(0, paramInt), "'null', 'true', 'false' or NaN");
    return true;
  }

  protected void _releaseBuffers()
    throws IOException
  {
    super._releaseBuffers();
    char[] arrayOfChar = this._inputBuffer;
    if (arrayOfChar != null)
    {
      this._inputBuffer = null;
      this._ioContext.releaseTokenBuffer(arrayOfChar);
    }
  }

  protected void _reportInvalidToken(String paramString1, String paramString2)
    throws IOException, JsonParseException
  {
    StringBuilder localStringBuilder = new StringBuilder(paramString1);
    while (true)
    {
      if ((this._inputPtr >= this._inputEnd) && (!loadMore()));
      char c;
      do
      {
        _reportError("Unrecognized token '" + localStringBuilder.toString() + "': was expecting ");
        return;
        c = this._inputBuffer[this._inputPtr];
      }
      while (!Character.isJavaIdentifierPart(c));
      this._inputPtr = (1 + this._inputPtr);
      localStringBuilder.append(c);
    }
  }

  public Object getInputSource()
  {
    return this._reader;
  }

  protected char getNextChar(String paramString)
    throws IOException, JsonParseException
  {
    if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
      _reportInvalidEOF(paramString);
    char[] arrayOfChar = this._inputBuffer;
    int i = this._inputPtr;
    this._inputPtr = (i + 1);
    return arrayOfChar[i];
  }

  protected final boolean loadMore()
    throws IOException
  {
    this._currInputProcessed += this._inputEnd;
    this._currInputRowStart -= this._inputEnd;
    Reader localReader = this._reader;
    int i = 0;
    int j;
    if (localReader != null)
    {
      j = this._reader.read(this._inputBuffer, 0, this._inputBuffer.length);
      if (j <= 0)
        break label74;
      this._inputPtr = 0;
      this._inputEnd = j;
      i = 1;
    }
    label74: 
    do
    {
      return i;
      _closeInput();
      i = 0;
    }
    while (j != 0);
    throw new IOException("Reader returned 0 characters when trying to read " + this._inputEnd);
  }

  public int releaseBuffered(Writer paramWriter)
    throws IOException
  {
    int i = this._inputEnd - this._inputPtr;
    if (i < 1)
      return 0;
    int j = this._inputPtr;
    paramWriter.write(this._inputBuffer, j, i);
    return i;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.ReaderBasedParserBase
 * JD-Core Version:    0.6.0
 */