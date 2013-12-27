package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

@GwtCompatible(emulated=true, serializable=true)
final class ImmutableAsList<E> extends RegularImmutableList<E>
{
  private final transient ImmutableCollection<E> collection;

  ImmutableAsList(Object[] paramArrayOfObject, ImmutableCollection<E> paramImmutableCollection)
  {
    super(paramArrayOfObject, 0, paramArrayOfObject.length);
    this.collection = paramImmutableCollection;
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws InvalidObjectException
  {
    throw new InvalidObjectException("Use SerializedForm");
  }

  public boolean contains(Object paramObject)
  {
    return this.collection.contains(paramObject);
  }

  Object writeReplace()
  {
    return new SerializedForm(this.collection);
  }

  static class SerializedForm
    implements Serializable
  {
    private static final long serialVersionUID;
    final ImmutableCollection<?> collection;

    SerializedForm(ImmutableCollection<?> paramImmutableCollection)
    {
      this.collection = paramImmutableCollection;
    }

    Object readResolve()
    {
      return this.collection.asList();
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableAsList
 * JD-Core Version:    0.6.0
 */