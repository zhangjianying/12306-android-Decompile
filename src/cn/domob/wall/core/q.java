package cn.domob.wall.core;

import android.content.ContentValues;
import android.content.Context;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.List<Lcn.domob.wall.core.q.a;>;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class q
{
  static q a;
  private static p b = new p(q.class.getSimpleName());

  static q a()
  {
    monitorenter;
    try
    {
      if (a == null)
        a = new q();
      q localq = a;
      return localq;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  private void a(b paramb, int paramInt, JSONArray paramJSONArray)
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("id", paramb.c());
      localJSONObject.put("tr", paramb.b());
      localJSONObject.put("sn", paramInt);
      paramJSONArray.put(localJSONObject);
      return;
    }
    catch (JSONException localJSONException)
    {
      while (true)
        b.a(localJSONException);
    }
  }

  protected List<a> a(k paramk)
  {
    List localList = a().a(paramk.d());
    ArrayList localArrayList1 = new ArrayList();
    Integer localInteger1 = Integer.valueOf(0);
    localList.add(null);
    Iterator localIterator = localList.iterator();
    ArrayList localArrayList2 = null;
    JSONArray localJSONArray = null;
    Object localObject = null;
    Integer localInteger2 = localInteger1;
    if (localIterator.hasNext())
    {
      b localb = (b)localIterator.next();
      if ((localb == null) || (localObject == null) || (!((b)localObject).a().equals(localb.a())))
      {
        if ((localJSONArray != null) && (localArrayList2 != null) && (localObject != null))
        {
          a((b)localObject, 1 + localInteger2.intValue(), localJSONArray);
          localArrayList2.add(localObject);
          a locala = new a();
          locala.a = ((b)localObject).a();
          locala.b = localJSONArray;
          locala.c = localArrayList2;
          localArrayList1.add(locala);
          localInteger2 = Integer.valueOf(0);
        }
        localArrayList2 = new ArrayList();
        localJSONArray = new JSONArray();
      }
      do
      {
        localObject = localb;
        break;
      }
      while (localObject == null);
      if (localb.c().equals(((b)localObject).c()));
      for (localInteger2 = Integer.valueOf(1 + localInteger2.intValue()); ; localInteger2 = Integer.valueOf(0))
      {
        localArrayList2.add(localObject);
        break;
        a((b)localObject, 1 + localInteger2.intValue(), localJSONArray);
      }
    }
    return (List<a>)localArrayList1;
  }

  // ERROR //
  List<b> a(String paramString)
  {
    // Byte code:
    //   0: invokestatic 139	cn/domob/wall/core/h:a	()Lcn/domob/wall/core/h;
    //   3: astore_2
    //   4: new 77	java/util/ArrayList
    //   7: dup
    //   8: invokespecial 78	java/util/ArrayList:<init>	()V
    //   11: astore_3
    //   12: aload_2
    //   13: ldc 141
    //   15: aconst_null
    //   16: ldc 143
    //   18: iconst_1
    //   19: anewarray 108	java/lang/String
    //   22: dup
    //   23: iconst_0
    //   24: aload_1
    //   25: aastore
    //   26: ldc 145
    //   28: invokevirtual 148	cn/domob/wall/core/h:a	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   31: astore 7
    //   33: aload 7
    //   35: astore 5
    //   37: aload 5
    //   39: ldc 38
    //   41: invokeinterface 154 2 0
    //   46: istore 8
    //   48: aload 5
    //   50: ldc 156
    //   52: invokeinterface 154 2 0
    //   57: istore 9
    //   59: aload 5
    //   61: ldc 158
    //   63: invokeinterface 154 2 0
    //   68: istore 10
    //   70: aload 5
    //   72: ldc 160
    //   74: invokeinterface 154 2 0
    //   79: istore 11
    //   81: aload 5
    //   83: ldc 49
    //   85: invokeinterface 154 2 0
    //   90: istore 12
    //   92: aload 5
    //   94: invokeinterface 163 1 0
    //   99: pop
    //   100: aload 5
    //   102: invokeinterface 166 1 0
    //   107: ifne +194 -> 301
    //   110: new 40	cn/domob/wall/core/q$b
    //   113: dup
    //   114: aload_0
    //   115: invokespecial 167	cn/domob/wall/core/q$b:<init>	(Lcn/domob/wall/core/q;)V
    //   118: astore 14
    //   120: aload 14
    //   122: aload 5
    //   124: iload 8
    //   126: invokeinterface 171 2 0
    //   131: invokevirtual 174	cn/domob/wall/core/q$b:e	(Ljava/lang/String;)V
    //   134: aload 14
    //   136: aload 5
    //   138: iload 9
    //   140: invokeinterface 171 2 0
    //   145: invokevirtual 176	cn/domob/wall/core/q$b:c	(Ljava/lang/String;)V
    //   148: aload 14
    //   150: aload 5
    //   152: iload 10
    //   154: invokeinterface 171 2 0
    //   159: invokevirtual 178	cn/domob/wall/core/q$b:d	(Ljava/lang/String;)V
    //   162: aload 14
    //   164: aload 5
    //   166: iload 11
    //   168: invokeinterface 171 2 0
    //   173: invokevirtual 180	cn/domob/wall/core/q$b:a	(Ljava/lang/String;)V
    //   176: aload 14
    //   178: aload 5
    //   180: iload 12
    //   182: invokeinterface 171 2 0
    //   187: invokevirtual 182	cn/domob/wall/core/q$b:b	(Ljava/lang/String;)V
    //   190: aload_3
    //   191: aload 14
    //   193: invokeinterface 90 2 0
    //   198: pop
    //   199: getstatic 24	cn/domob/wall/core/q:b	Lcn/domob/wall/core/p;
    //   202: astore 16
    //   204: iconst_4
    //   205: anewarray 4	java/lang/Object
    //   208: astore 17
    //   210: aload 17
    //   212: iconst_0
    //   213: aload 14
    //   215: invokevirtual 106	cn/domob/wall/core/q$b:a	()Ljava/lang/String;
    //   218: aastore
    //   219: aload 17
    //   221: iconst_1
    //   222: aload 14
    //   224: invokevirtual 51	cn/domob/wall/core/q$b:b	()Ljava/lang/String;
    //   227: aastore
    //   228: aload 17
    //   230: iconst_2
    //   231: aload 14
    //   233: invokevirtual 43	cn/domob/wall/core/q$b:c	()Ljava/lang/String;
    //   236: aastore
    //   237: aload 17
    //   239: iconst_3
    //   240: aload 14
    //   242: invokevirtual 183	cn/domob/wall/core/q$b:d	()Ljava/lang/String;
    //   245: aastore
    //   246: aload 16
    //   248: ldc 185
    //   250: aload 17
    //   252: invokestatic 189	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   255: invokevirtual 190	cn/domob/wall/core/p:a	(Ljava/lang/String;)V
    //   258: aload 5
    //   260: invokeinterface 193 1 0
    //   265: pop
    //   266: goto -166 -> 100
    //   269: astore 4
    //   271: getstatic 24	cn/domob/wall/core/q:b	Lcn/domob/wall/core/p;
    //   274: aload 4
    //   276: invokevirtual 64	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   279: getstatic 24	cn/domob/wall/core/q:b	Lcn/domob/wall/core/p;
    //   282: ldc 195
    //   284: invokevirtual 196	cn/domob/wall/core/p:e	(Ljava/lang/String;)V
    //   287: aload 5
    //   289: ifnull +10 -> 299
    //   292: aload 5
    //   294: invokeinterface 199 1 0
    //   299: aload_3
    //   300: areturn
    //   301: getstatic 24	cn/domob/wall/core/q:b	Lcn/domob/wall/core/p;
    //   304: astore 19
    //   306: iconst_1
    //   307: anewarray 4	java/lang/Object
    //   310: astore 20
    //   312: aload 20
    //   314: iconst_0
    //   315: aload 5
    //   317: invokeinterface 202 1 0
    //   322: invokestatic 84	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   325: aastore
    //   326: aload 19
    //   328: ldc 204
    //   330: aload 20
    //   332: invokestatic 189	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   335: invokevirtual 205	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   338: aload 5
    //   340: ifnull -41 -> 299
    //   343: aload 5
    //   345: invokeinterface 199 1 0
    //   350: aload_3
    //   351: areturn
    //   352: astore 6
    //   354: aconst_null
    //   355: astore 5
    //   357: aload 5
    //   359: ifnull +10 -> 369
    //   362: aload 5
    //   364: invokeinterface 199 1 0
    //   369: aload 6
    //   371: athrow
    //   372: astore 6
    //   374: goto -17 -> 357
    //   377: astore 4
    //   379: aconst_null
    //   380: astore 5
    //   382: goto -111 -> 271
    //
    // Exception table:
    //   from	to	target	type
    //   37	100	269	java/lang/Exception
    //   100	266	269	java/lang/Exception
    //   301	338	269	java/lang/Exception
    //   12	33	352	finally
    //   37	100	372	finally
    //   100	266	372	finally
    //   271	287	372	finally
    //   301	338	372	finally
    //   12	33	377	java/lang/Exception
  }

  void a(Context paramContext, List<b> paramList)
  {
    b.b("remove imps from the database which has been successfully sent");
    h localh = h.a();
    StringBuilder localStringBuilder = new StringBuilder();
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      String str = ((b)localIterator.next()).e();
      if (localStringBuilder.length() > 0)
        localStringBuilder.append(" or ");
      localStringBuilder.append("id");
      localStringBuilder.append(" = ? ");
      localArrayList.add(str);
    }
    localh.b("imp", localStringBuilder.toString(), (String[])localArrayList.toArray(new String[0]));
  }

  protected void a(k paramk, AdInfo paramAdInfo)
  {
    b.b("Database insert new imp info");
    try
    {
      h localh = h.a();
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("ad_id", paramAdInfo.getAdId());
      localContentValues.put("tr", paramAdInfo.d());
      localContentValues.put("sid", paramAdInfo.h());
      localContentValues.put("ppid", paramk.d());
      localh.a("imp", localContentValues);
      p localp = b;
      Object[] arrayOfObject = new Object[8];
      arrayOfObject[0] = "ad_id";
      arrayOfObject[1] = paramAdInfo.getAdId();
      arrayOfObject[2] = "tr";
      arrayOfObject[3] = paramAdInfo.d();
      arrayOfObject[4] = "sid";
      arrayOfObject[5] = paramAdInfo.h();
      arrayOfObject[6] = "ppid";
      arrayOfObject[7] = paramk.d();
      localp.a(String.format("succeed stored in the database   %s=%s   %s=%s   %s=%s", arrayOfObject));
      m.a().a(paramk, paramAdInfo);
      return;
    }
    catch (Exception localException)
    {
      while (true)
      {
        b.a(localException);
        b.e("Failed to insert alias info.");
      }
    }
  }

  class a
  {
    String a;
    JSONArray b;
    List<q.b> c;

    a()
    {
    }
  }

  class b
  {
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;

    b()
    {
    }

    String a()
    {
      return this.c;
    }

    void a(String paramString)
    {
      this.c = paramString;
    }

    String b()
    {
      return this.d;
    }

    void b(String paramString)
    {
      this.d = paramString;
    }

    String c()
    {
      return this.e;
    }

    void c(String paramString)
    {
      this.e = paramString;
    }

    String d()
    {
      return this.f;
    }

    void d(String paramString)
    {
      this.f = paramString;
    }

    String e()
    {
      return this.b;
    }

    void e(String paramString)
    {
      this.b = paramString;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.q
 * JD-Core Version:    0.6.0
 */