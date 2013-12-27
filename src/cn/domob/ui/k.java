package cn.domob.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.domob.wall.core.ControlInfo;
import cn.domob.wall.core.DService;

public class k
{
  private static o a = new o(k.class.getSimpleName());
  private Context b;
  private DService c;
  private ControlInfo d;

  public k(Context paramContext, DService paramDService, ControlInfo paramControlInfo)
  {
    this.b = paramContext;
    this.c = paramDService;
    this.d = paramControlInfo;
  }

  public View a()
  {
    RelativeLayout localRelativeLayout1 = (RelativeLayout)LayoutInflater.from(this.b).inflate(e.a(this.b, "l_handle"), null);
    ImageView localImageView = (ImageView)localRelativeLayout1.findViewById(d.a(this.b, "handle_background"));
    RelativeLayout localRelativeLayout2 = (RelativeLayout)localRelativeLayout1.findViewById(d.a(this.b, "handle_num_background"));
    TextView localTextView = (TextView)localRelativeLayout1.findViewById(d.a(this.b, "handle_num"));
    ((RelativeLayout)localRelativeLayout1.findViewById(d.a(this.b, "handleRelativeLayout"))).setVisibility(0);
    a.b("是否更改入口图片：" + this.d.isChangeEnterPic());
    if (this.d.isChangeEnterPic())
    {
      a.b("入口图片URL：" + this.d.getEnterPicURL());
      String str = this.d.getEnterPicURL();
      this.c.requestImageAsyn(this.b, str, localImageView, c.a(this.b, "u_handle_background"));
    }
    while (true)
    {
      a.b("新增应用提醒是否开启：" + this.d.isShowNewReminder());
      int i = this.d.getNumberOfNewAd();
      a.b("新增应用数量：" + i);
      localRelativeLayout2.setVisibility(4);
      localTextView.setVisibility(4);
      return localRelativeLayout1;
      localImageView.setImageResource(c.a(this.b, "u_handle_background"));
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.ui.k
 * JD-Core Version:    0.6.0
 */