package cn.domob.wall.core;

import java.util.List;

public class DetailsPageInfo
{
  private AdInfo a;

  public DetailsPageInfo()
  {
  }

  DetailsPageInfo(AdInfo paramAdInfo)
  {
    this.a = paramAdInfo;
  }

  AdInfo a()
  {
    return this.a;
  }

  public String getAdProvider()
  {
    return this.a.getAdProvider();
  }

  public AdInfo.AdType getAdType()
  {
    return this.a.getAdType();
  }

  public String getDesctiption()
  {
    return this.a.getAdDetailDescription();
  }

  public String getLogo()
  {
    return this.a.getAdLogoURL();
  }

  public String getName()
  {
    return this.a.getAdName();
  }

  public List<String> getScreenshot()
  {
    return this.a.getScreenshot();
  }

  public int getSize()
  {
    return this.a.getAdSize();
  }

  public String getVersion()
  {
    return this.a.getAdVersionName();
  }

  public boolean isNew()
  {
    return this.a.isNew();
  }

  public String toString()
  {
    return "DetailsPageInfo [adItemInfo=" + this.a + "]";
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.DetailsPageInfo
 * JD-Core Version:    0.6.0
 */