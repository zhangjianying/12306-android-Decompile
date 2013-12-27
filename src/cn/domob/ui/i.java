package cn.domob.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import cn.domob.wall.core.DService;
import java.util.List;

public class i extends BaseAdapter
{
  private static o d = new o(i.class.getSimpleName());
  private List<String> a;
  private Context b;
  private DService c;

  public i(List<String> paramList, Context paramContext, DService paramDService)
  {
    this.a = paramList;
    this.b = paramContext;
    this.c = paramDService;
  }

  public int getCount()
  {
    return this.a.size();
  }

  public Object getItem(int paramInt)
  {
    return this.a.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    a locala;
    if (paramView == null)
    {
      paramView = LayoutInflater.from(this.b).inflate(e.a(this.b, "l_details_gallery"), null);
      paramView.setLayoutParams(new Gallery.LayoutParams(-2, -2));
      locala = new a(null);
      locala.a = ((ImageView)paramView.findViewById(d.a(this.b, "gallery_image")));
      paramView.setTag(locala);
    }
    while (true)
    {
      d.b("图片url:" + (String)this.a.get(paramInt % this.a.size()));
      this.c.requestImageAsyn(this.b, (String)this.a.get(paramInt % this.a.size()), locala.a);
      locala.a.setScaleType(ImageView.ScaleType.FIT_XY);
      return paramView;
      locala = (a)paramView.getTag();
    }
  }

  private class a
  {
    ImageView a;

    private a()
    {
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.ui.i
 * JD-Core Version:    0.6.0
 */