package org.codehaus.jackson.node;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;

public final class ArrayNode extends ContainerNode
{
  protected ArrayList<JsonNode> _children;

  public ArrayNode(JsonNodeFactory paramJsonNodeFactory)
  {
    super(paramJsonNodeFactory);
  }

  private void _add(JsonNode paramJsonNode)
  {
    if (this._children == null)
      this._children = new ArrayList();
    this._children.add(paramJsonNode);
  }

  private void _insert(int paramInt, JsonNode paramJsonNode)
  {
    if (this._children == null)
    {
      this._children = new ArrayList();
      this._children.add(paramJsonNode);
      return;
    }
    if (paramInt < 0)
    {
      this._children.add(0, paramJsonNode);
      return;
    }
    if (paramInt >= this._children.size())
    {
      this._children.add(paramJsonNode);
      return;
    }
    this._children.add(paramInt, paramJsonNode);
  }

  private boolean _sameChildren(ArrayList<JsonNode> paramArrayList)
  {
    int i = paramArrayList.size();
    if (size() != i)
      return false;
    for (int j = 0; j < i; j++)
      if (!((JsonNode)this._children.get(j)).equals(paramArrayList.get(j)))
        return false;
    return true;
  }

  public JsonNode _set(int paramInt, JsonNode paramJsonNode)
  {
    if ((this._children == null) || (paramInt < 0) || (paramInt >= this._children.size()))
      throw new IndexOutOfBoundsException("Illegal index " + paramInt + ", array size " + size());
    return (JsonNode)this._children.set(paramInt, paramJsonNode);
  }

  public void add(double paramDouble)
  {
    _add(numberNode(paramDouble));
  }

  public void add(float paramFloat)
  {
    _add(numberNode(paramFloat));
  }

  public void add(int paramInt)
  {
    _add(numberNode(paramInt));
  }

  public void add(long paramLong)
  {
    _add(numberNode(paramLong));
  }

  public void add(String paramString)
  {
    if (paramString == null)
    {
      addNull();
      return;
    }
    _add(textNode(paramString));
  }

  public void add(BigDecimal paramBigDecimal)
  {
    if (paramBigDecimal == null)
    {
      addNull();
      return;
    }
    _add(numberNode(paramBigDecimal));
  }

  public void add(JsonNode paramJsonNode)
  {
    if (paramJsonNode == null)
      paramJsonNode = nullNode();
    _add(paramJsonNode);
  }

  public void add(boolean paramBoolean)
  {
    _add(booleanNode(paramBoolean));
  }

  public void add(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
    {
      addNull();
      return;
    }
    _add(binaryNode(paramArrayOfByte));
  }

  public JsonNode addAll(Collection<JsonNode> paramCollection)
  {
    if (paramCollection.size() > 0)
    {
      if (this._children == null)
        this._children = new ArrayList(paramCollection);
    }
    else
      return this;
    this._children.addAll(paramCollection);
    return this;
  }

  public JsonNode addAll(ArrayNode paramArrayNode)
  {
    int i = paramArrayNode.size();
    if (i > 0)
    {
      if (this._children == null)
        this._children = new ArrayList(i + 2);
      paramArrayNode.addContentsTo(this._children);
    }
    return this;
  }

  public ArrayNode addArray()
  {
    ArrayNode localArrayNode = arrayNode();
    _add(localArrayNode);
    return localArrayNode;
  }

  protected void addContentsTo(List<JsonNode> paramList)
  {
    if (this._children != null)
    {
      Iterator localIterator = this._children.iterator();
      while (localIterator.hasNext())
        paramList.add((JsonNode)localIterator.next());
    }
  }

  public void addNull()
  {
    _add(nullNode());
  }

  public ObjectNode addObject()
  {
    ObjectNode localObjectNode = objectNode();
    _add(localObjectNode);
    return localObjectNode;
  }

  public void addPOJO(Object paramObject)
  {
    if (paramObject == null)
    {
      addNull();
      return;
    }
    _add(POJONode(paramObject));
  }

