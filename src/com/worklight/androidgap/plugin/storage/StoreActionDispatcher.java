package com.worklight.androidgap.plugin.storage;

import com.worklight.androidgap.jsonstore.database.DatabaseSchema;
import com.worklight.androidgap.jsonstore.database.WritableDatabase;
import com.worklight.androidgap.jsonstore.util.Logger;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreActionDispatcher extends DatabaseActionDispatcher
{
  private static final String OPTION_ADD_INDEXES = "additionalSearchFields";
  private static final String OPTION_IS_ADD = "isAdd";
  private static final String OPTION_IS_ARRAY = "isArray";
  private static final String PARAM_DATA = "data";
  private static final String PARAM_OPTIONS = "options";

  protected StoreActionDispatcher()
  {
    super("store");
    BaseActionDispatcher.ParameterType[] arrayOfParameterType1 = new BaseActionDispatcher.ParameterType[2];
    arrayOfParameterType1[0] = BaseActionDispatcher.ParameterType.ARRAY;
    arrayOfParameterType1[1] = BaseActionDispatcher.ParameterType.OBJECT;
    addParameter("data", true, arrayOfParameterType1);
    BaseActionDispatcher.ParameterType[] arrayOfParameterType2 = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType2[0] = BaseActionDispatcher.ParameterType.OBJECT;
    addParameter("options", false, arrayOfParameterType2);
  }

  private Object getData(DatabaseActionDispatcher.Context paramContext)
  {
    return paramContext.getUntypedParameter("data");
  }

  private JSONObject getOptions(DatabaseActionDispatcher.Context paramContext)
  {
    return paramContext.getObjectParameter("options");
  }

  public PluginResult dispatch(DatabaseActionDispatcher.Context paramContext)
    throws Throwable
  {
    Object localObject = getData(paramContext);
    JSONObject localJSONObject1 = getOptions(paramContext);
    boolean bool1 = false;
    JSONObject localJSONObject2 = null;
    boolean bool2 = false;
    if (localJSONObject1 != null)
    {
      localJSONObject2 = localJSONObject1.optJSONObject("additionalSearchFields");
      bool1 = localJSONObject1.optBoolean("isAdd", false);
      bool2 = localJSONObject1.optBoolean("isArray", false);
    }
    int i;
    StoreAction localStoreAction;
    if ((bool2) || ((localObject instanceof JSONObject)))
    {
      i = 1;
      localStoreAction = new StoreAction((JSONObject)localObject, bool1, localJSONObject2, null);
    }
    try
    {
      while (true)
      {
        int k = ((Integer)paramContext.performWritableDatabaseAction(localStoreAction)).intValue();
        j = k;
        if (j == i)
          break;
        return new PluginResult(PluginResult.Status.ERROR, j);
        JSONArray localJSONArray = (JSONArray)localObject;
        i = localJSONArray.length();
        localStoreAction = new StoreAction(localJSONArray, bool1, localJSONObject2, null);
      }
      return new PluginResult(PluginResult.Status.OK, j);
    }
    catch (Throwable localThrowable)
    {
      while (true)
        int j = 0;
    }
  }

  private class StoreAction
    implements DatabaseActionDispatcher.WritableDatabaseAction<Integer>
  {
    private JSONObject addIndexes;
    private boolean isAdd;
    private LinkedList<JSONObject> objs;

    private StoreAction(JSONArray paramBoolean, boolean paramJSONObject, JSONObject arg4)
      throws JSONException
    {
      this(paramJSONObject, localJSONObject);
      int i = paramBoolean.length();
      for (int j = 0; j < i; j++)
        this.objs.add(paramBoolean.getJSONObject(j));
    }

    private StoreAction(JSONObject paramBoolean, boolean paramJSONObject1, JSONObject arg4)
    {
      this(paramJSONObject1, localJSONObject);
      this.objs.add(paramBoolean);
    }

    private StoreAction(boolean paramJSONObject, JSONObject arg3)
    {
      Object localObject;
      this.addIndexes = localObject;
      this.isAdd = paramJSONObject;
      this.objs = new LinkedList();
    }

    public Integer performAction(DatabaseSchema paramDatabaseSchema, WritableDatabase paramWritableDatabase)
      throws Throwable
    {
      int i = 0;
      Map localMap;
      try
      {
        Iterator localIterator = this.objs.iterator();
        if (localIterator.hasNext())
        {
          JSONObject localJSONObject = (JSONObject)localIterator.next();
          localMap = paramDatabaseSchema.mapObject(localJSONObject, this.addIndexes);
          localMap.put("json", localJSONObject.toString());
          if (this.isAdd)
          {
            localMap.put("_dirty", Long.valueOf(new Date().getTime()));
            localMap.put("_operation", "add");
          }
          while (true)
          {
            if (paramWritableDatabase.insert(localMap) == -1L)
              break label211;
            i++;
            break;
            localMap.put("_dirty", Integer.valueOf(0));
            localMap.put("_operation", "store");
          }
        }
      }
      catch (Throwable localThrowable)
      {
        if (StoreActionDispatcher.this.logger.isLoggable(6))
          StoreActionDispatcher.this.logger.logError("error while storing items in database \"" + paramDatabaseSchema.getName() + "\"", localThrowable);
      }
      return Integer.valueOf(i);
      label211: i = -1;
      Exception localException = new Exception("Error inserting row");
      StoreActionDispatcher.this.logger.logError("Could not insert document", localException);
      StoreActionDispatcher.this.logger.logError("Object was: " + localMap.toString());
      throw localException;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.StoreActionDispatcher
 * JD-Core Version:    0.6.0
 */