package org.codehaus.jackson.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.PrettyPrinter;
import org.codehaus.jackson.SerializableString;
import org.codehaus.jackson.io.CharacterEscapes;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.io.NumberOutput;
import org.codehaus.jackson.io.SerializedString;
import org.codehaus.jackson.util.CharTypes;

public class Utf8Generator extends JsonGeneratorBase
{
  private static final byte BYTE_0 = 48;
  private static final byte BYTE_BACKSLASH = 92;
  private static final byte BYTE_COLON = 58;
  private static final byte BYTE_COMMA = 44;
  private static final byte BYTE_LBRACKET = 91;
  private static final byte BYTE_LCURLY = 123;
  private static final byte BYTE_QUOTE = 34;
  private static final byte BYTE_RBRACKET = 93;
  private static final byte BYTE_RCURLY = 125;
  private static final byte BYTE_SPACE = 32;
  private static final byte BYTE_u = 117;
  private static final byte[] FALSE_BYTES;
  static final byte[] HEX_CHARS = CharTypes.copyHexBytes();
  private static final int MAX_BYTES_TO_BUFFER = 512;
  private static final byte[] NULL_BYTES = { 110, 117, 108, 108 };
  protected static final int SURR1_FIRST = 55296;
  protected static final int SURR1_LAST = 56319;
  protected static final int SURR2_FIRST = 56320;
  protected static final int SURR2_LAST = 57343;
  private static final byte[] TRUE_BYTES = { 116, 114, 117, 101 };
  protected static final int[] sOutputEscapes;
  protected boolean _bufferRecyclable;
  protected char[] _charBuffer;
  protected final int _charBufferLength;
  protected CharacterEscapes _characterEscapes;
  protected byte[] _entityBuffer;
  protected final IOContext _ioContext;
  protected int _maximumNonEscapedChar;
  protected byte[] _outputBuffer;
  protected final int _outputEnd;
  protected int[] _outputEscapes = sOutputEscapes;
  protected final int _outputMaxContiguous;
  protected final OutputStream _outputStream;
  protected int _outputTail = 0;

  static
  {
    FALSE_BYTES = new byte[] { 102, 97, 108, 115, 101 };
    sOutputEscapes = CharTypes.get7BitOutputEscapes();
  }

