package com.worklight.common;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;
import com.worklight.androidgap.WLDroidGap;
import java.net.MalformedURLException;
import java.net.URL;

public class WLPreferences extends PreferenceActivity
{
  public static final String APP_ID_PREF_KEY = "appIdPref";
  public static final String APP_VERSION_PREF_KEY = "appVersionPref";
  public static final String EDIT_WL_SERVER_URL_PREF_KEY = "editWLServerURLPref";
  public static final String LAST_SETTINGS_URL_STATE_PREF_KEY = "lastSettingsUrlStatePref";
  public static final String MODIFY_WL_SERVER_URL_PREF_KEY = "resetWLServerURLPref";
  private static final String NEW_APP_ID_PREF_KEY = "newAppIdPref";
  private static final String NEW_APP_VERSION_PREF_KEY = "newAppVersionPref";
  private static final String NEW_LINE = "\n";
  public static final String SHOULD_MODIFY_URL_PREF_KEY = "shouldModifyUrlPref";
  public static final String WL_SERVER_URL = "WLServerURL";
  private EditTextPreference appIdEditTextPreference = null;
  private EditTextPreference appVersionEditTextPreference = null;
  private CheckBoxPreference modifyServerURLCheckBoxPreference = null;
  private EditTextPreference serverURLEditTextPreference = null;
  private String serverURLafter = null;
  private String serverURLbefore = null;
  private WLConfig wlConfig = null;

  private void createModifyServerCheckbox()
  {
    this.modifyServerURLCheckBoxPreference = new CheckBoxPreference(this);
    this.modifyServerURLCheckBoxPreference.setTitle(WLUtils.getResourceString("serverAddress", this));
    this.modifyServerURLCheckBoxPreference.setSummary(WLUtils.getResourceString("changeServerAddress", this));
    this.modifyServerURLCheckBoxPreference.setKey("resetWLServerURLPref");
    this.modifyServerURLCheckBoxPreference.setPersistent(true);
  }

  private void createServerURLTextField()
  {
    String str1 = WLUtils.getResourceString("summaryWLServerUrl", this);
    this.serverURLEditTextPreference = new EditTextPreference(this);
    this.serverURLbefore = WLUtils.readWLPref(getApplicationContext(), "WLServerURL");
    if ((this.serverURLbefore == null) || (this.serverURLbefore.trim().equals("")))
      this.serverURLbefore = this.wlConfig.getRootURL();
    this.serverURLEditTextPreference.setTitle(WLUtils.getResourceString("titleWLServerUrl", this));
    this.serverURLEditTextPreference.setKey("editWLServerURLPref");
    this.serverURLEditTextPreference.setDefaultValue(this.serverURLbefore);
    String str2 = WLUtils.readWLPref(getApplicationContext(), "lastSettingsUrlStatePref");
    EditTextPreference localEditTextPreference1 = this.serverURLEditTextPreference;
    EditTextPreference localEditTextPreference2;
    if (str2 == null)
    {
      localEditTextPreference1.setSummary(str1);
      String str3 = WLUtils.readWLPref(getApplicationContext(), "shouldModifyUrlPref");
      localEditTextPreference2 = this.serverURLEditTextPreference;
      if ((str3 != null) && (Boolean.valueOf(str3).booleanValue()))
        break label201;
    }
    label201: for (boolean bool = false; ; bool = true)
    {
      localEditTextPreference2.setEnabled(bool);
      this.serverURLEditTextPreference.setPersistent(true);
      return;
      str1 = str2 + "\n" + str1;
      break;
    }
  }

  private EditTextPreference createTextField(String paramString1, String paramString2, String paramString3)
  {
    EditTextPreference localEditTextPreference = new EditTextPreference(this);
    localEditTextPreference.setTitle(WLUtils.getResourceString(paramString1, this));
    String str = WLUtils.readWLPref(getApplicationContext(), paramString2);
    if (str == null)
      str = paramString3;
    WLUtils.writeWLPref(getApplicationContext(), paramString2, str);
    localEditTextPreference.setSummary(str);
    localEditTextPreference.setDefaultValue(str);
    localEditTextPreference.setKey(paramString2);
    localEditTextPreference.setPersistent(true);
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
    int i = localDisplayMetrics.widthPixels;
    localEditTextPreference.getEditText().setWidth(i - 20);
    return localEditTextPreference;
  }

  private boolean isUrlChanged()
  {
    int i = 1;
    if (this.serverURLafter == null)
      i = 0;
    do
      while (true)
      {
        return i;
        if (this.serverURLbefore != null)
          break;
        if (this.serverURLafter == null)
          return false;
      }
    while (!this.serverURLbefore.equals(this.serverURLafter));
    return false;
  }

  private boolean isWebResourcesChanged()
  {
    int i;
    if (!this.wlConfig.getAppId().equals(WLUtils.readWLPref(getApplicationContext(), "newAppIdPref")))
    {
      i = 1;
      if (this.wlConfig.getApplicationVersion().equals(WLUtils.readWLPref(getApplicationContext(), "newAppVersionPref")))
        break label67;
    }
    label67: for (int j = 1; ; j = 0)
    {
      int k;
      if (i == 0)
      {
        k = 0;
        if (j == 0);
      }
      else
      {
        k = 1;
      }
      return k;
      i = 0;
      break;
    }
  }

