package cn.domob.wall.core;

import android.content.ContentValues;
import java.io.File;

public class t
{
  static t a;
  private static p b = new p(t.class.getSimpleName());

  static t a()
  {
    monitorenter;
    try
    {
      if (a == null)
        a = new t();
      t localt = a;
      return localt;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  // ERROR //
  private int f()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: invokestatic 39	cn/domob/wall/core/h:a	()Lcn/domob/wall/core/h;
    //   5: astore_2
    //   6: aload_2
    //   7: ldc 41
    //   9: iconst_1
    //   10: anewarray 43	java/lang/String
    //   13: dup
    //   14: iconst_0
    //   15: ldc 45
    //   17: aastore
    //   18: aconst_null
    //   19: aconst_null
    //   20: aconst_null
    //   21: invokevirtual 48	cn/domob/wall/core/h:a	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   24: astore 9
    //   26: aload 9
    //   28: astore 5
    //   30: aload 5
    //   32: invokeinterface 54 1 0
    //   37: ifeq +130 -> 167
    //   40: aload 5
    //   42: iconst_0
    //   43: invokeinterface 58 2 0
    //   48: istore 10
    //   50: iload 10
    //   52: istore 6
    //   54: aload 5
    //   56: ifnull +10 -> 66
    //   59: aload 5
    //   61: invokeinterface 61 1 0
    //   66: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   69: astore 7
    //   71: iconst_1
    //   72: anewarray 4	java/lang/Object
    //   75: astore 8
    //   77: aload 8
    //   79: iconst_0
    //   80: iload 6
    //   82: sipush 1024
    //   85: idiv
    //   86: invokestatic 67	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   89: aastore
    //   90: aload 7
    //   92: ldc 69
    //   94: aload 8
    //   96: invokestatic 73	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   99: invokevirtual 75	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   102: iload 6
    //   104: ireturn
    //   105: astore 4
    //   107: aconst_null
    //   108: astore 5
    //   110: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   113: aload 4
    //   115: invokevirtual 78	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   118: aload 5
    //   120: ifnull +41 -> 161
    //   123: aload 5
    //   125: invokeinterface 61 1 0
    //   130: iconst_0
    //   131: istore 6
    //   133: goto -67 -> 66
    //   136: astore_3
    //   137: aload_1
    //   138: ifnull +9 -> 147
    //   141: aload_1
    //   142: invokeinterface 61 1 0
    //   147: aload_3
    //   148: athrow
    //   149: astore_3
    //   150: aload 5
    //   152: astore_1
    //   153: goto -16 -> 137
    //   156: astore 4
    //   158: goto -48 -> 110
    //   161: iconst_0
    //   162: istore 6
    //   164: goto -98 -> 66
    //   167: iconst_0
    //   168: istore 6
    //   170: goto -116 -> 54
    //
    // Exception table:
    //   from	to	target	type
    //   6	26	105	java/lang/Exception
    //   6	26	136	finally
    //   30	50	149	finally
    //   110	118	149	finally
    //   30	50	156	java/lang/Exception
  }

  // ERROR //
  protected String a(String paramString)
  {
    // Byte code:
    //   0: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   3: ldc 81
    //   5: iconst_1
    //   6: anewarray 4	java/lang/Object
    //   9: dup
    //   10: iconst_0
    //   11: aload_1
    //   12: aastore
    //   13: invokestatic 73	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   16: invokevirtual 75	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   19: invokestatic 39	cn/domob/wall/core/h:a	()Lcn/domob/wall/core/h;
    //   22: astore_2
    //   23: aload_2
    //   24: ldc 41
    //   26: aconst_null
    //   27: ldc 83
    //   29: iconst_1
    //   30: anewarray 43	java/lang/String
    //   33: dup
    //   34: iconst_0
    //   35: aload_1
    //   36: aastore
    //   37: aconst_null
    //   38: invokevirtual 48	cn/domob/wall/core/h:a	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   41: astore 6
    //   43: aload 6
    //   45: astore 4
    //   47: aload 4
    //   49: ifnull +90 -> 139
    //   52: aload 4
    //   54: invokeinterface 86 1 0
    //   59: ifle +80 -> 139
    //   62: aload 4
    //   64: invokeinterface 89 1 0
    //   69: pop
    //   70: aload 4
    //   72: aload 4
    //   74: ldc 91
    //   76: invokeinterface 95 2 0
    //   81: invokeinterface 99 2 0
    //   86: astore 8
    //   88: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   91: new 101	java/lang/StringBuilder
    //   94: dup
    //   95: invokespecial 102	java/lang/StringBuilder:<init>	()V
    //   98: ldc 104
    //   100: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   103: aload 8
    //   105: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   108: invokevirtual 111	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   111: invokevirtual 75	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   114: aload 4
    //   116: ifnull +20 -> 136
    //   119: aload 4
    //   121: invokeinterface 114 1 0
    //   126: ifne +10 -> 136
    //   129: aload 4
    //   131: invokeinterface 61 1 0
    //   136: aload 8
    //   138: areturn
    //   139: aload 4
    //   141: ifnull +20 -> 161
    //   144: aload 4
    //   146: invokeinterface 114 1 0
    //   151: ifne +10 -> 161
    //   154: aload 4
    //   156: invokeinterface 61 1 0
    //   161: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   164: ldc 116
    //   166: invokevirtual 75	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   169: aconst_null
    //   170: areturn
    //   171: astore 5
    //   173: aconst_null
    //   174: astore 4
    //   176: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   179: aload 5
    //   181: invokevirtual 78	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   184: aload 4
    //   186: ifnull -25 -> 161
    //   189: aload 4
    //   191: invokeinterface 114 1 0
    //   196: ifne -35 -> 161
    //   199: aload 4
    //   201: invokeinterface 61 1 0
    //   206: goto -45 -> 161
    //   209: astore_3
    //   210: aconst_null
    //   211: astore 4
    //   213: aload 4
    //   215: ifnull +20 -> 235
    //   218: aload 4
    //   220: invokeinterface 114 1 0
    //   225: ifne +10 -> 235
    //   228: aload 4
    //   230: invokeinterface 61 1 0
    //   235: aload_3
    //   236: athrow
    //   237: astore_3
    //   238: goto -25 -> 213
    //   241: astore 5
    //   243: goto -67 -> 176
    //
    // Exception table:
    //   from	to	target	type
    //   23	43	171	java/lang/Exception
    //   23	43	209	finally
    //   52	114	237	finally
    //   176	184	237	finally
    //   52	114	241	java/lang/Exception
  }

  // ERROR //
  protected void a(int paramInt)
  {
    // Byte code:
    //   0: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   3: astore_2
    //   4: iconst_1
    //   5: anewarray 4	java/lang/Object
    //   8: astore_3
    //   9: aload_3
    //   10: iconst_0
    //   11: iload_1
    //   12: invokestatic 67	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   15: aastore
    //   16: aload_2
    //   17: ldc 119
    //   19: aload_3
    //   20: invokestatic 73	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   23: invokevirtual 75	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   26: invokestatic 39	cn/domob/wall/core/h:a	()Lcn/domob/wall/core/h;
    //   29: astore 4
    //   31: aload 4
    //   33: ldc 41
    //   35: iconst_2
    //   36: anewarray 43	java/lang/String
    //   39: dup
    //   40: iconst_0
    //   41: ldc 91
    //   43: aastore
    //   44: dup
    //   45: iconst_1
    //   46: ldc 121
    //   48: aastore
    //   49: aconst_null
    //   50: aconst_null
    //   51: ldc 123
    //   53: invokevirtual 48	cn/domob/wall/core/h:a	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   56: astore 8
    //   58: aload 8
    //   60: astore 6
    //   62: aload 6
    //   64: ifnull +241 -> 305
    //   67: aload 6
    //   69: ldc 91
    //   71: invokeinterface 95 2 0
    //   76: istore 9
    //   78: aload 6
    //   80: ldc 121
    //   82: invokeinterface 95 2 0
    //   87: istore 10
    //   89: aload 6
    //   91: invokeinterface 89 1 0
    //   96: pop
    //   97: iconst_0
    //   98: istore 12
    //   100: aload 6
    //   102: invokeinterface 126 1 0
    //   107: ifne +134 -> 241
    //   110: aload 6
    //   112: invokeinterface 129 1 0
    //   117: iload_1
    //   118: if_icmpge +123 -> 241
    //   121: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   124: new 101	java/lang/StringBuilder
    //   127: dup
    //   128: invokespecial 102	java/lang/StringBuilder:<init>	()V
    //   131: ldc 131
    //   133: invokevirtual 108	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   136: aload 6
    //   138: invokeinterface 129 1 0
    //   143: invokevirtual 134	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   146: invokevirtual 111	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   149: invokevirtual 75	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   152: aload 6
    //   154: iload 9
    //   156: invokeinterface 99 2 0
    //   161: astore 16
    //   163: aload 6
    //   165: iload 10
    //   167: invokeinterface 99 2 0
    //   172: astore 17
    //   174: aload_0
    //   175: aload 16
    //   177: invokevirtual 137	cn/domob/wall/core/t:e	(Ljava/lang/String;)V
    //   180: aload_0
    //   181: aload 17
    //   183: invokevirtual 138	cn/domob/wall/core/t:b	(Ljava/lang/String;)V
    //   186: iinc 12 1
    //   189: aload 6
    //   191: invokeinterface 54 1 0
    //   196: pop
    //   197: goto -97 -> 100
    //   200: astore 5
    //   202: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   205: aload 5
    //   207: invokevirtual 78	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   210: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   213: ldc 140
    //   215: invokevirtual 141	cn/domob/wall/core/p:e	(Ljava/lang/String;)V
    //   218: aload 6
    //   220: ifnull +20 -> 240
    //   223: aload 6
    //   225: invokeinterface 114 1 0
    //   230: ifne +10 -> 240
    //   233: aload 6
    //   235: invokeinterface 61 1 0
    //   240: return
    //   241: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   244: astore 13
    //   246: iconst_1
    //   247: anewarray 4	java/lang/Object
    //   250: astore 14
    //   252: aload 14
    //   254: iconst_0
    //   255: iload 12
    //   257: invokestatic 67	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   260: aastore
    //   261: aload 13
    //   263: ldc 143
    //   265: aload 14
    //   267: invokestatic 73	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   270: invokevirtual 75	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   273: invokestatic 39	cn/domob/wall/core/h:a	()Lcn/domob/wall/core/h;
    //   276: ldc 41
    //   278: invokevirtual 145	cn/domob/wall/core/h:a	(Ljava/lang/String;)I
    //   281: pop
    //   282: aload 6
    //   284: ifnull -44 -> 240
    //   287: aload 6
    //   289: invokeinterface 114 1 0
    //   294: ifne -54 -> 240
    //   297: aload 6
    //   299: invokeinterface 61 1 0
    //   304: return
    //   305: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   308: ldc 147
    //   310: invokevirtual 141	cn/domob/wall/core/p:e	(Ljava/lang/String;)V
    //   313: goto -31 -> 282
    //   316: astore 7
    //   318: aload 6
    //   320: ifnull +20 -> 340
    //   323: aload 6
    //   325: invokeinterface 114 1 0
    //   330: ifne +10 -> 340
    //   333: aload 6
    //   335: invokeinterface 61 1 0
    //   340: aload 7
    //   342: athrow
    //   343: astore 7
    //   345: aconst_null
    //   346: astore 6
    //   348: goto -30 -> 318
    //   351: astore 5
    //   353: aconst_null
    //   354: astore 6
    //   356: goto -154 -> 202
    //
    // Exception table:
    //   from	to	target	type
    //   67	97	200	java/lang/Exception
    //   100	186	200	java/lang/Exception
    //   189	197	200	java/lang/Exception
    //   241	282	200	java/lang/Exception
    //   305	313	200	java/lang/Exception
    //   67	97	316	finally
    //   100	186	316	finally
    //   189	197	316	finally
    //   202	218	316	finally
    //   241	282	316	finally
    //   305	313	316	finally
    //   31	58	343	finally
    //   31	58	351	java/lang/Exception
  }

  protected void a(String paramString1, String paramString2, int paramInt)
  {
    if (!v.g(paramString1));
    try
    {
      h localh = h.a();
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("url", paramString1);
      localContentValues.put("local_path", paramString2);
      localContentValues.put("lastUseTime", Long.valueOf(System.currentTimeMillis()));
      localContentValues.put("size", Integer.valueOf(paramInt));
      localh.a("image", localContentValues);
      b.b("Picture taken from the server stored in the database successfully");
      return;
    }
    catch (Exception localException)
    {
      b.a(localException);
      b.e("Failed to insert image info.");
    }
  }

  protected void b()
  {
    b.b("Cache management for image resources");
    c();
    d();
  }

  void b(String paramString)
  {
    b.b(String.format(" deleted url = ? records from the database url = %s", new Object[] { paramString }));
    h localh = h.a();
    String[] arrayOfString = new String[1];
    arrayOfString[0] = String.valueOf(paramString);
    localh.b("image", "url = ?", arrayOfString);
  }

  // ERROR //
  protected void c()
  {
    // Byte code:
    //   0: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   3: ldc 207
    //   5: invokevirtual 75	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   8: invokestatic 39	cn/domob/wall/core/h:a	()Lcn/domob/wall/core/h;
    //   11: astore_1
    //   12: iconst_2
    //   13: anewarray 43	java/lang/String
    //   16: dup
    //   17: iconst_0
    //   18: ldc 91
    //   20: aastore
    //   21: dup
    //   22: iconst_1
    //   23: ldc 121
    //   25: aastore
    //   26: astore_2
    //   27: iconst_1
    //   28: anewarray 43	java/lang/String
    //   31: astore 7
    //   33: aload 7
    //   35: iconst_0
    //   36: invokestatic 169	java/lang/System:currentTimeMillis	()J
    //   39: ldc2_w 208
    //   42: lsub
    //   43: invokestatic 212	java/lang/String:valueOf	(J)Ljava/lang/String;
    //   46: aastore
    //   47: aload_1
    //   48: ldc 41
    //   50: aload_2
    //   51: ldc 214
    //   53: aload 7
    //   55: aconst_null
    //   56: invokevirtual 48	cn/domob/wall/core/h:a	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   59: astore 8
    //   61: aload 8
    //   63: astore 4
    //   65: aload 4
    //   67: ifnull +194 -> 261
    //   70: aload 4
    //   72: ldc 121
    //   74: invokeinterface 95 2 0
    //   79: istore 9
    //   81: aload 4
    //   83: ldc 91
    //   85: invokeinterface 95 2 0
    //   90: istore 10
    //   92: aload 4
    //   94: invokeinterface 89 1 0
    //   99: pop
    //   100: aload 4
    //   102: invokeinterface 126 1 0
    //   107: ifne +92 -> 199
    //   110: aload 4
    //   112: iload 10
    //   114: invokeinterface 99 2 0
    //   119: astore 14
    //   121: aload_0
    //   122: aload 4
    //   124: iload 9
    //   126: invokeinterface 99 2 0
    //   131: invokevirtual 138	cn/domob/wall/core/t:b	(Ljava/lang/String;)V
    //   134: aload_0
    //   135: aload 14
    //   137: invokevirtual 137	cn/domob/wall/core/t:e	(Ljava/lang/String;)V
    //   140: aload 4
    //   142: invokeinterface 54 1 0
    //   147: pop
    //   148: goto -48 -> 100
    //   151: astore_3
    //   152: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   155: aload_3
    //   156: invokevirtual 78	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   159: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   162: ldc 140
    //   164: invokevirtual 141	cn/domob/wall/core/p:e	(Ljava/lang/String;)V
    //   167: aload 4
    //   169: ifnull +20 -> 189
    //   172: aload 4
    //   174: invokeinterface 114 1 0
    //   179: ifne +10 -> 189
    //   182: aload 4
    //   184: invokeinterface 61 1 0
    //   189: invokestatic 39	cn/domob/wall/core/h:a	()Lcn/domob/wall/core/h;
    //   192: ldc 41
    //   194: invokevirtual 145	cn/domob/wall/core/h:a	(Ljava/lang/String;)I
    //   197: pop
    //   198: return
    //   199: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   202: astore 12
    //   204: iconst_1
    //   205: anewarray 4	java/lang/Object
    //   208: astore 13
    //   210: aload 13
    //   212: iconst_0
    //   213: aload 4
    //   215: invokeinterface 129 1 0
    //   220: invokestatic 67	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   223: aastore
    //   224: aload 12
    //   226: ldc 216
    //   228: aload 13
    //   230: invokestatic 73	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   233: invokevirtual 218	cn/domob/wall/core/p:a	(Ljava/lang/String;)V
    //   236: aload 4
    //   238: ifnull -49 -> 189
    //   241: aload 4
    //   243: invokeinterface 114 1 0
    //   248: ifne -59 -> 189
    //   251: aload 4
    //   253: invokeinterface 61 1 0
    //   258: goto -69 -> 189
    //   261: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   264: ldc 220
    //   266: invokevirtual 75	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   269: goto -33 -> 236
    //   272: astore 5
    //   274: aload 4
    //   276: ifnull +20 -> 296
    //   279: aload 4
    //   281: invokeinterface 114 1 0
    //   286: ifne +10 -> 296
    //   289: aload 4
    //   291: invokeinterface 61 1 0
    //   296: aload 5
    //   298: athrow
    //   299: astore 5
    //   301: aconst_null
    //   302: astore 4
    //   304: goto -30 -> 274
    //   307: astore_3
    //   308: aconst_null
    //   309: astore 4
    //   311: goto -159 -> 152
    //
    // Exception table:
    //   from	to	target	type
    //   70	100	151	java/lang/Exception
    //   100	148	151	java/lang/Exception
    //   199	236	151	java/lang/Exception
    //   261	269	151	java/lang/Exception
    //   70	100	272	finally
    //   100	148	272	finally
    //   152	167	272	finally
    //   199	236	272	finally
    //   261	269	272	finally
    //   12	61	299	finally
    //   12	61	307	java/lang/Exception
  }

  void c(String paramString)
  {
    b.b(String.format("Update the database url = ? pictures resources last used timestamp url = %s", new Object[] { paramString }));
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("lastUseTime", Long.valueOf(System.currentTimeMillis()));
    h localh = h.a();
    String[] arrayOfString = new String[1];
    arrayOfString[0] = String.valueOf(paramString);
    localh.a("image", localContentValues, "url = ?", arrayOfString);
  }

  void d()
  {
    b.b("Adjust the pictures size of the space");
    int i = 0;
    while (e())
    {
      a(2);
      i++;
      if (i <= 100)
        continue;
      p localp = b;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(100);
      localp.d(String.format("More than %s times the cycle frequency and to delete pictures", arrayOfObject));
    }
  }

  // ERROR //
  protected void d(String paramString)
  {
    // Byte code:
    //   0: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   3: ldc 237
    //   5: iconst_1
    //   6: anewarray 4	java/lang/Object
    //   9: dup
    //   10: iconst_0
    //   11: aload_1
    //   12: aastore
    //   13: invokestatic 73	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   16: invokevirtual 75	cn/domob/wall/core/p:b	(Ljava/lang/String;)V
    //   19: invokestatic 39	cn/domob/wall/core/h:a	()Lcn/domob/wall/core/h;
    //   22: astore_2
    //   23: aload_2
    //   24: ldc 41
    //   26: iconst_1
    //   27: anewarray 43	java/lang/String
    //   30: dup
    //   31: iconst_0
    //   32: ldc 91
    //   34: aastore
    //   35: ldc 83
    //   37: iconst_1
    //   38: anewarray 43	java/lang/String
    //   41: dup
    //   42: iconst_0
    //   43: aload_1
    //   44: aastore
    //   45: aconst_null
    //   46: invokevirtual 48	cn/domob/wall/core/h:a	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   49: astore 6
    //   51: aload 6
    //   53: astore 4
    //   55: aload 4
    //   57: ifnull +100 -> 157
    //   60: aload 4
    //   62: ldc 91
    //   64: invokeinterface 95 2 0
    //   69: istore 7
    //   71: aload 4
    //   73: invokeinterface 89 1 0
    //   78: pop
    //   79: aload 4
    //   81: invokeinterface 126 1 0
    //   86: ifne +90 -> 176
    //   89: aload_0
    //   90: aload 4
    //   92: iload 7
    //   94: invokeinterface 99 2 0
    //   99: invokevirtual 137	cn/domob/wall/core/t:e	(Ljava/lang/String;)V
    //   102: aload_0
    //   103: aload_1
    //   104: invokevirtual 138	cn/domob/wall/core/t:b	(Ljava/lang/String;)V
    //   107: aload 4
    //   109: invokeinterface 54 1 0
    //   114: pop
    //   115: goto -36 -> 79
    //   118: astore_3
    //   119: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   122: aload_3
    //   123: invokevirtual 78	cn/domob/wall/core/p:a	(Ljava/lang/Throwable;)V
    //   126: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   129: ldc 140
    //   131: invokevirtual 141	cn/domob/wall/core/p:e	(Ljava/lang/String;)V
    //   134: aload 4
    //   136: ifnull +20 -> 156
    //   139: aload 4
    //   141: invokeinterface 114 1 0
    //   146: ifne +10 -> 156
    //   149: aload 4
    //   151: invokeinterface 61 1 0
    //   156: return
    //   157: getstatic 24	cn/domob/wall/core/t:b	Lcn/domob/wall/core/p;
    //   160: ldc 239
    //   162: iconst_1
    //   163: anewarray 4	java/lang/Object
    //   166: dup
    //   167: iconst_0
    //   168: aload_1
    //   169: aastore
    //   170: invokestatic 73	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   173: invokevirtual 235	cn/domob/wall/core/p:d	(Ljava/lang/String;)V
    //   176: aload 4
    //   178: ifnull -22 -> 156
    //   181: aload 4
    //   183: invokeinterface 114 1 0
    //   188: ifne -32 -> 156
    //   191: aload 4
    //   193: invokeinterface 61 1 0
    //   198: return
    //   199: astore 5
    //   201: aconst_null
    //   202: astore 4
    //   204: aload 4
    //   206: ifnull +20 -> 226
    //   209: aload 4
    //   211: invokeinterface 114 1 0
    //   216: ifne +10 -> 226
    //   219: aload 4
    //   221: invokeinterface 61 1 0
    //   226: aload 5
    //   228: athrow
    //   229: astore 5
    //   231: goto -27 -> 204
    //   234: astore_3
    //   235: aconst_null
    //   236: astore 4
    //   238: goto -119 -> 119
    //
    // Exception table:
    //   from	to	target	type
    //   60	79	118	java/lang/Exception
    //   79	115	118	java/lang/Exception
    //   157	176	118	java/lang/Exception
    //   23	51	199	finally
    //   60	79	229	finally
    //   79	115	229	finally
    //   119	134	229	finally
    //   157	176	229	finally
    //   23	51	234	java/lang/Exception
  }

  protected void e(String paramString)
  {
    try
    {
      b.a("try to delete file:" + paramString);
      if ((paramString != null) && (paramString.length() != 0))
      {
        File localFile = new File(paramString);
        if (localFile.exists())
        {
          if (localFile.delete())
          {
            b.d("Success to delete file " + paramString);
            return;
          }
          b.e("Failed to delete file " + paramString);
          return;
        }
      }
    }
    catch (Exception localException)
    {
      b.e("Error happened when deleting file " + paramString);
      b.a(localException);
    }
  }

  boolean e()
  {
    int i = f();
    if (i > 512000);
    for (int j = 1; j != 0; j = 0)
    {
      p localp2 = b;
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = Integer.valueOf(i / 1024);
      arrayOfObject2[1] = Integer.valueOf(500);
      localp2.d(String.format("Check local storage of image resources the space occupied by the sum of %s KB exceeds the maximum limit value %s KB", arrayOfObject2));
      return j;
    }
    p localp1 = b;
    Object[] arrayOfObject1 = new Object[2];
    arrayOfObject1[0] = Integer.valueOf(i / 1024);
    arrayOfObject1[1] = Integer.valueOf(500);
    localp1.d(String.format("Check local storage of image resources the space occupied by the sum of %s KB  does not exceeds the maximum limit value %s KB", arrayOfObject1));
    return j;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.t
 * JD-Core Version:    0.6.0
 */