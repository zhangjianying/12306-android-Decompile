package cn.domob.wall.core.download;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import cn.domob.wall.core.p;
import java.util.Hashtable;

public class DActivity extends Activity
{
  public static final String ACTIVITY_TYPE = "ActivityType";
  public static final String NOTICE_MESSAGE = "msg";
  public static final int TYPE_DOWNLOADER = 2;
  public static final int TYPE_INSTALL_RECEIVER = 1;
  public static final int TYPE_NONE = 0;
  public static final int TYPE_NOTICE = 3;
  public static final int TYPE_SHOW_WEBVIEW = 5;
  public static final int TYPE_UPLOAD_PIC = 4;
  public static final String WEBVIEW_URL_NAME = "webview_url";
  private static p a = new p(DActivity.class.getSimpleName());
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
            b localb = (b)b.a.get(DActivity.a(DActivity.this));
            if (localb != null)
            {
              localb.b();
              c localc = localb.a();
              if (localc != null)
                localc.b();
            }
            DActivity.this.finish();
          }
        }).setPositiveButton("继续下载", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            DActivity.this.finish();
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
          DActivity.this.finish();
        }
      }).show();
    }
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
  }

  public void onCreate(Bundle paramBundle)
  {
    int i = getIntent().getIntExtra("ActivityType", 0);
    a.b("intent activity type " + i);
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
 * Qualified Name:     cn.domob.wall.core.download.DActivity
 * JD-Core Version:    0.6.0
 */