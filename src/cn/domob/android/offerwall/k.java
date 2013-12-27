package cn.domob.android.offerwall;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

class k
{
  private static m a = new m(k.class.getSimpleName());
  private static final String h = "status";
  private static final String i = "refresh_interval";
  private static final String j = "results";
  private static final String k = "id";
  private static final String l = "rp_url";
  private static final String m = "name";
  private static final String n = "pkg";
  private static final String o = "file_url";
  private static final String p = "config";
  private static final String q = "autorun";
  private static final String r = "autodownload";
  private String b;
  private String c;
  private int d;
  private String e;
  private HashMap<Integer, a> f;
  private b g;

  protected static k a(String paramString)
  {
    k localk = new k();
    if (localk.b(paramString))
    {
      a.a("OfferWall response is ok.");
      return localk;
    }
    return null;
  }

  private boolean b(String paramString)
  {
    while (true)
    {
      int i2;
      try
      {
        this.b = paramString;
        JSONObject localJSONObject1 = new JSONObject(new JSONTokener(paramString));
        this.c = localJSONObject1.optString("status", null);
        this.d = localJSONObject1.optInt("refresh_interval", 3600);
        this.e = localJSONObject1.optString("results", null);
        JSONObject localJSONObject2 = localJSONObject1.optJSONObject("config");
        JSONArray localJSONArray = localJSONObject1.optJSONArray("results");
        if (localJSONArray == null)
          continue;
        this.f = new HashMap();
        int i1 = localJSONArray.length();
        i2 = 0;
        if (i2 >= i1)
          continue;
        a locala = new a(localJSONArray.getJSONObject(i2));
        if (locala != null)
        {
          this.f.put(Integer.valueOf(locala.a()), locala);
          break label179;
          if (localJSONObject2 == null)
            continue;
          this.g = new b(localJSONObject2);
          return true;
        }
      }
      catch (Exception localException)
      {
        a.a(localException);
        return false;
      }
      label179: i2++;
    }
  }

  protected String a()
  {
    return this.b;
  }

  protected String b()
  {
    return this.c;
  }

  protected int c()
  {
    return this.d;
  }

  protected String d()
  {
    return this.e;
  }

  protected HashMap<Integer, a> e()
  {
    return this.f;
  }

  protected b f()
  {
    return this.g;
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DomobOfferResponse [mOriginalRespStr=").append(this.b).append(", mStatus=").append(this.c).append(", mRefreshInterval=").append(this.d).append(", mResults=").append(this.e).append(", mConfig=").append(this.g).append("]");
    return localStringBuilder.toString();
  }

  class a
  {
    private int b;
    private String c;
    private String d;
    private String e;
    private String f;

    a(JSONObject arg2)
    {
      Object localObject;
      this.b = localObject.optInt("id");
      this.c = localObject.optString("rp_url", null);
      this.d = localObject.optString("name", null);
      this.e = localObject.optString("pkg", null);
      this.f = localObject.optString("file_url", null);
    }

    protected int a()
    {
      return this.b;
    }

    protected String b()
    {
      return this.c;
    }

    protected String c()
    {
      return this.d;
    }

    protected String d()
    {
      return this.e;
    }

    protected String e()
    {
      return this.f;
    }
  }

  class b
  {
    private boolean b;
    private boolean c;

    b(JSONObject arg2)
    {
      Object localObject;
      this.b = localObject.optBoolean("autorun", false);
      this.c = localObject.optBoolean("autodownload", false);
    }

    protected boolean a()
    {
      return this.b;
    }

    protected boolean b()
    {
      return this.c;
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("OfferConfig [mAutorun=").append(this.b).append(", mAutoDownload=").append(this.c).append("]");
      return localStringBuilder.toString();
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.android.offerwall.k
 * JD-Core Version:    0.6.0
 */