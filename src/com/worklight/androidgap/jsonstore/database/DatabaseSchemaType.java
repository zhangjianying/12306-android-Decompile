package com.worklight.androidgap.jsonstore.database;

public enum DatabaseSchemaType
{
  private String mappedType;
  private String name;

  static
  {
    DatabaseSchemaType[] arrayOfDatabaseSchemaType = new DatabaseSchemaType[4];
    arrayOfDatabaseSchemaType[0] = BOOLEAN;
    arrayOfDatabaseSchemaType[1] = INTEGER;
    arrayOfDatabaseSchemaType[2] = NUMBER;
    arrayOfDatabaseSchemaType[3] = STRING;
    $VALUES = arrayOfDatabaseSchemaType;
  }

  private DatabaseSchemaType(String paramString1, String paramString2)
  {
    this.name = paramString1;
    this.mappedType = paramString2;
  }

  protected static DatabaseSchemaType fromString(String paramString)
  {
    if (paramString.equals(BOOLEAN.name))
      return BOOLEAN;
    if (paramString.equals(INTEGER.name))
      return INTEGER;
    if (paramString.equals(NUMBER.name))
      return NUMBER;
    if (paramString.equals(STRING.name))
      return STRING;
    return null;
  }

  public String getMappedType()
  {
    return this.mappedType;
  }

  public String getName()
  {
    return this.name;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.jsonstore.database.DatabaseSchemaType
 * JD-Core Version:    0.6.0
 */