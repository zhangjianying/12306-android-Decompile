package cn.domob.android.offerwall;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

public class i
{
  static final String a = "http://duomeng.cn/api/12306/rp";
  private static m b = new m(i.class.getSimpleName());
  private static final String c = "ua";
  private static final String d = "ipb";
  private static final String e = "jstr";
  private static final String f = "type";
  private static final String g = "id";
  private static final String h = "idtype";
  private static final String i = "ts";

  static void a(Context paramContext, a parama)
  {
    try
    {
      String str = b(paramContext, parama);
      b.b("Report params: " + str);
      if (l.e(str))
      {
        b.e("Report params is null or empty.");
        return;
      }
      new e(paramContext, parama.c, str, new e.a(parama)
      {
        public void a(e parame)
        {
          if ((parame != null) && (parame.f() == 200))
          {
            i.a().b("Report finish: " + this.a.a);
            return;
          }
          i.a().e("Report failed: " + this.a.a);
        }
      }).b();
      return;
    }
    catch (Exception localException)
    {
      b.e("Error happened while reporting.");
      b.a(localException);
    }
  }

  private static String b(Context paramContext, a parama)
  {
    String str1;
    String str2;
    JSONArray localJSONArray1;
    JSONObject localJSONObject;
    JSONArray localJSONArray2;
    try
    {
      str1 = f.e(paramContext);
      str2 = DomobOfferWallSetting.a();
      localJSONArray1 = new JSONArray();
      localJSONObject = new JSONObject();
      localJSONObject.put("type", parama.a);
      localJSONArray2 = new JSONArray();
      Iterator localIterator = parama.b.iterator();
      while (localIterator.hasNext())
        localJSONArray2.put((String)localIterator.next());
    }
    catch (Exception localException)
    {
      b.e("Error happened while building report params.");
      b.a(localException);
      return null;
    }
    localJSONObject.put("id", localJSONArray2);
    String str3 = "app";
    if (parama.a == "installed")
      str3 = "pkg";
    localJSONObject.put("idtype", str3);
    localJSONObject.put("ts", String.valueOf(System.currentTimeMillis()));
    localJSONArray1.put(localJSONObject);
    String str4 = localJSONArray1.toString();
    HashMap localHashMap = new HashMap();
    localHashMap.put("ua", str1);
    localHashMap.put("ipb", str2);
    localHashMap.put("jstr", str4);
    String str5 = l.a(localHashMap);
    return str5;
  }

  static class a
  {
    String a;
    ArrayList<String> b = new ArrayList();
    String c;
  }

  class b
  {
    static final String a = "show_list";
    static final String b = "show_detail";
    static final String c = "download";
    static final String d = "download_finish";
    static final String e = "close_list";
    static final String f = "installed";
    static final String g = "run";

    b()
    {
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.android.offerwall.i
 * JD-Core Version:    0.6.0
 */