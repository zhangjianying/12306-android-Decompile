package org.codehaus.jackson.io;

import java.io.IOException;
import java.io.InputStream;

public final class MergedStream extends InputStream
{
  byte[] _buffer;
  protected final IOContext _context;
  final int _end;
  final InputStream _in;
  int _ptr;

  public MergedStream(IOContext paramIOContext, InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this._context = paramIOContext;
    this._in = paramInputStream;
    this._buffer = paramArrayOfByte;
    this._ptr = paramInt1;
    this._end = paramInt2;
  }

  private void freeMergedBuffer()
  {
    byte[] arrayOfByte = this._buffer;
    if (arrayOfByte != null)
    {
      this._buffer = null;
      if (this._context != null)
        this._context.releaseReadIOBuffer(arrayOfByte);
    }
  }

  public int available()
    throws IOException
  {
    if (this._buffer != null)
      return this._end - this._ptr;
    return this._in.available();
  }

  public void close()
    throws IOException
  {
    freeMergedBuffer();
    this._in.close();
  }

  public void mark(int paramInt)
  {
    if (this._buffer == null)
      this._in.mark(paramInt);
  }

  public boolean markSupported()
  {
    return (this._buffer == null) && (this._in.markSupported());
  }

  public int read()
    throws IOException
  {
    if (this._buffer != null)
    {
      byte[] arrayOfByte = this._buffer;
      int i = this._ptr;
      this._ptr = (i + 1);
      int j = 0xFF & arrayOfByte[i];
      if (this._ptr >= this._end)
        freeMergedBuffer();
      return j;
    }
    return this._in.read();
  }

  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    return read(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (this._buffer != null)
    {
      int i = this._end - this._ptr;
      if (paramInt2 > i)
        paramInt2 = i;
      System.arraycopy(this._buffer, this._ptr, paramArrayOfByte, paramInt1, paramInt2);
      this._ptr = (paramInt2 + this._ptr);
      if (this._ptr >= this._end)
        freeMergedBuffer();
      return paramInt2;
    }
    return this._in.read(paramArrayOfByte, paramInt1, paramInt2);
  }

  public void reset()
    throws IOException
  {
    if (this._buffer == null)
      this._in.reset();
  }

  public long skip(long paramLong)
    throws IOException
  {
    long l = 0L;
    if (this._buffer != null)
    {
      int i = this._end - this._ptr;
      if (i > paramLong)
      {
        this._ptr += (int)paramLong;
        return paramLong;
      }
      freeMergedBuffer();
      l += i;
      paramLong -= i;
    }
    if (paramLong > 0L)
      l += this._in.skip(paramLong);
    return l;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.io.MergedStream
 * JD-Core Version:    0.6.0
 */