  public Utf8Generator(IOContext paramIOContext, int paramInt, ObjectCodec paramObjectCodec, OutputStream paramOutputStream)
  {
    super(paramInt, paramObjectCodec);
    this._ioContext = paramIOContext;
    this._outputStream = paramOutputStream;
    this._bufferRecyclable = true;
    this._outputBuffer = paramIOContext.allocWriteEncodingBuffer();
    this._outputEnd = this._outputBuffer.length;
    this._outputMaxContiguous = (this._outputEnd >> 3);
    this._charBuffer = paramIOContext.allocConcatBuffer();
    this._charBufferLength = this._charBuffer.length;
    if (isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII))
      setHighestNonEscapedChar(127);
  }

  public Utf8Generator(IOContext paramIOContext, int paramInt1, ObjectCodec paramObjectCodec, OutputStream paramOutputStream, byte[] paramArrayOfByte, int paramInt2, boolean paramBoolean)
  {
    super(paramInt1, paramObjectCodec);
    this._ioContext = paramIOContext;
    this._outputStream = paramOutputStream;
    this._bufferRecyclable = paramBoolean;
    this._outputTail = paramInt2;
    this._outputBuffer = paramArrayOfByte;
    this._outputEnd = this._outputBuffer.length;
    this._outputMaxContiguous = (this._outputEnd >> 3);
    this._charBuffer = paramIOContext.allocConcatBuffer();
    this._charBufferLength = this._charBuffer.length;
    if (isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII))
      setHighestNonEscapedChar(127);
  }

  private int _handleLongCustomEscape(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
    throws IOException, JsonGenerationException
  {
    int i = paramArrayOfByte2.length;
    if (paramInt1 + i > paramInt2)
    {
      this._outputTail = paramInt1;
      _flushBuffer();
      int j = this._outputTail;
      if (i > paramArrayOfByte1.length)
      {
        this._outputStream.write(paramArrayOfByte2, 0, i);
        return j;
      }
      System.arraycopy(paramArrayOfByte2, 0, paramArrayOfByte1, j, i);
      paramInt1 = j + i;
    }
    if (paramInt1 + paramInt3 * 6 > paramInt2)
    {
      _flushBuffer();
      return this._outputTail;
    }
    return paramInt1;
  }

  private final int _outputMultiByteChar(int paramInt1, int paramInt2)
    throws IOException
  {
    byte[] arrayOfByte = this._outputBuffer;
    if ((paramInt1 >= 55296) && (paramInt1 <= 57343))
    {
      int m = paramInt2 + 1;
      arrayOfByte[paramInt2] = 92;
      int n = m + 1;
      arrayOfByte[m] = 117;
      int i1 = n + 1;
      arrayOfByte[n] = HEX_CHARS[(0xF & paramInt1 >> 12)];
      int i2 = i1 + 1;
      arrayOfByte[i1] = HEX_CHARS[(0xF & paramInt1 >> 8)];
      int i3 = i2 + 1;
      arrayOfByte[i2] = HEX_CHARS[(0xF & paramInt1 >> 4)];
      int i4 = i3 + 1;
      arrayOfByte[i3] = HEX_CHARS[(paramInt1 & 0xF)];
      return i4;
    }
    int i = paramInt2 + 1;
    arrayOfByte[paramInt2] = (byte)(0xE0 | paramInt1 >> 12);
    int j = i + 1;
    arrayOfByte[i] = (byte)(0x80 | 0x3F & paramInt1 >> 6);
    int k = j + 1;
    arrayOfByte[j] = (byte)(0x80 | paramInt1 & 0x3F);
    return k;
  }

  private final int _outputRawMultiByteChar(int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3)
    throws IOException
  {
    if ((paramInt1 >= 55296) && (paramInt1 <= 57343))
    {
      if (paramInt2 >= paramInt3)
        _reportError("Split surrogate on writeRaw() input (last character)");
      _outputSurrogates(paramInt1, paramArrayOfChar[paramInt2]);
      return paramInt2 + 1;
    }
    byte[] arrayOfByte = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfByte[i] = (byte)(0xE0 | paramInt1 >> 12);
    int j = this._outputTail;
    this._outputTail = (j + 1);
    arrayOfByte[j] = (byte)(0x80 | 0x3F & paramInt1 >> 6);
    int k = this._outputTail;
    this._outputTail = (k + 1);
    arrayOfByte[k] = (byte)(0x80 | paramInt1 & 0x3F);
    return paramInt2;
  }

  private final void _writeBytes(byte[] paramArrayOfByte)
    throws IOException
  {
    int i = paramArrayOfByte.length;
    if (i + this._outputTail > this._outputEnd)
    {
      _flushBuffer();
      if (i > 512)
      {
        this._outputStream.write(paramArrayOfByte, 0, i);
        return;
      }
    }
    System.arraycopy(paramArrayOfByte, 0, this._outputBuffer, this._outputTail, i);
    this._outputTail = (i + this._outputTail);
  }

  private final void _writeBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 + this._outputTail > this._outputEnd)
    {
      _flushBuffer();
      if (paramInt2 > 512)
      {
        this._outputStream.write(paramArrayOfByte, paramInt1, paramInt2);
        return;
      }
    }
    System.arraycopy(paramArrayOfByte, paramInt1, this._outputBuffer, this._outputTail, paramInt2);
    this._outputTail = (paramInt2 + this._outputTail);
  }

  private int _writeCustomEscape(byte[] paramArrayOfByte, int paramInt1, SerializableString paramSerializableString, int paramInt2)
    throws IOException, JsonGenerationException
  {
    byte[] arrayOfByte = paramSerializableString.asUnquotedUTF8();
    int i = arrayOfByte.length;
    if (i > 6)
      return _handleLongCustomEscape(paramArrayOfByte, paramInt1, this._outputEnd, arrayOfByte, paramInt2);
    System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt1, i);
    return paramInt1 + i;
  }

  private final void _writeCustomStringSegment2(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    if (this._outputTail + 6 * (paramInt2 - paramInt1) > this._outputEnd)
      _flushBuffer();
    int i = this._outputTail;
    byte[] arrayOfByte = this._outputBuffer;
    int[] arrayOfInt = this._outputEscapes;
    int j;
    CharacterEscapes localCharacterEscapes;
    int k;
    int m;
    if (this._maximumNonEscapedChar <= 0)
    {
      j = 65535;
      localCharacterEscapes = this._characterEscapes;
      k = i;
      m = paramInt1;
    }
    int n;
    int i1;
    while (true)
    {
      if (m >= paramInt2)
        break label413;
      n = m + 1;
      i1 = paramArrayOfChar[m];
      if (i1 <= 127)
      {
        if (arrayOfInt[i1] == 0)
        {
          int i7 = k + 1;
          arrayOfByte[k] = (byte)i1;
          k = i7;
          m = n;
          continue;
          j = this._maximumNonEscapedChar;
          break;
        }
        int i5 = arrayOfInt[i1];
        if (i5 > 0)
        {
          int i6 = k + 1;
          arrayOfByte[k] = 92;
          k = i6 + 1;
          arrayOfByte[i6] = (byte)i5;
          m = n;
          continue;
        }
        if (i5 == -2)
        {
          SerializableString localSerializableString2 = localCharacterEscapes.getEscapeSequence(i1);
          if (localSerializableString2 == null)
            throw new JsonGenerationException("Invalid custom escape definitions; custom escape not found for character code 0x" + Integer.toHexString(i1) + ", although was supposed to have one");
          k = _writeCustomEscape(arrayOfByte, k, localSerializableString2, paramInt2 - n);
          m = n;
          continue;
        }
        k = _writeGenericEscape(i1, k);
        m = n;
        continue;
      }
      if (i1 > j)
      {
        k = _writeGenericEscape(i1, k);
        m = n;
        continue;
      }
      SerializableString localSerializableString1 = localCharacterEscapes.getEscapeSequence(i1);
      if (localSerializableString1 == null)
        break label335;
      k = _writeCustomEscape(arrayOfByte, k, localSerializableString1, paramInt2 - n);
      m = n;
    }
    label335: int i4;
    if (i1 <= 2047)
    {
      int i3 = k + 1;
      arrayOfByte[k] = (byte)(0xC0 | i1 >> 6);
      i4 = i3 + 1;
      arrayOfByte[i3] = (byte)(0x80 | i1 & 0x3F);
    }
    for (int i2 = i4; ; i2 = _outputMultiByteChar(i1, k))
    {
      k = i2;
      m = n;
      break;
    }
    label413: this._outputTail = k;
  }

  private int _writeGenericEscape(int paramInt1, int paramInt2)
    throws IOException
  {
    byte[] arrayOfByte = this._outputBuffer;
    int i = paramInt2 + 1;
    arrayOfByte[paramInt2] = 92;
    int j = i + 1;
    arrayOfByte[i] = 117;
    int m;
    if (paramInt1 > 255)
    {
      int i2 = 0xFF & paramInt1 >> 8;
      int i3 = j + 1;
      arrayOfByte[j] = HEX_CHARS[(i2 >> 4)];
      m = i3 + 1;
      arrayOfByte[i3] = HEX_CHARS[(i2 & 0xF)];
      paramInt1 &= 255;
    }
    while (true)
    {
      int n = m + 1;
      arrayOfByte[m] = HEX_CHARS[(paramInt1 >> 4)];
      int i1 = n + 1;
      arrayOfByte[n] = HEX_CHARS[(paramInt1 & 0xF)];
      return i1;
      int k = j + 1;
      arrayOfByte[j] = 48;
      m = k + 1;
      arrayOfByte[k] = 48;
    }
  }

  private final void _writeLongString(String paramString)
    throws IOException, JsonGenerationException
  {
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte1 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfByte1[i] = 34;
    _writeStringSegments(paramString);
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte2 = this._outputBuffer;
    int j = this._outputTail;
    this._outputTail = (j + 1);
    arrayOfByte2[j] = 34;
  }

  private final void _writeLongString(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte1 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfByte1[i] = 34;
    _writeStringSegments(this._charBuffer, 0, paramInt2);
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte2 = this._outputBuffer;
    int j = this._outputTail;
    this._outputTail = (j + 1);
    arrayOfByte2[j] = 34;
  }

  private final void _writeNull()
    throws IOException
  {
    if (4 + this._outputTail >= this._outputEnd)
      _flushBuffer();
    System.arraycopy(NULL_BYTES, 0, this._outputBuffer, this._outputTail, 4);
    this._outputTail = (4 + this._outputTail);
  }

  private final void _writeQuotedInt(int paramInt)
    throws IOException
  {
    if (13 + this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte1 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfByte1[i] = 34;
    this._outputTail = NumberOutput.outputInt(paramInt, this._outputBuffer, this._outputTail);
    byte[] arrayOfByte2 = this._outputBuffer;
    int j = this._outputTail;
    this._outputTail = (j + 1);
    arrayOfByte2[j] = 34;
  }

  private final void _writeQuotedLong(long paramLong)
    throws IOException
  {
    if (23 + this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte1 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfByte1[i] = 34;
    this._outputTail = NumberOutput.outputLong(paramLong, this._outputBuffer, this._outputTail);
    byte[] arrayOfByte2 = this._outputBuffer;
    int j = this._outputTail;
    this._outputTail = (j + 1);
    arrayOfByte2[j] = 34;
  }

  private final void _writeQuotedRaw(Object paramObject)
    throws IOException
  {
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte1 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfByte1[i] = 34;
    writeRaw(paramObject.toString());
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte2 = this._outputBuffer;
    int j = this._outputTail;
    this._outputTail = (j + 1);
    arrayOfByte2[j] = 34;
  }

  private final void _writeSegmentedRaw(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    int i = this._outputEnd;
    byte[] arrayOfByte = this._outputBuffer;
    label17: int j;
    int m;
    int n;
    if (paramInt1 < paramInt2)
    {
      j = paramArrayOfChar[paramInt1];
      if (j >= 128)
      {
        if (3 + this._outputTail >= this._outputEnd)
          _flushBuffer();
        m = paramInt1 + 1;
        n = paramArrayOfChar[paramInt1];
        if (n >= 2048)
          break label173;
        int i1 = this._outputTail;
        this._outputTail = (i1 + 1);
        arrayOfByte[i1] = (byte)(0xC0 | n >> 6);
        int i2 = this._outputTail;
        this._outputTail = (i2 + 1);
        arrayOfByte[i2] = (byte)(0x80 | n & 0x3F);
      }
    }
    while (true)
    {
      paramInt1 = m;
      break;
      if (this._outputTail >= i)
        _flushBuffer();
      int k = this._outputTail;
      this._outputTail = (k + 1);
      arrayOfByte[k] = (byte)j;
      paramInt1++;
      if (paramInt1 < paramInt2)
        break label17;
      return;
      label173: _outputRawMultiByteChar(n, paramArrayOfChar, m, paramInt2);
    }
  }

  private final void _writeStringSegment(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    int i = paramInt2 + paramInt1;
    int j = this._outputTail;
    byte[] arrayOfByte = this._outputBuffer;
    int[] arrayOfInt = this._outputEscapes;
    int n;
    for (int k = j; ; k = n)
    {
      int m;
      if (paramInt1 < i)
      {
        m = paramArrayOfChar[paramInt1];
        if ((m <= 127) && (arrayOfInt[m] == 0));
      }
      else
      {
        this._outputTail = k;
        if (paramInt1 < i)
        {
          if (this._characterEscapes == null)
            break;
          _writeCustomStringSegment2(paramArrayOfChar, paramInt1, i);
        }
        return;
      }
      n = k + 1;
      arrayOfByte[k] = (byte)m;
      paramInt1++;
    }
    if (this._maximumNonEscapedChar == 0)
    {
      _writeStringSegment2(paramArrayOfChar, paramInt1, i);
      return;
    }
    _writeStringSegmentASCII2(paramArrayOfChar, paramInt1, i);
  }

  private final void _writeStringSegment2(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    if (this._outputTail + 6 * (paramInt2 - paramInt1) > this._outputEnd)
      _flushBuffer();
    int i = this._outputTail;
    byte[] arrayOfByte = this._outputBuffer;
    int[] arrayOfInt = this._outputEscapes;
    int j = i;
    int k = paramInt1;
    while (k < paramInt2)
    {
      int m = k + 1;
      int n = paramArrayOfChar[k];
      if (n <= 127)
      {
        if (arrayOfInt[n] == 0)
        {
          int i6 = j + 1;
          arrayOfByte[j] = (byte)n;
          j = i6;
          k = m;
          continue;
        }
        int i4 = arrayOfInt[n];
        if (i4 > 0)
        {
          int i5 = j + 1;
          arrayOfByte[j] = 92;
          j = i5 + 1;
          arrayOfByte[i5] = (byte)i4;
          k = m;
          continue;
        }
        j = _writeGenericEscape(n, j);
        k = m;
        continue;
      }
      int i3;
      if (n <= 2047)
      {
        int i2 = j + 1;
        arrayOfByte[j] = (byte)(0xC0 | n >> 6);
        i3 = i2 + 1;
        arrayOfByte[i2] = (byte)(0x80 | n & 0x3F);
      }
      for (int i1 = i3; ; i1 = _outputMultiByteChar(n, j))
      {
        j = i1;
        k = m;
        break;
      }
    }
    this._outputTail = j;
  }

  private final void _writeStringSegmentASCII2(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    if (this._outputTail + 6 * (paramInt2 - paramInt1) > this._outputEnd)
      _flushBuffer();
    int i = this._outputTail;
    byte[] arrayOfByte = this._outputBuffer;
    int[] arrayOfInt = this._outputEscapes;
    int j = this._maximumNonEscapedChar;
    int k = i;
    int m = paramInt1;
    while (m < paramInt2)
    {
      int n = m + 1;
      int i1 = paramArrayOfChar[m];
      if (i1 <= 127)
      {
        if (arrayOfInt[i1] == 0)
        {
          int i7 = k + 1;
          arrayOfByte[k] = (byte)i1;
          k = i7;
          m = n;
          continue;
        }
        int i5 = arrayOfInt[i1];
        if (i5 > 0)
        {
          int i6 = k + 1;
          arrayOfByte[k] = 92;
          k = i6 + 1;
          arrayOfByte[i6] = (byte)i5;
          m = n;
          continue;
        }
        k = _writeGenericEscape(i1, k);
        m = n;
        continue;
      }
      if (i1 > j)
      {
        k = _writeGenericEscape(i1, k);
        m = n;
        continue;
      }
      int i4;
      if (i1 <= 2047)
      {
        int i3 = k + 1;
        arrayOfByte[k] = (byte)(0xC0 | i1 >> 6);
        i4 = i3 + 1;
        arrayOfByte[i3] = (byte)(0x80 | i1 & 0x3F);
      }
      for (int i2 = i4; ; i2 = _outputMultiByteChar(i1, k))
      {
        k = i2;
        m = n;
        break;
      }
    }
    this._outputTail = k;
  }

  private final void _writeStringSegments(String paramString)
    throws IOException, JsonGenerationException
  {
    int i = paramString.length();
    int j = 0;
    char[] arrayOfChar = this._charBuffer;
    while (i > 0)
    {
      int k = Math.min(this._outputMaxContiguous, i);
      paramString.getChars(j, j + k, arrayOfChar, 0);
      if (k + this._outputTail > this._outputEnd)
        _flushBuffer();
      _writeStringSegment(arrayOfChar, 0, k);
      j += k;
      i -= k;
    }
  }

  private final void _writeStringSegments(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    do
    {
      int i = Math.min(this._outputMaxContiguous, paramInt2);
      if (i + this._outputTail > this._outputEnd)
        _flushBuffer();
      _writeStringSegment(paramArrayOfChar, paramInt1, i);
      paramInt1 += i;
      paramInt2 -= i;
    }
    while (paramInt2 > 0);
  }

  private final void _writeUTF8Segment(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    int[] arrayOfInt = this._outputEscapes;
    int i = paramInt1 + paramInt2;
    int k;
    for (int j = paramInt1; j < i; j = k)
    {
      k = j + 1;
      int m = paramArrayOfByte[j];
      if ((m < 0) || (arrayOfInt[m] == 0))
        continue;
      _writeUTF8Segment2(paramArrayOfByte, paramInt1, paramInt2);
      return;
    }
    if (paramInt2 + this._outputTail > this._outputEnd)
      _flushBuffer();
    System.arraycopy(paramArrayOfByte, paramInt1, this._outputBuffer, this._outputTail, paramInt2);
    this._outputTail = (paramInt2 + this._outputTail);
  }

  private final void _writeUTF8Segment2(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    int i = this._outputTail;
    if (i + paramInt2 * 6 > this._outputEnd)
    {
      _flushBuffer();
      i = this._outputTail;
    }
    byte[] arrayOfByte = this._outputBuffer;
    int[] arrayOfInt = this._outputEscapes;
    int j = paramInt2 + paramInt1;
    int k = i;
    int m = paramInt1;
    while (m < j)
    {
      int n = m + 1;
      int i1 = paramArrayOfByte[m];
      if ((i1 < 0) || (arrayOfInt[i1] == 0))
      {
        int i2 = k + 1;
        arrayOfByte[k] = i1;
        k = i2;
        m = n;
        continue;
      }
      int i3 = arrayOfInt[i1];
      int i6;
      if (i3 > 0)
      {
        int i5 = k + 1;
        arrayOfByte[k] = 92;
        i6 = i5 + 1;
        arrayOfByte[i5] = (byte)i3;
      }
      for (int i4 = i6; ; i4 = _writeGenericEscape(i1, k))
      {
        k = i4;
        m = n;
        break;
      }
    }
    this._outputTail = k;
  }

  private final void _writeUTF8Segments(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    do
    {
      int i = Math.min(this._outputMaxContiguous, paramInt2);
      _writeUTF8Segment(paramArrayOfByte, paramInt1, i);
      paramInt1 += i;
      paramInt2 -= i;
    }
    while (paramInt2 > 0);
  }

  protected final int _decodeSurrogate(int paramInt1, int paramInt2)
    throws IOException
  {
    if ((paramInt2 < 56320) || (paramInt2 > 57343))
      _reportError("Incomplete surrogate pair: first char 0x" + Integer.toHexString(paramInt1) + ", second 0x" + Integer.toHexString(paramInt2));
    return 65536 + (paramInt1 - 55296 << 10) + (paramInt2 - 56320);
  }

  protected final void _flushBuffer()
    throws IOException
  {
    int i = this._outputTail;
    if (i > 0)
    {
      this._outputTail = 0;
      this._outputStream.write(this._outputBuffer, 0, i);
    }
  }

  protected final void _outputSurrogates(int paramInt1, int paramInt2)
    throws IOException
  {
    int i = _decodeSurrogate(paramInt1, paramInt2);
    if (4 + this._outputTail > this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte = this._outputBuffer;
    int j = this._outputTail;
    this._outputTail = (j + 1);
    arrayOfByte[j] = (byte)(0xF0 | i >> 18);
    int k = this._outputTail;
    this._outputTail = (k + 1);
    arrayOfByte[k] = (byte)(0x80 | 0x3F & i >> 12);
    int m = this._outputTail;
    this._outputTail = (m + 1);
    arrayOfByte[m] = (byte)(0x80 | 0x3F & i >> 6);
    int n = this._outputTail;
    this._outputTail = (n + 1);
    arrayOfByte[n] = (byte)(0x80 | i & 0x3F);
  }

  protected void _releaseBuffers()
  {
    byte[] arrayOfByte = this._outputBuffer;
    if ((arrayOfByte != null) && (this._bufferRecyclable))
    {
      this._outputBuffer = null;
      this._ioContext.releaseWriteEncodingBuffer(arrayOfByte);
    }
    char[] arrayOfChar = this._charBuffer;
    if (arrayOfChar != null)
    {
      this._charBuffer = null;
      this._ioContext.releaseConcatBuffer(arrayOfChar);
    }
  }

  protected final void _verifyPrettyValueWrite(String paramString, int paramInt)
    throws IOException, JsonGenerationException
  {
    switch (paramInt)
    {
    default:
      _cantHappen();
    case 1:
    case 2:
    case 3:
    case 0:
    }
    do
    {
      return;
      this._cfgPrettyPrinter.writeArrayValueSeparator(this);
      return;
      this._cfgPrettyPrinter.writeObjectFieldValueSeparator(this);
      return;
      this._cfgPrettyPrinter.writeRootValueSeparator(this);
      return;
      if (!this._writeContext.inArray())
        continue;
      this._cfgPrettyPrinter.beforeArrayValues(this);
      return;
    }
    while (!this._writeContext.inObject());
    this._cfgPrettyPrinter.beforeObjectEntries(this);
  }

  protected final void _verifyValueWrite(String paramString)
    throws IOException, JsonGenerationException
  {
    int i = this._writeContext.writeValue();
    if (i == 5)
      _reportError("Can not " + paramString + ", expecting field name");
    if (this._cfgPrettyPrinter == null)
    {
      int j;
      switch (i)
      {
      default:
        return;
      case 1:
        j = 44;
      case 2:
      case 3:
      }
      while (true)
      {
        if (this._outputTail >= this._outputEnd)
          _flushBuffer();
        this._outputBuffer[this._outputTail] = j;
        this._outputTail = (1 + this._outputTail);
        return;
        j = 58;
        continue;
        j = 32;
      }
    }
    _verifyPrettyValueWrite(paramString, i);
  }

  protected void _writeBinary(Base64Variant paramBase64Variant, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    int i = paramInt2 - 3;
    int j = -6 + this._outputEnd;
    int k = paramBase64Variant.getMaxLineLength() >> 2;
    int i7;
    for (int m = paramInt1; m <= i; m = i7)
    {
      if (this._outputTail > j)
        _flushBuffer();
      int i3 = m + 1;
      int i4 = paramArrayOfByte[m] << 8;
      int i5 = i3 + 1;
      int i6 = (i4 | 0xFF & paramArrayOfByte[i3]) << 8;
      i7 = i5 + 1;
      this._outputTail = paramBase64Variant.encodeBase64Chunk(i6 | 0xFF & paramArrayOfByte[i5], this._outputBuffer, this._outputTail);
      k--;
      if (k > 0)
        continue;
      byte[] arrayOfByte1 = this._outputBuffer;
      int i8 = this._outputTail;
      this._outputTail = (i8 + 1);
      arrayOfByte1[i8] = 92;
      byte[] arrayOfByte2 = this._outputBuffer;
      int i9 = this._outputTail;
      this._outputTail = (i9 + 1);
      arrayOfByte2[i9] = 110;
      k = paramBase64Variant.getMaxLineLength() >> 2;
    }
    int n = paramInt2 - m;
    if (n > 0)
    {
      if (this._outputTail > j)
        _flushBuffer();
      int i1 = m + 1;
      int i2 = paramArrayOfByte[m] << 16;
      if (n == 2)
      {
        (i1 + 1);
        i2 |= (0xFF & paramArrayOfByte[i1]) << 8;
      }
      this._outputTail = paramBase64Variant.encodeBase64Partial(i2, n, this._outputBuffer, this._outputTail);
      return;
    }
  }

  protected final void _writeFieldName(String paramString)
    throws IOException, JsonGenerationException
  {
    if (!isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES))
    {
      _writeStringSegments(paramString);
      return;
    }
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte1 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfByte1[i] = 34;
    int j = paramString.length();
    if (j <= this._charBufferLength)
    {
      paramString.getChars(0, j, this._charBuffer, 0);
      if (j <= this._outputMaxContiguous)
      {
        if (j + this._outputTail > this._outputEnd)
          _flushBuffer();
        _writeStringSegment(this._charBuffer, 0, j);
      }
    }
    while (true)
    {
      if (this._outputTail >= this._outputEnd)
        _flushBuffer();
      byte[] arrayOfByte2 = this._outputBuffer;
      int k = this._outputTail;
      this._outputTail = (k + 1);
      arrayOfByte2[k] = 34;
      return;
      _writeStringSegments(this._charBuffer, 0, j);
      continue;
      _writeStringSegments(paramString);
    }
  }

  protected final void _writeFieldName(SerializableString paramSerializableString)
    throws IOException, JsonGenerationException
  {
    byte[] arrayOfByte1 = paramSerializableString.asQuotedUTF8();
    if (!isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES))
    {
      _writeBytes(arrayOfByte1);
      return;
    }
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte2 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfByte2[i] = 34;
    int j = arrayOfByte1.length;
    if (1 + (j + this._outputTail) < this._outputEnd)
    {
      System.arraycopy(arrayOfByte1, 0, this._outputBuffer, this._outputTail, j);
      this._outputTail = (j + this._outputTail);
      byte[] arrayOfByte4 = this._outputBuffer;
      int m = this._outputTail;
      this._outputTail = (m + 1);
      arrayOfByte4[m] = 34;
      return;
    }
    _writeBytes(arrayOfByte1);
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte3 = this._outputBuffer;
    int k = this._outputTail;
    this._outputTail = (k + 1);
    arrayOfByte3[k] = 34;
  }

  protected final void _writePPFieldName(String paramString, boolean paramBoolean)
    throws IOException, JsonGenerationException
  {
    int j;
    if (paramBoolean)
    {
      this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
      if (!isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES))
        break label207;
      if (this._outputTail >= this._outputEnd)
        _flushBuffer();
      byte[] arrayOfByte1 = this._outputBuffer;
      int i = this._outputTail;
      this._outputTail = (i + 1);
      arrayOfByte1[i] = 34;
      j = paramString.length();
      if (j > this._charBufferLength)
        break label199;
      paramString.getChars(0, j, this._charBuffer, 0);
      if (j > this._outputMaxContiguous)
        break label185;
      if (j + this._outputTail > this._outputEnd)
        _flushBuffer();
      _writeStringSegment(this._charBuffer, 0, j);
    }
    while (true)
    {
      if (this._outputTail >= this._outputEnd)
        _flushBuffer();
      byte[] arrayOfByte2 = this._outputBuffer;
      int k = this._outputTail;
      this._outputTail = (k + 1);
      arrayOfByte2[k] = 34;
      return;
      this._cfgPrettyPrinter.beforeObjectEntries(this);
      break;
      label185: _writeStringSegments(this._charBuffer, 0, j);
      continue;
      label199: _writeStringSegments(paramString);
    }
    label207: _writeStringSegments(paramString);
  }

  protected final void _writePPFieldName(SerializableString paramSerializableString, boolean paramBoolean)
    throws IOException, JsonGenerationException
  {
    if (paramBoolean)
      this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
    while (true)
    {
      boolean bool = isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
      if (bool)
      {
        if (this._outputTail >= this._outputEnd)
          _flushBuffer();
        byte[] arrayOfByte2 = this._outputBuffer;
        int j = this._outputTail;
        this._outputTail = (j + 1);
        arrayOfByte2[j] = 34;
      }
      _writeBytes(paramSerializableString.asQuotedUTF8());
      if (bool)
      {
        if (this._outputTail >= this._outputEnd)
          _flushBuffer();
        byte[] arrayOfByte1 = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = (i + 1);
        arrayOfByte1[i] = 34;
      }
      return;
      this._cfgPrettyPrinter.beforeObjectEntries(this);
    }
  }

  public void close()
    throws IOException
  {
    super.close();
    if ((this._outputBuffer != null) && (isEnabled(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT)))
      while (true)
      {
        JsonWriteContext localJsonWriteContext = getOutputContext();
        if (localJsonWriteContext.inArray())
        {
          writeEndArray();
          continue;
        }
        if (!localJsonWriteContext.inObject())
          break;
        writeEndObject();
      }
    _flushBuffer();
    if ((this._ioContext.isResourceManaged()) || (isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET)))
      this._outputStream.close();
    while (true)
    {
      _releaseBuffers();
      return;
      this._outputStream.flush();
    }
  }

  public final void flush()
    throws IOException
  {
    _flushBuffer();
    if ((this._outputStream != null) && (isEnabled(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM)))
      this._outputStream.flush();
  }

  public CharacterEscapes getCharacterEscapes()
  {
    return this._characterEscapes;
  }

  public int getHighestEscapedChar()
  {
    return this._maximumNonEscapedChar;
  }

  public Object getOutputTarget()
  {
    return this._outputStream;
  }

  public JsonGenerator setCharacterEscapes(CharacterEscapes paramCharacterEscapes)
  {
    this._characterEscapes = paramCharacterEscapes;
    if (paramCharacterEscapes == null)
    {
      this._outputEscapes = sOutputEscapes;
      return this;
    }
    this._outputEscapes = paramCharacterEscapes.getEscapeCodesForAscii();
    return this;
  }

  public JsonGenerator setHighestNonEscapedChar(int paramInt)
  {
    if (paramInt < 0)
      paramInt = 0;
    this._maximumNonEscapedChar = paramInt;
    return this;
  }

  public void writeBinary(Base64Variant paramBase64Variant, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("write binary value");
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte1 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfByte1[i] = 34;
    _writeBinary(paramBase64Variant, paramArrayOfByte, paramInt1, paramInt1 + paramInt2);
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte2 = this._outputBuffer;
    int j = this._outputTail;
    this._outputTail = (j + 1);
    arrayOfByte2[j] = 34;
  }

  public void writeBoolean(boolean paramBoolean)
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("write boolean value");
    if (5 + this._outputTail >= this._outputEnd)
      _flushBuffer();
    if (paramBoolean);
    for (byte[] arrayOfByte = TRUE_BYTES; ; arrayOfByte = FALSE_BYTES)
    {
      int i = arrayOfByte.length;
      System.arraycopy(arrayOfByte, 0, this._outputBuffer, this._outputTail, i);
      this._outputTail = (i + this._outputTail);
      return;
    }
  }

  public final void writeEndArray()
    throws IOException, JsonGenerationException
  {
    if (!this._writeContext.inArray())
      _reportError("Current context not an ARRAY but " + this._writeContext.getTypeDesc());
    if (this._cfgPrettyPrinter != null)
      this._cfgPrettyPrinter.writeEndArray(this, this._writeContext.getEntryCount());
    while (true)
    {
      this._writeContext = this._writeContext.getParent();
      return;
      if (this._outputTail >= this._outputEnd)
        _flushBuffer();
      byte[] arrayOfByte = this._outputBuffer;
      int i = this._outputTail;
      this._outputTail = (i + 1);
      arrayOfByte[i] = 93;
    }
  }

  public final void writeEndObject()
    throws IOException, JsonGenerationException
  {
    if (!this._writeContext.inObject())
      _reportError("Current context not an object but " + this._writeContext.getTypeDesc());
    this._writeContext = this._writeContext.getParent();
    if (this._cfgPrettyPrinter != null)
    {
      this._cfgPrettyPrinter.writeEndObject(this, this._writeContext.getEntryCount());
      return;
    }
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfByte[i] = 125;
  }

  public final void writeFieldName(String paramString)
    throws IOException, JsonGenerationException
  {
    int i = 1;
    int j = this._writeContext.writeFieldName(paramString);
    if (j == 4)
      _reportError("Can not write a field name, expecting a value");
    if (this._cfgPrettyPrinter != null)
    {
      if (j == i);
      while (true)
      {
        _writePPFieldName(paramString, i);
        return;
        i = 0;
      }
    }
    if (j == i)
    {
      if (this._outputTail >= this._outputEnd)
        _flushBuffer();
      byte[] arrayOfByte = this._outputBuffer;
      int k = this._outputTail;
      this._outputTail = (k + 1);
      arrayOfByte[k] = 44;
    }
    _writeFieldName(paramString);
  }

  public final void writeFieldName(SerializableString paramSerializableString)
    throws IOException, JsonGenerationException
  {
    int i = 1;
    int j = this._writeContext.writeFieldName(paramSerializableString.getValue());
    if (j == 4)
      _reportError("Can not write a field name, expecting a value");
    if (this._cfgPrettyPrinter != null)
    {
      if (j == i);
      while (true)
      {
        _writePPFieldName(paramSerializableString, i);
        return;
        i = 0;
      }
    }
    if (j == i)
    {
      if (this._outputTail >= this._outputEnd)
        _flushBuffer();
      byte[] arrayOfByte = this._outputBuffer;
      int k = this._outputTail;
      this._outputTail = (k + 1);
      arrayOfByte[k] = 44;
    }
    _writeFieldName(paramSerializableString);
  }

  public final void writeFieldName(SerializedString paramSerializedString)
    throws IOException, JsonGenerationException
  {
    int i = 1;
    int j = this._writeContext.writeFieldName(paramSerializedString.getValue());
    if (j == 4)
      _reportError("Can not write a field name, expecting a value");
    if (this._cfgPrettyPrinter != null)
    {
      if (j == i);
      while (true)
      {
        _writePPFieldName(paramSerializedString, i);
        return;
        i = 0;
      }
    }
    if (j == i)
    {
      if (this._outputTail >= this._outputEnd)
        _flushBuffer();
      byte[] arrayOfByte = this._outputBuffer;
      int k = this._outputTail;
      this._outputTail = (k + 1);
      arrayOfByte[k] = 44;
    }
    _writeFieldName(paramSerializedString);
  }

  public void writeNull()
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("write null value");
    _writeNull();
  }

  public void writeNumber(double paramDouble)
    throws IOException, JsonGenerationException
  {
    if ((this._cfgNumbersAsStrings) || (((Double.isNaN(paramDouble)) || (Double.isInfinite(paramDouble))) && (isEnabled(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS))))
    {
      writeString(String.valueOf(paramDouble));
      return;
    }
    _verifyValueWrite("write number");
    writeRaw(String.valueOf(paramDouble));
  }

  public void writeNumber(float paramFloat)
    throws IOException, JsonGenerationException
  {
    if ((this._cfgNumbersAsStrings) || (((Float.isNaN(paramFloat)) || (Float.isInfinite(paramFloat))) && (isEnabled(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS))))
    {
      writeString(String.valueOf(paramFloat));
      return;
    }
    _verifyValueWrite("write number");
    writeRaw(String.valueOf(paramFloat));
  }

  public void writeNumber(int paramInt)
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("write number");
    if (11 + this._outputTail >= this._outputEnd)
      _flushBuffer();
    if (this._cfgNumbersAsStrings)
    {
      _writeQuotedInt(paramInt);
      return;
    }
    this._outputTail = NumberOutput.outputInt(paramInt, this._outputBuffer, this._outputTail);
  }

  public void writeNumber(long paramLong)
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("write number");
    if (this._cfgNumbersAsStrings)
    {
      _writeQuotedLong(paramLong);
      return;
    }
    if (21 + this._outputTail >= this._outputEnd)
      _flushBuffer();
    this._outputTail = NumberOutput.outputLong(paramLong, this._outputBuffer, this._outputTail);
  }

  public void writeNumber(String paramString)
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("write number");
    if (this._cfgNumbersAsStrings)
    {
      _writeQuotedRaw(paramString);
      return;
    }
    writeRaw(paramString);
  }

  public void writeNumber(BigDecimal paramBigDecimal)
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("write number");
    if (paramBigDecimal == null)
    {
      _writeNull();
      return;
    }
    if (this._cfgNumbersAsStrings)
    {
      _writeQuotedRaw(paramBigDecimal);
      return;
    }
    writeRaw(paramBigDecimal.toString());
  }

  public void writeNumber(BigInteger paramBigInteger)
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("write number");
    if (paramBigInteger == null)
    {
      _writeNull();
      return;
    }
    if (this._cfgNumbersAsStrings)
    {
      _writeQuotedRaw(paramBigInteger);
      return;
    }
    writeRaw(paramBigInteger.toString());
  }

  public void writeRaw(char paramChar)
    throws IOException, JsonGenerationException
  {
    if (3 + this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte = this._outputBuffer;
    if (paramChar <= '')
    {
      int k = this._outputTail;
      this._outputTail = (k + 1);
      arrayOfByte[k] = (byte)paramChar;
      return;
    }
    if (paramChar < 'ࠀ')
    {
      int i = this._outputTail;
      this._outputTail = (i + 1);
      arrayOfByte[i] = (byte)(0xC0 | paramChar >> '\006');
      int j = this._outputTail;
      this._outputTail = (j + 1);
      arrayOfByte[j] = (byte)(0x80 | paramChar & 0x3F);
      return;
    }
    _outputRawMultiByteChar(paramChar, null, 0, 0);
  }

  public void writeRaw(String paramString)
    throws IOException, JsonGenerationException
  {
    int i = 0;
    int j = paramString.length();
    if (j > 0)
    {
      char[] arrayOfChar = this._charBuffer;
      int k = arrayOfChar.length;
      if (j < k);
      for (int m = j; ; m = k)
      {
        paramString.getChars(i, i + m, arrayOfChar, 0);
        writeRaw(arrayOfChar, 0, m);
        i += m;
        j -= m;
        break;
      }
    }
  }

  public void writeRaw(String paramString, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    if (paramInt2 > 0)
    {
      char[] arrayOfChar = this._charBuffer;
      int i = arrayOfChar.length;
      if (paramInt2 < i);
      for (int j = paramInt2; ; j = i)
      {
        paramString.getChars(paramInt1, paramInt1 + j, arrayOfChar, 0);
        writeRaw(arrayOfChar, 0, j);
        paramInt1 += j;
        paramInt2 -= j;
        break;
      }
    }
  }

  public final void writeRaw(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    int i = paramInt2 + (paramInt2 + paramInt2);
    if (i + this._outputTail > this._outputEnd)
    {
      if (this._outputEnd < i)
      {
        _writeSegmentedRaw(paramArrayOfChar, paramInt1, paramInt2);
        return;
      }
      _flushBuffer();
    }
    int j = paramInt2 + paramInt1;
    label47: label53: int k;
    int n;
    int i1;
    if (paramInt1 < j)
    {
      k = paramArrayOfChar[paramInt1];
      if (k <= 127)
        break label159;
      n = paramInt1 + 1;
      i1 = paramArrayOfChar[paramInt1];
      if (i1 >= 2048)
        break label197;
      byte[] arrayOfByte2 = this._outputBuffer;
      int i2 = this._outputTail;
      this._outputTail = (i2 + 1);
      arrayOfByte2[i2] = (byte)(0xC0 | i1 >> 6);
      byte[] arrayOfByte3 = this._outputBuffer;
      int i3 = this._outputTail;
      this._outputTail = (i3 + 1);
      arrayOfByte3[i3] = (byte)(0x80 | i1 & 0x3F);
    }
    while (true)
    {
      paramInt1 = n;
      break label47;
      break;
      label159: byte[] arrayOfByte1 = this._outputBuffer;
      int m = this._outputTail;
      this._outputTail = (m + 1);
      arrayOfByte1[m] = (byte)k;
      paramInt1++;
      if (paramInt1 < j)
        break label53;
      return;
      label197: _outputRawMultiByteChar(i1, paramArrayOfChar, n, j);
    }
  }

  public void writeRawUTF8String(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("write text value");
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte1 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfByte1[i] = 34;
    _writeBytes(paramArrayOfByte, paramInt1, paramInt2);
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte2 = this._outputBuffer;
    int j = this._outputTail;
    this._outputTail = (j + 1);
    arrayOfByte2[j] = 34;
  }

  public final void writeStartArray()
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("start an array");
    this._writeContext = this._writeContext.createChildArrayContext();
    if (this._cfgPrettyPrinter != null)
    {
      this._cfgPrettyPrinter.writeStartArray(this);
      return;
    }
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfByte[i] = 91;
  }

  public final void writeStartObject()
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("start an object");
    this._writeContext = this._writeContext.createChildObjectContext();
    if (this._cfgPrettyPrinter != null)
    {
      this._cfgPrettyPrinter.writeStartObject(this);
      return;
    }
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfByte[i] = 123;
  }

  public void writeString(String paramString)
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("write text value");
    if (paramString == null)
    {
      _writeNull();
      return;
    }
    int i = paramString.length();
    if (i > this._charBufferLength)
    {
      _writeLongString(paramString);
      return;
    }
    paramString.getChars(0, i, this._charBuffer, 0);
    if (i > this._outputMaxContiguous)
    {
      _writeLongString(this._charBuffer, 0, i);
      return;
    }
    if (2 + (i + this._outputTail) > this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte1 = this._outputBuffer;
    int j = this._outputTail;
    this._outputTail = (j + 1);
    arrayOfByte1[j] = 34;
    _writeStringSegment(this._charBuffer, 0, i);
    byte[] arrayOfByte2 = this._outputBuffer;
    int k = this._outputTail;
    this._outputTail = (k + 1);
    arrayOfByte2[k] = 34;
  }

  public final void writeString(SerializableString paramSerializableString)
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("write text value");
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte1 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfByte1[i] = 34;
    _writeBytes(paramSerializableString.asQuotedUTF8());
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte2 = this._outputBuffer;
    int j = this._outputTail;
    this._outputTail = (j + 1);
    arrayOfByte2[j] = 34;
  }

  public void writeString(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("write text value");
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte1 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfByte1[i] = 34;
    if (paramInt2 <= this._outputMaxContiguous)
    {
      if (paramInt2 + this._outputTail > this._outputEnd)
        _flushBuffer();
      _writeStringSegment(paramArrayOfChar, paramInt1, paramInt2);
    }
    while (true)
    {
      if (this._outputTail >= this._outputEnd)
        _flushBuffer();
      byte[] arrayOfByte2 = this._outputBuffer;
      int j = this._outputTail;
      this._outputTail = (j + 1);
      arrayOfByte2[j] = 34;
      return;
      _writeStringSegments(paramArrayOfChar, paramInt1, paramInt2);
    }
  }

  public final void writeStringField(String paramString1, String paramString2)
    throws IOException, JsonGenerationException
  {
    writeFieldName(paramString1);
    writeString(paramString2);
  }

  public void writeUTF8String(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("write text value");
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    byte[] arrayOfByte1 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfByte1[i] = 34;
    if (paramInt2 <= this._outputMaxContiguous)
      _writeUTF8Segment(paramArrayOfByte, paramInt1, paramInt2);
    while (true)
    {
      if (this._outputTail >= this._outputEnd)
        _flushBuffer();
      byte[] arrayOfByte2 = this._outputBuffer;
      int j = this._outputTail;
      this._outputTail = (j + 1);
      arrayOfByte2[j] = 34;
      return;
      _writeUTF8Segments(paramArrayOfByte, paramInt1, paramInt2);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.Utf8Generator
 * JD-Core Version:    0.6.0
 */