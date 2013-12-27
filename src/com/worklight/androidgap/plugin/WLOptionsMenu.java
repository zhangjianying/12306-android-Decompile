package com.worklight.androidgap.plugin;

import android.webkit.JavascriptInterface;
import com.worklight.androidgap.WLDroidGap;
import com.worklight.common.WLUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WLOptionsMenu
{
  private WLDroidGap activity = null;
  private boolean hasChanged = false;
  private boolean isEnabled = false;
  private boolean isInit = false;
  private boolean isVisible = false;
  private int itemIndexer = 0;
  private Map<Integer, WLMenuItem> items = null;
  private String skinName = null;

  public WLOptionsMenu(WLDroidGap paramWLDroidGap)
  {
    this.activity = paramWLDroidGap;
  }

  @JavascriptInterface
  private WLMenuItem getItemByJavaScriptId(String paramString)
  {
    Iterator localIterator = this.items.values().iterator();
    Object localObject;
    while (true)
    {
      boolean bool = localIterator.hasNext();
      localObject = null;
      if (!bool)
        break;
      WLMenuItem localWLMenuItem = (WLMenuItem)localIterator.next();
      if (!localWLMenuItem.getJavaScriptId().equals(paramString))
        continue;
      localObject = localWLMenuItem;
    }
    return localObject;
  }

  @JavascriptInterface
  private void setChanged()
  {
    this.hasChanged = true;
  }

  @JavascriptInterface
  public WLMenuItem addItem(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean)
  {
    if (getItemByJavaScriptId(paramString1) != null)
    {
      WLUtils.error("WL.OptionMenu.addItem failed because an item with id " + paramString1 + " already exists. Use unique id-s.");
      return null;
    }
    int i = this.itemIndexer;
    this.itemIndexer = (1 + this.itemIndexer);
    WLMenuItem localWLMenuItem = new WLMenuItem(i, paramString1, paramString2, paramString3, paramString4, paramBoolean);
    this.items.put(Integer.valueOf(i), localWLMenuItem);
    setChanged();
    return localWLMenuItem;
  }

  @JavascriptInterface
  public WLMenuItem getItem(String paramString)
  {
    WLMenuItem localWLMenuItem = getItemByJavaScriptId(paramString);
    if (localWLMenuItem == null)
      WLUtils.error("WL.OptionMenu.getItem failed because an item with id " + paramString + " does not exist.");
    return localWLMenuItem;
  }

  public WLMenuItem getItemById(int paramInt)
  {
    return (WLMenuItem)this.items.get(Integer.valueOf(paramInt));
  }

  public List<WLMenuItem> getItems()
  {
    ArrayList localArrayList = new ArrayList(this.items.values());
    Collections.sort(localArrayList, new Comparator()
    {
      public int compare(WLMenuItem paramWLMenuItem1, WLMenuItem paramWLMenuItem2)
      {
        return paramWLMenuItem1.getId() - paramWLMenuItem2.getId();
      }
    });
    return localArrayList;
  }

  @JavascriptInterface
  public String getSkinName()
  {
    return this.skinName;
  }

  @JavascriptInterface
  public boolean hasChanged()
  {
    if (this.hasChanged)
      return true;
    Iterator localIterator = this.items.values().iterator();
    while (localIterator.hasNext())
      if (((WLMenuItem)localIterator.next()).hasChanged())
        return true;
    return false;
  }

  @JavascriptInterface
  public void init()
  {
    this.skinName = WLUtils.readWLPref(this.activity, "wlSkinName");
    this.isVisible = true;
    this.isEnabled = true;
    this.hasChanged = false;
    this.items = new HashMap();
    this.itemIndexer = 0;
    this.isInit = true;
  }

  @JavascriptInterface
  public boolean isEnabled()
  {
    return this.isEnabled;
  }

  @JavascriptInterface
  public boolean isInit()
  {
    return this.isInit;
  }

  @JavascriptInterface
  public boolean isVisible()
  {
    return this.isVisible;
  }

  @JavascriptInterface
  public void removeItem(String paramString)
  {
    WLMenuItem localWLMenuItem = getItem(paramString);
    if (localWLMenuItem != null)
    {
      this.items.remove(Integer.valueOf(localWLMenuItem.getId()));
      setChanged();
    }
  }

  @JavascriptInterface
  public void removeItems()
  {
    this.items.clear();
    setChanged();
  }

  @JavascriptInterface
  public void setEnabled(boolean paramBoolean)
  {
    this.isEnabled = paramBoolean;
    setChanged();
  }

  @JavascriptInterface
  public void setVisible(boolean paramBoolean)
  {
    this.isVisible = paramBoolean;
    setChanged();
  }

  @JavascriptInterface
  public void unsetChanged()
  {
    this.hasChanged = false;
    Iterator localIterator = this.items.values().iterator();
    while (localIterator.hasNext())
      ((WLMenuItem)localIterator.next()).unsetChanged();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.WLOptionsMenu
 * JD-Core Version:    0.6.0
 */