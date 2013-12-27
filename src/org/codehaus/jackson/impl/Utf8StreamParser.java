package org.codehaus.jackson.impl;

import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.sym.BytesToNameCanonicalizer;
import org.codehaus.jackson.sym.Name;
import org.codehaus.jackson.util.ByteArrayBuilder;
import org.codehaus.jackson.util.CharTypes;
import org.codehaus.jackson.util.TextBuffer;

public final class Utf8StreamParser extends StreamBasedParserBase
{
  static final byte BYTE_LF = 10;
  private static final int[] sInputCodesLatin1;
  private static final int[] sInputCodesUtf8 = CharTypes.getInputCodeUtf8();
  protected ObjectCodec _objectCodec;
  private int _quad1;
  protected int[] _quadBuffer = new int[16];
  protected final BytesToNameCanonicalizer _symbols;
  protected boolean _tokenIncomplete = false;

  static
  {
    sInputCodesLatin1 = CharTypes.getInputCodeLatin1();
  }

  public Utf8StreamParser(IOContext paramIOContext, int paramInt1, InputStream paramInputStream, ObjectCodec paramObjectCodec, BytesToNameCanonicalizer paramBytesToNameCanonicalizer, byte[] paramArrayOfByte, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    super(paramIOContext, paramInt1, paramInputStream, paramArrayOfByte, paramInt2, paramInt3, paramBoolean);
    this._objectCodec = paramObjectCodec;
    this._symbols = paramBytesToNameCanonicalizer;
    if (!JsonParser.Feature.CANONICALIZE_FIELD_NAMES.enabledIn(paramInt1))
      _throwInternal();
  }

  private final int _decodeBase64Escape(Base64Variant paramBase64Variant, int paramInt1, int paramInt2)
    throws IOException, JsonParseException
  {
    if (paramInt1 != 92)
      throw reportInvalidChar(paramBase64Variant, paramInt1, paramInt2);
    int i = _decodeEscaped();
    int j;
    if ((i <= 32) && (paramInt2 == 0))
      j = -1;
    do
    {
      return j;
      j = paramBase64Variant.decodeBase64Char(i);
    }
    while (j >= 0);
    throw reportInvalidChar(paramBase64Variant, i, paramInt2);
  }

  private final int _decodeUtf8_2(int paramInt)
    throws IOException, JsonParseException
  {
    if (this._inputPtr >= this._inputEnd)
      loadMoreGuaranteed();
    byte[] arrayOfByte = this._inputBuffer;
    int i = this._inputPtr;
    this._inputPtr = (i + 1);
    int j = arrayOfByte[i];
    if ((j & 0xC0) != 128)
      _reportInvalidOther(j & 0xFF, this._inputPtr);
    return (paramInt & 0x1F) << 6 | j & 0x3F;
  }

  private final int _decodeUtf8_3(int paramInt)
    throws IOException, JsonParseException
  {
    if (this._inputPtr >= this._inputEnd)
      loadMoreGuaranteed();
    int i = paramInt & 0xF;
    byte[] arrayOfByte1 = this._inputBuffer;
    int j = this._inputPtr;
    this._inputPtr = (j + 1);
    int k = arrayOfByte1[j];
    if ((k & 0xC0) != 128)
      _reportInvalidOther(k & 0xFF, this._inputPtr);
    int m = i << 6 | k & 0x3F;
    if (this._inputPtr >= this._inputEnd)
      loadMoreGuaranteed();
    byte[] arrayOfByte2 = this._inputBuffer;
    int n = this._inputPtr;
    this._inputPtr = (n + 1);
    int i1 = arrayOfByte2[n];
    if ((i1 & 0xC0) != 128)
      _reportInvalidOther(i1 & 0xFF, this._inputPtr);
    return m << 6 | i1 & 0x3F;
  }

  private final int _decodeUtf8_3fast(int paramInt)
    throws IOException, JsonParseException
  {
    int i = paramInt & 0xF;
    byte[] arrayOfByte1 = this._inputBuffer;
    int j = this._inputPtr;
    this._inputPtr = (j + 1);
    int k = arrayOfByte1[j];
    if ((k & 0xC0) != 128)
      _reportInvalidOther(k & 0xFF, this._inputPtr);
    int m = i << 6 | k & 0x3F;
    byte[] arrayOfByte2 = this._inputBuffer;
    int n = this._inputPtr;
    this._inputPtr = (n + 1);
    int i1 = arrayOfByte2[n];
    if ((i1 & 0xC0) != 128)
      _reportInvalidOther(i1 & 0xFF, this._inputPtr);
    return m << 6 | i1 & 0x3F;
  }

  private final int _decodeUtf8_4(int paramInt)
    throws IOException, JsonParseException
  {
    if (this._inputPtr >= this._inputEnd)
      loadMoreGuaranteed();
    byte[] arrayOfByte1 = this._inputBuffer;
    int i = this._inputPtr;
    this._inputPtr = (i + 1);
    int j = arrayOfByte1[i];
    if ((j & 0xC0) != 128)
      _reportInvalidOther(j & 0xFF, this._inputPtr);
    int k = (paramInt & 0x7) << 6 | j & 0x3F;
    if (this._inputPtr >= this._inputEnd)
      loadMoreGuaranteed();
    byte[] arrayOfByte2 = this._inputBuffer;
    int m = this._inputPtr;
    this._inputPtr = (m + 1);
    int n = arrayOfByte2[m];
    if ((n & 0xC0) != 128)
      _reportInvalidOther(n & 0xFF, this._inputPtr);
    int i1 = k << 6 | n & 0x3F;
    if (this._inputPtr >= this._inputEnd)
      loadMoreGuaranteed();
    byte[] arrayOfByte3 = this._inputBuffer;
    int i2 = this._inputPtr;
    this._inputPtr = (i2 + 1);
    int i3 = arrayOfByte3[i2];
    if ((i3 & 0xC0) != 128)
      _reportInvalidOther(i3 & 0xFF, this._inputPtr);
    return (i1 << 6 | i3 & 0x3F) - 65536;
  }

