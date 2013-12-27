package cn.domob.wall.core;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdInfo
{
  protected static final int a = -1;
  private static p d = new p(AdInfo.class.getSimpleName());
  private List<String> A = new ArrayList();
  int b;
  String c;
  private int e;
  private int f;
  private int g = -1;
  private int h;
  private int i;
  private String j;
  private String k;
  private String l;
  private String m;
  private String n;
  private String o;
  private String p;
  private String q;
  private String r;
  private String s;
  private String t;
  private String u;
  private String v;
  private String w;
  private String x = "false";
  private String y = "true";
  private long z;

  public AdInfo(long paramLong, JSONObject paramJSONObject, String paramString1, String paramString2)
  {
    if (paramJSONObject != null)
    {
      this.j = paramString1;
      this.k = paramJSONObject.optString("id");
      this.e = paramJSONObject.optInt("type", 0);
      this.f = paramJSONObject.optInt("pos");
      this.l = paramJSONObject.optString("logo");
      this.m = paramJSONObject.optString("image");
      this.n = paramJSONObject.optString("name");
      this.o = paramJSONObject.optString("title");
      this.p = paramJSONObject.optString("texts");
      this.q = paramJSONObject.optString("provider");
      this.r = paramJSONObject.optString("desc");
      this.w = paramString2;
      try
      {
        JSONArray localJSONArray1 = new JSONArray(paramJSONObject.getString("screenshot"));
        localJSONArray2 = localJSONArray1;
        if (localJSONArray2 != null)
        {
          i1 = 0;
          if (i1 >= localJSONArray2.length())
            break label249;
        }
      }
      catch (JSONException localJSONException2)
      {
        try
        {
          while (true)
          {
            int i1;
            this.A.add(localJSONArray2.getString(i1));
            i1++;
          }
          localJSONException2 = localJSONException2;
          d.b(localJSONException2.toString());
          JSONArray localJSONArray2 = null;
        }
        catch (JSONException localJSONException1)
        {
          while (true)
            d.a(localJSONException1);
        }
        d.d("Screenshot of the page with details of the ad is empty");
        label249: this.s = paramJSONObject.optString("identifier");
        this.i = paramJSONObject.optInt("vc");
        this.t = paramJSONObject.optString("vn");
        this.c = paramJSONObject.optString("action_url");
        this.u = paramJSONObject.optString("click_tracker");
        this.h = paramJSONObject.optInt("size");
        this.z = paramJSONObject.optLong("release_time");
        d.b("上次请求时间：" + paramLong + "广告上线时间：" + this.z);
        if (paramLong != 0L);
      }
    }
    boolean bool2;
    for (this.x = Boolean.toString(false); ; this.x = Boolean.toString(bool2))
    {
      this.b = paramJSONObject.optInt("action_type");
      this.v = paramJSONObject.optString("tr");
      this.y = Boolean.toString(paramJSONObject.optBoolean("showd", Boolean.valueOf(this.y).booleanValue()));
      return;
      boolean bool1 = this.z < paramLong;
      bool2 = false;
      if (!bool1)
        continue;
      bool2 = true;
    }
  }

  protected int a()
  {
    return this.g;
  }

  protected boolean b()
  {
    return this.g != -1;
  }

  protected long c()
  {
    return this.z;
  }

  protected String d()
  {
    return this.v;
  }

  protected String e()
  {
    return this.c;
  }

  protected String f()
  {
    return this.u;
  }

  protected boolean g()
  {
    return Boolean.valueOf(this.y).booleanValue();
  }

  public ClickActionType getAdActionType()
  {
    if (this.b == 1)
      return ClickActionType.DOWNLOAD;
    if (this.b == 2)
      return ClickActionType.INTERNAL_BROWSER;
    if (this.b == 3)
      return ClickActionType.EXTERNAL_BROWSER;
    if (this.b == 4)
      return ClickActionType.LAUNCH;
    return ClickActionType.NONE;
  }

  public String getAdBannerImageURL()
  {
    return this.m;
  }

  public String getAdBriefText()
  {
    return this.p;
  }

  public String getAdDetailDescription()
  {
    return this.r;
  }

  public String getAdId()
  {
    return this.k;
  }

  public String getAdLogoURL()
  {
    return this.l;
  }

  public String getAdName()
  {
    return this.n;
  }

  public String getAdPackageName()
  {
    return this.s;
  }

  public int getAdPosition()
  {
    return this.f;
  }

  public String getAdProvider()
  {
    return this.q;
  }

  public int getAdSize()
  {
    return this.h;
  }

  public String getAdTitle()
  {
    return this.o;
  }

  public AdType getAdType()
  {
    if (this.e == 1)
      return AdType.GAME;
    if (this.e == 2)
      return AdType.APPLICATION;
    return AdType.NONE;
  }

  public int getAdVersionCode()
  {
    return this.i;
  }

  public String getAdVersionName()
  {
    return this.t;
  }

  public List<String> getScreenshot()
  {
    return this.A;
  }

  protected String h()
  {
    return this.j;
  }

  protected AdStyle i()
  {
    if (this.w != null)
    {
      if (this.w.equals(v.a()))
        return AdStyle.XX;
      if (this.w.equals("own"))
        return AdStyle.HOUSE_AD;
      if (this.w.equals("wheel"))
        return AdStyle.BANNER;
    }
    return AdStyle.NONE;
  }

  public boolean isNew()
  {
    return Boolean.valueOf(this.x).booleanValue();
  }

  public void setAdActualPosition(int paramInt)
  {
    this.g = (paramInt + 1);
  }

  public String toString()
  {
    return "AdInfo [mSearchId=" + this.j + ", mId=" + this.k + ", mType=" + this.e + ", mPosition=" + this.f + ", mActualPosition=" + this.g + ", mLogoURL=" + this.l + ", mName=" + this.n + ", mText=" + this.p + ", mProvider=" + this.q + ", mDescription=" + this.r + ", mScreenshot=" + this.A + ", mPackageName=" + this.s + ", mVersionCode=" + this.i + ", mVersionName=" + this.t + ", mActionURL=" + this.c + ", mSize=" + this.h + ", mReleaseTime=" + this.z + ", isNew=" + this.x + ", mActionType=" + this.b + ", mTracker=" + this.v + ", mStyle=" + this.w + ", isShowDetail=" + this.y + "]";
  }

  public static enum AdStyle
  {
    static
    {
      BANNER = new AdStyle("BANNER", 1);
      HOUSE_AD = new AdStyle("HOUSE_AD", 2);
      XX = new AdStyle("XX", 3);
      AdStyle[] arrayOfAdStyle = new AdStyle[4];
      arrayOfAdStyle[0] = NONE;
      arrayOfAdStyle[1] = BANNER;
      arrayOfAdStyle[2] = HOUSE_AD;
      arrayOfAdStyle[3] = XX;
      a = arrayOfAdStyle;
    }
  }

  public static enum AdType
  {
    static
    {
      GAME = new AdType("GAME", 1);
      APPLICATION = new AdType("APPLICATION", 2);
      AdType[] arrayOfAdType = new AdType[3];
      arrayOfAdType[0] = NONE;
      arrayOfAdType[1] = GAME;
      arrayOfAdType[2] = APPLICATION;
      a = arrayOfAdType;
    }
  }

  public static enum ClickActionType
  {
    static
    {
      DOWNLOAD = new ClickActionType("DOWNLOAD", 1);
      INTERNAL_BROWSER = new ClickActionType("INTERNAL_BROWSER", 2);
      EXTERNAL_BROWSER = new ClickActionType("EXTERNAL_BROWSER", 3);
      LAUNCH = new ClickActionType("LAUNCH", 4);
      ClickActionType[] arrayOfClickActionType = new ClickActionType[5];
      arrayOfClickActionType[0] = NONE;
      arrayOfClickActionType[1] = DOWNLOAD;
      arrayOfClickActionType[2] = INTERNAL_BROWSER;
      arrayOfClickActionType[3] = EXTERNAL_BROWSER;
      arrayOfClickActionType[4] = LAUNCH;
      a = arrayOfClickActionType;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.AdInfo
 * JD-Core Version:    0.6.0
 */