package com.worklight.androidgap.plugin.storage;

import android.database.Cursor;
import com.worklight.androidgap.jsonstore.database.DatabaseSchema;
import com.worklight.androidgap.jsonstore.database.ReadableDatabase;
import com.worklight.androidgap.jsonstore.util.JsonstoreUtil;
import com.worklight.androidgap.jsonstore.util.jackson.JacksonSerializedJSONArray;
import com.worklight.androidgap.jsonstore.util.jackson.JacksonSerializedJSONObject;
import com.worklight.androidgap.jsonstore.util.jackson.JacksonSerializedPluginResult;
import com.worklight.androidgap.jsonstore.util.jackson.JsonOrgModule;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONObject;

public class AllDirtyActionDispatcher extends DatabaseActionDispatcher
{
  private static final String PARAM_SELECTED_DOCS = "selectedDocs";
  private static final String SQL_ALL_DIRTY = "SELECT {0}, {1}, {2}, {3} FROM {4} WHERE {3} > 0 ORDER BY {3}";

  protected AllDirtyActionDispatcher()
  {
    super("allDirty");
    BaseActionDispatcher.ParameterType[] arrayOfParameterType = new BaseActionDispatcher.ParameterType[2];
    arrayOfParameterType[0] = BaseActionDispatcher.ParameterType.ARRAY;
    arrayOfParameterType[1] = BaseActionDispatcher.ParameterType.OBJECT;
    addParameter("selectedDocs", false, arrayOfParameterType);
  }

  private Object getSelectedDocs(DatabaseActionDispatcher.Context paramContext)
  {
    return paramContext.getUntypedParameter("selectedDocs");
  }

  public PluginResult dispatch(DatabaseActionDispatcher.Context paramContext)
    throws Throwable
  {
    HashSet localHashSet = new HashSet();
    Object localObject = getSelectedDocs(paramContext);
    JacksonSerializedJSONArray localJacksonSerializedJSONArray = new JacksonSerializedJSONArray();
    List localList = (List)paramContext.performReadableDatabaseAction(new AllDirtyAction(paramContext, null));
    if (localObject != null)
    {
      if (!(localObject instanceof JSONObject))
        break label115;
      localHashSet.add(Integer.valueOf(((JSONObject)localObject).getInt("_id")));
    }
    while (localHashSet.size() == 0)
    {
      Iterator localIterator2 = localList.iterator();
      while (localIterator2.hasNext())
        localJacksonSerializedJSONArray.put((JSONObject)localIterator2.next());
      label115: JSONArray localJSONArray = (JSONArray)localObject;
      int i = localJSONArray.length();
      for (int j = 0; j < i; j++)
        localHashSet.add(Integer.valueOf(localJSONArray.getJSONObject(j).getInt("_id")));
    }
    Iterator localIterator1 = localList.iterator();
    while (localIterator1.hasNext())
    {
      JSONObject localJSONObject = (JSONObject)localIterator1.next();
      if (!localHashSet.contains(Integer.valueOf(localJSONObject.getInt("_id"))))
        continue;
      localJacksonSerializedJSONArray.put(localJSONObject);
    }
    return new JacksonSerializedPluginResult(PluginResult.Status.OK, localJacksonSerializedJSONArray);
  }

  private class AllDirtyAction
    implements DatabaseActionDispatcher.ReadableDatabaseAction<List<JSONObject>>
  {
    private DatabaseActionDispatcher.Context context;

    private AllDirtyAction(DatabaseActionDispatcher.Context arg2)
    {
      Object localObject;
      this.context = localObject;
    }

    public List<JSONObject> performAction(DatabaseSchema paramDatabaseSchema, ReadableDatabase paramReadableDatabase)
      throws Throwable
    {
      LinkedList localLinkedList = new LinkedList();
      Object[] arrayOfObject = new Object[5];
      arrayOfObject[0] = "_id";
      arrayOfObject[1] = "json";
      arrayOfObject[2] = "_operation";
      arrayOfObject[3] = "_dirty";
      arrayOfObject[4] = this.context.getDatabaseName();
      Cursor localCursor = paramReadableDatabase.rawQuery(JsonstoreUtil.formatString("SELECT {0}, {1}, {2}, {3} FROM {4} WHERE {3} > 0 ORDER BY {3}", arrayOfObject), null);
      int i = localCursor.getCount();
      for (int j = 0; j < i; j++)
      {
        JacksonSerializedJSONObject localJacksonSerializedJSONObject = new JacksonSerializedJSONObject();
        localCursor.moveToNext();
        localJacksonSerializedJSONObject.put("_id", localCursor.getInt(0));
        localJacksonSerializedJSONObject.put("json", JsonOrgModule.deserializeJSONObject(localCursor.getString(1)));
        localJacksonSerializedJSONObject.put("_operation", localCursor.getString(2));
        localJacksonSerializedJSONObject.put("_dirty", localCursor.getLong(3));
        localLinkedList.add(localJacksonSerializedJSONObject);
      }
      localCursor.close();
      return localLinkedList;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.AllDirtyActionDispatcher
 * JD-Core Version:    0.6.0
 */