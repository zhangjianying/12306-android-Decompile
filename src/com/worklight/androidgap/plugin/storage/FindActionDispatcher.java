package com.worklight.androidgap.plugin.storage;

import android.database.Cursor;
import com.worklight.androidgap.jsonstore.database.DatabaseSchema;
import com.worklight.androidgap.jsonstore.database.ReadableDatabase;
import com.worklight.androidgap.jsonstore.util.Logger;
import com.worklight.androidgap.jsonstore.util.jackson.JacksonSerializedJSONArray;
import com.worklight.androidgap.jsonstore.util.jackson.JacksonSerializedJSONObject;
import com.worklight.androidgap.jsonstore.util.jackson.JacksonSerializedPluginResult;
import com.worklight.androidgap.jsonstore.util.jackson.JsonOrgModule;
import java.util.LinkedHashMap;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONObject;

public class FindActionDispatcher extends DatabaseActionDispatcher
{
  private static final String OPTION_EXACT = "exact";
  private static final String OPTION_LIMIT = "limit";
  private static final String OPTION_OFFSET = "offset";
  private static final String PARAM_OPTIONS = "options";
  private static final String PARAM_QUERY_OBJ = "queryObj";

  protected FindActionDispatcher()
  {
    super("find");
    BaseActionDispatcher.ParameterType[] arrayOfParameterType1 = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType1[0] = BaseActionDispatcher.ParameterType.ARRAY;
    addParameter("queryObj", true, arrayOfParameterType1);
    BaseActionDispatcher.ParameterType[] arrayOfParameterType2 = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType2[0] = BaseActionDispatcher.ParameterType.OBJECT;
    addParameter("options", false, true, arrayOfParameterType2);
  }

  private JSONObject getOptions(DatabaseActionDispatcher.Context paramContext)
  {
    return paramContext.getObjectParameter("options");
  }

  private JSONArray getQueryObj(DatabaseActionDispatcher.Context paramContext)
  {
    return paramContext.getArrayParameter("queryObj");
  }

  public PluginResult dispatch(DatabaseActionDispatcher.Context paramContext)
    throws Throwable
  {
    while (true)
    {
      int i;
      JSONArray localJSONArray2;
      int j;
      try
      {
        LinkedHashMap localLinkedHashMap = new LinkedHashMap();
        JSONArray localJSONArray1 = getQueryObj(paramContext);
        i = 0;
        if (i >= localJSONArray1.length())
          continue;
        FindAction localFindAction = new FindAction(localJSONArray1.getJSONObject(i), null);
        JSONObject localJSONObject1 = getOptions(paramContext);
        localFindAction.setExact(Boolean.valueOf(localJSONObject1.optBoolean("exact")));
        if (localJSONObject1 == null)
          continue;
        String str2 = localJSONObject1.optString("limit", null);
        if (str2 == null)
          continue;
        localFindAction.setLimit(str2);
        String str3 = localJSONObject1.optString("offset", null);
        if (str3 == null)
          continue;
        localFindAction.setOffset(str3);
        localJSONArray2 = (JSONArray)paramContext.performReadableDatabaseAction(localFindAction);
        if (this.logger.isLoggable(3))
        {
          this.logger.logDebug("find query result:");
          this.logger.logDebug("   " + localJSONArray2);
          break label321;
          if (j >= localJSONArray2.length())
            break label338;
          JSONObject localJSONObject2 = localJSONArray2.getJSONObject(j);
          Integer localInteger = Integer.valueOf(localJSONObject2.getInt("_id"));
          if (localLinkedHashMap.containsKey(localInteger))
            break label332;
          localLinkedHashMap.put(localInteger, localJSONObject2);
          break label332;
          JacksonSerializedPluginResult localJacksonSerializedPluginResult = new JacksonSerializedPluginResult(PluginResult.Status.OK, new JSONArray(localLinkedHashMap.values()));
          return localJacksonSerializedPluginResult;
        }
      }
      catch (Throwable localThrowable)
      {
        String str1 = "error while executing find query on database \"" + paramContext.getDatabaseName() + "\"";
        if (!this.logger.isLoggable(6))
          continue;
        this.logger.logError(str1, localThrowable);
        return new PluginResult(PluginResult.Status.ERROR, 22);
      }
      label321: if (localJSONArray2 != null)
      {
        j = 0;
        continue;
        label332: j++;
        continue;
      }
      label338: i++;
    }
  }

  private class FindAction
    implements DatabaseActionDispatcher.ReadableDatabaseAction<JSONArray>
  {
    private Boolean exact = Boolean.valueOf(false);
    private String limit = null;
    private String offset = null;
    private JSONObject queryObj;

    private FindAction(JSONObject arg2)
    {
      Object localObject;
      this.queryObj = localObject;
    }

    private String getLimit()
    {
      return this.limit;
    }

    private String getOffset()
    {
      return this.offset;
    }

    private void setLimit(String paramString)
    {
      this.limit = String.valueOf(paramString);
    }

    private void setOffset(String paramString)
    {
      this.offset = String.valueOf(paramString);
    }

    public Boolean getExact()
    {
      return this.exact;
    }

    public JSONArray performAction(DatabaseSchema paramDatabaseSchema, ReadableDatabase paramReadableDatabase)
      throws Throwable
    {
      JacksonSerializedJSONArray localJacksonSerializedJSONArray = new JacksonSerializedJSONArray();
      Cursor localCursor = paramReadableDatabase.findUsingQueryObject(this.queryObj, new String[] { "_id", "json" }, new String[] { "_deleted = 0" }, getLimit(), getOffset(), getExact());
      int i = localCursor.getCount();
      for (int j = 0; j < i; j++)
      {
        JacksonSerializedJSONObject localJacksonSerializedJSONObject = new JacksonSerializedJSONObject();
        localCursor.moveToNext();
        localJacksonSerializedJSONObject.put("_id", localCursor.getInt(0));
        localJacksonSerializedJSONObject.put("json", JsonOrgModule.deserializeJSONObject(localCursor.getString(1)));
        localJacksonSerializedJSONArray.put(localJacksonSerializedJSONObject);
      }
      localCursor.close();
      return localJacksonSerializedJSONArray;
    }

    public void setExact(Boolean paramBoolean)
    {
      this.exact = paramBoolean;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.FindActionDispatcher
 * JD-Core Version:    0.6.0
 */