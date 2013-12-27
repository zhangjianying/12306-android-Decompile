package cn.domob.wall.core;

import android.content.Context;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

class j
{
  private static p a = new p(j.class.getSimpleName());
  private static final String h = "ad";
  private static final String i = "error";
  private static final String j = "control";
  private String b;
  private String c;
  private a d;
  private ControlInfo e;
  private List<AdInfo> f;
  private List<AdInfo> g;

  private int a(List<AdInfo> paramList)
  {
    Iterator localIterator = paramList.iterator();
    int k = 0;
    if (localIterator.hasNext())
      if (!((AdInfo)localIterator.next()).isNew())
        break label46;
    label46: for (int m = k + 1; ; m = k)
    {
      k = m;
      break;
      return k;
    }
  }

  private ArrayList<AdInfo> a(List<AdInfo> paramList1, List<AdInfo> paramList2)
  {
    ArrayList localArrayList = new ArrayList();
    List localList = b(paramList2);
    int k = 0;
    int m = -1;
    int i2;
    for (int n = 0; k < localList.size(); n = i2)
    {
      int i1 = 0;
      i2 = n;
      while (i1 < -1 + (-1 + ((AdInfo)localList.get(k)).getAdPosition() - m))
      {
        if (i2 < paramList1.size())
          localArrayList.add(paramList1.get(i2));
        i2++;
        i1++;
      }
      localArrayList.add(localList.get(k));
      m = -1 + ((AdInfo)localList.get(k)).getAdPosition();
      k++;
    }
    while (n < paramList1.size())
    {
      localArrayList.add(paramList1.get(n));
      n++;
    }
    return localArrayList;
  }

  private List<AdInfo> a(long paramLong, String paramString1, JSONObject paramJSONObject, String paramString2)
  {
    JSONArray localJSONArray = paramJSONObject.optJSONArray(paramString1);
    ArrayList localArrayList = new ArrayList();
    int k = 0;
    while (true)
      if (k < localJSONArray.length())
        try
        {
          localArrayList.add(new AdInfo(paramLong, localJSONArray.getJSONObject(k), paramString2, paramString1));
          k++;
        }
        catch (JSONException localJSONException)
        {
          while (true)
            a.a(localJSONException);
        }
    return c(localArrayList);
  }

  private List<AdInfo> a(Context paramContext, List<AdInfo> paramList)
  {
    List localList = s.g(paramContext);
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      AdInfo localAdInfo = (AdInfo)localIterator.next();
      if ((!localList.contains(localAdInfo.getAdPackageName())) || (localAdInfo.getAdActionType() == AdInfo.ClickActionType.LAUNCH))
        continue;
      localIterator.remove();
      p localp = a;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = localAdInfo.getAdPackageName();
      localp.d(String.format("This application %s has been installed and ClickActionType is not launch, so removed, and is no longer displayed", arrayOfObject));
    }
    return paramList;
  }

  private List<AdInfo> a(ControlInfo paramControlInfo, List<AdInfo> paramList1, List<AdInfo> paramList2)
  {
    ControlInfo.a locala = paramControlInfo.getAdOrder();
    if (locala.equals(ControlInfo.a.a))
    {
      paramList2.addAll(paramList1);
      return paramList2;
    }
    if (locala.equals(ControlInfo.a.b))
    {
      paramList1.addAll(paramList2);
      return paramList1;
    }
    if (locala.equals(ControlInfo.a.c))
      return a(paramList1, paramList2);
    return null;
  }

  private List<AdInfo> b(List<AdInfo> paramList)
  {
    for (int k = 0; k < -1 + paramList.size(); k++)
      for (int m = k + 1; m < paramList.size(); m++)
      {
        if (((AdInfo)paramList.get(k)).getAdPosition() <= ((AdInfo)paramList.get(m)).getAdPosition())
          continue;
        AdInfo localAdInfo = (AdInfo)paramList.get(k);
        paramList.set(k, (AdInfo)paramList.get(m));
        paramList.set(m, localAdInfo);
      }
    return paramList;
  }

  private boolean b(Context paramContext, String paramString)
  {
    try
    {
      JSONObject localJSONObject1 = new JSONObject(new JSONTokener(paramString));
      this.b = localJSONObject1.optString("sid", "");
      this.c = localJSONObject1.optString("cid", "");
      if ((this.c != null) && (!this.c.equals("")))
        n.a().a(paramContext, this.c);
      JSONObject localJSONObject2 = localJSONObject1.optJSONObject("ad");
      JSONObject localJSONObject3 = localJSONObject1.optJSONObject("error");
      JSONObject localJSONObject4 = localJSONObject1.optJSONObject("control");
      if (localJSONObject3 != null)
      {
        this.d = new a(localJSONObject3);
        break label298;
      }
      if (localJSONObject4 != null)
        this.e = new ControlInfo(localJSONObject4);
      if (localJSONObject2 != null)
      {
        long l = n.a().a(paramContext);
        a.e("上次请求时间：" + l);
        this.g = a(l, "wheel", localJSONObject2, this.b);
        List localList1 = a(paramContext, a(l, v.a(), localJSONObject2, this.b));
        List localList2 = a(paramContext, a(l, "own", localJSONObject2, this.b));
        this.f = a(this.e, localList1, localList2);
        this.e.a(a(this.f));
        n.a().b(paramContext);
      }
    }
    catch (Exception localException)
    {
      a.a(localException);
      return false;
    }
    a.e("There is no ad response or error response.");
    label298: return true;
  }

  private List<AdInfo> c(List<AdInfo> paramList)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      AdInfo localAdInfo = (AdInfo)localIterator.next();
      if (!localAdInfo.isNew())
        continue;
      localArrayList.add(localAdInfo);
      localIterator.remove();
    }
    localArrayList.addAll(paramList);
    return localArrayList;
  }

  j a(Context paramContext, String paramString)
  {
    j localj = new j();
    if (localj.b(paramContext, paramString))
    {
      a.a("Ad/Error response is ok.");
      return localj;
    }
    return null;
  }

  String a()
  {
    return this.b;
  }

  a b()
  {
    return this.d;
  }

  ControlInfo c()
  {
    return this.e;
  }

  List<AdInfo> d()
  {
    return this.f;
  }

  List<AdInfo> e()
  {
    return this.g;
  }

  class a
  {
    private int b;
    private String c;

    a(JSONObject arg2)
    {
      Object localObject;
      this.b = localObject.optInt("code", 0);
      this.c = localObject.optString("text", null);
    }

    int a()
    {
      return this.b;
    }

    String b()
    {
      return this.c;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.j
 * JD-Core Version:    0.6.0
 */