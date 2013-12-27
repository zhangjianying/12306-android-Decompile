package cn.domob.ui;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.domob.wall.core.DService;
import cn.domob.wall.core.DetailsPageInfo;
import java.text.DecimalFormat;

public class j
{
  private static o a = new o(j.class.getSimpleName());
  private Context b;
  private DService c;

  public j(Context paramContext, DService paramDService)
  {
    this.b = paramContext;
    this.c = paramDService;
  }

  public float a()
  {
    Display localDisplay = ((WindowManager)this.b.getSystemService("window")).getDefaultDisplay();
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    localDisplay.getMetrics(localDisplayMetrics);
    return localDisplayMetrics.density;
  }

  public View a(DetailsPageInfo paramDetailsPageInfo)
  {
    RelativeLayout localRelativeLayout = (RelativeLayout)LayoutInflater.from(this.b).inflate(e.a(this.b, "l_details"), null);
    ScrollView localScrollView = (ScrollView)localRelativeLayout.findViewById(d.a(this.b, "scroll"));
    if (h.g(this.b) > h.f(this.b));
    for (int i = h.g(this.b); ; i = h.f(this.b))
    {
      if ((int)(i / h.b(this.b)) <= 500)
      {
        ViewGroup.LayoutParams localLayoutParams = localScrollView.getLayoutParams();
        localLayoutParams.height = (int)(310.0F * h.b(this.b));
        localScrollView.setLayoutParams(localLayoutParams);
      }
      ((Gallery)localRelativeLayout.findViewById(d.a(this.b, "details_gallery_screenshot"))).setAdapter(new i(paramDetailsPageInfo.getScreenshot(), this.b, this.c));
      ImageView localImageView = (ImageView)localRelativeLayout.findViewById(d.a(this.b, "details_logo"));
      this.c.requestImageAsyn(this.b, paramDetailsPageInfo.getLogo(), localImageView);
      ((TextView)localRelativeLayout.findViewById(d.a(this.b, "details_name"))).setText(paramDetailsPageInfo.getName());
      ((TextView)localRelativeLayout.findViewById(d.a(this.b, "details_versionName"))).setText("版本:" + paramDetailsPageInfo.getVersion());
      TextView localTextView1 = (TextView)localRelativeLayout.findViewById(d.a(this.b, "details_apkSize"));
      int j = paramDetailsPageInfo.getSize();
      String str = new DecimalFormat("0.00").format(j / 1024.0F / 1024.0F);
      localTextView1.setText("大小:" + str + "M");
      TextView localTextView2 = (TextView)localRelativeLayout.findViewById(d.a(this.b, "details_details"));
      SpannableString localSpannableString = new SpannableString("简介：");
      localSpannableString.setSpan(new ForegroundColorSpan(this.b.getResources().getColor(b.a(this.b, "details_text_color"))), 0, 3, 33);
      localSpannableString.setSpan(new AbsoluteSizeSpan(20 * (int)a()), 0, 2, 33);
      localTextView2.setText(localSpannableString);
      localTextView2.append(paramDetailsPageInfo.getDesctiption());
      localRelativeLayout.findViewById(d.a(this.b, "dl01")).setOnClickListener(new View.OnClickListener(paramDetailsPageInfo)
      {
        public void onClick(View paramView)
        {
          j.b().b("您点击了详情页下载按钮");
          j.a(j.this).onClickDetailsPageButton(this.a);
        }
      });
      return localRelativeLayout;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.ui.j
 * JD-Core Version:    0.6.0
 */