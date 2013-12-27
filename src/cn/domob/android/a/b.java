package cn.domob.android.a;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import cn.domob.android.offerwall.l;
import cn.domob.android.offerwall.m;
import java.io.File;
import java.util.Hashtable;

class b extends Handler
{
  b(a parama)
  {
  }

  public void handleMessage(Message paramMessage)
  {
    super.handleMessage(paramMessage);
    if ((paramMessage.what - a.a(this.a) < 5) && (paramMessage.what != 100));
    do
    {
      do
      {
        return;
        a.a(this.a, paramMessage.what);
        a.b(this.a, paramMessage.what);
        if (!a.c())
          continue;
        a.d(this.a).setLatestEventInfo(a.d(), a.b(this.a) + "正在下载", "已下载" + paramMessage.what + "%", a.c(this.a));
        a.f(this.a).notify(a.e(this.a), a.d(this.a));
      }
      while (a.g(this.a) != 100);
      a.e().a(a.class, a.b(this.a) + " download success");
      File localFile1 = new File(a.h(this.a));
      if (a.h(this.a).endsWith(".temp"))
      {
        a.a(this.a, a.h(this.a).substring(0, a.h(this.a).length() - ".temp".length()));
        File localFile2 = new File(a.h(this.a));
        a.e().a(a.class, a.b(this.a) + "FileName change to " + localFile2);
        localFile1.renameTo(localFile2);
      }
      a.a.remove(a.i(this.a));
      if (a.j(this.a) != null)
        a.j(this.a).a(a.h(this.a));
      a.b(this.a, 0);
    }
    while (!a.c());
    a.d(this.a).icon = 17301634;
    a.d(this.a).tickerText = (a.b(this.a) + "下载完毕");
    a.d(this.a).flags = 16;
    Intent localIntent1 = a.a(a.h(this.a));
    a.a(this.a, PendingIntent.getActivity(a.d(), a.e(this.a), localIntent1, 134217728));
    a.d(this.a).setLatestEventInfo(a.d(), a.b(this.a) + "下载完毕", "", a.c(this.a));
    a.f(this.a).notify(a.e(this.a), a.d(this.a));
    if ((a.k(this.a) != null) && (!a.k(this.a).equalsIgnoreCase(l.c(a.h(this.a)))))
    {
      a.j(this.a).b(a.i(this.a));
      a.e().b(a.d(), "md5 authorized failed");
      a.f(this.a).cancel(a.e(this.a));
      return;
    }
    Intent localIntent2 = a.a(a.h(this.a));
    localIntent2.addFlags(268435456);
    a.d().startActivity(localIntent2);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.android.a.b
 * JD-Core Version:    0.6.0
 */