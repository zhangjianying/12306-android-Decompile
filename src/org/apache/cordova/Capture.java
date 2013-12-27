package org.apache.cordova;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Capture extends CordovaPlugin
{
  private static final String AUDIO_3GPP = "audio/3gpp";
  private static final int CAPTURE_AUDIO = 0;
  private static final int CAPTURE_IMAGE = 1;
  private static final int CAPTURE_INTERNAL_ERR = 0;
  private static final int CAPTURE_NO_MEDIA_FILES = 3;
  private static final int CAPTURE_VIDEO = 2;
  private static final String IMAGE_JPEG = "image/jpeg";
  private static final String LOG_TAG = "Capture";
  private static final String VIDEO_3GPP = "video/3gpp";
  private static final String VIDEO_MP4 = "video/mp4";
  private CallbackContext callbackContext;
  private double duration;
  private long limit;
  private int numPics;
  private JSONArray results;

  private void captureAudio()
  {
    Intent localIntent = new Intent("android.provider.MediaStore.RECORD_SOUND");
    this.cordova.startActivityForResult(this, localIntent, 0);
  }

  private void captureImage()
  {
    this.numPics = queryImgDB(whichContentStore()).getCount();
    Intent localIntent = new Intent("android.media.action.IMAGE_CAPTURE");
    localIntent.putExtra("output", Uri.fromFile(new File(DirectoryManager.getTempDirectoryPath(this.cordova.getActivity()), "Capture.jpg")));
    this.cordova.startActivityForResult(this, localIntent, 1);
  }

  private void captureVideo(double paramDouble)
  {
    Intent localIntent = new Intent("android.media.action.VIDEO_CAPTURE");
    if (Build.VERSION.SDK_INT > 8)
      localIntent.putExtra("android.intent.extra.durationLimit", paramDouble);
    this.cordova.startActivityForResult(this, localIntent, 2);
  }

  private void checkForDuplicateImage()
  {
    Uri localUri1 = whichContentStore();
    Cursor localCursor = queryImgDB(localUri1);
    if (localCursor.getCount() - this.numPics == 2)
    {
      localCursor.moveToLast();
      int i = -1 + Integer.valueOf(localCursor.getString(localCursor.getColumnIndex("_id"))).intValue();
      Uri localUri2 = Uri.parse(localUri1 + "/" + i);
      this.cordova.getActivity().getContentResolver().delete(localUri2, null, null);
    }
  }

  private JSONObject createErrorObject(int paramInt, String paramString)
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("code", paramInt);
      localJSONObject.put("message", paramString);
      return localJSONObject;
    }
    catch (JSONException localJSONException)
    {
    }
    return localJSONObject;
  }

  private JSONObject createMediaFile(Uri paramUri)
  {
    File localFile = new File(FileHelper.getRealPath(paramUri, this.cordova));
    JSONObject localJSONObject = new JSONObject();
    while (true)
    {
      try
      {
        localJSONObject.put("name", localFile.getName());
        localJSONObject.put("fullPath", "file://" + localFile.getAbsolutePath());
        if ((localFile.getAbsoluteFile().toString().endsWith(".3gp")) || (localFile.getAbsoluteFile().toString().endsWith(".3gpp")))
        {
          if (!paramUri.toString().contains("/audio/"))
            continue;
          localJSONObject.put("type", "audio/3gpp");
          localJSONObject.put("lastModifiedDate", localFile.lastModified());
          localJSONObject.put("size", localFile.length());
          return localJSONObject;
          localJSONObject.put("type", "video/3gpp");
          continue;
        }
      }
      catch (JSONException localJSONException)
      {
        localJSONException.printStackTrace();
        return localJSONObject;
      }
      localJSONObject.put("type", FileHelper.getMimeType(localFile.getAbsolutePath(), this.cordova));
    }
  }

  private JSONObject getAudioVideoData(String paramString, JSONObject paramJSONObject, boolean paramBoolean)
    throws JSONException
  {
    MediaPlayer localMediaPlayer = new MediaPlayer();
    try
    {
      localMediaPlayer.setDataSource(paramString);
      localMediaPlayer.prepare();
      paramJSONObject.put("duration", localMediaPlayer.getDuration() / 1000);
      if (paramBoolean)
      {
        paramJSONObject.put("height", localMediaPlayer.getVideoHeight());
        paramJSONObject.put("width", localMediaPlayer.getVideoWidth());
      }
      return paramJSONObject;
    }
    catch (IOException localIOException)
    {
      Log.d("Capture", "Error: loading video file");
    }
    return paramJSONObject;
  }

  private JSONObject getFormatData(String paramString1, String paramString2)
    throws JSONException
  {
    JSONObject localJSONObject = new JSONObject();
    localJSONObject.put("height", 0);
    localJSONObject.put("width", 0);
    localJSONObject.put("bitrate", 0);
    localJSONObject.put("duration", 0);
    localJSONObject.put("codecs", "");
    if ((paramString2 == null) || (paramString2.equals("")) || ("null".equals(paramString2)))
      paramString2 = FileHelper.getMimeType(paramString1, this.cordova);
    Log.d("Capture", "Mime type = " + paramString2);
    if ((paramString2.equals("image/jpeg")) || (paramString1.endsWith(".jpg")))
      localJSONObject = getImageData(paramString1, localJSONObject);
    do
    {
      return localJSONObject;
      if (paramString2.endsWith("audio/3gpp"))
        return getAudioVideoData(paramString1, localJSONObject, false);
    }
    while ((!paramString2.equals("video/3gpp")) && (!paramString2.equals("video/mp4")));
    return getAudioVideoData(paramString1, localJSONObject, true);
  }

  private JSONObject getImageData(String paramString, JSONObject paramJSONObject)
    throws JSONException
  {
    BitmapFactory.Options localOptions = new BitmapFactory.Options();
    localOptions.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(FileHelper.stripFileProtocol(paramString), localOptions);
    paramJSONObject.put("height", localOptions.outHeight);
    paramJSONObject.put("width", localOptions.outWidth);
    return paramJSONObject;
  }

  private Cursor queryImgDB(Uri paramUri)
  {
    return this.cordova.getActivity().getContentResolver().query(paramUri, new String[] { "_id" }, null, null, null);
  }

  private Uri whichContentStore()
  {
    if (Environment.getExternalStorageState().equals("mounted"))
      return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    return MediaStore.Images.Media.INTERNAL_CONTENT_URI;
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    this.callbackContext = paramCallbackContext;
    this.limit = 1L;
    this.duration = 0.0D;
    this.results = new JSONArray();
    JSONObject localJSONObject = paramJSONArray.optJSONObject(0);
    if (localJSONObject != null)
    {
      this.limit = localJSONObject.optLong("limit", 1L);
      this.duration = localJSONObject.optDouble("duration", 0.0D);
    }
    if (paramString.equals("getFormatData"))
    {
      paramCallbackContext.success(getFormatData(paramJSONArray.getString(0), paramJSONArray.getString(1)));
      return true;
    }
    if (paramString.equals("captureAudio"))
    {
      captureAudio();
      return true;
    }
    if (paramString.equals("captureImage"))
    {
      captureImage();
      return true;
    }
    if (paramString.equals("captureVideo"))
    {
      captureVideo(this.duration);
      return true;
    }
    return false;
  }

  public void fail(JSONObject paramJSONObject)
  {
    this.callbackContext.error(paramJSONObject);
  }

  // ERROR //
  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    // Byte code:
    //   0: iload_2
    //   1: iconst_m1
    //   2: if_icmpne +444 -> 446
    //   5: iload_1
    //   6: ifne +66 -> 72
    //   9: aload_3
    //   10: invokevirtual 424	android/content/Intent:getData	()Landroid/net/Uri;
    //   13: astore 18
    //   15: aload_0
    //   16: getfield 381	org/apache/cordova/Capture:results	Lorg/json/JSONArray;
    //   19: aload_0
    //   20: aload 18
    //   22: invokespecial 426	org/apache/cordova/Capture:createMediaFile	(Landroid/net/Uri;)Lorg/json/JSONObject;
    //   25: invokevirtual 429	org/json/JSONArray:put	(Ljava/lang/Object;)Lorg/json/JSONArray;
    //   28: pop
    //   29: aload_0
    //   30: getfield 381	org/apache/cordova/Capture:results	Lorg/json/JSONArray;
    //   33: invokevirtual 431	org/json/JSONArray:length	()I
    //   36: i2l
    //   37: aload_0
    //   38: getfield 374	org/apache/cordova/Capture:limit	J
    //   41: lcmp
    //   42: iflt +25 -> 67
    //   45: aload_0
    //   46: getfield 372	org/apache/cordova/Capture:callbackContext	Lorg/apache/cordova/api/CallbackContext;
    //   49: new 433	org/apache/cordova/api/PluginResult
    //   52: dup
    //   53: getstatic 439	org/apache/cordova/api/PluginResult$Status:OK	Lorg/apache/cordova/api/PluginResult$Status;
    //   56: aload_0
    //   57: getfield 381	org/apache/cordova/Capture:results	Lorg/json/JSONArray;
    //   60: invokespecial 442	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;Lorg/json/JSONArray;)V
    //   63: invokevirtual 446	org/apache/cordova/api/CallbackContext:sendPluginResult	(Lorg/apache/cordova/api/PluginResult;)V
    //   66: return
    //   67: aload_0
    //   68: invokespecial 407	org/apache/cordova/Capture:captureAudio	()V
    //   71: return
    //   72: iload_1
    //   73: iconst_1
    //   74: if_icmpne +300 -> 374
    //   77: new 448	android/content/ContentValues
    //   80: dup
    //   81: invokespecial 449	android/content/ContentValues:<init>	()V
    //   84: astore 6
    //   86: aload 6
    //   88: ldc_w 451
    //   91: ldc 21
    //   93: invokevirtual 453	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   96: aload_0
    //   97: getfield 55	org/apache/cordova/Capture:cordova	Lorg/apache/cordova/api/CordovaInterface;
    //   100: invokeinterface 88 1 0
    //   105: invokevirtual 176	android/app/Activity:getContentResolver	()Landroid/content/ContentResolver;
    //   108: getstatic 365	android/provider/MediaStore$Images$Media:EXTERNAL_CONTENT_URI	Landroid/net/Uri;
    //   111: aload 6
    //   113: invokevirtual 457	android/content/ContentResolver:insert	(Landroid/net/Uri;Landroid/content/ContentValues;)Landroid/net/Uri;
    //   116: astore 17
    //   118: aload 17
    //   120: astore 11
    //   122: new 459	java/io/FileInputStream
    //   125: dup
    //   126: new 149	java/lang/StringBuilder
    //   129: dup
    //   130: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   133: aload_0
    //   134: getfield 55	org/apache/cordova/Capture:cordova	Lorg/apache/cordova/api/CordovaInterface;
    //   137: invokeinterface 88 1 0
    //   142: invokestatic 94	org/apache/cordova/DirectoryManager:getTempDirectoryPath	(Landroid/content/Context;)Ljava/lang/String;
    //   145: invokevirtual 159	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   148: ldc_w 461
    //   151: invokevirtual 159	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   154: invokevirtual 166	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   157: invokespecial 462	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   160: astore 12
    //   162: aload_0
    //   163: getfield 55	org/apache/cordova/Capture:cordova	Lorg/apache/cordova/api/CordovaInterface;
    //   166: invokeinterface 88 1 0
    //   171: invokevirtual 176	android/app/Activity:getContentResolver	()Landroid/content/ContentResolver;
    //   174: aload 11
    //   176: invokevirtual 466	android/content/ContentResolver:openOutputStream	(Landroid/net/Uri;)Ljava/io/OutputStream;
    //   179: astore 13
    //   181: sipush 4096
    //   184: newarray byte
    //   186: astore 14
    //   188: aload 12
    //   190: aload 14
    //   192: invokevirtual 470	java/io/FileInputStream:read	([B)I
    //   195: istore 15
    //   197: iload 15
    //   199: iconst_m1
    //   200: if_icmpeq +98 -> 298
    //   203: aload 13
    //   205: aload 14
    //   207: iconst_0
    //   208: iload 15
    //   210: invokevirtual 476	java/io/OutputStream:write	([BII)V
    //   213: goto -25 -> 188
    //   216: astore 7
    //   218: aload 7
    //   220: invokevirtual 477	java/io/IOException:printStackTrace	()V
    //   223: aload_0
    //   224: aload_0
    //   225: iconst_0
    //   226: ldc_w 479
    //   229: invokespecial 481	org/apache/cordova/Capture:createErrorObject	(ILjava/lang/String;)Lorg/json/JSONObject;
    //   232: invokevirtual 483	org/apache/cordova/Capture:fail	(Lorg/json/JSONObject;)V
    //   235: return
    //   236: astore 8
    //   238: ldc 24
    //   240: ldc_w 485
    //   243: invokestatic 489	org/apache/cordova/api/LOG:d	(Ljava/lang/String;Ljava/lang/String;)V
    //   246: aload_0
    //   247: getfield 55	org/apache/cordova/Capture:cordova	Lorg/apache/cordova/api/CordovaInterface;
    //   250: invokeinterface 88 1 0
    //   255: invokevirtual 176	android/app/Activity:getContentResolver	()Landroid/content/ContentResolver;
    //   258: getstatic 368	android/provider/MediaStore$Images$Media:INTERNAL_CONTENT_URI	Landroid/net/Uri;
    //   261: aload 6
    //   263: invokevirtual 457	android/content/ContentResolver:insert	(Landroid/net/Uri;Landroid/content/ContentValues;)Landroid/net/Uri;
    //   266: astore 10
    //   268: aload 10
    //   270: astore 11
    //   272: goto -150 -> 122
    //   275: astore 9
    //   277: ldc 24
    //   279: ldc_w 491
    //   282: invokestatic 489	org/apache/cordova/api/LOG:d	(Ljava/lang/String;Ljava/lang/String;)V
    //   285: aload_0
    //   286: aload_0
    //   287: iconst_0
    //   288: ldc_w 493
    //   291: invokespecial 481	org/apache/cordova/Capture:createErrorObject	(ILjava/lang/String;)Lorg/json/JSONObject;
    //   294: invokevirtual 483	org/apache/cordova/Capture:fail	(Lorg/json/JSONObject;)V
    //   297: return
    //   298: aload 13
    //   300: invokevirtual 496	java/io/OutputStream:flush	()V
    //   303: aload 13
    //   305: invokevirtual 499	java/io/OutputStream:close	()V
    //   308: aload 12
    //   310: invokevirtual 500	java/io/FileInputStream:close	()V
    //   313: aload_0
    //   314: getfield 381	org/apache/cordova/Capture:results	Lorg/json/JSONArray;
    //   317: aload_0
    //   318: aload 11
    //   320: invokespecial 426	org/apache/cordova/Capture:createMediaFile	(Landroid/net/Uri;)Lorg/json/JSONObject;
    //   323: invokevirtual 429	org/json/JSONArray:put	(Ljava/lang/Object;)Lorg/json/JSONArray;
    //   326: pop
    //   327: aload_0
    //   328: invokespecial 502	org/apache/cordova/Capture:checkForDuplicateImage	()V
    //   331: aload_0
    //   332: getfield 381	org/apache/cordova/Capture:results	Lorg/json/JSONArray;
    //   335: invokevirtual 431	org/json/JSONArray:length	()I
    //   338: i2l
    //   339: aload_0
    //   340: getfield 374	org/apache/cordova/Capture:limit	J
    //   343: lcmp
    //   344: iflt +25 -> 369
    //   347: aload_0
    //   348: getfield 372	org/apache/cordova/Capture:callbackContext	Lorg/apache/cordova/api/CallbackContext;
    //   351: new 433	org/apache/cordova/api/PluginResult
    //   354: dup
    //   355: getstatic 439	org/apache/cordova/api/PluginResult$Status:OK	Lorg/apache/cordova/api/PluginResult$Status;
    //   358: aload_0
    //   359: getfield 381	org/apache/cordova/Capture:results	Lorg/json/JSONArray;
    //   362: invokespecial 442	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;Lorg/json/JSONArray;)V
    //   365: invokevirtual 446	org/apache/cordova/api/CallbackContext:sendPluginResult	(Lorg/apache/cordova/api/PluginResult;)V
    //   368: return
    //   369: aload_0
    //   370: invokespecial 410	org/apache/cordova/Capture:captureImage	()V
    //   373: return
    //   374: iload_1
    //   375: iconst_2
    //   376: if_icmpne -310 -> 66
    //   379: aload_3
    //   380: invokevirtual 424	android/content/Intent:getData	()Landroid/net/Uri;
    //   383: astore 4
    //   385: aload_0
    //   386: getfield 381	org/apache/cordova/Capture:results	Lorg/json/JSONArray;
    //   389: aload_0
    //   390: aload 4
    //   392: invokespecial 426	org/apache/cordova/Capture:createMediaFile	(Landroid/net/Uri;)Lorg/json/JSONObject;
    //   395: invokevirtual 429	org/json/JSONArray:put	(Ljava/lang/Object;)Lorg/json/JSONArray;
    //   398: pop
    //   399: aload_0
    //   400: getfield 381	org/apache/cordova/Capture:results	Lorg/json/JSONArray;
    //   403: invokevirtual 431	org/json/JSONArray:length	()I
    //   406: i2l
    //   407: aload_0
    //   408: getfield 374	org/apache/cordova/Capture:limit	J
    //   411: lcmp
    //   412: iflt +25 -> 437
    //   415: aload_0
    //   416: getfield 372	org/apache/cordova/Capture:callbackContext	Lorg/apache/cordova/api/CallbackContext;
    //   419: new 433	org/apache/cordova/api/PluginResult
    //   422: dup
    //   423: getstatic 439	org/apache/cordova/api/PluginResult$Status:OK	Lorg/apache/cordova/api/PluginResult$Status;
    //   426: aload_0
    //   427: getfield 381	org/apache/cordova/Capture:results	Lorg/json/JSONArray;
    //   430: invokespecial 442	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;Lorg/json/JSONArray;)V
    //   433: invokevirtual 446	org/apache/cordova/api/CallbackContext:sendPluginResult	(Lorg/apache/cordova/api/PluginResult;)V
    //   436: return
    //   437: aload_0
    //   438: aload_0
    //   439: getfield 376	org/apache/cordova/Capture:duration	D
    //   442: invokespecial 413	org/apache/cordova/Capture:captureVideo	(D)V
    //   445: return
    //   446: iload_2
    //   447: ifne +48 -> 495
    //   450: aload_0
    //   451: getfield 381	org/apache/cordova/Capture:results	Lorg/json/JSONArray;
    //   454: invokevirtual 431	org/json/JSONArray:length	()I
    //   457: ifle +25 -> 482
    //   460: aload_0
    //   461: getfield 372	org/apache/cordova/Capture:callbackContext	Lorg/apache/cordova/api/CallbackContext;
    //   464: new 433	org/apache/cordova/api/PluginResult
    //   467: dup
    //   468: getstatic 439	org/apache/cordova/api/PluginResult$Status:OK	Lorg/apache/cordova/api/PluginResult$Status;
    //   471: aload_0
    //   472: getfield 381	org/apache/cordova/Capture:results	Lorg/json/JSONArray;
    //   475: invokespecial 442	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;Lorg/json/JSONArray;)V
    //   478: invokevirtual 446	org/apache/cordova/api/CallbackContext:sendPluginResult	(Lorg/apache/cordova/api/PluginResult;)V
    //   481: return
    //   482: aload_0
    //   483: aload_0
    //   484: iconst_3
    //   485: ldc_w 504
    //   488: invokespecial 481	org/apache/cordova/Capture:createErrorObject	(ILjava/lang/String;)Lorg/json/JSONObject;
    //   491: invokevirtual 483	org/apache/cordova/Capture:fail	(Lorg/json/JSONObject;)V
    //   494: return
    //   495: aload_0
    //   496: getfield 381	org/apache/cordova/Capture:results	Lorg/json/JSONArray;
    //   499: invokevirtual 431	org/json/JSONArray:length	()I
    //   502: ifle +25 -> 527
    //   505: aload_0
    //   506: getfield 372	org/apache/cordova/Capture:callbackContext	Lorg/apache/cordova/api/CallbackContext;
    //   509: new 433	org/apache/cordova/api/PluginResult
    //   512: dup
    //   513: getstatic 439	org/apache/cordova/api/PluginResult$Status:OK	Lorg/apache/cordova/api/PluginResult$Status;
    //   516: aload_0
    //   517: getfield 381	org/apache/cordova/Capture:results	Lorg/json/JSONArray;
    //   520: invokespecial 442	org/apache/cordova/api/PluginResult:<init>	(Lorg/apache/cordova/api/PluginResult$Status;Lorg/json/JSONArray;)V
    //   523: invokevirtual 446	org/apache/cordova/api/CallbackContext:sendPluginResult	(Lorg/apache/cordova/api/PluginResult;)V
    //   526: return
    //   527: aload_0
    //   528: aload_0
    //   529: iconst_3
    //   530: ldc_w 506
    //   533: invokespecial 481	org/apache/cordova/Capture:createErrorObject	(ILjava/lang/String;)Lorg/json/JSONObject;
    //   536: invokevirtual 483	org/apache/cordova/Capture:fail	(Lorg/json/JSONObject;)V
    //   539: return
    //
    // Exception table:
    //   from	to	target	type
    //   77	96	216	java/io/IOException
    //   96	118	216	java/io/IOException
    //   122	188	216	java/io/IOException
    //   188	197	216	java/io/IOException
    //   203	213	216	java/io/IOException
    //   238	246	216	java/io/IOException
    //   246	268	216	java/io/IOException
    //   277	297	216	java/io/IOException
    //   298	368	216	java/io/IOException
    //   369	373	216	java/io/IOException
    //   96	118	236	java/lang/UnsupportedOperationException
    //   246	268	275	java/lang/UnsupportedOperationException
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.Capture
 * JD-Core Version:    0.6.0
 */