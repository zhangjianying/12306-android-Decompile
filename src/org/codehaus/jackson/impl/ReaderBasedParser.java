package org.codehaus.jackson.impl;

import java.io.IOException;
import java.io.Reader;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.sym.CharsToNameCanonicalizer;
import org.codehaus.jackson.util.ByteArrayBuilder;
import org.codehaus.jackson.util.CharTypes;
import org.codehaus.jackson.util.TextBuffer;

public final class ReaderBasedParser extends ReaderBasedNumericParser
{
  protected ObjectCodec _objectCodec;
  protected final CharsToNameCanonicalizer _symbols;
  protected boolean _tokenIncomplete = false;

  public ReaderBasedParser(IOContext paramIOContext, int paramInt, Reader paramReader, ObjectCodec paramObjectCodec, CharsToNameCanonicalizer paramCharsToNameCanonicalizer)
  {
    super(paramIOContext, paramInt, paramReader);
    this._objectCodec = paramObjectCodec;
    this._symbols = paramCharsToNameCanonicalizer;
  }

  private final int _decodeBase64Escape(Base64Variant paramBase64Variant, char paramChar, int paramInt)
    throws IOException, JsonParseException
  {
    if (paramChar != '\\')
      throw reportInvalidChar(paramBase64Variant, paramChar, paramInt);
    char c = _decodeEscaped();
    int i;
    if ((c <= ' ') && (paramInt == 0))
      i = -1;
    do
    {
      return i;
      i = paramBase64Variant.decodeBase64Char(c);
    }
    while (i >= 0);
    throw reportInvalidChar(paramBase64Variant, c, paramInt);
  }

  private final JsonToken _nextAfterName()
  {
    this._nameCopied = false;
    JsonToken localJsonToken = this._nextToken;
    this._nextToken = null;
    if (localJsonToken == JsonToken.START_ARRAY)
      this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
    while (true)
    {
      this._currToken = localJsonToken;
      return localJsonToken;
      if (localJsonToken != JsonToken.START_OBJECT)
        continue;
      this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
    }
  }

  private String _parseFieldName2(int paramInt1, int paramInt2, int paramInt3)
    throws IOException, JsonParseException
  {
    this._textBuffer.resetWithShared(this._inputBuffer, paramInt1, this._inputPtr - paramInt1);
    char[] arrayOfChar1 = this._textBuffer.getCurrentSegment();
    int i = this._textBuffer.getCurrentSegmentSize();
    while (true)
    {
      if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
        _reportInvalidEOF(": was expecting closing '" + (char)paramInt3 + "' for name");
      char[] arrayOfChar2 = this._inputBuffer;
      int j = this._inputPtr;
      this._inputPtr = (j + 1);
      int k = arrayOfChar2[j];
      int m = k;
      if (m <= 92)
      {
        if (m != 92)
          break label178;
        k = _decodeEscaped();
      }
      int n;
      while (true)
      {
        paramInt2 = m + paramInt2 * 31;
        n = i + 1;
        arrayOfChar1[i] = k;
        if (n < arrayOfChar1.length)
          break label259;
        arrayOfChar1 = this._textBuffer.finishCurrentSegment();
        i = 0;
        break;
        label178: if (m > paramInt3)
          continue;
        if (m == paramInt3)
        {
          this._textBuffer.setCurrentLength(i);
          TextBuffer localTextBuffer = this._textBuffer;
          char[] arrayOfChar3 = localTextBuffer.getTextBuffer();
          int i1 = localTextBuffer.getTextOffset();
          int i2 = localTextBuffer.size();
          return this._symbols.findSymbol(arrayOfChar3, i1, i2, paramInt2);
        }
        if (m >= 32)
          continue;
        _throwUnquotedSpace(m, "name");
      }
      label259: i = n;
    }
  }

