package com.worklight.androidgap.jsonstore.database;

import android.database.Cursor;
import com.worklight.androidgap.jsonstore.util.JsonstoreUtil;
import com.worklight.androidgap.jsonstore.util.Logger;
import java.util.HashSet;
import java.util.Iterator;
import net.sqlcipher.database.SQLiteDatabase;

public class DatabaseAccessorImpl
  implements DatabaseAccessor
{
  private static final String SQL_CREATE_TABLE = "CREATE TABLE {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT, {2} {3} TEXT, {4} REAL DEFAULT 0, {5} INTEGER DEFAULT 0, {6} TEXT);";
  private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS {0};";
  private static final String SQL_TABLE_EXISTS = "SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = \"{0}\";";
  private static final HashSet<String> ignoredSchemaNodes = new HashSet();
  private static final Logger logger = JsonstoreUtil.getDatabaseLogger();
  private SQLiteDatabase database;
  private ReadableDatabase readableDB;
  private DatabaseSchema schema;
  private WritableDatabase writableDB;

  static
  {
    ignoredSchemaNodes.add("_deleted");
    ignoredSchemaNodes.add("_dirty");
    ignoredSchemaNodes.add("_id");
    ignoredSchemaNodes.add("json");
    ignoredSchemaNodes.add("_operation");
  }

  protected DatabaseAccessorImpl(SQLiteDatabase paramSQLiteDatabase, DatabaseSchema paramDatabaseSchema)
  {
    this.database = paramSQLiteDatabase;
    this.readableDB = new ReadableDatabase(paramSQLiteDatabase, paramDatabaseSchema);
    this.schema = paramDatabaseSchema;
    this.writableDB = new WritableDatabase(paramSQLiteDatabase, paramDatabaseSchema);
  }

  private void execSQL(String paramString, Object[] paramArrayOfObject)
  {
    String str = JsonstoreUtil.formatString(paramString, paramArrayOfObject);
    if (logger.isLoggable(3))
    {
      logger.logDebug("executing SQL on database \"" + this.schema.getName() + "\":");
      logger.logDebug("   " + str);
    }
    this.writableDB.getDatabase().execSQL(str);
  }

  private String formatSchemaColumns()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Iterator localIterator1 = this.schema.getNodeNames();
    HashSet localHashSet = new HashSet();
    while (localIterator1.hasNext())
    {
      String str2 = (String)localIterator1.next();
      if (ignoredSchemaNodes.contains(str2))
        continue;
      localHashSet.add(str2);
    }
    Iterator localIterator2 = localHashSet.iterator();
    while (localIterator2.hasNext())
    {
      String str1 = (String)localIterator2.next();
      localStringBuilder.append("'" + DatabaseSchema.getDatabaseSafeNodeName(str1) + "'");
      localStringBuilder.append(' ');
      localStringBuilder.append(this.schema.getNodeType(str1).getMappedType());
      localStringBuilder.append(", ");
    }
    return localStringBuilder.toString();
  }

  public void createTable()
  {
    String str = this.schema.getName();
    if (logger.isLoggable(3))
      logger.logDebug("creating database \"" + str + "\"");
    Object[] arrayOfObject = new Object[7];
    arrayOfObject[0] = str;
    arrayOfObject[1] = "_id";
    arrayOfObject[2] = formatSchemaColumns();
    arrayOfObject[3] = "json";
    arrayOfObject[4] = "_dirty";
    arrayOfObject[5] = "_deleted";
    arrayOfObject[6] = "_operation";
    execSQL("CREATE TABLE {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT, {2} {3} TEXT, {4} REAL DEFAULT 0, {5} INTEGER DEFAULT 0, {6} TEXT);", arrayOfObject);
  }

  public void dropTable()
  {
    String str = this.schema.getName();
    if (logger.isLoggable(3))
      logger.logDebug("dropping database \"" + str + "\"");
    execSQL("DROP TABLE IF EXISTS {0};", new Object[] { str });
  }

  public SQLiteDatabase getRawDatabase()
  {
    return this.database;
  }

  public ReadableDatabase getReadableDatabase()
  {
    return this.readableDB;
  }

  public DatabaseSchema getSchema()
  {
    return this.schema;
  }

  public boolean getTableExists()
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = this.schema.getName();
    String str = JsonstoreUtil.formatString("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = \"{0}\";", arrayOfObject);
    Cursor localCursor = this.readableDB.rawQuery(str, null);
    int i = 0;
    if (localCursor != null)
    {
      int j = localCursor.getCount();
      i = 0;
      if (j > 0)
        i = 1;
      localCursor.close();
    }
    return i;
  }

  public WritableDatabase getWritableDatabase()
  {
    return this.writableDB;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.jsonstore.database.DatabaseAccessorImpl
 * JD-Core Version:    0.6.0
 */