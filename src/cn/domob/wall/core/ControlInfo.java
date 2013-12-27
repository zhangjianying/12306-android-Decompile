package cn.domob.wall.core;

import org.json.JSONObject;

public class ControlInfo
{
  p a = new p(ControlInfo.class.getSimpleName());
  private String b;
  private int c = 172800000;
  private int d = 5;
  private int e = 15;
  private int f = 1;
  private int g = 300000;
  private int h = 10;
  private int i = 0;
  private String j = "false";
  private String k = "true";
  private String l = "true";
  private String m = "false";
  private String n = "true";

  public ControlInfo()
  {
  }

  public ControlInfo(JSONObject paramJSONObject)
  {
    if (paramJSONObject != null);
    try
    {
      this.c = paramJSONObject.optInt("cache_duration", this.c);
      this.j = Boolean.toString(paramJSONObject.optBoolean("ent_pic", Boolean.valueOf(this.j).booleanValue()));
      if (Boolean.valueOf(this.j).booleanValue())
        this.b = paramJSONObject.optString("pic_url", "");
      this.k = Boolean.toString(paramJSONObject.optBoolean("new_rem", Boolean.valueOf(this.k).booleanValue()));
      this.d = paramJSONObject.optInt("top_tim", this.d);
      this.l = Boolean.toString(paramJSONObject.optBoolean("dbut_show", Boolean.valueOf(this.l).booleanValue()));
      this.e = paramJSONObject.optInt("show_num", this.e);
      this.f = paramJSONObject.optInt("ad_order", this.f);
      this.m = Boolean.toString(paramJSONObject.optBoolean("app_classify", Boolean.valueOf(this.m).booleanValue()));
      this.n = Boolean.toString(paramJSONObject.optBoolean("ishavebanner", Boolean.valueOf(this.n).booleanValue()));
      this.h = paramJSONObject.optInt("maximum", this.h);
      this.g = paramJSONObject.optInt("interval", this.g);
      this.a.b("controlJsonObject: " + toString());
      return;
    }
    catch (Exception localException)
    {
      this.a.e("Error happens when decoding controlJsonObject: " + paramJSONObject.toString());
      this.a.a(localException);
    }
  }

  protected int a()
  {
    return this.c;
  }

  protected void a(int paramInt)
  {
    this.i = paramInt;
  }

  protected int b()
  {
    return this.h;
  }

  protected int c()
  {
    return this.g;
  }

  public a getAdOrder()
  {
    if (this.f == 1)
      return a.a;
    if (this.f == 2)
      return a.b;
    if (this.f == 3)
      return a.c;
    return a.a;
  }

  public int getBannerIntervalTime()
  {
    return this.d;
  }

  public String getEnterPicURL()
  {
    return this.b;
  }

  public int getNumberOfNewAd()
  {
    return this.i;
  }

  public int getShowAdNum()
  {
    return this.e;
  }

  public boolean isButtonShow()
  {
    return Boolean.valueOf(this.l).booleanValue();
  }

  public boolean isChangeEnterPic()
  {
    return Boolean.valueOf(this.j).booleanValue();
  }

  public boolean isClassify()
  {
    return Boolean.valueOf(this.m).booleanValue();
  }

  public boolean isHasBanner()
  {
    return Boolean.valueOf(this.n).booleanValue();
  }

  public boolean isShowNewReminder()
  {
    return Boolean.valueOf(this.k).booleanValue();
  }

  public String toString()
  {
    return "ControlInfo [mPicURL=" + this.b + ", mCacheDuration=" + this.c + ", bannerIntervalTime=" + this.d + ", mShowNum=" + this.e + ", mOrder=" + this.f + ", mInterval=" + this.g + ", maxNumberOfAdImpAllowSavedInDB=" + this.h + ", numberOfNewAd=" + this.i + ", mChangePic=" + this.j + ", mNewReminder=" + this.k + ", mRightButShow=" + this.l + ", mClassify=" + this.m + "]";
  }

  static enum a
  {
    static
    {
      a[] arrayOfa = new a[3];
      arrayOfa[0] = a;
      arrayOfa[1] = b;
      arrayOfa[2] = c;
      d = arrayOfa;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.ControlInfo
 * JD-Core Version:    0.6.0
 */