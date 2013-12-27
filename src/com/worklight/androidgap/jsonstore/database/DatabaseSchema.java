package com.worklight.androidgap.jsonstore.database;

import com.worklight.androidgap.jsonstore.util.jackson.JacksonSerializedJSONArray;
import com.worklight.androidgap.jsonstore.util.jackson.JacksonSerializedJSONObject;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseSchema
{
  private static final String[] specialChars = { "@", "$", "^", "&", "|", ">", "<", "?", "-" };
  private TreeMap<String, DatabaseSchemaType> internalNodes;
  private String name;
  private TreeMap<String, DatabaseSchemaType> nodes;
  private TreeMap<String, DatabaseSchemaType> safeNodes;

  public DatabaseSchema(String paramString)
  {
    this.name = paramString;
    this.nodes = new TreeMap();
    this.internalNodes = new TreeMap();
    this.safeNodes = new TreeMap();
    try
    {
      this.internalNodes.put("_deleted", DatabaseSchemaType.BOOLEAN);
      this.internalNodes.put("_dirty", DatabaseSchemaType.NUMBER);
      this.internalNodes.put("_id", DatabaseSchemaType.INTEGER);
      this.internalNodes.put("json", DatabaseSchemaType.STRING);
      this.internalNodes.put("_operation", DatabaseSchemaType.STRING);
      return;
    }
    catch (Throwable localThrowable)
    {
    }
  }

  public DatabaseSchema(String paramString, JSONObject paramJSONObject)
    throws Throwable
  {
    this(paramString);
    addNodesFromSchema(paramJSONObject);
  }

  private void addNode(String paramString, DatabaseSchemaType paramDatabaseSchemaType)
    throws Throwable
  {
    if (paramString == null)
      throw new Throwable("invalid node name (null) specified");
    String str1 = paramString.trim();
    if ((str1.equals("")) || (str1.indexOf("..") != -1) || (str1.startsWith(".")) || (str1.endsWith(".")))
      throw new Throwable("invalid node name (\"" + str1 + "\") " + "specified");
    String str2 = str1.toLowerCase();
    if ((this.nodes.containsKey(str2)) || (this.internalNodes.containsKey(str2)))
      throw new Throwable("node with name \"" + str1 + "\" " + "already exists in schema");
    this.nodes.put(str2, paramDatabaseSchemaType);
    this.safeNodes.put(getDatabaseSafeNodeName(str2), paramDatabaseSchemaType);
  }

  private void addNodesFromSchema(JSONObject paramJSONObject)
    throws Throwable
  {
    JSONArray localJSONArray = paramJSONObject.names();
    if (localJSONArray != null)
    {
      int i = localJSONArray.length();
      for (int j = 0; j < i; j++)
      {
        String str1 = localJSONArray.getString(j);
        String str2 = paramJSONObject.getString(str1);
        DatabaseSchemaType localDatabaseSchemaType = DatabaseSchemaType.fromString(str2);
        if (localDatabaseSchemaType == null)
          throw new Throwable("invalid type \"" + str2 + "\" for schema node \"" + str1 + "\"");
        addNode(str1, localDatabaseSchemaType);
      }
    }
  }

  private String encodeJSONArrayAsString(JSONArray paramJSONArray, String paramString)
    throws JSONException
  {
    int i = paramJSONArray.length();
    StringBuilder localStringBuilder = new StringBuilder();
    for (int j = 0; j < i; j++)
    {
      Object localObject = paramJSONArray.get(j);
      if ((localObject instanceof JSONObject))
        localObject = locateChildInObject((JSONObject)localObject, paramString);
      if (localObject == null)
        continue;
      localStringBuilder.append(localObject.toString());
      if (j >= i - 1)
        continue;
      localStringBuilder.append("-@-");
    }
    return localStringBuilder.toString();
  }

  public static String getDatabaseSafeNodeName(String paramString)
  {
    if (paramString == null)
      return null;
    String[] arrayOfString = specialChars;
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++)
      paramString = paramString.replace(arrayOfString[j], "");
    return paramString.replace('.', '_');
  }

  private Object getValueFromObjectCaseInsensitive(JSONObject paramJSONObject, String paramString)
  {
    JSONArray localJSONArray = paramJSONObject.names();
    if (localJSONArray == null);
    while (true)
    {
      return null;
      int i = localJSONArray.length();
      for (int j = 0; j < i; j++)
      {
        String str = localJSONArray.optString(j);
        if ((str != null) && (str.toLowerCase().equals(paramString)))
          return paramJSONObject.opt(str);
      }
    }
  }

  private Object locateChildInObject(JSONObject paramJSONObject, String paramString)
  {
    int i = paramString.indexOf('.');
    if (i == -1)
    {
      Object localObject2 = paramJSONObject.opt(paramString);
      if (localObject2 == null)
        localObject2 = getValueFromObjectCaseInsensitive(paramJSONObject, paramString);
      do
        return localObject2;
      while (!(localObject2 instanceof JSONArray));
      try
      {
        String str2 = encodeJSONArrayAsString((JSONArray)localObject2, paramString);
        return str2;
      }
      catch (JSONException localJSONException2)
      {
        return null;
      }
    }
    try
    {
      Object localObject1 = paramJSONObject.get(paramString.substring(0, i));
      if ((localObject1 instanceof JSONObject))
        return locateChildInObject((JSONObject)localObject1, paramString.substring(i + 1));
      if ((localObject1 instanceof JSONArray))
      {
        String str1 = encodeJSONArrayAsString((JSONArray)localObject1, paramString.substring(i + 1));
        return str1;
      }
      return null;
    }
    catch (JSONException localJSONException1)
    {
    }
    return null;
  }

  private void mergeIntoObject(JSONObject paramJSONObject, String paramString, Object paramObject)
    throws Throwable
  {
    int i = paramString.indexOf('.');
    if (i == -1)
    {
      mergeValues(paramJSONObject, paramString, paramObject);
      return;
    }
    String str = paramString.substring(0, i);
    Object localObject = paramJSONObject.optJSONObject(str);
    if (localObject == null)
    {
      localObject = new JacksonSerializedJSONObject();
      paramJSONObject.put(str, localObject);
    }
    mergeIntoObject((JSONObject)localObject, paramString.substring(i + 1), paramObject);
  }

  private void mergeValues(JSONObject paramJSONObject, String paramString, Object paramObject)
    throws Throwable
  {
    Object localObject = paramJSONObject.opt(paramString);
    if (localObject == null)
    {
      paramJSONObject.put(paramString, paramObject);
      return;
    }
    if ((localObject instanceof JSONArray))
    {
      JSONArray localJSONArray = (JSONArray)localObject;
      int i = localJSONArray.length();
      for (int j = 0; ; j++)
      {
        if (j >= i)
          break label69;
        if (localJSONArray.opt(j) == paramObject)
          break;
      }
      label69: localJSONArray.put(paramObject);
      return;
    }
    JacksonSerializedJSONArray localJacksonSerializedJSONArray = new JacksonSerializedJSONArray();
    localJacksonSerializedJSONArray.put(localObject);
    localJacksonSerializedJSONArray.put(paramObject);
    paramJSONObject.put(paramString, localJacksonSerializedJSONArray);
  }

  private JSONObject normalizeObject(JSONObject paramJSONObject)
    throws Throwable
  {
    Iterator localIterator = paramJSONObject.keys();
    JacksonSerializedJSONObject localJacksonSerializedJSONObject = new JacksonSerializedJSONObject();
    if (localIterator == null);
    while (true)
    {
      return localJacksonSerializedJSONObject;
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        mergeIntoObject(localJacksonSerializedJSONObject, str, normalizeOrCopyObject(paramJSONObject.get(str)));
      }
    }
  }

  private Object normalizeOrCopyObject(Object paramObject)
    throws Throwable
  {
    Object localObject;
    if ((paramObject instanceof JSONObject))
      localObject = normalizeObject((JSONObject)paramObject);
    while (true)
    {
      return localObject;
      if (!(paramObject instanceof JSONArray))
        break;
      localObject = new JacksonSerializedJSONArray();
      JSONArray localJSONArray = (JSONArray)paramObject;
      int i = localJSONArray.length();
      for (int j = 0; j < i; j++)
        ((JSONArray)localObject).put(normalizeOrCopyObject(localJSONArray.get(j)));
    }
    return paramObject;
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof DatabaseSchema))
      return false;
    return ((DatabaseSchema)paramObject).nodes.equals(this.nodes);
  }

  public boolean equals(TreeMap<String, String> paramTreeMap)
  {
    if (paramTreeMap.size() != this.nodes.size() + this.internalNodes.size());
    label200: 
    while (true)
    {
      return false;
      Set localSet = this.nodes.keySet();
      TreeSet localTreeSet = new TreeSet();
      Iterator localIterator1 = localSet.iterator();
      while (localIterator1.hasNext())
        localTreeSet.add(getDatabaseSafeNodeName((String)localIterator1.next()));
      Iterator localIterator2 = paramTreeMap.keySet().iterator();
      if (!localIterator2.hasNext())
        break;
      String str1 = (String)localIterator2.next();
      String str2 = getDatabaseSafeNodeName(str1);
      if (localTreeSet.contains(str2))
      {
        localDatabaseSchemaType = (DatabaseSchemaType)this.nodes.get(str2);
        if (localDatabaseSchemaType != null);
      }
      for (DatabaseSchemaType localDatabaseSchemaType = (DatabaseSchemaType)this.safeNodes.get(str2); ; localDatabaseSchemaType = (DatabaseSchemaType)this.internalNodes.get(str2))
      {
        if (localDatabaseSchemaType == null)
          break label200;
        if (localDatabaseSchemaType.getMappedType().equals(paramTreeMap.get(str1)))
          break;
        return false;
      }
    }
    return true;
  }

  public String getName()
  {
    return this.name;
  }

  public Iterator<String> getNodeNames()
  {
    return this.nodes.keySet().iterator();
  }

  public DatabaseSchemaType getNodeType(String paramString)
  {
    return (DatabaseSchemaType)this.nodes.get(paramString);
  }

  public Map<String, Object> mapObject(JSONObject paramJSONObject1, JSONObject paramJSONObject2)
    throws Throwable
  {
    Set localSet = this.nodes.keySet();
    TreeMap localTreeMap = new TreeMap();
    JSONObject localJSONObject = normalizeObject(paramJSONObject1);
    Iterator localIterator = localSet.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      Object localObject1 = locateChildInObject(localJSONObject, str);
      if (localObject1 != null)
        localTreeMap.put(str, localObject1);
      if (paramJSONObject2 == null)
        continue;
      Object localObject2 = locateChildInObject(paramJSONObject2, str);
      if (localObject2 == null)
        continue;
      localTreeMap.put(str, localObject2);
    }
    return localTreeMap;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.jsonstore.database.DatabaseSchema
 * JD-Core Version:    0.6.0
 */