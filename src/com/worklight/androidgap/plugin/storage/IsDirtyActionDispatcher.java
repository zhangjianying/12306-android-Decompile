package com.worklight.androidgap.plugin.storage;

import com.worklight.androidgap.jsonstore.util.JsonstoreUtil;
import com.worklight.androidgap.jsonstore.util.Logger;
import org.json.JSONObject;

public class IsDirtyActionDispatcher extends SimpleQueryActionDispatcher
{
  private static final String PARAM_OBJ = "obj";
  private static final String SQL_IS_DIRTY = "SELECT {0} FROM {1} WHERE {0} > 0 AND _id = {2}";

  protected IsDirtyActionDispatcher()
  {
    super("isDirty");
    BaseActionDispatcher.ParameterType[] arrayOfParameterType = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType[0] = BaseActionDispatcher.ParameterType.OBJECT;
    addParameter("obj", true, arrayOfParameterType);
  }

  private int getId(DatabaseActionDispatcher.Context paramContext)
    throws Throwable
  {
    return paramContext.getObjectParameter("obj").getInt("_id");
  }

  protected String getFormattedQuery(DatabaseActionDispatcher.Context paramContext)
    throws Throwable
  {
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = "_dirty";
    arrayOfObject[1] = paramContext.getDatabaseName();
    arrayOfObject[2] = Integer.valueOf(getId(paramContext));
    return JsonstoreUtil.formatString("SELECT {0} FROM {1} WHERE {0} > 0 AND _id = {2}", arrayOfObject);
  }

  protected int getModifiedResultValue(int paramInt)
  {
    if (paramInt != 0)
      paramInt = 1;
    return paramInt;
  }

  protected int getNotFoundResultValue()
  {
    return 0;
  }

  protected void logResult(DatabaseActionDispatcher.Context paramContext, int paramInt)
    throws Throwable
  {
    Logger localLogger;
    StringBuilder localStringBuilder;
    if (this.logger.isLoggable(3))
    {
      localLogger = this.logger;
      localStringBuilder = new StringBuilder().append("is document with ID ").append(getId(paramContext)).append(" in database \"").append(paramContext.getDatabaseName()).append("\" ").append("dirty? ");
      if (paramInt <= 0)
        break label83;
    }
    label83: for (String str = "true"; ; str = "false")
    {
      localLogger.logDebug(str);
      return;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.IsDirtyActionDispatcher
 * JD-Core Version:    0.6.0
 */