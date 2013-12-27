package org.apache.cordova;

import android.os.Bundle;

public class StandAlone extends DroidGap
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.loadUrl("file:///android_asset/www/index.html");
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.StandAlone
 * JD-Core Version:    0.6.0
 */