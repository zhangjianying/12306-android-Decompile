package org.apache.cordova;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.LOG;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;

public class CameraLauncher extends CordovaPlugin
  implements MediaScannerConnection.MediaScannerConnectionClient
{
  private static final int ALLMEDIA = 2;
  private static final int CAMERA = 1;
  private static final int DATA_URL = 0;
  private static final int FILE_URI = 1;
  private static final String GET_All = "Get All";
  private static final String GET_PICTURE = "Get Picture";
  private static final String GET_VIDEO = "Get Video";
  private static final int JPEG = 0;
  private static final String LOG_TAG = "CameraLauncher";
  private static final int NATIVE_URI = 2;
  private static final int PHOTOLIBRARY = 0;
  private static final int PICTURE = 0;
  private static final int PNG = 1;
  private static final int SAVEDPHOTOALBUM = 2;
  private static final int VIDEO = 1;
  public CallbackContext callbackContext;
  private MediaScannerConnection conn;
  private boolean correctOrientation;
  private int encodingType;
  private Uri imageUri;
  private int mQuality;
  private int mediaType;
  private int numPics;
  private boolean saveToPhotoAlbum;
  private Uri scanMe;
  private int targetHeight;
  private int targetWidth;

  public static int calculateSampleSize(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (paramInt1 / paramInt2 > paramInt3 / paramInt4)
      return paramInt1 / paramInt3;
    return paramInt2 / paramInt4;
  }

  private void checkForDuplicateImage(int paramInt)
  {
    int i = 1;
    Uri localUri1 = whichContentStore();
    Cursor localCursor = queryImgDB(localUri1);
    int j = localCursor.getCount();
    if ((paramInt == 1) && (this.saveToPhotoAlbum))
      i = 2;
    if (j - this.numPics == i)
    {
      localCursor.moveToLast();
      int k = Integer.valueOf(localCursor.getString(localCursor.getColumnIndex("_id"))).intValue();
      if (i == 2)
        k--;
      Uri localUri2 = Uri.parse(localUri1 + "/" + k);
      this.cordova.getActivity().getContentResolver().delete(localUri2, null, null);
    }
  }

  private void cleanup(int paramInt, Uri paramUri1, Uri paramUri2, Bitmap paramBitmap)
  {
    if (paramBitmap != null)
      paramBitmap.recycle();
    new File(FileHelper.stripFileProtocol(paramUri1.toString())).delete();
    checkForDuplicateImage(paramInt);
    if ((this.saveToPhotoAlbum) && (paramUri2 != null))
      scanForGallery(paramUri2);
    System.gc();
  }

  private File createCaptureFile(int paramInt)
  {
    if (paramInt == 0)
      return new File(DirectoryManager.getTempDirectoryPath(this.cordova.getActivity()), ".Pic.jpg");
    if (paramInt == 1)
      return new File(DirectoryManager.getTempDirectoryPath(this.cordova.getActivity()), ".Pic.png");
    throw new IllegalArgumentException("Invalid Encoding Type: " + paramInt);
  }

  private Bitmap getRotatedBitmap(int paramInt, Bitmap paramBitmap, ExifHelper paramExifHelper)
  {
    Matrix localMatrix = new Matrix();
    if (paramInt == 180)
      localMatrix.setRotate(paramInt);
    while (true)
    {
      Bitmap localBitmap = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), localMatrix, true);
      paramExifHelper.resetOrientation();
      return localBitmap;
      localMatrix.setRotate(paramInt, paramBitmap.getWidth() / 2.0F, paramBitmap.getHeight() / 2.0F);
    }
  }

  private Bitmap getScaledBitmap(String paramString)
  {
    Bitmap localBitmap1;
    if ((this.targetWidth <= 0) && (this.targetHeight <= 0))
      localBitmap1 = BitmapFactory.decodeFile(paramString);
    int[] arrayOfInt;
    Bitmap localBitmap2;
    do
    {
      BitmapFactory.Options localOptions;
      int j;
      do
      {
        int i;
        do
        {
          return localBitmap1;
          localOptions = new BitmapFactory.Options();
          localOptions.inJustDecodeBounds = true;
          BitmapFactory.decodeFile(paramString, localOptions);
          i = localOptions.outWidth;
          localBitmap1 = null;
        }
        while (i == 0);
        j = localOptions.outHeight;
        localBitmap1 = null;
      }
      while (j == 0);
      arrayOfInt = calculateAspectRatio(localOptions.outWidth, localOptions.outHeight);
      localOptions.inJustDecodeBounds = false;
      localOptions.inSampleSize = calculateSampleSize(localOptions.outWidth, localOptions.outHeight, this.targetWidth, this.targetHeight);
      localBitmap2 = BitmapFactory.decodeFile(paramString, localOptions);
      localBitmap1 = null;
    }
    while (localBitmap2 == null);
    return Bitmap.createScaledBitmap(localBitmap2, arrayOfInt[0], arrayOfInt[1], true);
  }

  private Uri getUriFromMediaStore()
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("mime_type", "image/jpeg");
    try
    {
      Uri localUri2 = this.cordova.getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, localContentValues);
      return localUri2;
    }
    catch (UnsupportedOperationException localUnsupportedOperationException1)
    {
      LOG.d("CameraLauncher", "Can't write to external media storage.");
      try
      {
        Uri localUri1 = this.cordova.getActivity().getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, localContentValues);
        return localUri1;
      }
      catch (UnsupportedOperationException localUnsupportedOperationException2)
      {
        LOG.d("CameraLauncher", "Can't write to internal media storage.");
      }
    }
    return null;
  }

  private Cursor queryImgDB(Uri paramUri)
  {
    return this.cordova.getActivity().getContentResolver().query(paramUri, new String[] { "_id" }, null, null, null);
  }

  private void scanForGallery(Uri paramUri)
  {
    this.scanMe = paramUri;
    if (this.conn != null)
      this.conn.disconnect();
    this.conn = new MediaScannerConnection(this.cordova.getActivity().getApplicationContext(), this);
    this.conn.connect();
  }

  private Uri whichContentStore()
  {
    if (Environment.getExternalStorageState().equals("mounted"))
      return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    return MediaStore.Images.Media.INTERNAL_CONTENT_URI;
  }

  private void writeUncompressedImage(Uri paramUri)
    throws FileNotFoundException, IOException
  {
    FileInputStream localFileInputStream = new FileInputStream(FileHelper.stripFileProtocol(this.imageUri.toString()));
    OutputStream localOutputStream = this.cordova.getActivity().getContentResolver().openOutputStream(paramUri);
    byte[] arrayOfByte = new byte[4096];
    while (true)
    {
      int i = localFileInputStream.read(arrayOfByte);
      if (i == -1)
        break;
      localOutputStream.write(arrayOfByte, 0, i);
    }
    localOutputStream.flush();
    localOutputStream.close();
    localFileInputStream.close();
  }

  public int[] calculateAspectRatio(int paramInt1, int paramInt2)
  {
    int i = this.targetWidth;
    int j = this.targetHeight;
    if ((i <= 0) && (j <= 0))
    {
      i = paramInt1;
      j = paramInt2;
    }
    while (true)
    {
      return new int[] { i, j };
      if ((i > 0) && (j <= 0))
      {
        j = i * paramInt2 / paramInt1;
        continue;
      }
      if ((i <= 0) && (j > 0))
      {
        i = j * paramInt1 / paramInt2;
        continue;
      }
      double d1 = i / j;
      double d2 = paramInt1 / paramInt2;
      if (d2 > d1)
      {
        j = i * paramInt2 / paramInt1;
        continue;
      }
      if (d2 >= d1)
        continue;
      i = j * paramInt1 / paramInt2;
    }
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    this.callbackContext = paramCallbackContext;
    if (paramString.equals("takePicture"))
    {
      this.saveToPhotoAlbum = false;
      this.targetHeight = 0;
      this.targetWidth = 0;
      this.encodingType = 0;
      this.mediaType = 0;
      this.mQuality = 80;
      this.mQuality = paramJSONArray.getInt(0);
      int i = paramJSONArray.getInt(1);
      int j = paramJSONArray.getInt(2);
      this.targetWidth = paramJSONArray.getInt(3);
      this.targetHeight = paramJSONArray.getInt(4);
      this.encodingType = paramJSONArray.getInt(5);
      this.mediaType = paramJSONArray.getInt(6);
      this.correctOrientation = paramJSONArray.getBoolean(8);
      this.saveToPhotoAlbum = paramJSONArray.getBoolean(9);
      if (this.targetWidth < 1)
        this.targetWidth = -1;
      if (this.targetHeight < 1)
        this.targetHeight = -1;
      if (j == 1)
        takePicture(i, this.encodingType);
      while (true)
      {
        PluginResult localPluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        localPluginResult.setKeepCallback(true);
        paramCallbackContext.sendPluginResult(localPluginResult);
        return true;
        if ((j != 0) && (j != 2))
          continue;
        getImage(j, i);
      }
    }
    return false;
  }

  public void failPicture(String paramString)
  {
    this.callbackContext.error(paramString);
  }

  public void getImage(int paramInt1, int paramInt2)
  {
    Intent localIntent = new Intent();
    String str = "Get Picture";
    if (this.mediaType == 0)
      localIntent.setType("image/*");
    while (true)
    {
      localIntent.setAction("android.intent.action.GET_CONTENT");
      localIntent.addCategory("android.intent.category.OPENABLE");
      if (this.cordova != null)
        this.cordova.startActivityForResult(this, Intent.createChooser(localIntent, new String(str)), 1 + (paramInt2 + 16 * (paramInt1 + 1)));
      return;
      if (this.mediaType == 1)
      {
        localIntent.setType("video/*");
        str = "Get Video";
        continue;
      }
      if (this.mediaType != 2)
        continue;
      localIntent.setType("*/*");
      str = "Get All";
    }
  }

  // ERROR //
  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    // Byte code:
    //   0: iconst_m1
    //   1: iload_1
    //   2: bipush 16
    //   4: idiv
    //   5: iadd
    //   6: istore 4
    //   8: iconst_m1
    //   9: iload_1
    //   10: bipush 16
    //   12: irem
    //   13: iadd
    //   14: istore 5
    //   16: iload 4
    //   18: iconst_1
    //   19: if_icmpne +542 -> 561
    //   22: iload_2
    //   23: iconst_m1
    //   24: if_icmpne +517 -> 541
    //   27: new 219	org/apache/cordova/ExifHelper
    //   30: dup
    //   31: invokespecial 459	org/apache/cordova/ExifHelper:<init>	()V
    //   34: astore 26
    //   36: aload_0
    //   37: getfield 375	org/apache/cordova/CameraLauncher:encodingType	I
    //   40: istore 38
    //   42: iconst_0
    //   43: istore 29
    //   45: iload 38
    //   47: ifne +55 -> 102
    //   50: aload 26
    //   52: new 101	java/lang/StringBuilder
    //   55: dup
    //   56: invokespecial 102	java/lang/StringBuilder:<init>	()V
    //   59: aload_0
    //   60: getfield 128	org/apache/cordova/CameraLauncher:cordova	Lorg/apache/cordova/api/CordovaInterface;
    //   63: invokeinterface 134 1 0
    //   68: invokestatic 186	org/apache/cordova/DirectoryManager:getTempDirectoryPath	(Landroid/content/Context;)Ljava/lang/String;
    //   71: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   74: ldc_w 461
    //   77: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   80: invokevirtual 118	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   83: invokevirtual 464	org/apache/cordova/ExifHelper:createInFile	(Ljava/lang/String;)V
    //   86: aload 26
    //   88: invokevirtual 467	org/apache/cordova/ExifHelper:readExifData	()V
    //   91: aload 26
    //   93: invokevirtual 470	org/apache/cordova/ExifHelper:getOrientation	()I
    //   96: istore 39
    //   98: iload 39
    //   100: istore 29
    //   102: aconst_null
    //   103: astore 30
    //   105: aconst_null
    //   106: astore 31
    //   108: iload 5
    //   110: ifne +1053 -> 1163
    //   113: aload_0
    //   114: aload_0
    //   115: getfield 343	org/apache/cordova/CameraLauncher:imageUri	Landroid/net/Uri;
    //   118: invokevirtual 156	android/net/Uri:toString	()Ljava/lang/String;
    //   121: invokestatic 162	org/apache/cordova/FileHelper:stripFileProtocol	(Ljava/lang/String;)Ljava/lang/String;
    //   124: invokespecial 472	org/apache/cordova/CameraLauncher:getScaledBitmap	(Ljava/lang/String;)Landroid/graphics/Bitmap;
    //   127: astore 30
    //   129: aload 30
    //   131: ifnonnull +18 -> 149
    //   134: aload_3
    //   135: invokevirtual 476	android/content/Intent:getExtras	()Landroid/os/Bundle;
    //   138: ldc_w 478
    //   141: invokevirtual 484	android/os/Bundle:get	(Ljava/lang/String;)Ljava/lang/Object;
    //   144: checkcast 150	android/graphics/Bitmap
    //   147: astore 30
    //   149: aload 30
    //   151: ifnonnull +48 -> 199
    //   154: ldc 28
    //   156: ldc_w 486
    //   159: invokestatic 491	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   162: pop
    //   163: aload_0
    //   164: ldc_w 493
    //   167: invokevirtual 495	org/apache/cordova/CameraLauncher:failPicture	(Ljava/lang/String;)V
    //   170: return
    //   171: astore 27
    //   173: aload 27
    //   175: invokevirtual 498	java/io/IOException:printStackTrace	()V
    //   178: iconst_0
    //   179: istore 29
    //   181: goto -79 -> 102
    //   184: astore 28
    //   186: aload 28
    //   188: invokevirtual 498	java/io/IOException:printStackTrace	()V
    //   191: aload_0
    //   192: ldc_w 500
    //   195: invokevirtual 495	org/apache/cordova/CameraLauncher:failPicture	(Ljava/lang/String;)V
    //   198: return
    //   199: iload 29
    //   201: ifeq +22 -> 223
    //   204: aload_0
    //   205: getfield 391	org/apache/cordova/CameraLauncher:correctOrientation	Z
    //   208: ifeq +15 -> 223
    //   211: aload_0
    //   212: iload 29
    //   214: aload 30
    //   216: aload 26
    //   218: invokespecial 502	org/apache/cordova/CameraLauncher:getRotatedBitmap	(ILandroid/graphics/Bitmap;Lorg/apache/cordova/ExifHelper;)Landroid/graphics/Bitmap;
    //   221: astore 30
    //   223: aload_0
    //   224: aload 30
    //   226: invokevirtual 506	org/apache/cordova/CameraLauncher:processPicture	(Landroid/graphics/Bitmap;)V
    //   229: aload_0
    //   230: iconst_0
    //   231: invokespecial 169	org/apache/cordova/CameraLauncher:checkForDuplicateImage	(I)V
    //   234: aload_0
    //   235: iconst_1
    //   236: aload_0
    //   237: getfield 343	org/apache/cordova/CameraLauncher:imageUri	Landroid/net/Uri;
    //   240: aload 31
    //   242: aload 30
    //   244: invokespecial 508	org/apache/cordova/CameraLauncher:cleanup	(ILandroid/net/Uri;Landroid/net/Uri;Landroid/graphics/Bitmap;)V
    //   247: return
    //   248: aload_0
    //   249: getfield 74	org/apache/cordova/CameraLauncher:saveToPhotoAlbum	Z
    //   252: ifeq +103 -> 355
    //   255: new 155	java/io/File
    //   258: dup
    //   259: aload_0
    //   260: invokespecial 510	org/apache/cordova/CameraLauncher:getUriFromMediaStore	()Landroid/net/Uri;
    //   263: aload_0
    //   264: getfield 128	org/apache/cordova/CameraLauncher:cordova	Lorg/apache/cordova/api/CordovaInterface;
    //   267: invokestatic 514	org/apache/cordova/FileHelper:getRealPath	(Landroid/net/Uri;Lorg/apache/cordova/api/CordovaInterface;)Ljava/lang/String;
    //   270: invokespecial 165	java/io/File:<init>	(Ljava/lang/String;)V
    //   273: invokestatic 518	android/net/Uri:fromFile	(Ljava/io/File;)Landroid/net/Uri;
    //   276: astore 31
    //   278: aload 31
    //   280: ifnonnull +10 -> 290
    //   283: aload_0
    //   284: ldc_w 520
    //   287: invokevirtual 495	org/apache/cordova/CameraLauncher:failPicture	(Ljava/lang/String;)V
    //   290: aload_0
    //   291: getfield 231	org/apache/cordova/CameraLauncher:targetHeight	I
    //   294: iconst_m1
    //   295: if_icmpne +109 -> 404
    //   298: aload_0
    //   299: getfield 229	org/apache/cordova/CameraLauncher:targetWidth	I
    //   302: iconst_m1
    //   303: if_icmpne +101 -> 404
    //   306: aload_0
    //   307: getfield 379	org/apache/cordova/CameraLauncher:mQuality	I
    //   310: bipush 100
    //   312: if_icmpne +92 -> 404
    //   315: aload_0
    //   316: getfield 391	org/apache/cordova/CameraLauncher:correctOrientation	Z
    //   319: ifne +85 -> 404
    //   322: aload_0
    //   323: aload 31
    //   325: invokespecial 522	org/apache/cordova/CameraLauncher:writeUncompressedImage	(Landroid/net/Uri;)V
    //   328: aload_0
    //   329: getfield 371	org/apache/cordova/CameraLauncher:callbackContext	Lorg/apache/cordova/api/CallbackContext;
    //   332: aload 31
    //   334: invokevirtual 156	android/net/Uri:toString	()Ljava/lang/String;
    //   337: invokevirtual 525	org/apache/cordova/api/CallbackContext:success	(Ljava/lang/String;)V
    //   340: aload_0
    //   341: getfield 371	org/apache/cordova/CameraLauncher:callbackContext	Lorg/apache/cordova/api/CallbackContext;
    //   344: aload 31
    //   346: invokevirtual 156	android/net/Uri:toString	()Ljava/lang/String;
    //   349: invokevirtual 525	org/apache/cordova/api/CallbackContext:success	(Ljava/lang/String;)V
    //   352: goto -118 -> 234
    //   355: new 155	java/io/File
    //   358: dup
    //   359: aload_0
    //   360: getfield 128	org/apache/cordova/CameraLauncher:cordova	Lorg/apache/cordova/api/CordovaInterface;
    //   363: invokeinterface 134 1 0
    //   368: invokestatic 186	org/apache/cordova/DirectoryManager:getTempDirectoryPath	(Landroid/content/Context;)Ljava/lang/String;
    //   371: new 101	java/lang/StringBuilder
    //   374: dup
    //   375: invokespecial 102	java/lang/StringBuilder:<init>	()V
    //   378: invokestatic 529	java/lang/System:currentTimeMillis	()J
    //   381: invokevirtual 532	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   384: ldc_w 534
    //   387: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   390: invokevirtual 118	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   393: invokespecial 191	java/io/File:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   396: invokestatic 518	android/net/Uri:fromFile	(Ljava/io/File;)Landroid/net/Uri;
    //   399: astore 31
    //   401: goto -123 -> 278
    //   404: aload_0
    //   405: aload_0
    //   406: getfield 343	org/apache/cordova/CameraLauncher:imageUri	Landroid/net/Uri;
    //   409: invokevirtual 156	android/net/Uri:toString	()Ljava/lang/String;
    //   412: invokestatic 162	org/apache/cordova/FileHelper:stripFileProtocol	(Ljava/lang/String;)Ljava/lang/String;
    //   415: invokespecial 472	org/apache/cordova/CameraLauncher:getScaledBitmap	(Ljava/lang/String;)Landroid/graphics/Bitmap;
    //   418: astore 30
    //   420: iload 29
    //   422: ifeq +22 -> 444
    //   425: aload_0
    //   426: getfield 391	org/apache/cordova/CameraLauncher:correctOrientation	Z
    //   429: ifeq +15 -> 444
    //   432: aload_0
    //   433: iload 29
    //   435: aload 30
    //   437: aload 26
    //   439: invokespecial 502	org/apache/cordova/CameraLauncher:getRotatedBitmap	(ILandroid/graphics/Bitmap;Lorg/apache/cordova/ExifHelper;)Landroid/graphics/Bitmap;
    //   442: astore 30
    //   444: aload_0
    //   445: getfield 128	org/apache/cordova/CameraLauncher:cordova	Lorg/apache/cordova/api/CordovaInterface;
    //   448: invokeinterface 134 1 0
    //   453: invokevirtual 140	android/app/Activity:getContentResolver	()Landroid/content/ContentResolver;
    //   456: aload 31
    //   458: invokevirtual 348	android/content/ContentResolver:openOutputStream	(Landroid/net/Uri;)Ljava/io/OutputStream;
    //   461: astore 32
    //   463: aload 30
    //   465: getstatic 539	android/graphics/Bitmap$CompressFormat:JPEG	Landroid/graphics/Bitmap$CompressFormat;
    //   468: aload_0
    //   469: getfield 379	org/apache/cordova/CameraLauncher:mQuality	I
    //   472: aload 32
    //   474: invokevirtual 543	android/graphics/Bitmap:compress	(Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
    //   477: pop
    //   478: aload 32
    //   480: invokevirtual 364	java/io/OutputStream:close	()V
    //   483: aload_0
    //   484: getfield 375	org/apache/cordova/CameraLauncher:encodingType	I
    //   487: ifne -147 -> 340
    //   490: aload_0
    //   491: getfield 74	org/apache/cordova/CameraLauncher:saveToPhotoAlbum	Z
    //   494: ifeq +33 -> 527
    //   497: aload_0
    //   498: getfield 128	org/apache/cordova/CameraLauncher:cordova	Lorg/apache/cordova/api/CordovaInterface;
    //   501: astore 36
    //   503: aload 31
    //   505: aload 36
    //   507: invokestatic 514	org/apache/cordova/FileHelper:getRealPath	(Landroid/net/Uri;Lorg/apache/cordova/api/CordovaInterface;)Ljava/lang/String;
    //   510: astore 35
    //   512: aload 26
    //   514: aload 35
    //   516: invokevirtual 546	org/apache/cordova/ExifHelper:createOutFile	(Ljava/lang/String;)V
    //   519: aload 26
    //   521: invokevirtual 549	org/apache/cordova/ExifHelper:writeExifData	()V
    //   524: goto -184 -> 340
    //   527: aload 31
    //   529: invokevirtual 552	android/net/Uri:getPath	()Ljava/lang/String;
    //   532: astore 34
    //   534: aload 34
    //   536: astore 35
    //   538: goto -26 -> 512
    //   541: iload_2
    //   542: ifne +11 -> 553
    //   545: aload_0
    //   546: ldc_w 554
    //   549: invokevirtual 495	org/apache/cordova/CameraLauncher:failPicture	(Ljava/lang/String;)V
    //   552: return
    //   553: aload_0
    //   554: ldc_w 556
    //   557: invokevirtual 495	org/apache/cordova/CameraLauncher:failPicture	(Ljava/lang/String;)V
    //   560: return
    //   561: iload 4
    //   563: ifeq +9 -> 572
    //   566: iload 4
    //   568: iconst_2
    //   569: if_icmpne +593 -> 1162
    //   572: iload_2
    //   573: iconst_m1
    //   574: if_icmpne +569 -> 1143
    //   577: aload_3
    //   578: invokevirtual 559	android/content/Intent:getData	()Landroid/net/Uri;
    //   581: astore 6
    //   583: aload_0
    //   584: getfield 377	org/apache/cordova/CameraLauncher:mediaType	I
    //   587: ifeq +16 -> 603
    //   590: aload_0
    //   591: getfield 371	org/apache/cordova/CameraLauncher:callbackContext	Lorg/apache/cordova/api/CallbackContext;
    //   594: aload 6
    //   596: invokevirtual 156	android/net/Uri:toString	()Ljava/lang/String;
    //   599: invokevirtual 525	org/apache/cordova/api/CallbackContext:success	(Ljava/lang/String;)V
    //   602: return
    //   603: aload_0
    //   604: getfield 231	org/apache/cordova/CameraLauncher:targetHeight	I
    //   607: iconst_m1
    //   608: if_icmpne +43 -> 651
    //   611: aload_0
    //   612: getfield 229	org/apache/cordova/CameraLauncher:targetWidth	I
    //   615: iconst_m1
    //   616: if_icmpne +35 -> 651
    //   619: iload 5
    //   621: iconst_1
    //   622: if_icmpeq +9 -> 631
    //   625: iload 5
    //   627: iconst_2
    //   628: if_icmpne +23 -> 651
    //   631: aload_0
    //   632: getfield 391	org/apache/cordova/CameraLauncher:correctOrientation	Z
    //   635: ifne +16 -> 651
    //   638: aload_0
    //   639: getfield 371	org/apache/cordova/CameraLauncher:callbackContext	Lorg/apache/cordova/api/CallbackContext;
    //   642: aload 6
    //   644: invokevirtual 156	android/net/Uri:toString	()Ljava/lang/String;
    //   647: invokevirtual 525	org/apache/cordova/api/CallbackContext:success	(Ljava/lang/String;)V
    //   650: return
    //   651: aload 6
    //   653: aload_0
    //   654: getfield 128	org/apache/cordova/CameraLauncher:cordova	Lorg/apache/cordova/api/CordovaInterface;
    //   657: invokestatic 514	org/apache/cordova/FileHelper:getRealPath	(Landroid/net/Uri;Lorg/apache/cordova/api/CordovaInterface;)Ljava/lang/String;
    //   660: astore 7
    //   662: aload 7
    //   664: aload_0
    //   665: getfield 128	org/apache/cordova/CameraLauncher:cordova	Lorg/apache/cordova/api/CordovaInterface;
    //   668: invokestatic 563	org/apache/cordova/FileHelper:getMimeType	(Ljava/lang/String;Lorg/apache/cordova/api/CordovaInterface;)Ljava/lang/String;
    //   671: astore 8
    //   673: aload 7
    //   675: ifnull +30 -> 705
    //   678: aload 8
    //   680: ifnull +25 -> 705
    //   683: aload 8
    //   685: ldc_w 274
    //   688: invokevirtual 567	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   691: ifne +31 -> 722
    //   694: aload 8
    //   696: ldc_w 569
    //   699: invokevirtual 567	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   702: ifne +20 -> 722
    //   705: ldc 28
    //   707: ldc_w 486
    //   710: invokestatic 491	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   713: pop
    //   714: aload_0
    //   715: ldc_w 571
    //   718: invokevirtual 495	org/apache/cordova/CameraLauncher:failPicture	(Ljava/lang/String;)V
    //   721: return
    //   722: aload_0
    //   723: aload 7
    //   725: invokespecial 472	org/apache/cordova/CameraLauncher:getScaledBitmap	(Ljava/lang/String;)Landroid/graphics/Bitmap;
    //   728: astore 10
    //   730: aload 10
    //   732: ifnonnull +20 -> 752
    //   735: ldc 28
    //   737: ldc_w 486
    //   740: invokestatic 491	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   743: pop
    //   744: aload_0
    //   745: ldc_w 493
    //   748: invokevirtual 495	org/apache/cordova/CameraLauncher:failPicture	(Ljava/lang/String;)V
    //   751: return
    //   752: aload_0
    //   753: getfield 391	org/apache/cordova/CameraLauncher:correctOrientation	Z
    //   756: ifeq +127 -> 883
    //   759: iconst_1
    //   760: anewarray 300	java/lang/String
    //   763: dup
    //   764: iconst_0
    //   765: ldc_w 573
    //   768: aastore
    //   769: astore 11
    //   771: aload_0
    //   772: getfield 128	org/apache/cordova/CameraLauncher:cordova	Lorg/apache/cordova/api/CordovaInterface;
    //   775: invokeinterface 134 1 0
    //   780: invokevirtual 140	android/app/Activity:getContentResolver	()Landroid/content/ContentResolver;
    //   783: aload_3
    //   784: invokevirtual 559	android/content/Intent:getData	()Landroid/net/Uri;
    //   787: aload 11
    //   789: aconst_null
    //   790: aconst_null
    //   791: aconst_null
    //   792: invokevirtual 304	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   795: astore 12
    //   797: iconst_0
    //   798: istore 13
    //   800: aload 12
    //   802: ifnull +29 -> 831
    //   805: aload 12
    //   807: iconst_0
    //   808: invokeinterface 576 2 0
    //   813: pop
    //   814: aload 12
    //   816: iconst_0
    //   817: invokeinterface 577 2 0
    //   822: istore 13
    //   824: aload 12
    //   826: invokeinterface 578 1 0
    //   831: iload 13
    //   833: ifeq +50 -> 883
    //   836: new 202	android/graphics/Matrix
    //   839: dup
    //   840: invokespecial 203	android/graphics/Matrix:<init>	()V
    //   843: astore 14
    //   845: aload 14
    //   847: iload 13
    //   849: i2f
    //   850: invokevirtual 207	android/graphics/Matrix:setRotate	(F)V
    //   853: aload 10
    //   855: invokevirtual 210	android/graphics/Bitmap:getWidth	()I
    //   858: istore 15
    //   860: aload 10
    //   862: invokevirtual 213	android/graphics/Bitmap:getHeight	()I
    //   865: istore 16
    //   867: aload 10
    //   869: iconst_0
    //   870: iconst_0
    //   871: iload 15
    //   873: iload 16
    //   875: aload 14
    //   877: iconst_1
    //   878: invokestatic 217	android/graphics/Bitmap:createBitmap	(Landroid/graphics/Bitmap;IIIILandroid/graphics/Matrix;Z)Landroid/graphics/Bitmap;
    //   881: astore 10
    //   883: iload 5
    //   885: ifne +23 -> 908
    //   888: aload_0
    //   889: aload 10
    //   891: invokevirtual 506	org/apache/cordova/CameraLauncher:processPicture	(Landroid/graphics/Bitmap;)V
    //   894: aload 10
    //   896: ifnull +8 -> 904
    //   899: aload 10
    //   901: invokevirtual 153	android/graphics/Bitmap:recycle	()V
    //   904: invokestatic 178	java/lang/System:gc	()V
    //   907: return
    //   908: iload 5
    //   910: iconst_1
    //   911: if_icmpeq +9 -> 920
    //   914: iload 5
    //   916: iconst_2
    //   917: if_icmpne -23 -> 894
    //   920: aload_0
    //   921: getfield 231	org/apache/cordova/CameraLauncher:targetHeight	I
    //   924: ifle +204 -> 1128
    //   927: aload_0
    //   928: getfield 229	org/apache/cordova/CameraLauncher:targetWidth	I
    //   931: ifle +197 -> 1128
    //   934: new 101	java/lang/StringBuilder
    //   937: dup
    //   938: invokespecial 102	java/lang/StringBuilder:<init>	()V
    //   941: aload_0
    //   942: getfield 128	org/apache/cordova/CameraLauncher:cordova	Lorg/apache/cordova/api/CordovaInterface;
    //   945: invokeinterface 134 1 0
    //   950: invokestatic 186	org/apache/cordova/DirectoryManager:getTempDirectoryPath	(Landroid/content/Context;)Ljava/lang/String;
    //   953: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   956: ldc_w 580
    //   959: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   962: invokevirtual 118	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   965: astore 18
    //   967: new 219	org/apache/cordova/ExifHelper
    //   970: dup
    //   971: invokespecial 459	org/apache/cordova/ExifHelper:<init>	()V
    //   974: astore 19
    //   976: aload_0
    //   977: getfield 375	org/apache/cordova/CameraLauncher:encodingType	I
    //   980: ifne +28 -> 1008
    //   983: aload 19
    //   985: aload 6
    //   987: aload_0
    //   988: getfield 128	org/apache/cordova/CameraLauncher:cordova	Lorg/apache/cordova/api/CordovaInterface;
    //   991: invokestatic 514	org/apache/cordova/FileHelper:getRealPath	(Landroid/net/Uri;Lorg/apache/cordova/api/CordovaInterface;)Ljava/lang/String;
    //   994: invokevirtual 464	org/apache/cordova/ExifHelper:createInFile	(Ljava/lang/String;)V
    //   997: aload 19
    //   999: invokevirtual 467	org/apache/cordova/ExifHelper:readExifData	()V
    //   1002: aload 19
    //   1004: invokevirtual 470	org/apache/cordova/ExifHelper:getOrientation	()I
    //   1007: pop
    //   1008: new 582	java/io/FileOutputStream
    //   1011: dup
    //   1012: aload 18
    //   1014: invokespecial 583	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
    //   1017: astore 21
    //   1019: aload 10
    //   1021: getstatic 539	android/graphics/Bitmap$CompressFormat:JPEG	Landroid/graphics/Bitmap$CompressFormat;
    //   1024: aload_0
    //   1025: getfield 379	org/apache/cordova/CameraLauncher:mQuality	I
    //   1028: aload 21
    //   1030: invokevirtual 543	android/graphics/Bitmap:compress	(Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
    //   1033: pop
    //   1034: aload 21
    //   1036: invokevirtual 364	java/io/OutputStream:close	()V
    //   1039: aload_0
    //   1040: getfield 375	org/apache/cordova/CameraLauncher:encodingType	I
    //   1043: ifne +15 -> 1058
    //   1046: aload 19
    //   1048: aload 18
    //   1050: invokevirtual 546	org/apache/cordova/ExifHelper:createOutFile	(Ljava/lang/String;)V
    //   1053: aload 19
    //   1055: invokevirtual 549	org/apache/cordova/ExifHelper:writeExifData	()V
    //   1058: aload_0
    //   1059: getfield 371	org/apache/cordova/CameraLauncher:callbackContext	Lorg/apache/cordova/api/CallbackContext;
    //   1062: new 101	java/lang/StringBuilder
    //   1065: dup
    //   1066: invokespecial 102	java/lang/StringBuilder:<init>	()V
    //   1069: ldc_w 585
    //   1072: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1075: aload 18
    //   1077: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1080: ldc_w 587
    //   1083: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1086: invokestatic 529	java/lang/System:currentTimeMillis	()J
    //   1089: invokevirtual 532	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   1092: invokevirtual 118	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1095: invokevirtual 525	org/apache/cordova/api/CallbackContext:success	(Ljava/lang/String;)V
    //   1098: goto -204 -> 894
    //   1101: astore 17
    //   1103: aload 17
    //   1105: invokevirtual 588	java/lang/Exception:printStackTrace	()V
    //   1108: aload_0
    //   1109: ldc_w 590
    //   1112: invokevirtual 495	org/apache/cordova/CameraLauncher:failPicture	(Ljava/lang/String;)V
    //   1115: goto -221 -> 894
    //   1118: astore 20
    //   1120: aload 20
    //   1122: invokevirtual 498	java/io/IOException:printStackTrace	()V
    //   1125: goto -117 -> 1008
    //   1128: aload_0
    //   1129: getfield 371	org/apache/cordova/CameraLauncher:callbackContext	Lorg/apache/cordova/api/CallbackContext;
    //   1132: aload 6
    //   1134: invokevirtual 156	android/net/Uri:toString	()Ljava/lang/String;
    //   1137: invokevirtual 525	org/apache/cordova/api/CallbackContext:success	(Ljava/lang/String;)V
    //   1140: goto -246 -> 894
    //   1143: iload_2
    //   1144: ifne +11 -> 1155
    //   1147: aload_0
    //   1148: ldc_w 592
    //   1151: invokevirtual 495	org/apache/cordova/CameraLauncher:failPicture	(Ljava/lang/String;)V
    //   1154: return
    //   1155: aload_0
    //   1156: ldc_w 594
    //   1159: invokevirtual 495	org/apache/cordova/CameraLauncher:failPicture	(Ljava/lang/String;)V
    //   1162: return
    //   1163: iload 5
    //   1165: iconst_1
    //   1166: if_icmpeq -918 -> 248
    //   1169: aconst_null
    //   1170: astore 30
    //   1172: aconst_null
    //   1173: astore 31
    //   1175: iload 5
    //   1177: iconst_2
    //   1178: if_icmpne -944 -> 234
    //   1181: goto -933 -> 248
    //
    // Exception table:
    //   from	to	target	type
    //   36	42	171	java/io/IOException
    //   50	98	171	java/io/IOException
    //   27	36	184	java/io/IOException
    //   113	129	184	java/io/IOException
    //   134	149	184	java/io/IOException
    //   154	170	184	java/io/IOException
    //   173	178	184	java/io/IOException
    //   204	223	184	java/io/IOException
    //   223	234	184	java/io/IOException
    //   234	247	184	java/io/IOException
    //   248	278	184	java/io/IOException
    //   283	290	184	java/io/IOException
    //   290	340	184	java/io/IOException
    //   340	352	184	java/io/IOException
    //   355	401	184	java/io/IOException
    //   404	420	184	java/io/IOException
    //   425	444	184	java/io/IOException
    //   444	512	184	java/io/IOException
    //   512	524	184	java/io/IOException
    //   527	534	184	java/io/IOException
    //   934	976	1101	java/lang/Exception
    //   976	1008	1101	java/lang/Exception
    //   1008	1058	1101	java/lang/Exception
    //   1058	1098	1101	java/lang/Exception
    //   1120	1125	1101	java/lang/Exception
    //   976	1008	1118	java/io/IOException
  }

  public void onMediaScannerConnected()
  {
    try
    {
      this.conn.scanFile(this.scanMe.toString(), "image/*");
      return;
    }
    catch (IllegalStateException localIllegalStateException)
    {
      LOG.e("CameraLauncher", "Can't scan file in MediaScanner after taking picture");
    }
  }

  public void onScanCompleted(String paramString, Uri paramUri)
  {
    this.conn.disconnect();
  }

  public void processPicture(Bitmap paramBitmap)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    try
    {
      if (paramBitmap.compress(Bitmap.CompressFormat.JPEG, this.mQuality, localByteArrayOutputStream))
      {
        String str = new String(Base64.encodeBase64(localByteArrayOutputStream.toByteArray()));
        this.callbackContext.success(str);
      }
      return;
    }
    catch (Exception localException)
    {
      while (true)
        failPicture("Error compressing image.");
    }
  }

  public void takePicture(int paramInt1, int paramInt2)
  {
    this.numPics = queryImgDB(whichContentStore()).getCount();
    Intent localIntent = new Intent("android.media.action.IMAGE_CAPTURE");
    File localFile = createCaptureFile(paramInt2);
    localIntent.putExtra("output", Uri.fromFile(localFile));
    this.imageUri = Uri.fromFile(localFile);
    if (this.cordova != null)
      this.cordova.startActivityForResult(this, localIntent, 1 + (paramInt1 + 32));
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.CameraLauncher
 * JD-Core Version:    0.6.0
 */