  private String _parseUnusualFieldName2(int paramInt1, int paramInt2, int[] paramArrayOfInt)
    throws IOException, JsonParseException
  {
    this._textBuffer.resetWithShared(this._inputBuffer, paramInt1, this._inputPtr - paramInt1);
    char[] arrayOfChar1 = this._textBuffer.getCurrentSegment();
    int i = this._textBuffer.getCurrentSegmentSize();
    int j = paramArrayOfInt.length;
    while (true)
    {
      if ((this._inputPtr >= this._inputEnd) && (!loadMore()));
      int i1;
      while (true)
      {
        this._textBuffer.setCurrentLength(i);
        TextBuffer localTextBuffer = this._textBuffer;
        char[] arrayOfChar2 = localTextBuffer.getTextBuffer();
        int m = localTextBuffer.getTextOffset();
        int n = localTextBuffer.size();
        return this._symbols.findSymbol(arrayOfChar2, m, n, paramInt2);
        int k = this._inputBuffer[this._inputPtr];
        if (k <= j)
          if (paramArrayOfInt[k] != 0)
            continue;
        do
        {
          this._inputPtr = (1 + this._inputPtr);
          paramInt2 = k + paramInt2 * 31;
          i1 = i + 1;
          arrayOfChar1[i] = k;
          if (i1 < arrayOfChar1.length)
            break label199;
          arrayOfChar1 = this._textBuffer.finishCurrentSegment();
          i = 0;
          break;
        }
        while (Character.isJavaIdentifierPart(k));
      }
      label199: i = i1;
    }
  }

  private final void _skipCComment()
    throws IOException, JsonParseException
  {
    while (true)
    {
      int j;
      if ((this._inputPtr < this._inputEnd) || (loadMore()))
      {
        char[] arrayOfChar = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = (i + 1);
        j = arrayOfChar[i];
        if (j > 42)
          continue;
        if (j != 42)
          break label101;
        if ((this._inputPtr < this._inputEnd) || (loadMore()));
      }
      else
      {
        _reportInvalidEOF(" in a comment");
        return;
      }
      if (this._inputBuffer[this._inputPtr] != '/')
        continue;
      this._inputPtr = (1 + this._inputPtr);
      return;
      label101: if (j >= 32)
        continue;
      if (j == 10)
      {
        _skipLF();
        continue;
      }
      if (j == 13)
      {
        _skipCR();
        continue;
      }
      if (j == 9)
        continue;
      _throwInvalidSpace(j);
    }
  }

  private final void _skipComment()
    throws IOException, JsonParseException
  {
    if (!isEnabled(JsonParser.Feature.ALLOW_COMMENTS))
      _reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
    if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
      _reportInvalidEOF(" in a comment");
    char[] arrayOfChar = this._inputBuffer;
    int i = this._inputPtr;
    this._inputPtr = (i + 1);
    int j = arrayOfChar[i];
    if (j == 47)
    {
      _skipCppComment();
      return;
    }
    if (j == 42)
    {
      _skipCComment();
      return;
    }
    _reportUnexpectedChar(j, "was expecting either '*' or '/' for a comment");
  }

  private final void _skipCppComment()
    throws IOException, JsonParseException
  {
    while (true)
    {
      int j;
      if ((this._inputPtr < this._inputEnd) || (loadMore()))
      {
        char[] arrayOfChar = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = (i + 1);
        j = arrayOfChar[i];
        if (j >= 32)
          continue;
        if (j == 10)
          _skipLF();
      }
      else
      {
        return;
      }
      if (j == 13)
      {
        _skipCR();
        return;
      }
      if (j == 9)
        continue;
      _throwInvalidSpace(j);
    }
  }

  private final int _skipWS()
    throws IOException, JsonParseException
  {
    while ((this._inputPtr < this._inputEnd) || (loadMore()))
    {
      char[] arrayOfChar = this._inputBuffer;
      int i = this._inputPtr;
      this._inputPtr = (i + 1);
      int j = arrayOfChar[i];
      if (j > 32)
      {
        if (j != 47)
          return j;
        _skipComment();
        continue;
      }
      if (j == 32)
        continue;
      if (j == 10)
      {
        _skipLF();
        continue;
      }
      if (j == 13)
      {
        _skipCR();
        continue;
      }
      if (j == 9)
        continue;
      _throwInvalidSpace(j);
    }
    throw _constructError("Unexpected end-of-input within/between " + this._parsingContext.getTypeDesc() + " entries");
  }

