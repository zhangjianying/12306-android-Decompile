package org.codehaus.jackson.node;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;

public class ObjectNode extends ContainerNode
{
  protected LinkedHashMap<String, JsonNode> _children = null;

  public ObjectNode(JsonNodeFactory paramJsonNodeFactory)
  {
    super(paramJsonNodeFactory);
  }

  private final JsonNode _put(String paramString, JsonNode paramJsonNode)
  {
    if (this._children == null)
      this._children = new LinkedHashMap();
    return (JsonNode)this._children.put(paramString, paramJsonNode);
  }

  public JsonToken asToken()
  {
    return JsonToken.START_OBJECT;
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == this);
    JsonNode localJsonNode1;
    JsonNode localJsonNode2;
    do
    {
      ObjectNode localObjectNode;
      Iterator localIterator;
      while (!localIterator.hasNext())
      {
        do
        {
          return true;
          if (paramObject == null)
            return false;
          if (paramObject.getClass() != getClass())
            return false;
          localObjectNode = (ObjectNode)paramObject;
          if (localObjectNode.size() != size())
            return false;
        }
        while (this._children == null);
        localIterator = this._children.entrySet().iterator();
      }
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      String str = (String)localEntry.getKey();
      localJsonNode1 = (JsonNode)localEntry.getValue();
      localJsonNode2 = localObjectNode.get(str);
    }
    while ((localJsonNode2 != null) && (localJsonNode2.equals(localJsonNode1)));
    return false;
  }

  public ObjectNode findParent(String paramString)
  {
    if (this._children != null)
    {
      Iterator localIterator = this._children.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if (paramString.equals(localEntry.getKey()))
          return this;
        JsonNode localJsonNode = ((JsonNode)localEntry.getValue()).findParent(paramString);
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
      Iterator localIterator = this._children.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if (paramString.equals(localEntry.getKey()))
        {
          if (paramList == null)
            paramList = new ArrayList();
          paramList.add(this);
          continue;
        }
        paramList = ((JsonNode)localEntry.getValue()).findParents(paramString, paramList);
      }
    }
    return paramList;
  }

  public JsonNode findValue(String paramString)
  {
    if (this._children != null)
    {
      Iterator localIterator = this._children.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if (paramString.equals(localEntry.getKey()))
          return (JsonNode)localEntry.getValue();
        JsonNode localJsonNode = ((JsonNode)localEntry.getValue()).findValue(paramString);
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
      Iterator localIterator = this._children.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if (paramString.equals(localEntry.getKey()))
        {
          if (paramList == null)
            paramList = new ArrayList();
          paramList.add(localEntry.getValue());
          continue;
        }
        paramList = ((JsonNode)localEntry.getValue()).findValues(paramString, paramList);
      }
    }
    return paramList;
  }

  public List<String> findValuesAsText(String paramString, List<String> paramList)
  {
    if (this._children != null)
    {
      Iterator localIterator = this._children.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if (paramString.equals(localEntry.getKey()))
        {
          if (paramList == null)
            paramList = new ArrayList();
          paramList.add(((JsonNode)localEntry.getValue()).getValueAsText());
          continue;
        }
        paramList = ((JsonNode)localEntry.getValue()).findValuesAsText(paramString, paramList);
      }
    }
    return paramList;
  }

  public JsonNode get(int paramInt)
  {
    return null;
  }

  public JsonNode get(String paramString)
  {
    if (this._children != null)
      return (JsonNode)this._children.get(paramString);
    return null;
  }

  public Iterator<JsonNode> getElements()
  {
    if (this._children == null)
      return ContainerNode.NoNodesIterator.instance();
    return this._children.values().iterator();
  }

  public Iterator<String> getFieldNames()
  {
    if (this._children == null)
      return ContainerNode.NoStringsIterator.instance();
    return this._children.keySet().iterator();
  }

  public Iterator<Map.Entry<String, JsonNode>> getFields()
  {
    if (this._children == null)
      return NoFieldsIterator.instance;
    return this._children.entrySet().iterator();
  }

  public int hashCode()
  {
    if (this._children == null)
      return -1;
    return this._children.hashCode();
  }

  public boolean isObject()
  {
    return true;
  }

  public JsonNode path(int paramInt)
  {
    return MissingNode.getInstance();
  }

  public JsonNode path(String paramString)
  {
    if (this._children != null)
    {
      JsonNode localJsonNode = (JsonNode)this._children.get(paramString);
      if (localJsonNode != null)
        return localJsonNode;
    }
    return MissingNode.getInstance();
  }

  public JsonNode put(String paramString, JsonNode paramJsonNode)
  {
    if (paramJsonNode == null)
      paramJsonNode = nullNode();
    return _put(paramString, paramJsonNode);
  }

  public void put(String paramString, double paramDouble)
  {
    _put(paramString, numberNode(paramDouble));
  }

  public void put(String paramString, float paramFloat)
  {
    _put(paramString, numberNode(paramFloat));
  }

  public void put(String paramString, int paramInt)
  {
    _put(paramString, numberNode(paramInt));
  }

  public void put(String paramString, long paramLong)
  {
    _put(paramString, numberNode(paramLong));
  }

  public void put(String paramString1, String paramString2)
  {
    if (paramString2 == null)
    {
      putNull(paramString1);
      return;
    }
    _put(paramString1, textNode(paramString2));
  }

  public void put(String paramString, BigDecimal paramBigDecimal)
  {
    if (paramBigDecimal == null)
    {
      putNull(paramString);
      return;
    }
    _put(paramString, numberNode(paramBigDecimal));
  }

  public void put(String paramString, boolean paramBoolean)
  {
    _put(paramString, booleanNode(paramBoolean));
  }

  public void put(String paramString, byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
    {
      putNull(paramString);
      return;
    }
    _put(paramString, binaryNode(paramArrayOfByte));
  }

  public JsonNode putAll(Map<String, JsonNode> paramMap)
  {
    if (this._children == null)
      this._children = new LinkedHashMap(paramMap);
    while (true)
    {
      return this;
      Iterator localIterator = paramMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        Object localObject = (JsonNode)localEntry.getValue();
        if (localObject == null)
          localObject = nullNode();
        this._children.put(localEntry.getKey(), localObject);
      }
    }
  }

  public JsonNode putAll(ObjectNode paramObjectNode)
  {
    int i = paramObjectNode.size();
    if (i > 0)
    {
      if (this._children == null)
        this._children = new LinkedHashMap(i);
      paramObjectNode.putContentsTo(this._children);
    }
    return this;
  }

  public ArrayNode putArray(String paramString)
  {
    ArrayNode localArrayNode = arrayNode();
    _put(paramString, localArrayNode);
    return localArrayNode;
  }

  protected void putContentsTo(Map<String, JsonNode> paramMap)
  {
    if (this._children != null)
    {
      Iterator localIterator = this._children.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        paramMap.put(localEntry.getKey(), localEntry.getValue());
      }
    }
  }

  public void putNull(String paramString)
  {
    _put(paramString, nullNode());
  }

  public ObjectNode putObject(String paramString)
  {
    ObjectNode localObjectNode = objectNode();
    _put(paramString, localObjectNode);
    return localObjectNode;
  }

  public void putPOJO(String paramString, Object paramObject)
  {
    _put(paramString, POJONode(paramObject));
  }

  public JsonNode remove(String paramString)
  {
    if (this._children != null)
      return (JsonNode)this._children.remove(paramString);
    return null;
  }

  public ObjectNode remove(Collection<String> paramCollection)
  {
    if (this._children != null)
    {
      Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        this._children.remove(str);
      }
    }
    return this;
  }

  public ObjectNode removeAll()
  {
    this._children = null;
    return this;
  }

  public ObjectNode retain(Collection<String> paramCollection)
  {
    if (this._children != null)
    {
      Iterator localIterator = this._children.entrySet().iterator();
      while (localIterator.hasNext())
      {
        if (paramCollection.contains(((Map.Entry)localIterator.next()).getKey()))
          continue;
        localIterator.remove();
      }
    }
    return this;
  }

  public ObjectNode retain(String[] paramArrayOfString)
  {
    return retain(Arrays.asList(paramArrayOfString));
  }

  public final void serialize(JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonProcessingException
  {
    paramJsonGenerator.writeStartObject();
    if (this._children != null)
    {
      Iterator localIterator = this._children.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        paramJsonGenerator.writeFieldName((String)localEntry.getKey());
        ((BaseJsonNode)localEntry.getValue()).serialize(paramJsonGenerator, paramSerializerProvider);
      }
    }
    paramJsonGenerator.writeEndObject();
  }

  public void serializeWithType(JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer)
    throws IOException, JsonProcessingException
  {
    paramTypeSerializer.writeTypePrefixForObject(this, paramJsonGenerator);
    if (this._children != null)
    {
      Iterator localIterator = this._children.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        paramJsonGenerator.writeFieldName((String)localEntry.getKey());
        ((BaseJsonNode)localEntry.getValue()).serialize(paramJsonGenerator, paramSerializerProvider);
      }
    }
    paramTypeSerializer.writeTypeSuffixForObject(this, paramJsonGenerator);
  }

  public int size()
  {
    if (this._children == null)
      return 0;
    return this._children.size();
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(32 + (size() << 4));
    localStringBuilder.append("{");
    if (this._children != null)
    {
      int i = 0;
      Iterator localIterator = this._children.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if (i > 0)
          localStringBuilder.append(",");
        i++;
        TextNode.appendQuoted(localStringBuilder, (String)localEntry.getKey());
        localStringBuilder.append(':');
        localStringBuilder.append(((JsonNode)localEntry.getValue()).toString());
      }
    }
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }

  public ObjectNode with(String paramString)
  {
    if (this._children == null)
      this._children = new LinkedHashMap();
    JsonNode localJsonNode;
    do
    {
      ObjectNode localObjectNode = objectNode();
      this._children.put(paramString, localObjectNode);
      return localObjectNode;
      localJsonNode = (JsonNode)this._children.get(paramString);
    }
    while (localJsonNode == null);
    if ((localJsonNode instanceof ObjectNode))
      return (ObjectNode)localJsonNode;
    throw new UnsupportedOperationException("Property '" + paramString + "' has value that is not of type ObjectNode (but " + localJsonNode.getClass().getName() + ")");
  }

  protected static class NoFieldsIterator
    implements Iterator<Map.Entry<String, JsonNode>>
  {
    static final NoFieldsIterator instance = new NoFieldsIterator();

    public boolean hasNext()
    {
      return false;
    }

    public Map.Entry<String, JsonNode> next()
    {
      throw new NoSuchElementException();
    }

    public void remove()
    {
      throw new IllegalStateException();
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.node.ObjectNode
 * JD-Core Version:    0.6.0
 */