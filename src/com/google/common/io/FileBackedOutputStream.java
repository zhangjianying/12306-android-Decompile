package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Beta
public final class FileBackedOutputStream extends OutputStream
{
  private File file;
  private final int fileThreshold;
  private MemoryOutput memory;
  private OutputStream out;
  private final boolean resetOnFinalize;
  private final InputSupplier<InputStream> supplier;

  public FileBackedOutputStream(int paramInt)
  {
    this(paramInt, false);
  }

  public FileBackedOutputStream(int paramInt, boolean paramBoolean)
  {
    this.fileThreshold = paramInt;
    this.resetOnFinalize = paramBoolean;
    this.memory = new MemoryOutput(null);
    this.out = this.memory;
    if (paramBoolean)
    {
      this.supplier = new InputSupplier()
      {
        protected void finalize()
        {
          try
          {
            FileBackedOutputStream.this.reset();
            return;
          }
          catch (Throwable localThrowable)
          {
            localThrowable.printStackTrace(System.err);
          }
        }

        public InputStream getInput()
          throws IOException
        {
          return FileBackedOutputStream.this.openStream();
        }
      };
      return;
    }
    this.supplier = new InputSupplier()
    {
      public InputStream getInput()
        throws IOException
      {
        return FileBackedOutputStream.this.openStream();
      }
    };
  }

  private InputStream openStream()
    throws IOException
  {
    monitorenter;
    try
    {
      if (this.file != null);
      for (Object localObject2 = new FileInputStream(this.file); ; localObject2 = new ByteArrayInputStream(this.memory.getBuffer(), 0, this.memory.getCount()))
        return localObject2;
    }
    finally
    {
      monitorexit;
    }
    throw localObject1;
  }

  private void update(int paramInt)
    throws IOException
  {
    if ((this.file == null) && (paramInt + this.memory.getCount() > this.fileThreshold))
    {
      File localFile = File.createTempFile("FileBackedOutputStream", null);
      if (this.resetOnFinalize)
        localFile.deleteOnExit();
      FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
      localFileOutputStream.write(this.memory.getBuffer(), 0, this.memory.getCount());
      localFileOutputStream.flush();
      this.out = localFileOutputStream;
      this.file = localFile;
      this.memory = null;
    }
  }

  public void close()
    throws IOException
  {
    monitorenter;
    try
    {
      this.out.close();
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public void flush()
    throws IOException
  {
    monitorenter;
    try
    {
      this.out.flush();
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  @VisibleForTesting
  File getFile()
  {
    monitorenter;
    try
    {
      File localFile = this.file;
      monitorexit;
      return localFile;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public InputSupplier<InputStream> getSupplier()
  {
    return this.supplier;
  }

  public void reset()
    throws IOException
  {
    monitorenter;
    label193: 
    try
    {
      close();
      try
      {
        if (this.memory == null)
        {
          this.memory = new MemoryOutput(null);
          this.out = this.memory;
          if (this.file == null)
            break label193;
          File localFile2 = this.file;
          this.file = null;
          if (localFile2.delete())
            break label193;
          throw new IOException("Could not delete: " + localFile2);
        }
      }
      finally
      {
        monitorexit;
      }
      this.memory.reset();
    }
    finally
    {
      if (this.memory == null)
        this.memory = new MemoryOutput(null);
      while (true)
      {
        this.out = this.memory;
        if (this.file == null)
          break;
        File localFile1 = this.file;
        this.file = null;
        if (localFile1.delete())
          break;
        throw new IOException("Could not delete: " + localFile1);
        this.memory.reset();
      }
    }
  }

  public void write(int paramInt)
    throws IOException
  {
    monitorenter;
    try
    {
      update(1);
      this.out.write(paramInt);
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public void write(byte[] paramArrayOfByte)
    throws IOException
  {
    monitorenter;
    try
    {
      write(paramArrayOfByte, 0, paramArrayOfByte.length);
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    monitorenter;
    try
    {
      update(paramInt2);
      this.out.write(paramArrayOfByte, paramInt1, paramInt2);
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  private static class MemoryOutput extends ByteArrayOutputStream
  {
    byte[] getBuffer()
    {
      return this.buf;
    }

    int getCount()
    {
      return this.count;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.io.FileBackedOutputStream
 * JD-Core Version:    0.6.0
 */