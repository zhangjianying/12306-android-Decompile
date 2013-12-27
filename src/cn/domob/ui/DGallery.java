package cn.domob.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

public class DGallery extends Gallery
{
  public DGallery(Context paramContext)
  {
    super(paramContext);
  }

  public DGallery(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public boolean isScroollingLeft(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2)
  {
    return paramMotionEvent2.getX() > paramMotionEvent1.getX();
  }

  public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
  {
    if (isScroollingLeft(paramMotionEvent1, paramMotionEvent2));
    for (int i = 21; ; i = 22)
    {
      onKeyDown(i, null);
      return true;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.ui.DGallery
 * JD-Core Version:    0.6.0
 */