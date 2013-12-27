package org.codehaus.jackson.impl;

import java.io.IOException;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.util.ByteArrayBuilder;
import org.codehaus.jackson.util.TextBuffer;
import org.codehaus.jackson.util.VersionUtil;

public abstract class JsonParserBase extends JsonParserMinimalBase
{
  protected byte[] _binaryValue;
  protected ByteArrayBuilder _byteArrayBuilder = null;
  protected boolean _closed;
  protected long _currInputProcessed = 0L;
  protected int _currInputRow = 1;
  protected int _currInputRowStart = 0;
  protected int _inputEnd = 0;
  protected int _inputPtr = 0;
  protected final IOContext _ioContext;
  protected boolean _nameCopied = false;
  protected char[] _nameCopyBuffer = null;
  protected JsonToken _nextToken;
  protected JsonReadContext _parsingContext;
  protected final TextBuffer _textBuffer;
  protected int _tokenInputCol = 0;
  protected int _tokenInputRow = 1;
  protected long _tokenInputTotal = 0L;

  protected JsonParserBase(IOContext paramIOContext, int paramInt)
  {
    this._features = paramInt;
    this._ioContext = paramIOContext;
    this._textBuffer = paramIOContext.constructTextBuffer();
    this._parsingContext = JsonReadContext.createRootContext(this._tokenInputRow, this._tokenInputCol);
  }

  protected abstract void _closeInput()
    throws IOException;

  protected abstract byte[] _decodeBase64(Base64Variant paramBase64Variant)
    throws IOException, JsonParseException;

  protected abstract void _finishString()
    throws IOException, JsonParseException;

  public ByteArrayBuilder _getByteArrayBuilder()
  {
    if (this._byteArrayBuilder == null)
      this._byteArrayBuilder = new ByteArrayBuilder();
    while (true)
    {
      return this._byteArrayBuilder;
      this._byteArrayBuilder.reset();
    }
  }

  protected void _handleEOF()
    throws JsonParseException
  {
    if (!this._parsingContext.inRoot())
      _reportInvalidEOF(": expected close marker for " + this._parsingContext.getTypeDesc() + " (from " + this._parsingContext.getStartLocation(this._ioContext.getSourceReference()) + ")");
  }

  protected void _releaseBuffers()
    throws IOException
  {
    this._textBuffer.releaseBuffers();
    char[] arrayOfChar = this._nameCopyBuffer;
    if (arrayOfChar != null)
    {
      this._nameCopyBuffer = null;
      this._ioContext.releaseNameCopyBuffer(arrayOfChar);
    }
  }

  protected void _reportMismatchedEndMarker(int paramInt, char paramChar)
    throws JsonParseException
  {
    String str = "" + this._parsingContext.getStartLocation(this._ioContext.getSourceReference());
    _reportError("Unexpected close marker '" + (char)paramInt + "': expected '" + paramChar + "' (for " + this._parsingContext.getTypeDesc() + " starting at " + str + ")");
  }

  public void close()
    throws IOException
  {
    if (!this._closed)
      this._closed = true;
    try
    {
      _closeInput();
      return;
    }
    finally
    {
      _releaseBuffers();
    }
    throw localObject;
  }

  public JsonLocation getCurrentLocation()
  {
    int i = 1 + (this._inputPtr - this._currInputRowStart);
    return new JsonLocation(this._ioContext.getSourceReference(), this._currInputProcessed + this._inputPtr - 1L, this._currInputRow, i);
  }

  public String getCurrentName()
    throws IOException, JsonParseException
  {
    if ((this._currToken == JsonToken.START_OBJECT) || (this._currToken == JsonToken.START_ARRAY))
      return this._parsingContext.getParent().getCurrentName();
    return this._parsingContext.getCurrentName();
  }

  public JsonReadContext getParsingContext()
  {
    return this._parsingContext;
  }

  public final long getTokenCharacterOffset()
  {
    return this._tokenInputTotal;
  }

  public final int getTokenColumnNr()
  {
    return 1 + this._tokenInputCol;
  }

  public final int getTokenLineNr()
  {
    return this._tokenInputRow;
  }

  public JsonLocation getTokenLocation()
  {
    return new JsonLocation(this._ioContext.getSourceReference(), getTokenCharacterOffset(), getTokenLineNr(), getTokenColumnNr());
  }

  public boolean hasTextCharacters()
  {
    if (this._currToken != null);
    switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()])
    {
    default:
      return false;
    case 1:
      return this._nameCopied;
    case 2:
    }
    return true;
  }

  public boolean isClosed()
  {
    return this._closed;
  }

  protected abstract boolean loadMore()
    throws IOException;

  protected final void loadMoreGuaranteed()
    throws IOException
  {
    if (!loadMore())
      _reportInvalidEOF();
  }

  public Version version()
  {
    return VersionUtil.versionFor(getClass());
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.JsonParserBase
 * JD-Core Version:    0.6.0
 */