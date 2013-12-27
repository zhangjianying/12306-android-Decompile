package com.worklight.androidgap.plugin.storage;

import com.worklight.androidgap.jsonstore.database.DatabaseSchema;
import com.worklight.androidgap.jsonstore.database.WritableDatabase;
import com.worklight.androidgap.jsonstore.util.Logger;
import com.worklight.androidgap.jsonstore.util.jackson.JacksonSerializedJSONArray;
import com.worklight.androidgap.jsonstore.util.jackson.JacksonSerializedPluginResult;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RemoveActionDispatcher extends DatabaseActionDispatcher
{
  private static final String OPTION_EXACT = "exact";
  private static final String OPTION_IS_ERASE = "isErase";
  private static final String PARAM_OPTIONS = "options";
  private static final String PARAM_QUERY_OBJ = "queryObj";

  protected RemoveActionDispatcher()
  {
    super("remove");
    BaseActionDispatcher.ParameterType[] arrayOfParameterType1 = new BaseActionDispatcher.ParameterType[2];
    arrayOfParameterType1[0] = BaseActionDispatcher.ParameterType.ARRAY;
    arrayOfParameterType1[1] = BaseActionDispatcher.ParameterType.OBJECT;
    addParameter("queryObj", true, arrayOfParameterType1);
    BaseActionDispatcher.ParameterType[] arrayOfParameterType2 = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType2[0] = BaseActionDispatcher.ParameterType.OBJECT;
    addParameter("options", false, arrayOfParameterType2);
  }

  private JSONObject getOptions(BaseActionDispatcher.Context paramContext)
  {
    return paramContext.getObjectParameter("options");
  }

  private Object getQueryObj(DatabaseActionDispatcher.Context paramContext)
  {
    return paramContext.getUntypedParameter("queryObj");
  }

  public PluginResult dispatch(DatabaseActionDispatcher.Context paramContext)
    throws Throwable
  {
    JSONObject localJSONObject = getOptions(paramContext);
    Object localObject1 = getQueryObj(paramContext);
    boolean bool1 = false;
    boolean bool2 = false;
    if (localJSONObject != null)
    {
      bool1 = localJSONObject.optBoolean("isErase", false);
      bool2 = localJSONObject.optBoolean("exact", false);
    }
    RemoveAction localRemoveAction;
    if ((localObject1 instanceof JSONObject))
      localRemoveAction = new RemoveAction((JSONObject)localObject1, bool1, bool2, null);
    try
    {
      while (true)
      {
        Object localObject3 = paramContext.performWritableDatabaseAction(localRemoveAction);
        localObject2 = localObject3;
        if (!(localObject2 instanceof Integer))
          break;
        return new PluginResult(PluginResult.Status.OK, ((Integer)localObject2).intValue());
        localRemoveAction = new RemoveAction((JSONArray)localObject1, bool1, bool2, null);
      }
      return new JacksonSerializedPluginResult(PluginResult.Status.ERROR, (JSONArray)localObject2);
    }
    catch (Throwable localThrowable)
    {
      while (true)
        Object localObject2 = null;
    }
  }

  private class RemoveAction
    implements DatabaseActionDispatcher.WritableDatabaseAction<Object>
  {
    private boolean exact;
    private boolean isErase;
    private LinkedList<JSONObject> objs;

    private RemoveAction(JSONArray paramBoolean1, boolean paramBoolean2, boolean arg4)
      throws JSONException
    {
      this(paramBoolean2, bool);
      int i = paramBoolean1.length();
      for (int j = 0; j < i; j++)
        this.objs.add(paramBoolean1.getJSONObject(j));
    }

    private RemoveAction(JSONObject paramBoolean1, boolean paramBoolean2, boolean arg4)
    {
      this(paramBoolean2, bool);
      this.objs.add(paramBoolean1);
    }

    private RemoveAction(boolean paramBoolean1, boolean arg3)
    {
      this.isErase = paramBoolean1;
      boolean bool;
      this.exact = bool;
      this.objs = new LinkedList();
    }

    public Object performAction(DatabaseSchema paramDatabaseSchema, WritableDatabase paramWritableDatabase)
      throws Throwable
    {
      Object localObject = new JacksonSerializedJSONArray();
      int i = 0;
      Iterator localIterator = this.objs.iterator();
      while (localIterator.hasNext())
      {
        JSONObject localJSONObject = (JSONObject)localIterator.next();
        try
        {
          int j = paramWritableDatabase.deleteIfRequired(localJSONObject, this.isErase, this.exact);
          i += j;
        }
        catch (Throwable localThrowable)
        {
          if (RemoveActionDispatcher.this.logger.isLoggable(6))
            RemoveActionDispatcher.this.logger.logError("error while removing/deleting document on database \"" + paramDatabaseSchema.getName() + "\"", localThrowable);
          ((JSONArray)localObject).put(localJSONObject);
        }
      }
      if (((JSONArray)localObject).length() == 0)
        localObject = Integer.valueOf(i);
      return localObject;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.RemoveActionDispatcher
 * JD-Core Version:    0.6.0
 */