  private void setServerURLFieldSummary()
  {
    String str = (String)this.serverURLEditTextPreference.getSummary();
    if (str.contains("\n"))
      str = str.substring(1 + str.lastIndexOf("\n"), str.length());
    if (this.serverURLafter == null)
    {
      this.serverURLEditTextPreference.setSummary(this.serverURLbefore + "\n" + str);
      return;
    }
    this.serverURLEditTextPreference.setSummary(this.serverURLafter + "\n" + str);
  }

  public void onBackPressed()
  {
    Intent localIntent = new Intent();
    localIntent.putExtra("isServerURLChanged", isUrlChanged());
    localIntent.putExtra("serverURL", this.serverURLafter);
    localIntent.putExtra("isWebResourcesChanged", isWebResourcesChanged());
    setResult(-1, localIntent);
    super.onBackPressed();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.wlConfig = new WLConfig(getApplication());
    PreferenceScreen localPreferenceScreen = getPreferenceManager().createPreferenceScreen(this);
    PreferenceCategory localPreferenceCategory1 = new PreferenceCategory(this);
    localPreferenceCategory1.setTitle(WLUtils.getResourceString("networkSettingsTitleWLServerUrl", this));
    localPreferenceScreen.addPreference(localPreferenceCategory1);
    this.serverURLbefore = WLUtils.readWLPref(getApplicationContext(), "WLServerURL");
    createModifyServerCheckbox();
    createServerURLTextField();
    localPreferenceCategory1.addPreference(this.modifyServerURLCheckBoxPreference);
    localPreferenceCategory1.addPreference(this.serverURLEditTextPreference);
    PreferenceCategory localPreferenceCategory2 = new PreferenceCategory(this);
    localPreferenceCategory2.setTitle(WLUtils.getResourceString("wlWebResourcesCategory", this));
    localPreferenceScreen.addPreference(localPreferenceCategory2);
    this.appIdEditTextPreference = createTextField("wlAppIdTitle", "newAppIdPref", WLDroidGap.getWLConfig().getAppId());
    this.appVersionEditTextPreference = createTextField("wlAppVersionTitle", "newAppVersionPref", "1.0");
    localPreferenceCategory2.addPreference(this.appIdEditTextPreference);
    localPreferenceCategory2.addPreference(this.appVersionEditTextPreference);
    setPreferenceScreen(localPreferenceScreen);
    this.modifyServerURLCheckBoxPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
    {
      public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
      {
        if (((Boolean)paramObject).booleanValue())
        {
          WLUtils.writeWLPref(WLPreferences.this.getApplicationContext(), "shouldModifyUrlPref", "true");
          WLPreferences.this.serverURLEditTextPreference.setEnabled(true);
          WLPreferences.access$102(WLPreferences.this, WLPreferences.this.serverURLEditTextPreference.getText());
          WLUtils.writeWLPref(WLPreferences.this.getApplicationContext(), "WLServerURL", WLPreferences.this.serverURLafter);
          return true;
        }
        WLUtils.writeWLPref(WLPreferences.this.getApplicationContext(), "shouldModifyUrlPref", "false");
        WLPreferences.access$102(WLPreferences.this, WLPreferences.this.wlConfig.getRootURL());
        WLUtils.writeWLPref(WLPreferences.this.getApplicationContext(), "WLServerURL", WLPreferences.this.serverURLafter);
        WLPreferences.this.serverURLEditTextPreference.setEnabled(false);
        return true;
      }
    });
    this.serverURLEditTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
    {
      public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
      {
        WLPreferences.access$102(WLPreferences.this, (String)paramObject);
        try
        {
          new URL(WLPreferences.this.serverURLafter);
          WLUtils.writeWLPref(WLPreferences.this.getApplicationContext(), "WLServerURL", WLPreferences.this.serverURLafter);
          WLUtils.writeWLPref(WLPreferences.this.getApplicationContext(), "lastSettingsUrlStatePref", WLPreferences.this.serverURLafter);
          WLPreferences.this.setServerURLFieldSummary();
          return true;
        }
        catch (MalformedURLException localMalformedURLException)
        {
          new AlertDialog.Builder(WLPreferences.this).setTitle(WLUtils.getResourceString("titleInvalidWLServerUrl", WLPreferences.this)).setMessage(WLPreferences.this.serverURLafter + " " + WLUtils.getResourceString("errorInvalidWLServerUrl", WLPreferences.this)).setNeutralButton(WLUtils.getResourceString("OKTitleWLServerUrl", WLPreferences.this), new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
            }
          }).show();
        }
        return false;
      }
    });
    this.appIdEditTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
    {
      public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
      {
        String str = (String)paramObject;
        WLUtils.writeWLPref(WLPreferences.this.getApplicationContext(), "newAppIdPref", str);
        WLPreferences.this.appIdEditTextPreference.setSummary(str);
        return true;
      }
    });
    this.appVersionEditTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
    {
      public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
      {
        String str = (String)paramObject;
        WLUtils.writeWLPref(WLPreferences.this.getApplicationContext(), "newAppVersionPref", str);
        WLPreferences.this.appVersionEditTextPreference.setSummary(str);
        return true;
      }
    });
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.common.WLPreferences
 * JD-Core Version:    0.6.0
 */