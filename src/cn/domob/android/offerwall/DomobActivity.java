package cn.domob.android.offerwall;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import cn.domob.android.a.a;
import cn.domob.android.a.c;
import java.util.Hashtable;

public class DomobActivity extends Activity
{
  public static final String ACTIVITY_TYPE = "DomobActivityType";
  public static final String EXPAND_BASE_URL = "ex_base_url";
  public static final String EXPAND_CONTENT = "ex_content";
  public static final String EXPAND_URL = "ex_url";
  public static final String NOTICE_MESSAGE = "msg";
  public static final int TYPE_DOWNLOADER = 2;
  public static final int TYPE_EXPAND = 4;
  public static final int TYPE_INSTALL_RECEIVER = 1;
  public static final int TYPE_NONE = 0;
  public static final int TYPE_NOTICE = 3;
  public static final int TYPE_UPLOAD_PIC = 5;
  private static m a = new m(DomobActivity.class.getSimpleName());
  private Context b = this;
  private String c = "";

  private void a()
  {
    if (getIntent().getExtras() != null)
    {
      Intent localIntent = getIntent();
      String str1 = localIntent.getStringExtra("appName");
      this.c = localIntent.getStringExtra("appId");
      String str2 = localIntent.getStringExtra("actType");
      if ((str2 != null) && (str2.equals("typeCancel")))
        new AlertDialog.Builder(this.b).setTitle("取消").setMessage(str1 + "正在下载是否取消?").setNegativeButton("取消下载", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            a locala = (a)a.a.get(DomobActivity.a(DomobActivity.this));
            if (locala != null)
            {
              locala.b();
              c localc = locala.a();
              if (localc != null)
                localc.b();
            }
            DomobActivity.this.finish();
          }
        }).setPositiveButton("继续下载", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            DomobActivity.this.finish();
          }
        }).show();
    }
  }

  private void b()
  {
    if (getIntent().getExtras() != null)
    {
      String str = getIntent().getStringExtra("msg");
      new AlertDialog.Builder(this.b).setMessage(str).setNegativeButton("确定", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          DomobActivity.this.finish();
        }
      }).show();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    int i = getIntent().getIntExtra("DomobActivityType", 0);
    a.a("Activity type: " + i);
    switch (i)
    {
    default:
      finish();
      return;
    case 2:
      super.onCreate(paramBundle);
      a();
      return;
    case 1:
      setTheme(16973835);
      super.onCreate(paramBundle);
      return;
    case 3:
    }
    super.onCreate(paramBundle);
    b();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.android.offerwall.DomobActivity
 * JD-Core Version:    0.6.0
 */