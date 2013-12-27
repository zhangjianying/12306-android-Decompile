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

public class ReplaceActionDispatcher extends DatabaseActionDispatcher
{
  private static final String OPTION_IS_REFRESH = "isRefresh";
  private static final String PARAM_OPTIONS = "options";
  private static final String PARAM_TO_UPDATE = "toUpdate";

  protected ReplaceActionDispatcher()
  {
    super("replace");
    BaseActionDispatcher.ParameterType[] arrayOfParameterType1 = new BaseActionDispatcher.ParameterType[2];
    arrayOfParameterType1[0] = BaseActionDispatcher.ParameterType.ARRAY;
    arrayOfParameterType1[1] = BaseActionDispatcher.ParameterType.OBJECT;
    addParameter("toUpdate", true, arrayOfParameterType1);
    BaseActionDispatcher.ParameterType[] arrayOfParameterType2 = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType2[0] = BaseActionDispatcher.ParameterType.OBJECT;
    addParameter("options", false, arrayOfParameterType2);
  }

  private JSONObject getOptions(BaseActionDispatcher.Context paramContext)
  {
    return paramContext.getObjectParameter("options");
  }

  private Object getToUpdate(DatabaseActionDispatcher.Context paramContext)
  {
    return paramContext.getUntypedParameter("toUpdate");
  }

  public PluginResult dispatch(DatabaseActionDispatcher.Context paramContext)
    throws Throwable
  {
    JSONObject localJSONObject = getOptions(paramContext);
    Object localObject1 = getToUpdate(paramContext);
    boolean bool = false;
    if (localJSONObject != null)
      bool = localJSONObject.optBoolean("isRefresh");
    ReplaceAction localReplaceAction;
    if ((localObject1 instanceof JSONObject))
      localReplaceAction = new ReplaceAction((JSONObject)localObject1, bool, null);
    try
    {
      while (true)
      {
        Object localObject3 = paramContext.performWritableDatabaseAction(localReplaceAction);
        localObject2 = localObject3;
        if (!(localObject2 instanceof Integer))
          break;
        return new PluginResult(PluginResult.Status.OK, ((Integer)localObject2).intValue());
        localReplaceAction = new ReplaceAction((JSONArray)localObject1, bool, null);
      }
      return new JacksonSerializedPluginResult(PluginResult.Status.ERROR, (JSONArray)localObject2);
    }
    catch (Throwable localThrowable)
    {
      while (true)
        Object localObject2 = null;
    }
  }

  private class ReplaceAction
    implements DatabaseActionDispatcher.WritableDatabaseAction<Object>
  {
    private boolean isRefresh;
    private LinkedList<JSONObject> objs;

    private ReplaceAction(JSONArray paramBoolean, boolean arg3)
      throws JSONException
    {
      this(bool);
      int i = paramBoolean.length();
      for (int j = 0; j < i; j++)
        this.objs.add(paramBoolean.getJSONObject(j));
    }

    private ReplaceAction(JSONObject paramBoolean, boolean arg3)
    {
      this(bool);
      this.objs.add(paramBoolean);
    }

    private ReplaceAction(boolean arg2)
    {
      boolean bool;
      this.isRefresh = bool;
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
          if (!this.isRefresh);
          for (boolean bool = true; ; bool = false)
          {
            int j = paramWritableDatabase.update(localJSONObject, bool);
            i += j;
            break;
          }
        }
        catch (Throwable localThrowable)
        {
          if (ReplaceActionDispatcher.this.logger.isLoggable(6))
            ReplaceActionDispatcher.this.logger.logError("error while updating document on database \"" + paramDatabaseSchema.getName() + "\"", localThrowable);
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
 * Qualified Name:     com.worklight.androidgap.plugin.storage.ReplaceActionDispatcher
 * JD-Core Version:    0.6.0
 */