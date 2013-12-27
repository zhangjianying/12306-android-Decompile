package com.tl.uic.util;

import android.os.AsyncTask;
import android.view.View;
import com.tl.uic.Tealeaf;

public class ScreenShotTask extends AsyncTask<View, Void, Boolean>
{
  protected final Boolean doInBackground(View[] paramArrayOfView)
  {
    Boolean localBoolean1 = Boolean.valueOf(false);
    try
    {
      Boolean localBoolean2 = Tealeaf.takeScreenShot(paramArrayOfView[0], paramArrayOfView[0].getClass().getName());
      return localBoolean2;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localBoolean1;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.util.ScreenShotTask
 * JD-Core Version:    0.6.0
 */