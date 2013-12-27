package org.codehaus.jackson.impl;

import java.io.ByteArrayInputStream;
import java.io.CharConversionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.format.InputAccessor;
import org.codehaus.jackson.format.MatchStrength;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.io.MergedStream;
import org.codehaus.jackson.io.UTF32Reader;
import org.codehaus.jackson.sym.BytesToNameCanonicalizer;
import org.codehaus.jackson.sym.CharsToNameCanonicalizer;

public final class ByteSourceBootstrapper
{
  static final byte UTF8_BOM_1 = -17;
  static final byte UTF8_BOM_2 = -69;
  static final byte UTF8_BOM_3 = -65;
  protected boolean _bigEndian = true;
  private final boolean _bufferRecyclable;
  protected int _bytesPerChar = 0;
  final IOContext _context;
  final InputStream _in;
  final byte[] _inputBuffer;
  private int _inputEnd;
  protected int _inputProcessed;
  private int _inputPtr;

  public ByteSourceBootstrapper(IOContext paramIOContext, InputStream paramInputStream)
  {
    this._context = paramIOContext;
    this._in = paramInputStream;
    this._inputBuffer = paramIOContext.allocReadIOBuffer();
    this._inputPtr = 0;
    this._inputEnd = 0;
    this._inputProcessed = 0;
    this._bufferRecyclable = true;
  }

  public ByteSourceBootstrapper(IOContext paramIOContext, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this._context = paramIOContext;
    this._in = null;
    this._inputBuffer = paramArrayOfByte;
    this._inputPtr = paramInt1;
    this._inputEnd = (paramInt1 + paramInt2);
    this._inputProcessed = (-paramInt1);
    this._bufferRecyclable = false;
  }

  private boolean checkUTF16(int paramInt)
  {
    if ((0xFF00 & paramInt) == 0);
    for (this._bigEndian = true; ; this._bigEndian = false)
    {
      this._bytesPerChar = 2;
      int j = 1;
      int i;
      do
      {
        return j;
        i = paramInt & 0xFF;
        j = 0;
      }
      while (i != 0);
    }
  }

  private boolean checkUTF32(int paramInt)
    throws IOException
  {
    if (paramInt >> 8 == 0)
      this._bigEndian = true;
    while (true)
    {
      this._bytesPerChar = 4;
      int j = 1;
      int i;
      do
      {
        return j;
        if ((0xFFFFFF & paramInt) == 0)
        {
          this._bigEndian = false;
          break;
        }
        if ((0xFF00FFFF & paramInt) == 0)
        {
          reportWeirdUCS4("3412");
          break;
        }
        i = 0xFFFF00FF & paramInt;
        j = 0;
      }
      while (i != 0);
      reportWeirdUCS4("2143");
    }
  }

  private boolean handleBOM(int paramInt)
    throws IOException
  {
    switch (paramInt)
    {
    default:
    case 65279:
    case -131072:
    case 65534:
    case -16842752:
    }
    int i;
    while (true)
    {
      i = paramInt >>> 16;
      if (i != 65279)
        break;
      this._inputPtr = (2 + this._inputPtr);
      this._bytesPerChar = 2;
      this._bigEndian = true;
      return true;
      this._bigEndian = true;
      this._inputPtr = (4 + this._inputPtr);
      this._bytesPerChar = 4;
      return true;
      this._inputPtr = (4 + this._inputPtr);
      this._bytesPerChar = 4;
      this._bigEndian = false;
      return true;
      reportWeirdUCS4("2143");
      reportWeirdUCS4("3412");
    }
    if (i == 65534)
    {
      this._inputPtr = (2 + this._inputPtr);
      this._bytesPerChar = 2;
      this._bigEndian = false;
      return true;
    }
    if (paramInt >>> 8 == 15711167)
    {
      this._inputPtr = (3 + this._inputPtr);
      this._bytesPerChar = 1;
      this._bigEndian = true;
      return true;
    }
    return false;
  }

