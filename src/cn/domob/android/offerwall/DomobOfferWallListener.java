package cn.domob.android.offerwall;

public abstract interface DomobOfferWallListener
{
  public abstract void onOfferWallLoadFailed(DomobOfferWallView paramDomobOfferWallView);

  public abstract void onOfferWallLoadFinished(DomobOfferWallView paramDomobOfferWallView);

  public abstract void onOfferWallLoadStart(DomobOfferWallView paramDomobOfferWallView);
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.android.offerwall.DomobOfferWallListener
 * JD-Core Version:    0.6.0
 */