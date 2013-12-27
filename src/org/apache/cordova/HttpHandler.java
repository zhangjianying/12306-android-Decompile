package org.apache.cordova;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpHandler
{
  private HttpEntity getHttpEntity(String paramString)
  {
    try
    {
      HttpEntity localHttpEntity = new DefaultHttpClient().execute(new HttpGet(paramString)).getEntity();
      return localHttpEntity;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return null;
  }

  private void writeToDisk(HttpEntity paramHttpEntity, String paramString)
    throws IllegalStateException, IOException
  {
    String str = "/sdcard/" + paramString;
    InputStream localInputStream = paramHttpEntity.getContent();
    byte[] arrayOfByte = new byte[1024];
    FileOutputStream localFileOutputStream = new FileOutputStream(str);
    while (true)
    {
      int i = localInputStream.read(arrayOfByte);
      if (i <= 0)
      {
        localFileOutputStream.flush();
        localFileOutputStream.close();
        return;
      }
      localFileOutputStream.write(arrayOfByte, 0, i);
    }
  }

  // ERROR //
  protected java.lang.Boolean get(String paramString1, String paramString2)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial 81	org/apache/cordova/HttpHandler:getHttpEntity	(Ljava/lang/String;)Lorg/apache/http/HttpEntity;
    //   5: astore_3
    //   6: aload_0
    //   7: aload_3
    //   8: aload_2
    //   9: invokespecial 83	org/apache/cordova/HttpHandler:writeToDisk	(Lorg/apache/http/HttpEntity;Ljava/lang/String;)V
    //   12: aload_3
    //   13: invokeinterface 86 1 0
    //   18: iconst_1
    //   19: invokestatic 92	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   22: areturn
    //   23: astore 4
    //   25: aload 4
    //   27: invokevirtual 33	java/lang/Exception:printStackTrace	()V
    //   30: iconst_0
    //   31: invokestatic 92	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   34: areturn
    //   35: astore 5
    //   37: aload 5
    //   39: invokevirtual 33	java/lang/Exception:printStackTrace	()V
    //   42: iconst_0
    //   43: invokestatic 92	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   46: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   6	12	23	java/lang/Exception
    //   12	18	35	java/lang/Exception
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.HttpHandler
 * JD-Core Version:    0.6.0
 */