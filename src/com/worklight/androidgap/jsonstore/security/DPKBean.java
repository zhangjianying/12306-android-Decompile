package com.worklight.androidgap.jsonstore.security;

import com.worklight.androidgap.jsonstore.util.jackson.JacksonSerializedJSONObject;
import com.worklight.androidgap.jsonstore.util.jackson.JsonOrgModule;
import org.json.JSONException;
import org.json.JSONObject;

public class DPKBean
{
  private static final String KEY_DPK = "dpk";
  private static final String KEY_ITERATIONS = "iterations";
  private static final String KEY_IV = "iv";
  private static final String KEY_SALT = "jsonSalt";
  private static final String KEY_VERSION = "version";
  private static final String VERSION_NUM = "1.0";
  private JSONObject obj;

  protected DPKBean(String paramString)
    throws JSONException
  {
    try
    {
      this.obj = JsonOrgModule.deserializeJSONObject(paramString);
      return;
    }
    catch (Throwable localThrowable)
    {
    }
    throw new JSONException(localThrowable);
  }

  protected DPKBean(String paramString1, String paramString2, String paramString3, int paramInt)
    throws JSONException
  {
    this.obj = new JacksonSerializedJSONObject();
    this.obj.put("dpk", paramString1);
    this.obj.put("iterations", paramInt);
    this.obj.put("iv", paramString2);
    this.obj.put("jsonSalt", paramString3);
    this.obj.put("version", "1.0");
  }

  public String getEncryptedDPK()
    throws JSONException
  {
    return this.obj.getString("dpk");
  }

  public String getIV()
    throws JSONException
  {
    return this.obj.getString("iv");
  }

  public int getIterations()
    throws JSONException
  {
    return this.obj.getInt("iterations");
  }

  public String getSalt()
    throws JSONException
  {
    return this.obj.getString("jsonSalt");
  }

  public String getVersion()
    throws JSONException
  {
    return this.obj.getString("version");
  }

  public String toString()
  {
    return this.obj.toString();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.jsonstore.security.DPKBean
 * JD-Core Version:    0.6.0
 */