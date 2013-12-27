package org.codehaus.jackson.util;

public class BufferRecycler
{
  public static final int DEFAULT_WRITE_CONCAT_BUFFER_LEN = 2000;
  protected final byte[][] _byteBuffers = new byte[ByteBufferType.values().length][];
  protected final char[][] _charBuffers = new char[CharBufferType.values().length][];

  private final byte[] balloc(int paramInt)
  {
    return new byte[paramInt];
  }

  private final char[] calloc(int paramInt)
  {
    return new char[paramInt];
  }

  public final byte[] allocByteBuffer(ByteBufferType paramByteBufferType)
  {
    int i = paramByteBufferType.ordinal();
    byte[] arrayOfByte = this._byteBuffers[i];
    if (arrayOfByte == null)
      return balloc(paramByteBufferType.size);
    this._byteBuffers[i] = null;
    return arrayOfByte;
  }

  public final char[] allocCharBuffer(CharBufferType paramCharBufferType)
  {
    return allocCharBuffer(paramCharBufferType, 0);
  }

  public final char[] allocCharBuffer(CharBufferType paramCharBufferType, int paramInt)
  {
    if (paramCharBufferType.size > paramInt)
      paramInt = paramCharBufferType.size;
    int i = paramCharBufferType.ordinal();
    char[] arrayOfChar = this._charBuffers[i];
    if ((arrayOfChar == null) || (arrayOfChar.length < paramInt))
      return calloc(paramInt);
    this._charBuffers[i] = null;
    return arrayOfChar;
  }

  public final void releaseByteBuffer(ByteBufferType paramByteBufferType, byte[] paramArrayOfByte)
  {
    this._byteBuffers[paramByteBufferType.ordinal()] = paramArrayOfByte;
  }

  public final void releaseCharBuffer(CharBufferType paramCharBufferType, char[] paramArrayOfChar)
  {
    this._charBuffers[paramCharBufferType.ordinal()] = paramArrayOfChar;
  }

  public static enum ByteBufferType
  {
    private final int size;

    static
    {
      WRITE_CONCAT_BUFFER = new ByteBufferType("WRITE_CONCAT_BUFFER", 2, 2000);
      ByteBufferType[] arrayOfByteBufferType = new ByteBufferType[3];
      arrayOfByteBufferType[0] = READ_IO_BUFFER;
      arrayOfByteBufferType[1] = WRITE_ENCODING_BUFFER;
      arrayOfByteBufferType[2] = WRITE_CONCAT_BUFFER;
      $VALUES = arrayOfByteBufferType;
    }

    private ByteBufferType(int paramInt)
    {
      this.size = paramInt;
    }
  }

  public static enum CharBufferType
  {
    private final int size;

    static
    {
      CONCAT_BUFFER = new CharBufferType("CONCAT_BUFFER", 1, 2000);
      TEXT_BUFFER = new CharBufferType("TEXT_BUFFER", 2, 200);
      NAME_COPY_BUFFER = new CharBufferType("NAME_COPY_BUFFER", 3, 200);
      CharBufferType[] arrayOfCharBufferType = new CharBufferType[4];
      arrayOfCharBufferType[0] = TOKEN_BUFFER;
      arrayOfCharBufferType[1] = CONCAT_BUFFER;
      arrayOfCharBufferType[2] = TEXT_BUFFER;
      arrayOfCharBufferType[3] = NAME_COPY_BUFFER;
      $VALUES = arrayOfCharBufferType;
    }

    private CharBufferType(int paramInt)
    {
      this.size = paramInt;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.util.BufferRecycler
 * JD-Core Version:    0.6.0
 */