  private final void _finishString2(char[] paramArrayOfChar, int paramInt)
    throws IOException, JsonParseException
  {
    int[] arrayOfInt = sInputCodesUtf8;
    byte[] arrayOfByte = this._inputBuffer;
    int m;
    int i1;
    while (true)
    {
      int i = this._inputPtr;
      if (i >= this._inputEnd)
      {
        loadMoreGuaranteed();
        i = this._inputPtr;
      }
      if (paramInt >= paramArrayOfChar.length)
      {
        paramArrayOfChar = this._textBuffer.finishCurrentSegment();
        paramInt = 0;
      }
      int j = Math.min(this._inputEnd, i + (paramArrayOfChar.length - paramInt));
      int k = i;
      int i2;
      for (m = paramInt; k < j; m = i2)
      {
        int n = k + 1;
        i1 = 0xFF & arrayOfByte[k];
        if (arrayOfInt[i1] != 0)
        {
          this._inputPtr = n;
          if (i1 != 34)
            break label164;
          this._textBuffer.setCurrentLength(m);
          return;
        }
        i2 = m + 1;
        paramArrayOfChar[m] = (char)i1;
        k = n;
      }
      this._inputPtr = k;
      paramInt = m;
    }
    label164: int i4;
    switch (arrayOfInt[i1])
    {
    default:
      if (i1 >= 32)
        break;
      _throwUnquotedSpace(i1, "string value");
      i4 = m;
    case 1:
    case 2:
    case 3:
    case 4:
    }
    while (true)
    {
      if (i4 >= paramArrayOfChar.length)
      {
        paramArrayOfChar = this._textBuffer.finishCurrentSegment();
        i4 = 0;
      }
      int i5 = i4 + 1;
      paramArrayOfChar[i4] = (char)i1;
      paramInt = i5;
      break;
      i1 = _decodeEscaped();
      i4 = m;
      continue;
      i1 = _decodeUtf8_2(i1);
      i4 = m;
      continue;
      if (this._inputEnd - this._inputPtr >= 2)
      {
        i1 = _decodeUtf8_3fast(i1);
        i4 = m;
        continue;
      }
      i1 = _decodeUtf8_3(i1);
      i4 = m;
      continue;
      int i3 = _decodeUtf8_4(i1);
      i4 = m + 1;
      paramArrayOfChar[m] = (char)(0xD800 | i3 >> 10);
      if (i4 >= paramArrayOfChar.length)
      {
        paramArrayOfChar = this._textBuffer.finishCurrentSegment();
        i4 = 0;
      }
      i1 = 0xDC00 | i3 & 0x3FF;
      continue;
      _reportInvalidChar(i1);
      i4 = m;
    }
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

  private final JsonToken _nextTokenNotInObject(int paramInt)
    throws IOException, JsonParseException
  {
    if (paramInt == 34)
    {
      this._tokenIncomplete = true;
      JsonToken localJsonToken8 = JsonToken.VALUE_STRING;
      this._currToken = localJsonToken8;
      return localJsonToken8;
    }
    switch (paramInt)
    {
    default:
      JsonToken localJsonToken7 = _handleUnexpectedValue(paramInt);
      this._currToken = localJsonToken7;
      return localJsonToken7;
    case 91:
      this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
      JsonToken localJsonToken6 = JsonToken.START_ARRAY;
      this._currToken = localJsonToken6;
      return localJsonToken6;
    case 123:
      this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
      JsonToken localJsonToken5 = JsonToken.START_OBJECT;
      this._currToken = localJsonToken5;
      return localJsonToken5;
    case 93:
    case 125:
      _reportUnexpectedChar(paramInt, "expected a value");
    case 116:
      _matchToken(JsonToken.VALUE_TRUE);
      JsonToken localJsonToken4 = JsonToken.VALUE_TRUE;
      this._currToken = localJsonToken4;
      return localJsonToken4;
    case 102:
      _matchToken(JsonToken.VALUE_FALSE);
      JsonToken localJsonToken3 = JsonToken.VALUE_FALSE;
      this._currToken = localJsonToken3;
      return localJsonToken3;
    case 110:
      _matchToken(JsonToken.VALUE_NULL);
      JsonToken localJsonToken2 = JsonToken.VALUE_NULL;
      this._currToken = localJsonToken2;
      return localJsonToken2;
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
    JsonToken localJsonToken1 = parseNumberText(paramInt);
    this._currToken = localJsonToken1;
    return localJsonToken1;
  }

  private final JsonToken _parseFloatText(char[] paramArrayOfChar, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
    throws IOException, JsonParseException
  {
    int i = 0;
    int j = 0;
    if (paramInt2 == 46)
    {
      int i7 = paramInt1 + 1;
      paramArrayOfChar[paramInt1] = (char)paramInt2;
      paramInt1 = i7;
      if ((this._inputPtr < this._inputEnd) || (loadMore()))
        break label359;
      i = 1;
      label46: if (j == 0)
        reportUnexpectedNumberChar(paramInt2, "Decimal point not followed by a digit");
    }
    int k = 0;
    int m;
    int i1;
    int i2;
    if (paramInt2 != 101)
    {
      k = 0;
      if (paramInt2 != 69);
    }
    else
    {
      if (paramInt1 >= paramArrayOfChar.length)
      {
        paramArrayOfChar = this._textBuffer.finishCurrentSegment();
        paramInt1 = 0;
      }
      m = paramInt1 + 1;
      paramArrayOfChar[paramInt1] = (char)paramInt2;
      if (this._inputPtr >= this._inputEnd)
        loadMoreGuaranteed();
      byte[] arrayOfByte1 = this._inputBuffer;
      int n = this._inputPtr;
      this._inputPtr = (n + 1);
      i1 = 0xFF & arrayOfByte1[n];
      if ((i1 != 45) && (i1 != 43))
        break label486;
      if (m < paramArrayOfChar.length)
        break label479;
      paramArrayOfChar = this._textBuffer.finishCurrentSegment();
      i2 = 0;
      label180: int i3 = i2 + 1;
      paramArrayOfChar[i2] = (char)i1;
      if (this._inputPtr >= this._inputEnd)
        loadMoreGuaranteed();
      byte[] arrayOfByte2 = this._inputBuffer;
      int i4 = this._inputPtr;
      this._inputPtr = (i4 + 1);
      i1 = 0xFF & arrayOfByte2[i4];
      paramInt1 = i3;
    }
    while (true)
    {
      int i5;
      if ((i1 <= 57) && (i1 >= 48))
      {
        k++;
        if (paramInt1 >= paramArrayOfChar.length)
        {
          paramArrayOfChar = this._textBuffer.finishCurrentSegment();
          paramInt1 = 0;
        }
        i5 = paramInt1 + 1;
        paramArrayOfChar[paramInt1] = (char)i1;
        if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
        {
          i = 1;
          paramInt1 = i5;
        }
      }
      else
      {
        if (k == 0)
          reportUnexpectedNumberChar(i1, "Exponent indicator not followed by a digit");
        if (i == 0)
          this._inputPtr = (-1 + this._inputPtr);
        this._textBuffer.setCurrentLength(paramInt1);
        return resetFloat(paramBoolean, paramInt3, j, k);
        label359: byte[] arrayOfByte4 = this._inputBuffer;
        int i8 = this._inputPtr;
        this._inputPtr = (i8 + 1);
        paramInt2 = 0xFF & arrayOfByte4[i8];
        i = 0;
        if (paramInt2 < 48)
          break label46;
        i = 0;
        if (paramInt2 > 57)
          break label46;
        j++;
        if (paramInt1 >= paramArrayOfChar.length)
        {
          paramArrayOfChar = this._textBuffer.finishCurrentSegment();
          paramInt1 = 0;
        }
        int i9 = paramInt1 + 1;
        paramArrayOfChar[paramInt1] = (char)paramInt2;
        paramInt1 = i9;
        break;
      }
      byte[] arrayOfByte3 = this._inputBuffer;
      int i6 = this._inputPtr;
      this._inputPtr = (i6 + 1);
      i1 = 0xFF & arrayOfByte3[i6];
      paramInt1 = i5;
      continue;
      label479: i2 = m;
      break label180;
      label486: paramInt1 = m;
      k = 0;
    }
  }

  private final JsonToken _parserNumber2(char[] paramArrayOfChar, int paramInt1, boolean paramBoolean, int paramInt2)
    throws IOException, JsonParseException
  {
    while (true)
    {
      if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
      {
        this._textBuffer.setCurrentLength(paramInt1);
        return resetInt(paramBoolean, paramInt2);
      }
      byte[] arrayOfByte = this._inputBuffer;
      int i = this._inputPtr;
      this._inputPtr = (i + 1);
      int j = 0xFF & arrayOfByte[i];
      if ((j > 57) || (j < 48))
      {
        if ((j != 46) && (j != 101) && (j != 69))
          break;
        return _parseFloatText(paramArrayOfChar, paramInt1, j, paramBoolean, paramInt2);
      }
      if (paramInt1 >= paramArrayOfChar.length)
      {
        paramArrayOfChar = this._textBuffer.finishCurrentSegment();
        paramInt1 = 0;
      }
      int k = paramInt1 + 1;
      paramArrayOfChar[paramInt1] = (char)j;
      paramInt2++;
      paramInt1 = k;
    }
    this._inputPtr = (-1 + this._inputPtr);
    this._textBuffer.setCurrentLength(paramInt1);
    return resetInt(paramBoolean, paramInt2);
  }

  private final void _skipCComment()
    throws IOException, JsonParseException
  {
    int[] arrayOfInt = CharTypes.getInputCodeComment();
    while ((this._inputPtr < this._inputEnd) || (loadMore()))
    {
      byte[] arrayOfByte = this._inputBuffer;
      int i = this._inputPtr;
      this._inputPtr = (i + 1);
      int j = 0xFF & arrayOfByte[i];
      int k = arrayOfInt[j];
      if (k == 0)
        continue;
      switch (k)
      {
      default:
        _reportInvalidChar(j);
        break;
      case 42:
        if (this._inputBuffer[this._inputPtr] != 47)
          continue;
        this._inputPtr = (1 + this._inputPtr);
        return;
      case 10:
        _skipLF();
        break;
      case 13:
        _skipCR();
      }
    }
    _reportInvalidEOF(" in a comment");
  }

  private final void _skipComment()
    throws IOException, JsonParseException
  {
    if (!isEnabled(JsonParser.Feature.ALLOW_COMMENTS))
      _reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
    if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
      _reportInvalidEOF(" in a comment");
    byte[] arrayOfByte = this._inputBuffer;
    int i = this._inputPtr;
    this._inputPtr = (i + 1);
    int j = 0xFF & arrayOfByte[i];
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
    int[] arrayOfInt = CharTypes.getInputCodeComment();
    while ((this._inputPtr < this._inputEnd) || (loadMore()))
    {
      byte[] arrayOfByte = this._inputBuffer;
      int i = this._inputPtr;
      this._inputPtr = (i + 1);
      int j = 0xFF & arrayOfByte[i];
      int k = arrayOfInt[j];
      if (k == 0)
        continue;
      switch (k)
      {
      case 42:
      default:
        _reportInvalidChar(j);
        break;
      case 10:
        _skipLF();
      case 13:
      }
    }
    return;
    _skipCR();
  }

  private final void _skipUtf8_2(int paramInt)
    throws IOException, JsonParseException
  {
    if (this._inputPtr >= this._inputEnd)
      loadMoreGuaranteed();
    byte[] arrayOfByte = this._inputBuffer;
    int i = this._inputPtr;
    this._inputPtr = (i + 1);
    int j = arrayOfByte[i];
    if ((j & 0xC0) != 128)
      _reportInvalidOther(j & 0xFF, this._inputPtr);
  }

  private final void _skipUtf8_3(int paramInt)
    throws IOException, JsonParseException
  {
    if (this._inputPtr >= this._inputEnd)
      loadMoreGuaranteed();
    byte[] arrayOfByte1 = this._inputBuffer;
    int i = this._inputPtr;
    this._inputPtr = (i + 1);
    int j = arrayOfByte1[i];
    if ((j & 0xC0) != 128)
      _reportInvalidOther(j & 0xFF, this._inputPtr);
    if (this._inputPtr >= this._inputEnd)
      loadMoreGuaranteed();
    byte[] arrayOfByte2 = this._inputBuffer;
    int k = this._inputPtr;
    this._inputPtr = (k + 1);
    int m = arrayOfByte2[k];
    if ((m & 0xC0) != 128)
      _reportInvalidOther(m & 0xFF, this._inputPtr);
  }

  private final void _skipUtf8_4(int paramInt)
    throws IOException, JsonParseException
  {
    if (this._inputPtr >= this._inputEnd)
      loadMoreGuaranteed();
    byte[] arrayOfByte1 = this._inputBuffer;
    int i = this._inputPtr;
    this._inputPtr = (i + 1);
    int j = arrayOfByte1[i];
    if ((j & 0xC0) != 128)
      _reportInvalidOther(j & 0xFF, this._inputPtr);
    if (this._inputPtr >= this._inputEnd)
      loadMoreGuaranteed();
    if ((j & 0xC0) != 128)
      _reportInvalidOther(j & 0xFF, this._inputPtr);
    if (this._inputPtr >= this._inputEnd)
      loadMoreGuaranteed();
    byte[] arrayOfByte2 = this._inputBuffer;
    int k = this._inputPtr;
    this._inputPtr = (k + 1);
    int m = arrayOfByte2[k];
    if ((m & 0xC0) != 128)
      _reportInvalidOther(m & 0xFF, this._inputPtr);
  }

  private final int _skipWS()
    throws IOException, JsonParseException
  {
    while ((this._inputPtr < this._inputEnd) || (loadMore()))
    {
      byte[] arrayOfByte = this._inputBuffer;
      int i = this._inputPtr;
      this._inputPtr = (i + 1);
      int j = 0xFF & arrayOfByte[i];
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
      byte[] arrayOfByte = this._inputBuffer;
      int i = this._inputPtr;
      this._inputPtr = (i + 1);
      int j = 0xFF & arrayOfByte[i];
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

  private final int _verifyNoLeadingZeroes()
    throws IOException, JsonParseException
  {
    if ((this._inputPtr >= this._inputEnd) && (!loadMore()));
    int i;
    do
    {
      i = 48;
      while ((this._inputPtr >= this._inputEnd) && (!loadMore()))
        do
        {
          return i;
          i = 0xFF & this._inputBuffer[this._inputPtr];
          if ((i < 48) || (i > 57))
            return 48;
          if (!isEnabled(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS))
            reportInvalidNumber("Leading zeroes not allowed");
          this._inputPtr = (1 + this._inputPtr);
        }
        while (i != 48);
      i = 0xFF & this._inputBuffer[this._inputPtr];
      if ((i < 48) || (i > 57))
        return 48;
      this._inputPtr = (1 + this._inputPtr);
    }
    while (i == 48);
    return i;
  }

  private final Name addName(int[] paramArrayOfInt, int paramInt1, int paramInt2)
    throws JsonParseException
  {
    int i = paramInt2 + (-4 + (paramInt1 << 2));
    int j;
    char[] arrayOfChar;
    int m;
    label50: int n;
    int i2;
    int i3;
    label110: int i1;
    if (paramInt2 < 4)
    {
      j = paramArrayOfInt[(paramInt1 - 1)];
      paramArrayOfInt[(paramInt1 - 1)] = (j << (4 - paramInt2 << 3));
      arrayOfChar = this._textBuffer.emptyAndGetCurrentSegment();
      int k = 0;
      m = 0;
      if (k >= i)
        break label463;
      n = 0xFF & paramArrayOfInt[(k >> 2)] >> (3 - (k & 0x3) << 3);
      k++;
      if (n <= 127)
        break label501;
      if ((n & 0xE0) != 192)
        break label397;
      i2 = n & 0x1F;
      i3 = 1;
      if (k + i3 > i)
        _reportInvalidEOF(" in field name");
      int i4 = paramArrayOfInt[(k >> 2)] >> (3 - (k & 0x3) << 3);
      k++;
      if ((i4 & 0xC0) != 128)
        _reportInvalidOther(i4);
      n = i2 << 6 | i4 & 0x3F;
      if (i3 > 1)
      {
        int i6 = paramArrayOfInt[(k >> 2)] >> (3 - (k & 0x3) << 3);
        k++;
        if ((i6 & 0xC0) != 128)
          _reportInvalidOther(i6);
        n = n << 6 | i6 & 0x3F;
        if (i3 > 2)
        {
          int i7 = paramArrayOfInt[(k >> 2)] >> (3 - (k & 0x3) << 3);
          k++;
          if ((i7 & 0xC0) != 128)
            _reportInvalidOther(i7 & 0xFF);
          n = n << 6 | i7 & 0x3F;
        }
      }
      if (i3 <= 2)
        break label501;
      int i5 = n - 65536;
      if (m >= arrayOfChar.length)
        arrayOfChar = this._textBuffer.expandCurrentSegment();
      i1 = m + 1;
      arrayOfChar[m] = (char)(55296 + (i5 >> 10));
      n = 0xDC00 | i5 & 0x3FF;
    }
    while (true)
    {
      if (i1 >= arrayOfChar.length)
        arrayOfChar = this._textBuffer.expandCurrentSegment();
      m = i1 + 1;
      arrayOfChar[i1] = (char)n;
      break label50;
      j = 0;
      break;
      label397: if ((n & 0xF0) == 224)
      {
        i2 = n & 0xF;
        i3 = 2;
        break label110;
      }
      if ((n & 0xF8) == 240)
      {
        i2 = n & 0x7;
        i3 = 3;
        break label110;
      }
      _reportInvalidInitial(n);
      i2 = 1;
      i3 = i2;
      break label110;
      label463: String str = new String(arrayOfChar, 0, m);
      if (paramInt2 < 4)
        paramArrayOfInt[(paramInt1 - 1)] = j;
      return this._symbols.addName(str, paramArrayOfInt, paramInt1);
      label501: i1 = m;
    }
  }

  private final Name findName(int paramInt1, int paramInt2)
    throws JsonParseException
  {
    Name localName = this._symbols.findName(paramInt1);
    if (localName != null)
      return localName;
    this._quadBuffer[0] = paramInt1;
    return addName(this._quadBuffer, 1, paramInt2);
  }

  private final Name findName(int paramInt1, int paramInt2, int paramInt3)
    throws JsonParseException
  {
    Name localName = this._symbols.findName(paramInt1, paramInt2);
    if (localName != null)
      return localName;
    this._quadBuffer[0] = paramInt1;
    this._quadBuffer[1] = paramInt2;
    return addName(this._quadBuffer, 2, paramInt3);
  }

  private final Name findName(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
    throws JsonParseException
  {
    if (paramInt1 >= paramArrayOfInt.length)
    {
      paramArrayOfInt = growArrayBy(paramArrayOfInt, paramArrayOfInt.length);
      this._quadBuffer = paramArrayOfInt;
    }
    int i = paramInt1 + 1;
    paramArrayOfInt[paramInt1] = paramInt2;
    Name localName = this._symbols.findName(paramArrayOfInt, i);
    if (localName == null)
      localName = addName(paramArrayOfInt, i, paramInt3);
    return localName;
  }

  public static int[] growArrayBy(int[] paramArrayOfInt, int paramInt)
  {
    if (paramArrayOfInt == null)
      return new int[paramInt];
    int i = paramArrayOfInt.length;
    int[] arrayOfInt = new int[i + paramInt];
    System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, i);
    return arrayOfInt;
  }

  private int nextByte()
    throws IOException, JsonParseException
  {
    if (this._inputPtr >= this._inputEnd)
      loadMoreGuaranteed();
    byte[] arrayOfByte = this._inputBuffer;
    int i = this._inputPtr;
    this._inputPtr = (i + 1);
    return 0xFF & arrayOfByte[i];
  }

  private final Name parseFieldName(int paramInt1, int paramInt2, int paramInt3)
    throws IOException, JsonParseException
  {
    return parseEscapedFieldName(this._quadBuffer, 0, paramInt1, paramInt2, paramInt3);
  }

  private final Name parseFieldName(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws IOException, JsonParseException
  {
    this._quadBuffer[0] = paramInt1;
    return parseEscapedFieldName(this._quadBuffer, 1, paramInt2, paramInt3, paramInt4);
  }

  protected byte[] _decodeBase64(Base64Variant paramBase64Variant)
    throws IOException, JsonParseException
  {
    ByteArrayBuilder localByteArrayBuilder = _getByteArrayBuilder();
    while (true)
    {
      if (this._inputPtr >= this._inputEnd)
        loadMoreGuaranteed();
      byte[] arrayOfByte1 = this._inputBuffer;
      int i = this._inputPtr;
      this._inputPtr = (i + 1);
      int j = 0xFF & arrayOfByte1[i];
      if (j <= 32)
        continue;
      int k = paramBase64Variant.decodeBase64Char(j);
      if (k < 0)
      {
        if (j == 34)
          return localByteArrayBuilder.toByteArray();
        k = _decodeBase64Escape(paramBase64Variant, j, 0);
        if (k < 0)
          continue;
      }
      int m = k;
      if (this._inputPtr >= this._inputEnd)
        loadMoreGuaranteed();
      byte[] arrayOfByte2 = this._inputBuffer;
      int n = this._inputPtr;
      this._inputPtr = (n + 1);
      int i1 = 0xFF & arrayOfByte2[n];
      int i2 = paramBase64Variant.decodeBase64Char(i1);
      if (i2 < 0)
        i2 = _decodeBase64Escape(paramBase64Variant, i1, 1);
      int i3 = i2 | m << 6;
      if (this._inputPtr >= this._inputEnd)
        loadMoreGuaranteed();
      byte[] arrayOfByte3 = this._inputBuffer;
      int i4 = this._inputPtr;
      this._inputPtr = (i4 + 1);
      int i5 = 0xFF & arrayOfByte3[i4];
      int i6 = paramBase64Variant.decodeBase64Char(i5);
      if (i6 < 0)
      {
        if (i6 != -2)
          i6 = _decodeBase64Escape(paramBase64Variant, i5, 2);
        if (i6 == -2)
        {
          if (this._inputPtr >= this._inputEnd)
            loadMoreGuaranteed();
          byte[] arrayOfByte5 = this._inputBuffer;
          int i11 = this._inputPtr;
          this._inputPtr = (i11 + 1);
          int i12 = 0xFF & arrayOfByte5[i11];
          if (!paramBase64Variant.usesPaddingChar(i12))
            throw reportInvalidChar(paramBase64Variant, i12, 3, "expected padding character '" + paramBase64Variant.getPaddingChar() + "'");
          localByteArrayBuilder.append(i3 >> 4);
          continue;
        }
      }
      int i7 = i6 | i3 << 6;
      if (this._inputPtr >= this._inputEnd)
        loadMoreGuaranteed();
      byte[] arrayOfByte4 = this._inputBuffer;
      int i8 = this._inputPtr;
      this._inputPtr = (i8 + 1);
      int i9 = 0xFF & arrayOfByte4[i8];
      int i10 = paramBase64Variant.decodeBase64Char(i9);
      if (i10 < 0)
      {
        if (i10 != -2)
          i10 = _decodeBase64Escape(paramBase64Variant, i9, 3);
        if (i10 == -2)
        {
          localByteArrayBuilder.appendTwoBytes(i7 >> 2);
          continue;
        }
      }
      localByteArrayBuilder.appendThreeBytes(i10 | i7 << 6);
    }
  }

  protected int _decodeCharForError(int paramInt)
    throws IOException, JsonParseException
  {
    int i = paramInt;
    int j;
    if (i < 0)
    {
      if ((i & 0xE0) != 192)
        break label153;
      i &= 31;
      j = 1;
    }
    while (true)
    {
      int k = nextByte();
      if ((k & 0xC0) != 128)
        _reportInvalidOther(k & 0xFF);
      i = i << 6 | k & 0x3F;
      if (j > 1)
      {
        int m = nextByte();
        if ((m & 0xC0) != 128)
          _reportInvalidOther(m & 0xFF);
        i = i << 6 | m & 0x3F;
        if (j > 2)
        {
          int n = nextByte();
          if ((n & 0xC0) != 128)
            _reportInvalidOther(n & 0xFF);
          i = i << 6 | n & 0x3F;
        }
      }
      return i;
      label153: if ((i & 0xF0) == 224)
      {
        i &= 15;
        j = 2;
        continue;
      }
      if ((i & 0xF8) == 240)
      {
        i &= 7;
        j = 3;
        continue;
      }
      _reportInvalidInitial(i & 0xFF);
      j = 1;
    }
  }

  protected final char _decodeEscaped()
    throws IOException, JsonParseException
  {
    if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
      _reportInvalidEOF(" in character escape sequence");
    byte[] arrayOfByte1 = this._inputBuffer;
    int i = this._inputPtr;
    this._inputPtr = (i + 1);
    int j = arrayOfByte1[i];
    switch (j)
    {
    default:
      return _handleUnrecognizedCharacterEscape((char)_decodeCharForError(j));
    case 98:
      return '\b';
    case 116:
      return '\t';
    case 110:
      return '\n';
    case 102:
      return '\f';
    case 114:
      return '\r';
    case 34:
    case 47:
    case 92:
      return (char)j;
    case 117:
    }
    int k = 0;
    for (int m = 0; m < 4; m++)
    {
      if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
        _reportInvalidEOF(" in character escape sequence");
      byte[] arrayOfByte2 = this._inputBuffer;
      int n = this._inputPtr;
      this._inputPtr = (n + 1);
      int i1 = arrayOfByte2[n];
      int i2 = CharTypes.charToHex(i1);
      if (i2 < 0)
        _reportUnexpectedChar(i1, "expected a hex-digit for character escape sequence");
      k = i2 | k << 4;
    }
    return (char)k;
  }

  protected void _finishString()
    throws IOException, JsonParseException
  {
    int i = this._inputPtr;
    if (i >= this._inputEnd)
    {
      loadMoreGuaranteed();
      i = this._inputPtr;
    }
    char[] arrayOfChar = this._textBuffer.emptyAndGetCurrentSegment();
    int[] arrayOfInt = sInputCodesUtf8;
    int j = Math.min(this._inputEnd, i + arrayOfChar.length);
    byte[] arrayOfByte = this._inputBuffer;
    int n;
    for (int k = 0; i < j; k = n)
    {
      int m = 0xFF & arrayOfByte[i];
      if (arrayOfInt[m] != 0)
      {
        if (m != 34)
          break;
        this._inputPtr = (i + 1);
        this._textBuffer.setCurrentLength(k);
        return;
      }
      i++;
      n = k + 1;
      arrayOfChar[k] = (char)m;
    }
    this._inputPtr = i;
    _finishString2(arrayOfChar, k);
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

  protected JsonToken _handleApostropheValue()
    throws IOException, JsonParseException
  {
    int i = 0;
    char[] arrayOfChar = this._textBuffer.emptyAndGetCurrentSegment();
    int[] arrayOfInt = sInputCodesUtf8;
    byte[] arrayOfByte = this._inputBuffer;
    int n;
    while (true)
    {
      if (this._inputPtr >= this._inputEnd)
        loadMoreGuaranteed();
      if (i >= arrayOfChar.length)
      {
        arrayOfChar = this._textBuffer.finishCurrentSegment();
        i = 0;
      }
      int j = this._inputEnd;
      int k = this._inputPtr + (arrayOfChar.length - i);
      if (k < j)
        j = k;
      while (this._inputPtr < j)
      {
        int m = this._inputPtr;
        this._inputPtr = (m + 1);
        n = 0xFF & arrayOfByte[m];
        if ((n == 39) || (arrayOfInt[n] != 0))
        {
          if (n != 39)
            break label163;
          this._textBuffer.setCurrentLength(i);
          return JsonToken.VALUE_STRING;
        }
        int i4 = i + 1;
        arrayOfChar[i] = (char)n;
        i = i4;
      }
    }
    label163: switch (arrayOfInt[n])
    {
    default:
      if (n < 32)
        _throwUnquotedSpace(n, "string value");
      _reportInvalidChar(n);
    case 1:
    case 2:
    case 3:
      while (true)
      {
        if (i >= arrayOfChar.length)
        {
          arrayOfChar = this._textBuffer.finishCurrentSegment();
          i = 0;
        }
        int i3 = i + 1;
        arrayOfChar[i] = (char)n;
        i = i3;
        break;
        if (n == 34)
          continue;
        n = _decodeEscaped();
        continue;
        n = _decodeUtf8_2(n);
        continue;
        if (this._inputEnd - this._inputPtr >= 2)
        {
          n = _decodeUtf8_3fast(n);
          continue;
        }
        n = _decodeUtf8_3(n);
      }
    case 4:
    }
    int i1 = _decodeUtf8_4(n);
    int i2 = i + 1;
    arrayOfChar[i] = (char)(0xD800 | i1 >> 10);
    if (i2 >= arrayOfChar.length)
      arrayOfChar = this._textBuffer.finishCurrentSegment();
    for (i = 0; ; i = i2)
    {
      n = 0xDC00 | i1 & 0x3FF;
      break;
    }
  }

  protected JsonToken _handleInvalidNumberStart(int paramInt, boolean paramBoolean)
    throws IOException, JsonParseException
  {
    double d = (-1.0D / 0.0D);
    if (paramInt == 73)
    {
      if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
        _reportInvalidEOFInValue();
      byte[] arrayOfByte = this._inputBuffer;
      int i = this._inputPtr;
      this._inputPtr = (i + 1);
      paramInt = arrayOfByte[i];
      if (paramInt != 78)
        break label161;
      String str2;
      if (paramBoolean)
      {
        str2 = "-INF";
        if (!_matchToken(str2, 3))
          break label151;
        if (!isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS))
          break label120;
        if (!paramBoolean)
          break label113;
      }
      while (true)
      {
        return resetAsNaN(str2, d);
        str2 = "+INF";
        break;
        label113: d = (1.0D / 0.0D);
      }
      label120: _reportError("Non-standard token '" + str2 + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
    }
    while (true)
    {
      label151: reportUnexpectedNumberChar(paramInt, "expected digit (0-9) to follow minus sign, for valid numeric value");
      return null;
      label161: if (paramInt != 110)
        continue;
      String str1;
      if (paramBoolean)
      {
        str1 = "-Infinity";
        label176: if (!_matchToken(str1, 3))
          break label214;
        if (!isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS))
          break label223;
        if (!paramBoolean)
          break label216;
      }
      while (true)
      {
        return resetAsNaN(str1, d);
        str1 = "+Infinity";
        break label176;
        label214: break;
        label216: d = (1.0D / 0.0D);
      }
      label223: _reportError("Non-standard token '" + str1 + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
    }
  }

  protected JsonToken _handleUnexpectedValue(int paramInt)
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
          byte[] arrayOfByte2 = this._inputBuffer;
          int j = this._inputPtr;
          this._inputPtr = (j + 1);
          _reportUnexpectedChar(0xFF & arrayOfByte2[j], "expected 'NaN' or a valid value");
        }
      }
    case 43:
    }
    if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
      _reportInvalidEOFInValue();
    byte[] arrayOfByte1 = this._inputBuffer;
    int i = this._inputPtr;
    this._inputPtr = (i + 1);
    return _handleInvalidNumberStart(0xFF & arrayOfByte1[i], false);
  }

  protected final Name _handleUnusualFieldName(int paramInt)
    throws IOException, JsonParseException
  {
    Name localName;
    if ((paramInt == 39) && (isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES)))
    {
      localName = _parseApostropheFieldName();
      return localName;
    }
    if (!isEnabled(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES))
      _reportUnexpectedChar(paramInt, "was expecting double-quote to start field name");
    int[] arrayOfInt1 = CharTypes.getInputCodeUtf8JsNames();
    if (arrayOfInt1[paramInt] != 0)
      _reportUnexpectedChar(paramInt, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
    int[] arrayOfInt2 = this._quadBuffer;
    int i = 0;
    int j = 0;
    int m;
    for (int k = 0; ; k = m)
    {
      if (j < 4)
      {
        j++;
        i = paramInt | i << 8;
        m = k;
      }
      while (true)
      {
        if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
          _reportInvalidEOF(" in field name");
        paramInt = 0xFF & this._inputBuffer[this._inputPtr];
        if (arrayOfInt1[paramInt] == 0)
          break label249;
        if (j > 0)
        {
          if (m >= arrayOfInt2.length)
          {
            arrayOfInt2 = growArrayBy(arrayOfInt2, arrayOfInt2.length);
            this._quadBuffer = arrayOfInt2;
          }
          int n = m + 1;
          arrayOfInt2[m] = i;
          m = n;
        }
        localName = this._symbols.findName(arrayOfInt2, m);
        if (localName != null)
          break;
        return addName(arrayOfInt2, m, j);
        if (k >= arrayOfInt2.length)
        {
          arrayOfInt2 = growArrayBy(arrayOfInt2, arrayOfInt2.length);
          this._quadBuffer = arrayOfInt2;
        }
        m = k + 1;
        arrayOfInt2[k] = i;
        i = paramInt;
        j = 1;
      }
      label249: this._inputPtr = (1 + this._inputPtr);
    }
  }

  protected void _matchToken(JsonToken paramJsonToken)
    throws IOException, JsonParseException
  {
    byte[] arrayOfByte = paramJsonToken.asByteArray();
    int i = 1;
    int j = arrayOfByte.length;
    while (i < j)
    {
      if (this._inputPtr >= this._inputEnd)
        loadMoreGuaranteed();
      if (arrayOfByte[i] != this._inputBuffer[this._inputPtr])
        _reportInvalidToken(paramJsonToken.asString().substring(0, i), "'null', 'true' or 'false'");
      this._inputPtr = (1 + this._inputPtr);
      i++;
    }
  }

  protected final boolean _matchToken(String paramString, int paramInt)
    throws IOException, JsonParseException
  {
    int i = paramString.length();
    do
    {
      if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
        _reportInvalidEOF(" in a value");
      if (this._inputBuffer[this._inputPtr] != paramString.charAt(paramInt))
        _reportInvalidToken(paramString.substring(0, paramInt), "'null', 'true', 'false' or NaN");
      this._inputPtr = (1 + this._inputPtr);
      paramInt++;
    }
    while (paramInt < i);
    if ((this._inputPtr >= this._inputEnd) && (!loadMore()));
    do
      return true;
    while (!Character.isJavaIdentifierPart((char)_decodeCharForError(0xFF & this._inputBuffer[this._inputPtr])));
    this._inputPtr = (1 + this._inputPtr);
    _reportInvalidToken(paramString.substring(0, paramInt), "'null', 'true', 'false' or NaN");
    return true;
  }

  protected final Name _parseApostropheFieldName()
    throws IOException, JsonParseException
  {
    if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
      _reportInvalidEOF(": was expecting closing ''' for name");
    byte[] arrayOfByte1 = this._inputBuffer;
    int i = this._inputPtr;
    this._inputPtr = (i + 1);
    int j = 0xFF & arrayOfByte1[i];
    Name localName;
    if (j == 39)
      localName = BytesToNameCanonicalizer.getEmptyName();
    int[] arrayOfInt1;
    int k;
    int m;
    int[] arrayOfInt2;
    int n;
    int i7;
    while (true)
    {
      return localName;
      arrayOfInt1 = this._quadBuffer;
      k = 0;
      m = 0;
      arrayOfInt2 = sInputCodesLatin1;
      n = 0;
      if (j != 39)
        break;
      if (m <= 0)
        break label527;
      if (n >= arrayOfInt1.length)
      {
        arrayOfInt1 = growArrayBy(arrayOfInt1, arrayOfInt1.length);
        this._quadBuffer = arrayOfInt1;
      }
      i7 = n + 1;
      arrayOfInt1[n] = k;
      localName = this._symbols.findName(arrayOfInt1, i7);
      if (localName == null)
        return addName(arrayOfInt1, i7, m);
    }
    if ((j != 34) && (arrayOfInt2[j] != 0))
    {
      if (j == 92)
        break label374;
      _throwUnquotedSpace(j, "name");
    }
    int i5;
    label277: int i1;
    while (true)
    {
      if (j > 127)
      {
        if (m >= 4)
        {
          if (n >= arrayOfInt1.length)
          {
            arrayOfInt1 = growArrayBy(arrayOfInt1, arrayOfInt1.length);
            this._quadBuffer = arrayOfInt1;
          }
          int i6 = n + 1;
          arrayOfInt1[n] = k;
          k = 0;
          m = 0;
          n = i6;
        }
        if (j >= 2048)
          break label382;
        k = k << 8 | (0xC0 | j >> 6);
        m++;
        i5 = n;
        j = 0x80 | j & 0x3F;
        n = i5;
      }
      if (m >= 4)
        break label481;
      m++;
      k = j | k << 8;
      i1 = n;
      label312: if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
        _reportInvalidEOF(" in field name");
      byte[] arrayOfByte2 = this._inputBuffer;
      int i2 = this._inputPtr;
      this._inputPtr = (i2 + 1);
      j = 0xFF & arrayOfByte2[i2];
      n = i1;
      break;
      label374: j = _decodeEscaped();
    }
    label382: int i3 = k << 8 | (0xE0 | j >> 12);
    int i4 = m + 1;
    if (i4 >= 4)
    {
      if (n >= arrayOfInt1.length)
      {
        arrayOfInt1 = growArrayBy(arrayOfInt1, arrayOfInt1.length);
        this._quadBuffer = arrayOfInt1;
      }
      i5 = n + 1;
      arrayOfInt1[n] = i3;
      i3 = 0;
      i4 = 0;
    }
    while (true)
    {
      k = i3 << 8 | (0x80 | 0x3F & j >> 6);
      m = i4 + 1;
      break label277;
      label481: if (n >= arrayOfInt1.length)
      {
        arrayOfInt1 = growArrayBy(arrayOfInt1, arrayOfInt1.length);
        this._quadBuffer = arrayOfInt1;
      }
      i1 = n + 1;
      arrayOfInt1[n] = k;
      k = j;
      m = 1;
      break label312;
      label527: i7 = n;
      break;
      i5 = n;
    }
  }

  protected final Name _parseFieldName(int paramInt)
    throws IOException, JsonParseException
  {
    if (paramInt != 34)
      return _handleUnusualFieldName(paramInt);
    if (9 + this._inputPtr > this._inputEnd)
      return slowParseFieldName();
    byte[] arrayOfByte = this._inputBuffer;
    int[] arrayOfInt = sInputCodesLatin1;
    int i = this._inputPtr;
    this._inputPtr = (i + 1);
    int j = 0xFF & arrayOfByte[i];
    if (arrayOfInt[j] == 0)
    {
      int k = this._inputPtr;
      this._inputPtr = (k + 1);
      int m = 0xFF & arrayOfByte[k];
      if (arrayOfInt[m] == 0)
      {
        int n = m | j << 8;
        int i1 = this._inputPtr;
        this._inputPtr = (i1 + 1);
        int i2 = 0xFF & arrayOfByte[i1];
        if (arrayOfInt[i2] == 0)
        {
          int i3 = i2 | n << 8;
          int i4 = this._inputPtr;
          this._inputPtr = (i4 + 1);
          int i5 = 0xFF & arrayOfByte[i4];
          if (arrayOfInt[i5] == 0)
          {
            int i6 = i5 | i3 << 8;
            int i7 = this._inputPtr;
            this._inputPtr = (i7 + 1);
            int i8 = 0xFF & arrayOfByte[i7];
            if (arrayOfInt[i8] == 0)
            {
              this._quad1 = i6;
              return parseMediumFieldName(i8, arrayOfInt);
            }
            if (i8 == 34)
              return findName(i6, 4);
            return parseFieldName(i6, i8, 4);
          }
          if (i5 == 34)
            return findName(i3, 3);
          return parseFieldName(i3, i5, 3);
        }
        if (i2 == 34)
          return findName(n, 2);
        return parseFieldName(n, i2, 2);
      }
      if (m == 34)
        return findName(j, 1);
      return parseFieldName(j, m, 1);
    }
    if (j == 34)
      return BytesToNameCanonicalizer.getEmptyName();
    return parseFieldName(0, j, 0);
  }

  protected void _reportInvalidChar(int paramInt)
    throws JsonParseException
  {
    if (paramInt < 32)
      _throwInvalidSpace(paramInt);
    _reportInvalidInitial(paramInt);
  }

  protected void _reportInvalidInitial(int paramInt)
    throws JsonParseException
  {
    _reportError("Invalid UTF-8 start byte 0x" + Integer.toHexString(paramInt));
  }

  protected void _reportInvalidOther(int paramInt)
    throws JsonParseException
  {
    _reportError("Invalid UTF-8 middle byte 0x" + Integer.toHexString(paramInt));
  }

  protected void _reportInvalidOther(int paramInt1, int paramInt2)
    throws JsonParseException
  {
    this._inputPtr = paramInt2;
    _reportInvalidOther(paramInt1);
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
        _reportError("Unrecognized token '" + localStringBuilder.toString() + "': was expecting " + paramString2);
        return;
        byte[] arrayOfByte = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = (i + 1);
        c = (char)_decodeCharForError(arrayOfByte[i]);
      }
      while (!Character.isJavaIdentifierPart(c));
      this._inputPtr = (1 + this._inputPtr);
      localStringBuilder.append(c);
    }
  }

  protected final void _skipCR()
    throws IOException
  {
    if (((this._inputPtr < this._inputEnd) || (loadMore())) && (this._inputBuffer[this._inputPtr] == 10))
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
    int[] arrayOfInt = sInputCodesUtf8;
    byte[] arrayOfByte = this._inputBuffer;
    int i = this._inputPtr;
    int j = this._inputEnd;
    int n;
    if (i >= j)
    {
      loadMoreGuaranteed();
      n = this._inputPtr;
      j = this._inputEnd;
    }
    for (int k = n; ; k = i)
    {
      int m;
      if (k < j)
      {
        i = k + 1;
        m = 0xFF & arrayOfByte[k];
        if (arrayOfInt[m] == 0)
          continue;
        this._inputPtr = i;
        if (m == 34)
          return;
      }
      else
      {
        this._inputPtr = k;
        break;
      }
      switch (arrayOfInt[m])
      {
      default:
        if (m < 32)
          _throwUnquotedSpace(m, "string value");
        break;
      case 1:
        _decodeEscaped();
        break;
      case 2:
        _skipUtf8_2(m);
        break;
      case 3:
        _skipUtf8_3(m);
        break;
      case 4:
        _skipUtf8_4(m);
        break;
        _reportInvalidChar(m);
        break;
      }
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

  public String getText()
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
    if (!this._parsingContext.inObject())
      return _nextTokenNotInObject(i);
    Name localName = _parseFieldName(i);
    this._parsingContext.setCurrentName(localName.getName());
    this._currToken = JsonToken.FIELD_NAME;
    int j = _skipWS();
    if (j != 58)
      _reportUnexpectedChar(j, "was expecting a colon to separate field name and value");
    int k = _skipWS();
    if (k == 34)
    {
      this._tokenIncomplete = true;
      this._nextToken = JsonToken.VALUE_STRING;
      return this._currToken;
    }
    JsonToken localJsonToken1;
    switch (k)
    {
    default:
      localJsonToken1 = _handleUnexpectedValue(k);
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
    while (true)
    {
      this._nextToken = localJsonToken1;
      return this._currToken;
      localJsonToken1 = JsonToken.START_ARRAY;
      continue;
      localJsonToken1 = JsonToken.START_OBJECT;
      continue;
      _reportUnexpectedChar(k, "expected a value");
      _matchToken(JsonToken.VALUE_TRUE);
      localJsonToken1 = JsonToken.VALUE_TRUE;
      continue;
      _matchToken(JsonToken.VALUE_FALSE);
      localJsonToken1 = JsonToken.VALUE_FALSE;
      continue;
      _matchToken(JsonToken.VALUE_NULL);
      localJsonToken1 = JsonToken.VALUE_NULL;
      continue;
      localJsonToken1 = parseNumberText(k);
    }
  }

  protected Name parseEscapedFieldName(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws IOException, JsonParseException
  {
    int[] arrayOfInt = sInputCodesLatin1;
    label100: int k;
    label145: int i1;
    if (arrayOfInt[paramInt3] != 0)
    {
      if (paramInt3 == 34)
      {
        if (paramInt4 > 0)
        {
          if (paramInt1 >= paramArrayOfInt.length)
          {
            paramArrayOfInt = growArrayBy(paramArrayOfInt, paramArrayOfInt.length);
            this._quadBuffer = paramArrayOfInt;
          }
          int i2 = paramInt1 + 1;
          paramArrayOfInt[paramInt1] = paramInt2;
          paramInt1 = i2;
        }
        Name localName = this._symbols.findName(paramArrayOfInt, paramInt1);
        if (localName == null)
          localName = addName(paramArrayOfInt, paramInt1, paramInt4);
        return localName;
      }
      if (paramInt3 != 92)
      {
        _throwUnquotedSpace(paramInt3, "name");
        if (paramInt3 <= 127)
          break label422;
        if (paramInt4 < 4)
          break label416;
        if (paramInt1 >= paramArrayOfInt.length)
        {
          paramArrayOfInt = growArrayBy(paramArrayOfInt, paramArrayOfInt.length);
          this._quadBuffer = paramArrayOfInt;
        }
        k = paramInt1 + 1;
        paramArrayOfInt[paramInt1] = paramInt2;
        paramInt2 = 0;
        paramInt4 = 0;
        if (paramInt3 >= 2048)
          break label278;
        paramInt2 = paramInt2 << 8 | (0xC0 | paramInt3 >> 6);
        paramInt4++;
        i1 = k;
        paramInt3 = 0x80 | paramInt3 & 0x3F;
      }
    }
    label278: label409: label416: label422: for (int i = i1; ; i = paramInt1)
    {
      label210: int m;
      int n;
      if (paramInt4 < 4)
      {
        paramInt4++;
        paramInt2 = paramInt3 | paramInt2 << 8;
        paramInt1 = i;
        if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
          _reportInvalidEOF(" in field name");
        byte[] arrayOfByte = this._inputBuffer;
        int j = this._inputPtr;
        this._inputPtr = (j + 1);
        paramInt3 = 0xFF & arrayOfByte[j];
        break;
        paramInt3 = _decodeEscaped();
        break label100;
        m = paramInt2 << 8 | (0xE0 | paramInt3 >> 12);
        n = paramInt4 + 1;
        if (n < 4)
          break label409;
        if (k >= paramArrayOfInt.length)
        {
          paramArrayOfInt = growArrayBy(paramArrayOfInt, paramArrayOfInt.length);
          this._quadBuffer = paramArrayOfInt;
        }
        i1 = k + 1;
        paramArrayOfInt[k] = m;
        m = 0;
        n = 0;
      }
      while (true)
      {
        paramInt2 = m << 8 | (0x80 | 0x3F & paramInt3 >> 6);
        paramInt4 = n + 1;
        break;
        if (i >= paramArrayOfInt.length)
        {
          paramArrayOfInt = growArrayBy(paramArrayOfInt, paramArrayOfInt.length);
          this._quadBuffer = paramArrayOfInt;
        }
        paramInt1 = i + 1;
        paramArrayOfInt[i] = paramInt2;
        paramInt2 = paramInt3;
        paramInt4 = 1;
        break label210;
        i1 = k;
      }
      k = paramInt1;
      break label145;
    }
  }

  protected Name parseLongFieldName(int paramInt)
    throws IOException, JsonParseException
  {
    int[] arrayOfInt1 = sInputCodesLatin1;
    int i8;
    for (int i = 2; ; i = i8)
    {
      if (this._inputEnd - this._inputPtr < 4)
        return parseEscapedFieldName(this._quadBuffer, i, 0, paramInt, 0);
      byte[] arrayOfByte1 = this._inputBuffer;
      int j = this._inputPtr;
      this._inputPtr = (j + 1);
      int k = 0xFF & arrayOfByte1[j];
      if (arrayOfInt1[k] != 0)
      {
        if (k == 34)
          return findName(this._quadBuffer, i, paramInt, 1);
        return parseEscapedFieldName(this._quadBuffer, i, paramInt, k, 1);
      }
      int m = k | paramInt << 8;
      byte[] arrayOfByte2 = this._inputBuffer;
      int n = this._inputPtr;
      this._inputPtr = (n + 1);
      int i1 = 0xFF & arrayOfByte2[n];
      if (arrayOfInt1[i1] != 0)
      {
        if (i1 == 34)
          return findName(this._quadBuffer, i, m, 2);
        return parseEscapedFieldName(this._quadBuffer, i, m, i1, 2);
      }
      int i2 = i1 | m << 8;
      byte[] arrayOfByte3 = this._inputBuffer;
      int i3 = this._inputPtr;
      this._inputPtr = (i3 + 1);
      int i4 = 0xFF & arrayOfByte3[i3];
      if (arrayOfInt1[i4] != 0)
      {
        if (i4 == 34)
          return findName(this._quadBuffer, i, i2, 3);
        return parseEscapedFieldName(this._quadBuffer, i, i2, i4, 3);
      }
      int i5 = i4 | i2 << 8;
      byte[] arrayOfByte4 = this._inputBuffer;
      int i6 = this._inputPtr;
      this._inputPtr = (i6 + 1);
      int i7 = 0xFF & arrayOfByte4[i6];
      if (arrayOfInt1[i7] != 0)
      {
        if (i7 == 34)
          return findName(this._quadBuffer, i, i5, 4);
        return parseEscapedFieldName(this._quadBuffer, i, i5, i7, 4);
      }
      if (i >= this._quadBuffer.length)
        this._quadBuffer = growArrayBy(this._quadBuffer, i);
      int[] arrayOfInt2 = this._quadBuffer;
      i8 = i + 1;
      arrayOfInt2[i] = i5;
      paramInt = i7;
    }
  }

  protected final Name parseMediumFieldName(int paramInt, int[] paramArrayOfInt)
    throws IOException, JsonParseException
  {
    byte[] arrayOfByte1 = this._inputBuffer;
    int i = this._inputPtr;
    this._inputPtr = (i + 1);
    int j = 0xFF & arrayOfByte1[i];
    if (paramArrayOfInt[j] != 0)
    {
      if (j == 34)
        return findName(this._quad1, paramInt, 1);
      return parseFieldName(this._quad1, paramInt, j, 1);
    }
    int k = j | paramInt << 8;
    byte[] arrayOfByte2 = this._inputBuffer;
    int m = this._inputPtr;
    this._inputPtr = (m + 1);
    int n = 0xFF & arrayOfByte2[m];
    if (paramArrayOfInt[n] != 0)
    {
      if (n == 34)
        return findName(this._quad1, k, 2);
      return parseFieldName(this._quad1, k, n, 2);
    }
    int i1 = n | k << 8;
    byte[] arrayOfByte3 = this._inputBuffer;
    int i2 = this._inputPtr;
    this._inputPtr = (i2 + 1);
    int i3 = 0xFF & arrayOfByte3[i2];
    if (paramArrayOfInt[i3] != 0)
    {
      if (i3 == 34)
        return findName(this._quad1, i1, 3);
      return parseFieldName(this._quad1, i1, i3, 3);
    }
    int i4 = i3 | i1 << 8;
    byte[] arrayOfByte4 = this._inputBuffer;
    int i5 = this._inputPtr;
    this._inputPtr = (i5 + 1);
    int i6 = 0xFF & arrayOfByte4[i5];
    if (paramArrayOfInt[i6] != 0)
    {
      if (i6 == 34)
        return findName(this._quad1, i4, 4);
      return parseFieldName(this._quad1, i4, i6, 4);
    }
    this._quadBuffer[0] = this._quad1;
    this._quadBuffer[1] = i4;
    return parseLongFieldName(i6);
  }

  protected final JsonToken parseNumberText(int paramInt)
    throws IOException, JsonParseException
  {
    char[] arrayOfChar = this._textBuffer.emptyAndGetCurrentSegment();
    boolean bool;
    if (paramInt == 45)
      bool = true;
    int i;
    while (true)
      if (bool)
      {
        i = 0 + 1;
        arrayOfChar[0] = '-';
        if (this._inputPtr >= this._inputEnd)
          loadMoreGuaranteed();
        byte[] arrayOfByte2 = this._inputBuffer;
        int i3 = this._inputPtr;
        this._inputPtr = (i3 + 1);
        paramInt = 0xFF & arrayOfByte2[i3];
        if ((paramInt >= 48) && (paramInt <= 57))
          break;
        JsonToken localJsonToken = _handleInvalidNumberStart(paramInt, true);
        return localJsonToken;
        bool = false;
        continue;
      }
      else
      {
        i = 0;
      }
    if (paramInt == 48)
      paramInt = _verifyNoLeadingZeroes();
    int j = i + 1;
    arrayOfChar[i] = (char)paramInt;
    int k = 1;
    int m = this._inputPtr + arrayOfChar.length;
    if (m > this._inputEnd)
      m = this._inputEnd;
    while (true)
    {
      if (this._inputPtr >= m)
        return _parserNumber2(arrayOfChar, j, bool, k);
      byte[] arrayOfByte1 = this._inputBuffer;
      int n = this._inputPtr;
      this._inputPtr = (n + 1);
      int i1 = 0xFF & arrayOfByte1[n];
      if ((i1 < 48) || (i1 > 57))
      {
        if ((i1 != 46) && (i1 != 101) && (i1 != 69))
          break;
        return _parseFloatText(arrayOfChar, j, i1, bool, k);
      }
      k++;
      int i2 = j + 1;
      arrayOfChar[j] = (char)i1;
      j = i2;
    }
    this._inputPtr = (-1 + this._inputPtr);
    this._textBuffer.setCurrentLength(j);
    return resetInt(bool, k);
  }

  protected IllegalArgumentException reportInvalidChar(Base64Variant paramBase64Variant, int paramInt1, int paramInt2)
    throws IllegalArgumentException
  {
    return reportInvalidChar(paramBase64Variant, paramInt1, paramInt2, null);
  }

  protected IllegalArgumentException reportInvalidChar(Base64Variant paramBase64Variant, int paramInt1, int paramInt2, String paramString)
    throws IllegalArgumentException
  {
    String str;
    if (paramInt1 <= 32)
      str = "Illegal white space character (code 0x" + Integer.toHexString(paramInt1) + ") as character #" + (paramInt2 + 1) + " of 4-char base64 unit: can only used between units";
    while (true)
    {
      if (paramString != null)
        str = str + ": " + paramString;
      return new IllegalArgumentException(str);
      if (paramBase64Variant.usesPaddingChar(paramInt1))
      {
        str = "Unexpected padding character ('" + paramBase64Variant.getPaddingChar() + "') as character #" + (paramInt2 + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
        continue;
      }
      if ((!Character.isDefined(paramInt1)) || (Character.isISOControl(paramInt1)))
      {
        str = "Illegal character (code 0x" + Integer.toHexString(paramInt1) + ") in base64 content";
        continue;
      }
      str = "Illegal character '" + (char)paramInt1 + "' (code 0x" + Integer.toHexString(paramInt1) + ") in base64 content";
    }
  }

  public void setCodec(ObjectCodec paramObjectCodec)
  {
    this._objectCodec = paramObjectCodec;
  }

  protected Name slowParseFieldName()
    throws IOException, JsonParseException
  {
    if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
      _reportInvalidEOF(": was expecting closing '\"' for name");
    byte[] arrayOfByte = this._inputBuffer;
    int i = this._inputPtr;
    this._inputPtr = (i + 1);
    int j = 0xFF & arrayOfByte[i];
    if (j == 34)
      return BytesToNameCanonicalizer.getEmptyName();
    return parseEscapedFieldName(this._quadBuffer, 0, 0, j, 0);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.Utf8StreamParser
 * JD-Core Version:    0.6.0
 */