  public JsonToken asToken()
  {
    return JsonToken.START_ARRAY;
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == this);
    ArrayNode localArrayNode;
    while (true)
    {
      return true;
      if (paramObject == null)
        return false;
      if (paramObject.getClass() != getClass())
        return false;
      localArrayNode = (ArrayNode)paramObject;
      if ((this._children != null) && (this._children.size() != 0))
        break;
      if (localArrayNode.size() != 0)
        return false;
    }
    return localArrayNode._sameChildren(this._children);
  }

  public ObjectNode findParent(String paramString)
  {
    if (this._children != null)
    {
      Iterator localIterator = this._children.iterator();
      while (localIterator.hasNext())
      {
        JsonNode localJsonNode = ((JsonNode)localIterator.next()).findParent(paramString);
        if (localJsonNode != null)
          return (ObjectNode)localJsonNode;
      }
    }
    return null;
  }

  public List<JsonNode> findParents(String paramString, List<JsonNode> paramList)
  {
    if (this._children != null)
    {
      Iterator localIterator = this._children.iterator();
      while (localIterator.hasNext())
        paramList = ((JsonNode)localIterator.next()).findParents(paramString, paramList);
    }
    return paramList;
  }

  public JsonNode findValue(String paramString)
  {
    if (this._children != null)
    {
      Iterator localIterator = this._children.iterator();
      while (localIterator.hasNext())
      {
        JsonNode localJsonNode = ((JsonNode)localIterator.next()).findValue(paramString);
        if (localJsonNode != null)
          return localJsonNode;
      }
    }
    return null;
  }

  public List<JsonNode> findValues(String paramString, List<JsonNode> paramList)
  {
    if (this._children != null)
    {
      Iterator localIterator = this._children.iterator();
      while (localIterator.hasNext())
        paramList = ((JsonNode)localIterator.next()).findValues(paramString, paramList);
    }
    return paramList;
  }

  public List<String> findValuesAsText(String paramString, List<String> paramList)
  {
    if (this._children != null)
    {
      Iterator localIterator = this._children.iterator();
      while (localIterator.hasNext())
        paramList = ((JsonNode)localIterator.next()).findValuesAsText(paramString, paramList);
    }
    return paramList;
  }

  public JsonNode get(int paramInt)
  {
    if ((paramInt >= 0) && (this._children != null) && (paramInt < this._children.size()))
      return (JsonNode)this._children.get(paramInt);
    return null;
  }

  public JsonNode get(String paramString)
  {
    return null;
  }

  public Iterator<JsonNode> getElements()
  {
    if (this._children == null)
      return ContainerNode.NoNodesIterator.instance();
    return this._children.iterator();
  }

  public int hashCode()
  {
    int i;
    if (this._children == null)
      i = 1;
    while (true)
    {
      return i;
      i = this._children.size();
      Iterator localIterator = this._children.iterator();
      while (localIterator.hasNext())
      {
        JsonNode localJsonNode = (JsonNode)localIterator.next();
        if (localJsonNode == null)
          continue;
        i ^= localJsonNode.hashCode();
      }
    }
  }

  public void insert(int paramInt, double paramDouble)
  {
    _insert(paramInt, numberNode(paramDouble));
  }

  public void insert(int paramInt, float paramFloat)
  {
    _insert(paramInt, numberNode(paramFloat));
  }

  public void insert(int paramInt1, int paramInt2)
  {
    _insert(paramInt1, numberNode(paramInt2));
  }

  public void insert(int paramInt, long paramLong)
  {
    _insert(paramInt, numberNode(paramLong));
  }

  public void insert(int paramInt, String paramString)
  {
    if (paramString == null)
    {
      insertNull(paramInt);
      return;
    }
    _insert(paramInt, textNode(paramString));
  }

  public void insert(int paramInt, BigDecimal paramBigDecimal)
  {
    if (paramBigDecimal == null)
    {
      insertNull(paramInt);
      return;
    }
    _insert(paramInt, numberNode(paramBigDecimal));
  }

  public void insert(int paramInt, JsonNode paramJsonNode)
  {
    if (paramJsonNode == null)
      paramJsonNode = nullNode();
    _insert(paramInt, paramJsonNode);
  }

  public void insert(int paramInt, boolean paramBoolean)
  {
    _insert(paramInt, booleanNode(paramBoolean));
  }

  public void insert(int paramInt, byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
    {
      insertNull(paramInt);
      return;
    }
    _insert(paramInt, binaryNode(paramArrayOfByte));
  }

  public ArrayNode insertArray(int paramInt)
  {
    ArrayNode localArrayNode = arrayNode();
    _insert(paramInt, localArrayNode);
    return localArrayNode;
  }

  public void insertNull(int paramInt)
  {
    _insert(paramInt, nullNode());
  }

  public ObjectNode insertObject(int paramInt)
  {
    ObjectNode localObjectNode = objectNode();
    _insert(paramInt, localObjectNode);
    return localObjectNode;
  }

  public void insertPOJO(int paramInt, Object paramObject)
  {
    if (paramObject == null)
    {
      insertNull(paramInt);
      return;
    }
    _insert(paramInt, POJONode(paramObject));
  }

  public boolean isArray()
  {
    return true;
  }

  public JsonNode path(int paramInt)
  {
    if ((paramInt >= 0) && (this._children != null) && (paramInt < this._children.size()))
      return (JsonNode)this._children.get(paramInt);
    return MissingNode.getInstance();
  }

  public JsonNode path(String paramString)
  {
    return MissingNode.getInstance();
  }

  public JsonNode remove(int paramInt)
  {
    if ((paramInt >= 0) && (this._children != null) && (paramInt < this._children.size()))
      return (JsonNode)this._children.remove(paramInt);
    return null;
  }

  public ArrayNode removeAll()
  {
    this._children = null;
    return this;
  }

  public final void serialize(JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonProcessingException
  {
    paramJsonGenerator.writeStartArray();
    if (this._children != null)
    {
      Iterator localIterator = this._children.iterator();
      while (localIterator.hasNext())
        ((BaseJsonNode)(JsonNode)localIterator.next()).writeTo(paramJsonGenerator);
    }
    paramJsonGenerator.writeEndArray();
  }

  public void serializeWithType(JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer)
    throws IOException, JsonProcessingException
  {
    paramTypeSerializer.writeTypePrefixForArray(this, paramJsonGenerator);
    if (this._children != null)
    {
      Iterator localIterator = this._children.iterator();
      while (localIterator.hasNext())
        ((BaseJsonNode)(JsonNode)localIterator.next()).writeTo(paramJsonGenerator);
    }
    paramTypeSerializer.writeTypeSuffixForArray(this, paramJsonGenerator);
  }

  public JsonNode set(int paramInt, JsonNode paramJsonNode)
  {
    if (paramJsonNode == null)
      paramJsonNode = nullNode();
    return _set(paramInt, paramJsonNode);
  }

  public int size()
  {
    if (this._children == null)
      return 0;
    return this._children.size();
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(16 + (size() << 4));
    localStringBuilder.append('[');
    if (this._children != null)
    {
      int i = 0;
      int j = this._children.size();
      while (i < j)
      {
        if (i > 0)
          localStringBuilder.append(',');
        localStringBuilder.append(((JsonNode)this._children.get(i)).toString());
        i++;
      }
    }
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.node.ArrayNode
 * JD-Core Version:    0.6.0
 */