package cn.domob.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.domob.wall.core.AdInfo;
import cn.domob.wall.core.AdInfo.ClickActionType;
import cn.domob.wall.core.ControlInfo;
import cn.domob.wall.core.DService;
import cn.domob.wall.core.DService.OnImageDownload;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class n extends BaseAdapter
{
  private static o b = new o(n.class.getSimpleName());
  int a;
  private ArrayList<AdInfo> c = new ArrayList();
  private Context d;
  private ControlInfo e;
  private DService f;

  public n(Context paramContext, ArrayList<AdInfo> paramArrayList, ControlInfo paramControlInfo, DService paramDService)
  {
    this.c = paramArrayList;
    this.d = paramContext;
    this.e = paramControlInfo;
    this.f = paramDService;
    this.a = (int)((-140 + (int)(h.c(this.d) / h.b(this.d))) * h.b(this.d));
  }

  public int getCount()
  {
    return this.c.size();
  }

  public Object getItem(int paramInt)
  {
    return this.c.get(paramInt);
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
      paramView = (RelativeLayout)LayoutInflater.from(this.d).inflate(e.a(this.d, "l_home_list_element"), null);
      locala = new a();
      locala.a = ((ImageView)paramView.findViewById(d.a(this.d, "lsit_logo")));
      locala.b = ((ImageView)paramView.findViewById(d.a(this.d, "lsit_logo_new")));
      locala.c = ((TextView)paramView.findViewById(d.a(this.d, "list_name")));
      locala.d = ((TextView)paramView.findViewById(d.a(this.d, "list_describe")));
      locala.e = ((RelativeLayout)paramView.findViewById(d.a(this.d, "list_actiontype_bg")));
      locala.f = ((TextView)paramView.findViewById(d.a(this.d, "list_actiontype")));
      locala.g = ((TextView)paramView.findViewById(d.a(this.d, "list_action_des")));
      locala.h = ((TextView)paramView.findViewById(d.a(this.d, "list_apksize")));
      paramView.setTag(locala);
      locala.d.setWidth(this.a);
      locala.a.setImageResource(c.a(this.d, "u_list_logo"));
      locala.a.setTag(((AdInfo)this.c.get(paramInt)).getAdLogoURL());
      this.f.requestImage(((AdInfo)this.c.get(paramInt)).getAdLogoURL(), locala.a, new DService.OnImageDownload()
      {
        public void onDownloadSuc(Bitmap paramBitmap, String paramString, ImageView paramImageView)
        {
          if ((paramImageView.getTag().equals(paramString)) && (paramBitmap != null))
            paramImageView.setImageBitmap(paramBitmap);
        }
      });
      if (!this.e.isShowNewReminder())
        break label448;
      if (!((AdInfo)this.c.get(paramInt)).isNew())
        break label436;
      locala.b.setVisibility(0);
    }
    while (true)
    {
      if (this.e.isButtonShow())
        break label460;
      locala.e.setVisibility(4);
      locala.g.setVisibility(4);
      locala.e.setOnClickListener(new View.OnClickListener(paramInt)
      {
        public void onClick(View paramView)
        {
          ((AdInfo)n.a(n.this).get(this.a)).setAdActualPosition(this.a);
          n.b(n.this).onClickWallItemButton((AdInfo)n.a(n.this).get(this.a));
        }
      });
      locala.c.setText(((AdInfo)this.c.get(paramInt)).getAdTitle());
      locala.d.setText(((AdInfo)this.c.get(paramInt)).getAdBriefText());
      return paramView;
      locala = (a)paramView.getTag();
      break;
      label436: locala.b.setVisibility(4);
      continue;
      label448: locala.b.setVisibility(4);
    }
    label460: if (((AdInfo)this.c.get(paramInt)).getAdActionType().name().equals("DOWNLOAD"))
    {
      locala.f.setText("下载");
      locala.g.setText("免费下载");
      locala.h.setVisibility(0);
      String str = new DecimalFormat("0.00").format(((AdInfo)this.c.get(paramInt)).getAdSize() / 1024.0F / 1024.0F);
      locala.h.setText(str + "M");
    }
    while (true)
    {
      locala.h.setVisibility(4);
      locala.g.setVisibility(4);
      break;
      if (((AdInfo)this.c.get(paramInt)).getAdActionType().name().equals("INTERNAL_BROWSER"))
      {
        locala.f.setText("详情");
        locala.g.setText("查看详情");
        locala.h.setVisibility(4);
        continue;
      }
      if (((AdInfo)this.c.get(paramInt)).getAdActionType().name().equals("EXTERNAL_BROWSER"))
      {
        locala.f.setText("详情");
        locala.g.setText("查看详情");
        locala.h.setVisibility(4);
        continue;
      }
      if (((AdInfo)this.c.get(paramInt)).getAdActionType().name().equals("LAUNCH"))
      {
        locala.h.setVisibility(4);
        continue;
      }
      locala.e.setVisibility(4);
      locala.g.setVisibility(4);
      locala.h.setVisibility(4);
    }
  }

  public class a
  {
    ImageView a;
    ImageView b;
    TextView c;
    TextView d;
    RelativeLayout e;
    TextView f;
    TextView g;
    TextView h;

    public a()
    {
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.ui.n
 * JD-Core Version:    0.6.0
 */