  private final int _skipWSOrEnd()
    throws IOException, JsonParseException
  {
    while ((this._inputPtr < this._inputEnd) || (loadMore()))
    {
      char[] arrayOfChar = this._inputBuffer;
      int i = this._inputPtr;
      this._inputPtr = (i + 1);
      int j = arrayOfChar[i];
      if (j > 32)
      {
        if (j != 47)
          return j;
        _skipComment();
        continue;
      }
      if (j == 32)
        continue;
      if (j == 10)
      {
        _skipLF();
        continue;
      }
      if (j == 13)
      {
        _skipCR();
        continue;
      }
      if (j == 9)
        continue;
      _throwInvalidSpace(j);
    }
    _handleEOF();
    return -1;
  }

  protected byte[] _decodeBase64(Base64Variant paramBase64Variant)
    throws IOException, JsonParseException
  {
    ByteArrayBuilder localByteArrayBuilder = _getByteArrayBuilder();
    while (true)
    {
      if (this._inputPtr >= this._inputEnd)
        loadMoreGuaranteed();
      char[] arrayOfChar1 = this._inputBuffer;
      int i = this._inputPtr;
      this._inputPtr = (i + 1);
      char c1 = arrayOfChar1[i];
      if (c1 <= ' ')
        continue;
      int j = paramBase64Variant.decodeBase64Char(c1);
      if (j < 0)
      {
        if (c1 == '"')
          return localByteArrayBuilder.toByteArray();
        j = _decodeBase64Escape(paramBase64Variant, c1, 0);
        if (j < 0)
          continue;
      }
      int k = j;
      if (this._inputPtr >= this._inputEnd)
        loadMoreGuaranteed();
      char[] arrayOfChar2 = this._inputBuffer;
      int m = this._inputPtr;
      this._inputPtr = (m + 1);
      char c2 = arrayOfChar2[m];
      int n = paramBase64Variant.decodeBase64Char(c2);
      if (n < 0)
        n = _decodeBase64Escape(paramBase64Variant, c2, 1);
      int i1 = n | k << 6;
      if (this._inputPtr >= this._inputEnd)
        loadMoreGuaranteed();
      char[] arrayOfChar3 = this._inputBuffer;
      int i2 = this._inputPtr;
      this._inputPtr = (i2 + 1);
      char c3 = arrayOfChar3[i2];
      int i3 = paramBase64Variant.decodeBase64Char(c3);
      if (i3 < 0)
      {
        if (i3 != -2)
          i3 = _decodeBase64Escape(paramBase64Variant, c3, 2);
        if (i3 == -2)
        {
          if (this._inputPtr >= this._inputEnd)
            loadMoreGuaranteed();
          char[] arrayOfChar5 = this._inputBuffer;
          int i7 = this._inputPtr;
          this._inputPtr = (i7 + 1);
          char c5 = arrayOfChar5[i7];
          if (!paramBase64Variant.usesPaddingChar(c5))
            throw reportInvalidChar(paramBase64Variant, c5, 3, "expected padding character '" + paramBase64Variant.getPaddingChar() + "'");
          localByteArrayBuilder.append(i1 >> 4);
          continue;
        }
      }
      int i4 = i3 | i1 << 6;
      if (this._inputPtr >= this._inputEnd)
        loadMoreGuaranteed();
      char[] arrayOfChar4 = this._inputBuffer;
      int i5 = this._inputPtr;
      this._inputPtr = (i5 + 1);
      char c4 = arrayOfChar4[i5];
      int i6 = paramBase64Variant.decodeBase64Char(c4);
      if (i6 < 0)
      {
        if (i6 != -2)
          i6 = _decodeBase64Escape(paramBase64Variant, c4, 3);
        if (i6 == -2)
        {
          localByteArrayBuilder.appendTwoBytes(i4 >> 2);
          continue;
        }
      }
      localByteArrayBuilder.appendThreeBytes(i6 | i4 << 6);
    }
  }

