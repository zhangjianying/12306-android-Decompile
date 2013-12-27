package org.apache.cordova;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;
import org.apache.commons.codec.binary.Base64;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.apache.cordova.file.EncodingException;
import org.apache.cordova.file.FileExistsException;
import org.apache.cordova.file.InvalidModificationException;
import org.apache.cordova.file.NoModificationAllowedException;
import org.apache.cordova.file.TypeMismatchException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FileUtils extends CordovaPlugin
{
  public static int ABORT_ERR = 0;
  public static int APPLICATION = 0;
  public static int ENCODING_ERR = 0;
  public static int INVALID_MODIFICATION_ERR = 0;
  public static int INVALID_STATE_ERR = 0;
  private static final String LOG_TAG = "FileUtils";
  public static int NOT_FOUND_ERR = 1;
  public static int NOT_READABLE_ERR;
  public static int NO_MODIFICATION_ALLOWED_ERR;
  public static int PATH_EXISTS_ERR;
  public static int PERSISTENT;
  public static int QUOTA_EXCEEDED_ERR;
  public static int RESOURCE;
  public static int SECURITY_ERR = 2;
  public static int SYNTAX_ERR;
  public static int TEMPORARY;
  public static int TYPE_MISMATCH_ERR;

  static
  {
    ABORT_ERR = 3;
    NOT_READABLE_ERR = 4;
    ENCODING_ERR = 5;
    NO_MODIFICATION_ALLOWED_ERR = 6;
    INVALID_STATE_ERR = 7;
    SYNTAX_ERR = 8;
    INVALID_MODIFICATION_ERR = 9;
    QUOTA_EXCEEDED_ERR = 10;
    TYPE_MISMATCH_ERR = 11;
    PATH_EXISTS_ERR = 12;
    TEMPORARY = 0;
    PERSISTENT = 1;
    RESOURCE = 2;
    APPLICATION = 3;
  }

  private boolean atRootDirectory(String paramString)
  {
    String str = FileHelper.getRealPath(paramString, this.cordova);
    return (str.equals(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + this.cordova.getActivity().getPackageName() + "/cache")) || (str.equals(Environment.getExternalStorageDirectory().getAbsolutePath())) || (str.equals("/data/data/" + this.cordova.getActivity().getPackageName()));
  }

  private void copyAction(File paramFile1, File paramFile2)
    throws FileNotFoundException, IOException
  {
    FileInputStream localFileInputStream = new FileInputStream(paramFile1);
    FileOutputStream localFileOutputStream = new FileOutputStream(paramFile2);
    FileChannel localFileChannel1 = localFileInputStream.getChannel();
    FileChannel localFileChannel2 = localFileOutputStream.getChannel();
    try
    {
      localFileChannel1.transferTo(0L, localFileChannel1.size(), localFileChannel2);
      return;
    }
    finally
    {
      localFileInputStream.close();
      localFileOutputStream.close();
      localFileChannel1.close();
      localFileChannel2.close();
    }
    throw localObject;
  }

  private JSONObject copyDirectory(File paramFile1, File paramFile2)
    throws JSONException, IOException, NoModificationAllowedException, InvalidModificationException
  {
    if ((paramFile2.exists()) && (paramFile2.isFile()))
      throw new InvalidModificationException("Can't rename a file to a directory");
    if (isCopyOnItself(paramFile1.getAbsolutePath(), paramFile2.getAbsolutePath()))
      throw new InvalidModificationException("Can't copy itself into itself");
    if ((!paramFile2.exists()) && (!paramFile2.mkdir()))
      throw new NoModificationAllowedException("Couldn't create the destination directory");
    File[] arrayOfFile = paramFile1.listFiles();
    int i = arrayOfFile.length;
    int j = 0;
    if (j < i)
    {
      File localFile = arrayOfFile[j];
      if (localFile.isDirectory())
        copyDirectory(localFile, paramFile2);
      while (true)
      {
        j++;
        break;
        copyFile(localFile, new File(paramFile2.getAbsoluteFile() + File.separator + localFile.getName()));
      }
    }
    return getEntry(paramFile2);
  }

  private JSONObject copyFile(File paramFile1, File paramFile2)
    throws IOException, InvalidModificationException, JSONException
  {
    if ((paramFile2.exists()) && (paramFile2.isDirectory()))
      throw new InvalidModificationException("Can't rename a file to a directory");
    copyAction(paramFile1, paramFile2);
    return getEntry(paramFile2);
  }

  private File createDestination(String paramString, File paramFile1, File paramFile2)
  {
    if (("null".equals(paramString)) || ("".equals(paramString)))
      paramString = null;
    if (paramString != null)
      return new File(paramFile2.getAbsolutePath() + File.separator + paramString);
    return new File(paramFile2.getAbsolutePath() + File.separator + paramFile1.getName());
  }

  private File createFileObject(String paramString)
  {
    return new File(FileHelper.getRealPath(paramString, this.cordova));
  }

  private File createFileObject(String paramString1, String paramString2)
  {
    if (paramString2.startsWith("/"))
      return new File(paramString2);
    String str = FileHelper.getRealPath(paramString1, this.cordova);
    return new File(str + File.separator + paramString2);
  }

  public static JSONObject getEntry(File paramFile)
    throws JSONException
  {
    JSONObject localJSONObject = new JSONObject();
    localJSONObject.put("isFile", paramFile.isFile());
    localJSONObject.put("isDirectory", paramFile.isDirectory());
    localJSONObject.put("name", paramFile.getName());
    localJSONObject.put("fullPath", "file://" + paramFile.getAbsolutePath());
    return localJSONObject;
  }

  private JSONObject getEntry(String paramString)
    throws JSONException
  {
    return getEntry(new File(paramString));
  }

  private JSONObject getFile(String paramString1, String paramString2, JSONObject paramJSONObject, boolean paramBoolean)
    throws FileExistsException, IOException, TypeMismatchException, EncodingException, JSONException
  {
    boolean bool1 = false;
    boolean bool2 = false;
    if (paramJSONObject != null)
    {
      bool1 = paramJSONObject.optBoolean("create");
      bool2 = false;
      if (bool1)
        bool2 = paramJSONObject.optBoolean("exclusive");
    }
    if (paramString2.contains(":"))
      throw new EncodingException("This file has a : in it's name");
    File localFile = createFileObject(paramString1, paramString2);
    if (bool1)
    {
      if ((bool2) && (localFile.exists()))
        throw new FileExistsException("create/exclusive fails");
      if (paramBoolean)
        localFile.mkdir();
      while (!localFile.exists())
      {
        throw new FileExistsException("create fails");
        localFile.createNewFile();
      }
    }
    if (!localFile.exists())
      throw new FileNotFoundException("path does not exist");
    if (paramBoolean)
    {
      if (localFile.isFile())
        throw new TypeMismatchException("path doesn't exist or is file");
    }
    else if (localFile.isDirectory())
      throw new TypeMismatchException("path doesn't exist or is directory");
    return getEntry(localFile);
  }

  private JSONObject getFileMetadata(String paramString)
    throws FileNotFoundException, JSONException
  {
    File localFile = createFileObject(paramString);
    if (!localFile.exists())
      throw new FileNotFoundException("File: " + paramString + " does not exist.");
    JSONObject localJSONObject = new JSONObject();
    localJSONObject.put("size", localFile.length());
    localJSONObject.put("type", FileHelper.getMimeType(paramString, this.cordova));
    localJSONObject.put("name", localFile.getName());
    localJSONObject.put("fullPath", paramString);
    localJSONObject.put("lastModifiedDate", localFile.lastModified());
    return localJSONObject;
  }

  private long getMetadata(String paramString)
    throws FileNotFoundException
  {
    File localFile = createFileObject(paramString);
    if (!localFile.exists())
      throw new FileNotFoundException("Failed to find file in getMetadata");
    return localFile.lastModified();
  }

  private JSONObject getParent(String paramString)
    throws JSONException
  {
    String str = FileHelper.getRealPath(paramString, this.cordova);
    if (atRootDirectory(str))
      return getEntry(str);
    return getEntry(new File(str).getParent());
  }

  private boolean isCopyOnItself(String paramString1, String paramString2)
  {
    return (paramString2.startsWith(paramString1)) && (paramString2.indexOf(File.separator, -1 + paramString1.length()) != -1);
  }

  private JSONObject moveDirectory(File paramFile1, File paramFile2)
    throws IOException, JSONException, InvalidModificationException, NoModificationAllowedException, FileExistsException
  {
    if ((paramFile2.exists()) && (paramFile2.isFile()))
      throw new InvalidModificationException("Can't rename a file to a directory");
    if (isCopyOnItself(paramFile1.getAbsolutePath(), paramFile2.getAbsolutePath()))
      throw new InvalidModificationException("Can't move itself into itself");
    if ((paramFile2.exists()) && (paramFile2.list().length > 0))
      throw new InvalidModificationException("directory is not empty");
    if (!paramFile1.renameTo(paramFile2))
    {
      copyDirectory(paramFile1, paramFile2);
      if (paramFile2.exists())
        removeDirRecursively(paramFile1);
    }
    else
    {
      return getEntry(paramFile2);
    }
    throw new IOException("moved failed");
  }

  private JSONObject moveFile(File paramFile1, File paramFile2)
    throws IOException, JSONException, InvalidModificationException
  {
    if ((paramFile2.exists()) && (paramFile2.isDirectory()))
      throw new InvalidModificationException("Can't rename a file to a directory");
    if (!paramFile1.renameTo(paramFile2))
    {
      copyAction(paramFile1, paramFile2);
      if (paramFile2.exists())
        paramFile1.delete();
    }
    else
    {
      return getEntry(paramFile2);
    }
    throw new IOException("moved failed");
  }

  private void notifyDelete(String paramString)
  {
    String str = FileHelper.getRealPath(paramString, this.cordova);
    try
    {
      this.cordova.getActivity().getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "_data = ?", new String[] { str });
      return;
    }
    catch (UnsupportedOperationException localUnsupportedOperationException)
    {
    }
  }

  private byte[] readAsBinaryHelper(String paramString, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = paramInt2 - paramInt1;
    byte[] arrayOfByte = new byte[i];
    InputStream localInputStream = FileHelper.getInputStreamFromUriString(paramString, this.cordova);
    int j = 0;
    if (paramInt1 > 0)
      localInputStream.skip(paramInt1);
    while (i > 0)
    {
      j = localInputStream.read(arrayOfByte, j, i);
      if (j < 0)
        break;
      i -= j;
    }
    return arrayOfByte;
  }

  private JSONArray readEntries(String paramString)
    throws FileNotFoundException, JSONException
  {
    File localFile = createFileObject(paramString);
    if (!localFile.exists())
      throw new FileNotFoundException();
    JSONArray localJSONArray = new JSONArray();
    if (localFile.isDirectory())
    {
      File[] arrayOfFile = localFile.listFiles();
      for (int i = 0; i < arrayOfFile.length; i++)
      {
        if (!arrayOfFile[i].canRead())
          continue;
        localJSONArray.put(getEntry(arrayOfFile[i]));
      }
    }
    return localJSONArray;
  }

  private boolean remove(String paramString)
    throws NoModificationAllowedException, InvalidModificationException
  {
    File localFile = createFileObject(paramString);
    if (atRootDirectory(paramString))
      throw new NoModificationAllowedException("You can't delete the root directory");
    if ((localFile.isDirectory()) && (localFile.list().length > 0))
      throw new InvalidModificationException("You can't delete a directory that is not empty.");
    return localFile.delete();
  }

  private boolean removeDirRecursively(File paramFile)
    throws FileExistsException
  {
    if (paramFile.isDirectory())
    {
      File[] arrayOfFile = paramFile.listFiles();
      int i = arrayOfFile.length;
      for (int j = 0; j < i; j++)
        removeDirRecursively(arrayOfFile[j]);
    }
    if (!paramFile.delete())
      throw new FileExistsException("could not delete: " + paramFile.getName());
    return true;
  }

  private boolean removeRecursively(String paramString)
    throws FileExistsException
  {
    File localFile = createFileObject(paramString);
    if (atRootDirectory(paramString))
      return false;
    return removeDirRecursively(localFile);
  }

  private JSONObject requestFileSystem(int paramInt)
    throws IOException, JSONException
  {
    JSONObject localJSONObject = new JSONObject();
    if (paramInt == TEMPORARY)
    {
      localJSONObject.put("name", "temporary");
      if (Environment.getExternalStorageState().equals("mounted"))
      {
        new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + this.cordova.getActivity().getPackageName() + "/cache/").mkdirs();
        localJSONObject.put("root", getEntry(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + this.cordova.getActivity().getPackageName() + "/cache/"));
        return localJSONObject;
      }
      new File("/data/data/" + this.cordova.getActivity().getPackageName() + "/cache/").mkdirs();
      localJSONObject.put("root", getEntry("/data/data/" + this.cordova.getActivity().getPackageName() + "/cache/"));
      return localJSONObject;
    }
    if (paramInt == PERSISTENT)
    {
      localJSONObject.put("name", "persistent");
      if (Environment.getExternalStorageState().equals("mounted"))
      {
        localJSONObject.put("root", getEntry(Environment.getExternalStorageDirectory()));
        return localJSONObject;
      }
      localJSONObject.put("root", getEntry("/data/data/" + this.cordova.getActivity().getPackageName()));
      return localJSONObject;
    }
    throw new IOException("No filesystem of type requested");
  }

  private JSONObject resolveLocalFileSystemURI(String paramString)
    throws IOException, JSONException
  {
    String str = URLDecoder.decode(paramString, "UTF-8");
    File localFile;
    if (str.startsWith("content:"))
    {
      Cursor localCursor = this.cordova.getActivity().managedQuery(Uri.parse(str), new String[] { "_data" }, null, null, null);
      int j = localCursor.getColumnIndexOrThrow("_data");
      localCursor.moveToFirst();
      localFile = new File(localCursor.getString(j));
    }
    while (!localFile.exists())
    {
      throw new FileNotFoundException();
      new URL(str);
      if (str.startsWith("file://"))
      {
        int i = str.indexOf("?");
        if (i < 0)
        {
          localFile = new File(str.substring(7, str.length()));
          continue;
        }
        localFile = new File(str.substring(7, i));
        continue;
      }
      localFile = new File(str);
    }
    if (!localFile.canRead())
      throw new IOException();
    return getEntry(localFile);
  }

  private JSONObject transferTo(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
    throws JSONException, NoModificationAllowedException, IOException, InvalidModificationException, EncodingException, FileExistsException
  {
    String str1 = FileHelper.getRealPath(paramString1, this.cordova);
    String str2 = FileHelper.getRealPath(paramString2, this.cordova);
    if ((paramString3 != null) && (paramString3.contains(":")))
      throw new EncodingException("Bad file name");
    File localFile1 = new File(str1);
    if (!localFile1.exists())
      throw new FileNotFoundException("The source does not exist");
    File localFile2 = new File(str2);
    if (!localFile2.exists())
      throw new FileNotFoundException("The source does not exist");
    File localFile3 = createDestination(paramString3, localFile1, localFile2);
    if (localFile1.getAbsolutePath().equals(localFile3.getAbsolutePath()))
      throw new InvalidModificationException("Can't copy a file onto itself");
    JSONObject localJSONObject;
    if (localFile1.isDirectory())
      if (paramBoolean)
        localJSONObject = moveDirectory(localFile1, localFile3);
    while (true)
    {
      return localJSONObject;
      return copyDirectory(localFile1, localFile3);
      if (!paramBoolean)
        break;
      localJSONObject = moveFile(localFile1, localFile3);
      if (!paramString1.startsWith("content://"))
        continue;
      notifyDelete(paramString1);
      return localJSONObject;
    }
    return copyFile(localFile1, localFile3);
  }

  private long truncateFile(String paramString, long paramLong)
    throws FileNotFoundException, IOException, NoModificationAllowedException
  {
    if (paramString.startsWith("content://"))
      throw new NoModificationAllowedException("Couldn't truncate file given its content URI");
    RandomAccessFile localRandomAccessFile = new RandomAccessFile(FileHelper.getRealPath(paramString, this.cordova), "rw");
    try
    {
      if (localRandomAccessFile.length() >= paramLong)
        localRandomAccessFile.getChannel().truncate(paramLong);
      while (true)
      {
        return paramLong;
        long l = localRandomAccessFile.length();
        paramLong = l;
      }
    }
    finally
    {
      localRandomAccessFile.close();
    }
    throw localObject;
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    try
    {
      if (paramString.equals("testSaveLocationExists"))
      {
        boolean bool3 = DirectoryManager.testSaveLocationExists();
        paramCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, bool3));
        break label975;
      }
      if (paramString.equals("getFreeDiskSpace"))
      {
        long l4 = DirectoryManager.getFreeDiskSpace(false);
        paramCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, (float)l4));
      }
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      paramCallbackContext.error(NOT_FOUND_ERR);
      break label975;
      if (paramString.equals("testFileExists"))
      {
        boolean bool2 = DirectoryManager.testFileExists(paramJSONArray.getString(0));
        paramCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, bool2));
      }
    }
    catch (FileExistsException localFileExistsException)
    {
      paramCallbackContext.error(PATH_EXISTS_ERR);
      break label975;
      if (paramString.equals("testDirectoryExists"))
      {
        boolean bool1 = DirectoryManager.testFileExists(paramJSONArray.getString(0));
        paramCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, bool1));
      }
    }
    catch (NoModificationAllowedException localNoModificationAllowedException)
    {
      paramCallbackContext.error(NO_MODIFICATION_ALLOWED_ERR);
      break label975;
      if (paramString.equals("readAsText"))
      {
        String str = paramJSONArray.getString(1);
        int i2 = paramJSONArray.getInt(2);
        int i3 = paramJSONArray.getInt(3);
        readFileAs(paramJSONArray.getString(0), i2, i3, paramCallbackContext, str, 1);
      }
    }
    catch (InvalidModificationException localInvalidModificationException)
    {
      paramCallbackContext.error(INVALID_MODIFICATION_ERR);
      break label975;
      if (paramString.equals("readAsDataURL"))
      {
        int n = paramJSONArray.getInt(1);
        int i1 = paramJSONArray.getInt(2);
        readFileAs(paramJSONArray.getString(0), n, i1, paramCallbackContext, null, -1);
      }
    }
    catch (MalformedURLException localMalformedURLException)
    {
      paramCallbackContext.error(ENCODING_ERR);
      break label975;
      if (paramString.equals("readAsArrayBuffer"))
      {
        int k = paramJSONArray.getInt(1);
        int m = paramJSONArray.getInt(2);
        readFileAs(paramJSONArray.getString(0), k, m, paramCallbackContext, null, 6);
      }
    }
    catch (IOException localIOException)
    {
      paramCallbackContext.error(INVALID_MODIFICATION_ERR);
      break label975;
      if (paramString.equals("readAsBinaryString"))
      {
        int i = paramJSONArray.getInt(1);
        int j = paramJSONArray.getInt(2);
        readFileAs(paramJSONArray.getString(0), i, j, paramCallbackContext, null, 7);
      }
    }
    catch (EncodingException localEncodingException)
    {
      paramCallbackContext.error(ENCODING_ERR);
      break label975;
      if (paramString.equals("write"))
      {
        long l3 = write(paramJSONArray.getString(0), paramJSONArray.getString(1), paramJSONArray.getInt(2));
        paramCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, (float)l3));
      }
    }
    catch (TypeMismatchException localTypeMismatchException)
    {
      paramCallbackContext.error(TYPE_MISMATCH_ERR);
    }
    if (paramString.equals("truncate"))
    {
      long l2 = truncateFile(paramJSONArray.getString(0), paramJSONArray.getLong(1));
      paramCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, (float)l2));
    }
    else if (paramString.equals("requestFileSystem"))
    {
      long l1 = paramJSONArray.optLong(1);
      if ((l1 != 0L) && (l1 > 1024L * DirectoryManager.getFreeDiskSpace(true)))
        paramCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, QUOTA_EXCEEDED_ERR));
      else
        paramCallbackContext.success(requestFileSystem(paramJSONArray.getInt(0)));
    }
    else if (paramString.equals("resolveLocalFileSystemURI"))
    {
      paramCallbackContext.success(resolveLocalFileSystemURI(paramJSONArray.getString(0)));
    }
    else if (paramString.equals("getMetadata"))
    {
      paramCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, (float)getMetadata(paramJSONArray.getString(0))));
    }
    else if (paramString.equals("getFileMetadata"))
    {
      paramCallbackContext.success(getFileMetadata(paramJSONArray.getString(0)));
    }
    else if (paramString.equals("getParent"))
    {
      paramCallbackContext.success(getParent(paramJSONArray.getString(0)));
    }
    else if (paramString.equals("getDirectory"))
    {
      paramCallbackContext.success(getFile(paramJSONArray.getString(0), paramJSONArray.getString(1), paramJSONArray.optJSONObject(2), true));
    }
    else if (paramString.equals("getFile"))
    {
      paramCallbackContext.success(getFile(paramJSONArray.getString(0), paramJSONArray.getString(1), paramJSONArray.optJSONObject(2), false));
    }
    else if (paramString.equals("remove"))
    {
      if (remove(paramJSONArray.getString(0)))
      {
        notifyDelete(paramJSONArray.getString(0));
        paramCallbackContext.success();
      }
      else
      {
        paramCallbackContext.error(NO_MODIFICATION_ALLOWED_ERR);
      }
    }
    else if (paramString.equals("removeRecursively"))
    {
      if (removeRecursively(paramJSONArray.getString(0)))
        paramCallbackContext.success();
      else
        paramCallbackContext.error(NO_MODIFICATION_ALLOWED_ERR);
    }
    else if (paramString.equals("moveTo"))
    {
      paramCallbackContext.success(transferTo(paramJSONArray.getString(0), paramJSONArray.getString(1), paramJSONArray.getString(2), true));
    }
    else if (paramString.equals("copyTo"))
    {
      paramCallbackContext.success(transferTo(paramJSONArray.getString(0), paramJSONArray.getString(1), paramJSONArray.getString(2), false));
    }
    else if (paramString.equals("readEntries"))
    {
      paramCallbackContext.success(readEntries(paramJSONArray.getString(0)));
    }
    else
    {
      return false;
    }
    label975: return true;
  }

  public void readFileAs(String paramString1, int paramInt1, int paramInt2, CallbackContext paramCallbackContext, String paramString2, int paramInt3)
  {
    this.cordova.getThreadPool().execute(new Runnable(paramString1, paramInt1, paramInt2, paramInt3, paramString2, paramCallbackContext)
    {
      public void run()
      {
        try
        {
          byte[] arrayOfByte1 = FileUtils.this.readAsBinaryHelper(this.val$filename, this.val$start, this.val$end);
          PluginResult localPluginResult;
          switch (this.val$resultType)
          {
          default:
            String str1 = FileHelper.getMimeType(this.val$filename, FileUtils.this.cordova);
            byte[] arrayOfByte2 = Base64.encodeBase64(arrayOfByte1);
            String str2 = "data:" + str1 + ";base64," + new String(arrayOfByte2, "US-ASCII");
            localPluginResult = new PluginResult(PluginResult.Status.OK, str2);
          case 1:
          case 6:
          case 7:
          }
          while (true)
          {
            this.val$callbackContext.sendPluginResult(localPluginResult);
            return;
            localPluginResult = new PluginResult(PluginResult.Status.OK, new String(arrayOfByte1, this.val$encoding));
            continue;
            localPluginResult = new PluginResult(PluginResult.Status.OK, arrayOfByte1);
            continue;
            localPluginResult = new PluginResult(PluginResult.Status.OK, arrayOfByte1, true);
          }
        }
        catch (FileNotFoundException localFileNotFoundException)
        {
          this.val$callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.IO_EXCEPTION, FileUtils.NOT_FOUND_ERR));
          return;
        }
        catch (IOException localIOException)
        {
          Log.d("FileUtils", localIOException.getLocalizedMessage());
          this.val$callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.IO_EXCEPTION, FileUtils.NOT_READABLE_ERR));
        }
      }
    });
  }

  public long write(String paramString1, String paramString2, int paramInt)
    throws FileNotFoundException, IOException, NoModificationAllowedException
  {
    if (paramString1.startsWith("content://"))
      throw new NoModificationAllowedException("Couldn't write to file given its content URI");
    String str = FileHelper.getRealPath(paramString1, this.cordova);
    boolean bool = false;
    if (paramInt > 0)
    {
      truncateFile(str, paramInt);
      bool = true;
    }
    byte[] arrayOfByte1 = paramString2.getBytes();
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte1);
    FileOutputStream localFileOutputStream = new FileOutputStream(str, bool);
    byte[] arrayOfByte2 = new byte[arrayOfByte1.length];
    localByteArrayInputStream.read(arrayOfByte2, 0, arrayOfByte2.length);
    localFileOutputStream.write(arrayOfByte2, 0, arrayOfByte1.length);
    localFileOutputStream.flush();
    localFileOutputStream.close();
    return arrayOfByte1.length;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.FileUtils
 * JD-Core Version:    0.6.0
 */