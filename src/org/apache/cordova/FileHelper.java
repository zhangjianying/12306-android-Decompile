package org.apache.cordova;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.LOG;

public class FileHelper
{
  private static final String LOG_TAG = "FileUtils";
  private static final String _DATA = "_data";

  public static InputStream getInputStreamFromUriString(String paramString, CordovaInterface paramCordovaInterface)
    throws IOException
  {
    if (paramString.startsWith("content"))
    {
      Uri localUri = Uri.parse(paramString);
      return paramCordovaInterface.getActivity().getContentResolver().openInputStream(localUri);
    }
    if (paramString.startsWith("file:///android_asset/"))
    {
      String str = paramString.substring(22);
      return paramCordovaInterface.getActivity().getAssets().open(str);
    }
    return new FileInputStream(getRealPath(paramString, paramCordovaInterface));
  }

  public static String getMimeType(String paramString, CordovaInterface paramCordovaInterface)
  {
    if (paramString.startsWith("content://"))
    {
      Uri localUri = Uri.parse(paramString);
      return paramCordovaInterface.getActivity().getContentResolver().getType(localUri);
    }
    String str = MimeTypeMap.getFileExtensionFromUrl(paramString.replace(" ", "%20").toLowerCase());
    if (str.equals("3ga"))
      return "audio/3gpp";
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(str);
  }

  public static String getRealPath(Uri paramUri, CordovaInterface paramCordovaInterface)
  {
    return getRealPath(paramUri.toString(), paramCordovaInterface);
  }

  public static String getRealPath(String paramString, CordovaInterface paramCordovaInterface)
  {
    String str;
    if (paramString.startsWith("content://"))
    {
      String[] arrayOfString = { "_data" };
      Cursor localCursor = paramCordovaInterface.getActivity().managedQuery(Uri.parse(paramString), arrayOfString, null, null, null);
      int i = localCursor.getColumnIndexOrThrow("_data");
      localCursor.moveToFirst();
      str = localCursor.getString(i);
      if (str == null)
        LOG.e("FileUtils", "Could get real path for URI string %s", new Object[] { paramString });
    }
    while (true)
    {
      return str;
      if (!paramString.startsWith("file://"))
        break;
      str = paramString.substring(7);
      if (!str.startsWith("/android_asset/"))
        continue;
      LOG.e("FileUtils", "Cannot get real path for URI string %s because it is a file:///android_asset/ URI.", new Object[] { paramString });
      return null;
    }
    return paramString;
  }

  public static String stripFileProtocol(String paramString)
  {
    if (paramString.startsWith("file://"))
      paramString = paramString.substring(7);
    return paramString;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.FileHelper
 * JD-Core Version:    0.6.0
 */