package org.codehaus.jackson.node;

import java.util.Iterator;
import java.util.Map.Entry;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.JsonToken;

abstract class NodeCursor extends JsonStreamContext
{
  final NodeCursor _parent;

  public NodeCursor(int paramInt, NodeCursor paramNodeCursor)
  {
    this._type = paramInt;
    this._index = -1;
    this._parent = paramNodeCursor;
  }

  public abstract boolean currentHasChildren();

  public abstract JsonNode currentNode();

  public abstract JsonToken endToken();

  public abstract String getCurrentName();

  public final NodeCursor getParent()
  {
    return this._parent;
  }

  public final NodeCursor iterateChildren()
  {
    JsonNode localJsonNode = currentNode();
    if (localJsonNode == null)
      throw new IllegalStateException("No current node");
    if (localJsonNode.isArray())
      return new Array(localJsonNode, this);
    if (localJsonNode.isObject())
      return new Object(localJsonNode, this);
    throw new IllegalStateException("Current node of type " + localJsonNode.getClass().getName());
  }

  public abstract JsonToken nextToken();

  public abstract JsonToken nextValue();

  protected static final class Array extends NodeCursor
  {
    Iterator<JsonNode> _contents;
    JsonNode _currentNode;

    public Array(JsonNode paramJsonNode, NodeCursor paramNodeCursor)
    {
      super(paramNodeCursor);
      this._contents = paramJsonNode.getElements();
    }

    public boolean currentHasChildren()
    {
      return ((ContainerNode)currentNode()).size() > 0;
    }

    public JsonNode currentNode()
    {
      return this._currentNode;
    }

    public JsonToken endToken()
    {
      return JsonToken.END_ARRAY;
    }

    public String getCurrentName()
    {
      return null;
    }

    public JsonToken nextToken()
    {
      if (!this._contents.hasNext())
      {
        this._currentNode = null;
        return null;
      }
      this._currentNode = ((JsonNode)this._contents.next());
      return this._currentNode.asToken();
    }

    public JsonToken nextValue()
    {
      return nextToken();
    }
  }

  protected static final class Object extends NodeCursor
  {
    Iterator<Map.Entry<String, JsonNode>> _contents;
    Map.Entry<String, JsonNode> _current;
    boolean _needEntry;

    public Object(JsonNode paramJsonNode, NodeCursor paramNodeCursor)
    {
      super(paramNodeCursor);
      this._contents = ((ObjectNode)paramJsonNode).getFields();
      this._needEntry = true;
    }

    public boolean currentHasChildren()
    {
      return ((ContainerNode)currentNode()).size() > 0;
    }

    public JsonNode currentNode()
    {
      if (this._current == null)
        return null;
      return (JsonNode)this._current.getValue();
    }

    public JsonToken endToken()
    {
      return JsonToken.END_OBJECT;
    }

    public String getCurrentName()
    {
      if (this._current == null)
        return null;
      return (String)this._current.getKey();
    }

    public JsonToken nextToken()
    {
      if (this._needEntry)
      {
        if (!this._contents.hasNext())
        {
          this._current = null;
          return null;
        }
        this._needEntry = false;
        this._current = ((Map.Entry)this._contents.next());
        return JsonToken.FIELD_NAME;
      }
      this._needEntry = true;
      return ((JsonNode)this._current.getValue()).asToken();
    }

    public JsonToken nextValue()
    {
      JsonToken localJsonToken = nextToken();
      if (localJsonToken == JsonToken.FIELD_NAME)
        localJsonToken = nextToken();
      return localJsonToken;
    }
  }

  protected static final class RootValue extends NodeCursor
  {
    protected boolean _done = false;
    JsonNode _node;

    public RootValue(JsonNode paramJsonNode, NodeCursor paramNodeCursor)
    {
      super(paramNodeCursor);
      this._node = paramJsonNode;
    }

    public boolean currentHasChildren()
    {
      return false;
    }

    public JsonNode currentNode()
    {
      return this._node;
    }

    public JsonToken endToken()
    {
      return null;
    }

    public String getCurrentName()
    {
      return null;
    }

    public JsonToken nextToken()
    {
      if (!this._done)
      {
        this._done = true;
        return this._node.asToken();
      }
      this._node = null;
      return null;
    }

    public JsonToken nextValue()
    {
      return nextToken();
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.node.NodeCursor
 * JD-Core Version:    0.6.0
 */