package com.worklight.androidgap.plugin;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.worklight.androidgap.NoSuchResourceException;
import com.worklight.androidgap.WLDroidGap;
import com.worklight.common.WLUtils;

public class WLMenuItem
{
  private String callback;
  private boolean hasChanged;
  private int id;
  private String imagePath;
  private boolean isEnabled;
  private String javascriptId;
  private String title;

  public WLMenuItem(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean)
  {
    this.id = paramInt;
    this.javascriptId = paramString1;
    this.callback = paramString2;
    this.title = paramString3;
    this.isEnabled = paramBoolean;
    setImagePath(paramString4);
    this.hasChanged = true;
  }

  private void setChanged()
  {
    this.hasChanged = true;
  }

  public String getCallback()
  {
    return this.callback;
  }

  public int getId()
  {
    return this.id;
  }

  public Drawable getImage(WLDroidGap paramWLDroidGap)
  {
    String str = this.imagePath;
    Object localObject = null;
    Resources localResources;
    if (str != null)
      localResources = paramWLDroidGap.getResources();
    try
    {
      Drawable localDrawable = localResources.getDrawable(WLUtils.getResourceId(paramWLDroidGap, "drawable", this.imagePath));
      localObject = localDrawable;
      return localObject;
    }
    catch (NoSuchResourceException localNoSuchResourceException)
    {
      WLUtils.error(localNoSuchResourceException.getMessage());
    }
    return null;
  }

  public String getImagePath()
  {
    return this.imagePath;
  }

  public String getJavaScriptId()
  {
    return this.javascriptId;
  }

  public String getTitle()
  {
    return this.title;
  }

  public boolean hasChanged()
  {
    return this.hasChanged;
  }

  public boolean isEnabled()
  {
    return this.isEnabled;
  }

  public void setEnabled(boolean paramBoolean)
  {
    this.isEnabled = paramBoolean;
    setChanged();
  }

  public void setImagePath(String paramString)
  {
    if ((paramString != null) && (!paramString.equals(this.imagePath)))
    {
      this.imagePath = paramString;
      setChanged();
    }
  }

  public void setTitle(String paramString)
  {
    this.title = paramString;
    setChanged();
  }

  public void unsetChanged()
  {
    this.hasChanged = false;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.WLMenuItem
 * JD-Core Version:    0.6.0
 */