package org.codehaus.jackson.io;

import java.lang.ref.SoftReference;
import org.codehaus.jackson.util.ByteArrayBuilder;
import org.codehaus.jackson.util.CharTypes;
import org.codehaus.jackson.util.TextBuffer;

public final class JsonStringEncoder
{
  private static final byte[] HEX_BYTES;
  private static final char[] HEX_CHARS = CharTypes.copyHexChars();
  private static final int INT_0 = 48;
  private static final int INT_BACKSLASH = 92;
  private static final int INT_U = 117;
  private static final int SURR1_FIRST = 55296;
  private static final int SURR1_LAST = 56319;
  private static final int SURR2_FIRST = 56320;
  private static final int SURR2_LAST = 57343;
  protected static final ThreadLocal<SoftReference<JsonStringEncoder>> _threadEncoder;
  protected ByteArrayBuilder _byteBuilder;
  protected final char[] _quoteBuffer = new char[6];
  protected TextBuffer _textBuffer;

  static
  {
    HEX_BYTES = CharTypes.copyHexBytes();
    _threadEncoder = new ThreadLocal();
  }

  public JsonStringEncoder()
  {
    this._quoteBuffer[0] = '\\';
    this._quoteBuffer[2] = '0';
    this._quoteBuffer[3] = '0';
  }

  private int _appendByteEscape(int paramInt1, int paramInt2, ByteArrayBuilder paramByteArrayBuilder, int paramInt3)
  {
    paramByteArrayBuilder.setCurrentSegmentLength(paramInt3);
    paramByteArrayBuilder.append(92);
    if (paramInt2 < 0)
    {
      paramByteArrayBuilder.append(117);
      if (paramInt1 > 255)
      {
        int i = paramInt1 >> 8;
        paramByteArrayBuilder.append(HEX_BYTES[(i >> 4)]);
        paramByteArrayBuilder.append(HEX_BYTES[(i & 0xF)]);
        paramInt1 &= 255;
        paramByteArrayBuilder.append(HEX_BYTES[(paramInt1 >> 4)]);
        paramByteArrayBuilder.append(HEX_BYTES[(paramInt1 & 0xF)]);
      }
    }
    while (true)
    {
      return paramByteArrayBuilder.getCurrentSegmentLength();
      paramByteArrayBuilder.append(48);
      paramByteArrayBuilder.append(48);
      break;
      paramByteArrayBuilder.append((byte)paramInt2);
    }
  }

  private int _appendSingleEscape(int paramInt, char[] paramArrayOfChar)
  {
    if (paramInt < 0)
    {
      int i = -(paramInt + 1);
      paramArrayOfChar[1] = 'u';
      paramArrayOfChar[4] = HEX_CHARS[(i >> 4)];
      paramArrayOfChar[5] = HEX_CHARS[(i & 0xF)];
      return 6;
    }
    paramArrayOfChar[1] = (char)paramInt;
    return 2;
  }

  private int _convertSurrogate(int paramInt1, int paramInt2)
  {
    if ((paramInt2 < 56320) || (paramInt2 > 57343))
      throw new IllegalArgumentException("Broken surrogate pair: first char 0x" + Integer.toHexString(paramInt1) + ", second 0x" + Integer.toHexString(paramInt2) + "; illegal combination");
    return 65536 + (paramInt1 - 55296 << 10) + (paramInt2 - 56320);
  }

  private void _throwIllegalSurrogate(int paramInt)
  {
    if (paramInt > 1114111)
      throw new IllegalArgumentException("Illegal character point (0x" + Integer.toHexString(paramInt) + ") to output; max is 0x10FFFF as per RFC 4627");
    if (paramInt >= 55296)
    {
      if (paramInt <= 56319)
        throw new IllegalArgumentException("Unmatched first part of surrogate pair (0x" + Integer.toHexString(paramInt) + ")");
      throw new IllegalArgumentException("Unmatched second part of surrogate pair (0x" + Integer.toHexString(paramInt) + ")");
    }
    throw new IllegalArgumentException("Illegal character point (0x" + Integer.toHexString(paramInt) + ") to output");
  }

  public static JsonStringEncoder getInstance()
  {
    SoftReference localSoftReference = (SoftReference)_threadEncoder.get();
    if (localSoftReference == null);
    for (JsonStringEncoder localJsonStringEncoder = null; ; localJsonStringEncoder = (JsonStringEncoder)localSoftReference.get())
    {
      if (localJsonStringEncoder == null)
      {
        localJsonStringEncoder = new JsonStringEncoder();
        _threadEncoder.set(new SoftReference(localJsonStringEncoder));
      }
      return localJsonStringEncoder;
    }
  }

