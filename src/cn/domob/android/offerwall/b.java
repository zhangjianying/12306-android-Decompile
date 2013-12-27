package cn.domob.android.offerwall;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

class b
{
  protected static final String a = "domob_config";
  protected static final String b = "cookie_id";
  protected static final String c = "interval";
  protected static final String d = "disable";
  protected static final String e = "timestamp";
  protected static final String f = "time";
  protected static final String g = "next_time";
  private Context h;
  private String i;
  private SharedPreferences j;

  b(Context paramContext, String paramString)
  {
    this.h = paramContext;
    this.i = paramString;
    this.j = this.h.getSharedPreferences(this.i, 0);
  }

  protected float a(String paramString, float paramFloat)
  {
    return this.j.getFloat(paramString, paramFloat);
  }

  protected int a(String paramString, int paramInt)
  {
    return this.j.getInt(paramString, paramInt);
  }

  protected long a(String paramString, long paramLong)
  {
    return this.j.getLong(paramString, paramLong);
  }

  protected String a(String paramString1, String paramString2)
  {
    return this.j.getString(paramString1, paramString2);
  }

  protected void a(HashMap<String, Object> paramHashMap)
  {
    SharedPreferences.Editor localEditor;
    while (true)
    {
      String str;
      Object localObject2;
      synchronized (this.j)
      {
        localEditor = this.j.edit();
        Iterator localIterator = paramHashMap.keySet().iterator();
        if (!localIterator.hasNext())
          break;
        str = (String)localIterator.next();
        localObject2 = paramHashMap.get(str);
        if ((localObject2 instanceof String))
          localEditor.putString(str, (String)localObject2);
      }
      if ((localObject2 instanceof Boolean))
      {
        localEditor.putBoolean(str, ((Boolean)localObject2).booleanValue());
        continue;
      }
      if ((localObject2 instanceof Integer))
      {
        localEditor.putInt(str, ((Integer)localObject2).intValue());
        continue;
      }
      if ((localObject2 instanceof Float))
      {
        localEditor.putFloat(str, ((Float)localObject2).floatValue());
        continue;
      }
      if (!(localObject2 instanceof Long))
        continue;
      localEditor.putLong(str, ((Long)localObject2).longValue());
    }
    localEditor.commit();
    monitorexit;
  }

  protected boolean a(String paramString, boolean paramBoolean)
  {
    return this.j.getBoolean(paramString, paramBoolean);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.android.offerwall.b
 * JD-Core Version:    0.6.0
 */