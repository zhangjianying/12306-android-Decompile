package com.worklight.androidgap.plugin.storage;

import com.worklight.androidgap.jsonstore.util.JsonstoreUtil;
import com.worklight.androidgap.jsonstore.util.Logger;

public class LocalCountActionDispatcher extends SimpleQueryActionDispatcher
{
  private static final String SQL_LOCALCOUNT = "SELECT COUNT(*) FROM {0} WHERE {1} > 0";

  protected LocalCountActionDispatcher()
  {
    super("localCount");
  }

  protected String getFormattedQuery(DatabaseActionDispatcher.Context paramContext)
    throws Throwable
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = paramContext.getDatabaseName();
    arrayOfObject[1] = "_dirty";
    return JsonstoreUtil.formatString("SELECT COUNT(*) FROM {0} WHERE {1} > 0", arrayOfObject);
  }

  protected void logResult(DatabaseActionDispatcher.Context paramContext, int paramInt)
    throws Throwable
  {
    if (this.logger.isLoggable(3))
      this.logger.logDebug("database \"" + paramContext.getDatabaseName() + "\" contains " + paramInt + " dirty records");
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.LocalCountActionDispatcher
 * JD-Core Version:    0.6.0
 */