  public byte[] encodeAsUTF8(String paramString)
  {
    ByteArrayBuilder localByteArrayBuilder = this._byteBuilder;
    if (localByteArrayBuilder == null)
    {
      localByteArrayBuilder = new ByteArrayBuilder(null);
      this._byteBuilder = localByteArrayBuilder;
    }
    int i = paramString.length();
    int j = 0;
    byte[] arrayOfByte = localByteArrayBuilder.resetAndGetFirstSegment();
    int k = arrayOfByte.length;
    int m = 0;
    int i1;
    int i2;
    label69: int i13;
    if (m < i)
    {
      int n = m + 1;
      i1 = paramString.charAt(m);
      i2 = n;
      if (i1 <= 127)
      {
        if (j >= k)
        {
          arrayOfByte = localByteArrayBuilder.finishCurrentSegment();
          k = arrayOfByte.length;
          j = 0;
        }
        i13 = j + 1;
        arrayOfByte[j] = (byte)i1;
        if (i2 >= i)
          j = i13;
      }
    }
    while (true)
    {
      return this._byteBuilder.completeAndCoalesce(j);
      int i14 = i2 + 1;
      i1 = paramString.charAt(i2);
      j = i13;
      i2 = i14;
      break label69;
      if (j >= k)
      {
        arrayOfByte = localByteArrayBuilder.finishCurrentSegment();
        k = arrayOfByte.length;
      }
      for (int i3 = 0; ; i3 = j)
      {
        int i6;
        if (i1 < 2048)
        {
          i6 = i3 + 1;
          arrayOfByte[i3] = (byte)(0xC0 | i1 >> 6);
        }
        for (int i7 = i2; ; i7 = i2)
        {
          if (i6 >= k)
          {
            arrayOfByte = localByteArrayBuilder.finishCurrentSegment();
            k = arrayOfByte.length;
            i6 = 0;
          }
          int i8 = i6 + 1;
          arrayOfByte[i6] = (byte)(0x80 | i1 & 0x3F);
          j = i8;
          m = i7;
          break;
          if ((i1 >= 55296) && (i1 <= 57343))
            break label357;
          int i4 = i3 + 1;
          arrayOfByte[i3] = (byte)(0xE0 | i1 >> 12);
          if (i4 >= k)
          {
            arrayOfByte = localByteArrayBuilder.finishCurrentSegment();
            k = arrayOfByte.length;
            i4 = 0;
          }
          int i5 = i4 + 1;
          arrayOfByte[i4] = (byte)(0x80 | 0x3F & i1 >> 6);
          i6 = i5;
        }
        label357: if (i1 > 56319)
          _throwIllegalSurrogate(i1);
        if (i2 >= i)
          _throwIllegalSurrogate(i1);
        i7 = i2 + 1;
        i1 = _convertSurrogate(i1, paramString.charAt(i2));
        if (i1 > 1114111)
          _throwIllegalSurrogate(i1);
        int i9 = i3 + 1;
        arrayOfByte[i3] = (byte)(0xF0 | i1 >> 18);
        if (i9 >= k)
        {
          arrayOfByte = localByteArrayBuilder.finishCurrentSegment();
          k = arrayOfByte.length;
          i9 = 0;
        }
        int i10 = i9 + 1;
        arrayOfByte[i9] = (byte)(0x80 | 0x3F & i1 >> 12);
        if (i10 >= k)
        {
          arrayOfByte = localByteArrayBuilder.finishCurrentSegment();
          k = arrayOfByte.length;
        }
        for (int i11 = 0; ; i11 = i10)
        {
          int i12 = i11 + 1;
          arrayOfByte[i11] = (byte)(0x80 | 0x3F & i1 >> 6);
          i6 = i12;
          break;
        }
      }
    }
  }

  public char[] quoteAsString(String paramString)
  {
    TextBuffer localTextBuffer = this._textBuffer;
    if (localTextBuffer == null)
    {
      localTextBuffer = new TextBuffer(null);
      this._textBuffer = localTextBuffer;
    }
    char[] arrayOfChar = localTextBuffer.emptyAndGetCurrentSegment();
    int[] arrayOfInt = CharTypes.get7BitOutputEscapes();
    int i = arrayOfInt.length;
    int j = 0;
    int k = paramString.length();
    int m = 0;
    label57: int n;
    int i2;
    int i3;
    if (j < k)
    {
      n = paramString.charAt(j);
      if ((n < i) && (arrayOfInt[n] != 0))
      {
        i2 = j + 1;
        i3 = _appendSingleEscape(arrayOfInt[paramString.charAt(j)], this._quoteBuffer);
        if (m + i3 <= arrayOfChar.length)
          break label239;
        int i4 = arrayOfChar.length - m;
        if (i4 > 0)
          System.arraycopy(this._quoteBuffer, 0, arrayOfChar, m, i4);
        arrayOfChar = localTextBuffer.finishCurrentSegment();
        int i5 = i3 - i4;
        System.arraycopy(this._quoteBuffer, i4, arrayOfChar, m, i5);
        m += i5;
      }
    }
    while (true)
    {
      j = i2;
      break;
      if (m >= arrayOfChar.length)
      {
        arrayOfChar = localTextBuffer.finishCurrentSegment();
        m = 0;
      }
      int i1 = m + 1;
      arrayOfChar[m] = n;
      j++;
      if (j >= k)
      {
        m = i1;
        localTextBuffer.setCurrentLength(m);
        return localTextBuffer.contentsAsArray();
      }
      m = i1;
      break label57;
      label239: System.arraycopy(this._quoteBuffer, 0, arrayOfChar, m, i3);
      m += i3;
    }
  }

