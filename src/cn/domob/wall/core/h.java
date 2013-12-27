package cn.domob.wall.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;

final class h extends SQLiteOpenHelper
{
  static final String a = "imp";
  static final String b = "image";
  static final String c = "wall.db";
  static final int d = 2;
  private static p e = new p(h.class.getSimpleName());
  private static h f = null;
  private SQLiteDatabase g = null;

  private h(Context paramContext)
  {
    super(paramContext, "wall.db", null, 2);
  }

  static h a()
  {
    return f;
  }

  static void a(Context paramContext)
  {
    monitorenter;
    try
    {
      if (f == null)
        f = new h(paramContext.getApplicationContext());
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

  public int a(String paramString)
  {
    Cursor localCursor = this.g.rawQuery("select count(*) from " + paramString, null);
    int i;
    if (localCursor != null)
      if (localCursor.moveToNext())
      {
        i = localCursor.getInt(0);
        localCursor.close();
      }
    while (true)
    {
      p localp = e;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Integer.valueOf(i);
      arrayOfObject[1] = paramString;
      localp.b(String.format("The total number of rows is %s, table: %s ", arrayOfObject));
      return i;
      i = 0;
      break;
      i = 0;
    }
  }

  public int a(String paramString1, ContentValues paramContentValues, String paramString2, String[] paramArrayOfString)
  {
    e.b(String.format("Update table: %s, where: ", new Object[] { paramString1, paramString2 }));
    return this.g.update(paramString1, paramContentValues, paramString2, paramArrayOfString);
  }

  public int a(String paramString1, String paramString2, String[] paramArrayOfString)
  {
    Cursor localCursor = this.g.rawQuery("select count(*) from " + paramString1 + " where " + paramString2, paramArrayOfString);
    int i;
    if (localCursor != null)
      if (localCursor.moveToNext())
      {
        i = localCursor.getInt(0);
        localCursor.close();
      }
    while (true)
    {
      p localp = e;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Integer.valueOf(i);
      arrayOfObject[1] = paramString1;
      localp.b(String.format("The query result is %s lines, table: %s", arrayOfObject));
      return i;
      i = 0;
      break;
      i = 0;
    }
  }

  public Cursor a(String paramString, String[] paramArrayOfString)
  {
    e.b("RawQuery: " + paramString);
    return this.g.rawQuery(paramString, paramArrayOfString);
  }

  public Cursor a(String paramString1, String[] paramArrayOfString1, String paramString2, String[] paramArrayOfString2, String paramString3)
  {
    SQLiteQueryBuilder localSQLiteQueryBuilder = new SQLiteQueryBuilder();
    localSQLiteQueryBuilder.setTables(paramString1);
    return localSQLiteQueryBuilder.query(this.g, paramArrayOfString1, paramString2, paramArrayOfString2, null, null, paramString3);
  }

  public void a(Context paramContext, String paramString)
  {
    e.d("Delete all data!! table" + paramString);
    b(paramString, null, null);
  }

  public boolean a(String paramString, ContentValues paramContentValues)
  {
    if (this.g.insert(paramString, null, paramContentValues) <= 0L)
    {
      e.b("Insert: failed! " + paramContentValues.toString());
      return false;
    }
    return true;
  }

  public int b(String paramString1, String paramString2, String[] paramArrayOfString)
  {
    if ((paramString2 != null) && (paramArrayOfString != null))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      int j = paramArrayOfString.length;
      for (int k = 0; k < j; k++)
      {
        localStringBuilder.append(paramArrayOfString[k]);
        localStringBuilder.append(";");
      }
      p localp = e;
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = paramString1;
      arrayOfObject[1] = paramString2;
      arrayOfObject[2] = localStringBuilder.toString();
      localp.b(String.format("delete table=%s  where=%s  whereArgs=%s", arrayOfObject));
    }
    while (true)
    {
      int i = this.g.delete(paramString1, paramString2, paramArrayOfString);
      e.b("Successfully deleted data quantity is " + i);
      return i;
      if (paramString2 == null)
        continue;
      e.b(String.format("delete table=%s  where=%s", new Object[] { paramString1, paramString2 }));
    }
  }

  public void b(String paramString)
  {
    e.b("execSQL: " + paramString);
    this.g.execSQL(paramString);
  }

  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (%s integer primary key autoincrement,%s VARCHAR ,%s TEXT,%s VARCHAR ,%s VARCHAR);", new Object[] { "imp", "id", "sid", "tr", "ad_id", "ppid" }));
    paramSQLiteDatabase.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (%s integer primary key autoincrement ,%s INTEGER ,%s TEXT ,%s TEXT,%s INTEGER);", new Object[] { "image", "id", "lastUseTime", "url", "local_path", "size" }));
  }

  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS imp;");
    onCreate(paramSQLiteDatabase);
  }

  static final class a
    implements BaseColumns
  {
    static final String a = "id";
    static final String b = "url";
    static final String c = "lastUseTime";
    static final String d = "local_path";
    static final String e = "size";
  }

  static final class b
    implements BaseColumns
  {
    static final String a = "id";
    static final String b = "sid";
    static final String c = "tr";
    static final String d = "ad_id";
    static final String e = "ppid";
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.h
 * JD-Core Version:    0.6.0
 */