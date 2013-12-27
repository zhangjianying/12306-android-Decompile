package com.worklight.androidgap.plugin.storage;

import com.worklight.androidgap.jsonstore.database.DatabaseSchema;
import com.worklight.androidgap.jsonstore.database.WritableDatabase;
import com.worklight.androidgap.jsonstore.util.Logger;
import java.util.HashMap;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONObject;

public class MarkCleanActionDispatcher extends DatabaseActionDispatcher
{
  private static final String PARAM_DOCS = "docs";
  private static final String PARAM_OPTIONS = "options";

  protected MarkCleanActionDispatcher()
  {
    super("markClean");
    BaseActionDispatcher.ParameterType[] arrayOfParameterType1 = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType1[0] = BaseActionDispatcher.ParameterType.ARRAY;
    addParameter("docs", true, arrayOfParameterType1);
    BaseActionDispatcher.ParameterType[] arrayOfParameterType2 = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType2[0] = BaseActionDispatcher.ParameterType.OBJECT;
    addParameter("options", false, arrayOfParameterType2);
  }

  private JSONArray getDocs(DatabaseActionDispatcher.Context paramContext)
  {
    return paramContext.getArrayParameter("docs");
  }

  public PluginResult dispatch(DatabaseActionDispatcher.Context paramContext)
    throws Throwable
  {
    JSONArray localJSONArray = getDocs(paramContext);
    for (int i = 0; i < localJSONArray.length(); i++)
    {
      JSONObject localJSONObject = localJSONArray.getJSONObject(i);
      MarkCleanAction localMarkCleanAction = new MarkCleanAction(localJSONObject.getInt("_id"), localJSONObject.getString("_operation"), null);
      try
      {
        if (((Integer)paramContext.performWritableDatabaseAction(localMarkCleanAction)).intValue() > 0)
          continue;
        PluginResult localPluginResult = new PluginResult(PluginResult.Status.ERROR, 15);
        return localPluginResult;
      }
      catch (Throwable localThrowable)
      {
        Exception localException = new Exception("Failed trying to mark document clean");
        this.logger.logError("Failed trying to mark document clean", localException);
        return new PluginResult(PluginResult.Status.ERROR, 15);
      }
    }
    return new PluginResult(PluginResult.Status.OK, 0);
  }

  private class MarkCleanAction
    implements DatabaseActionDispatcher.WritableDatabaseAction<Integer>
  {
    private int id;
    private String operation;

    private MarkCleanAction(int paramString, String arg3)
    {
      this.id = paramString;
      Object localObject;
      this.operation = localObject;
    }

    public Integer performAction(DatabaseSchema paramDatabaseSchema, WritableDatabase paramWritableDatabase)
      throws Throwable
    {
      if (this.operation.equals("remove"))
      {
        String[] arrayOfString1 = { "_id" };
        Object[] arrayOfObject1 = new Object[1];
        arrayOfObject1[0] = Integer.valueOf(this.id);
        return Integer.valueOf(paramWritableDatabase.delete(arrayOfString1, arrayOfObject1));
      }
      HashMap localHashMap = new HashMap();
      localHashMap.put("_id", Integer.valueOf(this.id));
      String[] arrayOfString2 = { "_dirty", "_deleted", "_operation" };
      Object[] arrayOfObject2 = new Object[3];
      arrayOfObject2[0] = Integer.valueOf(0);
      arrayOfObject2[1] = Integer.valueOf(0);
      arrayOfObject2[2] = "";
      return Integer.valueOf(paramWritableDatabase.update(arrayOfString2, arrayOfObject2, localHashMap));
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.MarkCleanActionDispatcher
 * JD-Core Version:    0.6.0
 */