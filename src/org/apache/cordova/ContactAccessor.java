package org.apache.cordova;

import android.util.Log;
import android.webkit.WebView;
import java.util.HashMap;
import org.apache.cordova.api.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class ContactAccessor
{
  protected final String LOG_TAG = "ContactsAccessor";
  protected CordovaInterface mApp;
  protected WebView mView;

  protected HashMap<String, Boolean> buildPopulationSet(JSONArray paramJSONArray)
  {
    HashMap localHashMap = new HashMap();
    while (true)
    {
      int i;
      String str;
      try
      {
        if ((paramJSONArray.length() != 1) || (!paramJSONArray.getString(0).equals("*")))
          break label530;
        localHashMap.put("displayName", Boolean.valueOf(true));
        localHashMap.put("name", Boolean.valueOf(true));
        localHashMap.put("nickname", Boolean.valueOf(true));
        localHashMap.put("phoneNumbers", Boolean.valueOf(true));
        localHashMap.put("emails", Boolean.valueOf(true));
        localHashMap.put("addresses", Boolean.valueOf(true));
        localHashMap.put("ims", Boolean.valueOf(true));
        localHashMap.put("organizations", Boolean.valueOf(true));
        localHashMap.put("birthday", Boolean.valueOf(true));
        localHashMap.put("note", Boolean.valueOf(true));
        localHashMap.put("urls", Boolean.valueOf(true));
        localHashMap.put("photos", Boolean.valueOf(true));
        localHashMap.put("categories", Boolean.valueOf(true));
        return localHashMap;
        if (i >= paramJSONArray.length())
          break label528;
        str = paramJSONArray.getString(i);
        if (!str.startsWith("displayName"))
          continue;
        localHashMap.put("displayName", Boolean.valueOf(true));
        break label536;
        if (str.startsWith("name"))
        {
          localHashMap.put("displayName", Boolean.valueOf(true));
          localHashMap.put("name", Boolean.valueOf(true));
        }
      }
      catch (JSONException localJSONException)
      {
        Log.e("ContactsAccessor", localJSONException.getMessage(), localJSONException);
        return localHashMap;
      }
      if (str.startsWith("nickname"))
      {
        localHashMap.put("nickname", Boolean.valueOf(true));
      }
      else if (str.startsWith("phoneNumbers"))
      {
        localHashMap.put("phoneNumbers", Boolean.valueOf(true));
      }
      else if (str.startsWith("emails"))
      {
        localHashMap.put("emails", Boolean.valueOf(true));
      }
      else if (str.startsWith("addresses"))
      {
        localHashMap.put("addresses", Boolean.valueOf(true));
      }
      else if (str.startsWith("ims"))
      {
        localHashMap.put("ims", Boolean.valueOf(true));
      }
      else if (str.startsWith("organizations"))
      {
        localHashMap.put("organizations", Boolean.valueOf(true));
      }
      else if (str.startsWith("birthday"))
      {
        localHashMap.put("birthday", Boolean.valueOf(true));
      }
      else if (str.startsWith("note"))
      {
        localHashMap.put("note", Boolean.valueOf(true));
      }
      else if (str.startsWith("urls"))
      {
        localHashMap.put("urls", Boolean.valueOf(true));
      }
      else if (str.startsWith("photos"))
      {
        localHashMap.put("photos", Boolean.valueOf(true));
      }
      else if (str.startsWith("categories"))
      {
        localHashMap.put("categories", Boolean.valueOf(true));
        break label536;
        label528: return localHashMap;
        label530: i = 0;
        continue;
      }
      label536: i++;
    }
  }

  public abstract JSONObject getContactById(String paramString)
    throws JSONException;

  protected String getJsonString(JSONObject paramJSONObject, String paramString)
  {
    String str = null;
    if (paramJSONObject != null);
    try
    {
      str = paramJSONObject.getString(paramString);
      if (str.equals("null"))
      {
        Log.d("ContactsAccessor", paramString + " is string called 'null'");
        str = null;
      }
      return str;
    }
    catch (JSONException localJSONException)
    {
      Log.d("ContactsAccessor", "Could not get = " + localJSONException.getMessage());
    }
    return str;
  }

  protected boolean isRequired(String paramString, HashMap<String, Boolean> paramHashMap)
  {
    Boolean localBoolean = (Boolean)paramHashMap.get(paramString);
    if (localBoolean == null)
      return false;
    return localBoolean.booleanValue();
  }

  public abstract boolean remove(String paramString);

  public abstract String save(JSONObject paramJSONObject);

  public abstract JSONArray search(JSONArray paramJSONArray, JSONObject paramJSONObject);

  class WhereOptions
  {
    private String where;
    private String[] whereArgs;

    WhereOptions()
    {
    }

    public String getWhere()
    {
      return this.where;
    }

    public String[] getWhereArgs()
    {
      return this.whereArgs;
    }

    public void setWhere(String paramString)
    {
      this.where = paramString;
    }

    public void setWhereArgs(String[] paramArrayOfString)
    {
      this.whereArgs = paramArrayOfString;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.ContactAccessor
 * JD-Core Version:    0.6.0
 */