  protected final char _decodeEscaped()
    throws IOException, JsonParseException
  {
    if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
      _reportInvalidEOF(" in character escape sequence");
    char[] arrayOfChar1 = this._inputBuffer;
    int i = this._inputPtr;
    this._inputPtr = (i + 1);
    char c = arrayOfChar1[i];
    switch (c)
    {
    default:
      c = _handleUnrecognizedCharacterEscape(c);
    case '"':
    case '/':
    case '\\':
      return c;
    case 'b':
      return '\b';
    case 't':
      return '\t';
    case 'n':
      return '\n';
    case 'f':
      return '\f';
    case 'r':
      return '\r';
    case 'u':
    }
    int j = 0;
    for (int k = 0; k < 4; k++)
    {
      if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
        _reportInvalidEOF(" in character escape sequence");
      char[] arrayOfChar2 = this._inputBuffer;
      int m = this._inputPtr;
      this._inputPtr = (m + 1);
      int n = arrayOfChar2[m];
      int i1 = CharTypes.charToHex(n);
      if (i1 < 0)
        _reportUnexpectedChar(n, "expected a hex-digit for character escape sequence");
      j = i1 | j << 4;
    }
    return (char)j;
  }

  protected void _finishString()
    throws IOException, JsonParseException
  {
    int i = this._inputPtr;
    int j = this._inputEnd;
    if (i < j)
    {
      int[] arrayOfInt = CharTypes.getInputCodeLatin1();
      int k = arrayOfInt.length;
      do
      {
        int m = this._inputBuffer[i];
        if ((m < k) && (arrayOfInt[m] != 0))
        {
          if (m != 34)
            break;
          this._textBuffer.resetWithShared(this._inputBuffer, this._inputPtr, i - this._inputPtr);
          this._inputPtr = (i + 1);
          return;
        }
        i++;
      }
      while (i < j);
    }
    this._textBuffer.resetWithCopy(this._inputBuffer, this._inputPtr, i - this._inputPtr);
    this._inputPtr = i;
    _finishString2();
  }

  protected void _finishString2()
    throws IOException, JsonParseException
  {
    char[] arrayOfChar1 = this._textBuffer.getCurrentSegment();
    int i = this._textBuffer.getCurrentSegmentSize();
    if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
      _reportInvalidEOF(": was expecting closing quote for a string value");
    char[] arrayOfChar2 = this._inputBuffer;
    int j = this._inputPtr;
    this._inputPtr = (j + 1);
    int k = arrayOfChar2[j];
    int m = k;
    if (m <= 92)
    {
      if (m != 92)
        break label122;
      k = _decodeEscaped();
    }
    while (true)
    {
      if (i >= arrayOfChar1.length)
      {
        arrayOfChar1 = this._textBuffer.finishCurrentSegment();
        i = 0;
      }
      int n = i + 1;
      arrayOfChar1[i] = k;
      i = n;
      break;
      label122: if (m > 34)
        continue;
      if (m == 34)
      {
        this._textBuffer.setCurrentLength(i);
        return;
      }
      if (m >= 32)
        continue;
      _throwUnquotedSpace(m, "string value");
    }
  }

