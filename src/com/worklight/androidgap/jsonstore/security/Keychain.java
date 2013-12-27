package com.worklight.androidgap.jsonstore.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import org.json.JSONException;

public class Keychain
{
  private static final String PREFS_NAME_DPK = "dpkPrefs";
  private static final String PREF_NAME_DPK = "dpk";
  private SharedPreferences prefs;

  protected Keychain(Context paramContext)
  {
    this.prefs = paramContext.getSharedPreferences("dpkPrefs", 0);
  }

  private String buildTag(String paramString)
  {
    if (paramString.equals("jsonstore"))
      return "dpk";
    return "dpk-" + paramString;
  }

  public void destroy()
  {
    SharedPreferences.Editor localEditor = this.prefs.edit();
    localEditor.clear();
    localEditor.commit();
  }

  public DPKBean getDPKBean(String paramString)
    throws JSONException
  {
    String str = this.prefs.getString(buildTag(paramString), null);
    if (str == null)
      return null;
    return new DPKBean(str);
  }

  public boolean isDPKAvailable(String paramString)
  {
    return this.prefs.getString(buildTag(paramString), null) != null;
  }

  public void setDPKBean(String paramString, DPKBean paramDPKBean)
  {
    SharedPreferences.Editor localEditor = this.prefs.edit();
    localEditor.putString(buildTag(paramString), paramDPKBean.toString());
    localEditor.commit();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.jsonstore.security.Keychain
 * JD-Core Version:    0.6.0
 */