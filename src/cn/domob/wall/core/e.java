package cn.domob.wall.core;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

class e
{
  private static p c = new p(e.class.getSimpleName());
  private final String a = "turnoff";
  private JSONArray b = new JSONArray();

  public e(String paramString)
  {
    try
    {
      this.b = new JSONObject(new JSONTokener(paramString)).optJSONArray("turnoff");
      return;
    }
    catch (JSONException localJSONException)
    {
      c.e("Config resp is not in JSONObject");
      return;
    }
    catch (Exception localException)
    {
      c.a(localException);
    }
  }

  private ArrayList<String> a(JSONArray paramJSONArray)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    while (true)
      if (i < paramJSONArray.length())
        try
        {
          localArrayList.add(paramJSONArray.getString(i));
          i++;
        }
        catch (JSONException localJSONException)
        {
          while (true)
            c.a(localJSONException);
        }
    return localArrayList;
  }

  public ArrayList<String> a()
  {
    return a(this.b);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.e
 * JD-Core Version:    0.6.0
 */