package org.codehaus.jackson.util;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;

public final class ByteArrayBuilder extends OutputStream
{
  static final int DEFAULT_BLOCK_ARRAY_SIZE = 40;
  private static final int INITIAL_BLOCK_SIZE = 500;
  private static final int MAX_BLOCK_SIZE = 262144;
  private static final byte[] NO_BYTES = new byte[0];
  private final BufferRecycler _bufferRecycler;
  private byte[] _currBlock;
  private int _currBlockPtr;
  private final LinkedList<byte[]> _pastBlocks = new LinkedList();
  private int _pastLen;

  public ByteArrayBuilder()
  {
    this(null);
  }

  public ByteArrayBuilder(int paramInt)
  {
    this(null, paramInt);
  }

  public ByteArrayBuilder(BufferRecycler paramBufferRecycler)
  {
    this(paramBufferRecycler, 500);
  }

  public ByteArrayBuilder(BufferRecycler paramBufferRecycler, int paramInt)
  {
    this._bufferRecycler = paramBufferRecycler;
    if (paramBufferRecycler == null)
    {
      this._currBlock = new byte[paramInt];
      return;
    }
    this._currBlock = paramBufferRecycler.allocByteBuffer(BufferRecycler.ByteBufferType.WRITE_CONCAT_BUFFER);
  }

  private void _allocMore()
  {
    this._pastLen += this._currBlock.length;
    int i = Math.max(this._pastLen >> 1, 1000);
    if (i > 262144)
      i = 262144;
    this._pastBlocks.add(this._currBlock);
    this._currBlock = new byte[i];
    this._currBlockPtr = 0;
  }

  public void append(int paramInt)
  {
    if (this._currBlockPtr >= this._currBlock.length)
      _allocMore();
    byte[] arrayOfByte = this._currBlock;
    int i = this._currBlockPtr;
    this._currBlockPtr = (i + 1);
    arrayOfByte[i] = (byte)paramInt;
  }

  public void appendThreeBytes(int paramInt)
  {
    if (2 + this._currBlockPtr < this._currBlock.length)
    {
      byte[] arrayOfByte1 = this._currBlock;
      int i = this._currBlockPtr;
      this._currBlockPtr = (i + 1);
      arrayOfByte1[i] = (byte)(paramInt >> 16);
      byte[] arrayOfByte2 = this._currBlock;
      int j = this._currBlockPtr;
      this._currBlockPtr = (j + 1);
      arrayOfByte2[j] = (byte)(paramInt >> 8);
      byte[] arrayOfByte3 = this._currBlock;
      int k = this._currBlockPtr;
      this._currBlockPtr = (k + 1);
      arrayOfByte3[k] = (byte)paramInt;
      return;
    }
    append(paramInt >> 16);
    append(paramInt >> 8);
    append(paramInt);
  }

  public void appendTwoBytes(int paramInt)
  {
    if (1 + this._currBlockPtr < this._currBlock.length)
    {
      byte[] arrayOfByte1 = this._currBlock;
      int i = this._currBlockPtr;
      this._currBlockPtr = (i + 1);
      arrayOfByte1[i] = (byte)(paramInt >> 8);
      byte[] arrayOfByte2 = this._currBlock;
      int j = this._currBlockPtr;
      this._currBlockPtr = (j + 1);
      arrayOfByte2[j] = (byte)paramInt;
      return;
    }
    append(paramInt >> 8);
    append(paramInt);
  }

  public void close()
  {
  }

  public byte[] completeAndCoalesce(int paramInt)
  {
    this._currBlockPtr = paramInt;
    return toByteArray();
  }

  public byte[] finishCurrentSegment()
  {
    _allocMore();
    return this._currBlock;
  }

  public void flush()
  {
  }

  public byte[] getCurrentSegment()
  {
    return this._currBlock;
  }

  public int getCurrentSegmentLength()
  {
    return this._currBlockPtr;
  }

  public void release()
  {
    reset();
    if ((this._bufferRecycler != null) && (this._currBlock != null))
    {
      this._bufferRecycler.releaseByteBuffer(BufferRecycler.ByteBufferType.WRITE_CONCAT_BUFFER, this._currBlock);
      this._currBlock = null;
    }
  }

  public void reset()
  {
    this._pastLen = 0;
    this._currBlockPtr = 0;
    if (!this._pastBlocks.isEmpty())
      this._pastBlocks.clear();
  }

  public byte[] resetAndGetFirstSegment()
  {
    reset();
    return this._currBlock;
  }

  public void setCurrentSegmentLength(int paramInt)
  {
    this._currBlockPtr = paramInt;
  }

  public byte[] toByteArray()
  {
    int i = this._pastLen + this._currBlockPtr;
    byte[] arrayOfByte1;
    if (i == 0)
      arrayOfByte1 = NO_BYTES;
    do
    {
      return arrayOfByte1;
      arrayOfByte1 = new byte[i];
      int j = 0;
      Iterator localIterator = this._pastBlocks.iterator();
      while (localIterator.hasNext())
      {
        byte[] arrayOfByte2 = (byte[])localIterator.next();
        int m = arrayOfByte2.length;
        System.arraycopy(arrayOfByte2, 0, arrayOfByte1, j, m);
        j += m;
      }
      System.arraycopy(this._currBlock, 0, arrayOfByte1, j, this._currBlockPtr);
      int k = j + this._currBlockPtr;
      if (k == i)
        continue;
      throw new RuntimeException("Internal error: total len assumed to be " + i + ", copied " + k + " bytes");
    }
    while (this._pastBlocks.isEmpty());
    reset();
    return arrayOfByte1;
  }

  public void write(int paramInt)
  {
    append(paramInt);
  }

  public void write(byte[] paramArrayOfByte)
  {
    write(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    while (true)
    {
      int i = Math.min(this._currBlock.length - this._currBlockPtr, paramInt2);
      if (i > 0)
      {
        System.arraycopy(paramArrayOfByte, paramInt1, this._currBlock, this._currBlockPtr, i);
        paramInt1 += i;
        this._currBlockPtr = (i + this._currBlockPtr);
        paramInt2 -= i;
      }
      if (paramInt2 <= 0)
        return;
      _allocMore();
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.util.ByteArrayBuilder
 * JD-Core Version:    0.6.0
 */