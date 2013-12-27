package com.tl.uic.model;

import com.tl.uic.util.GCUtil;
import java.io.Serializable;

public class TLFCacheFile
  implements ModelBase, Serializable
{
  private static final long serialVersionUID = 6949011040915615537L;
  private Boolean _isImage = Boolean.valueOf(false);
  private Object data;
  private String directory;
  private String fileName;
  private String sessionId;

  public TLFCacheFile()
  {
  }

  public TLFCacheFile(Object paramObject, String paramString)
  {
    this.data = paramObject;
    this.sessionId = paramString;
  }

  public final Boolean clean()
  {
    this._isImage = Boolean.valueOf(false);
    GCUtil.clean(this.data);
    this.data = null;
    this.sessionId = null;
    this.directory = null;
    this.fileName = null;
    return Boolean.valueOf(true);
  }

  public final Object getData()
  {
    return this.data;
  }

  public final String getDirectory()
  {
    return this.directory;
  }

  public final String getFileName()
  {
    return this.fileName;
  }

  public final String getSessionId()
  {
    return this.sessionId;
  }

  public final Boolean isImage()
  {
    return this._isImage;
  }

  public final void isImage(Boolean paramBoolean)
  {
    this._isImage = paramBoolean;
  }

  public final void setData(Object paramObject)
  {
    this.data = paramObject;
  }

  public final void setDirectory(String paramString)
  {
    this.directory = paramString;
  }

  public final void setFileName(String paramString)
  {
    this.fileName = paramString;
  }

  public final void setSessionId(String paramString)
  {
    this.sessionId = paramString;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.TLFCacheFile
 * JD-Core Version:    0.6.0
 */