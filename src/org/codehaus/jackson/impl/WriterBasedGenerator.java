package org.codehaus.jackson.impl;

import java.io.IOException;
import java.io.Writer;
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

public final class WriterBasedGenerator extends JsonGeneratorBase
{
  protected static final char[] HEX_CHARS = CharTypes.copyHexChars();
  protected static final int SHORT_WRITE = 32;
  protected static final int[] sOutputEscapes = CharTypes.get7BitOutputEscapes();
  protected CharacterEscapes _characterEscapes;
  protected SerializableString _currentEscape;
  protected char[] _entityBuffer;
  protected final IOContext _ioContext;
  protected int _maximumNonEscapedChar;
  protected char[] _outputBuffer;
  protected int _outputEnd;
  protected int[] _outputEscapes = sOutputEscapes;
  protected int _outputHead = 0;
  protected int _outputTail = 0;
  protected final Writer _writer;

  public WriterBasedGenerator(IOContext paramIOContext, int paramInt, ObjectCodec paramObjectCodec, Writer paramWriter)
  {
    super(paramInt, paramObjectCodec);
    this._ioContext = paramIOContext;
    this._writer = paramWriter;
    this._outputBuffer = paramIOContext.allocConcatBuffer();
    this._outputEnd = this._outputBuffer.length;
    if (isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII))
      setHighestNonEscapedChar(127);
  }

  private char[] _allocateEntityBuffer()
  {
    char[] arrayOfChar = new char[14];
    arrayOfChar[0] = '\\';
    arrayOfChar[2] = '\\';
    arrayOfChar[3] = 'u';
    arrayOfChar[4] = '0';
    arrayOfChar[5] = '0';
    arrayOfChar[8] = '\\';
    arrayOfChar[9] = 'u';
    this._entityBuffer = arrayOfChar;
    return arrayOfChar;
  }

  private final void _appendCharacterEscape(char paramChar, int paramInt)
    throws IOException, JsonGenerationException
  {
    if (paramInt >= 0)
    {
      if (2 + this._outputTail > this._outputEnd)
        _flushBuffer();
      char[] arrayOfChar2 = this._outputBuffer;
      int i5 = this._outputTail;
      this._outputTail = (i5 + 1);
      arrayOfChar2[i5] = '\\';
      char[] arrayOfChar3 = this._outputBuffer;
      int i6 = this._outputTail;
      this._outputTail = (i6 + 1);
      arrayOfChar3[i6] = (char)paramInt;
      return;
    }
    if (paramInt != -2)
    {
      if (2 + this._outputTail > this._outputEnd)
        _flushBuffer();
      int j = this._outputTail;
      char[] arrayOfChar1 = this._outputBuffer;
      int k = j + 1;
      arrayOfChar1[j] = '\\';
      int m = k + 1;
      arrayOfChar1[k] = 'u';
      int i1;
      if (paramChar > 'ÿ')
      {
        int i3 = 0xFF & paramChar >> '\b';
        int i4 = m + 1;
        arrayOfChar1[m] = HEX_CHARS[(i3 >> 4)];
        i1 = i4 + 1;
        arrayOfChar1[i4] = HEX_CHARS[(i3 & 0xF)];
        paramChar = (char)(paramChar & 0xFF);
      }
      while (true)
      {
        int i2 = i1 + 1;
        arrayOfChar1[i1] = HEX_CHARS[(paramChar >> '\004')];
        arrayOfChar1[i2] = HEX_CHARS[(paramChar & 0xF)];
        this._outputTail = i2;
        return;
        int n = m + 1;
        arrayOfChar1[m] = '0';
        i1 = n + 1;
        arrayOfChar1[n] = '0';
      }
    }
    String str;
    if (this._currentEscape == null)
      str = this._characterEscapes.getEscapeSequence(paramChar).getValue();
    int i;
    while (true)
    {
      i = str.length();
      if (i + this._outputTail <= this._outputEnd)
        break;
      _flushBuffer();
      if (i <= this._outputEnd)
        break;
      this._writer.write(str);
      return;
      str = this._currentEscape.getValue();
      this._currentEscape = null;
    }
    str.getChars(0, i, this._outputBuffer, this._outputTail);
    this._outputTail = (i + this._outputTail);
  }

  private final int _prependOrWriteCharacterEscape(char[] paramArrayOfChar, int paramInt1, int paramInt2, char paramChar, int paramInt3)
    throws IOException, JsonGenerationException
  {
    if (paramInt3 >= 0)
    {
      if ((paramInt1 > 1) && (paramInt1 < paramInt2))
      {
        paramInt1 -= 2;
        paramArrayOfChar[paramInt1] = '\\';
        paramArrayOfChar[(paramInt1 + 1)] = (char)paramInt3;
      }
      while (true)
      {
        return paramInt1;
        char[] arrayOfChar2 = this._entityBuffer;
        if (arrayOfChar2 == null)
          arrayOfChar2 = _allocateEntityBuffer();
        arrayOfChar2[1] = (char)paramInt3;
        this._writer.write(arrayOfChar2, 0, 2);
      }
    }
    if (paramInt3 != -2)
    {
      int i1;
      int i3;
      if ((paramInt1 > 5) && (paramInt1 < paramInt2))
      {
        int m = paramInt1 - 6;
        int n = m + 1;
        paramArrayOfChar[m] = '\\';
        i1 = n + 1;
        paramArrayOfChar[n] = 'u';
        if (paramChar > 'ÿ')
        {
          int i5 = 0xFF & paramChar >> '\b';
          int i6 = i1 + 1;
          paramArrayOfChar[i1] = HEX_CHARS[(i5 >> 4)];
          i3 = i6 + 1;
          paramArrayOfChar[i6] = HEX_CHARS[(i5 & 0xF)];
          paramChar = (char)(paramChar & 0xFF);
          int i4 = i3 + 1;
          paramArrayOfChar[i3] = HEX_CHARS[(paramChar >> '\004')];
          paramArrayOfChar[i4] = HEX_CHARS[(paramChar & 0xF)];
          paramInt1 = i4 - 5;
        }
      }
      while (true)
      {
        return paramInt1;
        int i2 = i1 + 1;
        paramArrayOfChar[i1] = '0';
        i3 = i2 + 1;
        paramArrayOfChar[i2] = '0';
        break;
        char[] arrayOfChar1 = this._entityBuffer;
        if (arrayOfChar1 == null)
          arrayOfChar1 = _allocateEntityBuffer();
        this._outputHead = this._outputTail;
        if (paramChar > 'ÿ')
        {
          int j = 0xFF & paramChar >> '\b';
          int k = paramChar & 0xFF;
          arrayOfChar1[10] = HEX_CHARS[(j >> 4)];
          arrayOfChar1[11] = HEX_CHARS[(j & 0xF)];
          arrayOfChar1[12] = HEX_CHARS[(k >> 4)];
          arrayOfChar1[13] = HEX_CHARS[(k & 0xF)];
          this._writer.write(arrayOfChar1, 8, 6);
          continue;
        }
        arrayOfChar1[6] = HEX_CHARS[(paramChar >> '\004')];
        arrayOfChar1[7] = HEX_CHARS[(paramChar & 0xF)];
        this._writer.write(arrayOfChar1, 2, 6);
      }
    }
    String str;
    if (this._currentEscape == null)
    {
      str = this._characterEscapes.getEscapeSequence(paramChar).getValue();
      int i = str.length();
      if ((paramInt1 < i) || (paramInt1 >= paramInt2))
        break label489;
      paramInt1 -= i;
      str.getChars(0, i, paramArrayOfChar, paramInt1);
    }
    while (true)
    {
      return paramInt1;
      str = this._currentEscape.getValue();
      this._currentEscape = null;
      break;
      label489: this._writer.write(str);
    }
  }

  private final void _prependOrWriteCharacterEscape(char paramChar, int paramInt)
    throws IOException, JsonGenerationException
  {
    if (paramInt >= 0)
    {
      if (this._outputTail >= 2)
      {
        int i7 = -2 + this._outputTail;
        this._outputHead = i7;
        char[] arrayOfChar4 = this._outputBuffer;
        int i8 = i7 + 1;
        arrayOfChar4[i7] = '\\';
        this._outputBuffer[i8] = (char)paramInt;
        return;
      }
      char[] arrayOfChar3 = this._entityBuffer;
      if (arrayOfChar3 == null)
        arrayOfChar3 = _allocateEntityBuffer();
      this._outputHead = this._outputTail;
      arrayOfChar3[1] = (char)paramInt;
      this._writer.write(arrayOfChar3, 0, 2);
      return;
    }
    if (paramInt != -2)
    {
      if (this._outputTail >= 6)
      {
        char[] arrayOfChar2 = this._outputBuffer;
        int n = -6 + this._outputTail;
        this._outputHead = n;
        arrayOfChar2[n] = '\\';
        int i1 = n + 1;
        arrayOfChar2[i1] = 'u';
        int i3;
        if (paramChar > 'ÿ')
        {
          int i5 = 0xFF & paramChar >> '\b';
          int i6 = i1 + 1;
          arrayOfChar2[i6] = HEX_CHARS[(i5 >> 4)];
          i3 = i6 + 1;
          arrayOfChar2[i3] = HEX_CHARS[(i5 & 0xF)];
          paramChar = (char)(paramChar & 0xFF);
        }
        while (true)
        {
          int i4 = i3 + 1;
          arrayOfChar2[i4] = HEX_CHARS[(paramChar >> '\004')];
          arrayOfChar2[(i4 + 1)] = HEX_CHARS[(paramChar & 0xF)];
          return;
          int i2 = i1 + 1;
          arrayOfChar2[i2] = '0';
          i3 = i2 + 1;
          arrayOfChar2[i3] = '0';
        }
      }
      char[] arrayOfChar1 = this._entityBuffer;
      if (arrayOfChar1 == null)
        arrayOfChar1 = _allocateEntityBuffer();
      this._outputHead = this._outputTail;
      if (paramChar > 'ÿ')
      {
        int k = 0xFF & paramChar >> '\b';
        int m = paramChar & 0xFF;
        arrayOfChar1[10] = HEX_CHARS[(k >> 4)];
        arrayOfChar1[11] = HEX_CHARS[(k & 0xF)];
        arrayOfChar1[12] = HEX_CHARS[(m >> 4)];
        arrayOfChar1[13] = HEX_CHARS[(m & 0xF)];
        this._writer.write(arrayOfChar1, 8, 6);
        return;
      }
      arrayOfChar1[6] = HEX_CHARS[(paramChar >> '\004')];
      arrayOfChar1[7] = HEX_CHARS[(paramChar & 0xF)];
      this._writer.write(arrayOfChar1, 2, 6);
      return;
    }
    String str;
    if (this._currentEscape == null)
      str = this._characterEscapes.getEscapeSequence(paramChar).getValue();
    while (true)
    {
      int i = str.length();
      if (this._outputTail < i)
        break;
      int j = this._outputTail - i;
      this._outputHead = j;
      str.getChars(0, i, this._outputBuffer, j);
      return;
      str = this._currentEscape.getValue();
      this._currentEscape = null;
    }
    this._outputHead = this._outputTail;
    this._writer.write(str);
  }

  private void _writeLongString(String paramString)
    throws IOException, JsonGenerationException
  {
    _flushBuffer();
    int i = paramString.length();
    int j = 0;
    int k = this._outputEnd;
    int m;
    if (j + k > i)
    {
      m = i - j;
      label30: paramString.getChars(j, j + m, this._outputBuffer, 0);
      if (this._characterEscapes == null)
        break label75;
      _writeSegmentCustom(m);
    }
    while (true)
    {
      j += m;
      if (j < i)
        break;
      return;
      m = k;
      break label30;
      label75: if (this._maximumNonEscapedChar != 0)
      {
        _writeSegmentASCII(m, this._maximumNonEscapedChar);
        continue;
      }
      _writeSegment(m);
    }
  }

  private final void _writeNull()
    throws IOException
  {
    if (4 + this._outputTail >= this._outputEnd)
      _flushBuffer();
    int i = this._outputTail;
    char[] arrayOfChar = this._outputBuffer;
    arrayOfChar[i] = 'n';
    int j = i + 1;
    arrayOfChar[j] = 'u';
    int k = j + 1;
    arrayOfChar[k] = 'l';
    int m = k + 1;
    arrayOfChar[m] = 'l';
    this._outputTail = (m + 1);
  }

  private final void _writeQuotedInt(int paramInt)
    throws IOException
  {
    if (13 + this._outputTail >= this._outputEnd)
      _flushBuffer();
    char[] arrayOfChar1 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfChar1[i] = '"';
    this._outputTail = NumberOutput.outputInt(paramInt, this._outputBuffer, this._outputTail);
    char[] arrayOfChar2 = this._outputBuffer;
    int j = this._outputTail;
    this._outputTail = (j + 1);
    arrayOfChar2[j] = '"';
  }

  private final void _writeQuotedLong(long paramLong)
    throws IOException
  {
    if (23 + this._outputTail >= this._outputEnd)
      _flushBuffer();
    char[] arrayOfChar1 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfChar1[i] = '"';
    this._outputTail = NumberOutput.outputLong(paramLong, this._outputBuffer, this._outputTail);
    char[] arrayOfChar2 = this._outputBuffer;
    int j = this._outputTail;
    this._outputTail = (j + 1);
    arrayOfChar2[j] = '"';
  }

  private final void _writeQuotedRaw(Object paramObject)
    throws IOException
  {
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    char[] arrayOfChar1 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfChar1[i] = '"';
    writeRaw(paramObject.toString());
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    char[] arrayOfChar2 = this._outputBuffer;
    int j = this._outputTail;
    this._outputTail = (j + 1);
    arrayOfChar2[j] = '"';
  }

  private final void _writeSegment(int paramInt)
    throws IOException, JsonGenerationException
  {
    int[] arrayOfInt = this._outputEscapes;
    int i = arrayOfInt.length;
    int j = 0;
    int m;
    for (int k = 0; ; k = _prependOrWriteCharacterEscape(this._outputBuffer, j, paramInt, m, arrayOfInt[m]))
    {
      if (j < paramInt)
      {
        m = this._outputBuffer[j];
        if ((m >= i) || (arrayOfInt[m] == 0))
          break label76;
      }
      while (true)
      {
        int n = j - k;
        if (n <= 0)
          break label88;
        this._writer.write(this._outputBuffer, k, n);
        if (j < paramInt)
          break label88;
        return;
        label76: j++;
        if (j < paramInt)
          break;
      }
      label88: j++;
    }
  }

  private final void _writeSegmentASCII(int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    int[] arrayOfInt = this._outputEscapes;
    int i = Math.min(arrayOfInt.length, 1 + this._maximumNonEscapedChar);
    int j = 0;
    int k = 0;
    int n;
    for (int m = 0; ; m = _prependOrWriteCharacterEscape(this._outputBuffer, j, paramInt1, n, k))
    {
      if (j < paramInt1)
      {
        n = this._outputBuffer[j];
        if (n >= i)
          break label94;
        k = arrayOfInt[n];
        if (k == 0)
          break label106;
      }
      while (true)
      {
        int i1 = j - m;
        if (i1 <= 0)
          break label118;
        this._writer.write(this._outputBuffer, m, i1);
        if (j < paramInt1)
          break label118;
        return;
        label94: if (n > paramInt2)
        {
          k = -1;
          continue;
        }
        label106: j++;
        if (j < paramInt1)
          break;
      }
      label118: j++;
    }
  }

  private final void _writeSegmentCustom(int paramInt)
    throws IOException, JsonGenerationException
  {
    int[] arrayOfInt = this._outputEscapes;
    int i;
    int j;
    CharacterEscapes localCharacterEscapes;
    int k;
    if (this._maximumNonEscapedChar < 1)
    {
      i = 65535;
      j = Math.min(arrayOfInt.length, 1 + this._maximumNonEscapedChar);
      localCharacterEscapes = this._characterEscapes;
      k = 0;
    }
    label47: int n;
    for (int m = 0; ; m = _prependOrWriteCharacterEscape(this._outputBuffer, k, paramInt, n, arrayOfInt[n]))
    {
      if (k < paramInt)
      {
        n = this._outputBuffer[k];
        if (n >= j)
          break label112;
        if (arrayOfInt[n] == 0)
          break label144;
      }
      while (true)
      {
        int i1 = k - m;
        if (i1 <= 0)
          break label156;
        this._writer.write(this._outputBuffer, m, i1);
        if (k < paramInt)
          break label156;
        return;
        i = this._maximumNonEscapedChar;
        break;
        label112: if (n > i)
          continue;
        SerializableString localSerializableString = localCharacterEscapes.getEscapeSequence(n);
        this._currentEscape = localSerializableString;
        if (localSerializableString != null)
          continue;
        label144: k++;
        if (k < paramInt)
          break label47;
      }
      label156: k++;
    }
  }

  private void _writeString(String paramString)
    throws IOException, JsonGenerationException
  {
    int i = paramString.length();
    if (i > this._outputEnd)
    {
      _writeLongString(paramString);
      return;
    }
    if (i + this._outputTail > this._outputEnd)
      _flushBuffer();
    paramString.getChars(0, i, this._outputBuffer, this._outputTail);
    if (this._characterEscapes != null)
    {
      _writeStringCustom(i);
      return;
    }
    if (this._maximumNonEscapedChar != 0)
    {
      _writeStringASCII(i, this._maximumNonEscapedChar);
      return;
    }
    _writeString2(i);
  }

  private final void _writeString(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    if (this._characterEscapes != null)
      _writeStringCustom(paramArrayOfChar, paramInt1, paramInt2);
    while (true)
    {
      return;
      if (this._maximumNonEscapedChar != 0)
      {
        _writeStringASCII(paramArrayOfChar, paramInt1, paramInt2, this._maximumNonEscapedChar);
        return;
      }
      int i = paramInt2 + paramInt1;
      int[] arrayOfInt = this._outputEscapes;
      int j = arrayOfInt.length;
      while (paramInt1 < i)
      {
        int k = paramInt1;
        int m = paramArrayOfChar[paramInt1];
        int n;
        label82: int i1;
        if ((m < j) && (arrayOfInt[m] != 0))
        {
          n = paramInt1;
          i1 = n - k;
          if (i1 >= 32)
            break label172;
          if (i1 + this._outputTail > this._outputEnd)
            _flushBuffer();
          if (i1 > 0)
          {
            System.arraycopy(paramArrayOfChar, k, this._outputBuffer, this._outputTail, i1);
            this._outputTail = (i1 + this._outputTail);
          }
        }
        while (true)
        {
          if (n < i)
            break label191;
          return;
          paramInt1++;
          if (paramInt1 < i)
            break;
          n = paramInt1;
          break label82;
          label172: _flushBuffer();
          this._writer.write(paramArrayOfChar, k, i1);
        }
        label191: paramInt1 = n + 1;
        char c = paramArrayOfChar[n];
        _appendCharacterEscape(c, arrayOfInt[c]);
      }
    }
  }

  private void _writeString2(int paramInt)
    throws IOException, JsonGenerationException
  {
    int i = paramInt + this._outputTail;
    int[] arrayOfInt = this._outputEscapes;
    int j = arrayOfInt.length;
    if (this._outputTail < i)
    {
      int m;
      do
      {
        int k = this._outputBuffer[this._outputTail];
        if ((k < j) && (arrayOfInt[k] != 0))
        {
          int n = this._outputTail - this._outputHead;
          if (n > 0)
            this._writer.write(this._outputBuffer, this._outputHead, n);
          char[] arrayOfChar = this._outputBuffer;
          int i1 = this._outputTail;
          this._outputTail = (i1 + 1);
          char c = arrayOfChar[i1];
          _prependOrWriteCharacterEscape(c, arrayOfInt[c]);
          break;
        }
        m = 1 + this._outputTail;
        this._outputTail = m;
      }
      while (m < i);
    }
  }

  private void _writeStringASCII(int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    int i = paramInt1 + this._outputTail;
    int[] arrayOfInt = this._outputEscapes;
    int j = Math.min(arrayOfInt.length, 1 + this._maximumNonEscapedChar);
    if (this._outputTail < i)
    {
      label131: int m;
      do
      {
        int k = this._outputBuffer[this._outputTail];
        int n;
        if (k < j)
        {
          n = arrayOfInt[k];
          if (n == 0);
        }
        else
        {
          while (true)
          {
            int i1 = this._outputTail - this._outputHead;
            if (i1 > 0)
              this._writer.write(this._outputBuffer, this._outputHead, i1);
            this._outputTail = (1 + this._outputTail);
            _prependOrWriteCharacterEscape(k, n);
            break;
            if (k <= paramInt2)
              break label131;
            n = -1;
          }
        }
        m = 1 + this._outputTail;
        this._outputTail = m;
      }
      while (m < i);
    }
  }

  private final void _writeStringASCII(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3)
    throws IOException, JsonGenerationException
  {
    int i = paramInt2 + paramInt1;
    int[] arrayOfInt = this._outputEscapes;
    int j = Math.min(arrayOfInt.length, paramInt3 + 1);
    int k = 0;
    while (true)
    {
      int m;
      int n;
      label59: int i1;
      if (paramInt1 < i)
      {
        m = paramInt1;
        n = paramArrayOfChar[paramInt1];
        if (n >= j)
          break label129;
        k = arrayOfInt[n];
        if (k == 0)
          break label142;
        i1 = paramInt1 - m;
        if (i1 >= 32)
          break label154;
        if (i1 + this._outputTail > this._outputEnd)
          _flushBuffer();
        if (i1 > 0)
        {
          System.arraycopy(paramArrayOfChar, m, this._outputBuffer, this._outputTail, i1);
          this._outputTail = (i1 + this._outputTail);
        }
      }
      while (true)
      {
        if (paramInt1 < i)
          break label173;
        return;
        label129: if (n > paramInt3)
        {
          k = -1;
          break label59;
        }
        label142: paramInt1++;
        if (paramInt1 < i)
          break;
        break label59;
        label154: _flushBuffer();
        this._writer.write(paramArrayOfChar, m, i1);
      }
      label173: paramInt1++;
      _appendCharacterEscape(n, k);
    }
  }

  private void _writeStringCustom(int paramInt)
    throws IOException, JsonGenerationException
  {
    int i = paramInt + this._outputTail;
    int[] arrayOfInt = this._outputEscapes;
    int j;
    int k;
    CharacterEscapes localCharacterEscapes;
    if (this._maximumNonEscapedChar < 1)
    {
      j = 65535;
      k = Math.min(arrayOfInt.length, j + 1);
      localCharacterEscapes = this._characterEscapes;
      label41: if (this._outputTail >= i);
    }
    else
    {
      label181: int n;
      do
      {
        int m = this._outputBuffer[this._outputTail];
        int i1;
        if (m < k)
        {
          i1 = arrayOfInt[m];
          if (i1 == 0);
        }
        else
        {
          while (true)
          {
            int i2 = this._outputTail - this._outputHead;
            if (i2 > 0)
              this._writer.write(this._outputBuffer, this._outputHead, i2);
            this._outputTail = (1 + this._outputTail);
            _prependOrWriteCharacterEscape(m, i1);
            break label41;
            j = this._maximumNonEscapedChar;
            break;
            if (m > j)
            {
              i1 = -1;
              continue;
            }
            SerializableString localSerializableString = localCharacterEscapes.getEscapeSequence(m);
            this._currentEscape = localSerializableString;
            if (localSerializableString == null)
              break label181;
            i1 = -2;
          }
        }
        n = 1 + this._outputTail;
        this._outputTail = n;
      }
      while (n < i);
    }
  }

  private final void _writeStringCustom(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    int i = paramInt2 + paramInt1;
    int[] arrayOfInt = this._outputEscapes;
    int j;
    int k;
    CharacterEscapes localCharacterEscapes;
    int m;
    if (this._maximumNonEscapedChar < 1)
    {
      j = 65535;
      k = Math.min(arrayOfInt.length, j + 1);
      localCharacterEscapes = this._characterEscapes;
      m = 0;
    }
    while (true)
    {
      int n;
      label53: int i1;
      label77: int i2;
      if (paramInt1 < i)
      {
        n = paramInt1;
        i1 = paramArrayOfChar[paramInt1];
        if (i1 >= k)
          break label156;
        m = arrayOfInt[i1];
        if (m == 0)
          break label196;
        i2 = paramInt1 - n;
        if (i2 >= 32)
          break label208;
        if (i2 + this._outputTail > this._outputEnd)
          _flushBuffer();
        if (i2 > 0)
        {
          System.arraycopy(paramArrayOfChar, n, this._outputBuffer, this._outputTail, i2);
          this._outputTail = (i2 + this._outputTail);
        }
      }
      while (true)
      {
        if (paramInt1 < i)
          break label227;
        return;
        j = this._maximumNonEscapedChar;
        break;
        label156: if (i1 > j)
        {
          m = -1;
          break label77;
        }
        SerializableString localSerializableString = localCharacterEscapes.getEscapeSequence(i1);
        this._currentEscape = localSerializableString;
        if (localSerializableString != null)
        {
          m = -2;
          break label77;
        }
        label196: paramInt1++;
        if (paramInt1 < i)
          break label53;
        break label77;
        label208: _flushBuffer();
        this._writer.write(paramArrayOfChar, n, i2);
      }
      label227: paramInt1++;
      _appendCharacterEscape(i1, m);
    }
  }

  private void writeRawLong(String paramString)
    throws IOException, JsonGenerationException
  {
    int i = this._outputEnd - this._outputTail;
    paramString.getChars(0, i, this._outputBuffer, this._outputTail);
    this._outputTail = (i + this._outputTail);
    _flushBuffer();
    int j = i;
    int k = paramString.length() - i;
    while (k > this._outputEnd)
    {
      int m = this._outputEnd;
      paramString.getChars(j, j + m, this._outputBuffer, 0);
      this._outputHead = 0;
      this._outputTail = m;
      _flushBuffer();
      j += m;
      k -= m;
    }
    paramString.getChars(j, j + k, this._outputBuffer, 0);
    this._outputHead = 0;
    this._outputTail = k;
  }

  protected final void _flushBuffer()
    throws IOException
  {
    int i = this._outputTail - this._outputHead;
    if (i > 0)
    {
      int j = this._outputHead;
      this._outputHead = 0;
      this._outputTail = 0;
      this._writer.write(this._outputBuffer, j, i);
    }
  }

  protected void _releaseBuffers()
  {
    char[] arrayOfChar = this._outputBuffer;
    if (arrayOfChar != null)
    {
      this._outputBuffer = null;
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
      char[] arrayOfChar1 = this._outputBuffer;
      int i8 = this._outputTail;
      this._outputTail = (i8 + 1);
      arrayOfChar1[i8] = '\\';
      char[] arrayOfChar2 = this._outputBuffer;
      int i9 = this._outputTail;
      this._outputTail = (i9 + 1);
      arrayOfChar2[i9] = 'n';
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

  protected void _writeFieldName(String paramString, boolean paramBoolean)
    throws IOException, JsonGenerationException
  {
    if (this._cfgPrettyPrinter != null)
    {
      _writePPFieldName(paramString, paramBoolean);
      return;
    }
    if (1 + this._outputTail >= this._outputEnd)
      _flushBuffer();
    if (paramBoolean)
    {
      char[] arrayOfChar3 = this._outputBuffer;
      int k = this._outputTail;
      this._outputTail = (k + 1);
      arrayOfChar3[k] = ',';
    }
    if (!isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES))
    {
      _writeString(paramString);
      return;
    }
    char[] arrayOfChar1 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfChar1[i] = '"';
    _writeString(paramString);
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    char[] arrayOfChar2 = this._outputBuffer;
    int j = this._outputTail;
    this._outputTail = (j + 1);
    arrayOfChar2[j] = '"';
  }

  public void _writeFieldName(SerializableString paramSerializableString, boolean paramBoolean)
    throws IOException, JsonGenerationException
  {
    if (this._cfgPrettyPrinter != null)
    {
      _writePPFieldName(paramSerializableString, paramBoolean);
      return;
    }
    if (1 + this._outputTail >= this._outputEnd)
      _flushBuffer();
    if (paramBoolean)
    {
      char[] arrayOfChar5 = this._outputBuffer;
      int n = this._outputTail;
      this._outputTail = (n + 1);
      arrayOfChar5[n] = ',';
    }
    char[] arrayOfChar1 = paramSerializableString.asQuotedChars();
    if (!isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES))
    {
      writeRaw(arrayOfChar1, 0, arrayOfChar1.length);
      return;
    }
    char[] arrayOfChar2 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfChar2[i] = '"';
    int j = arrayOfChar1.length;
    if (1 + (j + this._outputTail) >= this._outputEnd)
    {
      writeRaw(arrayOfChar1, 0, j);
      if (this._outputTail >= this._outputEnd)
        _flushBuffer();
      char[] arrayOfChar4 = this._outputBuffer;
      int m = this._outputTail;
      this._outputTail = (m + 1);
      arrayOfChar4[m] = '"';
      return;
    }
    System.arraycopy(arrayOfChar1, 0, this._outputBuffer, this._outputTail, j);
    this._outputTail = (j + this._outputTail);
    char[] arrayOfChar3 = this._outputBuffer;
    int k = this._outputTail;
    this._outputTail = (k + 1);
    arrayOfChar3[k] = '"';
  }

  protected final void _writePPFieldName(String paramString, boolean paramBoolean)
    throws IOException, JsonGenerationException
  {
    if (paramBoolean)
      this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
    while (isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES))
    {
      if (this._outputTail >= this._outputEnd)
        _flushBuffer();
      char[] arrayOfChar1 = this._outputBuffer;
      int i = this._outputTail;
      this._outputTail = (i + 1);
      arrayOfChar1[i] = '"';
      _writeString(paramString);
      if (this._outputTail >= this._outputEnd)
        _flushBuffer();
      char[] arrayOfChar2 = this._outputBuffer;
      int j = this._outputTail;
      this._outputTail = (j + 1);
      arrayOfChar2[j] = '"';
      return;
      this._cfgPrettyPrinter.beforeObjectEntries(this);
    }
    _writeString(paramString);
  }

  protected final void _writePPFieldName(SerializableString paramSerializableString, boolean paramBoolean)
    throws IOException, JsonGenerationException
  {
    if (paramBoolean)
      this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
    char[] arrayOfChar1;
    while (true)
    {
      arrayOfChar1 = paramSerializableString.asQuotedChars();
      if (!isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES))
        break;
      if (this._outputTail >= this._outputEnd)
        _flushBuffer();
      char[] arrayOfChar2 = this._outputBuffer;
      int i = this._outputTail;
      this._outputTail = (i + 1);
      arrayOfChar2[i] = '"';
      writeRaw(arrayOfChar1, 0, arrayOfChar1.length);
      if (this._outputTail >= this._outputEnd)
        _flushBuffer();
      char[] arrayOfChar3 = this._outputBuffer;
      int j = this._outputTail;
      this._outputTail = (j + 1);
      arrayOfChar3[j] = '"';
      return;
      this._cfgPrettyPrinter.beforeObjectEntries(this);
    }
    writeRaw(arrayOfChar1, 0, arrayOfChar1.length);
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
      this._writer.close();
    while (true)
    {
      _releaseBuffers();
      return;
      this._writer.flush();
    }
  }

  public final void flush()
    throws IOException
  {
    _flushBuffer();
    if ((this._writer != null) && (isEnabled(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM)))
      this._writer.flush();
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
    return this._writer;
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
    char[] arrayOfChar1 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfChar1[i] = '"';
    _writeBinary(paramBase64Variant, paramArrayOfByte, paramInt1, paramInt1 + paramInt2);
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    char[] arrayOfChar2 = this._outputBuffer;
    int j = this._outputTail;
    this._outputTail = (j + 1);
    arrayOfChar2[j] = '"';
  }

  public void writeBoolean(boolean paramBoolean)
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("write boolean value");
    if (5 + this._outputTail >= this._outputEnd)
      _flushBuffer();
    int i = this._outputTail;
    char[] arrayOfChar = this._outputBuffer;
    int n;
    if (paramBoolean)
    {
      arrayOfChar[i] = 't';
      int i1 = i + 1;
      arrayOfChar[i1] = 'r';
      int i2 = i1 + 1;
      arrayOfChar[i2] = 'u';
      n = i2 + 1;
      arrayOfChar[n] = 'e';
    }
    while (true)
    {
      this._outputTail = (n + 1);
      return;
      arrayOfChar[i] = 'f';
      int j = i + 1;
      arrayOfChar[j] = 'a';
      int k = j + 1;
      arrayOfChar[k] = 'l';
      int m = k + 1;
      arrayOfChar[m] = 's';
      n = m + 1;
      arrayOfChar[n] = 'e';
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
      char[] arrayOfChar = this._outputBuffer;
      int i = this._outputTail;
      this._outputTail = (i + 1);
      arrayOfChar[i] = ']';
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
    char[] arrayOfChar = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfChar[i] = '}';
  }

  public final void writeFieldName(String paramString)
    throws IOException, JsonGenerationException
  {
    int i = 1;
    int j = this._writeContext.writeFieldName(paramString);
    if (j == 4)
      _reportError("Can not write a field name, expecting a value");
    if (j == i);
    while (true)
    {
      _writeFieldName(paramString, i);
      return;
      i = 0;
    }
  }

  public final void writeFieldName(SerializableString paramSerializableString)
    throws IOException, JsonGenerationException
  {
    int i = 1;
    int j = this._writeContext.writeFieldName(paramSerializableString.getValue());
    if (j == 4)
      _reportError("Can not write a field name, expecting a value");
    if (j == i);
    while (true)
    {
      _writeFieldName(paramSerializableString, i);
      return;
      i = 0;
    }
  }

  public final void writeFieldName(SerializedString paramSerializedString)
    throws IOException, JsonGenerationException
  {
    int i = 1;
    int j = this._writeContext.writeFieldName(paramSerializedString.getValue());
    if (j == 4)
      _reportError("Can not write a field name, expecting a value");
    if (j == i);
    while (true)
    {
      _writeFieldName(paramSerializedString, i);
      return;
      i = 0;
    }
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
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    char[] arrayOfChar = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfChar[i] = paramChar;
  }

  public void writeRaw(String paramString)
    throws IOException, JsonGenerationException
  {
    int i = paramString.length();
    int j = this._outputEnd - this._outputTail;
    if (j == 0)
    {
      _flushBuffer();
      j = this._outputEnd - this._outputTail;
    }
    if (j >= i)
    {
      paramString.getChars(0, i, this._outputBuffer, this._outputTail);
      this._outputTail = (i + this._outputTail);
      return;
    }
    writeRawLong(paramString);
  }

  public void writeRaw(String paramString, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    int i = this._outputEnd - this._outputTail;
    if (i < paramInt2)
    {
      _flushBuffer();
      i = this._outputEnd - this._outputTail;
    }
    if (i >= paramInt2)
    {
      paramString.getChars(paramInt1, paramInt1 + paramInt2, this._outputBuffer, this._outputTail);
      this._outputTail = (paramInt2 + this._outputTail);
      return;
    }
    writeRawLong(paramString.substring(paramInt1, paramInt1 + paramInt2));
  }

  public void writeRaw(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    if (paramInt2 < 32)
    {
      if (paramInt2 > this._outputEnd - this._outputTail)
        _flushBuffer();
      System.arraycopy(paramArrayOfChar, paramInt1, this._outputBuffer, this._outputTail, paramInt2);
      this._outputTail = (paramInt2 + this._outputTail);
      return;
    }
    _flushBuffer();
    this._writer.write(paramArrayOfChar, paramInt1, paramInt2);
  }

  public void writeRawUTF8String(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    _reportUnsupportedOperation();
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
    char[] arrayOfChar = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfChar[i] = '[';
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
    char[] arrayOfChar = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfChar[i] = '{';
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
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    char[] arrayOfChar1 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfChar1[i] = '"';
    _writeString(paramString);
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    char[] arrayOfChar2 = this._outputBuffer;
    int j = this._outputTail;
    this._outputTail = (j + 1);
    arrayOfChar2[j] = '"';
  }

  public final void writeString(SerializableString paramSerializableString)
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("write text value");
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    char[] arrayOfChar1 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfChar1[i] = '"';
    char[] arrayOfChar2 = paramSerializableString.asQuotedChars();
    int j = arrayOfChar2.length;
    if (j < 32)
    {
      if (j > this._outputEnd - this._outputTail)
        _flushBuffer();
      System.arraycopy(arrayOfChar2, 0, this._outputBuffer, this._outputTail, j);
      this._outputTail = (j + this._outputTail);
    }
    while (true)
    {
      if (this._outputTail >= this._outputEnd)
        _flushBuffer();
      char[] arrayOfChar3 = this._outputBuffer;
      int k = this._outputTail;
      this._outputTail = (k + 1);
      arrayOfChar3[k] = '"';
      return;
      _flushBuffer();
      this._writer.write(arrayOfChar2, 0, j);
    }
  }

  public void writeString(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("write text value");
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    char[] arrayOfChar1 = this._outputBuffer;
    int i = this._outputTail;
    this._outputTail = (i + 1);
    arrayOfChar1[i] = '"';
    _writeString(paramArrayOfChar, paramInt1, paramInt2);
    if (this._outputTail >= this._outputEnd)
      _flushBuffer();
    char[] arrayOfChar2 = this._outputBuffer;
    int j = this._outputTail;
    this._outputTail = (j + 1);
    arrayOfChar2[j] = '"';
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
    _reportUnsupportedOperation();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.WriterBasedGenerator
 * JD-Core Version:    0.6.0
 */