  protected final String _getText2(JsonToken paramJsonToken)
  {
    if (paramJsonToken == null)
      return null;
    switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[paramJsonToken.ordinal()])
    {
    default:
      return paramJsonToken.asString();
    case 1:
      return this._parsingContext.getCurrentName();
    case 2:
    case 3:
    case 4:
    }
    return this._textBuffer.contentsAsString();
  }

  protected final JsonToken _handleApostropheValue()
    throws IOException, JsonParseException
  {
    char[] arrayOfChar1 = this._textBuffer.emptyAndGetCurrentSegment();
    int i = this._textBuffer.getCurrentSegmentSize();
    if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
      _reportInvalidEOF(": was expecting closing quote for a string value");
    char[] arrayOfChar2 = this._inputBuffer;
    int j = this._inputPtr;
    this._inputPtr = (j + 1);
    int k = arrayOfChar2[j];
    int m = k;
    if (m <= 92)
    {
      if (m != 92)
        break label122;
      k = _decodeEscaped();
    }
    while (true)
    {
      if (i >= arrayOfChar1.length)
      {
        arrayOfChar1 = this._textBuffer.finishCurrentSegment();
        i = 0;
      }
      int n = i + 1;
      arrayOfChar1[i] = k;
      i = n;
      break;
      label122: if (m > 39)
        continue;
      if (m == 39)
      {
        this._textBuffer.setCurrentLength(i);
        return JsonToken.VALUE_STRING;
      }
      if (m >= 32)
        continue;
      _throwUnquotedSpace(m, "string value");
    }
  }

  protected final JsonToken _handleUnexpectedValue(int paramInt)
    throws IOException, JsonParseException
  {
    switch (paramInt)
    {
    default:
    case 39:
    case 78:
      while (true)
      {
        _reportUnexpectedChar(paramInt, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
        return null;
        if (isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES))
        {
          return _handleApostropheValue();
          if (_matchToken("NaN", 1))
          {
            if (isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS))
              return resetAsNaN("NaN", (0.0D / 0.0D));
            _reportError("Non-standard token 'NaN': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
          }
          char[] arrayOfChar2 = this._inputBuffer;
          int j = this._inputPtr;
          this._inputPtr = (j + 1);
          _reportUnexpectedChar(arrayOfChar2[j], "expected 'NaN' or a valid value");
        }
      }
    case 43:
    }
    if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
      _reportInvalidEOFInValue();
    char[] arrayOfChar1 = this._inputBuffer;
    int i = this._inputPtr;
    this._inputPtr = (i + 1);
    return _handleInvalidNumberStart(arrayOfChar1[i], false);
  }

  protected final String _handleUnusualFieldName(int paramInt)
    throws IOException, JsonParseException
  {
    if ((paramInt == 39) && (isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES)))
      return _parseApostropheFieldName();
    if (!isEnabled(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES))
      _reportUnexpectedChar(paramInt, "was expecting double-quote to start field name");
    int[] arrayOfInt = CharTypes.getInputCodeLatin1JsNames();
    int i = arrayOfInt.length;
    boolean bool;
    int j;
    int k;
    int m;
    if (paramInt < i)
      if ((arrayOfInt[paramInt] == 0) && ((paramInt < 48) || (paramInt > 57)))
      {
        bool = true;
        if (!bool)
          _reportUnexpectedChar(paramInt, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
        j = this._inputPtr;
        k = this._inputEnd;
        m = 0;
        if (j >= k)
          break label243;
      }
    do
    {
      int i1 = this._inputBuffer[j];
      if (i1 < i)
      {
        if (arrayOfInt[i1] != 0)
        {
          int i3 = -1 + this._inputPtr;
          this._inputPtr = j;
          return this._symbols.findSymbol(this._inputBuffer, i3, j - i3, m);
          bool = false;
          break;
          bool = Character.isJavaIdentifierPart((char)paramInt);
          break;
        }
      }
      else if (!Character.isJavaIdentifierPart(i1))
      {
        int i2 = -1 + this._inputPtr;
        this._inputPtr = j;
        return this._symbols.findSymbol(this._inputBuffer, i2, j - i2, m);
      }
      m = i1 + m * 31;
      j++;
    }
    while (j < k);
    label243: int n = -1 + this._inputPtr;
    this._inputPtr = j;
    return _parseUnusualFieldName2(n, m, arrayOfInt);
  }

  protected void _matchToken(JsonToken paramJsonToken)
    throws IOException, JsonParseException
  {
    String str = paramJsonToken.asString();
    int i = 1;
    int j = str.length();
    while (i < j)
    {
      if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
        _reportInvalidEOF(" in a value");
      if (this._inputBuffer[this._inputPtr] != str.charAt(i))
        _reportInvalidToken(str.substring(0, i), "'null', 'true' or 'false'");
      this._inputPtr = (1 + this._inputPtr);
      i++;
    }
  }

  protected final String _parseApostropheFieldName()
    throws IOException, JsonParseException
  {
    int i = this._inputPtr;
    int j = this._inputEnd;
    int k = 0;
    int i1;
    if (i < j)
    {
      int[] arrayOfInt = CharTypes.getInputCodeLatin1();
      int n = arrayOfInt.length;
      i1 = this._inputBuffer[i];
      if (i1 == 39)
      {
        int i2 = this._inputPtr;
        this._inputPtr = (i + 1);
        return this._symbols.findSymbol(this._inputBuffer, i2, i - i2, k);
      }
      if ((i1 >= n) || (arrayOfInt[i1] == 0))
        break label110;
    }
    while (true)
    {
      int m = this._inputPtr;
      this._inputPtr = i;
      return _parseFieldName2(m, k, 39);
      label110: k = i1 + k * 31;
      i++;
      if (i < j)
        break;
    }
  }

  protected final String _parseFieldName(int paramInt)
    throws IOException, JsonParseException
  {
    if (paramInt != 34)
      return _handleUnusualFieldName(paramInt);
    int i = this._inputPtr;
    int j = this._inputEnd;
    int k = 0;
    if (i < j)
    {
      int[] arrayOfInt = CharTypes.getInputCodeLatin1();
      int n = arrayOfInt.length;
      do
      {
        int i1 = this._inputBuffer[i];
        if ((i1 < n) && (arrayOfInt[i1] != 0))
        {
          if (i1 != 34)
            break;
          int i2 = this._inputPtr;
          this._inputPtr = (i + 1);
          return this._symbols.findSymbol(this._inputBuffer, i2, i - i2, k);
        }
        k = i1 + k * 31;
        i++;
      }
      while (i < j);
    }
    int m = this._inputPtr;
    this._inputPtr = i;
    return _parseFieldName2(m, k, 34);
  }

  protected final void _skipCR()
    throws IOException
  {
    if (((this._inputPtr < this._inputEnd) || (loadMore())) && (this._inputBuffer[this._inputPtr] == '\n'))
      this._inputPtr = (1 + this._inputPtr);
    this._currInputRow = (1 + this._currInputRow);
    this._currInputRowStart = this._inputPtr;
  }

  protected final void _skipLF()
    throws IOException
  {
    this._currInputRow = (1 + this._currInputRow);
    this._currInputRowStart = this._inputPtr;
  }

  protected void _skipString()
    throws IOException, JsonParseException
  {
    this._tokenIncomplete = false;
    int i = this._inputPtr;
    int j = this._inputEnd;
    char[] arrayOfChar = this._inputBuffer;
    while (true)
    {
      if (i >= j)
      {
        this._inputPtr = i;
        if (!loadMore())
          _reportInvalidEOF(": was expecting closing quote for a string value");
        i = this._inputPtr;
        j = this._inputEnd;
      }
      int k = i + 1;
      int m = arrayOfChar[i];
      if (m <= 92)
      {
        if (m == 92)
        {
          this._inputPtr = k;
          _decodeEscaped();
          i = this._inputPtr;
          j = this._inputEnd;
          continue;
        }
        if (m <= 34)
        {
          if (m == 34)
          {
            this._inputPtr = k;
            return;
          }
          if (m < 32)
          {
            this._inputPtr = k;
            _throwUnquotedSpace(m, "string value");
          }
        }
      }
      i = k;
    }
  }

  public void close()
    throws IOException
  {
    super.close();
    this._symbols.release();
  }

  public byte[] getBinaryValue(Base64Variant paramBase64Variant)
    throws IOException, JsonParseException
  {
    if ((this._currToken != JsonToken.VALUE_STRING) && ((this._currToken != JsonToken.VALUE_EMBEDDED_OBJECT) || (this._binaryValue == null)))
      _reportError("Current token (" + this._currToken + ") not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary");
    if (this._tokenIncomplete);
    try
    {
      this._binaryValue = _decodeBase64(paramBase64Variant);
      this._tokenIncomplete = false;
      return this._binaryValue;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
    }
    throw _constructError("Failed to decode VALUE_STRING as base64 (" + paramBase64Variant + "): " + localIllegalArgumentException.getMessage());
  }

  public ObjectCodec getCodec()
  {
    return this._objectCodec;
  }

  public final String getText()
    throws IOException, JsonParseException
  {
    JsonToken localJsonToken = this._currToken;
    if (localJsonToken == JsonToken.VALUE_STRING)
    {
      if (this._tokenIncomplete)
      {
        this._tokenIncomplete = false;
        _finishString();
      }
      return this._textBuffer.contentsAsString();
    }
    return _getText2(localJsonToken);
  }

  public char[] getTextCharacters()
    throws IOException, JsonParseException
  {
    if (this._currToken != null)
    {
      switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()])
      {
      default:
        return this._currToken.asCharArray();
      case 1:
        String str;
        int i;
        if (!this._nameCopied)
        {
          str = this._parsingContext.getCurrentName();
          i = str.length();
          if (this._nameCopyBuffer != null)
            break label116;
          this._nameCopyBuffer = this._ioContext.allocNameCopyBuffer(i);
        }
        while (true)
        {
          str.getChars(0, i, this._nameCopyBuffer, 0);
          this._nameCopied = true;
          return this._nameCopyBuffer;
          if (this._nameCopyBuffer.length >= i)
            continue;
          this._nameCopyBuffer = new char[i];
        }
      case 2:
        label116: if (!this._tokenIncomplete)
          break;
        this._tokenIncomplete = false;
        _finishString();
      case 3:
      case 4:
      }
      return this._textBuffer.getTextBuffer();
    }
    return null;
  }

  public int getTextLength()
    throws IOException, JsonParseException
  {
    JsonToken localJsonToken = this._currToken;
    int i = 0;
    if (localJsonToken != null);
    switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()])
    {
    default:
      i = this._currToken.asCharArray().length;
      return i;
    case 1:
      return this._parsingContext.getCurrentName().length();
    case 2:
      if (!this._tokenIncomplete)
        break;
      this._tokenIncomplete = false;
      _finishString();
    case 3:
    case 4:
    }
    return this._textBuffer.size();
  }

  public int getTextOffset()
    throws IOException, JsonParseException
  {
    if (this._currToken != null);
    switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()])
    {
    case 1:
    default:
      return 0;
    case 2:
      if (!this._tokenIncomplete)
        break;
      this._tokenIncomplete = false;
      _finishString();
    case 3:
    case 4:
    }
    return this._textBuffer.getTextOffset();
  }

  public JsonToken nextToken()
    throws IOException, JsonParseException
  {
    if (this._currToken == JsonToken.FIELD_NAME)
      return _nextAfterName();
    if (this._tokenIncomplete)
      _skipString();
    int i = _skipWSOrEnd();
    if (i < 0)
    {
      close();
      this._currToken = null;
      return null;
    }
    this._tokenInputTotal = (this._currInputProcessed + this._inputPtr - 1L);
    this._tokenInputRow = this._currInputRow;
    this._tokenInputCol = (-1 + (this._inputPtr - this._currInputRowStart));
    this._binaryValue = null;
    if (i == 93)
    {
      if (!this._parsingContext.inArray())
        _reportMismatchedEndMarker(i, '}');
      this._parsingContext = this._parsingContext.getParent();
      JsonToken localJsonToken3 = JsonToken.END_ARRAY;
      this._currToken = localJsonToken3;
      return localJsonToken3;
    }
    if (i == 125)
    {
      if (!this._parsingContext.inObject())
        _reportMismatchedEndMarker(i, ']');
      this._parsingContext = this._parsingContext.getParent();
      JsonToken localJsonToken2 = JsonToken.END_OBJECT;
      this._currToken = localJsonToken2;
      return localJsonToken2;
    }
    if (this._parsingContext.expectComma())
    {
      if (i != 44)
        _reportUnexpectedChar(i, "was expecting comma to separate " + this._parsingContext.getTypeDesc() + " entries");
      i = _skipWS();
    }
    boolean bool = this._parsingContext.inObject();
    if (bool)
    {
      String str = _parseFieldName(i);
      this._parsingContext.setCurrentName(str);
      this._currToken = JsonToken.FIELD_NAME;
      int j = _skipWS();
      if (j != 58)
        _reportUnexpectedChar(j, "was expecting a colon to separate field name and value");
      i = _skipWS();
    }
    JsonToken localJsonToken1;
    switch (i)
    {
    default:
      localJsonToken1 = _handleUnexpectedValue(i);
    case 34:
    case 91:
    case 123:
    case 93:
    case 125:
    case 116:
    case 102:
    case 110:
    case 45:
    case 48:
    case 49:
    case 50:
    case 51:
    case 52:
    case 53:
    case 54:
    case 55:
    case 56:
    case 57:
    }
    while (bool)
    {
      this._nextToken = localJsonToken1;
      return this._currToken;
      this._tokenIncomplete = true;
      localJsonToken1 = JsonToken.VALUE_STRING;
      continue;
      if (!bool)
        this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
      localJsonToken1 = JsonToken.START_ARRAY;
      continue;
      if (!bool)
        this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
      localJsonToken1 = JsonToken.START_OBJECT;
      continue;
      _reportUnexpectedChar(i, "expected a value");
      _matchToken(JsonToken.VALUE_TRUE);
      localJsonToken1 = JsonToken.VALUE_TRUE;
      continue;
      _matchToken(JsonToken.VALUE_FALSE);
      localJsonToken1 = JsonToken.VALUE_FALSE;
      continue;
      _matchToken(JsonToken.VALUE_NULL);
      localJsonToken1 = JsonToken.VALUE_NULL;
      continue;
      localJsonToken1 = parseNumberText(i);
    }
    this._currToken = localJsonToken1;
    return localJsonToken1;
  }

  protected IllegalArgumentException reportInvalidChar(Base64Variant paramBase64Variant, char paramChar, int paramInt)
    throws IllegalArgumentException
  {
    return reportInvalidChar(paramBase64Variant, paramChar, paramInt, null);
  }

  protected IllegalArgumentException reportInvalidChar(Base64Variant paramBase64Variant, char paramChar, int paramInt, String paramString)
    throws IllegalArgumentException
  {
    String str;
    if (paramChar <= ' ')
      str = "Illegal white space character (code 0x" + Integer.toHexString(paramChar) + ") as character #" + (paramInt + 1) + " of 4-char base64 unit: can only used between units";
    while (true)
    {
      if (paramString != null)
        str = str + ": " + paramString;
      return new IllegalArgumentException(str);
      if (paramBase64Variant.usesPaddingChar(paramChar))
      {
        str = "Unexpected padding character ('" + paramBase64Variant.getPaddingChar() + "') as character #" + (paramInt + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
        continue;
      }
      if ((!Character.isDefined(paramChar)) || (Character.isISOControl(paramChar)))
      {
        str = "Illegal character (code 0x" + Integer.toHexString(paramChar) + ") in base64 content";
        continue;
      }
      str = "Illegal character '" + paramChar + "' (code 0x" + Integer.toHexString(paramChar) + ") in base64 content";
    }
  }

  public void setCodec(ObjectCodec paramObjectCodec)
  {
    this._objectCodec = paramObjectCodec;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.ReaderBasedParser
 * JD-Core Version:    0.6.0
 */