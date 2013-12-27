package org.codehaus.jackson.map.util;

public abstract class PrimitiveArrayBuilder<T>
{
  static final int INITIAL_CHUNK_SIZE = 12;
  static final int MAX_CHUNK_SIZE = 262144;
  static final int SMALL_CHUNK_SIZE = 16384;
  Node<T> _bufferHead;
  Node<T> _bufferTail;
  int _bufferedEntryCount;
  T _freeBuffer;

  protected abstract T _constructArray(int paramInt);

  protected void _reset()
  {
    if (this._bufferTail != null)
      this._freeBuffer = this._bufferTail.getData();
    this._bufferTail = null;
    this._bufferHead = null;
    this._bufferedEntryCount = 0;
  }

  public final T appendCompletedChunk(T paramT, int paramInt)
  {
    Node localNode = new Node(paramT, paramInt);
    if (this._bufferHead == null)
    {
      this._bufferTail = localNode;
      this._bufferHead = localNode;
      this._bufferedEntryCount = (paramInt + this._bufferedEntryCount);
      if (paramInt >= 16384)
        break label72;
    }
    label72: for (int i = paramInt + paramInt; ; i = paramInt + (paramInt >> 2))
    {
      return _constructArray(i);
      this._bufferTail.linkNext(localNode);
      this._bufferTail = localNode;
      break;
    }
  }

  public T completeAndClearBuffer(T paramT, int paramInt)
  {
    int i = paramInt + this._bufferedEntryCount;
    Object localObject = _constructArray(i);
    int j = 0;
    for (Node localNode = this._bufferHead; localNode != null; localNode = localNode.next())
      j = localNode.copyData(localObject, j);
    System.arraycopy(paramT, 0, localObject, j, paramInt);
    int k = j + paramInt;
    if (k != i)
      throw new IllegalStateException("Should have gotten " + i + " entries, got " + k);
    return localObject;
  }

  public T resetAndStart()
  {
    _reset();
    if (this._freeBuffer == null)
      return _constructArray(12);
    return this._freeBuffer;
  }

  static final class Node<T>
  {
    final T _data;
    final int _dataLength;
    Node<T> _next;

    public Node(T paramT, int paramInt)
    {
      this._data = paramT;
      this._dataLength = paramInt;
    }

    public int copyData(T paramT, int paramInt)
    {
      System.arraycopy(this._data, 0, paramT, paramInt, this._dataLength);
      return paramInt + this._dataLength;
    }

    public T getData()
    {
      return this._data;
    }

    public void linkNext(Node<T> paramNode)
    {
      if (this._next != null)
        throw new IllegalStateException();
      this._next = paramNode;
    }

    public Node<T> next()
    {
      return this._next;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.util.PrimitiveArrayBuilder
 * JD-Core Version:    0.6.0
 */