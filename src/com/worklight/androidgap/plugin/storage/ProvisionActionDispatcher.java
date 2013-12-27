package com.worklight.androidgap.plugin.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.worklight.androidgap.jsonstore.database.DatabaseAccessor;
import com.worklight.androidgap.jsonstore.database.DatabaseManager;
import com.worklight.androidgap.jsonstore.database.DatabaseSchema;
import com.worklight.androidgap.jsonstore.util.Logger;
import java.io.File;
import org.apache.cordova.api.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONObject;

public class ProvisionActionDispatcher extends BaseActionDispatcher
{
  private static final String OPTION_ADD_INDEXES = "additionalSearchFields";
  private static final String OPTION_DROP_COLLECTION = "dropCollection";
  private static final String OPTION_FIPS_ENABLED = "fipsEnabled";
  private static final String OPTION_PASSWORD = "collectionPassword";
  private static final String OPTION_USERNAME = "username";
  private static final String PARAM_DBNAME = "dbName";
  private static final String PARAM_OPTIONS = "options";
  private static final String PARAM_SCHEMA = "schema";

  protected ProvisionActionDispatcher()
  {
    super("provision");
    BaseActionDispatcher.ParameterType[] arrayOfParameterType1 = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType1[0] = BaseActionDispatcher.ParameterType.STRING;
    addParameter("dbName", true, arrayOfParameterType1);
    BaseActionDispatcher.ParameterType[] arrayOfParameterType2 = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType2[0] = BaseActionDispatcher.ParameterType.OBJECT;
    addParameter("schema", true, arrayOfParameterType2);
    BaseActionDispatcher.ParameterType[] arrayOfParameterType3 = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType3[0] = BaseActionDispatcher.ParameterType.OBJECT;
    addParameter("options", true, false, arrayOfParameterType3);
  }

  private void checkVersionMigration(Context paramContext)
    throws Throwable
  {
    SharedPreferences localSharedPreferences = paramContext.getSharedPreferences("JsonstorePrefs", 0);
    if (localSharedPreferences.getString("JsonstoreVer", null) == null)
    {
      this.logger.logInfo("Performing migration to JSONStore 2.0");
      File localFile1 = paramContext.getDatabasePath("wljsonstore");
      if ((!localFile1.exists()) && (!localFile1.mkdir()))
      {
        Exception localException2 = new Exception("Unable to create jsonstore directory");
        this.logger.logError("Unable to create jsonstore directory", localException2);
        throw localException2;
      }
      File localFile2 = paramContext.getDatabasePath("com.ibm.worklight.database");
      if (localFile2.exists())
      {
        if (localFile2.renameTo(new File(localFile1, "jsonstore.sqlite")))
          this.logger.logInfo("Migration to JSONStore 2.0 successful");
      }
      else
      {
        SharedPreferences.Editor localEditor = localSharedPreferences.edit();
        localEditor.putString("JsonstoreVer", "2.0");
        localEditor.commit();
      }
    }
    else
    {
      return;
    }
    Exception localException1 = new Exception("Unable to migrate existing JSONStore database to version 2.0");
    this.logger.logError("Unable to migrate existing JSONStore database to version 2.0", localException1);
    throw localException1;
  }

  private String getDatabaseName(BaseActionDispatcher.Context paramContext)
  {
    return paramContext.getStringParameter("dbName");
  }

  private JSONObject getOptions(BaseActionDispatcher.Context paramContext)
  {
    return paramContext.getObjectParameter("options");
  }

  private JSONObject getSchema(BaseActionDispatcher.Context paramContext, JSONObject paramJSONObject)
    throws Throwable
  {
    JSONObject localJSONObject = paramContext.getObjectParameter("schema");
    if (paramJSONObject != null)
    {
      JSONArray localJSONArray = paramJSONObject.names();
      if (localJSONArray != null)
      {
        int i = localJSONArray.length();
        for (int j = 0; j < i; j++)
        {
          String str = localJSONArray.getString(j);
          localJSONObject.put(str, paramJSONObject.getString(str));
        }
      }
    }
    return localJSONObject;
  }

