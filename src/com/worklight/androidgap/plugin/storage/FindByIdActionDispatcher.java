package com.worklight.androidgap.plugin.storage;

import android.database.Cursor;
import com.worklight.androidgap.jsonstore.database.DatabaseSchema;
import com.worklight.androidgap.jsonstore.database.ReadableDatabase;
import com.worklight.androidgap.jsonstore.util.JsonstoreUtil;
import com.worklight.androidgap.jsonstore.util.Logger;
import com.worklight.androidgap.jsonstore.util.jackson.JacksonSerializedJSONArray;
import com.worklight.androidgap.jsonstore.util.jackson.JacksonSerializedJSONObject;
import com.worklight.androidgap.jsonstore.util.jackson.JacksonSerializedPluginResult;
import com.worklight.androidgap.jsonstore.util.jackson.JsonOrgModule;
import java.util.Iterator;
import java.util.List;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONObject;

public class FindByIdActionDispatcher extends DatabaseActionDispatcher
{
  private static final String PARAM_IDS = "ids";

  protected FindByIdActionDispatcher()
  {
    super("findById");
    BaseActionDispatcher.ParameterType[] arrayOfParameterType = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType[0] = BaseActionDispatcher.ParameterType.ARRAY;
    addParameter("ids", true, arrayOfParameterType);
  }

  private JSONArray getIds(DatabaseActionDispatcher.Context paramContext)
  {
    return paramContext.getArrayParameter("ids");
  }

  public PluginResult dispatch(DatabaseActionDispatcher.Context paramContext)
    throws Throwable
  {
    JSONArray localJSONArray1 = getIds(paramContext);
    try
    {
      JSONArray localJSONArray2 = (JSONArray)paramContext.performReadableDatabaseAction(new FindByIdAction(localJSONArray1, null));
      if (localJSONArray2 == null)
        return new PluginResult(PluginResult.Status.ERROR, 22);
      JacksonSerializedPluginResult localJacksonSerializedPluginResult = new JacksonSerializedPluginResult(PluginResult.Status.OK, localJSONArray2);
      return localJacksonSerializedPluginResult;
    }
    catch (Throwable localThrowable)
    {
      if (this.logger.isLoggable(6))
        this.logger.logError("error while executing find by ID query on database \"" + paramContext.getDatabaseName() + "\"", localThrowable);
    }
    return new PluginResult(PluginResult.Status.ERROR, 22);
  }

  private class FindByIdAction
    implements DatabaseActionDispatcher.ReadableDatabaseAction<JSONArray>
  {
    private JSONArray ids;

    private FindByIdAction(JSONArray arg2)
    {
      Object localObject;
      this.ids = localObject;
    }

    public JSONArray performAction(DatabaseSchema paramDatabaseSchema, ReadableDatabase paramReadableDatabase)
      throws Throwable
    {
      int[] arrayOfInt = new int[this.ids.length()];
      JacksonSerializedJSONArray localJacksonSerializedJSONArray = new JacksonSerializedJSONArray();
      for (int i = 0; i < arrayOfInt.length; i++)
        arrayOfInt[i] = this.ids.getInt(i);
      Iterator localIterator = JsonstoreUtil.splitArray(arrayOfInt).iterator();
      while (true)
      {
        Cursor localCursor;
        int j;
        if (localIterator.hasNext())
        {
          localCursor = paramReadableDatabase.findByIds((int[])localIterator.next());
          j = localCursor.getCount();
          if (j < 0)
          {
            localCursor.close();
            localJacksonSerializedJSONArray = null;
          }
        }
        else
        {
          return localJacksonSerializedJSONArray;
        }
        for (int k = 0; k < j; k++)
        {
          JacksonSerializedJSONObject localJacksonSerializedJSONObject = new JacksonSerializedJSONObject();
          localCursor.moveToNext();
          localJacksonSerializedJSONObject.put("_id", localCursor.getInt(0));
          localJacksonSerializedJSONObject.put("json", JsonOrgModule.deserializeJSONObject(localCursor.getString(1)));
          localJacksonSerializedJSONArray.put(localJacksonSerializedJSONObject);
        }
        localCursor.close();
      }
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.FindByIdActionDispatcher
 * JD-Core Version:    0.6.0
 */