  public static MatchStrength hasJSONFormat(InputAccessor paramInputAccessor)
    throws IOException
  {
    MatchStrength localMatchStrength;
    if (!paramInputAccessor.hasMoreBytes())
      localMatchStrength = MatchStrength.INCONCLUSIVE;
    int i;
    while (true)
    {
      return localMatchStrength;
      byte b = paramInputAccessor.nextByte();
      if (b == -17)
      {
        if (!paramInputAccessor.hasMoreBytes())
          return MatchStrength.INCONCLUSIVE;
        if (paramInputAccessor.nextByte() != -69)
          return MatchStrength.NO_MATCH;
        if (!paramInputAccessor.hasMoreBytes())
          return MatchStrength.INCONCLUSIVE;
        if (paramInputAccessor.nextByte() != -65)
          return MatchStrength.NO_MATCH;
        if (!paramInputAccessor.hasMoreBytes())
          return MatchStrength.INCONCLUSIVE;
        b = paramInputAccessor.nextByte();
      }
      i = skipSpace(paramInputAccessor, b);
      if (i < 0)
        return MatchStrength.INCONCLUSIVE;
      if (i == 123)
      {
        int m = skipSpace(paramInputAccessor);
        if (m < 0)
          return MatchStrength.INCONCLUSIVE;
        if ((m == 34) || (m == 125))
          return MatchStrength.SOLID_MATCH;
        return MatchStrength.NO_MATCH;
      }
      if (i == 91)
      {
        int k = skipSpace(paramInputAccessor);
        if (k < 0)
          return MatchStrength.INCONCLUSIVE;
        if ((k == 93) || (k == 91))
          return MatchStrength.SOLID_MATCH;
        return MatchStrength.SOLID_MATCH;
      }
      localMatchStrength = MatchStrength.WEAK_MATCH;
      if ((i == 34) || ((i <= 57) && (i >= 48)))
        continue;
      if (i != 45)
        break;
      int j = skipSpace(paramInputAccessor);
      if (j < 0)
        return MatchStrength.INCONCLUSIVE;
      if ((j > 57) || (j < 48))
        return MatchStrength.NO_MATCH;
    }
    if (i == 110)
      return tryMatch(paramInputAccessor, "ull", localMatchStrength);
    if (i == 116)
      return tryMatch(paramInputAccessor, "rue", localMatchStrength);
    if (i == 102)
      return tryMatch(paramInputAccessor, "alse", localMatchStrength);
    return MatchStrength.NO_MATCH;
  }

  private void reportWeirdUCS4(String paramString)
    throws IOException
  {
    throw new CharConversionException("Unsupported UCS-4 endianness (" + paramString + ") detected");
  }

  private static final int skipSpace(InputAccessor paramInputAccessor)
    throws IOException
  {
    if (!paramInputAccessor.hasMoreBytes())
      return -1;
    return skipSpace(paramInputAccessor, paramInputAccessor.nextByte());
  }

  private static final int skipSpace(InputAccessor paramInputAccessor, byte paramByte)
    throws IOException
  {
    while (true)
    {
      int i = paramByte & 0xFF;
      if ((i != 32) && (i != 13) && (i != 10) && (i != 9))
        return i;
      if (!paramInputAccessor.hasMoreBytes())
        return -1;
      paramByte = paramInputAccessor.nextByte();
      (paramByte & 0xFF);
    }
  }

  private static final MatchStrength tryMatch(InputAccessor paramInputAccessor, String paramString, MatchStrength paramMatchStrength)
    throws IOException
  {
    int i = 0;
    int j = paramString.length();
    while (true)
    {
      if (i < j)
      {
        if (!paramInputAccessor.hasMoreBytes())
          paramMatchStrength = MatchStrength.INCONCLUSIVE;
      }
      else
        return paramMatchStrength;
      if (paramInputAccessor.nextByte() != paramString.charAt(i))
        return MatchStrength.NO_MATCH;
      i++;
    }
  }

