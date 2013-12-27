package org.apache.cordova;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

public class JSONUtils
{
  public static List<String> toStringList(JSONArray paramJSONArray)
    throws JSONException
  {
    ArrayList localArrayList;
    if (paramJSONArray == null)
      localArrayList = null;
    while (true)
    {
      return localArrayList;
      localArrayList = new ArrayList();
      for (int i = 0; i < paramJSONArray.length(); i++)
        localArrayList.add(paramJSONArray.get(i).toString());
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.JSONUtils
 * JD-Core Version:    0.6.0
 */