  private boolean isSchemaMismatched(String paramString, DatabaseSchema paramDatabaseSchema, BaseActionDispatcher.Context paramContext)
  {
    DatabaseManager localDatabaseManager = DatabaseManager.getInstance();
    try
    {
      DatabaseAccessor localDatabaseAccessor = localDatabaseManager.getDatabase(paramString);
      if (!localDatabaseAccessor.getSchema().equals(paramDatabaseSchema))
        return true;
    }
    catch (Exception localException)
    {
      return localDatabaseManager.checkDatabaseAgainstSchema(paramContext.getCordovaContext().getActivity(), paramString, paramDatabaseSchema);
    }
    return false;
  }

  // ERROR //
  public org.apache.cordova.api.PluginResult dispatch(BaseActionDispatcher.Context paramContext)
    throws Throwable
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial 217	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher:getDatabaseName	(Lcom/worklight/androidgap/plugin/storage/BaseActionDispatcher$Context;)Ljava/lang/String;
    //   5: astore_2
    //   6: aload_0
    //   7: aload_1
    //   8: invokespecial 219	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher:getOptions	(Lcom/worklight/androidgap/plugin/storage/BaseActionDispatcher$Context;)Lorg/json/JSONObject;
    //   11: astore_3
    //   12: aconst_null
    //   13: astore 4
    //   15: iconst_0
    //   16: istore 5
    //   18: iconst_0
    //   19: istore 6
    //   21: aconst_null
    //   22: astore 7
    //   24: aconst_null
    //   25: astore 8
    //   27: aload_3
    //   28: ifnull +227 -> 255
    //   31: aload_3
    //   32: ldc 8
    //   34: invokevirtual 222	org/json/JSONObject:optJSONObject	(Ljava/lang/String;)Lorg/json/JSONObject;
    //   37: astore 4
    //   39: aload_3
    //   40: ldc 11
    //   42: iconst_0
    //   43: invokevirtual 226	org/json/JSONObject:optBoolean	(Ljava/lang/String;Z)Z
    //   46: istore 5
    //   48: aload_3
    //   49: ldc 14
    //   51: iconst_0
    //   52: invokevirtual 226	org/json/JSONObject:optBoolean	(Ljava/lang/String;Z)Z
    //   55: istore 6
    //   57: aload_3
    //   58: ldc 17
    //   60: ldc 228
    //   62: invokevirtual 231	org/json/JSONObject:optString	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   65: astore 7
    //   67: aload_3
    //   68: ldc 20
    //   70: ldc 233
    //   72: invokevirtual 231	org/json/JSONObject:optString	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   75: astore 8
    //   77: aload_0
    //   78: getfield 76	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher:logger	Lcom/worklight/androidgap/jsonstore/util/Logger;
    //   81: iconst_3
    //   82: invokevirtual 237	com/worklight/androidgap/jsonstore/util/Logger:isLoggable	(I)Z
    //   85: ifeq +170 -> 255
    //   88: aload_0
    //   89: getfield 76	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher:logger	Lcom/worklight/androidgap/jsonstore/util/Logger;
    //   92: new 239	java/lang/StringBuilder
    //   95: dup
    //   96: invokespecial 241	java/lang/StringBuilder:<init>	()V
    //   99: ldc 243
    //   101: invokevirtual 247	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   104: aload 4
    //   106: invokevirtual 250	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   109: invokevirtual 254	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   112: invokevirtual 257	com/worklight/androidgap/jsonstore/util/Logger:logDebug	(Ljava/lang/String;)V
    //   115: aload_0
    //   116: getfield 76	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher:logger	Lcom/worklight/androidgap/jsonstore/util/Logger;
    //   119: new 239	java/lang/StringBuilder
    //   122: dup
    //   123: invokespecial 241	java/lang/StringBuilder:<init>	()V
    //   126: ldc_w 259
    //   129: invokevirtual 247	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   132: iload 6
    //   134: invokevirtual 262	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
    //   137: invokevirtual 254	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   140: invokevirtual 257	com/worklight/androidgap/jsonstore/util/Logger:logDebug	(Ljava/lang/String;)V
    //   143: aload_0
    //   144: getfield 76	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher:logger	Lcom/worklight/androidgap/jsonstore/util/Logger;
    //   147: new 239	java/lang/StringBuilder
    //   150: dup
    //   151: invokespecial 241	java/lang/StringBuilder:<init>	()V
    //   154: ldc_w 264
    //   157: invokevirtual 247	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   160: iload 5
    //   162: invokevirtual 262	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
    //   165: invokevirtual 254	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   168: invokevirtual 257	com/worklight/androidgap/jsonstore/util/Logger:logDebug	(Ljava/lang/String;)V
    //   171: aload_0
    //   172: getfield 76	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher:logger	Lcom/worklight/androidgap/jsonstore/util/Logger;
    //   175: new 239	java/lang/StringBuilder
    //   178: dup
    //   179: invokespecial 241	java/lang/StringBuilder:<init>	()V
    //   182: ldc_w 266
    //   185: invokevirtual 247	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   188: aload 8
    //   190: invokevirtual 247	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   193: invokevirtual 254	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   196: invokevirtual 257	com/worklight/androidgap/jsonstore/util/Logger:logDebug	(Ljava/lang/String;)V
    //   199: aload_0
    //   200: getfield 76	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher:logger	Lcom/worklight/androidgap/jsonstore/util/Logger;
    //   203: astore 26
    //   205: new 239	java/lang/StringBuilder
    //   208: dup
    //   209: invokespecial 241	java/lang/StringBuilder:<init>	()V
    //   212: ldc_w 268
    //   215: invokevirtual 247	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   218: astore 27
    //   220: aload 7
    //   222: ifnull +13 -> 235
    //   225: ldc 228
    //   227: aload 7
    //   229: invokevirtual 271	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   232: ifeq +122 -> 354
    //   235: ldc_w 273
    //   238: astore 28
    //   240: aload 26
    //   242: aload 27
    //   244: aload 28
    //   246: invokevirtual 247	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   249: invokevirtual 254	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   252: invokevirtual 257	com/worklight/androidgap/jsonstore/util/Logger:logDebug	(Ljava/lang/String;)V
    //   255: new 193	com/worklight/androidgap/jsonstore/database/DatabaseSchema
    //   258: dup
    //   259: aload_2
    //   260: aload_0
    //   261: aload_1
    //   262: aload 4
    //   264: invokespecial 275	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher:getSchema	(Lcom/worklight/androidgap/plugin/storage/BaseActionDispatcher$Context;Lorg/json/JSONObject;)Lorg/json/JSONObject;
    //   267: invokespecial 278	com/worklight/androidgap/jsonstore/database/DatabaseSchema:<init>	(Ljava/lang/String;Lorg/json/JSONObject;)V
    //   270: astore 9
    //   272: aload_0
    //   273: aload_1
    //   274: invokevirtual 201	com/worklight/androidgap/plugin/storage/BaseActionDispatcher$Context:getCordovaContext	()Lorg/apache/cordova/api/CordovaInterface;
    //   277: invokeinterface 207 1 0
    //   282: invokespecial 280	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher:checkVersionMigration	(Landroid/content/Context;)V
    //   285: invokestatic 182	com/worklight/androidgap/jsonstore/database/DatabaseManager:getInstance	()Lcom/worklight/androidgap/jsonstore/database/DatabaseManager;
    //   288: astore 12
    //   290: aload 8
    //   292: ifnonnull +96 -> 388
    //   295: aload 12
    //   297: invokevirtual 283	com/worklight/androidgap/jsonstore/database/DatabaseManager:getDbPath	()Ljava/lang/String;
    //   300: ifnonnull +10 -> 310
    //   303: aload 12
    //   305: ldc 111
    //   307: invokevirtual 286	com/worklight/androidgap/jsonstore/database/DatabaseManager:setDbPath	(Ljava/lang/String;)V
    //   310: aload 12
    //   312: invokevirtual 283	com/worklight/androidgap/jsonstore/database/DatabaseManager:getDbPath	()Ljava/lang/String;
    //   315: ldc 111
    //   317: invokevirtual 290	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   320: ifne +83 -> 403
    //   323: new 215	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher$CloseAllException
    //   326: dup
    //   327: aload_0
    //   328: ldc_w 292
    //   331: invokespecial 295	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher$CloseAllException:<init>	(Lcom/worklight/androidgap/plugin/storage/ProvisionActionDispatcher;Ljava/lang/String;)V
    //   334: astore 13
    //   336: aload 13
    //   338: athrow
    //   339: astore 11
    //   341: new 297	org/apache/cordova/api/PluginResult
    //   344: dup
    //   345: getstatic 303	org/apache/cordova/api/PluginResult$Status:ERROR	Lorg/apache/cordova/api/PluginResult$Status;
    //   348: bipush 250
    //   350: invokespecial 306	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;I)V
    //   353: areturn
    //   354: ldc_w 308
    //   357: astore 28
    //   359: goto -119 -> 240
    //   362: astore 25
    //   364: aload_0
    //   365: getfield 76	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher:logger	Lcom/worklight/androidgap/jsonstore/util/Logger;
    //   368: ldc_w 310
    //   371: aload 25
    //   373: invokevirtual 107	com/worklight/androidgap/jsonstore/util/Logger:logError	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   376: new 297	org/apache/cordova/api/PluginResult
    //   379: dup
    //   380: getstatic 303	org/apache/cordova/api/PluginResult$Status:ERROR	Lorg/apache/cordova/api/PluginResult$Status;
    //   383: iconst_m1
    //   384: invokespecial 306	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;I)V
    //   387: areturn
    //   388: aload 12
    //   390: invokevirtual 283	com/worklight/androidgap/jsonstore/database/DatabaseManager:getDbPath	()Ljava/lang/String;
    //   393: ifnonnull +121 -> 514
    //   396: aload 12
    //   398: aload 8
    //   400: invokevirtual 286	com/worklight/androidgap/jsonstore/database/DatabaseManager:setDbPath	(Ljava/lang/String;)V
    //   403: aload 7
    //   405: ifnull +274 -> 679
    //   408: aload 7
    //   410: ldc 228
    //   412: invokevirtual 271	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   415: istore 14
    //   417: iload 14
    //   419: ifne +260 -> 679
    //   422: iload 6
    //   424: iconst_1
    //   425: if_icmpne +236 -> 661
    //   428: invokestatic 315	com/worklight/androidgap/jsonstore/security/FipsWrapper:getFipsMode	()I
    //   431: istore 22
    //   433: iload 22
    //   435: ifne +8 -> 443
    //   438: invokestatic 318	com/worklight/androidgap/jsonstore/security/FipsWrapper:enableFips	()I
    //   441: istore 22
    //   443: aload_0
    //   444: getfield 76	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher:logger	Lcom/worklight/androidgap/jsonstore/util/Logger;
    //   447: iconst_4
    //   448: invokevirtual 237	com/worklight/androidgap/jsonstore/util/Logger:isLoggable	(I)Z
    //   451: ifeq +31 -> 482
    //   454: aload_0
    //   455: getfield 76	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher:logger	Lcom/worklight/androidgap/jsonstore/util/Logger;
    //   458: new 239	java/lang/StringBuilder
    //   461: dup
    //   462: invokespecial 241	java/lang/StringBuilder:<init>	()V
    //   465: ldc_w 320
    //   468: invokevirtual 247	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   471: iload 22
    //   473: invokevirtual 323	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   476: invokevirtual 254	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   479: invokevirtual 83	com/worklight/androidgap/jsonstore/util/Logger:logInfo	(Ljava/lang/String;)V
    //   482: iload 22
    //   484: ifne +177 -> 661
    //   487: aload_0
    //   488: getfield 76	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher:logger	Lcom/worklight/androidgap/jsonstore/util/Logger;
    //   491: ldc_w 325
    //   494: invokevirtual 327	com/worklight/androidgap/jsonstore/util/Logger:logError	(Ljava/lang/String;)V
    //   497: new 297	org/apache/cordova/api/PluginResult
    //   500: dup
    //   501: getstatic 303	org/apache/cordova/api/PluginResult$Status:ERROR	Lorg/apache/cordova/api/PluginResult$Status;
    //   504: bipush 216
    //   506: invokespecial 306	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;I)V
    //   509: astore 23
    //   511: aload 23
    //   513: areturn
    //   514: aload 12
    //   516: invokevirtual 283	com/worklight/androidgap/jsonstore/database/DatabaseManager:getDbPath	()Ljava/lang/String;
    //   519: new 239	java/lang/StringBuilder
    //   522: dup
    //   523: invokespecial 241	java/lang/StringBuilder:<init>	()V
    //   526: aload 8
    //   528: invokevirtual 247	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   531: ldc_w 329
    //   534: invokevirtual 247	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   537: invokevirtual 254	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   540: invokevirtual 271	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   543: ifne -140 -> 403
    //   546: new 215	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher$CloseAllException
    //   549: dup
    //   550: aload_0
    //   551: new 239	java/lang/StringBuilder
    //   554: dup
    //   555: invokespecial 241	java/lang/StringBuilder:<init>	()V
    //   558: ldc_w 331
    //   561: invokevirtual 247	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   564: aload 12
    //   566: invokevirtual 283	com/worklight/androidgap/jsonstore/database/DatabaseManager:getDbPath	()Ljava/lang/String;
    //   569: invokevirtual 247	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   572: ldc_w 333
    //   575: invokevirtual 247	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   578: invokevirtual 254	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   581: invokespecial 295	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher$CloseAllException:<init>	(Lcom/worklight/androidgap/plugin/storage/ProvisionActionDispatcher;Ljava/lang/String;)V
    //   584: astore 24
    //   586: aload 24
    //   588: athrow
    //   589: astore 10
    //   591: aload_0
    //   592: getfield 76	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher:logger	Lcom/worklight/androidgap/jsonstore/util/Logger;
    //   595: ldc_w 335
    //   598: aload 10
    //   600: invokevirtual 107	com/worklight/androidgap/jsonstore/util/Logger:logError	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   603: aload 10
    //   605: invokevirtual 338	java/lang/Throwable:getMessage	()Ljava/lang/String;
    //   608: ldc_w 340
    //   611: invokevirtual 344	java/lang/String:contains	(Ljava/lang/CharSequence;)Z
    //   614: ifeq +184 -> 798
    //   617: new 297	org/apache/cordova/api/PluginResult
    //   620: dup
    //   621: getstatic 303	org/apache/cordova/api/PluginResult$Status:ERROR	Lorg/apache/cordova/api/PluginResult$Status;
    //   624: bipush 253
    //   626: invokespecial 306	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;I)V
    //   629: areturn
    //   630: astore 20
    //   632: aload_0
    //   633: getfield 76	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher:logger	Lcom/worklight/androidgap/jsonstore/util/Logger;
    //   636: ldc_w 346
    //   639: aload 20
    //   641: invokevirtual 107	com/worklight/androidgap/jsonstore/util/Logger:logError	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   644: new 297	org/apache/cordova/api/PluginResult
    //   647: dup
    //   648: getstatic 303	org/apache/cordova/api/PluginResult$Status:ERROR	Lorg/apache/cordova/api/PluginResult$Status;
    //   651: bipush 216
    //   653: invokespecial 306	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;I)V
    //   656: astore 21
    //   658: aload 21
    //   660: areturn
    //   661: aload 12
    //   663: aload_1
    //   664: invokevirtual 201	com/worklight/androidgap/plugin/storage/BaseActionDispatcher$Context:getCordovaContext	()Lorg/apache/cordova/api/CordovaInterface;
    //   667: invokeinterface 207 1 0
    //   672: aload 7
    //   674: aload 8
    //   676: invokevirtual 350	com/worklight/androidgap/jsonstore/database/DatabaseManager:setDatabaseKey	(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;)V
    //   679: iload 5
    //   681: ifne +54 -> 735
    //   684: aload_0
    //   685: aload_2
    //   686: aload 9
    //   688: aload_1
    //   689: invokespecial 352	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher:isSchemaMismatched	(Ljava/lang/String;Lcom/worklight/androidgap/jsonstore/database/DatabaseSchema;Lcom/worklight/androidgap/plugin/storage/BaseActionDispatcher$Context;)Z
    //   692: ifeq +43 -> 735
    //   695: new 297	org/apache/cordova/api/PluginResult
    //   698: dup
    //   699: getstatic 303	org/apache/cordova/api/PluginResult$Status:ERROR	Lorg/apache/cordova/api/PluginResult$Status;
    //   702: bipush 254
    //   704: invokespecial 306	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;I)V
    //   707: areturn
    //   708: astore 19
    //   710: aload_0
    //   711: getfield 76	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher:logger	Lcom/worklight/androidgap/jsonstore/util/Logger;
    //   714: ldc_w 354
    //   717: aload 19
    //   719: invokevirtual 107	com/worklight/androidgap/jsonstore/util/Logger:logError	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   722: new 297	org/apache/cordova/api/PluginResult
    //   725: dup
    //   726: getstatic 303	org/apache/cordova/api/PluginResult$Status:ERROR	Lorg/apache/cordova/api/PluginResult$Status;
    //   729: bipush 253
    //   731: invokespecial 306	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;I)V
    //   734: areturn
    //   735: aload 12
    //   737: aload_1
    //   738: invokevirtual 201	com/worklight/androidgap/plugin/storage/BaseActionDispatcher$Context:getCordovaContext	()Lorg/apache/cordova/api/CordovaInterface;
    //   741: invokeinterface 207 1 0
    //   746: aload 9
    //   748: iload 5
    //   750: invokevirtual 358	com/worklight/androidgap/jsonstore/database/DatabaseManager:provisionDatabase	(Landroid/content/Context;Lcom/worklight/androidgap/jsonstore/database/DatabaseSchema;Z)Z
    //   753: istore 15
    //   755: iload 15
    //   757: ifeq +23 -> 780
    //   760: iconst_1
    //   761: istore 17
    //   763: new 297	org/apache/cordova/api/PluginResult
    //   766: dup
    //   767: getstatic 361	org/apache/cordova/api/PluginResult$Status:OK	Lorg/apache/cordova/api/PluginResult$Status;
    //   770: iload 17
    //   772: invokespecial 306	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;I)V
    //   775: astore 18
    //   777: aload 18
    //   779: areturn
    //   780: aload 12
    //   782: aload_2
    //   783: invokevirtual 186	com/worklight/androidgap/jsonstore/database/DatabaseManager:getDatabase	(Ljava/lang/String;)Lcom/worklight/androidgap/jsonstore/database/DatabaseAccessor;
    //   786: invokeinterface 365 1 0
    //   791: pop
    //   792: iconst_0
    //   793: istore 17
    //   795: goto -32 -> 763
    //   798: new 297	org/apache/cordova/api/PluginResult
    //   801: dup
    //   802: getstatic 303	org/apache/cordova/api/PluginResult$Status:ERROR	Lorg/apache/cordova/api/PluginResult$Status;
    //   805: iconst_m1
    //   806: invokespecial 306	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;I)V
    //   809: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   272	290	339	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher$CloseAllException
    //   295	310	339	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher$CloseAllException
    //   310	339	339	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher$CloseAllException
    //   388	403	339	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher$CloseAllException
    //   408	417	339	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher$CloseAllException
    //   428	433	339	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher$CloseAllException
    //   438	443	339	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher$CloseAllException
    //   443	482	339	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher$CloseAllException
    //   487	511	339	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher$CloseAllException
    //   514	589	339	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher$CloseAllException
    //   632	658	339	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher$CloseAllException
    //   661	679	339	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher$CloseAllException
    //   684	708	339	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher$CloseAllException
    //   710	735	339	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher$CloseAllException
    //   735	755	339	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher$CloseAllException
    //   780	792	339	com/worklight/androidgap/plugin/storage/ProvisionActionDispatcher$CloseAllException
    //   255	272	362	java/lang/Throwable
    //   272	290	589	java/lang/Throwable
    //   295	310	589	java/lang/Throwable
    //   310	339	589	java/lang/Throwable
    //   388	403	589	java/lang/Throwable
    //   408	417	589	java/lang/Throwable
    //   514	589	589	java/lang/Throwable
    //   632	658	589	java/lang/Throwable
    //   684	708	589	java/lang/Throwable
    //   710	735	589	java/lang/Throwable
    //   735	755	589	java/lang/Throwable
    //   780	792	589	java/lang/Throwable
    //   428	433	630	java/lang/Throwable
    //   438	443	630	java/lang/Throwable
    //   443	482	630	java/lang/Throwable
    //   487	511	630	java/lang/Throwable
    //   661	679	708	java/lang/Throwable
  }

  class CloseAllException extends Exception
  {
    private static final long serialVersionUID = 1L;

    public CloseAllException()
    {
    }

    public CloseAllException(String arg2)
    {
      super();
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.ProvisionActionDispatcher
 * JD-Core Version:    0.6.0
 */