  public JsonParser constructParser(int paramInt, ObjectCodec paramObjectCodec, BytesToNameCanonicalizer paramBytesToNameCanonicalizer, CharsToNameCanonicalizer paramCharsToNameCanonicalizer)
    throws IOException, JsonParseException
  {
    JsonEncoding localJsonEncoding = detectEncoding();
    boolean bool1 = JsonParser.Feature.CANONICALIZE_FIELD_NAMES.enabledIn(paramInt);
    boolean bool2 = JsonParser.Feature.INTERN_FIELD_NAMES.enabledIn(paramInt);
    if ((localJsonEncoding == JsonEncoding.UTF8) && (bool1))
    {
      BytesToNameCanonicalizer localBytesToNameCanonicalizer = paramBytesToNameCanonicalizer.makeChild(bool1, bool2);
      return new Utf8StreamParser(this._context, paramInt, this._in, paramObjectCodec, localBytesToNameCanonicalizer, this._inputBuffer, this._inputPtr, this._inputEnd, this._bufferRecyclable);
    }
    return new ReaderBasedParser(this._context, paramInt, constructReader(), paramObjectCodec, paramCharsToNameCanonicalizer.makeChild(bool1, bool2));
  }

  public Reader constructReader()
    throws IOException
  {
    JsonEncoding localJsonEncoding = this._context.getEncoding();
    switch (1.$SwitchMap$org$codehaus$jackson$JsonEncoding[localJsonEncoding.ordinal()])
    {
    default:
      throw new RuntimeException("Internal error");
    case 1:
    case 2:
      return new UTF32Reader(this._context, this._in, this._inputBuffer, this._inputPtr, this._inputEnd, this._context.getEncoding().isBigEndian());
    case 3:
    case 4:
    case 5:
    }
    InputStream localInputStream = this._in;
    Object localObject;
    if (localInputStream == null)
      localObject = new ByteArrayInputStream(this._inputBuffer, this._inputPtr, this._inputEnd);
    while (true)
    {
      return new InputStreamReader((InputStream)localObject, localJsonEncoding.getJavaName());
      if (this._inputPtr < this._inputEnd)
      {
        localObject = new MergedStream(this._context, localInputStream, this._inputBuffer, this._inputPtr, this._inputEnd);
        continue;
      }
      localObject = localInputStream;
    }
  }

  public JsonEncoding detectEncoding()
    throws IOException, JsonParseException
  {
    int j;
    int i;
    if (ensureLoaded(4))
    {
      j = this._inputBuffer[this._inputPtr] << 24 | (0xFF & this._inputBuffer[(1 + this._inputPtr)]) << 16 | (0xFF & this._inputBuffer[(2 + this._inputPtr)]) << 8 | 0xFF & this._inputBuffer[(3 + this._inputPtr)];
      if (handleBOM(j))
        i = 1;
    }
    JsonEncoding localJsonEncoding;
    while (i == 0)
    {
      localJsonEncoding = JsonEncoding.UTF8;
      this._context.setEncoding(localJsonEncoding);
      return localJsonEncoding;
      if (checkUTF32(j))
      {
        i = 1;
        continue;
      }
      boolean bool3 = checkUTF16(j >>> 16);
      i = 0;
      if (!bool3)
        continue;
      i = 1;
      continue;
      boolean bool1 = ensureLoaded(2);
      i = 0;
      if (!bool1)
        continue;
      boolean bool2 = checkUTF16((0xFF & this._inputBuffer[this._inputPtr]) << 8 | 0xFF & this._inputBuffer[(1 + this._inputPtr)]);
      i = 0;
      if (!bool2)
        continue;
      i = 1;
    }
    if (this._bytesPerChar == 2)
    {
      if (this._bigEndian);
      for (localJsonEncoding = JsonEncoding.UTF16_BE; ; localJsonEncoding = JsonEncoding.UTF16_LE)
        break;
    }
    if (this._bytesPerChar == 4)
    {
      if (this._bigEndian);
      for (localJsonEncoding = JsonEncoding.UTF32_BE; ; localJsonEncoding = JsonEncoding.UTF32_LE)
        break;
    }
    throw new RuntimeException("Internal error");
  }

  protected boolean ensureLoaded(int paramInt)
    throws IOException
  {
    int i = 1;
    int j = this._inputEnd - this._inputPtr;
    while (true)
    {
      if (j < paramInt)
        if (this._in != null)
          break label37;
      label37: for (int k = -1; k < i; k = this._in.read(this._inputBuffer, this._inputEnd, this._inputBuffer.length - this._inputEnd))
      {
        i = 0;
        return i;
      }
      this._inputEnd = (k + this._inputEnd);
      j += k;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.ByteSourceBootstrapper
 * JD-Core Version:    0.6.0
 */