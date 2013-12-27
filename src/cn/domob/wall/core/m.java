package cn.domob.wall.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import org.json.JSONArray;
import org.json.JSONObject;

class m
{
  private static p a = new p(m.class.getSimpleName());
  private static m b;

  static m a()
  {
    monitorenter;
    try
    {
      if (b == null)
        b = new m();
      m localm = b;
      return localm;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  private int b(k paramk, AdInfo paramAdInfo)
  {
    h localh = h.a();
    String[] arrayOfString = new String[3];
    arrayOfString[0] = paramk.d();
    arrayOfString[1] = paramAdInfo.h();
    arrayOfString[2] = paramAdInfo.getAdId();
    return localh.a("imp", "ppid=? and sid=? and id=? ", arrayOfString);
  }

  void a(k paramk)
  {
    paramk.h().execute(new Runnable(paramk)
    {
      public void run()
      {
        m.b().b("start to report imp");
        Iterator localIterator = q.a().a(this.a).iterator();
        while (localIterator.hasNext())
        {
          q.a locala = (q.a)localIterator.next();
          r localr = r.a();
          localr.getClass();
          r.f localf = new r.f(localr, this.a);
          localf.a(locala.b.toString());
          localf.b(locala.a);
          localf.a();
        }
      }
    });
  }

  void a(k paramk, AdInfo paramAdInfo)
  {
    if (b(paramk, paramAdInfo) > paramk.e());
  }

  void a(k paramk, AdInfo paramAdInfo, r.i parami)
  {
    r localr = r.a();
    localr.getClass();
    r.b localb = new r.b(localr, paramk, paramAdInfo);
    localb.a(parami);
    localb.a();
  }

  void a(k paramk, AdInfo paramAdInfo, r.j paramj)
  {
    r localr = r.a();
    localr.getClass();
    r.c localc = new r.c(localr, paramk, paramAdInfo);
    localc.a(paramj);
    if (paramj.name().equals(r.j.b.name()))
    {
      paramAdInfo.setAdActualPosition(0);
      localc.b(0);
    }
    while (true)
    {
      localc.a(b(paramk, paramAdInfo));
      localc.a();
      return;
      localc.b(paramAdInfo.a());
    }
  }

  void a(k paramk, AdInfo paramAdInfo, r.l paraml)
  {
    r localr = r.a();
    localr.getClass();
    r.d locald = new r.d(localr, paramk, paramAdInfo);
    locald.a(paraml);
    locald.a();
  }

  void a(k paramk, AdInfo paramAdInfo, r.m paramm, String paramString)
  {
    r localr = r.a();
    localr.getClass();
    r.h localh = new r.h(localr, paramk, paramAdInfo);
    localh.a(paramm);
    localh.a(paramString);
    localh.a();
  }

  void a(k paramk, DService.ReportUserActionType paramReportUserActionType)
  {
    r localr = r.a();
    localr.getClass();
    r.o localo = new r.o(localr, paramk);
    localo.a(paramReportUserActionType);
    localo.a();
  }

  void a(k paramk, ArrayList<AdInfo> paramArrayList)
  {
    paramk.h().execute(new Runnable(paramArrayList, paramk)
    {
      public void run()
      {
        JSONArray localJSONArray;
        try
        {
          localJSONArray = new JSONArray();
          if (this.a != null)
          {
            Iterator localIterator = this.a.iterator();
            while (localIterator.hasNext())
            {
              AdInfo localAdInfo = (AdInfo)localIterator.next();
              JSONObject localJSONObject = new JSONObject();
              localJSONObject.put("id", localAdInfo.getAdId());
              localJSONObject.put("tr", localAdInfo.d());
              localJSONObject.put("sn", "1");
              localJSONArray.put(localJSONObject);
            }
          }
        }
        catch (Exception localException)
        {
          m.b().a(localException);
        }
        return;
        r localr = r.a();
        localr.getClass();
        r.f localf = new r.f(localr, this.b);
        localf.a(localJSONArray.toString());
        localf.b(((AdInfo)this.a.get(0)).h());
        localf.a();
      }
    });
  }

  void b(k paramk, AdInfo paramAdInfo, r.i parami)
  {
    r localr = r.a();
    localr.getClass();
    r.b localb = new r.b(localr, paramk, paramAdInfo);
    localb.a(parami);
    localb.a();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.m
 * JD-Core Version:    0.6.0
 */