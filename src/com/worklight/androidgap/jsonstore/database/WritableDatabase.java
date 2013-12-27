package com.worklight.androidgap.jsonstore.database;

import android.content.ContentValues;
import android.database.Cursor;
import com.worklight.androidgap.jsonstore.util.JsonstoreUtil;
import com.worklight.androidgap.jsonstore.util.Logger;
import com.worklight.androidgap.jsonstore.util.jackson.JacksonSerializedJSONObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.sqlcipher.database.SQLiteDatabase;
import org.json.JSONException;
import org.json.JSONObject;

public class WritableDatabase extends ReadableDatabase
{
  private static final String SQL_AND = " AND";
  private static final String SQL_DELETE = "DELETE FROM {0} WHERE {1};";
  private static final String SQL_INSERT = "INSERT INTO {0} ({1}) VALUES ({2});";
  private static final String SQL_UPDATE = "UPDATE {0} SET {1}{2};";
  private static final String SQL_WHERE = " WHERE";

  protected WritableDatabase(SQLiteDatabase paramSQLiteDatabase, DatabaseSchema paramDatabaseSchema)
  {
    super(paramSQLiteDatabase, paramDatabaseSchema);
  }

  public int delete(String[] paramArrayOfString, Object[] paramArrayOfObject)
  {
    String str1 = getSchema().getName();
    StringBuilder localStringBuilder = new StringBuilder();
    if ((paramArrayOfString == null) || (paramArrayOfString.length < 1))
      localStringBuilder.append("1");
    while (this.logger.isLoggable(3))
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = str1;
      arrayOfObject[1] = localStringBuilder.toString();
      String str2 = JsonstoreUtil.formatString("DELETE FROM {0} WHERE {1};", arrayOfObject);
      this.logger.logDebug("executing delete on database \"" + str1 + "\":");
      this.logger.logDebug("   " + str2);
      this.logger.logDebug("   args:");
      for (int j = 0; j < paramArrayOfObject.length; j++)
        this.logger.logDebug("      " + paramArrayOfObject[j]);
      for (int k = 0; k < paramArrayOfString.length; k++)
      {
        localStringBuilder.append(paramArrayOfString[k]);
        localStringBuilder.append(" = ?");
        if (k >= -1 + paramArrayOfString.length)
          continue;
        localStringBuilder.append(" AND");
      }
    }
    String[] arrayOfString = new String[paramArrayOfObject.length];
    for (int i = 0; i < paramArrayOfObject.length; i++)
      arrayOfString[i] = paramArrayOfObject[i].toString();
    return getDatabase().delete(str1, localStringBuilder.toString(), arrayOfString);
  }

  public int deleteIfRequired(JSONObject paramJSONObject, boolean paramBoolean1, boolean paramBoolean2)
    throws JSONException
  {
    boolean bool = paramBoolean1;
    int i = 1;
    try
    {
      String str2 = findOperationForObjectById(paramJSONObject.getInt("_id"));
      str1 = str2;
      if (i != 0)
      {
        if ((str1 != null) && (str1.equals("add")))
          bool = true;
        if (bool)
        {
          String[] arrayOfString3 = { "_id" };
          Object[] arrayOfObject3 = new Object[1];
          arrayOfObject3[0] = Integer.valueOf(paramJSONObject.getInt("_id"));
          return delete(arrayOfString3, arrayOfObject3);
        }
      }
    }
    catch (JSONException localJSONException)
    {
      while (true)
      {
        String str1 = null;
        i = 0;
      }
      String[] arrayOfString4 = { "_deleted", "_dirty", "_operation" };
      Object[] arrayOfObject4 = new Object[3];
      arrayOfObject4[0] = Integer.valueOf(1);
      arrayOfObject4[1] = Long.valueOf(new Date().getTime());
      arrayOfObject4[2] = "remove";
      return update(arrayOfString4, arrayOfObject4, paramJSONObject);
    }
    int j = 0;
    Cursor localCursor = findUsingQueryObject(paramJSONObject, new String[] { "_id", "_operation" }, new String[] { "_deleted = 0" }, null, null, Boolean.valueOf(paramBoolean2));
    int k = localCursor.getCount();
    int m = 0;
    if (m < k)
    {
      JacksonSerializedJSONObject localJacksonSerializedJSONObject = new JacksonSerializedJSONObject();
      localCursor.moveToNext();
      int n = localCursor.getInt(0);
      String[] arrayOfString1;
      Object[] arrayOfObject1;
      if (("add".compareToIgnoreCase(localCursor.getString(1)) == 0) || (paramBoolean1))
      {
        arrayOfString1 = new String[] { "_id" };
        arrayOfObject1 = new Object[1];
        arrayOfObject1[0] = Integer.valueOf(n);
      }
      String[] arrayOfString2;
      Object[] arrayOfObject2;
      for (int i1 = delete(arrayOfString1, arrayOfObject1); ; i1 = update(arrayOfString2, arrayOfObject2, localJacksonSerializedJSONObject.put("_id", n)))
      {
        j += i1;
        m++;
        break;
        arrayOfString2 = new String[] { "_deleted", "_dirty", "_operation" };
        arrayOfObject2 = new Object[3];
        arrayOfObject2[0] = Integer.valueOf(1);
        arrayOfObject2[1] = Long.valueOf(new Date().getTime());
        arrayOfObject2[2] = "remove";
      }
    }
    int i2 = j;
    localCursor.close();
    return i2;
  }

  public long insert(String paramString, String[] paramArrayOfString, Object[] paramArrayOfObject)
  {
    ContentValues localContentValues = new ContentValues();
    String str1 = getSchema().getName();
    if (this.logger.isLoggable(3))
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      StringBuilder localStringBuilder2 = new StringBuilder();
      for (int i = 0; i < paramArrayOfString.length; i++)
      {
        localStringBuilder1.append("'" + DatabaseSchema.getDatabaseSafeNodeName(paramArrayOfString[i]) + "'");
        localStringBuilder2.append(paramArrayOfObject[i].toString());
        if (i >= -1 + paramArrayOfString.length)
          continue;
        localStringBuilder1.append(", ");
        localStringBuilder2.append(", ");
      }
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = str1;
      arrayOfObject[1] = localStringBuilder1.toString();
      arrayOfObject[2] = localStringBuilder2.toString();
      String str2 = JsonstoreUtil.formatString("INSERT INTO {0} ({1}) VALUES ({2});", arrayOfObject);
      this.logger.logDebug("executing insert on database \"" + str1 + "\":");
      this.logger.logDebug("   " + str2);
    }
    for (int j = 0; j < paramArrayOfString.length; j++)
      localContentValues.put("'" + DatabaseSchema.getDatabaseSafeNodeName(paramArrayOfString[j]) + "'", paramArrayOfObject[j].toString());
    return getDatabase().insert(str1, null, localContentValues);
  }

  public long insert(Map<String, Object> paramMap)
  {
    String[] arrayOfString = new String[paramMap.size()];
    Object[] arrayOfObject = new Object[paramMap.size()];
    int i = 0;
    Iterator localIterator = paramMap.keySet().iterator();
    if (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      arrayOfString[i] = str;
      Object localObject = paramMap.get(str);
      if ((localObject instanceof Boolean))
        if (!((Boolean)localObject).booleanValue())
          break label122;
      label122: for (int k = 1; ; k = 0)
      {
        localObject = Integer.valueOf(k);
        int j = i + 1;
        arrayOfObject[i] = localObject;
        i = j;
        break;
      }
    }
    return insert(null, arrayOfString, arrayOfObject);
  }

  public int update(Map<String, Object> paramMap1, Map<String, Object> paramMap2)
  {
    String[] arrayOfString = new String[paramMap1.size()];
    Object[] arrayOfObject = new Object[paramMap1.size()];
    int i = 0;
    Iterator localIterator = paramMap1.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      arrayOfString[i] = ("[" + DatabaseSchema.getDatabaseSafeNodeName(str) + "]");
      int j = i + 1;
      arrayOfObject[i] = paramMap1.get(str);
      i = j;
    }
    return update(arrayOfString, arrayOfObject, paramMap2);
  }

  public int update(JSONObject paramJSONObject, boolean paramBoolean)
    throws Throwable
  {
    long l;
    if (paramBoolean)
      l = new Date().getTime();
    int i;
    HashMap localHashMap;
    Map localMap;
    String str;
    while (true)
    {
      i = paramJSONObject.getInt("_id");
      localHashMap = new HashMap();
      JSONObject localJSONObject = paramJSONObject.getJSONObject("json");
      localMap = getSchema().mapObject(localJSONObject, null);
      localMap.put("_dirty", Long.valueOf(l));
      localMap.put("_id", Integer.valueOf(i));
      localMap.put("json", localJSONObject.toString());
      str = findOperationForObjectById(i);
      if ((str != null) && (!str.equals("remove")))
        break;
      throw new Throwable();
      l = 0L;
    }
    if (!str.equals("add"))
      localMap.put("_operation", "replace");
    localHashMap.put("_id", Integer.valueOf(i));
    return update(localMap, localHashMap);
  }

  public int update(String[] paramArrayOfString, Object[] paramArrayOfObject, Map<String, Object> paramMap)
  {
    String str1 = getSchema().getName();
    ContentValues localContentValues = new ContentValues();
    StringBuilder localStringBuilder1 = null;
    String[] arrayOfString1 = null;
    if (paramMap != null)
    {
      int n = paramMap.size();
      localStringBuilder1 = null;
      arrayOfString1 = null;
      if (n > 0)
      {
        int i1 = 0;
        Iterator localIterator = paramMap.keySet().iterator();
        localStringBuilder1 = new StringBuilder();
        arrayOfString1 = new String[paramMap.size()];
        while (localIterator.hasNext())
        {
          String str6 = (String)localIterator.next();
          localStringBuilder1.append(' ');
          localStringBuilder1.append(str6);
          localStringBuilder1.append(" = ?");
          if (localIterator.hasNext())
            localStringBuilder1.append(" AND");
          int i2 = i1 + 1;
          arrayOfString1[i1] = paramMap.get(str6).toString();
          i1 = i2;
        }
      }
    }
    if (this.logger.isLoggable(3))
    {
      StringBuilder localStringBuilder2 = new StringBuilder();
      for (int i = 0; i < paramArrayOfString.length; i++)
      {
        localStringBuilder2.append(paramArrayOfString[i]);
        localStringBuilder2.append(" = ");
        localStringBuilder2.append(paramArrayOfObject[i]);
        if (i >= -1 + paramArrayOfString.length)
          continue;
        localStringBuilder2.append(", ");
      }
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = str1;
      arrayOfObject[1] = localStringBuilder2.toString();
      if (localStringBuilder1 == null);
      for (String str2 = ""; ; str2 = " WHERE" + localStringBuilder1.toString())
      {
        arrayOfObject[2] = str2;
        String str3 = JsonstoreUtil.formatString("UPDATE {0} SET {1}{2};", arrayOfObject);
        this.logger.logDebug("executing update on database \"" + str1 + "\":");
        this.logger.logDebug("   sql: " + str3);
        this.logger.logDebug("   arguments:");
        for (String str5 : arrayOfString1)
          this.logger.logDebug("      " + str5);
      }
    }
    for (int m = 0; m < paramArrayOfString.length; m++)
      localContentValues.put(paramArrayOfString[m], paramArrayOfObject[m].toString());
    SQLiteDatabase localSQLiteDatabase = getDatabase();
    if (localStringBuilder1 == null);
    for (String str4 = null; ; str4 = localStringBuilder1.toString())
      return localSQLiteDatabase.update(str1, localContentValues, str4, arrayOfString1);
  }

  public int update(String[] paramArrayOfString, Object[] paramArrayOfObject, JSONObject paramJSONObject)
    throws JSONException
  {
    Iterator localIterator = paramJSONObject.keys();
    HashMap localHashMap = new HashMap();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      localHashMap.put(str, paramJSONObject.get(str));
    }
    return update(paramArrayOfString, paramArrayOfObject, localHashMap);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.jsonstore.database.WritableDatabase
 * JD-Core Version:    0.6.0
 */