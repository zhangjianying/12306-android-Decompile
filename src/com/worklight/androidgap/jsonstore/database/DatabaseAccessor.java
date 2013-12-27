package com.worklight.androidgap.jsonstore.database;

import net.sqlcipher.database.SQLiteDatabase;

public abstract interface DatabaseAccessor
{
  public abstract SQLiteDatabase getRawDatabase();

  public abstract ReadableDatabase getReadableDatabase();

  public abstract DatabaseSchema getSchema();

  public abstract WritableDatabase getWritableDatabase();
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.jsonstore.database.DatabaseAccessor
 * JD-Core Version:    0.6.0
 */