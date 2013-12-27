package org.codehaus.jackson.map.util;

import java.lang.reflect.Array;
import java.util.List;

public final class ObjectBuffer
{
  static final int INITIAL_CHUNK_SIZE = 12;
  static final int MAX_CHUNK_SIZE = 262144;
  static final int SMALL_CHUNK_SIZE = 16384;
  private Node _bufferHead;
  private Node _bufferTail;
  private int _bufferedEntryCount;
  private Object[] _freeBuffer;

  protected final void _copyTo(Object paramObject, int paramInt1, Object[] paramArrayOfObject, int paramInt2)
  {
    int i = 0;
    for (Node localNode = this._bufferHead; localNode != null; localNode = localNode.next())
    {
      Object[] arrayOfObject = localNode.getData();
      int k = arrayOfObject.length;
      System.arraycopy(arrayOfObject, 0, paramObject, i, k);
      i += k;
    }
    System.arraycopy(paramArrayOfObject, 0, paramObject, i, paramInt2);
    int j = i + paramInt2;
    if (j != paramInt1)
      throw new IllegalStateException("Should have gotten " + paramInt1 + " entries, got " + j);
  }

  protected void _reset()
  {
    if (this._bufferTail != null)
      this._freeBuffer = this._bufferTail.getData();
    this._bufferTail = null;
    this._bufferHead = null;
    this._bufferedEntryCount = 0;
  }

  public Object[] appendCompletedChunk(Object[] paramArrayOfObject)
  {
    Node localNode = new Node(paramArrayOfObject);
    int i;
    if (this._bufferHead == null)
    {
      this._bufferTail = localNode;
      this._bufferHead = localNode;
      i = paramArrayOfObject.length;
      this._bufferedEntryCount = (i + this._bufferedEntryCount);
      if (i >= 16384)
        break label73;
    }
    label73: for (int j = i + i; ; j = i + (i >> 2))
    {
      return new Object[j];
      this._bufferTail.linkNext(localNode);
      this._bufferTail = localNode;
      break;
    }
  }

  public int bufferedSize()
  {
    return this._bufferedEntryCount;
  }

  public void completeAndClearBuffer(Object[] paramArrayOfObject, int paramInt, List<Object> paramList)
  {
    for (Node localNode = this._bufferHead; localNode != null; localNode = localNode.next())
    {
      Object[] arrayOfObject = localNode.getData();
      int j = 0;
      int k = arrayOfObject.length;
      while (j < k)
      {
        paramList.add(arrayOfObject[j]);
        j++;
      }
    }
    for (int i = 0; i < paramInt; i++)
      paramList.add(paramArrayOfObject[i]);
  }

  public Object[] completeAndClearBuffer(Object[] paramArrayOfObject, int paramInt)
  {
    int i = paramInt + this._bufferedEntryCount;
    Object[] arrayOfObject = new Object[i];
    _copyTo(arrayOfObject, i, paramArrayOfObject, paramInt);
    return arrayOfObject;
  }

  public <T> T[] completeAndClearBuffer(Object[] paramArrayOfObject, int paramInt, Class<T> paramClass)
  {
    int i = paramInt + this._bufferedEntryCount;
    Object[] arrayOfObject = (Object[])(Object[])Array.newInstance(paramClass, i);
    _copyTo(arrayOfObject, i, paramArrayOfObject, paramInt);
    _reset();
    return arrayOfObject;
  }

  public int initialCapacity()
  {
    if (this._freeBuffer == null)
      return 0;
    return this._freeBuffer.length;
  }

  public Object[] resetAndStart()
  {
    _reset();
    if (this._freeBuffer == null)
      return new Object[12];
    return this._freeBuffer;
  }

  static final class Node
  {
    final Object[] _data;
    Node _next;

    public Node(Object[] paramArrayOfObject)
    {
      this._data = paramArrayOfObject;
    }

    public Object[] getData()
    {
      return this._data;
    }

    public void linkNext(Node paramNode)
    {
      if (this._next != null)
        throw new IllegalStateException();
      this._next = paramNode;
    }

    public Node next()
    {
      return this._next;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.util.ObjectBuffer
 * JD-Core Version:    0.6.0
 */