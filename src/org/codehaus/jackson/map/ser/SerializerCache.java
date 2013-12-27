package org.codehaus.jackson.map.ser;

import java.util.HashMap;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.impl.ReadOnlyClassToSerializerMap;
import org.codehaus.jackson.type.JavaType;

public final class SerializerCache
{
  private ReadOnlyClassToSerializerMap _readOnlyMap = null;
  private HashMap<TypeKey, JsonSerializer<Object>> _sharedMap = new HashMap(64);

  public void addAndResolveNonTypedSerializer(Class<?> paramClass, JsonSerializer<Object> paramJsonSerializer, SerializerProvider paramSerializerProvider)
    throws JsonMappingException
  {
    monitorenter;
    try
    {
      if (this._sharedMap.put(new TypeKey(paramClass, false), paramJsonSerializer) == null)
        this._readOnlyMap = null;
      if ((paramJsonSerializer instanceof ResolvableSerializer))
        ((ResolvableSerializer)paramJsonSerializer).resolve(paramSerializerProvider);
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void addAndResolveNonTypedSerializer(JavaType paramJavaType, JsonSerializer<Object> paramJsonSerializer, SerializerProvider paramSerializerProvider)
    throws JsonMappingException
  {
    monitorenter;
    try
    {
      if (this._sharedMap.put(new TypeKey(paramJavaType, false), paramJsonSerializer) == null)
        this._readOnlyMap = null;
      if ((paramJsonSerializer instanceof ResolvableSerializer))
        ((ResolvableSerializer)paramJsonSerializer).resolve(paramSerializerProvider);
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void addTypedSerializer(Class<?> paramClass, JsonSerializer<Object> paramJsonSerializer)
  {
    monitorenter;
    try
    {
      if (this._sharedMap.put(new TypeKey(paramClass, true), paramJsonSerializer) == null)
        this._readOnlyMap = null;
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void addTypedSerializer(JavaType paramJavaType, JsonSerializer<Object> paramJsonSerializer)
  {
    monitorenter;
    try
    {
      if (this._sharedMap.put(new TypeKey(paramJavaType, true), paramJsonSerializer) == null)
        this._readOnlyMap = null;
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void flush()
  {
    monitorenter;
    try
    {
      this._sharedMap.clear();
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public ReadOnlyClassToSerializerMap getReadOnlyLookupMap()
  {
    monitorenter;
    try
    {
      ReadOnlyClassToSerializerMap localReadOnlyClassToSerializerMap = this._readOnlyMap;
      if (localReadOnlyClassToSerializerMap == null)
      {
        localReadOnlyClassToSerializerMap = ReadOnlyClassToSerializerMap.from(this._sharedMap);
        this._readOnlyMap = localReadOnlyClassToSerializerMap;
      }
      return localReadOnlyClassToSerializerMap.instance();
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public int size()
  {
    monitorenter;
    try
    {
      int i = this._sharedMap.size();
      monitorexit;
      return i;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public JsonSerializer<Object> typedValueSerializer(Class<?> paramClass)
  {
    monitorenter;
    try
    {
      JsonSerializer localJsonSerializer = (JsonSerializer)this._sharedMap.get(new TypeKey(paramClass, true));
      return localJsonSerializer;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public JsonSerializer<Object> typedValueSerializer(JavaType paramJavaType)
  {
    monitorenter;
    try
    {
      JsonSerializer localJsonSerializer = (JsonSerializer)this._sharedMap.get(new TypeKey(paramJavaType, true));
      return localJsonSerializer;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public JsonSerializer<Object> untypedValueSerializer(Class<?> paramClass)
  {
    monitorenter;
    try
    {
      JsonSerializer localJsonSerializer = (JsonSerializer)this._sharedMap.get(new TypeKey(paramClass, false));
      return localJsonSerializer;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public JsonSerializer<Object> untypedValueSerializer(JavaType paramJavaType)
  {
    monitorenter;
    try
    {
      JsonSerializer localJsonSerializer = (JsonSerializer)this._sharedMap.get(new TypeKey(paramJavaType, false));
      return localJsonSerializer;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public static final class TypeKey
  {
    protected Class<?> _class;
    protected int _hashCode;
    protected boolean _isTyped;
    protected JavaType _type;

    public TypeKey(Class<?> paramClass, boolean paramBoolean)
    {
      this._class = paramClass;
      this._type = null;
      this._isTyped = paramBoolean;
      this._hashCode = hash(paramClass, paramBoolean);
    }

    public TypeKey(JavaType paramJavaType, boolean paramBoolean)
    {
      this._type = paramJavaType;
      this._class = null;
      this._isTyped = paramBoolean;
      this._hashCode = hash(paramJavaType, paramBoolean);
    }

    private static final int hash(Class<?> paramClass, boolean paramBoolean)
    {
      int i = paramClass.getName().hashCode();
      if (paramBoolean)
        i++;
      return i;
    }

    private static final int hash(JavaType paramJavaType, boolean paramBoolean)
    {
      int i = -1 + paramJavaType.hashCode();
      if (paramBoolean)
        i--;
      return i;
    }

    public final boolean equals(Object paramObject)
    {
      if (paramObject == this);
      TypeKey localTypeKey;
      while (true)
      {
        return true;
        localTypeKey = (TypeKey)paramObject;
        if (localTypeKey._isTyped != this._isTyped)
          break label55;
        if (this._class == null)
          break;
        if (localTypeKey._class != this._class)
          return false;
      }
      return this._type.equals(localTypeKey._type);
      label55: return false;
    }

    public final int hashCode()
    {
      return this._hashCode;
    }

    public void resetTyped(Class<?> paramClass)
    {
      this._type = null;
      this._class = paramClass;
      this._isTyped = true;
      this._hashCode = hash(paramClass, true);
    }

    public void resetTyped(JavaType paramJavaType)
    {
      this._type = paramJavaType;
      this._class = null;
      this._isTyped = true;
      this._hashCode = hash(paramJavaType, true);
    }

    public void resetUntyped(Class<?> paramClass)
    {
      this._type = null;
      this._class = paramClass;
      this._isTyped = false;
      this._hashCode = hash(paramClass, false);
    }

    public void resetUntyped(JavaType paramJavaType)
    {
      this._type = paramJavaType;
      this._class = null;
      this._isTyped = false;
      this._hashCode = hash(paramJavaType, false);
    }

    public final String toString()
    {
      if (this._class != null)
        return "{class: " + this._class.getName() + ", typed? " + this._isTyped + "}";
      return "{type: " + this._type + ", typed? " + this._isTyped + "}";
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.SerializerCache
 * JD-Core Version:    0.6.0
 */