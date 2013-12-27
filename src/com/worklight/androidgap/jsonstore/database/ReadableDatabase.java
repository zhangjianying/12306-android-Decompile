package com.worklight.androidgap.jsonstore.database;

import android.database.Cursor;
import com.worklight.androidgap.jsonstore.util.JsonstoreUtil;
import com.worklight.androidgap.jsonstore.util.Logger;
import java.util.ArrayList;
import java.util.Iterator;
import net.sqlcipher.database.SQLiteDatabase;
import org.json.JSONException;
import org.json.JSONObject;

public class ReadableDatabase
{
  private static final String SQL_AND = " AND ";
  private static final String SQL_EQ = " = ?";
  private static final String SQL_FIND = "SELECT {0} FROM {1};";
  private static final String SQL_FIND_BY_ID = "SELECT {0}, {1} FROM {2} WHERE {3} AND _deleted = 0";
  private static final String SQL_FIND_OP = "SELECT {0} FROM {1} WHERE {2} LIKE ?";
  private static final String SQL_FIND_WHERE = "SELECT {0} FROM {1} WHERE {2};";
  private static final String SQL_FIND_WHERE_WITH_LIMIT = "SELECT {0} FROM {1} WHERE {2} LIMIT {3};";
  private static final String SQL_FIND_WHERE_WITH_LIMIT_AND_OFFSET = "SELECT {0} FROM {1} WHERE {2} LIMIT {3} OFFSET {4};";
  private static final String SQL_FIND_WHERE_WITH_NEGATIVE_LIMIT = "SELECT {0} FROM {1} WHERE {2} ORDER BY {3} DESC LIMIT {4};";
  private static final String SQL_LIKE = " LIKE ?";
  private static final String SQL_OR = " OR ";
  private SQLiteDatabase database;
  protected Logger logger;
  private DatabaseSchema schema;

  protected ReadableDatabase(SQLiteDatabase paramSQLiteDatabase, DatabaseSchema paramDatabaseSchema)
  {
    this.database = paramSQLiteDatabase;
    this.logger = JsonstoreUtil.getDatabaseLogger();
    this.schema = paramDatabaseSchema;
  }

  public Cursor findByIds(int[] paramArrayOfInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    String[] arrayOfString = new String[paramArrayOfInt.length];
    for (int i = 0; i < paramArrayOfInt.length; i++)
    {
      localStringBuilder.append("_id");
      localStringBuilder.append(" = ?");
      if (i < -1 + paramArrayOfInt.length)
        localStringBuilder.append(" OR ");
      arrayOfString[i] = ("" + paramArrayOfInt[i]);
    }
    Object[] arrayOfObject = new Object[4];
    arrayOfObject[0] = "_id";
    arrayOfObject[1] = "json";
    arrayOfObject[2] = this.schema.getName();
    arrayOfObject[3] = localStringBuilder.toString();
    return rawQuery(JsonstoreUtil.formatString("SELECT {0}, {1} FROM {2} WHERE {3} AND _deleted = 0", arrayOfObject), arrayOfString);
  }

  protected String findOperationForObjectById(int paramInt)
  {
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = "_operation";
    arrayOfObject[1] = this.schema.getName();
    arrayOfObject[2] = "_id";
    String str1 = JsonstoreUtil.formatString("SELECT {0} FROM {1} WHERE {2} LIKE ?", arrayOfObject);
    String[] arrayOfString = new String[1];
    arrayOfString[0] = ("" + paramInt);
    Cursor localCursor = rawQuery(str1, arrayOfString);
    if (localCursor.getCount() < 1)
    {
      localCursor.close();
      return null;
    }
    localCursor.moveToNext();
    String str2 = localCursor.getString(0);
    localCursor.close();
    return str2;
  }