  public byte[] quoteAsUTF8(String paramString)
  {
    ByteArrayBuilder localByteArrayBuilder = this._byteBuilder;
    if (localByteArrayBuilder == null)
    {
      localByteArrayBuilder = new ByteArrayBuilder(null);
      this._byteBuilder = localByteArrayBuilder;
    }
    int i = 0;
    int j = paramString.length();
    int k = 0;
    byte[] arrayOfByte = localByteArrayBuilder.resetAndGetFirstSegment();
    int n;
    int i1;
    label198: int i5;
    int i6;
    while (true)
    {
      int[] arrayOfInt;
      if (i < j)
        arrayOfInt = CharTypes.get7BitOutputEscapes();
      while (true)
      {
        int m = paramString.charAt(i);
        if ((m > 127) || (arrayOfInt[m] != 0))
        {
          if (k >= arrayOfByte.length)
          {
            arrayOfByte = localByteArrayBuilder.finishCurrentSegment();
            k = 0;
          }
          n = i + 1;
          i1 = paramString.charAt(i);
          if (i1 > 127)
            break label198;
          k = _appendByteEscape(i1, arrayOfInt[i1], localByteArrayBuilder, k);
          arrayOfByte = localByteArrayBuilder.getCurrentSegment();
          i = n;
          break;
        }
        if (k >= arrayOfByte.length)
        {
          arrayOfByte = localByteArrayBuilder.finishCurrentSegment();
          k = 0;
        }
        int i15 = k + 1;
        arrayOfByte[k] = (byte)m;
        i++;
        if (i >= j)
        {
          k = i15;
          return this._byteBuilder.completeAndCoalesce(k);
        }
        k = i15;
      }
      if (i1 > 2047)
        break;
      int i14 = k + 1;
      arrayOfByte[k] = (byte)(0xC0 | i1 >> 6);
      i5 = 0x80 | i1 & 0x3F;
      i6 = i14;
      i = n;
      if (i6 >= arrayOfByte.length)
      {
        arrayOfByte = localByteArrayBuilder.finishCurrentSegment();
        i6 = 0;
      }
      int i7 = i6 + 1;
      arrayOfByte[i6] = (byte)i5;
      k = i7;
    }
    int i2;
    if ((i1 < 55296) || (i1 > 57343))
    {
      i2 = k + 1;
      arrayOfByte[k] = (byte)(0xE0 | i1 >> 12);
      if (i2 < arrayOfByte.length)
        break label573;
      arrayOfByte = localByteArrayBuilder.finishCurrentSegment();
    }
    label573: for (int i3 = 0; ; i3 = i2)
    {
      int i4 = i3 + 1;
      arrayOfByte[i3] = (byte)(0x80 | 0x3F & i1 >> 6);
      i5 = 0x80 | i1 & 0x3F;
      i6 = i4;
      i = n;
      break;
      if (i1 > 56319)
        _throwIllegalSurrogate(i1);
      if (n >= j)
        _throwIllegalSurrogate(i1);
      i = n + 1;
      int i8 = _convertSurrogate(i1, paramString.charAt(n));
      if (i8 > 1114111)
        _throwIllegalSurrogate(i8);
      int i9 = k + 1;
      arrayOfByte[k] = (byte)(0xF0 | i8 >> 18);
      if (i9 >= arrayOfByte.length)
        arrayOfByte = localByteArrayBuilder.finishCurrentSegment();
      for (int i10 = 0; ; i10 = i9)
      {
        int i11 = i10 + 1;
        arrayOfByte[i10] = (byte)(0x80 | 0x3F & i8 >> 12);
        if (i11 >= arrayOfByte.length)
          arrayOfByte = localByteArrayBuilder.finishCurrentSegment();
        for (int i12 = 0; ; i12 = i11)
        {
          int i13 = i12 + 1;
          arrayOfByte[i12] = (byte)(0x80 | 0x3F & i8 >> 6);
          i5 = 0x80 | i8 & 0x3F;
          i6 = i13;
          break;
        }
      }
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.io.JsonStringEncoder
 * JD-Core Version:    0.6.0
 */