  public Cursor findUsingQueryObject(JSONObject paramJSONObject, String[] paramArrayOfString1, String[] paramArrayOfString2, String paramString1, String paramString2, Boolean paramBoolean)
    throws JSONException
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    int i = paramJSONObject.length();
    ArrayList localArrayList = new ArrayList();
    StringBuilder localStringBuilder2 = new StringBuilder();
    if ((paramArrayOfString1 == null) || (paramArrayOfString1.length == 0))
      localStringBuilder1.append('*');
    while ((paramArrayOfString2 != null) && (paramArrayOfString2.length > 0))
    {
      for (int i3 = 0; i3 < paramArrayOfString2.length; i3++)
      {
        localStringBuilder2.append(paramArrayOfString2[i3]);
        if (i3 >= -1 + paramArrayOfString2.length)
          continue;
        localStringBuilder2.append(" AND ");
      }
      for (int i4 = 0; i4 < paramArrayOfString1.length; i4++)
      {
        localStringBuilder1.append(paramArrayOfString1[i4]);
        if (i4 >= -1 + paramArrayOfString1.length)
          continue;
        localStringBuilder1.append(", ");
      }
    }
    if (i == 0);
    while (true)
    {
      String str1;
      try
      {
        int i2 = Integer.parseInt(paramString1);
        i1 = i2;
        if (localStringBuilder2.length() <= 0)
          continue;
        if (paramString1 != null)
          continue;
        Object[] arrayOfObject9 = new Object[3];
        arrayOfObject9[0] = localStringBuilder1.toString();
        arrayOfObject9[1] = this.schema.getName();
        arrayOfObject9[2] = localStringBuilder2.toString();
        str1 = JsonstoreUtil.formatString("SELECT {0} FROM {1} WHERE {2};", arrayOfObject9);
        return rawQuery(str1, (String[])localArrayList.toArray(new String[localArrayList.size()]));
      }
      catch (NumberFormatException localNumberFormatException2)
      {
        int i1 = 0;
        continue;
        if (paramString2 == null)
          continue;
        Object[] arrayOfObject8 = new Object[5];
        arrayOfObject8[0] = localStringBuilder1.toString();
        arrayOfObject8[1] = this.schema.getName();
        arrayOfObject8[2] = localStringBuilder2.toString();
        arrayOfObject8[3] = paramString1;
        arrayOfObject8[4] = paramString2;
        str1 = JsonstoreUtil.formatString("SELECT {0} FROM {1} WHERE {2} LIMIT {3} OFFSET {4};", arrayOfObject8);
        continue;
        if (i1 >= 0)
          continue;
        String str6 = String.valueOf(Math.abs(i1));
        Object[] arrayOfObject7 = new Object[5];
        arrayOfObject7[0] = localStringBuilder1.toString();
        arrayOfObject7[1] = this.schema.getName();
        arrayOfObject7[2] = localStringBuilder2.toString();
        arrayOfObject7[3] = "_id";
        arrayOfObject7[4] = str6;
        str1 = JsonstoreUtil.formatString("SELECT {0} FROM {1} WHERE {2} ORDER BY {3} DESC LIMIT {4};", arrayOfObject7);
        continue;
        Object[] arrayOfObject6 = new Object[4];
        arrayOfObject6[0] = localStringBuilder1.toString();
        arrayOfObject6[1] = this.schema.getName();
        arrayOfObject6[2] = localStringBuilder2.toString();
        arrayOfObject6[3] = paramString1;
        str1 = JsonstoreUtil.formatString("SELECT {0} FROM {1} WHERE {2} LIMIT {3};", arrayOfObject6);
        continue;
        Object[] arrayOfObject5 = new Object[2];
        arrayOfObject5[0] = localStringBuilder1.toString();
        arrayOfObject5[1] = this.schema.getName();
        str1 = JsonstoreUtil.formatString("SELECT {0} FROM {1};", arrayOfObject5);
        continue;
      }
      try
      {
        int n = Integer.parseInt(paramString1);
        j = n;
        localStringBuilder3 = new StringBuilder();
        localIterator = paramJSONObject.keys();
        if (!paramBoolean.booleanValue())
          while (true)
          {
            if (!localIterator.hasNext())
              break label1008;
            String str5 = (String)localIterator.next();
            Object localObject2 = paramJSONObject.get(str5);
            localStringBuilder3.append("[" + DatabaseSchema.getDatabaseSafeNodeName(str5) + "]");
            localStringBuilder3.append(" LIKE ?");
            if (localIterator.hasNext())
              localStringBuilder3.append(" AND ");
            if ((localObject2 instanceof Boolean))
            {
              if (!((Boolean)localObject2).booleanValue())
                break;
              m = 1;
              localObject2 = Integer.valueOf(m);
            }
            localArrayList.add("%" + localObject2 + "%");
          }
      }
      catch (NumberFormatException localNumberFormatException1)
      {
        int j;
        StringBuilder localStringBuilder3;
        Iterator localIterator;
        while (true)
        {
          j = 0;
          continue;
          int m = 0;
        }
        if (localIterator.hasNext())
        {
          String str3 = (String)localIterator.next();
          Object localObject1 = paramJSONObject.get(str3);
          String str4 = "[" + DatabaseSchema.getDatabaseSafeNodeName(str3) + "]";
          if ((localObject1 instanceof Boolean))
            if (!((Boolean)localObject1).booleanValue())
              break label1002;
          label1002: for (int k = 1; ; k = 0)
          {
            localObject1 = Integer.valueOf(k);
            localStringBuilder3.append(" ( ");
            localStringBuilder3.append(str4);
            localStringBuilder3.append(" = ?");
            localStringBuilder3.append(" OR ");
            localStringBuilder3.append(str4);
            localStringBuilder3.append(" LIKE ?");
            localStringBuilder3.append(" OR ");
            localStringBuilder3.append(str4);
            localStringBuilder3.append(" LIKE ?");
            localStringBuilder3.append(" OR ");
            localStringBuilder3.append(str4);
            localStringBuilder3.append(" LIKE ?");
            localArrayList.add("" + localObject1);
            localArrayList.add("%-@-" + localObject1);
            localArrayList.add("%-@-" + localObject1 + "-@-%");
            localArrayList.add(localObject1 + "-@-%");
            localStringBuilder3.append(" ) ");
            if (!localIterator.hasNext())
              break;
            localStringBuilder3.append(" AND ");
            break;
          }
        }
        label1008: if (localStringBuilder2.length() > 0)
        {
          localStringBuilder3.append(" AND ");
          localStringBuilder3.append(localStringBuilder2);
        }
        if (paramString1 == null)
        {
          Object[] arrayOfObject4 = new Object[3];
          arrayOfObject4[0] = localStringBuilder1.toString();
          arrayOfObject4[1] = this.schema.getName();
          arrayOfObject4[2] = localStringBuilder3.toString();
          str1 = JsonstoreUtil.formatString("SELECT {0} FROM {1} WHERE {2};", arrayOfObject4);
          continue;
        }
        if (paramString2 != null)
        {
          Object[] arrayOfObject3 = new Object[5];
          arrayOfObject3[0] = localStringBuilder1.toString();
          arrayOfObject3[1] = this.schema.getName();
          arrayOfObject3[2] = localStringBuilder3.toString();
          arrayOfObject3[3] = paramString1;
          arrayOfObject3[4] = paramString2;
          str1 = JsonstoreUtil.formatString("SELECT {0} FROM {1} WHERE {2} LIMIT {3} OFFSET {4};", arrayOfObject3);
          continue;
        }
        if (j < 0)
        {
          String str2 = String.valueOf(Math.abs(j));
          Object[] arrayOfObject2 = new Object[5];
          arrayOfObject2[0] = localStringBuilder1.toString();
          arrayOfObject2[1] = this.schema.getName();
          arrayOfObject2[2] = localStringBuilder3.toString();
          arrayOfObject2[3] = "_id";
          arrayOfObject2[4] = str2;
          str1 = JsonstoreUtil.formatString("SELECT {0} FROM {1} WHERE {2} ORDER BY {3} DESC LIMIT {4};", arrayOfObject2);
          continue;
        }
        Object[] arrayOfObject1 = new Object[4];
        arrayOfObject1[0] = localStringBuilder1.toString();
        arrayOfObject1[1] = this.schema.getName();
        arrayOfObject1[2] = localStringBuilder3.toString();
        arrayOfObject1[3] = paramString1;
        str1 = JsonstoreUtil.formatString("SELECT {0} FROM {1} WHERE {2} LIMIT {3};", arrayOfObject1);
      }
    }
  }

  protected SQLiteDatabase getDatabase()
  {
    return this.database;
  }

  protected DatabaseSchema getSchema()
  {
    return this.schema;
  }

  public Cursor rawQuery(String paramString, String[] paramArrayOfString)
  {
    if (this.logger.isLoggable(3))
    {
      this.logger.logDebug("executing query on database \"" + this.schema.getName() + "\":");
      this.logger.logDebug("   " + paramString);
      if (paramArrayOfString != null)
      {
        this.logger.logDebug("arguments:");
        int i = paramArrayOfString.length;
        for (int j = 0; j < i; j++)
        {
          String str = paramArrayOfString[j];
          this.logger.logDebug("   " + str);
        }
      }
    }
    return this.database.rawQuery(paramString, paramArrayOfString);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.jsonstore.database.ReadableDatabase
 * JD-Core Version:    0.6.0
 */